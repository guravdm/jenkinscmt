'use strict';

CMTApp.controller('AnalyticalDashboardController', ['$scope','$rootScope','$stateParams', '$state', 'ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$uibModal', '$filter', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService, $uibModal, $filter) {

	$scope.taskList = {};
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;


	$scope.getOverDashboardCount = function(){

		$scope.frmSearchDate = $filter('date')($scope.date_from, 'yyyy-MM-dd');
		$scope.toSearchDate = $filter('date')($scope.date_to, 'yyyy-MM-dd');
		// console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);
		// console.log('frmSearchDate : ' + $scope.frmSearchDate + '\t toSearchDate : ' + $scope.toSearchDate);

		var obj={
				date_from: $scope.frmSearchDate,
				date_to: $scope.toSearchDate
		};

		$scope.loading=true;
		ApiCallFactory.getOverDashboardCount(obj).success(function(res, status){			
			// $scope.loading = false;
			DataFactory.setShowLoader(false);
			$scope.taskList = res.taskList;
			$scope.overallChart($scope.taskList);
			console.log('count list : ' + JSON.stringify($scope.taskList));
		}).error(function(error){
			spinnerService.hide('html5spinner');
			$scope.loading=false;
			console.log("get count list====="+error);
		});
	};
	$scope.getOverDashboardCount();



	/**
	 * Grpahs started
	 */
	$scope.complianceChartData = [];
	$scope.overallChart = function(data) {
		$scope.complianceChartData = data;
		console.log('Data : ' + $scope.complianceChartData[0].Complied);

		$('#overallChart').jqChart({
			title: {
				text: 'Pie Chart'
			},
			legend: {
				title: 'Countries'
			},
			animation: {
				duration: 1
			},
			series: [
				{
					type: 'pie',
					class: 'mySeries',
					labels: {
						stringFormat: '%.1f%%',
						valueType: 'percentage'
					},
					explodedRadius: 10,
					explodedSlices: [6],
					data: [['Complied', $scope.complianceChartData[0].Complied], 
						['Posing', $scope.complianceChartData[0].PosingRisk], 
						['Non-Complied', $scope.complianceChartData[0].NonComplied], 
						['Delayed', $scope.complianceChartData[0].Delayed], 
						['Delayed-Reported', $scope.complianceChartData[0].DelayedReported], 
						['Re-Opened', $scope.complianceChartData[0].ReOpened]]
				}
				]
		})


	}

	$scope.overallChart();


	/**
	 * End
	 */

	$scope.tmapObj = {};
	$scope.NtmapObj = {};
	$scope.errMessage = {};
	$scope.lega_date={};

	$scope.getDates =function(){

		if($scope.tmapObj.ttrn_prior_days_buffer<0)
			$scope.tmapObj.ttrn_prior_days_buffer = 0;

		$scope.unit_date = new Date($scope.lega_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.func_date = new Date($scope.unit_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.eval_date = new Date($scope.func_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.exec_date = new Date($scope.eval_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
	}


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




}]);