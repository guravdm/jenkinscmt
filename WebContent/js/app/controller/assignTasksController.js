'use strict';

CMTApp
		.controller(
				'assignTasksController',
				[
						'$scope',
						'$rootScope',
						'$stateParams',
						'$state',
						'ApiCallFactory',
						'$location',
						'DataFactory',
						'Storage',
						'toaster',
						'spinnerService',
						'$window',
						'$http',
						function($scope, $rootScope, $stateParams, $state,
								ApiCallFactory, $location, DataFactory,
								Storage, toaster, spinnerService, $window,
								$http) {

							$scope.taskList = [];
							$scope.tmapObj = {};
							$scope.searchObj = {};
							$scope.searchObj.dataRequiredFor = "taskMapping"
							$scope.searchObj.searching_for = "tasksmapping";
							// / $scope.searchObj.country_id=0;
							$scope.searchObj.state_id = 0;
							$scope.searchObj.cat_id = 0;
							$scope.searchObj.ChooseSOrC = 'central';
							$scope.alllegiList = {};
							$scope.legiList = {
								selected : ""
							};
							$scope.legiRuleL = {
								selected : ""
							};

							$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
							$scope.userRoleId = $rootScope.getUserRoleId;
							
							if($scope.userRoleId < 6){
								$state.go('login');
							}
							
							$scope.originalTaskList = [];
							// $scope.tmapObj={};
							// $scope.tmapObj.tasks_list=[];

							$scope.originalTaskList = [];
							$scope.originalEntityList = [];
							$scope.originalUnitList = [];
							$scope.originalFunctionList = [];
							$scope.originalExecutorList = [];
							$scope.originalEvaluatorList = [];
							$scope.originalFunctionHeadList = [];

							$scope.showAllTask = true;

							// /get countries
							$scope.getcountries = function() {
								$scope.categoryOfLawList = [];
								$scope.stateList = [];

								ApiCallFactory
										.getCountriesList()
										.success(function(res, status) {
											$scope.countriesList = res;

										})
										.error(
												function(error) {
													console
															.log(" getCountriesList ====="
																	+ error);
												});

							};
							$scope.getcountries();

							$scope.getStateList = function() {
								$scope.categoryOfLawList = [];
								if ($scope.searchObj.ChooseSOrC != undefined
										&& $scope.searchObj.ChooseSOrC != null) {
									if ($scope.searchObj.country_id != null
											&& $scope.searchObj.country_id != 0
											&& $scope.searchObj.country_id != undefined) {
										ApiCallFactory
												.getStateList($scope.searchObj)
												.success(function(res, status) {
													$scope.stateList = res;
												})
												.error(
														function(error) {
															console
																	.log(" getStateList ====="
																			+ error);
														});
									}
								}
							};

							$scope.getCategoryOfLawList = function() {
								$scope.searchObj.cat_id = 0;
								if ($scope.searchObj.ChooseSOrC != undefined
										&& $scope.searchObj.ChooseSOrC != null) {
									if ($scope.searchObj.ChooseSOrC == 'central') {
										$scope.searchObj.state_id = 2;
									}
								}
								if ($scope.searchObj.country_id != 0
										&& $scope.searchObj.state_id != 0
										&& $scope.searchObj.state_id != null
										&& $scope.searchObj.country_id != null) {
									ApiCallFactory
											.getCategoryOfLawList(
													$scope.searchObj)
											.success(function(res, status) {
												$scope.categoryOfLawList = res;
											})
											.error(
													function(error) {
														console
																.log(" getCategoryOfLawList ====="
																		+ error);
													});
								}
							};

							$scope.getList = function() {
								if ($scope.searchObj.country_id != null
										&& $scope.searchObj.country_id != 0
										&& $scope.searchObj.country_id != undefined) {
									$scope.searchObj.cat_id = 0;
									if ($scope.searchObj.ChooseSOrC == 'central') {
										$scope.getCategoryOfLawList();
										$scope.getalllegiList();
									} else {
										$scope.legiRuleL.selected = undefined;
										$scope.legiList.selected = undefined;
										$scope.getStateList();
									}
								} else {
									$scope.categoryOfLawList = [];
									$scope.stateList = [];
								}
							}

							$scope.getalllegiList = function() {
								$scope.legiRuleL.selected = undefined;
								$scope.legiList.selected = undefined;
								if ($scope.searchObj.country_id != null
										&& $scope.searchObj.country_id != 0
										&& $scope.searchObj.country_id != undefined) {
									ApiCallFactory
											.getalllegiList($scope.searchObj)
											.success(function(res, status) {
												$scope.alllegiList = res;
											})
											.error(
													function(error) {
														console
																.log(" getalllegiList ====="
																		+ error);
													});
								}
							};

							$scope.getalllegiRuleList = function() {
								$scope.searchObj.legi_id = $scope.legiList.selected.legi_id;
								if ($scope.searchObj.legi_id != 0
										&& $scope.searchObj.legi_id != undefined) {
									ApiCallFactory
											.getalllegiRuleList(
													$scope.searchObj)
											.success(function(res, status) {
												$scope.legiRuleList = res;
											})
											.error(
													function(error) {
														console
																.log(" getalllegiRuleList ====="
																		+ error);
													});
								}
							};

							$scope.clearLegiRuleL = function($event) {
								$event.stopPropagation();
								$scope.legiRuleL.selected = undefined;

							}
							$scope.clearLegiList = function($event) {
								$event.stopPropagation();
								$scope.legiList.selected = undefined;

							}

							// getTaskList
							/*
							 * $scope.getTaskList=function(formValid){
							 * 
							 * if($scope.legiList.selected!=undefined){
							 * $scope.searchObj.legi_id=$scope.legiList.selected.legi_id;
							 * }else{ $scope.searchObj.legi_id=0; }
							 * if($scope.legiRuleL.selected!=undefined){
							 * $scope.searchObj.rule_id=$scope.legiRuleL.selected.rule_id;
							 * }else{ $scope.searchObj.rule_id=0; }
							 * if(formValid){
							 * spinnerService.show('html5spinner');
							 * $scope.tmapObj={ tmap_dept_id:null,
							 * tmap_loca_id:$scope.searchObj.loca_id,
							 * tmap_orga_id:$scope.searchObj.orga_id,
							 * tmap_pr_user_id:null, tmap_rw_user_id:null,
							 * tmap_fh_user_id:null, tasks_list: [] };
							 * $scope.searchObj.dataRequiredFor = "taskMapping";
							 * ApiCallFactory.getTaskList($scope.searchObj).success(function(res,status){
							 * spinnerService.hide('html5spinner');
							 * if(status==200){ if(res.errorMessage!="Failed"){
							 * $scope.taskList=res.allTasks;
							 * $scope.originalExecutorList=res.assignDropDowns[0].Executor;
							 * $scope.originalEvaluatorList=res.assignDropDowns[0].Evaluator;
							 * $scope.originalFunctionHeadList=res.assignDropDowns[0].Function_Head;
							 * 
							 * ///
							 * console.log('res.allTasks'+JSON.stringify(res.allTasks));
							 * //$scope.tmapObj.tmap_orga_id=$scope.searchObj.orga_id;
							 * //$scope.tmapObj.tmap_loca_id=$scope.searchObj.loca_id
							 * }else{ console.log(" getTaskList =====error"); } }
							 * }).error(function(error){
							 * spinnerService.hide('html5spinner');
							 * console.log(" getTaskList ====="+error); }); } };
							 * 
							 */

							// getAssignTaskId on change check box
							$scope.tmapObj.tasks_list = []
							$scope.getCheckTaskId = function(selectId,
									lexcare_task_id, task_id) {
								if (selectId == true) {
									var obj = {
										tmap_task_id : task_id,
										tmap_lexcare_task_id : lexcare_task_id
									}
									$scope.tmapObj.tasks_list.push(obj);
								} else {
									var index = 0;

									angular
											.forEach(
													$scope.tmapObj.tasks_list,
													function(item) {
														if (item.tmap_task_id == task_id) {
															$scope.tmapObj.tasks_list
																	.splice(
																			index,
																			1);
														}
														index++;
													});

								}
								if ($scope.tmapObj.tasks_list.length == $scope.taskList.length) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}

							}

							// select all check box
							$scope.checkAll = function() {
								$scope.tmapObj.tasks_list = [];
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}

								angular
										.forEach(
												$scope.taskList,
												function(item) {
													item.SelectedTaskId = $scope.selectedAll;
													if ($scope.selectedAll) {
														var obj = {
															tmap_task_id : item.task_id,
															tmap_lexcare_task_id : item.lexcare_task_id
														}
														$scope.tmapObj.tasks_list
																.push(obj);
													} else {
														$scope.tmapObj.tasks_list = [];
													}
												});
							};

							// getEntityUnitFunctionDesignationList
							$scope.getEntityUnitFunctionDesignationList = function() {
								var obj = {}
								ApiCallFactory
										.getEntityUnitFunctionDesignationList(
												obj)
										.success(
												function(res, status) {
													if (res.errorMessage != "Failed") {
														// /
														// $scope.taskList=res.allTasks;
														console
																.log('res'
																		+ JSON
																				.stringify(res));
														// $scope.originalTaskList=res.allTasks;
														$scope.originalEntityList = res.Entity;
														$scope.entityList = res.Entity;
														$scope.originalUnitList = res.Unit;
														$scope.originalFunctionList = res.Function;

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

								// /Add executor and Evaluator and function head
								$scope.executorList = [];
								$scope.evaluatorList = [];
								$scope.functionheadList = [];
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

								// /Add executor and Evaluator and function head
								$scope.executorList = [];
								$scope.evaluatorList = [];
								$scope.functionheadList = [];

							}

							// Get Executor evalutor function head list
							$scope.getFunctionDependentArray = function() {
								$scope.executorList = [];
								$scope.evaluatorList = [];
								$scope.functionheadList = [];
								if ($scope.tmapObj.tmap_orga_id != ""
										&& $scope.tmapObj.tmap_loca_id != ""
										&& $scope.tmapObj.tmap_dept_id != ""
										&& $scope.originalExecutorList.length != 0) {
									angular
											.forEach(
													$scope.originalExecutorList,
													function(item) {
														if ((item.orga_id == $scope.tmapObj.tmap_orga_id)
																&& (item.loca_id == $scope.tmapObj.tmap_loca_id)
																&& (item.dept_id == $scope.tmapObj.tmap_dept_id)) {
															$scope.executorList
																	.push(item);
															console
																	.log("item==="
																			+ JSON
																					.stringify(item));
														}

													});
								}
								;

								if ($scope.tmapObj.tmap_orga_id != ""
										&& $scope.tmapObj.tmap_loca_id != ""
										&& $scope.tmapObj.tmap_dept_id != ""
										&& $scope.originalEvaluatorList.length != 0) {
									angular
											.forEach(
													$scope.originalEvaluatorList,
													function(item) {
														if ((item.orga_id == $scope.tmapObj.tmap_orga_id)
																&& (item.loca_id == $scope.tmapObj.tmap_loca_id)
																&& (item.dept_id == $scope.tmapObj.tmap_dept_id)) {
															$scope.evaluatorList
																	.push(item);
														}

													});
								}
								;

								if ($scope.tmapObj.tmap_orga_id != ""
										&& $scope.tmapObj.tmap_loca_id != ""
										&& $scope.tmapObj.tmap_dept_id != ""
										&& $scope.originalFunctionHeadList.length != 0) {
									angular
											.forEach(
													$scope.originalFunctionHeadList,
													function(item) {
														if ((item.orga_id == $scope.tmapObj.tmap_orga_id)
																&& (item.loca_id == $scope.tmapObj.tmap_loca_id)
																&& (item.dept_id == $scope.tmapObj.tmap_dept_id)) {
															$scope.functionheadList
																	.push(item);
														}

													});
								}
								;
							}

							// assginTask
							$scope.assginTask = function(formValid) {
								if (formValid && $scope.tmapObj.tasks_list.length == 0) {
									toaster.warning("Select task");
								}
								if (formValid && $scope.tmapObj.tasks_list.length > 0) {
									spinnerService.show('html5spinner');
									$scope.locaListArray = [];
									if ($scope.tmapObj.tmap_loca_id.length > 0) {
										for (var i = 0; i < $scope.tmapObj.tmap_loca_id.length; i++) {
											// alert($scope.tmapObj.tmap_loca_id[i]);
											$scope.obj = {
												tmap_loca_id : $scope.tmapObj.tmap_loca_id[i]
											}
											$scope.locaListArray.push($scope.obj);
										}
									}

									$scope.obj = {
										"orga_id" : $scope.tmapObj.tmap_orga_id,
										"loca_list" : $scope.locaListArray,
										"dept_id" : $scope.tmapObj.tmap_dept_id,
										"pr_user_id" : $scope.tmapObj.tmap_pr_user_id,
										"rw_user_id" : $scope.tmapObj.tmap_rw_user_id,
										"fh_user_id" : $scope.tmapObj.tmap_fh_user_id,
										"tasks_list" : $scope.tmapObj.tasks_list
									}
									

									// DataFactory.setShowLoader(true);
									ApiCallFactory.assginTask($scope.obj).success(
													function(res, status) {
														spinnerService.hide('html5spinner');
														// DataFactory.setShowLoader(false);
														if (status === 200 && res.responseMessage != "Failed") {
															toaster.success("Success","Task assigned successfully");
															$scope.taskSearchForm.$setPristine();
															$scope.taskSearchForm.$setUntouched();

															$scope.assignTaskForm.$setPristine();
															$scope.assignTaskForm.$setUntouched();
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
																country_id : null,
																state_id : null,
																cat_id : null,
																legi_id : null,
																rule_id : null,
																orga_id : null,
																loca_id : null
															};
															// $window.location.reload();
															$scope.searchObj.searching_for = "tasksmapping";
															$scope.searchObj.ChooseSOrC = 'central';
															$scope.taskList = [];
															$scope.selectedAll = false;
															$scope.legiList = {
																selected : ""
															};
															$scope.legiRuleL = {
																selected : ""
															};

														} else {
															toaster
																	.error(
																			"Failed",
																			"Error in assginTask");
														}
													})
											.error(
													function(error) {
														DataFactory
																.setShowLoader(false);
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
							
							$scope.getEntityListForSearch = function() {
								spinnerService.show('html5spinner');
								$http({
									url : "./getentitylist",
									method : "get",
								}).then(function(result) {
									spinnerService.hide('html5spinner');
									$scope.entityListForSearch = result.data;
								}, function(result) {
									spinnerService.hide('html5spinner');
								});

							}

							$scope.getEntityListForSearch();
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
							
							$scope.getUnitListByEntityOnSearch = function(entity) {
								spinnerService.show('html5spinner');
								$http({
									url : "./getunitlist",
									method : "get",
									params : {
										'entity_id' : entity
									}
								}).then(function(result) {
									spinnerService.hide('html5spinner');
									$scope.unitListOnSearch = result.data;
								}, function(result) {
									spinnerService.hide('html5spinner');
								});

							}


							$scope.getFunctionListByUnit = function() {
								// spinnerService.show('html5spinner');

								$scope.locaListArray = [];
								if ($scope.tmapObj.tmap_loca_id.length > 0) {
									for (var i = 0; i < $scope.tmapObj.tmap_loca_id.length; i++) {
										// alert($scope.tmapObj.tmap_loca_id[i]);
										$scope.obj = {
											tmap_loca_id : $scope.tmapObj.tmap_loca_id[i]
										}
										$scope.locaListArray.push($scope.obj);
									}
								}

								$scope.obj = {
									"orga_id" : $scope.tmapObj.tmap_orga_id,
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
								if ($scope.tmapObj.tmap_loca_id.length > 0) {
									for (var i = 0; i < $scope.tmapObj.tmap_loca_id.length; i++) {
										// alert($scope.tmapObj.tmap_loca_id[i]);
										$scope.obj = {
											tmap_loca_id : $scope.tmapObj.tmap_loca_id[i]
										}
										$scope.locaListArray.push($scope.obj);
									}
								}

								$scope.obj = {
									"orga_id" : $scope.tmapObj.tmap_orga_id,
									"loca_list" : $scope.locaListArray,
									"dept_id" : $scope.tmapObj.tmap_dept_id
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
								if ($scope.tmapObj.tmap_loca_id.length > 0) {
									for (var i = 0; i < $scope.tmapObj.tmap_loca_id.length; i++) {
										// alert($scope.tmapObj.tmap_loca_id[i]);
										$scope.obj = {
											tmap_loca_id : $scope.tmapObj.tmap_loca_id[i]
										}
										$scope.locaListArray.push($scope.obj);
									}
								}

								$scope.obj = {
									"orga_id" : $scope.tmapObj.tmap_orga_id,
									"loca_list" : $scope.locaListArray,
									"dept_id" : $scope.tmapObj.tmap_dept_id
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
								if ($scope.tmapObj.tmap_loca_id.length > 0) {
									for (var i = 0; i < $scope.tmapObj.tmap_loca_id.length; i++) {
										// alert($scope.tmapObj.tmap_loca_id[i]);
										$scope.obj = {
											tmap_loca_id : $scope.tmapObj.tmap_loca_id[i]
										}
										$scope.locaListArray.push($scope.obj);
									}
								}

								$scope.obj = {
									"orga_id" : $scope.tmapObj.tmap_orga_id,
									"loca_list" : $scope.locaListArray,
									"dept_id" : $scope.tmapObj.tmap_dept_id
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

							$scope.getTaskList = function(formValid) {

								if ($scope.legiList.selected != undefined) {
									$scope.searchObj.legi_id = $scope.legiList.selected.legi_id;
								} else {
									$scope.searchObj.legi_id = 0;
								}
								if ($scope.legiRuleL.selected != undefined) {
									$scope.searchObj.rule_id = $scope.legiRuleL.selected.rule_id;
								} else {
									$scope.searchObj.rule_id = 0;
								}
								if (formValid) {
									spinnerService.show('html5spinner');
									$scope.tmapObj = {
										tmap_dept_id : null,
										tmap_loca_id : null,
										tmap_orga_id : null,
										tmap_pr_user_id : null,
										tmap_rw_user_id : null,
										tmap_fh_user_id : null,
										tasks_list : []
									};

									ApiCallFactory
											.getTaskListToAssign(
													$scope.searchObj)
											.success(
													function(res, status) {
														spinnerService
																.hide('html5spinner');
														if (status == 200) {
															if (res.errorMessage != "Failed") {
																$scope.taskList = res.allTasks;
															} else {
																console
																		.log(" getTaskList =====error");
															}
														}
													})
											.error(
													function(error) {
														spinnerService
																.hide('html5spinner');
														console
																.log(" getTaskList ====="
																		+ error);
													});
								}

							}
							
							$scope.importAssignTask = function(){
								$state.transitionTo('importAssignTask',{'entity_name': null,'loca_name':null,'dept_name':null,'task_id':null,'exe_name':null,'eval_name':null,'funHead_name':null});
							}
						} ]);