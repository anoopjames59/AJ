define([
	"dojo/_base/array",
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/_ModelObject",
	"ecm/model/Desktop",
	"ecm/model/Request"
], function(array, declare, lang, _ModelObject, Desktop, Request){
var Config = declare(_ModelObject, {

	constructor: function(id, name, values){
		this.values = values || {};
	},

	get: function(key){
		return this.values[key];
	},

	set: function(key, value){
		this.values[key] = value;
	}

});

Config.getConfig = function(name, id, callback){
	var repository = Desktop.getAuthenticatingRepository();
	var params = {repositoryId: repository ? repository.id : null, application: "ier", action: "get", configuration: name, id: id || "default"};
	Request.invokeService("admin/configuration", null, params, function(response){
		if(callback){
			var values = response.configuration;
			var id = values.id;
			delete values.id;
			var config = new Config(id, name, values);
			callback(config);
		}
	});
};

Config.saveConfig = function(config, callback){
	var repository = Desktop.getAuthenticatingRepository();
	var params = {repositoryId: repository ? repository.id : null, application: "ier", action: "save", configuration: config.name, id: config.id || "default"};
	var values = config.values || {};
	Request.postService("admin/configuration", null, params, "text/json", JSON.stringify(values), function(response){
		if(callback){
			callback(response);
		}
	});
};

Config.deleteConfig = function(config, callback){
	var name = null;
	var id = null;
	if(lang.isArray(config)){
		name = config[0].name;
		id = array.map(config, function(c){
			return c.id;
		}).join(",");
	}else{
		name = config.name;
		id = config.id;
	}
	var repository = Desktop.getAuthenticatingRepository();
	var params = {repositoryId: repository ? repository.id : null, application: "ier", action: "delete", configuration: name, id: id};
	Request.invokeService("admin/configuration", null, params, function(response){
		if(callback){
			callback(response);
		}
	});
};

Config.listConfig = function(name, callback){
	var repository = Desktop.getAuthenticatingRepository();
	var params = {repositoryId: repository ? repository.id : null, application: "ier", action: "list", configuration: name, id: "default"};
	Request.invokeService("admin/configuration", null, params, function(response){
		if(callback){
			var list = array.map(response.list || [], function(values){
				var id = values.id;
				delete values.id;
				return new Config(id, name, values);
			});
			callback(list);
		}
	});
};

return Config;
});
