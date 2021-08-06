'use strict';

CMTApp.controller('listShowCauseNoticeController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','$filter','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,$filter,spinnerService) {

	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.originaltaskList=[];
	
	$scope.getAllShowCauseNotice=function(){
		var obj={}
		spinnerService.show('html5spinner');
		ApiCallFactory.getAllShowCauseNotice(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
				$scope.originaltaskList=res.notice_list;
				$scope.ShowCauseNoticeList=res.notice_list;
				$scope.originalEntityList=res.DD_data[0].Entity;
				$scope.entityList=res.DD_data[0].Entity;
				$scope.originalUnitList=res.DD_data[0].Unit;
				$scope.originalFunctionList=res.DD_data[0].Function;
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get user list====="+error);
		});
	};
	$scope.getAllShowCauseNotice();
	
	
	$scope.getEntityDependentArray = function(){
		$scope.unitList=[];
		if($scope.searchObj.orga_id!="" && $scope.originalUnitList.length!=0){
			angular.forEach($scope.originalUnitList, function (item) {
				if( item.orga_id == $scope.searchObj.orga_id){
					$scope.unitList.push(item);
				}

			});
		};
	}
	
	
	$scope.getUnitDependentArray= function(){
		$scope.functionList=[];
		if($scope.searchObj.orga_id!="" && $scope.searchObj.loca_id!="" && $scope.originalFunctionList.length!=0){
			angular.forEach($scope.originalFunctionList, function (item) {
				if( (item.orga_id == $scope.searchObj.orga_id) && (item.loca_id == $scope.searchObj.loca_id)){
					$scope.functionList.push(item);
				}

			});
		};
	}
	
	$scope.searchTasks = function(){
		$scope.ShowCauseNoticeList=[];
		angular.forEach($scope.originaltaskList, function (data) {
				if ((!$scope.searchObj.orga_id || data.orga_id === $scope.searchObj.orga_id) && 
					(!$scope.searchObj.loca_id || data.loca_id === $scope.searchObj.loca_id) &&
					(!$scope.searchObj.dept_id || data.dept_id === $scope.searchObj.dept_id)){
				$scope.ShowCauseNoticeList.push(data);
			} 
		});
	}
	
	
	$scope.getShowCauseHistoryList=function(id){
		console.log("dfghjkl "+id);
		$state.transitionTo('showCauseNoticeDetails',{'scau_id':id});
	};
	
	
	$scope.editShowCuaeNotice=function(id){
		$state.go('createShowCauseNotice',{'scau_id':id});
	}
	
}]);