'use strict';

CMTApp.controller('tasktaskConfigLogsLogsController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage,toaster,spinnerService) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	$scope.getConfigLogs = function() {
		spinnerService.show('html5spinner');
		ApiCallFactory.getConfigLogs().success(function(res, status) {
			spinnerService.hide('html5spinner');
			if(status==200) {
				$scope.configList = res;
				// console.log("in if : " + JSON.stringify($scope.configList));
				toaster.success("Success", "loading...");
			}
		});
	}
	
	$scope.getConfigLogs();

}]);
