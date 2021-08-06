'use strict';

CMTApp.controller('FinalAuditReportController', ['$scope','$rootScope','$stateParams', '$state', 'ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$uibModal', '$filter','$window', '$http', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService, $uibModal, $filter,$window, $http) {

	var role = Storage.get('userDetais.sess_role_id');
	console.log('role : ' + role);

	var loca_type 		= [];
	$scope.entityListt  = [];
	$scope.taskObjPostAudit = {};
	$scope.searchObjFinalPostAudit = {};

	$scope.varEntity 	= [];
	$scope.UnitList     = [];
	$scope.FunctionList = [];
	$scope.objStatus = null;

	$scope.UnitList             = [];
	$scope.FunctionList         = [];


	$scope.reportList = [];

	$scope.finalComplianceAuditReport = function () {
		$scope.searchObjFinalPostAudit;

		spinnerService.show('html5spinner');

		$scope.frmSearchDate = $filter('date')($scope.searchObjFinalPostAudit.date_from, 'yyyy-MM-dd');
		$scope.toSearchDate = $filter('date')($scope.searchObjFinalPostAudit.date_to, 'yyyy-MM-dd');
		console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);
		console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);

		var obj = {
				date_from: $scope.frmSearchDate,
				date_to: $scope.toSearchDate,
				orgaId: $scope.searchObjFinalPostAudit.orga_id,
				locaId: $scope.searchObjFinalPostAudit.loca_id,
				deptId: $scope.searchObjFinalPostAudit.dept_id
		};

		ApiCallFactory.finalComplianceAuditReport(obj).success(function(res, status) {
			spinnerService.show('html5spinner');

			if(status === 200) {
				$scope.reportList = res.finalAuditReport;
				
				spinnerService.hide('html5spinner');
			}else {
				toaster.error("Failed", "Something went wrong while fetching");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("final audit report : " + error);
		});

	}


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


	/* Date Code start*/
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

		

	}

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
	
	
	
//	PDF Code
	
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
			title: 'Compliance Management Final Audit Tool Report',
			subject: 'Report',
			author: 'Lexcare',
			keywords: 'lexcare compliance tool',
			creator: 'Compliance Management Tool'

		});
		doc1.cellInitialize();
		doc1.save('final_audi_report.pdf');	
	}
	
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
			sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"finalReport"+d+"(Main Task).xls");
		}  
		else{
			// sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"IE Excel Export.xls");
			$("#example tr td a").removeAttr("href");
			$("#example tr td a").css("color","black");
			$("#example").table2excel({		
				name: "Report",
				filename: "finalReport"+d+"(Main Task)" 
			}); 

		} 

	}

}]);