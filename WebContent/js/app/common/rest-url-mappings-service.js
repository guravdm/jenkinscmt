'use strict';

CMTApp.factory('RestURLMappingService', [ 'DataFactory', '$rootScope',
	function(DataFactory, $rootScope) {
	var RestUrl = {};

	RestUrl.getOverDashboardCountURL = function() {
		return './complianceDashboardCount';
	};
	
	RestUrl.getFunctionListByOrgaIdURL = function() {
		return './getFunctionListByOrgaId';
	};
	
	RestUrl.finalComplianceAuditReportURL = function() {
		return './finalComplianceAuditReport';
	};
	
	
	/** 
	 * @Logs URL's
	 */
	
	RestUrl.searchComplianceStatusAuditPieChartURL = function() {
		return './searchAuditDashboard';
	};
	
	RestUrl.searchComplianceAuditChartURL = function() {
		return './searchMonthlyComplianceAuditChart';
	};


	RestUrl.searchProgressReportURL = function() {
		return './statusCompletionReport';
	};

	RestUrl.listSimplyCompDocumentsURL = function() {
		return './listSimplyCompDocuments';
	};
	
	RestUrl.getTaskActivityListURL = function() {
		return './getTaskActivityListURL';
	};

	RestUrl.getQueryBuilderURL = function() {
		return './queryBuilder';
	};

	RestUrl.getTasksDeactivationQueryBuilderURL = function() {
		return './queryDeActivation';
	};

	RestUrl.getDisabletasksURL = function() {
		return './queryDisableTasks';
	};

	RestUrl.getLoginLogsURL = function() {
		return './loginLogs';
	};

	RestUrl.getAssignLogsURL = function() {
		return './assignLogs';
	};

	RestUrl.getChangeCompOwnerLogsURL = function() {
		return './changeComplianceOwnerLogs';
	};

	RestUrl.getConfigLogsURL = function() {
		return './tasksConfigLogs';
	};

	RestUrl.getComplianceReportLogsURL = function() {
		return './complianceReportLogs';
	};

	RestUrl.getmailLogsURL = function() {
		return './emailLogs';
	};

	RestUrl.getActivateDeActivateLogsURL = function() {
		return './activateDeActivateLogs';
	};

	RestUrl.getReactivateLogsURL = function() {
		return './reactivateTasks';
	};


	/**
	 *  End Log's code
	 */

	// RestUrl.authURL = '';
	RestUrl.getAuthURL = function() {
		return './authenticateUser';
	};

	RestUrl.getForgotPasswordURL = function() {
		return './forgotpassword';
	};

	// add deparment
	RestUrl.addDeparmentURL = function() {
		return './savefunction';
	};

	// get deparment
	RestUrl.getDeparmentListURL = function() {
		return './listfunctions';
	};

	// get deparment
	RestUrl.updateDeparmentURL = function() {
		return './updatefunction';
	};

	// get deparment
	RestUrl.duplicateDepartmentURL = function() {
		return './isDeptNameExist';
	};

	// add Entity
	RestUrl.addEntityURL = function() {
		return './saveentity';
	};

	// get Entity by user access
	RestUrl.getEntityListURL = function() {
		return './listentities';
	};

	// get All Entity

	RestUrl.getAllEntityListURL = function() {
		return './listallentities';
	};

	RestUrl.getMainEntityListURL = function() {
		return './listAllForAddingEntity';
	};

	RestUrl.getEntityMappingListURL = function() {
		return './listentitiesmapping';
	};

	RestUrl.updateEntityURL = function() {
		return './updateentity';
	};

	// get Entity
	RestUrl.duplicateEntityURL = function() {
		return './isOrgaNameExist';
	};

	/*
	 * RestUrl.getEntitySelectListURL = function(){ return
	 * './listentitiesmapping'; };
	 */

	// get Unit list as per user access level
	RestUrl.getUnitListURL = function() {
		return './listunits';
	};

	// get all unit list from the DB
	RestUrl.getAllUnitListURL = function() {
		return './listallunits';
	};

	// add unit
	RestUrl.addUnitURL = function() {
		return './saveunit';
	};

	RestUrl.updateUnitURL = function() {
		return './updateunit';
	};

	RestUrl.duplicateUnitURL = function() {
		return './isLocaNameExist';
	};

	RestUrl.getFunctionListURL = function() {
		return './listfunctions';
	};

	RestUrl.getFunctionListByUIdEIdURL = function() {
		return './getMappedDepartments';
	};

	RestUrl.addEntityMappingURL = function() {
		return './saveentitiesmapping';
	};
	/*
	 * RestUrl.updateEntityMappingURL = function(){ /// return
	 * './listentitiesmapping'; };
	 */
	RestUrl.uploadLegalFileURL = function() {
		return './importlegalupdatesfromfile';
	};

	// get user list
	RestUrl.getUserListURL = function() {
		return './listusers';
	};

	// enable disable user
	RestUrl.enableDisableUserURL = function() {
		return './enabledisableuser';
	};

	RestUrl.getUnitListByIdURL = function() {
		return './getMappedUnits';
	};

	RestUrl.getFunctionListByIdURL = function() {
		return '';
	};

	RestUrl.getUsersDetailsURL = function() {
		return './editUser';
	};

	RestUrl.addUserURL = function() {
		return './saveUser';
	};
	RestUrl.updateUserURL = function() {
		return './updateUser';
	};

	RestUrl.checkDuplicateUserNameURL = function() {
		return './isusernameexists';
	};

	RestUrl.addAccessLevelURL = function() {
		return '';
	};

	RestUrl.saveAccessURL = function() {
		return './saveuseraccess';
	};

	RestUrl.getUserAccessListURL = function() {
		return './getuseraccess';
	};

	RestUrl.getImportedTasksListURL = function() {
		return './getimportedtasks';
	};

	// get Designation list
	RestUrl.getDesignationListURL = function() {
		return './listdesignations';
	};

	// add designation
	RestUrl.addDesignationURL = function() {
		return './savedesignations';
	};

	// duplicate dsignation
	RestUrl.duplicateDesignationURL = function() {
		return './isDesiNameExist';
	};

	// get designation
	RestUrl.updateDesignationURL = function() {
		return './updatedesignation';
	};

	// get compliance repository
	RestUrl.getRepositoryListURL = function() {
		return './getalltasks';
	};

	// /Assign task
	RestUrl.getCountriesListURL = function() {
		return './getdistinctcountries';
	};
	RestUrl.getStateListURL = function() {
		return './getallstateforcountry';
	};

	RestUrl.getCategoryOfLawListURL = function() {
		return './getallcatlaw';
	};

	RestUrl.getalllegiListURL = function() {
		return './getalllegi';
	};
	RestUrl.getalllegiRuleListURL = function() {
		return './getalllegirule';
	};

	RestUrl.getTaskListURL = function() {
		return './searchtasksforusermapping';
	};

	RestUrl.assginTaskURL = function() {
		return './savetasksusermapping';
	};

	// getTaskList
	RestUrl.getActiveDeativeTaskListURL = function() {
		return './getallconfiguredtaskforactivationpage';
	};

	// chang task status
	RestUrl.changeTaskStatusURL = function() {
		return './activationoftasks';
	};

	// get tasks for enabling and disabling
	RestUrl.getTaskForEnableDisableURL = function() {
		return './getallmappedtasksforenablingpage';
	};

	RestUrl.getCalendarDateListURL = function() {
		return './showComplianceCalendar';
	};

	// change task status to enable or disable
	RestUrl.changeTaskStatusForEnableDisableURL = function() {
		return './enablingoftasks'
	}

	// get mapped tasks for configuration
	RestUrl.getTasksForConfigurationURL = function() {
		return './getallmappedtasksforconfigurationpage'
	}

	// get entity unit location list for add user page
	RestUrl.getEntityUnitFunctionDesignationListURL = function() {
		return './getAllMapping'
	}

	// /Dashboard compliance status pie chart
	RestUrl.complianceStatusPieChartURL = function() {
		return './getoverallcompliancestatus'
	}

	// Dashboard entity level
	RestUrl.entityLevelChartURL = function() {
		return './getentitywiseComplianceStatus'
	}

	// Dashboard unit Compliance level
	RestUrl.unitComplianceChartURL = function() {
		return './getunitwiseComplianceStatus'
	}

	// Dashboard function Compliance level
	RestUrl.functionComplianceChartURL = function() {
		return './getfunctionwiseComplianceStatus'
	}

	// save Task configuration
	RestUrl.saveTasksConfigurationURL = function() {
		return './savetasksconfiguration'
	}

	// change Compliance Owner
	RestUrl.changeComplianceOwnerURL = function() {
		return './changecomplianceowner'
	}

	//get entity unit location list for report page
	RestUrl.getEntityUnitFunctionDesignationListReportURL = function(){
		return './getAllMappingReport'
	}

	// Generate Reports
	RestUrl.generateReportsURL = function() {
		return './fetchreports'
	}

	// get Task History
	RestUrl.getTasksHistoryURL = function() {
		return './gethistoryfortask'
	}

	// get Task details
	RestUrl.getTasksDetailsURL = function() {
		return './getdetailsfortask'
	}

	// save complete Task details
	RestUrl.saveTasksCompletionURL = function() {
		return './savetaskcompletion'
	}

	// get userdetail
	RestUrl.getUserDetailsURL = function() {
		return './myaccount'
	}

	// upload pic
	RestUrl.updateUserPicURL = function() {
		return ''
	}
	RestUrl.changePasswordURL = function() {
		return './changepassword'
	}

	// logout
	RestUrl.logoutURL = function() {
		return './userlogout'
	}

	// drilledReport
	RestUrl.getDrilledReportListURL = function() {
		return './getgraphdrilldown'
	}

	/*
	 * RestUrl.downloadProofComplianceURL = function(){ return
	 * './downloadProofOfCompliance' }
	 */

	// send Mail to support
	RestUrl.sendMailToSupportURL = function() {
		return './getSupportQuery'
	}

	// update Task configuration
	RestUrl.updateTasksConfigurationURL = function() {
		return './updatetasksconfiguration'
	}
	// Download user list
	RestUrl.downloaduserlistURL = function() {
		return './downloaduserlist'
	}

	// send Credentials
	RestUrl.sendCredentialsURL = function() {
		return './sendcredentialsmail'
	}

	// Get tasks list for default task configuration
	RestUrl.getTasksForDefaultConfigurationURL = function() {
		return './searchTaskForDefaultConfiguration'
	}

	// Save default task configuration
	RestUrl.SaveDefaultTaskConfigurationURL = function() {
		return './saveDefaultTaskConfiguration'
	}
	// get Default Task
	RestUrl.getDefaultTaskURL = function() {
		return './getdefaulttaskforinitiation'
	}

	// Initiate Default Task
	RestUrl.initiateDefaultTaskURL = function() {
		return './initiateDefaultConfiguredTask'
	}

	// approve Task
	RestUrl.approveTaskURL = function() {
		return './approveTask'
	}

	// approve Task
	RestUrl.reopenTaskURL = function() {
		return './reopenTask'
	}

	// Delete task history
	RestUrl.deleteTaskHistoryURL = function() {
		return './deleteTaskHistory'
	}

	// Delete task mapping
	RestUrl.deleteTaskMappingURL = function() {
		return './deleteTaskMapping'
	}

	// Default task configuration repository
	RestUrl.getDefaultTaskListURL = function() {
		return './getDefaultTaskList'
	}

	// Update default task configuration
	RestUrl.updateDefaultTaskConfigurationURL = function() {
		return './updateDefaultTaskConfiguration'
	}

	// Get entity unit function drop down for show cause notice
	RestUrl.getAccessWiseOrgaLocaDeptURL = function() {
		return './getAccessWiseOrgaLocaDept'
	}

	// create show cause notice
	RestUrl.saveShowCauseNoticeURL = function() {
		return './saveShowCauseNotice'
	}

	// List show cause notice
	RestUrl.getAllShowCauseNoticeURL = function() {
		return './getAllShowCauseNotice'
	}

	// Remove user access
	RestUrl.removeUserAccessURL = function() {
		return './removeUserAccess'
	}

	// save action item
	RestUrl.saveActionItemURL = function() {
		return './saveActionItem'
	}

	// List all action items
	RestUrl.getAllActionItemURL = function() {
		return './getAllActionItem'
	}

	/*
	 * //download show cause notice document
	 * downloadShowCauseDocumentURL = function(){ return
	 * './downloadShowCauseDocument' }
	 */

	// Submit reason for non compliance
	RestUrl.updateTaskReasonForNonComplianceURL = function() {
		return './updateTaskReasonForNonCompliance'
	}

	// Get show cause notice details
	RestUrl.getShowCauseNoticeDetailsURL = function() {
		return './getShowCauseNoticeDetails'
	}

	// Get show cause notice details
	RestUrl.updateShowCauseNoticeURL = function() {
		return './updateShowCauseNotice'
	}

	// get user mapping list.
	RestUrl.getUsermappinglistURL = function() {
		return './usermappinglist'
	}

	// get user mapping list.
	RestUrl.getExportDataURL = function() {
		return './getalltasksforexport'
	}

	RestUrl.getCertificateDetailsURL = function(){
		return './generateCertificate'
	}

	RestUrl.downloadCertificateDetailsURL = function(){
		return './downloadCertificateDetails'
	}

	// Import sub tasks
	RestUrl.getUserDefinedTaskURL = function() {
		return './getUserDefinedTask'
	}

	// upload sub tasks
	RestUrl.importsubtaskURL = function() {
		return './importsubtask'
	}

	// get Imported sub tasks
	RestUrl.getimportedsubtaskURL = function() {
		return './getimportedsubtask'
	}

	// get sub tasks for configuration
	RestUrl.getsubtasksforconfigurationURL = function() {
		return './getsubtasksforconfiguration'
	}

	// save sub task configuration
	RestUrl.savesubtasksconfigurationURL = function() {
		return './savesubtasksconfiguration'
	}

	// Get configured tasks for activation
	RestUrl.getconfiguredsubtaskURL = function() {
		return './getconfiguredsubtask'
	}

	// activate deactivate sub tasks
	RestUrl.updateStatusURL = function() {
		return './updateStatus'
	}

	// save sub task completion
	RestUrl.savesubtaskcompletionURL = function() {
		return './savesubtaskcompletion'
	}

	// multiple task completion
	RestUrl.gettaskformultiplecompletionURL = function() {
		return './gettaskformultiplecompletion'
	}

	// get all documents
	RestUrl.getalldocumentsURL = function() {
		return './getalldocuments'
	}

	// delete task document
	RestUrl.deleteTaskDocumentURL = function() {
		return './deleteTaskDocument'
	}

	RestUrl.getuserwithaccessforcommonemailURL = function() {
		return './getuserwithaccessforcommonemail'
	}

	RestUrl.sendcommonemailURL = function() {
		return './sendcommonemail'
	}

	/**
	 * To-DO List URL's
	 */
	RestUrl.getPosingTasksURL = function() {
		return './getposingtasks';
	};

	RestUrl.getWaitingForApprovalComplianceRepositoryURL = function() {
		return './waitingforapprovaltasks';
	};

	RestUrl.getReopenedRepositoryDataURL = function() {
		return './reopenedtasks';
	};

	/**
	 * @Date 08-Aug-2019 For Repository Page calling new API's
	 */

	RestUrl.getEntityListByUserIdURL = function() {
		return './loadEntityById';
	};

	RestUrl.getUnitListByOrgaIdAndUserIdURL = function() {
		return './loadUnitById';
	};

	RestUrl.getFunctionListByUnitIdURL = function() {
		return './loadFunctions';
	};

	/**
	 * End
	 */
	RestUrl.getExeEvalFunHeadListURL = function() {
		return './getExeEvalFuncHeadList'
	}

	// Get task by FROM TO TO Date
	RestUrl.searchComplianceStatusPieChartURL = function() {
		return './searchgetoverallcompliancestatus'
	}

	RestUrl.getExecutorListURL = function() {
		return './getExecutorList'
	}
	RestUrl.getEvaluatorListURL = function() {
		return './getEvaluatorList'
	}
	RestUrl.getFunHeadListURL = function() {
		return './getFunHeadList'
	}
	RestUrl.getTaskListToAssignURL = function() {
		return './getTaskListToAssign'
	}
	RestUrl.getFunctionListByUnitURL = function() {
		return './getFunctionlist'
	}
	RestUrl.searchTaskForConfigurationURL = function() {
		return './searchConfiguartionPage'
	}
	RestUrl.getExportDataByIdURL = function(){
		return './getExportDataById'
	}
	RestUrl.getuserwithaccessforcommonemailURL = function(){
		return './getuserwithaccessforcommonemail'
	}

	RestUrl.sendcommonemailURL = function(){
		return './sendcommonemail'
	}

	/**
	 * To-DO List URL's
	 */
	RestUrl.getPosingTasksURL = function(){
		return './getposingtasks';
	};


	RestUrl.getWaitingForApprovalComplianceRepositoryURL = function(){
		return './waitingforapprovaltasks';
	};

	RestUrl.getReopenedRepositoryDataURL = function(){
		return './reopenedtasks';
	};

	/**
	 * @Date 08-Aug-2019
	 * For Repository Page calling new API's
	 */

	RestUrl.getEntityListByUserIdURL = function(){
		return './loadEntityById';
	};

	RestUrl.getUnitListByOrgaIdAndUserIdURL = function(){
		return './loadUnitById';
	};

	RestUrl.getFunctionListByUnitIdURL = function(){
		return './loadFunctions';
	};
	RestUrl.getExeEvalFunHeadListURL = function(){
		return './getExeEvalFuncHeadList'
	}
	RestUrl.getExportDataByIdURL = function(){
		return './getExportDataById'
	}
	RestUrl.getDownloadDataURL = function(){
		return './downloadData'
	}

	//Get task by FROM TO TO Date
	RestUrl.searchComplianceStatusPieChartURL = function () {
		return './searchgetoverallcompliancestatus'
	}


	/**
	 * new Dashboard controller code start
	 */


	RestUrl.getOverallGraphCountURL = function(){
		return './getoverallCountForDashboard';
	};

	RestUrl.getEntityRisksCountURL = function(){
		return './getEntityRisksCount';
	};

	RestUrl.getUnitRisksCountURL = function(){
		return './getUnitRisksCount';
	};

	RestUrl.getFunctionRiskGraphCountURL = function(){
		return './getFunctionRisksCount';
	};

	RestUrl.getLocationListByOrgaIdURL = function(){
		return './getLocationListByOrgaId';
	};

	RestUrl.filterDataByOrgaIdAndUnitIdURL = function(){
		return './filterDataByOrgaIdAndUnitIdURL';
	};

	/**
	 * new Dashboard controller end
	 */

	RestUrl.getTaskForChangeComplianceOwnerPageURL = function(){
		return './getTaskForChangeComplianceOwnerPage';
	};
	RestUrl.getExeListForActivationPageURL = function(){
		return './getExeListForActivationPage';
	};
	RestUrl.getEvalListForActivationPageURL = function(){
		return './getEvalListForActivationPage';
	};
	RestUrl.searchActivationPageURL = function(){
		return './searchActivationPage';
	};
	RestUrl.searchEnableDisablePageURL = function(){
		return './searchEnableDisablePage';
	};
	RestUrl.getExportReportByIdURL = function(){
		return './getExportReportById';
	};

	/**
	 * Import entity RestURL
	 */
	RestUrl.importEntityFileURL = function() {
		return './importentity';
	};

	/**
	 * Import Unit RestURL
	 */
	RestUrl.importLocationFileURL = function() {
		return './importunit';
	};


	/**
	 * Import Function RestURL
	 */
	RestUrl.importDepartmentFileURL = function() {
		return './importfunction';
	};

	RestUrl.uploadUserListURL = function(){
		return './importusersfromfile';
	};

	RestUrl.uploadEntityMappingListURL = function(){
		return './importentitymappingfromfile';
	};

	RestUrl.getExeListForChangeOwnerURL = function(){
		return './getExeListForChangeOwner';
	};

	RestUrl.getEvalListForChangeOwnerURL = function(){
		return './getEvalListForChangeOwner';
	};
	RestUrl.getFunHeadListForChangeOwnerURL = function(){
		return './getFunHeadListForChangeOwner';
	};
	RestUrl.approveAllTaskURL = function(){
		return './approveAllTask';
	};
	RestUrl.ExportXlsNewURL = function() {
		return './getExportDrillReport'
	}
	RestUrl.updateTasksCompletionURL = function() {
		return './updateTasksCompletion'
	}
	RestUrl.deleteTaskDocumentURL = function() {
		return './deleteTaskDocument'
	}

	RestUrl.approveSubTaskURL = function() {
		return './approveSubTask';
	};
	RestUrl.reopenSubTaskURL = function() {
		return './reopenSubTask';
	};
	RestUrl.updateSubTasksConfigurationURL = function() {
		return './updateSubTasksConfiguration';
	};
	RestUrl.deleteSubTaskDocumentURL = function() {
		return './deleteSubTaskDocument';
	};
	RestUrl.deleteSubTaskHistoryURL = function() {
		return './deleteSubTaskHistory';
	};
	RestUrl.updateSubTasksConfigurationDetailsURL = function() {
		return './updateSubTasksConfigurationDates';
	};
	RestUrl.getSubTasksHistoryURL = function() {
		return './getSubTaskHistoryList';
	};
	RestUrl.getTaskHistoryListForCalenderURL = function() {
		return './getTaskHistoryListForCalender';
	};

	RestUrl.getTaskListForDefaultTaskConfigurationURL = function() {
		return './getTaskListForDefaultTaskConfiguration';
	};

	RestUrl.getAllTasksURL = function() {
		return './getalltasksforrepository';
	};

	RestUrl.importTaskToCompleteURL = function() {
		return './importTaskToComplete';
	};

	RestUrl.downloadProofOfComplianceURL = function() {
		return './checkDocumentBeforeDownload';
	};

	RestUrl.importAssignTaskFileURL = function() {
		return './uploadAssignTaskList';
	};

	/**
	 * Auditors Dashboard Link
	 */

	RestUrl.auditorComplianceStatusPieChartURL = function() {
		return './auditTaskDashboard'
	}

	RestUrl.sendNonCompliedTaskURL = function() {
		return './makeNonCompliedTasks';
	};


	RestUrl.approverCompliedTasksURL = function() {
		return './approverCompliedTasksURL';
	};

	RestUrl.copyComplianceOwnerURL = function() {
		return './copyComplianceOwner'
	}

	/**
	 * End
	 */

	/** 
	 * @Logs URL's
	 */


	RestUrl.getHeadCountListURL = function() {
		return './getHeadCountsByLocation';
	};

	RestUrl.monthlyComplianceAuditChartURL = function() {
		return './getMonthlyComplianceStatus';
	};

	RestUrl.getQueryBuilderURL = function() {
		return './queryBuilder';
	};

	RestUrl.getTasksDeactivationQueryBuilderURL = function() {
		return './queryDeActivation';
	};

	RestUrl.getDisabletasksURL = function() {
		return './queryDisableTasks';
	};

	RestUrl.submitSimplyCompDocumentsURL = function() {
		return './submitSimplyCompDocumentsURL'
	}

	return RestUrl;

} ]);


