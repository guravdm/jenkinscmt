'use strict';

CMTApp.controller('entityController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
/*	if($scope.userRoleId < 6){
		$state.go('login');
	}*/
	
	$scope.entityObj={};
	$scope.newEntityName=true;
	$scope.parentArray=[];
	$scope.entityListobj=[];

	if(!angular.isUndefined($stateParams.orga_name) && $stateParams.orga_id!=0){
		$scope.entityObj.orga_name=$stateParams.orga_name;
		$scope.entityObj.orga_id=$stateParams.orga_id;
		$scope.entityObj.orga_parent_id=$stateParams.orga_parent_id;
		//console.log("IN IF");
	}else{
		//console.log("IN ELSE");
		$scope.entityObj={};
		$scope.entityObj.orga_id=0;
	}
	//console.log("entity "+JSON.stringify($scope.entityObj));
	$scope.getMainEntityList=function(){
		ApiCallFactory.getMainEntityList().success(function(res,status){
			if(status === 200){
				if(res.length>0){
					$scope.parentArray=res;
				}

			}else{
				toaster.error("Failed", "Error in get  entity select list");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("add entity===="+error);
		});
	}
	$scope.getMainEntityList();

	//add new deparment
	$scope.addEntity=function(formValid){
		spinnerService.show('html5spinner');
		//console.log("name "+$scope.newOrgaName);
		if(formValid && $scope.newOrgaName){
			//DataFactory.setShowLoader(true);
			ApiCallFactory.addEntity($scope.entityObj).success(function(res,status){
				spinnerService.hide('html5spinner');
				//DataFactory.setShowLoader(false);
				if(res.responseMessage == "Failed")
				{
					toaster.error("Authorization Failed");
					$state.go('login');
				} 
				else {
					if(res.responseMessage == "Success") {
						$scope.loading = false;
						toaster.success("Success", "Entity saved successfully");
						$state.go('EntityList');
					} else {
						toaster.error("Failed", "Error in save");
					}
				} 
				
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("add entity===="+error);
			});
		}

	}


	//Check duplicate Entity
	$scope.duplicateEntity=function(formValid){
		if(formValid){
			ApiCallFactory.duplicateEntity($scope.entityObj).success(function(res,status){
				if(status === 200){
					if(res.duplicate == "True"){
						$scope.newOrgaName=false;
						toaster.warning("Warning", "Organization name already exist");
					}else{
						$scope.newOrgaName=true;
					}
				}
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("duplicate Organization==== "+error);
			});
		}
	}


	$scope.updateEntity=function(formValid){
		//console.log(" name "+$scope.newOrgaName);
		if(formValid && $scope.newOrgaName){
			DataFactory.setShowLoader(true);
			ApiCallFactory.updateEntity($scope.entityObj).success(function(res,status){
				DataFactory.setShowLoader(false);
				if(res.responseMessage == "Failed")
				{
					$state.go('login');
				} 
				else {
					if(res.responseMessage == "Success") {
						$scope.loading = false;
						toaster.success("Success", "Entity updated successfully");
						$state.go('EntityList');
					}
				} 
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("add entity===="+error);
			});
		}
	}
}]);