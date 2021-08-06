'use strict';

CMTApp.controller('changePasswordController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $window,$location, DataFactory,Storage,toaster,spinnerService) {
	
	$rootScope.currentuser =Storage.get('userDetais.user_full_name');
	$rootScope.getDefaultUserPassword=Storage.get('userDetais.sess_user_default_password');
    
	$scope.changePwObj={};
	$scope.changePwObj={
			user_user_new_password:'',
			user_user_old_password:'',
			cnfPassword:'',
	};
	
	
	
	//get UserDetails
	$scope.changePassword=function(formValid){
		if(formValid){

			if($scope.changePwObj.user_user_new_password !=undefined && $scope.changePwObj.user_user_old_password !=undefined && $scope.changePwObj.cnfPassword !=undefined){
	   // var reg= /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,}$/;
		//Allowed special Character
		var reg=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[_!@#\$%\^&\*])[a-zA-Z\d_!@#\$%\^&\*]{8,}$/;
			
		if($scope.changePwObj.user_user_new_password != $scope.changePwObj.user_user_old_password){
		if($scope.changePwObj.user_user_new_password == $scope.changePwObj.cnfPassword){
			if(reg.test($scope.changePwObj.user_user_new_password)){
				spinnerService.show('html5spinner');
				ApiCallFactory.changePassword($scope.changePwObj).success(function(res,status){
					spinnerService.hide('html5spinner');
					if(status==200 && res.responseMessage == "PasswordChanged"){
						$window.sessionStorage.setItem('userDetais.sess_user_default_password', res.sess_user_default_password);
						$state.go('dashboard');
						toaster.success("Success", "Password changed successfully");
						
					//    $location.path('/dashboard');
					}
					if(status==200 && res.responseMessage == "ExistingPasswordDoNotMatch"){
						toaster.warning("Old password mismatch");
						$scope.changePwObj={};
					}
				}).error(function(error){
					spinnerService.hide('html5spinner');
					console.log("changePasswords====="+error);
					toaster.warning("Password not changed, Please try it again.");
				});
			}else{
				toaster.warning("Password should be minimun 8 characters. (atleast one Uppercase,one lowercase letter, one digit and one Special Character )");
			}
		}
		}else{
			
		}
	}
	}
	
	};
	
	
	
}]);