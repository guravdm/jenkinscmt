'use strict';
CMTApp.controller('myAccountController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster) {
	
	$scope.userDetails={};
	
	//get UserDetails
	$scope.getUserDetails=function(){
		var obj={};
		ApiCallFactory.getUserDetailsInfo(obj).success(function(res,status){
			if(status==200 && res.responseMessage!='Failed'){
				$scope.userDetails=res;
			}else{
				console.log("get user details== failed===");
			}
		}).error(function(error){
			console.log("get user details====="+error);
		});
	};
	$scope.getUserDetails();
	
	
	$scope.updateUserPic=function(formValid){
		if(formValid){
		var formData = new FormData();
		var dummyJson={name:'Mahesh'};
		formData.append("profilePic",$scope.userDetails.profilePic);
		ApiCallFactory.updateUserPic(formData).success(function(res,status){
			console.log("in apicallfactory");
			if(status==200){
				console.log("in if");
				//$scope.userDetails=res;
			}
		}).error(function(error){
			console.log("update user details====="+error);
		});
		}
	};

	
	
}]);