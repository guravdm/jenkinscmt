'use strict';

CMTApp.controller('taskController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','$window','spinnerService','$mdDialog','$timeout', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,$window,spinnerService,$mdDialog,$timeout) {
	
	$scope.deleteDocument = {}
	
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
	
	//$scope.eventNotOccured=false;
	
	$scope.task_details = [];
	$scope.taskCompletion = {};
	$scope.subTaskCompletion = {};
	$scope.reOpen = {};
	$scope.taskCompletion.ttrn_completed_date = new Date();
	$scope.currentDate = new Date();
	console.log("Todays Date is : "+$scope.currentDate);
	
	$scope.multipleCompletion = {};
	
	//$scope.taskCompletion.ttrn_ids = [];
	$scope.task_ttrn_ids = [];
	
	$scope.statusDetailsClick = 0;
	
	if(!angular.isUndefined($stateParams.task_id) && $stateParams.task_id!=0){
		$scope.task.task_id=$stateParams.task_id;
		$window.sessionStorage.setItem('task_id',$stateParams.task_id);
	}else{
	    	if(!angular.isUndefined($window.sessionStorage.getItem('task_id')) && $window.sessionStorage.getItem('task_id')!=0){
	    		$scope.task.task_id=$window.sessionStorage.getItem('task_id');
	    	}
		   // $window.sessionStorage.removeItem('task_id');
	}
	
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
	
	if(!angular.isUndefined($stateParams.task_id) && $stateParams.task_id!=0){
		$scope.multipleCompletion.tmap_client_task_id = $stateParams.task_id;
	}
	
	if(!angular.isUndefined($stateParams.ttrn_id) && $stateParams.ttrn_id!=0){
		$scope.multipleCompletion.ttrn_id=$stateParams.ttrn_id;
	}
	
	$scope.status=$stateParams.status
	console.log("staus is "+$scope.status);
	
	$scope.frequency=$stateParams.frequency
	//console.log("frequency is"+$scope.frequency);
	
	$scope.frequency=$stateParams.frequency
	//console.log("frequency is"+$scope.frequency);
	
	//$scope.legalDueDate
	if(!angular.isUndefined($stateParams.legalDueDate) && $stateParams.legalDueDate!=0){
		var legal = $stateParams.legalDueDate.split("-");
		var legalDueDate = new Date(legal[2], legal[1] - 1, legal[0]);
		$scope.legalDueDate= legalDueDate;
		
	}else{
		
	}
	console.log("Legal Due Date is : "+$scope.legalDueDate);
	
	if(!angular.isUndefined($stateParams.reason) || $stateParams.reason!=''){
		$scope.taskCompletion.ttrn_reason_for_non_compliance=$stateParams.reason;
	}else{
		//$scope.taskCompletion.ttrn_performer_comments=$stateParams.comments;
	}
	
	
	
	//console.log("STAte Date     "+$stateParams.completedDate);
	if(!angular.isUndefined($stateParams.completedDate) && $stateParams.completedDate!=0){
		var from = $stateParams.completedDate.split("-");
		var completedDate = new Date(from[2], from[1] - 1, from[0]);
		
		$scope.taskCompletion.ttrn_completed_date= completedDate;
	}else{
		//$scope.taskCompletion.ttrn_performer_comments=$stateParams.completedDate;
	}
	
	//console.log("TASK Cooments "+$scope.taskCompletion.ttrn_performer_comments);
	//console.log("TASK Date     "+$scope.taskCompletion.ttrn_completed_date);
	
	//get task history
	$scope.getTaskHistoryList=function(){
		var obj={tmap_client_task_id:$scope.task.task_id};
		if(!angular.isUndefined($scope.task.task_id) && $scope.task.task_id!=0){
			ApiCallFactory.getTasksHistory(obj).success(function(res,status){
				$scope.task_history = res.task_history;
				$scope.subTaskHistory = res.subTaskHistory;
				
				$scope.editDate={
						role:Storage.get('userDetais.sess_role_id'),
				}
				//console.log("role "+$scope.editDates.role);

				//$scope.task_history.documents = 
			}).error(function(error){
				console.log("task History====="+error);
			});
	      }
	}
	
	
	$scope.getTaskHistoryList();
	
	//get task history
	$scope.getTaskDetails=function(){ 
		
		if($scope.statusDetailsClick==0){ //If click status is 0 then only get details from DB
			spinnerService.show('html5spinner');
		var obj={tmap_client_task_id:$scope.task.task_id};
		if(!angular.isUndefined($scope.task.task_id) && $scope.task.task_id!=0){
			ApiCallFactory.getTasksDetails(obj).success(function(res,status){
				spinnerService.hide('html5spinner');
				$scope.task_details = res;
				$scope.statusDetailsClick = 1;
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("task Details====="+error);
			});
	      }
		}
	}
	
	
	//$scope.eventNotOccured     = false;
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
	$scope.CompleteTaskPage=function(ttrn_id,document,comments,completedDate,status,reason,frequency,legalDueDate){ //document contain 1 - mandatory doc and 0 - Non mandatory
		$state.transitionTo('completeTask', {'ttrn_id':ttrn_id,'task_id':$scope.task.task_id,'document':document,'comments':comments,'completedDate':completedDate,'status':status,'reason':reason,'frequency':frequency,'legalDueDate':legalDueDate});
	};
	
	
	
	$scope.saveCompletion= function(formValid){
		if(formValid && !angular.isUndefined($scope.task.ttrn_id) && $scope.task.ttrn_id!=0){
			spinnerService.show('html5spinner');
			var obj= {ttrn_id:$scope.task.ttrn_id};
			$scope.task_ttrn_ids.push(obj);
			
			$scope.taskCompletion.ttrn_ids = $scope.task_ttrn_ids;
			$scope.taskCompletion.ttrn_completed_date = moment($scope.taskCompletion.ttrn_completed_date).format('DD-MM-YYYY');
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
				spinnerService.hide('html5spinner');
				if(res.responseMessage=='Success'){
					toaster.success("Success", "Task completed successfully.");
					$state.transitionTo('taskDetails', {'task_id':$scope.task.task_id});
				}else{
					toaster.error("Failed", "Please try again.");
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("task Completion====="+error);
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
	
	//Approve task
	 $scope.approveTask=function(ttrn_id){
		// console.log(task_id);
		 $scope.taskCompletion.ttrn_id=ttrn_id;
	  		ApiCallFactory.approveTask($scope.taskCompletion).success(function(res,status){
	  			if(status === 200 && res.responseMessage == "Success"){
	  				toaster.success("Success", "Task approved successfully.");
	  				$window.location.reload();
	  			}	
	  		}).error(function(error){
				console.log("Error while approving the task====="+error);
			});
	  	
	 }
	 
	 $scope.deleteTask=function(ttrn_id,client_task_id,ev,index){
		    var confirm = $mdDialog.confirm()
		          .title('Are you sure you want to delete the task?')
		          .targetEvent(ev)
		          .ok('Yes')
		          .cancel('NO');
		    $mdDialog.show(confirm).then(function() {
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
		    });
		 }
	 
	 //get ttrn_id and reopening comments 	
	 $scope.reOpen={
			 ttrn_id : 0,
			 reopen_comment : "",
	 }
	  $scope.reopenTask=function(data){
		  //$scope.taskCompletion.ttrn_id=ttrn_id;
		  $scope.reOpen.ttrn_id = data.ttrn_id;
		  //console.log("TTRN ID "+$scope.taskCompletion.ttrn_id);
			  	
	  }
	
	 //Reopen Task
	 $scope.sendReOpenTask=function(formValid){
		 //console.log("ID for reopen: "+$scope.reOpen.ttrn_id);
		 //console.log("Comment for reopen: "+$scope.reOpen.reopen_comment);
		 if(formValid){
				ApiCallFactory.reopenTask($scope.reOpen).success(function(res,status){
					if(status === 200 && res.responseMessage == "Success"){
						//$scope.reOpen.reopen_comment="";	
						$('#reOpenTask').modal("hide");
						$window.location.reload();
					}
					
				}).error(function(error){
					console.log("Error while approving the task====="+error);
				});
		 }
	 }
	
	 
	//get ttrn_id and reopening comments 	
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
	 
	//get task history
	$scope.getTaskHistoryList=function(task_id){
		$state.transitionTo('taskDetails', {'task_id':task_id});
	};
	$scope.downloadProof = function(udoc_id){
            $window.location = "./downloadProofOfCompliance?udoc_id="+udoc_id;
            $state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});
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
  $scope.tmapObj = {};
  $scope.NtmapObj = {};
  $scope.errMessage = {};
  $scope.getDates =function(){
  		
  	//var legal = new Date($scope.tmapObj.ttrn_legal_due_date);
		if($scope.tmapObj.ttrn_prior_days_buffer<0)
			$scope.tmapObj.ttrn_prior_days_buffer = 0;
			
		$scope.unit_date = new Date($scope.lega_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.func_date = new Date($scope.unit_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.eval_date = new Date($scope.func_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		$scope.exec_date = new Date($scope.eval_date.getTime() - $scope.tmapObj.ttrn_prior_days_buffer * 24 * 60 * 60 * 1000);
		
		
	}
  
  /* edit dates */
  //console.log("edit date");
  $scope.editDates = function(data){
	  //console.log("DATA "+JSON.stringify(data));
	  $scope.NtmapObj.ttrn_id = data.ttrn_id;
	  $scope.NtmapObj.ttrn_legal_date = data.ttrn_legal_due_date;
	  $scope.NtmapObj.ttrn_unit_date = data.ttrn_uh_due_date;
	  $scope.NtmapObj.ttrn_function_date = data.ttrn_fh_due_date;
	  $scope.NtmapObj.ttrn_evaluator_date = data.ttrn_rw_due_date;
	  $scope.NtmapObj.ttrn_executor_date = data.ttrn_pr_due_date;
	  
	  $('#editConfiguration').modal();
	  
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
		  $scope.tmapObj.ttrn_pr_due_date 		= moment($scope.exec_date).format('DD-MM-YYYY');
		  
		 // console.log("JSON "+JSON.stringify($scope.tmapObj));
		  
		//Update Task Configuration dates
		  ApiCallFactory.updateTasksConfiguration($scope.tmapObj).success(function(res,status){
			  spinnerService.hide('html5spinner');
				if(status === 200){
					$('#editConfiguration').modal("hide");
					toaster.success("Success", "Task dates updated successfully.");
					$window.location.reload();
				}else{
					toaster.error("Failed", "Error in dates.");
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("update Configuration===="+error);
			});
		  
	  }
	  
	  
  }
  
  $scope.gettaskformultiplecompletion=function(){
	  spinnerService.show('html5spinner');
		 
		  ApiCallFactory.gettaskformultiplecompletion($scope.multipleCompletion).success(function(res,status){
			  spinnerService.hide('html5spinner');
				if(status === 200 && res.response == "success"){
					$scope.multipleTasks = res.task_list;
					$scope.multipleLength = $scope.multipleTasks.length;
					//alert("length : "+$scope.multipleLength);
				}else{
					toaster.error("Failed", "Error.");
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("update Configuration===="+error);
			});
  }
  
  if(!angular.isUndefined($stateParams.ttrn_id) && $stateParams.ttrn_id!=0 && !angular.isUndefined($stateParams.task_id) && $stateParams.task_id!=0){
	  $scope.gettaskformultiplecompletion();
  }
  
  
  //get check box ID
	$scope.getCheckTaskId=function(selectId,task_id){
		if(selectId==true){
			var obj={
					ttrn_id:task_id,
			}
			$scope.task_ttrn_ids.push(obj);
		}else{
			var index=0;
			angular.forEach($scope.task_ttrn_ids, function (item) {
				if(item.ttrn_id==task_id){
					$scope.task_ttrn_ids.splice(index, 1);   
					//	  break;
				}
				index++;
			});
		}
		if($scope.task_ttrn_ids.length==$scope.multipleTasks.length){
			$scope.selectedAll=true;
		}else{
			$scope.selectedAll=false;
		}
		
	}
  
	//Delete document
	  $scope.deleteTaskDocument=function(udoc_id,ev,index){
		    var confirm = $mdDialog.confirm()
		          .title('Are you sure you want to delete the document?')
		          .targetEvent(ev)
		          .ok('Yes')
		          .cancel('No');
		    $mdDialog.show(confirm).then(function() {
		    	$scope.deleteDocument.udoc_id			  = udoc_id;
		      ApiCallFactory.deleteTaskDocument($scope.deleteDocument).success(function(res,status){
		  			if(status === 200 && res.responseMessage == "Success"){
		  				toaster.success("Success", "Document deleted successfully.");
		  				/*angular.forEach($scope.documentList, function (item) {
		  					$scope.documentList.splice(index,1);
		  				})*/
		  				$timeout($scope.callAtTimeout, 2000);
		  			}else{
		  				toaster.error("Faild", "Please try again");
		  		}	
		  		}).error(function(error){
					console.log("Error while deleting the task====="+error);
				});
		    });
		 }
	  
	  $scope.callAtTimeout=function () {
		  $window.location.reload();
		}
  
  
}]);