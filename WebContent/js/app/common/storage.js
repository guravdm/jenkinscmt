'use strict';

CMTApp.factory('Storage',[ function(){
	var Storage = {};
	
	Storage.set = function (key, value){
		if(Storage.isSessionStorageSupported()){
			sessionStorage.setItem(key, angular.toJson(value));
		}
	};
	
	Storage.get = function (key){
		if(Storage.isSessionStorageSupported()){
		///	return angular.fromJson(sessionStorage.getItem(key));
			return sessionStorage.getItem(key);
		}
	};
	
	Storage.clear = function (){
		if(Storage.isSessionStorageSupported()){
			sessionStorage.clear();
		}
	};
	
	Storage.remove = function (key){
		if(Storage.isSessionStorageSupported()){
			sessionStorage.removeItem(key);
		}
	};
	
	Storage.isSessionStorageSupported = function(){
		var storageTestKey = 'sTest',
		storage = window.sessionStorage;

		try {
			storage.setItem(storageTestKey, 'test');
			storage.removeItem(storageTestKey);
		} catch (e) {
			if (e.code == DOMException.QUOTA_EXCEEDED_ERR && storage.length == 0) {
				// private mode
			} 
			return false;
		}
		return true;
	};
	
	return Storage;
}]);
