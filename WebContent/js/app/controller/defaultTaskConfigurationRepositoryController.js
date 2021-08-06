'use strict';

CMTApp.controller('defaultTaskConfigurationRepositoryController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	$scope.tmapObj={};
	$scope.NtmapObj={};
	$scope.All_Tasks={};
	$scope.tmapObj.tasks_list=[];
	$scope.originaltaskList=[];
	$scope.showOnMultipleSelection=false;
	//$scope.originalTaskList=[];
	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.originalUsersList=[];
	$scope.searchObj={};
	$scope.NtmapObj.dtco_legal_days = 0;
	$scope.NtmapObj.dtco_uh_days = 0;
	$scope.NtmapObj.dtco_fh_days = 0;
	$scope.NtmapObj.dtco_rw_days = 0;
	$scope.NtmapObj.dtco_pr_days = 0;
	$scope.after_before=[{id:"Same",name:"Same"},{id:"Before",name:"Before"},{id:"After",name:"After"}];
	
	
	$scope.getTaskForEnableDisable=function(){
		//spinnerService.show('html5spinner');
		var obj={}
		ApiCallFactory.getTaskForEnableDisable(obj).success(function(res,status){
			//spinnerService.hide('html5spinner');
			if(status==200){
				$scope.editDate={
						role:Storage.get('userDetais.sess_role_id'),
				}
				console.log("role "+$scope.editDate.role);
			$scope.All_Tasks=res.All_Tasks;
			$scope.AllTasksCount = $scope.All_Tasks.length;
			//$scope.originalTaskList=res.All_Tasks;
			$scope.originalEntityList=res.Filters[0].Entity;
			$scope.entityList=res.Filters[0].Entity;
			$scope.originalUnitList=res.Filters[0].Unit;
			$scope.originalFunctionList=res.Filters[0].Function;
			$scope.originalUsersList=res.Filters[0].Users;
			//console.log('res.All_Tasks'+JSON.stringify(res.All_Tasks));
			}
		}).error(function(error){
			//spinnerService.hide('html5spinner');
			console.log("get enable disable list====="+error);
		});
	};
	$scope.getTaskForEnableDisable();
	
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
		 
		$scope.getTaskList = function (){
			spinnerService.show('html5spinner');
			ApiCallFactory.getDefaultTaskList($scope.searchObj).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status==200){
				if(res.errorMessage!="Failed"){
				$scope.originaltaskList=res;
				$scope.taskList=res;
				}else{
					console.log(" getTaskList =====error");
				}
			}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log(" getTaskList ====="+error);
			});
			
			
	}
	$scope.getTaskList();	
	
	$scope.searchTasks = function(){
		$scope.taskList=[];
		angular.forEach($scope.originaltaskList, function (data) {
				if ((!$scope.searchObj.orga_id || data.tmap_orga_id === $scope.searchObj.orga_id) && 
					(!$scope.searchObj.loca_id || data.tmap_loca_id === $scope.searchObj.loca_id) &&
					(!$scope.searchObj.dept_id || data.tmap_dept_id === $scope.searchObj.dept_id) &&
					(!$scope.searchObj.exec_id || data.tmap_pr_id === $scope.searchObj.exec_id) &&
					(!$scope.searchObj.eval_id || data.tmap_rw_id === $scope.searchObj.eval_id)&&
					(!$scope.searchObj.dtco_after_before || data.dtco_after_before === $scope.searchObj.dtco_after_before)){
				$scope.taskList.push(data);
			} 
		});
	}
	
	$scope.editDefaultConfiguration = function(data){
		console.log("DATA "+JSON.stringify(data));
		$scope.NtmapObj.dtco_id                  = data.dtco_id;
		$scope.NtmapObj.dtco_client_task_id      = data.dtco_client_task_id;
		$scope.tmapObj.dtco_after_before         = data.dtco_after_before;
		$scope.tmapObj.dtco_legal_days           = data.dtco_legal_days;
		$scope.tmapObj.dtco_uh_days              = data.dtco_uh_days;
		$scope.tmapObj.dtco_fh_days              = data.dtco_fh_days;
		$scope.tmapObj.dtco_rw_days              = data.dtco_rw_days;
		$scope.tmapObj.dtco_pr_days              = data.dtco_pr_days;
		$('#editConfiguration').modal();
	}
	
	
	$scope.getDays =function(){
    	// $scope.ttrn_prior_days_buffer;
    	if($scope.NtmapObj.dtco_after_before=="After"){
    		if($scope.NtmapObj.ttrn_prior_days_buffer<0)
    			$scope.NtmapObj.ttrn_prior_days_buffer = 0;
    		
    	console.log("buffer days "+$scope.NtmapObj.ttrn_prior_days_buffer);
		$scope.NtmapObj.dtco_uh_days = $scope.NtmapObj.dtco_legal_days - $scope.NtmapObj.ttrn_prior_days_buffer;
		if($scope.NtmapObj.dtco_uh_days<0){
			$scope.NtmapObj.dtco_uh_days=0;
		}
		$scope.NtmapObj.dtco_fh_days = $scope.NtmapObj.dtco_uh_days - $scope.NtmapObj.ttrn_prior_days_buffer ;
		if($scope.NtmapObj.dtco_fh_days<0){
			$scope.NtmapObj.dtco_fh_days=0;
		}
		$scope.NtmapObj.dtco_rw_days = $scope.NtmapObj.dtco_fh_days - $scope.NtmapObj.ttrn_prior_days_buffer ;
		if($scope.NtmapObj.dtco_rw_days<0){
			$scope.NtmapObj.dtco_rw_days=0;
		}
		$scope.NtmapObj.dtco_pr_days = $scope.NtmapObj.dtco_rw_days - $scope.NtmapObj.ttrn_prior_days_buffer ;
		if($scope.NtmapObj.dtco_pr_days<0){
			$scope.NtmapObj.dtco_pr_days=0;
		}
    	}else{
    		$scope.NtmapObj.dtco_uh_days = parseInt($scope.NtmapObj.dtco_legal_days) + parseInt($scope.NtmapObj.ttrn_prior_days_buffer);
    		$scope.NtmapObj.dtco_fh_days = parseInt($scope.NtmapObj.dtco_uh_days) + parseInt($scope.NtmapObj.ttrn_prior_days_buffer) ;
    		$scope.NtmapObj.dtco_rw_days = parseInt($scope.NtmapObj.dtco_fh_days) + parseInt($scope.NtmapObj.ttrn_prior_days_buffer) ;
    		$scope.NtmapObj.dtco_pr_days = parseInt($scope.NtmapObj.dtco_rw_days) + parseInt($scope.NtmapObj.ttrn_prior_days_buffer) ;
    	}
		
		
	};	
	 
  $scope.validateDays = function() {
       $scope.errMessage = {};
       // var curDate = new Date();
        var flag = 0;
        $scope.ttrn_lh_days = parseInt($scope.NtmapObj.dtco_legal_days);
        $scope.ttrn_uh_days = parseInt($scope.NtmapObj.dtco_uh_days);
       $scope.ttrn_fh_days = parseInt($scope.NtmapObj.dtco_fh_days);
       $scope.ttrn_rw_days = parseInt($scope.NtmapObj.dtco_rw_days);
       $scope.ttrn_pr_days = parseInt($scope.NtmapObj.dtco_pr_days);
        
		if($scope.NtmapObj.dtco_after_before=="After"){
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
        	$scope.NtmapObj.validate_days = "TRUE";
        	return true;
        }else{
        	$scope.NtmapObj.validate_days = null;
        	return false;
        }
       
    };
    
    $scope.updateDefaultTaskConfiguration = function(formValid){ 
    	 if(formValid && $scope.validateDays()){
    		 if($scope.NtmapObj.dtco_after_before == 'After' || $scope.NtmapObj.dtco_after_before == 'Before'){
				  $scope.NtmapObj.dtco_legal_days  = $scope.NtmapObj.dtco_legal_days;
				  $scope.NtmapObj.dtco_uh_days 	 = 	$scope.NtmapObj.dtco_uh_days;
				  $scope.NtmapObj.dtco_fh_days 	 =  $scope.NtmapObj.dtco_fh_days;
				  $scope.NtmapObj.dtco_rw_days 	=  $scope.NtmapObj.dtco_rw_days;
				  $scope.NtmapObj.dtco_pr_days  =  $scope.NtmapObj.dtco_pr_days;
				  
				  
			  }else{
				  $scope.NtmapObj.dtco_legal_days 	= 0;
				  $scope.NtmapObj.dtco_uh_days 		= 0;
				  $scope.NtmapObj.dtco_fh_days 		= 0;
				  $scope.NtmapObj.dtco_rw_days 		= 0;
				  $scope.NtmapObj.dtco_pr_days 		= 0;
			 }
    		 
    		 ApiCallFactory.updateDefaultTaskConfiguration($scope.NtmapObj).success(function(res,status){
				  spinnerService.hide('html5spinner');
					if(status === 200 && res.responseMessage!= "Fail"){
						$('#editConfiguration').modal("hide");
						toaster.success("Success", "Task updated successfully.");
						
						angular.forEach($scope.originaltaskList, function (item) {
							if(item.dtco_id === $scope.NtmapObj.dtco_id){
								item.dtco_after_before  =  $scope.NtmapObj.dtco_after_before;
								item.dtco_legal_days    =  parseInt($scope.NtmapObj.dtco_legal_days);
								  item.dtco_uh_days 	=  parseInt($scope.NtmapObj.dtco_uh_days);
								  item.dtco_fh_days 	=  parseInt($scope.NtmapObj.dtco_fh_days);
								  item.dtco_rw_days 	=  parseInt($scope.NtmapObj.dtco_rw_days);
								  item.dtco_pr_days     =  parseInt($scope.NtmapObj.dtco_pr_days);
							}
						});
						
						angular.forEach($scope.taskList, function (item) {
							if(item.dtco_id === $scope.NtmapObj.dtco_id){
								item.dtco_after_before  =  $scope.NtmapObj.dtco_after_before;
								item.dtco_legal_days    =  parseInt($scope.NtmapObj.dtco_legal_days);
								  item.dtco_uh_days 	=  parseInt($scope.NtmapObj.dtco_uh_days);
								  item.dtco_fh_days 	=  parseInt($scope.NtmapObj.dtco_fh_days);
								  item.dtco_rw_days 	=  parseInt($scope.NtmapObj.dtco_rw_days);
								  item.dtco_pr_days     =  parseInt($scope.NtmapObj.dtco_pr_days);
							}
						});
						
						//$scope.selectedAll = false;
						//$scope.taskList = [];
						//$window.location.reload();
						$scope.NtmapObj={};
					}else{
						toaster.error("Failed", "Error in update configuration");
					}
				}).error(function(error){
					spinnerService.hide('html5spinner');
					console.log("Save Configuration===="+error);
				});
    	 }
    }
    
	
}]);