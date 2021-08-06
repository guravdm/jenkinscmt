'use strict';

CMTApp.controller('usersListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window) {
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	$scope.tmapObj={};
	$scope.showButton = true;
	//get user List
	$scope.getUserList = function(){
		$scope.loading=true;
		//spinnerService.show('html5spinner');
		ApiCallFactory.getUserList().success(function(res,status){			
			//spinnerService.hide('html5spinner');

			$scope.loading=true;
			if(res[0].responseMessage == "Failed")
			{
				$state.go('login');
			}else {
				if(status==200) {
					$scope.loading = false;
					DataFactory.setShowLoader(false);
					$scope.userList=res;
				}
			}
			/*if(status==200){
			$scope.loading=false;
			DataFactory.setShowLoader(false);
			$scope.userList=res;
			}*/
		}).error(function(error){
			spinnerService.hide('html5spinner');
			$scope.loading=false;
			console.log("get user list====="+error);
		});
	};
	$scope.getUserList();
	
	//Edit entity info
	$scope.showEditEntity=function(id){
		$state.go('User',{'user_id':id});
	}
	
	//Add entity infoorga_parent_id
	$scope.showAddUser=function(){
		$state.go('User',{'user_id':0});
	}
	
	//enable disable User
	 $scope.changeUserStatus = function (statusVal,id) {
		 if(id!=0){
	    		$scope.tmapObj={
	    				user_id:id,
	    				user_enable_status:statusVal
	    				
	    		}
	    	}
		
		 ApiCallFactory.enableDisableUser($scope.tmapObj).success(function(res,status){
			 if(status === 200 && res.responseMessage=='Success'){
 				if(statusVal==1){
 					toaster.success("Success", "User enabled successfully");
 				}else{
 					toaster.success("Success", "User disabled successfully");
 				}

 				if(id!=0){
 					angular.forEach($scope.userList, function (item,index) {
 						if(item.user_id==id){
 							if(statusVal==1){
 								$scope.userList[index].user_enable_status=1;
 							}else{
 								$scope.userList[index].user_enable_status=0;
 							}

 						}
 					});
 				}
 			}else{
 				if(statusVal==1){
 					toaster.error("Failed", "Error while performing action");;
 				}else{
 					toaster.error("Failed", "Error while performing action");;
 				}

 			} 
		 })
	 
	 }
	 
	 
	 $scope.ExportUserList=function(){
		 ApiCallFactory.downloaduserlist().success(function(res,status){
			 if(status==200){
				 $scope.exportlist=res.user_list;
				 console.log('res '+JSON.stringify(res.user_list));
		  /*var Curr_Date = new Date();
			var Curr_date1= moment(Curr_Date).format('DD-MM-YYYY');*/
			//$('#reportList1').tableExport({type:'excel',escape:'false'});		
			//$("#ExportUserList tr td a").removeAttr("href");
			//$("#ExportUserList tr td a").css("color","black");
				 var count = res.user_list.length;
				 var timeOut = 0;
				 if(count>0 && count <=50){
					 timeOut = 2000;
				 }
				 if(count>50 && count <=100){
					 timeOut = 3000;
				 }
				 if(count>100 && count <=200){
					 timeOut = 4000;
				 }
				 if(count>200 && count <=500){
					 timeOut = 8000;
				 }
			
				 $scope.showButton = false;
				 setTimeout(function(){ 
					// spinnerService.hide('html5spinner');
					 $scope.exportUserExcel();
				 }, timeOut);
				
				 
			
			 }
		 })
		}
	 $scope.exportUserExcel = function(){
		 $scope.showButton = true;
		 $("#ExportUserList").table2excel({		
			    name: "Report",
			    filename: "UserList" 
			    	
			  });
		
	 }
	 
	 //send credentials
	 $scope.sendCredentials=function(id) {
		 spinnerService.show('html5spinner');
		 $scope.json = {user_id:id};
		 ApiCallFactory.sendCredentials($scope.json).success(function(res,status){
			 spinnerService.hide('html5spinner');
			 if(status==200){
				 console.log("in if");
 				toaster.success("Success", "Credentials Sent"); 
			 }
		 })
	 }
	
	$scope.showImportForm = function(){
			$state.go('importUserList');
	}
}]);