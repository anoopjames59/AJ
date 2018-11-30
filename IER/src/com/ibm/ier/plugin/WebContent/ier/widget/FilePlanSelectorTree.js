define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/_base/connect",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"dijit/Tree",
    	"ecm/LoggerMixin",
    	"ecm/model/Desktop",
    	"ecm/model/Repository",
    	"ier/model/FilePlan",
    	"ier/model/FilePlansTreeModel",
    	"dojo/text!./templates/FilePlanSelectorTree.html"
], function(dojo_declare, dojo_lang, dojo_connect, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, dijit_Tree,
		ecm_LoggerMixin, ecm_model_desktop, ecm_model_Repository, ier_model_FilePlan, ier_model_FilePlansTreeModel, templateString){
	

/**
 * @name ier.widget.FileplanSelector
 * @class Provides a tree interface to navigate fileplan items. This widget will display the repositories and then fileplans
 * @augments dijit._Widget
 */
return dojo_declare("ier.widget.FilePlanSelectorTree", [ ecm_LoggerMixin, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin ], {
	/** @lends ier.widget.WorklistSelector.prototype */

	templateString: templateString,
	widgetsInTemplate: true,

	_style: "tree",
	getIconClass: null,
	getRowClass: null,

	postCreate: function() {
		this.logEntry("postCreate");
		
		this.inherited(arguments);
		this._createRendering();
		
		//expand the initial repository that was used to log in with.
		var repository = ecm_model_desktop.getAuthenticatingRepository();
		this.setRepository(repository);
		
		this.logExit("postCreate");
	},
	
	setRepository: function(repository){
		var itemNode = this._tree.getNodesByItem(repository);

		if(repository.isIERLoaded() && itemNode && itemNode.length > 0)
			this._tree._expandNode(itemNode[0]);
	},
	
	getTreeModel: function(){
		return this._treeModel;
	},

	/**
	 * Clears the selected node in the list.
	 */
	clearSelection: function() {
		this.logEntry("clearSelection");
		
		if (this._tree && this._tree.selectedItems) {
			var selectedNodes = this._tree.getNodesByItem(this._tree.selectedItems[0]);

			if (selectedNodes && selectedNodes.length > 0) {
				selectedNodes[0].setSelected(false);
			}
		}
		
		this.logExit("clearSelection");
	},
	
	refreshRoot: function(){
		this._createTree();
	},

	/**
	 * Sets the style of the selector. There are two supported styles: "tree" and "dropdown".
	 * 
	 * @param style
	 *            Selector style (tree or dropdown).
	 */
	setStyle: function(style) {
		this._style = style;
	},

	/**
	 * Creates the dijit rendering.
	 */
	_createRendering: function() {
		this._createTree();
	},

	/**
	 * Creates a tree to display the list of search templates.
	 */
	_createTree: function() {
		this.logEntry("_createTree");
		
		if (this._dropdown) {
			this._dropdown.destroy();
		}
		if (this._tree) {
			this._tree.destroy();
		}
		if (!this._treeModel) {
			this._treeModel = new ier_model_FilePlansTreeModel();
		}
		
		if(this._treeOnClickHandler){
			dojo_connect.disconnect(this._treeOnClickHandler);
		}
		
		if(this._treeOnOpenHandler){
			dojo_connect.disconnect(this._treeOnOpenHandler);
		}

		this.getIconClass = this.getIconClass ? this.getIconClass : function(/*dojo.data.Item*/item, /*Boolean*/opened) {
			if(item instanceof ecm_model_Repository)
				return "ecmRepositoryIcon";
			else if(item instanceof ier_model_FilePlan)
				return "filePlanIcon";
			else
				return "";
		};
		
		this.getRowClass = this.getRowClass ? this.getRowClass : function(item, opened){
			if(item instanceof ecm_model_Repository)
				return "ecmFolderNotSelectable";
		},

		this._tree = new dijit_Tree({
			model: this._treeModel,
			showRoot: false,
			persist: false,
			getIconClass: this.getIconClass,
			getRowClass: this.getRowClass,
			style: "padding:5px"
		});
		this._treeOnClickHandler = this.connect(this._tree, "onClick", "_onTreeClick");
		this._treeOnOpenHandler = this.connect(this._tree, "onOpen", "_onTreeNodeOpen");
		this.domNode.appendChild(this._tree.domNode);
		
		this.logExit("_createTree");
	},

	/**
	 * Handles a tree click event.
	 * 
	 * @param item
	 *            Selected item
	 * @param node
	 *            Tree node selected
	 * @param evt
	 *            Event handle.
	 */
	_onTreeClick: function(/* dojo.data */item, /*TreeNode*/node, /*Event*/evt) {
		this.logEntry("_onTreeClick");
		
		if(item instanceof ecm_model_Repository){
			this._tree._expandNode(node);
		}
		else if(item instanceof ier_model_FilePlan){
			this.onFilePlanSelected(item.repository, item);
		}
		else {}
			//do nothing
		
		this.logExit("_onTreeClick");
	},
	
	_onTreeNodeOpen: function(item, node){
		
	},
	
	/**event to override for selecting a fileplan**/
	onFilePlanSelected: function(repository, fileplan){
		
	}
});});
