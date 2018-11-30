require(
		[ "dojo/_base/declare", "dojo/_base/lang", "dojo/aspect", "dojo/query", "dojo/dom","dijit/registry"],
		function(declare, lang, aspect, query, dom, registry) {
			/**
			 * Use this function to add any global JavaScript methods your plug-in requires.
			 */
			
			//alert("checking if called");

			aspect
					.after(
							ecm.widget.dialog.EditPropertiesDialog.prototype,
							"show",
							function() { /*
							 * * hide all unnecessary components in
							 * 'EditPropertiesDialog'
							 */// version box
								
								console.log("Starting aspect for properties");

								console.log(query(".dijitTabContent "));

								console.log(dom.byId("dijit_layout_TabContainer_0_tablist_ecm_widget_ItemSecurityPane_0"));
								
								dom.byId("dijit_layout_TabContainer_0_tablist_ecm_widget_ItemSecurityPane_0").innerHTML = "Something";
								var widget = registry.byId("dijit_layout_TabContainer_0_tablist_ecm_widget_ItemCommentsPane_0");
								console.log(widget);
								
								widget.domNode.style.display = "none";
								
								//console.log(query("dijit_layout_TabContainer_0_tablist_ecm_widget_ItemSecurityPane_0").parent(".dijitTabContent"));
								/*query("dijit_layout_TabContainer_0_tablist_ecm_widget_ItemSecurityPane_0").forEach(function (node) {
						               var parentCategory = query(node).closest('.dijitTab');
						       console.log(parentCategory);  
								});*/
								/*
								this._itemEditPane._versionSelectorPane.domNode.style.display = "none";
								//secrurity
								pane
								this._itemEditPane._itemSecurityPane.controlButton.domNode.style.display = "none";
								//folders
								//filed
								//in
								this._itemEditPane._itemFoldersFileInPane.controlButton.domNode.style.display = "none";
								//parent
								//document
								//pane
								this._itemEditPane._itemParentDocumentsPane.controlButton.domNode.style.display = "none";
								//versions
								pane
								this._itemEditPane._itemVersionsPane.controlButton.domNode.style.display = "none";
								*/

							});
			
			
					aspect
					.after(
							ier.widget.layout.ConfigurePane.prototype,
							"loadContent",
							function() { 
							 //* * hide all unnecessary components in
							 //* 'EditPropertiesDialog'
							 // version box
								
								console.log("Starting aspect for Configurationpane");
								console.debug(this.linkDropdown);
								//console.debug(dijit.byId('linkDropdown').options);
								for(var i=7; i>0; i--){
									var firstOption = this.linkDropdown.options[i];
									this.linkDropdown.removeOption(firstOption);
								}

								

							});
		});
