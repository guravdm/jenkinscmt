var CMTApp = angular.module('CMTApp', [ 'ui.router', 'toaster', 'ngMessages',
	'ngSanitize', 'ui.select', 'ui.calendar', 'ui.bootstrap', 'ngMaterial',
	'ngCookies', 'angularSpinners', 'infinite-scroll',
	'dataGridUtils.fixedHeader', 'long2know', 'ngIdle' ]);

CMTApp.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider

	.state('login', {
		url : "/login",
		templateUrl : "views/userLogin.html",
		controller : 'loginController'

	})
	
	.state('wipdashboard', {
		url : "/wipdashboard",
		templateUrl : "views/wipdashboard.html",
		controller : 'AnalyticalDashboardController'
	})
	
	.state('finalAuditReport', {
		url : "/finalAuditReport",
		templateUrl : "views/finalAuditReport.html",
		controller : 'FinalAuditReportController'
	})

	.state('executorLists', {
		url : "/executorLists",
		templateUrl : "views/executorLists.html",
		controller : 'executorListsController'
	})
	
	.state('progressReport', {
		url : "/progressReport",
		templateUrl : "views/progressReport.html",
		controller : 'progressReportController'
	})

	.state('addDocuments', {
		url : "/addDocuments",
		templateUrl : "views/addDocuments.html",
		controller : 'addDocumentsController'
	})

	.state('DepartmentList', {
		url : "/DepartmentList",
		templateUrl : "views/listDepartments.html",
		controller : 'departmentListController'

	}).state('Department', {
		url : "/Department",
		params : {
			'dept_name' : null,
			'dept_id' : 0
		},
		templateUrl : "views/addDepartment.html",
		controller : 'departmentController'
	})

	.state('copyTasks', {
		url : "/copyTasks",
		templateUrl : "views/copyTasks.html",
		controller : 'copyTasksController'
	})

	.state('EntityList', {
		url : "/EntityList",
		templateUrl : "views/listOrganizations.html",
		controller : 'entityListController'

	}).state('Entity', {
		url : "/Entity",
		params : {
			'orga_name' : null,
			'orga_id' : 0,
			'orga_parent_id' : 0
		},
		templateUrl : "views/addOrganization.html",
		controller : 'entityController'
	})

	.state('EntityMappingList', {
		url : "/EntityMappingList",
		templateUrl : "views/listEntitiesMapping.html",
		controller : 'entityMappingListController'
	})

	.state('EntityMapping', {
		url : "/EntityMapping",
		params : {
			'enti_id' : 0
		},
		templateUrl : "views/editEntitiesMapping.html",
		controller : 'entityMappingController'

	}).state('UsersList', {
		url : "/UsersList",
		templateUrl : "views/listUsers.html",
		controller : 'usersListController'

	}).state('User', {
		url : "/User",
		params : {
			'user_id' : 0
		},
		templateUrl : "views/addUser.html",
		controller : 'userController'
	})

	.state('UserAccess', {
		url : "/UserAccess",
		params : {
			'user_id' : 0,
			'user_first_name' : null,
			'user_last_name' : null
		},
		templateUrl : "views/setAccessLevel.html",
		controller : 'userAccessController'
	})

	.state('LegalUpdates', {
		url : "/LegalUpdates",
		templateUrl : "views/updateLegal.html",
		controller : 'updateLegalController'
	})

	.state('ComplianceManager', {
		url : "/ComplianceManager",
		templateUrl : "views/complianceMappingPanel.html",
		// / controller: 'updateLegalController'
	})

	.state('ImportedTask', {
		url : "/ImportedTask",
		templateUrl : "views/listImportedTasks.html",
		controller : 'importedTasksListController'
	})

	.state('LocationList', {
		url : "/LocationList",
		templateUrl : "views/listLocations.html",
		controller : 'locationListController'
	})

	.state('Location', {
		url : "/Location",
		params : {
			'loca_name' : null,
			'loca_id' : 0
		},
		templateUrl : "views/addLocation.html",
		controller : 'locationController'

	}).state('DesignationList', {
		url : "/DesignationList",
		templateUrl : "views/listDesignations.html",
		controller : 'designationListController'

	}).state('Designation', {
		url : "/Designation",
		params : {
			'desi_name' : null,
			'desi_id' : 0
		},
		templateUrl : "views/addDesignation.html",
		controller : 'designationController'

	}).state('showComplianceRepository', {
		url : "/showComplianceRepository",
		templateUrl : "views/listComplianceRepository.html",
		controller : 'cmpRepositoryController'
	})

	.state('AssignTasks', {
		url : "/AssignTasks",
		templateUrl : "views/addAssignTasks.html",
		controller : 'assignTasksController'
	})

	.state('listAssignedTasksForConfiguration', {
		url : "/listAssignedTasksForConfiguration",
		templateUrl : "views/taskConfiguration.html",
		controller : 'taskConfigurationController'
	})

	.state('ComplianceCalendar', {
		url : "/ComplianceCalendar",
		templateUrl : "views/complianceCalendar.html",
		controller : 'complianceCalendarController'
	})

	.state('listAssignedTasksForActivation', {
		url : "/listAssignedTasksForActivation",
		templateUrl : "views/activateTasks.html",
		controller : 'activateTasksListController'
	})

	.state('EnableDisable', {
		url : "/EnableDisable",
		templateUrl : "views/enableDisable.html",
		controller : 'enableDisableController'
	})

	.state('dashboard', {
		url : "/dashboard",
		templateUrl : "views/dashboard.html",
		controller : 'dashboardController'
	})

	.state('changeComplianceOwner', {
		url : "/changeComplianceOwner",
		templateUrl : "views/changeComplianceOwner.html",
		controller : 'changeComplianceOwnerController'
	})

	.state('generateReports', {
		url : "/generateReports",
		templateUrl : "views/generateReports.html",
		controller : 'generateReportsController'
	})


	.state('taskDetails', { 
		url: "/taskDetails", 
		params:{'task_id':0},
		templateUrl: "views/complianceTaskDetails.html", 
		controller: 'taskController' 
	})

	.state('completeTask', {
		url : "/completeTask",
		params : {
			'ttrn_id' : 0,
			'task_id' : 0,
			'document' : 0,
			'comments' : '',
			'completedDate' : 0,
			'status' : '',
			'reason' : '',
			'frequency' : '',
			'legalDueDate' : 0
		},
		templateUrl : "views/taskCompletion.html",
		controller : 'taskController'
	})

	.state('myAccount', {
		url : "/myAccount",
		templateUrl : "views/myAccount.html",
		controller : 'myAccountController'
	})

	.state('ChangePassword', {
		url : "/ChangePassword",
		templateUrl : "views/changePassword.html",
		controller : 'changePasswordController'
	})

	.state('DrilledReport', {
		url : "/DrilledReport",
		// params:{'chart_name':null,'status':null,'entity':null,'unit':null,'department':null,'taskList':null},
		params : {
			'myobj' : null
		},
		templateUrl : "views/drilledReport.html",
		controller : 'drilledReportController'
	})

	.state('TechSupport', {
		url : "/TechSupport",
		templateUrl : "views/techSupport.html",
		controller : 'techSupportController'
	})

	.state('listAssignedTasksForDefaultConfiguration', {
		url : "/listAssignedTasksForDefaultConfiguration",
		templateUrl : "views/defaultTaskConfiguration.html",
		controller : 'defaultTaskConfigurationController'
	})

	.state('defaultTaskConfigurationRepository', {
		url : "/defaultTaskConfigurationRepository",
		templateUrl : "views/defaultTaskConfigurationRepository.html",
		controller : 'defaultTaskConfigurationRepositoryController'
	})

	.state('mappingList', {
		url : "/mappingList",
		templateUrl : "views/mappingList.html",
		controller : 'mappingListController'
	})

	.state('listShowCauseNotice', {
		url : "/listShowCauseNotice",
		templateUrl : "views/listShowCauseNotice.html",
		controller : 'listShowCauseNoticeController'
	})

	.state('createShowCauseNotice', {
		url : "/createShowCauseNotice",
		params : {
			'scau_id' : 0
		},
		templateUrl : "views/createShowCauseNotice.html",
		controller : 'createShowCauseNoticeController'
	})

	.state('showCauseNoticeDetails', {
		url : "/showCauseNoticeDetails",
		params : {
			'scau_id' : 0
		},
		templateUrl : "views/showCauseNoticeDetails.html",
		controller : 'showCauseNoticeDetailsController'
	})

	.state('showCauseNoticeAction', {
		url : "/showCauseNoticeAction",
		templateUrl : "views/showCauseNoticeAction.html",
		controller : 'showCauseNoticeDetailsController'
	})

	.state('exportData', {
		url : "/exportData",
		templateUrl : "views/exportData.html",
		controller : 'exportDataController'
	})

	/*
	 * .state('generateCertificate', { url : "/generateCertificate", templateUrl :
	 * "views/generateCertificate.html", controller :
	 * 'generateCertificateController' })
	 * 
	 * .state('certificate', { url : "/certificate", //
	 * params:{'from':0,'to':0,'year':0}, templateUrl :
	 * "views/certificate.html", controller : 'generateCertificateController' })
	 */

	.state('generateCertificate', {
		url : "/generateCertificate",
		templateUrl : "views/generateCertificate.html",
		controller : 'generateCertificateController'
	})

	.state('certificate', {
		url : "/certificate",
		// params:{'from':0,'to':0,'year':0},
		templateUrl : "views/certificate.html",
		controller : 'generateCertificateController'
	})

	.state('subTaskComplianceManager', {
		url : "/subTaskComplianceManager",
		templateUrl : "views/subTaskComplianceMappingPanel.html",
		// / controller: 'updateLegalController'
	})

	.state('importSubTasks', {
		url : "/importSubTasks",
		templateUrl : "views/importSubTasks.html",
		controller : 'importSubTasksController'
	})

	.state('importedSubTasks', {
		url : "/importedSubTasks",
		templateUrl : "views/importedSubTasks.html",
		controller : 'importedSubTasksController'
	})

	.state('importSubTasksForID', {
		url : "/importSubTasksForID",
		params : {
			'clientTaskId' : 0
		},
		templateUrl : "views/importSubTasksForID.html",
		controller : 'importSubTasksForIDController'
	})

	.state('subTaskConfiguration', {
		url : "/subTaskConfiguration",
		templateUrl : "views/subTaskConfiguration.html",
		controller : 'subTaskConfigurationController'
	})

	.state('subTaskActivation', {
		url : "/subTaskActivation",
		templateUrl : "views/subTaskActivation.html",
		controller : 'subTaskActivationController'
	})

	.state('documents', {
		url : "/documents",
		templateUrl : "views/documents.html",
		controller : 'documentsController'
	})

	.state('commonEmail', {
		url : "/commonEmail",
		templateUrl : "views/commonEmail.html",
		controller : 'commonEmailController'
	})


	.state('userCredentialsemailLogs', {
		url : "/userCredentialsemailLogs",
		templateUrl : "views/userCredentialsemailLogs.html",
		controller : 'userCredentialsemailLog'
	})

	.state('importUserList', {
		url : "/importUserList",
		templateUrl : "views/importUserList.html",
		controller : 'importUserController'

	})

	.state('importEntityMappingList', {
		url : "/importEntityMappingList",
		templateUrl : "views/importEntityMappingList.html",
		controller : 'importEntityMappingController'

	})

	.state('importEntity', {
		url : "/importEntity",
		params : {
			'orga_name' : null,
			'orga_id' : 0,
			'orga_parent_id' : 0
		},
		templateUrl : "views/importOrganization.html",
		controller : 'importEntityController'
	})

	.state('importLocation', {
		url : "/importLocation",
		params : {
			'loca_name' : null,
			'loca_id' : 0
		},
		templateUrl : "views/importLocation.html",
		controller : 'importLocationController'

	}).state('importDepartment', {
		url : "/importDepartment",
		params : {
			'dept_name' : null,
			'dept_id' : 0
		},
		templateUrl : "views/importAddDepartment.html",
		controller : 'importDepartmentController'

	}).state('importAssignTask', {
		url : "/importAssignTask",
		params : {
			'entity_name' : null,
			'loca_name' : null,
			'dept_name' : null,
			'task_id' : null,
			'exe_name' : null,
			'eval_name' : null,
			'funHead_name' : null
		},
		templateUrl : "views/importAssignTask.html",
		controller : 'importAssignTaskController'
	}).state('generateMonthlyCertificate', {
		url : "/generateMonthlyCertificate",
		templateUrl : "views/generateMonthlyCertificate.html",
		controller : 'generateMonthlyCertificateController'
	})

	.state('monthlyCertificate', {
		url : "/monthlyCertificate",
		params : {
			'from' : 0,
			/* 'to' : 0, */
			'year' : 0,
			'certfor' : '',
			'generateFor' : ''
		},
		templateUrl : "views/monthlyCertificate.html",
		controller : 'generateMonthlyCertificateController'
	})

	.state('generateQuarterlyCertificate', {
		url : "/generateQuarterlyCertificate",
		templateUrl : "views/generateQuarterlyCertificate.html",
		controller : 'generateQuarterlyCertificateController'
	})

	.state('quarterlyCertificate', {
		url : "/quarterlyCertificate",
		params : {
			'from' : 0,
			'to' : 0,
			'year' : 0,
			'certfor' : '',
			'generateFor' : ''
		},
		templateUrl : "views/quarterlyCertificate.html",
		controller : 'generateQuarterlyCertificateController'
	})

	.state('analytics', {
		url : "/analytics",
		templateUrl : "views/new-dashboard.html",
		controller : 'newdashboardController'
	}).state('dashboardmenu', {
		url : "/dashboardmenu",
		templateUrl : "views/dashboardmenu.html",
	})

	.state('commonlogs', {
		url : "/commonlogs",
		templateUrl : "views/commonlogs.html",
		/* controller : 'commonLogsController' */
	})

	.state('loginLogs', {
		url : "/loginLogs",
		templateUrl : "views/loginLogoutLog.html",
		controller : 'loginLogsLogsController'
	})

	.state('taskAssignLogs', {
		url : "/taskAssignLogs",
		templateUrl : "views/taskAssignLogs.html",
		controller : 'taskAssignLogsController'
	})


	.state('changeComplianceOwnerLogs', {
		url : "/changeComplianceOwnerLogs",
		templateUrl : "views/changeComplianceOwnerLogs.html",
		controller : 'changeComplianceOwnerLogsLogsController'
	})

	.state('emailLogs', {
		url : "/emailLogs",
		templateUrl : "views/emailLogs.html",
		controller : 'emailLogsController'
	})

	/*.state('reportsLogs', {
		url : "/reportsLogs",
		templateUrl : "views/reportsLogs.html",
		controller : 'reportsLogsController'
	})*/

	.state('taskConfigLogs', {
		url : "/taskConfigLogs",
		templateUrl : "views/taskConfigLogs.html",
		controller : 'tasktaskConfigLogsLogsController'
	})

	.state('complianceReportLogs', {
		url : "/complianceReportLogs",
		templateUrl : "views/reportsLogs.html",
		controller : 'complianceReportLogsController'
	})


	.state('activateDeactivateLogs', {
		url : "/activateDeactivateLogs",
		templateUrl : "views/activateDeactivateLogs.html",
		controller : 'activateDeactivateLogsController'
	})

	.state('reactivationLogs', {
		url : "/reactivationLogs",
		templateUrl : "views/reactivationLogs.html",
		controller : 'reactivationLogsController'
	})

	.state('queryBuilder', {
		url : "/queryBuilder",
		templateUrl : "views/queryBuilder.html",
		controller : 'queryBuilderController'
	})

	.state('auditTasks', {
		url : "/auditTasks",
		templateUrl : "views/auditTasks.html",
		controller : 'auditTasksController'
	})

	.state('postAuditReport', {
		url : "/postAuditReport",
		templateUrl : "views/postAuditReport.html",
		controller : 'postAuditReportController'
	})

	.state('auditorDashboard', {
		url : "/auditorDashboard",
		templateUrl : "views/auditorDashboard.html",
		controller : 'auditDashboardController'
	})

	.state('headCounts', {
		url : "/headCounts",
		templateUrl : "views/headCounts.html",
		controller : 'headCountsController'
	})

	;
	$urlRouterProvider.otherwise("/login");

});

//Added for date picker
CMTApp.config(function($mdDateLocaleProvider) {
	$mdDateLocaleProvider.formatDate = function(date) {
		return date ? moment(date).format('DD-MM-YYYY') : '';
	};

	$mdDateLocaleProvider.parseDate = function(dateString) {
		var m = moment(dateString, 'DD-MM-YYYY', true);
		return m.isValid() ? m.toDate() : new Date(NaN);
	};
});

CMTApp.config([ '$compileProvider', function($compileProvider) {
	$compileProvider.aHrefSanitizationWhitelist(/^\s*(|blob|):/);
} ]);

///Interceptor for token
CMTApp.factory('AuthInterceptor', function($window, $q, $location) {
	return {
		request : function(config) {
			config.headers = config.headers || {};
			/*
			 * if ($window.sessionStorage.getItem('token')!=null &&
			 * $window.sessionStorage.getItem('token')!=undefined) {
			 * config.headers.Authorization =
			 * $window.sessionStorage.getItem('token'); }else
			 */if ($window.localStorage.getItem('token') != null
					 && $window.localStorage.getItem('token') != undefined) {
				 config.headers.Authorization = $window.localStorage
				 .getItem('token');
			 }

			 return config || $q.when(config);
		},
		responseError : function(response) {
			if (response.status === 401) {
				// here I preserve login page
				$location.path('/login');
				/*
				 * $window.sessionStorage.removeItem('token');
				 * $window.sessionStorage.removeItem('userDetais');
				 * $window.sessionStorage.removeItem('task_id');
				 */
				window.localStorage['storageName'] = '';
				$window.localStorage.removeItem('token');
				$window.localStorage.removeItem('userDetais');
				$window.localStorage.removeItem('task_id');
			}
			return $q.reject(response);
		}
	};
});

//Register the previously created AuthInterceptor.
CMTApp.config(function($httpProvider) {
	$httpProvider.interceptors.push('AuthInterceptor');
});
CMTApp.run(function($state, $rootScope) {
	$rootScope.$state = $state;
	$('#addBackground').addClass('login_body');
});

CMTApp
.controller(
		'routeCtrl',
		[
			'$scope',
			'$location',
			'Storage',
			'$state',
			'$stateParams',
			'$rootScope',
			'ApiCallFactory',
			'$window',
			'Idle',
			'$uibModal',
			'toaster',
			function($scope, $location, Storage, $state,
					$stateParams, $rootScope, ApiCallFactory,
					$window, Idle, $uibModal, toaster) {
				$scope.state = $state;
				$scope.stateArray = [];
				$scope.latestState = [];
				$scope.backButton = false;
				$rootScope.currentuser = Storage
				.get('userDetais.user_full_name');
				$rootScope.getDefaultUserPassword = Storage
				.get('userDetais.sess_user_default_password');
				$rootScope.getUserRoleId = Storage
				.get('userDetais.sess_role_id');
				$rootScope.getUserID = Storage
				.get('userDetais.sess_user_id');
				$rootScope.prvPath = '';

				$rootScope.currentuser = $window.localStorage
				.getItem('userDetais.user_full_name'); // Storage.get('userDetais.user_full_name');
				$rootScope.getDefaultUserPassword = $window.localStorage
				.getItem('userDetais.sess_user_default_password');
				$rootScope.getUserRoleId = $window.localStorage
				.getItem('userDetais.sess_role_id');
				$rootScope.getUserID = $window.localStorage
				.getItem('userDetais.sess_user_id');
				// console.log("role is "+$scope.getUserRoleId);
				// /logout function
				$scope.logout = function() {
					$("#profile").hide();
					$("#slide").hide();
					var obj = {};
					ApiCallFactory
					.logout(obj)
					.success(
							function(res, status) {
								if (res.responseMessage == 'Success') {
									/*
									 * $window.sessionStorage.removeItem('token');
									 * $window.sessionStorage.removeItem('userDetais');
									 * $window.sessionStorage.removeItem('userDetais.user_full_name');
									 * $window.sessionStorage.removeItem('userDetais.sess_user_id');
									 * $window.sessionStorage.removeItem('userDetais.sess_user_default_password');
									 * $window.sessionStorage.removeItem('userDetais.sess_role_id');
									 * $window.sessionStorage.removeItem('userDetais.sess_user_email');
									 * $window.sessionStorage.removeItem('userDetais.sess_user_mobile');
									 */

									window.localStorage['storageName'] = '';
									$window.localStorage
									.removeItem('token');
									$window.localStorage
									.removeItem('userDetais');
									$window.localStorage
									.removeItem('userDetais.user_full_name');
									$window.localStorage
									.removeItem('userDetais.sess_user_id');
									$window.localStorage
									.removeItem('userDetais.sess_user_default_password');
									$window.localStorage
									.removeItem('userDetais.sess_role_id');
									$window.localStorage
									.removeItem('userDetais.sess_user_email');
									$window.localStorage
									.removeItem('userDetais.sess_user_mobile');
									$window.localStorage
									.removeItem('tscn_scau_id');
									$window.sessionStorage
									.removeItem('userDetais.sess_role_id');
									$window.sessionStorage
									.removeItem('userDetais.user_full_name');

									$state.go('login');

								}
							});
				}

				// Ideal Timeout Start
				$scope.events = [];

				$scope.started = false;

				function closeModals() {
					if ($scope.warning) {
						$scope.warning.close();
						$scope.warning = null;
					}

					if ($scope.timedout) {
						$scope.timedout.close();
						$scope.timedout = null;
					}
				}

				$scope.minutes = 0;
				$scope.seconds = 0;
				$scope.$on('IdleStart', function() {
					// the user appears to have gone idle

					closeModals();
					// alert("Warning dialog logout in 5 sec");

					$scope.warning = $uibModal.open({
						templateUrl : 'warning-dialog.html',
						windowClass : 'modal-danger',
						scope : $scope
					});

				});

				$scope
				.$on(
						'IdleWarn',
						function(e, countdown) {
							// follows after the IdleStart
							// event, but includes a
							// countdown until the user is
							// considered timed out
							// the countdown arg is the
							// number of seconds remaining
							// until then.
							// you can change the title or
							// display a warning dialog from
							// here.
							// you can let them resume their
							// session by calling
							// Idle.watch()
							$scope.minutes = Math
							.floor(countdown / 60);
							$scope.seconds = (countdown - $scope.minutes * 60);
							// console.log($sccope.seconds);
							// /toaster.warning("You are
							// idle. You'll be logged out in
							// "+$scope.minutes);

						});

				$scope.$on('IdleTimeout', function() {
					// the user has timed out (meaning idleDuration
					// + timeout has passed without any activity)
					// this is where you'd log them
					closeModals();
					// alert("Your session has been expired.");

					$scope.timedout = $uibModal.open({
						templateUrl : 'timedout-dialog.html',
						windowClass : 'modal-danger'
					});
					$scope.logout();
				});

				$scope.$on('IdleEnd', function() {
					// the user has come back from AFK and is doing
					// stuff. if you are warning them, you can use
					// this to hide the dialog
					closeModals();

				});

				$scope.$on('Keepalive', function() {
					// do something to keep the user's session alive
				});
				// Idle timeout End

				$scope.start = function() {
					closeModals();
					Idle.watch();
					$scope.started = true;
				};

				$scope.stop = function() {
					closeModals();
					Idle.unwatch();
					$scope.started = false;

				};

				// On state change call function
				$rootScope
				.$on(
						'$stateChangeSuccess',
						function(ev, to, toParams, from,
								fromParams) {
							if ($scope.state.current.name == 'login') {
								if (fromParams.task_id != undefined)
									$rootScope.prvPath = from.name
									+ "/"
									+ fromParams.task_id;
								else
									$rootScope.prvPath = from.name;

								$scope.stateArray = [];
								$scope.latestState = [];
								$scope.backButton = false;
								$rootScope.currentuser = null;
								$scope.logout();
								$scope.stop();

							}

							if ($window.localStorage
									.getItem('token') == null
									|| $window.localStorage
									.getItem('token') == undefined) {
								if (fromParams.task_id != undefined)
									$rootScope.prvPath = from.name
									+ "/"
									+ fromParams.task_id;
								else
									$rootScope.prvPath = from.name;

								$scope.stateArray = [];
								$scope.latestState = [];
								$scope.backButton = false;
								$rootScope.currentuser = null;
								$scope.logout();
								$scope.stop();

							} else {
								$scope.start();
							}
							// Back button functionlity
							// logic
							if (to.name != 'login') {
								$('#addBackground')
								.removeClass(
								'login_body');
								if ($rootScope.getDefaultUserPassword != 0) {
									$scope.obj = {
											previousStateObj : from,
											previousStateParamObj : fromParams
									};
									if (from.name != "") {
										if ($scope.stateArray.length > 0) {
											if ($scope.backButton) {
												$scope.backButton = false;
											} else {
												$scope.stateArray
												.push($scope.obj);
											}
										} else {
											$scope.stateArray
											.push($scope.obj);
										}
									}
								} else {
									$state
									.go('ChangePassword');
								}
							} else {
								$('#addBackground')
								.addClass(
								'login_body');
								$scope.stateArray = [];
								$scope.latestState = [];
								$scope.backButton = false;
								$rootScope.currentuser = null;
								$scope.logout();
							}

						});

				// /Back to previous state functionality
				$scope.backToPreviousState = function() {
					$scope.backButton = true
					$scope.stateArraydummy = angular
					.copy($scope.stateArray);
					$scope.latestState = $scope.stateArraydummy
					.slice(-1);
					$scope.stateArray = $scope.stateArray.slice(0,
							-1);
					$state
					.transitionTo(
							$scope.latestState[0].previousStateObj.name,
							$scope.latestState[0].previousStateParamObj);
				}

			} ]);

CMTApp.config(function(IdleProvider, KeepaliveProvider) {
	// configure Idle settings
	IdleProvider.idle(60 * 10); // in seconds
	IdleProvider.timeout(60 * 5); // in seconds
	KeepaliveProvider.interval(2); // in seconds
}).run(function(Idle) {
	// start watching when the app runs. also starts the Keepalive service by
	// default.
	Idle.watch();
});

//Added for select search functionality
CMTApp.filter('propsFilter', function() {
	return function(items, props) {
		var out = [];

		if (angular.isArray(items)) {
			items
			.forEach(function(item) {
				var itemMatches = false;

				var keys = Object.keys(props);
				for (var i = 0; i < keys.length; i++) {
					var prop = keys[i];
					var text = props[prop].toLowerCase();
					if (item[prop].toString().toLowerCase().indexOf(
							text) !== -1) {
						itemMatches = true;
						break;
					}
				}

				if (itemMatches) {
					out.push(item);
				}
			});
		} else {
			// Let the output be the input untouched
			out = items;
		}

		return out;
	}
});

///Added for dropdown search functionality
CMTApp.filter('to_trusted', [ '$sce', function($sce) {
	return function(text) {
		return $sce.trustAsHtml(text);
	};
} ]);

//Added for file type ng-model
CMTApp.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files);
				});
			});
		}
	};
} ]);

//Added for file type ng-model
CMTApp.directive('validFile', [ '$parse', function($parse) {
	return {
		require : 'ngModel',
		link : function(scope, el, attrs, ngModel) {
			el.bind('change', function() {
				scope.$apply(function() {
					ngModel.$setViewValue(el.val());
					ngModel.$render();
					var model = $parse(attrs.ngModel);
					var modelSetter = model.assign;
					modelSetter(scope, el[0].files);
				});
			});
		}
	}
} ]);

//password mach & unmach
CMTApp.directive("unMatcher", function($timeout) {
	return {
		restrict : "A",

		require : "ngModel",

		link : function(scope, element, attributes, ngModel) {
			ngModel.$validators.unMatcher = function(modelValue) {
				return attributes.unMatcher !== modelValue;
			};
		}
	};
});

//Confirm password
CMTApp.directive('confirmPwd', function($interpolate, $parse) {
	return {
		require : 'ngModel',
		link : function(scope, elem, attr, ngModelCtrl) {

			var pwdToMatch = $parse(attr.confirmPwd);
			var pwdFn = $interpolate(attr.confirmPwd)(scope);

			scope.$watch(pwdFn, function(newVal) {
				ngModelCtrl.$setValidity('password',
						ngModelCtrl.$viewValue == newVal);
			})

			ngModelCtrl.$validators.password = function(modelValue, viewValue) {
				var value = modelValue || viewValue;
				return value == pwdToMatch(scope);
			};

		}
	}
});

CMTApp
.directive(
		'siteHeader',
		function() {
			return {
				restrict : 'E',
				template : '<button class="btn btn-primary" style="padding-left: 10px; margin-left: 15px; margin-top: -15px; box-shadow: none; background-color: transparent; height: 30px; width: 100px; background-repeat: no-repeat; background-size: contain;   background-image: url(images/DashboardIcons/backold.png); border-radius: 4px; font-size: 14px;"> {{back}}</button>',
				scope : {
					back : '@back',
					forward : '@forward',
					icons : '@icons'
				},
				link : function(scope, element, attrs) {

					element.bind('error', function() {
						angular.element(this).attr("src",
						"images/DashboardIcons/backold.png");
					});

					$(element[0]).on('click', function() {
						history.back();
						scope.$apply();
					});

					$(element[1]).on('click', function() {
						history.forward();
						scope.$apply();
					});
				}
			};
		});

CMTApp
.directive(
		'scroll',
		[ function() {
			return {
				link : function(scope, element, attrs) {
					// ng-repeat delays the actual width of the element.
					// this listens for the change and updates the
					// scroll bar
					function widthListener() {
						if (anchor.width() != lastWidth)
							updateScroll();
					}

					function updateScroll() {
						// for whatever reason this gradually takes away
						// 1 pixel when it sets the width.
						// $div2.width(anchor.width() + 1);

						// make the scroll bars the same width
						// $div1.width($div2.width());

						// sync the real scrollbar with the virtual one.
						$wrapper1
						.scroll(function() {
							$wrapper2.scrollLeft($wrapper1
									.scrollLeft());
						});

						// sync the virtual scrollbar with the real one.
						$wrapper2
						.scroll(function() {
							$wrapper1.scrollLeft($wrapper2
									.scrollLeft());
						});
					}

					var anchor = element.find('[data-anchor]'), lastWidth = anchor
					.width(), listener;

					// so that when you go to a new link it stops
					// listening
					element.on('remove', function() {
						clearInterval(listener);
					});

					// creates the top virtual scrollbar
					// element.wrapInner("<div class='div2' />");
					element.wrapInner("<div class='wrapper2' />");

					// contains the element with a real scrollbar
					element
					.prepend("<div class='wrapper1'><div class='div1'></div></div>");

					var $wrapper1 = element.find('.wrapper1'), $div1 = element
					.find('.div1'), $wrapper2 = element
					.find('.wrapper2')
					// $div2 = element.find('.div2')

					// force our virtual scrollbar to work the way we
					// want.
					$wrapper1.css({
						width : "100%",
						border : "none 0px rgba(0, 0, 0, 0)",
						overflowX : "scroll",
						overflowY : "hidden",
						height : "20px",
					});

					$div1.css({
						height : "20px",
					});

					$wrapper2.css({
						width : "100%",
						overflowX : "scroll",
					});

					listener = setInterval(function() {
						widthListener();
					}, 650);

					updateScroll();
				}
			}
		} ]);
