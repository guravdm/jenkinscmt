'use strict';

CMTApp.controller('loginController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage,toaster,spinnerService) {

		$scope.isLoginFormValid = false;
	$scope.loginFailed = false;
	$scope.credentials={
		user_username:"",
		user_userpassword:""
	};
	console.clear();

	
	//remove sesstion value
	/*$window.sessionStorage.removeItem('token');
	$window.sessionStorage.removeItem('userDetais');
	$window.sessionStorage.removeItem('userDetais.user_full_name');
	$window.sessionStorage.removeItem('userDetais.sess_user_id');
	$window.sessionStorage.removeItem('userDetais.sess_user_default_password');
	$window.sessionStorage.removeItem('userDetais.sess_role_id');
	$window.sessionStorage.removeItem('userDetais.sess_user_email');
	$window.sessionStorage.removeItem('userDetais.sess_user_mobile');*/
	
	//remove sesstion value
	$window.localStorage.removeItem('token');
	$window.localStorage.removeItem('userDetais');
	$window.localStorage.removeItem('userDetais.user_full_name');
	$window.localStorage.removeItem('userDetais.sess_user_id');
	$window.localStorage.removeItem('userDetais.sess_user_default_password');
	$window.localStorage.removeItem('userDetais.sess_role_id');
	$window.localStorage.removeItem('userDetais.sess_user_email');
	$window.localStorage.removeItem('userDetais.sess_user_mobile');
	
	
	$scope.forgotPasswordCredentials={
			user_username:"",
			user_email:""
		};
		
	
    $scope.login = function(formValid) {
    	if(formValid){
    		    spinnerService.show('html5spinner');
    		ApiCallFactory.doLogin($scope.credentials).success(function(res,status){
    			spinnerService.hide('html5spinner');
    			if(status === 200 && res.responseMessage == "UserAuthenticated"){
    				
    			//	$window.location.reload();
    				/* $window.sessionStorage.setItem('token', res.authentication_token);
    				 $window.sessionStorage.setItem('userDetais.user_full_name', res.user_full_name);
    				 $window.sessionStorage.setItem('userDetais.sess_user_id', res.sess_user_id);
    				 $window.sessionStorage.setItem('userDetais.sess_user_default_password', res.sess_user_default_password);
    				 $window.sessionStorage.setItem('userDetais.sess_role_id', res.sess_role_id);
    				 $window.sessionStorage.setItem('userDetais.sess_user_email',res.sess_user_email);
    				 $window.sessionStorage.setItem('userDetais.sess_user_mobile',res.sess_user_mobile);*/
    				
    				 $window.sessionStorage.setItem('userDetais.user_full_name', res.user_full_name);
    				 $window.sessionStorage.setItem('userDetais.sess_role_id', res.sess_role_id);
    				 
    				 $window.localStorage.setItem('token', res.authentication_token);
    				 $window.localStorage.setItem('userDetais.user_full_name', res.user_full_name);
    				 $window.localStorage.setItem('userDetais.sess_user_id', res.sess_user_id);
    				 $window.localStorage.setItem('userDetais.sess_user_default_password', res.sess_user_default_password);
    				 $window.localStorage.setItem('userDetais.sess_role_id', res.sess_role_id);
    				 $window.localStorage.setItem('userDetais.sess_user_email',res.sess_user_email);
    				 $window.localStorage.setItem('userDetais.sess_user_mobile',res.sess_user_mobile);
    				 
    				 $window.localStorage.setItem('userDetais.userEntity', res.userEntity);
    				 $window.localStorage.setItem('userDetais.userFunction', res.userFunction);
    				 $window.localStorage.setItem('userDetais.userLocation', res.userLocation);
    				 //console.log('userEntity : ' + res.userEntity);
    				 
    				 $rootScope.userEntity = res.userEntity;
    				 $rootScope.userFunction = res.userFunction;
    				 $rootScope.userLocation = res.userLocation;
    				 
    				 
    				 $rootScope.currentuser= res.user_full_name;
    				// $rootScope.currentuser =Storage.get('userDetais.user_full_name');
    				 $rootScope.getUserRoleId=res.sess_role_id;
    				 $rootScope.getUserRoleId=Storage.get('userDetais.sess_role_id');
    				 $rootScope.getUserID=res.sess_user_id;
    				 $rootScope.getUserID=$window.localStorage.getItem('userDetais.sess_user_id');
    				// alert("ID : "+$rootScope.getUserID);
    				 $rootScope.getDefaultUserPassword=Storage.get('userDetais.sess_user_default_password');
    				 if($rootScope.prvPath !='' && $rootScope.prvPath !='DrilledReport'){
    					 $location.path($rootScope.prvPath).replace();
    					 $rootScope.prvPath = '';
    				 }else{
    					 $location.path('/dashboard'); 
    				 }
    				/* if(res.sess_role_id==8){
    					 $location.path('/listShowCauseNotice'); 
    				 }*/
    				 
    				 if(res.sess_user_default_password==0){
    					 $location.path('/ChangePassword'); 
    				 }else{
    					$location.path('/dashboard');
    					/*$location.path('/dashboardmenu');*/
    				 }
    			} else if(res.responseMessage == "loggedIn"){
    				spinnerService.hide('html5spinner');
    				toaster.error("The user is already logged in.");			    
  				    $window.localStorage.removeItem('token');
				    $window.localStorage.removeItem('userDetais');
    			}else if(res.responseMessage == "UsernameDoesNotExists"){
    				spinnerService.hide('html5spinner');
    				toaster.error("Failed", "User not found.");
  				    
  				    $window.localStorage.removeItem('token');
				    $window.localStorage.removeItem('userDetais');
    			}else if(res.responseMessage == "IncorrectPassword"){
    				 spinnerService.hide('html5spinner');
    				  toaster.error("Failed", "Invalid username and password.");    				  
    				  $window.localStorage.removeItem('token');
  				      $window.localStorage.removeItem('userDetais');
    			}else if(res.responseMessage == "IncorrectPassword, You have 2 login attempts left"){
    				spinnerService.hide('html5spinner');
  				  	toaster.error("Invalid username and password. You have 2 login attempts left ");			  
  				  	$window.localStorage.removeItem('token');
				    $window.localStorage.removeItem('userDetais');
    			}else if(res.responseMessage == "IncorrectPassword, You have 1 login attempts left"){
    				spinnerService.hide('html5spinner');
  				  	toaster.error("Invalid username and password. You have 1 login attempt left ");			  
  				  	$window.localStorage.removeItem('token');
				    $window.localStorage.removeItem('userDetais');
    			}else if(res.responseMessage == "IncorrectPassword, You have 0 login attempts left"){
    				spinnerService.hide('html5spinner');
  				  	toaster.error("Your account has been locked. Please contact support desk or try after sometime. ");			  
  				  	$window.localStorage.removeItem('token');
				    $window.localStorage.removeItem('userDetais');
    			}else if(res.responseMessage == "Account Locked"){
    				spinnerService.hide('html5spinner');
  				  	toaster.error(" Your account has been locked. Try after 10 minutes.");			  
  				  	$window.localStorage.removeItem('token');
				    $window.localStorage.removeItem('userDetais');
    			}else if(res.responseMessage == "DisabledUser"){
    				spinnerService.hide('html5spinner');
  				  toaster.error("Failed", "Disabled user.");
				 /* $window.sessionStorage.removeItem('token');
				  $window.sessionStorage.removeItem('userDetais');*/
				 
				    $window.localStorage.removeItem('token');
				    $window.localStorage.removeItem('userDetais');
				    
			}else if(res.responseMessage == "Failed"){
				spinnerService.hide('html5spinner');
				  toaster.error("Failed", "Please Login again.");
				  /*$window.sessionStorage.removeItem('token');
				  $window.sessionStorage.removeItem('userDetais');*/
				 
				  $window.localStorage.removeItem('token');
				  $window.localStorage.removeItem('userDetais');
			}
    		}).error(function(error){
    			//$window.sessionStorage.removeItem('token');
    		   spinnerService.hide('html5spinner');
    			console.log(error);
    		});
    	}
    };
    
    //forgot password functionality
    $scope.resetPsssword=function(formValid){
    	if(formValid){
    		//console.log("Form valid");
    	       	ApiCallFactory.doForgotPassword($scope.forgotPasswordCredentials).success(function(res,status){
    	    	//console.log("in api"+status);
    	    	console.log("result "+res.responseMessage);
    	    	if(status === 200 && res.responseMessage == "Success"){
    	    		//console.log("result"+res.responseMessage);
    	    		console.log("in if");
    				//$window.sessionStorage.setItem('userDetais.sess_user_default_password', 0);
    				toaster.success("Success", "Your Password has been changed. Please check your email inbox");
    				$scope.forgotPasswordCredentials.user_username="";
    				$scope.forgotPasswordCredentials.user_email="";
                    $('#forgotPassword').modal('hide');
    				 //$scope.uibmodalStack.dismissAll();
    			}else{
    				if(status === 200 && res.responseMessage == "InvalidUsername_email_id"){
    					toaster.error("Failed", "Invalid User Name Or Email Id");
    				}
    				else{
    					if(status === 200 && res.responseMessage == "Failed"){
    						toaster.error("Failed", "Problem while sending mail");
    					}
    				}
    				
    			}
    		}).error(function(error){
    			//$window.sessionStorage.removeItem('token');
     		 //  spinnerService.hide('html5spinner');
     			console.log();
     		});
    	}
    }
    
}]);
