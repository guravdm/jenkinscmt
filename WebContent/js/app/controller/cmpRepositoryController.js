
'use strict';

CMTApp.controller('cmpRepositoryController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window','$http','$mdDialog', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window,$http,$mdDialog) {

	$scope.user_id = $window.localStorage.getItem('userDetais.sess_user_id');
	//alert($scope.user_id);
	$scope.deleteDocument  = {};
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
	$scope.showOnMultipleSelection=false;
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
	$scope.termFileCount = 1;

	//For multiple task completion
	$scope.multipleCompletion = {};
	//$scope.allRuleList          = [];
	//$scope.searchObj.orga_id    
	$scope.legiRuleList        = [];
	$scope.scrollTasks = [];
	$scope.defaultInitiation = {};
	$scope.defaultInitiation.date_of_initiation = new Date();
	$scope.showButton = true;
	$scope.totalTask = true;

	$scope.format = 'dd-MM-yyyy'; //$scope.formats[0];
	$scope.maxDate = new Date();
	$scope.subtaskcompetedDate = function() {
		$scope.popup_completed_date.opened = true;
	};

	$scope.popup_completed_date = {
			opened: false
	};

	$scope.nextExamination = function() {
		$scope.popup_next_examination.opened = true;
	};

	$scope.popup_next_examination = {
			opened: false
	};


	$scope.task = {};
	$scope.proofCompliance = {};
	$scope.task_history = {};
	$scope.subTaskHistory = {};
	//$scope.task_history.documents = {};

	//$scope.eventNotOccured=false;
	$scope.client_task_id = null;
	$scope.task_details = [];
	$scope.taskCompletion = {};
	$scope.subTaskCompletion = {};
	$scope.reOpen = {};
	$scope.taskCompletion.ttrn_completed_date = new Date();
	$scope.currentDate = new Date();
	console.log("Todays Date is : "+$scope.currentDate);

	//$scope.taskCompletion.ttrn_ids = [];
	$scope.task_ttrn_ids = [];
	$scope.ttrn_task_status = null;
	$scope.statusDetailsClick = 0;


	if(!angular.isUndefined($stateParams.task_id) && $stateParams.task_id!=0){
		$scope.task.task_id=$stateParams.task_id;
		$window.sessionStorage.setItem('task_id',$stateParams.task_id);
	}else{
		if(!angular.isUndefined($window.sessionStorage.getItem('task_id')) && $window.sessionStorage.getItem('task_id')!=0){
			$scope.task.task_id=$window.sessionStorage.getItem('task_id');
		}
		// $window.sessionStorage.removeItem('task_id');
	}

	if(!angular.isUndefined($stateParams.ttrn_id) && $stateParams.ttrn_id!=0){
		$scope.task.ttrn_id=$stateParams.ttrn_id;
	}else{
		$scope.task.ttrn_id=$stateParams.ttrn_id;
	}

	if(!angular.isUndefined($stateParams.document) && $stateParams.document!=0){
		$scope.task.document=$stateParams.document;
	}else{
		$scope.task.document=$stateParams.document;
	}

	if(!angular.isUndefined($stateParams.comments) || $stateParams.comments!=''){
		$scope.taskCompletion.ttrn_performer_comments=$stateParams.comments;
	}else{
		//$scope.taskCompletion.ttrn_performer_comments=$stateParams.comments;
	}

	$scope.status=$stateParams.status
	console.log("staus is "+$scope.status);

	$scope.frequency=$stateParams.frequency
	//console.log("frequency is"+$scope.frequency);

	$scope.frequency=$stateParams.frequency
	//console.log("frequency is"+$scope.frequency);

	//$scope.legalDueDate
	if(!angular.isUndefined($stateParams.legalDueDate) && $stateParams.legalDueDate!=0){
		var legal = $stateParams.legalDueDate.split("-");
		var legalDueDate = new Date(legal[2], legal[1] - 1, legal[0]);
		$scope.legalDueDate= legalDueDate;

	}else{

	}
	console.log("Legal Due Date is : "+$scope.legalDueDate);

	if(!angular.isUndefined($stateParams.reason) || $stateParams.reason!=''){
		$scope.taskCompletion.ttrn_reason_for_non_compliance=$stateParams.reason;
	}else{
		//$scope.taskCompletion.ttrn_performer_comments=$stateParams.comments;
	}

	$scope.today = function () {
		$scope.taskCompletion.ttrn_completed_date = new Date();
	};
	$scope.mindate = new Date();
	$scope.dateformat="dd-MM-yyyy";
	$scope.today();
	$scope.showcalendar = function ($event) {
		$scope.showdp = true;
	};
	$scope.showdp = false;


	//get repository List
	$scope.getComplianceRepository=function(){
		spinnerService.show('html5spinner');
		ApiCallFactory.getRepositoryList($scope.searchObj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
				$scope.originalExecutor   	= res.OrganogramFilter[0].Executor;
				$scope.originalEvaluator   	= res.OrganogramFilter[0].Evaluator;
				$scope.originalFunctionHead   = res.OrganogramFilter[0].FunctionHead;
				$scope.originalLegiList     = res.TasksFilter[0].Legislation;
				$scope.originalAllRuleList  = res.TasksFilter[0].Rule;
				$scope.OriginalEvents       = res.TasksFilter[0].Event;
				$scope.OriginalSubEvents    = res.TasksFilter[0].Sub_Event;

				$scope.addMoreItems();//To get first 500 records

				$scope.getExecutorEvaluatorList(); 
				$scope.getLegislationList(); 			
				$scope.getalllegiRuleList();		 
				$scope.getEventSubEvent(); 
				$scope.getSubEvent();
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get repository list====="+error);
		});
	};
	$scope.getComplianceRepository();

	$scope.searchObj.orga_id = 0;
	$scope.searchObj.loca_id = 0;
	$scope.searchObj.dept_id = 0;
	$scope.searchObj.evaluator_id = 0;
	$scope.searchObj.executor_id = 0;
	$scope.searchObj.cat_law_id = 0;
	$scope.searchObj.impact = null;
	$scope.searchObj.proh_pres = null;
	$scope.searchObj.type_of_task = null;
	$scope.searchObj.frequency = null;
	$scope.searchObj.task_status = null;
	$scope.searchObj.event = null;
	$scope.searchObj.sub_event = null;
	$scope.searchObj.task_id = null;

	if($scope.legiList.selected!=undefined){
		//	alert("hiii");
		$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
	}else{
		$scope.searchObj.legi_id=0;
	}


	if($scope.legiRuleL.selected!=undefined){

		//alert($scope.legiRuleL.selected.task_legi_id);
		$scope.searchObj.rule_id=$scope.legiRuleL.selected.task_rule_id;			
	}else{

		$scope.searchObj.rule_id=0;
	}

	$scope.globalTasksLength = 0;

	$scope.searchRepositoryList = function() {

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

		//	$scope.getDefaultTask();

		if($scope.searchObj.frequency != 'Event_Based'){
			spinnerService.show('html5spinner');
			$http({
				url : "./searchrepository",
				method : "post",
				params : {
					'data' : $scope.searchObj
				}
			})
			.then(
					function(result) {
						spinnerService.hide('html5spinner');
						$scope.repositoryList = result.data.repoData;	
						$scope.defaultTasks.length = 0;
						//alert(result.data.repoData.length);
						//$scope.AllTasksCount  = $scope.repositoryList.length;
						//alert(JSON.stringify($scope.repositoryList));																			
					},
					function(result) {
						spinnerService.hide('html5spinner');
					});
		}
		else{
			$scope.getAllTasks();
		}
	}

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

		//	alert(JSON.stringify($scope.UnitList));
	}

	//Get Executor/Evaluator list
	$scope.getExecutorEvaluatorList= function(){
		//alert("hiii");
		$scope.executorList=[];
		$scope.evaluatorList=[];
		$scope.funheadList = [];
		var allFunctionHead_array = [];


		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id == null && $scope.originalExecutor.length!=0){
			angular.forEach($scope.originalExecutor, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id)){

					$scope.executorList.push(item);
				}

			});

			angular.forEach($scope.originalEvaluator, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id)){
					$scope.evaluatorList.push(item);
				}

			});

			angular.forEach($scope.originalFunctionHead, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id)){
					if ($.inArray(item.orga_id, allFunctionHead_array)==-1) {
						allFunctionHead_array.push(item.orga_id);
						$scope.funheadList.push(item);
					}

				}

			});
		}
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id != null && $scope.searchObj.dept_id == null && $scope.originalExecutor.length!=0){
			angular.forEach($scope.originalExecutor, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id && item.loca_id == $scope.searchObj.loca_id)){
					$scope.executorList.push(item);
				}
			});

			angular.forEach($scope.originalEvaluator, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id && item.loca_id == $scope.searchObj.loca_id)){
					$scope.evaluatorList.push(item);
				}

			});

			angular.forEach($scope.originalFunctionHead, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id && item.loca_id == $scope.searchObj.loca_id)){
					$scope.funheadList.push(item);
				}

			});
		}

		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id != null && $scope.searchObj.dept_id != null && $scope.originalExecutor.length!=0){
			angular.forEach($scope.originalExecutor, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id && item.loca_id == $scope.searchObj.loca_id && item.dept_id == $scope.searchObj.dept_id)){
					$scope.executorList.push(item);
				}
			});

			angular.forEach($scope.originalEvaluator, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id && item.loca_id == $scope.searchObj.loca_id && item.dept_id == $scope.searchObj.dept_id)){
					$scope.evaluatorList.push(item);
				}

			});

			angular.forEach($scope.originalFunctionHead, function (item) {
				if((item.orga_id == $scope.searchObj.orga_id && item.loca_id == $scope.searchObj.loca_id && item.dept_id == $scope.searchObj.dept_id)){
					$scope.funheadList.push(item);
				}

			});
		}



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

	$scope.getLegislationList = function() {
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

	$scope.getLegislationListByOrga = function() {
		$scope.alllegiList=[];
		var alllegiList_array = [];
		if($scope.searchObj.orga_id!="" && $scope.originalLegiList.length!=0){
			angular.forEach($scope.originalLegiList, function (item) {
				if ((!$scope.searchObj.orga_id || item.orga_id == $scope.searchObj.orga_id)){
					if ($.inArray(item.task_legi_id, alllegiList_array)==-1) {
						alllegiList_array.push(item.task_legi_id);
						$scope.alllegiList.push(item);
					}
				}
			});
		};
	}


	$scope.getLegislationListByUnit = function() {
		$scope.alllegiList=[];
		var alllegiList_array = [];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.originalLegiList.length!=0){
			angular.forEach($scope.originalLegiList, function (item) {
				if ((!$scope.searchObj.orga_id || item.orga_id == $scope.searchObj.orga_id) && (!$scope.searchObj.loca_id || item.loca_id == $scope.searchObj.loca_id)){
					if ($.inArray(item.task_legi_id, alllegiList_array)==-1) {
						alllegiList_array.push(item.task_legi_id);
						$scope.alllegiList.push(item);
					}
				}
			});
		};
	}





	$scope.getEventSubEvent=function(){ 
		/*if($scope.legiList.selected!=undefined){
		$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
	}else{
		$scope.searchObj.legi_id=0;
	}
	if($scope.legiRuleL.selected!=undefined){
		$scope.searchObj.rule_id=$scope.legiRuleL.selected.task_rule_id;
	}else{
		$scope.searchObj.rule_id=0;
	}*/

		//$scope.event                = [];
		$scope.sub_event            = [];
		var event = [];
		/*if(	$scope.searchObj.legi_id!=0 && $scope.searchObj.legi_id!=undefined){
		angular.forEach($scope.OriginalEvents, function (item) {
			if((item.task_legi_id == $scope.searchObj.legi_id)){
				if(event.indexOf(item.task_Event) === -1){
					event.push(item.task_Event);
					$scope.event.push(item);
				}
			}
		});
	}*/

		$scope.event = [];
		var event_array = [];
		/*if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.OriginalEvents.length!=0){
		angular.forEach($scope.OriginalEvents, function (item) {
			console.log(" Event : "+item.task_Event);
			if ((!$scope.searchObj.orga_id || item.orga_id == $scope.searchObj.orga_id) 
					&& (!$scope.searchObj.loca_id || item.loca_id == $scope.searchObj.loca_id) 
					&& (!$scope.searchObj.dept_id || item.dept_id == $scope.searchObj.dept_id)
					&& (!$scope.searchObj.legi_id || item.task_legi_id == $scope.searchObj.legi_id)
					&& (!$scope.searchObj.rule_id || item.task_rule_id == $scope.searchObj.rule_id)){
				if ($.inArray(item.task_Event, event_array)==-1) {
					event_array.push(item.task_Event);
					$scope.event.push(item);
				}
			}
		});
	};*/

		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalLegiList.length!=0){
			angular.forEach($scope.OriginalEvents, function (item) {
				if ((!$scope.searchObj.orga_id || item.orga_id == $scope.searchObj.orga_id) && (!$scope.searchObj.loca_id || item.loca_id == $scope.searchObj.loca_id)&& (!$scope.searchObj.dept_id || item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.task_Event, event_array)==-1) {
						event_array.push(item.task_Event);
						$scope.event.push(item);
					}
				}
			});
		};
	}
	//Get Sub event as per event
	$scope.getSubEvent=function(){ 
		$scope.sub_event = [];
		var sub_event_array = [];
		/*angular.forEach($scope.OriginalAllTasks, function (item) {
			//console.log(item.task_Event+"  Sub Event "+item.task_Sub_Event);
			if(item.task_event == $scope.searchObj.event){ 
				if(sub_event.indexOf(item.task_sub_event) === -1){
					sub_event.push(item.task_sub_event);
					$scope.sub_event.push(item);
				}

			}
		});*/

		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.OriginalSubEvents.length!=0){
			angular.forEach($scope.OriginalSubEvents, function (item) {
				console.log(" Sub Event : "+item.task_Sub_Event);
				if ((!$scope.searchObj.orga_id || item.orga_id == $scope.searchObj.orga_id) 
						&& (!$scope.searchObj.loca_id || item.loca_id == $scope.searchObj.loca_id) 
						&& (!$scope.searchObj.dept_id || item.dept_id == $scope.searchObj.dept_id)
						&& (!$scope.searchObj.legi_id || item.task_legi_id == $scope.searchObj.legi_id)
						&& (!$scope.searchObj.rule_id || item.task_rule_id == $scope.searchObj.rule_id)
						&& (!$scope.searchObj.event || item.task_Event == $scope.searchObj.event)){
					if ($.inArray(item.task_Sub_Event, sub_event_array)==-1) {
						sub_event_array.push(item.task_Sub_Event);
						$scope.sub_event.push(item);
					}
				}
			});
		};
	}
	//$scope.searchRepo = false; //if true then scroll event not bind task
	$scope.searchRepository=function(){
		$scope.getComplianceRepository();
		$scope.maintainSearchHistory();
		$scope.showButton = false;
		$scope.getDefaultTask();
		//$scope.searchRepo = true;
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
	$scope.defaultTasks = [];
	$scope.defaultSearch = {};
	$scope.getDefaultTask = function(){
		$scope.defaultTasks = [];
		if($scope.searchObj.event!=undefined || $scope.searchObj.sub_event!=undefined){
			//console.log("IN CALL" +$scope.searchObj.event+"  "+$scope.searchObj.sub_event);
			ApiCallFactory.getDefaultTask($scope.defaultSearch).success(function(res,status){ 


				//Original Task list for loop
				angular.forEach($scope.OriginalAllTasks, function (data) { 

					angular.forEach(res, function (default_data) {  

						if( default_data.dtco_client_id== data.tmap_client_task_id && 
								(!$scope.searchObj.orga_id || data.orga_id === $scope.searchObj.orga_id) && 
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
								(!$scope.searchObj.sub_event || data.task_sub_event === $scope.searchObj.sub_event)){
							data.dtco_id = default_data.dtco_id;
							$scope.defaultTasks.push(data);

						}

					});


				});


			});

		}
	}//ENd fun

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
	$scope.errMessage = {};
	$scope.validateForm = function() {
		// $scope.errMessage = '';
		var curDate = new Date();
		var flag = 0;

		$scope.initiation_date = new Date($scope.defaultInitiation.date_of_initiation);
		if($scope.initiation_date==undefined && $scope.initiation_date ==null){
			$scope.errMessage.date = 'Select Date.';
			flag = 1;
		}else{
			$scope.errMessage.date = '';
		}
		if($scope.defaultInitiation.comments==null){
			flag = 1;
			$scope.errMessage.comments = 'Enter Comments.';
		}else{
			$scope.errMessage.comments = '';
		}
		if(flag==1){
			return false;
		}else{
			return true;
		}
	}


	$scope.initiateTask = function(formValid){
		/* var text = moment($scope.defaultInitiation.date_of_initiation).format('DD-MM-YYYY');; //$scope.defaultInitiation.date_of_initiation.toString();
	        alert($scope.defaultInitiation.date_of_initiation);
	        var comp = text.split('-');
	        var d = parseInt(comp[0], 10);
	        var m = parseInt(comp[1], 10);
	        var y = parseInt(comp[2], 10);
	        var date = new Date(y,m-1,d);
	        if (date.getFullYear() == y && date.getMonth() + 1 == m && date.getDate() == d) {
	            alert('Valid date');
	        } else {
	            alert('Invalid date');
	        } */
		if($scope.validateForm()&& $scope.defaultInitiation.tasks_list.length >0){
			$("#initiate").hide();
			$scope.defaultInitiation.date_of_initiation 	= moment($scope.defaultInitiation.date_of_initiation).format('DD-MM-YYYY');
			spinnerService.hide('html5spinner');
			// console.log(" In Valid "+JSON.stringify($scope.defaultInitiation));
			ApiCallFactory.initiateDefaultTask($scope.defaultInitiation).success(function(res,status){  

				//console.log("REs "+res.successDtcoId); //responseMessage
				if(res.length>=1){
					$scope.defaultTasks = [];
					toaster.success("Success", "Task initiated successfully.");
					$window.location.reload();
				}else{
					toaster.error("Error", "Please try again.");
				}

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

	$scope.searchTask = {}
	//$scope.searchTask.task_id = $scope.task_id;
	$scope.searchTaskById = function(){
		spinnerService.show('html5spinner');

		$http({
			url : "./searchrepository",
			method : "post",
			params : {
				'data' : $scope.searchObj
			}
		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.repositoryList = result.data.repoData;	
					$scope.totalTask = true;
					//alert(JSON.stringify($scope.repositoryList));																			
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

	}

	$scope.ExportExcels_Maintask = function() {
		$("#example tr td a").removeAttr("href");
		$("#example tr td a").css("color","black");
		$("#example").table2excel({						  
			filename: "Repository List" 
		}); 
	}

	$scope.getTaskHistoryList=function(task_id){																
		var obj={tmap_client_task_id:task_id};
		//	alert(JSON.stringify(obj));
		if(!angular.isUndefined(task_id) && task_id!=0){
			ApiCallFactory.getTasksHistory(obj).success(function(res,status){
				$scope.task_history = res.task_history;
				$scope.subTaskHistory = res.subTaskHistory;
				$('#changeTask').modal();
				$scope.editDate={
						role:Storage.get('userDetais.sess_role_id'),
				}
				//console.log("role "+$scope.editDates.role);
				$scope.getTaskDetails(task_id);
				//$scope.client_task_id = task_id;

				//$scope.task_history.documents = 
			}).error(function(error){
				console.log("task History====="+error);
			});
		}
	}

	$scope.getTaskDetails=function(task_id){ 
		//alert(task_id);
		if($scope.statusDetailsClick==0){ //If click status is 0 then only get details from DB
			//spinnerService.show('html5spinner');
			var obj={tmap_client_task_id:task_id};
			//alert(JSON.stringify(obj));
			if(!angular.isUndefined(task_id) && task_id!=0){
				ApiCallFactory.getTasksDetails(obj).success(function(res,status){
					spinnerService.hide('html5spinner');
					$scope.task_details = res;
					$scope.statusDetailsClick = 0;
				}).error(function(error){
					spinnerService.hide('html5spinner');
					console.log("task Details====="+error);
				});
			}
		}
	}

	$scope.showEventNotOccured = true;
	$scope.eventCheked = function (res){

		if(res){
			$scope.showEventNotOccured = false;
			$scope.taskCompletion.ttrn_event_not_occure="Yes";
			$scope.taskCompletion.ttrn_performer_comments = "NA";
			$scope.taskCompletion.ttrn_reason_for_non_compliance = "NA";
		}
		else{
			$scope.showEventNotOccured = true;
			$scope.taskCompletion.ttrn_event_not_occure="No";
		}
	}

	//get task history
	$scope.CompleteTaskPage=function(ttrn_id,document,comments,completedDate,status,reason,frequency,legalDueDate,clientTaskId){ //document contain 1 - mandatory doc and 0 - Non mandatory
		//alert(clientTaskId);									
		$scope.task.task_id=clientTaskId;									
		$scope.task.ttrn_id=ttrn_id;

		$scope.task_iiidddd = clientTaskId;
		$scope.ttrrrnnnn_id = ttrn_id;

		$scope.task.document=document;						
		$scope.taskCompletion.ttrn_performer_comments=comments;									
		$scope.status=status;
		console.log("staus is "+$scope.status);									
		$scope.frequency=frequency;
		console.log("frequency is"+$scope.frequency);							
		var legal =legalDueDate.split("-");
		var legalDueDate = new Date(legal[2], legal[1] - 1, legal[0]);
		$scope.legalDueDate= legalDueDate;

		console.log("Legal Due Date is : "+$scope.legalDueDate);	

		if($scope.taskCompletion.ttrn_reason_for_non_compliance == null || $scope.taskCompletion.ttrn_reason_for_non_compliance){
			$scope.taskCompletion.ttrn_reason_for_non_compliance = '';
		}else
			$scope.taskCompletion.ttrn_reason_for_non_compliance=reason;

		console.log("reason:" +$scope.taskCompletion.ttrn_reason_for_non_compliance);

		/*$scope.today = function () {
	        $scope.taskCompletion.ttrn_completed_date = new Date();
	    };*/
		if(completedDate!=0 ){
			//alert(completedDate);
			var compDate =completedDate.split("-");
			//alert("Completed Date : "+ new Date(compDate[2],compDate[1]-1,compDate[0]));
			$scope.taskCompletion.ttrn_completed_date = new Date(compDate[2],compDate[1]-1,compDate[0])
		}else{
			$scope.taskCompletion.ttrn_completed_date = new Date();
		}

		$("#taskCompletionLoader").modal('hide');
		$scope.checkAllMultipleCompletion();
		$("#completeTask").modal();
		$scope.gettaskformultiplecompletion($scope.task_iiidddd,$scope.ttrrrnnnn_id);
		//$window.open('completeTask', {'ttrn_id':ttrn_id,'task_id':clientTaskId,'document':document,'comments':comments,'completedDate':completedDate,'status':status,'reason':reason,'frequency':frequency,'legalDueDate':legalDueDate});
	};

	$scope.saveCompletion= function(formValid){

		if(formValid && !angular.isUndefined($scope.task.ttrn_id) && $scope.task.ttrn_id!=0){
			$("#taskCompletionLoader").modal();
			spinnerService.show('html5spinner');
			var obj= {ttrn_id:$scope.task.ttrn_id};
			$scope.task_ttrn_ids.push(obj);

			$scope.taskCompletion.ttrn_ids = $scope.task_ttrn_ids;
			$scope.taskCompletion.ttrn_completed_date = moment($scope.taskCompletion.ttrn_completed_date).format('DD-MM-YYYY');

			if($scope.taskCompletion.ttrn_reason_for_non_compliance == null || $scope.taskCompletion.ttrn_reason_for_non_compliance){
				$scope.taskCompletion.ttrn_reason_for_non_compliance = '';
			}else
				$scope.taskCompletion.ttrn_reason_for_non_compliance=$scope.taskCompletion.ttrn_reason_for_non_compliance;

			//$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance;
			//console.log("check box value "+$scope.showEventNotOccured);
			if($scope.showEventNotOccured==true){
				$scope.taskCompletion.ttrn_event_not_occure="No";
			}
			//	$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance; 
			$scope.stringifyTaskCompletion= JSON.stringify($scope.taskCompletion)
			//DataFactory.setShowLoader(true);
			var formData = new FormData();
			if(!angular.isUndefined($scope.proofCompliance.ttrn_proof_of_compliance)){
				for (var i=0; i<$scope.proofCompliance.ttrn_proof_of_compliance.length; i++) {
					formData.append("ttrn_proof_of_compliance", $scope.proofCompliance.ttrn_proof_of_compliance[i]);
				}
			}  


			///formData.append("ttrn_proof_of_compliance",$scope.proofCompliance.ttrn_proof_of_compliance);
			formData.append("jsonString",$scope.stringifyTaskCompletion);

			ApiCallFactory.saveTasksCompletion(formData).success(function(res,status){
				//alert($scope.task.task_id);
				spinnerService.hide('html5spinner');
				if(res.responseMessage=='Success'){
					toaster.success("Success", "Task completed successfully.");
					$('#completeTask').modal('hide');	
					$("#taskCompletionLoader").modal('hide');
					$scope.task_ttrn_ids = [];
					$scope.taskCompletion = {};
					$scope.getTaskHistoryList($scope.task.task_id);
					$scope.getTaskDetails($scope.task.task_id);

					$("#changeTask").modal();

					//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
				}else if(res.responseMessage == 'Invalid File Type'){
					toaster.error("Invalid File Type");
					$("#taskCompletionLoader").modal('hide');
				}else{
					toaster.error("Failed", "Please try again.");
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				$("#taskCompletionLoader").modal('hide');
				console.log("task Completion====="+error);
			});


		}
	}

	$scope.deleteTask=function(ttrn_id,client_task_id,ev,index){
		/*  var confirm = $mdDialog.confirm()
	          .title('Are you sure you want to delete the task?')
	          .targetEvent(ev)
	          .ok('Yes')
	          .cancel('NO');*/
		var isConfirmed = confirm("Are you sure to delete this record ?");

		if (isConfirmed) {
			$scope.taskCompletion.ttrn_id			  = ttrn_id;
			$scope.taskCompletion.ttrn_client_task_id = client_task_id;
			ApiCallFactory.deleteTaskHistory($scope.taskCompletion).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					toaster.success("Success", "Task deleted successfully.");
					$scope.task_history.splice(index,1);
				}else{
					toaster.error("Faild", "Please try again");
				}	
			}).error(function(error){
				console.log("Error while deleting the task====="+error);
			});
		} else {
			return false;
		}
	}

	$scope.nonComplianceReason={
			ttrn_id : 0,
			ttrn_reason_for_non_compliance : "",
	}
	$scope.reasonForNonCompliance=function(data){
		//$scope.taskCompletion.ttrn_id=ttrn_id;
		$scope.nonComplianceReason.ttrn_id = data.ttrn_id;
		//console.log("TTRN ID "+$scope.nonComplianceReason.ttrn_id);

	}
	$scope.sendReasonForNonCompliance=function(formValid){
		//console.log("ID for noncompliance: "+$scope.nonComplianceReason.ttrn_id);
		//console.log("Comment for noncompliance: "+$scope.nonComplianceReason.ttrn_reason_for_non_compliance);
		if(formValid){
			ApiCallFactory.updateTaskReasonForNonCompliance($scope.nonComplianceReason).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					//$scope.reOpen.reopen_comment="";	
					$('#resonForNonCompliance').modal("hide");
					toaster.success("Success", "Reason For NonCompliance submitted successfully.");
					//$window.location.reload();
				}

			}).error(function(error){
				console.log("Error while approving the task====="+error);
			});
		}
	}

	//get ttrn_id and reopening comments 	
	$scope.reOpen={
			ttrn_id : 0,
			reopen_comment : "",
	}
	$scope.reopenTask=function(data){
		//$scope.taskCompletion.ttrn_id=ttrn_id;
		$scope.reOpen.ttrn_id = data.ttrn_id;
		$scope.reOpen.client_task_id = data.client_task_id;

		//console.log("TTRN ID "+$scope.taskCompletion.ttrn_id);

	}

	//Reopen Task
	$scope.sendReOpenTask=function(formValid){
		//console.log("ID for reopen: "+$scope.reOpen.ttrn_id);
		//console.log("Comment for reopen: "+$scope.reOpen.reopen_comment);
		if(formValid){
			ApiCallFactory.reopenTask($scope.reOpen).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					//$scope.reOpen.reopen_comment="";	
					$('#reOpenTask').modal("hide");
					$scope.getTaskHistoryList($scope.reOpen.client_task_id);
					$scope.getTaskDetails($scope.reOpen.client_task_id);
					//	$window.location.reload();
				}

			}).error(function(error){
				console.log("Error while approving the task====="+error);
			});
		}
	}

	//Approve task
	$scope.approveTask=function(ttrn_id, client_task_id){
		// console.log(task_id);
		$scope.taskCompletion.ttrn_id=ttrn_id;	 
		ApiCallFactory.approveTask($scope.taskCompletion).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				toaster.success("Success", "Task approved successfully.");
				//$window.location.reload();
				$scope.getTaskHistoryList(client_task_id);
				$scope.getTaskDetails(client_task_id);
			}	
		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});

	}

	$scope.tmapObj = {};
	$scope.NtmapObj = {};
	$scope.errMessage = {};
	$scope.getDates =function(){

		//var legal = new Date($scope.tmapObj.ttrn_legal_due_date);
		if($scope.tmapObj.ttrn_prior_days_buffer<0)
			$scope.tmapObj.ttrn_prior_days_buffer = 0;

		$scope.unit_date = new Date($scope.lega_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.func_date = new Date($scope.unit_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.eval_date = new Date($scope.func_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.exec_date = new Date($scope.eval_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);


	}

	$scope.editDates = function(data){
		//console.log("DATA "+JSON.stringify(data));
		//	alert(JSON.stringify(data));
		$scope.NtmapObj.ttrn_client_task_id = data.client_task_id;
		$scope.NtmapObj.ttrn_id = data.ttrn_id;
		$scope.NtmapObj.ttrn_legal_date = data.ttrn_legal_due_date;
		$scope.NtmapObj.ttrn_unit_date = data.ttrn_uh_due_date;
		$scope.NtmapObj.ttrn_function_date = data.ttrn_fh_due_date;
		$scope.NtmapObj.ttrn_evaluator_date = data.ttrn_rw_due_date;
		$scope.NtmapObj.ttrn_executor_date = data.ttrn_pr_due_date;
		$scope.NtmapObj.ttrn_performer_name = data.ttrn_performer_name;
		$scope.NtmapObj.user_email = data.user_email;


		$('#editConfiguration').modal();

	}

	$scope.tmapObj ={};
	$scope.updateDates = function(formValid){
		//console.log("FORM "+formValid);
		//console.log("Dates "+$scope.validateDates());
		if($scope.validateDates() && formValid){
			$scope.tmapObj.ttrn_id                = $scope.NtmapObj.ttrn_id;
			$scope.tmapObj.ttrn_legal_due_date 	= moment($scope.lega_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_uh_due_date 		= moment($scope.unit_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_fh_due_date 		= moment($scope.func_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_rw_due_date 		= moment($scope.eval_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_pr_due_date 	= moment($scope.exec_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_performer_name  = $scope.NtmapObj.ttrn_performer_name;
			$scope.tmapObj.user_email =  $scope.NtmapObj.user_email

			// console.log("JSON "+JSON.stringify($scope.tmapObj));

			//Update Task Configuration dates
			ApiCallFactory.updateTasksConfiguration($scope.tmapObj).success(function(res,status){
				//alert(  $scope.NtmapObj.ttrn_client_task_id);
				spinnerService.hide('html5spinner');
				if(status === 200){
					$('#editConfiguration').modal("hide");
					toaster.success("Success", "Task dates updated successfully.");
					$scope.getTaskHistoryList($scope.NtmapObj.ttrn_client_task_id);
					$scope.getTaskDetails($scope.NtmapObj.ttrn_client_task_id);
					$("#changeTask").modal();
					//	$window.location.reload();
				}else{
					toaster.error("Failed", "Error in dates.");
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("update Configuration===="+error);
			});

		}


	}


	$scope.validateSubTaskDates = function() {
		// $scope.errMessage = '';
		$scope.curDate = new Date();
		var flag = 0;
		$scope.ttrn_completed_date = new Date($scope.subTaskCompletion.ttrn_completed_date);
		$scope.ttrn_next_examination_date = new Date($scope.subTaskCompletion.ttrn_next_examination_date);

		if($scope.ttrn_completed_date >= $scope.curDate){ 
			$scope.errMessage.completed_date = 'Completed date should not be greater than todays  date.';
			flag = 1;
			//return false;
		}else{
			$scope.errMessage.completed_date = '';
		}

		if($scope.ttrn_completed_date >= $scope.ttrn_next_examination_date){ 
			$scope.errMessage.next_examination_date = 'Next Examination date must be greater than completed date.';
			flag = 1;
			//return false;
		}else{
			$scope.errMessage.next_examination_date = '';
		}

		//If all date valid then set true
		if(flag==0){
			$scope.tmapObj.validate_dates = "TRUE";
			return true;
		}else{
			$scope.tmapObj.validate_dates = null;
			return false;
		}

	}
	$scope.subTaskComplete= function(data){

		$scope.subTaskCompletion.ttrn_id = data.ttrn_id;
		$scope.client_task_id = data.client_task_id;
		$scope.ttrn_task_status = data.ttrn_task_status;
		$scope.ttrn_document = data.ttrn_document;
		$scope.subTaskCompletion.ttrn_performer_comments = data.ttrn_performer_comments;
		$scope.subTaskCompletion.ttrn_reason_for_non_compliance = data.ttrn_reason_for_non_compliance;
		if($scope.ttrn_task_status == "Completed"){
			var from = data.ttrn_completed_date.split("-");
			var completedDate = new Date(from[2], from[1] - 1, from[0]);
			$scope.subTaskCompletion.ttrn_completed_date = completedDate;

			/*var fromm = data.ttrn_next_examination_date.split("-");
				 var completedDate = new Date(fromm[2], fromm[1] - 1, fromm[0]);
				 $scope.subTaskCompletion.ttrn_next_examination_date = completedDate;*/
		}else{
			$scope.subTaskCompletion.ttrn_completed_date = new Date();
			//	$scope.subTaskCompletion.ttrn_next_examination_date = new Date();
		} 

	}


	$scope.saveSubTaskCompletion= function(formValid){	
		//alert("In Complete sub task");
		if(formValid){

			$("#subTaskCompletionLoader").modal();
			//spinnerService.show('html5spinner');
			var obj= {ttrn_sub_id:$scope.subTaskCompletion.ttrn_id};
			$scope.task_ttrn_ids.push(obj);

			if($scope.subTaskCompletion.ttrn_reason_for_non_compliance == undefined || $scope.subTaskCompletion.ttrn_reason_for_non_compliance == ""){
				$scope.subTaskCompletion.ttrn_reason_for_non_compliance = "";
			}else
				$scope.subTaskCompletion.ttrn_reason_for_non_compliance = $scope.subTaskCompletion.ttrn_reason_for_non_compliance;

			$scope.subTaskCompletion.ttrn_sub_ids = $scope.task_ttrn_ids;
			$scope.subTaskCompletion.ttrn_performer_comments = $scope.subTaskCompletion.ttrn_performer_comments;
			//$scope.subTaskCompletion.ttrn_reason_for_non_compliance = $scope.subTaskCompletion.ttrn_reason_for_non_compliance;
			$scope.subTaskCompletion.ttrn_completed_date = moment($scope.subTaskCompletion.ttrn_completed_date).format('DD-MM-YYYY');
			//$scope.subTaskCompletion.ttrn_next_examination_date = moment($scope.subTaskCompletion.ttrn_next_examination_date).format('DD-MM-YYYY');
			//console.log("check box value "+$scope.showEventNotOccured);

			//	$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance; 
			$scope.stringifyTaskCompletion= JSON.stringify($scope.subTaskCompletion)
			var formData = new FormData();
			if(!angular.isUndefined($scope.proofCompliance.ttrn_proof_of_compliance)){
				for (var i=0; i<$scope.proofCompliance.ttrn_proof_of_compliance.length; i++) {
					formData.append("ttrn_proof_of_compliance", $scope.proofCompliance.ttrn_proof_of_compliance[i]);
				}
			}  

			///formData.append("ttrn_proof_of_compliance",$scope.proofCompliance.ttrn_proof_of_compliance);
			formData.append("jsonString",$scope.stringifyTaskCompletion);

			ApiCallFactory.savesubtaskcompletion(formData).success(function(res,status){
				//spinnerService.hide('html5spinner');
				if(res.responseMessage=='Success'){
					toaster.success("Success", "Task completed successfully.");
					$('#SubTaskCompletion').modal("hide");
					$("#subTaskCompletionLoader").modal('hide');
					//alert($scope.client_task_id);
					//	$("#changeTask").modal();
					$scope.getTaskHistoryList($scope.client_task_id);
					$scope.getTaskDetails($scope.client_task_id);
					$scope.task_ttrn_ids = [];

					$('#ttrn_proof_of_compliance').val('');

					//$window.location.reload();
					//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
				}else{
					toaster.error("Failed", "Please try again.");
					$("#subTaskCompletionLoader").modal('hide');
					$scope.task_ttrn_ids = [];
					$('#ttrn_proof_of_compliance').val('');
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				$("#subTaskCompletionLoader").modal('hide');
				console.log("task Completion====="+error);
				$('#ttrn_proof_of_compliance').val('');
			});


		}
	}


	$scope.downloadProof = function(udoc_id,client_task_id){

		var obj={doc_id:udoc_id};
		ApiCallFactory.downloadProofOfCompliance(obj).success(function(res,status){
			//spinnerService.hide('html5spinner');
			console.log("responseMessage:" +res[0].responseMessage);
			if(res[0].responseMessage == "Failed") {
				toaster.error("Failed", "This file contains malicious data which can damage your machine.The file will not be downloaded and will be deleted from the System.");
				$scope.deleteDocument.udoc_id			  = udoc_id;
				ApiCallFactory.deleteTaskDocument($scope.deleteDocument).success(function(res,status){
					if(status === 200 && res.responseMessage == "Success"){
						/*toaster.success("Success", "Document deleted successfully.");*/
						$scope.getTaskHistoryList(client_task_id);
						$scope.getTaskDetails(client_task_id);
					}else{
						toaster.error("Failed", "Please try again");
					}	
				}).error(function(error){
					console.log("Error while deleting the task====="+error);
				});
			} else if(res[0].responseMessage == "File Not Found") {
				toaster.error("File Not Found");
			} else {				
				$window.location = "./downloadProofOfCompliance?udoc_id="+udoc_id;
				toaster.success("Success", "Document downloaded successfully.");

			}
		}).error(function(error){
			toaster.error("Failed", "Please try again");
		});

		//  $window.location = "./downloadProofOfCompliance?udoc_id="+udoc_id;
		// $state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});


	}

	/* Date Code start*/

	$scope.inlineOptions = {
			customClass: getDayClass,
			minDate: new Date(),
			showWeeks: true
	};

	// Disable weekend selection
	function disabled(data) {
		var date = data.date,
		mode = data.mode;
		return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
	}

	/*$scope.toggleMin = function() {
	    $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
	    $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
	  };

	  $scope.toggleMin();*/

	/* Open */
	$scope.open_legal = function() {
		$scope.popup_legal.opened = true;
	};

	$scope.open_unit = function() {
		$scope.popup_unit.opened = true;
	};

	$scope.open_func = function() {
		$scope.popup_func.opened = true;
	};

	$scope.open_eval = function() {
		$scope.popup_eval.opened = true;
	};

	$scope.open_exec = function() {
		$scope.popup_exec.opened = true;
	};
	$scope.open_first = function() {
		$scope.popup_first.opened = true;
	};

	$scope.open_second = function() {
		$scope.popup_second.opened = true; 
	};

	$scope.open_third = function() {
		$scope.popup_third.opened = true;
	};
	/* END Open */

	/* Close */
	$scope.popup_legal = {
			opened: false
	};

	$scope.popup_unit = {
			opened: false
	};

	$scope.popup_func = {
			opened: false
	};
	$scope.popup_eval = {
			opened: false
	};
	$scope.popup_exec = {
			opened: false
	};
	$scope.popup_first = {
			opened: false
	};
	$scope.popup_second = {
			opened: false
	};
	$scope.popup_third = {
			opened: false
	};
	/*  $scope.setDate = function(year, month, day) {
	    $scope.dt = new Date(year, month, day);
	  };*/
	/* End Close */

	$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$scope.format = 'dd-MM-yyyy'; //$scope.formats[0];
	$scope.altInputFormats = ['M!/d!/yyyy'];



	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);
	var afterTomorrow = new Date();
	afterTomorrow.setDate(tomorrow.getDate() + 1);
	$scope.events = [
		{
			date: tomorrow,
			status: 'full'
		},
		{
			date: afterTomorrow,
			status: 'partially'
		}
		];

	function getDayClass(data) {
		var date = data.date,
		mode = data.mode;
		if (mode === 'day') {
			var dayToCheck = new Date(date).setHours(0,0,0,0);

			for (var i = 0; i < $scope.events.length; i++) {
				var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

				if (dayToCheck === currentDay) {
					return $scope.events[i].status;
				}
			}
		}

		return '';
	}

	/*Date code end*/

	$scope.validateDates = function() {
		// $scope.errMessage = '';
		var curDate = new Date();
		var flag = 0;
		$scope.ttrn_lh_due_date = new Date($scope.lega_date);
		$scope.ttrn_uh_due_date = new Date($scope.unit_date);
		$scope.ttrn_fh_due_date = new Date($scope.func_date);
		$scope.ttrn_rw_due_date = new Date($scope.eval_date);
		$scope.ttrn_pr_due_date = new Date($scope.exec_date);

		if($scope.lega_date==undefined){
			$scope.errMessage.lh_date = 'Select Legal date.';
			flag = 1;
		}else{
			$scope.errMessage.lh_date = '';
		}

		if($scope.ttrn_uh_due_date > $scope.ttrn_lh_due_date){ 
			$scope.errMessage.uh_date = 'Unit head date must be less than OR equal to Legal date.';
			flag = 1;
			//return false;
		}else{
			$scope.errMessage.uh_date = '';
		}

		if($scope.ttrn_fh_due_date > $scope.ttrn_uh_due_date){ 
			$scope.errMessage.fh_date = 'Function head date must be less than OR equal to Unit head date.';
			flag = 1;
			//return false;
		}else{
			$scope.errMessage.fh_date = '';
		}
		if($scope.ttrn_rw_due_date > $scope.ttrn_fh_due_date){ 
			$scope.errMessage.rw_date = 'Evaluator date must be less than OR equal to Function head date.';
			flag = 1;
			//return false;
		}else{
			$scope.errMessage.rw_date = '';
		}
		if($scope.ttrn_pr_due_date > $scope.ttrn_rw_due_date){ 
			$scope.errMessage.pr_date = 'Executor date must be less than OR equal to Evaluator date.';
			flag = 1;
			//return false;
		}else{
			$scope.errMessage.pr_date = '';
		}

		//If all date valid then set true
		if(flag==0){
			$scope.tmapObj.validate_dates = "TRUE";
			return true;
		}else{
			$scope.tmapObj.validate_dates = null;
			return false;
		}

	}


	// New Repository code
	$scope.getEntityList = function() {
		spinnerService.show('html5spinner');		
		$http({
			url : "./getentity",
			method : "get",				
		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.entityList = result.data;																							
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});
	}

	$scope.getEntityList();

	$scope.exportData = function() {

		if($scope.legiRuleL.selected != undefined){
			$scope.searchObj.rule_id = $scope.legiRuleL.selected.task_rule_id;			
		}else{		 
			$scope.searchObj.rule_id = 0;
		}

		if($scope.legiList.selected!=undefined){
			$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
		}else{
			$scope.searchObj.legi_id=0;
		}

		ApiCallFactory.getExportDataById($scope.searchObj).success(function(res,status){
			//spinnerService.show('html5spinner');
			if(status==200){

				$window.location = "./getExportData";
				$state.transitionTo('showComplianceRepository');

			}
		}).error(function(error){

			spinnerService.hide('html5spinner');
			//console.log("get repository list====="+error);
		});
	}



	$scope.getUnitListByEntity = function(entity) {
		spinnerService.show('html5spinner');
		$http({
			url : "./getunit",
			method : "get",
			params : {
				'entity_id' : entity
			}
		}).then(function(result) {
			spinnerService.hide('html5spinner');
			$scope.unitList = result.data;
		}, function(result) {
			spinnerService.hide('html5spinner');
		});

	}

	/*$scope.functionList = [];
		var dept_array = [];*/
	$scope.getFunctionListByUnit = function(unitList,orga_id) {
		$scope.functionList = [];
		var dept_array = [];
		spinnerService.show('html5spinner');
		$http({
			url : "./getFunction",
			method : "get",
			params : {
				'unit_id' : unitList,
				'orga_id' : orga_id
			}
		}).then(function(result) {
			spinnerService.hide('html5spinner');

			$scope.OriginalfunctionList = result.data;

			angular.forEach($scope.OriginalfunctionList, function (item) {
				if ($.inArray(item.dept_id, dept_array)==-1) {
					dept_array.push(item.dept_id);
					$scope.functionList.push(item);
				}
			});

		}, function(result) {
			spinnerService.hide('html5spinner');
		});

	}

	$scope.getCategoryLaw = function(unitList) {	 		 
		spinnerService.show('html5spinner');		
		$http({
			url : "./getCategory",
			method : "get",	

		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.categoryList = result.data;																							
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

	}
	$scope.getCategoryLaw();

	$scope.getTypeOfTask = function(unitList) {	 		 
		spinnerService.show('html5spinner');		
		$http({
			url : "./getTypeOfTask",
			method : "get",	

		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.typeOfTaskList = result.data;																							
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

	}
	$scope.getTypeOfTask();

	$scope.getFrequencyList = function(unitList) {	 		 
		spinnerService.show('html5spinner');		
		$http({
			url : "./getFrequencyList",
			method : "get",	

		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.frequencyList = result.data;																							
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

	}
	$scope.getFrequencyList();

	/*	 $scope.exportData = function() {

	    	 ApiCallFactory.getExportDataById($scope.searchObj).success(function(res,status){
	  			//spinnerService.show('html5spinner');
	  			if(status==200){  			
	  				//$scope.downloadData();
	  				toaster.success("File Exported Successfully");

	  			}
	  		}).error(function(error){
	  			spinnerService.hide('html5spinner');
	  			//console.log("get repository list====="+error);
	  		});
	       }

	   $scope.downloadData = function(){

    	   ApiCallFactory.getDownloadData().success(function(res,status){
	  			//spinnerService.show('html5spinner');
	  			if(status==200){
	  				toaster.success("File Exported Successfully");

	  			}
	  		}).error(function(error){
	  			spinnerService.hide('html5spinner');
	  			//console.log("get repository list====="+error);
	  		});    	   
       }
	 */
	/*$scope.download3 = function(){
			alert("hii");
			$window.location = "./getExportData";
			$state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});
		}
	 */
	/*$scope.exportData = function() {

			ApiCallFactory.getExportDataById($scope.searchObj).success(function(res,status){
				//spinnerService.show('html5spinner');
				if(status==200){  			
					//$scope.downloadData();

					$window.location = "./getExportData";
					//$state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});

				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				//console.log("get repository list====="+error);
			});
		}
	 */
	$scope.updateCompletion= function(formValid){

		if(formValid && !angular.isUndefined($scope.task.ttrn_id) && $scope.task.ttrn_id!=0){
			spinnerService.show('html5spinner');
			var obj= {ttrn_id:$scope.task.ttrn_id};
			$scope.task_ttrn_ids.push(obj);

			$scope.taskCompletion.ttrn_ids = $scope.task_ttrn_ids;
			$scope.taskCompletion.ttrn_completed_date = moment($scope.taskCompletion.ttrn_completed_date).format('DD-MM-YYYY');
			$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance;
			//console.log("check box value "+$scope.showEventNotOccured);
			if($scope.showEventNotOccured==true){
				$scope.taskCompletion.ttrn_event_not_occure="No";
			}
			//	$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance; 
			$scope.stringifyTaskCompletion= JSON.stringify($scope.taskCompletion)
			//DataFactory.setShowLoader(true);
			var formData = new FormData();
			if(!angular.isUndefined($scope.proofCompliance.ttrn_proof_of_compliance)){
				for (var i=0; i<$scope.proofCompliance.ttrn_proof_of_compliance.length; i++) {
					formData.append("ttrn_proof_of_compliance", $scope.proofCompliance.ttrn_proof_of_compliance[i]);
				}
			}  


			///formData.append("ttrn_proof_of_compliance",$scope.proofCompliance.ttrn_proof_of_compliance);
			formData.append("jsonString",$scope.stringifyTaskCompletion);

			ApiCallFactory.updateTasksCompletion(formData).success(function(res,status){
				//alert($scope.task.task_id);
				spinnerService.hide('html5spinner');
				if(res.responseMessage=='Success'){ 
					toaster.success("Success", "Task completed successfully.");
					$('#completeTask').modal('hide');	
					$scope.task_ttrn_ids = [];
					$('#ttrn_proof_of_compliance').val('');
					$scope.getTaskHistoryList($scope.task.task_id);
					$scope.getTaskDetails($scope.task.task_id);

					$("#changeTask").modal();

					//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
				}else{
					toaster.error("Failed", "Please try again.");
					$('#ttrn_proof_of_compliance').val('');
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("task Completion====="+error);
			});


		}
	}


	$scope.addDocument = function(){		
		//alert(termFileCount);
		$scope.addDoc = true;
		$("#filesContainer").append('<div class="col-md-12" id="termFile'+termFileCount+'">'
				+'<div class="form-group">'
				+'<label class="col-md-3 control-label">Document:</label>'
				+'<div class="col-md-7">'
				+'<input type="file" name="comp_doc" class="file-loading" />'
				+'</div>'
				+'<div class="col-md-2" style="text-align: right; cursor: pointer;">'
				+'<i class="glyphicon glyphicon-remove-circle"	onclick="deleteTermRow('+termFileCount+');" style="color: red;" title="Delete"></i>'
				+'</div>'
				+'</div>'
				+'</div>'
		);
		termFileCount++;

	}

	$scope.deleteTaskDocument=function(udoc_id,client_task_id,ev,index){
		/* var confirm = $mdDialog.confirm()
			          .title('Are you sure you want to delete the document?')
			          .targetEvent(ev)
			          .ok('Yes')
			          .cancel('No');*/
		var isConfirmed = confirm("Are you sure you want to delete this record ?");
		//  $mdDialog.show(confirm).then(function() {
		if (isConfirmed) {
			$scope.deleteDocument.udoc_id			  = udoc_id;
			ApiCallFactory.deleteTaskDocument($scope.deleteDocument).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					toaster.success("Success", "Document deleted successfully.");
					$scope.getTaskHistoryList(client_task_id);
					$scope.getTaskDetails(client_task_id);
				}else{
					toaster.error("Failed", "Please try again");
				}	
			}).error(function(error){
				console.log("Error while deleting the task====="+error);
			});
		}
		//});
	}

	//Multiple task completion

	$scope.gettaskformultiplecompletion = function (task_id,ttrn_id) {
		$scope.multipleCompletion.tmap_client_task_id = task_id;
		$scope.multipleCompletion.ttrn_id = ttrn_id;
		spinnerService.show('html5spinner');

		ApiCallFactory.gettaskformultiplecompletion($scope.multipleCompletion).success(function (res, status) {
			spinnerService.hide('html5spinner');
			if (status === 200 && res.response == "success") {
				$scope.multipleTasks = res.task_list;
				$scope.multipleLength = $scope.multipleTasks.length;
				//alert("length : "+$scope.multipleLength);
			} else {
				toaster.error("Failed", "Error.");
			}
		}).error(function (error) {
			spinnerService.hide('html5spinner');
			console.log("update Configuration====" + error);
		});
	}

	if (!angular.isUndefined($stateParams.ttrn_id) && $stateParams.ttrn_id != 0 && !angular.isUndefined($stateParams.task_id) && $stateParams.task_id != 0) {
		$scope.gettaskformultiplecompletion();
	}


	//get check box ID
	$scope.getCheckTaskIdMultipleCompletion = function (selectId, task_id) {
		if (selectId == true) {
			var obj = {
					ttrn_id: task_id,
			}
			$scope.task_ttrn_ids.push(obj);
		} else {
			var index = 0;
			angular.forEach($scope.task_ttrn_ids, function (item) {
				if (item.ttrn_id == task_id) {
					$scope.task_ttrn_ids.splice(index, 1);
				}
				index++;
			});
		}
		if ($scope.task_ttrn_ids.length == $scope.multipleTasks.length) {
			$scope.selectedAll = true;
		} else {
			$scope.selectedAll = false;
		}

	}


	//select all check box
	$scope.checkAllMultipleCompletion = function () {
		$scope.task_ttrn_ids = [];
		if ($scope.selectedAll) {
			$scope.selectedAll = true;
		} else {
			$scope.selectedAll = false;
		}

		angular.forEach($scope.multipleTasks, function (item) {
			item.SelectedTaskId = $scope.selectedAll;
			if ($scope.selectedAll) {
				var obj = {
						ttrn_id: item.ttrn_id
						//tmap_lexcare_task_id:item.lexcare_task_id
				}
				$scope.task_ttrn_ids.push(obj);
			} else {
				$scope.task_ttrn_ids = [];
			}
		});
	};
	// End Multiple task completion code

	/* Start Sub Task Code*/
	$scope.approveSubTask = function(history){
		// console.log(task_id);
		$scope.subTaskCompletion.ttrn_id=history.ttrn_id; 
		ApiCallFactory.approveSubTask($scope.subTaskCompletion).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				toaster.success("Success", "Task approved successfully.");
				//$window.location.reload();
				$scope.getTaskHistoryList(history.client_task_id);
				$scope.getTaskDetails(history.client_task_id);
			}else {
				alert(res.responseMessage);
			}	
		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});
	}

	$scope.reopenSubTask=function(data){
		//$scope.taskCompletion.ttrn_id=ttrn_id;
		$scope.reOpen.ttrn_id = data.ttrn_sub_task_id;
		$scope.reOpen.client_task_id = data.client_task_id;				

	}

	$scope.sendReOpenSubTask=function(formValid){
		//console.log("ID for reopen: "+$scope.reOpen.ttrn_id);
		//console.log("Comment for reopen: "+$scope.reOpen.reopen_comment);
		if(formValid){
			ApiCallFactory.reopenSubTask($scope.reOpen).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					//$scope.reOpen.reopen_comment="";	
					$('#reOpenSubTask').modal("hide");
					$scope.getTaskHistoryList($scope.reOpen.client_task_id);
					$scope.getTaskDetails($scope.reOpen.client_task_id);
					//	$window.location.reload();
				}

			}).error(function(error){
				console.log("Error while approving the task====="+error);
			});
		}
	}


	$scope.editSubTaskDates = function(data){
		//console.log("DATA "+JSON.stringify(data));
		//	alert(JSON.stringify(data));
		$scope.NtmapObj.ttrn_client_task_id = data.client_task_id;
		$scope.NtmapObj.ttrn_id = data.ttrn_id;
		$scope.NtmapObj.ttrn_sub_id = data.ttrn_sub_task_id;			  
		$scope.NtmapObj.ttrn_legal_date = data.ttrn_legal_due_date;
		$scope.NtmapObj.ttrn_unit_date = data.ttrn_uh_due_date;
		$scope.NtmapObj.ttrn_function_date = data.ttrn_fh_due_date;
		$scope.NtmapObj.ttrn_evaluator_date = data.ttrn_rw_due_date;
		$scope.NtmapObj.ttrn_executor_date = data.ttrn_pr_due_date;
		$scope.NtmapObj.ttrn_performer_name = data.ttrn_performer_name;
		$scope.NtmapObj.user_email = data.user_email;				

		$('#editSubTaskConfiguration').modal();

	}


	$scope.updateSubTaskDates = function(formValid){
		//console.log("FORM "+formValid);
		//console.log("Dates "+$scope.validateDates());
		if($scope.validateDates() && formValid){
			$scope.tmapObj.ttrn_id                = $scope.NtmapObj.ttrn_id;
			$scope.tmapObj.ttrn_sub_id            = $scope.NtmapObj.ttrn_sub_id;
			$scope.tmapObj.ttrn_legal_due_date 	= moment($scope.lega_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_uh_due_date 		= moment($scope.unit_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_fh_due_date 		= moment($scope.func_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_rw_due_date 		= moment($scope.eval_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_pr_due_date 	= moment($scope.exec_date).format('DD-MM-YYYY');
			$scope.tmapObj.ttrn_performer_name  = $scope.NtmapObj.ttrn_performer_name;
			$scope.tmapObj.user_email =  $scope.NtmapObj.user_email

			// console.log("JSON "+JSON.stringify($scope.tmapObj));

			//Update Task Configuration dates
			ApiCallFactory.updateSubTasksConfiguration($scope.tmapObj).success(function(res,status){
				//alert(  $scope.NtmapObj.ttrn_client_task_id);
				spinnerService.hide('html5spinner');
				if(status === 200){
					$('#editSubTaskConfiguration').modal("hide");
					toaster.success("Success", "Task dates updated successfully.");
					$scope.getTaskHistoryList($scope.NtmapObj.ttrn_client_task_id);
					$scope.getTaskDetails($scope.NtmapObj.ttrn_client_task_id);
					$("#changeTask").modal();
					//	$window.location.reload();
				}else{
					toaster.error("Failed", "Error in dates.");
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("update Configuration===="+error);
			});

		}


	}

	$scope.downloadSubtaskDocument = function(udoc_sub_id){
		$window.location = "./downloadSubtaskDocument?udoc_sub_id="+udoc_sub_id;
		//$state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});
	}

	$scope.deleteSubTaskDocument=function(udoc_sub_task_id,client_task_id,ev,index){
		/* var confirm = $mdDialog.confirm()
						          .title('Are you sure you want to delete the document?')
						          .targetEvent(ev)
						          .ok('Yes')
						          .cancel('No');*/
		var isConfirmed = confirm("Are you sure you want to delete this document ?");
		//  $mdDialog.show(confirm).then(function() {
		if (isConfirmed) {
			$scope.deleteDocument.udoc_sub_task_id = udoc_sub_task_id;
			ApiCallFactory.deleteSubTaskDocument($scope.deleteDocument).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					toaster.success("Success", "Document deleted successfully.");
					$scope.getTaskHistoryList(client_task_id);
					$scope.getTaskDetails(client_task_id);
				}else{
					toaster.error("Failed", "Please try again");
				}	
			}).error(function(error){
				console.log("Error while deleting the task====="+error);
			});
		}
		//});
	}

	$scope.deleteSubTask=function(ttrn_sub_task_id,client_task_id,ev,index){
		/*  var confirm = $mdDialog.confirm()
						          .title('Are you sure you want to delete the task?')
						          .targetEvent(ev)
						          .ok('Yes')
						          .cancel('NO');*/
		var isConfirmed = confirm("Are you sure to delete this record ?");

		if (isConfirmed) {
			$scope.taskCompletion.ttrn_sub_task_id	= ttrn_sub_task_id;
			$scope.taskCompletion.ttrn_client_task_id = client_task_id;
			ApiCallFactory.deleteSubTaskHistory($scope.taskCompletion).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					toaster.success("Success", "Task deleted successfully.");
					$scope.task_history.splice(index,1);
					$scope.getTaskHistoryList(client_task_id);
					$scope.getTaskDetails(client_task_id);
				}else{
					toaster.error("Failed", "Please try again");
				}	
			}).error(function(error){
				console.log("Error while deleting the task====="+error);
			});
		} else {
			return false;
		}
	}	
	/* End Sub Task Code */

	$scope.getAllTasks = function(){
		spinnerService.show('html5spinner');
		ApiCallFactory.getAllTasks($scope.searchObj).success(function(res){
			spinnerService.hide('html5spinner');	
			//alert(JSON.stringify(res.repoData));
			$scope.defaultTasks  = res.repoData;
			$scope.repositoryList.length = 0;

		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get repository list====="+error);
		});

	};


}]);