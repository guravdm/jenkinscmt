'use strict';

CMTApp
		.controller(
				'complianceCalendarController',
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
						'$compile',
						'spinnerService',
						'$http',
                        '$window',
                        '$mdDialog',
						function($scope, $rootScope, $stateParams, $state,
								ApiCallFactory, $location, DataFactory,
								Storage, toaster, $compile, spinnerService,
								$http,$window,$mdDialog) {
							$scope.searchObj = {};
							$scope.configObj = {};
							$scope.deleteDocument = {};
							// $scope.searchObj.searching_for="taskConfiguration";
							$scope.Events = [];
							$scope.configObj.dataRequiredFor = "taskEnabling";
							$scope.originalTaskListt = [];
							$scope.originalEntityListt = [];
							$scope.originalUnitListt = [];
							$scope.originalFunctionListt = [];
							$scope.originalExecutorListt = [];
							$scope.originalEvaluatorListt = [];
							$scope.executorListt = [];
							$scope.evaluatorListt = [];
							
							//For multiple task completion
							$scope.multipleCompletion = {};
							$scope.sub_task_id = null;
							$scope.client_task_id = null;
							
							$scope.format = 'dd-MM-yyyy'; //$scope.formats[0];
							$scope.maxDate = new Date();
							 $scope.subtaskcompetedDate = function() {
								    $scope.popup_completed_date.opened = true;
								  };
							 
							 $scope.popup_completed_date = {
									   opened: false
									  };
								  
							  $scope.nextExamination = function() {
								    $scope.popup_next_examination.opened = true;
								   };
								   
							$scope.popup_next_examination = {
								   opened: false
								  };
							
							
							$scope.task = {};
							$scope.proofCompliance = {};
							$scope.task_history = {};
							$scope.subTaskHistory = {};
							//$scope.task_history.documents = {};
							
							//$scope.eventNotOccured=false;
							
							$scope.task_details = [];
							$scope.taskCompletion = {};
							$scope.subTaskCompletion = {};
							$scope.reOpen = {};
							$scope.reOpenForSubtask = {};
							$scope.taskCompletion.ttrn_completed_date = new Date();
							$scope.currentDate = new Date();
							console.log("Todays Date is : "+$scope.currentDate);
							
							//$scope.taskCompletion.ttrn_ids = [];
							$scope.task_ttrn_ids = [];
							
							$scope.statusDetailsClick = 0;
							
					
							$scope.today = function () {
						        $scope.taskCompletion.ttrn_completed_date = new Date();
						    };
						    $scope.mindate = new Date();
						    $scope.dateformat="dd-MM-yyyy";
						    $scope.today();
						    $scope.showcalendar = function ($event) {
						        $scope.showdp = true;
						    };
						    $scope.showdp = false;
							

							$scope.getEntityUnitFunctionDesignationList = function() {
								// var obj={}
								ApiCallFactory
										.getTaskList($scope.configObj)
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
								if ($scope.configObj.tmap_orga_id != ""
										&& $scope.originalUnitListt.length != 0) {
									angular
											.forEach(
													$scope.originalUnitListt,
													function(item) {
														if (item.orga_id == $scope.configObj.tmap_orga_id) {
															$scope.unitListt
																	.push(item);
														}

													});
								}
								;

								// /Add executor and Evaluator and function head
								$scope.executorListt = [];
								$scope.evaluatorListt = [];
								$scope.functionheadListt = [];
							}

							$scope.getUnitDependentArrayy = function() {
								$scope.functionListt = [];
								if ($scope.configObj.tmap_orga_id != ""
										&& $scope.configObj.tmap_loca_id != ""
										&& $scope.originalFunctionListt.length != 0) {
									angular
											.forEach(
													$scope.originalFunctionListt,
													function(item) {
														if ((item.orga_id == $scope.configObj.tmap_orga_id)
																&& (item.loca_id == $scope.configObj.tmap_loca_id)) {
															$scope.functionListt
																	.push(item);
														}
														// console.log('FunctionList
														// '+$scope.functionListt);

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
								if ($scope.configObj.tmap_loca_id != ""
										&& $scope.configObj.tmap_loca_id != ""
										&& $scope.configObj.tmap_dept_id != ""
										&& $scope.originalExecutorListt.length != 0) {
									angular
											.forEach(
													$scope.originalExecutorListt,
													function(item) {
														if ((item.orga_id == $scope.configObj.tmap_orga_id)
																&& (item.loca_id == $scope.configObj.tmap_loca_id)
																&& (item.dept_id == $scope.configObj.tmap_dept_id)) {
															// console.log('In
															// IF');
															$scope.executorListt
																	.push(item);

														}
														// console.log('orga
														// id:'+item.orga_id+'
														// loca id
														// :'+item.loca_id+'
														// dept id:
														// '+item.dept_id+'
														// username:
														// '+item.user_name+'
														// user id:
														// '+item.user_id);
														// console.log('executorListt
														// '+$scope.executorListt);
													});
								}

								if ($scope.configObj.tmap_loca_id != ""
										&& $scope.configObj.tmap_loca_id != ""
										&& $scope.configObj.tmap_dept_id != ""
										&& $scope.originalEvaluatorListt.length != 0) {
									angular
											.forEach(
													$scope.originalEvaluatorListt,
													function(item) {
														if ((item.orga_id == $scope.configObj.tmap_orga_id)
																&& (item.loca_id == $scope.configObj.tmap_loca_id)
																&& (item.dept_id == $scope.configObj.tmap_dept_id)) {
															// $scope.executorListt.push(item);
															$scope.evaluatorListt
																	.push(item);
														}
														// console.log('orga
														// id:'+item.orga_id+'
														// loca id
														// :'+item.loca_id+'
														// dept id:
														// '+item.dept_id+'
														// username:
														// '+item.user_name+'
														// user id:
														// '+item.user_id);
														// console.log('evaluatorListt
														// '+$scope.evaluatorListt);
													});
								}

							}
							$scope.originalTask = [];
							// get all calendar date list
							$scope.getCalendarDateList = function() {
								spinnerService.show('html5spinner');
								ApiCallFactory
										.getCalendarDateList($scope.configObj)
										.success(
												function(res, status) {
													spinnerService
															.hide('html5spinner');
													if (status === 200) {
														console.log(JSON.stringify(res.task_assigned_to_user));
														$scope.originalTask = res.task_assigned_to_user;
														angular.forEach(
																		res.task_assigned_to_user,
																		function(
																				item) {
																			var task_type = 'blue';
																			var text_color = 'white';

																			/*-------------User Role wise set color------------*/
																			if (item.task_which_entity_wise == "Department Wise") {
																				task_type = "#FFBF00"; // red
																				// event.borderColor
																				// =
																				// "#FFBF00";
																				// //red
																			}
																			if (item.task_which_entity_wise == "Performer Wise") {
																				task_type = "#0000FF";
																				// event.borderColor
																				// =
																				// "white";//event.backgroundColor
																				// =
																				// "#01DF01";
																				// event.borderColor
																				// =
																				// "white";//event.borderColor
																				// =
																				// "#FFBF00";
																			}
																			if (item.task_which_entity_wise == "Reviewer Wise") {
																				task_type = "#0101DF";
																				// event.borderColor
																				// =
																				// "#0101DF";
																			}
																			/*-------------User Role wise set color------------*/
																			/*-------------legal status wise set color------------*/

																			if (item.task_status == "Delayed") {
																				task_type = "#BFBFBF";
																				// event.borderColor
																				// =
																				// "#BFBFBF";
																			}
																			
																			if (item.task_status == "Delayed_Reported") {
																				task_type = "#bfd630";
																				text_color = 'black';
																			}
																			
																			if (item.task_status == "Complied") {
																				task_type = "#00CC33";
																				// event.borderColor
																				// =
																				// "#00CC33";
																			}
																			if (item.task_status == "Non Complied") {
																				task_type = "#FF0000";
																				// event.borderColor
																				// =
																				// "#FF0000";
																			}
																			if (item.task_status == "Posing Risk") {
																				/*
																				 * event.backgroundColor =
																				 * "#FFFF00";
																				 * event.borderColor =
																				 * "#FFFF00";
																				 */
																				task_type = "#FFFF00";
																				text_color = 'black';
																				// event.borderColor
																				// =
																				// "#FFFF00";
																				// event.textColor
																				// =
																				// "black";
																			}
																			if (item.task_status == "WaitingForApproval") {
																				/*
																				 * event.backgroundColor =
																				 * "#FFFF00";
																				 * event.borderColor =
																				 * "#FFFF00";
																				 */
																				task_type = "#499ed6";
																				text_color = 'white';
																				// event.borderColor
																				// =
																				// "#FFFF00";
																				// event.textColor
																				// =
																				// "black";
																			}
																			if (item.task_status == "Reopned") {
																				/*
																				 * event.backgroundColor =
																				 * "#FFFF00";
																				 * event.borderColor =
																				 * "#FFFF00";
																				 */
																				task_type = "#f9ac31";
																				text_color = 'white';
																				// event.borderColor
																				// =
																				// "#FFFF00";
																				// event.textColor
																				// =
																				// "black";
																			}
																			/*-------------End legal status wise set color------------*/

																			var task_id = "";
																			var sub_task_id = null;
																			if (item.type == "MainTask")
																				task_id = "Task Id ["
																						+ item.client_task_id
																						+ "]";
																			else
																				task_id = "Task Id ["
																						+ item.task_sub_task_id
																						+ "]";

																			if(item.task_sub_task_id != null)
																				sub_task_id = item.task_sub_task_id;
																			else
																				sub_task_id = null;
																			/*
																			 * var
																			 * obj={
																			 * id:
																			 * item.client_task_id,
																			 * title:
																			 * task_id,
																			 * start:
																			 * new
																			 * Date(item.calender_date), //
																			 * url:
																			 * "https://google.com/",
																			 * tooltip :
																			 * item.task_activity,
																			 * allDay:
																			 * true,
																			 * color:
																			 * task_type,
																			 * textColor:
																			 * text_color }
																			 * $scope.Events.push(obj);
																			 */
																			$scope.Events
																					.push({
																						id : item.client_task_id,
																						sub_task_id : sub_task_id,
																						title : task_id,
																						start : new Date(
																								item.calender_date
																										.replace(
																												/ /g,
																												'T')),
																						// url:
																						// "https://google.com/",
																						tooltip : item.task_activity,
																						allDay : true,
																						color : task_type,
																						textColor : text_color
																					});
																		});
													} else {
														// toaster.error("Failed",
														// "Error in get main
														// entity list");
													}
												})
										.error(
												function(error) {
													// DataFactory.setShowLoader(false);
													spinnerService
															.hide('html5spinner');
													console.log("Get Date===="
															+ error);
												});
							}
							$scope.getCalendarDateList();

							$scope.uiConfig = {
								calendar : {
									header : {
										left : 'month basicWeek basicDay',
										center : 'title',
										right : 'today prev,next'
									},
									events : $scope.Events,
									eventRender : function(event, element, view) {
										element.attr({
											"tooltip-placement" : "top",
											'uib-tooltip' : event.tooltip,
											'tooltip-append-to-body' : true
										});
										$compile(element)($scope);
									},
									eventClick : function(event) {
										/*$state.transitionTo('taskDetails', {
											'task_id' : event.id
										});*/
										//alert(event.sub_task_id);
										$scope.getTaskHistoryListForCalender(event.id,event.sub_task_id);
										$scope.getTaskDetails(event.id);
										$("#changeTask").modal();
									}
								}
							};

							$scope.searchTask = function() {
								spinnerService.show('html5spinner');
								var orga_id = $scope.configObj.orga_id;
								var loca_id = $scope.configObj.loca_id;
								var dept_id = $scope.configObj.dept_id;
								var per_id = $scope.configObj.pr_user_id;
								var rev_id = $scope.configObj.rw_user_id;
								var fh_id = $scope.configObj.fh_user_id;
								
								$scope.Events = [];
								angular
										.forEach(
												$scope.originalTask,
												function(item) {
													spinnerService.hide('html5spinner');
													if ((!$scope.configObj.orga_id || item.task_orga_id === $scope.configObj.orga_id)
															&& (!$scope.configObj.loca_id || item.task_loca_id === $scope.configObj.loca_id)
															&& (!$scope.configObj.dept_id || item.task_dept_id === $scope.configObj.dept_id)
															&& (!$scope.configObj.rw_user_id || item.task_reviewer_id === $scope.configObj.rw_user_id)
															&& (!$scope.configObj.pr_user_id || item.task_performer_id === $scope.configObj.pr_user_id)
															&& (!$scope.configObj.fh_user_id || item.task_function_head_id === $scope.configObj.fh_user_id)) {
														console
																.log("ORGA_ID "
																		+ item.task_orga_id);
														console
																.log("LOCA_ID "
																		+ item.task_loca_id);
														console
																.log("DEPT_ID "
																		+ item.task_dept_id);
														console
																.log("Rev_ID "
																		+ item.task_reviewer_id);
														console
																.log("Per_ID "
																		+ item.task_performer_id);

														var task_type = 'blue';
														var text_color = 'white';

														/*-------------User Role wise set color------------*/
														if (item.task_which_entity_wise == "Department Wise") {
															task_type = "#FFBF00"; // red
														}
														if (item.task_which_entity_wise == "Performer Wise") {
															task_type = "#0000FF";
														}
														if (item.task_which_entity_wise == "Reviewer Wise") {
															task_type = "#0101DF";
														}
														/*-------------User Role wise set color------------*/
														/*-------------legal status wise set color------------*/

														if (item.task_status == "Delayed") {
															task_type = "#BFBFBF";
														}
														if (item.task_status == "Delayed_Reported") {
															task_type = "#bfd630";
														}
														if (item.task_status == "Complied") {
															task_type = "#00CC33";
														}
														if (item.task_status == "Non Complied") {
															task_type = "#FF0000";
														}
														if (item.task_status == "Posing Risk") {
															task_type = "#FFFF00";
															text_color = 'black';
														}
														if (item.task_status == "WaitingForApproval") {
															task_type = "#499ed6";
															text_color = 'white';
														}
														if (item.task_status == "Reopned") {
															task_type = "#f9ac31";
															text_color = 'white';
														}
														/*-------------End legal status wise set color------------*/

														var task_id = "";
														var sub_task_id = null;
														if (item.type == "MainTask")
															task_id = "Task Id ["
																	+ item.client_task_id
																	+ "]";
														else
															task_id = "Task Id ["
																	+ item.task_sub_task_id
																	+ "]";

														if(item.task_sub_task_id != null)
															sub_task_id = item.task_sub_task_id;
														else
															sub_task_id = null;

														$scope.Events
																.push({
																	id : item.client_task_id,
																	sub_task_id : sub_task_id,
																	title : task_id,
																	start : new Date(
																			item.calender_date
																					.replace(
																							/ /g,
																							'T')),
																	// url:
																	// "https://google.com/",
																	tooltip : item.task_activity,
																	allDay : true,
																	color : task_type,
																	textColor : text_color
																});

													}

												});
								if ($scope.Events.length == 0) {
									toaster.warning("No result found.");
								}

								$scope.uiConfig = {
									calendar : {
										header : {
											left : 'month basicWeek basicDay',
											center : 'title',
											right : 'today prev,next'
										},
										events : $scope.Events,
										eventRender : function(event, element,
												view) {
											element.attr({
												"tooltip-placement" : "top",
												'uib-tooltip' : event.tooltip,
												'tooltip-append-to-body' : true
											});
											$compile(element)($scope);
										},
										eventClick : function(event) {
											/*$state.transitionTo('taskDetails',
													{
														'task_id' : event.id
													});*/
											//alert(event.sub_task_id);
											$scope.getTaskHistoryListForCalender(event.id,event.sub_task_id);
											$scope.getTaskDetails(event.id);
											$("#changeTask").modal();
										}
									}
								};

								$("#loader").hide();

							}

							$scope.getEntityList = function() {
								spinnerService.show('html5spinner');
								$http({
									url : "./getentity",
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
									url : "./getunit",
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
							$scope.getFunctionListByUnit = function(unitList,orga_id) {
								$scope.functionList = [];
								var dept_array = [];
								spinnerService.show('html5spinner');
								$http({
									url : "./getFunction",
									method : "get",
									params : {
										'unit_id' : unitList,
										'orga_id' : orga_id
									}
								}).then(function(result) {
									spinnerService.hide('html5spinner');
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
							
							$scope.getExecutorEvaluatorList = function(){

								$scope.executorList=[];
								$scope.evaluatorList=[];
								$scope.functionHeadList=[];

								if($scope.configObj.orga_id!="" && $scope.configObj.loca_id == null && $scope.originalExecutor.length!=0){
									angular.forEach($scope.originalExecutor, function (item) {
										if((item.orga_id == $scope.configObj.orga_id)){
											$scope.executorList.push(item);
										}
										
									});
									
									angular.forEach($scope.originalEvaluator, function (item) {
										if((item.orga_id == $scope.configObj.orga_id)){
											$scope.evaluatorList.push(item);
										}
										
									});
									
									angular.forEach($scope.originalFunctionHead, function (item) {
										if((item.orga_id == $scope.configObj.orga_id)){
											$scope.functionHeadList.push(item);
										}
										
									});
								}
								if($scope.configObj.orga_id!="" && $scope.configObj.loca_id != null && $scope.configObj.dept_id == null && $scope.originalExecutor.length!=0){
									angular.forEach($scope.originalExecutor, function (item) {
										if((item.orga_id == $scope.configObj.orga_id && item.loca_id == $scope.configObj.loca_id)){
											$scope.executorList.push(item);
										}
									});
									
									angular.forEach($scope.originalEvaluator, function (item) {
										if((item.orga_id == $scope.configObj.orga_id && item.loca_id == $scope.configObj.loca_id)){
											$scope.evaluatorList.push(item);
										}
										
									});
									
									angular.forEach($scope.originalFunctionHead, function (item) {
										if((item.orga_id == $scope.configObj.orga_id && item.loca_id == $scope.configObj.loca_id)){
											$scope.functionHeadList.push(item);
										}
										
									});
								}
								
								if($scope.configObj.orga_id!="" && $scope.configObj.loca_id != null && $scope.configObj.dept_id != null && $scope.originalExecutor.length!=0){
									angular.forEach($scope.originalExecutor, function (item) {
										if((item.orga_id == $scope.configObj.orga_id && item.loca_id == $scope.configObj.loca_id && item.dept_id == $scope.configObj.dept_id)){
											$scope.executorList.push(item);
										}
									});
									
									angular.forEach($scope.originalEvaluator, function (item) {
										if((item.orga_id == $scope.configObj.orga_id && item.loca_id == $scope.configObj.loca_id && item.dept_id == $scope.configObj.dept_id)){
											$scope.evaluatorList.push(item);
										}
										
									});
									
									angular.forEach($scope.originalFunctionHead, function (item) {
										if((item.orga_id == $scope.configObj.orga_id && item.loca_id == $scope.configObj.loca_id && item.dept_id == $scope.configObj.dept_id)){
											$scope.functionHeadList.push(item);
										}
										
									});
								}
										
								
							
							}
							$scope.getExeEvalFunctionHeadList = function(){
								 spinnerService.show('html5spinner');
									ApiCallFactory.getExeEvalFunHeadList($scope.configObj).success(function(res,status){
										spinnerService.hide('html5spinner');
										if(status==200){
										$scope.originalExecutor   	= res.OrganogramFilter[0].Executor;
										$scope.originalEvaluator   	= res.OrganogramFilter[0].Evaluator;
										$scope.originalFunctionHead   	= res.OrganogramFilter[0].FunctionHead;
													
										 $scope.getExecutorEvaluatorList(); 									
										}
									}).error(function(error){
										spinnerService.hide('html5spinner');
										console.log("get repository list====="+error);
									});
								};	
							
								
								$scope.getTaskHistoryList=function(task_id){																
									var obj={tmap_client_task_id:task_id};
									//alert(JSON.stringify(obj));
									if(!angular.isUndefined(task_id) && task_id!=0){
										ApiCallFactory.getTasksHistory(obj).success(function(res,status){
											$scope.task_history = res.task_history;
											$scope.subTaskHistory = res.subTaskHistory;
											
											$scope.editDate={
													role:Storage.get('userDetais.sess_role_id'),
											}
											//console.log("role "+$scope.editDates.role);

											//$scope.task_history.documents = 
										}).error(function(error){
											console.log("task History====="+error);
										});
								      }
								}
							
								$scope.getTaskDetails=function(task_id){ 
									//alert($scope.statusDetailsClick);
									if($scope.statusDetailsClick==0){ //If click status is 0 then only get details from DB
										//spinnerService.show('html5spinner');
									var obj={tmap_client_task_id:task_id};
									//alert(JSON.stringify(obj));
									if(!angular.isUndefined(task_id) && task_id!=0){
										ApiCallFactory.getTasksDetails(obj).success(function(res,status){
											spinnerService.hide('html5spinner');
											$scope.task_details = res;
											$scope.statusDetailsClick = 0;
										}).error(function(error){
											spinnerService.hide('html5spinner');
											console.log("task Details====="+error);
										});
								      }
									}
								}
								
								 $scope.showEventNotOccured = true;
								  $scope.eventCheked = function (res){
									
									  if(res){
										  $scope.showEventNotOccured = false;
										  $scope.taskCompletion.ttrn_event_not_occure="Yes";
										  $scope.taskCompletion.ttrn_performer_comments = "NA";
										  $scope.taskCompletion.ttrn_reason_for_non_compliance = "NA";
									  }
									  else{
										  $scope.showEventNotOccured = true;
										  $scope.taskCompletion.ttrn_event_not_occure="No";
									  }
								  }
								
								//get task history
								$scope.CompleteTaskPage=function(ttrn_id,document,comments,completedDate,status,reason,frequency,legalDueDate,clientTaskId){ //document contain 1 - mandatory doc and 0 - Non mandatory
									//alert(clientTaskId);									
									$scope.task.task_id=clientTaskId;									
									$scope.task.ttrn_id=ttrn_id;
									
									$scope.task_iiidddd = clientTaskId;
									$scope.ttrrrnnnn_id = ttrn_id;
									
									$scope.task.document=document;						
									$scope.taskCompletion.ttrn_performer_comments=comments;									
									$scope.status=status;
									console.log("staus is "+$scope.status);									
									$scope.frequency=frequency;
									console.log("frequency is"+$scope.frequency);
									$scope.stringifyTaskCompletion = {};
									var legal =legalDueDate.split("-");
									var legalDueDate = new Date(legal[2], legal[1] - 1, legal[0]);
									$scope.legalDueDate= legalDueDate;
								
									console.log("Legal Due Date is : "+$scope.legalDueDate);	
									if($scope.taskCompletion.ttrn_reason_for_non_compliance == null || $scope.taskCompletion.ttrn_reason_for_non_compliance){
										$scope.taskCompletion.ttrn_reason_for_non_compliance = '';
									}else
										$scope.taskCompletion.ttrn_reason_for_non_compliance=reason;
									console.log("reason:" +$scope.taskCompletion.ttrn_reason_for_non_compliance);
									
									/*$scope.today = function () {
								        $scope.taskCompletion.ttrn_completed_date = new Date();
								    };*/
									if(completedDate!=0 ){
										//alert(completedDate);
										var compDate =completedDate.split("-");
										//alert("Completed Date : "+ new Date(compDate[2],compDate[1]-1,compDate[0]));
										$scope.taskCompletion.ttrn_completed_date = new Date(compDate[2],compDate[1]-1,compDate[0])
									}else{
											$scope.taskCompletion.ttrn_completed_date = new Date();
									}
									$("#taskCompletionLoader").modal('hide');
									 $scope.checkAllMultipleCompletion();
									$("#completeTask").modal();
									$scope.gettaskformultiplecompletion($scope.task_iiidddd,$scope.ttrrrnnnn_id);
									//$window.open('completeTask', {'ttrn_id':ttrn_id,'task_id':clientTaskId,'document':document,'comments':comments,'completedDate':completedDate,'status':status,'reason':reason,'frequency':frequency,'legalDueDate':legalDueDate});
								};
			
								$scope.saveCompletion= function(formValid){
									
									if(formValid && !angular.isUndefined($scope.task.ttrn_id) && $scope.task.ttrn_id!=0){
										$("#taskCompletionLoader").modal();
										spinnerService.show('html5spinner');
										var obj= {ttrn_id:$scope.task.ttrn_id};
										$scope.task_ttrn_ids.push(obj);
										
										$scope.taskCompletion.ttrn_ids = $scope.task_ttrn_ids;
										$scope.taskCompletion.ttrn_completed_date = moment($scope.taskCompletion.ttrn_completed_date).format('DD-MM-YYYY');
										
										if($scope.taskCompletion.ttrn_reason_for_non_compliance == null || $scope.taskCompletion.ttrn_reason_for_non_compliance){
											$scope.taskCompletion.ttrn_reason_for_non_compliance = '';
										}else
											$scope.taskCompletion.ttrn_reason_for_non_compliance=$scope.taskCompletion.ttrn_reason_for_non_compliance;
										
										//console.log("check box value "+$scope.showEventNotOccured);
										if($scope.showEventNotOccured==true){
											 $scope.taskCompletion.ttrn_event_not_occure="No";
										}
									//	$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance; 
										$scope.stringifyTaskCompletion= JSON.stringify($scope.taskCompletion)
										//DataFactory.setShowLoader(true);
										var formData = new FormData();
										if(!angular.isUndefined($scope.proofCompliance.ttrn_proof_of_compliance)){
											for (var i=0; i<$scope.proofCompliance.ttrn_proof_of_compliance.length; i++) {
												formData.append("ttrn_proof_of_compliance", $scope.proofCompliance.ttrn_proof_of_compliance[i]);
											}
										}  
										
										
										///formData.append("ttrn_proof_of_compliance",$scope.proofCompliance.ttrn_proof_of_compliance);
										formData.append("jsonString",$scope.stringifyTaskCompletion);
										
										ApiCallFactory.saveTasksCompletion(formData).success(function(res,status){
											//alert($scope.task.task_id);
											spinnerService.hide('html5spinner');
											if(res.responseMessage=='Success'){
												toaster.success("Success", "Task completed successfully.");
												$('#completeTask').modal('hide');
												$('#ttrn_proof_of_compliance').val('');
												$("#taskCompletionLoader").modal('hide');
												$scope.task_ttrn_ids = [];
												$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
												$scope.getTaskDetails($scope.task.task_id);
												$scope.stringifyTaskCompletion = {};
												$("#changeTask").modal();
												//location.reload();
												
												//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
											}else if(res.responseMessage == 'Invalid File Type'){
												toaster.error("Invalid File Type");
												$("#taskCompletionLoader").modal('hide');
											}else{
												toaster.error("Failed", "Please try again.");
												$("#taskCompletionLoader").modal('hide');
											}
										}).error(function(error){
											spinnerService.hide('html5spinner');
											console.log("task Completion====="+error);
										});
										
										
									}
								}
								
								$scope.deleteTask=function(ttrn_id,client_task_id,ev,index){
									var isConfirmed = confirm("Are you sure to delete this record ?");
								    
									if (isConfirmed) {
								    	$scope.taskCompletion.ttrn_id			  = ttrn_id;
								    	$scope.taskCompletion.ttrn_client_task_id = client_task_id;
								      ApiCallFactory.deleteTaskHistory($scope.taskCompletion).success(function(res,status){
								  			if(status === 200 && res.responseMessage == "Success"){
								  				toaster.success("Success", "Task deleted successfully.");
								  				$scope.task_history.splice(index,1);
								  			}else{
								  				toaster.error("Faild", "Please try again");
								  		}	
								  		}).error(function(error){
											console.log("Error while deleting the task====="+error);
										});
								    } else {
								    	return false;
								    }
								}
							 
								 $scope.nonComplianceReason={
										 ttrn_id : 0,
										 ttrn_reason_for_non_compliance : "",
								 }
								  $scope.reasonForNonCompliance=function(data){
									  //$scope.taskCompletion.ttrn_id=ttrn_id;
									  $scope.nonComplianceReason.ttrn_id = data.ttrn_id;
									  //console.log("TTRN ID "+$scope.nonComplianceReason.ttrn_id);
										  	
								  }
								 $scope.sendReasonForNonCompliance=function(formValid){
									 //console.log("ID for noncompliance: "+$scope.nonComplianceReason.ttrn_id);
									 //console.log("Comment for noncompliance: "+$scope.nonComplianceReason.ttrn_reason_for_non_compliance);
									 if(formValid){
											ApiCallFactory.updateTaskReasonForNonCompliance($scope.nonComplianceReason).success(function(res,status){
												if(status === 200 && res.responseMessage == "Success"){
													//$scope.reOpen.reopen_comment="";	
													$('#resonForNonCompliance').modal("hide");
													toaster.success("Success", "Reason For NonCompliance submitted successfully.");
													//$window.location.reload();
												}
												
											}).error(function(error){
												console.log("Error while approving the task====="+error);
											});
									 }
								 }
								 
								 //get ttrn_id and reopening comments 	
								 $scope.reOpen={
										 ttrn_id : 0,
										 reopen_comment : "",
								 }
								  $scope.reopenTask=function(data){
									  //$scope.taskCompletion.ttrn_id=ttrn_id;
									  $scope.reOpen.ttrn_id = data.ttrn_id;
									  $scope.reOpen.client_task_id = data.client_task_id;
									  //console.log("TTRN ID "+$scope.taskCompletion.ttrn_id);
										  	
								  }
								
								 //Reopen Task
								 $scope.sendReOpenTask=function(formValid){
									 //alert("data");
									
											ApiCallFactory.reopenTask($scope.reOpen).success(function(res,status){
												if(status === 200 && res.responseMessage == "Success"){
													//$scope.reOpen.reopen_comment="";	
													$('#reOpenTask').modal("hide");
													$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
													$scope.getTaskDetails($scope.client_task_id);
													//$window.location.reload();
												}
												
											}).error(function(error){
												console.log("Error while approving the task====="+error);
											});
									 
								 }
								 
								//Approve task
								 $scope.approveTask=function(ttrn_id, client_task_id){
									// console.log(task_id);
									 $scope.taskCompletion.ttrn_id=ttrn_id;
								  		ApiCallFactory.approveTask($scope.taskCompletion).success(function(res,status){
								  			if(status === 200 && res.responseMessage == "Success"){
								  				toaster.success("Success", "Task approved successfully.");
								  				//$window.location.reload();
								  				$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
								  				$scope.getTaskDetails($scope.client_task_id);
								  			}else{
												alert(res.responseMessage);
											}
								  		}).error(function(error){
											console.log("Error while approving the task====="+error);
										});
								  	
								 }
								 
								  $scope.tmapObj = {};
								  $scope.NtmapObj = {};
								  $scope.errMessage = {};
								  $scope.getDates =function(){
								  		
								  	//var legal = new Date($scope.tmapObj.ttrn_legal_due_date);
										if($scope.tmapObj.ttrn_prior_days_buffer<0)
											$scope.tmapObj.ttrn_prior_days_buffer = 0;
											
										$scope.unit_date = new Date($scope.lega_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
										$scope.func_date = new Date($scope.unit_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
										$scope.eval_date = new Date($scope.func_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
										$scope.exec_date = new Date($scope.eval_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
										
										
									}
								 
								  $scope.editDates = function(data){
										//console.log("DATA "+JSON.stringify(data));
									//	alert(JSON.stringify(data));
									  $scope.NtmapObj.ttrn_client_task_id = data.client_task_id;
									  $scope.NtmapObj.ttrn_id = data.ttrn_id;
									  $scope.NtmapObj.ttrn_legal_date = data.ttrn_legal_due_date;
									  $scope.NtmapObj.ttrn_unit_date = data.ttrn_uh_due_date;
									  $scope.NtmapObj.ttrn_function_date = data.ttrn_fh_due_date;
									  $scope.NtmapObj.ttrn_evaluator_date = data.ttrn_rw_due_date;
									  $scope.NtmapObj.ttrn_executor_date = data.ttrn_pr_due_date;
									  $scope.NtmapObj.ttrn_performer_name = data.ttrn_performer_name;
									  $scope.NtmapObj.user_email = data.user_email;
										
									  
									  $('#editConfiguration').modal();
									  
								  }
								  
								  $scope.tmapObj ={};
								  $scope.updateDates = function(formValid){
									  //console.log("FORM "+formValid);
									  //console.log("Dates "+$scope.validateDates());
									  if($scope.validateDates() && formValid){
										  $scope.tmapObj.ttrn_id                = $scope.NtmapObj.ttrn_id;
										  $scope.tmapObj.ttrn_legal_due_date 	= moment($scope.lega_date).format('DD-MM-YYYY');
										  $scope.tmapObj.ttrn_uh_due_date 		= moment($scope.unit_date).format('DD-MM-YYYY');
										  $scope.tmapObj.ttrn_fh_due_date 		= moment($scope.func_date).format('DD-MM-YYYY');
										  $scope.tmapObj.ttrn_rw_due_date 		= moment($scope.eval_date).format('DD-MM-YYYY');
											$scope.tmapObj.ttrn_pr_due_date 	= moment($scope.exec_date).format('DD-MM-YYYY');
											$scope.tmapObj.ttrn_performer_name  = $scope.NtmapObj.ttrn_performer_name;
											$scope.tmapObj.user_email =  $scope.NtmapObj.user_email
										  
										 // console.log("JSON "+JSON.stringify($scope.tmapObj));
										  
										//Update Task Configuration dates
										  ApiCallFactory.updateTasksConfiguration($scope.tmapObj).success(function(res,status){
											 // alert(  $scope.NtmapObj.ttrn_client_task_id);
											  spinnerService.hide('html5spinner');
												if(status === 200){
													$('#editConfiguration').modal("hide");
													toaster.success("Success", "Task dates updated successfully.");
													$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
													$scope.getTaskDetails($scope.NtmapObj.ttrn_client_task_id);
													$("#changeTask").modal();
												//	$window.location.reload();
												}else{
													toaster.error("Failed", "Error in dates.");
												}
											}).error(function(error){
												spinnerService.hide('html5spinner');
												console.log("update Configuration===="+error);
											});
										  
									  }
									  
									  
								  }
								  
								  $scope.validateSubTaskDates = function() {
								      // $scope.errMessage = '';
								       $scope.curDate = new Date();
								       var flag = 0;
								        $scope.ttrn_completed_date = new Date($scope.subTaskCompletion.ttrn_completed_date);
								        $scope.ttrn_next_examination_date = new Date($scope.subTaskCompletion.ttrn_next_examination_date);
										
								        if($scope.ttrn_completed_date >= $scope.curDate){ 
									       	$scope.errMessage.completed_date = 'Completed date should not be greater than todays  date.';
									       	flag = 1;
									         //return false;
									       }else{
									       	$scope.errMessage.completed_date = '';
									       }
								        
								       if($scope.ttrn_completed_date >= $scope.ttrn_next_examination_date){ 
								       	$scope.errMessage.next_examination_date = 'Next Examination date must be greater than completed date.';
								       	flag = 1;
								         //return false;
								       }else{
								       	$scope.errMessage.next_examination_date = '';
								       }
								       
								       //If all date valid then set true
								       if(flag==0){
								       	$scope.tmapObj.validate_dates = "TRUE";
								       	return true;
								       }else{
								       	$scope.tmapObj.validate_dates = null;
								       	return false;
								       }
								      
								   }
								$scope.subTaskComplete= function(data){
									
									 $scope.subTaskCompletion.ttrn_id = data.ttrn_id;
									 $scope.ttrn_task_status = data.ttrn_task_status;
									 $scope.ttrn_document = data.ttrn_document;
									 $scope.subTaskCompletion.ttrn_performer_comments = data.ttrn_performer_comments;
									 $scope.subTaskCompletion.ttrn_reason_for_non_compliance = data.ttrn_reason_for_non_compliance;
									 if($scope.ttrn_task_status == "Completed"){
										 var from = data.ttrn_completed_date.split("-");
										 var completedDate = new Date(from[2], from[1] - 1, from[0]);
										 $scope.subTaskCompletion.ttrn_completed_date = completedDate;
										 
										// var fromm = data.ttrn_next_examination_date.split("-");
										 //var completedDate = new Date(fromm[2], fromm[1] - 1, fromm[0]);
										// $scope.subTaskCompletion.ttrn_next_examination_date = completedDate;
									 }else{
										 $scope.subTaskCompletion.ttrn_completed_date = new Date();
											//$scope.subTaskCompletion.ttrn_next_examination_date = new Date();
									 } 
									
								}
								
								$scope.saveSubTaskCompletion= function(formValid){
									//alert("In Complete sub task");
									if(formValid && $scope.validateSubTaskDates()){
										$("#subTaskCompletionLoader").modal();
										spinnerService.show('html5spinner');
										var obj= {ttrn_sub_id:$scope.subTaskCompletion.ttrn_id};
										$scope.task_ttrn_ids.push(obj);
										
										if($scope.subTaskCompletion.ttrn_reason_for_non_compliance == undefined || $scope.subTaskCompletion.ttrn_reason_for_non_compliance == ""){
											$scope.subTaskCompletion.ttrn_reason_for_non_compliance = "";
										}else
											$scope.subTaskCompletion.ttrn_reason_for_non_compliance = $scope.subTaskCompletion.ttrn_reason_for_non_compliance;
										
										$scope.subTaskCompletion.ttrn_sub_ids = $scope.task_ttrn_ids;
										$scope.subTaskCompletion.ttrn_performer_comments = $scope.subTaskCompletion.ttrn_performer_comments;
										//$scope.subTaskCompletion.ttrn_reason_for_non_compliance = $scope.subTaskCompletion.ttrn_reason_for_non_compliance;
										$scope.subTaskCompletion.ttrn_completed_date = moment($scope.subTaskCompletion.ttrn_completed_date).format('DD-MM-YYYY');
										//$scope.subTaskCompletion.ttrn_next_examination_date = moment($scope.subTaskCompletion.ttrn_next_examination_date).format('DD-MM-YYYY');
										//console.log("check box value "+$scope.showEventNotOccured);
										
									//	$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance; 
										$scope.stringifyTaskCompletion= JSON.stringify($scope.subTaskCompletion)
										var formData = new FormData();
										if(!angular.isUndefined($scope.proofCompliance.ttrn_proof_of_compliance)){
											for (var i=0; i<$scope.proofCompliance.ttrn_proof_of_compliance.length; i++) {
												formData.append("ttrn_proof_of_compliance", $scope.proofCompliance.ttrn_proof_of_compliance[i]);
											}
										}  
										
										///formData.append("ttrn_proof_of_compliance",$scope.proofCompliance.ttrn_proof_of_compliance);
										formData.append("jsonString",$scope.stringifyTaskCompletion);
										
										ApiCallFactory.savesubtaskcompletion(formData).success(function(res,status){
											spinnerService.hide('html5spinner');
											if(res.responseMessage=='Success'){
												toaster.success("Success", "Task completed successfully.");
												$('#SubTaskCompletion').modal("hide");
												$("#subTaskCompletionLoader").modal('hide');
												//alert($scope.client_task_id);
												$scope.task_ttrn_ids = [];
												$('#ttrn_proof_of_compliance').val('');
												$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
								  				$scope.getTaskDetails($scope.client_task_id);
												$window.location.reload();
												//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
											}else{
												toaster.error("Failed", "Please try again.");
												$scope.task_ttrn_ids = [];
												$('#ttrn_proof_of_compliance').val('');
												$("#subTaskCompletionLoader").modal('hide');
											}
										}).error(function(error){
											spinnerService.hide('html5spinner');
											console.log("task Completion====="+error);
											$('#ttrn_proof_of_compliance').val('');
											$("#subTaskCompletionLoader").modal('hide');
										});
										
										
									}
								}
								 
								$scope.downloadProof = function(udoc_id , client_task_id){
									
									var obj={doc_id:udoc_id};
									ApiCallFactory.downloadProofOfCompliance(obj).success(function(res,status){
										//spinnerService.hide('html5spinner');
										console.log("responseMessage:" +res[0].responseMessage);
										if(res[0].responseMessage == "Failed") {
											toaster.error("Failed", "This file contains malicious data which can damage your machine.The file will not be downloaded and will be deleted from the System.");
											$scope.deleteDocument.udoc_id			  = udoc_id;
										      ApiCallFactory.deleteTaskDocument($scope.deleteDocument).success(function(res,status){
										  			if(status === 200 && res.responseMessage == "Success"){
										  				/*toaster.success("Success", "Document deleted successfully.");*/
										  				$scope.getTaskHistoryList(client_task_id);
										  				$scope.getTaskDetails(client_task_id);
										  			}else{
										  				toaster.error("Failed", "Please try again");
										  		}	
										  		}).error(function(error){
													console.log("Error while deleting the task====="+error);
												});
										} else if(res[0].responseMessage == "File Not Found") {
											toaster.error("File Not Found");
										} else {				
											$window.location = "./downloadProofOfCompliance?udoc_id="+udoc_id;
											toaster.success("Success", "Document downloaded successfully.");
											
										}
									}).error(function(error){
										toaster.error("Failed", "Please try again");
									});
									
							      //  $window.location = "./downloadProofOfCompliance?udoc_id="+udoc_id;
							       // $state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});
								
							      
								}
								
								/* Date Code start*/
								
								  $scope.inlineOptions = {
								    customClass: getDayClass,
								    minDate: new Date(),
								    showWeeks: true
								  };

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
									
									/*Date code end*/
								  
								  $scope.validateDates = function() {
								      // $scope.errMessage = '';
								       var curDate = new Date();
								       var flag = 0;
								        $scope.ttrn_lh_due_date = new Date($scope.lega_date);
								        $scope.ttrn_uh_due_date = new Date($scope.unit_date);
										$scope.ttrn_fh_due_date = new Date($scope.func_date);
										$scope.ttrn_rw_due_date = new Date($scope.eval_date);
										$scope.ttrn_pr_due_date = new Date($scope.exec_date);
								       
										if($scope.lega_date==undefined){
											$scope.errMessage.lh_date = 'Select Legal date.';
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
									
								       //If all date valid then set true
								       if(flag==0){
								       	$scope.tmapObj.validate_dates = "TRUE";
								       	return true;
								       }else{
								       	$scope.tmapObj.validate_dates = null;
								       	return false;
								       }
								      
								   }
								  
								  $scope.updateCompletion= function(formValid){

										if(formValid && !angular.isUndefined($scope.task.ttrn_id) && $scope.task.ttrn_id!=0){
											spinnerService.show('html5spinner');
											var obj= {ttrn_id:$scope.task.ttrn_id};
											$scope.task_ttrn_ids.push(obj);

											$scope.taskCompletion.ttrn_ids = $scope.task_ttrn_ids;
											$scope.taskCompletion.ttrn_completed_date = moment($scope.taskCompletion.ttrn_completed_date).format('DD-MM-YYYY');
											$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance;
											//console.log("check box value "+$scope.showEventNotOccured);
											if($scope.showEventNotOccured==true){
												$scope.taskCompletion.ttrn_event_not_occure="No";
											}
											//	$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance; 
											$scope.stringifyTaskCompletion= JSON.stringify($scope.taskCompletion)
											//DataFactory.setShowLoader(true);
											var formData = new FormData();
											if(!angular.isUndefined($scope.proofCompliance.ttrn_proof_of_compliance)){
												for (var i=0; i<$scope.proofCompliance.ttrn_proof_of_compliance.length; i++) {
													formData.append("ttrn_proof_of_compliance", $scope.proofCompliance.ttrn_proof_of_compliance[i]);
												}
											}  


											///formData.append("ttrn_proof_of_compliance",$scope.proofCompliance.ttrn_proof_of_compliance);
											formData.append("jsonString",$scope.stringifyTaskCompletion);

											ApiCallFactory.updateTasksCompletion(formData).success(function(res,status){
												//alert($scope.task.task_id);
												spinnerService.hide('html5spinner');
												if(res.responseMessage=='Success'){ 
													toaster.success("Success", "Task completed successfully.");
													$('#completeTask').modal('hide');	
													$scope.task_ttrn_ids = [];
													$('#ttrn_proof_of_compliance').val('');
													$scope.getTaskHistoryList($scope.task.task_id);
													$scope.getTaskDetails($scope.task.task_id);

													$("#changeTask").modal();

													//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
												}else{
													toaster.error("Failed", "Please try again.");
													$('#ttrn_proof_of_compliance').val('');
												}
											}).error(function(error){
												spinnerService.hide('html5spinner');
												console.log("task Completion====="+error);
											});


										}
									}
								  
								  $scope.deleteTaskDocument=function(udoc_id,client_task_id,ev,index){
									   /* var confirm = $mdDialog.confirm()
									          .title('Are you sure you want to delete the document?')
									          .targetEvent(ev)
									          .ok('Yes')
									          .cancel('No');*/
									    var isConfirmed = confirm("Are you sure you want to delete this record ?");
									  //  $mdDialog.show(confirm).then(function() {
									    if (isConfirmed) {
									    	$scope.deleteDocument.udoc_id			  = udoc_id;
									      ApiCallFactory.deleteTaskDocument($scope.deleteDocument).success(function(res,status){
									  			if(status === 200 && res.responseMessage == "Success"){
									  				toaster.success("Success", "Document deleted successfully.");
									  				$scope.getTaskHistoryList(client_task_id);
									  				$scope.getTaskDetails(client_task_id);
									  			}else{
									  				toaster.error("Failed", "Please try again");
									  		}	
									  		}).error(function(error){
												console.log("Error while deleting the task====="+error);
											});
									    }
									    //});
									 }
								  
								//Multiple task completion
									
									$scope.gettaskformultiplecompletion = function (task_id,ttrn_id) {
										$scope.multipleCompletion.tmap_client_task_id = task_id;
										$scope.multipleCompletion.ttrn_id = ttrn_id;
										spinnerService.show('html5spinner');

										ApiCallFactory.gettaskformultiplecompletion($scope.multipleCompletion).success(function (res, status) {
											spinnerService.hide('html5spinner');
											if (status === 200 && res.response == "success") {
												$scope.multipleTasks = res.task_list;
												$scope.multipleLength = $scope.multipleTasks.length;
												//alert("length : "+$scope.multipleLength);
											} else {
												toaster.error("Failed", "Error.");
											}
										}).error(function (error) {
											spinnerService.hide('html5spinner');
											console.log("update Configuration====" + error);
										});
									}

									if (!angular.isUndefined($stateParams.ttrn_id) && $stateParams.ttrn_id != 0 && !angular.isUndefined($stateParams.task_id) && $stateParams.task_id != 0) {
										$scope.gettaskformultiplecompletion();
									}


									//get check box ID
									$scope.getCheckTaskIdMultipleCompletion = function (selectId, task_id) {
										if (selectId == true) {
											var obj = {
												ttrn_id: task_id,
											}
											$scope.task_ttrn_ids.push(obj);
										} else {
											var index = 0;
											angular.forEach($scope.task_ttrn_ids, function (item) {
												if (item.ttrn_id == task_id) {
													$scope.task_ttrn_ids.splice(index, 1);
												}
												index++;
											});
										}
										if ($scope.task_ttrn_ids.length == $scope.multipleTasks.length) {
											$scope.selectedAll = true;
										} else {
											$scope.selectedAll = false;
										}

									}


									//select all check box
									$scope.checkAllMultipleCompletion = function () {
										$scope.task_ttrn_ids = [];
										if ($scope.selectedAll) {
											$scope.selectedAll = true;
										} else {
											$scope.selectedAll = false;
										}

										angular.forEach($scope.multipleTasks, function (item) {
											item.SelectedTaskId = $scope.selectedAll;
											if ($scope.selectedAll) {
												var obj = {
													ttrn_id: item.ttrn_id
													//tmap_lexcare_task_id:item.lexcare_task_id
												}
												$scope.task_ttrn_ids.push(obj);
											} else {
												$scope.task_ttrn_ids = [];
											}
										});
									};
								// End Multiple task completion code
									
									 $scope.approveSubTask = function(history){
											// console.log(task_id);
											 $scope.subTaskCompletion.ttrn_id=history.ttrn_id;	 
										  		ApiCallFactory.approveSubTask($scope.subTaskCompletion).success(function(res,status){
										  			if(status === 200 && res.responseMessage == "Success"){
										  				toaster.success("Success", "Task approved successfully.");
										  				//$window.location.reload();
										  				$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
										  				$scope.getTaskDetails(history.client_task_id);
										  			}else{
														alert(res.responseMessage);
													}
										  		}).error(function(error){
													console.log("Error while approving the task====="+error);
												});
										 }
									 
									 $scope.reOpenForSubtask={
											 ttrn_id : 0,
											 reopen_comment : "",
									 }
									 $scope.reopenSubTask=function(data){
										  //$scope.taskCompletion.ttrn_id=ttrn_id;
										  $scope.reOpenForSubtask.ttrn_id = data.ttrn_sub_task_id;
										  $scope.reOpenForSubtask.client_task_id = data.client_task_id;				
											  	
									  }
										 
										 $scope.sendReOpenSubTask=function(formValid){
											 //console.log("ID for reopen: "+$scope.reOpen.ttrn_id);
											 //console.log("Comment for reopen: "+$scope.reOpen.reopen_comment);
											 if(formValid){
													ApiCallFactory.reopenSubTask($scope.reOpenForSubtask).success(function(res,status){
														if(status === 200 && res.responseMessage == "Success"){
															//$scope.reOpen.reopen_comment="";	
															$('#reOpenSubTask').modal("hide");
															$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
															$scope.getTaskDetails($scope.client_task_id);
														//	$window.location.reload();
														}
														
													}).error(function(error){
														console.log("Error while approving the task====="+error);
													});
											 }
										 }
										 
										 
										 $scope.editSubTaskDates = function(data){
												//console.log("DATA "+JSON.stringify(data));
											//	alert(JSON.stringify(data));
											  $scope.NtmapObj.ttrn_client_task_id = data.client_task_id;
											  $scope.NtmapObj.ttrn_id = data.ttrn_id;
											  $scope.NtmapObj.ttrn_sub_id = data.ttrn_sub_task_id;			  
											  $scope.NtmapObj.ttrn_legal_date = data.ttrn_legal_due_date;
											  $scope.NtmapObj.ttrn_unit_date = data.ttrn_uh_due_date;
											  $scope.NtmapObj.ttrn_function_date = data.ttrn_fh_due_date;
											  $scope.NtmapObj.ttrn_evaluator_date = data.ttrn_rw_due_date;
											  $scope.NtmapObj.ttrn_executor_date = data.ttrn_pr_due_date;
											  $scope.NtmapObj.ttrn_performer_name = data.ttrn_performer_name;
											  $scope.NtmapObj.user_email = data.user_email;
												
											  
											  $('#editSubTaskConfiguration').modal();
											  
										  }
										
										 
										  $scope.updateSubTaskDates = function(formValid){
											  //console.log("FORM "+formValid);
											  //console.log("Dates "+$scope.validateDates());
											  if($scope.validateDates() && formValid){
												  $scope.tmapObj.ttrn_id                = $scope.NtmapObj.ttrn_id;
												  $scope.tmapObj.ttrn_sub_id            = $scope.NtmapObj.ttrn_sub_id;
												  $scope.tmapObj.ttrn_legal_due_date 	= moment($scope.lega_date).format('DD-MM-YYYY');
												  $scope.tmapObj.ttrn_uh_due_date 		= moment($scope.unit_date).format('DD-MM-YYYY');
												  $scope.tmapObj.ttrn_fh_due_date 		= moment($scope.func_date).format('DD-MM-YYYY');
												  $scope.tmapObj.ttrn_rw_due_date 		= moment($scope.eval_date).format('DD-MM-YYYY');
													$scope.tmapObj.ttrn_pr_due_date 	= moment($scope.exec_date).format('DD-MM-YYYY');
													$scope.tmapObj.ttrn_performer_name  = $scope.NtmapObj.ttrn_performer_name;
													$scope.tmapObj.user_email =  $scope.NtmapObj.user_email
												  
												 // console.log("JSON "+JSON.stringify($scope.tmapObj));
												  
												//Update Task Configuration dates
												  ApiCallFactory.updateSubTasksConfiguration($scope.tmapObj).success(function(res,status){
													  //alert(  $scope.NtmapObj.ttrn_client_task_id);
													  spinnerService.hide('html5spinner');
														if(status === 200){
															$('#editSubTaskConfiguration').modal("hide");
															toaster.success("Success", "Task dates updated successfully.");
															$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
															$scope.getTaskDetails($scope.NtmapObj.ttrn_client_task_id);
															$("#changeTask").modal();
														//	$window.location.reload();
														}else{
															toaster.error("Failed", "Error in dates.");
														}
													}).error(function(error){
														spinnerService.hide('html5spinner');
														console.log("update Configuration===="+error);
													});
												  
											  }
											  
											  
										  }
										  
											$scope.downloadSubtaskDocument = function(udoc_sub_id){
										        $window.location = "./downloadSubtaskDocument?udoc_sub_id="+udoc_sub_id;
										        //$state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});
											}
											
											 $scope.deleteSubTaskDocument=function(udoc_sub_task_id,client_task_id,ev,index){
												   /* var confirm = $mdDialog.confirm()
												          .title('Are you sure you want to delete the document?')
												          .targetEvent(ev)
												          .ok('Yes')
												          .cancel('No');*/
												    var isConfirmed = confirm("Are you sure you want to delete this document ?");
												  //  $mdDialog.show(confirm).then(function() {
												    if (isConfirmed) {
												    	$scope.deleteDocument.udoc_sub_task_id = udoc_sub_task_id;
												      ApiCallFactory.deleteSubTaskDocument($scope.deleteDocument).success(function(res,status){
												  			if(status === 200 && res.responseMessage == "Success"){
												  				toaster.success("Success", "Document deleted successfully.");
												  				$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
												  				$scope.getTaskDetails(client_task_id);
												  			}else{
												  				toaster.error("Failed", "Please try again");
												  		}	
												  		}).error(function(error){
															console.log("Error while deleting the task====="+error);
														});
												    }
												    //});
												 }
											 
											 $scope.deleteSubTask=function(ttrn_sub_task_id,client_task_id,ev,index){
												  /*  var confirm = $mdDialog.confirm()
												          .title('Are you sure you want to delete the task?')
												          .targetEvent(ev)
												          .ok('Yes')
												          .cancel('NO');*/
													var isConfirmed = confirm("Are you sure to delete this record ?");
												    
													if (isConfirmed) {
												    	$scope.taskCompletion.ttrn_sub_task_id	= ttrn_sub_task_id;
												    	$scope.taskCompletion.ttrn_client_task_id = client_task_id;
												      ApiCallFactory.deleteSubTaskHistory($scope.taskCompletion).success(function(res,status){
												  			if(status === 200 && res.responseMessage == "Success"){
												  				toaster.success("Success", "Task deleted successfully.");
												  				$scope.task_history.splice(index,1);
												  				$scope.getTaskHistoryListForCalender($scope.client_task_id,$scope.sub_task_id);
												  				$scope.getTaskDetails(client_task_id);
												  			}else{
												  				toaster.error("Failed", "Please try again");
												  		}	
												  		}).error(function(error){
															console.log("Error while deleting the task====="+error);
														});
												    } else {
												    	return false;
												    }
												}	 
											 
												$scope.getTaskHistoryListForCalender=function(task_id,sub_task_id){																
													var obj={tmap_client_task_id:task_id,
															 ttrn_sub_task_id: sub_task_id};
													//alert(JSON.stringify(obj));
													if(!angular.isUndefined(task_id) && task_id!=0){
														ApiCallFactory.getTaskHistoryListForCalender(obj).success(function(res,status){
															$scope.task_history = res.task_history;
															$scope.subTaskHistory = res.subTaskHistory;
															
															$scope.editDate={
																	role:Storage.get('userDetais.sess_role_id'),
															}
															//console.log("role "+$scope.editDates.role);
															$scope.client_task_id = task_id;
															$scope.sub_task_id = sub_task_id;
															//$scope.task_history.documents = 
														}).error(function(error){
															console.log("task History====="+error);
														});
												      }
												}
											
						} ]);