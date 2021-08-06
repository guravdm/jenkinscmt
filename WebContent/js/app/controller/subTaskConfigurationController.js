'use strict';

CMTApp.controller('subTaskConfigurationController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$sce','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$sce,spinnerService,$window) {
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	$scope.tmapObj={};
	$scope.configObj={};
	$scope.configObj.dataRequiredFor="taskEnabling";
	$scope.configObj.tmap_pr_user_id=null;
	$scope.configObj.tmap_rw_user_id=null;
	//$scope.configObj.tmap_fh_user_id=null;
	$scope.originalTaskListt=[];
	$scope.originalEntityListt=[];
	$scope.originalUnitListt=[];
	$scope.originalFunctionListt=[];
	$scope.originalExecutorListt=[];
	$scope.originalEvaluatorListt=[];
	//$scope.originalFunctionHeadListt=[];
	
	$scope.executorListt=[];
	$scope.evaluatorListt=[];
	$scope.searchObj={};
	$scope.searchObj.searching_for="tasksconfiguration";
	$scope.searchObj.state_id=0;
	$scope.searchObj.cat_id=0;
	$scope.searchObj.ChooseSOrC ='central';
	$scope.alllegiList=[];
	$scope.legiRuleList=[];
	$scope.legiList={selected:""};
	$scope.legiRuleL={selected:""};
	$scope.searchObj.frequency="NA";
	$scope.searchObj.orga_id=0;
	$scope.searchObj.loca_id=0;
	$scope.searchObj.dept_id=0;
	$scope.searchObj.executor=0;
	$scope.searchObj.evaluator=0;
	$scope.tmapObj.ttrn_next_examination_date = new Date();
	$scope.tmapObj.ttrn_legal_due_date = new Date();
	$scope.tmapObj.ttrn_uh_due_date = new Date();
	$scope.tmapObj.ttrn_fh_due_date = new Date();
	$scope.tmapObj.ttrn_rw_due_date = new Date();
	$scope.tmapObj.ttrn_pr_due_date = new Date();
	$scope.tmapObj.ttrn_impact_on_organization = "";
	$scope.tmapObj.ttrn_impact_on_unit = "";
	$scope.tmapObj.ttrn_impact = "";
	
	$scope.searchObj={};
	$scope.importSubTaskList=[];
	$scope.originalTaskList=[];
	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.originalExecutorList=[];
	$scope.originalEvaluatorList=[];
	$scope.originalEquipmentTypeList=[];
	$scope.originalEquipmentNumberList = [];
	$scope.originalEquipmentLocationList = [];
	$scope.originalEquipmentFrequencyList = [];
	$scope.alllegiList			= {};
	$scope.originalAllRuleList  = {};
	$scope.legiList				= {selected:""};
	$scope.legiRuleL			= {selected:""};
	$scope.legiRuleL.selected   = undefined;
	$scope.legiList.selected    = undefined;
	
	$scope.tmapObj.ttrn_prior_days_buffer = 0;
	$scope.tmapObj.ttrn_alert_days        = 0;
	$scope.extra_alert                    = false;

	$scope.tmapObj.ttrn_document   = 0;
	$scope.tmapObj.ttrn_historical = 0;
	//$scope.tmapObj.ttrn_frequency_for_alerts       = "NA";
	$scope.tmapObj.ttrn_allow_back_date_completion = 0;
	$scope.tmapObj.ttrn_allow_approver_reopening   = 0;
	$scope.tmapObj.ttrn_no_of_back_days_allowed    = 0;
	$scope.impactList = [];
	$scope.impactUnitList = [];
	//$scope.impactEntityList = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
	//$scope.frequency = [{id:"User_Defined",name:"User Defined"},{id:"One_Time",name:"One Time"},{id:"Weekly",name:"Weekly"},{id:"Fortnightly",name:"Fortnightly"},{id:"Monthly",name:"Monthly"},{id:"Two_Monthly",name:"Two Monthly"},{id:"Quarterly",name:"Quarterly"},{id:"Half_Yearly",name:"Half Yearly"},{id:"Yearly",name:"Yearly"},{id:"Eighteen_Monthly",name:"Eighteen Monthly"},{id:"Two_yearly",name:"Two yearly"},{id:"Three_yearly",name:"Three yearly"},{id:"Four_yearly",name:"Four yearly"},{id:"Five_Yearly",name:"Five Yearly"},{id:"Ten_Yearly",name:"Ten Yearly"},{id:"Twenty_Yearly",name:"Twenty Yearly"}]; /*,{id:"Event_Based",name:"Event_Based"}*/
	
	
	$scope.errMessage = {};
	
	

	 $scope.clearLegiRuleL = function($event) {
		    $event.stopPropagation(); 
		    $scope.legiRuleL.selected=undefined;
			
		  }
	 $scope.clearLegiList = function($event) {
		    $event.stopPropagation(); 
		    $scope.legiList.selected=undefined;
			
	 }
		 

	 $scope.getUserDefinedTask=function(){
			var obj={}
			ApiCallFactory.getsubtasksforconfiguration(obj).success(function(res,status){
				if(res.errorMessage!="Failed"){
				$scope.taskList=res.Task;
				//console.log('res'+JSON.stringify(res));
				$scope.originalTaskList=res.Task;
				$scope.originalEntityList=res.Entity;
				$scope.entityList=res.Entity;
				$scope.originalUnitList=res.Unit;
				$scope.originalFunctionList=res.Function;
				$scope.originalExecutorList=res.Executor;
				$scope.originalEvaluatorList=res.Evaluator;
				$scope.alllegiList          = res.Legislations;
				$scope.originalAllRuleList  = res.Rules;
				$scope.equipmentTypeList = res.Equip_Type;
				$scope.originalEquipmentTypeList = res.Equip_Type;
				$scope.originalEquipmentNumberList = res.Equip_Number;
				$scope.originalEquipmentLocationList = res.Equip_Loca;
				$scope.originalEquipmentFrequencyList = res.Equip_Freq;
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
		
		$scope.getEquipmentTypeDependent = function(){
			$scope.equipmentNumberList=[];
			if($scope.searchObj.equip_type!="" && $scope.originalEquipmentNumberList.length!=0){
				angular.forEach($scope.originalEquipmentNumberList, function (item) {
					if( item.equip_type == $scope.searchObj.equip_type){
						$scope.equipmentNumberList.push(item);
					}

				});
			};
		}
		
		$scope.getEquipmentNumberDependent = function(){
			$scope.equipmentUnitList=[];
			if($scope.searchObj.equip_type!="" && $scope.searchObj.equip_number!="" && $scope.originalEquipmentLocationList.length!=0){
				angular.forEach($scope.originalEquipmentLocationList, function (item) {
					if( (item.equip_type == $scope.searchObj.equip_type) && (item.equip_number == $scope.searchObj.equip_number)){
						$scope.equipmentUnitList.push(item);
					}

				});
			};
		}
		
		$scope.getEquipmentLocationDependent = function(){
			$scope.equipmentFrequencyList=[];
			if($scope.searchObj.equip_type!="" && $scope.searchObj.equip_number!="" && $scope.searchObj.equip_loca!="" && $scope.originalEquipmentFrequencyList.length!=0){
				angular.forEach($scope.originalEquipmentFrequencyList, function (item) {
					if( (item.equip_type == $scope.searchObj.equip_type) && (item.equip_number == $scope.searchObj.equip_number) && (item.equip_loca == $scope.searchObj.equip_loca)){
						$scope.equipmentFrequencyList.push(item);
					}

				});
			};
		}
		
		$scope.searchSubTasks = function(){
			$scope.taskList=[];
			angular.forEach($scope.originalTaskList, function (data) {
				if ( (!$scope.searchObj.orga_id || data.orga_id === $scope.searchObj.orga_id) && 
						(!$scope.searchObj.loca_id || data.loca_id === $scope.searchObj.loca_id) &&
						(!$scope.searchObj.dept_id || data.dept_id === $scope.searchObj.dept_id) &&
						(!$scope.searchObj.legi_id || data.sub_legi_id === $scope.searchObj.legi_id) &&
						(!$scope.searchObj.rule_id || data.sub_rule_id === $scope.searchObj.rule_id) &&
						(!$scope.searchObj.executor_id || data.exec_id === $scope.searchObj.executor_id) &&
						(!$scope.searchObj.evaluator_id || data.eval_id === $scope.searchObj.evaluator_id)&&
						(!$scope.searchObj.equip_type || data.sub_equip_type === $scope.searchObj.equip_type) &&
						(!$scope.searchObj.equip_number || data.sub_equip_number === $scope.searchObj.equip_number)&&
						(!$scope.searchObj.equip_loca || data.sub_equip_location === $scope.searchObj.equip_loca)&&
						(!$scope.searchObj.equip_Freq || data.sub_equip_frequency === $scope.searchObj.equip_Freq)){
					$scope.taskList.push(data);
				} 
			});
			
		}
	
	//getAssignTaskId on change check box
	 $scope.tmapObj.tasks_list=[];
	 $scope.getCheckTaskId=function(selectId,sub_client_task_id,sub_task_id){
		 if(selectId==true){
			 var obj={
					 ttrn_client_task_id    : sub_client_task_id,
					 ttrn_sub_task_id : sub_task_id
					 
        			}
			 $scope.tmapObj.tasks_list.push(obj);
		 }else{ 
			 var index=0;
			 angular.forEach($scope.tmapObj.tasks_list, function (item) {
				 if(item.ttrn_sub_task_id==sub_task_id){
					  $scope.tmapObj.tasks_list.splice(index, 1);   
				//	  break;
				 }
				 index++;
				 
			 });
			
			 
		}
		 if($scope.tmapObj.tasks_list.length==$scope.taskList.length){
			 $scope.selectedAll=true;
		 }else{
			 $scope.selectedAll=false;
		 }
			
	 }
	
	//select all check box
	  $scope.checkAll = function () {
		  $scope.tmapObj.tasks_list=[];
	        if ($scope.selectedAll) {
	            $scope.selectedAll = true;
	         } else {
	            $scope.selectedAll = false;
	        }
	
	        angular.forEach($scope.taskList, function (item) {
	            item.SelectedTaskId = $scope.selectedAll;
	            if($scope.selectedAll){
	            	var obj={
	            			ttrn_client_task_id    : item.sub_client_task_id,
	            			ttrn_sub_task_id : item.sub_task_id
	            			}
	            	$scope.tmapObj.tasks_list.push(obj);
	            }else{
	            	$scope.tmapObj.tasks_list=[];
	            }
	        });
	    };
	
	
	    $scope.getDates =function(){
	    	console.log("in configuration : buffer day "+$scope.tmapObj.ttrn_prior_days_buffer);
	    	if($scope.tmapObj.ttrn_prior_days_buffer<0)
				$scope.tmapObj.ttrn_prior_days_buffer = 0;
	    	
			$scope.tmapObj.ttrn_uh_due_date = new Date($scope.tmapObj.ttrn_legal_due_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
			$scope.tmapObj.ttrn_fh_due_date = new Date($scope.tmapObj.ttrn_uh_due_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
			$scope.tmapObj.ttrn_rw_due_date = new Date($scope.tmapObj.ttrn_fh_due_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
			$scope.tmapObj.ttrn_pr_due_date = new Date($scope.tmapObj.ttrn_rw_due_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
			
			
		};	

	  
	  
	  $scope.validateDates = function() {
	       // $scope.errMessage = '';
	        var curDate = new Date();
	        var flag = 0;
	       // $scope.ttrn_next_examination_date = new Date($scope.tmapObj.ttrn_next_examination_date);
	        $scope.ttrn_lh_due_date = new Date($scope.tmapObj.ttrn_legal_due_date);
	        $scope.ttrn_uh_due_date = new Date($scope.tmapObj.ttrn_uh_due_date);
			$scope.ttrn_fh_due_date = new Date($scope.tmapObj.ttrn_fh_due_date);
			$scope.ttrn_rw_due_date = new Date($scope.tmapObj.ttrn_rw_due_date);
			$scope.ttrn_pr_due_date = new Date($scope.tmapObj.ttrn_pr_due_date);
	        
	        /*if($scope.ttrn_lh_due_date < curDate){ 
	        	$scope.errMessage.lh_date = 'Legal date must be greater than OR equal to current date.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.lh_date ='';
	        }*/
			
			if($scope.ttrn_lh_due_date > $scope.ttrn_next_examination_date){
				$scope.errMessage.lh_date = 'Legal due date must be greater than OR equal to Unit Head Date';
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
	        
	        //If back date allowed
	       // console.log("ALLOWED "+$scope.tmapObj.ttrn_allow_back_date_completion);
	        if($scope.tmapObj.ttrn_allow_back_date_completion==1){
	        	//console.log("In if "+$scope.tmapObj.ttrn_no_of_back_days_allowed);
	        	if($scope.tmapObj.ttrn_no_of_back_days_allowed>0){ //console.log("In True ");
	        	     $scope.errMessage.back_date = '';
	        	}else{
	        		$scope.errMessage.back_date = 'Days must be greater than zero.';
		        	flag = 1;
	        	}
	        	
	        }else{
	        	$scope.errMessage.back_date = '';
	        }
	        
	          $scope.ttrn_first_alert 		= $scope.tmapObj.ttrn_first_alert ? new Date($scope.tmapObj.ttrn_first_alert) : '';   //moment($scope.tmapObj.ttrn_first_alert).format('DD-MM-YYYY');
			  $scope.ttrn_second_alert 		= $scope.tmapObj.ttrn_second_alert ? new Date($scope.tmapObj.ttrn_second_alert): '';   //moment($scope.tmapObj.ttrn_second_alert).format('DD-MM-YYYY');
			  $scope.ttrn_third_alert 		= $scope.tmapObj.ttrn_third_alert ? new Date($scope.tmapObj.ttrn_third_alert) : ''; //moment($scope.tmapObj.ttrn_third_alert).format('DD-MM-YYYY');
	        
			// console.log("ALERT "+flag+" Ectra Alert "+$scope.extra_alert);
			  if($scope.extra_alert ==true){
				  console.log("Firts Name "+$scope.tmapObj.ttrn_first_alert);
				   if(angular.isUndefined($scope.tmapObj.ttrn_first_alert)){
					   console.log("In IF ")
		        	  $scope.errMessage.fa_date = 'Select first alert date.';
		        	flag = 1;
		        }else{
		        	$scope.errMessage.fa_date = '';
		        }
		        
		        if($scope.ttrn_first_alert > $scope.ttrn_pr_due_date ){ 
		        	$scope.errMessage.fa_date = 'First alert must be less than Executor date.';
		        	flag = 1;
		          //return false;
		        }else{
		        	$scope.errMessage.fa_date = '';
		        }
		        
		        if($scope.ttrn_second_alert !=undefined && $scope.ttrn_second_alert > $scope.ttrn_first_alert ){ 
		        	$scope.errMessage.sa_date = 'Second alert must be less than First alert.';
		        	flag = 1;
		          //return false;
		        }else{
		        	$scope.errMessage.sa_date = '';
		        }
		        
		        if($scope.ttrn_third_alert !=undefined && $scope.ttrn_third_alert > $scope.ttrn_second_alert ){ 
		        	$scope.errMessage.ta_date = 'Third alert must be less than Second alert.';
		        	flag = 1;
		          //return false;
		        }else{
		        	$scope.errMessage.ta_date = '';
		        }
		        
			 }
		
	        //If all date valid then set true
	        if(flag==0){
	        	$scope.tmapObj.validate_dates = "TRUE";
	        	return true;
	        }else{
	        	$scope.tmapObj.validate_dates = null;
	        	return false;
	        }
	       
	    };
	  
	  $scope.saveConfiguration = function(formValid){ 
		 // console.log("Savee Configuration");
		 // $scope.validateDates();
		  if(formValid && $scope.validateDates()){
			  spinnerService.show('html5spinner');
			  	  $scope.tmapObj.ttrn_allow_approver_reopening    = $scope.tmapObj.ttrn_allow_approver_reopening;
				  $scope.tmapObj.ttrn_legal_due_date 	= moment($scope.tmapObj.ttrn_legal_due_date).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_uh_due_date 		= moment($scope.tmapObj.ttrn_uh_due_date).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_fh_due_date 		= moment($scope.tmapObj.ttrn_fh_due_date).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_rw_due_date 		= moment($scope.tmapObj.ttrn_rw_due_date).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_pr_due_date 		= moment($scope.tmapObj.ttrn_pr_due_date).format('DD-MM-YYYY');
				  
				  $scope.tmapObj.ttrn_first_alert 		= $scope.tmapObj.ttrn_first_alert ? moment($scope.tmapObj.ttrn_first_alert).format('DD-MM-YYYY') : '';   //moment($scope.tmapObj.ttrn_first_alert).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_second_alert 		= $scope.tmapObj.ttrn_second_alert ? moment($scope.tmapObj.ttrn_second_alert).format('DD-MM-YYYY') : '';   //moment($scope.tmapObj.ttrn_second_alert).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_third_alert 		= $scope.tmapObj.ttrn_third_alert ? moment($scope.tmapObj.ttrn_third_alert).format('DD-MM-YYYY') : ''; //moment($scope.tmapObj.ttrn_third_alert).format('DD-MM-YYYY');
			  
			 // $scope.tmapObj.ttrn_performer_user_id    = 0;
			  $scope.tmapObj.ttrn_frequency_for_alerts =  $scope.tmapObj.ttrn_frequency_for_operation;
			  
			  //Save Task Configuration
			  ApiCallFactory.savesubtasksconfiguration($scope.tmapObj).success(function(res,status){
				  spinnerService.hide('html5spinner');
					if(status === 200 && res.responseMessage!= "Failed"){
						$scope.tmapObj.tasks_list=[];
						toaster.success("Success", "Sub task configured successfully.");
						$scope.selectedAll = false;
						$scope.taskList = [];
						$window.location.reload();
						
					}else{
						toaster.error("Failed", "Error in configuration");
					}
				}).error(function(error){
					spinnerService.hide('html5spinner');
					console.log("Save Configuration===="+error);
				});
		  }
		  
	  }
	  
	  $scope.checkStatus = function(status){  
		  $scope.extra_alert = status;
	  }	  
	  
}]);