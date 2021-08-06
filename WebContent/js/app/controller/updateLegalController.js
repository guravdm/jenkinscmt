'use strict';

CMTApp.controller('updateLegalController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	$scope.LegalObj={};
	$scope.hideTable=true;
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	//add new deparment
	$scope.uploadFile=function(formValid){
		if(formValid){
		spinnerService.show('html5spinner');		
		var formData = new FormData();
		var dummyJson={name:'Mahesh'};
		//formData.append("legal_update_activity_list",$scope.LegalObj.legal_update_activity_list);
		if(!angular.isUndefined($scope.LegalObj.legal_update_activity_list)){
			for (var i=0; i<$scope.LegalObj.legal_update_activity_list.length; i++) {
				formData.append("legal_update_activity_list",$scope.LegalObj.legal_update_activity_list[i]);
			}
		}
		
		formData.append("jsonString",JSON.stringify(dummyJson));
		
		ApiCallFactory.uploadLegalFile(formData).success(function(res,status){
			spinnerService.hide('html5spinner');
			
			if(res.responseMessage == 'File type mismatch'){
				toaster.error("File Type Mismatch, Please import CSV file");
			}else if(status === 200 && res.neglectedTasks.length==0){
				toaster.success("Success", "Sheet uploaded successfully");
			}else if(status === 200){
				$scope.hideTable=false;
				$scope.listOfNegligence=res.neglectedTasks;
					
			} else {
				toaster.error("Failed", "Error in import");
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("Error in import===="+error);
			
		});
		}

	}
	

}]);