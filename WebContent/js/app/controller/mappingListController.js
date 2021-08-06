'use strict';

CMTApp.controller('mappingListController', ['$scope','$rootScope','$stateParams', '$state','ApiCallFactory','$location', 'DataFactory', 'Storage','toaster','spinnerService','$window', function ($scope,$rootScope,$stateParams, $state,ApiCallFactory, $location, DataFactory, Storage,toaster,spinnerService,$window) {
	
	$scope.getUsermappinglist=function(){
		var obj={}
		spinnerService.show('html5spinner');
		ApiCallFactory.getUsermappinglist(obj).success(function(res,status){
			spinnerService.hide('html5spinner');
			if(status==200){
				$scope.mappingList=res.orgonogram;
			}
		}).error(function(error){
			spinnerService.hide('html5spinner');
			console.log("get user list====="+error);
		});
	};
	$scope.getUsermappinglist();
	
	$scope.userDetail=function(id){
		$state.go('User',{'user_id':id});
	}
	
	
	
	// to generate data in CSV format
	$scope.ExportExcels_Maintask=function(){
		/*var Curr_Date = new Date();
		var Curr_date1= moment(Curr_Date).format('DD-MM-YYYY');*/
		//$('#reportList1').tableExport({type:'excel',escape:'false'});	

		/*$("#example tr td a").removeAttr("href");
		$("#example tr td a").css("color","black");
		$("#example").table2excel({		
				    name: "Report",
				    filename: "DrilledReport_(Main Task)" 
				  }); */
		var date = new Date();
		var d = date.getFullYear() + '-' + date.getMonth() + 1 + '-' + date.getDate();
		/* var blob = new Blob([document.getElementById('example').innerHTML], {
          type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"

      });
      $window.saveAs(blob, "Report_" + d + ".xls");*/

		var tab_text="<table border='2px'><tr>";
		var textRange; var j=0;
		var tab = document.getElementById('example'); // id of table

		for(j = 0 ; j < tab.rows.length ; j++) 
		{     
			tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
		}

		tab_text=tab_text+"</table>";
		tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
		tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
		tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, "");


		var txtArea1 = document.getElementById('txtArea1');
		/*txtArea1.contentWindow.document.open("txt/html","replace");
  	txtArea1.contentWindow.document.write(tab_text);
  	txtArea1.contentWindow.document.close();
  	txtArea1.contentWindow.focus(); 
  	txtArea1.contentWindow.focus(); 
  	var sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"IE Excel Export.xls");*/
		var ua = window.navigator.userAgent;
		var msie = ua.indexOf("MSIE "); 
		var sa;
		if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
		{
			txtArea1.contentWindow.document.write(tab_text);
			txtArea1.contentWindow.document.close();
			txtArea1.contentWindow.focus(); 
			txtArea1.contentWindow.focus();
			sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"DrilledReport_"+d+"(Main Task).xls");
		}  
		else{
			// sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"IE Excel Export.xls");
			$("#example tr td a").removeAttr("href");
			$("#example tr td a").css("color","black");
			$("#example").table2excel({		
				name: "Report",
				filename: "DrilledReport_"+d+"(Main Task)" 
			});
		}
	}
	
	
	
	
	$scope.generateMaintask_Pdf=function() {
		var doc1 = new jsPDF('l', 'pt', 'a4');
		var res1 = doc1.autoTableHtmlToJson(document.getElementById("example"));
		doc1.autoTable(res1.columns, res1.data, {
			// tableWidth: 'auto',
			startY: 10,
			// startX: 5,
			styles: {
				overflow: 'linebreak',
				fontSize: 5,
			},
			columnStyles: {
				0: {columnWidth: 90}, // task id
				1: {columnWidth: 120}, // task id
				2: {columnWidth: 120}, // entity
				3: {columnWidth: 120}, // Unit
				4: {columnWidth: 120}, // function
				5: {columnWidth: 120}, // Legislation
				6: {columnWidth: 120}, // Rule
				7: {columnWidth: 140}, // Reference
				8: {columnWidth: 50}, // Who
				9: {columnWidth: 100}, // When
				10: {columnWidth: 40}, // Activity
				11: {columnWidth: 40}, // Executor
				12: {columnWidth: 40}, // legal due date
			},
			theme: 'grid',
		});		
		doc1.setProperties({
			title: 'Compliance Management Tool Report',
			subject: 'Report',
			author: 'Lexcare',
			keywords: 'lexcare compliance tool',
			creator: 'Compliance Management Tool'

		});

		doc1.cellInitialize();
		doc1.save('Main_Task_DrilledReport.pdf');	



	}
	
	
}]);