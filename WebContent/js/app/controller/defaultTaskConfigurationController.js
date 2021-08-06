'use strict';

CMTApp.controller('defaultTaskConfigurationController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$sce','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$sce,spinnerService,$window) {
	
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
	$scope.searchObj.event="NA";
	$scope.searchObj.sub_event="NA";
	$scope.tmapObj.dtco_legal_days = 0;
	$scope.tmapObj.dtco_uh_days = 0;
	$scope.tmapObj.dtco_fh_days = 0;
	$scope.tmapObj.dtco_rw_days = 0;
	$scope.tmapObj.dtco_pr_days = 0;
	
	
	
	
	$scope.tmapObj.ttrn_prior_days_buffer = 0;
	//$scope.tmapObj.ttrn_alert_days        = 0;
	//$scope.extra_alert                    = false;

	//$scope.tmapObj.ttrn_document   = 0;
	//$scope.tmapObj.ttrn_historical = 0;
	//$scope.tmapObj.ttrn_frequency_for_alerts       = "NA";
	//$scope.tmapObj.ttrn_allow_back_date_completion = 0;
	//$scope.tmapObj.ttrn_allow_approver_reopening   = 0;
//	$scope.tmapObj.ttrn_no_of_back_days_allowed    = 0;
	$scope.impactList = [];
	$scope.impactUnitList = [];
	$scope.impactEntityList = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
	//$scope.frequency = [{id:"Event_Based",name:"Event_Based"}]; 
	$scope.tmapObj.dtco_after_before="";
	$scope.after_before=[{id:"Same",name:"Same"},{id:"Before",name:"Before"},{id:"After",name:"After"}];
	
	
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
	$scope.getEntityUnitFunctionDesignationList();
	
	
	
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
			$scope.searchObj.orga_id   = $scope.configObj.tmap_orga_id != undefined ? $scope.configObj.tmap_orga_id :0;
			$scope.searchObj.loca_id   = $scope.configObj.tmap_loca_id != undefined ? $scope.configObj.tmap_loca_id :0;		
			$scope.searchObj.dept_id   = $scope.configObj.tmap_dept_id != undefined ? $scope.configObj.tmap_dept_id :0;
			$scope.searchObj.executor  = $scope.configObj.tmap_pr_user_id !=undefined ? $scope.configObj.tmap_pr_user_id :0;
			$scope.searchObj.evaluator = $scope.configObj.tmap_rw_user_id !=undefined ? $scope.configObj.tmap_rw_user_id :0;
				
			
			console.log(" search object "+JSON.stringify($scope.searchObj));
			//console.log(" search obkect "+$scope.searchObj.dept_id);
			//console.log(" search obkect "+$scope.searchObj.dept_id);
			spinnerService.show('html5spinner');
			$scope.searchObj.searching_for="defaultconfiguration";
		ApiCallFactory.getTaskListForDefaultTaskConfiguration($scope.searchObj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			$scope.taskList=res;
			//$scope.tmapObj.tmap_orga_id=$scope.searchObj.orga_id;
			//$scope.tmapObj.tmap_loca_id=$scope.searchObj.loca_id
			//console.log('res'+res);
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log(" getTaskList ====="+error);
		});
		}
	};
	
	
	//getAssignTaskId on change check box
	 $scope.tmapObj.tasks_list=[];
	 $scope.getCheckTaskId=function(selectId,client_task_id){
		 if(selectId==true){
			 var obj={
					 dtco_client_task_id    : client_task_id,
					// ttrn_performer_user_id : ttrn_performer_user_id
					 
        			}
			 $scope.tmapObj.tasks_list.push(obj);
		 }else{ 
			 var index=0;
			 angular.forEach($scope.tmapObj.tasks_list, function (item) {
				 if(item.dtco_client_task_id==client_task_id){
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
	            			dtco_client_task_id    : item.tmap_client_task_id,
	   					   // ttrn_performer_user_id : item.exec_id
	            			}
	            	$scope.tmapObj.tasks_list.push(obj);
	            }else{
	            	$scope.tmapObj.tasks_list=[];
	            }
	        });
	    };
	
	  // console.log("buffer days "+$scope.ttrn_prior_days_buffer);
	    $scope.getDays =function(){
	    	// $scope.ttrn_prior_days_buffer;
	    	if($scope.tmapObj.dtco_after_before=="After"){
	    		if($scope.tmapObj.ttrn_prior_days_buffer<0)
	    			$scope.tmapObj.ttrn_prior_days_buffer = 0;
	    		
	    	console.log("buffer days "+$scope.tmapObj.ttrn_prior_days_buffer);
			$scope.tmapObj.dtco_uh_days = $scope.tmapObj.dtco_legal_days - $scope.tmapObj.ttrn_prior_days_buffer;
			if($scope.tmapObj.dtco_uh_days<0){
				$scope.tmapObj.dtco_uh_days=0;
			}
			$scope.tmapObj.dtco_fh_days = $scope.tmapObj.dtco_uh_days - $scope.tmapObj.ttrn_prior_days_buffer ;
			if($scope.tmapObj.dtco_fh_days<0){
				$scope.tmapObj.dtco_fh_days=0;
			}
			$scope.tmapObj.dtco_rw_days = $scope.tmapObj.dtco_fh_days - $scope.tmapObj.ttrn_prior_days_buffer ;
			if($scope.tmapObj.dtco_rw_days<0){
				$scope.tmapObj.dtco_rw_days=0;
			}
			$scope.tmapObj.dtco_pr_days = $scope.tmapObj.dtco_rw_days - $scope.tmapObj.ttrn_prior_days_buffer ;
			if($scope.tmapObj.dtco_pr_days<0){
				$scope.tmapObj.dtco_pr_days=0;
			}
	    	}else{
	    		$scope.tmapObj.dtco_uh_days = parseInt($scope.tmapObj.dtco_legal_days) + parseInt($scope.tmapObj.ttrn_prior_days_buffer);
	    		$scope.tmapObj.dtco_fh_days = parseInt($scope.tmapObj.dtco_uh_days) + parseInt($scope.tmapObj.ttrn_prior_days_buffer) ;
	    		$scope.tmapObj.dtco_rw_days = parseInt($scope.tmapObj.dtco_fh_days) + parseInt($scope.tmapObj.ttrn_prior_days_buffer) ;
	    		$scope.tmapObj.dtco_pr_days = parseInt($scope.tmapObj.dtco_rw_days) + parseInt($scope.tmapObj.ttrn_prior_days_buffer) ;
	    	}
			
			
		};	
		 
	  $scope.validateDays = function() {
	       // $scope.errMessage = '';
	       // var curDate = new Date();
	        var flag = 0;
	        $scope.ttrn_lh_days = parseInt($scope.tmapObj.dtco_legal_days);
	        $scope.ttrn_uh_days = parseInt($scope.tmapObj.dtco_uh_days);
	       $scope.ttrn_fh_days = parseInt($scope.tmapObj.dtco_fh_days);
	       $scope.ttrn_rw_days = parseInt($scope.tmapObj.dtco_rw_days);
	       $scope.ttrn_pr_days = parseInt($scope.tmapObj.dtco_pr_days);
	        
			if($scope.tmapObj.dtco_after_before=="After"){
	        if($scope.ttrn_lh_days < 0){ 
	        	$scope.errMessage.lh_date = 'Legal days must be greater than 0.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.lh_date ='';
	        }
	        if($scope.ttrn_uh_days > $scope.ttrn_lh_days){ 
	        	$scope.errMessage.uh_date = 'Unit head days must be less than OR equal to Legal days.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.uh_date = '';
	        }
	        
	        if($scope.ttrn_fh_days > $scope.ttrn_uh_days){ 
	        	$scope.errMessage.fh_date = 'Function head days must be less than OR equal to Unit head days.';
	        	flag = 1;
	         //return false;
	        }else{
	        	$scope.errMessage.fh_date = '';
	        }
	        if($scope.ttrn_rw_days > $scope.ttrn_fh_days){ 
	        	$scope.errMessage.rw_date = 'Evaluator days must be less than OR equal to Function head days.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.rw_date = '';
	        }
	        if($scope.ttrn_pr_days > $scope.ttrn_rw_days){ 
	        	$scope.errMessage.pr_date = 'Executor days must be less than OR equal to Evaluator days.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.pr_date = '';
	        }
	  }else{
		  if($scope.ttrn_lh_days < 0){ 
	        	$scope.errMessage.lh_date = 'Legal days must be greater than 0.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.lh_date ='';
	        }
	        if($scope.ttrn_uh_days < $scope.ttrn_lh_days){ 
	        	$scope.errMessage.uh_date = 'Unit head days must be greater than OR equal to Legal days.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.uh_date = '';
	        }
	        
	        if($scope.ttrn_fh_days < $scope.ttrn_uh_days){ 
	        	$scope.errMessage.fh_date = 'Function head days must be greater than OR equal to Unit head days.';
	        	flag = 1;
	         //return false;
	        }else{
	        	$scope.errMessage.fh_date = '';
	        }
	        if($scope.ttrn_rw_days < $scope.ttrn_fh_days){ 
	        	$scope.errMessage.rw_date = 'Evaluator days must be greater than OR equal to Function head days.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.rw_date = '';
	        }
	        if($scope.ttrn_pr_days < $scope.ttrn_rw_days){ 
	        	$scope.errMessage.pr_date = 'Executor days must be greater than OR equal to Evaluator days.';
	        	flag = 1;
	          //return false;
	        }else{
	        	$scope.errMessage.pr_date = '';
	        }
	  }
	       
		
	        //If all date valid then set true
	        if(flag==0){
	        	$scope.tmapObj.validate_days = "TRUE";
	        	return true;
	        }else{
	        	$scope.tmapObj.validate_days = null;
	        	return false;
	        }
	       
	    };
	  
	  $scope.saveConfiguration = function(formValid){ 
		 // console.log("Savee Configuration");
		 // $scope.validateDates();
		  if(formValid && $scope.validateDays()){
			  spinnerService.show('html5spinner');
			  if($scope.tmapObj.dtco_after_before == 'After' || $scope.tmapObj.dtco_after_before == 'Before'){
				  $scope.tmapObj.dtco_legal_days  = $scope.tmapObj.dtco_legal_days;
				  $scope.tmapObj.dtco_uh_days 	 = 	$scope.tmapObj.dtco_uh_days;
				  $scope.tmapObj.dtco_fh_days 	 =  $scope.tmapObj.dtco_fh_days;
				  $scope.tmapObj.dtco_rw_days 	=  $scope.tmapObj.dtco_rw_days;
				  $scope.tmapObj.dtco_pr_days  =  $scope.tmapObj.dtco_pr_days;
				  $scope.tmapObj.dtco_event   = ""
				  $scope.tmapObj.dtco_sub_event=""
				  
			  }else{
				  $scope.tmapObj.dtco_legal_days 	= 0;
				  $scope.tmapObj.dtco_uh_days 		= 0;
				  $scope.tmapObj.dtco_fh_days 		= 0;
				  $scope.tmapObj.dtco_rw_days 		= 0;
				  $scope.tmapObj.dtco_pr_days 		= 0;
				  $scope.tmapObj.dtco_event   = ""
				  $scope.tmapObj.dtco_sub_event=""
			 }
			 
			  
			  //Save Task Configuration
			  ApiCallFactory.SaveDefaultTaskConfiguration($scope.tmapObj).success(function(res,status){
				  spinnerService.hide('html5spinner');
					if(status === 200 && res.responseMessage!= "Failed"){
						$scope.tmapObj.tasks_list=[];
						toaster.success("Success", "Task configured successfully.");
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
	  
	 /* $scope.checkStatus = function(status){  
		  $scope.extra_alert = status;
	  }*/	  
	  
}]);