define([
	"dojo/_base/declare",
	"ier/constants",
	"ier/model/_BaseEntityObject"
], function(dojo_declare, ier_constants, ier_model_BaseEntityObject){

/**
 * @name ier.model.NamingPattern
 * @class Represents a single Naming Pattern.
 * @augments ier.model._BaseEntityObject
 */
var NamingPattern = dojo_declare("ier.model.NamingPattern", [ier_model_BaseEntityObject], {
	/** @lends ecm.model.NamingPattern.prototype */

	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {	
	},
	
	/**
	 * Returns the pattern name
	 */
	getPatternName: function(){
		return this.attributes[ier_constants.Property_PatternName];
	},
	
	/**
	 * Return true or false whether the current pattern level is applied to name.  If not, it means it will be applied to the ID.
	 */
	isAppliedToName: function(){
		return this.attributes[ier_constants.Property_ApplyToNameOrId] == "Name";
	}
});

ier_model_BaseEntityObject.registerClass("NamingPattern", NamingPattern);

return NamingPattern;
});