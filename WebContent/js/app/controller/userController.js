'use strict';

CMTApp.controller('userController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {

	$scope.userObj={};
	$scope.entityArray=[];
	$scope.designationArray=[];
	$scope.unitArray=[];
	$scope.functionArray=[];
//	$scope.searchObj={};
	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	//$scope.originalDEsignationList=[];
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;

	/*if($scope.userRoleId < 6){
		$state.go('login');
	}
	 */

	$scope.roleArray=[{user_role_id: 1, user_role_name :'Executor'},
		{user_role_id: 2, user_role_name :'Evaluator'},
		{user_role_id: 3, user_role_name :'Function Head'},
		{user_role_id: 4, user_role_name :'Unit Head'},
		{user_role_id: 5, user_role_name :'Entity Head'},
		{user_role_id: 6, user_role_name :'Administrator'},
		{user_role_id: 7, user_role_name :'Superadmin'},
		{user_role_id: 8, user_role_name :'Chief Function Head'},
		{user_role_id: 9, user_role_name :'Compliance Officer'},
		{user_role_id: 10, user_role_name :'Chief Compliance Head'},
		{user_role_id: 11, user_role_name :'Internal Auditor'},
		{user_role_id: 12, user_role_name :'Auditor'},
		{user_role_id: 13, user_role_name :'CFO'},
		{user_role_id: 14, user_role_name :'MD'}
		]

	$scope.gender = [
		{ id: 1, name: 'Male'},
		{ id: 2, name: 'Female'},
		]

	$scope.getEntityUnitFunctionDesignationList=function(){
		var obj={}
		ApiCallFactory.getEntityUnitFunctionDesignationList(obj).success(function(res,status){
			$scope.originalEntityList=res.Entity;
			$scope.entityList=res.Entity;
			$scope.originalUnitList=res.Unit;
			$scope.originalFunctionList=res.Function;
			console.log('res.Function');
		}).error(function(error){
			console.log("getEntityUnitFunctionDesignationList====="+error);
		});
	};
	$scope.getEntityUnitFunctionDesignationList();


	$scope.getEntityDependentArray = function(){
		$scope.unitList=[];
		if($scope.userObj.user_organization_id!="" && $scope.originalUnitList.length!=0){
			angular.forEach($scope.originalUnitList, function (item) {
				if( item.orga_id == $scope.userObj.user_organization_id){
					$scope.unitList.push(item);

				}
			});
		};
	}

	$scope.getUnitDependentArray= function(){
		$scope.functionList=[];
		if($scope.userObj.user_organization_id!="" && $scope.userObj.user_location_id!="" && $scope.originalFunctionList.length!=0){
			angular.forEach($scope.originalFunctionList, function (item) {
				if( (item.orga_id == $scope.userObj.user_organization_id) && (item.loca_id == $scope.userObj.user_location_id)){
					$scope.functionList.push(item);
				}

			});
		};
	}



	//get department List
	$scope.getDesignationList=function(){
		ApiCallFactory.getDesignationList().success(function(res,status){
			$scope.designationListttt=res;
			console.log('Designation '+JSON.stringify(res));
		}).error(function(error){
			console.log("get designation list====="+error);
		});
	};
	$scope.getDesignationList();


	if(!angular.isUndefined($stateParams.user_id) && $stateParams.user_id!=0){

		var obj={}
		ApiCallFactory.getEntityUnitFunctionDesignationList(obj).success(function(res,status){
			$scope.originalEntityList=res.Entity;
			$scope.entityList=res.Entity;
			$scope.originalUnitList=res.Unit;
			$scope.originalFunctionList=res.Function;

			$scope.getUserDetails();
		}).error(function(error){
			console.log("getEntityUnitFunctionDesignationList====="+error);
		});


		$scope.userObj.user_id=$stateParams.user_id
		$scope.getUserDetails=function(){
			ApiCallFactory.getUserDetails($scope.userObj).success(function(res,status){
				if(res.responseMessage!='Failed'){

					$scope.userObj.user_email= res.user_email;
					$scope.userObj.user_employee_id= res.user_employee_id;
					$scope.userObj.user_last_name= res.user_last_name;
					$scope.userObj.user_username= res.user_username;
					$scope.userObj.user_designation_id= res.user_designation_id;
					$scope.userObj.user_created_at= res.user_created_at;
					$scope.userObj.user_address= res.user_address;
					$scope.userObj.user_approval_status= res.user_approval_status;
					$scope.userObj.user_report_to= res.user_report_to;
					$scope.userObj.user_department_id= res.user_department_id;
					$scope.userObj.user_added_by= res.user_added_by;
					$scope.userObj.user_role_id= res.user_role_id;
					$scope.userObj.user_first_name= res.user_first_name;
					$scope.userObj.user_enable_status= res.user_enable_status;
					$scope.userObj.user_id= res.user_id;
					$scope.userObj.user_mobile= parseInt(res.user_mobile);
					$scope.userObj.user_organization_id=res.user_organization_id;
					$scope.userObj.user_default_password_changed=res.user_default_password_changed;
					///	$scope.userObj.user_userpassword= res.user_userpassword;
					$scope.userObj.user_location_id= res.user_location_id
					$scope.userObj.gender= res.gender;
					
					console.log('res.gender : ' + res.gender);
					
					$scope.userObj.user_organization_id=res.user_organization_id;
					$scope.unitList=[];
					if($scope.userObj.user_organization_id!="" && $scope.originalUnitList.length!=0){
						angular.forEach($scope.originalUnitList, function (item) {
							if( item.orga_id == $scope.userObj.user_organization_id){
								$scope.unitList.push(item);
							}
						});
					};
					$scope.userObj.user_location_id= res.user_location_id


					$scope.functionList=[];
					if($scope.userObj.user_organization_id!="" && $scope.userObj.user_location_id!="" && $scope.originalFunctionList.length!=0){
						angular.forEach($scope.originalFunctionList, function (item) {
							if( (item.orga_id == $scope.userObj.user_organization_id) && (item.loca_id == $scope.userObj.user_location_id)){
								$scope.functionList.push(item);
							}

						});
					};

					$scope.user_designation_idData= res.user_designation_id;
					ApiCallFactory.getDesignationList().success(function(resdata,status){
						$scope.designationList=resdata;
						$scope.userObj.user_designation_id= $scope.user_designation_idData;
					}).error(function(error){
						console.log("get designation list====="+error);
					});



					///	$scope.userObj.user_role_id= res.user_role_id;

				}
			}).error(function(error){
				console.log("getUserDetails====="+error);
			});
		};

		/*		call user list details*/

	}else{
		$scope.userObj={};
		$scope.userObj.user_id=$stateParams.user_id;
		$scope.getEntityUnitFunctionDesignationList();
		///	$scope.getDesignationList();
	}


	//add new user
	$scope.addUser=function(formValid){
		if(formValid){

			if($scope.userObj.confirm_password == $scope.userObj.user_userpassword){
				if($scope.userObj.user_mobile==undefined){
					$scope.userObj.user_mobile=null;
				}
				if($scope.userObj.user_employee_id==undefined){
					$scope.userObj.user_employee_id=null;
				}
				if($scope.userObj.user_address==undefined){
					$scope.userObj.user_address=null;
				}
				if($scope.userObj.user_designation_id==undefined){
					$scope.userObj.user_designation_id=null;
				}
				//check duplicate flag
				ApiCallFactory.checkDuplicateUserName($scope.userObj).success(function(res,status) {
					// console.log('res.userNameExists : ' + res.userNameExists);
					$scope.checkDuplicateName = res.userNameExists;
					if($scope.checkDuplicateName == false){
						spinnerService.show('html5spinner');
						ApiCallFactory.addUser($scope.userObj).success(function(res,status){
							spinnerService.hide('html5spinner');
							if(res.responseMessage == "Failed")
							{
								toaster.error("Authorization Failed");
								$state.go('login');
							} 
							else {
								if(res.responseMessage == "Success") {
									$scope.loading = false;
									toaster.success("Success", "User saved successfully");
									$state.go('UsersList');
								} else {
									toaster.error("Failed", "Error in save");
								}
							} 

						}).error(function(error){
							DataFactory.setShowLoader(false);
							console.log("add user===="+error);
						});
					}else{
						toaster.warning("User name already exist");
					}
				}).error(function(error){
					console.log("checkDuplicateUserName ====="+error);
				});
			}else{
				toaster.warning("Confirm password must be equal to password.");
			}
		}
	}


	$scope.updateUser=function(formValid){
		if(formValid){

			if($scope.userObj.confirm_password == $scope.userObj.user_userpassword){
				if($scope.userObj.user_mobile==undefined){
					$scope.userObj.user_mobile=null;
				}
				if($scope.userObj.user_employee_id==undefined){
					$scope.userObj.user_employee_id=null;
				}
				if($scope.userObj.user_address==undefined){
					$scope.userObj.user_address=null;
				}
				if($scope.userObj.user_designation_id==undefined){
					$scope.userObj.user_designation_id=null;
				}
				if($scope.userObj.user_userpassword==undefined){
					$scope.userObj.user_userpassword='';
				}
				if($scope.userObj.confirm_password==undefined){
					$scope.userObj.confirm_password='';
				}

				ApiCallFactory.checkDuplicateUserName($scope.userObj).success(function(res,status){
					$scope.checkDuplicateName=res.userNameExists;
					if($scope.checkDuplicateName == false){
						spinnerService.show('html5spinner');
						ApiCallFactory.updateUser($scope.userObj).success(function(res,status){
							spinnerService.hide('html5spinner');
							if(res.responseMessage == "Failed")
							{
								$state.go('login');
							} 
							else {
								if(res.responseMessage == "Success") {
									$scope.loading = false;
									toaster.success("Success", "User updated successfully");
									$state.go('UsersList');
								}
							} 

						}).error(function(error){
							DataFactory.setShowLoader(false);
							console.log("update user===="+error);
						});
					}else{
						toaster.warning("User name already exist");
					}
				}).error(function(error){
					console.log("checkDuplicateUserName ====="+error);
				});


			}else{
				toaster.warning("Confirm password must be equal to password.");
			}
		}
	}


	//Edit user access
	$scope.editUserAccess=function(id,fname,lname){
		$state.transitionTo('UserAccess', {'user_id':id,'user_first_name':fname,'user_last_name':lname});
	}
}]);