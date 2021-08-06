'use strict';

CMTApp.controller('createShowCauseNoticeController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService,$window) {

	console.log("In show cause notice");
	
	$scope.searchObj={};
	$scope.showCauseNotice={};
	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.originalUsersList=[];
	
	$scope.proofCompliance = {};
	
	$scope.showCauseNotice.tscn_scau_id = $stateParams.scau_id;
	
	console.log("status 1 for editing is : "+$scope.showCauseNotice.tscn_scau_id);
	
	$scope.showCauseNotice.scau_action_taken = "Yes";
	
	 $scope.format = 'dd-MM-yyyy'; //$scope.formats[0];
	 $scope.noticeDate = function() {
		    $scope.popup_notice_date.opened = true;
		  };
	 
		 $scope.popup_notice_date = {
			   opened: false
			  };
		  
	  $scope.receivedOn = function() {
		    $scope.popup_received_on.opened = true;
		   };
		   
	$scope.popup_received_on = {
		   opened: false
		  };
	
	$scope.replyDeadline = function() {
	    $scope.popup_reply_deadline.opened = true;
	   };
	   
	$scope.popup_reply_deadline = {
	   opened: false
	  };

	$scope.reminderRequired = function() {
		$scope.popup_reminder_required.opened = true;
   };
   
   $scope.popup_reminder_required = {
		   opened: false
  };
	
	
	$scope.getAccessWiseOrgaLocaDept=function(){
		var obj=0;
		ApiCallFactory.getAccessWiseOrgaLocaDept(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
			$scope.originalEntityList=res.assignDropDowns[0].Entity;
			$scope.entityList=res.assignDropDowns[0].Entity;
			$scope.originalUnitList=res.assignDropDowns[0].Unit;
			$scope.originalFunctionList=res.assignDropDowns[0].Function;
			$scope.originalUsersList=res.assignDropDowns[0].UserList;
			
			$scope.getEntityDependentArray();
			$scope.getUnitDependentArray();
			$scope.getFunctionDependentArray();
			//console.log('res.All_Tasks'+JSON.stringify(res.All_Tasks));
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get access wise orga loca list====="+error);
		});
	}
	$scope.getAccessWiseOrgaLocaDept();
	
	
	$scope.getEntityDependentArray = function(){
		$scope.unitList=[];
		console.log("search Object: "+JSON.stringify($scope.searchObj));
		if($scope.searchObj.orga_id!="" && $scope.originalUnitList.length!=0){
			angular.forEach($scope.originalUnitList, function (item) {
				if( item.orga_id == $scope.searchObj.orga_id){
					$scope.unitList.push(item);
				}

			});
		};

		
		///Add executor and Evaluator
		$scope.executorList=[];
		$scope.evaluatorList=[];
		var executor_array = [];
		var evaluator_array = [];
		if($scope.searchObj.orga_id!="" && $scope.originalUsersList.length!=0){
			angular.forEach($scope.originalUsersList, function (item) {
				
				if(item.orga_id == $scope.searchObj.orga_id){
					 if ($.inArray(item.user_id, executor_array)==-1) {
					    	executor_array.push(item.user_id);
					        $scope.executorList.push(item);
					    }
					}

				if(item.orga_id == $scope.searchObj.orga_id){
					if ($.inArray(item.user_id, evaluator_array)==-1) {
						evaluator_array.push(item.user_id);
						$scope.evaluatorList.push(item);
					}
				}

			});
		};
	}
	
	

	$scope.getUnitDependentArray= function(){
		$scope.functionList=[];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.originalFunctionList.length!=0){
			angular.forEach($scope.originalFunctionList, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					$scope.functionList.push(item);
				}

			});
		};


		///Add executor and Evaluator
		$scope.executorList=[];
		$scope.evaluatorList=[];
		var executor_array = [];
		var evaluator_array = [];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalUsersList.length!=0){
			angular.forEach($scope.originalUsersList, function (item) {
				
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					if ($.inArray(item.user_id, executor_array)==-1) {
						executor_array.push(item.user_id);
						$scope.executorList.push(item);
					}
				}
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					if ($.inArray(item.user_id, evaluator_array)==-1) {
						evaluator_array.push(item.user_id);
						$scope.evaluatorList.push(item);
					}
				}

			});
		};
	}

	$scope.getFunctionDependentArray= function(){
		$scope.executorList=[];
		$scope.evaluatorList=[];
		var executor_array = [];
		var evaluator_array = [];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.searchObj.dept_id!="" && $scope.originalUsersList.length!=0){
			angular.forEach($scope.originalUsersList, function (item) {
				
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.user_id, executor_array)==-1) {
						executor_array.push(item.user_id);
						$scope.executorList.push(item);
					}
				}
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id) && (item.dept_id == $scope.searchObj.dept_id)){
					if ($.inArray(item.user_id, evaluator_array)==-1) {
						evaluator_array.push(item.user_id);
						$scope.evaluatorList.push(item);
					}
				}

			});
		};
	}
	
	
	$scope.validateDates = function() {
		 var flag = false;
	    $scope.errMessage = {};
	    var notice_date = null;
	    var received_date = null;
	    var deadline_date = null;
	    var reminder_date = null;
	    
		var from = angular.isDefined($scope.showCauseNotice.scau_notice_date);
		if(from)
			notice_date = new Date($scope.showCauseNotice.scau_notice_date);
		else
			$scope.errMessage.scau_notice_date = 'Please select notice date.';
		
		var to = angular.isDefined($scope.showCauseNotice.scau_received_date);
		if(to)
			received_date = new Date($scope.showCauseNotice.scau_received_date);
		else
			$scope.errMessage.scau_received_date = 'Please select received on date.';
		
		var deadline = angular.isDefined($scope.showCauseNotice.scau_deadline_date);
		if(deadline)
			deadline_date = new Date($scope.showCauseNotice.scau_deadline_date)
		else
			$scope.errMessage.scau_deadline_date = 'Please select notice reply deadline date'
		
		
		
			
	    
		if(from || to || deadline){
			/*if(notice_date > received_date){ 
	     	   $scope.errMessage.scau_notice_date = 'Notice date must be less than "Recived On" date.';
	     	    flag = false;
			}else
				 flag = true;*/
			
			if(received_date < notice_date){ 
		     	$scope.errMessage.scau_received_date = 'Recived On date must be greater than "Notice" date.'; 
		     	flag = false;
			}else
				 flag = true;
			
			if(received_date < notice_date || received_date > deadline_date){
				$scope.errMessage.scau_deadline_date = 'Notice reply deadline must be greater than received on date.';
				flag = false;
				}else
					 flag = true;
		}else
			flag = false;
		
		var reminder = angular.isDefined($scope.showCauseNotice.scau_remainder_date);
		if($scope.showCauseNotice.scau_remainder_date!=null && reminder && flag){
			reminder_date = new Date($scope.showCauseNotice.scau_remainder_date)
			if(reminder_date >= deadline_date || reminder_date <= received_date){
				$scope.errMessage.scau_remainder_date = 'Reminder date must be less than notice reply deadline and must be greater than received on date.'
					flag = false;
			}else
				flag = true; 
		}
		if(from && to && deadline && flag)
		    return true;
		else
			return false;
 }
	
	
	  $scope.actionTaken = function(){
		
		  if($scope.showCauseNotice.scau_action_taken = true)
			  $scope.showCauseNotice.scau_action_taken="Yes";
		  else
			  $scope.showCauseNotice.scau_action_taken="No";
	  }
	
	//Create Show Cause Notice
	$scope.createShowCuaseNotice= function(formValid){
		
		//$scope.showCauseNotice.scau_related_to==undefined
		
		if(formValid && $scope.validateDates()){
			if($scope.showCauseNotice.scau_ralated_to==undefined || $scope.showCauseNotice.scau_ralated_to == ""){
				$scope.showCauseNotice.scau_ralated_to="";
			}else
				$scope.showCauseNotice.scau_ralated_to=$scope.showCauseNotice.scau_ralated_to;
			
			if($scope.showCauseNotice.scau_comments==undefined || $scope.showCauseNotice.scau_comments == ""){
				$scope.showCauseNotice.scau_comments="";
			}else
				$scope.showCauseNotice.scau_comments=$scope.showCauseNotice.scau_comments;
			
			if($scope.showCauseNotice.scau_next_action_item==undefined || $scope.showCauseNotice.scau_next_action_item == ""){
				$scope.showCauseNotice.scau_next_action_item="";
			}else
				$scope.showCauseNotice.scau_next_action_item=$scope.showCauseNotice.scau_next_action_item;
			
			/*if($scope.showCauseNotice.scau_deadline_date==undefined  || $scope.showCauseNotice.scau_deadline_date == ""){
				$scope.showCauseNotice.scau_deadline_date="";
			}else
				$scope.showCauseNotice.scau_deadline_date=moment($scope.showCauseNotice.scau_deadline_date).format('DD-MM-YYYY');*/
			
			if($scope.showCauseNotice.scau_remainder_date==undefined || $scope.showCauseNotice.scau_remainder_date == ""){
				$scope.showCauseNotice.scau_remainder_date="";
			}else
				$scope.showCauseNotice.scau_remainder_date=moment($scope.showCauseNotice.scau_remainder_date).format('DD-MM-YYYY');
			
			
			spinnerService.show('html5spinner');
			$scope.showCauseNotice.scau_orga_id = $scope.searchObj.orga_id;
			$scope.showCauseNotice.scau_loca_id = $scope.searchObj.loca_id;
			$scope.showCauseNotice.scau_dept_id = $scope.searchObj.dept_id;
			//$scope.showCauseNotice.scau_ralated_to = $scope.showCauseNotice.scau_ralated_to;
			$scope.showCauseNotice.scau_notice_date = moment($scope.showCauseNotice.scau_notice_date).format('DD-MM-YYYY');
			$scope.showCauseNotice.scau_received_date = moment($scope.showCauseNotice.scau_received_date).format('DD-MM-YYYY');
			$scope.showCauseNotice.scau_deadline_date = moment($scope.showCauseNotice.scau_deadline_date).format('DD-MM-YYYY');
			$scope.showCauseNotice.scau_responsible_person = $scope.searchObj.executor_id;
			$scope.showCauseNotice.scau_reporting_person = $scope.searchObj.evaluator_id;
			
			$scope.stringifyshowCauseNotice= JSON.stringify($scope.showCauseNotice)
			var formData = new FormData();
			if(!angular.isUndefined($scope.proofCompliance.show_cause_doc)){
				for (var i=0; i<$scope.proofCompliance.show_cause_doc.length; i++) {
					formData.append("show_cause_doc", $scope.proofCompliance.show_cause_doc[i]);
				}
			} 
			formData.append("jsonString",$scope.stringifyshowCauseNotice);
			
			ApiCallFactory.saveShowCauseNotice(formData).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status==200 && res.responseMessage=='Success'){
					toaster.success("Success", "Show cause notice created.");
					$state.go('listShowCauseNotice');
				}else{
					spinnerService.hide('html5spinner');
					toaster.error("Failed", "Please try again.");
				}
			});
		}
		
	}
	
	//Get show cause notice details.
	$scope.showCauseNotice.tscn_scau_id = $stateParams.scau_id;	
	//$scope.showCauseNotice.scau_orga_id=$scope.searchObj.orga_id;
	$scope.showCauseNoticeDetails=function(){
		
		var obj={scau_id:$scope.showCauseNotice.tscn_scau_id};
		ApiCallFactory.getShowCauseNoticeDetails(obj).success(function(res,status){
			
			if(res.responseMessage!='Failed'){
				//$scope.ShowCauseNoticeDetails = res.ShowCauseNoticeDetails;
				$scope.searchObj.orga_id=res.ShowCauseNoticeDetails.orga_id;
				
				$scope.searchObj.orga_name=res.ShowCauseNoticeDetails.orga_name;
				$scope.searchObj.loca_id=res.ShowCauseNoticeDetails.loca_id;
				$scope.searchObj.loca_name=res.ShowCauseNoticeDetails.loca_name;
				$scope.searchObj.dept_id=res.ShowCauseNoticeDetails.dept_id;
				$scope.searchObj.dept_name=res.ShowCauseNoticeDetails.dept_name;
				$scope.showCauseNotice.scau_ralated_to=res.ShowCauseNoticeDetails.related_to;
				
				var from = res.ShowCauseNoticeDetails.notice_date.split("-");
				var f = new Date(from[2], from[1] - 1, from[0]);
				$scope.showCauseNotice.scau_notice_date= f;
				
				var from1 = res.ShowCauseNoticeDetails.recieved_date.split("-");
				var f1 = new Date(from1[2], from1[1] - 1, from1[0]);
				$scope.showCauseNotice.scau_received_date=f1;
				
				var from2 = res.ShowCauseNoticeDetails.deadline_date.split("-");
				var f2 = new Date(from2[2], from2[1] - 1, from2[0]);
				$scope.showCauseNotice.scau_deadline_date=f2;
				
				$scope.showCauseNotice.scau_comments=res.ShowCauseNoticeDetails.comments;
				$scope.showCauseNotice.scau_action_taken=res.ShowCauseNoticeDetails.action_taken;
				$scope.showCauseNotice.scau_next_action_item=res.ShowCauseNoticeDetails.next_action_item;
				$scope.searchObj.executor_id=res.ShowCauseNoticeDetails.responsible_user_id;
				//$scope.searchObj.ex.user_name=res.ShowCauseNoticeDetails.responsible_user_name;
				$scope.showCauseNotice.scau_reporting_person=res.ShowCauseNoticeDetails.reporting_user_name;
				$scope.searchObj.evaluator_id=res.ShowCauseNoticeDetails.reporting_user_id;
				
				var from3 = res.ShowCauseNoticeDetails.reminder_date.split("-");
				var f3 = new Date(from3[2], from3[1] - 1, from3[0]);
				$scope.showCauseNotice.scau_remainder_date=f3;
				
				$scope.getEntityDependentArray();
				$scope.getUnitDependentArray();
				$scope.getFunctionDependentArray();
				
			}
		})
		
		
	}
	/*$scope.formatDate = function(){
		
	}*/
	
	if(!angular.isUndefined($stateParams.scau_id) && $stateParams.scau_id!=0){
		console.log("In if");
		$scope.showCauseNoticeDetails();
	}
	
	//Update show cause notice.
	$scope.showCauseNotice.scau_id = $stateParams.scau_id;	
	console.log("SCAU ID is : "+$scope.showCauseNotice.scau_id);
	$scope.updateShowCauseNotice=function(formValid){
		console.log("VALID "+$scope.validateDates()+" FORM "+formValid);
		if(formValid && $scope.validateDates()){ 
			spinnerService.show('html5spinner');
			if($scope.showCauseNotice.scau_ralated_to==undefined || $scope.showCauseNotice.scau_ralated_to == ""){
				$scope.showCauseNotice.scau_ralated_to="";
			}else
				$scope.showCauseNotice.scau_ralated_to=$scope.showCauseNotice.scau_ralated_to;
			
			if($scope.showCauseNotice.scau_comments==undefined || $scope.showCauseNotice.scau_comments == ""){
				$scope.showCauseNotice.scau_comments="";
			}else
				$scope.showCauseNotice.scau_comments=$scope.showCauseNotice.scau_comments;
			
			if($scope.showCauseNotice.scau_next_action_item==undefined || $scope.showCauseNotice.scau_next_action_item == ""){
				$scope.showCauseNotice.scau_next_action_item="";
			}else
				$scope.showCauseNotice.scau_next_action_item=$scope.showCauseNotice.scau_next_action_item;
			
			if($scope.showCauseNotice.scau_remainder_date==undefined || $scope.showCauseNotice.scau_remainder_date == "" || isNaN($scope.showCauseNotice.scau_remainder_date)){
				$scope.showCauseNotice.scau_remainder_date="";
			}else
				$scope.showCauseNotice.scau_remainder_date=moment($scope.showCauseNotice.scau_remainder_date).format('DD-MM-YYYY');
			
			
			$scope.showCauseNotice.scau_orga_id = $scope.searchObj.orga_id;
			$scope.showCauseNotice.scau_loca_id = $scope.searchObj.loca_id;
			$scope.showCauseNotice.scau_dept_id = $scope.searchObj.dept_id;
			//$scope.showCauseNotice.scau_ralated_to = $scope.showCauseNotice.scau_ralated_to;
			$scope.showCauseNotice.scau_notice_date = moment($scope.showCauseNotice.scau_notice_date).format('DD-MM-YYYY');
			$scope.showCauseNotice.scau_received_date = moment($scope.showCauseNotice.scau_received_date).format('DD-MM-YYYY');
			$scope.showCauseNotice.scau_deadline_date = moment($scope.showCauseNotice.scau_deadline_date).format('DD-MM-YYYY');
			$scope.showCauseNotice.scau_responsible_person = $scope.searchObj.executor_id;
			$scope.showCauseNotice.scau_reporting_person = $scope.searchObj.evaluator_id;
			
			$scope.stringifyshowCauseNotice= JSON.stringify($scope.showCauseNotice)
			var formData = new FormData();
			if(!angular.isUndefined($scope.proofCompliance.show_cause_doc)){
				for (var i=0; i<$scope.proofCompliance.show_cause_doc.length; i++) {
					formData.append("show_cause_doc", $scope.proofCompliance.show_cause_doc[i]);
				}
			} 
			formData.append("jsonString",$scope.stringifyshowCauseNotice);
			
			ApiCallFactory.updateShowCauseNotice(formData).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status === 200 && res.responseMessage== "Success"){
					toaster.success("Success", "Show cause notice updated successfully");
					$state.go('listShowCauseNotice');
				}else{
					toaster.error("Faild", "Error in update");
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("update show cause notice===="+error);
			});
		}
	}
}]);