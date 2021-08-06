'use strict';

CMTApp.controller('designationListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	//get department List
	$scope.getDesignationList=function(){
		spinnerService.show('html5spinner');
		ApiCallFactory.getDesignationList().success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			$scope.designationList=res;
			console.log('res'+JSON.stringify(res));
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get designation list====="+error);
		});
	};
	$scope.getDesignationList();
	
	//Edit designation info
	$scope.showEditDesignation=function(id, name){
		$scope.designationtObj={
				desi_name:name,
				desi_id:id,
		};
		$state.transitionTo('Designation', {'desi_name':name,'desi_id':id});
	//	$state.go('Department',{'dept_name':name,'dept_id':id});
	}
	
	//Add designation info
	$scope.showAddDesignation=function(){
		$state.transitionTo('Designation',{'desi_name':null,'desi_id':0});
		
		//$state.go('Department');
	}
	
	
}]);