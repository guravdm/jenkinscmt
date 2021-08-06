'use strict';

CMTApp.controller('importSubTasksController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window) {
	
	$scope.searchObj={};
	$scope.taskList=[];
	$scope.originalTaskList=[];
	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.originalExecutorList=[];
	$scope.originalEvaluatorList=[];
	$scope.alllegiList			= {};
	$scope.originalAllRuleList  = {};
	$scope.legiList				= {selected:""};
	$scope.legiRuleL			= {selected:""};
	$scope.legiRuleL.selected   = undefined;
	$scope.legiList.selected    = undefined;
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	
	$scope.getUserDefinedTask=function(){
		var obj={}
		ApiCallFactory.getUserDefinedTask(obj).success(function(res,status){
			if(res.errorMessage!="Failed"){
			$scope.taskList=res.SUBTASK;
			//console.log('res'+JSON.stringify(res));
			$scope.originalTaskList=res.SUBTASK;
			$scope.originalEntityList=res.Entity;
			$scope.entityList=res.Entity;
			$scope.originalUnitList=res.Unit;
			$scope.originalFunctionList=res.Function;
			$scope.originalExecutorList=res.Executor;
			$scope.originalEvaluatorList=res.Evaluator;
			$scope.alllegiList          = res.Legislation;
			$scope.originalAllRuleList  = res.Rule;
			
			}else{
				console.log("get List=====Failed");
			}
		}).error(function(error){
			console.log("get List====="+error);
		});
	};
	$scope.getUserDefinedTask();
	
	$scope.getEntityDependentArray = function(){
		$scope.unitList=[];
		if($scope.searchObj.orga_id!="" && $scope.originalUnitList.length!=0){
			angular.forEach($scope.originalUnitList, function (item) {
				if( item.orga_id == $scope.searchObj.orga_id){
					$scope.unitList.push(item);
				}

			});
		};

		
		///Add executor and Evaluator
		$scope.executorList=[];
		$scope.evaluatorList=[];
		var executor_array = [];
		var evaluator_array = [];
		if($scope.searchObj.orga_id!="" && $scope.originalExecutorList.length!=0){
			angular.forEach($scope.originalExecutorList, function (item) {
				
				if(item.orga_id == $scope.searchObj.orga_id){
					 if ($.inArray(item.user_id, executor_array)==-1) {
					    	executor_array.push(item.user_id);
					        $scope.executorList.push(item);
					    }
					}
			});
		};
		if($scope.searchObj.orga_id!="" && $scope.originalEvaluatorList.length!=0){
			angular.forEach($scope.originalEvaluatorList, function (item) {
				if(item.orga_id == $scope.searchObj.orga_id){
					if ($.inArray(item.user_id, evaluator_array)==-1) {
						evaluator_array.push(item.user_id);
						$scope.evaluatorList.push(item);
					}
				}

			});
		};
	}
	
	$scope.getUnitDependentArray= function(){
		$scope.functionList=[];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.originalFunctionList.length!=0){
			angular.forEach($scope.originalFunctionList, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					$scope.functionList.push(item);
				}

			});
		};


		///Add executor and Evaluator
		$scope.executorList=[];
		$scope.evaluatorList=[];
		var executor_array = [];
		var evaluator_array = [];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalExecutorList.length!=0){
			angular.forEach($scope.originalExecutorList, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					if ($.inArray(item.user_id, executor_array)==-1) {
						executor_array.push(item.user_id);
						$scope.executorList.push(item);
					}
				}
			});
		};
		
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalEvaluatorList.length!=0){
			angular.forEach($scope.originalEvaluatorList, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					if ($.inArray(item.user_id, evaluator_array)==-1) {
						evaluator_array.push(item.user_id);
						$scope.evaluatorList.push(item);
					}
				}

			});
		};
	}
	
	
	$scope.getFunctionDependentArray= function(){
		$scope.executorList=[];
		$scope.evaluatorList=[];
		var executor_array = [];
		var evaluator_array = [];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalExecutorList.length!=0){
			angular.forEach($scope.originalExecutorList, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.user_id, executor_array)==-1) {
						executor_array.push(item.user_id);
						$scope.executorList.push(item);
					}
				}
			});
		};
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalEvaluatorList.length!=0){
			angular.forEach($scope.originalEvaluatorList, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.user_id, executor_array)==-1) {
						executor_array.push(item.user_id);
						$scope.evaluatorList.push(item);
					}
				}
			});
		};
	}
  
	$scope.importSubTask= function(id){
		$state.go('importSubTasksForID',{'clientTaskId':id});
	}
	
	
	$scope.clearLegiRuleL = function($event) {
	    $event.stopPropagation(); 
	    $scope.legiRuleL.selected=undefined;
		
	  }
 $scope.clearLegiList = function($event) {
	    $event.stopPropagation(); 
	    $scope.legiList.selected=undefined;
		
 }
	
	$scope.getalllegiRuleList=function(){
		//$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
		$scope.legiRuleList = [];
		if($scope.legiList.selected!=undefined){
			$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
		}else{
			$scope.searchObj.legi_id=0;
		}
		if($scope.legiRuleL.selected!=undefined){
			$scope.searchObj.rule_id=$scope.legiRuleL.selected.task_legi_id;
		}else{
			$scope.searchObj.rule_id=0;
		}
		
		if(	$scope.searchObj.legi_id!=0 && $scope.searchObj.legi_id!=undefined){
			angular.forEach($scope.originalAllRuleList, function (item) {
				if( (item.task_legi_id == $scope.searchObj.legi_id)){
					$scope.legiRuleList.push(item);
				}

			});
		}
	}
	
	$scope.searchSubTasks = function(){
		$scope.taskList=[];
		angular.forEach($scope.originalTaskList, function (data) {
			if ( (!$scope.searchObj.orga_id || data.orga_id === $scope.searchObj.orga_id) && 
					(!$scope.searchObj.loca_id || data.loca_id === $scope.searchObj.loca_id) &&
					(!$scope.searchObj.dept_id || data.dept_id === $scope.searchObj.dept_id) &&
					(!$scope.searchObj.legi_id || data.task_legi_id === $scope.searchObj.legi_id) &&
					(!$scope.searchObj.rule_id || data.task_rule_id === $scope.searchObj.rule_id) &&
					(!$scope.searchObj.executor_id || data.exec_id === $scope.searchObj.executor_id) &&
					(!$scope.searchObj.evaluator_id || data.eval_id === $scope.searchObj.evaluator_id)){
				$scope.taskList.push(data);
			} 
		});
		
	}
}]);