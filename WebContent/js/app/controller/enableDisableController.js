'use strict';

CMTApp.controller('enableDisableController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$mdDialog','$http' , function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster, spinnerService,$mdDialog,$http) {
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	$scope.tmapObj={};
	$scope.All_Tasks={};
	$scope.tmapObj.tasks_list=[];
	$scope.originalTaskList=[];
	$scope.showOnMultipleSelection=false;
	$scope.originalTaskList=[];
	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.originalUsersList=[];
	$scope.searchObj={};
	$scope.alllegiList = {};
	//$scope.searchObj.exec_id=0;
	//$scope.searchObj.eval_id=0;
	
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
	
	$scope.showAllTask=true;
	 
	//get task list for enablig and disabling List
	$scope.getTaskForEnableDisable=function(){
		spinnerService.show('html5spinner');
		var obj={}
		ApiCallFactory.getTaskForEnableDisable(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
				$scope.editDate={
						role:Storage.get('userDetais.sess_role_id'),
				}
				console.log("role "+$scope.editDate.role);
			//$scope.All_Tasks=res.All_Tasks;
			$scope.AllTasksCount = $scope.All_Tasks.length;
			$scope.originalTaskList=res.All_Tasks;
			$scope.originalEntityList=res.Filters[0].Entity;
			$scope.entityList=res.Filters[0].Entity;
			$scope.originalUnitList=res.Filters[0].Unit;
			$scope.originalFunctionList=res.Filters[0].Function;
			$scope.originalUsersList=res.Filters[0].Users;
			//console.log('res.All_Tasks'+JSON.stringify(res.All_Tasks));
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get enable disable list====="+error);
		});
	};
	//$scope.getTaskForEnableDisable();
	
	
		//get check box ID
	 $scope.getCheckTaskId=function(selectId,task_id){
		 console.log("TASK ID "+task_id);
		 if(selectId==true){
			 console.log("True  ");  
			 var obj={
					 tmap_id:task_id,
       			}
			 $scope.tmapObj.tasks_list.push(obj);
		 }else{
			 var index=0;
			 angular.forEach($scope.tmapObj.tasks_list, function (item) {
				 if(item.tmap_id==task_id){ 
					 console.log("Remove  ");  
					  $scope.tmapObj.tasks_list.splice(index, 1);   
				//	  break;
				 }
				 index++;
			 });
		}
		 if($scope.tmapObj.tasks_list.length==$scope.All_Tasks.length){
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
	        if ($scope.selectedAll) {
	            $scope.selectedAll = true;
	         } else {
	            $scope.selectedAll = false;
	        }
	
	        angular.forEach($scope.All_Tasks, function (item) {
	            item.selectedId = $scope.selectedAll;
	            if($scope.selectedAll){
	            	
	            	var obj={
	            			tmap_id:item.tmap_id,
	            		//	tmap_client_tasks_id:item.client_task_id
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
	
	
	    //enable or disable task
	    $scope.changeTaskStatus = function (statusVal,id) {
	    	
	    	if(id!=0){
	    		$scope.tmapObj={
	    				operation_to_perform:statusVal,
	    				tasks_list:[{tmap_id:id }]
	    		}
	    	}else{
	    		$scope.tmapObj.operation_to_perform=statusVal;
	    	}
	    	if($scope.tmapObj.tasks_list.length>0){
	    		DataFactory.setShowLoader(true);
	    		ApiCallFactory.changeTaskStatusForEnableDisable($scope.tmapObj).success(function(res,status){
	    			DataFactory.setShowLoader(false);
	    			if(status === 200 && res.responseMessage=='Success'){
	    				if(statusVal=='enable'){
	    					toaster.success("Success", "Task enabled successfully");
	    				}else{
	    					toaster.success("Success", "Task disabled successfully");
	    				}

	    				if(id!=0){
	    					angular.forEach($scope.All_Tasks, function (item, index) {
	    						if(item.tmap_id==id){
	    							if(statusVal=='enable'){
	    								$scope.All_Tasks[index].tmap_enable_status=1;
	    							}else{
	    								$scope.All_Tasks[index].tmap_enable_status=0;
	    							}

	    						}
	    					});
	    				}else{
	    					for(var i=0;i<$scope.tmapObj.tasks_list.length; i++){
	    						for(var j=0;i<$scope.All_Tasks.length; j++){
	    							if($scope.All_Tasks[j].tmap_id !=undefined){
	    								if( $scope.tmapObj.tasks_list[i].tmap_id == $scope.All_Tasks[j].tmap_id){
	    									if(statusVal=='enable'){
	    										$scope.All_Tasks[j].tmap_enable_status=1;
	    									}else{
	    										$scope.All_Tasks[j].tmap_enable_status=0;
	    									}
	    									$scope.All_Tasks[j].selectedId=false;
	    									break;
	    								}
	    							}
	    						}
	    					}
	    				}

	    				$scope.selectedAll = false;
	    				$scope.tmapObj.tasks_list=[];
	    				if($scope.tmapObj.tasks_list.length>=2) {
	    					$scope.showOnMultipleSelection=true;
	    				}else{
	    					$scope.showOnMultipleSelection=false;
	    				}
	    				$scope.selectedAll = false;

	    			}else{
	    				if(statusVal=='enable'){
	    					toaster.error("Failed", "Error while performing action");;
	    				}else{
	    					toaster.error("Failed", "Error while performing action");;
	    				}

	    			}
	    		});
	    	}
	    }
	
	    
	    
	    $scope.deleteTask={
	    		tmap_id : 0,
	    		ttrn_client_task_id : 0,
		 }
	    
	    $scope.deleteTaskMapping=function(data,ev,index){
			  var confirm = $mdDialog.confirm()
	          .title('Are you sure you want to delete the task?')
	          .targetEvent(ev)
	          .ok('Yes')
	          .cancel('NO');
	    $mdDialog.show(confirm).then(function() {
	    	 $scope.deleteTask.tmap_id 			= data.tmap_id;
	    	 $scope.deleteTask.lexcare_task_id 	= data.task_lexcare_id;
			  $scope.deleteTask.ttrn_client_task_id = data.tmap_client_tasks_id;
	      ApiCallFactory.deleteTaskMapping($scope.deleteTask).success(function(res,status){
	  			if(status === 200 && res.responseMessage == "Success"){
	  				toaster.success("Success", "Task mapping deleted successfully.");
	  				$scope.All_Tasks.splice(index,1);
	  			}else if(status === 200 && res.responseMessage == "CheckTaskHistory"){
	  				//toaster.error("Faild", "Please delete task history first");
	  				$mdDialog.show(
	  				      $mdDialog.alert()
	  				        //.parent(angular.element(document.querySelector('#popupContainer')))
	  				        .clickOutsideToClose(true)
	  				        .title('Alert')
	  				        .textContent('Please delete task history first.')
	  				        //.ariaLabel('Alert Dialog Demo')
	  				        .ok('Ok')
	  				        .targetEvent(ev)
	  				    );
	  		}	
	  		}).error(function(error){
				console.log("Error while deleting the task====="+error);
			});
	    });
				  	
		  }
	    
	//Search code
	    
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


		
		//Search task
		$scope.searchTasks = function(){
			$scope.All_Tasks=[];
			if($scope.legiList.selected!=undefined){
				
				$scope.searchObj.legi_id=$scope.legiList.selected.task_legi_id;
			}else{
				$scope.searchObj.legi_id=0;
			}

			if($scope.legiRuleL.selected!=undefined){

				//alert($scope.legiRuleL.selected.task_legi_id);
				$scope.searchObj.rule_id=$scope.legiRuleL.selected.task_legi_id;			
			}else{

				$scope.searchObj.rule_id=0;
			}
			
			$scope.obj = {
					"orga_id" : $scope.searchObj.orga_id,
					"loca_id" : $scope.searchObj.loca_id,
					"dept_id" : $scope.searchObj.dept_id,
					"pr_user_id": $scope.searchObj.exec_id,
					"rw_user_id": $scope.searchObj.eval_id,
					"legi_id": $scope.searchObj.legi_id,
					"rule_id": $scope.searchObj.rule_id
			}
			ApiCallFactory.searchEnableDisablePage($scope.obj).success(
					function(res, status) {
						if (res.responseMessage == "Success") {
							//spinnerService.show('html5spinner');
							$scope.All_Tasks = res.All_Tasks;
						
						} else {
							toaster.error("Failed","Error in Search");
						}
					}).error(
					function(error) {
						console.log("get List====="
								+ error);
					});
			
		
			/*angular.forEach($scope.originalTaskList, function (data) {
				if ( (!$scope.searchObj.orga_id || data.orga_id === $scope.searchObj.orga_id) && 
						(!$scope.searchObj.loca_id || data.loca_id === $scope.searchObj.loca_id) &&
						(!$scope.searchObj.dept_id || data.dept_id === $scope.searchObj.dept_id) &&
						(!$scope.searchObj.executor_id || data.executor_id === $scope.searchObj.executor_id) &&
						(!$scope.searchObj.evaluator_id || data.evaluator_id === $scope.searchObj.evaluator_id)){
					$scope.All_Tasks.push(data);
				} 
				
				
			});*/
			
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

	/*	$scope.functionList = [];
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
			spinnerService.show('html5spinner');
			ApiCallFactory
					.getTaskForChangeComplianceOwnerPage(obj)
					.success(
							function(res, status) {
								spinnerService
										.hide('html5spinner');
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