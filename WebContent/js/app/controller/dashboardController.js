'use strict';

CMTApp.controller('dashboardController', ['$scope','$rootScope','$stateParams', '$state', 'ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$uibModal', '$filter','$window', '$http', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService, $uibModal, $filter, $window, $http) {

	$scope.innerWidth = "Total Width: " + $window.innerWidth + "px";
	// console.log('width : ' + $scope.innerWidth);

	var loca_type 		= [];
	var subUnitArray    = [];
	$scope.deleteDocument = {};
	$scope.dashboardObj = {};
	$scope.entityListt  = [];
	$scope.taskObj = {};

	//For multiple task completion
	$scope.multipleCompletion = {};
	$scope.sub_task_id = null;

	$scope.varEntity 	= [];
	$scope.UnitList     = [];
	$scope.FunctionList = [];
	$scope.objStatus = null;

	$scope.complied = 0;
	$scope.NonComplied = 0;
	$scope.PosingRisk = 0;
	$scope.Delayed = 0;
	$scope.totaltasksinloop = 0;
	$scope.totalactivetasks = 0;
	$scope.totalcompletedtasks = 0;
	$scope.Pending = 0;
	$scope.WaitingForApproval = 0;
	$scope.ReOpened = 0;
	$scope.DelayedReported = 0;
	$scope.dashboardObj.fquarter = 0;
	$scope.entityName = null;
	//nitin
	$scope.AllTasks				= [];
	$scope.searchObj 			= {};
	$scope.configObj = {};
	$scope.originalEntity 		= [];
	$scope.originalUnit   		= [];
	$scope.originalFunction   	= [];
	$scope.originalExecutor   	= [];
	$scope.originalEvaluator   	= [];
	$scope.originalCatLaw       = [];
	$scope.alllegiList			= {};
	$scope.originalLegiList     = [];
	$scope.originalAllRuleList  = {};
	$scope.OriginalAllTasks     = [];
	$scope.OriginalEvents       = [];
	$scope.OriginalSubEvents    = [];

	$scope.UnitList             = [];
	$scope.FunctionList         = [];

	$scope.showAdvanceSearch    = false;
	$scope.CategoryOfLaw        = [];
	$scope.impactList 			= [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
	$scope.Proh_Pres            = [{id:"Prohibitive",name:"Prohibitive"},{id:"Prescriptive",name:"Prescriptive"}];
	$scope.legiList				= {selected:""};
	$scope.legiRuleL			= {selected:""};
	$scope.legiRuleL.selected   = undefined;
	$scope.legiList.selected    = undefined;
	$scope.event                = {};
	$scope.sub_event            = {};


	$scope.legiRuleList        = [];
	$scope.scrollTasks = [];
	$scope.defaultInitiation = {};
	$scope.defaultInitiation.date_of_initiation = new Date();
	$scope.showButton = true;

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
	$scope.client_task_id = null;
	//$scope.eventNotOccured=false;

	$scope.task_details = [];
	$scope.taskCompletion = {};
	$scope.subTaskCompletion = {};
	$scope.reOpen = {};
	$scope.status = null;
	$scope.showOnMultipleSelection=false;
	$scope.dashboardObj.tasks_list = [];
	$scope.taskCompletion.ttrn_completed_date = new Date();
	$scope.currentDate = new Date();
	// console.log("Todays Date is : "+$scope.currentDate);

	//$scope.taskCompletion.ttrn_ids = [];
	$scope.task_ttrn_ids = [];

	$scope.statusDetailsClick = 0;



	if(!angular.isUndefined($stateParams.ttrn_id) && $stateParams.ttrn_id!=0){
		$scope.task.ttrn_id=$stateParams.ttrn_id;
	}else{
		$scope.task.ttrn_id=$stateParams.ttrn_id;
	}

	if(!angular.isUndefined($stateParams.document) && $stateParams.document!=0){
		$scope.task.document=$stateParams.document;
	}else{
		$scope.task.document=$stateParams.document;
	}

	if(!angular.isUndefined($stateParams.comments) || $stateParams.comments!=''){
		$scope.taskCompletion.ttrn_performer_comments=$stateParams.comments;
	}else{
		//$scope.taskCompletion.ttrn_performer_comments=$stateParams.comments;
	}

	$scope.status=$stateParams.status
	// console.log("staus is "+$scope.status);

	$scope.frequency=$stateParams.frequency
	//// console.log("frequency is"+$scope.frequency);

	$scope.frequency=$stateParams.frequency
	//// console.log("frequency is"+$scope.frequency);

	//$scope.legalDueDate
	if(!angular.isUndefined($stateParams.legalDueDate) && $stateParams.legalDueDate!=0){
		var legal = $stateParams.legalDueDate.split("-");
		var legalDueDate = new Date(legal[2], legal[1] - 1, legal[0]);
		$scope.legalDueDate= legalDueDate;

	}else{

	}
	// console.log("Legal Due Date is : "+$scope.legalDueDate);

	if(!angular.isUndefined($stateParams.reason) || $stateParams.reason!=''){
		$scope.taskCompletion.ttrn_reason_for_non_compliance=$stateParams.reason;
	}else{
		//$scope.taskCompletion.ttrn_performer_comments=$stateParams.comments;
	}

	$scope.today = function () {
		$scope.taskCompletion.ttrn_completed_date = new Date();
	};
	$scope.mindate = new Date();
	$scope.dateformat="dd-MM-yyyy";
	$scope.today();
	$scope.showcalendar = function ($event) {
		$scope.showdp = true;
	};
	$scope.showdp = false;//nitin

	var currentDate = new Date();

	var getCurrentMonth = currentDate.getMonth() + 1;
	// console.log('current month : ' + getCurrentMonth);

	// console.log('byMonth : ' + getCurrentMonth);
	if(getCurrentMonth >= 4 && getCurrentMonth <= 6){
		$scope.dashboardObj.fmonth = 1;
		// console.log('Q1');
	}else if(getCurrentMonth >= 7 && getCurrentMonth <= 9){
		$scope.dashboardObj.fmonth = 2;
		// console.log('Q2');
	}else if(getCurrentMonth >= 10 && getCurrentMonth <= 12){
		$scope.dashboardObj.fmonth = 3;
		// console.log('Q3');
	}else {
		$scope.dashboardObj.fmonth = 4;
		// // console.log('Q4');
	}

	var currentDateString = (currentDate.getMonth()+1)+ '/'+ currentDate.getDate() + '/'+ currentDate.getFullYear();
	// // console.log('currentDateString : ' + currentDateString);
	// $scope.dashboardObj.fmonth = 1;

	/**
	 * Hide and show functions for dashboard
	 */


	$scope.overAllGraphTable = true;
	$scope.IsVisibleEntityGraph = false;

	$scope.OverallClick = function(){
		$scope.overAllGraphTable = $scope.overAllGraphTable = true;
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = false;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = false;
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = false;
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = false;
	}

	$scope.entityClick = function(){
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = true;
		$scope.overAllGraphTable = $scope.overAllGraphTable = false;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = false;
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = false;
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = false;
	}

	$scope.unitClick = function() {
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = true;
		$scope.overAllGraphTable = $scope.overAllGraphTable = false;
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = false;
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = false;
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = false;
	}

	$scope.functionClick = function() {
		// // console.log('functionClick : ');
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = true;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = false;
		$scope.overAllGraphTable = $scope.overAllGraphTable = false;
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = false;
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = false;
	}

	$scope.financialClick = function() {
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = true;
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = false;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = false;
		$scope.overAllGraphTable = $scope.overAllGraphTable = false;
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = false;
	}

	$scope.YearList = 
		[
			{year:2016,year_id:"2016-2017"},
			{year:2017,year_id:"2017-2018"},
			{year:2018,year_id:"2018-2019"},
			{year:2019,year_id:"2019-2020"},
			{year:2020,year_id:"2020-2021"},
			{year:2021,year_id:"2021-2022"},
			{year:2022,year_id:"2022-2023"},
			{year:2023,year_id:"2023-2024"}
			];

	$scope.MonthList = [
		{month:"January",month_id:0},
		{month:"February",month_id:1},
		{month:"March",month_id:2},
		{month:"April",month_id:3},
		{month:"May",month_id:4},
		{month:"June",month_id:5},
		{month:"July",month_id:6},
		{month:"August",month_id:7},
		{month:"September",month_id:8},
		{month:"October",month_id:9},
		{month:"November",month_id:10},
		{month:"December",month_id:11}
		];

	$scope.QuarterList = [
		{month: "--select--", month_id: 0},
		{ month: "Quarter 1", month_id: 1 },
		{ month: "Quarter 2", month_id: 2 },
		{ month: "Quarter 3", month_id: 3 },
		{ month: "Quarter 4", month_id: 4 }
		];


	$scope.CategoryList = [];
	$scope.categoryList = [
		{ id: 1, name: "East" }, 
		{ id: 2, name: "West" }, 
		{ id: 3, name: "South" }, 
		{ id: 4, name: "North" }
		];


	var curr_date = new Date();
	var curr_year = curr_date.getFullYear();
	//$scope.dashboardObj.fyear = curr_year;


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
	$scope.taskList = [];
	$scope.taskAllList = [];

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
	//return false;


	//Get Pie Chart for compliance status
	$scope.getcomplianceStatusPieChart = function() {

		var todayMinus = new Date();
		var todayPlus = new Date();
		// set previous 60 days date from today and next 30 from todays date in (From and to date)
		$scope.date_from =  new Date(todayMinus.setDate(todayMinus.getDate() - 20)); // 15 //60
		$scope.date_to = new Date(todayPlus.setDate(todayPlus.getDate() + 20)); // 5 // 30

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

		$scope.frmSearchDate = $filter('date')($scope.date_from, 'yyyy-MM-dd');
		$scope.toSearchDate = $filter('date')($scope.date_to, 'yyyy-MM-dd');
		// // console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);
		// // console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);

		var obj={
				date_from: $scope.frmSearchDate,
				date_to: $scope.toSearchDate,
				orgaId: $scope.orgaId,
				quarter: $scope.fquarter
		};

		ApiCallFactory.complianceStatusPieChart(obj).success(function(res,status){
			DataFactory.setShowLoader(false);
			//$scope.getEntityChart();
			if(status === 200){
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

				/**
				 * tasks with status
				 */
				//// console.log($scope.taskList)

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

				// // console.log('$scope.complied : ' + $scope.complied);
				// // console.log('res : ' + res);

				$scope.loadOtherGraphs();
				$scope.loadQuarterGraphOnLoad();

				$scope.getFincialGraphByMonth();

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
				toaster.error("Failed", "Something went wrong while fetching \"OverAll Compliance Status\"");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			// // console.log("complianceStatusPieChart===="+error);
		});
	}

	$scope.getcomplianceStatusPieChart();



	/**
	 * KPI Boxes DrilledReport
	 */


	$('.panel-complied').click(function() {
		var complied = $('.panel-complied .text-center').text();
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "Complied";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		// {'chart_name':'overall','status':'Complied','entity':"",'unit':"",'department':""}
		$scope.drilledReport($scope.searchObj);
		// alert(complied);
	});

	$('.panel-posing').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "PosingRisk";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('.panel-noncomplied').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "NonComplied";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('.panel-wfapp').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "Waiting For Approval";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('.panel-reopened').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "Re-Opened";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('.panel-delayed').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "Delayed";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('.panel-delayedReported').click(function() {
		// // console.log('searchObj.status : ' + $scope.searchObj.status);
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "DelayedReported";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});


	/**
	 * KPI Boxes DrilledReport code End
	 */



	/**
	 * All Graph Table drilled report 
	 */

	/**
	 * for overall
	 */

	$('#CompliedClick').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "Complied";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('#PosingRiskClick').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "PosingRisk";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('#NonCompliedClick').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "NonComplied";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('#DelayedClick').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "Delayed";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('#WaitingForApprovalClick').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "Waiting For Approval";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('#ReOpenedClick').click(function() {
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "Re-Opened";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('#DelayedReportedClick').click(function() {
		// console.log('searchObj.status : ' + $scope.searchObj.status);
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "DelayedReported";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	$('.panel-delayedReported').click(function() {
		// console.log('searchObj.status : ' + $scope.searchObj.status);
		$scope.searchObj.chart_name = "overall";
		$scope.searchObj.status = "DelayedReported";
		$scope.searchObj.entity = 0;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	});

	/**
	 * Overall drilled report end
	 */


	/**
	 * Entity Wise Graph Click Function
	 */

	$scope.entityName = "";
	$scope.entityOneClickEvent = function(ent) {
		$scope.entityName = ent;
		// console.log('value is : ' + $scope.entityName);
		$scope.searchObj.chart_name = "entityLevel";
		$scope.searchObj.status = "Complied";
		$scope.searchObj.entity = $scope.entityName;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	}

	$scope.entityTwoClickEvent = function(ent) {
		$scope.entityName = ent;
		// console.log('value is : ' + $scope.entityName);
		$scope.searchObj.chart_name = "entityLevel";
		$scope.searchObj.status = "PosingRisk";
		$scope.searchObj.entity = $scope.entityName;
		$scope.searchObj.unit = 0;
		$scope.searchObj.department = 0;
		$scope.drilledReport($scope.searchObj);
	}

	$scope.entityDrilledObj={};
	$('#entityLevel').bind('dataPointMouseUp', function(e, data) {
//		$scope.entityDrilledObj.fromDate = obj.fromDate;
//		$scope.entityDrilledObj.toDate = obj.toDate;
//		$scope.entityDrilledObj.status =  data.series.title;
//		$scope.entityDrilledObj.entity =  data.x;
//		$scope.entityDrilledReport($scope.entityDrilledObj);
		$scope.drilledReport({'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0});
	});

	$('#CategoryBarChart').bind('dataPointMouseUp', function (e, data) {
		// console.log('CategoryBarChart clicked ');
		$scope.drilledReport({ 'chart_name': 'categoryLevel', 'status': data.series.title, 'entity': 0, 'unit': data.x, 'department': 0 });
	});

	// to open drilled report once clicked from entity table

	$scope.entityDrilledDown = function(entityName, status) {
		// console.log('Entity Name : ' + entityName);
		//console.log('status : ' + status);
		// console.log('status : ' +$scope.EntityChartData.status);
		$scope.drilledReport({'chart_name':'entityLevel','status':status,'entity':entityName,'unit':0,'department':0});
	}	

	/*Locaion/UNIT wise table clicked Start*/

	$scope.unitChartComplied = function(unit, status) {
		$scope.drilledReport({'chart_name':'unitLevel','status':status,'entity':0,'unit':unit,'department':0});
	}

	$scope.unitChartPosingRisk = function(unit, status) {
		// // console.log('unit name : ' + unit);
		// console.log('unit status : ' + status);
		$scope.drilledReport({'chart_name':'unitLevel','status':status,'entity':0,'unit':unit,'department':0});
	}

	$scope.unitChartNonComplied = function(unit, status) {
		$scope.drilledReport({'chart_name':'unitLevel','status':status,'entity':0,'unit':unit,'department':0});
	}

	$scope.unitChartDelayed = function(unit, status) {
		$scope.drilledReport({'chart_name':'unitLevel','status':status,'entity':0,'unit':unit,'department':0});
	}

	$scope.unitChartDelayedReported = function(unit, status) {
		$scope.drilledReport({'chart_name':'unitLevel','status':status,'entity':0,'unit':unit,'department':0});
	}

	$scope.unitChartWaitingForApproval = function(unit, status) {
		$scope.drilledReport({'chart_name':'unitLevel','status':status,'entity':0,'unit':unit,'department':0});
	}

	$scope.unitChartReOpened = function(unit, status) {
		$scope.drilledReport({'chart_name':'unitLevel','status':status,'entity':0,'unit':unit,'department':0});
	}
	/*Locaion/UNIT wise table clicked End*/

	/*	Function wise drilled down report on table clicked START*/
	$scope.functionDrilledDown = function(functions, status) {
		$scope.drilledReport({'chart_name':'departmentLevel','status':status,'entity':0,'unit':0,'department':functions});
	}
	/*	Function wise drilled down report on table clicked END*/


	$('#entityPosingRiskClick').click(function() {

		$('#entityLevel').bind('dataPointMouseUp', function (e, data) {
			$scope.drilledReport({'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0});
		});
	});

	$('#entityNonCompliedClick').click(function() {
		$('#entityLevel').bind('dataPointMouseUp', function (e, data) {
			$scope.drilledReport({'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0});
		});
	});

	$('#entityDelayedClick').click(function() {
		$('#entityLevel').bind('dataPointMouseUp', function (e, data) {
			$scope.drilledReport({'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0});
		});
	});

	$('#entityWaitingForApprovalClick').click(function() {
		$('#entityLevel').bind('dataPointMouseUp', function (e, data) {
			$scope.drilledReport({'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0});
		});
	});


	$('#entityReOpenedClick').click(function() {
		$('#entityLevel').bind('dataPointMouseUp', function (e, data) {
			$scope.drilledReport({'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0});
		});
	});



	/**
	 * End code here
	 */



	$scope.searchObj={};
	$scope.chart_name="";
//	Click Evevent & Dril-Down For Pie Chart
	$('#jqChart1').bind('dataPointMouseUp', function (e, data) {
		//// console.log('drilledDown report jqChart1 fun() : ');
		// console.log('data.dataItem  : ' + data.dataItem[0])
		$scope.status = data.dataItem[0];
		if(data.dataItem[0] === "Complied"){	
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "Complied";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			// {'chart_name':'overall','status':'Complied','entity':"",'unit':"",'department':""}
			$scope.drilledReport($scope.searchObj);
			// $state.go('DrilledReport',$scope.searchObj);
			// // console.log("name "+chart_name);
		}else if(data.dataItem[0] === "Posing"){
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "PosingRisk";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			// $state.go('DrilledReport',{'chart_name':'overall','status':'PosingRisk','entity':0,'unit':0,'department':0});
			$scope.drilledReport($scope.searchObj);
		}else if(data.dataItem[0] === "Non-Complied"){
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "NonComplied";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'NonComplied','entity':"",'unit':"",'department':""});
		}else if(data.dataItem[0] === "Waiting For Approval"){
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "Waiting For Approval";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'Waiting For Approval','entity':0,'unit':0,'department':0});
		}else if(data.dataItem[0] === "Re-Opened"){
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "Re-Opened";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'Re-Opened','entity':0,'unit':0,'department':0});
		}else if(data.dataItem[0] === "Delayed"){
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "Delayed";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'Delayed','entity':0,'unit':0,'department':0});
		}else{
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "DelayedReported";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'Delayed','entity':0,'unit':0,'department':0});
		}
	});

	//internal compliance graph dilled report
	$('#jqChart2').bind('dataPointMouseUp', function (e, data) {
		if(data.dataItem[0] === "Complied"){		
			$scope.searchObj.chart_name = "internal";
			$scope.searchObj.status = "Complied";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			//{'chart_name':'overall','status':'Complied','entity':"",'unit':"",'department':""}
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',$scope.searchObj);
			//// console.log("name "+chart_name);
		}else if(data.dataItem[0] === "Posing"){
			$scope.searchObj.chart_name = "internal";
			$scope.searchObj.status = "PosingRisk";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			//$state.go('DrilledReport',{'chart_name':'overall','status':'PosingRisk','entity':0,'unit':0,'department':0});
			$scope.drilledReport($scope.searchObj);
		}else if(data.dataItem[0] === "Non-Complied"){
			$scope.searchObj.chart_name = "internal";
			$scope.searchObj.status = "NonComplied";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'NonComplied','entity':"",'unit':"",'department':""});
		}else if(data.dataItem[0] === "Waiting For Approval"){
			$scope.searchObj.chart_name = "internal";
			$scope.searchObj.status = "Waiting For Approval";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'Waiting For Approval','entity':0,'unit':0,'department':0});
		}else if(data.dataItem[0] === "Re-Opened"){
			$scope.searchObj.chart_name = "internal";
			$scope.searchObj.status = "Re-Opened";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'Re-Opened','entity':0,'unit':0,'department':0});
		}else if(data.dataItem[0] === "Delayed-Reported"){ 
			$scope.searchObj.chart_name = "internal";
			$scope.searchObj.status = "Delayed-Reported";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
		}else {
			$scope.searchObj.chart_name = "internal";
			$scope.searchObj.status = "Delayed";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
			//$state.go('DrilledReport',{'chart_name':'overall','status':'Delayed','entity':0,'unit':0,'department':0});
		}
	});



	// Click Evevent & Dril-Down For Department Chart
	$('#DepartmentBarChart').bind('dataPointMouseUp', function (e, data) {
		//$state.go('DrilledReport',{'chart_name':'departmentLevel','status':data.series.title,'entity':0,'unit':0,'department':data.x});
//		// console.log("e "+e);   
//		// console.log("data "+data.x);
//		// console.log("status "+data.series.title);
		$scope.drilledReport({'chart_name':'departmentLevel','status':data.series.title,'entity':0,'unit':0,'department':data.x});
	});

//	Click Evevent & Dril-Down For Location Chart
	$('#LocationBarChart').bind('dataPointMouseUp', function (e, data) {
		//$state.go('DrilledReport',{'chart_name':'unitLevel','status':data.series.title,'entity':0,'unit':data.x,'department':0});
//		console.log("e "+e);   
//		console.log("data "+data.x);
		$scope.drilledReport({'chart_name':'unitLevel','status':data.series.title,'entity':0,'unit':data.x,'department':0});
	});

//	Click Evevent & Dril-Down For Entity Chart
	// $('#entityLevel').bind('dataPointMouseUp', function (e, data) {
	//$state.go('DrilledReport',{'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0});
//	console.log("e "+e);   
//	console.log("data "+data.x);
//	// console.log("status is "+data.series.title);
	// $scope.drilledReport({'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0});
	// });

	$('#financeLevel').bind('dataPointMouseUp', function (e, data) {
		// console.log('financeLevel dataPointMouseUp ');
		// console.log("e "+e);   
		// console.log("data "+data.x);
		// console.log("status is "+data.series.title);
		$scope.drilledReport({'chart_name':'financeLevel','status':data.series.title,'month':data.x,'unit':0,'department':0});
	});

	/**
	 * on Load function load quarter Graph
	 */

	$scope.loadQuarterGraphOnLoad = function() {
		/**
		 * Quarter wise data
		 */

		$scope.entityQuarterGraph                    = {};
		$scope.entityQuarterGraph.Month              = [];
		$scope.entityQuarterGraph.Quarter            = [];
		$scope.entityQuarterGraph.Complied           = [];
		$scope.entityQuarterGraph.NonComplied        = [];
		$scope.entityQuarterGraph.PosingRisk         = [];
		$scope.entityQuarterGraph.Delayed            = [];
		$scope.entityQuarterGraph.WaitingForApproval = [];
		$scope.entityQuarterGraph.ReOpened           = [];

		var month = new Array();
		month[0] = "January";
		month[1] = "February";
		month[2] = "March";
		month[3] = "April";
		month[4] = "May";
		month[5] = "June";
		month[6] = "July";
		month[7] = "August";
		month[8] = "September";
		month[9] = "October";
		month[10] = "November";
		month[11] = "December";


		/**
		 * End Quarter To Month
		 */


		if ($scope.dashboardObj.fquarter == 0) {
			$scope.entityQuarterGraph.Quarter.push("Quarter 1");

			month[3] = "April";
			month[4] = "May";
			month[5] = "June";

			$scope.entityQuarterGraph.Quarter.push("Quarter 2");

			month[6] = "July";
			month[7] = "August";
			month[8] = "September";

			$scope.entityQuarterGraph.Quarter.push("Quarter 3");

			month[9] = "October";
			month[10] = "November";
			month[11] = "December";

			$scope.entityQuarterGraph.Quarter.push("Quarter 4");

			month[0] = "January";
			month[1] = "February";
			month[2] = "March";
		}

		angular.forEach($scope.entityQuarterGraph.Quarter, function (value, key) {
			//alert(value.length);
			/*Financial graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			var fDelayed_Reported = 0;
			var quarter = "";
			/*Financial graph */

			// var curr_date = new Date();

			// var curr_year = curr_date.getFullYear();
			if(value === "Quarter 1"){
				quarter = ["April","May","June"];
			}else if(value === "Quarter 2"){
				quarter = ["July","August","September"];
			}else if(value === "Quarter 3"){
				quarter = ["October","November","December"];
			}else if(value === "Quarter 4"){
				quarter = ["January","February","March"];
			}

			var next_year = curr_year + 1;

			angular.forEach($scope.taskList, function (data) {
				var legal_Date = data.ttrn_legal_due_date.split("-");
				// console.log('Quarter legal_Date : ' + legal_Date);
				var curDate = new Date();
				//console.log('curDate : ' + curDate);
				//console.log('lglDate : ' + curDate.getFullYear());
				var legalDate = curDate.getFullYear();
				var curMonth = curDate.getMonth();

				var date_month = parseInt(legal_Date[1]) - 1;
				var a = date_month - 1;

				var date_lag = new Date(legal_Date[2], legal_Date[1] - 1, legal_Date[0]);
				// console.log(date_lag);

//				if ((legalDate >= curr_year && legalDate <= next_year) && ((legalDate == curr_year && date_lag.getMonth() >= 3) || (legalDate == next_year && date_lag.getMonth() <= 3))) {
				if((legal_Date[2]>= curr_year  && legal_Date[2] <= next_year) && ((legal_Date[2]== curr_year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3)) ){
					for(var i = 0;i<quarter.length;i++){					
						if (month[date_lag.getMonth()] === quarter[i]) {
							if (data.status == 'complied') {
								fcomplied++;
							}

							if (data.status == 'noncomplied') {
								fnoncomplied++;
							}

							if (data.status == 'posingrisk') {
								fposingrisk++;
							}

							if (data.status == 'delayed') {
								fdelayed++;
							}

							if (data.status == 'waitingforapproval') {
								fpartially_Completed++;
							}
							if (data.status == 'reopen') {
								fre_opened++;
							}
							if (data.status == 'delayed-reported') {
								fDelayed_Reported++;
							}
						}
					}

				}

			});
			//alert(fcomplied);

			$scope.entityQuarterGraph.Complied.push(fcomplied);
			$scope.entityQuarterGraph.NonComplied.push(fnoncomplied);
			$scope.entityQuarterGraph.PosingRisk.push(fposingrisk);
			$scope.entityQuarterGraph.Delayed.push(fdelayed);
			$scope.entityQuarterGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.entityQuarterGraph.ReOpened.push(fre_opened);

		});
		$('#quarterWiseLoaders').show();
		$scope.loadEntityQuarterWiseBarGraph($scope.entityQuarterGraph);

		/**
		 * End
		 */
	}
	/**
	 * End
	 */

	/**
	 * On Search Function Load Graph
	 */
	$scope.onSearchGraph = function() {

		var orga_list = [];
		var orga_name_list = [];
		var loca_list = [];
		var loca_name_list = [];
		var dept_list = [];

		$scope.quarterToMonthGraph                    = {};
		$scope.quarterToMonthGraph.Month              = [];
		$scope.quarterToMonthGraph.Complied           = [];
		$scope.quarterToMonthGraph.NonComplied        = [];
		$scope.quarterToMonthGraph.PosingRisk         = [];
		$scope.quarterToMonthGraph.Delayed            = [];
		$scope.quarterToMonthGraph.WaitingForApproval = [];
		$scope.quarterToMonthGraph.ReOpened           = [];

		var month = new Array();
		month[0] = "January";
		month[1] = "February";
		month[2] = "March";
		month[3] = "April";
		month[4] = "May";
		month[5] = "June";
		month[6] = "July";
		month[7] = "August";
		month[8] = "September";
		month[9] = "October";
		month[10] = "November";
		month[11] = "December";

		$scope.quarterToMonthGraph.Month.push("April");
		$scope.quarterToMonthGraph.Month.push("May");
		$scope.quarterToMonthGraph.Month.push("June");
		$scope.quarterToMonthGraph.Month.push("July");
		$scope.quarterToMonthGraph.Month.push("August");
		$scope.quarterToMonthGraph.Month.push("September");
		$scope.quarterToMonthGraph.Month.push("October");
		$scope.quarterToMonthGraph.Month.push("November");
		$scope.quarterToMonthGraph.Month.push("December");
		$scope.quarterToMonthGraph.Month.push("January");
		$scope.quarterToMonthGraph.Month.push("February");
		$scope.quarterToMonthGraph.Month.push("March");

		angular.forEach($scope.quarterToMonthGraph.Month,function(value, key){

			/* Quarter To Month graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			/* Quarter To Month graph */

			// var curr_year = curr_date.getFullYear();
			var next_year = curr_year + 1;

			angular.forEach($scope.taskList, function (data) { 
				var legal_Date = data.ttrn_legal_due_date.split("-");
				// console.log('legal_Date : ' + legal_Date);
				var date_month = parseInt(legal_Date[1])-1;
				var a = date_month-1;

				var date_lag = new Date(legal_Date[2],legal_Date[1]-1,legal_Date[0]);
				// console.log(date_lag);

				if((legal_Date[2]>= curr_year  && legal_Date[2] <= next_year) && ((legal_Date[2]== curr_year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3)) ){
					if(month[date_lag.getMonth()] === value){
						if(data.status=='complied'){
							fcomplied++;
						}

						if(data.status=='noncomplied'){
							fnoncomplied++;
						}

						if(data.status=='posingrisk'){
							fposingrisk++;
						}

						if(data.status=='delayed'){
							fdelayed++;
						}

						if(data.status=='waitingforapproval'){
							fpartially_Completed++;
						}
						if(data.status=='reopen'){
							fre_opened++;
						}
					}
				}
			});
			$scope.quarterToMonthGraph.Complied.push(fcomplied);
			$scope.quarterToMonthGraph.NonComplied.push(fnoncomplied);
			$scope.quarterToMonthGraph.PosingRisk.push(fposingrisk);
			$scope.quarterToMonthGraph.Delayed.push(fdelayed);
			$scope.quarterToMonthGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.quarterToMonthGraph.ReOpened.push(fre_opened); 
		});
		$scope.quarterToMonthlyGraph($scope.quarterToMonthGraph);
	}


	$scope.financeMonthGraphLoadFun = function() {

		var orga_list = [];
		var orga_name_list = [];
		var loca_list = [];
		var loca_name_list = [];
		var dept_list = [];

		$scope.quarterToMonthGraph                    = {};
		$scope.quarterToMonthGraph.Month              = [];
		$scope.quarterToMonthGraph.Complied           = [];
		$scope.quarterToMonthGraph.NonComplied        = [];
		$scope.quarterToMonthGraph.PosingRisk         = [];
		$scope.quarterToMonthGraph.Delayed            = [];
		$scope.quarterToMonthGraph.WaitingForApproval = [];
		$scope.quarterToMonthGraph.ReOpened           = [];

		var month = new Array();
		month[0] = "January";
		month[1] = "February";
		month[2] = "March";
		month[3] = "April";
		month[4] = "May";
		month[5] = "June";
		month[6] = "July";
		month[7] = "August";
		month[8] = "September";
		month[9] = "October";
		month[10] = "November";
		month[11] = "December";

		$scope.quarterToMonthGraph.Month.push("April");
		$scope.quarterToMonthGraph.Month.push("May");
		$scope.quarterToMonthGraph.Month.push("June");
		$scope.quarterToMonthGraph.Month.push("July");
		$scope.quarterToMonthGraph.Month.push("August");
		$scope.quarterToMonthGraph.Month.push("September");
		$scope.quarterToMonthGraph.Month.push("October");
		$scope.quarterToMonthGraph.Month.push("November");
		$scope.quarterToMonthGraph.Month.push("December");
		$scope.quarterToMonthGraph.Month.push("January");
		$scope.quarterToMonthGraph.Month.push("February");
		$scope.quarterToMonthGraph.Month.push("March");


		angular.forEach($scope.quarterToMonthGraph.Month,function(value, key){

			/* Quarter To Month graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			/* Quarter To Month graph */

			// var curr_year = curr_date.getFullYear();
			var next_year = curr_year + 1;

			angular.forEach($scope.taskList, function (data) { 
				var legal_Date = data.ttrn_legal_due_date.split("-");
				// console.log('legal_Date : ' + legal_Date);
				var date_month = parseInt(legal_Date[1])-1;
				var a = date_month-1;

				var date_lag = new Date(legal_Date[2],legal_Date[1]-1,legal_Date[0]);
				// console.log(date_lag);
				// console.log('orga id : ' + $scope.dashboardObj.organisationId);
				if($scope.dashboardObj.organisationId == data.orga_id){
					if((legal_Date[2]>= curr_year  && legal_Date[2] <= next_year) && ((legal_Date[2]== curr_year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3)) ) {
						if(month[date_lag.getMonth()] === value){
							if(data.status=='complied'){
								fcomplied++;
							}

							if(data.status=='noncomplied'){
								fnoncomplied++;
							}

							if(data.status=='posingrisk'){
								fposingrisk++;
							}

							if(data.status=='delayed'){
								fdelayed++;
							}

							if(data.status=='waitingforapproval'){
								fpartially_Completed++;
							}
							if(data.status=='reopen'){
								fre_opened++;
							}
						}
					}
				}
			});
			$scope.quarterToMonthGraph.Complied.push(fcomplied);
			$scope.quarterToMonthGraph.NonComplied.push(fnoncomplied);
			$scope.quarterToMonthGraph.PosingRisk.push(fposingrisk);
			$scope.quarterToMonthGraph.Delayed.push(fdelayed);
			$scope.quarterToMonthGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.quarterToMonthGraph.ReOpened.push(fre_opened); 
		});
		$scope.loadFinanceGraph($scope.quarterToMonthGraph);

	}

	$scope.loadFinanceMonthGraphFun = function(data) {
		// console.log('loadFinanceMonthGraphFun meth() called  ');
		$scope.FinanceChartData = data;
		var background = {
				type : 'linearGradient',
				x0 : 0,
				y0 : 0,
				x1 : 0,
				y1 : 1,
				colorStops : [{
					offset : 0,
					color : '#d2e6c9'
				}, {
					offset : 1,
					color : 'white'
				}]
		};

		$scope.chart_widht = 750;
		$scope.chart_height = 350;
		$scope.axisLocation='bottom';
		$scope.graphBarType='stacked100Column';
		$scope.labelLocation='left';

		if($scope.FinanceChartData.Month.length > 12) {
			$scope.chart_widht = 1150;
			$scope.chart_height = 850;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}

		$('#financeLevel').jqChart({
			orientation: 'horizontal',
			width: $scope.chart_widht,
			height: $scope.chart_height,
			legend : {
				customItems : [ {
					text : {
						text : 'Complied'
					},
					marker : {
						fillStyle : '#03C523 ',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Posing'
					},
					marker : {
						fillStyle : '#fafaa4f0',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Non-Complied'
					},
					marker : {
						fillStyle : '#FF0000',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#BFBFBF',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed-Reported'
					},
					marker : {
						fillStyle : '#bfd630',
						type : 'circle'
					}
				}, /*{
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#85c1e9',
						type : 'circle'
					}
				},*/ {
					text : {
						text : 'Re-Opened'
					},
					marker : {
						fillStyle : '#f5b041',
						type : 'circle'
					}
				} ]
			},
			border : {
				strokeStyle : '#6ba851'
			},
			background : background,
			animation : {
				duration : 1
			},
			shadows : {
				enabled : true
			},
			axes : [ {
				type : 'category',
				location : $scope.axisLocation,
				categories : $scope.FinanceChartData.Month, 
			}, {
				type : 'linearGradient',
				location : $scope.labelLocation,
				minimum : 0,
				maximum : 100,
				interval : 10,
				labels : {
					stringFormat : '%.1f%%'
				}
			} ],
			series : [ {
				type : $scope.graphBarType,
				title : 'Complied',
				fillStyles : [ '#03C523 ' ],
				data : $scope.FinanceChartData.Complied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'PosingRisk',
				fillStyles : [ '#fafaa4f0' ],
				data : $scope.FinanceChartData.PosingRisk,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'NonComplied',
				fillStyles : [ '#FF0000' ],
				data :  $scope.FinanceChartData.NonComplied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed',
				fillStyles : [ '#BFBFBF' ],
				data : $scope.FinanceChartData.Delayed,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, /*{
				type : $scope.graphBarType,
				title : 'Waiting For Approval',
				fillStyles : [ '#85c1e9' ],
				data : $scope.FinanceChartData.WaitingForApproval,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},*/ {
				type : $scope.graphBarType,
				title : 'Re-Opened',
				fillStyles : [ '#f5b041' ],
				data : $scope.FinanceChartData.ReOpened,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			} ,{
				type : $scope.graphBarType,
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.FinanceChartData.Delayed_Reported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}]
		});
	}

	$('#myDivFor').bind('dataPointMouseUp', function (e, data) {
		// alert('hiiiiiiiiiii');
		// alert('e data : ' + e);
		// alert('data : ' + data.y);
	});

	$scope.myDivGrpah = function () {
		$('#myDivFor').jqChart({
			title: "Click Event Testing",
			legend: { visible: false },
			animation: { duration: 1 },
			series: [
				{
					type: 'column',
					title: 'Column',
					data: [
						['A', 46], ['B', 35], ['C', 68], ['D', 30],
						['E', 27], ['F', 85], ['D', 43], ['H', 29]
						],
						cursor: 'pointer'
				}
				]
		});
	}

	//	On page load
	$scope.loadOtherGraphs = function() {
		// $scope.myDivGrpah();
		var orga_list = [];
		var orga_name_list = [];
		var loca_list = [];
		var loca_name_list = [];
		var dept_list = [];

		/* Overall Graph*/
		$scope.overAllGraph 					= {};
		$scope.overAllGraph.Complied 			= [];
		$scope.overAllGraph.NonComplied 		= [];
		$scope.overAllGraph.PosingRisk 			= [];
		$scope.overAllGraph.Delayed 			= [];
		$scope.overAllGraph.WaitingForApproval 	= [];
		$scope.overAllGraph.ReOpened 			= [];
		$scope.overAllGraph.Delayed_Reported = [];

		/*End*/

		$scope.internalGraph 					= {};
		$scope.internalGraph.Complied 			= [];
		$scope.internalGraph.NonComplied 		= [];
		$scope.internalGraph.PosingRisk 		= [];
		$scope.internalGraph.Delayed 			= [];
		$scope.internalGraph.WaitingForApproval = [];
		$scope.internalGraph.ReOpened 			= [];
		//Updated from below
		$scope.internalGraph.Delayed_Reported	= [];

		$scope.entityGraph 						= {};
		$scope.entityGraph.Entity 				= [];
		$scope.entityGraph.Complied 			= [];
		$scope.entityGraph.NonComplied 			= [];
		$scope.entityGraph.PosingRisk 			= [];
		$scope.entityGraph.Delayed 				= [];
		$scope.entityGraph.WaitingForApproval 	= [];
		$scope.entityGraph.ReOpened 			= [];
		$scope.entityGraph.Delayed_Reported = [];

		$scope.entityPieChartrGraph 					= {};
		$scope.entityPieChartrGraph.Entity 				= [];
		$scope.entityPieChartrGraph.Complied 			= [];
		$scope.entityPieChartrGraph.NonComplied 		= [];
		$scope.entityPieChartrGraph.PosingRisk 			= [];
		$scope.entityPieChartrGraph.Delayed 			= [];
		$scope.entityPieChartrGraph.WaitingForApproval 	= [];
		$scope.entityPieChartrGraph.ReOpened 			= [];

		$scope.financialGraph                    = {};
		$scope.financialGraph.Month              = [];
		$scope.financialGraph.Complied           = [];
		$scope.financialGraph.NonComplied        = [];
		$scope.financialGraph.PosingRisk         = [];
		$scope.financialGraph.Delayed            = [];
		$scope.financialGraph.WaitingForApproval = [];
		$scope.financialGraph.ReOpened           = [];
		$scope.financialGraph.Delayed_Reported 	 = [];

		$scope.unitGraph                        = {};
		$scope.unitGraph.Unit                   = [];
		$scope.unitGraph.Complied 			    = [];
		$scope.unitGraph.NonComplied 		    = [];
		$scope.unitGraph.PosingRisk 			= [];
		$scope.unitGraph.Delayed 			    = [];
		$scope.unitGraph.WaitingForApproval 	= [];
		$scope.unitGraph.ReOpened 			    = [];
		$scope.unitGraph.Delayed_Reported 		= [];

		$scope.functionGraph                        = {};
		$scope.functionGraph.Function               = [];
		$scope.functionGraph.Complied 			    = [];
		$scope.functionGraph.NonComplied 		    = [];
		$scope.functionGraph.PosingRisk 			= [];
		$scope.functionGraph.Delayed 			    = [];
		$scope.functionGraph.WaitingForApproval 	= [];
		$scope.functionGraph.ReOpened 			    = [];
		$scope.functionGraph.Delayed_Reported 		= [];

		$scope.categoryWiseGraph = {};
		$scope.categoryWiseGraph.Complied = [];
		$scope.categoryWiseGraph.NonComplied = [];
		$scope.categoryWiseGraph.PosingRisk = [];
		$scope.categoryWiseGraph.Delayed = [];
		$scope.categoryWiseGraph.WaitingForApproval = [];
		$scope.categoryWiseGraph.ReOpened = [];
		$scope.categoryWiseGraph.Delayed_Reported = [];

		$scope.SubUnitGraph                        = {};
		$scope.SubUnitGraph.Unit                   = [];
		$scope.SubUnitGraph.Complied 			   = [];
		$scope.SubUnitGraph.NonComplied 		   = [];
		$scope.SubUnitGraph.PosingRisk 			   = [];
		$scope.SubUnitGraph.Delayed 			   = [];
		$scope.SubUnitGraph.WaitingForApproval 	   = [];
		$scope.SubUnitGraph.ReOpened 			   = [];
		$scope.SubUnitGraph.Delayed_Reported 	   = [];


		var month = new Array();
		month[0] = "January";
		month[1] = "February";
		month[2] = "March";
		month[3] = "April";
		month[4] = "May";
		month[5] = "June";
		month[6] = "July";
		month[7] = "August";
		month[8] = "September";
		month[9] = "October";
		month[10] = "November";
		month[11] = "December";

		$scope.financialGraph.Month.push("April");
		$scope.financialGraph.Month.push("May");
		$scope.financialGraph.Month.push("June");
		$scope.financialGraph.Month.push("July");
		$scope.financialGraph.Month.push("August");
		$scope.financialGraph.Month.push("September");
		$scope.financialGraph.Month.push("October");
		$scope.financialGraph.Month.push("November");
		$scope.financialGraph.Month.push("December");
		$scope.financialGraph.Month.push("January");
		$scope.financialGraph.Month.push("February");
		$scope.financialGraph.Month.push("March");

		angular.forEach($scope.financialGraph.Month,function(value, key){

			/*Financial graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			var fDelayed_Reported = 0;
			/*Financial graph */

			// var curr_date = new Date();

			// var curr_year = curr_date.getFullYear();
			var next_year = curr_year + 1;

			angular.forEach($scope.taskList, function (data) { 
				var legal_Date = data.ttrn_legal_due_date.split("-");
				// console.log('legal_Date : ' + legal_Date);
				var date_month = parseInt(legal_Date[1])-1;
				var a = date_month-1;

				var date_lag = new Date(legal_Date[2],legal_Date[1]-1,legal_Date[0]);
				// console.log(date_lag);

				if((legal_Date[2]>= curr_year  && legal_Date[2] <= next_year) && ((legal_Date[2]== curr_year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3)) ){
					if(month[date_lag.getMonth()] === value){
						if(data.status=='complied'){
							fcomplied++;
						}

						if(data.status=='noncomplied'){
							fnoncomplied++;
						}

						if(data.status=='posingrisk'){
							fposingrisk++;
						}

						if(data.status=='delayed'){
							fdelayed++;
						}

						if(data.status=='waitingforapproval'){
							fpartially_Completed++;
						}
						if(data.status=='reopen'){
							fre_opened++;
						}
						if (data.status == 'delayed-reported') {
							fDelayed_Reported++;
						}
					}
				}
			});

			$scope.financialGraph.Complied.push(fcomplied);
			$scope.financialGraph.NonComplied.push(fnoncomplied);
			$scope.financialGraph.PosingRisk.push(fposingrisk);
			$scope.financialGraph.Delayed.push(fdelayed);
			$scope.financialGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.financialGraph.ReOpened.push(fre_opened); 
			$scope.financialGraph.Delayed_Reported.push(fDelayed_Reported);
		} );
		$scope.loadFinanceGraph($scope.financialGraph);
		/*$scope.loadFinanceGraphByQuarter($scope.financialGraph);*/
		$scope.financeLevelGraphFun($scope.financialGraph);

		angular.forEach($scope.taskList, function (data) { 
			var id_orga = data.orga_id;
			var id_loca = data.loca_id;
			var id_dept = data.dept_id;
			var exec_Date = data.date;

			// var legal_Date = data.ttrn_legal_due_date.split("-");
			// var legalDueDate = new Date(legal_Date);
			// var monthFincance = month[legalDueDate.getMonth()];
			//console.log("Legal due date is :"+executor_date);
			//console.log(" month is :"+month[monthFincance.getMonth()]);


			if(orga_list.indexOf(id_orga) === -1){
				orga_list.push(id_orga);
				$scope.entityGraph.Entity.push(data.orga_name); // Add organization Name
				// console.log('data.orga_name : ' + data.orga_name);
				$scope.entityPieChartrGraph.Entity.push(data.orga_name);
			}


			if(loca_list.indexOf(id_loca) === -1){
				loca_list.push(id_loca);
				$scope.unitGraph.Unit.push(data.loca_name);

				//loca type : 1 - East, 2 - West 3, - South, 4 - North
				if(loca_type.indexOf(data.loca_type) === -1 && data.loca_type == 4){
					loca_type.push(data.loca_type);
					var obj ={
							id : data.loca_id,
							type : data.loca_type,
							name : "North"
					}
					subUnitArray.push(obj);

				}else if(loca_type.indexOf(data.loca_type) ===-1 && data.loca_type==3 ){
					loca_type.push(data.loca_type);
					var obj ={
							id : data.loca_id,
							type : data.loca_type,
							name : "South"
					}
					subUnitArray.push(obj);

				}else if(loca_type.indexOf(data.loca_type) ===-1 && data.loca_type==1){
					loca_type.push(data.loca_type);
					var obj ={
							id : data.loca_id,
							type : data.loca_type,
							name : "East"
					}
					subUnitArray.push(obj);

				}else if(loca_type.indexOf(data.loca_type) ===-1 && data.loca_type==2){
					loca_type.push(data.loca_type);
					var obj ={
							id : data.loca_id,
							type : data.loca_type,
							name : "West"
					}
					subUnitArray.push(obj);

				}

			}

			if(dept_list.indexOf(id_dept) === -1){
				dept_list.push(id_dept);
				$scope.functionGraph.Function.push(data.dept_name);
			}
		});//End task list foreach

		/*OverAll */
		var ocomplied = 0;
		var ononcomplied = 0;
		var oposingrisk = 0;
		var odelayed = 0;
		var opending = 0;
		var opartially_Completed = 0;
		var ore_opened = 0;
		var odelayed_reported = 0;
		/*OverAll */

		///Internal
		var incomplied = 0;
		var innoncomplied = 0;
		var inposingrisk = 0;
		var indelayed = 0;
		var inpending = 0;
		var inpartially_Completed = 0;
		var inre_opened = 0;
		var indelayed_reported = 0;

		//OverAll and Entity Wise graph Count
		for(var i = 0; i< orga_list.length; i++){    

			/*Entity */
			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0; 

			var compliedPie = 0;
			var noncompliedPie = 0;
			var posingriskPie = 0;
			var delayedPie = 0;
			var pendingPie = 0;
			var partially_CompletedPie = 0;
			var re_openedPie = 0;
			var delayed_reportedPie = 0;

			/*Entity */

			angular.forEach($scope.taskList, function (data) { 
				//alert(data.status);
				if(orga_list[i]==data.orga_id){ 	//	Orga Id matched
					if(data.status=='complied'){
						complied++; 
						compliedPie++; 
						ocomplied++;
					}

					if(data.status=='noncomplied'){
						noncomplied++;
						noncompliedPie++;
						ononcomplied++;
					}

					if(data.status=='posingrisk'){
						posingrisk++;
						posingriskPie++;
						oposingrisk++;
					}

					if(data.status=='delayed'){
						delayed++;
						delayedPie++;
						odelayed++;
					}

					if(data.status=='waitingforapproval'){
						partially_Completed++;
						partially_CompletedPie++;
						opartially_Completed++;
					}
					if(data.status=='reopen'){

						re_opened++;
						re_openedPie++;
						ore_opened++;
						//alert("Re-Opened : "+re_openedPie);
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
						odelayed_reported++;
						delayed_reportedPie++;
					}
				}//End IF Orga Id matched


				//Internal count 
				if(orga_list[i]==data.orga_id){//Orga Id matched
					if(data.status=='complied' && data.task_cat_law =="Internal"){
						incomplied++; 
					}

					if(data.status=='noncomplied' && data.task_cat_law =="Internal"){
						innoncomplied++;
					}

					if(data.status=='posingrisk' && data.task_cat_law =="Internal"){
						inposingrisk++;
					}

					if(data.status=='delayed' && data.task_cat_law =="Internal"){
						indelayed++;
					}

					if(data.status=='waitingforapproval' && data.task_cat_law =="Internal"){
						inpartially_Completed++;
					}
					if(data.status=='reopen' && data.task_cat_law =="Internal"){
						inre_opened++;
					}
					if(data.status=='delayed_reported' && data.task_cat_law =="Internal"){
						indelayed_reported++;
					}
				}
			});

			$scope.entityGraph.Complied.push(complied);
			$scope.entityGraph.NonComplied.push(noncomplied);
			$scope.entityGraph.PosingRisk.push(posingrisk);
			$scope.entityGraph.Delayed.push(delayed);
			$scope.entityGraph.WaitingForApproval.push(partially_Completed);
			$scope.entityGraph.ReOpened.push(re_opened);
			$scope.entityGraph.Delayed_Reported.push(delayed_reported);

			$scope.entityPieChartrGraph.Complied.push(compliedPie);
			$scope.entityPieChartrGraph.NonComplied.push(noncompliedPie);
			$scope.entityPieChartrGraph.PosingRisk.push(posingriskPie);
			$scope.entityPieChartrGraph.Delayed.push(delayedPie);
			$scope.entityPieChartrGraph.WaitingForApproval.push(partially_CompletedPie);
			$scope.entityPieChartrGraph.ReOpened.push(re_openedPie);
			//$scope.entityPieChartrGraph.Delayed_Reported.push(delayed_reportedPie);


			$scope.internalGraph.Complied.push(incomplied);
			$scope.internalGraph.NonComplied.push(innoncomplied);
			$scope.internalGraph.PosingRisk.push(inposingrisk);
			$scope.internalGraph.Delayed.push(indelayed);
			$scope.internalGraph.WaitingForApproval.push(inpartially_Completed);
			$scope.internalGraph.ReOpened.push(inre_opened);
			$scope.internalGraph.Delayed_Reported.push(indelayed_reported);

		}//End OverAll and Entity Wise graph Count

		//console.log("Entity JSON "+JSON.stringify($scope.entityGraph));

		$scope.overAllGraph.Complied = ocomplied;;
		$scope.overAllGraph.NonComplied = ononcomplied;
		$scope.overAllGraph.PosingRisk = oposingrisk;
		$scope.overAllGraph.Delayed = odelayed;
		$scope.overAllGraph.WaitingForApproval = opartially_Completed;
		$scope.overAllGraph.ReOpened = ore_opened;
		$scope.overAllGraph.Delayed_Reported = odelayed_reported;

		$("#entityChart").show();
		$("#entityPieChart").show();
		$("#entityPieChart1").show();
		$("#entityPieChart2").show();
		$("#entityPieChart3").show();
		$("#entityPieChart4").show();
		$("#entityPieChart").show();
		$scope.loadEntityGraph($scope.entityGraph);//Pass data to entity search function
		$scope.loadInternalGraph($scope.internalGraph);//pass  data internal funtion
		$scope.loadEntityWisePieChartGraph($scope.entityPieChartrGraph);
		$scope.loadCountries($scope.entityPieChartrGraph);
		$scope.loadOverAllGraph($scope.overAllGraph); //pass Searched data overall funtion
		//End Search OverAll and Entity graph

		//Unit Wise graph Count
		for(var i = 0; i< loca_list.length; i++){

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;

			angular.forEach($scope.taskList, function (data) { 


				if(loca_list[i]==data.loca_id){
					if(data.status=='complied'){
						complied++; 
					}

					if(data.status=='noncomplied'){
						noncomplied++;
					}

					if(data.status=='posingrisk'){
						posingrisk++;
					}

					if(data.status=='delayed'){
						delayed++;
					}

					if(data.status=='waitingforapproval'){
						partially_Completed++;
					}
					if(data.status=='reopen'){
						re_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
					}
				}//End IF 
			});

			$scope.unitGraph.Complied.push(complied);
			$scope.unitGraph.NonComplied.push(noncomplied);
			$scope.unitGraph.PosingRisk.push(posingrisk);
			$scope.unitGraph.Delayed.push(delayed);
			$scope.unitGraph.WaitingForApproval.push(partially_Completed);
			$scope.unitGraph.ReOpened.push(re_opened);
			$scope.unitGraph.Delayed_Reported.push(delayed_reported);


		}//End unit Wise graph Count
		$("#locGraph").show();
		$scope.loadUnitGraph($scope.unitGraph);	


		//Sub Unit Wise graph Count

		for(var i in subUnitArray)
		{

			//var id = subUnitArray[i].id;
			var name = subUnitArray[i].name;
			var type = subUnitArray[i].type;

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;

			angular.forEach($scope.taskList, function (data) { 

				if(type==data.loca_type){
					if(data.status=='complied'){
						complied++; 
					}

					if(data.status=='noncomplied'){
						noncomplied++;
					}

					if(data.status=='posingrisk'){
						posingrisk++;
					}

					if(data.status=='delayed'){
						delayed++;
					}

					if(data.status=='waitingforapproval'){
						partially_Completed++;
					}
					if(data.status=='reopen'){
						re_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
					}
				}//End IF 
			});
			$scope.SubUnitGraph.Unit.push(name);
			$scope.SubUnitGraph.Complied.push(complied);
			$scope.SubUnitGraph.NonComplied.push(noncomplied);
			$scope.SubUnitGraph.PosingRisk.push(posingrisk);
			$scope.SubUnitGraph.Delayed.push(delayed);
			$scope.SubUnitGraph.WaitingForApproval.push(partially_Completed);
			$scope.SubUnitGraph.ReOpened.push(re_opened);
			$scope.SubUnitGraph.Delayed_Reported.push(delayed_reported);
		}
		//End sub unit Wise graph Count
		$("#CategoryBarChart").show();
		$scope.loadCategoryGraph($scope.SubUnitGraph);

		//Dept Wise graph Count
		for(var i = 0; i< dept_list.length; i++){    

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;

			angular.forEach($scope.taskList, function (data) { 
				if(dept_list[i]==data.dept_id){
					if(data.status=='complied'){
						complied++; 
					}

					if(data.status=='noncomplied'){
						noncomplied++;
					}

					if(data.status=='posingrisk'){
						posingrisk++;
					}

					if(data.status=='delayed'){
						delayed++;
					}

					if(data.status=='waitingforapproval'){
						partially_Completed++;
					}
					if(data.status=='reopen'){
						re_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
					}
				}//End IF 
			});

			$scope.functionGraph.Complied.push(complied);
			$scope.functionGraph.NonComplied.push(noncomplied);
			$scope.functionGraph.PosingRisk.push(posingrisk);
			$scope.functionGraph.Delayed.push(delayed);
			$scope.functionGraph.WaitingForApproval.push(partially_Completed);
			$scope.functionGraph.ReOpened.push(re_opened);
			$scope.functionGraph.Delayed_Reported.push(delayed_reported);

		}//End unit Wise graph Count
		$("#deptGraph").show();
		$scope.loadFunctionGraph($scope.functionGraph);


	}

	$scope.configObj = {};
	$scope.configObj.dataRequiredFor = "taskEnabling";
//	getEntityUnitFunctionDesignationList
	$scope.getEntityUnitFunctionDesignationList=function(){
		//var obj={}
		ApiCallFactory.getTaskList($scope.configObj).success(function(res,status){
			if(res.errorMessage!="Failed"){

				$scope.entityListt  = res.assignDropDowns[0].Entity;
				// console.log('$scope.entityListt getTaskList : ' + $scope.entityListt[0] + ' \t 2 : ' + $scope.entityListt[1]);
				$scope.UnitList     = res.assignDropDowns[0].Unit;
				// $scope.FunctionList = res.assignDropDowns[0].Function;
			}else{
				console.log("get List=====Failed");
			}
		}).error(function(error){
			console.log("get List====="+error);
		});
	};
	// $scope.getEntityUnitFunctionDesignationList();


	/**
	 * @Date 27-07-2021
	 * @getEntity Method
	 */

	$scope.getEntityList = function() {
		spinnerService.show('html5spinner');		
		$http({
			url : "./getentity",
			method : "get",				
		})
		.then(
				function(result) {
					spinnerService.hide('html5spinner');
//					$scope.entityList = result.data;
					$scope.entityListt = result.data;
					// console.log('entity orga_id Data : ' + JSON.stringify($scope.entityListt));
				},
				function(result) {
					spinnerService.hide('html5spinner');
				});
	}
	$scope.getEntityList();



	/**
	 * @Date 27-03-2021
	 * @getUnit Method by Entity wise
	 */

	$scope.getUnitListByEntity = function(entity) {
		// console.log('entity : ' + entity);
		var entityId = $scope.dashboardObj.FunlevelEntity;
		var unitId = $scope.dashboardObj.FunlevelUnit;
		// console.log('unitId : ' + unitId + '\t entityId : ' + entityId);
		spinnerService.show('html5spinner');
		$http({
			url : "./getunit",
			method : "get",
			params : {
				/*'entity_id' : entity*/
				'entity_id' : entityId
			}
		}).then(function(result) {
			spinnerService.hide('html5spinner');
//			$scope.UnitList = result.data;
			$scope.UnitListDD = result.data;
			$scope.getFunctionListByOrgaId();
			spinnerService.show('html5spinner');
		}, function(result) {
			spinnerService.hide('html5spinner');
		});

	}


	/**
	 * @Date 27-03-2021
	 * @getFuntionByUnitAndEntity wise
	 */

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
//					$scope.functionList.push(item);
					$scope.FunctionList.push(item);
				}
			});

		}, function(result) {
			spinnerService.hide('html5spinner');
		});

	}


	$scope.getUnits = function(){

		$scope.dept_list = [];
		var orga_id = $scope.dashboardObj.FunlevelEntity;

		var loca_list = [];
		// $scope.UnitListDD = [];
		angular.forEach($scope.UnitList, function (unit_data) { 
			if( (!orga_id || unit_data.orga_id === orga_id)){
				//Add Locations
				if(loca_list.indexOf(unit_data.loca_id) === -1){
					loca_list.push(unit_data.loca_id);

					if(orga_id!=null){
						var obj = {
								loca_id : unit_data.loca_id,
								loca_name : unit_data.loca_name
						}
						// $scope.UnitListDD.push(obj);
					}

				}

			}
		});
	}

	// 1160
	/**
	 * Monthly Graph from Quarter to Month
	 */

	$scope.quarterToMonthlyGraph = function(data) {
		$scope.monthlyData = data;
		// console.log('data : ' + $scope.monthlyData);
		// console.log('month : ' + $scope.monthlyData.Month);

		var background = {
				type : 'linearGradient',
				x0 : 0,
				y0 : 0,
				x1 : 0,
				y1 : 1,
				colorStops : [ {
					offset : 0,
					color : '#d2e6c9'
				}, {
					offset : 1,
					color : 'white'
				} ]
		};

		$scope.chart_widht = 1150;
		$scope.chart_height = 350;
		$scope.axisLocation='bottom';
		$scope.graphBarType='stacked100Column';
		$scope.labelLocation='left';

		if($scope.monthlyData.Month.length > 12) {
			$scope.chart_widht = 1150;
			$scope.chart_height = 850;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}

		$('#quarterWiseLevelGraph').jqChart({
			orientation: 'horizontal',
			width: $scope.chart_widht,
			height: $scope.chart_height,
			legend : {
				customItems : [ {
					text : {
						text : 'Complied'
					},
					marker : {
						fillStyle : '#03C523 ',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Posing'
					},
					marker : {
						fillStyle : '#fafaa4f0',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Non-Complied'
					},
					marker : {
						fillStyle : '#FF0000',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#BFBFBF',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed-Reported'
					},
					marker : {
						fillStyle : '#bfd630',
						type : 'circle'
					}
				}  ]
			},
			border : {
				strokeStyle : '#6ba851'
			},
			background : background,
			animation : {
				duration : 1
			},
			shadows : {
				enabled : true
			},
			axes : [ {
				type : 'category',
				location : $scope.axisLocation,
				categories : $scope.monthlyData.Month, 
			}, {
				type : 'linearGradient',
				location : $scope.labelLocation,
				minimum : 0,
				maximum : 100,
				interval : 10,
				labels : {
					stringFormat : '%.1f%%'
				}
			} ],
			series : [ {
				type : $scope.graphBarType,
				title : 'Complied',
				fillStyles : [ '#03C523 ' ],
				data : $scope.monthlyData.Complied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'PosingRisk',
				fillStyles : [ '#fafaa4f0' ],
				data : $scope.monthlyData.PosingRisk,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'NonComplied',
				fillStyles : [ '#FF0000' ],
				data :  $scope.monthlyData.NonComplied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed',
				fillStyles : [ '#BFBFBF' ],
				data : $scope.monthlyData.Delayed,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, /*{
				type : $scope.graphBarType,
				title : 'Waiting For Approval',
				fillStyles : [ '#85c1e9' ],
				data : $scope.monthlyData.WaitingForApproval,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},*/ {
				type : $scope.graphBarType,
				title : 'Re-Opened',
				fillStyles : [ '#f5b041' ],
				data : $scope.monthlyData.ReOpened,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			} ]
		});
	}

	/**
	 * End Code of Monthly Graph
	 */

//	** Start Date Wise Serch Graph
	$scope.searchGraph = function() {

		// console.log('search graph() called ');
		
		$scope.entityListt = [];
		$scope.getEntityList();

		spinnerService.show('html5spinner');
		//	console.log('from Date : ' + $scope.date_from);
		//	console.log('to Date : ' + $scope.date_to);

		$scope.frmSearchDate = $filter('date')($scope.date_from, 'yyyy-MM-dd');
		$scope.toSearchDate = $filter('date')($scope.date_to, 'yyyy-MM-dd');
		// // console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);
		// console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);

		var obj = {
				date_from: $scope.frmSearchDate,
				date_to: $scope.toSearchDate,
				orgaId: $scope.orgaId,
				quarter: $scope.fquarter
		};

		ApiCallFactory.searchComplianceStatusPieChart(obj).success(function(res,status) {
			spinnerService.hide('html5spinner');
			//$scope.getEntityChart();
			if(status === 200) {
				$("#pieLoader").hide();
				$("#internalPieLoader").hide();
				$("#entLoader").hide();
				$("#entPieChartLoader").hide();
				$("#financeLoader").hide();
				$("#locLoader").hide();
				$("#deptLoader").hide();
				$("#IsVisibleRegionLevelChart").hide();
				$("#regionLi").hide();
				$("#overAllPieChart").show();
				// $scope.complianceChartData = res;
				$scope.taskList = res.taskList;

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

				$scope.loadOtherGraphs();
				var background = {
						type: 'linearGradient',
						x0: 0,
						y0: 0,
						x1: 0,
						y1: 1,
						colorStops: [{offset: 0, color: '#d2e6c9'},
							{offset: 1, color: 'white'}]
				};
			} else {
				toaster.error("Failed", "Something went wrong while fetching \"OverAll Compliance Status\"");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			// console.log("complianceStatusPieChart==== " + error);
		});

	}
//	**  End Code of Monthly Graph


//	-- Start Graph Code --//
	$scope.complianceChartData = [];
	$scope.loadOverAllGraph = function(data){
		$scope.complianceChartData = data;

		//console.log('$scope.complianceChartData : ' + $scope.complianceChartData);

		// alert('loadOverAllGraph complianceChartData : ' + $scope.complianceChartData.ReOpened);
		var background = {
				type: 'linearGradient',
				x0: 0,
				y0: 0,
				x1: 0,
				y1: 1,
				colorStops: [{offset: 0, color: '#d2e6c9'},
					{offset: 1, color: 'white'}]
		};

		$('#jqChart1').jqChart({
			orientation: 'horizontal',
			title: { text: 'Mahindra Logistics Chart' },
			font: '12px Open Sans, sans-serif !important',
			border: {strokeStyle: '#6ba851'},
			background: background,
			animation: {duration: 1},
			shadows: {
				enabled: true
			},
			series: [
				{
					type: 'pie',
					// fillStyles: ['#03C523', '#FBFB35', '#F53636', '#C8CFCB','#bfd630','#85c1e9','#f5b041' ],
					fillStyles: ['#03C523', '#FBFB35', '#F53636', '#C8CFCB','#bfd630', '#f5b041' ],
					labels: {
						stringFormat: '%.1f%%',
						valueType: 'percentage',
						font: '15px sans-serif',
						fillStyle: 'black'
					},
					explodedRadius: 10,
					explodedSlices: [7],
					data: [['Complied', $scope.complianceChartData.Complied], 
						['Posing', $scope.complianceChartData.PosingRisk], 
						['Non-Complied', $scope.complianceChartData.NonComplied], 
						['Delayed', $scope.complianceChartData.Delayed], 
						['Delayed-Reported', $scope.complianceChartData.Delayed_Reported], 
						// ['Waiting For Approval', $scope.complianceChartData.WaitingForApproval], 
						['Re-Opened', $scope.complianceChartData.ReOpened]]
				}
				]
		});

	}

	$scope.loadInternalGraph = function(data){
		$scope.internalComplianceChartData = data;
		var background = {
				type: 'linearGradient',
				x0: 0,
				y0: 0,
				x1: 0,
				y1: 1,
				colorStops: [{offset: 0, color: '#d2e6c9'},
					{offset: 1, color: 'white'}]
		};



		$('#jqChart2').jqChart({
			orientation: 'horizontal',
			width: $scope.innerWidth,
			height: 370,
			border: {strokeStyle: '#6ba851'},
			background: background,
			animation: {duration: 1},
			shadows: {
				enabled: true
			},
			series: [
				{
					type: 'pie',
					//fillStyles: ['#03C523 ', '#fafaa4f0', '#FF0000 ', '#BFBFBF', '#85c1e9', '#f5b041'],
					//fillStyles: ['#03C523 ', '#fafaa4f0', '#FF0000 ', '#BFBFBF', '#85c1e9', '#f5b041'],
					fillStyles: ['#03C523 ', '#fafaa4f0', '#FF0000 ', '#BFBFBF','#bfd630', '#f5b041' ],
					labels: {
						stringFormat: '%.1f%%',
						valueType: 'percentage',
						font: '15px sans-serif',
						fillStyle: 'white'
					},
					explodedRadius: 10,
					explodedSlices: [7],
					data: [['Complied', $scope.internalComplianceChartData.Complied], 
						['Posing', $scope.internalComplianceChartData.PosingRisk], 
						['Non-Complied', $scope.internalComplianceChartData.NonComplied], 
						['Delayed', $scope.internalComplianceChartData.Delayed], 
						['Delayed-Reported', $scope.internalComplianceChartData.Delayed_Reported], 
						// ['Waiting For Approval', $scope.internalComplianceChartData.WaitingForApproval], 
						['Re-Opened', $scope.internalComplianceChartData.ReOpened] ]
				}
				]
		});

	}

	/**
	 * Entity wise Pie chart
	 */

	$scope.loadEntityWisePieChartGraph = function(data) {
		$scope.EntityChartData = data;
		$scope.varEntity = $scope.EntityChartData.Entity;

		var background = {
				type: 'linearGradient',
				x0: 0,
				y0: 0,
				x1: 0,
				y1: 1,
				colorStops: [{offset: 0, color: '#d2e6c9'},
					{offset: 1, color: 'white'}]
		};

		$('#entityLevelPieChart').jqChart({});

		/**
		 * Second Pie Chart
		 */

		$('#entityLevelPieChart1').jqChart({});

		/**
		 * Third
		 */
		$('#entityLevelPieChart2').jqChart({});

		/**
		 * Third
		 */


		$('#entityLevelPieChart3').jqChart({});

		/**
		 * Fourth
		 */

		$('#entityLevelPieChart4').jqChart({});

		/**
		 * Fifth
		 */

		$('#entityLevelPieChart5').jqChart({});
	}

	/**
	 * End
	 */

	$scope.EntityChartData = [];
	$scope.EntityGraphJsonData = {};

	$scope.EntityChartDataObjectKey = {};
	$scope.loadEntityGraph = function(data){

		$scope.EntityChartData = data;
		$scope.EntityGraphJsonData = data;

		// console.log('EntityGraphJsonData : ' + $scope.EntityGraphJsonData.Entity);

		var route = [{
			source : [
				"San Jose",
				"San Francisco",
				"src A",
				"src B"
				],

				destination : [
					"Wellington",
					"Mumbai",
					"des A",
					"des B"
					]
		}];

		$scope.product = route;
		// console.log('product : ' + $scope.product);
		var background = {
				type : 'linearGradient',
				x0 : 0,
				y0 : 0,
				x1 : 0,
				y1 : 1,
				colorStops : [ {
					offset : 0,
					color : '#d2e6c9'
				}, {
					offset : 1,
					color : 'white'
				} ]
		};

		$('#entityLevel').jqChart({
			orientation: 'horizontal',
			width: $window.innerWidth - 400,
			/*height:750,*/
			height: $scope.functionLength * 180,
			title: { text: 'Vertical Level Chart' },
			legend: { location: 'right' },
			border: { padding: 10 },
			animation: { duration: 1 },

			shadows: {
				enabled: true
			},
			legend : {
				location: 'right',
				customItems : [ {
					text : {
						text : 'Complied'
					},
					marker : {
						fillStyle : '#03C523 ',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Posing'
					},
					marker : {
						fillStyle : '#FFFF00',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Non-Complied'
					},
					marker : {
						fillStyle : '#FF0000',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#BFBFBF',
						type : 'circle'
					}
				},  {
					text : {
						text : 'Delayed-Reported'
					},
					marker : {
						fillStyle : '#bfd630',
						type : 'circle'
					}
				}, /*{
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#85c1e9',
						type : 'circle'
					}
				},*/
				{
					text : {
						text : 'Re-Opened'
					},
					marker : {
						fillStyle : '#f5b041',
						type : 'circle'
					}
				}]
			},
			border : {
				strokeStyle : '#6ba851'
			},
			background : background,
			animation : {
				duration : 1
			},
			shadows : {
				enabled : true
			},
			axes : [{
				type: 'category',
				location: 'bottom',
				categories: $scope.EntityChartData.Entity
			}, {
				type: 'linearGradient',
				location: 'left',
				minimum: 0,
				maximum: 100,
				interval: 10,
				labels: {
					stringFormat: '%.1f%%'
				}
			} ],
			series : [ {
				type : 'stacked100Bar',
				title : 'Complied',
				fillStyles : [ '#03C523 ' ],
				data : $scope.EntityChartData.Complied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : 'stacked100Bar',
				title : 'PosingRisk',
				fillStyles : [ '#FFFF00' ],
				data : $scope.EntityChartData.PosingRisk,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : 'stacked100Bar',
				title : 'NonComplied',
				fillStyles : [ '#FF0000' ],
				data :  $scope.EntityChartData.NonComplied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : 'stacked100Bar',
				title : 'Delayed',
				fillStyles : [ '#BFBFBF' ],
				data : $scope.EntityChartData.Delayed,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},{
				type : 'stacked100Bar',
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.EntityChartData.Delayed_Reported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, /*{
				type : 'stacked100Bar',
				title : 'Waiting For Approval',
				fillStyles : [ '#85c1e9' ],
				data : $scope.EntityChartData.WaitingForApproval,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},*/ {
				type : 'stacked100Bar',
				title : 'Re-Opened',
				fillStyles : [ '#f5b041' ],
				data : $scope.EntityChartData.ReOpened,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			} ]
		});
	}

	// Code to load Unit/Location Bar chart
	$scope.UnitNames = [];
	$scope.unitcomplied = [];
	$scope.unitnonComplied = [];
	$scope.unitdelayed = [];
	$scope.unitwaitingForApp = [];
	$scope.unitposingRisks = [];
	$scope.unitreopend = [];
	$scope.unitDataList = {};


	$scope.loadUnitGraph = function(data){$scope.unitChartData =data;
	$scope.unitLength = $scope.unitChartData.Unit.length;
	var background = {
			type : 'linearGradient',
			x0 : 0,
			y0 : 0,
			x1 : 0,
			y1 : 1,
			colorStops : [ {
				offset : 0,
				color : '#d2e6c9'
			}, {
				offset : 1,
				color : 'white'
			} ]
	};

	/*$scope.chart_widht = $scope.innerWidth;
	$scope.chart_height = 3000;*/
	$scope.chart_widht = $window.innerWidth - 400;
	$scope.chart_height = $scope.unitLength * 70;
	$scope.axisLocation='bottom';
	$scope.graphBarType='stacked100Column';
	$scope.labelLocation='left';

	if ($scope.unitLength >=1 && $scope.unitLength <=9){
		// console.log(' if ');
		/*$scope.chart_widht = $scope.innerWidth;
		$scope.chart_height = 15000;*/
		$scope.chart_widht = $window.innerWidth - 400;
		$scope.chart_height = $scope.unitLength * 70;
		$scope.axisLocation='bottom';
		$scope.graphBarType='stacked100Column';
		$scope.labelLocation='left';
	}else if ($scope.unitLength >=10 && $scope.unitLength <=21){
		// console.log('else if');
		/*$scope.chart_widht = $scope.innerWidth;
		$scope.chart_height = 15000;*/
		$scope.chart_widht = $window.innerWidth - 400;
		$scope.chart_height = $scope.unitLength * 70;
		$scope.axisLocation='left';
		$scope.graphBarType='stacked100Bar';
		$scope.labelLocation='bottom';
	}else /*if($scope.unitLength >=20 && $scope.unitLength <=39)*/{
		// console.log('else part ');
		/*$scope.chart_widht = $scope.innerWidth;
		$scope.chart_height = 15000;*/
		$scope.chart_widht = $window.innerWidth - 400;
		$scope.chart_height = $scope.unitLength * 70;
		$scope.axisLocation='left';
		$scope.graphBarType='stacked100Bar';
		$scope.labelLocation='bottom';
	}

	$('#LocationBarChart').jqChart({
		orientation: 'horizontal',
		/*width: $scope.chart_widht,
		height: $scope.chart_height,*/

		width: $window.innerWidth - 400,
		height: $scope.unitLength * 70,
		//height:3000,
		legend : {
			location: 'bottom',
			customItems : [ {
				text : {
					text : 'Complied'
				},
				marker : {
					fillStyle : '#03C523',
					type : 'circle'
				}
			}, {
				text : {
					text : 'Posing'
				},
				marker : {
					fillStyle : '#FFFF00',
					type : 'circle'
				}
			}, {
				text : {
					text : 'Non-Complied'
				},
				marker : {
					fillStyle : '#FF0000',
					type : 'circle'
				}
			}, {
				text : {
					text : 'Delayed'
				},
				marker : {
					fillStyle : '#BFBFBF',
					type : 'circle'
				}
			},  {
				text : {
					text : 'Delayed-Reported'
				},
				marker : {
					fillStyle : '#bfd630',
					type : 'circle'
				}
			}, /*{
				text : {
					text : 'Waiting For Approval'
				},
				marker : {
					fillStyle : '#85c1e9',
					type : 'circle'
				}
			},*/ {
				text : {
					text : 'Re-Opened'
				},
				marker : {
					fillStyle : '#f5b041',
					type : 'circle'
				}
			}]
		},
		border : {
			strokeStyle : '#6ba851'
		},
		background : background,
		animation : {
			duration : 1
		},
		shadows : {
			enabled : true
		},
		axes : [ {
			type : 'category',
			location : $scope.axisLocation,
			categories : $scope.unitChartData.Unit
		}, {
			type : 'linearGradient',
			location : $scope.labelLocation,
			minimum : 0,
			maximum : 100,
			interval : 10,
			labels : {
				stringFormat : '%.1f%%'
			}
		} ],
		series : [ {
			type : $scope.graphBarType,
			title : 'Complied',
			fillStyles : [ '#03C523' ],
			data : $scope.unitChartData.Complied,
			labels : {
				stringFormat : '%.2f%%',
				valueType : 'percentage',
				font : '12px sans-serif'
			}
		}, {
			type : $scope.graphBarType,
			title : 'PosingRisk',
			fillStyles : [ '#FFFF00' ],
			data : $scope.unitChartData.PosingRisk,
			labels : {
				stringFormat : '%.2f%%',
				valueType : 'percentage',
				font : '12px sans-serif'
			}
		}, {
			type : $scope.graphBarType,
			title : 'NonComplied',
			fillStyles : [ '#FF0000' ],
			data : $scope.unitChartData.NonComplied,
			labels : {
				stringFormat : '%.2f%%',
				valueType : 'percentage',
				font : '12px sans-serif'
			}
		}, {
			type : $scope.graphBarType,
			title : 'Delayed',
			fillStyles : [ '#BFBFBF' ],
			data : $scope.unitChartData.Delayed,
			labels : {
				stringFormat : '%.2f%%',
				valueType : 'percentage',
				font : '12px sans-serif'
			}
		},{
			type : $scope.graphBarType,
			title : 'Delayed-Reported',
			fillStyles : [ '#bfd630' ],
			data : $scope.unitChartData.Delayed_Reported,
			cursor : 'pointer',
			labels : {
				stringFormat : '%.2f%%',
				valueType : 'percentage',
				font : '12px sans-serif'
			}
		}, /*{
			type : $scope.graphBarType,
			title : 'Waiting For Approval',
			fillStyles : [ '#85c1e9' ],
			data : $scope.unitChartData.WaitingForApproval,
			labels : {
				stringFormat : '%.2f%%',
				valueType : 'percentage',
				font : '12px sans-serif'
			}
		},*/ {
			type : $scope.graphBarType,
			title : 'Re-Opened',
			fillStyles : [ '#f5b041' ],
			data : $scope.unitChartData.ReOpened,
			labels : {
				stringFormat : '%.2f%%',
				valueType : 'percentage',
				font : '12px sans-serif'
			}
		}]
	});}

	$scope.loadFunctionGraph = function(data) {

		$scope.functionChartData =data;
		$scope.functionLength = $scope.functionChartData.Function.length;
		// console.log('$scope.functionLength : ' + $scope.functionLength);
		var background = {
				type : 'linearGradient',
				x0 : 0,
				y0 : 0,
				x1 : 0,
				y1 : 1,
				colorStops : [ {
					offset : 0,
					color : '#d2e6c9'
				}, {
					offset : 1,
					color : 'white'
				} ]
		};
//		$scope.chart_widht = 735;
//		$scope.chart_height = 350;
		$scope.axisLocation='bottom';
		$scope.graphBarType='stacked100Column';
		$scope.labelLocation='bottom';

		if ($scope.functionLength >=1 && $scope.functionLength <=9){
			$scope.chart_widht = $window.innerWidth - 400;
			$scope.chart_height = $scope.functionLength * 150;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}else if ($scope.functionLength >=10 && $scope.functionLength <=22){
			$scope.chart_widht = $window.innerWidth - 400;
			$scope.chart_height = $scope.functionLength * 150;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}else {
			$scope.chart_widht = $window.innerWidth - 400;
			$scope.chart_height = $scope.functionLength * 150;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}
		$('#DepartmentBarChart').jqChart({
			orientation: 'horizontal',
			title: {text: 'Function Level Chart'},
			height :$scope.functionLength * 150,
			width:$window.innerWidth - 400,
			legend : {
				location: 'bottom',
				customItems : [ {
					text : {
						text : 'Complied'
					},
					marker : {
						fillStyle : '#03C523 ',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Posing'
					},
					marker : {
						fillStyle : '#FFFF00',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Non-Complied'
					},
					marker : {
						fillStyle : '#FF0000',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#BFBFBF',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed Reported'
					},
					marker : {
						fillStyle : '#bfd630',
						type : 'circle'
					}
				},
				/*{
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#85c1e9',
						type : 'circle'
					}
				},*/ {
					text : {
						text : 'Re-Opened'
					},
					marker : {
						fillStyle : '#f5b041',
						type : 'circle'
					}
				} ]
			},
			border : {
				strokeStyle : '#6ba851'
			},
			background : background,
			animation : {
				duration : 1
			},
			shadows : {
				enabled : true
			},
			axes : [ {
				type : 'category',
				location : $scope.axisLocation,
				categories : $scope.functionChartData.Function
			}, {
				type : 'linearGradient',
				location : $scope.labelLocation,
				minimum : 0,
				maximum : 100,
				interval : 10,
				labels : {
					stringFormat : '%.1f%%'
				}
			} ],
			series : [ {
				type : $scope.graphBarType,
				title : 'Complied',
				fillStyles : [ '#03C523 ' ],
				data : $scope.functionChartData.Complied,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'PosingRisk',
				fillStyles : [ '#FFFF00' ],
				data : $scope.functionChartData.PosingRisk,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'NonComplied',
				fillStyles : [ '#FF0000' ],
				data : $scope.functionChartData.NonComplied,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed',
				fillStyles : [ '#BFBFBF' ],
				data : $scope.functionChartData.Delayed,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},{
				type : $scope.graphBarType,
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.functionChartData.Delayed_Reported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, /*{
				type : $scope.graphBarType,
				title : 'Waiting For Approval',
				fillStyles : [ '#85c1e9' ],
				data : $scope.functionChartData.WaitingForApproval,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},*/ {
				type : $scope.graphBarType,
				title : 'Re-Opened',
				fillStyles : [ '#f5b041' ],
				data : $scope.functionChartData.ReOpened,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}]
		});

	}

//	Unit Graph -Search entity wise graph
	$scope.searchEntityWise = function(){
		// console.log("Entity ID "+$scope.dashboardObj.entity);
		var orga_id = $scope.dashboardObj.entity;

		$scope.unitGraph                        = {};
		$scope.unitGraph.Unit                   = [];
		$scope.unitGraph.Complied 			    = [];
		$scope.unitGraph.NonComplied 		    = [];
		$scope.unitGraph.PosingRisk 			= [];
		$scope.unitGraph.Delayed 			    = [];
		$scope.unitGraph.WaitingForApproval 	= [];
		$scope.unitGraph.ReOpened 			    = [];

		var loca_list = [];
		angular.forEach($scope.UnitList, function (unit_data) { 
			// console.log('unit_data : ' + unit_data.loca_name);
			if( (!orga_id || unit_data.orga_id === orga_id)){
				if(loca_list.indexOf(unit_data.loca_id) === -1){
					loca_list.push(unit_data.loca_id);
					$scope.unitGraph.Unit.push(unit_data.loca_name);
					//console.log("Loca Name "+unit_data.loca_name);
				}
			}
		});


		var complied    = 0;
		var noncomplied = 0;
		var posingrisk 	= 0;
		var delayed 	= 0;
		var pending 	= 0;
		var partially_Completed = 0;
		var re_opened 	= 0;

		//Unit Wise graph Count
		for(var i = 0; i< loca_list.length; i++){

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var from_date = null;
			var from_to   = null;

			var from = angular.isDefined($scope.date_from);
			if(from)
				from_date = new Date($scope.date_from);

			var to = angular.isDefined($scope.date_to);
			if(to)
				from_to = new Date($scope.date_to);

			angular.forEach($scope.taskList, function (data) { 

				var task_date = new Date(data.date);

				if((!orga_id || data.orga_id === orga_id) && (loca_list[i]===data.loca_id) && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					//if(task_date>=from_date && task_date <=from_to){
					if(data.status=='complied'){
						complied++; 
					}

					if(data.status=='noncomplied'){
						noncomplied++;
					}

					if(data.status=='posingrisk'){
						posingrisk++;
					}

					if(data.status=='delayed'){
						delayed++;
					}

					if(data.status=='waitingforapproval'){
						partially_Completed++;
					}
					if(data.status=='reopen'){
						re_opened++;
					}
					if(data.status=='delayed-reported'){
						delayed_reported++;
					}
					//}
				}//End IF 
			});

			$scope.unitGraph.Complied.push(complied);
			$scope.unitGraph.NonComplied.push(noncomplied);
			$scope.unitGraph.PosingRisk.push(posingrisk);
			$scope.unitGraph.Delayed.push(delayed);
			$scope.unitGraph.WaitingForApproval.push(partially_Completed);
			$scope.unitGraph.ReOpened.push(re_opened);

			$scope.loadUnitGraph($scope.unitGraph);	
		}//End unit Wise graph Count

	}

	$scope.getFunctionListByOrgaId = function() {
		$scope.dashboardObj.FunlevelEntity;
		$scope.dashboardObj.FunlevelUnit;

		var obj = {
				orga_id : $scope.dashboardObj.FunlevelEntity,
				loca_id : $scope.dashboardObj.FunlevelUnit,
		}

		ApiCallFactory.getFunctionListByOrgaId(obj).success(function(res, status){
			if(status === 200){
				$scope.FunctionList = res;
				// console.log('list orga unit fun' + $scope.FunctionList);
				$scope.searchFuntionLevelGraph();
			}else{
				// console.log("get getFunctionListByOrgaId ===== Failed");
			}
		}).error(function(error){
			console.log("get getFunctionListByOrgaId ===== " + error);
		});
	}


	$scope.searchFuntionLevelGraph = function(){

		$scope.functionGraph                        = {};
		$scope.functionGraph.Function               = [];
		$scope.functionGraph.Complied 			    = [];
		$scope.functionGraph.NonComplied 		    = [];
		$scope.functionGraph.PosingRisk 			= [];
		$scope.functionGraph.Delayed 			    = [];
		$scope.functionGraph.Delayed_Reported			    = [];
		$scope.functionGraph.WaitingForApproval 	= [];
		$scope.functionGraph.ReOpened 			    = [];

		var orga_id = $scope.dashboardObj.FunlevelEntity;
		var loca_id = $scope.dashboardObj.FunlevelUnit;
		var dept_id = $scope.dashboardObj.FunlevelFunction;



		// console.log('orga_id : ' + $scope.dashboardObj.FunlevelEntity + '\t loca_id : ' + $scope.dashboardObj.FunlevelUnit + '\t dept_id : ' + $scope.dashboardObj.FunlevelFunction)

		var loca_list = [];
		var dept_list = [];
		// console.log('function list : ' + $scope.FunctionList);
		angular.forEach($scope.FunctionList, function (fun_data) { 
			if( (!orga_id || fun_data.orga_id === orga_id) && (!loca_id || fun_data.loca_id === loca_id)){
				//Add Departments
				if(dept_list.indexOf(fun_data.dept_id) === -1){
					dept_list.push(fun_data.dept_id);
					$scope.functionGraph.Function.push(fun_data.dept_name);
				}
			}
		});

		var complied    = 0;
		var noncomplied = 0;
		var posingrisk 	= 0;
		var delayed 	= 0;
		var pending 	= 0;
		var partially_Completed = 0;
		var re_opened 	= 0;
		var delayed_reported = 0;

		//Function Wise graph Count
		for(var i = 0; i< dept_list.length; i++){

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;
			var from_date = null;
			var from_to   = null;

			var from = angular.isDefined($scope.date_from);
			if(from)
				from_date = new Date($scope.date_from);

			var to = angular.isDefined($scope.date_to);
			if(to)
				from_to = new Date($scope.date_to);

			angular.forEach($scope.taskList, function (data) { 

				var task_date = new Date(data.date);

				if((!orga_id || data.orga_id === orga_id) && (!loca_id || loca_id === data.loca_id) && (dept_list[i]===data.dept_id) && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					//if(task_date>=from_date && task_date <=from_to){
					if(data.status=='complied'){
						complied++; 
					}

					if(data.status=='noncomplied'){
						noncomplied++;
					}

					if(data.status=='posingrisk'){
						posingrisk++;
					}

					if(data.status=='delayed'){
						delayed++;
					}

					if(data.status=='waitingforapproval'){
						partially_Completed++;
					}
					if(data.status=='reopen'){
						re_opened++;
					}
					if(data.status=='delayed-reported'){
						delayed_reported++;
					}
					//}

				}//End IF 


			});

			$scope.functionGraph.Complied.push(complied);
			$scope.functionGraph.NonComplied.push(noncomplied);
			$scope.functionGraph.PosingRisk.push(posingrisk);
			$scope.functionGraph.Delayed.push(delayed);
			$scope.functionGraph.WaitingForApproval.push(partially_Completed);
			$scope.functionGraph.ReOpened.push(re_opened);
			$scope.functionGraph.Delayed_Reported.push(delayed_reported);

			$scope.loadFunctionGraph($scope.functionGraph);	
		}//End function Wise graph Count

	}

//	-- End Graph Code --//

	$scope.drilledReport = function(obj){
		$scope.entityName =obj.entity;
		// console.log('Drilled report method called..');
		// console.log('obj drilledReport : ' + obj);

		var reportList = [];
		var reportSubTaskList = [];
		var from_date = null;
		var from_to   = null;
		var reportSubTaskList = [];

		var from = angular.isDefined($scope.date_from);
		if(from)
			from_date = new Date($scope.date_from);

		var to = angular.isDefined($scope.date_to);
		if(to)
			from_to = new Date($scope.date_to);

		angular.forEach($scope.taskList, function (data) {
			//console.log('data : ' + JSON.stringify(data));
			var task_date = new Date(data.date);
			$scope.status = data.status;
			$scope.objStatus = obj.status;

			if(obj.chart_name=="overall"){

				if(obj.status=="PosingRisk" && data.status=="posingrisk"){
					//reportList.push(data);
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(obj.status=="Complied" && data.status=='complied'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);

				}

				if(obj.status=="NonComplied" && data.status=='noncomplied'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(obj.status=="Delayed" && data.status=='delayed' ){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);

				}

				if( obj.status=="Waiting For Approval" && data.status=='waitingforapproval'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);

				}
				if(obj.status=="Re-Opened" && data.status=='reopen'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);

				}

				//Delayed reported drilled down will open once clicked on main menue

				if(obj.status=="DelayedReported" && data.status=='delayed-reported'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);

				}
			}


			//Sub unit level drill data

			if(obj.chart_name=="categoryLevel"){

				var location = obj.unit;
				var loca_type = 0;

				/*if(location=="Corporate Office")
					loca_type = 1;
				else*/ if(location=="West")
					loca_type = 2;
				else if(location=="South")
					loca_type = 3;
				else if(location=="North")
					loca_type = 4;

				if(loca_type == data.loca_type &&  obj.status=="PosingRisk" && data.status=="posingrisk" && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(loca_type == data.loca_type && obj.status=="Complied" && data.status=='complied' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(loca_type == data.loca_type && obj.status=="NonComplied" && data.status=='noncomplied' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(loca_type == data.loca_type && obj.status=="Delayed" && data.status=='delayed' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(loca_type == data.loca_type && obj.status=="Waiting For Approval" && data.status=='waitingforapproval' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}
				if(loca_type == data.loca_type && obj.status=="Re-Opened" && data.status=='reopen' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}
				if(loca_type == data.loca_type && obj.status=="Delayed-Reported" && data.status=='delayed-reported' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

			}

			//console.log("Maintask Size:" + reportList.length);
			//console.log("Subtask Size:" + reportSubTaskList.length);

			if(obj.chart_name=="internal"){
				if(obj.status=="PosingRisk" && data.status=="posingrisk" && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_cat_law=='Internal')
						reportList.push(data);
				}

				if(obj.status=="Complied" && data.status=='complied' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_cat_law=='Internal')
						reportList.push(data);
				}

				if(obj.status=="NonComplied" && data.status=='noncomplied' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_cat_law=='Internal')
						reportList.push(data);
				}

				if(obj.status=="Delayed" && data.status=='delayed' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_cat_law=='Internal')
						reportList.push(data);
				}

				if( obj.status=="Waiting For Approval" && data.status=='waitingforapproval' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_cat_law=='Internal')
						reportList.push(data);
				}
				if(obj.status=="Re-Opened" && data.status=='reopen' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_cat_law=='Internal')
						reportList.push(data);
				}
				if(obj.status=="Delayed-Reported" && data.status=='delayed-reported' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_cat_law=='Internal')
						reportList.push(data);
				}
			}

			if(obj.chart_name=="entityLevel") {
				if(obj.entity==data.orga_name &&  obj.status=="PosingRisk" && data.status=="posingrisk"){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(obj.entity==data.orga_name && obj.status=="Complied" && data.status=='complied' ){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(obj.entity==data.orga_name && obj.status=="NonComplied" && data.status=='noncomplied'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(obj.entity==data.orga_name && obj.status=="Delayed" && data.status=='delayed'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if(obj.entity==data.orga_name && obj.status=="Waiting For Approval" && data.status=='waitingforapproval'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}
				if(obj.entity==data.orga_name && obj.status=="Re-Opened" && data.status=='reopen'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}


				if(obj.entity==data.orga_name && obj.status=="Delayed-Reported" && data.status=='delayed-reported'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}
			}

			if(obj.chart_name=="unitLevel"){
				var orga_id = $scope.dashboardObj.entity;

				if((!orga_id || data.orga_id === orga_id) && obj.unit == data.loca_name &&  obj.status=="PosingRisk" && data.status=="posingrisk"){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && obj.unit == data.loca_name && obj.status=="Complied" && data.status=='complied'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && obj.unit == data.loca_name && obj.status=="NonComplied" && data.status=='noncomplied' ){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && obj.unit == data.loca_name && obj.status=="Delayed" && data.status=='delayed'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && obj.unit == data.loca_name && obj.status=="Delayed-Reported" && data.status=='delayed-reported'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && obj.unit == data.loca_name && obj.status=="Waiting For Approval" && data.status=='waitingforapproval'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}
				if((!orga_id || data.orga_id === orga_id) && obj.unit == data.loca_name && obj.status=="Re-Opened" && data.status=='reopen'){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

			}

			if(obj.chart_name=="departmentLevel"){
				var orga_id = $scope.dashboardObj.FunlevelEntity;
				var loca_id = $scope.dashboardObj.FunlevelUnit;

				if((!orga_id || data.orga_id === orga_id) && (!loca_id || data.loca_id === loca_id) && obj.department == data.dept_name &&  obj.status=="PosingRisk" && data.status=="posingrisk" && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && (!loca_id || data.loca_id === loca_id) && obj.department == data.dept_name && obj.status=="Complied" && data.status=='complied' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && (!loca_id || data.loca_id === loca_id) && obj.department == data.dept_name && obj.status=="NonComplied" && data.status=='noncomplied' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && (!loca_id || data.loca_id === loca_id) && obj.department == data.dept_name && obj.status=="Delayed" && data.status=='delayed' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

				if((!orga_id || data.orga_id === orga_id) && (!loca_id || data.loca_id === loca_id) && obj.department == data.dept_name && obj.status=="Delayed-Reported" && data.status=='delayed-reported' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}
				if((!orga_id || data.orga_id === orga_id) && (!loca_id || data.loca_id === loca_id) && obj.department == data.dept_name && obj.status=="Waiting For Approval" && data.status=='waitingforapproval' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}
				if((!orga_id || data.orga_id === orga_id) && (!loca_id || data.loca_id === loca_id) && obj.department == data.dept_name && obj.status=="Re-Opened" && data.status=='reopen' && (!(from && to) || task_date>=from_date && task_date <=from_to)){
					if(data.task_type=='Main')
						reportList.push(data);
					else
						reportSubTaskList.push(data);
				}

			}

			/* Financial Graph  */
			var month = new Array();
			month[0] = "January";
			month[1] = "February";
			month[2] = "March";
			month[3] = "April";
			month[4] = "May";
			month[5] = "June";
			month[6] = "July";
			month[7] = "August";
			month[8] = "September";
			month[9] = "October";
			month[10] = "November";
			month[11] = "December";

			if(obj.chart_name=="financeLevel") {
				var changed_month = $scope.dashboardObj.fmonth;
				var changed_year = $scope.dashboardObj.fyear;
				var next_year = changed_year + 1;
				var value = obj.month;
				if(month.indexOf(value) >-1){
					var legal_Date = data.ttrn_legal_due_date.split("-");

					var date_month = parseInt(legal_Date[1])-1;

					var date_lag = new Date(legal_Date[2],legal_Date[1]-1,legal_Date[0]);
					// console.log(date_lag);

					if(obj.status=="PosingRisk" && data.status=="posingrisk" && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2]>= changed_year  && legal_Date[2] <= next_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() < 3))
					){
						if(month[date_lag.getMonth()] === value) {
							if(data.task_type=='Main')
								reportList.push(data);
							else
								reportSubTaskList.push(data);
						}
					}

					if(obj.status=="Complied" && data.status=='complied'){
						if(month[date_lag.getMonth()] === value){ 
							if(data.task_type=='Main')
								reportList.push(data);
							else
								reportSubTaskList.push(data);
						}
					}

					if(obj.status=="NonComplied" && data.status=='noncomplied'){
						if(month[date_lag.getMonth()] === value){ 
							if(data.task_type=='Main')
								reportList.push(data);
							else
								reportSubTaskList.push(data);
						}
					}

					if(obj.status=="Delayed" && data.status=='delayed'){
						if(month[date_lag.getMonth()] === value){ 
							if(data.task_type=='Main')
								reportList.push(data);
							else
								reportSubTaskList.push(data);
						}
					}

					if(obj.status=="DelayedReported" && data.status=='delayed-reported'){
						if(month[date_lag.getMonth()] === value){ 
							if(data.task_type=='Main')
								reportList.push(data);
							else
								reportSubTaskList.push(data);
						}
					}
					if(obj.status=="Delayed-Reported" && data.status=='delayed-reported'){
						if(month[date_lag.getMonth()] === value){ 
							if(data.task_type=='Main')
								reportList.push(data);
							else
								reportSubTaskList.push(data);
						}
					}

					if( obj.status=="Waiting For Approval" && data.status=='waitingforapproval' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2]>= changed_year  && legal_Date[2] <= next_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() < 3))
					){
						if(month[date_lag.getMonth()] === value){ 
							if(data.task_type=='Main')
								reportList.push(data);
							else
								reportSubTaskList.push(data);
						}
					}
					if(obj.status=="Re-Opened" && data.status=='reopen' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2]>= changed_year  && legal_Date[2] <= next_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() < 3))
					){
						if(month[date_lag.getMonth()] === value){ 
							if(data.task_type=='Main')
								reportList.push(data);
							else
								reportSubTaskList.push(data);
						}
					}
				}else{
					var task_date = new Date(data.date);
					var legal_Date = data.ttrn_legal_due_date.split("-");
					var date_month = parseInt(legal_Date[1])-1;
					var date_lag = new Date(legal_Date[2],legal_Date[1]-1,legal_Date[0]);
					// console.log(date_lag);

					if(obj.status=="PosingRisk" && data.status=='posingrisk' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& date_lag.getMonth() >2 && (legal_Date[2]== changed_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() == changed_month))
							&& data.dept_name === value  ){
						//if(data.dept_name === value){ 
						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);
						//}
					}else if( obj.status=="PosingRisk" && data.status=='posingrisk' && date_lag.getMonth() < 2 && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2] == next_year) && ((legal_Date[2] == next_year && date_lag.getMonth() == changed_month)) 
							&& data.dept_name === value ){

						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}

					if(obj.status=="Complied" && data.status=='complied' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& date_lag.getMonth() >2 && (legal_Date[2]== changed_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() == changed_month))
							&& data.dept_name === value  ) {
						//if(data.dept_name === value){ 
						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}else if(obj.status=="Complied" && data.status=='complied' && date_lag.getMonth() < 2 && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2] == next_year) && ((legal_Date[2] == next_year && date_lag.getMonth() == changed_month)) 
							&& data.dept_name === value ){

						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}


					if(obj.status=="NonComplied" && data.status=='noncomplied' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& date_lag.getMonth() >2 && (legal_Date[2]== changed_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() == changed_month))
							&& data.dept_name === value  ){
						//if(data.dept_name === value){ 
						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}else if( obj.status=="NonComplied" && data.status=='noncomplied' && date_lag.getMonth() < 2 && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2] == next_year) && ((legal_Date[2] == next_year && date_lag.getMonth() == changed_month)) 
							&& data.dept_name === value ){

						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}

					if(obj.status=="Delayed" && data.status=='delayed' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& date_lag.getMonth() >2 && (legal_Date[2]== changed_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() == changed_month))
							&& data.dept_name === value  ){
						//if(data.dept_name === value){ 
						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}else if(obj.status=="Delayed" && data.status=='delayed' && date_lag.getMonth() < 2 && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2] == next_year) && ((legal_Date[2] == next_year && date_lag.getMonth() == changed_month)) 
							&& data.dept_name === value ){

						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}


					if(obj.status=="Delayed-Reported" && data.status=='delayed-reported' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& date_lag.getMonth() >2 && (legal_Date[2]== changed_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() == changed_month))
							&& data.dept_name === value  ){
						//if(data.dept_name === value){ 
						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}else if(obj.status=="Delayed-Reported" && data.status=='delayed-reported' && date_lag.getMonth() < 2 && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2] == next_year) && ((legal_Date[2] == next_year && date_lag.getMonth() == changed_month)) 
							&& data.dept_name === value ){

						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}


					if(obj.status=="Waiting For Approval" && data.status=='waitingforapproval' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& date_lag.getMonth() >2 && (legal_Date[2]== changed_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() == changed_month))
							&& data.dept_name === value  ){
						//if(data.dept_name === value){ 
						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}else if(obj.status=="Waiting For Approval" && data.status=='waitingforapproval' && date_lag.getMonth() < 2 && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2] == next_year) && ((legal_Date[2] == next_year && date_lag.getMonth() == changed_month)) 
							&& data.dept_name === value ){

						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}

					if(obj.status=="Re-Opened" && data.status=='reopened' && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& date_lag.getMonth() >2 && (legal_Date[2]== changed_year) && ((legal_Date[2]== changed_year && date_lag.getMonth() == changed_month))
							&& data.dept_name === value  ){
						//if(data.dept_name === value){ 
						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}else if(obj.status=="Re-Opened" && data.status=='reopened' && date_lag.getMonth() < 2 && (!(from && to) || task_date>=from_date && task_date <=from_to)
							&& (legal_Date[2] == next_year) && ((legal_Date[2] == next_year && date_lag.getMonth() == changed_month)) 
							&& data.dept_name === value ){

						if(data.task_type=='Main')
							reportList.push(data);
						else
							reportSubTaskList.push(data);

					}
				}
			} //End Financial graph
		});
		//$scope.reportList = reportList;
		//$scope.reportSubTaskList = reportSubTaskList;
		obj.reportList = reportList;
		//console.log("reportList:" +JSON.stringify(obj.reportList));
		obj.reportSubTaskList = reportSubTaskList;
		//console.log("Sub task List " +JSON.stringify(obj.reportSubTaskList));
		$scope.open(obj);

		//$state.go('DrilledReport',obj);
		//$scope.loadModel($scope.reportList,obj.status);
	}

	$scope.open = function(obj) {
		//console.log("Object Data : "+obj.reportList.length)
		if(obj.reportList.length<=0){
			if(obj.reportSubTaskList.length<=0){
				toaster.error("No task available under  " + obj.status);
				return;
			}
		}
		//if(obj.reportList)
		$scope.obj=obj;
		$('#drilledModal').modal();
	};


	$scope.loadFinanceGraph = function(data) {
		// console.log('loadFinanceGraph meth() ');
		$scope.FinanceChartData = data;
		var background = {
				type : 'linearGradient',
				x0 : 0,
				y0 : 0,
				x1 : 0,
				y1 : 1,
				colorStops : [{
					offset : 0,
					color : '#d2e6c9'
				}, {
					offset : 1,
					color : 'white'
				}]
		};

		$scope.chart_widht = 1090;
		$scope.chart_height = 350;
		$scope.axisLocation='bottom';
		$scope.graphBarType='stacked100Column';
		$scope.labelLocation='left';

		if($scope.FinanceChartData.Month.length > 12){
			$scope.chart_widht = 1150;
			$scope.chart_height = 850;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}

		$('#financeLevel').jqChart({
			orientation: 'horizontal',
			width: $scope.chart_widht,
			height: $scope.chart_height,	
			legend : {
				customItems : [ {
					text : {
						text : 'Complied'
					},
					marker : {
						fillStyle : '#03C523 ',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Posing'
					},
					marker : {
						fillStyle : '#FFFF00',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Non-Complied'
					},
					marker : {
						fillStyle : '#FF0000',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#BFBFBF',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed-Reported'
					},
					marker : {
						fillStyle : '#bfd630',
						type : 'circle'
					}
				} , /*{
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#85c1e9',
						type : 'circle'
					}
				},*/ {
					text : {
						text : 'Re-Opened'
					},
					marker : {
						fillStyle : '#f5b041',
						type : 'circle'
					}
				}  ]
			},
			border : {
				strokeStyle : '#6ba851'
			},
			background : background,
			animation : {
				duration : 1
			},
			shadows : {
				enabled : true
			},
			axes : [ {
				type : 'category',
				location : $scope.axisLocation,
				categories : $scope.FinanceChartData.Month, 
			}, {
				type : 'linearGradient',
				location : $scope.labelLocation,
				minimum : 0,
				maximum : 100,
				interval : 10,
				labels : {
					stringFormat : '%.1f%%'
				}
			} ],
			series : [ {
				type : $scope.graphBarType,
				title : 'Complied',
				fillStyles : [ '#03C523 ' ],
				data : $scope.FinanceChartData.Complied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'PosingRisk',
				fillStyles : [ '#FFFF00' ],
				data : $scope.FinanceChartData.PosingRisk,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'NonComplied',
				fillStyles : [ '#FF0000' ],
				data :  $scope.FinanceChartData.NonComplied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed',
				fillStyles : [ '#BFBFBF' ],
				data : $scope.FinanceChartData.Delayed,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},{
				type :  $scope.graphBarType,
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.FinanceChartData.Delayed_Reported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, /*{
				type : $scope.graphBarType,
				title : 'Waiting For Approval',
				fillStyles : [ '#85c1e9' ],
				data : $scope.FinanceChartData.WaitingForApproval,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},*/ {
				type : $scope.graphBarType,
				title : 'Re-Opened',
				fillStyles : [ '#f5b041' ],
				data : $scope.FinanceChartData.ReOpened,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			} ]
		});
	}



	$scope.loadEntityQuarterWiseBarGraph = function(data){

		$scope.FinanceChartData = data;
		//  alert(JSON.stringify($scope.FinanceChartData));
		//	alert($scope.FinanceChartData.Month.length);
		var background = {
				type: 'linearGradient',
				x0: 0,
				y0: 0,
				x1: 0,
				y1: 1,
				colorStops: [{
					offset: 0,
					color: '#d2e6c9'
				}, {
					offset: 1,
					color: 'white'
				}]
		};

		$scope.chart_widht = 1150;
		$scope.chart_height = 350;
		$scope.axisLocation = 'bottom';
		$scope.graphBarType = 'stacked100Column';
		$scope.labelLocation = 'left';
		//alert($scope.FinanceChartData.Month.length);
		if ($scope.FinanceChartData.Quarter.length > 12) {
			$scope.chart_widht = 1150;
			$scope.chart_height = 850;
			$scope.axisLocation = 'left';
			$scope.graphBarType = 'stacked100Bar';
			$scope.labelLocation = 'bottom';
		}

		$('#quarterWiseLevelGraph').jqChart({
			orientation: 'horizontal',
			width: $scope.chart_widht,
			height: $scope.chart_height,
			legend: {
				customItems: [{
					text: {
						text: 'Complied'
					},
					marker: {
						fillStyle: '#03C523 ',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Posing'
					},
					marker: {
						fillStyle: '#fafaa4f0',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Non-Complied'
					},
					marker: {
						fillStyle: '#FF0000',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Delayed'
					},
					marker: {
						fillStyle: '#BFBFBF',
						type: 'circle'
					}
				}, /*{
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#85c1e9',
						type : 'circle'
					}
				},*/ {
					text : {
						text : 'Re-Opened'
					},
					marker : {
						fillStyle : '#f5b041',
						type : 'circle'
					}
				}]
			},
			border: {
				strokeStyle: '#6ba851'
			},
			background: background,
			animation: {
				duration: 1
			},
			shadows: {
				enabled: true
			},
			axes: [{
				type: 'category',
				location: $scope.axisLocation,
				categories: $scope.FinanceChartData.Quarter,
			}, {
				type: 'linearGradient',
				location: $scope.labelLocation,
				minimum: 0,
				maximum: 100,
				interval: 10,
				labels: {
					stringFormat: '%.1f%%'
				}
			}],
			series: [{
				type: $scope.graphBarType,
				title: 'Complied',
				fillStyles: ['#03C523 '],
				data: $scope.FinanceChartData.Complied,
				cursor: 'pointer',
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'PosingRisk',
				fillStyles: ['#fafaa4f0'],
				data: $scope.FinanceChartData.PosingRisk,
				cursor: 'pointer',
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'NonComplied',
				fillStyles: ['#FF0000'],
				data: $scope.FinanceChartData.NonComplied,
				cursor: 'pointer',
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'Delayed',
				fillStyles: ['#BFBFBF'],
				data: $scope.FinanceChartData.Delayed,
				cursor: 'pointer',
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, /*{
				type : $scope.graphBarType,
				title : 'Waiting For Approval',
				fillStyles : [ '#85c1e9' ],
				data : $scope.FinanceChartData.WaitingForApproval,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},*/ {
				type : $scope.graphBarType,
				title : 'Re-Opened',
				fillStyles : [ '#f5b041' ],
				data : $scope.FinanceChartData.ReOpened,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}]
		});
	}


	$scope.getQuarterGraphInMonth = function(data) {
		$scope.monthlyChartData = data;
	}

	/**
	 * ng-change quarter graph
	 */

	$scope.dashboardObj.orgaId = 'null';
	/**
	 * Hidden
	 */


	$scope.testFun = function() {
		var date = new Date();
		var curMonth = date.getMonth() + 1;
		// console.log('current month : ' + curMonth);

		if($scope.date_from == null || $scope.date_from == undefined || $scope.date_from == ''){
			$scope.frmSearchDate = '0';
			$scope.toSearchDate = '0';
			console.log('From Search Date is : ' + $scope.frmSearchDate + '\t to date : ' + $scope.toSearchDate);
		}else {
			$scope.frmSearchDate = $filter('date')($scope.date_from, 'yyyy-MM-dd');
			$scope.toSearchDate = $filter('date')($scope.date_to, 'yyyy-MM-dd');
		}

		var obj1 = {
				date_from: $scope.frmSearchDate,
				date_to: $scope.toSearchDate,
				orgaId: $scope.dashboardObj.orgaId
		};
		// alert("test fun ");

		ApiCallFactory.searchComplianceStatusQrtrBarChart(obj1).success(function(res,status) {


			DataFactory.setShowLoader(false);
			if(status === 200){
				// $scope.complianceChartData = res;
				$scope.taskAllList = res.taskList;
				$scope.entityQuarterGraph                    = {};
				$scope.entityQuarterGraph.Month              = [];
				$scope.entityQuarterGraph.Quarter            = [];
				$scope.entityQuarterGraph.Complied           = [];
				$scope.entityQuarterGraph.NonComplied        = [];
				$scope.entityQuarterGraph.PosingRisk         = [];
				$scope.entityQuarterGraph.Delayed            = [];
				$scope.entityQuarterGraph.WaitingForApproval = [];
				$scope.entityQuarterGraph.ReOpened           = [];


				var month = new Array();
				month[0] = "January";
				month[1] = "February";
				month[2] = "March";
				month[3] = "April";
				month[4] = "May";
				month[5] = "June";
				month[6] = "July";
				month[7] = "August";
				month[8] = "September";
				month[9] = "October";
				month[10] = "November";
				month[11] = "December";

				if ($scope.dashboardObj.fquarter == 0) {
					$scope.entityQuarterGraph.Quarter.push("Quarter 1");

					month[3] = "April";
					month[4] = "May";
					month[5] = "June";

					$scope.entityQuarterGraph.Quarter.push("Quarter 2");

					month[6] = "July";
					month[7] = "August";
					month[8] = "September";

					$scope.entityQuarterGraph.Quarter.push("Quarter 3");

					month[9] = "October";
					month[10] = "November";
					month[11] = "December";

					$scope.entityQuarterGraph.Quarter.push("Quarter 4");

					month[0] = "January";
					month[1] = "February";
					month[2] = "March";
				}

				angular.forEach($scope.entityQuarterGraph.Quarter, function (value, key) {
					//alert(value.length);
					/*Financial graph */
					var fcomplied = 0;
					var fnoncomplied = 0;
					var fposingrisk = 0;
					var fdelayed = 0;
					var fpending = 0;
					var fpartially_Completed = 0;
					var fre_opened = 0;
					var fDelayed_Reported = 0;
					var quarter = "";
					/*Financial graph */

					// var curr_date = new Date();

					// var curr_year = curr_date.getFullYear();
					if(value === "Quarter 1"){
						quarter = ["April","May","June"];
					}else if(value === "Quarter 2"){
						quarter = ["July","August","September"];
					}else if(value === "Quarter 3"){
						quarter = ["October","November","December"];
					}else if(value === "Quarter 4"){
						quarter = ["January","February","March"];
					}

					var next_year = curr_year + 1;

					angular.forEach($scope.taskAllList, function (data) {
						var legal_Date = data.ttrn_legal_due_date.split("-");
						// console.log('Quarter legal_Date : ' + legal_Date);
						var curDate = new Date();
						// console.log('curDate entity wise data : ' + curDate);
						//	console.log('lglDate : ' + curDate.getFullYear());
						var legalDate = curDate.getFullYear();
						var curMonth = curDate.getMonth();

						var date_month = parseInt(legal_Date[1]) - 1;
						var a = date_month - 1;

						var date_lag = new Date(legal_Date[2], legal_Date[1] - 1, legal_Date[0]);
						// console.log(date_lag);

						var oganiazationId = data.orga_id;
						// console.log('oganiazationId : ' + oganiazationId);
						// if(legal_Date[2]>= curr_year  && legal_Date[2] <= next_year ){
						if(data.orga_id == $scope.dashboardObj.orgaId) {
							if((legal_Date[2]>= curr_year  && legal_Date[2] <= next_year) && ((legal_Date[2]== curr_year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3)) ){

								for(var i = 0;i<quarter.length;i++){					
									if (month[date_lag.getMonth()] === quarter[i]) {
										if (data.status == 'complied') {
											fcomplied++;
										}

										if (data.status == 'noncomplied') {
											fnoncomplied++;
										}

										if (data.status == 'posingrisk') {
											fposingrisk++;
										}

										if (data.status == 'delayed') {
											fdelayed++;
										}

										if (data.status == 'waitingforapproval') {
											fpartially_Completed++;
										}
										if (data.status == 'reopen') {
											fre_opened++;
										}
										if (data.status == 'delayed-reported') {
											fDelayed_Reported++;
										}
									}
								}
							}
						}
					});
					//alert(fcomplied);
					$scope.entityQuarterGraph.Complied.push(fcomplied);
					$scope.entityQuarterGraph.NonComplied.push(fnoncomplied);
					$scope.entityQuarterGraph.PosingRisk.push(fposingrisk);
					$scope.entityQuarterGraph.Delayed.push(fdelayed);
					$scope.entityQuarterGraph.WaitingForApproval.push(fpartially_Completed);
					$scope.entityQuarterGraph.ReOpened.push(fre_opened);
					$scope.financialGraph.Delayed_Reported.push(fDelayed_Reported);

				});
				$('#quarterWiseLoaders').show();
				$scope.loadEntityQuarterWiseBarGraph($scope.entityQuarterGraph);

			}else{
				toaster.error("Failed", "Something went wrong while fetching \"OverAll Compliance Status\"");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("complianceStatusPieChart ==== " + error);
		});


	}

	/**
	 * End
	 */



	$scope.ExportTask=true;
	$scope.toggle=function() {
		$scope.ExportTask = !$scope.ExportTask;
	}


//	to generate PDF
	$scope.generateMaintask_Pdf=function(){
		var doc1 = new jsPDF('p', 'pt', 'a4');
		var res1 = doc1.autoTableHtmlToJson(document.getElementById("example"));
		doc1.autoTable(res1.columns, res1.data, {
			startY: 10,
			styles: {
				overflow: 'linebreak',
				fontSize: 5,
			},
			columnStyles: {
				1: {columnWidth: 'auto'}
			}
		});		
		doc1.setProperties({
			title: 'Compliance Management Tool Report',
			subject: 'Report',
			author: 'Lexcare',
			keywords: 'lexcare compliance tool',
			creator: 'Compliance Management Tool'

		});
		doc1.cellInitialize();
		doc1.save('Main_Task_DrilledReport.pdf');	
	}


	// to generate data in CSV format
	/*$scope.ExportExcels_Maintask=function(){
		var Curr_Date = new Date();
		var Curr_date1= moment(Curr_Date).format('DD-MM-YYYY');
		//$('#reportList1').tableExport({type:'excel',escape:'false'});		
		$("#example tr td a").removeAttr("href");
		$("#example tr td a").css("color","black");
		$("#example").table2excel({		
			name: "Report",
			filename: "DrilledReport_(Main Task)" 
		}); 
	}*/


	// to generate data in CSV format
	$scope.ExportExcels_Maintask=function(){
		/*var Curr_Date = new Date();
		var Curr_date1= moment(Curr_Date).format('DD-MM-YYYY');*/
		//$('#reportList1').tableExport({type:'excel',escape:'false'});	

		/*$("#example tr td a").removeAttr("href");
		$("#example tr td a").css("color","black");
		$("#example").table2excel({		
				    name: "Report",
				    filename: "DrilledReport_(Main Task)" 
				  }); */
		var date = new Date();
		var d = date.getFullYear() + '-' + date.getMonth() + 1 + '-' + date.getDate();
		/* var blob = new Blob([document.getElementById('example').innerHTML], {
          type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"

      });
      $window.saveAs(blob, "Report_" + d + ".xls");*/

		var tab_text="<table border='2px'><tr>";
		var textRange; var j=0;
		var tab = document.getElementById('example'); // id of table

		for(j = 0 ; j < tab.rows.length ; j++) 
		{     
			tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
		}

		tab_text=tab_text+"</table>";
		tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
		tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
		tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, "");


		var txtArea1 = document.getElementById('txtArea1');
		/*txtArea1.contentWindow.document.open("txt/html","replace");
  	txtArea1.contentWindow.document.write(tab_text);
  	txtArea1.contentWindow.document.close();
  	txtArea1.contentWindow.focus(); 
  	txtArea1.contentWindow.focus(); 
  	var sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"IE Excel Export.xls");*/
		var ua = window.navigator.userAgent;
		var msie = ua.indexOf("MSIE "); 
		var sa;
		if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
		{
			txtArea1.contentWindow.document.write(tab_text);
			txtArea1.contentWindow.document.close();
			txtArea1.contentWindow.focus(); 
			txtArea1.contentWindow.focus();
			sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"DrilledReport_"+d+"(Main Task).xls");
		}  
		else{
			// sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"IE Excel Export.xls");
			$("#example tr td a").removeAttr("href");
			$("#example tr td a").css("color","black");
			$("#example").table2excel({		
				name: "Report",
				filename: "DrilledReport_"+d+"(Main Task)" 
			}); 

		} 

	}

	$scope.generateSubtask_Pdf=function(){
		var doc1 = new jsPDF('p', 'pt', 'a4');
		var res1 = doc1.autoTableHtmlToJson(document.getElementById("example1"));
		doc1.autoTable(res1.columns, res1.data, {
			startY: 10,
			styles: {
				overflow: 'linebreak',
				fontSize: 5,
			},
			columnStyles: {
				1: {columnWidth: 'auto'}
			}
		});		
		doc1.setProperties({
			title: 'Compliance Management Tool Report',
			subject: 'Report',
			author: 'Lexcare',
			keywords: 'lexcare compliance tool',
			creator: 'Compliance Management Tool'

		});
		doc1.cellInitialize();
		doc1.save('Sub_Task_DrilledReport.pdf');	
	}


	// to generate data in CSV format
	$scope.ExportExcels_Subtask=function(){
		/*var Curr_Date = new Date();
		var Curr_date1= moment(Curr_Date).format('DD-MM-YYYY');*/
		//$('#reportList1').tableExport({type:'excel',escape:'false'});		
		$("#example1 tr td a").removeAttr("href");
		$("#example1 tr td a").css("color","black");
		$("#example1").table2excel({		
			name: "Report",
			filename: "DrilledReport_(Sub Task)" 
		}); 
	}


	//get task history
	/*$scope.getTaskHistoryList=function(task_id){
		$state.transitionTo('taskDetails', {'task_id':task_id});
	};*/

//	onchange of financial year 
	$scope.getFincialGraphByYear = function(){
		var year = $scope.dashboardObj.fyear;
		//alert(year);
		var orga_list = [];
		var orga_name_list = [];
		var loca_list = [];
		var loca_name_list = [];
		var dept_list = [];


		/* Overall Graph*/
		$scope.overAllGraph = {};
		$scope.overAllGraph.Complied = [];
		$scope.overAllGraph.NonComplied = [];
		$scope.overAllGraph.PosingRisk = [];
		$scope.overAllGraph.Delayed = [];
		$scope.overAllGraph.WaitingForApproval = [];
		$scope.overAllGraph.ReOpened = [];
		$scope.overAllGraph.Delayed_Reported = [];

		/*End*/

		$scope.entityGraph = {};
		$scope.entityGraph.Entity = [];
		$scope.entityGraph.Complied = [];
		$scope.entityGraph.NonComplied = [];
		$scope.entityGraph.PosingRisk = [];
		$scope.entityGraph.Delayed = [];
		$scope.entityGraph.WaitingForApproval = [];
		$scope.entityGraph.ReOpened = [];
		$scope.entityGraph.Delayed_Reported = [];

		$scope.unitGraph = {};
		$scope.unitGraph.Unit = [];
		$scope.unitGraph.Complied = [];
		$scope.unitGraph.NonComplied = [];
		$scope.unitGraph.PosingRisk = [];
		$scope.unitGraph.Delayed = [];
		$scope.unitGraph.WaitingForApproval = [];
		$scope.unitGraph.ReOpened = [];
		$scope.unitGraph.Delayed_Reported = [];

		$scope.functionGraph = {};
		$scope.functionGraph.Function = [];
		$scope.functionGraph.Complied = [];
		$scope.functionGraph.NonComplied = [];
		$scope.functionGraph.PosingRisk = [];
		$scope.functionGraph.Delayed = [];
		$scope.functionGraph.WaitingForApproval = [];
		$scope.functionGraph.ReOpened = [];
		$scope.functionGraph.Delayed_Reported = [];

		$scope.categoryWiseGraph = {};
		$scope.categoryWiseGraph.Complied = [];
		$scope.categoryWiseGraph.NonComplied = [];
		$scope.categoryWiseGraph.PosingRisk = [];
		$scope.categoryWiseGraph.Delayed = [];
		$scope.categoryWiseGraph.WaitingForApproval = [];
		$scope.categoryWiseGraph.ReOpened = [];
		$scope.categoryWiseGraph.Delayed_Reported = [];

		$scope.financialGraph = {};
		$scope.financialGraph.Month = [];
		$scope.financialGraph.Quarter = [];
		$scope.financialGraph.Complied = [];
		$scope.financialGraph.NonComplied = [];
		$scope.financialGraph.PosingRisk = [];
		$scope.financialGraph.Delayed = [];
		$scope.financialGraph.WaitingForApproval = [];
		$scope.financialGraph.ReOpened = [];
		$scope.financialGraph.Delayed_Reported = [];


		if ($scope.dashboardObj.fmonth == 1) {
			$scope.financialGraph.Month.push("April");
			$scope.financialGraph.Month.push("May");
			$scope.financialGraph.Month.push("June");

			month[3] = "April";
			month[4] = "May"; 
			month[5] = "June";

		}else if ($scope.dashboardObj.fmonth == 2) {
			$scope.financialGraph.Month.push("July");
			$scope.financialGraph.Month.push("August");
			$scope.financialGraph.Month.push("September");

			month[6] = "July";
			month[7] = "August";
			month[8] = "September";
		}
		else if ($scope.dashboardObj.fmonth == 3) {
			$scope.financialGraph.Month.push("October");
			$scope.financialGraph.Month.push("November");
			$scope.financialGraph.Month.push("December");

			month[9] = "October";
			month[10] = "November";
			month[11] = "December";
		}
		else if ($scope.dashboardObj.fmonth == 4) {
			$scope.financialGraph.Month.push("January");
			$scope.financialGraph.Month.push("February");
			$scope.financialGraph.Month.push("March");

			month[0] = "January";
			month[1] = "February";
			month[2] = "March";

		}else if ($scope.dashboardObj.fmonth == 0) {
			$scope.financialGraph.Quarter.push("Quarter 1");

			month[3] = "April";
			month[4] = "May";
			month[5] = "June";

			$scope.financialGraph.Quarter.push("Quarter 2");

			month[6] = "July";
			month[7] = "August";
			month[8] = "September";

			$scope.financialGraph.Quarter.push("Quarter 3");

			month[9] = "October";
			month[10] = "November";
			month[11] = "December";

			$scope.financialGraph.Quarter.push("Quarter 4");

			month[0] = "January";
			month[1] = "February";
			month[2] = "March";
		}

		angular.forEach($scope.financialGraph.Quarter, function (value, key) {
			//alert(value.length);
			/*Financial graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			var fdelayed_reported = 0;
			var quarter;
			/*Financial graph */

			// var curr_date = new Date();

			// var curr_year = curr_date.getFullYear();
			if(value === "Quarter 1"){
				quarter = ["April","May","June"];
			}else if(value === "Quarter 2"){
				quarter = ["July","August","September"];
			}else if(value === "Quarter 3"){
				quarter = ["October","November","December"];
			}else if(value === "Quarter 4"){
				quarter = ["January","February","March"];
			}

			var next_year = year + 1;

			angular.forEach($scope.taskList, function (data) {
				var legal_Date = data.ttrn_legal_due_date.split("-");

				var date_month = parseInt(legal_Date[1]) - 1;
				var a = date_month - 1;

				var date_lag = new Date(legal_Date[2], legal_Date[1] - 1, legal_Date[0]);
				// console.log(date_lag);

				if ((legal_Date[2] >= year && legal_Date[2] <= next_year) && ((legal_Date[2] == year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3))) {
					for(var i = 0;i<quarter.length;i++){					
						if (month[date_lag.getMonth()] === quarter[i]) {
							if (data.status == 'complied') {
								fcomplied++;
							}

							if (data.status == 'noncomplied') {
								fnoncomplied++;
							}

							if (data.status == 'posingrisk') {
								fposingrisk++;
							}

							if (data.status == 'delayed') {
								fdelayed++;
							}

							if (data.status == 'waitingforapproval') {
								fpartially_Completed++;
							}
							if (data.status == 'reopen') {
								fre_opened++;
							}
							if (data.status == 'delayed-reported') {
								fdelayed_reported++;
							}
						}
					}

				}

			});
			//alert(fcomplied);

			$scope.financialGraph.Complied.push(fcomplied);
			$scope.financialGraph.NonComplied.push(fnoncomplied);
			$scope.financialGraph.PosingRisk.push(fposingrisk);
			$scope.financialGraph.Delayed.push(fdelayed);
			$scope.financialGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.financialGraph.ReOpened.push(fre_opened);
			$scope.financialGraph.Delayed_Reported.push(fdelayed_reported);


		});

		angular.forEach($scope.financialGraph.Month, function (value, key) {

			var year = $scope.dashboardObj.fyear;

			/*Financial graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			var  fdelayed_reported = 0;
			/*Financial graph */

			// var curr_date = new Date();

			// var curr_year = curr_date.getFullYear();
			var next_year = year + 1;

			angular.forEach($scope.taskList, function (data) {
				var legal_Date = data.ttrn_legal_due_date.split("-");

				var date_month = parseInt(legal_Date[1]) - 1;
				var a = date_month - 1;

				var date_lag = new Date(legal_Date[2], legal_Date[1] - 1, legal_Date[0]);
				// console.log(date_lag);

				if ((legal_Date[2] >= year && legal_Date[2] <= next_year) && ((legal_Date[2] == year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3))) {
					if (month[date_lag.getMonth()] === value) {
						if (data.status == 'complied') {
							fcomplied++;
						}

						if (data.status == 'noncomplied') {
							fnoncomplied++;
						}

						if (data.status == 'posingrisk') {
							fposingrisk++;
						}

						if (data.status == 'delayed') {
							fdelayed++;
						}

						if (data.status == 'waitingforapproval') {
							fpartially_Completed++;
						}
						if (data.status == 'reopen') {
							fre_opened++;
						}
						if (data.status == 'delayed-reported') {
							fdelayed_reported++;
						}
					}

				}

			});
			//alert(fcomplied);
			$scope.financialGraph.Complied.push(fcomplied);
			$scope.financialGraph.NonComplied.push(fnoncomplied);
			$scope.financialGraph.PosingRisk.push(fposingrisk);
			$scope.financialGraph.Delayed.push(fdelayed);
			$scope.financialGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.financialGraph.ReOpened.push(fre_opened);
			$scope.financialGraph.Delayed_Reported.push(fdelayed_reported);


		});

		if($scope.dashboardObj.fmonth == 0){
			$scope.loadFinanceGraphByQuarter($scope.financialGraph);
		}else{
			$scope.loadFinanceGraph($scope.financialGraph);
		}

		angular.forEach($scope.taskList, function (data) {
			var id_orga = data.orga_id;
			var id_loca = data.loca_id;
			var id_dept = data.dept_id;
			var id_category = data.task_loca_category;

			if (orga_list.indexOf(id_orga) === -1) {
				orga_list.push(id_orga);
				$scope.entityGraph.Entity.push(data.orga_name); // Add organization Name
			}
			if (loca_list.indexOf(id_loca) === -1) {
				loca_list.push(id_loca);
				$scope.unitGraph.Unit.push(data.loca_name);
			}

			if (dept_list.indexOf(id_dept) === -1) {
				dept_list.push(id_dept);
				$scope.functionGraph.Function.push(data.dept_name);
			}


		});//End task list foreach

		/*OverAll */
		var ocomplied = 0;
		var ononcomplied = 0;
		var oposingrisk = 0;
		var odelayed = 0;
		var opending = 0;
		var opartially_Completed = 0;
		var ore_opened = 0;
		var odelayed_reported = 0;
		/*OverAll */


		//OverAll and Entity Wise graph Count
		for (var i = 0; i < orga_list.length; i++) {

			/*Entity */
			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;
			/*Entity */

			angular.forEach($scope.taskList, function (data) {

				if (orga_list[i] == data.orga_id) {//Orga Id matched
					if (data.status == 'complied') {
						complied++;
						ocomplied++;
					}

					if (data.status == 'noncomplied') {
						noncomplied++;
						ononcomplied++;
					}

					if (data.status == 'posingrisk') {
						posingrisk++;
						oposingrisk++;
					}

					if (data.status == 'delayed') {
						delayed++;
						odelayed++;
					}

					if (data.status == 'waitingforapproval') {
						partially_Completed++;
						opartially_Completed++;
					}
					if (data.status == 'reopen') {
						re_opened++;
						ore_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
						odelayed_reported++;
					}


				}//End IF Orga Id matched


			});
			//	alert("entity wise:" +complied);
			$scope.entityGraph.Complied.push(complied);
			$scope.entityGraph.NonComplied.push(noncomplied);
			$scope.entityGraph.PosingRisk.push(posingrisk);
			$scope.entityGraph.Delayed.push(delayed);
			$scope.entityGraph.WaitingForApproval.push(partially_Completed);
			$scope.entityGraph.ReOpened.push(re_opened);
			$scope.entityGraph.Delayed_Reported.push(delayed_reported);

		}//End OverAll and Entity Wise graph Count

		//console.log("Entity JSON "+JSON.stringify($scope.entityGraph));
		//alert(ocomplied);
		$scope.overAllGraph.Complied = ocomplied;;
		$scope.overAllGraph.NonComplied = ononcomplied;
		$scope.overAllGraph.PosingRisk = oposingrisk;
		$scope.overAllGraph.Delayed = odelayed;
		$scope.overAllGraph.WaitingForApproval = opartially_Completed;
		$scope.overAllGraph.ReOpened = re_opened;
		$scope.overAllGraph.Delayed_Reported = odelayed_reported;

		$("#entityChart").show();
		$scope.loadEntityGraph($scope.entityGraph);//Pass data to entity search function
		//$scope.loadOverAllGraph($scope.overAllGraph);//pass Searched data overall funtion
		//End Search OverAll and Entity graph

		//Unit Wise graph Count
		for (var i = 0; i < loca_list.length; i++) {

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;

			angular.forEach($scope.taskList, function (data) {

				if (loca_list[i] == data.loca_id) {
					if (data.status == 'complied') {
						complied++;
					}

					if (data.status == 'noncomplied') {
						noncomplied++;
					}

					if (data.status == 'posingrisk') {
						posingrisk++;
					}

					if (data.status == 'delayed') {
						delayed++;
					}

					if (data.status == 'waitingforapproval') {
						partially_Completed++;
					}
					if (data.status == 'reopen') {
						re_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
					}
				}//End IF 


			});

			$scope.unitGraph.Complied.push(complied);
			$scope.unitGraph.NonComplied.push(noncomplied);
			$scope.unitGraph.PosingRisk.push(posingrisk);
			$scope.unitGraph.Delayed.push(delayed);
			$scope.unitGraph.WaitingForApproval.push(partially_Completed);
			$scope.unitGraph.ReOpened.push(re_opened);
			$scope.unitGraph.Delayed_Reported.push(delayed_reported);
			$("#locGraph").show();
			$scope.loadUnitGraph($scope.unitGraph);
		}
//		End unit Wise graph Count

//		Dept Wise graph Count
		for (var i = 0; i < dept_list.length; i++) {

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;

			angular.forEach($scope.taskList, function (data) {
				if (dept_list[i] == data.dept_id) {
					if (data.status == 'complied') {
						complied++;
					}

					if (data.status == 'noncomplied') {
						noncomplied++;
					}

					if (data.status == 'posingrisk') {
						posingrisk++;
					}

					if (data.status == 'delayed') {
						delayed++;
					}

					if (data.status == 'waitingforapproval') {
						partially_Completed++;
					}
					if (data.status == 'reopen') {
						re_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
					}
				}//End IF 


			});

			$scope.functionGraph.Complied.push(complied);
			$scope.functionGraph.NonComplied.push(noncomplied);
			$scope.functionGraph.PosingRisk.push(posingrisk);
			$scope.functionGraph.Delayed.push(delayed);
			$scope.functionGraph.WaitingForApproval.push(partially_Completed);
			$scope.functionGraph.ReOpened.push(re_opened);
			$scope.functionGraph.Delayed_Reported.push(delayed_reported);
			$("#deptGraph").show();
			$scope.loadFunctionGraph($scope.functionGraph);

		} // End unit Wise graph Count
	}

	$scope.getFincialGraphByMonth = function(){

		// alert("hii");
		$scope.financialGraph = {};		
		$scope.financialGraph.Month = [];
		$scope.financialGraph.Quarter = [];
		$scope.financialGraph.Complied = [];
		$scope.financialGraph.NonComplied = [];
		$scope.financialGraph.PosingRisk = [];
		$scope.financialGraph.Delayed = [];
		$scope.financialGraph.WaitingForApproval = [];
		$scope.financialGraph.ReOpened = [];
		$scope.financialGraph.Delayed_Reported = [];

		var month = new Array();

		//alert($scope.dashboardObj.fmonth);
		if ($scope.dashboardObj.fmonth == 1) {
			$scope.financialGraph.Month.push("April");
			$scope.financialGraph.Month.push("May");
			$scope.financialGraph.Month.push("June");

			month[3] = "April";
			month[4] = "May"; 
			month[5] = "June";
		}
		else if ($scope.dashboardObj.fmonth == 2) {
			$scope.financialGraph.Month.push("July");
			$scope.financialGraph.Month.push("August");
			$scope.financialGraph.Month.push("September");

			month[6] = "July";
			month[7] = "August";
			month[8] = "September";
		}
		else if ($scope.dashboardObj.fmonth == 3) {
			$scope.financialGraph.Month.push("October");
			$scope.financialGraph.Month.push("November");
			$scope.financialGraph.Month.push("December");

			month[9] = "October";
			month[10] = "November";
			month[11] = "December";
		}
		else if ($scope.dashboardObj.fmonth == 4) {
			$scope.financialGraph.Month.push("January");
			$scope.financialGraph.Month.push("February");
			$scope.financialGraph.Month.push("March");

			month[0] = "January";
			month[1] = "February";
			month[2] = "March";
		}
		else if ($scope.dashboardObj.fmonth == 0) {
			$scope.financialGraph.Quarter.push("Quarter 1");

			month[3] = "April";
			month[4] = "May";
			month[5] = "June";
			$scope.financialGraph.Quarter.push("Quarter 2");

			month[6] = "July";
			month[7] = "August";
			month[8] = "September";
			$scope.financialGraph.Quarter.push("Quarter 3");

			month[9] = "October";
			month[10] = "November";
			month[11] = "December";
			$scope.financialGraph.Quarter.push("Quarter 4");

			month[0] = "January";
			month[1] = "February";
			month[2] = "March";

		}	

		angular.forEach($scope.financialGraph.Month, function (value, key) {

			var year = $scope.dashboardObj.fyear;
			//alert(year);
			/*Financial graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			var fdelayed_reported = 0;
			/*Financial graph */

			// var curr_date = new Date();

			// var curr_year = curr_date.getFullYear();
			var next_year = year + 1;

			angular.forEach($scope.taskList, function (data) {
				var legal_Date = data.ttrn_legal_due_date.split("-");

				var date_month = parseInt(legal_Date[1]) - 1;
				var a = date_month - 1;

				var date_lag = new Date(legal_Date[2], legal_Date[1] - 1, legal_Date[0]);
				// console.log(date_lag);
				if($scope.dashboardObj.fmonth == 1 || $scope.dashboardObj.fmonth == 2 || $scope.dashboardObj.fmonth == 3)
					if ((legal_Date[2] >= year && legal_Date[2] < next_year) && ((legal_Date[2] == year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3))) {
						if (month[date_lag.getMonth()] === value) {
							if (data.status == 'complied') {
								fcomplied++;
							}

							if (data.status == 'noncomplied') {
								fnoncomplied++;
							}

							if (data.status == 'posingrisk') {
								fposingrisk++;
							}

							if (data.status == 'delayed') {
								fdelayed++;
							}

							if (data.status == 'waitingforapproval') {
								fpartially_Completed++;
							}
							if (data.status == 'reopen') {
								fre_opened++;
							}
							if (data.status == 'delayed-reported') {
								fdelayed_reported++;
							}
						}

					}


				if($scope.dashboardObj.fmonth == 4){
					if ((legal_Date[2] >= year && legal_Date[2] <= next_year) && ((legal_Date[2] == year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3))) {
						if (month[date_lag.getMonth()] === value) {
							if (data.status == 'complied') {
								fcomplied++;
							}

							if (data.status == 'noncomplied') {
								fnoncomplied++;
							}

							if (data.status == 'posingrisk') {
								fposingrisk++;
							}

							if (data.status == 'delayed') {
								fdelayed++;
							}

							if (data.status == 'waitingforapproval') {
								fpartially_Completed++;
							}
							if (data.status == 'reopen') {
								fre_opened++;
							}
							if (data.status == 'delayed-reported') {
								fdelayed_reported++;
							}
						}

					}

				}

			});
			//alert(fcomplied);
			$scope.financialGraph.Complied.push(fcomplied);
			$scope.financialGraph.NonComplied.push(fnoncomplied);
			$scope.financialGraph.PosingRisk.push(fposingrisk);
			$scope.financialGraph.Delayed.push(fdelayed);
			$scope.financialGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.financialGraph.ReOpened.push(fre_opened);
			$scope.financialGraph.Delayed_Reported.push(fdelayed_reported);


		});

		//$scope.loadFinanceGraph($scope.financialGraph);

		angular.forEach($scope.financialGraph.Quarter, function (value, key) {
			//alert(value);
			/*Financial graph */
			var year = $scope.dashboardObj.fyear;
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fdelayed_reported = 0;
			var fre_opened = 0;
			var quarter;
			/*Financial graph */

			// var curr_date = new Date();

			// var curr_year = curr_date.getFullYear();
			if(value === "Quarter 1"){
				quarter = ["April","May","June"];
			}else if(value === "Quarter 2"){
				quarter = ["July","August","September"];
			}else if(value === "Quarter 3"){
				quarter = ["October","November","December"];
			}else if(value === "Quarter 4"){
				quarter = ["January","February","March"];
			}

			var next_year = year + 1;
			//alert(quarter.length);
			angular.forEach($scope.taskList, function (data) {
				var legal_Date = data.ttrn_legal_due_date.split("-");

				var date_month = parseInt(legal_Date[1]) - 1;
				var a = date_month - 1;

				var date_lag = new Date(legal_Date[2], legal_Date[1] - 1, legal_Date[0]);
				// console.log(date_lag);
				if(value === "Quarter 1" || value === "Quarter 2" || value === "Quarter 3") {
					if ((legal_Date[2] >= year && legal_Date[2] < next_year) && ((legal_Date[2] == year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3))) {

						for(var i = 0;i < quarter.length;i++){					
							if (month[date_lag.getMonth()] === quarter[i]) {	

								if (data.status == 'complied') {
									fcomplied++;
								}

								if (data.status == 'noncomplied') {
									fnoncomplied++;
								}

								if (data.status == 'posingrisk') {
									fposingrisk++;
								}

								if (data.status == 'delayed') {
									fdelayed++;
								}

								if (data.status == 'waitingforapproval') {
									fpartially_Completed++;
								}
								if (data.status == 'reopen') {
									fre_opened++;
								}
								if (data.status == 'delayed-reported') {
									fdelayed_reported++;
								}
							}
						}
					}
				}

				if(value === "Quarter 4") {
					if ((legal_Date[2] >= year && legal_Date[2] <= next_year) && ((legal_Date[2] == year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3))) {

						for(var i = 0;i < quarter.length;i++){					
							if (month[date_lag.getMonth()] === quarter[i]) {	

								if (data.status == 'complied') {
									fcomplied++;
								}

								if (data.status == 'noncomplied') {
									fnoncomplied++;
								}

								if (data.status == 'posingrisk') {
									fposingrisk++;
								}

								if (data.status == 'delayed') {
									fdelayed++;
								}

								if (data.status == 'waitingforapproval') {
									fpartially_Completed++;
								}
								if (data.status == 'reopen') {
									fre_opened++;
								}
								if (data.status == 'delayed-reported') {
									fdelayed_reported++;
								}
							}
						}
					}

				}

			});

			$scope.financialGraph.Complied.push(fcomplied);
			$scope.financialGraph.NonComplied.push(fnoncomplied);
			$scope.financialGraph.PosingRisk.push(fposingrisk);
			$scope.financialGraph.Delayed.push(fdelayed);
			$scope.financialGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.financialGraph.ReOpened.push(fre_opened);
			$scope.financialGraph.Delayed_Reported.push(fdelayed_reported);
		});

		if($scope.dashboardObj.fmonth == 0){
			$scope.loadFinanceGraphByQuarter($scope.financialGraph);
		}else{
			$scope.loadFinanceGraph($scope.financialGraph);
		}
	}


	$scope.getFincialGraphByYear = function(){
		var year = $scope.dashboardObj.fyear;
		//alert(year);
		var orga_list = [];
		var orga_name_list = [];
		var loca_list = [];
		var loca_name_list = [];
		var dept_list = [];


		/* Overall Graph*/
		$scope.overAllGraph = {};
		$scope.overAllGraph.Complied = [];
		$scope.overAllGraph.NonComplied = [];
		$scope.overAllGraph.PosingRisk = [];
		$scope.overAllGraph.Delayed = [];
		$scope.overAllGraph.WaitingForApproval = [];
		$scope.overAllGraph.ReOpened = [];
		$scope.overAllGraph.Delayed_Reported = [];

		/*End*/

		$scope.entityGraph = {};
		$scope.entityGraph.Entity = [];
		$scope.entityGraph.Complied = [];
		$scope.entityGraph.NonComplied = [];
		$scope.entityGraph.PosingRisk = [];
		$scope.entityGraph.Delayed = [];
		$scope.entityGraph.WaitingForApproval = [];
		$scope.entityGraph.ReOpened = [];
		$scope.entityGraph.Delayed_Reported = [];

		$scope.unitGraph = {};
		$scope.unitGraph.Unit = [];
		$scope.unitGraph.Complied = [];
		$scope.unitGraph.NonComplied = [];
		$scope.unitGraph.PosingRisk = [];
		$scope.unitGraph.Delayed = [];
		$scope.unitGraph.WaitingForApproval = [];
		$scope.unitGraph.ReOpened = [];
		$scope.unitGraph.Delayed_Reported = [];

		$scope.functionGraph = {};
		$scope.functionGraph.Function = [];
		$scope.functionGraph.Complied = [];
		$scope.functionGraph.NonComplied = [];
		$scope.functionGraph.PosingRisk = [];
		$scope.functionGraph.Delayed = [];
		$scope.functionGraph.WaitingForApproval = [];
		$scope.functionGraph.ReOpened = [];
		$scope.functionGraph.Delayed_Reported = [];

		$scope.categoryWiseGraph = {};
		$scope.categoryWiseGraph.Complied = [];
		$scope.categoryWiseGraph.NonComplied = [];
		$scope.categoryWiseGraph.PosingRisk = [];
		$scope.categoryWiseGraph.Delayed = [];
		$scope.categoryWiseGraph.WaitingForApproval = [];
		$scope.categoryWiseGraph.ReOpened = [];
		$scope.categoryWiseGraph.Delayed_Reported = [];

		$scope.financialGraph = {};
		$scope.financialGraph.Month = [];
		$scope.financialGraph.Quarter = [];
		$scope.financialGraph.Complied = [];
		$scope.financialGraph.NonComplied = [];
		$scope.financialGraph.PosingRisk = [];
		$scope.financialGraph.Delayed = [];
		$scope.financialGraph.WaitingForApproval = [];
		$scope.financialGraph.ReOpened = [];
		$scope.financialGraph.Delayed_Reported = [];


		if ($scope.dashboardObj.fmonth == 1) {
			$scope.financialGraph.Month.push("April");
			$scope.financialGraph.Month.push("May");
			$scope.financialGraph.Month.push("June");

			month[3] = "April";
			month[4] = "May"; 
			month[5] = "June";

		}else if ($scope.dashboardObj.fmonth == 2) {
			$scope.financialGraph.Month.push("July");
			$scope.financialGraph.Month.push("August");
			$scope.financialGraph.Month.push("September");

			month[6] = "July";
			month[7] = "August";
			month[8] = "September";
		}
		else if ($scope.dashboardObj.fmonth == 3) {
			$scope.financialGraph.Month.push("October");
			$scope.financialGraph.Month.push("November");
			$scope.financialGraph.Month.push("December");

			month[9] = "October";
			month[10] = "November";
			month[11] = "December";
		}
		else if ($scope.dashboardObj.fmonth == 4) {
			$scope.financialGraph.Month.push("January");
			$scope.financialGraph.Month.push("February");
			$scope.financialGraph.Month.push("March");

			month[0] = "January";
			month[1] = "February";
			month[2] = "March";

		}else if ($scope.dashboardObj.fmonth == 0) {
			$scope.financialGraph.Quarter.push("Quarter 1");

			month[3] = "April";
			month[4] = "May";
			month[5] = "June";

			$scope.financialGraph.Quarter.push("Quarter 2");

			month[6] = "July";
			month[7] = "August";
			month[8] = "September";

			$scope.financialGraph.Quarter.push("Quarter 3");

			month[9] = "October";
			month[10] = "November";
			month[11] = "December";

			$scope.financialGraph.Quarter.push("Quarter 4");

			month[0] = "January";
			month[1] = "February";
			month[2] = "March";
		}

		angular.forEach($scope.financialGraph.Quarter, function (value, key) {
			//alert(value.length);
			/*Financial graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			var fdelayed_reported = 0;
			var quarter;
			/*Financial graph */

			// var curr_date = new Date();

			// var curr_year = curr_date.getFullYear();
			if(value === "Quarter 1"){
				quarter = ["April","May","June"];
			}else if(value === "Quarter 2"){
				quarter = ["July","August","September"];
			}else if(value === "Quarter 3"){
				quarter = ["October","November","December"];
			}else if(value === "Quarter 4"){
				quarter = ["January","February","March"];
			}

			var next_year = year + 1;

			angular.forEach($scope.taskList, function (data) {
				var legal_Date = data.ttrn_legal_due_date.split("-");

				var date_month = parseInt(legal_Date[1]) - 1;
				var a = date_month - 1;

				var date_lag = new Date(legal_Date[2], legal_Date[1] - 1, legal_Date[0]);
				// console.log(date_lag);

				if ((legal_Date[2] >= year && legal_Date[2] <= next_year) && ((legal_Date[2] == year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3))) {
					for(var i = 0;i<quarter.length;i++){					
						if (month[date_lag.getMonth()] === quarter[i]) {
							if (data.status == 'complied') {
								fcomplied++;
							}

							if (data.status == 'noncomplied') {
								fnoncomplied++;
							}

							if (data.status == 'posingrisk') {
								fposingrisk++;
							}

							if (data.status == 'delayed') {
								fdelayed++;
							}

							if (data.status == 'waitingforapproval') {
								fpartially_Completed++;
							}
							if (data.status == 'reopen') {
								fre_opened++;
							}
							if (data.status == 'delayed-reported') {
								fdelayed_reported++;
							}
						}
					}

				}

			});
			//alert(fcomplied);

			$scope.financialGraph.Complied.push(fcomplied);
			$scope.financialGraph.NonComplied.push(fnoncomplied);
			$scope.financialGraph.PosingRisk.push(fposingrisk);
			$scope.financialGraph.Delayed.push(fdelayed);
			$scope.financialGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.financialGraph.ReOpened.push(fre_opened);
			$scope.financialGraph.Delayed_Reported.push(fdelayed_reported);


		});

		angular.forEach($scope.financialGraph.Month, function (value, key) {

			var year = $scope.dashboardObj.fyear;

			/*Financial graph */
			var fcomplied = 0;
			var fnoncomplied = 0;
			var fposingrisk = 0;
			var fdelayed = 0;
			var fpending = 0;
			var fpartially_Completed = 0;
			var fre_opened = 0;
			var  fdelayed_reported = 0;
			/*Financial graph */

			// var curr_date = new Date();

			// var curr_year = curr_date.getFullYear();
			var next_year = year + 1;

			angular.forEach($scope.taskList, function (data) {
				var legal_Date = data.ttrn_legal_due_date.split("-");

				var date_month = parseInt(legal_Date[1]) - 1;
				var a = date_month - 1;

				var date_lag = new Date(legal_Date[2], legal_Date[1] - 1, legal_Date[0]);
				// console.log(date_lag);

				if ((legal_Date[2] >= year && legal_Date[2] <= next_year) && ((legal_Date[2] == year && date_lag.getMonth() >= 3) || (legal_Date[2] == next_year && date_lag.getMonth() <= 3))) {
					if (month[date_lag.getMonth()] === value) {
						if (data.status == 'complied') {
							fcomplied++;
						}

						if (data.status == 'noncomplied') {
							fnoncomplied++;
						}

						if (data.status == 'posingrisk') {
							fposingrisk++;
						}

						if (data.status == 'delayed') {
							fdelayed++;
						}

						if (data.status == 'waitingforapproval') {
							fpartially_Completed++;
						}
						if (data.status == 'reopen') {
							fre_opened++;
						}
						if (data.status == 'delayed-reported') {
							fdelayed_reported++;
						}
					}

				}

			});
			//alert(fcomplied);
			$scope.financialGraph.Complied.push(fcomplied);
			$scope.financialGraph.NonComplied.push(fnoncomplied);
			$scope.financialGraph.PosingRisk.push(fposingrisk);
			$scope.financialGraph.Delayed.push(fdelayed);
			$scope.financialGraph.WaitingForApproval.push(fpartially_Completed);
			$scope.financialGraph.ReOpened.push(fre_opened);
			$scope.financialGraph.Delayed_Reported.push(fdelayed_reported);


		});

		if($scope.dashboardObj.fmonth == 0){
			$scope.loadFinanceGraphByQuarter($scope.financialGraph);
		}else{
			$scope.loadFinanceGraph($scope.financialGraph);
		}

		angular.forEach($scope.taskList, function (data) {
			var id_orga = data.orga_id;
			var id_loca = data.loca_id;
			var id_dept = data.dept_id;
			var id_category = data.task_loca_category;

			if (orga_list.indexOf(id_orga) === -1) {
				orga_list.push(id_orga);
				$scope.entityGraph.Entity.push(data.orga_name); // Add organization Name
			}
			if (loca_list.indexOf(id_loca) === -1) {
				loca_list.push(id_loca);
				$scope.unitGraph.Unit.push(data.loca_name);
			}

			if (dept_list.indexOf(id_dept) === -1) {
				dept_list.push(id_dept);
				$scope.functionGraph.Function.push(data.dept_name);
			}


		});//End task list foreach

		/*OverAll */
		var ocomplied = 0;
		var ononcomplied = 0;
		var oposingrisk = 0;
		var odelayed = 0;
		var opending = 0;
		var opartially_Completed = 0;
		var ore_opened = 0;
		var odelayed_reported = 0;
		/*OverAll */


		//OverAll and Entity Wise graph Count
		for (var i = 0; i < orga_list.length; i++) {

			/*Entity */
			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;
			/*Entity */

			angular.forEach($scope.taskList, function (data) {

				if (orga_list[i] == data.orga_id) {//Orga Id matched
					if (data.status == 'complied') {
						complied++;
						ocomplied++;
					}

					if (data.status == 'noncomplied') {
						noncomplied++;
						ononcomplied++;
					}

					if (data.status == 'posingrisk') {
						posingrisk++;
						oposingrisk++;
					}

					if (data.status == 'delayed') {
						delayed++;
						odelayed++;
					}

					if (data.status == 'waitingforapproval') {
						partially_Completed++;
						opartially_Completed++;
					}
					if (data.status == 'reopen') {
						re_opened++;
						ore_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
						odelayed_reported++;
					}


				}//End IF Orga Id matched


			});
			//	alert("entity wise:" +complied);
			$scope.entityGraph.Complied.push(complied);
			$scope.entityGraph.NonComplied.push(noncomplied);
			$scope.entityGraph.PosingRisk.push(posingrisk);
			$scope.entityGraph.Delayed.push(delayed);
			$scope.entityGraph.WaitingForApproval.push(partially_Completed);
			$scope.entityGraph.ReOpened.push(re_opened);
			$scope.entityGraph.Delayed_Reported.push(delayed_reported);

		}//End OverAll and Entity Wise graph Count

		//console.log("Entity JSON "+JSON.stringify($scope.entityGraph));
		//alert(ocomplied);
		$scope.overAllGraph.Complied = ocomplied;;
		$scope.overAllGraph.NonComplied = ononcomplied;
		$scope.overAllGraph.PosingRisk = oposingrisk;
		$scope.overAllGraph.Delayed = odelayed;
		$scope.overAllGraph.WaitingForApproval = opartially_Completed;
		$scope.overAllGraph.ReOpened = re_opened;
		$scope.overAllGraph.Delayed_Reported = odelayed_reported;

		$("#entityChart").show();
		$scope.loadEntityGraph($scope.entityGraph);//Pass data to entity search function
		//$scope.loadOverAllGraph($scope.overAllGraph);//pass Searched data overall funtion
		//End Search OverAll and Entity graph

		//Unit Wise graph Count
		for (var i = 0; i < loca_list.length; i++) {

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;

			angular.forEach($scope.taskList, function (data) {

				if (loca_list[i] == data.loca_id) {
					if (data.status == 'complied') {
						complied++;
					}

					if (data.status == 'noncomplied') {
						noncomplied++;
					}

					if (data.status == 'posingrisk') {
						posingrisk++;
					}

					if (data.status == 'delayed') {
						delayed++;
					}

					if (data.status == 'waitingforapproval') {
						partially_Completed++;
					}
					if (data.status == 'reopen') {
						re_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
					}
				}//End IF 


			});

			$scope.unitGraph.Complied.push(complied);
			$scope.unitGraph.NonComplied.push(noncomplied);
			$scope.unitGraph.PosingRisk.push(posingrisk);
			$scope.unitGraph.Delayed.push(delayed);
			$scope.unitGraph.WaitingForApproval.push(partially_Completed);
			$scope.unitGraph.ReOpened.push(re_opened);
			$scope.unitGraph.Delayed_Reported.push(delayed_reported);
			$("#locGraph").show();
			$scope.loadUnitGraph($scope.unitGraph);
		}//End unit Wise graph Count

		//Dept Wise graph Count
		for (var i = 0; i < dept_list.length; i++) {

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;

			angular.forEach($scope.taskList, function (data) {
				if (dept_list[i] == data.dept_id) {
					if (data.status == 'complied') {
						complied++;
					}

					if (data.status == 'noncomplied') {
						noncomplied++;
					}

					if (data.status == 'posingrisk') {
						posingrisk++;
					}

					if (data.status == 'delayed') {
						delayed++;
					}

					if (data.status == 'waitingforapproval') {
						partially_Completed++;
					}
					if (data.status == 'reopen') {
						re_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
					}
				}//End IF 


			});

			$scope.functionGraph.Complied.push(complied);
			$scope.functionGraph.NonComplied.push(noncomplied);
			$scope.functionGraph.PosingRisk.push(posingrisk);
			$scope.functionGraph.Delayed.push(delayed);
			$scope.functionGraph.WaitingForApproval.push(partially_Completed);
			$scope.functionGraph.ReOpened.push(re_opened);
			$scope.functionGraph.Delayed_Reported.push(delayed_reported);
			$("#deptGraph").show();
			$scope.loadFunctionGraph($scope.functionGraph);

		}//End unit Wise graph Count
	}



	/**
	 * Testing grapg with click events
	 */

//	window.onload = function(e){
//	$scope.loadCountries();
//	}

	$('#countriesGraph').bind('dataPointMouseUp', function (e, data) {
		console.log(data.dataItem[0]);
		if(data.dataItem[0] == "Complied"){
			console.log('inside if condition');
			$scope.searchObj.chart_name = "entityLevelPieChart";
			$scope.searchObj.status = "Complied";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
		}else if(data.dataItem[0] == "Posing"){
			$scope.searchObj.chart_name = "entityLevelPieChart";
			$scope.searchObj.status = "PosingRisk";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
		}else if(data.dataItem[0] == "Non-Complied"){
			console.log('data y : ' + data.x);
			console.log('data.dataItem  : ' + data.dataItem);
			$scope.searchObj.chart_name = "entityLevelPieChart";
			$scope.searchObj.status = "NonComplied";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			console.log('$scope.searchObj : ' + $scope.searchObj);
			$scope.drilledReport($scope.searchObj);
		}else if(data.dataItem[0] == "Delayed"){
			$scope.searchObj.chart_name = "entityLevelPieChart";
			$scope.searchObj.status = "Delayed";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
		}else if(data.dataItem[0] == "Waiting For Approval"){
			$scope.searchObj.chart_name = "entityLevelPieChart";
			$scope.searchObj.status = "Waiting For Approval";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
		}else{
			$scope.searchObj.chart_name = "entityLevelPieChart";
			$scope.searchObj.status = "Re-Opened";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.drilledReport($scope.searchObj);
		}
	});

	$scope.loadCountries = function(data) {
		var NonComplied = 0;
		$scope.Entity = [];
		$scope.myData = data;
		// console.log('myData var : ' + $scope.myData.Entity[0]);
		// console.log('data nonComplied : ' + $scope.myData.NonComplied)

		var background = {
				type: 'linearGradient',
				x0: 0,
				y0: 0,
				x1: 0,
				y1: 1,
				colorStops: [{ offset: 0, color: '#d2e6c9' },
					{ offset: 1, color: 'white' }]
		};

		$('#countriesGraph').jqChart({
			title: { text: $scope.myData.Entity[0] },
			legend: { title: 'Task Status' },
			border: { strokeStyle: '#6ba851' },
			background: background,
			animation: { duration: 1 },
			shadows: {
				enabled: true
			},
			series: [
				{
					type: 'pie',
					// fillStyles: ['#03C523 ', '#fafaa4f0', '#FF0000 ', '#BFBFBF', '#85c1e9', '#f5b041'],
					fillStyles: ['#03C523 ', '#fafaa4f0', '#FF0000 ', '#BFBFBF', '#f5b041'],
					labels: {
						stringFormat: '%.1f%%',
						valueType: 'percentage',
						font: '15px sans-serif',
						fillStyle: 'white'
					},
					explodedRadius: 10,
					explodedSlices: [5],
					data: [
						['Complied', $scope.myData.Complied[0]], 
						['Posing', $scope.myData.PosingRisk[0]], 
						['Non-Complied', $scope.myData.NonComplied[0]],
						['Delayed', $scope.myData.Delayed[0]], 
						['Waiting For Approval', $scope.myData.WaitingForApproval[0]], 
						['Re-Opened', $scope.myData.ReOpened[0]]
						]
				}
				]
		});
	}


	/**
	 * End
	 */


	/**
	 * FinanceLevel Graph Example
	 */

	$scope.financeLevelGraphFun = function(data) {

		$scope.FinanceChartData = data;
		console.log($scope.FinanceChartData.Month);

		var background = {
				type : 'linearGradient',
				x0 : 0,
				y0 : 0,
				x1 : 0,
				y1 : 1,
				colorStops : [ {
					offset : 0,
					color : '#d2e6c9'
				}, {
					offset : 1,
					color : 'white'
				} ]
		};

		$scope.chart_widht = 1150;
		$scope.chart_height = 350;
		$scope.axisLocation='bottom';
		$scope.graphBarType='stacked100Column';
		$scope.labelLocation='left';

		$('#financeLevelGraphDiv').jqChart({

			orientation: 'horizontal',
			width: $scope.chart_widht,
			height: $scope.chart_height,
			legend : {
				customItems : [ {
					text : {
						text : 'Complied'
					},
					marker : {
						fillStyle : '#03C523 ',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Posing'
					},
					marker : {
						fillStyle : '#fafaa4f0',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Non-Complied'
					},
					marker : {
						fillStyle : '#FF0000',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#BFBFBF',
						type : 'circle'
					}
				}, 
				{
					text : {
						text : 'Delayed-Reported'
					},
					marker : {
						fillStyle : '#bfd630',
						type : 'circle'
					}
				}, /*{
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#85c1e9',
						type : 'circle'
					}
				},*/ {
					text : {
						text : 'Re-Opened'
					},
					marker : {
						fillStyle : '#f5b041',
						type : 'circle'
					}
				} ]
			},
			border : {
				strokeStyle : '#6ba851'
			},
			background : background,
			animation : {
				duration : 1
			},
			shadows : {
				enabled : true
			},
			axes : [ {
				type : 'category',
				location : $scope.axisLocation,
				categories : $scope.FinanceChartData.Month
			}, {
				type : 'linearGradient',
				location : $scope.labelLocation,
				minimum : 0,
				maximum : 100,
				interval : 10,
				labels : {
					stringFormat : '%.1f%%'
				}
			} ],
			series : [ {
				type : $scope.graphBarType,
				title : 'Complied',
				fillStyles : [ '#03C523 ' ],
				data : $scope.FinanceChartData.Complied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'PosingRisk',
				fillStyles : [ '#fafaa4f0' ],
				data : $scope.FinanceChartData.PosingRisk,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'NonComplied',
				fillStyles : [ '#FF0000' ],
				data :  $scope.FinanceChartData.NonComplied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed',
				fillStyles : [ '#BFBFBF' ],
				data : $scope.FinanceChartData.Delayed,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.FinanceChartData.Delayed_Reported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, /*{
				type : $scope.graphBarType,
				title : 'Waiting For Approval',
				fillStyles : [ '#85c1e9' ],
				data : $scope.FinanceChartData.WaitingForApproval,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},*/ {
				type : $scope.graphBarType,
				title : 'Re-Opened',
				fillStyles : [ '#f5b041' ],
				data : $scope.FinanceChartData.ReOpened,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}]
		});
	}

	$('#financeLevelGraphDiv').bind('dataPointMouseUp', function (e, data) {
		console.log(data.dataItem);
		console.log('data X : ' + data.x);
		console.log('Title : ' + data.series.title);
		$scope.drilledReport({'chart_name':'financeLevelGraphDiv','status':data.series.title,'entity':0,'unit':0,'month':data.x});
	});

	/**
	 * End
	 */

	$scope.count = 0;
	$scope.myFunc = function() {
		$scope.count++;
		var config = {
				fileName : 'financeLevel.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#financeLevel").jqChart('exportToImage', config);
	};

	$scope.exportEntityLevelPieChartGraph = function() {
		var config = {
				fileName : 'graph.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#entityLevelPieChart").jqChart('exportToImage', config);
	};

	$scope.exportEntityLevelPieChart1Graph = function() {
		var config = {
				fileName : 'graph.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#entityLevelPieChart1").jqChart('exportToImage', config);
	};

	$scope.exportEntityLevelPieChart2Graph = function() {
		var config = {
				fileName : 'graph.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#entityLevelPieChart2").jqChart('exportToImage', config);
	};

	$scope.exportEntityLevelPieChart3Graph = function() {
		var config = {
				fileName : 'graph.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#entityLevelPieChart3").jqChart('exportToImage', config);
	};

	$scope.exportEntityLevelPieChart4Graph = function() {
		var config = {
				fileName : 'graph.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#entityLevelPieChart4").jqChart('exportToImage', config);
	};

	$scope.exportEntityLevelGraphChart = function() {
		var config = {
				fileName : 'graph.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#entityLevel").jqChart('exportToImage', config);
	};

	//Export overall graph in image Formate
	$scope.exportOverAllGraphChart = function() {
		var config = {
				fileName : 'overAllGraph.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#jqChart1").jqChart('exportToImage', config);
	};

	$scope.exportLocationBarChart = function() {
		var config = {
				fileName : 'UnitBarChart.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#LocationBarChart").jqChart('exportToImage', config);
	};

	$scope.exportDepartmentBarChart = function() {
		var config = {
				fileName : 'FunctioBarChart.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#DepartmentBarChart").jqChart('exportToImage', config);
	};

	$scope.exportFinancialBarChart = function() {
		var config = {
				fileName : 'FinancialGraph.png',
				type : 'image/png' // 'image/png' or 'image/jpeg'
		}
		$("#financeLevel").jqChart('exportToImage', config);
	};

	$scope.loadFinanceGraphByQuarter = function(data){
		$scope.FinanceChartData = data;
		//alert(JSON.stringify($scope.FinanceChartData));
		//	alert($scope.FinanceChartData.Month.length);
		var background = {
				type: 'linearGradient',
				x0: 0,
				y0: 0,
				x1: 0,
				y1: 1,
				colorStops: [{
					offset: 0,
					color: '#d2e6c9'
				}, {
					offset: 1,
					color: 'white'
				}]
		};

		$scope.chart_widht = 1150;
		$scope.chart_height = 350;
		$scope.axisLocation = 'bottom';
		$scope.graphBarType = 'stacked100Column';
		$scope.labelLocation = 'left';
		//alert($scope.FinanceChartData.Month.length);
		if ($scope.FinanceChartData.Quarter.length > 12) {
			$scope.chart_widht = 1150;
			$scope.chart_height = 850;
			$scope.axisLocation = 'left';
			$scope.graphBarType = 'stacked100Bar';
			$scope.labelLocation = 'bottom';
		}

		$('#financeLevel').jqChart({
			orientation: 'horizontal',
			width: $scope.chart_widht,
			height: $scope.chart_height,

			legend: {
				customItems: [{
					text: {
						text: 'Complied'
					},
					marker: {
						fillStyle: '#00CC33',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Posing'
					},
					marker: {
						fillStyle: '#FFFF00',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Non-Complied'
					},
					marker: {
						fillStyle: '#FF0000',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Delayed'
					},
					marker: {
						fillStyle: '#BFBFBF',
						type: 'circle'
					}
				}, {
					text : {
						text : 'Delayed Reported'
					},
					marker : {
						fillStyle : '#bfd630',
						type : 'circle'
					}
				} ]
			},
			border: {
				strokeStyle: '#6ba851'
			},
			background: background,
			animation: {
				duration: 1
			},
			shadows: {
				enabled: true
			},
			axes: [{
				type: 'category',
				location: $scope.axisLocation,
				categories: $scope.FinanceChartData.Quarter,
			}, {
				type: 'linearGradient',
				location: $scope.labelLocation,
				minimum: 0,
				maximum: 100,
				interval: 10,
				labels: {
					stringFormat: '%.1f%%'
				}
			}],
			series: [{
				type: $scope.graphBarType,
				title: 'Complied',
				fillStyles: ['#00CC33'],
				data: $scope.FinanceChartData.Complied,
				cursor: 'pointer',
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'PosingRisk',
				fillStyles: ['#FFFF00'],
				data: $scope.FinanceChartData.PosingRisk,
				cursor: 'pointer',
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'NonComplied',
				fillStyles: ['#FF0000'],
				data: $scope.FinanceChartData.NonComplied,
				cursor: 'pointer',
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'Delayed',
				fillStyles: ['#BFBFBF'],
				data: $scope.FinanceChartData.Delayed,
				cursor: 'pointer',
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			},{
				type : $scope.graphBarType,
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.FinanceChartData.Delayed_Reported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			} ]
		});
	}



//	Region Level graph jqChart fun


	$scope.searchCategoryWiseGraph = function () {

		//alert("In search category");
		$("#catLoader").show();
		$scope.categoryWiseGraph = {};
		$scope.categoryWiseGraph.Unit = [];
		$scope.categoryWiseGraph.Complied = [];
		$scope.categoryWiseGraph.NonComplied = [];
		$scope.categoryWiseGraph.PosingRisk = [];
		$scope.categoryWiseGraph.Delayed = [];
		$scope.categoryWiseGraph.WaitingForApproval = [];
		$scope.categoryWiseGraph.ReOpened = [];
		$scope.categoryWiseGraph.Delayed_Reported = [];

		var orga_id = $scope.dashboardObj.categoryLevelEntity;
		var loca_id = $scope.dashboardObj.categoryLevelUnit;
		var category_id = $scope.dashboardObj.category;

		var loca_list = [];

		angular.forEach($scope.UnitList, function (unit_data) {
			if ((!orga_id || unit_data.orga_id === orga_id) && (!category_id || unit_data.loca_category === category_id)) {
				if (loca_list.indexOf(unit_data.loca_id) === -1) {
					loca_list.push(unit_data.loca_id);
					$scope.categoryWiseGraph.Unit.push(unit_data.loca_name);
					console.log("Loca Name for Category : " + unit_data.loca_name);
				}
			}
		});

		var complied = 0;
		var noncomplied = 0;
		var posingrisk = 0;
		var delayed = 0;
		var pending = 0;
		var partially_Completed = 0;
		var re_opened = 0;
		var delayed_reported = 0;

		//Category Wise graph Count
		for (var i = 0; i < loca_list.length; i++) {

			var complied = 0;
			var noncomplied = 0;
			var posingrisk = 0;
			var delayed = 0;
			var pending = 0;
			var partially_Completed = 0;
			var re_opened = 0;
			var delayed_reported = 0;
			var from_date = null;
			var from_to = null;

			var from = angular.isDefined($scope.date_from);
			if (from)
				from_date = new Date($scope.date_from);

			var to = angular.isDefined($scope.date_to);
			if (to)
				from_to = new Date($scope.date_to);

			angular.forEach($scope.taskList, function (data) {

				var task_date = new Date(data.date);

				if ((!orga_id || data.orga_id === orga_id) && (!loca_list[i] || data.loca_id === loca_list[i]) && (!category_id || category_id === data.task_loca_category) && (!(from && to) || task_date >= from_date && task_date <= from_to)) {
					//if(task_date>=from_date && task_date <=from_to){

					if (data.status == 'complied') {
						complied++;
					}

					if (data.status == 'noncomplied') {
						noncomplied++;
					}

					if (data.status == 'posingrisk') {
						posingrisk++;
					}

					if (data.status == 'delayed') {
						delayed++;
					}

					if (data.status == 'waitingforapproval') {
						partially_Completed++;
					}
					if (data.status == 'reopen') {
						re_opened++;
					}
					if (data.status == 'delayed-reported') {
						delayed_reported++;
					}

					//}

				}//End IF 
			});

			$scope.categoryWiseGraph.Complied.push(complied);
			$scope.categoryWiseGraph.NonComplied.push(noncomplied);
			$scope.categoryWiseGraph.PosingRisk.push(posingrisk);
			$scope.categoryWiseGraph.Delayed.push(delayed);
			$scope.categoryWiseGraph.WaitingForApproval.push(partially_Completed);
			$scope.categoryWiseGraph.ReOpened.push(re_opened);
			$scope.categoryWiseGraph.Delayed_Reported = delayed_reported;

			$scope.loadCategoryGraph($scope.categoryWiseGraph);
			$("#catLoader").hide();
		}//End function Wise graph Count


	}


	$scope.loadCategoryGraph = function (data) {

		$scope.categoryChartData = data;
		$scope.unitLength = $scope.categoryChartData.Unit.length;
		var background = {
				type: 'linearGradient',
				x0: 0,
				y0: 0,
				x1: 0,
				y1: 1,
				colorStops: [{
					offset: 0,
					color: '#d2e6c9'
				}, {
					offset: 1,
					color: 'white'
				}]
		};

		$scope.chart_widht = 1090;
		$scope.chart_height = 350;
		$scope.axisLocation = 'bottom';
		$scope.graphBarType = 'stacked100Column';
		$scope.labelLocation = 'left';

		if ($scope.unitLength >= 10) {
			$scope.chart_widht = 1150;
			$scope.chart_height = 1500;
			$scope.axisLocation = 'left';
			$scope.graphBarType = 'stacked100Bar';
			$scope.labelLocation = 'bottom';
		}

		$('#CategoryBarChart').jqChart({
			orientation: 'horizontal',
			width: $scope.chart_widht,
			height: $scope.chart_height,
			legend: {
				customItems: [{
					text: {
						text: 'Complied'
					},
					marker: {
						fillStyle: '#00CC33',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Posing'
					},
					marker: {
						fillStyle: '#FFFF00',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Non-Complied'
					},
					marker: {
						fillStyle: '#FF0000',
						type: 'circle'
					}
				}, {
					text: {
						text: 'Delayed'
					},
					marker: {
						fillStyle: '#BFBFBF',
						type: 'circle'
					}
				}, {
					text : {
						text : 'Delayed Reported'
					},
					marker : {
						fillStyle : '#bfd630',
						type : 'circle'
					}
				} ]
			},
			border: {
				strokeStyle: '#6ba851'
			},
			background: background,
			animation: {
				duration: 1
			},
			shadows: {
				enabled: true
			},
			axes: [{
				type: 'category',
				location: $scope.axisLocation,
				categories: $scope.categoryChartData.Unit
			}, {
				type: 'linearGradient',
				location: $scope.labelLocation,
				minimum: 0,
				maximum: 100,
				interval: 10,
				labels: {
					stringFormat: '%.1f%%'
				}
			}],
			series: [{
				type: $scope.graphBarType,
				title: 'Complied',
				fillStyles: ['#00CC33'],
				data: $scope.categoryChartData.Complied,
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'PosingRisk',
				fillStyles: ['#FFFF00'],
				data: $scope.categoryChartData.PosingRisk,
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'NonComplied',
				fillStyles: ['#FF0000'],
				data: $scope.categoryChartData.NonComplied,
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			}, {
				type: $scope.graphBarType,
				title: 'Delayed',
				fillStyles: ['#BFBFBF'],
				data: $scope.categoryChartData.Delayed,
				labels: {
					stringFormat: '%.2f%%',
					valueType: 'percentage',
					font: '12px sans-serif'
				}
			},{
				type : $scope.graphBarType,
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.categoryChartData.Delayed_Reported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			} ]
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
				$scope.client_task_id = task_id;
				//$scope.task_history.documents = 
			}).error(function(error){
				console.log("task History====="+error);
			});
		}
	}

	$scope.getTaskDetails=function(task_id){ 
		//alert(task_id);
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
//		$scope.task.formName = formName;
//		console.log('formName : ' + $scope.task.formName);

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
		$scope.gettaskformultiplecompletion($scope.task_iiidddd, $scope.ttrrrnnnn_id);

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
					$scope.getcomplianceStatusPieChart();

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
						ttrn_id: item.ttrn_id
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
					$scope.getcomplianceStatusPieChart();
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
			auditor: "auditPage"
	}
	$scope.reopenTask=function(data){
		//$scope.taskCompletion.ttrn_id=ttrn_id;
		$scope.reOpen.ttrn_id = data.ttrn_id;
		$scope.reOpen.client_task_id = data.client_task_id;

		//console.log("TTRN ID "+$scope.taskCompletion.ttrn_id);

	}

	//Reopen Task
	$scope.sendReOpenTask=function(){
		//if(formValid){
		ApiCallFactory.reopenTask($scope.reOpen).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				//$scope.reOpen.reopen_comment="";	
				$('#reOpenTask').modal("hide");
				$scope.getTaskHistoryList($scope.reOpen.client_task_id);
				$scope.getTaskDetails($scope.reOpen.client_task_id);
				//	$window.location.reload();
			}

		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});
		//}
	}

	//Approve task
	$scope.approveTask=function(ttrn_id, client_task_id){
		$scope.taskCompletion.ttrn_id=ttrn_id;

		ApiCallFactory.approveTask($scope.taskCompletion).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				toaster.success("Success", "Task approved successfully.");
				$scope.getTaskHistoryList(client_task_id);
				$scope.getTaskDetails(client_task_id);
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
	$scope.lega_date={};

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
				//alert(  $scope.NtmapObj.ttrn_client_task_id);
				spinnerService.hide('html5spinner');
				if(status === 200){
					$('#editConfiguration').modal("hide");
					toaster.success("Success", "Task dates updated successfully.");
					$scope.getTaskHistoryList($scope.NtmapObj.ttrn_client_task_id);
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
		//$scope.ttrn_next_examination_date = new Date($scope.subTaskCompletion.ttrn_next_examination_date);

		if($scope.ttrn_completed_date >= $scope.curDate){ 
			$scope.errMessage.completed_date = 'Completed date should not be greater than todays  date.';
			flag = 1;
			//return false;
		}else{
			$scope.errMessage.completed_date = '';
		}
		/*
		if($scope.ttrn_completed_date >= $scope.ttrn_next_examination_date){ 
			$scope.errMessage.next_examination_date = 'Next Examination date must be greater than completed date.';
			flag = 1;
			//return false;
		}else{
			$scope.errMessage.next_examination_date = '';
		}*/

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

			/*var fromm = data.ttrn_next_examination_date.split("-");
			var completedDate = new Date(fromm[2], fromm[1] - 1, fromm[0]);
			$scope.subTaskCompletion.ttrn_next_examination_date = completedDate;*/
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
					$scope.task_ttrn_ids = [];
					$('#ttrn_proof_of_compliance').val('');
					//alert($scope.client_task_id);
					//$scope.getTaskHistoryList($scope.client_task_id);
					$scope.getSubTaskHistoryList($scope.sub_task_id);

					//$scope.getTaskDetails($scope.client_task_id);
					//$window.location.reload();
					//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
				}else{
					toaster.error("Failed", "Please try again.");
					$("#subTaskCompletionLoader").modal('hide');
					$('#ttrn_proof_of_compliance').val('');
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				$("#subTaskCompletionLoader").modal('hide');
				$scope.task_ttrn_ids = [];
				$('#ttrn_proof_of_compliance').val('');
				console.log("task Completion====="+error);
			});


		}
	}

	$scope.downloadProof = function(udoc_id,client_task_id){	
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

	/*	$scope.download3 = function(){
		alert("in js");
		$window.location = "./getExportDrillReport";
		$state.transitionTo('dashboard');
	}*/

	/*	$scope.download4 = function(status){
		alert(status);
		$window.location = "./downloadExportDrillReport?status="+status;

	}*/
	$scope.ExportXlsNew=function(Campstatus){
		//alert($scope.entityName);
		var obj = {
				'entity_name' : $scope.entityName,
				'status' : Campstatus,
				'fromDate' : $scope.frmSearchDate,
				'toDate': $scope.toSearchDate
		}
		spinnerService.show('html5spinner');
		ApiCallFactory.ExportXlsNew(obj).success(function(res,status){
			if(status === 200){
				//alert("Download successfully completed.");
				toaster.success("File Downloaded Successfully !")
				$window.location = "./downloadExportDrillReport";
				spinnerService.hide('html5spinner');
			}
		}).error(function(error){
			console.log("Error while export excel "+error);
		});
	}

	$scope.deleteTaskDocument=function(udoc_id,client_task_id,ev,index){
		/* var confirm = $mdDialog.confirm()
		          .title('Are you sure you want to delete the document?')
		          .targetEvent(ev)
		          .ok('Yes')
		          .cancel('No');*/
		var isConfirmed = confirm("Are you sure you want to delete this document ?");
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

	$scope.getCheckTaskId=function(list){

		console.log("TASK ID "+ JSON.stringify(list));
		if(list.selectedId==true){
			console.log("True  ");  
			var obj={
					ttrn_id:list.ttrn_id,
			}
			$scope.dashboardObj.tasks_list.push(obj);
		}else{
			var index=0;
			angular.forEach($scope.dashboardObj.tasks_list, function (item) {
				if(item.ttrn_id==list.ttrn_id){ 
					console.log("Remove  ");  
					$scope.dashboardObj.tasks_list.splice(index, 1);   
					//	  break;
				}
				index++;
			});
		}
		if($scope.dashboardObj.tasks_list.length == $scope.obj.reportList.length){
			$scope.selectedAll=true;
		}else{
			$scope.selectedAll=false;
		}

		if($scope.dashboardObj.tasks_list.length>=1) {
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

		var obj = {};
		angular.forEach($scope.obj.reportList, function (item) {
			item.selectedId = $scope.selectedAll;

			if($scope.selectedAll){           	
				obj = {
						ttrn_id:item.ttrn_id,
				} 

			} else {
				$scope.dashboardObj.tasks_list=[];
			}	          
		});
		$scope.dashboardObj.tasks_list.push(obj);
		if($scope.dashboardObj.tasks_list.length>=1) {
			$scope.showOnMultipleSelection=true;
		}else{
			$scope.showOnMultipleSelection=false;
		}
	};

	//Approve all task
	$scope.approveALLTask=function(){

		if($scope.dashboardObj.tasks_list.length>=1){
			ApiCallFactory.approveAllTask($scope.dashboardObj).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status === 200 && res.responseMessage == "Success"){
					toaster.success("Success", "Task approved successfully.");
					$scope.showOnMultipleSelection=false;
					$scope.selectedAll = false;
					$scope.dashboardObj.tasks_list=[];
					$('#drilledModal').modal('hide');	

				}	
			}).error(function(error){
				console.log("Error while approving the task====="+error);
			});

		}

	}

	$scope.approveSubTask = function(history){
		// console.log(task_id);
		$scope.subTaskCompletion.ttrn_sub_id=history.ttrn_sub_id;	 
		ApiCallFactory.approveSubTask($scope.subTaskCompletion).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				toaster.success("Success", "Task approved successfully.");
				//$window.location.reload();
				//$scope.getTaskHistoryList(history.client_task_id);
				//$scope.getTaskDetails(history.client_task_id);
				$scope.getSubTaskHistoryList(sub_task_id);
			}else{
				//alert(res.responseMessage);
			}
		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});
	}
	$scope.reopenSubTask=function(data){
		//$scope.taskCompletion.ttrn_id=ttrn_id;
		$scope.reOpen.ttrn_id = data.ttrn_sub_task_id;
		$scope.reOpen.client_task_id = data.client_task_id;				

	}

	$scope.sendReOpenSubTask=function(formValid){
		//console.log("ID for reopen: "+$scope.reOpen.ttrn_id);
		//console.log("Comment for reopen: "+$scope.reOpen.reopen_comment);
		if(formValid){
			ApiCallFactory.reopenSubTask($scope.reOpen).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					//$scope.reOpen.reopen_comment="";	
					$('#reOpenSubTask').modal("hide");
					$scope.getSubTaskHistoryList(sub_task_id);
					//$scope.getTaskHistoryList($scope.reOpen.client_task_id);
					//$scope.getTaskDetails($scope.reOpen.client_task_id);
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
					$scope.getSubTaskHistoryList(sub_task_id);
					//$scope.getTaskHistoryList($scope.NtmapObj.ttrn_client_task_id);
					//$scope.getTaskDetails($scope.NtmapObj.ttrn_client_task_id);
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
					$scope.getSubTaskHistoryList(sub_task_id);
					//$scope.getTaskHistoryList(client_task_id);
					//$scope.getTaskDetails(client_task_id);
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
					$scope.subTaskHistory.splice(index,1);
					$scope.getSubTaskHistoryList(sub_task_id);
					//$scope.getTaskHistoryList(client_task_id);
					//$scope.getTaskDetails(client_task_id);
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


	$scope.getSubTaskHistoryList=function(task_id){
		//alert(task_id);
		var obj={ttrn_sub_task_id:task_id};
		//	alert(JSON.stringify(obj));
		if(!angular.isUndefined(task_id) && task_id!=0){
			ApiCallFactory.getSubTasksHistory(obj).success(function(res,status){
				//$scope.task_history = res.task_history;
				$scope.subTaskHistory = res.subTaskHistory;
				$('#subTaskList').modal();	
				$scope.editDate={
						role:Storage.get('userDetais.sess_role_id'),
				}
				$scope.sub_task_id = task_id;
				//$scope.task_history.documents = 
			}).error(function(error){
				console.log("task History====="+error);
			});
		}
	}

	$scope.approveSubTask = function(history){
		// console.log(task_id);
		$scope.subTaskCompletion.ttrn_id=history.ttrn_id;  
		ApiCallFactory.approveSubTask($scope.subTaskCompletion).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				toaster.success("Success", "Task approved successfully.");
				//$window.location.reload();
				//$scope.getTaskHistoryList(history.client_task_id);
				//$scope.getTaskDetails(history.client_task_id);
				$scope.getSubTaskHistoryList($scope.sub_task_id);
			}else{
				alert(res.responseMessage);
			}
		}).error(function(error){
			console.log("Error while approving the task====="+error);
		});
	}
	$scope.reopenSubTask=function(data){
		//$scope.taskCompletion.ttrn_id=ttrn_id;
		$scope.reOpen.ttrn_id = data.ttrn_sub_task_id;
		$scope.reOpen.client_task_id = data.client_task_id;				

	}

	$scope.sendReOpenSubTask=function(formValid){
		//console.log("ID for reopen: "+$scope.reOpen.ttrn_id);
		//console.log("Comment for reopen: "+$scope.reOpen.reopen_comment);
		if(formValid){
			ApiCallFactory.reopenSubTask($scope.reOpen).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					//$scope.reOpen.reopen_comment="";	
					$('#reOpenSubTask').modal("hide");
					$scope.getSubTaskHistoryList($scope.sub_task_id);
					//$scope.getTaskHistoryList($scope.reOpen.client_task_id);
					//$scope.getTaskDetails($scope.reOpen.client_task_id);
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
					$scope.getSubTaskHistoryList($scope.sub_task_id);
					//$scope.getTaskHistoryList($scope.NtmapObj.ttrn_client_task_id);
					//$scope.getTaskDetails($scope.NtmapObj.ttrn_client_task_id);

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
					$scope.getSubTaskHistoryList($scope.sub_task_id);
					//$scope.getTaskHistoryList(client_task_id);
					//$scope.getTaskDetails(client_task_id);
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
					$scope.subTaskHistory.splice(index,1);
					$scope.getSubTaskHistoryList($scope.sub_task_id);
					//$scope.getTaskHistoryList(client_task_id);
					//$scope.getTaskDetails(client_task_id);
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


	$scope.getSubTaskHistoryList=function(task_id, client_task_id){
		//alert(task_id);
		var obj={ttrn_sub_task_id:task_id};
		//	alert(JSON.stringify(obj));
		if(!angular.isUndefined(task_id) && task_id!=0){
			ApiCallFactory.getSubTasksHistory(obj).success(function(res,status){
				//$scope.task_history = res.task_history;
				$scope.subTaskHistory = res.subTaskHistory;
				$scope.getTaskDetails(client_task_id);
				$scope.client_task_id = client_task_id;
				$('#subTaskList').modal();	
				$scope.editDate={
						role:Storage.get('userDetais.sess_role_id'),
				}

				$scope.sub_task_id = task_id;
				//$scope.task_history.documents = 
			}).error(function(error){
				console.log("task History====="+error);
			});
		}
	}

	$scope.importTaskToComplete = function() {
		$("#importTaskToComplete").modal();
	}

	$scope.uploadFile=function(formValid) {
		/*	if(formValid) {*/
		spinnerService.show('html5spinner');		
		var formData = new FormData();
		var dummyJson = { name: 'Swapnali' };
		if(!angular.isUndefined($scope.taskObj.task_list)) {
			for (var i=0; i<$scope.taskObj.task_list.length; i++) {
				formData.append("task_list", $scope.taskObj.task_list[i]);
			}
		}
		formData.append("jsonString", JSON.stringify(dummyJson));
		console.log('JSON.stringify(dummyJson) : ' + JSON.stringify(dummyJson));

		ApiCallFactory.importTaskToComplete(formData).success(function(res,status) {

			if(status == 200){
				spinnerService.hide('html5spinner');
				toaster.success("Success", "Sheet uploaded successfully");
				location.reload();
			} else {
				toaster.success("Failed", "Please try again");
			}


		}).error(function(error) {
			spinnerService.hide('html5spinner');
			console.log("Error in import===="+error);

		});
		//}
	}

}]);