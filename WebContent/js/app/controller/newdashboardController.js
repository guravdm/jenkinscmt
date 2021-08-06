'use strict';

CMTApp.controller('newdashboardController', ['$scope','$rootScope','$stateParams', '$state', 'ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$uibModal', '$filter', '$http', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService, $uibModal, $filter, $http) {

	var loca_type 		= [];
	var subUnitArray    = [];
	
	$scope.dashboardObj = {};
	$scope.entityListt  = [];
	$scope.varEntity 	= [];
	$scope.UnitList     = [];
	$scope.FunctionList = [];

	$scope.complied = 0;
	$scope.NonComplied = 0;
	$scope.PosingRisk = 0;
	$scope.Delayed = 0;
	$scope.Pending = 0;
	$scope.WaitingForApproval = 0;
	$scope.ReOpened = 0;
	$scope.DelayedReported = 0;

	$scope.totaltasksinloop = 0;
	$scope.totalactivetasks = 0;
	$scope.totalcompletedtasks = 0;

	$scope.dashboardObj.fquarter = 0;
	$scope.showOnMultipleSelection=false;
	// nitin
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
	$scope.dashboardObj.tasks_list = [];
	$scope.format = 'dd-MM-yyyy'; // $scope.formats[0];
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

	$scope.YearList = 
		[
			{year:2016,year_id:"2016-2017"},
			{year:2017,year_id:"2017-2018"},
			{year:2018,year_id:"2018-2019"},
			{year:2019,year_id:"2019-2020"}
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
		/* { month: "--select--", month_id: 0 }, */
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


	$scope.task = {};
	$scope.proofCompliance = {};
	$scope.task_history = {};
	$scope.subTaskHistory = {};
	// $scope.task_history.documents = {};

	// $scope.eventNotOccured=false;

	$scope.task_details = [];
	$scope.taskCompletion = {};
	$scope.subTaskCompletion = {};
	$scope.reOpen = {};
	$scope.taskCompletion.ttrn_completed_date = new Date();
	$scope.currentDate = new Date();
	//console.log("Todays Date is : "+$scope.currentDate);

	$scope.overAllGraphTable = true;
	$scope.IsVisibleEntityGraph = false;

//	window.onload = function() {
//	// $scope.OverallClick();
//	$scope.getOverallGraphMethod();
//	console.log("Hi Page loaded")
//	};

	/**
	 * @author DnyaneshG
	 * @Date 29-11-2012
	 * @method hide and show graphs based click event
	 */

	$scope.overall_graph_kpi_boxes = true;
	$scope.entity_graph_kpi_boxes = false;
	$scope.unit_graph_kpi_boxes = false;
	$scope.function_graph_kpi_boxes = false;
	$scope.finance_graph_kpi_boxes = false;

	$scope.OverallClick = function() {
		$scope.date_from = '';
		$scope.date_to = '';
		$scope.getOverallGraphMethod();
		$scope.overall_graph_kpi_boxes = true;
		$scope.entity_graph_kpi_boxes = false;
		$scope.unit_graph_kpi_boxes = false;
		$scope.function_graph_kpi_boxes = false;
		$scope.finance_graph_kpi_boxes = false;
		$scope.overAllGraphTable = $scope.overAllGraphTable = true;
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = false;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = false;
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = false;
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = false;
	}

	$scope.entityClick = function() {

		$scope.date_from = '';
		$scope.date_to = '';

		$scope.entityRiskCount();
		$scope.overall_graph_kpi_boxes = false;
		$scope.entity_graph_kpi_boxes = true;
		$scope.unit_graph_kpi_boxes = false;
		$scope.function_graph_kpi_boxes = false;
		$scope.finance_graph_kpi_boxes = false;

		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = true;
		$scope.overAllGraphTable = $scope.overAllGraphTable = false;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = false;
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = false;
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = false;
	}

	$scope.unitClick = function() {

		$scope.date_from = '';
		$scope.date_to = '';

		$scope.unitRiskCount();
		$scope.overall_graph_kpi_boxes = false;
		$scope.entity_graph_kpi_boxes = false;
		$scope.unit_graph_kpi_boxes = true;
		$scope.function_graph_kpi_boxes = false;
		$scope.finance_graph_kpi_boxes = false;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = true;
		$scope.overAllGraphTable = $scope.overAllGraphTable = false;
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = false;
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = false;
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = false;
	}

	$scope.functionClick = function() {
		$scope.dashboardObj.FunlevelEntity = '';
		$scope.dashboardObj.FunlevelUnit = '';

		$scope.date_from = '';
		$scope.date_to = '';
		$scope.functionRiskGraphCount	();
		$scope.overall_graph_kpi_boxes = false;
		$scope.entity_graph_kpi_boxes = false;
		$scope.unit_graph_kpi_boxes = false;
		$scope.function_graph_kpi_boxes = true;
		$scope.finance_graph_kpi_boxes = false;

		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = true;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = false;
		$scope.overAllGraphTable = $scope.overAllGraphTable = false;
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = false;
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = false;
	}

	$scope.financialClick = function() {
		$scope.date_from = '';
		$scope.date_to = '';
		$scope.financialGraph();
		$scope.IsVisibleFinanceChart = $scope.IsVisibleFinanceChart = true;
		$scope.IsVisibleDepartmentBarChart = $scope.IsVisibleDepartmentBarChart = false;
		$scope.IsVisibleLocationBarChart = $scope.IsVisibleLocationBarChart = false;
		$scope.overAllGraphTable = $scope.overAllGraphTable = false;
		$scope.IsVisibleEntityGraph = $scope.IsVisibleEntityGraph = false;
	}

	/**
	 * hide and show graphs code end here
	 */


	/**
	 * @author DnyaneshG
	 * @Date 16-12-2019
	 * @method for search graphs
	 */

	$scope.searchGraph = function () {
		//console.log($scope.date_from);
		//console.log($scope.date_to);
		//console.log('$scope.toDate : ' + $scope.date_to);
		var fromDate = '';
		var toDate = '';
		var obj = {
				fromDate : $filter('date')($scope.date_from, "yyyy-MM-dd"),
				toDate : $filter('date')($scope.date_to, "yyyy-MM-dd"),
		}
		//console.log('obj : ' + JSON.stringify(obj));
		ApiCallFactory.getOverallGraphCount(obj).success(function(res,status){
			$scope.data = res.taskList;
			//console.log('$scope.data : ' + JSON.stringify($scope.data));
			$scope.overAllgraph($scope.data);
		});
	}


	$scope.searchEntityGraphByDate = function () {
		//console.log($scope.date_from);
		//	console.log($scope.date_to);
		//console.log('$scope.toDate : ' + $scope.date_to);
		var fromDate = '';
		var toDate = '';
		var obj = {
				fromDate : $filter('date')($scope.date_from, "yyyy-MM-dd"),
				toDate : $filter('date')($scope.date_to, "yyyy-MM-dd"),
		}
		//console.log('obj : ' + JSON.stringify(obj));
		ApiCallFactory.getEntityRisksCount(obj).success(function(res, status){
			$scope.data = res.taskList;
			$scope.entityList = res.entityList;
			$scope.loadEntityGraphBy();
		});
	}


	$scope.searchLocationBarChartByDate = function () {

		//console.log($scope.date_from);
		//console.log($scope.date_to);
		//console.log('$scope.toDate  searchLocationBarChartByDate : ' + $scope.date_to);
		var fromDate = '';
		var toDate = '';
		var obj = {
				fromDate : $filter('date')($scope.date_from, "yyyy-MM-dd"),
				toDate : $filter('date')($scope.date_to, "yyyy-MM-dd"),
		}
		//console.log('obj : ' + JSON.stringify(obj));
		ApiCallFactory.getUnitRisksCount(obj).success(function(res, status){
			$scope.data = res.taskList;
			$scope.locaList = res.locaList;
			$scope.loadUnitGraphBy();
		});
	}

	$scope.searchDepartmentBarChartByDate = function () {

		//console.log('$scope.toDate  searchDepartmentBarChartByDate : ' + $scope.date_to);
		var fromDate = '0';
		var toDate = '0';
		var obj = {
				fromDate : $filter('date')($scope.date_from, "yyyy-MM-dd"),
				toDate : $filter('date')($scope.date_to, "yyyy-MM-dd"),
		}
		//console.log('searchDepartmentBarChartByDate obj : ' + JSON.stringify(obj));	
		ApiCallFactory.getFunctionRiskGraphCount(obj).success(function(res, status) {
			$scope.data = res.taskList;
			// console.log('orga_name : ' + JSON.stringify(res.orgaList));
			$scope.functionList = res.deptList;
			$scope.entityListt = res.orgaList;
			$scope.loadFunctionGraphBy();
		});
	}


	/**
	 * finance Level searrch
	 */

	/**
	 * End Search Graph here
	 */




	/**
	 * @author DnyaneshG purpose of method Getoverall graph count.
	 * @Date 29-11-2019
	 */

	$scope.getOverallGraphMethod = function() {
		var dt = new Date();
		//console.log(dt);
		var x = 50;
		var fromDateUI = dt.setDate(dt.getDate() - 50);
		var toDateUI = dt.setDate(dt.getDate() + 50);
		//console.log("date plus one day is : " + fromDateUI);
		//console.log("date minus one day is : " + toDateUI);
		var froDates = $filter('date')(fromDateUI, "yyyy-MM-dd");
		var toDates = $filter('date')(toDateUI, "yyyy-MM-dd");
		//console.log('froDates : ' + froDates + '\t toDates : ' + toDates);
		var obj = {
				fromDate : froDates,
				toDate : toDates,
		}
		ApiCallFactory.getOverallGraphCount(obj).success(function(res,status) {
			$scope.data = res.taskList;
			// console.log('$scope.data : ' + JSON.stringify($scope.data));
			$scope.overAllgraph($scope.data);
		});
	}
	$scope.getOverallGraphMethod();


	$scope.overAllgraph = function(res) {

		$scope.NonComplied =  res[0].NonComplied;
		$scope.PosingRisk = res[0].PosingRisk;
		$scope.Complied = res[0].Complied;
		$scope.Delayed = res[0].Delayed;
		$scope.WaitingForApproval = res[0].WaitingForApproval;
		$scope.ReOpened = res[0].ReOpened;
		$scope.DelayedReported = res[0].DelayedReported;

		var background = {
				type: 'linearGradient',
				x0: 0,
				y0: 0,
				x1: 0,
				y1: 1,
				colorStops: [{ offset: 0, color: '#d2e6c9' },
					{ offset: 1, color: 'white' }]
		};

		$('#jqChart').jqChart({
			title: { text: 'Pie Chart' },
			legend: { title: 'Status' },
			border: { strokeStyle: '#6ba851' },
			background: background,
			animation: { duration: 1 },
			shadows: {
				enabled: true
			},
			series: [
				{
					type: 'pie',
					fillStyles: ['#8df3bc ', '#fafaa4f0', '#ffa7a7 ', '#e1d5d5','#bfd630','#dbebf6','#f3d7a9' ],
					labels: {
						stringFormat: '%.1f%%',
						valueType: 'percentage',
						font: '15px sans-serif',
						fillStyle: 'white'
					},
					explodedRadius: 10,
					explodedSlices: [5],
					data: [
						['Complied', $scope.Complied], 
						['PosingRisk', $scope.PosingRisk], 
						['Non-Complied', $scope.NonComplied], 
						['Delayed', $scope.Delayed], 
						['DelayedReported', $scope.DelayedReported], 
						['Waiting For Approval', $scope.WaitingForApproval], 
						['ReOpened', $scope.ReOpened]
						]
				}
				]
		});

		$('#jqChart').bind('tooltipFormat', function (e, data) {
			var percentage = data.series.getPercentage(data.value);
			percentage = data.chart.stringFormat(percentage, '%.2f%%');

			return '<b>' + data.dataItem[0] + '</b><br />' +
			data.value + ' (' + percentage + ')';
		});
	}

	/**
	 * overall graph code end here
	 */


	/**
	 * entityRiskCount method starts from here
	 * 
	 * @authod DnyaneshG
	 * @Start_Date 01-12-2019
	 */

	$scope.entityRiskCount = function() {
		var dt = new Date();
		console.log(dt);
		var x = 50;
		var fromDateUI = dt.setDate(dt.getDate() - 50);
		var toDateUI = dt.setDate(dt.getDate() + 50);
		//console.log("date plus one day is : " + fromDateUI);
		//console.log("date minus one day is : " + toDateUI);
		var froDates = $filter('date')(fromDateUI, "yyyy-MM-dd");
		var toDates = $filter('date')(toDateUI, "yyyy-MM-dd");
		//console.log('froDates : ' + froDates + '\t toDates : ' + toDates);
		var obj = {
				fromDate : froDates,
				toDate : toDates,
		}
		ApiCallFactory.getEntityRisksCount(obj).success(function(res, status){
			$scope.data = res.taskList;
			$scope.entityList = res.entityList;
			$scope.loadEntityGraphBy();
		});
	}


	$scope.loadEntityGraphBy = function() {
		$scope.entityGraph 						= {};
		$scope.entityGraph.Entity 				= [];
		$scope.entityGraph.EntityId				= [];
		$scope.entityGraph.Complied 			= [];
		$scope.entityGraph.NonComplied 			= [];
		$scope.entityGraph.PosingRisk			= [];
		$scope.entityGraph.Delayed 				= [];
		$scope.entityGraph.WaitingForApproval 	= [];
		$scope.entityGraph.ReOpened 			= [];
		$scope.entityGraph.DelayedReported 		= [];

		angular.forEach($scope.entityList, function (value, key) {
			$scope.entityGraph.Entity.push(value.EntityName);
			$scope.entityGraph.EntityId.push(value.entityId);
		});

		angular.forEach($scope.data, function (data) {
			$scope.entityGraph.NonComplied.push(data.NonComplied);
			$scope.entityGraph.PosingRisk.push(data.PosingRisk);
			$scope.entityGraph.Complied.push(data.Complied);
			$scope.entityGraph.Delayed.push(data.Delayed);
			$scope.entityGraph.WaitingForApproval.push(data.WaitingForApproval);
			$scope.entityGraph.ReOpened.push(data.ReOpened);
			$scope.entityGraph.DelayedReported.push(data.DelayedReported);
		});
		$scope.loadentityGraph($scope.entityGraph);
	}



	$scope.EntityChartData = [];
	$scope.loadentityGraph = function(data) {

		$scope.EntityChartData = data;
		// console.log('complied : ' + $scope.EntityChartData.Complied);
		// console.log('entityLevel Data : ' + JSON.stringify($scope.EntityChartData));

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

		$('#entityLevel').jqChart({
			width:550,
			title: { text: 'Entity Level Chart' },
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
						fillStyle : '#8df3bc ',
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
						fillStyle : '#ffa7a7',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#e1d5d5',
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
				}, {
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#dbebf6',
						type : 'circle'
					}
				}, {
					text : {
						text : 'ReOpened'
					},
					marker : {
						fillStyle : '#f3d7a9',
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
				categories: $scope.EntityChartData.Entity,
				labels: { angle : 10 }
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
			series : [{
				type : 'stacked100Column',
				title : 'Complied',
				fillStyles : [ '#8df3bc ' ],
				data : $scope.EntityChartData.Complied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : 'stacked100Column',
				title : 'PosingRisk',
				fillStyles : [ '#fafaa4f0' ],
				data : $scope.EntityChartData.PosingRisk,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : 'stacked100Column',
				title : 'NonComplied',
				fillStyles : [ '#ffa7a7' ],
				data :  $scope.EntityChartData.NonComplied,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : 'stacked100Column',
				title : 'Delayed',
				fillStyles : [ '#e1d5d5' ],
				data : $scope.EntityChartData.Delayed,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			},{
				type : 'stacked100Column',
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.EntityChartData.DelayedReported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : 'stacked100Column',
				title : 'Waiting For Approval',
				fillStyles : [ '#dbebf6' ],
				data : $scope.EntityChartData.WaitingForApproval,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : 'stacked100Column',
				title : 'ReOpened',
				fillStyles : [ '#f3d7a9' ],
				data : $scope.EntityChartData.ReOpened,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}]
		});
	}


	/**
	 * Entity wise graph code and search code end here
	 */


	/**
	 * @purpose get unit graph data
	 * @author DnyaneshG
	 * @Date 02-12-2019 - 03-12-2019
	 * 
	 */


	$scope.unitRiskCount = function() {
		var dt = new Date();
		console.log(dt);
		var x = 50;
		var fromDateUI = dt.setDate(dt.getDate() - 50);
		var toDateUI = dt.setDate(dt.getDate() + 50);
		//console.log("date plus one day is : " + fromDateUI);
		//console.log("date minus one day is : " + toDateUI);
		var froDates = $filter('date')(fromDateUI, "yyyy-MM-dd");
		var toDates = $filter('date')(toDateUI, "yyyy-MM-dd");
		//console.log('froDates : ' + froDates + '\t toDates : ' + toDates);
		var obj = {
				fromDate : froDates,
				toDate : toDates,
		}
		ApiCallFactory.getUnitRisksCount(obj).success(function(res, status){
			$scope.data = res.taskList;
			$scope.locaList = res.locaList;
			$scope.loadUnitGraphBy();
		});
	}


	$scope.loadUnitGraphBy = function() {

		$scope.unitGraph = {};
		$scope.unitGraph.Unit 				= [];
		$scope.unitGraph.Complied 			= [];
		$scope.unitGraph.NonComplied 		= [];
		$scope.unitGraph.PosingRisk			= [];
		$scope.unitGraph.Delayed 			= [];
		$scope.unitGraph.WaitingForApproval	= [];
		$scope.unitGraph.ReOpened 			= [];
		$scope.unitGraph.DelayedReported 	= [];

		angular.forEach($scope.locaList, function (value, key) {
			$scope.unitGraph.Unit.push(value.locaName);
		});

		angular.forEach($scope.data, function (data) {
			$scope.unitGraph.NonComplied.push(data.NonComplied);
			$scope.unitGraph.PosingRisk.push(data.PosingRisk);
			$scope.unitGraph.Complied.push(data.Complied);
			$scope.unitGraph.Delayed.push(data.Delayed);
			$scope.unitGraph.WaitingForApproval.push(data.WaitingForApproval);
			$scope.unitGraph.ReOpened.push(data.ReOpened);
			$scope.unitGraph.DelayedReported.push(data.DelayedReported);
		});
		$scope.loadUnitGraph($scope.unitGraph);
	}

	$scope.unitChartData = [];
	$scope.unitDataList = {};
	$scope.loadUnitGraph = function(data) {

		$scope.unitChartData = data;
		console.log($scope.unitChartData);
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

		$scope.chart_widht = 550;
		$scope.chart_height = 350;
		$scope.axisLocation='bottom';
		$scope.graphBarType='stacked100Column';
		$scope.labelLocation='left';

		if ($scope.unitLength >=1 && $scope.unitLength <=9){
			$scope.chart_widht = 550;
			$scope.chart_height = 350;
			$scope.axisLocation='bottom';
			$scope.graphBarType='stacked100Column';
			$scope.labelLocation='left';
		}else if ($scope.unitLength >=10 && $scope.unitLength <=21){
			$scope.chart_widht = 550;
			$scope.chart_height = 800;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}else /* if($scope.unitLength >=20 && $scope.unitLength <=39) */{
			$scope.chart_widht = 550;
			$scope.chart_height = 1150;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}

		$('#LocationBarChart').jqChart({
			orientation: 'horizontal',
			width: $scope.chart_widht,
			// height: $scope.chart_height,
			height:600,
			legend : {
				customItems : [ {
					text : {
						text : 'Complied'
					},
					marker : {
						fillStyle : '#8df3bc',
						type : 'circle'
					}
				}, {
					text : {
						text : 'PosingRisk'
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
						fillStyle : '#ffa7a7',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#e1d5d5',
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
				}, {
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#dbebf6',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Re-Opened'
					},
					marker : {
						fillStyle : '#f3d7a9',
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
				fillStyles : [ '#8df3bc' ],
				data : $scope.unitChartData.Complied,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'PosingRisk',
				fillStyles : [ '#fafaa4f0' ],
				data : $scope.unitChartData.PosingRisk,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'NonComplied',
				fillStyles : [ '#ffa7a7' ],
				data : $scope.unitChartData.NonComplied,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed',
				fillStyles : [ '#e1d5d5' ],
				data : $scope.unitChartData.Delayed,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed-Reported',
				fillStyles : [ '#bfd630' ],
				data : $scope.unitChartData.DelayedReported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Waiting For Approval',
				fillStyles : [ '#dbebf6' ],
				data : $scope.unitChartData.WaitingForApproval,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Re-Opened',
				fillStyles : [ '#f3d7a9' ],
				data : $scope.unitChartData.ReOpened,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}]
		});
	}



	/**
	 * @Date 03-12-2019
	 * @authod DnyaneshG
	 * @method is used to generate function graph
	 */

	$scope.entityListt  = [];
	$scope.functionList = [];
	$scope.UnitList     = [];

	$scope.functionRiskGraphCount = function() {

		var dt = new Date();
		console.log(dt);
		var x = 50;
		var fromDateUI = dt.setDate(dt.getDate() - 50);
		var toDateUI = dt.setDate(dt.getDate() + 50);
		//console.log("date plus one day is : " + fromDateUI);
		//console.log("date minus one day is : " + toDateUI);
		var froDates = $filter('date')(fromDateUI, "yyyy-MM-dd");
		var toDates = $filter('date')(toDateUI, "yyyy-MM-dd");
		// console.log('froDates : ' + froDates + '\t toDates : ' + toDates);
		var obj = {
				fromDate : froDates,
				toDate : toDates,
		}
		ApiCallFactory.getFunctionRiskGraphCount(obj).success(function(res, status){
			$scope.data = res.taskList;
			// console.log('orga_name : ' + JSON.stringify(res.orgaList));
			$scope.functionList = res.deptList;
			$scope.entityListt = res.orgaList;
			$scope.loadFunctionGraphBy();
			// getFunctionRiskGraphCountURL
		});
	}


	/**
	 * @author DnyaneshG
	 * @Date 13-12-2019
	 * @method get unit names by orgaId and by access
	 */

	$scope.unitNamesList     = [];

	/**
	 * by orga wise filter
	 */
	$scope.getUnits = function() {

		var orgamId = $scope.dashboardObj.FunlevelEntity;

		$scope.dashboardObj.FunlevelUnit = '';
		var dt = new Date();
		console.log(dt);
		var x = 50;
		var fromDateUI = dt.setDate(dt.getDate() - 50);
		var toDateUI = dt.setDate(dt.getDate() + 50);
		var froDates = $filter('date')(fromDateUI, "yyyy-MM-dd");
		var toDates = $filter('date')(toDateUI, "yyyy-MM-dd");
		var obj = {
				fromDate : froDates,
				toDate : toDates,
				orgaId : orgamId,
		}
		ApiCallFactory.getLocationListByOrgaId(obj).success(function(res, status) {
			$scope.data = res.taskList;
			$scope.functionList = res.deptList;
			$scope.entityListt = res.orgaList;
			$scope.unitList = res.unitList;
			$scope.loadFunctionGraphBy();
		});
	}


	/**
	 * by unit wise filter
	 */


	$scope.searchFuntionLevelGraph = function() {

		var orgamId = $scope.dashboardObj.FunlevelEntity;
		var unitId = $scope.dashboardObj.FunlevelUnit;
		$scope.functionGraph.Function			= [];
		var dt = new Date();
		console.log(dt);
		var x = 50;
		var fromDateUI = dt.setDate(dt.getDate() - 50);
		var toDateUI = dt.setDate(dt.getDate() + 50);
		var froDates = $filter('date')(fromDateUI, "yyyy-MM-dd");
		var toDates = $filter('date')(toDateUI, "yyyy-MM-dd");
		var obj = {
				fromDate : froDates,
				toDate : toDates,
				orgaId : orgamId,
				unitId : unitId
		}
		ApiCallFactory.filterDataByOrgaIdAndUnitId(obj).success(function(res, status) {
			$scope.data = res.taskList;
			$scope.functionList = res.deptList;
			$scope.entityListt = res.orgaList;
			$scope.unitList = res.unitList;
			$scope.loadFunctionGraphBy();
		});
	}

	$scope.loadFunctionGraphBy = function() {

		$scope.functionGraph = {};
		$scope.functionGraph.Function			= [];
		$scope.functionGraph.Complied 			= [];
		$scope.functionGraph.NonComplied 		= [];
		$scope.functionGraph.PosingRisk			= [];
		$scope.functionGraph.Delayed 			= [];
		$scope.functionGraph.WaitingForApproval = [];
		$scope.functionGraph.ReOpened 			= [];
		$scope.functionGraph.DelayedReported 	= [];

		angular.forEach($scope.functionList, function (value, key) {
			$scope.functionGraph.Function.push(value.deptName);
			console.log('functionList name : ' + value.deptName);
		});

		angular.forEach($scope.data, function (data) {
			$scope.functionGraph.NonComplied.push(data.NonComplied);
			$scope.functionGraph.PosingRisk.push(data.PosingRisk);
			$scope.functionGraph.Complied.push(data.Complied);
			$scope.functionGraph.Delayed.push(data.Delayed);
			$scope.functionGraph.WaitingForApproval.push(data.WaitingForApproval);
			$scope.functionGraph.ReOpened.push(data.ReOpened);
			$scope.functionGraph.DelayedReported.push(data.DelayedReported);
		});
		$scope.loadFunctionChartGraph($scope.functionGraph);
	}

	$scope.functionChartData = [];
	$scope.functionDataList = {};
	$scope.loadFunctionChartGraph = function(data) {

		$scope.functionChartData = data;
		// console.log('json Data : ' + JSON.stringify($scope.functionChartData));
		$scope.functionLength = $scope.functionChartData.Function.length;
		console.log('length : ' + $scope.functionLength);
		console.log('functions name : ' + $scope.functionChartData.Function);

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
		$scope.chart_widht = 735;
		$scope.chart_height = 350;
		$scope.axisLocation='bottom';
		$scope.graphBarType='stacked100Column';
		$scope.labelLocation='bottom';

		if ($scope.functionLength >=1 && $scope.functionLength <=9){
			$scope.chart_widht = 735;
			$scope.chart_height = 350;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}else if ($scope.functionLength >=10 && $scope.functionLength <=22){
			$scope.chart_widht = 735;
			$scope.chart_height = 350;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}else {
			$scope.chart_widht = 735;
			$scope.chart_height = 350;
			$scope.axisLocation='left';
			$scope.graphBarType='stacked100Bar';
			$scope.labelLocation='bottom';
		}
		$('#DepartmentBarChart').jqChart({
			orientation: 'horizontal',
			title: {text: 'Function Level Chart'},
			height :1000,
			width:550,
			legend : {
				location: 'bottom',
				customItems : [ {
					text : {
						text : 'Complied'
					},
					marker : {
						fillStyle : '#8df3bc ',
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
						fillStyle : '#ffa7a7',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Delayed'
					},
					marker : {
						fillStyle : '#e1d5d5',
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
				},{
					text : {
						text : 'Waiting For Approval'
					},
					marker : {
						fillStyle : '#dbebf6',
						type : 'circle'
					}
				}, {
					text : {
						text : 'Re-Opened'
					},
					marker : {
						fillStyle : '#f3d7a9',
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
				fillStyles : [ '#8df3bc ' ],
				data : $scope.functionChartData.Complied,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'PosingRisk',
				fillStyles : [ '#fafaa4f0' ],
				data : $scope.functionChartData.PosingRisk,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'NonComplied',
				fillStyles : [ '#ffa7a7' ],
				data : $scope.functionChartData.NonComplied,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Delayed',
				fillStyles : [ '#e1d5d5' ],
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
				data : $scope.functionChartData.DelayedReported,
				cursor : 'pointer',
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Waiting For Approval',
				fillStyles : [ '#dbebf6' ],
				data : $scope.functionChartData.WaitingForApproval,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}, {
				type : $scope.graphBarType,
				title : 'Re-Opened',
				fillStyles : [ '#f3d7a9' ],
				data : $scope.functionChartData.ReOpened,
				labels : {
					stringFormat : '%.2f%%',
					valueType : 'percentage',
					font : '12px sans-serif'
				}
			}]
		});

	}

	/**
	 * code end for function graph
	 */


//	$scope.functionGraph = function() {
//	var background = {
//	type: 'linearGradient',
//	x0: 0,
//	y0: 0,
//	x1: 0,
//	y1: 1,
//	colorStops: [{ offset: 0, color: '#d2e6c9' },
//	{ offset: 1, color: 'white' }]
//	};

//	$('#DepartmentBarChart').jqChart({
//	title: { text: 'Pie Chart' },
//	legend: { title: 'Status' },
//	border: { strokeStyle: '#6ba851' },
//	background: background,
//	animation: { duration: 1 },
//	shadows: {
//	enabled: true
//	},
//	series: [
//	{
//	type: 'pie',
//	fillStyles: ['#418CF0', '#FCB441', '#E0400A', '#056492', '#BFBFBF', '#1A3B69', '#FFE382'],
//	labels: {
//	stringFormat: '%.1f%%',
//	valueType: 'percentage',
//	font: '15px sans-serif',
//	fillStyle: 'white'
//	},
//	explodedRadius: 10,
//	explodedSlices: [5],
//	data: [['United States', 65], ['United Kingdom', 58], ['Germany', 30],
//	['India', 60], ['Russia', 65], ['China', 75]]
//	}
//	]
//	});
//	}



	$scope.financialGraph = function() {

		$('#financeLevel').jqChart({
			title: { text: 'Finance Graph' },
			animation: { duration: 1 },
			shadows: {
				enabled: true
			},
			axes: [
				{
					type: 'category',
					location: 'bottom',
					categories: ['A', 'B', 'C', 'D', 'E', 'F', 'G']
				},
				{
					type: 'linear',
					location: 'left',
					labels: {
						stringFormat: '%d%%'
					}
				}
				],
				series: [
					{
						type: 'stacked100Column',
						title: 'Stacked 1',
						data: [62, 70, 68, 58, 52, 60, 48],
						labels: {
							font: '12px sans-serif'
						}
					},
					{
						type: 'stacked100Column',
						title: 'Stacked 2',
						data: [56, 30, 62, 65, 40, 36, 70],
						labels: {
							font: '12px sans-serif'
						}
					},
					{
						type: 'stacked100Column',
						title: 'Stacked 3',
						data: [33, 42, 54, 23, 54, 47, 61],
						labels: {
							font: '12px sans-serif'
						}
					}
					]
		});
	}

	/**
	 * @author DnyaneshG
	 * @Date 09-12-2019
	 * @method for drilled down report
	 */
	var myObj = {};
	$scope.seObj={};

	$scope.searchObjsFromDate = $scope.date_from;
	$scope.searchObjsToDate = $scope.date_to;

	$scope.chart_name="";
	$scope.fromDate = "";
	$scope.toDate = "";

	// Click Evevent & Dril-Down For Pie Chart
	$('#jqChart').bind('dataPointMouseUp', function (e, data) {
		// console.log('drilledDown report jqChart1 fun() : ');
		// console.log('data.dataItem  : ' + data.dataItem[0])

		var froDates =  $filter('date')($scope.date_from, "yyyy-MM-dd");
		var tDates = $filter('date')($scope.date_to, "yyyy-MM-dd");
		//console.log('froDates : ' + froDates);
		//console.log('tDates : ' + tDates);
		if(tDates == undefined || tDates == '') {
			froDates = 0;
			tDates = 0;
		}else {
			froDates = $filter('date')($scope.date_from, "yyyy-MM-dd");
			tDates = $filter('date')($scope.date_to, "yyyy-MM-dd");
		}
		//console.log('tDates : ' + tDates);
		if(data.dataItem[0] === "Complied") {
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "Complied";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.searchObj.fromDate = froDates;
			$scope.searchObj.toDate = tDates;
			$scope.getOverallDrilledReport($scope.searchObj);
		}else if(data.dataItem[0] === "PosingRisk") {
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "PosingRisk";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.searchObj.fromDate = froDates;
			$scope.searchObj.toDate = tDates;
			$scope.getOverallDrilledReport($scope.searchObj);
		}else if(data.dataItem[0] === "Non-Complied") {
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "NonComplied";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.searchObj.fromDate = froDates;
			$scope.searchObj.toDate = tDates;
			$scope.getOverallDrilledReport($scope.searchObj);
		}else if(data.dataItem[0] === "Waiting For Approval") {
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "Waiting For Approval";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.searchObj.fromDate = froDates;
			$scope.searchObj.toDate = tDates;
			$scope.getOverallDrilledReport($scope.searchObj);
		} else if(data.dataItem[0] === "ReOpened") {
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "ReOpened";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.searchObj.fromDate = froDates;
			$scope.searchObj.toDate = tDates;
			$scope.getOverallDrilledReport($scope.searchObj);
		} else if(data.dataItem[0] === "DelayedReported") {
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "DelayedReported";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.searchObj.fromDate = froDates;
			$scope.searchObj.toDate = tDates;
			// $scope.drilledReport($scope.searchObj);
			$scope.getOverallDrilledReport($scope.searchObj);
		}else {
			$scope.searchObj.chart_name = "overall";
			$scope.searchObj.status = "Delayed";
			$scope.searchObj.entity = 0;
			$scope.searchObj.unit = 0;
			$scope.searchObj.department = 0;
			$scope.searchObj.fromDate = froDates;
			$scope.searchObj.toDate = tDates;
			$scope.getOverallDrilledReport($scope.searchObj);
		}
	});

	/**
	 * overall drilled down report code end
	 */


	/**
	 * @author DnyaneshG
	 * @Date 11-12-2019
	 * @method entity drilled down report
	 */

	$scope.entityDrilledObj={};
	$('#entityLevel').bind('dataPointMouseUp', function(e, data) {
		//console.log('data entityLevel : ' + data);
		var froDates =  $filter('date')($scope.date_from, "yyyy-MM-dd");
		var tDates = $filter('date')($scope.date_to, "yyyy-MM-dd");

		//	console.log('from date : ' + froDates + '\t todate : ' + tDates);

		if(tDates == undefined || tDates == '') {
			froDates = 0;
			tDates = 0;
		}else {
			froDates = $filter('date')($scope.date_from, "yyyy-MM-dd");
			tDates = $filter('date')($scope.date_to, "yyyy-MM-dd");
		}
		$scope.getEntityDrilledReport({'chart_name':'entityLevel','status':data.series.title,'entity':data.x,'unit':0,'department':0, 'fromDate': froDates, 'toDate': tDates});
	});

	/**
	 * End entity drilled down report
	 */

	$('#LocationBarChart').bind('dataPointMouseUp', function (e, data) {
		//console.log('data LocationBarChart : ' + data);
		var froDates =  $filter('date')($scope.date_from, "yyyy-MM-dd");
		var tDates = $filter('date')($scope.date_to, "yyyy-MM-dd");

		if(tDates == undefined || tDates == '') {
			froDates = 0;
			tDates = 0;
		}else {
			froDates = $filter('date')($scope.date_from, "yyyy-MM-dd");
			tDates = $filter('date')($scope.date_to, "yyyy-MM-dd");
		}
		$scope.drilledLocationBarChartReport({'chart_name':'unitLevel','status':data.series.title,'entity':0,'unit':data.x,'department':0, 'fromDate': froDates, 'toDate': tDates});
	});


	/**
	 * @author DnyaneshG
	 * @Date 13-12-2019
	 * @method for DepartmentBarChart drilldown report
	 */

	$('#DepartmentBarChart').bind('dataPointMouseUp', function (e, data) {
		//console.log('data DepartmentBarChart : ' + data);
		var orgId = 0;
		var unName = 0;
		if($scope.dashboardObj.FunlevelEntity == "") {
			orgId = 0;
		}else{
			orgId = $scope.dashboardObj.FunlevelEntity;
		}
		if($scope.dashboardObj.FunlevelUnit == ""){
			unName = 0;
		}else{
			unName = $scope.dashboardObj.FunlevelUnit;
		}
		console.log('orgId : ' + orgId + '\t unName : ' + unName);

		var froDates = $filter('date')($scope.date_from, "yyyy-MM-dd");
		var tDates = $filter('date')($scope.date_to, "yyyy-MM-dd");
		//console.log('froDates : ' + froDates);
		//console.log('tDates : ' + tDates);
		if(tDates == undefined || tDates == '') {
			froDates = 0;
			tDates = 0;
		}else {
			froDates = $filter('date')($scope.date_from, "yyyy-MM-dd");
			tDates = $filter('date')($scope.date_to, "yyyy-MM-dd");
		}
		//console.log('froDates : ' + froDates);
		//console.log('tDates : ' + tDates);
		$scope.drilledFunctionBarChartReport({'chart_name':'departmentLevel','status':data.series.title,'entity':orgId,'unit':unName,'department':data.x, 'fromDate': froDates, 'toDate': tDates});
	});

	/**
	 * End
	 */

	/**
	 * @author DnyaneshG
	 * @Date 09-12-2019
	 * @method for drilled down report
	 */


	$scope.drilledReport = function(obj){
		//console.log('drilledReport obj : ' + JSON.stringify(obj));

		angular.forEach($scope.taskList, function (data) {
			console.log('chart_name : ' + obj.chart_name);
			console.log('data : ' + JSON.stringify(data));
		});

		$scope.open(obj);
	}


	/**
	 * @author DnyaneshG
	 * @Date 09-12-2019
	 * @method return overall data on modal
	 */

	$scope.getOverallDrilledReport = function(objData) {
		//console.log('getOverallDrilledReport : ' + JSON.stringify(objData));
		$http({
			url : "./getoveralldrilleddata",
			params : {
				'jsonString' : objData
			},
			method : "get"
		}).then(function(result) {

			$scope.reportList = result.data;

			$scope.open($scope.reportList);

		}, function(result) {

		});
	}


	/**
	 * @author DnyaneshG
	 * @Date 11-12-2019
	 * @method return entity wise data on modal
	 */

	$scope.getEntityDrilledReport = function(objData) {
		$http({
			url : "./getEntityRisksModalData",
			params : {
				'jsonString' : objData
			},
			method : "get"
		}).then(function(result) {

			$scope.reportList = result.data;

			$scope.open($scope.reportList);

		}, function(result) {

		});
	}

	/**
	 * Unit level graph drilled down report
	 */

	$scope.drilledLocationBarChartReport = function(objData) {
		$http({
			url : "./getUnitRisksModalData",
			params : {
				'jsonString' : objData
			},
			method : "get"
		}).then(function(result) {

			$scope.reportList = result.data;

			$scope.open($scope.reportList);

		}, function(result) {

		});
	}

	/**
	 * @author DnyaneshG
	 * @Date 13-12-2019
	 * @method get Function wise data in modal
	 */

	$scope.drilledFunctionBarChartReport = function(objData) {
		$http({
			url : "./getFunctionRisksModalData",
			params : {
				'jsonString' : objData
			},
			method : "get"
		}).then(function(result) {

			$scope.reportList = result.data;
			$scope.open($scope.reportList);

		}, function(result) {

		});
	}


	$scope.open = function(obj) {
		 console.log("Object Data : "+JSON.stringify(obj));
		if(obj.reportList.length<=0){
			// alert('Nothing to show here..!')
			toaster.error("work in progress " + obj.status);
			return;
		}
		// if(obj.reportList)
		$scope.obj=obj;
		$('#drilledModal').modal();
	};

	/**
	 * End
	 */

	var curr_date = new Date();
	var curr_year = curr_date.getFullYear();
	$scope.dashboardObj.fyear = curr_year;


	$scope.format = 'dd-MM-yyyy'; // $scope.formats[0];

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

/*	$scope.getTaskHistoryList=function(task_id){
		$state.transitionTo('taskDetails', {'task_id':task_id});
	};*/

	
	$scope.getCheckTaskId=function(history){
		 console.log("TASK ID "+ history.ttrn_id);
		 if(history.selectedId==true){
			 console.log("True  ");  
			 var obj={
					 ttrn_id:history.ttrn_id,
      			}
			 $scope.dashboardObj.tasks_list.push(obj);
		 }else{
			 var index=0;
			 angular.forEach($scope.dashboardObj.tasks_list, function (item) {
				 if(item.ttrn_id==history.ttrn_id){ 
					 console.log("Remove  ");  
					  $scope.dashboardObj.tasks_list.splice(index, 1);   
				//	  break;
				 }
				 index++;
			 });
		}
		 if($scope.dashboardObj.tasks_list.length==$scope.reportList.length){
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
	        angular.forEach($scope.reportList, function (item) {
	            item.selectedId = $scope.selectedAll;
	            if($scope.selectedAll){           	
	            	 obj = {
	            			ttrn_id:item.ttrn_id,
	            	} 
	            	
	            } else {
	            	 var index=0;
	    			 angular.forEach($scope.dashboardObj.tasks_list, function (item) {
	    				 if(item.ttrn_id==ttrn_id){ 
	    					 console.log("Remove  ");  
	    					  $scope.dashboardObj.tasks_list.splice(index, 1);   
	    				//	  break;
	    				 }
	    				 index++;
	    			 });      
	            }	          
	        });
	        $scope.dashboardObj.tasks_list.push(obj);
	        if($scope.dashboardObj.tasks_list.length>=1) {
				$scope.showOnMultipleSelection=true;
			}else{
				$scope.showOnMultipleSelection=false;
			}
	    };
	

	  //Approve task
		$scope.approveAllTask=function(){
						
			if($scope.dashboardObj.tasks_list.length>0){
		  		ApiCallFactory.approveAllTask($scope.dashboardObj).success(function(res,status){
		  			if(status === 200 && res.responseMessage == "Success"){
		  				toaster.success("Success", "Task approved successfully.");
		  				$scope.showOnMultipleSelection=false;
		  				$scope.selectedAll = false;
		  			
		  			}	
		  		}).error(function(error){
					console.log("Error while approving the task====="+error);
				});
		  	
		 }
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

		$scope.getTaskDetails = function(task_id){ 
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

		//get task history
		$scope.CompleteTaskPage=function(ttrn_id,document,comments,completedDate,status,reason,frequency,legalDueDate,clientTaskId){ //document contain 1 - mandatory doc and 0 - Non mandatory
			//alert(clientTaskId);									
			$scope.task.task_id=clientTaskId;									
			$scope.task.ttrn_id=ttrn_id;
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
			console.log("Legal Due Date is : "+$scope.legalDueDate);	
			
			if($scope.taskCompletion.ttrn_reason_for_non_compliance == null || $scope.taskCompletion.ttrn_reason_for_non_compliance ==""){
				$scope.taskCompletion.ttrn_reason_for_non_compliance = '';
			}else
				$scope.taskCompletion.ttrn_reason_for_non_compliance=reason;
			
			console.log("reason:" +$scope.taskCompletion.ttrn_reason_for_non_compliance);

			$scope.today = function () {
				$scope.taskCompletion.ttrn_completed_date = completedDate;
			};
			$("#completeTask").modal();
			//$window.open('completeTask', {'ttrn_id':ttrn_id,'task_id':clientTaskId,'document':document,'comments':comments,'completedDate':completedDate,'status':status,'reason':reason,'frequency':frequency,'legalDueDate':legalDueDate});
		};

		$scope.saveCompletion= function(formValid){

			if(formValid && !angular.isUndefined($scope.task.ttrn_id) && $scope.task.ttrn_id!=0){
				spinnerService.show('html5spinner');
				var obj= {ttrn_id:$scope.task.ttrn_id};
				$scope.task_ttrn_ids.push(obj);

				$scope.taskCompletion.ttrn_ids = $scope.task_ttrn_ids;
				$scope.taskCompletion.ttrn_completed_date = moment($scope.taskCompletion.ttrn_completed_date).format('DD-MM-YYYY');
				$scope.taskCompletion.ttrn_reason_for_non_compliance = $scope.taskCompletion.ttrn_reason_for_non_compliance;
			
				if($scope.taskCompletion.ttrn_reason_for_non_compliance == null || $scope.taskCompletion.ttrn_reason_for_non_compliance==""){
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
						$('#ttrn_proof_of_compliance').val('');
							$scope.getTaskHistoryList($scope.task.task_id);
							$scope.getTaskDetails($scope.task.task_id);

						$("#changeTask").modal();

						//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
					}else{
						toaster.error("Failed", "Please try again.");
					}
				}).error(function(error){
					spinnerService.hide('html5spinner');
					console.log("task Completion====="+error);
				});


			}
		}

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
			if(formValid){
				ApiCallFactory.reopenTask($scope.reOpen).success(function(res,status){
					if(status === 200 && res.responseMessage == "Success"){
						//$scope.reOpen.reopen_comment="";	
						$('#reOpenTask').modal("hide");
						toaster.success("Task Reopened Successfully");
						//$scope.getTaskHistoryList($scope.reOpen.client_task_id);
						//$scope.getTaskDetails($scope.reOpen.client_task_id);
						//	$window.location.reload();
					}

				}).error(function(error){
					console.log("Error while reopening the task====="+error);
				});
			}
		}

		//Approve task
		$scope.approveTask=function(ttrn_id, client_task_id){
			$scope.taskCompletion.ttrn_id=ttrn_id;

			ApiCallFactory.approveTask($scope.taskCompletion).success(function(res,status){
				if(status === 200 && res.responseMessage == "Success"){
					toaster.success("Success", "Task approved successfully.");
					
				}	
			}).error(function(error){
				console.log("Error while approving the task====="+error);
			});

		}

		$scope.tmapObj = {};
		$scope.NtmapObj = {};
		$scope.errMessage = {};
		$scope.lega_date={};
		

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

				var fromm = data.ttrn_next_examination_date.split("-");
				var completedDate = new Date(fromm[2], fromm[1] - 1, fromm[0]);
				$scope.subTaskCompletion.ttrn_next_examination_date = completedDate;
			}else{
				$scope.subTaskCompletion.ttrn_completed_date = new Date();
				$scope.subTaskCompletion.ttrn_next_examination_date = new Date();
			} 

		}

		$scope.saveSubTaskCompletion= function(formValid){
			//alert("In Complete sub task");
			if(formValid && $scope.validateSubTaskDates()){
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
				$scope.subTaskCompletion.ttrn_next_examination_date = moment($scope.subTaskCompletion.ttrn_next_examination_date).format('DD-MM-YYYY');
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
						//$('#SubTaskCompletion').modal("hide");
						toaster.success("Success", "Task completed successfully.");
						$window.location.reload();
						//$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
					}else{
						toaster.error("Failed", "Please try again.");
					}
				}).error(function(error){
					spinnerService.hide('html5spinner');
					console.log("task Completion====="+error);
				});


			}
		}

		$scope.downloadProof = function(udoc_id){
			$window.location = "./downloadProofOfCompliance?udoc_id="+udoc_id;
			$state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});
		}
}]);