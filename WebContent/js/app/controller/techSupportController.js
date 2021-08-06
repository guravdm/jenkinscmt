'use strict';

CMTApp.controller('techSupportController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService) {

	$scope.techSupportCredentials={
			Cname:Storage.get('userDetais.user_full_name'),
			Cemail:Storage.get('userDetais.sess_user_email'),
			Cmobile:Storage.get('userDetais.sess_user_mobile'),
			Cmessage:""
	}
	console.log("mobile :"+$scope.techSupportCredentials.user_mobile);
	 console.log("email :"+$scope.techSupportCredentials.user_email);
	 
	 $scope.sendQuery=function(formValid){
    	if(formValid){
    		ApiCallFactory.sendMailToSupport($scope.techSupportCredentials).success(function(res,status){
    	    	//console.log("in api"+status);
    	    	console.log("result "+res.responseMessage);
    	    	if(status === 200 && res.responseMessage == "Success"){
    	    		//console.log("result"+res.responseMessage);
    	    		console.log("in if");
    				toaster.success("Success", "Query Submitted");    	
    				$scope.techSupportCredentials.Cmessage="";
    				 $location.path('/dashboard');
    			}else{
    				toaster.error("Failed", "Please try again");
    			}
    		}).error(function(error){
    			console.log();
     		});
    		
    	}
	}


}]);