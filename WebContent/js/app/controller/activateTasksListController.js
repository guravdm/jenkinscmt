'use strict';

CMTApp.controller('activateTasksListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService','$http', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService,$http) {

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
	$scope.searchObj={};
	$scope.showAllTask=true;	
	$scope.NtmapObj={};
	$scope.alllegiList = {};
	$scope.originalAllRuleList = {};
	$scope.legiList = {
		selected : ""
	};
	$scope.legiRuleL = {
		selected : ""
	};
	$scope.legiRuleL.selected = undefined;
	$scope.legiList.selected = undefined;

	//getActiveDeativeTaskList
	
	$scope.searchObj.orga_id = 0;
	$scope.searchObj.loca_id = 0;
	$scope.searchObj.dept_id = 0;
	$scope.searchObj.exec_id = 0;
	$scope.searchObj.eval_id = 0;
	$scope.searchObj.ttrn_status = null;
	
	$scope.getActiveDeativeTaskList=function(){
		var obj={}
		    spinnerService.show('html5spinner');
		ApiCallFactory.getActiveDeativeTaskList(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			//$scope.taskList=res.All_Tasks;
			$scope.originalTaskList=res.All_Tasks;
			$scope.originalEntityList=res.Filters[0].Entity;
			$scope.entityList=res.Filters[0].Entity;
			$scope.originalUnitList=res.Filters[0].Unit;
			$scope.originalFunctionList=res.Filters[0].Function;
			$scope.originalUsersList=res.Filters[0].Users;
			/*	
			$scope.entityList=res.Filters.Entity;
			$scope.functionList=res.Filters.Function;
			$scope.usersList=res.Filters.Users;*/
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("getActiveDeativeTaskList====="+error);
		});
	};
	//$scope.getActiveDeativeTaskList();

	//get check box ID
	$scope.getCheckTaskId=function(selectId,task_id){
		if(selectId==true){
			var obj={
					ttrn_id:task_id,
			}
			$scope.tmapObj.tasks_list.push(obj);
		}else{
			var index=0;
			angular.forEach($scope.tmapObj.tasks_list, function (item) {
				if(item.ttrn_id==task_id){
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
						ttrn_id:item.ttrn_id,
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
					operation_to_perform:statusVal,
					tasks_list:[{ttrn_id:id }]
			}
		}else{
			$scope.tmapObj.operation_to_perform=statusVal;
		}
		if($scope.tmapObj.tasks_list.length>0){
			//DataFactory.setShowLoader(true);
			ApiCallFactory.changeTaskStatus($scope.tmapObj).success(function(res,status){
				//DataFactory.setShowLoader(false);
				if(status === 200 && res.responseMessage=='Success'){
					if(statusVal=='activate'){
						toaster.success("Success", "Task activated successfully");
					}else{
						toaster.success("Success", "Task deactivated successfully");
					}

					if(id!=0){
						///var index=0;
						angular.forEach($scope.taskList, function (item, index) {
							if(item.ttrn_id==id){
								if(statusVal=='activate'){
									$scope.taskList[index].ttrn_status='Active';
								}else{
									$scope.taskList[index].ttrn_status='Inactive';
								}
								
								$scope.taskList[index].selectedId=false;
							}
						});
					}else{
						for(var i=0;i<$scope.tmapObj.tasks_list.length; i++){
							for(var j=0;i<$scope.taskList.length; j++){
								if($scope.taskList[j].ttrn_id !=undefined){
									if( $scope.tmapObj.tasks_list[i].ttrn_id==$scope.taskList[j].ttrn_id){
										if(statusVal=='activate'){
											$scope.taskList[j].ttrn_status='Active';
										}else{
											$scope.taskList[j].ttrn_status='Inactive';
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
					if(statusVal=='activate'){
						toaster.error("Failed", "Error while performing action");;
					}else{
						toaster.error("Failed", "Error while performing action");;
					}

				}
			});
		}
	}

	////Search code start
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
		if($scope.searchObj.orga_id!="" && $scope.originalUsersList.length!=0){
			angular.forEach($scope.originalUsersList, function (item) {
				
				console.log("Item "+item.exec_id); // JSON.stringify(item)
				console.log("Eval "+item.eval_id);
				
				//$.each(catalog.products, function(index, value) {
				if(item.orga_id == $scope.searchObj.orga_id){
					if ($.inArray(item.exec_id, executor_array)==-1) {
						executor_array.push(item.exec_id);
						$scope.executorList.push(item);
					}
				}
				if(item.orga_id == $scope.searchObj.orga_id){
					if ($.inArray(item.eval_id, evaluator_array)==-1) {
						evaluator_array.push(item.eval_id);
						$scope.evaluatorList.push(item);
					}
				}
				//});
				
			/*	if(item.orga_id == $scope.searchObj.orga_id){
					$scope.executorList.push(item);
				}

				if(item.orga_id == $scope.searchObj.orga_id){
					$scope.evaluatorList.push(item);
				}*/
				

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
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalUsersList.length!=0){
			angular.forEach($scope.originalUsersList, function (item) {
				/*if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					$scope.executorList.push(item);
				}

				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					$scope.evaluatorList.push(item);
				}*/
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					if ($.inArray(item.exec_id, executor_array)==-1) {
						executor_array.push(item.exec_id);
						$scope.executorList.push(item);
					}
				}
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					if ($.inArray(item.eval_id, evaluator_array)==-1) {
						evaluator_array.push(item.eval_id);
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
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalUsersList.length!=0){
			angular.forEach($scope.originalUsersList, function (item) {
				/*if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					$scope.executorList.push(item);
				}

				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					$scope.evaluatorList.push(item);
				}*/
				
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.exec_id, executor_array)==-1) {
						executor_array.push(item.exec_id);
						$scope.executorList.push(item);
					}
				}
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.eval_id, evaluator_array)==-1) {
						evaluator_array.push(item.eval_id);
						$scope.evaluatorList.push(item);
					}
				}

			});
		};
	}


	$scope.searchTasks = function(){
		$scope.taskList=[];
		if($scope.legiList.selected!=undefined){
			
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
		
		$scope.obj = {
				"orga_id" : $scope.searchObj.orga_id,
				"loca_id" : $scope.searchObj.loca_id,
				"dept_id" : $scope.searchObj.dept_id,
				"pr_user_id": $scope.searchObj.exec_id,
				"rw_user_id": $scope.searchObj.eval_id,
				"status" : $scope.searchObj.ttrn_status,
				"legi_id": $scope.searchObj.legi_id,
				"rule_id": $scope.searchObj.rule_id
		}
		ApiCallFactory.searchActivationPage($scope.obj).success(
				function(res, status) {
					if (res.responseMessage == "Success") {
						//spinnerService.show('html5spinner');
						$scope.taskList = res.All_Tasks;
					
					} else {
						toaster.error("Failed","Error in Search");
					}
				}).error(
				function(error) {
					console.log("get List====="
							+ error);
				});
		
	/*	spinnerService.show('html5spinner');
		angular.forEach($scope.originalTaskList, function (data) {
			//spinnerService.hide('html5spinner');
			if ( (!$scope.searchObj.orga_id || data.orga_id === $scope.searchObj.orga_id) && 
					(!$scope.searchObj.loca_id || data.loca_id === $scope.searchObj.loca_id) &&
					(!$scope.searchObj.dept_id || data.dept_id === $scope.searchObj.dept_id) &&
					(!$scope.searchObj.exec_id || data.exec_id === $scope.searchObj.exec_id) &&
					(!$scope.searchObj.eval_id || data.eval_id === $scope.searchObj.eval_id) &&
					(!$scope.searchObj.ttrn_status || data.ttrn_status === $scope.searchObj.ttrn_status)){
				
				$scope.taskList.push(data);
			} 
			//spinnerService.hide('html5spinner');
		});
		spinnerService.hide('html5spinner');*/
	}

	$scope.impactEntityList = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
	$scope.frequency        = [{id:"User_Defined",name:"User Defined"},{id:"One_Time",name:"One Time"},{id:"Weekly",name:"Weekly"},{id:"Fortnightly",name:"Fortnightly"},{id:"Monthly",name:"Monthly"},{id:"Two_Monthly",name:"Two Monthly"},{id:"Quarterly",name:"Quarterly"},,{id:"Four_Monthly",name:"Four Monthly"},{id:"Half_Yearly",name:"Half Yearly"},{id:"Yearly",name:"Yearly"},{id:"Eighteen_Monthly",name:"Eighteen Monthly"},{id:"Two_yearly",name:"Two yearly"},{id:"Three_yearly",name:"Three yearly"},{id:"Five_Yearly",name:"Five Yearly"},{id:"Six_Yearly",name:"Six Yearly"},{id:"Six_Yearly",name:"Six Yearly"},{id:"Seven_Yearly",name:"Seven Yearly"},{id:"Eight_Yearly",name:"Eight Yearly"},{id:"Nine_Yearly",name:"Nine Yearly"},{id:"Ten_Yearly",name:"Ten Yearly"},{id:"Twenty_Yearly",name:"Twenty Yearly"}]; /*,{id:"Event_Based",name:"Event_Based"}*/
	$scope.lega_date = null;
	$scope.showDatesTr         = false;
	$scope.editConfiguration = function(data){
		
		//console.log("DATA "+JSON.stringify(data));
		$scope.tmapObj.ttrn_id = data.ttrn_id;
		$scope.NtmapObj.client_task_id              = data.tmap_client_tasks_id;
		$scope.tmapObj.ttrn_impact_on_organization  = data.ttrn_impact_on_organization;
		$scope.tmapObj.ttrn_impact_on_unit          = data.ttrn_impact_on_unit;
		$scope.tmapObj.ttrn_impact                  = data.ttrn_impact;
		$scope.tmapObj.ttrn_frequency_for_operation = data.ttrn_frequency_for_operation;
		$scope.tmapObj.ttrn_alert_days              = data.ttrn_alert_days;
		$scope.showDatesTr         = false;
      
		$scope.originalFrequency = data.ttrn_frequency_for_operation;
			
		if(!angular.isUndefined(data.ttrn_legal_due_date) && data.ttrn_legal_due_date!=''){
			var from = data.ttrn_legal_due_date.split("-");
			//var completedDate = new Date(from[2], from[1] - 1, from[0]);
			$scope.lega_date = new Date(from[2], from[1] - 1, from[0]);
			//$scope.legal_date = data.ttrn_legal_due_date;//new Date(from[2], from[1] - 1, from[0]);
		}
		
		if(!angular.isUndefined(data.ttrn_uh_due_date) && data.ttrn_uh_due_date!=''){
			var from = data.ttrn_uh_due_date.split("-");
			$scope.unit_date = new Date(from[2], from[1] - 1, from[0]);
		}
		if(!angular.isUndefined(data.ttrn_fh_due_date) && data.ttrn_fh_due_date!=''){
			var from = data.ttrn_fh_due_date.split("-");
			$scope.func_date = new Date(from[2], from[1] - 1, from[0]);
		}
		if(!angular.isUndefined(data.ttrn_rw_due_date) && data.ttrn_rw_due_date!=''){
			var from = data.ttrn_rw_due_date.split("-");
			$scope.eval_date = new Date(from[2], from[1] - 1, from[0]);
		}
		if(!angular.isUndefined(data.ttrn_pr_due_date) && data.ttrn_pr_due_date!=''){
			var from = data.ttrn_pr_due_date.split("-");
			$scope.exec_date = new Date(from[2], from[1] - 1, from[0]);
		}
		
		$scope.tmapObj.ttrn_prior_days_buffer = data.ttrn_prior_days_buffer;
		
		if(data.ttrn_historical==1)
			$scope.NtmapObj.ttrn_historical = true;
		else
			$scope.NtmapObj.ttrn_historical = false;
		
        if(data.ttrn_document==1)
        	$scope.NtmapObj.ttrn_document = true;
        else
        	$scope.NtmapObj.ttrn_document = false;
        
        console.log("BACk DATE allowed "+data.ttrn_no_of_back_days_allowed);
        console.log("BACk DATE completion "+data.ttrn_allow_back_date_completion);
        $scope.tmapObj.ttrn_no_of_back_days_allowed = data.ttrn_no_of_back_days_allowed
        
        if(data.ttrn_allow_back_date_completion==1){
        	$scope.tmapObj.ttrn_no_of_back_days_allowed = data.ttrn_no_of_back_days_allowed;
        	$scope.tmapObj.ttrn_allow_back_date_completion = true;
        }	
        else
        	$scope.tmapObj.ttrn_allow_back_date_completion = false;
        	
        if(data.ttrn_allow_approver_reopening ==1)
        	$scope.NtmapObj.ttrn_allow_approver_reopening = true;
        else
        	$scope.NtmapObj.ttrn_allow_approver_reopening = false;
		
        
        if(data.ttrn_first_alert!=''){
        	$scope.extra_alert = true;
        	var from = data.ttrn_first_alert.split("-");
			$scope.first_alert = new Date(from[2], from[1] - 1, from[0]);
        	
        }
        if(data.ttrn_second_alert!=''){
        	var from = data.ttrn_second_alert.split("-");
			$scope.second_alert = new Date(from[2], from[1] - 1, from[0]);
        	
        }
        if(data.ttrn_third_alert!=''){
        	var from = data.ttrn_first_alert.split("-");
			$scope.third_alert = new Date(from[2], from[1] - 1, from[0]);
        	
        }
        
		$('#editConfiguration').modal();
		
	}
	
	$scope.updateImpact = function(){
		
		$scope.tmapObj.ttrn_impact_on_organization = $scope.NtmapObj.ttrn_impact_on_organization;
		$scope.tmapObj.ttrn_impact_on_unit         = $scope.NtmapObj.ttrn_impact_on_unit;
		$scope.tmapObj.ttrn_impact                 = $scope.NtmapObj.ttrn_impact;
		
	}
	$scope.updateOtherText = function(){
		   
		$scope.tmapObj.ttrn_alert_days = $scope.NtmapObj.ttrn_alert_days;
		if($scope.NtmapObj.ttrn_alert_days ==null|| $scope.NtmapObj.ttrn_alert_days<0)
			$scope.tmapObj.ttrn_alert_days = 0;
	    
		/*if($scope.NtmapObj.ttrn_document==true)
			$scope.tmapObj.ttrn_document = "Yes";
		
		if($scope.NtmapObj.ttrn_document==false)
			$scope.tmapObj.ttrn_document = "No";
		
		if($scope.NtmapObj.ttrn_historical==true)
			$scope.tmapObj.ttrn_historical = "Yes";
		
		if($scope.NtmapObj.ttrn_historical==false)
			$scope.tmapObj.ttrn_historical = "No";
		
		if($scope.NtmapObj.ttrn_allow_approver_reopening==true){
			$scope.tmapObj.ttrn_allow_approver_reopening = "Allowed";
		}
		if($scope.NtmapObj.ttrn_allow_approver_reopening==false){
			$scope.tmapObj.ttrn_allow_approver_reopening = "Not-Allowed";
		}*/
	}
	$scope.getUnitImpact = function(){ 
		  //console.log("Impact "+$scope.tmapObj.ttrn_impact_on_organization);
		
		  if($scope.NtmapObj.ttrn_impact_on_organization=='Severe'){
			  $scope.impactUnitList = [ {id:"Severe",name:"Severe"}];
			  $scope.impactList     = [{id:"Severe",name:"Severe"}];
		  }
		  
		  if($scope.NtmapObj.ttrn_impact_on_organization=='Major'){
			  $scope.impactUnitList = [ {id:"Severe",name:"Severe"},{id:"Major",name:"Major"}];
			  $scope.impactList     = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"}];
		  }
		  if($scope.NtmapObj.ttrn_impact_on_organization=='Moderate'){
			  $scope.impactUnitList = [ {id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"}];
			  $scope.impactList     = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"}];
		  }
		  if($scope.NtmapObj.ttrn_impact_on_organization=='Low'){
			  $scope.impactUnitList = [ {id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
			  $scope.impactList     = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
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
	  //Update task configuration
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
			  ApiCallFactory.updateTasksConfiguration($scope.tmapObj).success(function(res,status){
				  spinnerService.hide('html5spinner');
					if(status === 200){
						$('#editConfiguration').modal("hide");
						toaster.success("Success", "Task configuration updated successfully.");
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
	  
		$scope.getEntityList = function() {
			spinnerService.show('html5spinner');
			$http({
				url : "./getentitylist",
				method : "get",
			}).then(function(result) {
				spinnerService.hide('html5spinner');
				$scope.entityList = result.data;
			}, function(result) {
				spinnerService.hide('html5spinner');
			});

		}
		$scope.getEntityList();

		$scope.getUnitListByEntity = function(entity) {
			spinnerService.show('html5spinner');
			$http({
				url : "./getunitlist",
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
		$scope.getFunctionListByUnit = function(unit,orga_id) {
			$scope.functionList = [];
			var dept_array = [];
			$http({
				url : "./getFunction",
				method : "get",
				params : {
					'unit_id' : unit,
					'orga_id' : orga_id
				}
			}).then(function(result) {
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
		
		$scope.getExecutorListForActivationPage = function(){
			
			$scope.obj = {
					"orga_id" : $scope.searchObj.orga_id,
					"loca_id" : $scope.searchObj.loca_id,
					"dept_id" : $scope.searchObj.dept_id
			}
			ApiCallFactory
			.getExeListForActivationPage($scope.obj)
			.success(
					function(res, status) {
						if (res.responseMessage == "Success") {
							$scope.exeList = res.Executor;
							$scope.executorList = [];
							var key = {};
							for (var i = 0; i < $scope.exeList.length; i++) {
								$scope.val = $scope.exeList[i].user_id;
								if (angular
										.isUndefined(key[$scope.val])) {
									key[$scope.val] = $scope.val;
									$scope.executorList
											.push($scope.exeList[i]);

								}
							}
						} else {
							toaster
									.error(
											"Failed",
											"Error in ExecutorList");
						}
					}).error(
					function(error) {
						console.log("get List====="
								+ error);
					});
			
		}
		
		$scope.getEvalListForActivationPage = function(){
			
			$scope.obj = {
					"orga_id" : $scope.searchObj.orga_id,
					"loca_id" : $scope.searchObj.loca_id,
					"dept_id" : $scope.searchObj.dept_id
			}
			ApiCallFactory
			.getEvalListForActivationPage($scope.obj)
			.success(
					function(res, status) {
						if (res.responseMessage == "Success") {
							$scope.evalList = res.Evaluator;
							$scope.evaluatorList = [];
							var key = {};
							for (var i = 0; i < $scope.evalList.length; i++) {
								$scope.val = $scope.evalList[i].user_id;
								if (angular
										.isUndefined(key[$scope.val])) {
									key[$scope.val] = $scope.val;
									$scope.evaluatorList
											.push($scope.evalList[i]);

								}
							}
						} else {
							toaster
									.error(
											"Failed",
											"Error in EvaluatorList");
						}
					}).error(
					function(error) {
						console.log("get List====="
								+ error);
					});
			
		}
		
		$scope.getTaskForEnableDisable = function() {
			var obj = {}
			//spinnerService.show('html5spinner');
			ApiCallFactory
					.getTaskForChangeComplianceOwnerPage(obj)
					.success(
							function(res, status) {
								spinnerService.hide('html5spinner');
								if (status == 200) {
									
									$scope.originalLegiList = res.Filters[0].Legislations;
									$scope.originalAllRuleList = res.Filters[0].Rules;
									
								}
							})
					.error(
							function(error) {
								spinnerService
										.hide('html5spinner');
								console
										.log("get enable disable list====="
												+ error);
							});
		};
		$scope.getTaskForEnableDisable();
		
		$scope.getLegislationList = function() {
			
			$scope.alllegiList = [];
			var alllegiList_array = [];
			if ($scope.searchObj.orga_id != ""
					|| $scope.searchObj.loca_id != ""
					|| $scope.searchObj.dept_id != ""
					&& $scope.originalLegiList.length != 0) {
				angular
						.forEach(
								$scope.originalLegiList,
								function(item) {
									
									if ((!$scope.searchObj.orga_id || item.orga_id == $scope.searchObj.orga_id)
											&& (!$scope.searchObj.loca_id || item.loca_id == $scope.searchObj.loca_id)
											&& (!$scope.searchObj.dept_id || item.dept_id == $scope.searchObj.dept_id)) {
										if ($.inArray(
														item.task_legi_id,
														alllegiList_array) == -1) {
											alllegiList_array
													.push(item.task_legi_id);
											$scope.alllegiList
													.push(item);
										}
									}
								});
			}
			;
		}
		
		$scope.getalllegiRuleList = function() {
			// $scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
			$scope.legiRuleList = [];
			if ($scope.legiList.selected != undefined) {
				$scope.searchObj.legi_id = $scope.legiList.selected.task_legi_id;
			} else {
				$scope.searchObj.legi_id = 0;
			}
			if ($scope.legiRuleL.selected != undefined) {
				$scope.searchObj.rule_id = $scope.legiRuleL.selected.task_legi_id;
			} else {
				$scope.searchObj.rule_id = 0;
			}

			if ($scope.searchObj.legi_id != 0
					&& $scope.searchObj.legi_id != undefined) {
				angular
						.forEach(
								$scope.originalAllRuleList,
								function(item) {
									if ((item.task_legi_id == $scope.searchObj.legi_id)) {
										$scope.legiRuleList
												.push(item);
									}

								});
			}
		}

		$scope.clearLegiRuleL = function($event) {
			$event.stopPropagation(); 
			$scope.legiRuleL.selected=undefined;

		}
		$scope.clearLegiList = function($event) {
			$event.stopPropagation(); 
			$scope.legiList.selected=undefined;

		}


}]);