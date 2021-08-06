'use strict';

CMTApp.controller('activityLogDataController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService) {
	
	$scope.maxDate = new Date();
	
	$scope.loginLogStartDate = new Date((new Date()).toDateString());
	$scope.loginLogEndDate = new Date((new Date()).toDateString());
	
	$scope.docDelLogStartDate = new Date((new Date()).toDateString());
	$scope.docDelLogEndDate = new Date((new Date()).toDateString());;
	
	$scope.reportGenerationLogStartDate = new Date((new Date()).toDateString());
	$scope.reportGenerationLogEndDate = new Date((new Date()).toDateString());
	
	$scope.emailLogStartDate = new Date((new Date()).toDateString());
	$scope.emailLogEndDate = new Date((new Date()).toDateString());
	
	
	$scope.taskMappLogStartDate = new Date((new Date()).toDateString());
	$scope.taskMappLogEndDate = new Date((new Date()).toDateString());
	
	$scope.taskCongigLogStartDate = new Date((new Date()).toDateString());
	$scope.taskCongigLogEndDate = new Date((new Date()).toDateString());
	
	
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
	
	
	$scope.validateLogDates = function(fromDate,toDate) {
	    var from_date = null;
	    var from_to = null;
	    if(fromDate != null){
	    	var from = angular.isDefined(fromDate);
	    	if(from){
	    		from_date = new Date(fromDate);
	    	}else
	    		toaster.error("Invalid date", "Select \"From\" date.");
	    }else{
	    	toaster.error("Invalid date", "Select \"From\" date.");
	    }
	    
	    if(toDate != null){
	    	var to = angular.isDefined(toDate);
	    	if(to){
	    		from_to = new Date(toDate);
	    	}else
	    		toaster.error("Invalid date", "Select \"To\" date.");
	    }else{
	    	toaster.error("Invalid date", "Select \"To\" date.");
	    }
	    
		var flag = true;
		if(from && from_date){
			if(from_date > from_to){ 
				toaster.error("Invalid date range", "\"From\" date must be less than \"To\" date.");
	     	    flag = false;
			}else if(from_date == from_to){
				flag = true;
			}
		}
		
		if(flag)
		    return true;
		else
			return false;
	  
 }

	// get email log list method
	$scope.getEmailLogsData=function(){
		if($scope.validateLogDates($scope.emailLogStartDate,$scope.emailLogEndDate)){
			var obj = {}
			obj.logStartDate = $filter('date')($scope.emailLogStartDate, "dd-MM-yyyy"); //new Date($scope.emailLogStartDate);
			obj.logEndDate = $filter('date')($scope.emailLogEndDate, "dd-MM-yyyy"); //new Date($scope.emailLogEndDate);
			
			spinnerService.show('html5spinner');
			ApiCallFactory.emailsLogsForEmails(obj).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status==200){
					$scope.userList=res;
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("get user list====="+error);
			});
		}
	};
	$scope.getEmailLogsData();
	
	
	// get task import logs list
	/*$scope.getImportTaskLogsData=function(){
		var obj = {}
		spinnerService.show('html5spinner');
		ApiCallFactory.LegalUpdatesLogs(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
				$scope.importTaskList = res;
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get user list=====" + error);
		});
	};
	$scope.getImportTaskLogsData();
	
	
	// get task deletion logs list
	$scope.getDeletionLogsData=function(){
		var obj = {}
		spinnerService.show('html5spinner');
		ApiCallFactory.deletionLogsCalFact(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
				$scope.deletionLogsList = res;
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get user list=====" + error);
		});
	};
	$scope.getDeletionLogsData();
	
	// get task Task config logs list
	$scope.getTaskConfigLogsData=function(){
		var obj = {}
		spinnerService.show('html5spinner');
		ApiCallFactory.taskConfigLogs(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
				$scope.taskConfigLogsList = res;
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get user list=====" + error);
		});
	};
	$scope.getTaskConfigLogsData();*/
	
	
	$scope.getLegalUpdatesLogList=function(){
		if($scope.validateLogDates($scope.taskCongigLogStartDate,$scope.taskCongigLogEndDate)){
			spinnerService.show('html5spinner');
			var obj={}
			
			obj.logStartDate = $filter('date')($scope.taskCongigLogStartDate, "dd-MM-yyyy") ;//new Date($scope.taskCongigLogStartDate);
			obj.logEndDate = $filter('date')($scope.taskCongigLogEndDate, "dd-MM-yyyy");//new Date($scope.taskCongigLogEndDate);
			  
			ApiCallFactory.getTaskConfigLogList(obj).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status==200){
					$scope.taskConfiglogList=res;
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("logController list====="+error);
			});
		}
	};
	$scope.getLegalUpdatesLogList();
	
	
	
	
	
	
	
	// get task Task mapping logs list
	$scope.gettaskMappingLogsData=function(){
		 $scope.sortType     = 'name'; // set the default sort type
		  $scope.sortReverse  = false;  // set the default sort order
		  $scope.searchFish   = '';  
		  if($scope.validateLogDates($scope.taskMappLogStartDate,$scope.taskMappLogEndDate)){
			  var obj = {}
			  obj.logStartDate = $filter('date')($scope.taskMappLogStartDate, "dd-MM-yyyy") ;//new Date($scope.taskMappLogStartDate);
			  obj.logEndDate = $filter('date')($scope.taskMappLogEndDate, "dd-MM-yyyy") ;//new Date($scope.taskMappLogEndDate);
				
			  spinnerService.show('html5spinner');
			  ApiCallFactory.taskMappingLogs(obj).success(function(res,status){
				  spinnerService.hide('html5spinner');
				  if(status==200){
					  $scope.taskMappingLogsList = res.list;
				  }
			  }).error(function(error){
				  spinnerService.hide('html5spinner');
				  console.log("get user list=====" + error);
			  });
		  }
	};
	$scope.gettaskMappingLogsData();
	
	//Extended Audit log
	// get task AllLoginLogs list
	$scope.getAllLoginLogsData=function(){
		console.log("loginLogStartDate=====" + $scope.loginLogStartDate);
		console.log("loginLogEndDate=====" + $scope.loginLogEndDate);
		
		if($scope.validateLogDates($scope.loginLogStartDate,$scope.loginLogEndDate)){
			var obj = {}
			obj.logStartDate = $filter('date')($scope.loginLogStartDate, "dd-MM-yyyy"); //new Date($scope.loginLogStartDate);
			obj.logEndDate = $filter('date')($scope.loginLogEndDate, "dd-MM-yyyy"); //new Date($scope.loginLogEndDate);
			
			spinnerService.show('html5spinner');
			ApiCallFactory.allLoginLogs(obj).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status==200){
					$scope.loginList=res;
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("Error getAllLoginLogsData =====" + error);
			});
		}
	};
	$scope.getAllLoginLogsData();
	
	
	$scope.getAllDocDeleteLogsData=function(){
		if($scope.validateLogDates($scope.docDelLogStartDate,$scope.docDelLogEndDate)){
			var obj = {}
			obj.logStartDate = $filter('date')($scope.docDelLogStartDate, "dd-MM-yyyy"); //new Date($scope.docDelLogStartDate);
			obj.logEndDate = $filter('date')($scope.docDelLogEndDate, "dd-MM-yyyy"); //new Date($scope.docDelLogEndDate);
			
			spinnerService.show('html5spinner');
			ApiCallFactory.docDeleteLogs(obj).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status==200){
					$scope.docDeleteLogList=res;
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("Error getAllDocDeleteLogsData =====" + error);
			});
		}
	};
	$scope.getAllDocDeleteLogsData();
	
	$scope.getAllReportGenearationlogData=function(){
		if($scope.validateLogDates($scope.reportGenerationLogStartDate,$scope.reportGenerationLogEndDate)){
			var obj = {}
			obj.logStartDate = $filter('date')($scope.reportGenerationLogStartDate, "dd-MM-yyyy"); //new Date($scope.reportGenerationLogStartDate);
			obj.logEndDate = $filter('date')($scope.reportGenerationLogEndDate, "dd-MM-yyyy"); //new Date($scope.reportGenerationLogEndDate);

			spinnerService.show('html5spinner');
			ApiCallFactory.reportGenearationLogs(obj).success(function(res,status){
				spinnerService.hide('html5spinner');
				if(status==200){
					$scope.reportLogList=res;
				}
			}).error(function(error){
				spinnerService.hide('html5spinner');
				console.log("Error getAllReportGenearationlog =====" + error);
			});
		}
	};
	$scope.getAllReportGenearationlogData();
	
}]);