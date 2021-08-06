'use strict';

CMTApp.factory('ApiCallFactory', ['RestFactory', 'RestURLMappingService', 'HttpContentTypeService', function(RestFactory, RestURLMappingService, HttpContentTypeService){

	var ApiCallFactory = {};

	/**
	 * new analytical dashboard query
	 */
	ApiCallFactory.getOverDashboardCount = function(obj){
		return RestFactory.performPost(RestURLMappingService.getOverDashboardCountURL(), obj);
	};
	
	ApiCallFactory.getFunctionListByOrgaId = function(obj){
		return RestFactory.performPost(RestURLMappingService.getFunctionListByOrgaIdURL(), obj);
	};
	
	ApiCallFactory.finalComplianceAuditReport = function(obj) {
		return RestFactory.performPost(RestURLMappingService.finalComplianceAuditReportURL(), obj);
	};
	
	/**
	 * End new dashboard
	 */

	ApiCallFactory.searchComplianceStatusAuditPieChart = function(obj){
		return RestFactory.performPost(RestURLMappingService.searchComplianceStatusAuditPieChartURL(), obj);
	};
	
	ApiCallFactory.searchProgressReport = function(obj){
		return RestFactory.performPost(RestURLMappingService.searchProgressReportURL(), obj);
	};

	ApiCallFactory.searchComplianceAuditChart = function(obj){
		return RestFactory.performPost(RestURLMappingService.searchComplianceAuditChartURL(), obj);
	};


	ApiCallFactory.getQueryBuilder = function(obj){
		return RestFactory.performPost(RestURLMappingService.getQueryBuilderURL(), obj);
	};

	ApiCallFactory.getTasksDeactivationQueryBuilder = function(obj){
		return RestFactory.performPost(RestURLMappingService.getTasksDeactivationQueryBuilderURL(), obj);
	};

	ApiCallFactory.getDisabletasks = function(obj){
		return RestFactory.performPost(RestURLMappingService.getDisabletasksURL(), obj);
	};

	/**
	 * 
	 */

	/**
	 * @for all logs
	 */

	ApiCallFactory.getLoginLogs = function(){
		return RestFactory.performPost(RestURLMappingService.getLoginLogsURL());
	};

	ApiCallFactory.getAssignLogs = function(){
		return RestFactory.performPost(RestURLMappingService.getAssignLogsURL());
	};

	ApiCallFactory.getChangeCompOwnerLogs = function(){
		return RestFactory.performPost(RestURLMappingService.getChangeCompOwnerLogsURL());
	};

	ApiCallFactory.getConfigLogs = function(){
		return RestFactory.performPost(RestURLMappingService.getConfigLogsURL());
	};

	ApiCallFactory.getComplianceReportLogs = function(){
		return RestFactory.performPost(RestURLMappingService.getComplianceReportLogsURL());
	};

	ApiCallFactory.getmailLogs = function(){
		return RestFactory.performPost(RestURLMappingService.getmailLogsURL());
	};

	ApiCallFactory.getActivateDeActivateLogs = function(){
		return RestFactory.performPost(RestURLMappingService.getActivateDeActivateLogsURL());
	};


	ApiCallFactory.getReactivateLogs = function(){
		return RestFactory.performPost(RestURLMappingService.getReactivateLogsURL());
	};


	/**
	 * @End of all logs methods
	 */

	//Do login
	ApiCallFactory.doLogin = function(credentials){
		return RestFactory.performPost(RestURLMappingService.getAuthURL(), credentials);
	};
	//Forgot password
	/*ApiCallFactory.doLogin = function(credentials){
		return RestFactory.performPost(RestURLMappingService.getForgotPassword(), credentials, HttpContentTypeService.APPLICATION_JSON);
	};
	 */
	ApiCallFactory.doForgotPassword = function(credentials){
		return RestFactory.performPost(RestURLMappingService.getForgotPasswordURL(), credentials);
	};

	//get department list
	ApiCallFactory.getDeparmentList = function(){
		return RestFactory.performGet(RestURLMappingService.getDeparmentListURL());
	};

	//add deparment 
	ApiCallFactory.addDeparment = function(deparment){
		return RestFactory.performPost(RestURLMappingService.addDeparmentURL(),deparment);
	};

	//update department list
	ApiCallFactory.updateDeparment = function(deparment){
		return RestFactory.performPost(RestURLMappingService.updateDeparmentURL(),deparment);
	};

	//duplicate department list
	ApiCallFactory.duplicateDepartment = function(deparment){
		return RestFactory.performPost(RestURLMappingService.duplicateDepartmentURL(),deparment);
	};

	//get entity list
	ApiCallFactory.getEntityList = function(){
		return RestFactory.performGet(RestURLMappingService.getEntityListURL());
	};

	//get enity list
	ApiCallFactory.getAllEntityList = function(){
		return RestFactory.performGet(RestURLMappingService.getAllEntityListURL());
	};

	//add entity
	ApiCallFactory.addEntity = function(entity){
		return RestFactory.performPost(RestURLMappingService.addEntityURL(),entity);
	};

	//update entity
	ApiCallFactory.updateEntity = function(entity){
		return RestFactory.performPost(RestURLMappingService.updateEntityURL(),entity);
	};
	//duplicate entity
	ApiCallFactory.duplicateEntity = function(entity){
		return RestFactory.performPost(RestURLMappingService.duplicateEntityURL(),entity);
	};
	//get entity list
	ApiCallFactory.getMainEntityList = function(){
		return RestFactory.performGet(RestURLMappingService.getMainEntityListURL());
	};


	//Get entity mapping list

	ApiCallFactory.getEntityMappingList = function(){
		return RestFactory.performGet(RestURLMappingService.getEntityMappingListURL());
	};

	///Get select list for save entity
	/*ApiCallFactory.getEntitySelectList = function(entityId){
		return RestFactory.performPost(RestURLMappingService.getEntitySelectListURL(entityId),entityId);
	};
	 */
	ApiCallFactory.getUnitList = function(){
		return RestFactory.performGet(RestURLMappingService.getUnitListURL());
	};	

	//Get All unit list from the DB

	ApiCallFactory.getAllUnitList = function(){
		return RestFactory.performGet(RestURLMappingService.getAllUnitListURL());
	};


	//update unit
	ApiCallFactory.updateUnit = function(location){
		return RestFactory.performPost(RestURLMappingService.updateUnitURL(),location);
	};

	//add deparment 
	ApiCallFactory.addUnit = function(location){
		return RestFactory.performPost(RestURLMappingService.addUnitURL(),location);
	};

	//duplicate unit
	ApiCallFactory.duplicateUnit = function(location){
		return RestFactory.performPost(RestURLMappingService.duplicateUnitURL(),location);
	};

	ApiCallFactory.getFunctionList = function(){
		return RestFactory.performGet(RestURLMappingService.getFunctionListURL());
	};	

	ApiCallFactory.getFunctionListByUIdEId = function(obj){
		return RestFactory.performPost(RestURLMappingService.getFunctionListByUIdEIdURL(),obj);
	};



	//add entity mapping
	ApiCallFactory.addEntityMapping = function(entity){
		return RestFactory.performPost(RestURLMappingService.addEntityMappingURL(),entity);
	};

	//update entity
	ApiCallFactory.updateEntityMapping = function(entity){
		return RestFactory.performPost(RestURLMappingService.updateEntityMappingURL(),entity);
	};

	///user list
	ApiCallFactory.getUserList = function(){
		return RestFactory.performGet(RestURLMappingService.getUserListURL());
	};

	ApiCallFactory.getUserDetailsInfo = function(userId){
		return RestFactory.performPost(RestURLMappingService.getUserDetailsURL(),userId);
	};

	//getUnitListById
	ApiCallFactory.getUnitListById = function(entityId){
		return RestFactory.performPost(RestURLMappingService.getUnitListByIdURL(),entityId);
	};

	//get function list by unit Id
	ApiCallFactory.getFunctionListById = function(unitId){
		return RestFactory.performPost(RestURLMappingService.getFunctionListByIdURL(),unitId);
	};

	//add user
	ApiCallFactory.addUser = function(userObj){
		return RestFactory.performPost(RestURLMappingService.addUserURL(),userObj);
	};

	//update user
	ApiCallFactory.updateUser = function(userObj){
		return RestFactory.performPost(RestURLMappingService.updateUserURL(),userObj);
	};
	ApiCallFactory.checkDuplicateUserName = function(userObj){
		return RestFactory.performPost(RestURLMappingService.checkDuplicateUserNameURL(),userObj);
	};

	//enable disable User
	ApiCallFactory.enableDisableUser = function(userId){
		return RestFactory.performPost(RestURLMappingService.enableDisableUserURL(),userId);
	};


	//add access
	ApiCallFactory.addAccessLevel = function(userAccessObj){
		return RestFactory.performPost(RestURLMappingService.addAccessLevelURL(),userAccessObj);
	};

	//save access
	ApiCallFactory.saveAccess = function(userAccessObj){
		return RestFactory.performPost(RestURLMappingService.saveAccessURL(),userAccessObj);
	};



	//get access
	ApiCallFactory.getUserAccessList = function(userAccessObj){
		return RestFactory.performPost(RestURLMappingService.getUserAccessListURL(),userAccessObj);
	};


	/// import legal document
	ApiCallFactory.uploadLegalFile = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.uploadLegalFileURL(),fileObj);
	};

	//get access
	ApiCallFactory.getImportedTasksList = function(){
		return RestFactory.performGet(RestURLMappingService.getImportedTasksListURL());
	};

	//get designation list
	ApiCallFactory.getDesignationList = function(){
		return RestFactory.performGet(RestURLMappingService.getDesignationListURL());
	};

	//add designation
	ApiCallFactory.addDesignation = function(designation){
		return RestFactory.performPost(RestURLMappingService.addDesignationURL(),designation);
	};

	//update designation list
	ApiCallFactory.updateDesignation = function(designation){
		return RestFactory.performPost(RestURLMappingService.updateDesignationURL(),designation);
	};

	//duplicate designation list
	ApiCallFactory.duplicateDesignation = function(designation){
		return RestFactory.performPost(RestURLMappingService.duplicateDesignationURL(),designation);
	};

	//get compliance repository
	ApiCallFactory.getRepositoryList = function(AllTasks){
		return RestFactory.performPost(RestURLMappingService.getRepositoryListURL(),AllTasks);
	};

	ApiCallFactory.getWaitingForApprovalComplianceRepository = function(AllTasks){
		return RestFactory.performPost(RestURLMappingService.getWaitingForApprovalComplianceRepositoryURL(), AllTasks);
	};

	ApiCallFactory.getReopenedRepositoryData = function(AllTasks){
		return RestFactory.performPost(RestURLMappingService.getReopenedRepositoryDataURL(), AllTasks);
	};

	//Assign task relates calls
	ApiCallFactory.getCountriesList = function(){
		return RestFactory.performGet(RestURLMappingService.getCountriesListURL());
	};
	ApiCallFactory.getStateList = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getStateListURL(),cobj);
	};
	ApiCallFactory.getCategoryOfLawList = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getCategoryOfLawListURL(),cobj);
	};
	ApiCallFactory.getalllegiList = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getalllegiListURL(),cobj);
	};

	ApiCallFactory.getalllegiRuleList = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getalllegiRuleListURL(),cobj);
	};

	ApiCallFactory.getTaskList = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getTaskListURL(),cobj);
	};
	ApiCallFactory.assginTask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.assginTaskURL(),cobj);
	};

	//active deactive task
	ApiCallFactory.getActiveDeativeTaskList = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getActiveDeativeTaskListURL(),cobj);
	};

	ApiCallFactory.changeTaskStatus = function(cobj){
		return RestFactory.performPost(RestURLMappingService.changeTaskStatusURL(),cobj);
	};


	//enable/disable task
	ApiCallFactory.getTaskForEnableDisable = function(All_Tasks){
		return RestFactory.performPost(RestURLMappingService.getTaskForEnableDisableURL(),All_Tasks);
	};

	ApiCallFactory.getCalendarDateList = function(obj){
		return RestFactory.performPost(RestURLMappingService.getCalendarDateListURL(),obj);
	};

	ApiCallFactory.changeTaskStatusForEnableDisable = function(cobj){
		return RestFactory.performPost(RestURLMappingService.changeTaskStatusForEnableDisableURL(),cobj);
	};

	//get mapped tasks for configuration
	ApiCallFactory.getTasksForConfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getTasksForConfigurationURL(),cobj);
	};


	//get entity unit location list for add user page
	ApiCallFactory.getEntityUnitFunctionDesignationListReport = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getEntityUnitFunctionDesignationListReportURL(),cobj);
	};

	//get entity unit location list for add user page
	ApiCallFactory.getEntityUnitFunctionDesignationList = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getEntityUnitFunctionDesignationListURL(),cobj);
	};

	//complianceStatusPieChart
	ApiCallFactory.complianceStatusPieChart = function(obj){
		return RestFactory.performPost(RestURLMappingService.complianceStatusPieChartURL(),obj);
	};

	//entity Level Chart
	ApiCallFactory.entityLevelChart = function(obj){
		return RestFactory.performPost(RestURLMappingService.entityLevelChartURL(),obj);
	};

	//unit Level Chart
	ApiCallFactory.unitComplianceChart = function(obj){
		return RestFactory.performPost(RestURLMappingService.unitComplianceChartURL(),obj);
	};

	//function Level Chart
	ApiCallFactory.functionComplianceChart = function(obj){
		return RestFactory.performPost(RestURLMappingService.functionComplianceChartURL(),obj);
	};

	//Save tasks for configuration
	ApiCallFactory.saveTasksConfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.saveTasksConfigurationURL(),cobj);
	};

	//change Compliance Owner
	ApiCallFactory.changeComplianceOwner = function(cobj){
		return RestFactory.performPost(RestURLMappingService.changeComplianceOwnerURL(),cobj);
	};

	//Generate Reports
	ApiCallFactory.generateReports = function(cobj){
		return RestFactory.performPost(RestURLMappingService.generateReportsURL(),cobj);
	};

	//get tasks history
	ApiCallFactory.getTasksHistory = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getTasksHistoryURL(),cobj);
	};

	//get tasks details
	ApiCallFactory.getTasksDetails = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getTasksDetailsURL(),cobj);
	};

	//get tasks details
	ApiCallFactory.saveTasksCompletion = function(formData){
		return RestFactory.performPost(RestURLMappingService.saveTasksCompletionURL(),formData);
	};


	ApiCallFactory.getUserDetails = function(obj){
		return RestFactory.performPost(RestURLMappingService.getUsersDetailsURL(),obj);
	};


	ApiCallFactory.updateUserPic = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.updateUserPicURL(),fileObj);
	};

	ApiCallFactory.changePassword = function(obj){
		return RestFactory.performPost(RestURLMappingService.changePasswordURL(),obj);
	};

	//user logout
	ApiCallFactory.logout = function(obj){
		return RestFactory.performPost(RestURLMappingService.logoutURL(),obj);
	};

	ApiCallFactory.getDrilledReportList = function(obj){
		return RestFactory.performPost(RestURLMappingService.getDrilledReportListURL(),obj);
	};

	/*ApiCallFactory.downloadProofCompliance = function(obj){
			return RestFactory.performPost(RestURLMappingService.downloadProofComplianceURL(),obj);
		};*/

	//support Query
	ApiCallFactory.sendMailToSupport = function(obj){
		return RestFactory.performPost(RestURLMappingService.sendMailToSupportURL(),obj);
	};

	//Update tasks for configuration
	ApiCallFactory.updateTasksConfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.updateTasksConfigurationURL(),cobj);
	};

	//download user list
	ApiCallFactory.downloaduserlist = function(cobj){
		return RestFactory.performPost(RestURLMappingService.downloaduserlistURL(),cobj);
	};

	//send credentials
	ApiCallFactory.sendCredentials = function(cobj){
		return RestFactory.performPost(RestURLMappingService.sendCredentialsURL(),cobj);
	};

	//Default configuration
	ApiCallFactory.getTasksForDefaultConfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getTasksForDefaultConfigurationURL(),cobj);
	};

	//save default task configuration
	ApiCallFactory.SaveDefaultTaskConfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.SaveDefaultTaskConfigurationURL(),cobj);
	};
	//get Default Task
	ApiCallFactory.getDefaultTask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getDefaultTaskURL(),cobj);
	};
	//Initiate Default Task
	ApiCallFactory.initiateDefaultTask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.initiateDefaultTaskURL(),cobj);
	};

	//Approve Task
	ApiCallFactory.approveTask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.approveTaskURL(),cobj);
	};

	//Reopen Task
	ApiCallFactory.reopenTask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.reopenTaskURL(),cobj);
	};

	//Delete task history
	ApiCallFactory.deleteTaskHistory = function(cobj){
		return RestFactory.performPost(RestURLMappingService.deleteTaskHistoryURL(),cobj);
	};

	//Delete task mapping
	ApiCallFactory.deleteTaskMapping = function(cobj){
		return RestFactory.performPost(RestURLMappingService.deleteTaskMappingURL(),cobj);
	};

	//Default task configuration repository
	ApiCallFactory.getDefaultTaskList = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getDefaultTaskListURL(),cobj);
	};

	//Update default task configuration
	ApiCallFactory.updateDefaultTaskConfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.updateDefaultTaskConfigurationURL(),cobj);
	};

	//Get entity unit function drop down for show cause notice
	ApiCallFactory.getAccessWiseOrgaLocaDept = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getAccessWiseOrgaLocaDeptURL(),cobj);
	};

	//create show cause notice
	ApiCallFactory.saveShowCauseNotice = function(cobj){
		return RestFactory.performPost(RestURLMappingService.saveShowCauseNoticeURL(),cobj);
	};

	//List show cause notice
	ApiCallFactory.getAllShowCauseNotice = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getAllShowCauseNoticeURL(),cobj);
	};

	//Remove user access
	ApiCallFactory.removeUserAccess = function(cobj){
		return RestFactory.performPost(RestURLMappingService.removeUserAccessURL(),cobj);
	};

	//Save action Item
	ApiCallFactory.saveActionItem = function(cobj){
		return RestFactory.performPost(RestURLMappingService.saveActionItemURL(),cobj);
	};

	//List all action Items
	ApiCallFactory.getAllActionItem = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getAllActionItemURL(),cobj);
	};

	/*//download show cause notice document
		ApiCallFactory.downloadShowCauseDocument = function(cobj){
			return RestFactory.performPost(RestURLMappingService.downloadShowCauseDocumentURL(),cobj);
		};*/

	//Reason For NonCompliance 
	ApiCallFactory.updateTaskReasonForNonCompliance = function(cobj){
		return RestFactory.performPost(RestURLMappingService.updateTaskReasonForNonComplianceURL(),cobj);
	};

	//Get show cause notice details
	ApiCallFactory.getShowCauseNoticeDetails = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getShowCauseNoticeDetailsURL(),cobj);
	};

	//Update show case notice
	ApiCallFactory.updateShowCauseNotice = function(cobj){
		return RestFactory.performPost(RestURLMappingService.updateShowCauseNoticeURL(),cobj);
	};

	//Get User Mapping List
	ApiCallFactory.getUsermappinglist = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getUsermappinglistURL(),cobj);
	};

	//get export data
	ApiCallFactory.getExportData = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getExportDataURL(),cobj);
	};

	//getCertificateData
	ApiCallFactory.getCertificateDetails = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getCertificateDetailsURL(),cobj);

	};

	//download CertificateData
	ApiCallFactory.downloadCertificateDetails = function(){
		return RestFactory.performGet(RestURLMappingService.downloadCertificateDetailsURL());
	};

	//import sub tasks
	ApiCallFactory.getUserDefinedTask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getUserDefinedTaskURL(),cobj);

	};

	//Upload Sub Tasks
	ApiCallFactory.importsubtask = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.importsubtaskURL(),fileObj);
	};

	//imported sub tasks
	ApiCallFactory.getimportedsubtask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getimportedsubtaskURL(),cobj);
	};

	//imported sub tasks
	ApiCallFactory.getsubtasksforconfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getsubtasksforconfigurationURL(),cobj);
	};

	//save sub task configuration
	ApiCallFactory.savesubtasksconfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.savesubtasksconfigurationURL(),cobj);
	};

	//Get configured tasks for activation
	ApiCallFactory.getconfiguredsubtask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getconfiguredsubtaskURL(),cobj);
	};

	//activate deactivate sub tasks
	ApiCallFactory.updateStatus = function(cobj){
		return RestFactory.performPost(RestURLMappingService.updateStatusURL(),cobj);
	};

	//save sub task completion
	ApiCallFactory.savesubtaskcompletion = function(formData){
		return RestFactory.performPost(RestURLMappingService.savesubtaskcompletionURL(),formData);
	};

	//multiple task completion
	ApiCallFactory.gettaskformultiplecompletion = function(cobj){
		return RestFactory.performPost(RestURLMappingService.gettaskformultiplecompletionURL(),cobj);
	};

	//get all documents
	ApiCallFactory.getalldocuments = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getalldocumentsURL(),cobj);
	};

	//delete task document
	ApiCallFactory.deleteTaskDocument = function(cobj){
		return RestFactory.performPost(RestURLMappingService.deleteTaskDocumentURL(),cobj);
	};

	ApiCallFactory.getuserwithaccessforcommonemail = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getuserwithaccessforcommonemailURL(),cobj);
	};

	ApiCallFactory.sendcommonemail = function(cobj){
		return RestFactory.performPost(RestURLMappingService.sendcommonemailURL(),cobj);
	};

	ApiCallFactory.getPosingTasks = function(AllTasks){
		return RestFactory.performPost(RestURLMappingService.getPosingTasksURL(), AllTasks);
	};


	//Get task by FROM TO TO Date
	ApiCallFactory.searchComplianceStatusPieChart = function(cobj){
		return RestFactory.performPost(RestURLMappingService.searchComplianceStatusPieChartURL(),cobj);
	};


	/**
	 * new ApiCallFactory calling for Repository page
	 */

	ApiCallFactory.getEntityListByUserId = function(){
		return RestFactory.performPost(RestURLMappingService.getEntityListByUserIdURL());
	};

	ApiCallFactory.getUnitListByOrgaIdAndUserId = function(orgaId){
		return RestFactory.performPost(RestURLMappingService.getUnitListByOrgaIdAndUserIdURL(), orgaId);
	};

	ApiCallFactory.getFunctionListByUnitId = function(unitId){
		return RestFactory.performPost(RestURLMappingService.getFunctionListByUnitIdURL(), unitId);
	};

	ApiCallFactory.getExeEvalFunHeadList = function(obj){
		return RestFactory.performPost(RestURLMappingService.getExeEvalFunHeadListURL(),obj);
	};

	ApiCallFactory.getExportDataById = function(obj){
		return RestFactory.performPost(RestURLMappingService.getExportDataByIdURL(),obj);
	};

	ApiCallFactory.getExecutorList = function(obj){
		return RestFactory.performPost(RestURLMappingService.getExecutorListURL(),obj);
	};
	ApiCallFactory.getEvaluatorList = function(obj){
		return RestFactory.performPost(RestURLMappingService.getEvaluatorListURL(),obj);
	};
	ApiCallFactory.getFunHeadList = function(obj){
		return RestFactory.performPost(RestURLMappingService.getFunHeadListURL(),obj);
	};
	ApiCallFactory.getTaskListToAssign = function(obj){
		return RestFactory.performPost(RestURLMappingService.getTaskListToAssignURL(),obj);
	};
	ApiCallFactory.getFunctionListByUnit = function(obj){
		return RestFactory.performPost(RestURLMappingService.getFunctionListByUnitURL(),obj);
	};
	//get mapped tasks for configuration
	ApiCallFactory.searchTaskForConfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.searchTaskForConfigurationURL(),cobj);
	};

	ApiCallFactory.getExportDataById = function(obj){
		return RestFactory.performPost(RestURLMappingService.getExportDataByIdURL(),obj);
	};
	ApiCallFactory.getDownloadData = function(){
		return RestFactory.performGet(RestURLMappingService.getDownloadDataURL());
	};
	/*//getCertificateData
	ApiCallFactory.getMonthlyCertificateDetails = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getMonthlyCertificateDetailsURL(),cobj);		
	};

	//getCertificateData
	ApiCallFactory.getQuarterlyCertificateDetails = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getQuarterlyCertificateDetailsURL(),cobj);		
	};
	 */

	/**
	 * Dashboard data
	 */

	ApiCallFactory.getOverallGraphCount = function(obj){
		return RestFactory.performPost(RestURLMappingService.getOverallGraphCountURL(), obj);
	};

	ApiCallFactory.getEntityRisksCount = function(obj){
		return RestFactory.performPost(RestURLMappingService.getEntityRisksCountURL(), obj);
	};

	ApiCallFactory.getUnitRisksCount = function(obj){
		return RestFactory.performPost(RestURLMappingService.getUnitRisksCountURL(), obj);
	};

	ApiCallFactory.getFunctionRiskGraphCount = function(obj){
		return RestFactory.performPost(RestURLMappingService.getFunctionRiskGraphCountURL(), obj);
	};

	ApiCallFactory.getLocationListByOrgaId = function(obj){
		return RestFactory.performPost(RestURLMappingService.getLocationListByOrgaIdURL(), obj);
	};

	ApiCallFactory.filterDataByOrgaIdAndUnitId = function(obj){
		return RestFactory.performPost(RestURLMappingService.filterDataByOrgaIdAndUnitIdURL(), obj);
	};

	/**
	 * new dashboard methods end
	 */
	ApiCallFactory.getTaskForChangeComplianceOwnerPage = function(obj){
		return RestFactory.performPost(RestURLMappingService.getTaskForChangeComplianceOwnerPageURL(),obj);
	};
	ApiCallFactory.getExeListForActivationPage = function(obj){
		return RestFactory.performPost(RestURLMappingService.getExeListForActivationPageURL(),obj);
	};
	ApiCallFactory.getEvalListForActivationPage = function(obj){
		return RestFactory.performPost(RestURLMappingService.getEvalListForActivationPageURL(),obj);
	};
	ApiCallFactory.searchActivationPage = function(obj){
		return RestFactory.performPost(RestURLMappingService.searchActivationPageURL(),obj);
	};
	ApiCallFactory.searchEnableDisablePage = function(obj){
		return RestFactory.performPost(RestURLMappingService.searchEnableDisablePageURL(),obj);
	};
	ApiCallFactory.getExportReportById = function(obj){
		return RestFactory.performPost(RestURLMappingService.getExportReportByIdURL(),obj);
	};

	/**
	 * Import entity doc's
	 */
	ApiCallFactory.importEntityFile = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.importEntityFileURL(),fileObj);
	};

	/**
	 * Import Unit method
	 */

	ApiCallFactory.importLocationFile = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.importLocationFileURL(),fileObj);
	};

	/**
	 * Import Department Method
	 */
	ApiCallFactory.importDepartmentFile = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.importDepartmentFileURL(),fileObj);
	};

	ApiCallFactory.uploadUserList = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.uploadUserListURL(),fileObj);
	};

	ApiCallFactory.uploadEntityMappingList = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.uploadEntityMappingListURL(),fileObj);
	};
	ApiCallFactory.getExeListForChangeOwner = function(obj){
		return RestFactory.performPost(RestURLMappingService.getExeListForChangeOwnerURL(),obj);
	};
	ApiCallFactory.getEvalListForChangeOwner = function(obj){
		return RestFactory.performPost(RestURLMappingService.getEvalListForChangeOwnerURL(),obj);
	};
	ApiCallFactory.getFunHeadListForChangeOwner = function(obj){
		return RestFactory.performPost(RestURLMappingService.getFunHeadListForChangeOwnerURL(),obj);
	};
	ApiCallFactory.getTaskForChangeComplianceOwnerPage = function(obj){
		return RestFactory.performPost(RestURLMappingService.getTaskForChangeComplianceOwnerPageURL(),obj);
	};

	ApiCallFactory.importAssignTaskFile = function(fileObj){
		return RestFactory.performPostFile(RestURLMappingService.importAssignTaskFileURL(),fileObj);
	};
	ApiCallFactory.approveAllTask = function(Obj){
		return RestFactory.performPost(RestURLMappingService.approveAllTaskURL(),Obj);
	};
	ApiCallFactory.ExportXlsNew = function(obj){
		return RestFactory.performPost(RestURLMappingService.ExportXlsNewURL(),obj);
	};
	ApiCallFactory.updateTasksCompletion = function(obj){
		return RestFactory.performPost(RestURLMappingService.updateTasksCompletionURL(),obj);
	};
	ApiCallFactory.deleteTaskDocument = function(obj){
		return RestFactory.performPost(RestURLMappingService.deleteTaskDocumentURL(),obj);
	};

	ApiCallFactory.approveSubTask = function(obj){
		return RestFactory.performPost(RestURLMappingService.approveSubTaskURL(),obj);
	};
	ApiCallFactory.reopenSubTask = function(obj){
		return RestFactory.performPost(RestURLMappingService.reopenSubTaskURL(),obj);
	};
	ApiCallFactory.updateSubTasksConfiguration = function(obj){
		return RestFactory.performPost(RestURLMappingService.updateSubTasksConfigurationURL(),obj);
	};
	ApiCallFactory.deleteSubTaskDocument = function(obj){
		return RestFactory.performPost(RestURLMappingService.deleteSubTaskDocumentURL(),obj);
	};
	ApiCallFactory.deleteSubTaskHistory = function(obj){
		return RestFactory.performPost(RestURLMappingService.deleteSubTaskHistoryURL(),obj);
	};
	ApiCallFactory.updateSubTasksConfigurationDetails = function(obj){
		return RestFactory.performPost(RestURLMappingService.updateSubTasksConfigurationDetailsURL(),obj);
	};
	ApiCallFactory.getSubTasksHistory = function(obj){
		return RestFactory.performPost(RestURLMappingService.getSubTasksHistoryURL(),obj);
	};
	ApiCallFactory.getTaskHistoryListForCalender = function(obj){
		return RestFactory.performPost(RestURLMappingService.getTaskHistoryListForCalenderURL(),obj);
	};
	ApiCallFactory.getTaskListForDefaultTaskConfiguration = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getTaskListForDefaultTaskConfigurationURL(),cobj);
	};
	ApiCallFactory.getAllTasks = function(cobj){
		return RestFactory.performPost(RestURLMappingService.getAllTasksURL(),cobj);
	};
	ApiCallFactory.importTaskToComplete = function(cobj){
		return RestFactory.performPost(RestURLMappingService.importTaskToCompleteURL(),cobj);
	};
	ApiCallFactory.downloadProofOfCompliance = function(obj){
		return RestFactory.performPost(RestURLMappingService.downloadProofOfComplianceURL(),obj);
	};

	ApiCallFactory.approverCompliedTasks = function(cobj){
		return RestFactory.performPost(RestURLMappingService.approverCompliedTasksURL(),cobj);
	};

	ApiCallFactory.auditorComplianceStatusPieChart = function(obj){
		return RestFactory.performPost(RestURLMappingService.auditorComplianceStatusPieChartURL(),obj);
	};

	ApiCallFactory.sendNonCompliedTask = function(cobj){
		return RestFactory.performPost(RestURLMappingService.sendNonCompliedTaskURL(),cobj);
	};

	ApiCallFactory.getHeadCountList = function() {
		return RestFactory.performGet(RestURLMappingService.getHeadCountListURL());
	};

	ApiCallFactory.getQueryBuilder = function(obj){
		return RestFactory.performPost(RestURLMappingService.getQueryBuilderURL(), obj);
	};

	ApiCallFactory.getTasksDeactivationQueryBuilder = function(obj){
		return RestFactory.performPost(RestURLMappingService.getTasksDeactivationQueryBuilderURL(), obj);
	};

	ApiCallFactory.monthlyComplianceAuditChart = function(obj) {
		return RestFactory.performPost(RestURLMappingService.monthlyComplianceAuditChartURL(), obj);
	};

	ApiCallFactory.copyComplianceOwner = function(cobj){
		return RestFactory.performPost(RestURLMappingService.copyComplianceOwnerURL(),cobj);
	};

	ApiCallFactory.submitSimplyCompDocuments = function(cobj){
		return RestFactory.performPost(RestURLMappingService.submitSimplyCompDocumentsURL(),cobj);
	};


	ApiCallFactory.listSimplyCompDocuments = function(){
		return RestFactory.performGet(RestURLMappingService.listSimplyCompDocumentsURL());
	};
	
	ApiCallFactory.getTaskActivityList = function(){
		return RestFactory.performGet(RestURLMappingService.getTaskActivityListURL());
	};


	return ApiCallFactory;
}]);