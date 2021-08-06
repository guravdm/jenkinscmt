'use strict';

CMTApp.controller('importSubTasksForIDController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window) {
	
	$scope.clientTaskID = $stateParams.clientTaskId;
	console.log("Sub Task ID : "+$scope.clientTaskID);
	
	if(!angular.isUndefined($stateParams.clientTaskId) && $stateParams.clientTaskId!=0){
		$scope.clientTaskID=$stateParams.clientTaskId;
		$window.sessionStorage.setItem('clientTaskID',$stateParams.clientTaskId);
	}else{
	    	if(!angular.isUndefined($window.sessionStorage.getItem('clientTaskID')) && $window.sessionStorage.getItem('clientTaskID')!=0){
	    		$scope.clientTaskID=$window.sessionStorage.getItem('clientTaskID');
	    	}
		   // $window.sessionStorage.removeItem('task_id');
	}
	
	$scope.LegalObj={};
	$scope.hideTable=true;
	
	//add new deparment
	$scope.uploadFile=function(formValid){
		if(formValid){
		spinnerService.show('html5spinner');		
		var formData = new FormData();
		var ID={clientTaskId:$scope.clientTaskID};
		//formData.append("legal_update_activity_list",$scope.LegalObj.legal_update_activity_list);
		if(!angular.isUndefined($scope.LegalObj.subTaskFile )){
			for (var i=0; i<$scope.LegalObj.subTaskFile .length; i++) {
				formData.append("subTaskFile ",$scope.LegalObj.subTaskFile [i]);
			}
		}
		formData.append("json",JSON.stringify(ID));
		
		ApiCallFactory.importsubtask(formData).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status === 200 && res.Neglected_Task.length==0){
				toaster.success("Success", "Sheet uploaded successfully");
			}else if(status === 200 && res.Neglected_Task.length>0){
				$scope.hideTable=false;
					$scope.listOfNegligence=res.Neglected_Task;
					$scope.empty_fields=res.Empty_Fields;
			}else{
				toaster.error("Failed", "Error in import");
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("Error in import===="+error);
			
		});
		}

	}
}]);