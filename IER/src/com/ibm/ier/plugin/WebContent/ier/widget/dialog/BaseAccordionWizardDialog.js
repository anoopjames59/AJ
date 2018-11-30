define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-style",
	"dojo/dom-class",
	"dojo/aspect",
	"ier/constants",
	"ier/messages",
	"ier/widget/dialog/IERBaseDialog",
	"dojo/text!./templates/BaseAccordionWizardDialogContent.html",
	"dijit/layout/AccordionContainer" //in content
], function(dojo_declare, dojo_lang, dojo_style, dojo_class, dojo_aspect, ier_constants, ier_messages, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.BaseAccordianWizardDialog
 * @class The base dialog to present wizard based on an accordion container design.  Caller must add child panes in manually.  All the states of the buttons are automatically
 * managed by the dialog.  If a pane needs to be disabled awaiting further instruction from a previous pane, set the pane to be "disabled" and all the buttons and onclicks will
 * be shut off.
 * 
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.BaseAccordionWizardDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.BaseAccordianWizardDialog.prototype */

	contentString: contentString,
	
	/**
	 * The label for the finish button.  Defaults to just "Finish".
	 */
	finishButtonLabel: ier_messages.baseDialog_finishButton,
	
	postCreate: function(){
		this.inherited(arguments);

		var defaultButton = this.addButton(this.finishButtonLabel, "_finishButtonClicked", true, false);
		this.addButton(ier_messages.baseDialog_nextButton, "_nextButtonClicked", false, false);		
		this.addButton(ier_messages.baseDialog_previousButton, "_previousButtonClicked", true, false);
		this.setDefaultButton(defaultButton);
		
		//connect to each pane's onselected to change the button's state
		dojo_aspect.after(this.baseAccordionContainer, "selectChild", dojo_lang.hitch(this, function(page, animation){
			this.updateButtonsState(page);
			this.onPaneSelected(page);
		}), true);
	},
	
	/**
	 * Must be called by sub classes of the base accordion wizard dialog to do some initialization work after all the child panes have been added
	 */
	startup: function(){
		this.inherited(arguments);
	
		var children = this.getChildPanes();
		
		//go through all the panes and disable them accordingly
		if(children){
			for(var i in children){
				var pane = children[i];
				
				//next pane should be disabled
				if(!this.hasNext(pane)){
					var index = this.getPaneIndex(pane);
					var nextIndex = index + 1;
					if(nextIndex <= children.length - 1){
						nextPane = this.getChildPane(nextIndex);
						if(nextPane){
							this.disablePane(nextPane);				
						}
					}
				}
				
			}
		}
		
		this.updateButtonsState();
		
		this.baseAccordionContainer.startup();
	},
	
	/**
	 * Adds a child pane that will be rendered
	 */
	addChildPane: function(pane){
		this.inherited(arguments);
		
		this.baseAccordionContainer.addChild(pane);
	},
	
	/**
	 * Selects the provided pane
	 */
	selectPane: function(pane){
		if(pane){
			this.enablePane(pane);
			this.baseAccordionContainer.selectChild(pane);
		}
	},
	
	/**
	 * Selects the next pane in the list
	 */
	selectNextPane: function(){
		this.baseAccordionContainer.forward();
		var pane = this.getCurrentPane();
		if(pane){
			this.enablePane(pane);
		}
	},
	
	/**
	 * Selects the previous pane in the list
	 */
	selectPreviousPane: function() {
		this.baseAccordionContainer.back();
		var pane = this.getCurrentPane();
		if(pane){
			this.enablePane(pane);
		}
	},
	
	/**
	 * Returns the current pane
	 */
	getCurrentPane: function(){
		return this.baseAccordionContainer.selectedChildWidget;
	},
	
	/**
	 * Event invoked when the next button is clicked
	 * 
	 * @param pane
	 * 			The next pane
	 */
	onNextButtonClicked: function(pane){	
	},
	
	/**
	 * Event invoked when the previous button is clicked
	 * 
	 * @param pane
	 * 			The previous pane
	 */
	onPreviousButtonClicked: function(pane){
	},
	
	/**
	 * Event invoked when the finish button is clicked
	 */
	onFinishButtonClicked: function(){
		
	},
	
	/**
	 * Event invoked when the pane is selected
	 */
	onPaneSelected: function(pane){
		
	},
	
	/**
	 * Whether there the previous child exists and is not disabled
	 * @param pane - the pane used to check.  If it is not provided, the curent pane is automatically used
	 */
	hasPrevious:function(pane){
		var currentPane = pane ? pane : this.getCurrentPane();
		if(currentPane){
			var index = this.getPaneIndex(currentPane);
			
			//check if the current child is not already at the beginning
			if(index != 0 || index != -1){
				
				//check if the previous pane is disabled.  If it is, disable the previous button
				var previousPane = this.getPreviousPane(pane);
				if(previousPane && !previousPane.disabled)
					return true;
			}
		}
		
		return false;
	},
	
	/**
	 * Whether there a next child exists and is not disabled
	 * @param pane - the pane used to check.  If it is not provided, the curent pane is automatically used

	 */
	hasNext: function(pane) {
		var currentPane = pane ? pane : this.getCurrentPane();
		if(currentPane){
			var index = this.getPaneIndex(currentPane);
			
			//check to see if the current child is not already at the end
			if(index != -1 || index > this.getChildPanes().length) {
				
				//check to see if the next pane is not disabled
				var nextPane = this.getNextPane(pane);
				if(nextPane && !nextPane.disabled){
					return true;
				}
			}
		}
		
		return false;
	},
	
	/**
	 * Get the next pane from the current pane
	 */
	getNextPane: function(pane){
		var currentPane = pane ? pane : this.getCurrentPane();
		if(currentPane){
			var index = this.getPaneIndex(currentPane);
			var nextIndex = index + 1;
			if(nextIndex <= this.getChildPanes().length - 1){
				nextPane = this.getChildPane(nextIndex);
				return nextPane;
			}
		}
		return null;
	},
	
	/**
	 * Get the previous pane from the current pane
	 */
	getPreviousPane: function(pane){
		var currentPane = pane ? pane : this.getCurrentPane();
		if(currentPane){
			var index = this.getPaneIndex(currentPane);
			var previousIndex = index - 1;
			if(previousIndex >= 0){
				previousPane = this.getChildPane(previousIndex);
				return previousPane;
			}
		}
		return null;
	},
	
	/**
	 * Get the child pane's index
	 */
	getPaneIndex: function(child){
		return this.baseAccordionContainer.getIndexOfChild(child);
	},
	
	/**
	 * Get the pane at the provided index
	 */
	getChildPane: function(index){
		var children = this.getChildPanes();
		return children[index];
	},
	
	/**
	 * Gets all the child panes added
	 */
	getChildPanes: function(){
		return this.baseAccordionContainer.getChildren();
	},
	
	/**
	 * Internal function for when the next button is clicked.  It selects the next pane and updates the button state accordingly
	 */
	_nextButtonClicked: function() {		
		this.selectNextPane();
		
		this.onNextButtonClicked(this.getCurrentPane());
	},
	
	/**
	 * Internal function for when the previous button is clicked.  It selects the previous pane and updates the button state accordingly
	 */
	_previousButtonClicked: function() {
		this.selectPreviousPane();
		
		this.onPreviousButtonClicked(this.getCurrentPane());
	},
	
	/**
	 * Internal function for when the finish button is clicked.  Right now it just invokes the onFinishButtonClicked event for sub classes to deal with.
	 */
	_finishButtonClicked: function() {
		if(this.isDefaultButtonEnabled)
			this.onFinishButtonClicked();
	},
	
	/**
	 * Enables a pane in the accordion wizard
	 */
	enablePane: function(pane) {
		if(pane.disabled && pane._buttonWidget){
			dojo_class.remove(pane.domNode, "dijitDisabled");
			dojo_class.remove(pane._buttonWidget.domNode, "dijitDisabled");
			if(pane._buttonWidget._onTitleClick && pane._buttonWidget.previousOnTitleClick) {
			    pane._buttonWidget._onTitleClick = pane._buttonWidget.previousOnTitleClick;
			}
			
			if(pane._buttonWidget._onKeyPress && pane._buttonWidget.previousOnKeyPress) {
			    pane._buttonWidget._onKeyPress = pane._buttonWidget.previousOnKeyPress;
			}
			pane.disabled = false;
			pane.set("disabled", false);
		}
	},
	
	/**
	 * Disables a pane in the accodion wizard
	 */
	disablePane: function(pane){
		pane.disabled = true;
		//a hack to disable a pane in an accordion pane.  I can't figure out a different way to do this
		if(pane._buttonWidget){
			dojo_class.add(pane.domNode, "dijitDisabled");
			dojo_class.add(pane._buttonWidget.domNode, "dijitDisabled");
			if(pane._buttonWidget._onTitleClick) {
				if(!pane._buttonWidget.previousOnTitleClick)
					pane._buttonWidget.previousOnTitleClick =  pane._buttonWidget._onTitleClick;
			    pane._buttonWidget._onTitleClick = function() { /* no op */ }; 
			}
			
			if(pane._buttonWidget._onKeyPress) {
				if(!pane._buttonWidget.previousOnKeyPress)
					pane._buttonWidget.previousOnKeyPress =  pane._buttonWidget._onKeyPress;
			    pane._buttonWidget._onKeyPress = function() { /* no op */ };
			}
		}
	},
	
	/**
	 * Update the buttons state
	 */
	updateButtonsState: function(pane){
		//previous button
		this.setButtonEnabled(this.childButtons[2], this.hasPrevious(pane));
		
		//next button
		this.setButtonEnabled(this.childButtons[1], this.hasNext(pane));
		
		//finish button
		this.setButtonEnabled(this.childButtons[0], this.validateInput());
	}
});});
