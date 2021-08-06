'use strict';

CMTApp.controller('exportDataController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window) {
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	$scope.AllTasks				= [];
	$scope.searchObj 			= {};
	$scope.originalEntity 		= [];
	$scope.originalUnit   		= [];
	$scope.originalFunction   	= [];
	$scope.originalExecutor   	= [];
	$scope.originalEvaluator   	= [];
	$scope.originalCatLaw       = [];
	$scope.alllegiList			= {};
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
	$scope.showButton = true;
	$scope.event=false;
	$scope.state=false;
	
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
 
 $scope.ExportTask=true;
	$scope.toggle=function(){
		$scope.ExportTask = !$scope.ExportTask;
	}
	
	
	$scope.getComplianceRepository=function(){
		 spinnerService.show('html5spinner');
		ApiCallFactory.getExportData($scope.AllTasks).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			$scope.AllTasks       		= res.AllTasks;
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
			$scope.alllegiList          = res.TasksFilter[0].Legislation;
			$scope.originalAllRuleList  = res.TasksFilter[0].Rule;
			$scope.OriginalEvents       = res.TasksFilter[0].Event;
			$scope.OriginalSubEvents    = res.TasksFilter[0].Sub_Event;
			$scope.OriginalAllTasks     = res.AllTasks;
		//	$scope.addMoreItems();//To get first 500 records
			//console.log("Filter Data "+JSON.stringify($scope.OriginalSubEvents));
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get repository list====="+error);
		});
	};
	$scope.getComplianceRepository();
	
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
	
	$scope.getEventSubEvent=function(){ 
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
		
		$scope.event                = [];
		$scope.sub_event            = [];
		var event = [];
		if(	$scope.searchObj.legi_id!=0 && $scope.searchObj.legi_id!=undefined){
			angular.forEach($scope.OriginalEvents, function (item) {
				if((item.task_legi_id == $scope.searchObj.legi_id)){
					if(event.indexOf(item.task_Event) === -1){
						event.push(item.task_Event);
						$scope.event.push(item);
					}
					//$scope.event.push(item);
				}

			});
		}
	}
	
	
	$scope.searchRepository=function(){ 
		spinnerService.show('html5spinner');
		$scope.exportData=[];
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
					(!$scope.searchObj.event || data.task_event === $scope.searchObj.event)/* &&
					(!$scope.searchObj.sub_event || data.task_sub_event === $scope.searchObj.sub_event)*/
					
			   ){
				
				//spinnerService.show('html5spinner');
				$scope.exportData.push(data);
				spinnerService.hide('html5spinner');
				//$scope.scrollTasks.push(data);
			}
			
			//spinnerService.hide('html5spinner');
		});
		$scope.showButton = true;
		$scope.AllTasksCount  		= $scope.scrollTasks.length;
		
	}
	
	
	
	//to generate PDF
	 $scope.generateExportData_Pdf=function(){
		var doc1 = new jsPDF('p', 'pt', 'a4');
		var res1 = doc1.autoTableHtmlToJson(document.getElementById("exportData"));
		doc1.autoTable(res1.columns, res1.data, {
		    startY: 10,
		    styles: {
		      overflow: 'linebreak',
		      fontSize: 5,
		    },
		    columnStyles: {
		      1: {columnWidth: 'auto'}
		    }
		  });		
		doc1.setProperties({
		        title: 'Compliance Management Tool Data',
		        subject: 'Export Data',
		        author: 'Lexcare',
		        keywords: 'lexcare compliance tool',
		        creator: 'Compliance Management Tool'
		        	
		    });

		doc1.cellInitialize();
		doc1.save('Export_Data.pdf');				
	}
	 
	 
	 // to generate data in CSV format
	  $scope.ExportExcels_ExportData=function(){
		 
			$("#exportData tr td a").removeAttr("href");
			$("#exportData").tableExport({type:'excel',escape:'false',tableName:'exportData'});
			
		}
	  
	  
	  $scope.checkAll = function () {
		  		$scope.country = $scope.selectedAll;
		  		$scope.procedure = $scope.selectedAll;
		  		$scope.taskImpact = $scope.selectedAll;
		  		$scope.prohibitivePrecriptive = $scope.selectedAll;
		  		$scope.state = $scope.selectedAll;
		  		$scope.frequency = $scope.selectedAll;
	        	$scope.organizationImpact = $scope.selectedAll;
	        	$scope.specificDueDate = $scope.selectedAll;
	        	$scope.categoryOfLaw = $scope.selectedAll;
	        	$scope.legalDueDate = $scope.selectedAll;
	        	$scope.unitImpact = $scope.selectedAll;
	        	$scope.subEvent = $scope.selectedAll;
	        	$scope.legislation = $scope.selectedAll;
	        	$scope.effectiveDate = $scope.selectedAll;
	        	$scope.implication = $scope.selectedAll;
	        	$scope.subsequentAmountPerDay = $scope.selectedAll;
	        	$scope.rule = $scope.selectedAll;
	        	$scope.event = $scope.selectedAll;
	        	$scope.imprisonmentDuration = $scope.selectedAll;
	        	$scope.typeOfTask = $scope.selectedAll;
	        	$scope.reference = $scope.selectedAll;
	        	$scope.exemptionCreiteria = $scope.selectedAll;
	        	$scope.imprisonmentAppliesTo = $scope.selectedAll;
	        	$scope.weblink = $scope.selectedAll;
	        	$scope.who = $scope.selectedAll;
	        	$scope.fineAmount = $scope.selectedAll;
	        	$scope.taskLevel = $scope.selectedAll;
	        	$scope.statutoryAuthority = $scope.selectedAll;
	        	$scope.when = $scope.selectedAll;
	        	$scope.linkedTaskId = $scope.selectedAll;
	        	$scope.moreInfo = $scope.selectedAll;
	        	$scope.complianceActivity = $scope.selectedAll;
	        	$scope.formNo = $scope.selectedAll;
	        	$scope.interlinkage = $scope.selectedAll;
	        	$scope.configuredFrequency = $scope.selectedAll;
	        	$scope.configuredLegalDueDate = $scope.selectedAll;
	        	$scope.configuredUnitHeadDate = $scope.selectedAll;
	        	$scope.configuredFunctionHeadDate = $scope.selectedAll;
	        	$scope.configuredEvaluatorDate = $scope.selectedAll;
	        	$scope.ConfiguredExecutorDate = $scope.selectedAll;
	        	$scope.ConfiguredStatus = $scope.selectedAll;
	        	$scope.entityName = $scope.selectedAll;
	        	$scope.unitName = $scope.selectedAll;
	        	$scope.departmentName = $scope.selectedAll;
	        	$scope.executorName = $scope.selectedAll;
	        	$scope.evaluatorName = $scope.selectedAll;
	        	$scope.functionHeadName = $scope.selectedAll;
	        	
	       };
}]);