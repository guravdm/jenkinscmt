'use strict';

CMTApp.factory('HttpContentTypeService', function(){
	
	var HttpContentType = {};
	
	HttpContentType.APPLICATION_JSON = 'application/json';
	HttpContentType.applicationJson = function(){
		return HttpContentType.APPLICATION_JSON;
	};
	HttpContentType.MULTIPART_FORM_DATA = 'multipart/form-data';
	HttpContentType.multipartFormData = function(){
		return HttpContentType.MULTIPART_FORM_DATA;
	};
	
	return HttpContentType;
	
});
