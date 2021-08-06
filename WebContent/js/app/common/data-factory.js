'use strict';

CMTApp.factory('DataFactory', function($rootScope){
	
	var DataFactory = {};
	DataFactory.showLoader = false;
	DataFactory.setShowLoader = function(showLoader){
		$rootScope.CMTLoader=showLoader;
		DataFactory.showLoader = showLoader;
	};
	DataFactory.getShowLoader = function(){
		return DataFactory.showLoader;
	};
	return DataFactory;
});
