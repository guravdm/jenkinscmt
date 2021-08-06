'use strict';

CMTApp.controller('commonEmailController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window) {

	$scope.searchObj={};
	$scope.tmapObj={};
	$scope.Events = [];
	$scope.originalTaskListt=[];
	$scope.originalEntityListt=[];
	$scope.originalUnitListt=[];
	$scope.originalFunctionListt=[];
	$scope.originalExecutorListt=[];
	$scope.originalEvaluatorListt=[];
	$scope.executorListt=[];
	$scope.evaluatorListt=[];
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	/*$scope.tmapObj={
			email_ids :"",
			subject:"",
			body:""
	};*/
	
	$scope.getuserwithaccessforcommonemail=function(){
		var obj={}
		ApiCallFactory.getuserwithaccessforcommonemail(obj).success(function(res,status){
			if(res.errorMessage!="Failed"){
		
			$scope.originalEntityListt=res.Entity;
			$scope.entityListt=res.Entity;
			$scope.originalUnitListt=res.Unit;
			$scope.originalFunctionListt=res.Function;
			$scope.originalExecutorListt=res.Users;
			//$scope.originalFunctionHeadListt=res.assignDropDowns[0].Function_Head;
			
			var executor_array = [];
			angular.forEach($scope.originalExecutorListt, function (item) {
				if ($.inArray(item.user_id, executor_array)==-1) {
				   	executor_array.push(item.user_id);
				    $scope.executorListt.push(item);
				}
				});
			}else{
				console.log("get List=====Failed");
			}
		}).error(function(error){
			console.log("get List====="+error);
		});
	};
	$scope.getuserwithaccessforcommonemail();
	
	
	$scope.getEntityDependentArray = function(){
		$scope.unitList=[];
		//console.log("search Object: "+JSON.stringify($scope.searchObj));
		if($scope.searchObj.orga_id!="" && $scope.originalUnitListt.length!=0){
			angular.forEach($scope.originalUnitListt, function (item) {
				if( item.orga_id == $scope.searchObj.orga_id){
					$scope.unitList.push(item);
				}

			});
		};

		
		///Add executor and Evaluator
		$scope.executorListt=[];
		var executor_array = [];
		if($scope.searchObj.orga_id!="" && $scope.originalExecutorListt.length!=0){
			angular.forEach($scope.originalExecutorListt, function (item) {
				if(item.orga_id == $scope.searchObj.orga_id){
					 if ($.inArray(item.user_id, executor_array)==-1) {
					    	executor_array.push(item.user_id);
					        $scope.executorListt.push(item);
					    }
					}
			});
		};
	}
	
	$scope.getUnitDependentArray= function(){
		$scope.functionList=[];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.originalFunctionListt.length!=0){
			angular.forEach($scope.originalFunctionListt, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					$scope.functionList.push(item);
				}

			});
		};


		///Add executor 
		$scope.executorListt=[];
		var executor_array = [];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalExecutorListt.length!=0){
			angular.forEach($scope.originalExecutorListt, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					if ($.inArray(item.user_id, executor_array)==-1) {
						executor_array.push(item.user_id);
						$scope.executorListt.push(item);
					}
				}
			});
		};
	}
	
	
	$scope.getFunctionDependentArray= function(){
		$scope.executorListt=[];
		var executor_array = [];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalExecutorListt.length!=0){
			angular.forEach($scope.originalExecutorListt, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.user_id, executor_array)==-1) {
						executor_array.push(item.user_id);
						$scope.executorListt.push(item);
					}
				}
			});
		};
	}
	
	$scope.proofCompliance = {};
	$scope.sendCommonEmailAlert=function(formValid){
		if(formValid){
		
		spinnerService.show('html5spinner');
		$scope.tmapObj={
			email_ids :$scope.searchObj.executor_id,
			subject:$scope.tmapObj.subject,
			body:$scope.tmapObj.body
		};
		
		$scope.stringifyCommonEmail= JSON.stringify($scope.tmapObj)
		var formData = new FormData();
		
		if(!angular.isUndefined($scope.proofCompliance.ttrn_proof_of_compliance)){
			for (var i=0; i<$scope.proofCompliance.ttrn_proof_of_compliance.length; i++) {
				formData.append("ttrn_proof_of_compliance", $scope.proofCompliance.ttrn_proof_of_compliance[i]);
			}
		}  
		formData.append("jsonString",$scope.stringifyCommonEmail);
		
		ApiCallFactory.sendcommonemail(formData).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200 && res.responseMessage=="Success"){
				toaster.success("Success", "Email alert sent successfully.");
				$scope.tmapObj = {};
				$scope.searchObj = {};
				$location.path('/dashboard');
			}else{
				toaster.error("Failed", "Please try again.");
				$scope.tmapObj = {};
				$scope.searchObj = {};
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log(" getTaskList ====="+error);
		});
		
		}
	}
	
	
	
}]);