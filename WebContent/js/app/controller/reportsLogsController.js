'use strict';

CMTApp.controller('reportsLogsController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage,toaster,spinnerService) {

	$scope.getLoginLogs = function() {
		spinnerService.show('html5spinner');
		ApiCallFactory.getLoginLogs().success(function(res, status) {
			spinnerService.hide('html5spinner');
			if(status==200) {
				$scope.loginList = res;
				// console.log("in if : " + JSON.stringify($scope.logList));
				toaster.success("Success", "loading...");
			}
		});
	}
	
	$scope.getLoginLogs();

}]);
