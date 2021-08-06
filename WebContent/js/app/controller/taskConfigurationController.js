'use strict';

CMTApp.controller('taskConfigurationController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$sce','spinnerService','$window','$http', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$sce,spinnerService,$window,$http) {
	
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
	$scope.tmapObj.ttrn_legal_due_date = new Date();
	$scope.tmapObj.ttrn_uh_due_date = new Date();
	$scope.tmapObj.ttrn_fh_due_date = new Date();
	$scope.tmapObj.ttrn_rw_due_date = new Date();
	$scope.tmapObj.ttrn_pr_due_date = new Date();
	$scope.tmapObj.ttrn_impact_on_organization = "";
	$scope.tmapObj.ttrn_impact_on_unit = "";
	$scope.tmapObj.ttrn_impact = "";
	
	
	
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
	$scope.impactEntityList = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
	$scope.frequency = [{id:"User_Defined",name:"User Defined"},{id:"One_Time",name:"One Time"},{id:"Weekly",name:"Weekly"},{id:"Fortnightly",name:"Fortnightly"},{id:"Monthly",name:"Monthly"},{id:"Two_Monthly",name:"Two Monthly"},{id:"Quarterly",name:"Quarterly"},{id:"Four_Monthly",name:"Four Monthly"},{id:"Half_Yearly",name:"Half Yearly"},{id:"Yearly",name:"Yearly"},{id:"Eighteen_Monthly",name:"Eighteen Monthly"},{id:"Two_yearly",name:"Two yearly"},{id:"Three_yearly",name:"Three yearly"},{id:"Four_yearly",name:"Four yearly"},{id:"Five_Yearly",name:"Five Yearly"},{id:"Six_Yearly",name:"Six Yearly"},{id:"Seven_Yearly",name:"Seven Yearly"},{id:"Eight_Yearly",name:"Eight Yearly"},{id:"Nine_Yearly",name:"Nine Yearly"},{id:"Ten_Yearly",name:"Ten Yearly"},{id:"Twenty_Yearly",name:"Twenty Yearly"}]; /*,{id:"Event_Based",name:"Event_Based"}*/
	
	
	$scope.errMessage = {};
	
	///get countries
	$scope.getcountries=function(){
		$scope.categoryOfLawList=[];
		$scope.stateList=[];
	
		ApiCallFactory.getCountriesList().success(function(res,status){
			$scope.countriesList=res;
			
		}).error(function(error){
			console.log(" getCountriesList ====="+error);
		});
		
	};
	$scope.getcountries();
	
	$scope.getStateList=function(){
		$scope.categoryOfLawList=[];
		if($scope.searchObj.ChooseSOrC !=undefined && $scope.searchObj.ChooseSOrC !=null){
		if($scope.searchObj.country_id !=0){
			ApiCallFactory.getStateList($scope.searchObj).success(function(res,status){
				$scope.stateList=res;
			}).error(function(error){
				console.log(" getStateList ====="+error);
			});
		}
		}
	};

	
	$scope.getCategoryOfLawList=function(){
		$scope.searchObj.cat_id=0;
		if($scope.searchObj.ChooseSOrC !=undefined && $scope.searchObj.ChooseSOrC !=null){
			if($scope.searchObj.ChooseSOrC =='central'){
				 $scope.searchObj.state_id =2;
			}
		}
		if($scope.searchObj.country_id !=0 && $scope.searchObj.state_id !=0 && $scope.searchObj.state_id !=null && $scope.searchObj.country_id !=null){
		ApiCallFactory.getCategoryOfLawList($scope.searchObj).success(function(res,status){
			$scope.categoryOfLawList=res;
		}).error(function(error){
			console.log(" getCategoryOfLawList ====="+error);
		});
		}
	};

	 $scope.clearLegiRuleL = function($event) {
		    $event.stopPropagation(); 
		    $scope.legiRuleL.selected=undefined;
			
		  }
	 $scope.clearLegiList = function($event) {
		    $event.stopPropagation(); 
		    $scope.legiList.selected=undefined;
			
	 }
		 

	$scope.getList=function(){
		if($scope.searchObj.country_id !=null && $scope.searchObj.country_id !=0  && $scope.searchObj.country_id !=undefined ){
			$scope.searchObj.cat_id=0;
			if(	$scope.searchObj.ChooseSOrC=='central'){
				$scope.getCategoryOfLawList();
				$scope.getalllegiList();
			}else{
				$scope.legiRuleL.selected=undefined;
				$scope.legiList.selected=undefined;
				$scope.getStateList();
			}
		}else{
			$scope.categoryOfLawList=[];
			$scope.stateList=[];
		}
	}
	
	
	$scope.getalllegiList=function(){
		$scope.legiRuleL.selected=undefined;
		$scope.legiList.selected=undefined;
		$scope.alllegiList=[];
		if($scope.searchObj.country_id !=0){
		ApiCallFactory.getalllegiList($scope.searchObj).success(function(res,status){
			$scope.alllegiList=res;
		}).error(function(error){
			console.log(" getalllegiList ====="+error);
		});
		}
	};
	

	$scope.getalllegiRuleList=function(){
		$scope.searchObj.legi_id=$scope.legiList.selected.legi_id;
		$scope.legiRuleList=[];
		if(	$scope.searchObj.legi_id!=0 && $scope.searchObj.legi_id!=undefined){
		ApiCallFactory.getalllegiRuleList($scope.searchObj).success(function(res,status){
			$scope.legiRuleList=res;
		}).error(function(error){
			console.log(" getalllegiRuleList ====="+error);
		});
		}
	};
	
	
	
	//getEntityUnitFunctionDesignationList
	$scope.getEntityUnitFunctionDesignationList=function(){
		//var obj={}
		ApiCallFactory.getTaskList($scope.configObj).success(function(res,status){
			if(res.errorMessage!="Failed"){
		
			$scope.originalEntityListt=res.assignDropDowns[0].Entity;
			$scope.entityListt=res.assignDropDowns[0].Entity;
		//	console.log('Entity '+res.assignDropDowns[0].Entity);
			$scope.originalUnitListt=res.assignDropDowns[0].Unit;
		//	console.log('Unit '+res.assignDropDowns[0].Unit);
			$scope.originalFunctionListt=res.assignDropDowns[0].Function;
		//	console.log('Functions '+res.assignDropDowns[0].Function);
			$scope.originalExecutorListt=res.assignDropDowns[0].Executor;
		//	console.log('Executor '+res.assignDropDowns[0].Executor);
			$scope.originalEvaluatorListt=res.assignDropDowns[0].Evaluator;
	//		console.log('Evaluator '+res.assignDropDowns[0].Evaluator);
			$scope.originalFunctionHeadListt=res.assignDropDowns[0].Function_Head;
		//	console.log('Function Head '+res.assignDropDowns[0].Function_Head);
			
			}else{
				console.log("get List=====Failed");
			}
		}).error(function(error){
			console.log("get List====="+error);
		});
	};
	//$scope.getEntityUnitFunctionDesignationList();
	
	
	
	//get function list
	$scope.getEntityDependentArrayy = function(){
		$scope.unitListt=[];
		if($scope.configObj.tmap_orga_id!="" && $scope.originalUnitListt.length!=0){
			angular.forEach($scope.originalUnitListt, function (item) {
				if( item.orga_id == $scope.configObj.tmap_orga_id){
					$scope.unitListt.push(item);
				}

			});
		};

		///Add executor and Evaluator and function head
		$scope.executorListt=[];
		$scope.evaluatorListt=[];
		$scope.functionheadListt=[];
	}

	
	$scope.getUnitDependentArrayy= function(){
		$scope.functionListt=[];
		if($scope.configObj.tmap_orga_id!="" && $scope.configObj.tmap_loca_id!="" && $scope.originalFunctionListt.length!=0){
			angular.forEach($scope.originalFunctionListt, function (item) {
				if( (item.orga_id == $scope.configObj.tmap_orga_id) && (item.loca_id == $scope.configObj.tmap_loca_id)){
					$scope.functionListt.push(item);
				}
				console.log('FunctionList '+$scope.functionListt);

			});
		};


		///Add executor and Evaluator and function head
		$scope.executorListt=[];
		$scope.evaluatorListt=[];
		$scope.functionheadListt=[];
		
	}
	
	
	//Get Executor evaluator function head list
	$scope.getFunctionDependentArrayy= function(){
		$scope.executorListt=[];
		$scope.evaluatorListt=[];
		$scope.functionheadListt=[];
		if($scope.configObj.tmap_loca_id!="" && $scope.configObj.tmap_loca_id!="" && $scope.configObj.tmap_dept_id!="" && $scope.originalExecutorListt.length!=0){
			angular.forEach($scope.originalExecutorListt, function (item) {
				if( (item.orga_id == $scope.configObj.tmap_orga_id) && (item.loca_id == $scope.configObj.tmap_loca_id) && (item.dept_id == $scope.configObj.tmap_dept_id)){
					console.log('In IF');
					$scope.executorListt.push(item);
					
				}
				console.log('orga id:'+item.orga_id+' loca id :'+item.loca_id+' dept id: '+item.dept_id+' username: '+item.user_name+' user id: '+item.user_id);
			console.log('executorListt '+$scope.executorListt);
			});
		}
		
		if($scope.configObj.tmap_loca_id!="" && $scope.configObj.tmap_loca_id!="" && $scope.configObj.tmap_dept_id!="" && $scope.originalEvaluatorListt.length!=0){
			angular.forEach($scope.originalEvaluatorListt, function (item) {
				if( (item.orga_id == $scope.configObj.tmap_orga_id) && (item.loca_id == $scope.configObj.tmap_loca_id) && (item.dept_id == $scope.configObj.tmap_dept_id)){
					//$scope.executorListt.push(item);
					$scope.evaluatorListt.push(item);
				}
				console.log('orga id:'+item.orga_id+' loca id :'+item.loca_id+' dept id: '+item.dept_id+' username: '+item.user_name+' user id: '+item.user_id);
				console.log('evaluatorListt '+$scope.evaluatorListt);
			});
		}
		
	}
	
	
	
	
	//getTaskList
	$scope.getTaskList=function(formValid){
		$scope.taskList = [];
		$scope.tmapObj.tasks_list=[];
		//$scope.configObj={};
		if($scope.legiList.selected!=undefined){
			$scope.searchObj.legi_id=$scope.legiList.selected.legi_id;
		}else{
			$scope.searchObj.legi_id=0;
		}
		if($scope.legiRuleL.selected!=undefined){
			$scope.searchObj.rule_id=$scope.legiRuleL.selected.rule_id;
		}else{
			$scope.searchObj.rule_id=0;
		}
		//console.log('searchObj'+$scope.searchObj);
		if(formValid){
			$scope.locaListArray = [];
			$scope.searchObj.orga_id   = $scope.searchObj.tmap_orga_id != undefined ? $scope.searchObj.tmap_orga_id :0;
			$scope.searchObj.loca_id   = $scope.searchObj.tmap_loca_id != undefined ? $scope.searchObj.tmap_loca_id :0;		
			$scope.searchObj.dept_id   = $scope.searchObj.tmap_dept_id != undefined ? $scope.searchObj.tmap_dept_id :0;
			$scope.searchObj.executor  = $scope.searchObj.tmap_pr_user_id !=undefined ? $scope.searchObj.tmap_pr_user_id :0;
			$scope.searchObj.evaluator = $scope.searchObj.tmap_rw_user_id !=undefined ? $scope.searchObj.tmap_rw_user_id :0;
			
			
			if ($scope.searchObj.loca_id.length > 0) {
				for (var i = 0; i < $scope.searchObj.loca_id.length; i++) {					
					$scope.obj = {
						loca_id : $scope.searchObj.loca_id[i]
					}
					$scope.locaListArray.push($scope.obj);
				}
			}

			$scope.obj = {
				"orga_id" : $scope.searchObj.orga_id  ,
				"loca_list" : $scope.locaListArray,
				"dept_id" : $scope.searchObj.dept_id,
				"executor" : $scope.searchObj.executor,
				"evaluator" : $scope.searchObj.evaluator,
				"frequency" : $scope.searchObj.frequency,
				"country_id" : $scope.searchObj.country_id,				
				"state_id" : $scope.searchObj.state_id,
				"cat_id" : $scope.searchObj.cat_id,
				"legi_id" : $scope.searchObj.legi_id,
				"rule_id" : $scope.searchObj.rule_id,
				"searching_for": $scope.searchObj.searching_for			
			}
			
			console.log(" search object "+JSON.stringify($scope.searchObj));
			//console.log(" search obkect "+$scope.searchObj.dept_id);
			//console.log(" search obkect "+$scope.searchObj.dept_id);
			spinnerService.show('html5spinner');
		ApiCallFactory.searchTaskForConfiguration($scope.obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			$scope.taskList=res;
		
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log(" getTaskList ====="+error);
		});
		}
	};
	
	
	//getAssignTaskId on change check box
	 $scope.tmapObj.tasks_list=[];
	 $scope.getCheckTaskId=function(selectId,client_task_id,ttrn_performer_user_id){
		 if(selectId==true){
			 var obj={
					 ttrn_client_task_id    : client_task_id,
					 ttrn_performer_user_id : ttrn_performer_user_id
					 
        			}
			 $scope.tmapObj.tasks_list.push(obj);
		 }else{ 
			 var index=0;
			 angular.forEach($scope.tmapObj.tasks_list, function (item) {
				 if(item.ttrn_client_task_id==client_task_id){
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
	            			ttrn_client_task_id    : item.tmap_client_task_id,
	   					    ttrn_performer_user_id : item.exec_id
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

	  $scope.getUnitImpact = function(){ 
		  //console.log("Impact "+$scope.tmapObj.ttrn_impact_on_organization);
		  if($scope.tmapObj.ttrn_impact_on_organization=='Severe'){
			  $scope.impactUnitList = [ {id:"Severe",name:"Severe"}];
			  $scope.impactList     = [{id:"Severe",name:"Severe"}];
		  }
		  
		  if($scope.tmapObj.ttrn_impact_on_organization=='Major'){
			  $scope.impactUnitList = [ {id:"Severe",name:"Severe"},{id:"Major",name:"Major"}];
			  $scope.impactList     = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"}];
		  }
		  if($scope.tmapObj.ttrn_impact_on_organization=='Moderate'){
			  $scope.impactUnitList = [ {id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"}];
			  $scope.impactList     = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"}];
		  }
		  if($scope.tmapObj.ttrn_impact_on_organization=='Low'){
			  $scope.impactUnitList = [ {id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
			  $scope.impactList     = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
		  }
		  
	  }
	  
	  $scope.validateDates = function() {
	       // $scope.errMessage = '';
	        var curDate = new Date();
	        var flag = 0;
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
			  if($scope.tmapObj.ttrn_frequency_for_operation != 'User_Defined' || $scope.tmapObj.ttrn_frequency_for_operation != 'Event_Based'){
				  $scope.tmapObj.ttrn_legal_due_date 	= moment($scope.tmapObj.ttrn_legal_due_date).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_uh_due_date 		= moment($scope.tmapObj.ttrn_uh_due_date).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_fh_due_date 		= moment($scope.tmapObj.ttrn_fh_due_date).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_rw_due_date 		= moment($scope.tmapObj.ttrn_rw_due_date).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_pr_due_date 		= moment($scope.tmapObj.ttrn_pr_due_date).format('DD-MM-YYYY');
				  
				  if($scope.tmapObj.ttrn_audit_date != undefined || $scope.tmapObj.ttrn_audit_date != null) {
					  console.log('audit date : ' + $scope.tmapObj.ttrn_audit_date );
					  $scope.tmapObj.ttrn_audit_date 		= moment($scope.tmapObj.ttrn_audit_date).format('DD-MM-YYYY');  
				  }else {
					  $scope.tmapObj.ttrn_audit_date 		= '';  
				  }
				  
				  
				  $scope.tmapObj.ttrn_first_alert 		= $scope.tmapObj.ttrn_first_alert ? moment($scope.tmapObj.ttrn_first_alert).format('DD-MM-YYYY') : '';   //moment($scope.tmapObj.ttrn_first_alert).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_second_alert 		= $scope.tmapObj.ttrn_second_alert ? moment($scope.tmapObj.ttrn_second_alert).format('DD-MM-YYYY') : '';   //moment($scope.tmapObj.ttrn_second_alert).format('DD-MM-YYYY');
				  $scope.tmapObj.ttrn_third_alert 		= $scope.tmapObj.ttrn_third_alert ? moment($scope.tmapObj.ttrn_third_alert).format('DD-MM-YYYY') : ''; //moment($scope.tmapObj.ttrn_third_alert).format('DD-MM-YYYY');
			  }else{
				  $scope.tmapObj.ttrn_legal_due_date 	= '';
				  $scope.tmapObj.ttrn_uh_due_date 		= '';
				  $scope.tmapObj.ttrn_fh_due_date 		= '';
				  $scope.tmapObj.ttrn_rw_due_date 		= '';
				  $scope.tmapObj.ttrn_pr_due_date 		= '';
				  
				  $scope.tmapObj.ttrn_audit_date		= '';
				  $scope.tmapObj.ttrn_first_alert 		= '';
				  $scope.tmapObj.ttrn_second_alert 		= '';
				  $scope.tmapObj.ttrn_third_alert 		= '';
			  }
			 // $scope.tmapObj.ttrn_performer_user_id    = 0;
			  $scope.tmapObj.ttrn_frequency_for_alerts =  $scope.tmapObj.ttrn_frequency_for_operation;
			  
			  //Save Task Configuration
			  ApiCallFactory.saveTasksConfiguration($scope.tmapObj).success(function(res,status){
				  spinnerService.hide('html5spinner');
					if(status === 200 && res.responseMessage!= "Failed"){
						$scope.tmapObj.tasks_list=[];
						toaster.success("Success", "Task configured successfully.");
						$scope.selectedAll = false;
						$scope.taskList = [];
						//$scope.tmapObj.ttrn_legal_due_date = new Date();
						//$scope.tmapObj.ttrn_uh_due_date = new Date();
						
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
	  
	  
	  // developed by Swapnali
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

		$scope.getFunctionListByUnit = function() {
			// spinnerService.show('html5spinner');

			$scope.locaListArray = [];
			if ($scope.searchObj.tmap_loca_id.length > 0) {
				for (var i = 0; i < $scope.searchObj.tmap_loca_id.length; i++) {
					// alert($scope.tmapObj.tmap_loca_id[i]);
					$scope.obj = {
						tmap_loca_id : $scope.searchObj.tmap_loca_id[i]
					}
					$scope.locaListArray.push($scope.obj);
				}
			}

			$scope.obj = {
				"orga_id" : $scope.searchObj.tmap_orga_id,
				"loca_list" : $scope.locaListArray
			}
			

			ApiCallFactory
					.getFunctionListByUnit($scope.obj)
					.success(
							function(res, status) {
								$scope.funList = res;
								$scope.functionList = [];
								var key = {};
								for (var i = 0; i < $scope.funList.length; i++) {
									// alert($scope.funList[i].dept_id);
									$scope.val = $scope.funList[i].dept_id;
									//$scope.dept_name = $scope.funList[i].dept_name;
									if (angular
											.isUndefined(key[$scope.val])) {
										key[$scope.val] = $scope.val;
										$scope.functionList
												.push($scope.funList[i]);
										// alert(JSON.stringify($scope.functionList));
									}
								}

							}).error(
							function(error) {
								console.log("get List====="
										+ error);
							});

		}

		$scope.getExecutorList = function() {
			$scope.locaListArray = [];
			if ($scope.searchObj.tmap_loca_id.length > 0) {
				for (var i = 0; i < $scope.searchObj.tmap_loca_id.length; i++) {
					// alert($scope.tmapObj.tmap_loca_id[i]);
					$scope.obj = {
						tmap_loca_id : $scope.searchObj.tmap_loca_id[i]
					}
					$scope.locaListArray.push($scope.obj);
				}
			}

			$scope.obj = {
				"orga_id" : $scope.searchObj.tmap_orga_id,
				"loca_list" : $scope.locaListArray,
				"dept_id" : $scope.searchObj.tmap_dept_id
			}
			
			ApiCallFactory
					.getExecutorList($scope.obj)
					.success(
							function(res, status) {
								if (res.responseMessage == "Success") {
									$scope.exeList = res.Executor;
									$scope.executorList = [];
									var key = {};
									for (var i = 0; i < $scope.exeList.length; i++) {
										// alert($scope.funList[i].dept_id);
										$scope.val = $scope.exeList[i].user_id;
										//$scope.dept_name = $scope.funList[i].dept_name;
										if (angular
												.isUndefined(key[$scope.val])) {
											key[$scope.val] = $scope.val;
											$scope.executorList
													.push($scope.exeList[i]);
											// alert(JSON.stringify($scope.functionList));
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

		$scope.getEvaluatorList = function() {
			$scope.locaListArray = [];
			if ($scope.searchObj.tmap_loca_id.length > 0) {
				for (var i = 0; i < $scope.searchObj.tmap_loca_id.length; i++) {
					// alert($scope.tmapObj.tmap_loca_id[i]);
					$scope.obj = {
						tmap_loca_id : $scope.searchObj.tmap_loca_id[i]
					}
					$scope.locaListArray.push($scope.obj);
				}
			}

			$scope.obj = {
				"orga_id" : $scope.searchObj.tmap_orga_id,
				"loca_list" : $scope.locaListArray,
				"dept_id" : $scope.searchObj.tmap_dept_id
			}
			ApiCallFactory
					.getEvaluatorList($scope.obj)
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

		$scope.getFunHeadList = function() {
			$scope.locaListArray = [];
			if ($scope.searchObj.tmap_loca_id.length > 0) {
				for (var i = 0; i < $scope.searchObj.tmap_loca_id.length; i++) {
					// alert($scope.tmapObj.tmap_loca_id[i]);
					$scope.obj = {
						tmap_loca_id : $scope.searchObj.tmap_loca_id[i]
					}
					$scope.locaListArray.push($scope.obj);
				}
			}

			$scope.obj = {
				"orga_id" : $scope.searchObj.tmap_orga_id,
				"loca_list" : $scope.locaListArray,
				"dept_id" : $scope.searchObj.tmap_dept_id
			}
			ApiCallFactory
					.getFunHeadList($scope.obj)
					.success(
							function(res, status) {
								if (res.responseMessage == "Success") {
									$scope.funheadList = res.FunctionHead;
									$scope.functionheadList = [];
									var key = {};
									for (var i = 0; i < $scope.funheadList.length; i++) {
										$scope.val = $scope.funheadList[i].user_id;
										if (angular
												.isUndefined(key[$scope.val])) {
											key[$scope.val] = $scope.val;
											$scope.functionheadList
													.push($scope.funheadList[i]);															
										}
									}
									
								} else {
									toaster.error("Failed","Error in FunctionHeadList");
								}
							}).error(
							function(error) {
								console.log("get List====="
										+ error);
							});
		}

}]);