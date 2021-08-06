'use strict';

CMTApp.controller('copyTasksController', [ '$scope', '$rootScope', '$stateParams', '$state', 'ApiCallFactory', '$location', 'DataFactory', 'Storage', 'toaster', 'spinnerService', '$window', '$http', function($scope, $rootScope, $stateParams, $state, ApiCallFactory, $location, DataFactory, Storage, toaster, spinnerService, $window, $http) {

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;

	if($scope.userRoleId < 6){
		$state.go('login');
	}

	$scope.tmapObj = {};
	$scope.All_Tasks = {};
	$scope.tmapObj.tasks_list = [];

	$scope.tmapObj.client_tasks_list = [];


	$scope.originalTaskList = [];
	$scope.tmapObj.dataRequiredFor = "taskEnabling";
	$scope.showOnMultipleSelection = false;
	$scope.originalTaskList = [];
	$scope.originalEntityList = [];
	$scope.originalUnitList = [];
	$scope.originalFunctionList = [];
	$scope.originalUsersList = [];
	$scope.originalFunctionHeadList = [];
	$scope.searchObj = {};
	$scope.showAllTask = true;

	$scope.originalLegislationList = [];
	$scope.originalRuleList = [];


	$scope.originalTaskListt = [];
	$scope.originalEntityListt = [];
	$scope.originalUnitListt = [];
	$scope.originalFunctionListt = [];
	$scope.originalExecutorListt = [];
	$scope.originalEvaluatorListt = [];
	$scope.originalFunctionHeadListt = [];
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

	$scope.executorListt = [];
	$scope.evaluatorListt = [];
	$scope.functionheadListt = [];

	$scope.tmapObj.tmap_orga_id = 0;
	$scope.tmapObj.tmap_loca_id = 0;
	$scope.tmapObj.tmap_dept_id = 0;
	$scope.tmapObj.tmap_pr_user_id = 0;
	$scope.tmapObj.tmap_rw_user_id = 0;
	$scope.tmapObj.tmap_fh_user_id = 0;


	// get task list for enablig and disabling List
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

	// get check box ID
	$scope.getCheckTaskId = function(selectId, task_id, lexcare_task_id, tmap_client_tasks_id) {
		console.log("TASK ID " + task_id + "\t lexcare_task_id : " + lexcare_task_id + "\t tmap_client_tasks_id " + tmap_client_tasks_id);
		if (selectId == true) {
			console.log("True  ");
			var obj = {
					tmap_id : task_id,
					tmap_client_tasks_id : tmap_client_tasks_id
			}

			$scope.tmapObj.tasks_list.push(obj);
			var obj2 = {
					tmap_client_tasks_id : tmap_client_tasks_id
			}
			$scope.tmapObj.client_tasks_list.push(obj2);
		} else {
			var index = 0;
			angular.forEach($scope.tmapObj.tasks_list, function(item) {

				if (item.tmap_id == task_id) {
					console.log("Remove  ");
					$scope.tmapObj.tasks_list
					.splice(index, 1);
					// break;
				}
				index + 1;

			});
			
			angular.forEach($scope.tmapObj.client_tasks_list, function(item) {

				if (item.tmap_id == task_id) {
					console.log("Remove  ");
					$scope.tmapObj.client_tasks_list
					.splice(index, 1);
					// break;
				}
				index + 1;

			});
			
		}
		if ($scope.tmapObj.tasks_list.length == $scope.All_Tasks.length) {
			$scope.selectedAll = true;
		} else {
			$scope.selectedAll = false;
		}

		if ($scope.tmapObj.tasks_list.length >= 2) {
			$scope.showOnMultipleSelection = true;
		} else {
			$scope.showOnMultipleSelection = false;
		}

	}

	// select all check box
	$scope.checkAll = function() {
		if ($scope.selectedAll) {
			$scope.selectedAll = true;
		} else {
			$scope.selectedAll = false;
		}

		angular
		.forEach(
				$scope.All_Tasks,
				function(item) {
					item.selectedId = $scope.selectedAll;
					if ($scope.selectedAll) {

						var obj = {
								tmap_id : item.tmap_id,
								// tmap_client_tasks_id:item.client_task_id
						}
						$scope.tmapObj.tasks_list
						.push(obj);
					} else {

						$scope.tmapObj.tasks_list = [];
					}
				});
	};

	$scope.clearLegiRuleL = function($event) {
		$event.stopPropagation();
		$scope.legiRuleL.selected = undefined;

	}
	$scope.clearLegiList = function($event) {
		$event.stopPropagation();
		$scope.legiList.selected = undefined;
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

	$scope.getLegislationList = function() {
		$scope.alllegiList = [];
		var alllegiList_array = [];
		if ($scope.searchObj.orga_id != ""
			&& $scope.searchObj.loca_id != ""
				&& $scope.searchObj.dept_id != ""
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

	// Search code

	$scope.getEntityDependentArray = function() {
		$scope.unitList = [];
		if ($scope.searchObj.orga_id != ""
			&& $scope.originalUnitList.length != 0) {
			angular
			.forEach(
					$scope.originalUnitList,
					function(item) {
						if (item.orga_id == $scope.searchObj.orga_id) {
							$scope.unitList
							.push(item);
						}

					});
		}
		;

		// /Add executor and Evaluator
		$scope.executorList = [];
		$scope.evaluatorList = [];
		$scope.functionHeadList = [];
		var executor_array = [];
		var evaluator_array = [];
		var functionHead_array = [];
		if ($scope.searchObj.orga_id != ""
			&& $scope.originalUsersList.length != 0) {
			angular
			.forEach(
					$scope.originalUsersList,
					function(item) {
						/*
						 * if(item.orga_id ==
						 * $scope.searchObj.orga_id){
						 * $scope.executorList.push(item); }
						 */
						if (item.orga_id == $scope.searchObj.orga_id) {
							if ($
									.inArray(
											item.exec_id,
											executor_array) == -1) {
								executor_array
								.push(item.exec_id);
								$scope.executorList
								.push(item);
							}
						}
						if (item.orga_id == $scope.searchObj.orga_id) {
							if ($
									.inArray(
											item.eval_id,
											evaluator_array) == -1) {
								evaluator_array
								.push(item.eval_id);
								$scope.evaluatorList
								.push(item);
							}
						}

						/*
						 * if(item.orga_id ==
						 * $scope.searchObj.orga_id){
						 * $scope.evaluatorList.push(item); }
						 */

					});
		}
		;
		if ($scope.searchObj.orga_id != ""
			&& $scope.originalFunctionHeadList.length != 0) {
			angular
			.forEach(
					$scope.originalFunctionHeadList,
					function(item) {
						if (item.orga_id == $scope.searchObj.orga_id) {
							if ($
									.inArray(
											item.fh_id,
											functionHead_array) == -1) {
								functionHead_array
								.push(item.fh_id);
								$scope.functionHeadList
								.push(item);
							}
						}
					});
		}
		;
	}

	$scope.getUnitDependentArray = function() {
		$scope.functionList = [];
		if ($scope.searchObj.orga_id != ""
			&& $scope.searchObj.loca_id != ""
				&& $scope.originalFunctionList.length != 0) {
			angular
			.forEach(
					$scope.originalFunctionList,
					function(item) {
						if ((item.orga_id == $scope.searchObj.orga_id)
								&& (item.loca_id == $scope.searchObj.loca_id)) {
							$scope.functionList
							.push(item);
						}

					});
		}
		;

		// /Add executor and Evaluator
		$scope.executorList = [];
		$scope.evaluatorList = [];
		$scope.functionHeadList = [];
		var executor_array = [];
		var evaluator_array = [];
		var functionHead_array = [];
		if ($scope.searchObj.orga_id != ""
			&& $scope.searchObj.loca_id != ""
				&& $scope.searchObj.dept_id != ""
					&& $scope.originalUsersList.length != 0) {
			angular
			.forEach(
					$scope.originalUsersList,
					function(item) {
						/*
						 * if( (item.orga_id ==
						 * $scope.searchObj.orga_id) &&
						 * (item.loca_id ==
						 * $scope.searchObj.loca_id)){
						 * $scope.executorList.push(item); }
						 * 
						 * if( (item.orga_id ==
						 * $scope.searchObj.orga_id) &&
						 * (item.loca_id ==
						 * $scope.searchObj.loca_id)){
						 * $scope.evaluatorList.push(item); }
						 */

						if ((item.orga_id == $scope.searchObj.orga_id)
								&& (item.loca_id == $scope.searchObj.loca_id)) {
							if ($
									.inArray(
											item.exec_id,
											executor_array) == -1) {
								executor_array
								.push(item.exec_id);
								$scope.executorList
								.push(item);
							}
						}
						if ((item.orga_id == $scope.searchObj.orga_id)
								&& (item.loca_id == $scope.searchObj.loca_id)) {
							if ($
									.inArray(
											item.eval_id,
											evaluator_array) == -1) {
								evaluator_array
								.push(item.eval_id);
								$scope.evaluatorList
								.push(item);
							}
						}

					});
		}
		;

		if ($scope.searchObj.orga_id != ""
			&& $scope.searchObj.loca_id != ""
				&& $scope.searchObj.dept_id != ""
					&& $scope.originalFunctionHeadList.length != 0) {
			angular
			.forEach(
					$scope.originalFunctionHeadList,
					function(item) {
						if ((item.orga_id == $scope.searchObj.orga_id)
								&& (item.loca_id == $scope.searchObj.loca_id)) {
							if ($
									.inArray(
											item.fh_id,
											functionHead_array) == -1) {
								functionHead_array
								.push(item.fh_id);
								$scope.functionHeadList
								.push(item);
							}
						}

					});
		}
		;
	}

	$scope.getFunctionDependentArray = function() {
		$scope.executorList = [];
		$scope.evaluatorList = [];
		$scope.functionHeadList = [];
		var executor_array = [];
		var evaluator_array = [];
		var functionHead_array = [];
		if ($scope.searchObj.orga_id != ""
			&& $scope.searchObj.loca_id != ""
				&& $scope.searchObj.dept_id != ""
					&& $scope.originalUsersList.length != 0) {
			angular
			.forEach(
					$scope.originalUsersList,
					function(item) {
						/*
						 * if( (item.orga_id ==
						 * $scope.searchObj.orga_id) &&
						 * (item.loca_id ==
						 * $scope.searchObj.loca_id) &&
						 * (item.dept_id ==
						 * $scope.searchObj.dept_id)){
						 * $scope.executorList.push(item); }
						 * 
						 * if( (item.orga_id ==
						 * $scope.searchObj.orga_id) &&
						 * (item.loca_id ==
						 * $scope.searchObj.loca_id) &&
						 * (item.dept_id ==
						 * $scope.searchObj.dept_id)){
						 * $scope.evaluatorList.push(item); }
						 */

						if ((item.orga_id == $scope.searchObj.orga_id)
								&& (item.loca_id == $scope.searchObj.loca_id)
								&& (item.dept_id == $scope.searchObj.dept_id)) {
							if ($
									.inArray(
											item.exec_id,
											executor_array) == -1) {
								executor_array
								.push(item.exec_id);
								$scope.executorList
								.push(item);
							}
						}
						if ((item.orga_id == $scope.searchObj.orga_id)
								&& (item.loca_id == $scope.searchObj.loca_id)
								&& (item.dept_id == $scope.searchObj.dept_id)) {
							if ($
									.inArray(
											item.eval_id,
											evaluator_array) == -1) {
								evaluator_array
								.push(item.eval_id);
								$scope.evaluatorList
								.push(item);
							}
						}

					});
		}
		;
		if ($scope.searchObj.orga_id != ""
			&& $scope.searchObj.loca_id != ""
				&& $scope.searchObj.dept_id != ""
					&& $scope.originalFunctionHeadList.length != 0) {
			angular
			.forEach(
					$scope.originalFunctionHeadList,
					function(item) {
						if ((item.orga_id == $scope.searchObj.orga_id)
								&& (item.loca_id == $scope.searchObj.loca_id)
								&& (item.dept_id == $scope.searchObj.dept_id)) {
							if ($
									.inArray(
											item.fh_id,
											functionHead_array) == -1) {
								functionHead_array
								.push(item.fh_id);
								$scope.functionHeadList
								.push(item);
							}
						}

					});
		}
		;
	}



	// Search task
	$scope.searchTasks = function() {

		if($scope.legiList.selected!=undefined){

			$scope.tmapObj.legi_id=$scope.legiList.selected.task_legi_id;
		}else{
			$scope.tmapObj.legi_id=0;
		}

		if($scope.legiRuleL.selected!=undefined){

			//alert($scope.legiRuleL.selected.task_legi_id);
			$scope.tmapObj.rule_id=$scope.legiRuleL.selected.task_rule_id;			
		}else{

			$scope.tmapObj.rule_id=0;
		}
		///	 alert("hii");
		spinnerService.show('html5spinner');
		$http({
			url : "./searchcomplianceownerpage",
			method : "post",
			params : {
				'data' : $scope.tmapObj
			}
		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.All_Tasks = result.data.repoData;	

					//alert(result.data.repoData.length);
					//$scope.AllTasksCount  = $scope.repositoryList.length;
					//alert(JSON.stringify($scope.repositoryList));																			
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

		/*	$scope.All_Tasks = [];
								angular
										.forEach(
												$scope.originalTaskList,
												function(data) {
													if ((!$scope.searchObj.orga_id || data.orga_id === $scope.searchObj.orga_id)
															&& (!$scope.searchObj.loca_id || data.loca_id === $scope.searchObj.loca_id)
															&& (!$scope.searchObj.dept_id || data.dept_id === $scope.searchObj.dept_id)
															&& (!$scope.searchObj.executor_id || data.executor_id === $scope.searchObj.executor_id)
															&& (!$scope.searchObj.evaluator_id || data.evaluator_id === $scope.searchObj.evaluator_id)
															&& (!$scope.searchObj.functionHead_id || data.function_head_id === $scope.searchObj.functionHead_id)
															&& (!$scope.searchObj.legi_id || data.task_legi_id === $scope.searchObj.legi_id)
															&& (!$scope.searchObj.rule_id || data.task_rule_id === $scope.searchObj.rule_id)) {
														$scope.All_Tasks
																.push(data);
													}

												});
		 */
	}

	// for change in compliance owner
	// getEntityUnitFunctionDesignationList
	$scope.getEntityUnitFunctionDesignationList = function() {
		// var obj={}
		ApiCallFactory
		.getTaskList($scope.tmapObj)
		.success(
				function(res, status) {
					if (res.errorMessage != "Failed") {

						$scope.originalEntityListt = res.assignDropDowns[0].Entity;
						$scope.entityListt = res.assignDropDowns[0].Entity;
						// console.log('Entity
						// '+res.assignDropDowns[0].Entity);
						$scope.originalUnitListt = res.assignDropDowns[0].Unit;
						// console.log('Unit
						// '+res.assignDropDowns[0].Unit);
						$scope.originalFunctionListt = res.assignDropDowns[0].Function;
						// console.log('Functions
						// '+res.assignDropDowns[0].Function);
						$scope.originalExecutorListt = res.assignDropDowns[0].Executor;
						// console.log('Executor
						// '+res.assignDropDowns[0].Executor);
						$scope.originalEvaluatorListt = res.assignDropDowns[0].Evaluator;
						// console.log('Evaluator
						// '+res.assignDropDowns[0].Evaluator);
						$scope.originalFunctionHeadListt = res.assignDropDowns[0].Function_Head;
						// console.log('Function
						// Head
						// '+res.assignDropDowns[0].Function_Head);

					} else {
						console
						.log("get List=====Failed");
					}
				}).error(
						function(error) {
							console.log("get List====="
									+ error);
						});
	};
	// $scope.getEntityUnitFunctionDesignationList();

	// get function list
	$scope.getEntityDependentArrayy = function() {
		$scope.unitListt = [];
		if ($scope.tmapObj.tmap_orga_id != ""
			&& $scope.originalUnitListt.length != 0) {
			angular
			.forEach(
					$scope.originalUnitListt,
					function(item) {
						if (item.orga_id == $scope.tmapObj.tmap_orga_id) {
							$scope.unitListt
							.push(item);
						}

					});
		}

		// /Add executor and Evaluator and function head
		$scope.executorListt = [];
		$scope.evaluatorListt = [];
		$scope.functionheadListt = [];
	}

	$scope.getUnitDependentArrayy = function() {
		$scope.functionListt = [];
		if ($scope.tmapObj.tmap_orga_id != ""
			&& $scope.tmapObj.tmap_loca_id != ""
				&& $scope.originalFunctionListt.length != 0) {
			angular
			.forEach(
					$scope.originalFunctionListt,
					function(item) {
						if ((item.orga_id == $scope.tmapObj.tmap_orga_id)
								&& (item.loca_id == $scope.tmapObj.tmap_loca_id)) {
							$scope.functionListt
							.push(item);
						}
						console
						.log('FunctionList '
								+ $scope.functionListt);

					});
		}
		;

		// /Add executor and Evaluator and function head
		$scope.executorListt = [];
		$scope.evaluatorListt = [];
		$scope.functionheadListt = [];

	}

	// Get Executor evaluator function head list
	$scope.getFunctionDependentArrayy = function() {
		$scope.executorListt = [];
		$scope.evaluatorListt = [];
		$scope.functionheadListt = [];
		if ($scope.tmapObj.tmap_loca_id != ""
			&& $scope.tmapObj.tmap_loca_id != ""
				&& $scope.tmapObj.tmap_dept_id != ""
					&& $scope.originalExecutorListt.length != 0) {
			angular
			.forEach(
					$scope.originalExecutorListt,
					function(item) {
						if ((item.orga_id == $scope.tmapObj.tmap_orga_id)
								&& (item.loca_id == $scope.tmapObj.tmap_loca_id)
								&& (item.dept_id == $scope.tmapObj.tmap_dept_id)) {
							console
							.log('In IF');
							$scope.executorListt
							.push(item);
						}
						console
						.log('orga id:'
								+ item.orga_id
								+ ' loca id :'
								+ item.loca_id
								+ ' dept id: '
								+ item.dept_id
								+ ' username: '
								+ item.user_name
								+ ' user id: '
								+ item.user_id);
						console
						.log('executorListt '
								+ $scope.executorListt);
					});
		}

		if ($scope.tmapObj.tmap_loca_id != ""
			&& $scope.tmapObj.tmap_loca_id != ""
				&& $scope.tmapObj.tmap_dept_id != ""
					&& $scope.originalEvaluatorListt.length != 0) {
			angular
			.forEach(
					$scope.originalEvaluatorListt,
					function(item) {
						if ((item.orga_id == $scope.tmapObj.tmap_orga_id)
								&& (item.loca_id == $scope.tmapObj.tmap_loca_id)
								&& (item.dept_id == $scope.tmapObj.tmap_dept_id)) {
							$scope.evaluatorListt
							.push(item);
						}
						console
						.log('orga id:'
								+ item.orga_id
								+ ' loca id :'
								+ item.loca_id
								+ ' dept id: '
								+ item.dept_id
								+ ' username: '
								+ item.user_name
								+ ' user id: '
								+ item.user_id);
						console
						.log('evaluatorListt '
								+ $scope.evaluatorListt);
					});
		}

		if ($scope.tmapObj.tmap_loca_id != ""
			&& $scope.tmapObj.tmap_loca_id != ""
				&& $scope.tmapObj.tmap_dept_id != ""
					&& $scope.originalFunctionHeadListt.length != 0) {
			angular
			.forEach(
					$scope.originalFunctionHeadListt,
					function(item) {
						if ((item.orga_id == $scope.tmapObj.tmap_orga_id)
								&& (item.loca_id == $scope.tmapObj.tmap_loca_id)
								&& (item.dept_id == $scope.tmapObj.tmap_dept_id)) {
							$scope.functionheadListt
							.push(item);
						}
						console
						.log('orga id:'
								+ item.orga_id
								+ ' loca id :'
								+ item.loca_id
								+ ' dept id: '
								+ item.dept_id
								+ ' username: '
								+ item.user_name
								+ ' user id: '
								+ item.user_id);
						console
						.log('functionheadListt '
								+ $scope.functionheadListt);
					});
		}
	}

	// assginTask
	$scope.assginTask = function(formValid) {
		if (formValid
				&& $scope.tmapObj.tasks_list.length == 0) {
			toaster.warning("Select task");
		}
		if (formValid
				&& $scope.tmapObj.tasks_list.length > 0) {
			spinnerService.show('html5spinner');

			$scope.obj = {
					"orga_id" : $scope.tmapObj.tmap_orga_id,
					"loca_id" : $scope.tmapObj.tmap_loca_id,
					"dept_id" : $scope.tmapObj.tmap_dept_id,
					"pr_user_id" : $scope.tmapObj.tmap_pr_user_id,
					"rw_user_id" : $scope.tmapObj.tmap_rw_user_id,
					"fh_user_id" : $scope.tmapObj.tmap_fh_user_id,
					"tasks_list" : $scope.tmapObj.tasks_list
			}

			spinnerService.show('html5spinner');
			ApiCallFactory.copyComplianceOwner($scope.obj).success(function(res, status) {
				spinnerService .hide('html5spinner');
				if (status === 200) {
					$scope.tmapObj.tasks_list = [];
					toaster
					.success( "Success", "Task assigned successfully");
					$scope.All_Tasks = {};
					$scope.assignTaskForm
					.$setPristine();
					$scope.assignTaskForm
					.$setUntouched();
					// reset field
					$scope.tmapObj = {
							tmap_dept_id : null,
							tmap_loca_id : null,
							tmap_orga_id : null,
							tmap_pr_user_id : null,
							tmap_rw_user_id : null,
							tmap_fh_user_id : null,
							tasks_list : []
					};
					$scope.searchObj = {
							orga_id : null,
							loca_id : null,
							dept_id : null,
							executor_id : null,
							evaluator_id : null,

					};
					$scope.tmapObj.dataRequiredFor = "taskEnabling";
					$scope.selectedAll = false;
					$scope
					.getTaskForEnableDisable();
				} else {
					toaster
					.error(
							"Failed",
					"Error in assginTask");
				}
			})
			.error(
					function(error) {
						spinnerService
						.hide('html5spinner');
						console
						.log("assginTaskt===="
								+ error);
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

	$scope.getEntityListOnSearch = function() {
		spinnerService.show('html5spinner');
		$http({
			url : "./getentitylist",
			method : "get",
		}).then(function(result) {
			spinnerService.hide('html5spinner');
			$scope.entityListOnSearch = result.data;
		}, function(result) {
			spinnerService.hide('html5spinner');
		});

	}

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

	$scope.getExecutorList = function() {

		$scope.obj = {
				"orga_id" : $scope.tmapObj.orga_id,
				"loca_id" : $scope.tmapObj.loca_id,
				"dept_id" : $scope.tmapObj.dept_id
		}

		ApiCallFactory
		.getExeListForChangeOwner($scope.obj)
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

	$scope.getEvaluatorList = function() {

		$scope.obj = {
				"orga_id" : $scope.tmapObj.orga_id,
				"loca_id" : $scope.tmapObj.loca_id,
				"dept_id" : $scope.tmapObj.dept_id
		}
		ApiCallFactory
		.getEvalListForChangeOwner($scope.obj)
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


		$scope.obj = {
				"orga_id" : $scope.tmapObj.orga_id,
				"loca_id" : $scope.tmapObj.loca_id,
				"dept_id" : $scope.tmapObj.dept_id
		}
		ApiCallFactory
		.getFunHeadListForChangeOwner($scope.obj)
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
						toaster
						.error(
								"Failed",
						"Error in FunctionHeadList");
					}
				}).error(
						function(error) {
							console.log("get List====="
									+ error);
						});
	}



	$scope.getExecutorListOnSearch = function() {

		$scope.obj = {
				"orga_id" : $scope.tmapObj.tmap_orga_id,
				"loca_id" : $scope.tmapObj.tmap_loca_id,
				"dept_id" : $scope.tmapObj.tmap_dept_id
		}

		ApiCallFactory
		.getExeListForChangeOwner($scope.obj)
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

	$scope.getEvaluatorListOnSearch = function() {

		$scope.obj = {
				"orga_id" : $scope.tmapObj.tmap_orga_id,
				"loca_id" : $scope.tmapObj.tmap_loca_id,
				"dept_id" : $scope.tmapObj.tmap_dept_id
		}
		ApiCallFactory
		.getEvalListForChangeOwner($scope.obj)
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

	$scope.getFunHeadListOnSearch = function() {


		$scope.obj = {
				"orga_id" : $scope.tmapObj.tmap_orga_id,
				"loca_id" : $scope.tmapObj.tmap_loca_id,
				"dept_id" : $scope.tmapObj.tmap_dept_id
		}
		ApiCallFactory
		.getFunHeadListForChangeOwner($scope.obj)
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
						toaster
						.error(
								"Failed",
						"Error in FunctionHeadList");
					}
				}).error(
						function(error) {
							console.log("get List====="
									+ error);
						});
	}



} ]);