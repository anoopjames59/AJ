define([
    	"dojo/i18n!ier/nls/messages"
], function(dojo_i18n_ier_messages){
	/**
	 * @name ier.messages .
	 * @description A global instance of the messages for the IBM Enterprise Records client.
	 */
	var messages = dojo_i18n_ier_messages;

	//product names are not to be translated
	messages.product_name = "IBM Enterprise Records";
	messages.product_version = "5.1.2";
	messages.build_version = "rec560.177";
	messages.build_date = "11/04/2013";

	return messages;
});


