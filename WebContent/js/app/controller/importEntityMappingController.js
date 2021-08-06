'use strict';

CMTApp.controller('importEntityMappingController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	$scope.entityObj={};
	$scope.hideTable=true;
	
	//add new deparment
	$scope.importEntityMappingList = function(formValid){
        //alert("hii");
		if(formValid){
		spinnerService.show('html5spinner');		
		var formData = new FormData();
		var dummyJson={name:'Swapnali'};
		//formData.append("legal_update_activity_list",$scope.userObj.user_list);
		if(!angular.isUndefined($scope.entityObj.entity_mapping_list)){
			for (var i=0; i<$scope.entityObj.entity_mapping_list.length; i++) {
				formData.append("entity_mapping_list",$scope.entityObj.entity_mapping_list[i]);
			}
		}
		formData.append("jsonString",JSON.stringify(dummyJson));
		
		ApiCallFactory.uploadEntityMappingList(formData).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(res.responseMessage == 'Success'){
				toaster.success("Success", "Sheet uploaded successfully");
				$state.go('EntityMappingList');
			} else if(res.responseMessage == 'File type mismatch'){
				toaster.error("File Type Mismatch, Please import CSV file");
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