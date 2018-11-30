define([
	"ecm/widget/dialog/MessageDialog"
], function(MessageDialog){
var dialogs = [];

function cleanup(){
	for(var i = dialogs.length - 1; i >= 0; i--){
		var dialog = dialogs[i];
		if(!dialog.open){
			dialogs.splice(i, 1);
			dialog.destroyRecursive();
		}
	}
}

function manage(dialog){
	cleanup();
	if(dialog){
		dialogs.push(dialog);
	}
}

function showMessage(text){
	var dialog = new MessageDialog({text: text});
	dialog.show();
	manage(dialog);
}

return {
	/**
	 * Destroys closed dialogs being managed.
	 */
	cleanup: cleanup,
	/**
	 * Makes a dialog being managed, so that it will be destroyed later.
	 * @param dialog
	 */
	manage: manage,
	/**
	 * Shows a message dialog being managed.
	 * @param text
	 */
	showMessage: showMessage
};
});
