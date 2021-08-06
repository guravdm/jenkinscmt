'use strict';

CMTApp.controller('entityMappingController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService) {
	$scope.entityMapObj={};
	$scope.entityListobj={};
	$scope.unitListobj={};
	$scope.functionListobj={};
	$scope.entityMappingList=[];
	$scope.hideMappingTable=true;
	                
	if(!angular.isUndefined($stateParams.enti_id)){
		$scope.entityMapObj.enti_id=$stateParams.enti_id;
	}
	
	$rootScope.getUserRoleId = Storage.get('userDetais.sess_role_id');
	$scope.userRoleId = $rootScope.getUserRoleId;
	
	if($scope.userRoleId < 6){
		$state.go('login');
	}
	
	
//get all entity list	
	$scope.getEntityList=function(){
		ApiCallFactory.getAllEntityList().success(function(res,status){
			if(status === 200){
				if(res.length>0){
					$scope.entityListobj=res;
				}
			}else{
				toaster.error("Failed", "Error in get main entity list");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("add entity===="+error);
		});
	}
	$scope.getEntityList();
	
	
	$scope.getUnitList=function(){
		ApiCallFactory.getAllUnitList().success(function(res,status){
			if(status === 200){
				if(res.length>0){
					$scope.unitListobj=res;
				}
			}else{
				toaster.error("Failed", "Error in get main entity list");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("add entity===="+error);
		});
	}
	$scope.getUnitList();
	$scope.getFunctionList=function(){
		ApiCallFactory.getFunctionList().success(function(res,status){
			if(status === 200){
				if(res.length>0){
					$scope.functionListobj=res;
				}
			}else{
				toaster.error("Failed", "Error in get main entity list");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("add entity===="+error);
		});
	}	
	
	$scope.getFunctionList();
	
	
	
	$scope.getFunctionListByUIdEId=function(entity,unit){
		if(entity.orga_id!="" && unit.loca_id!=""){
			$scope.entityMappingList=[];
			var DumyArray=[];
		var Obj={
				"enti_orga_id":entity.orga_id,
				"enti_loca_id":unit.loca_id
				}

		DataFactory.setShowLoader(true);
		ApiCallFactory.getFunctionListByUIdEId(Obj).success(function(res,status){
			DataFactory.setShowLoader(false);
			if(status === 200){
				$scope.hideMappingTable=false;
				$scope.functionListobj=res.unmappedDepartments;
				if(res.mappedDepartments.length>0){
					
					for(var i=0; i<res.mappedDepartments.length;i++){
						DumyArray.push(res.mappedDepartments[i].dept_name);
						
					}
					$scope.array1=(DumyArray).toString();
					
					$scope.dummyObj={ orga_name:entity.orga_name,
							   unit_name:unit.loca_name,
							   dept_name:$scope.array1
								}
					$scope.entityMappingList.unshift($scope.dummyObj);
				
				}
				
			}else{
				toaster.error("Faild", "get function");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("get department===="+error);
		});
		}

	}
	

	
	//add new entity mapping
	$scope.addEntityMapping=function(formValid){
		if(formValid){
			spinnerService.show('html5spinner');
			$scope.deptListArray=[];
			if($scope.entityMapObj.dept_list.length>0){
				for(var i=0;i<$scope.entityMapObj.dept_list.length;i++){
					$scope.obj={enti_dept_id:$scope.entityMapObj.dept_list[i].dept_id}
					$scope.deptListArray.push($scope.obj);
				   }
			}
			$scope.entityMappingObj={
										"enti_orga_id":$scope.entityMapObj.enti_orga.orga_id,
										"enti_loca_id":$scope.entityMapObj.enti_loca.loca_id,
										"dept_list":$scope.deptListArray
										
									}
		DataFactory.setShowLoader(true);
		ApiCallFactory.addEntityMapping($scope.entityMappingObj).success(function(res,status){
			spinnerService.hide('html5spinner');
			//DataFactory.setShowLoader(false);
			if(res.responseMessage == "Failed")
			{
				toaster.error("Authorization Failed");
				$state.go('login');
			} 
			else {
				if(res.responseMessage == "Success") {
					$scope.loading = false;
					$scope.getFunctionListByUIdEId($scope.entityMapObj.enti_orga,$scope.entityMapObj.enti_loca);
					toaster.success("Success", "Function added successfully");
				} else {
					toaster.error("Failed", "Error in save");
				}
			} 
			
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("add mapping===="+error);
		});
		}

	}
	
	
	
/*	//update new entity mapping
	$scope.updateEntityMapping=function(formValid){
		if(formValid){
		DataFactory.setShowLoader(true);
		ApiCallFactory.updateEntityMapping($scope.entityMapObj).success(function(res,status){
			DataFactory.setShowLoader(false);
			if(status === 200){
					toaster.success("Success", "Entity mapping updated successfully");
					$state.go('EntityMappingList');
			}else{
				toaster.error("Faild", "Error in save");
			}
		}).error(function(error){
			DataFactory.setShowLoader(false);
			console.log("update entity===="+error);
		});
		}

	}*/
	
	
/*	$scope.updateMapping=function(formValid){
		if(formValid){
			DataFactory.setShowLoader(true);
			ApiCallFactory.updateEntity($scope.entityMapObj).success(function(res,status){
				DataFactory.setShowLoader(false);
				if(status === 200){
					toaster.success("Success", "Entity updated successfully");
					$state.go('EntityList');
				}else{
					toaster.error("Faild", "Error in update");
				}
			}).error(function(error){
				DataFactory.setShowLoader(false);
				console.log("add entity===="+error);
			});
	 }
	}*/
	
	
	//get entity mapping List
	/*$scope.getEntityMappingList=function(){
		ApiCallFactory.getEntityMappingList().success(function(res,status){
			$scope.entityMappingList=res;
		}).error(function(error){
			console.log("get Entity mapping list====="+error);
		});
	};*/
	
}]);