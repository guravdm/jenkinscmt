'use strict';

CMTApp.factory('RestFactory', ['$http', function($http){
	
	var RestFactory = {};
	
	RestFactory.performPost = function(apiURL, requestData, contentType){
		return $http({
	        method: "POST",
	        url: apiURL,
	        headers : { 'Content-Type': contentType },
	        data:requestData
		});
	};
	
	RestFactory.performGet = function(apiURL){
		return $http({
	        method: "GET",
	        url: apiURL,
		});
	};
	
	
	RestFactory.performPostFile = function(apiURL, requestData){
		return $http({
	        method: "POST",
	        url: apiURL,
	        data:requestData,
	        withCredentials: true,
	        headers: {'Content-Type': undefined },
	        transformRequest: angular.identity
		});
		
	/*	
		return	$http.post(apiURL, requestData, {
	        withCredentials: true,
	        headers: {'Content-Type': undefined },
	        transformRequest: angular.identity
	    });*/
	};
	return RestFactory;
	
}]);
