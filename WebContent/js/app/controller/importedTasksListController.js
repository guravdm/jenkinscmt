'use strict';

CMTApp.controller('importedTasksListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	
	$scope.importList={};
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	//get entity List
	$scope.getImportedTasksList=function(){
		spinnerService.show('html5spinner');
		ApiCallFactory.getImportedTasksList().success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			$scope.importList=res;
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get Entity list====="+error);
		});
	};
	$scope.getImportedTasksList();
	
	
	
}]);