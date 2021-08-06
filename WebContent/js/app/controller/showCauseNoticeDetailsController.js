'use strict';

CMTApp.controller('showCauseNoticeDetailsController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService,$window) {

	
	$scope.showCauseNoticeAction={};
	$scope.statusDetailsClick = 0;

	$scope.showCauseNoticeAction.tscn_scau_id = $stateParams.scau_id;
	
	console.log("staus id is "+$scope.showCauseNoticeAction.tscn_scau_id);
	
	if(!angular.isUndefined($stateParams.scau_id) && $stateParams.scau_id!=0){
		$scope.showCauseNoticeAction.tscn_scau_id = $stateParams.scau_id;
		$window.sessionStorage.setItem('tscn_scau_id',$stateParams.scau_id);
	}else{
	    	if(!angular.isUndefined($window.sessionStorage.getItem('tscn_scau_id')) && $window.sessionStorage.getItem('tscn_scau_id')!=0){
	    		$scope.showCauseNoticeAction.tscn_scau_id = $window.sessionStorage.getItem('tscn_scau_id');
	    		console.log("status 1 is : "+$scope.showCauseNoticeAction.tscn_scau_id);
	    	}
		   // $window.sessionStorage.removeItem('task_id');
	}
	
	$scope.statusForNotice=[{id:"Open",name:"Open"},{id:"Reply_Sent",name:"Reply Sent"},{id:"Closed",name:"Closed"}];
	
	$scope.format = 'dd-MM-yyyy'; //$scope.formats[0];
	 $scope.dueDate = function() {
		    $scope.popup_next_due_date.opened = true;
		  };
	 
		 $scope.popup_next_due_date = {
			   opened: false
			  };
		  
		 $scope.reminder = function() {
				$scope.popup_reminder_date.opened = true;
		   };
		   
		   $scope.popup_reminder_date = {
				   opened: false
		  };
	
		   $scope.validateDates = function() {
			    $scope.errMessage = {};
			    var notice_date = null;
			    var received_date = null;
			    
				var from = angular.isDefined($scope.showCauseNoticeAction.tscn_next_due_date);
				if(from)
					notice_date = new Date($scope.showCauseNoticeAction.tscn_next_due_date);
				else
					$scope.errMessage.tscn_next_due_date = 'Please select next due date.';
				
				
				var to = angular.isDefined($scope.showCauseNoticeAction.tscn_reminder_date);
				if(to)
					received_date = new Date($scope.showCauseNoticeAction.tscn_reminder_date);
				else
					$scope.errMessage.tscn_reminder_date = 'Please select reminder date.';
				
			     var flag = true;
				if(from && to){
					if(received_date >= notice_date){ 
				     	$scope.errMessage.tscn_reminder_date = 'Remainder date must be less than next due date.'; 
				     	flag = false;
					}else
						 flag = true;
					
				}
				
				if(from && to && flag)
				    return true;
				else
					return false;
		 }
		   
		   //moment($scope.showCauseNoticeAction.remainderDate).format('DD-MM-YYYY')
		   $scope.createShowCuaseNoticeAction= function(formValid){
			  
			   if(formValid && $scope.validateDates()){
				   spinnerService.show('html5spinner');
				   $scope.showCauseNoticeAction.tcau_status = $scope.showCauseNoticeAction.tcau_status;
				   $scope.showCauseNoticeAction.tscn_action_taken = $scope.showCauseNoticeAction.tscn_action_taken;
				   $scope.showCauseNoticeAction.tscn_next_action_item = $scope.showCauseNoticeAction.tscn_next_action_item;
				   $scope.showCauseNoticeAction.tscn_comment = $scope.showCauseNoticeAction.tscn_comment;
				   $scope.showCauseNoticeAction.tscn_next_due_date = moment($scope.showCauseNoticeAction.tscn_next_due_date).format('DD-MM-YYYY');
				   $scope.showCauseNoticeAction.tscn_reminder_date = moment($scope.showCauseNoticeAction.tscn_reminder_date).format('DD-MM-YYYY');
				   
				   $scope.stringifyshowCauseNoticeAction= JSON.stringify($scope.showCauseNoticeAction)
					var formData = new FormData();
					if(!angular.isUndefined($scope.showCauseNoticeAction.action_document)){
						for (var i=0; i<$scope.showCauseNoticeAction.action_document.length; i++) {
							formData.append("action_document", $scope.showCauseNoticeAction.action_document[i]);
						}
					} 
					formData.append("jsonString",$scope.stringifyshowCauseNoticeAction);
					
					ApiCallFactory.saveActionItem(formData).success(function(res,status){
						spinnerService.hide('html5spinner');
						if(status==200 && res.responseMessage=='Success'){
							toaster.success("Success", "Show cause notice action created.");
							$state.go('showCauseNoticeDetails');
						}else{
							toaster.error("Failed", "Please try again.");
						}
					});
			   }
		   }
		   
		   
		   $scope.getAllActionItem=function(){
				var obj={scau_id:$scope.showCauseNoticeAction.tscn_scau_id};
				spinnerService.show('html5spinner');
				ApiCallFactory.getAllActionItem(obj).success(function(res,status){
					spinnerService.hide('html5spinner');
					if(status==200){
						$scope.showCauseNoticeActionHistory=res.action_list;
					}
				}).error(function(error){
					spinnerService.hide('html5spinner');
					console.log("get user list====="+error);
				});
			};
			$scope.getAllActionItem();
			
			
			//Get show cause notice details
			$scope.getTaskDetails=function(){ 
				if($scope.statusDetailsClick==0){ //If click status is 0 then only get details from DB
					spinnerService.show('html5spinner');
					var obj={scau_id:$scope.showCauseNoticeAction.tscn_scau_id};
					ApiCallFactory.getShowCauseNoticeDetails(obj).success(function(res,status){
						spinnerService.hide('html5spinner');
						$scope.ShowCauseNoticeDetails = res.ShowCauseNoticeDetails;
						$scope.statusDetailsClick = 1;
					}).error(function(error){
						spinnerService.hide('html5spinner');
						console.log("task Details====="+error);
					});
			      
				}
			}
			
			
			$scope.downloadProof = function(doc_id){
				console.log()
	            $window.location = "./downloadShowCauseDocument?doc_id="+doc_id;
	            //$window.location.reload();
	            //$state.go('listShowCauseNotice');
		}
}]);