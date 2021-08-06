'use strict';

CMTApp.controller('headCountsController', ['$scope','$rootScope','ApiCallFactory','$window','$location', 'DataFactory', 'Storage','toaster','spinnerService', '$state' ,function ($scope,$rootScope, ApiCallFactory,$window, $location, DataFactory, Storage, toaster, spinnerService, $state) {
	console.clear();
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;


	$scope.getHeadCountList = function(){
		$scope.loading=true;
		ApiCallFactory.getHeadCountList().success(function(res,status){			
			$scope.loading=true;
			if(status==200) {
				$scope.loading = false;
				DataFactory.setShowLoader(false);
				$scope.headCountList = res;
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			$scope.loading=false;
			console.log("get head count list ===== " + error);
		});
	};
	$scope.getHeadCountList();

}]);
