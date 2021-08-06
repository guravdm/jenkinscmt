'use strict';

CMTApp.controller('entityListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	//get entity List
	$scope.getEntityList=function(){
		spinnerService.show('html5spinner');
		ApiCallFactory.getEntityList().success(function(res,status){
			spinnerService.hide('html5spinner');
			$scope.loading=true;
			if(res[0].responseMessage == "Failed")
			{
				$state.go('login');
			} else {
				if(status==200) {
					$scope.loading = false;
					DataFactory.setShowLoader(false);
					$scope.entityList=res;
				}
			} 
				
			
			/*if(status==200){
			$scope.entityList=res;
			}*/
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get Entity list====="+error);
		});
	};
	$scope.getEntityList();
	
	//Edit entity info
	$scope.showEditEntity=function(id, name, parentId){
		$state.go('Entity',{'orga_name':name,'orga_id':id,'orga_parent_id':parentId});
	}
	
	//Add entity infoorga_parent_id
	$scope.showAddEntity=function(){
		$state.go('Entity',{'orga_name':null,'orga_id':0,'orga_parent_id':0});
	}
	
	$scope.importAddEntity=function(){
		$state.go('importEntity',{'orga_name':null,'orga_id':0,'orga_parent_id':0});
	}
	
}]);