'use strict';

CMTApp.controller('taskAssignLogsController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage,toaster,spinnerService) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	$scope.getAssignLogs = function() {
		spinnerService.show('html5spinner');
		ApiCallFactory.getAssignLogs().success(function(res, status) {
			spinnerService.hide('html5spinner');
			if(status==200) {
				$scope.assignList = res;
				// console.log("in if : " + JSON.stringify($scope.assignList));
				toaster.success("Success", "loading...");
			}
		});
	}
	
	$scope.getAssignLogs();

}]);
