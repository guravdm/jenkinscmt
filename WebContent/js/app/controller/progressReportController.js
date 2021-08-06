'use strict';

CMTApp.controller('progressReportController', ['$scope','$rootScope','$stateParams', '$state', 'ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$uibModal', '$filter','$window', '$http', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService, $uibModal, $filter,$window, $http) {

	var role = Storage.get('userDetais.sess_role_id');
	console.log('role : ' + role);


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


	$scope.dataSource = [];

	$("#tblLoader").hide();

	$scope.progressReport = function () {
		
		/*DataFactory.setShowLoader(true);*/
		
		// $("#tblLoader").show();
		
		spinnerService.show('html5spinner');

		$scope.frmSearchDate = $filter('date')($scope.date_from, 'yyyy-MM-dd');
		$scope.toSearchDate = $filter('date')($scope.date_to, 'yyyy-MM-dd');
		var obj = {
				fromDate : $scope.frmSearchDate,
				toDate : $scope.toSearchDate
		}

		DataFactory.setShowLoader(true);
		ApiCallFactory.searchProgressReport(obj).success(function(res, status){
			spinnerService.show('html5spinner');
			if(status === 200){
				spinnerService.hide('html5spinner');
//				$("#tblLoader").hide();
				$scope.dataSource = res.taskList;
			}else if(status == 404){
//				$("#tblLoader").hide();
				spinnerService.hide('html5spinner');
				toaster.error("Failed", "URL Incorrect");
			}else {
//				$("#tblLoader").hide();
				spinnerService.hide('html5spinner');
				toaster.error("Failed", "Something went wrong while fetching progress report ");
			}
		}).error(function(error){
//			$("#tblLoader").hide();
			spinnerService.hide('html5spinner');
			console.log("progress report : " + error);
		});

	}


	$scope.ExportExcels_Maintask=function(){
		console.log('clicked me!');
		$("#example tr td a").removeAttr("href");
		$("#example tr td a").css("color","black");
		$("#example").table2excel({		
			name: "Report",
			filename: "ProgressReport_(Main Task)" 
		}); 
	}


}]);