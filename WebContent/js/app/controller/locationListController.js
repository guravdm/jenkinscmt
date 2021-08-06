'use strict';

CMTApp.controller('locationListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster, spinnerService) {
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;

	//get Location List
	$scope.getUnitList=function(){
		spinnerService.show('html5spinner');
		ApiCallFactory.getUnitList().success(function(res,status){
			spinnerService.hide('html5spinner');
			$scope.loading=true;
			if(res[0].responseMessage == "Failed")
			{
				$state.go('login');
			}else {
				if(status==200) {
					$scope.loading = false;
					DataFactory.setShowLoader(false);
					$scope.locationList=res;
				}
			}
			/*if(status==200){
			$scope.locationList=res;
			console.log('res'+JSON.stringify(res));
			}*/
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get location list====="+error);
		});
	};
	$scope.getUnitList();
	
	//Edit Location info
	$scope.showEditUnit=function(id, name){
		$scope.locationObj={
				loca_name:name,
				loca_id:id,
		};
		$state.transitionTo('Location', {'loca_name':name,'loca_id':id});
	//	$state.go('Department',{'dept_name':name,'dept_id':id});
	}
	
	
	//Add location info
	$scope.showAddLocation=function(){
		$state.transitionTo('Location',{'loca_name':null,'loca_id':0});
		
		//$state.go('Department'); 
	}
	$scope.importLocation = function(){
		$state.transitionTo('importLocation', { 'loca_name': null, 'loca_id': 0 });
	}
	
}]);