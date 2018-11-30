define([ 
   "dojo/_base/lang",
   "dojo/_base/array",
   "dojo/dom-construct",
   "dojo/dom-style",
   "dijit/registry",
   "ier/messages",
   "ier/constants",
   "ier/util/util",
   "ier/model/_BaseEntityObject",
   "ier/widget/DDScheduleCompositeButton",
   "ier/widget/ObjectCompositeButton"
], function(dojo_lang, dojo_array, domConstruct, domStyle, dijit_registry, ier_messages, ier_constants, ier_util, ier_model_BaseEntityObject, ier_widget_DDScheduleCompositeButton, 
		ier_widget_ObjectCompositeButton) {
	
/**
 * The decorators are attached by matching the column's decorator field with the decorator's function below.  This field is constructed inside IERSearchResultsMediator.  
 */
	
	/**
	 * The decorator for the various states of an entity item
	 * @param item
	 * @returns
	 */
	dojo_lang.setObject("ierMultiStateIconsDecorator", function(item) {
		var display = '';
		var attributes = item.attributes;

		if(item.isDispositionInProgress == true || item.isDispositionInProgress == "true")
			display += '<img class="ierDispositionInProgressIcon" alt="' + ier_messages.contentList_dispositionInProgress + '" title="' + ier_messages.contentList_dispositionInProgress + '" src="dojo/resources/blank.gif" />';
		else if(item.isReadyForDisposition == true || item.isReadyForDisposition == "true")
			display += '<img class="ierDispositionReadyIcon" alt="' + ier_messages.contentList_dispositionReady + '" title="' + ier_messages.contentList_dispositionReady + '" src="dojo/resources/blank.gif" />';
		else if(item.isBasicScheduleInProgress == true || item.isBasicScheduleInProgress == "true")
			display += '<img class="ierBasicScheduleDispositionInProgressIcon" alt="' + ier_messages.contentList_basicScheduleDispositionInProgress + '" title="' + ier_messages.contentList_basicScheduleDispositionInProgress + '" src="dojo/resources/blank.gif" />';
		else
			display += '<img src="dojo/resources/blank.gif" height="16px" width="20px" alt="">';

		if(item.isClosed == true || item.isClosed == "true")
			display += '<img class="ierCloseIcon" alt="' + ier_messages.contentList_closed + '" title="' + ier_messages.contentList_closed + '" src="dojo/resources/blank.gif" />';
		else if(item.isReopen == true || item.isReopen == "true")
			display += '<img class="ierReopenIcon" alt="' + ier_messages.contentList_reopened + '" title="' + ier_messages.contentList_reopened + '" src="dojo/resources/blank.gif" />';
		else
			display += '<img src="dojo/resources/blank.gif" height="16px" width="20px" alt="">';

		if(attributes.OnHold == true || attributes.OnHold == "true")
			display += '<img class="ierHoldIcon" alt="' + ier_messages.contentList_onHold + '" title="' + ier_messages.contentList_onHold + '" src="dojo/resources/blank.gif" />';
		else
			display += '<img src="dojo/resources/blank.gif" height="16px" width="20px" alt="">';

		return display;
	});

	/**
	 * The decorator for the the mimetype icon
	 * @param data
	 * @returns
	 */
	dojo_lang.setObject("ierMimeTypeDecorator", function(item, rowId, rowIndex) {
		var iconCss = ier_util.getIconClass(item);
		var tooltip = ier_util.getMimetypeTooltip(item) || "";
		var mimeTypeString = "";
		
		if(iconCss != null){
			mimeTypeString = '<img class="' + iconCss + '" alt="' + tooltip + '" title="' + tooltip 
				+ '" src="dojo/resources/blank.gif"';
		}
		
		if(item.isFolder()){
			mimeTypeString += ' role="link" onclick="ecm.widget.listView.ContentList.callMethod(\'' + this.id + '\', \'_performDefaultActionForRowId\', \'' + rowId + '\')"';
		}
		if(mimeTypeString.length != 0){
			mimeTypeString += ' />';
		}
		
		return mimeTypeString;
	});
	
	dojo_lang.setObject("ierObjectTypeDecorator", function(data) {
		var entry = 
			'<div data-dojo-attach-point="containerNode" style="width: 100%"></div>';
		return entry;
	});
	
	/**
	 * The in-cell value for object types
	 * @param value
	 * @returns
	 */
	dojo_lang.setObject("ierObjectTypeCellValueFormatter", function(gridData, storeData, cellWidget) {
		var value = gridData;
		var row = cellWidget.cell.row;
		var item = row.item();
		var columnName = cellWidget.cell.column.columnName;
		var objectItem = item ? item.getIERObjectItem(columnName) : null;
		var objectCompositeButton = null;
		var isNew = false;
		
		if(cellWidget && cellWidget.containerNode && cellWidget.containerNode.children && cellWidget.containerNode.children[0]){
			var objectButton = dijit_registry.getEnclosingWidget(cellWidget.containerNode.children[0]);
			if(objectButton instanceof ier_widget_DDScheduleCompositeButton || objectButton instanceof ier_widget_ObjectCompositeButton)
				objectCompositeButton = objectButton
		}
		
		//special case for dd containers
		if(item && item instanceof ier.model.RecordCategory && item.isDefensibleDisposal()){
			if(objectCompositeButton != null && !(objectCompositeButton instanceof ier_widget_DDScheduleCompositeButton)){
				objectCompositeButton.destroy();
				objectCompositeButton = null;
			}
		
			if(objectCompositeButton == null) {
				isNew = true;
				objectCompositeButton = new ier_widget_DDScheduleCompositeButton({
					parentItem: item,
					listenToChanges: true
				});
			}
			objectCompositeButton.setSelectedItem(item.getDefensibleDisposalSchedule());
		}
		
		else if(value && objectItem){
			if(objectCompositeButton != null && !(objectCompositeButton instanceof ier_widget_ObjectCompositeButton)){
				objectCompositeButton.destroy();
				objectCompositeButton = null;
			}
		
			if(objectCompositeButton == null) {
				isNew = true;
				objectCompositeButton = new ier_widget_ObjectCompositeButton({
					parentItem: item,
					parentItemColumn: columnName,
					listenToChanges: true
				});
			}
			
			var resultSet = this.getResultSet();
			var repository = resultSet.repository;
			objectCompositeButton.setRepository(repository);
			objectCompositeButton.setSelectedItem(objectItem);
		}
		else {
			// Since cellWidget will be reused when sorting rows by clicking a column,
			// need to hide the object composite button already embedded.
			if(objectCompositeButton)
				domStyle.set(objectCompositeButton.domNode, "display", "none");
		}
		
		if(objectCompositeButton && cellWidget){
			
			if(isNew) {
				domStyle.set(objectCompositeButton.domNode, "width", "100%");
				domConstruct.place(objectCompositeButton.domNode, cellWidget.containerNode, "only");
			}
			else {
				domStyle.set(objectCompositeButton.domNode, "display", "");
			}
		}
	});
	
	/**
	 * Decorator for the tasks Status column
	 */
	dojo_lang.setObject("ierAsyncTaskStatusColumnDecorator", function(item, rowId, rowIndex) {
		var display = '';
		
		if(item){
			var attributes = item.attributes;
			var value = attributes[ier_constants.Attribute_Status];
			
			if(value == ier_constants.TaskStatus_Completed){
				display = '<img class="taskStatusCompletedIcon" style="padding-right:5px;" alt="' + ier_messages.taskPane_completedStatus
				+ '" title="' + ier_messages.taskPane_completedStatus + '" src="dojo/resources/blank.gif" />';
			}
			else if(value == ier_constants.TaskStatus_Scheduled){
				display = '<img class="taskStatusScheduledIcon" style="padding-right:5px;" alt="' + ier_messages.taskPane_scheduledStatus
				+ '" title="' + ier_messages.taskPane_scheduledStatus + '" src="dojo/resources/blank.gif" />';
			}
			else if(value == ier_constants.TaskStatus_Failed){
				display = '<img class="taskStatusFailedIcon" style="padding-right:5px;" alt="' + ier_messages.taskPane_failedStatus
				+ '" title="' + ier_messages.taskPane_failedStatus + '" src="dojo/resources/blank.gif" />';
			}
			else if(value == ier_constants.TaskStatus_Paused){
				display = '<img class="taskStatusPausedIcon" style="padding-right:5px;" alt="' + ier_messages.taskPane_disabledStatus
				+ '" title="' + ier_messages.taskPane_disabledStatus + '" src="dojo/resources/blank.gif" />';
			}
			else {
				display = '<img class="taskStatusInProgressIcon" style="padding-right:5px;" alt="' + ier_messages.taskPane_inProgressStatus 
				+ '" title="' + ier_messages.taskPane_inProgressStatus + '" src="dojo/resources/blank.gif" />';
			}
			
			display += ier_util.getStatusDisplayValue(value);
		}

		return display;
	});
	
	/**
	 * Decorator for the tasks type column
	 */
	dojo_lang.setObject("ierAsyncTaskTypeColumnDecorator", function(item, rowId, rowIndex) {
		var display = '';
		
		if(item){
			var attributes = item.attributes;
			var value = attributes[ier_constants.Attribute_Type];
			var isRecurring = item.isTaskRecurring ? item.isTaskRecurring() : false;
			var icon = isRecurring ? "taskRecurringIcon" : ier_util.getTypeDisplayIcon(value);
			value = ier_util.getTypeDisplayValue(value);
			display = '<img class="' + icon + '" style="padding-right: 5px;" alt="' + value
			+ '" title="' + value + '" src="dojo/resources/blank.gif" />';
			
			display += value;
		}

		return display;
	});
});