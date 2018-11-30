define([
	"dojo/_base/declare",
	"ecm/model/_ModelObject"
], function(dojo_declare, ecm_model_ModelObject){

/**
 * @name ier.model.CategoryTask
 * @class Represents a task category.  It holds all the information to render a category of task appropriately
 * @augments ecm.model._ModelObject
 */
return dojo_declare("ier.model.CategoryTask", [ecm_model_ModelObject], {
	/** @lends ecm.model._ModelObject.prototype */

	/**
	 * The class of the task listing pane responsible for this category task
	 */
	taskListingPaneClass: "ier/widget/tasks/MultiStatusTasksListingPane",
	
	/**
	 * The status of this particular set of tasks
	 */
	taskStatus: null,
	
	/**
	 * Whether this set of tasks is recurring
	 */
	isRecurring: null,
	
	/**
	 * The type of this task
	 */
	taskType: null,
	
	/**
	 * The user id associated with this type of task
	 */
	taskUserId : null,
	
	nameFilter: "",
	
	parent: "IER",
	
	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {	
		
	}
});});