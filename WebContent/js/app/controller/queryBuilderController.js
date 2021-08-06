'use strict';

CMTApp.controller('queryBuilderController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService', '$state' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage, toaster, spinnerService, $state) {

	$scope.queryObj = {};
	$scope.queryObjDeact = {};
	$scope.queryObjDis = {};

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	$scope.getQueryBuilder = function(formValid) {

		console.log('Data : ' + JSON.stringify($scope.queryObj));

		$scope.data = JSON.stringify($scope.queryObj);
		console.log('$scope.data : ' + $scope.data);
		spinnerService.show('html5spinner');
		ApiCallFactory.getQueryBuilder($scope.queryObj).success(function(res, status) {
			spinnerService.hide('html5spinner');
			if(status === 200 ){
				toaster.success("Success", "Query run successfully");
				$state.go('queryBuilder');
			}else{
				toaster.error("Faild", "Error in save");
			}
		});
	}

	/**
	 * DeActivation method
	 */
	$scope.getTasksDeactivationQueryBuilder = function(formValid) {
		$scope.data = JSON.stringify($scope.queryObjDeact);
		console.log('$scope.data : ' + $scope.data);
		spinnerService.show('html5spinner');
		ApiCallFactory.getTasksDeactivationQueryBuilder($scope.queryObjDeact).success(function(res, status) {
			spinnerService.hide('html5spinner');
			if(status === 200 ){
				toaster.success("Success", "Query run successfully");
				$state.go('queryBuilder');
			}else{
				toaster.error("Faild", "Error in save");
			}
		});
	}
	
	/**
	 * Three
	 */
	$scope.getDisabletasks = function(formValid) {
		$scope.data = JSON.stringify($scope.queryObjDis);
		console.log('$scope.data : ' + $scope.data);
		spinnerService.show('html5spinner');
		ApiCallFactory.getDisabletasks($scope.queryObjDis).success(function(res, status) {
			spinnerService.hide('html5spinner');
			if(status === 200 ){
				toaster.success("Success", "Query run successfully");
				$state.go('queryBuilder');
			}else{
				toaster.error("Faild", "Error in save");
			}
		});
	}



}]);
