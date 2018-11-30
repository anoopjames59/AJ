define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dojo/dom-style",
	"ecm/widget/FoldersFiledInPane",
	"dijit/form/RadioButton",
	"ier/util/util",
	"ier/messages",
	"dijit/_base/manager"
], function(dojo_declare, dojo_lang, dom_class, dom_style, ecm_FoldersFiledInPane, RadioButton, ier_util, ier_messages, dijit_manager){
	return dojo_declare("ier.widget.panes.EntityItemFoldersFiledInPane", [ecm_FoldersFiledInPane], {
		postCreate: function(){
			this.inherited(arguments);
			dom_class.add(this.domNode, "ierEntityItemFoldersFiledInPane");
			dom_style.set(this.domNode, "margin-top", "10px");
		},
		
		render: function() {
			this.logEntry("render");

			if (this.item && !this.rendered) {

				//get folders filed in
				var self = this;
				this.item.retrieveFoldersFiledIn(function(folders, columns) {
					self._createGrid(folders, columns);
					var structure = self.grid.structure;
					structure[0].formatter = dojo_lang.hitch(self, "formatRecordContainerIcon");
					if(folders && folders.length > 1) {
						var newCol = {
							field: "contentItem",
							name: ier_messages.recordPropDlg_securityParent,
							width: "80px",
							formatter: dojo_lang.hitch(self, "formatParentSecurityIcon")
						};
						structure.unshift(newCol);
					}
					self.grid.setStructure(structure);
				});

				this.rendered = true;
			}

			this.logExit("render");
		},
		
		getSecurityParentItemId: function() {
			return this._securityParentItemId;
		},
		
		formatParentSecurityIcon: function(data, index) {
			var sfId = this.item.attributes["SecurityFolder"];
			var parentItemId = data.id;
			var radio = new RadioButton({
				checked: parentItemId == sfId,
				name: "ierEntityItemFoldersFiledInPaneSecurityParent",
				value: parentItemId
			});
			this.connect(radio, "onClick", dojo_lang.hitch(this, function(event) {
				this._securityParentItemId = radio.get("value");
			}));
			this.connect(radio, "onChange", dojo_lang.hitch(this, function(event) {
				if(event) {
					this._securityParentItemId = radio.get("value");
					this.onFiledInChanged();
				}
			}));
			
			this.connect(this.grid, "onSelected", dojo_lang.hitch(this, function(index) {
				var selectedRow = this.grid.getRowNode(index);
				var ws = dijit_manager.findWidgets(selectedRow);
				dojo.forEach(ws, function(w){
					if(w.declaredClass == "dijit.form.RadioButton"){
						w.set("checked", "checked");
					}
				}, this);
			}));
			return radio;
		},
		
		formatRecordContainerIcon: function(data, index) {
			var item = this.grid.getItem(index);
			var parentItem = this.grid.store.getValue(item, "contentItem");
			var className = ier_util.getIconClass(parentItem);
			var altText = ier_util.getMimetypeTooltip(parentItem) || "";
			return '<img role="presentation" class="' + className
				+ ' alt="' + altText + '"' + ' title="' + altText + '" src="dojo/resources/blank.gif"></img>';
		},

		/**
		 * This event is invoked if filed-in is changed
		 */
		onFiledInChanged: function(){
		}
	});
});