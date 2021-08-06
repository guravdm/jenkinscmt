'use strict';

CMTApp.controller('drilledReportController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window) {

	$scope.reportObj={};
	$scope.reOpen       = {};
	$scope.taskCompletion = {};
	//spinnerService.show('html5spinner');
	if(!angular.isUndefined($stateParams.chart_name) && $stateParams.chart_name!=null){
	//	spinnerService.hide('html5spinner');
		$scope.reportObj.chart_name   =$stateParams.chart_name;
		$scope.reportObj.status       =$stateParams.status;
		$scope.reportObj.entity       =$stateParams.entity;
		$scope.reportObj.unit         =$stateParams.unit;
		$scope.reportObj.department   =$stateParams.department;
		$scope.reportList             =$stateParams.taskList;
	}
	//spinnerService.hide('html5spinner');
	//$state.obj = $stateParams.myobj;
	//console.log("Chart obj in if "+$state.obj);
	if(!angular.isUndefined($stateParams.myobj) && $stateParams.myobj!=null){ 
		$scope.obj = $stateParams.myobj;
		window.localStorage['storageName'] = angular.toJson($stateParams.myobj);
		//$window.sessionStorage.setItem('obj',$stateParams.myobj);
	}else{
		$scope.obj = JSON.parse(window.localStorage['storageName']);
		/*if(!angular.isUndefined($window.sessionStorage.getItem('obj'))){
			$scope.obj=$window.sessionStorage.getItem('obj');
    	}*/
	}
	
	
	$scope.ExportTask=true;
	$scope.toggle=function(){
		$scope.ExportTask = !$scope.ExportTask;
	}
	
	
	//to generate PDF
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
	  $scope.ExportExcels_Maintask=function(){
		  /*var Curr_Date = new Date();
			var Curr_date1= moment(Curr_Date).format('DD-MM-YYYY');*/
			//$('#reportList1').tableExport({type:'excel',escape:'false'});		
			$("#example tr td a").removeAttr("href");
			$("#example tr td a").css("color","black");
			$("#example").table2excel({		
					    name: "Report",
					    filename: "DrilledReport_(Main Task)" 
					  }); 
		}
	
		//get task history
		$scope.getTaskHistoryList=function(task_id){
			$state.transitionTo('taskDetails', {'task_id':task_id});
		};
		
		//Approve task
		 $scope.approveTask=function(ttrn_id,index){
			// console.log(task_id);
			 $scope.taskCompletion.ttrn_id=ttrn_id;
		  		ApiCallFactory.approveTask($scope.taskCompletion).success(function(res,status){
		  			if(status === 200 && res.responseMessage == "Success"){
		  				toaster.success("Success", "Task approved successfully.");
		  				$scope.obj.reportList.splice(index,1);
		  				//$window.location.reload();
		  			}	
		  		}).error(function(error){
					console.log("Error while approving the task====="+error);
				});
		  	
		 }
		 
		//get ttrn_id and reopening comments 	
		 $scope.reOpen={
				 ttrn_id : 0,
				 reopen_comment : "",
		 }
		  $scope.reopenTask=function(data){
			  $scope.reOpen.ttrn_id = data.ttrn_id;
				  	
		  }
		
		 //Reopen Task
		 $scope.sendReOpenTask=function(formValid,index){
			 //console.log("ID for reopen: "+$scope.reOpen.ttrn_id);
			 //console.log("Comment for reopen: "+$scope.reOpen.reopen_comment);
			 if(formValid){
					ApiCallFactory.reopenTask($scope.reOpen).success(function(res,status){
						if(status === 200 && res.responseMessage == "Success"){
							//$scope.reOpen.reopen_comment="";	
							$('#reOpenTask').modal("hide");
							toaster.success("Success", "Task has been reopened.");
							$scope.obj.reportList.splice(index,1);
							$scope.reOpen       = {};
							//$window.location.reload();
						}
						
					}).error(function(error){
						console.log("Error while approving the task====="+error);
					});
			 }
		 }
		 
		 $scope.downloadProof = function(udoc_id){
	            $window.location = "./downloadProofOfCompliance?udoc_id="+udoc_id;
	            $state.transitionTo('DrilledReport', {'task_id':$window.sessionStorage.getItem('task_id')});
		}
}]);