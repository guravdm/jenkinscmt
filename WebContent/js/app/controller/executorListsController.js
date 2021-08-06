'use strict';

CMTApp.controller('executorListsController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', '$http', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService, $http) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;

	$scope.searchObj 			= {};

	/*	if($scope.userRoleId < 6){
		$state.go('login');
	}*/

	$scope.getEntityList = function() {			
		// spinnerService.show('html5spinner');		
		$http({
			url : "./getentity",
			method : "get",				
		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.entityList = result.data;																							
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});
	}

	$scope.getEntityList();

	$scope.getFunctionListByUnit = function(unitList,orga_id) {
		$scope.functionList = [];
		var dept_array = [];
		spinnerService.show('html5spinner');
		$http({
			url : "./getFunction",
			method : "get",
			params : {
				'unit_id' : unitList,
				'orga_id' : orga_id
			}
		}).then(function(result) {
			spinnerService.hide('html5spinner');

			$scope.OriginalfunctionList = result.data;

			angular.forEach($scope.OriginalfunctionList, function (item) {
				if ($.inArray(item.dept_id, dept_array)==-1) {
					dept_array.push(item.dept_id);
					$scope.functionList.push(item);
				}
			});

		}, function(result) {
			spinnerService.hide('html5spinner');
		});

	}


	$scope.getUnitListByEntity = function(entity) {
		spinnerService.show('html5spinner');
		$http({
			url : "./getunit",
			method : "get",
			params : {
				'entity_id' : entity
			}
		}).then(function(result) {
			spinnerService.hide('html5spinner');
			$scope.unitList = result.data;
		}, function(result) {
			spinnerService.hide('html5spinner');
		});

	}

	$scope.searchLists = function() {

		spinnerService.show('html5spinner');
		$http({
			url : "./searchExecutorList",
			method : "post",
			params : {
				'data' : $scope.searchObj
			}
		})
		.then(
				function(result) {
					console.log('result : ' + result);
					spinnerService.hide('html5spinner');
					$scope.exeList = result.data.executorLists;
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

	}


}]);