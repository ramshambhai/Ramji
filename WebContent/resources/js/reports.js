var currGroup="";
var globalGrandParentNode="";
var globalParentNode="";
var globalCurrentNode="";
var globalReportTypeSelected="";
var scannedServerData = [];
var cdrServerData = [];
var widsServerData = [];
var utcStartTime="";
var utcEndTime="";

$(document).ready(function(){
$('#target_scan').hide();
    $(".dateSelect").datetimepicker({
    	dateFormat: "yy-mm-dd",
    	timeFormat: 'HH:mm'
    }); 
loadJSTree();

        	$("#downloadReport").click(function(){
			getReport();
			});
});





function loadJSTree(){
//var sufiData=getSufiData();

 $("#leftReportTree").jstree({
                    "json_data" :{
					"data":[{
					"data":"Reports",
					"attr":{"rel":"1stLevel","id":"default"},
					"children":
					[{"data":"Operations Report","attr":{"rel":"2ndLevel","id":"oprReportId"}}]}
					]
					},
         "types": {
             'types': {
                 '1stLevel': {
                     'icon': {
                         'image': '../../'+"resources/images/RootContainer.gif"
                     }
                 },
                 "2ndLevel": {
                     "icon": {
                         "image": '../../'+"resources/images/job.gif"
                     }
                 },
                 "3rdLevel": {
                     'icon': {
                         'image': '../../'+"resources/images/ne.gif"
                     }
                 },
				 "4thLevel": {
                     "icon": {
                         "image": '../../'+"resources/images/ViewNode.gif"
                     }
                 }
             }
         },
                    "plugins" : ["themes", "json_data", "ui", "contextmenu", "crrm", "types","sort","state"]
                }).bind("select_node.jstree", function(e, data)
				{
				console.log("select node jstree");
				//var cuid=$('#leftTree').jstree('get_selected').attr('id');
				//var cuattr=$('#leftTree').jstree('get_selected').attr('rel');
				    var leftTreeSelectedNode = data.rslt.obj;
                	var list = data.rslt.obj.parents('ul');
					var parentNode = ($(list[0]).prev('a')).text();
					parentNode=parentNode.trim();
					var grandParentNode = ($(list[1]).prev('a')).text();
					grandParentNode=grandParentNode.trim();
					//var currentNode=$("#leftTree").jstree("get_selected").text();
					var currentNode=$('#leftReportTree .jstree-clicked').text();
					currentNode=currentNode.trim();
					console.log('curr is :'+currentNode);
					globalGrandParentNode=grandParentNode;
					globalParentNode=parentNode;
					globalCurrentNode=currentNode;
					var currentNodeLevel=$('#leftReportTree').jstree('get_selected').attr('rel');
					if(parentNode=='Reports' && currentNode=='Operations Report'){
					showOperationsView();
					}else{
					$('#uploadDiv').show();
					$('#reportsDiv').hide();	
					}
				});
				
	    $('#leftReportTree').bind("loaded.jstree", function (event, data) {
		
		$(this).jstree("open_all");
					/*		        	$(data.rslt.obj).find('li').find.each(function (i);
	{console.log(data);
       		data.inst.open_node($(this));
	});*/
    });
	
	     $("#leftReportTree").delegate("a", "dblclick", function (e) {
         $("#leftReportTree").jstree("toggle_node", this);
     });
	$('#leftReportTree li ul').css("color","red");
	$('#leftReportTree li a').css("background-color","green");
}

function showOperationsView(){
$('#changePasswordDiv').show();
$('#dataBackupDiv').hide();
$('#factoryResetDiv').hide();
$('#systemRestoreDiv').hide();
$('#systemTestDiv').hide();
$('#reportType').text('Operations Report');
getAllOperations();
globalReportTypeSelected="OPERATIONS";
}

        	function getReport(){
			if($("#startTime").val() == null || $("#startTime").val() == "")
            	{
            	alert("Please Fill the Start Date");
            	return;
            	}
           if($("#endTime").val() == null || $("#endTime").val() == "")
            	{
            	alert("Please Fill the End Date");
            	return;
            	}
				utcStartTime=toUtcTime($("#startTime").val());
				utcEndTime=toUtcTime($("#endTime").val());
			$('#loadingBox').show();
			$('#reportsDiv').hide();
        		$.ajax({
        			url:"../../Operations",
        			//url:"../resources/test.json",
        			data:{"methodName":"getDetailedData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val()),"operationId":$('#operationOptions').val()},
        			type:"post",
        			success:function(cdrData)
        			{
        				var cdrDetailedData = JSON.parse(cdrData);
						var nodeWiseDetailedDataTable=cdrDetailedData.nodeWiseDetailedDataTable;
						var countryWiseDetailedDataTable=cdrDetailedData.countryWiseDetailedDataTable;
						$("#detail_tab_1 .node_row").remove("");
						$("#detail_tab_1 tbody").append(nodeWiseDetailedDataTable);
						$("#detail_tab_2 .count_row").remove("");
						$("#detail_tab_2 tbody").append(countryWiseDetailedDataTable);
        				try{
						if(cdrDetailedData[0].result != undefined && cdrDetailedData[0].result == "fail")
        				{
        					alert("Limit Exceed Please try Short time span");
        				}else{
						generateCdrReport();
						}
						}catch(err){
						generateCdrReport();
						}						
        			}
					});
        	}
			
		function getScannedOprColumnsData()
        {
        		var collnames={};
        	
        
        		collnames['IP']='ip';
        		collnames['Count']='count';
        	    collnames['Packet Type']='packet_type';        	    
        		collnames['Freq']='freq';
        		collnames['Band']='band';
        		collnames['ARFCN']='arfcn';
        		collnames['UARFCN']='uarfcn';
        		collnames['MCC']='mcc';
        		collnames['Country']='country';
        		collnames['MNC']='mnc';
        		collnames['Ppr']='opr';
        		collnames['LAC']='lac';        	    
        		collnames['Cell']='cell';
        		collnames['NCC']='ncc';
        		collnames['BCC']='bcc';
        		collnames['RSSI']='rssi';
        		collnames['SNR']='snr';
        		collnames['TA']='ta';
        	    collnames['SysLoc']='sysloc';        	    
        		collnames['T-Stamp']='tstmp';
        		
        		return collnames;
        }
		
		function generateCdrReport()
        {
        		$.ajax({
        			url:"../../Operations",
        			//url:"../resources/test.json",
        			data:{"methodName":"getOprReportData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val()),"operationId":$('#operationOptions').val()},
        			type:"post",
        			success:function(cdrData)
        			{
        				cdrServerData = JSON.parse(cdrData);
        				
        				try{
        				if(cdrServerData[0].result != undefined && cdrServerData[0].result == "fail")
        				{
        					alert("Limit Exceed Please try Short time span");
        				}else{
						generateWidsReport();
						}
						}catch(err){
						generateWidsReport();
						}						
        			}
					});
        	
        } 
		
       function generateWidsReport()
        {
        		$.ajax({
        			url:"../../Operations",
        			//url:"../resources/test.json",
        			data:{"methodName":"getWidsReportData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val()),"type":$("#widsFilterOptions").val(),"operationId":$('#operationOptions').val()},
        			type:"post",
        			success:function(cdrData)
        			{
        				widsServerData = JSON.parse(cdrData);
        				try{
						if(widsServerData[0].result != undefined && widsServerData[0].result == "fail")
        				{
        					alert("Limit Exceed Please try Short time span");
        				}else{
						generateScannedOprReport();
						}
						}catch(err){
						generateScannedOprReport();
						}											
        			}
        		});
        	
        }

		function generateScannedOprReport(){
					$.ajax({
        			url:"../../Operations",
        			//url:"../resources/test.json",
        			data:{"methodName":"getOprLogsData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val()),"operationId":$('#operationOptions').val()},
        			type:"post",
        			success:function(cdrData)
        			{
        				scannedServerData = JSON.parse(cdrData);
        				
        				try{
        				if(scannedServerData[0].result != undefined && scannedServerData[0].result == "fail")
        				{
        					alert("Limit Exceed Please try with short time span");
        				}else{
						downloadOperationsExcelReport();
						}
						}catch(err){
						downloadOperationsExcelReport();
						}
					}
        		});
} 		
		
		function downloadOperationsExcelReport(){		
                    		var reportNameString="Operation Data";
                  			
        					var colArray=[];
                    		var cdrColData = getCdrColumnsData();
                    	    var widsColData = getWidsColumnsData();
							var scannedOprColumnsData=getScannedOprColumnsData();
                    	    
                    	    colArray[0]=cdrColData;
                    	    colArray[1]=widsColData;
							colArray[2]=scannedOprColumnsData;
                    	    
                    	    var xlData = [];
                    	    xlData[0] = cdrServerData;
                    	    xlData[1]=widsServerData; 
							xlData[2]=scannedServerData;
                    	    $('#loadingBox').hide();
							$('#reportsDiv').show();
                    		tablesToExcel(colArray,xlData,['CDR','WIDS','Scanned Operator'], 'Detailed Report.xls', 'Excel');
var headerText='<h3 align="center">SUMMARY REPORT</h3>'; 
headerText+='For Duration: '+utcStartTime+' (GMT/UTC) to '+utcEndTime+' (GMT/UTC)<br>';
headerText+='Generated Time: ' + new Date()+'<br><br>' ;    
var header = headerText.bold();	
							$("#tableUnion").battatech_excelexport({
							containerid: "tableUnion"
							, datatype: 'table'
							, reportName: header
							, fileName:"Summary Report"			   
							});

		}
		
		function getCdrColumnsData()
        {
        
            
        		var collnames={};
        
        		collnames['Name']='name';
        		//collnames['LOC']='loc';
        		collnames['IP']='ip';
        		collnames['Count']='count';
        		collnames['IMSI']='imsi';
        		collnames['IMEI']='imei';
        	    collnames['MCC']='mcc';        	    
        		collnames['MNC']='mnc';        		
        		collnames['Opr']='oprname';
        		collnames['Country']='country';
        		collnames['CGI']='cgi';
        		collnames['T-Angle']='angle';
        		collnames['P-Angle']='anglet';
        		collnames['Curr Opr']='c_opr';
        		collnames['Curr Country']='c_count';
        		collnames['ULAFCRN']='ulrfcn';
        		collnames['DLAFCRN']='dlarfcn';
        		collnames['TAC']='tac';
        		collnames['Power']='power';
        		collnames['RxL']='rxl';
        		collnames['Act RxL']='realrxl';
        		collnames['TA']='ta';
        	    collnames['BTS']='bts';
        	    collnames['PSC']='psc';
        		collnames['A-Type']='atype';
        		collnames['A-Gain']='again';
        		collnames['A-Height']='aheight';
        		//collnames['A-Elevation']='aelevation';
        		//collnames['A-Direction']='adirection';
				collnames['Tx Power']='txpower';        		
				collnames['BAND']='band';        		
				collnames['HBW']='hbw';        		
				collnames['VBW']='vbw'; 
				collnames['TILT']='tilt';        		
				collnames['Azimuth']='azimuth'; 				
        		collnames['Terrain Type']='ttype';        		
        		collnames['Lat Long']='latlon';
        		collnames['M-Type']='mobile_type';
				collnames['MS LOC']='msloc';
				collnames['Triangulated']='iscalulated';
        		collnames['Tstmp']='tstmp';        	    
        		return collnames;
        }
		
		function getWidsColumnsData()
        {
        	
        	var collnames={};
    		collnames['Opr Name']='name';
    		//collnames['Loc']='loc';        		        		
    		collnames['IP']='ip';        		
    		collnames['Count']='count';
    	    collnames['Power']='power';        	    
    		collnames['Freq']='freq';
    		collnames['T-Angle']='angle';
    		collnames['P-Angle']='anglet';
    		collnames['T-Stmp']='tstmp';
    		collnames['Lat-Long']='latlon';
    		//collnames['Lon']='lon';
    		collnames['A-Type']='atype';
    		collnames['A-Gain']='again';
    		collnames['A-Height']='aheight';
    		collnames['A-Elevation']='aelevation';
    		collnames['A-Direction']='adirection';
    		collnames['Logtime']='logtime';      	    
        		
        	return collnames;
        }
		
	var tablesToExceli = (function() {
    var uri = 'data:application/vnd.ms-excel;base64,'
    , tmplWorkbookXML = '<?xml version="1.0"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">'
      + '<DocumentProperties xmlns="urn:schemas-microsoft-com:office:office"><Author>Axel Richter</Author><Created>{created}</Created></DocumentProperties>'
      + '<Styles>'
      + '<Style ss:ID="Currency"><NumberFormat ss:Format="Currency"></NumberFormat></Style>'
      + '<Style ss:ID="Date"><NumberFormat ss:Format="Medium Date"></NumberFormat></Style>'
      + '</Styles>' 
      + '{worksheets}</Workbook>'
    , tmplWorksheetXML = '<Worksheet ss:Name="{nameWS}"><Table>{rows}</Table></Worksheet>'
    , tmplCellXML = '<Cell{attributeStyleID}{attributeFormula}><Data ss:Type="{nameType}">{data}</Data></Cell>'
    , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
    , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
    return function(tables, wsnames, wbname, appname) {
      var ctx = "";
      var workbookXML = "";
      var worksheetsXML = "";
      var rowsXML = "";

      for (var i = 0; i < tables.length; i++) {
	  		var th = document.querySelectorAll('#'+tables[i]+' thead tr:first-child th');
		var cols = [].reduce.call(th, function (p, c) {
		var colspan = c.getAttribute('colspan') || 1;
		return p + +colspan;
		}, 0);
        if (!tables[i].nodeType) tables[i] = document.getElementById(tables[i]);
        for (var j = 0; j < tables[i].rows.length; j++) {
          rowsXML += '<Row>'
          for (var k = 0; k < cols; k++) {
            //var dataValue = tables[i].rows[j].cells[k].getAttribute("data-value");
			if(tables[i].rows[j].cells[k]==undefined){
            dataValue="";
			}else{
			dataValue = tables[i].rows[j].cells[k].innerHTML;
			}
            ctx = {  attributeStyleID: ''
                   , nameType: 'String'
                   , data:dataValue
                   , attributeFormula:''
                  };
            rowsXML += format(tmplCellXML, ctx);
          }
          rowsXML += '</Row>'
        }
        ctx = {rows: rowsXML, nameWS: wsnames[i] || 'Sheet' + i};
        worksheetsXML += format(tmplWorksheetXML, ctx);
        rowsXML = "";
      }

      ctx = {created: (new Date()).getTime(), worksheets: worksheetsXML};
      workbookXML = format(tmplWorkbookXML, ctx);



      var link = document.createElement("A");
      link.href = uri + base64(workbookXML);
      link.download = wbname || 'Workbook.xls';
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  })();

  function getAllOperations(){
	$.ajax({
		url:"../../Operations",
		data:{"methodName":"getAllOperations"},
		type:'post',
		success:function(data)
		{
		var allOperations=JSON.parse(data);
		$('#operationOptions').empty();
		var options='<option value="-1">All</option>';
		for(var i=0;i<allOperations.length;i++){
		options+='<option value="'+allOperations[i].id+'">'+allOperations[i].name+'</option>';
		}
		$('#operationOptions').append(options);
		}
	});
}
		
		
		
		
		
		
		
		
		
		
		
		
