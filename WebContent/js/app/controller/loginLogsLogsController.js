'use strict';

CMTApp.controller('loginLogsLogsController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage,toaster,spinnerService) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
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



//spinnerService.show('html5spinner');
//ApiCallFactory.getUnitList().success(function(res,status){
//	spinnerService.hide('html5spinner');
//	if(status==200){
//	$scope.locationList=res;
//	console.log('res'+JSON.stringify(res));
//	}
//}).error(function(error){
//	spinnerService.hide('html5spinner');
//	console.log("get location list====="+error);
//});