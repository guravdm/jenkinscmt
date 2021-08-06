'use strict';

CMTApp.controller('addDocumentsController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', '$filter', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window, $filter) {

	var roleId = Storage.get('userDetais.sess_role_id');
	
	$scope.roleId = roleId;
	
	console.log('role : ' + roleId);
	
	$scope.searchObj = {};
	$scope.proofCompliance = {};

	
	$scope.getTaskActivityList = function(){
		$scope.loading=true;
		ApiCallFactory.getTaskActivityList().success(function(res, status){			
			$scope.loading = false;
			DataFactory.setShowLoader(false);
			$scope.taskActivityList = res.tasksRepo;
		}).error(function(error){
			spinnerService.hide('html5spinner');
			$scope.loading=false;
			console.log("get user list====="+error);
		});
	};
	$scope.getTaskActivityList();
	
	
	$scope.getDocumentLists = function(){
		$scope.loading=true;
		ApiCallFactory.listSimplyCompDocuments().success(function(res, status){			
			$scope.loading = false;
			DataFactory.setShowLoader(false);
			$scope.docLists = res.docrepos;
		}).error(function(error){
			spinnerService.hide('html5spinner');
			$scope.loading=false;
			console.log("get user list====="+error);
		});
	};
	$scope.getDocumentLists();
	
	$scope.downloadComplianceDocument = function(docId) {
		console.log('docId : ' + docId);
		// downloadProofOfCompliance
		$window.location = "./downloadComplianceDocument?docId="+docId;
	}
	
	
	
	$scope.submitPODocuments = function() {

		$scope.frmDate = $filter('date')($scope.date_from, 'yyyy-MM-dd');
		$scope.tDate = $filter('date')($scope.date_to, 'yyyy-MM-dd');
		console.log('from date : ' + $scope.frmDate);

		/*var obj = {
				docName : $scope.searchObj.docName,
				docDescription : $scope.searchObj.docDescription,
				fromDate : $scope.frmDate,
				toDate : $scope.tDate
		}*/
		$scope.searchObj.docName = $scope.searchObj.docName;
		$scope.searchObj.docDescription = $scope.searchObj.docDescription;
		$scope.searchObj.tskState = $scope.searchObj.tskState;
		$scope.searchObj.date_from = $filter('date')($scope.date_from, 'yyyy-MM-dd'); // moment($scope.searchObj.date_from).format('YYYY-MM-DD');
		$scope.searchObj.date_to = $filter('date')($scope.date_to, 'yyyy-MM-dd'); // moment($scope.searchObj.date_to).format('YYYY-MM-DD');

		$scope.stringifyTaskCompletion= JSON.stringify($scope.searchObj)
		var formData = new FormData();
		console.log('file : ' + $scope.proofCompliance.ttrn_proof_of_compliance);
		if(!angular.isUndefined($scope.proofCompliance.ttrn_proof_of_compliance)){
			for (var i=0; i<$scope.proofCompliance.ttrn_proof_of_compliance.length; i++) {
				formData.append("ttrn_proof_of_compliance", $scope.proofCompliance.ttrn_proof_of_compliance[i]);
			}
		}  

		formData.append("jsonString", $scope.stringifyTaskCompletion);
		console.log('proofCompliance : ' + formData);

		ApiCallFactory.submitSimplyCompDocuments(formData).success(function(res, status){
			spinnerService.hide('html5spinner');
			if(res.responseMessage=='Success'){
				toaster.success("Success", "Document Added Successfully.");
				$scope.getDocumentLists();
			}else if(res.responseMessage == 'Invalid File Type'){
				toaster.error("Invalid File Type");
				$scope.getDocumentLists();
			}else{
				toaster.error("Failed", "Please try again.");
				$scope.getDocumentLists();
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			toaster.error("Failed", "something went wrong");
			console.log("something went wrong! " + error);
		});

	}

	$scope.downloadDocument = function(doc_id){
		$window.location = "./downloadProofOfCompliance?udoc_id="+doc_id;
	}


	$scope.errMessage = {};
	$scope.lega_date={};

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
			$scope.errMessage.pr_date = 'Report date must be less than OR equal to Evaluator date.';
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