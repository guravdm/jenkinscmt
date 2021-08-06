'use strict';

CMTApp.controller('emailLogsController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage,toaster,spinnerService) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	$scope.getEmailLogs = function() {
		spinnerService.show('html5spinner');
		ApiCallFactory.getmailLogs().success(function(res, status) {
			spinnerService.hide('html5spinner');
			if(status==200) {
				$scope.logList = res;
				// console.log("in if : " + JSON.stringify($scope.logList));
				toaster.success("Success", "loading...");
			}
		});
	}
	
	$scope.getEmailLogs();

}]);
