define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/dom-class",
    	"dojo/dom-construct",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"ecm/widget/dialog/BaseDialog",
    	"ier/util/util",
    	"ier/messages"
], function(dojo_declare, dojo_array, dojo_class, dojo_construct, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin,
		ecm_dialog_BaseDialog, ier_util, ier_messages){

/**
 * @name ier.widget.dialog.IERBaseDialog
 * @class Provides a base dialog for IER dialogs
 * @augments ecm.widget.dialog.BaseDialog
 */
var baseDialog = dojo_declare("ier.widget.dialog.IERBaseDialog", [dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_dialog_BaseDialog], {
	/** @lends ier.widget.dialog.IERBaseDialog.prototype */
	
	widgetsInTemplate: true,
	_ierMessages: ier_messages,
	repository: null,
	
	postCreate: function() {
		this.inherited(arguments);
		dojo_class.add(this.domNode, "ierBaseDialog");
		this.setResizable(true);
		this.resize();
	},
	
	/**
	 * Returns repository
	 * @returns
	 */
	getRepository: function(){
		return this.repository;
	},
	
	/**
	 * Sets repository
	 * @param repository
	 */
	setRepository: function(repository){
		this.repository = repository;
	},
	
	getChildPanes: function(){
		return this._childPanes;
	},
	
	/**
	 * The function should be called by subclasses to add the child panes.
	 */
	addChildPane: function(childPane){
		if(!this._childPanes)
			this._childPanes = [];
		
		this._childPanes.push(childPane);
		
		//connect to the child's pane onInputChange event so it will validate the dialog accordingly.
		this.connect(childPane, "onInputChange", function(widget){
			this.validateInput();
		});
		
		this.connect(childPane, "onErrorOccurred", function(error, type){
			this.setMessage(error, type);
		});
	},
	
	/**
	 * Adds a button to the dialog's button bar. Returns the dojo.form.Button instance created for the button.
	 * 
	 * @param buttonLabel
	 *            the label to use for the button.
	 * @param clickFunction
	 *            the name of a function in this object to call when the button is pressed.
	 * @param disabled
	 *            if true, the button will be initially disabled.
	 * @param isDefault
	 *            if true, this button will be "clicked" when enter is pressed anywhere on the dialog
	 */
	addButton: function(buttonLabel, clickFunction, disabled, isDefault) {
		if(!this.childButtons)
			this.childButtons = [];
		
		var button = null;
		if(isDefault)
			button = this.inherited(arguments, [buttonLabel, clickFunction, disabled, isDefault, "default_" + this.id]);
		else
			button = this.inherited(arguments);
			
		this.childButtons.push(button);
		
		if(isDefault){
			this._defaultChildButton = button;
		}
		
		return button;
	},
	
	setDefaultButton: function(button){
		this._defaultChildButton = button;
	},
	
	getDefaultButton: function(){
		return this._defaultChildButton;
	},
	
	isDefaultButtonEnabled: function(){
		if(this._defaultChildButton){
			return this._defaultChildButton.get("disabled");
		}
		return false;
	},
	
	/**
	 * Enables the provided button
	 * @param button {dojo.form.Button}
	 */
	setButtonEnabled: function(button, enabled){
		if(button)
			button.set("disabled", !enabled);
	},
	
	/**
	 * Validates the form and disables the default button if necessary
	 * @returns {Boolean}
	 */
	validateInput: function() {
		for(var i in this._childPanes){
			var childPane = this._childPanes[i];
			if(childPane.isValidationRequired()){
				var validate = childPane.validate();
				if(!validate){
					this.setButtonEnabled(this._defaultChildButton, false);
					return false;
				}
			}
		}
		this.setButtonEnabled(this._defaultChildButton, true);
		return true;
	},
	
	/**
	 * Creates a href link node with the provided link and text.  Append this into dialogs to create links
	 * @param link
	 * @param text
	 * @returns {String}
	 */
	createHrefLinkNode: function(link, text){
		return dojo_construct.create("a", { href: link, title: text, innerHTML: text, target: "_blank" });
	},
	
	/**
	 * Returns the base URL to use for help pages.
	 * Repeated here because ICN's one is private
	 */
	getHelpUrl: function(forPage) {
		return ier_util.getHelpUrl(forPage);
	},
	
	/**
	 * Push all widgets to this stack so they will be destroyed automatically.  If not you will need to manually destroy it.  
	 * Do not leave widgets undestroyed.  This will cause memory leaks.
	 */
	addChildWidget: function(widget){
		if(!this._childWidgets)
			this._childWidgets = [];
		
		
		this._childWidgets.push(widget);
	},
	
	destroy: function(){
		
		dojo_array.forEach(this._childPanes, function(widget){
			if(widget && widget.destroyRecursive)
				widget.destroyRecursive();
		});
		
		dojo_array.forEach(this._childWidgets, function(widget){
			if(widget && widget.destroyRecursive)
				widget.destroyRecursive();
		});
		
		this.inherited(arguments);
	},
	
	/**
	 * Add additional dom node css classes dynamically.
	 * @param cssClass
	 */
	addDomNodeCSSClass: function(cssClass){
		dojo_class.add(this.domNode, cssClass);
	}
});

return baseDialog;
});
