'use strict';

CMTApp.controller('generateReportsController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService,$window) {

	$scope.userObj={};

	$scope.tmapObj={
			ChooseROrL :'reportPeriod',
			legal_status:"NA",
			entity_id:0,
			unit_id:0,
			func_id:0,
			exec_id:0,
			eval_id:0,
			from_date:"",
			to_date:"",
			task_impact:"NA"

	};

	$scope.tmapObj.ChooseROrL ='reportPeriod';
	$scope.from_date = new Date();
	$scope.to_date = new Date();
	$scope.max_date = new Date();
	//$scope.searchObj.legalDateFrom = new Date();
	//$scope.searchObj.legalDateTo = new Date();


	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.impactList = [{id:"Severe",name:"Severe"},{id:"Major",name:"Major"},{id:"Moderate",name:"Moderate"},{id:"Low",name:"Low"}];
	$scope.statusList = [{id:"NA",name:"NA"},{id:"Complied",name:"Complied"},{id:"nonComplied",name:"Non-Complied"},{id:"Delayed",name:"Delayed"}]


	$scope.getEntityUnitFunctionDesignationListReport=function(){
		var obj={}
		ApiCallFactory.getEntityUnitFunctionDesignationListReport(obj).success(function(res,status){
			$scope.originalEntityList=res.Entity;
			$scope.entityList=res.Entity;
			$scope.originalUnitList=res.Unit;
			$scope.originalFunctionList=res.Function;
			//console.log('res.Function');
		}).error(function(error){
			console.log("getEntityUnitFunctionDesignationListReport====="+error);
		});
	};
	$scope.getEntityUnitFunctionDesignationListReport();


	$scope.getEntityDependentArray = function(){
		$scope.unitList=[];
		if($scope.tmapObj.entity_id!="" && $scope.originalUnitList.length!=0){
			angular.forEach($scope.originalUnitList, function (item) {
				if( item.orga_id == $scope.tmapObj.entity_id){
					$scope.unitList.push(item);

				}
			});
		};
	}

	$scope.getUnitDependentArray= function(){
		$scope.functionList=[];
		if($scope.tmapObj.entity_id!="" && $scope.tmapObj.unit_id!="" && $scope.originalFunctionList.length!=0){
			angular.forEach($scope.originalFunctionList, function (item) {
				if( (item.orga_id == $scope.tmapObj.entity_id) && (item.loca_id == $scope.tmapObj.unit_id)){
					$scope.functionList.push(item);
				}

			});
		};
	}


	//getTaskList
	$scope.getTaskList = function(formValid) {
		spinnerService.show('html5spinner');
		console.log("id "+$scope.tmapObj.entity_id);
		console.log("status "+$scope.tmapObj.legal_status);
		if(angular.isUndefined($scope.tmapObj.entity_id) || $scope.tmapObj.entity_id==null){
			$scope.tmapObj.entity_id=0;
		}
		if(angular.isUndefined($scope.tmapObj.unit_id) || $scope.tmapObj.unit_id==null){
			$scope.tmapObj.unit_id=0;
		}
		if(angular.isUndefined($scope.tmapObj.func_id) || $scope.tmapObj.func_id==null){
			$scope.tmapObj.func_id=0;
		}
		if(angular.isUndefined($scope.tmapObj.legal_status) || $scope.tmapObj.legal_status==null){
			$scope.tmapObj.legal_status="NA";
		}
		$scope.tmapObj.from_date 	= moment($scope.from_date).format('DD-MM-YYYY');
		$scope.tmapObj.to_date 	= moment($scope.to_date).format('DD-MM-YYYY');

		if(angular.isUndefined($scope.tmapObj.task_impact) || $scope.tmapObj.task_impact==null){
			$scope.tmapObj.task_impact="NA";
		}

		ApiCallFactory.generateReports($scope.tmapObj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200) {
				if(res.errorMessage!="Failed") {
					$scope.taskList = res.reportList;
					$scope.subTaskList = res.reportSubTaskList;
				} else {
					console.log(" getTaskList =====error");
				}
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log(" getTaskList ====="+error);
		});
	};

	//toggle function for export task.
	$scope.ExportTask=true;
	$scope.toggle=function(){
		$scope.ExportTask = !$scope.ExportTask;
	}


	//to generate PDF for Main Tasks
	$scope.generateMaintask_Pdf=function(){
		var doc1 = new jsPDF('p', 'pt', 'a4');
		var res1 = doc1.autoTableHtmlToJson(document.getElementById("reportList1"));
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
		doc1.save('Main_Task_Report.pdf');				
	}


	// to generate main tasks in CSV format
	$scope.ExportExcels_Maintask=function(){
		/*var Curr_Date = new Date();
				var Curr_date1= moment(Curr_Date).format('DD-MM-YYYY');*/
		//$('#reportList1').tableExport({type:'excel',escape:'false'});		
		$("#reportList1 tr td a").removeAttr("href");
		$("#reportList1 tr td a").css("color","black");
		$("#reportList1").table2excel({		
			name: "Report",
			filename: "Report_(Main Task)" 
		}); 
	}

	//Generate PDF for Sub Tasks
	$scope.generateSubtask_Pdf=function(){
		var doc1 = new jsPDF('p', 'pt', 'a4');
		var res1 = doc1.autoTableHtmlToJson(document.getElementById("reportList2"));
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
		doc1.save('Sub_Task_Report.pdf');				
	}


	// to generate sub tasks in CSV format
	$scope.ExportExcels_Subtask=function(){
		/*var Curr_Date = new Date();
					var Curr_date1= moment(Curr_Date).format('DD-MM-YYYY');*/
		//$('#reportList1').tableExport({type:'excel',escape:'false'});		
		$("#reportList2 tr td a").removeAttr("href");
		$("#reportList2 tr td a").css("color","black");
		$("#reportList2").table2excel({		
			name: "Report",
			filename: "Report_(Sub Task)" 
		}); 
	}


	$scope.downloadProof = function(udoc_id){
		$window.location = "./downloadProofOfCompliance?udoc_id="+udoc_id;
		$state.transitionTo('generateReports', {'task_id':$window.sessionStorage.getItem('task_id')});
	}

	$scope.exportData = function() {

		ApiCallFactory.getExportReportById($scope.tmapObj).success(function(res,status){
			//spinnerService.show('html5spinner');
			if(status==200){

				$window.location = "./getExportReport";
				$state.transitionTo('generateReports');

			}
		}).error(function(error){

			spinnerService.hide('html5spinner');
			//console.log("get repository list====="+error);
		});
	}
}]);