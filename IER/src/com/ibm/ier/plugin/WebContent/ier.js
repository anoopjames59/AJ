isCompatitibleIERVersion(function(isCompatible, message){
	if(isCompatible){
		if(ecm.model.desktop.getRequestParam("debug")) {
			loadScript("ier_uncompressed.js");
		}
		else {
			loadScript("ier_compressed.js.jgz", "ier_uncompressed.js");
		}
		ierApplicationPluginInit();
	}else {
		ecm.model.desktop.addMessage(message);
	}
});

function loadScript(url, backupUrl){
	try {
		var scriptUrl = ecm.model.Request.getPluginResourceUrl("IERApplicationPlugin", url);
		require([scriptUrl]);
	}
	catch(e){
		if(backupUrl){
			var scriptUrl = ecm.model.Request.getPluginResourceUrl("IERApplicationPlugin", backupUrl);
			require([scriptUrl]);
		}
	}
}

/**
 * The initialization function for the IER Application Plugin.
 */
function ierApplicationPluginInit() {
	require(["dojo/_base/declare", "dojo/aspect", "dojo/_base/lang", "ecm/model/Desktop", "ecm/model/Repository", "ier/widget/admin/config", "ier/model/IERDesktopMixin", 
	         "ier/widget/layout/CommonActionsHandler", "ier/model/IERRepositoryMixin"],
	         function(dojo_declare, dojo_aspect, dojo_lang, ecm_model_Desktop, ecm_model_Repository, ier_admin_config, ier_model_IERDesktopMixin, ier_widget_CommonActionsHandler,
	        		 ier_model_IERRepositoryMixin){
		dojo_aspect.after(ecm_model_Desktop, "onDesktopLoaded", function(response){
			if(ecm_model_Desktop.isLoggedIn()){
				ier_admin_config.loadConfiguration();
			}
			
			dojo_declare.safeMixin(ecm_model_Desktop, new ier_model_IERDesktopMixin(ecm_model_Desktop.id, ecm_model_Desktop.name));
			ecm_model_Desktop.saveAdditionalAttributes(response);
			
			ecm_model_Desktop.setActionsHandler(new ier_widget_CommonActionsHandler());
			
			//if the authenticating repository attributes are present, add them to the authenticating repository
			var authenticatingRepository = ecm_model_Desktop.getAuthenticatingRepository();
			if (response && response.authenticatingRepositoryAttributes && authenticatingRepository) {
				dojo_lang.mixin(authenticatingRepository.attributes, response.authenticatingRepositoryAttributes);
			}
			
			//loop through and retrieve IER-related information
			var repositories = ecm_model_Desktop.repositories;
			if (repositories) {
				for ( var i in repositories) {
					var repository = repositories[i];
					if(repository.type == "p8") {
						var recordType = repository.getRecordType() ? repository.getRecordType() : null;
						var ierRepository = new ier_model_IERRepositoryMixin(repository.id, repository.name, recordType, repository);
						dojo_declare.safeMixin(repository, ierRepository);
					}
				}
			}
			
			dojo_aspect.after(ecm_model_Desktop, "onLogin", function(repository){
				//load configuration
				ier_admin_config.loadConfiguration();
			}, true);
			
			dojo_aspect.after(ecm_model_Desktop, "onLogout", function(repository){
				ecm_model_Desktop.clearIERSessionContent();
			}, true);
			
		}, true);
	});
}

/**
 * Creates an instance of {@link ecm.model.Message} for an error, given the message prefix for the error.
 * 
 * @param messagePrefix
 *            The prefix of the set of strings related to the message as stored in the ecm.messages object.
 * @param inserts
 *            An array of strings to insert into the message.
 * @param isBackgroundRequest
 *            Boolean indicating if the message is being created by an background request.
 */
function createIERErrorMessage (messagePrefix, inserts, isBackgroundRequest, messages, dojo_string) {
	inserts = inserts || [];
	var messageText = messages[messagePrefix] ? dojo_string.substitute(messages[messagePrefix], inserts) : messagePrefix;
	var messageExplanation = messages[messagePrefix + "_explanation"] ? dojo_string.substitute(messages[messagePrefix + "_explanation"], inserts) : "";
	var messageUserResponse = messages[messagePrefix + "_userResponse"] ? dojo_string.substitute(messages[messagePrefix + "_userResponse"], inserts) : "";
	var messageAdminResponse = messages[messagePrefix + "_adminResponse"] ? dojo_string.substitute(messages[messagePrefix + "_adminResponse"], inserts) : "";
	var message = new ecm.model.Message({
			level: 4,
			text: messageText,
			explanation: messageExplanation,
			userResponse: messageUserResponse,
			adminResponse: messageAdminResponse
	});
	return message;
}

function isCompatitibleIERVersion (onComplete){
	//this version of declare plug-in requires the minimum version of 2.0.2.  It is incompatible with ICN 2.0.0 or ICN 2.0.1
	if(ecm.messages.product_version.indexOf("2.0.0") == 0 || ecm.messages.product_version.indexOf("2.0.1") == 0){
		require(["ier/messages", "dojo/string"], function(messages, dojo_string){
			onComplete(false, createIERErrorMessage("error_incompatitableVersion", null, false, messages, dojo_string));
		});
	} else
		onComplete(true);
}


