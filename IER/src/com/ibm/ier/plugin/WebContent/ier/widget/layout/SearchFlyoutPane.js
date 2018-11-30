define([
	"dojo/_base/declare",
	"dojo/dom-class",
	"ecm/widget/layout/SearchFlyoutPane",
	"ier/constants",
	"ier/widget/layout/_SearchPaneMixin"
], function(declare, dom_class, SearchFlyoutPane, constants, _SearchPaneMixin){

/**
 * @name ier.widget.layout.SearchFlyoutPane
 * @class Provides a fly-out pane that contains the search interface for a layout.
 * @augments ecm.widget.layout.SearchFlyoutPane
 */
return declare("ier.widget.layout.SearchFlyoutPane", [SearchFlyoutPane, _SearchPaneMixin], {

	buildRendering: function(){
		this.inherited(arguments);

		dom_class.add(this.domNode, "ierSearchFlyoutPane");
	},

	selectContentPane: function(uuid, params){
		if(uuid == "searchPane"){
			// redirect to IER Search pane
			this.selectContentPane(constants.Feature_IERSearch, params);
			return;
		}
		this.inherited(arguments);
	},

	focus: function(){
		this.inherited(arguments);

		this.selected = true;
	}

});
});
