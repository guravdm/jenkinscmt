'use strict';

CMTApp.controller('designationController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	$scope.designationObj={};
	$scope.newDesiName=true;
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	//$scope.showUpdateBtn=false;
	if(!angular.isUndefined($stateParams.desi_name)){
		$scope.designationObj.desi_name=$stateParams.desi_name;
		$scope.designationObj.desi_id=$stateParams.desi_id;
	}else{
		
		$scope.designationObj={};
	}
	//add new deparment
	$scope.addDesignation=function(formValid){
		if(formValid && $scope.newDesiName){
			spinnerService.show('html5spinner');
		//DataFactory.setShowLoader(true);
		ApiCallFactory.addDesignation($scope.designationObj).success(function(res,status){
			spinnerService.hide('html5spinner');
		  //DataFactory.setShowLoader(false);
			if(status === 200){
				toaster.success("Success", "Designation saved successfully");
				$state.go('DesignationList');
			}else{
				toaster.error("Faild", "Error in save");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("add designation===="+error);
		});
		}
	}
	
	//Check duplicate designation
	$scope.duplicateDesignation=function(formValid){
		if(formValid){
			ApiCallFactory.duplicateDesignation($scope.designationObj).success(function(res,status){
				if(status === 200){
					if(res.duplicate == "True"){
						 $scope.newDesiName=false;
					    //	toaster.warning("Warning", "Department name already exist");
					}else{
						 $scope.newDesiName=true;
					}
				}
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("duplicate designation===="+error);
			});
		}
	}
	
	//Update department
	$scope.updateDesignation=function(formValid){
		if(formValid && $scope.newDesiName){
			DataFactory.setShowLoader(true);
			ApiCallFactory.updateDesignation($scope.designationObj).success(function(res,status){
			  DataFactory.setShowLoader(false);
				if(status === 200){
					toaster.success("Success", "Designation updated successfully");
					$state.go('DesignationList');
				}else{
					toaster.error("Faild", "Error in update");
				}
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("update designation===="+error);
			});
	 }
	}
}]);