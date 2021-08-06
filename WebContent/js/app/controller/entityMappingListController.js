'use strict';

CMTApp.controller('entityMappingListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	//get entity mapping List
	$scope.getEntityMappingList=function(){
		spinnerService.show('html5spinner');
		ApiCallFactory.getEntityMappingList().success(function(res,status){
			spinnerService.hide('html5spinner');
			
			$scope.loading=true;
			if(res[0].responseMessage == "Failed")
			{
				$state.go('login');
			}else {
				if(status==200) {
					$scope.loading = false;
					DataFactory.setShowLoader(false);
					$scope.entityMappingList=res;
				}
			}
			/*if(status==200){
				
			$scope.entityMappingList=res;
			}*/
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get Entity mapping list====="+error);
		});
	};
	$scope.getEntityMappingList();
	
	//Edit entity info
	$scope.showEditEntity=function(id){
		$state.go('EntityMapping',{'enti_id':id});
	}
	
	//Add entity info
	$scope.showAddEntity=function(){
		$state.go('EntityMapping',{'enti_id':0});
	}
	$scope.showImportForm = function(){
		$state.go('importEntityMappingList');
	}
}]);