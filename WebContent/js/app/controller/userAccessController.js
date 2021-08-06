
'use strict';

CMTApp.controller('userAccessController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$mdDialog', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$mdDialog) {
	$scope.userAccessObj={};
	$scope.userAccessObj = {
			umap_orga:null,
			umap_loca:null,
			umap_dept:null
	};
	$scope.userArray=[];
	$scope.entityList=[];
	
	$scope.originalEntityList=[];
	$scope.originalUnitList=[];
	$scope.originalFunctionList=[];
	$scope.userAccessList=[];

	/*$scope.getUserAccessList=function(){
		ApiCallFactory.getUserAccessList($scope.userAccessObj).success(function(res,status){
			if(status === 200 && res.responseMessage!="Failed"){
					//$scope.AccessRemaining=res.AccessRemaining;
					$scope.AccessRemainingOriginal=res.AccessRemaining;
					$scope.AccessRemaining = res.AccessRemaining;
				///	$scope.AccessRemaining = angular.copy($scope.AccessRemainingOriginal);
					////alert($scope.AccessRemaining==$scope.AccessRemainingOriginal);
					$scope.userAccessList=res.AccessSet;
					$scope.getOriginalEntityArray();
			
			}else{
				toaster.error("Failed", "Error in get user access list");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("user access list===="+error);
		});
	}*/
	
	$scope.getUserAccessList=function(){
	spinnerService.show('html5spinner');
	ApiCallFactory.getUserAccessList($scope.userAccessObj).success(function(res,status){
		if(status === 200 && res.responseMessage!="Failed"){
				//$scope.AccessRemaining=res.AccessRemaining;
				$scope.AccessRemainingOriginal=res.AccessRemaining;
				$scope.AccessRemaining = res.AccessRemaining;
			///	$scope.AccessRemaining = angular.copy($scope.AccessRemainingOriginal);
				////alert($scope.AccessRemaining==$scope.AccessRemainingOriginal);
				$scope.userAccessList=res.AccessSet;
				$scope.getOriginalEntityArray();
				spinnerService.hide('html5spinner');
		
		}else{
			spinnerService.hide('html5spinner');

			toaster.error("Failed", "Error in get user access list");
		}
	}).error(function(error){
		DataFactory.setShowLoader(false);
		spinnerService.hide('html5spinner');

		console.log("user access list===="+error);
	});
	}

	$scope.getOriginalEntityArray = function(){
		$scope.originalEntityList=[];
		$scope.checkDuplicateEntity=[];
		$scope.entityList=[];
	
		if($scope.AccessRemaining.length!=0){
			angular.forEach($scope.AccessRemaining, function (item) {
				var obj={
						orga_id:item.orga_id,
						orga_name:item.orga_name,
					};
				//	$scope.originalEntityList.push(obj);
					
					if($scope.checkDuplicateEntity.length>0){
						if ($scope.checkDuplicateEntity.indexOf(item.orga_id) === -1) {
							$scope.entityList.push(obj);
							$scope.checkDuplicateEntity.push(item.orga_id);
						}
					}else{
						$scope.entityList.push(obj);
						$scope.checkDuplicateEntity.push(item.orga_id);
					}
					
					
			});
	};
	}
	
	
/*	$scope.getOriginalUnitArray = function(){
		$scope.originalUnitList=[];
		if($scope.AccessRemaining.length!=0){
			angular.forEach($scope.AccessRemaining, function (item) {
				var obj={
						loca_id:item.loca_id,
						loca_name:item.loca_name,
					};
					$scope.originalUnitList.push(obj);
			});
	};
	}
	*/
/*	
	$scope.getOriginalFunctionArray = function(){
		$scope.originalFunctionList=[];
		if($scope.AccessRemaining.length!=0){
			angular.forEach($scope.AccessRemaining, function (item) {
				var obj={
						orga_id:item.orga_id,
						dept_name:item.dept_name,
					};
					$scope.originalFunctionList.push(obj);
			});
	};
	}*/
	
	//get function list
	$scope.getEntityDependentArray = function(){
		$scope.unitList=[];
		$scope.checkDuplicateUnit=[];
		if($scope.userAccessObj.umap_orga !=null){
			angular.forEach($scope.AccessRemaining, function (item) {
				if(item.orga_id == $scope.userAccessObj.umap_orga.orga_id ){
					if($scope.checkDuplicateUnit.length>0){
						if ($scope.checkDuplicateUnit.indexOf(item.loca_id) === -1) {
							
							$scope.unitList.push(item);
							$scope.checkDuplicateUnit.push(item.loca_id);
						}
					}else{
						$scope.unitList.push(item);
						$scope.checkDuplicateUnit.push(item.loca_id);
					}
					
				}

			});
		};
	}

	
	$scope.getUnitDependentArray= function(){
		$scope.functionList=[];
		$scope.checkDuplicateFunction=[];
		if($scope.userAccessObj.umap_orga !=null && $scope.userAccessObj.umap_loca!=null){
			angular.forEach($scope.AccessRemaining, function (item) {
			//	alert($scope.userAccessObj.umap_loca.length);
				for(var i=0; i<$scope.userAccessObj.umap_loca.length;i++){
				if( (item.orga_id == $scope.userAccessObj.umap_orga.orga_id ) && (item.loca_id == $scope.userAccessObj.umap_loca[i].loca_id)){
					if($scope.checkDuplicateFunction.length>0){
						if ($scope.checkDuplicateFunction.indexOf(item.dept_id) === -1) {
								$scope.functionList.push(item);
								$scope.checkDuplicateFunction.push(item.dept_id);
						}
					}else{
						$scope.functionList.push(item);
						$scope.checkDuplicateFunction.push(item.dept_id);
					}
					
				}
				}

			});
		};
	};
	
	function getIndexOf(arr, val, prop) {
	      var l = arr.length,
	        k = 0;
	      for (k = 0; k < l; k = k + 1) {
	        if (arr[k][prop] === val) {
	          return k;
	        }
	      }
	      return false;
	    }
		
		
	$scope.userAccessObj.mapping_list=[];
	
	$scope.removeEntityIdArray=[];
	$scope.removeUnitIdArray=[];
	$scope.removeFunctionIdArray=[];
	$scope.removeFromOrignalAccessArray=[];
	function removeItem(array, item) {
	    for (var i = array.length - 1; i >= 0; i--)
	        if (array[i].loca_id === item.loca_id && array[i].dept_id === item.dept_id && array[i].orga_id === item.orga_id) {
	            array.splice(i, 1);
	           /// break; // remove this line if there could be multiple matching elements
	        }
	}
	//add new user
	$scope.addAccessLevel=function(formValid){
		//spinnerService.show('html5spinner');
		if(true){
			$scope.removeFromOrignalAccessArray=[];	 
			$scope.locaListArray = [];
			$scope.locaListName = [];
			if ($scope.userAccessObj.umap_loca.length > 0) {
				for (var i = 0; i < $scope.userAccessObj.umap_loca.length; i++) {
					// alert($scope.tmapObj.tmap_loca_id[i]);
					$scope.obj = {
						umap_loca_id : $scope.userAccessObj.umap_loca[i].loca_id,	
						umap_loca_name : $scope.userAccessObj.umap_loca[i].loca_name	
					}
					$scope.locaListArray.push($scope.obj);
				}
			}
			
			if($scope.userAccessObj.umap_orga !=null && $scope.userAccessObj.umap_loca!=null && $scope.userAccessObj.umap_dept!=null){
				
				for(var i=0;i< $scope.locaListArray.length;i++){
				var listObj={ 
						loca_id:$scope.locaListArray[i].umap_loca_id,
						orga_name:$scope.userAccessObj.umap_orga.orga_name,
						loca_name:$scope.locaListArray[i].umap_loca_name,
						orga_id:$scope.userAccessObj.umap_orga.orga_id ,
						dept_name:$scope.userAccessObj.umap_dept.dept_name,
						dept_id:$scope.userAccessObj.umap_dept.dept_id
				}
				var obj={
						umap_orga_id:$scope.userAccessObj.umap_orga.orga_id ,
						umap_loca_id:$scope.locaListArray[i].umap_loca_id,
						umap_dept_id:$scope.userAccessObj.umap_dept.dept_id
				};
				
				$scope.userAccessObj.mapping_list.unshift(obj);
				$scope.userAccessList.unshift(listObj);
				$scope.removeFromOrignalAccessArray.push(listObj);
				}
			
			}
			//alert(JSON.stringify($scope.locaListArray));
			if($scope.userAccessObj.umap_orga !=null && $scope.userAccessObj.umap_loca==null && $scope.userAccessObj.umap_dept==null){
				console.log("Asd2");
				if($scope.AccessRemaining.length!=0){
				/*	angular.forEach($scope.AccessRemaining, function (item, index) {*/
				for(var i=0;i<$scope.AccessRemaining.length;i++){
					var item=$scope.AccessRemaining[i];
					
						if($scope.userAccessObj.umap_orga.orga_id == item.orga_id){
						
							var listObj={
									loca_id:item.loca_id,
									orga_name:$scope.userAccessObj.umap_orga.orga_name,
									loca_name:item.loca_name,
									orga_id:$scope.userAccessObj.umap_orga.orga_id ,
									dept_name:item.dept_name,
									dept_id:item.dept_id
							}
							var obj={
									umap_orga_id:$scope.userAccessObj.umap_orga.orga_id ,
									umap_loca_id:item.loca_id,
									umap_dept_id:item.dept_id
							};
							$scope.userAccessObj.mapping_list.unshift(obj);
							$scope.userAccessList.unshift(listObj);
							$scope.removeFromOrignalAccessArray.push(listObj);
							
							
						}
				}
				};
			
			}
			
			
			if($scope.userAccessObj.umap_orga !=null && $scope.userAccessObj.umap_loca.length!=0 && $scope.userAccessObj.umap_dept==null){
				console.log("Asd3");
				if($scope.AccessRemaining.length!=0){
					for(var i=0;i<$scope.AccessRemaining.length;i++){
						var item=$scope.AccessRemaining[i];
						for(var i=0;i<$scope.userAccessObj.umap_loca.length;i++){
						if($scope.userAccessObj.umap_orga.orga_id  == item.orga_id  &&  $scope.userAccessObj.umap_loca[i].umap_loca_id == item.loca_id ){
						
							var listObj={
									loca_id:$scope.userAccessObj.umap_loca[i].umap_loca_id,
									orga_name:$scope.userAccessObj.umap_orga.orga_name,
									loca_name:$scope.userAccessObj.umap_loca[i].umap_loca_name,
									orga_id:$scope.userAccessObj.umap_orga.orga_id ,
									dept_name:item.dept_name,
									dept_id:item.dept_id
							}
							var obj={
									umap_orga_id:$scope.userAccessObj.umap_orga.orga_id ,
									umap_loca_id:$scope.userAccessObj.umap_loca[i].umap_loca_id,
									umap_dept_id:item.dept_id
							};
							$scope.userAccessObj.mapping_list.unshift(obj);
							$scope.userAccessList.unshift(listObj);
							$scope.removeFromOrignalAccessArray.push(listObj);
						}
					}
					}
				};
			}
			
			
		
			for(var a=0; a< $scope.removeFromOrignalAccessArray.length; a++){
				removeItem($scope.AccessRemainingOriginal, $scope.removeFromOrignalAccessArray[a]);
			}
			
			
			
			$scope.AccessRemaining=[];
			$scope.AccessRemaining = angular.copy($scope.AccessRemainingOriginal);
			$scope.getOriginalEntityArray();
			$scope.getEntityDependentArray();
			$scope.getUnitDependentArray();
			
			
		console.log("ASDasd==="+JSON.stringify($scope.userAccessObj.mapping_list));
		}
	}
	
	
	
	$scope.saveAccess=function(){
		if($scope.userAccessObj.mapping_list.length>0){
			//DataFactory.setShowLoader(true);
			//spinnerService.show('html5spinner');
			
			$('#set_access').hide();
			ApiCallFactory.saveAccess($scope.userAccessObj).success(function(res,status){
				//DataFactory.setShowLoader(false);
				//spinnerService.hide('html5spinner');
				$('#set_access').show();
				if(status === 200){
					toaster.success("Success", "User updated successfully");
					$state.go('UsersList');
				}else{
					toaster.error("Faild", "Error in update");
				}
			}).error(function(error){
				//DataFactory.setShowLoader(false);
				//spinnerService.hide('html5spinner');
				$('#set_access').hide();
				console.log("update user===="+error);
			});
	 }
	}
	
	
	if(!angular.isUndefined($stateParams.user_id) && $stateParams.user_id!=0){
		$scope.userAccessObj.umap_user_id=$stateParams.user_id;
		$scope.userAccessObj.user_id=$stateParams.user_id;
		
		$scope.userAccessObj.user_first_name=$stateParams.user_first_name;
		$scope.userAccessObj.user_last_name=$stateParams.user_last_name;
		$scope.getUserAccessList();
	}else{
		$scope.userAccessObj.user_id=$stateParams.user_id;
	}
	
	
	
	$scope.removeUserAccess=function(data,ev,index){
		$scope.removeAccess={};
		$scope.removeAccess.umap_ids=[];
		console.log("DAta "+JSON.stringify(data));
		  var confirm = $mdDialog.confirm()
        .title('Are you sure you want to remove the user access?')
        .targetEvent(ev)
        .ok('Yes')
        .cancel('NO');
  $mdDialog.show(confirm).then(function() {
	  $scope.removeAccess.umap_user_id = data.user_id;
	  var obj={
			  umap_id:data.umap_id,
			umap_orga_id:data.orga_id,
			umap_loca_id:data.loca_id,
			umap_dept_id:data.dept_id
	  }
	  $scope.removeAccess.umap_ids.push(obj);
	  console.log("Dsgf "+JSON.stringify($scope.removeAccess));
  	/* $scope.removeAccess={
  			 umap_user_id : data.user_id,
  			 umap_ids : []
  	 }*/
    ApiCallFactory.removeUserAccess($scope.removeAccess).success(function(res,status){
			if(status === 200 && res.responseMessage == "Success"){
				toaster.success("Success", "User access removed successfully.");
				$scope.userAccessList.splice(index,1);
			}else if(status === 200 && res.responseMessage == "TaskAssigned"){
				$mdDialog.show(
				      $mdDialog.alert()
				        .clickOutsideToClose(true)
				        .title('Alert')
				        .textContent('Some tasks are assigned to this user as Evaluator or Executor for this Function. Please change compliance owner first!')
				        .ok('Ok')
				        .targetEvent(ev)
				    );
		}	
		}).error(function(error){
			console.log("Error while deleting the task====="+error);
		});
  });
			  	
	  }
	
	$scope.keyPressHandler = function(e) {
	    if (e.keyCode === 13) {
	        e.preventDefault();
	        e.stopPropagation();
	    }
	}
	
}]);