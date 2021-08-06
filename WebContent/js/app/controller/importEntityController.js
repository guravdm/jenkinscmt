'use strict';

CMTApp.controller('importEntityController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	$scope.EntityObj={};
	$scope.hideTable=true;

	//add new entity's
	$scope.uploadFile=function(formValid) {
		if(formValid) {
			spinnerService.show('html5spinner');		
			var formData = new FormData();
			var dummyJson = { name: 'Mahesh' };
			if(!angular.isUndefined($scope.EntityObj.entity_update_list)){
				for (var i=0; i<$scope.EntityObj.entity_update_list.length; i++) {
					formData.append("entity_update_list", $scope.EntityObj.entity_update_list[i]);
				}
			}
			formData.append("jsonString", JSON.stringify(dummyJson));
			console.log('JSON.stringify(dummyJson) : ' + JSON.stringify(dummyJson));
			console.log('formData : ' + formData);

			ApiCallFactory.importEntityFile(formData).success(function(res,status) {
				spinnerService.hide('html5spinner');
				if(res.responseMessage == 'Success'){
					toaster.success("Success", "Sheet uploaded successfully");
					$state.go('EntityList');
				} else if(res.responseMessage == 'File type mismatch'){
					toaster.error("File Type Mismatch, Please import CSV file");
				} else {
					toaster.error("Failed", "Error in import");
				}
				
			}).error(function(error) {
				spinnerService.hide('html5spinner');
				console.log("Error in import===="+error);

			});
		}
	}
	

}]);