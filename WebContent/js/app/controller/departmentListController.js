'use strict';

CMTApp.controller('departmentListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	

	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	//get department List
	$scope.getDeparmentList=function(){
		spinnerService.show('html5spinner');
		ApiCallFactory.getDeparmentList().success(function(res,status){
			spinnerService.hide('html5spinner');
			$scope.loading=true;
			if(res[0].responseMessage == "Failed")
			{
				$state.go('login');
			}else {
				if(status==200) {
					$scope.loading = false;
					DataFactory.setShowLoader(false);
					$scope.departmentList=res;
				}
			}
			/*if(status==200){
			$scope.departmentList=res;
			console.log('res'+JSON.stringify(res));
			}*/
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get deparment list====="+error);
		});
	};
	$scope.getDeparmentList();
	
	//Edit department info
	$scope.showEditDepatment=function(id, name){
		$scope.departmentObj={
				dept_name:name,
				dept_id:id,
		};
		$state.transitionTo('Department', {'dept_name':name,'dept_id':id});
	//	$state.go('Department',{'dept_name':name,'dept_id':id});
	}
	
	//Add department info
	$scope.showAddDepatment=function(){
		$state.transitionTo('Department',{'dept_name':null,'dept_id':0});
		
		//$state.go('Department');
	}
	$scope.importDepatment = function(){
		$state.transitionTo('importDepartment', {'dept_name':null,'dept_id':0} );
	}
	
}]);