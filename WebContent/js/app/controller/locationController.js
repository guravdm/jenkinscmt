'use strict';

CMTApp.controller('locationController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	$scope.locationObj={};
	$scope.newLocaName=true;
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	/*if($scope.userRoleId < 6){
		$state.go('login');
	}*/
	
	//$scope.showUpdateBtn=false;
	if(!angular.isUndefined($stateParams.loca_name)){
		$scope.locationObj.loca_name=$stateParams.loca_name;
		$scope.locationObj.loca_id=$stateParams.loca_id;
	}else{
		
		$scope.locationObj={};
	}
	
	//add new deparment
	$scope.addLocation=function(formValid){
		if(formValid && $scope.newLocaName){
			spinnerService.show('html5spinner');
		//DataFactory.setShowLoader(true);
		ApiCallFactory.addUnit($scope.locationObj).success(function(res,status){
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
					toaster.success("Success", "Location saved successfully");
					$state.go('LocationList');
				} else {
					toaster.error("Failed", "Error in save");
				}
			} 
			
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("add location===="+error);
		});
		}
	}
	
	//Check duplicate Unit
	$scope.duplicateLocation=function(formValid){
		if(formValid){
			ApiCallFactory.duplicateUnit($scope.locationObj).success(function(res,status){
				if(status === 200){
					if(res.duplicate == "True"){
						 $scope.newLocaName=false;
					    	toaster.warning("Warning", "location name already exist");
					}else{
						 $scope.newLocaName=true;
					}
				}
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("duplicate Location===="+error);
			});
		}
	}
	
	//Update Location
	$scope.updateLocation=function(formValid){
		if(formValid && $scope.newLocaName){
			DataFactory.setShowLoader(true);
			ApiCallFactory.updateUnit($scope.locationObj).success(function(res,status){
			  DataFactory.setShowLoader(false);
				if(res.responseMessage == "Failed")
				{
					$state.go('login');
				} 
				else {
					if(res.responseMessage == "Success") {
						$scope.loading = false;
						toaster.success("Success", "Location updated successfully");
						$state.go('LocationList');
					}
				} 
				
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("update location===="+error);
			});
	 }
	}
}]);