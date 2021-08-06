'use strict';

CMTApp.controller('auditTasksController', ['$scope','$rootScope','$stateParams', '$state', 'ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$uibModal', '$filter','$window', '$http', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService, $uibModal, $filter,$window, $http) {
	/*console.clear();*/					

	var roleId = Storage.get('userDetais.sess_role_id');
	console.log('role : ' + roleId);

	$scope.roles = roleId;
	console.log('roles : ' + $scope.roles);

	$scope.taskList = [];
	$scope.taskAllList = [];

	$scope.totaltasksinloop = 0;
	$scope.totalTasks = 0;

	$scope.dashboardObj = {};
	$scope.task_ttrn_ids = [];

	$scope.statusDetailsClick = 0;

	$scope.approverApproveObj = {};
	$scope.compliedObj = {};
	$scope.approverNonCompliedObj = {};


	$scope.status=$stateParams.status
	console.log("staus is "+$scope.status);

	$scope.frequency=$stateParams.frequency
	//console.log("frequency is"+$scope.frequency);

	$scope.frequency=$stateParams.frequency;

//	$scope.today = function () {
//	$scope.taskCompletion.ttrn_completed_date = new Date();
//	};



	/**
	 * 
	 */


	$scope.mindate = new Date();
	$scope.dateformat="dd-MM-yyyy";
	//$scope.today();
	$scope.showcalendar = function ($event) {
		$scope.showdp = true;
	};
	$scope.showdp = false; // nitin

	var currentDate = new Date();

	var getCurrentMonth = currentDate.getMonth() + 1;
	console.log('current month : ' + getCurrentMonth);

	console.log('byMonth : ' + getCurrentMonth);
	if(getCurrentMonth >= 4 && getCurrentMonth <= 6){
		$scope.dashboardObj.fmonth = 1;
		console.log('Q1');
	}else if(getCurrentMonth >= 7 && getCurrentMonth <= 9){
		$scope.dashboardObj.fmonth = 2;
		console.log('Q2');
	}else if(getCurrentMonth >= 10 && getCurrentMonth <= 12){
		$scope.dashboardObj.fmonth = 3;
		console.log('Q3');
	}else {
		$scope.dashboardObj.fmonth = 4;
		console.log('Q4');
	}

	var currentDateString = (currentDate.getMonth()+1)+ '/'+ currentDate.getDate() + '/'+ currentDate.getFullYear();
	console.log('currentDateString : ' + currentDateString);

	$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$scope.format = 'dd-MM-yyyy'; //$scope.formats[0];
	$scope.altInputFormats = ['M!/d!/yyyy'];


	$scope.monthlyComplianceAuditChart = function() {

		

		var todayMinus = new Date();
		var todayPlus = new Date();
		// set previous 60 days date from today and next 30 from todays date in (From and to date)
		$scope.date_from =  new Date(todayMinus.setDate(todayMinus.getDate() - 60));
		$scope.date_to = new Date(todayPlus.setDate(todayPlus.getDate()+30));

		$("#pieLoader").show();
		$("#internalPieLoader").show();
		$("#entLoader").show();
		$("#entPieChartLoader").show();
		$("#financeLoader").show();
		$("#locLoader").show();
		$("#catLoader").show();
		$("#deptLoader").show();
		$("#overAllPieChart").hide();
		$("#quarterWiseLoader").show();
		DataFactory.setShowLoader(true);

		$scope.frmSearchDate = $filter('date')($scope.searchObj.date_from, 'yyyy-MM-dd');
		$scope.toSearchDate = $filter('date')($scope.searchObj.date_to, 'yyyy-MM-dd');
		// console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);
		// console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);

		var obj={
				/*date_from: $scope.frmSearchDate,
				date_to: $scope.toSearchDate,
				orgaId: $scope.orgaId,
				quarter: $scope.fquarter*/

				orga_id : $scope.searchObj.orga_id,
				loca_id : $scope.searchObj.loca_id,
				dept_id : $scope.searchObj.dept_id,
				date_from : $scope.frmSearchDate,
				date_to : $scope.toSearchDate
		};
		spinnerService.show('html5spinner');
		ApiCallFactory.monthlyComplianceAuditChart(obj).success(function(res,status){

			spinnerService.show('html5spinner');
			DataFactory.setShowLoader(false);
			//$scope.getEntityChart();
			if(status === 200){

				spinnerService.hide('html5spinner');

				$("#pieLoader").hide();
				$("#internalPieLoader").hide();
				$("#quarterWiseLoader").hide();
				$("#entLoader").hide();
				$("#entPieChartLoader").hide();
				$("#catLoader").hide();
				$("#financeLoader").hide();
				$("#locLoader").hide();
				$("#deptLoader").hide();
				$("#overAllPieChart").show();

				$scope.complianceChartData =res;
				$scope.taskList = res.taskList;
				console.log('res.taskList : ' + res.taskList);
				$scope.totaltasksinloop = res.totaltasksinloop;
				$scope.totalTasks = res.totalTasks;
				// console.log('$scope.taskList : ' + JSON.stringify($scope.taskList));

				/**
				 * tasks with status
				 */
				//console.log($scope.taskList)

				$scope.complied = res.Complied;
				$scope.NonComplied = res.NonComplied;
				$scope.PosingRisk = res.PosingRisk;
				$scope.Delayed = res.Delayed;
				$scope.DelayedReported = res.delayed_reported;
				$scope.Pending = res.Pending;
				$scope.WaitingForApproval = res.WaitingForApproval;
				$scope.ReOpened = res.ReOpened;
				$scope.totalcompletedtasks = res.totalcompletedtasks;
				$scope.totalactivetasks = res.totalactivetasks;

				// console.log('$scope.complied : ' + $scope.complied);
				// console.log('res : ' + res);

//				$scope.loadOtherGraphs();
//				$scope.loadQuarterGraphOnLoad();

//				$scope.getFincialGraphByMonth();

				var background = {
						type: 'linearGradient',
						x0: 0,
						y0: 0,
						x1: 0,
						y1: 1,
						colorStops: [{offset: 0, color: '#d2e6c9'},
							{offset: 1, color: 'white'}]
				};
			}else{
				spinnerService.hide('html5spinner');
				toaster.error("Failed", "Something went wrong while fetching \"OverAll Compliance Status\"");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			spinnerService.hide('html5spinner');
			console.log("complianceStatusPieChart==== " + error);
		});
	}

	// $scope.monthlyComplianceAuditChart();

	$scope.getEntityList = function() {		
		spinnerService.show('html5spinner');		
		$http({
			url : "./getentity",
			method : "get",				
		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.entityList = result.data;																							
				},
				function(result) {
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



	$scope.getCategoryLaw = function(unitList) {	 		 
		spinnerService.show('html5spinner');		
		$http({
			url : "./getCategory",
			method : "get",	

		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.categoryList = result.data;																							
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

	}
	$scope.getCategoryLaw();



	$scope.getTypeOfTask = function(unitList) {	 		 
		spinnerService.show('html5spinner');		
		$http({
			url : "./getTypeOfTask",
			method : "get",	

		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.typeOfTaskList = result.data;																							
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

	}
	$scope.getTypeOfTask();

	$scope.getFrequencyList = function(unitList) {	 		 
		spinnerService.show('html5spinner');		
		$http({
			url : "./getFrequencyList",
			method : "get",	

		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
					$scope.frequencyList = result.data;																							
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});

	}
	$scope.getFrequencyList();



	$scope.tmapObj = {};
	$scope.NtmapObj = {};
	$scope.errMessage = {};
	$scope.lega_date={};



//	Date functionality

	var curr_date = new Date();
	var curr_year = curr_date.getFullYear();
	$scope.dashboardObj.fyear = curr_year;


	$scope.format = 'dd-MM-yyyy'; //$scope.formats[0];

	$scope.open_from = function() {
		$scope.popup_from.opened = true;
	};

	$scope.open_to = function() {
		$scope.popup_to.opened = true;
	};

	$scope.popup_from = {
			opened: false
	};	   
	$scope.popup_to = {
			opened: false
	};

	$scope.validateDates = function() {
		$scope.errMessage = {};
		var from_date = null;
		var from_to = null;

		var from = angular.isDefined($scope.date_from);
		if(from)
			from_date = new Date($scope.date_from);
		else
			$scope.errMessage.date_from = 'Select from date.';

		var to = angular.isDefined($scope.date_to);

		if(to)
			from_to = new Date($scope.date_to);
		else
			$scope.errMessage.date_to = 'Select to date.';

		var flag = true;
		if(from && to){
			if(from_date >= from_to){ 
				$scope.errMessage.date_from = 'From date must be less than "To" date.';
				flag = false;
			}else
				flag = true;

			if(from_to <= from_date){ 
				$scope.errMessage.date_to = 'To date must be greater than "From" date.'; 
				flag = false;
			}else
				flag = true;
		}

		if(from && to && flag)
			return true;
		else
			return false;
	}

	$scope.getDates =function(){

		if($scope.tmapObj.ttrn_prior_days_buffer<0)
			$scope.tmapObj.ttrn_prior_days_buffer = 0;
		/*$scope.unit_date = new Date($scope.lega_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.func_date = new Date($scope.unit_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.eval_date = new Date($scope.func_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.exec_date = new Date($scope.eval_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);*/
	}



	/**
	 * @Approver Modal's code started
	 */


	$scope.reOpen={
			ttrn_id : 0,
			roleId: 0,
			reopen_comment : "",
			hPage : "auditPage"
	}

	$scope.approverApproveObj = {
			ttrn_id : 0,
			roleId: 0,
			reopen_comment : "",
			hPage : "auditPage"
	};

	$scope.compliedObj = {
			ttrn_id : 0,
			roleId: 0,
			reopen_comment : "",
			hPage : "auditPage"
	};


	$scope.approverNonCompliedObj = {
			ttrn_id : 0,
			roleId: 0,
			reopen_comment : "",
			hPage : "auditPage"
	};

	$scope.reopenTaskApprover = function(data){
		$("#reOpenTask").modal();
		console.log('reopenTaskApprover clicked');
		$scope.reOpen.ttrn_id = data.ttrn_id;
		$scope.reOpen.client_task_id = data.client_task_id;
	};

	$scope.ApproverApproveTaskPage = function(data){
		$("#ApproverApproveTaskPageModal").modal();
		console.log('reopenTaskApprover clicked');
		$scope.reOpen.ttrn_id = data.ttrn_id;
		$scope.reOpen.client_task_id = data.client_task_id;
	};

	$scope.ApproverCompliedTaskPage = function(data){
		$("#ApproverCompliedTaskPageModal").modal();
		console.log('reopenTaskApprover clicked');
		$scope.reOpen.role_id = $scope.roleId;
		$scope.reOpen.ttrn_id = data.ttrn_id;
		$scope.reOpen.client_task_id = data.client_task_id;
	};


	$scope.reopenTask=function(data){
		//$scope.taskCompletion.ttrn_id=ttrn_id;
		$scope.reOpen.roleId = roleId;
		$scope.reOpen.ttrn_id = data.ttrn_id;
		$scope.reOpen.client_task_id = data.client_task_id;

		//console.log("TTRN ID "+$scope.taskCompletion.ttrn_id);
	}

	//Reopen Task
	$scope.sendReOpenTask=function(){
		//if(formValid){
		ApiCallFactory.reopenTask($scope.reOpen).success(function(res,status) {
			if(status === 200 && res.responseMessage == "Success") {
				//$scope.reOpen.reopen_comment="";	
				$('#reOpenTask').modal("hide");
				$scope.getTaskHistoryList($scope.reOpen.client_task_id);
				$scope.getTaskDetails($scope.reOpen.client_task_id);
				//	$window.location.reload();
				$scope.monthlyComplianceAuditChart();
			}

		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});
		//}
	}

	//Approve task
	// approveTask
	$scope.sendAppApproveTask = function(){

		// approveTask
		ApiCallFactory.approverCompliedTasks($scope.approverApproveObj).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				toaster.success("Success", "Task approved successfully.");
				$scope.monthlyComplianceAuditChart();

//				$scope.getTaskHistoryList(client_task_id);
//				$scope.getTaskDetails(client_task_id);

			}else{
				alert(res.responseMessage);
			}	
		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});
	}

	$scope.searchObj = {
			orga_id : '0',
			loca_id : '0',
			dept_id : '0',
			date_from : '',
			date_to : ''
	}

	$scope.searchAuditRepo = function() {
		$http({
			url : "./searhMonthlyComplianceStatus",
			method : "get",
			params : {
				// 'data' : $scope.searchObj
				'orga_id' : $scope.searchObj.orga_id,
				'loca_id' : $scope.searchObj.loca_id,
				'dept_id' : $scope.searchObj.dept_id,
				'date_from' : $scope.searchObj.date_from,
				'date_to' : $scope.searchObj.date_to
			}
		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');

					// $scope.complianceChartData =res;
					$scope.taskList = result;

					$scope.repositoryList = result;
					$scope.taskList = JSON.stringify(result);
					console.log('length : ' + $scope.taskList);
					// $scope.defaultTasks.length = 0;
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});
	}

	$scope.getTaskHistoryList=function(task_id){
		var obj={tmap_client_task_id:task_id};
		//	alert(JSON.stringify(obj));
		if(!angular.isUndefined(task_id) && task_id!=0){
			ApiCallFactory.getTasksHistory(obj).success(function(res,status){
				$scope.task_history = res.task_history;
				$scope.subTaskHistory = res.subTaskHistory;
				$('#changeTask').modal();
				$scope.editDate={
						role:Storage.get('userDetais.sess_role_id'),
				}
				//console.log("role "+$scope.editDates.role);
				$scope.getTaskDetails(task_id);

				//$scope.task_history.documents = 
			}).error(function(error){
				console.log("task History====="+error);
			});
		}
	}


	$scope.reOpen={
			ttrn_id : 0,
			reopen_comment : "",
			auditor: "auditPage"
	}
	$scope.reopenTask = function(data) {
		$scope.reOpen.ttrn_id = data.ttrn_id;
		$scope.reOpen.client_task_id = data.client_task_id;
	}


	$scope.sendReOpenTask=function(){
		//if(formValid){
		ApiCallFactory.reopenTask($scope.reOpen).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success") {
				//$scope.reOpen.reopen_comment="";	
				toaster.success("Success", "Task reopened successfully.");
				$('#reOpenTask').modal("hide");
				$scope.getTaskHistoryList($scope.reOpen.client_task_id);
				$scope.getTaskDetails($scope.reOpen.client_task_id);
				//	$window.location.reload();
				$scope.monthlyComplianceAuditChart();
			}

		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});
		//}
	}

	/**
	 * sendNonCompliedTask Non-Complied-Task function
	 */

	$scope.sendNonCompliedTask = function() {
		console.log('non-complied : ' + JSON.stringify($scope.approverNonCompliedObj));

		ApiCallFactory.sendNonCompliedTask($scope.approverNonCompliedObj).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){

				$('#ApproverCompliedTaskPageModal').modal("hide");
				toaster.success("Warning", "Task Non Complied.");
				$scope.monthlyComplianceAuditChart();
//				$scope.getTaskHistoryList(client_task_id);
//				$scope.getTaskDetails(client_task_id);

			}else{
				alert(res.responseMessage);
			}	
		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});

	}

	/**
	 * Make Complied Tasks function start
	 */


	$scope.makeCompliedTask = function() {
		console.log('complied : ' + JSON.stringify($scope.compliedObj));

		ApiCallFactory.approverCompliedTasks($scope.compliedObj).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){

				toaster.success("Warning", "Task Complied.");
				$('#makeCompliedTaskPageModal').modal("hide");
				$scope.monthlyComplianceAuditChart();
//				$scope.getTaskHistoryList(client_task_id);
//				$scope.getTaskDetails(client_task_id);

			}else{
				toaster.success("Danger", "Something went wrong.");
				console.log(res.responseMessage);
			}	
		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});

	}


	/**
	 * End
	 */

	//Approve task
	$scope.approveTask=function(ttrn_id, client_task_id){
		$scope.taskCompletion.ttrn_id=ttrn_id;

		ApiCallFactory.approveTask($scope.taskCompletion).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				toaster.success("Success", "Task approved successfully.");
				$('#ApproverApproveTaskPageModal').modal("hide");
				$scope.monthlyComplianceAuditChart();
//				$scope.getTaskHistoryList(client_task_id);
//				$scope.getTaskDetails(client_task_id);

			}else{
				alert(res.responseMessage);
			}
		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});

	}

	/**
	 * @Approver Modal's code started
	 */

	$scope.reopenTaskApprover = function(data){
		$("#reOpenTask").modal();
		console.log('reopenTaskApprover clicked');
		$scope.reOpen.role_id = '12';
		$scope.reOpen.ttrn_id = data.ttrn_id;
		$scope.reOpen.client_task_id = data.client_task_id;
	};


	$scope.ApproverApproveTaskPage = function(data){
		$("#ApproverApproveTaskPageModal").modal();
		console.log('reopenTaskApprover clicked');
		$scope.approverApproveObj.role_id = '12';
		$scope.approverApproveObj.ttrn_id = data.ttrn_id;
		$scope.approverApproveObj.client_task_id = data.client_task_id;
	};

	$scope.makeCompliedModalPage = function(data){
		$("#makeCompliedTaskPageModal").modal();
		console.log('makeCompliedTaskPageModal clicked');
		$scope.compliedObj.role_id = '12';
		$scope.compliedObj.ttrn_id = data.ttrn_id;
		$scope.compliedObj.client_task_id = data.client_task_id;
	};


	$scope.ApproverCompliedTaskPage = function(data){
		$("#ApproverCompliedTaskPageModal").modal();
		console.log('ApproverCompliedTaskPage clicked');
		$scope.approverNonCompliedObj.role_id = '12';
		$scope.approverNonCompliedObj.ttrn_id = data.ttrn_id;
		$scope.approverNonCompliedObj.client_task_id = data.client_task_id;
	};


	$scope.sendNonCompliedTaskPage = function() {
		$scope.approverNonCompliedObj.role_id = '12';
		$scope.approverNonCompliedObj.ttrn_id = data.ttrn_id;
		$scope.approverNonCompliedObj.client_task_id = data.client_task_id;
	}



	$scope.getTaskDetails=function(task_id){ 
		//alert(task_id);
		if($scope.statusDetailsClick == 0){ //If click status is 0 then only get details from DB
			//spinnerService.show('html5spinner');
			var obj={tmap_client_task_id:task_id};
			//alert(JSON.stringify(obj));
			if(!angular.isUndefined(task_id) && task_id!=0){
				ApiCallFactory.getTasksDetails(obj).success(function(res, status) {
					spinnerService.hide('html5spinner');
					$scope.task_details = res;	
					$scope.statusDetailsClick = 0;
				}).error(function(error) {
					spinnerService.hide('html5spinner');
					console.log("task Details====="+error);
				});
			}
		}
	}


	/**
	 * End 
	 */


	/**
	 * Dashboard Queries Started
	 */


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
	$scope.CompleteTaskPage = function(ttrn_id,document,comments,completedDate,status,reason,frequency,legalDueDate,clientTaskId){ //document contain 1 - mandatory doc and 0 - Non mandatory
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

		var legal =legalDueDate.split("-");
		var legalDueDate = new Date(legal[2], legal[1] - 1, legal[0]);
		$scope.legalDueDate= legalDueDate;

		console.log("Legal Due Date is : "+$scope.legalDueDate);															
		$scope.taskCompletion.ttrn_reason_for_non_compliance=reason;
		console.log("reason:" +$scope.taskCompletion.ttrn_reason_for_non_compliance);

		if(completedDate!=0 ){
			var compDate =completedDate.split("-");
			$scope.taskCompletion.ttrn_completed_date = new Date(compDate[2],compDate[1]-1,compDate[0]);
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

			ApiCallFactory.saveTasksCompletion(formData).success(function(res,status){
				//alert($scope.task.task_id);
				spinnerService.hide('html5spinner');
				if(res.responseMessage=='Success'){
					toaster.success("Success", "Task completed successfully.");
					$('#completeTask').modal('hide');	
					$scope.task_ttrn_ids = [];
					$('#ttrn_proof_of_compliance').val('');
					$scope.getTaskHistoryList($scope.task.task_id);
					$scope.getTaskDetails($scope.task.task_id);
					$("#taskCompletionLoader").modal('hide');
					$("#changeTask").modal();
					$scope.showOnMultipleSelection=false;

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
				$("#taskCompletionLoader").modal('hide');
				console.log("task Completion====="+error);
			});
		}
	}

//	Multiple task completion

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
						ttrn_id: item.ttrn_idk
						//tmap_lexcare_task_id:item.lexcare_task_id
				}
				$scope.task_ttrn_ids.push(obj);
			} else {
				$scope.task_ttrn_ids = [];
			}
		});
	};
//	End Multiple task completion code

	$scope.deleteTask=function(ttrn_id,client_task_id,ev,index){
		/*  var confirm = $mdDialog.confirm()
		          .title('Are you sure you want to delete the task?')
		          .targetEvent(ev)
		          .ok('Yes')
		          .cancel('NO');*/
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



	/**
	 * download proof of compliance
	 */

	$scope.downloadProof = function(udoc_id,client_task_id){	
		var obj={doc_id:udoc_id};
		ApiCallFactory.downloadProofOfCompliance(obj).success(function(res,status){
			//spinnerService.hide('html5spinner');
			console.log("responseMessage:" +res[0].responseMessage);
			if(res[0].responseMessage == "Failed") {
				toaster.error("Failed", "This file contains malicious data which can damage your machine.The file will not be downloaded and will be deleted from the System.");
				$scope.deleteDocument.udoc_id			  = udoc_id;
				ApiCallFactory.deleteTaskDocument_s($scope.deleteDocument).success(function(res,status){
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


	/**
	 * End
	 */



}]);