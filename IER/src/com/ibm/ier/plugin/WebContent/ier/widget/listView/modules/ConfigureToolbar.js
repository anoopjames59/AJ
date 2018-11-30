define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dijit/registry",
    	"dijit/focus",
    	"ecm/model/Desktop",
    	"ier/widget/listView/modules/Toolbar",
    	"ier/constants",
    	"ier/util/menu"
], function(dojo_declare, dojo_array, dojo_lang, dijit_registry, dijit_focus, ecm_model_Desktop, ier_widget_listView_modules_Toolbar, ier_constants, ier_util_menu){
	
return dojo_declare("ier.widget.listView.modules.ConfigureToolbar", [ ier_widget_listView_modules_Toolbar ], {
	
	isConfigure: true,
	
	/**
	 * Event invoked when toolbar buttons are created
	 */
	onToolbarButtonsCreated: function(toolbarButtons) {
		this.inherited(arguments);
	},

	/**
	 * Overriding the updateToolbarState to handle special cases for AddContainer actions.  They should be disabled if the user has no rights to add to the current container
	 * @param action
	 */
	updateToolbarState: function(action) {
		this.inherited(arguments);
		
		//By default since all the add containers buttons are global actions, they are enabled.  Disable them explicitly if the user has no rights to perform the action based
		//on the parent folder and not the selected items.  Without doing this, the actions will be disabled based on the user's current selected item.
		/*dojo_array.forEach(this.getToolbarButtons(), function(toolbarButton){
			var action = toolbarButton.action;
			
			if (action && action.id == ier_constants.Action_RunReport) {
				var selectedItems = this.getSelectedItems();
				toolbarButton.set("disabled", (!(selectedItems && selectedItems.length == 1)));
			}
		}, this);*/
	},

	/**
	 * Overriding the cleanUpToolButtons to stop timer and unwatch focused node
	 */
	cleanUpToolButtons: function() {
		if(this._timeoutHandle){
			clearTimeout(this._timeoutHandle);
			this._timeoutHandle = null;
		}
		if(this._watchHandle){
			this._watchHandle.unwatch();
			this._watchHandle = null;
		}

		this.inherited(arguments);
	},

	/**
	 * Overriding the onToolbarButtonsCreated to set focus on a button that previously had focus
	 * @param toolbarButtons
	 */
	onToolbarButtonsCreated: function(toolbarButtons){
		// save action id of a button that has focus last
		dojo_array.forEach(toolbarButtons, function(toolbarButton){
			var actionId = toolbarButton.action.id;
			this.connect(toolbarButton, "onFocus", function(){
				this._selectedToolBarButtonActionId = actionId;
			});
		}, this);

		if(this._selectedToolBarButtonActionId){
			var focusFunc = function(buttons, actionId){
				dojo_array.some(buttons, function(button){
					if(!button._destroyed && button.action.id == actionId){
						button.focus();
						return true;
					}
				});
			};

			var isVisibleFunc = function(node){
				while(node){
					if(node.style && node.style.display == "none"){
						return false;
					}
					node = node.parentNode;
				}
				return true;
			};

			var timeoutFunc = dojo_lang.hitch(this, function(){
				if(dijit_focus.curNode == null || !isVisibleFunc(dijit_focus.curNode)){
					// when focus was already lost, immediately focus on a button that previously had focus
					this._timeoutHandle = null;
					focusFunc(toolbarButtons, this._selectedToolBarButtonActionId);
				}else{
					// when focus is valid so far, later focus on a button that previously had focus
					this._timeoutHandle = setTimeout(function(){
						timeoutFunc();
					}, 500);
				}
			});
			timeoutFunc();

			this._watchHandle = dijit_focus.watch("curNode", dojo_lang.hitch(this, function(name, oldVal, newVal){
				this._watchHandle.unwatch();
				this._watchHandle = null;

				if(this._timeoutHandle){
					clearTimeout(this._timeoutHandle);
					this._timeoutHandle = null;

					if(!newVal){
						// when focus is lost, focus on a button that previously had focus
						focusFunc(toolbarButtons, this._selectedToolBarButtonActionId);
					}
				}
			}));
		}

		this.inherited(arguments);
	}
});});
