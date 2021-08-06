'use strict';

CMTApp.controller('departmentController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	$scope.departmentObj={};
	$scope.newDeptName=true;
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	/*if($scope.userRoleId < 6){
		$state.go('login');
	}*/
	
	//$scope.showUpdateBtn=false;
	if(!angular.isUndefined($stateParams.dept_name)){
		$scope.departmentObj.dept_name=$stateParams.dept_name;
		$scope.departmentObj.dept_id=$stateParams.dept_id;
	}else{
		
		$scope.departmentObj={};
	}
	//add new deparment
	$scope.addDepartment=function(formValid){
		if(formValid && $scope.newDeptName){
			spinnerService.show('html5spinner');
		//DataFactory.setShowLoader(true);
		ApiCallFactory.addDeparment($scope.departmentObj).success(function(res,status){
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
					toaster.success("Success", "Department saved successfully");
					$state.go('DepartmentList');
				} else {
					toaster.error("Failed", "Error in save");
				}
			} 
			
			
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("add department===="+error);
		});
		}
	}
	
	//Check duplicate deparment
	$scope.duplicateDepartment=function(formValid){
		if(formValid){
			ApiCallFactory.duplicateDepartment($scope.departmentObj).success(function(res,status){
				if(status === 200){
					if(res.duplicate == "True"){
						 $scope.newDeptName=false;
					    	toaster.warning("Warning", "Department name already exist");
					}else{
						 $scope.newDeptName=true;
					}
				}
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("duplicate department===="+error);
			});
		}
	}
	
	//Update department
	$scope.updateDepartment=function(formValid){
		if(formValid && $scope.newDeptName){
			DataFactory.setShowLoader(true);
			ApiCallFactory.updateDeparment($scope.departmentObj).success(function(res,status){
			  DataFactory.setShowLoader(false);
				if(res.responseMessage == "Failed")
				{
					$state.go('login');
				} 
				else {
					if(res.responseMessage == "Success") {
						$scope.loading = false;
						toaster.success("Success", "Department updated successfully");
						$state.go('DepartmentList');
					}
				} 
			
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("update department===="+error);
			});
	 }
	}
}]);