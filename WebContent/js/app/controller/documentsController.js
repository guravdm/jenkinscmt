'use strict';

CMTApp.controller('documentsController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window) {
	
	$scope.AllTasks				= [];
	$scope.searchObj 			= {};
	$scope.originalEntity 		= [];
	$scope.originalUnit   		= [];
	$scope.originalFunction   	= [];
	$scope.originalExecutor   	= [];
	$scope.originalEvaluator   	= [];
	$scope.originalCatLaw       = [];
	$scope.alllegiList			= {};
	$scope.originalLegiList     = [];
	$scope.originalAllRuleList  = {};
	$scope.OriginalAllTasks     = [];
	$scope.OriginalEvents       = [];
	$scope.OriginalSubEvents    = [];
	// $scope.limit = 100;
	$scope.UnitList             = [];
	$scope.FunctionList         = [];
	
	$scope.showAdvanceSearch    = false;
	$scope.CategoryOfLaw        = [];
	$scope.impactList 			= [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
	$scope.Proh_Pres            = [{id:"Prohibitive",name:"Prohibitive"},{id:"Prescriptive",name:"Prescriptive"}];
	$scope.legiList				= {selected:""};
	$scope.legiRuleL			= {selected:""};
	$scope.legiRuleL.selected   = undefined;
	$scope.legiList.selected    = undefined;
	$scope.event                = {};
	$scope.sub_event            = {};
	
	//$scope.allRuleList          = [];
	//$scope.searchObj.orga_id    
	$scope.legiRuleList        = [];
	$scope.scrollTasks = [];
	$scope.defaultInitiation = {};
	$scope.defaultInitiation.date_of_initiation = new Date();
	$scope.showButton = true;
	
	//get repository List
	$scope.getComplianceRepository=function(){
		 spinnerService.show('html5spinner');
		ApiCallFactory.getalldocuments($scope.AllTasks).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			//$scope.AllTasks       		= res.AllTasks;
			//console.log('res'+JSON.stringify(res.AllTasks));
			$scope.AllTasksCount  		= $scope.AllTasks.length;
			$scope.originalEntity 		= res.OrganogramFilter[0].Entity;
			$scope.originalUnit   		= res.OrganogramFilter[0].Unit;
			$scope.originalFunction   	= res.OrganogramFilter[0].Function;
			$scope.originalExecutor   	= res.OrganogramFilter[0].Executor;
			$scope.originalEvaluator   	= res.OrganogramFilter[0].Evaluator;
			$scope.originalCatLaw       = res.TasksFilter[0].Cat_law;
			$scope.originalFrequency    = res.TasksFilter[0].Frequency;
			$scope.originalTypeOfTask   = res.TasksFilter[0].Type_Of_Task;
			//$scope.alllegiList          = res.TasksFilter[0].Legislation;
			$scope.originalLegiList     = res.TasksFilter[0].Legislation;
			$scope.originalAllRuleList  = res.TasksFilter[0].Rule;
			$scope.OriginalEvents       = res.TasksFilter[0].Event;
			$scope.OriginalSubEvents    = res.TasksFilter[0].Sub_Event;
			$scope.OriginalAllTasks     = res.AllTasks;
			$scope.addMoreItems();//To get first 500 records
			//console.log("Filter Data "+JSON.stringify($scope.OriginalSubEvents));
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get repository list====="+error);
		});
	};
	$scope.getComplianceRepository();
	
	//get task history
	$scope.getTaskHistoryList=function(task_id){
		
		$state.transitionTo('taskDetails', {'task_id':task_id});

	};
	
	$scope.getFunctionList = function(){
		
		$scope.FunctionList=[];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.originalFunction.length!=0){
			angular.forEach($scope.originalFunction, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					$scope.FunctionList.push(item);
				}

			});
		};
		
	}
	
	$scope.getUnitList = function(){
			$scope.UnitList=[];
			if($scope.searchObj.orga_id!="" && $scope.originalUnit.length!=0){
				angular.forEach($scope.originalUnit, function (item) {
					if( item.orga_id == $scope.searchObj.orga_id){
						$scope.UnitList.push(item);
					}
	
				});
			}
			
		}
	
	//Get Executor/Evaluator list
	$scope.getExecutorEvaluatorList= function(){
		$scope.executorList=[];
		$scope.evaluatorList=[];
		var executor_array = [];
		var evaluator_array = [];
		if($scope.searchObj.orga_id!="" || $scope.searchObj.loca_id!="" || $scope.searchObj.dept_id!="" || $scope.originalExecutor.length!=0){
			angular.forEach($scope.originalExecutor, function (item) {
				if( (!$scope.searchObj.orga_id || item.orga_id === $scope.searchObj.orga_id) || (!$scope.searchObj.loca_id || item.loca_id === $scope.searchObj.loca_id) || (!$scope.searchObj.dept_id || item.dept_id === $scope.searchObj.dept_id)){
					//$scope.executorList.push(item);
					//console.log("item==="+JSON.stringify(item));
					if ($.inArray(item.user_id, executor_array)==-1) {
						executor_array.push(item.user_id);
						$scope.executorList.push(item);
					}
				}
			
			});
		};
		
		if($scope.searchObj.orga_id!="" || $scope.searchObj.loca_id!="" || $scope.searchObj.dept_id!="" || $scope.originalEvaluator.length!=0){
			angular.forEach($scope.originalEvaluator, function (item) {
				if((!$scope.searchObj.orga_id || item.orga_id === $scope.searchObj.orga_id) || (!$scope.searchObj.loca_id || item.loca_id === $scope.searchObj.loca_id) || (!$scope.searchObj.dept_id || item.dept_id === $scope.searchObj.dept_id)){
					//$scope.evaluatorList.push(item);
					if ($.inArray(item.user_id, evaluator_array)==-1) {
						evaluator_array.push(item.user_id);
						$scope.evaluatorList.push(item);
					}
				}
			
			});
		};
		
	}
	
	$scope.showAdvance= function(){ 
		if($scope.showAdvanceSearch==false)
			$scope.showAdvanceSearch = true;
		else
			$scope.showAdvanceSearch = false
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
							
	$scope.getLegislationList= function(){
		$scope.alllegiList=[];
		var alllegiList_array = [];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalLegiList.length!=0){
			angular.forEach($scope.originalLegiList, function (item) {
				if ((!$scope.searchObj.orga_id || item.orga_id == $scope.searchObj.orga_id) && (!$scope.searchObj.loca_id || item.loca_id == $scope.searchObj.loca_id)&& (!$scope.searchObj.dept_id || item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.task_legi_id, alllegiList_array)==-1) {
						alllegiList_array.push(item.task_legi_id);
						$scope.alllegiList.push(item);
					}
				}
			});
		};
	}
	
	$scope.getEventSubEvent=function(){ 
		if($scope.legiList.selected!=undefined){
			$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
		}else{
			$scope.searchObj.legi_id=0;
		}
		if($scope.legiRuleL.selected!=undefined){
			$scope.searchObj.rule_id=$scope.legiRuleL.selected.task_rule_id;
		}else{
			$scope.searchObj.rule_id=0;
		}
		
		//$scope.event                = [];
		$scope.sub_event            = [];
		/*var event = [];
		if(	$scope.searchObj.legi_id!=0 && $scope.searchObj.legi_id!=undefined){
			angular.forEach($scope.OriginalEvents, function (item) {
				if((item.task_legi_id == $scope.searchObj.legi_id)){
					if(event.indexOf(item.task_Event) === -1){
						event.push(item.task_Event);
						$scope.event.push(item);
					}
				}
			});
		}*/
		
		
	}
	
	$scope.searchRepo = false; //if true then scroll event not bind task
	$scope.searchRepository=function(){ 
		$scope.showButton = false;
		
		$scope.searchRepo = true;
		$scope.AllTasks = [];
		$scope.scrollTasks = [];
		//spinnerService.show('html5spinner');
		//$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
		
		if($scope.legiList.selected!=undefined){
			$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
		}else{
			$scope.searchObj.legi_id=0;
		}
		if($scope.legiRuleL.selected!=undefined){
			$scope.searchObj.rule_id=$scope.legiRuleL.selected.task_rule_id;
		}else{
			$scope.searchObj.rule_id=0;
		}
		
		angular.forEach($scope.OriginalAllTasks, function (data) {
			
			if ( (!$scope.searchObj.orga_id || data.orga_id === $scope.searchObj.orga_id) && 
					(!$scope.searchObj.loca_id || data.loca_id === $scope.searchObj.loca_id) &&
					(!$scope.searchObj.dept_id || data.dept_id === $scope.searchObj.dept_id) &&
					(!$scope.searchObj.evaluator_id || data.reviewer_id === $scope.searchObj.evaluator_id) &&
					(!$scope.searchObj.executor_id || data.performer_id === $scope.searchObj.executor_id) &&
					(!$scope.searchObj.cat_law_id || data.task_cat_law_id === $scope.searchObj.cat_law_id) &&
					(!$scope.searchObj.impact || data.task_impact === $scope.searchObj.impact) &&
					(!$scope.searchObj.proh_pres || data.task_prohibitive === $scope.searchObj.proh_pres) &&
					(!$scope.searchObj.type_of_task || data.task_type_of_task === $scope.searchObj.type_of_task) &&
					(!$scope.searchObj.frequency || data.task_frequency_for_operation === $scope.searchObj.frequency) &&
					(!$scope.searchObj.task_status || data.ttrn_status === $scope.searchObj.task_status) &&
					(!$scope.searchObj.legi_id || data.task_legi_id === $scope.searchObj.legi_id) &&
					(!$scope.searchObj.rule_id || data.task_rule_id === $scope.searchObj.rule_id) &&
					(!$scope.searchObj.event || data.task_event === $scope.searchObj.event) &&
					(!$scope.searchObj.sub_event || data.task_sub_event === $scope.searchObj.sub_event)
					
			   ){
				
				//spinnerService.show('html5spinner');
				
				$scope.scrollTasks.push(data);
			}
			
			//spinnerService.hide('html5spinner');
		});
		$scope.showButton = true;
		$scope.AllTasksCount  		= $scope.scrollTasks.length;
		
	}
	//On scroll load task
	$scope.taskCount       = 500;
	$scope.start_count     = 0;
	$scope.scrollStatus    = false;
	$scope.cnt             = 0;
	$scope.appendAllRecord = false;
	$scope.addMoreItems = function(){
		if($scope.cnt != $scope.AllTasksCount && $scope.OriginalAllTasks.length!=0 && $scope.scrollStatus == false && $scope.appendAllRecord == false && $scope.searchRepo==false){
			$scope.scrollStatus = true;
			for (var j=$scope.start_count; (j < $scope.taskCount && j <$scope.AllTasksCount); j++) {
		        $scope.scrollTasks.push($scope.OriginalAllTasks[j]);
		        $scope.cnt++;
		    }
			$scope.start_count  +=500
			$scope.taskCount    +=500;
			$scope.scrollStatus  = false;
		}
	}

	
	//getAssignTaskId on change check box
	// $scope.tmapObj.tasks_list=[];
	$scope.defaultInitiation.tasks_list = [];
	 $scope.getCheckTaskId = function(selectId,dtco_id){
		 if(selectId==true){
			 var obj={
					 dtco_id    : dtco_id
       			}
			 $scope.defaultInitiation.tasks_list.push(obj);
		 }else{ 
			 var index=0;
			 angular.forEach($scope.defaultInitiation.tasks_list, function (item) {
				 if(item.dtco_id==dtco_id){
					 $scope.defaultInitiation.tasks_list.splice(index, 1);   
				//	  break;
				 }
				 index++;
				 
			 });
			
			 
		}
			
	 }
	 
	 $scope.moreinformation = {};
	 $scope.moreInfo = function(data){
		 $scope.moreinformation.client_task_id = data.tmap_client_task_id;
		 $scope.moreinformation.enti_name = data.orga_name;
		 $scope.moreinformation.unit_name = data.loca_name;
		 $scope.moreinformation.fun_name = data.dept_name;
		 $scope.moreinformation.rev_name = data.reviewer_name;
		 $scope.moreinformation.per_name = data.performer_name;
		 $scope.moreinformation.fh_name = data.function_head_name;
		 $("#more_info").modal();
		 
	 }
	 
	 $scope.getLegiByCatLawId = function(){
		 $scope.alllegiList = [];
		 var alllegiList_array = [];
			if($scope.searchObj.cat_law_id!=undefined){
				
			}else{
				$scope.searchObj.cat_law_id=0;
			}
			
			if(	$scope.searchObj.cat_law_id!=0 && $scope.searchObj.cat_law_id!=undefined){
				angular.forEach($scope.originalLegiList, function (item) {
					if(item.task_cat_law_id == $scope.searchObj.cat_law_id){
						if ($.inArray(item.task_legi_id, alllegiList_array)==-1) {
							alllegiList_array.push(item.task_legi_id);
							$scope.alllegiList.push(item);
						}
					}

				});
			}else{
				$scope.getLegislationList();
				 //$scope.alllegiList = $scope.originalLegiList;
			}
		 
		 
	 }
	 
	 $scope.downloadDocument = function(doc_id){
         $window.location = "./downloadProofOfCompliance?udoc_id="+doc_id;
         //$state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});
	}
	 
}]);