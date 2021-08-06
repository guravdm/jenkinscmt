'use strict';

CMTApp.controller('generateCertificateController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$mdDialog','$http','$window' , function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster, spinnerService,$mdDialog,$http,$window) {

	$scope.certificate 	= {};
	$scope.task 		= {};
	$scope.notices 		= {};
	$scope.certificatelistArray = [];

	// $scope.format = 'dd-MM-yyyy'; //$scope.formats[0];
	$scope.open_from = function() {
		$scope.popup_from.opened = true;
	};

	$scope.open_to = function() {
		$scope.popup_to.opened = true;
	};

	$scope.popup_from = {
			opened: false
	};	   
	$scope.popup_to = {
			opened: false
	};

	$scope.validateDates = function() {
		$scope.errMessage = {};
		var from_date = null;
		var from_to = null;

		var from = angular.isDefined($scope.date_from);
		if(from)
			from_date = new Date($scope.date_from);
		else
			$scope.errMessage.date_from = 'Select from date.';

		var to = angular.isDefined($scope.date_to);

		if(to)
			from_to = new Date($scope.date_to);
		else
			$scope.errMessage.date_to = 'Select to date.';

		var flag = true;
		if(from && to){
			if(from_date >= from_to){ 
				$scope.errMessage.date_from = 'From date must be less than "To" date.';
				flag = false;
			}else
				flag = true;

			if(from_to <= from_date){ 
				$scope.errMessage.date_to = 'To date must be greater than "From" date.'; 
				flag = false;
			}else
				flag = true;
		}

		if(from && to && flag)
			return true;
		else
			return false;

	}

	if(!angular.isUndefined($stateParams.from) && $stateParams.from!=0){
		$scope.certificate.from = $stateParams.from;
		//$window.sessionStorage.setItem('task_id',$stateParams.task_id);
	}

	/*if(!angular.isUndefined($stateParams.to) && $stateParams.to!=0){
		$scope.certificate.to = $stateParams.to;
		//$window.sessionStorage.setItem('task_id',$stateParams.task_id);
	}

	if(!angular.isUndefined($stateParams.year) && $stateParams.year!=0){
		$scope.certificate.year = $stateParams.year;
		//$window.sessionStorage.setItem('task_id',$stateParams.task_id);
	}

	if($stateParams.from ==1 && $stateParams.to==3){
		$scope.certificate.month = "January-March"
	}
	if($stateParams.from ==4 && $stateParams.to==6){
		$scope.certificate.month = "April-June"
	}
	if($stateParams.from ==7 && $stateParams.to==9){
		$scope.certificate.month = "July-September"
	}
	if($stateParams.from ==10 && $stateParams.to==12){
		$scope.certificate.month = "October-December"
	}*/

	/*$scope.gernerateCertificate = function(){
		//alert($scope.certificate.querter);
		if($scope.certificate.querter==1){
			$scope.certificate.from = 1;
			$scope.certificate.to = 3;
		}
		if($scope.certificate.querter==2){
			$scope.certificate.from = 4;
			$scope.certificate.to = 6;
		}
		if($scope.certificate.querter==3){
			$scope.certificate.from = 7;
			$scope.certificate.to = 9;
		}
		if($scope.certificate.querter==4){
			$scope.certificate.from = 10;
			$scope.certificate.to = 12;
		}

	//	$state.transitionTo('certificate', {'from':$scope.certificate.from,'to':$scope.certificate.to,'year':$scope.certificate.year});
	}*/

	$scope.gernerateCertificate = function(){

		spinnerService.show('html5spinner');

		$scope.certificate.date_from 	= moment($scope.date_from).format('DD-MM-YYYY');
		$scope.certificate.date_to 	= moment($scope.date_to).format('DD-MM-YYYY');
		//if(!angular.isUndefined($stateParams.from) && $scope.certificate.from!=0 && !angular.isUndefined($stateParams.to) &&  $scope.certificate.to !=0 && $scope.certificate.year!=0){
		//if($scope.validateDates()){
		//$scope.certificate.currDate = moment(new Date()).format('DD-MM-YYYY')


		ApiCallFactory.getCertificateDetails($scope.certificate).success(function(res, status) {
			// spinnerService.show('html5spinner');
			if(status === 200){
				//$('#editConfiguration').modal("hide");

				$scope.task    = res.Task;
				$scope.notices = res.Notices;
				$scope.legTask = res.LegTask;
				toaster.success("Success", "Certificate generated successfully.");
				spinnerService.hide('html5spinner');
				location.reload();
//				$window.location.reload();
			}else{
				toaster.error("Failed", "Error in dates.");
				spinnerService.hide('html5spinner');
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get certificate details===="+error);
		});


		//}

	}
	//$scope.getCertificate();

	$scope.printCertificate = function(){

		var printContents = document.getElementById("printPage").innerHTML;
		var popupWin = window.open('', '_blank', 'width=300,height=1000');
		popupWin.document.open();
		popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" /></head><body onload="window.print()">' + printContents + '</body></html>');
		popupWin.document.close();

		/*$("#btnPrint").css("display","none");
	 	$("a").removeAttr("href");
	 	$("a").css("color","black");
	 	//$("@page").css("size","landscape");
	 	$("#imgLogo").css("margin-bottom","40px");
		$("#lastP").css("margin-bottom","300px");
		$("p").css("margin-bottom","8px");
		//// add css to table 
				$("table").css("border-collapse","collapse");
				$("h4").css("text-align","center");
				$("#example_length").hide();
				$("#example_info").hide();
				$(".pagination").hide();
				$("#example_filter").hide();
				$("tr").css("font-size","12px");
				$("td").css("font-size","11px");
			    $("#tblcerti").css("border-style","thin");
				$("#tblcerti").css("font-family","'Helvetica Neue', Helvetica, Arial, sans-serif");
				$("tr").css("border","1.5px solid #ddd");
				$("td").css("border","1.5px solid #ddd");
		 		$("p").css("text-align","justify");
				$("td").css("vertical-align","top"); 


    var contents = $("#printPage").html();
    var frame1 = $('<iframe />');
    frame1[0].name = "frame1";
    frame1.css({ "position": "absolute", "top": "-1000000px" });
    $("body").append(frame1);
    var frameDoc = frame1[0].contentWindow ? frame1[0].contentWindow : frame1[0].contentDocument.document ? frame1[0].contentDocument.document : frame1[0].contentDocument;
    frameDoc.document.open();
    frameDoc.document.write(contents);
    frameDoc.document.close();*/

	}


	$scope.downloadCertificate = function() {
		ApiCallFactory.downloadCertificateDetails().success(function(res, status) {
			spinnerService.hide('html5spinner');
			console.log('res : ' + res);
			if (status === 200) {
				$scope.certificatelistArray =  res;
				console.log('certificatelistArray: ' + $scope.certificatelistArray.from);
			} else {

			}
		})
		.error(function(error) {
			spinnerService.hide('html5spinner');
			console.log("get certificate details===="+ error);
		});

	}
	$scope.downloadCertificate();

	$scope.downloadDocument = function(certificate){
		$window.location = "./downloadDocuments?certificate=" + certificate;
		//$state.transitionTo('taskDetails', {'task_id':$window.sessionStorage.getItem('task_id')});
	}

	$scope.download = function(certificate){
		//$window.location = "./download?certificate=" +certificate;
		//alert(certificate);
		console.log('certificate : ' + certificate);
		$scope.certificateName = {
				certificateName : certificate
		};
		ApiCallFactory.downloadCertificate($scope.certificateName).success(function(res, status) {
			spinnerService.hide('html5spinner');
			if (status === 200) {
				$scope.certificatelist = res;
				console.log('list of certificates : ' + $scope.certificatelist);
			} else {

			}
		})
		.error(function(error) {
			spinnerService.hide('html5spinner');
			console.log("get certificate details===="+ error);
		});


	}

}]);