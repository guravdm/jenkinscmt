'use strict';

CMTApp.controller('subTaskActivationController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	$scope.taskList=[];
	$scope.originalTaskList=[];
	$scope.tmapObj={};
	$scope.tmapObj.tasks_list=[];
	$scope.showOnMultipleSelection=false;
	$scope.originalTaskList=[];
	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.originalUsersList=[];
	$scope.originalExecutorList=[];
	$scope.originalEvaluatorList=[];
	$scope.originalEquipmentTypeList=[];
	$scope.originalEquipmentNumberList = [];
	$scope.originalEquipmentLocationList = [];
	$scope.originalEquipmentFrequencyList = [];
	$scope.searchObj={};
	$scope.showAllTask=true;	
	$scope.tmapObj ={};
	$scope.NtmapObj={};
	
	$scope.searchObj={};
	$scope.importSubTaskList=[];
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
	//getActiveDeativeTaskList
	$scope.getActiveDeativeSubTaskList=function(){
		var obj={}
		    spinnerService.show('html5spinner');
		ApiCallFactory.getconfiguredsubtask(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			$scope.taskList=res.Task;
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
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("getActiveDeativeTaskList====="+error);
		});
	};
	$scope.getActiveDeativeSubTaskList();


	$scope.clearLegiRuleL = function($event) {
	    $event.stopPropagation(); 
	    $scope.legiRuleL.selected=undefined;
		
	  }
 $scope.clearLegiList = function($event) {
	    $event.stopPropagation(); 
	    $scope.legiList.selected=undefined;
		
 }

	//get check box ID
	$scope.getCheckTaskId=function(selectId,sub_id){
		if(selectId==true){
			var obj={
					ttrn_sub_id:sub_id,
			}
			$scope.tmapObj.tasks_list.push(obj);
		}else{
			var index=0;
			angular.forEach($scope.taskList, function (item) {
				if(item.ttrn_sub_id==sub_id){
					$scope.tmapObj.tasks_list.splice(index, 1);   
					//	  break;
				}
				index+1;
			});
		}
		if($scope.tmapObj.tasks_list.length==$scope.taskList.length){
			$scope.selectedAll=true;
		}else{
			$scope.selectedAll=false;
		}
		if($scope.tmapObj.tasks_list.length>=2) {
			$scope.showOnMultipleSelection=true;
		}else{
			$scope.showOnMultipleSelection=false;
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
			item.selectedId = $scope.selectedAll;
			if($scope.selectedAll){
				var obj={
						ttrn_sub_id:item.sub_id,
				}
				$scope.tmapObj.tasks_list.push(obj);
			}else{
				$scope.tmapObj.tasks_list=[];
			}
		});


		if($scope.tmapObj.tasks_list.length>=2) {
			$scope.showOnMultipleSelection=true;
		}else{
			$scope.showOnMultipleSelection=false;
		}
	};

	$scope.changeTaskStatus = function (statusVal,id) {
	//	$scope.selectedId=0;
		if(id!=0){
			$scope.tmapObj={
					status:statusVal,
					tasks_list:[{ttrn_sub_id:id }]
			}
		}else{
			$scope.tmapObj.status=statusVal;
		}
		if($scope.tmapObj.tasks_list.length>0){
			//DataFactory.setShowLoader(true);
			ApiCallFactory.updateStatus($scope.tmapObj).success(function(res,status){
				//DataFactory.setShowLoader(false);
				if(status === 200 && res.successIDs.length>0){
					if(statusVal=='Active'){
						toaster.success("Success", "Task activated successfully");
					}else{
						toaster.success("Success", "Task deactivated successfully");
					}

					if(id!=0){
						///var index=0;
						angular.forEach($scope.taskList, function (item, index) {
							if(item.sub_id==id){
								if(statusVal=='Active'){
									$scope.taskList[index].sub_status='Active';
								}else{
									$scope.taskList[index].sub_status='Inactive';
								}
								
								$scope.taskList[index].selectedId=false;
							}
						});
					}else{
						for(var i=0;i<$scope.tmapObj.tasks_list.length; i++){
							for(var j=0;i<$scope.taskList.length; j++){
								if($scope.taskList[j].sub_id !=undefined){
									if( $scope.tmapObj.tasks_list[i].ttrn_sub_id==$scope.taskList[j].sub_id){
										if(statusVal=='Active'){
											$scope.taskList[j].sub_status='Active';
										}else{
											$scope.taskList[j].sub_status='Inactive';
										}
										$scope.taskList[j].selectedId=false;
										break;
									}
								}
							}
						}
					};

					$scope.selectedAll = false;
					$scope.tmapObj.tasks_list=[];
					if($scope.tmapObj.tasks_list.length>=2) {
						$scope.showOnMultipleSelection=true;
					}else{
						$scope.showOnMultipleSelection=false;
					}
					$scope.selectedAll = false;

				}else{
					if(statusVal=='Active'){
						toaster.error("Failed", "Error while performing action");;
					}else{
						toaster.error("Failed", "Error while performing action");;
					}

				}
			});
		}
	}

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
					(!$scope.searchObj.sub_status || data.sub_status === $scope.searchObj.sub_status) &&
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

	$scope.frequency        = [{id:"User_Defined",name:"User Defined"},{id:"One_Time",name:"One Time"},{id:"Weekly",name:"Weekly"},{id:"Fortnightly",name:"Fortnightly"},{id:"Monthly",name:"Monthly"},{id:"Two_Monthly",name:"Two Monthly"},{id:"Quarterly",name:"Quarterly"},,{id:"Four_Monthly",name:"Four Monthly"},{id:"Half_Yearly",name:"Half Yearly"},{id:"Yearly",name:"Yearly"},{id:"Eighteen_Monthly",name:"Eighteen Monthly"},{id:"Two_yearly",name:"Two yearly"},{id:"Three_yearly",name:"Three yearly"},{id:"Five_Yearly",name:"Five Yearly"},{id:"Six_Yearly",name:"Six Yearly"},{id:"Six_Yearly",name:"Six Yearly"},{id:"Seven_Yearly",name:"Seven Yearly"},{id:"Eight_Yearly",name:"Eight Yearly"},{id:"Nine_Yearly",name:"Nine Yearly"},{id:"Ten_Yearly",name:"Ten Yearly"},{id:"Twenty_Yearly",name:"Twenty Yearly"}]; /*,{id:"Event_Based",name:"Event_Based"}*/
	$scope.lega_date = null;
	$scope.showDatesTr         = false;
	$scope.editSubTaskConfiguration = function(data){
		//console.log("data:" +JSON.stringify(data));
		
		$scope.tmapObj.sub_id = data.sub_id;
		$scope.tmapObj.sub_client_task_id = data.sub_client_task_id;
		$scope.NtmapObj.sub_task_id              = data.sub_task_id;
		$scope.tmapObj.ttrn_frequency_for_operation = data.sub_equip_frequency;
		$scope.tmapObj.ttrn_alert_days              = data.sub_ttrn_alert_prior_day;
		$scope.showDatesTr         = false;
      
		$scope.originalFrequency = data.sub_equip_frequency;
		
		if(!angular.isUndefined(data.sub_task_legal_due_date) && data.sub_task_legal_due_date!=''){
			var from = data.sub_task_legal_due_date.split("-");
			//var completedDate = new Date(from[2], from[1] - 1, from[0]);
			$scope.lega_date = new Date(from[2], from[1] - 1, from[0]);
			//$scope.legal_date = data.ttrn_legal_due_date;//new Date(from[2], from[1] - 1, from[0]);
		}
		
		if(!angular.isUndefined(data.sub_task_uh_due_date) && data.sub_task_uh_due_date!=''){
			var from = data.sub_task_uh_due_date.split("-");
			$scope.unit_date = new Date(from[2], from[1] - 1, from[0]);
		}
		if(!angular.isUndefined(data.sub_task_fh_due_date) && data.sub_task_fh_due_date!=''){
			var from = data.sub_task_fh_due_date.split("-");
			$scope.func_date = new Date(from[2], from[1] - 1, from[0]);
		}
		if(!angular.isUndefined(data.sub_task_rw_due_date) && data.sub_task_rw_due_date!=''){
			var from = data.sub_task_rw_due_date.split("-");
			$scope.eval_date = new Date(from[2], from[1] - 1, from[0]);
		}
		if(!angular.isUndefined(data.sub_task_pr_due_date) && data.sub_task_pr_due_date!=''){
			var from = data.sub_task_pr_due_date.split("-");
			$scope.exec_date = new Date(from[2], from[1] - 1, from[0]);
		}
		
		$scope.tmapObj.ttrn_prior_days_buffer = data.sub_ttrn_buffer_days;
		
		if(data.ttrn_sub_task_historical==1)
			$scope.NtmapObj.ttrn_historical = true;
		else
			$scope.NtmapObj.ttrn_historical = false;
		
        if(data.ttrn_sub_task_document==1)
        	$scope.NtmapObj.ttrn_document = true;
        else
        	$scope.NtmapObj.ttrn_document = false;
        
        console.log("BACk DATE allowed "+data.ttrn_sub_task_back_date_allowed);
        console.log("BACk DATE completion "+data.ttrn_sub_task_back_date_allowed);
        $scope.tmapObj.ttrn_no_of_back_days_allowed = data.ttrn_sub_task_back_date_allowed
        
        if(data.ttrn_sub_task_back_date_allowed==1){
        	$scope.tmapObj.ttrn_no_of_back_days_allowed = data.ttrn_sub_task_back_date_allowed;
        	$scope.tmapObj.ttrn_allow_back_date_completion = true;
        }	
        else
        	$scope.tmapObj.ttrn_allow_back_date_completion = false;
        	
        if(data.ttrn_sub_task_allow_approver_reopening ==1)
        	$scope.NtmapObj.ttrn_allow_approver_reopening = true;
        else
        	$scope.NtmapObj.ttrn_allow_approver_reopening = false;
		
        
        if(data.ttrn_sub_task_first_alert!=''){
        	$scope.extra_alert = true;
        	var from = data.ttrn_sub_task_first_alert.split("-");
			$scope.first_alert = new Date(from[2], from[1] - 1, from[0]);
        	
        }
        if(data.ttrn_sub_task_second_alert!=''){
        	var from = data.ttrn_sub_task_second_alert.split("-");
			$scope.second_alert = new Date(from[2], from[1] - 1, from[0]);
        	
        }
        if(data.ttrn_sub_task_third_alert!=''){
        	var from = data.ttrn_sub_task_third_alert.split("-");
			$scope.third_alert = new Date(from[2], from[1] - 1, from[0]);
        	
        }
        
		$('#editConfiguration').modal();
	}
	
	 $scope.showDates = function(){
		  
		 // $scope.originalFrequency = original_freq;
		  //console.log("Original Freq 0"+$scope.originalFrequency);
		  //console.log("Current Freq 0"+$scope.NtmapObj.ttrn_frequency_for_operation);
		  $scope.tmapObj.ttrn_frequency_for_operation = $scope.NtmapObj.ttrn_frequency_for_operation;
		  if($scope.NtmapObj.ttrn_frequency_for_operation !='User_Defined'){
			  $scope.showDatesTr = true;
		  }else{
			  $scope.showDatesTr = false;
		  }
		  
	  }
	 
	 $scope.getDates =function(){
			
	    	//var legal = new Date($scope.tmapObj.ttrn_legal_due_date);
			if($scope.tmapObj.ttrn_prior_days_buffer<0)
				$scope.tmapObj.ttrn_prior_days_buffer = 0;
				
			$scope.unit_date = new Date($scope.lega_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
			$scope.func_date = new Date($scope.unit_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
			$scope.eval_date = new Date($scope.func_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
			$scope.exec_date = new Date($scope.eval_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
			
			
		}
		
		$scope.errMessage = {};
		 $scope.validateDates = function() {
		       // $scope.errMessage = '';
		        var curDate = new Date();
		        var flag = 0;
		        $scope.ttrn_lh_due_date = new Date($scope.lega_date);
		        $scope.ttrn_uh_due_date = new Date($scope.unit_date);
				$scope.ttrn_fh_due_date = new Date($scope.func_date);
				$scope.ttrn_rw_due_date = new Date($scope.eval_date);
				$scope.ttrn_pr_due_date = new Date($scope.exec_date);
		        
		        /*if($scope.ttrn_lh_due_date < curDate){ 
		        	$scope.errMessage.lh_date = 'Legal date must be greater than OR equal to current date.';
		        	flag = 1;
		          //return false;
		        }else{
		        	$scope.errMessage.lh_date ='';
		        }*/
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
		        if($scope.NtmapObj.ttrn_allow_back_date_completion){
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
		        
		          $scope.ttrn_first_alert 		= $scope.first_alert ? new Date($scope.first_alert) : '';   //moment($scope.tmapObj.ttrn_first_alert).format('DD-MM-YYYY');
				  $scope.ttrn_second_alert 		= $scope.second_alert ? new Date($scope.second_alert): '';   //moment($scope.tmapObj.ttrn_second_alert).format('DD-MM-YYYY');
				  $scope.ttrn_third_alert 		= $scope.third_alert ? new Date($scope.third_alert) : ''; //moment($scope.tmapObj.ttrn_third_alert).format('DD-MM-YYYY');
				  if($scope.extra_alert ==true){
					  console.log("Firts alert "+$scope.first_alert);
					   if(angular.isUndefined($scope.first_alert)){
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
		/*$scope.today = function() {
		    $scope.dt = new Date();
		  };
		  $scope.today();*/

		/*  $scope.clear = function() {
		    $scope.dt = null;
		  };*/

		  $scope.inlineOptions = {
		    customClass: getDayClass,
		    minDate: new Date(),
		    showWeeks: true
		  };

		 /* $scope.dateOptions = {
		    dateDisabled: disabled,
		    formatYear: 'yy',
		    maxDate: new Date(2020, 5, 22),
		    minDate: new Date(),
		    startingDay: 1
		  };*/

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
		  
		  $scope.updateOtherText = function(){
			   
				$scope.tmapObj.ttrn_alert_days = $scope.NtmapObj.ttrn_alert_days;
				if($scope.NtmapObj.ttrn_alert_days ==null|| $scope.NtmapObj.ttrn_alert_days<0)
					$scope.tmapObj.ttrn_alert_days = 0;
			    
				
			}
		  
		  $scope.updateConfiguration = function(formValid){
			 
			  if($scope.validateDates() && formValid){
				  if($scope.NtmapObj.ttrn_historical==true)
					  $scope.tmapObj.ttrn_historical = 1
			       else
					 $scope.tmapObj.ttrn_historical = 0
				  
				  if($scope.NtmapObj.ttrn_document==true)
					  $scope.tmapObj.ttrn_document = 1
				      else
					  $scope.tmapObj.ttrn_document = 0
					  
				  if($scope.NtmapObj.ttrn_allow_approver_reopening==true)
					  $scope.tmapObj.ttrn_allow_approver_reopening = 1
				      else
					  $scope.tmapObj.ttrn_allow_approver_reopening = 0
					  
				  
				  if($scope.NtmapObj.ttrn_allow_back_date_completion==true)
					  $scope.tmapObj.ttrn_allow_back_date_completion = 1;
				  else
					  $scope.tmapObj.ttrn_allow_back_date_completion = 0;
				  
				  spinnerService.show('html5spinner');
				  if($scope.tmapObj.ttrn_frequency_for_operation != 'User_Defined' || $scope.tmapObj.ttrn_frequency_for_operation != 'Event_Based'){
					  $scope.tmapObj.ttrn_legal_due_date 	= moment($scope.lega_date).format('DD-MM-YYYY');
					  $scope.tmapObj.ttrn_uh_due_date 		= moment($scope.unit_date).format('DD-MM-YYYY');
					  $scope.tmapObj.ttrn_fh_due_date 		= moment($scope.func_date).format('DD-MM-YYYY');
					  $scope.tmapObj.ttrn_rw_due_date 		= moment($scope.eval_date).format('DD-MM-YYYY');
					  $scope.tmapObj.ttrn_pr_due_date 		= moment($scope.exec_date).format('DD-MM-YYYY');
					  if($scope.extra_alert == true){
					  $scope.tmapObj.ttrn_first_alert 		= $scope.first_alert ? moment($scope.first_alert).format('DD-MM-YYYY') : '';   //moment($scope.tmapObj.ttrn_first_alert).format('DD-MM-YYYY');
					  $scope.tmapObj.ttrn_second_alert 		= $scope.second_alert ? moment($scope.second_alert).format('DD-MM-YYYY') : '';   //moment($scope.tmapObj.ttrn_second_alert).format('DD-MM-YYYY');
					  $scope.tmapObj.ttrn_third_alert 		= $scope.third_alert ? moment($scope.third_alert).format('DD-MM-YYYY') : ''; //moment($scope.tmapObj.ttrn_third_alert).format('DD-MM-YYYY');
					  }
					  else{
						  $scope.tmapObj.ttrn_first_alert 		= '';
						  $scope.tmapObj.ttrn_second_alert 		= '';
						  $scope.tmapObj.ttrn_third_alert 		= '';
					}
				  }else{
					  $scope.tmapObj.ttrn_legal_due_date 	= '';
					  $scope.tmapObj.ttrn_uh_due_date 		= '';
					  $scope.tmapObj.ttrn_fh_due_date 		= '';
					  $scope.tmapObj.ttrn_rw_due_date 		= '';
					  $scope.tmapObj.ttrn_pr_due_date 		= '';
					  
					  $scope.tmapObj.ttrn_first_alert 		= '';
					  $scope.tmapObj.ttrn_second_alert 		= '';
					  $scope.tmapObj.ttrn_third_alert 		= '';
				  }
				 // $scope.tmapObj.ttrn_performer_user_id    = 0;
				  
				  $scope.tmapObj.ttrn_frequency_for_alerts =  $scope.tmapObj.ttrn_frequency_for_operation;
				  
				  
				  //Update Task Configuration
				  ApiCallFactory.updateSubTasksConfigurationDetails($scope.tmapObj).success(function(res,status){
					  spinnerService.hide('html5spinner');
						if(status === 200){
							$('#editConfiguration').modal("hide");
							toaster.success("Success", "Task configuration updated successfully.");
							$scope.getActiveDeativeSubTaskList();
							$scope.NtmapObj={};
						}else{
							toaster.error("Failed", "Error in configuration.");
						}
					}).error(function(error){
						spinnerService.hide('html5spinner');
						console.log("update Configuration===="+error);
					});
			 
			  }
		  }

}]);