'use strict';

CMTApp.controller('changeComplianceOwnerLogsLogsController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage,toaster,spinnerService) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	$scope.getChangeCompOwnerLogs = function() {
		spinnerService.show('html5spinner');
		ApiCallFactory.getChangeCompOwnerLogs().success(function(res, status) {
			spinnerService.hide('html5spinner');
			if(status==200) {
				$scope.changeOwnerList = res;
				// console.log("in if : " + JSON.stringify($scope.changeOwnerList));
				toaster.success("Success", "loading...");
			}
		});
	}
	
	$scope.getChangeCompOwnerLogs();

}]);
