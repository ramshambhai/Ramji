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
        	
			$("#purgeData").click(function(){
				purgeData();
			});
        	
			$("#resetAllData").click(function(){
				resetAllData();
			});
			
			$("#fullDbBackup").click(function(){
				fullDbBackup();
			});
			
        	$("#systemLogReport").click(function(){
        		getSystemLogReport();
			});
			
			$("#factoryResetConfig").click(function(){
				factoryResetConfig();
			});
			
			$("#changePasswordButton").click(function(){
				updatePassword('admin');
			});
			
			$("#saveProfBtn").click(function(){
				saveProfile();
			});
			
			$("#showCurrProfBtn").click(function(){
				getCurrentProfileConfig();
			});
			
			$("#showProfBtn").click(function(){
				getProfileConfig();
			});
			
			$("#applyProfBtn").click(function(){
				applyProfile();
			});
			
			$("#deleteProfBtn").click(function(){
				deleteProfile();
			});
			
			getSysUsersBasedOnLevel();
			//getBackupHistory();
});

	var factoryResetConfig =function(){
		var confirmStatus=confirm("Are you sure to Factory Reset Configurations?");
		if(confirmStatus){
		$.ajax
		({
			url:"../../service/common/factoryresetconfig",
			async:true,
			type: "get",
			success:function(data)
			{  
                                        if(data.result=="success")
                                        {
											alert("Factory Reset Configurations applied successfully.");
											window.location.reload();
											
										}else{
											alert(data.message);
										}
			},
			error:function()
				{
					alert("Change password failed", function() {	});
				}
		});
		}
	}

	var updatePassword =function(type)
	{
	var changedPassword =  $("#newAdminPassword").val();
	var confirmPassword = $("#confirmAdminPassword").val();
	var match = false;
		var oldPassword = "not applicable";
		var userId = $('#userDataSelect').val();
		var userName = $('#userDataSelect option:selected').text();
    if(changedPassword=="" || confirmPassword=="")
    {
       alert("Password cannot be blank");
    }else if(changedPassword == confirmPassword)
	{
		match = true;
	}else
	{
		alert("Confirm New Password mismatch");
	}
	if(match==true)
	{
		$.ajax
				({
					url:"../../service/common/changepassword",
					async:true,
					type: "post",
					data:
					{
						"type":type,
						"userId":userId,
						"userName":userName,
						"oldPassword":oldPassword,
						"changedPassword":changedPassword,
						"confirmPassword":confirmPassword
					},
					success:function(data)
					{  
                                                if(data.result=="success")
                                                {
													alert("Password Changed Successfully");
													window.location.reload();
													
												}else{
													alert(data.message);
												}
					},
					error:function()
						{
							alert("Change password failed", function() {	});
						}
				});
	}
}

var getBackupHistory = function(){
			$.ajax
				({
					url:"../../service/common/getbackuphistory",
					type: "post",
					success:function(data)
					{ 
						$("#backupHistoryTableTbody").html("");
						var rows="";
						var fileArray=data.fileNames;
						for(var i=0;i<fileArray.length;i++){
							rows+='<tr><td>'+fileArray[i]+'</td>'+
								  '<td><input type="button" class="backupButton btn-default" value="Download" id="downloadBackupHistory" onclick="downloadBackupHistory(\''+data.dirName+'\',\''+fileArray[i]+'\')" /></td>'+
								  '<td><input type="button" class="backupButton btn-default" value="Delete" id="deleteBackupHistory" onclick="deleteBackupHistory(\''+data.dirName+'\',\''+fileArray[i]+'\')" /></td></tr>';
						}
						$("#backupHistoryTableTbody").html(rows);
					},
					error:function()
						{
							//alert("Error in getting backup history", function() {	});
							console.log("Error in getting backup history");
						}
				});

}

var downloadBackupHistory = function(dirName,fileName){

				window.location.href='../../resources/'+dirName+'/'+fileName;

}

var deleteBackupHistory = function(dirName,fileName){
	$.ajax
	({
			url:"../../service/common/deletebackuphistory",
			type:'post',
			data:{"dirName":dirName,"fileName":fileName},
			success:function(data)
			{
			if(data.result=="success"){
				alert("Backup Deleted Successfully");
				window.location.reload();
			}else{
				alert("Problem in deleting Backup");
			}
			},
			error:function(data){
				alert("Problem in deleting Backup");
			}
	});
}

function loadJSTree(){
//var sufiData=getSufiData();

 $("#leftReportTree").jstree({
                    "json_data" :{
					"data":[{
					"data":"Admin",
					"attr":{"rel":"1stLevel","id":"adminId"},
					"children":
					[
					{"data":"User Management","attr":{"rel":"2ndLevel","id":"changePasswordId"}},
					{"data":"Data Backup/Reset","attr":{"rel":"2ndLevel","id":"dataBackupId"}},
					{"data":"Backup History","attr":{"rel":"2ndLevel","id":"backupHistoryId"}},
					{"data":"Save/Apply/Delete Config","attr":{"rel":"2ndLevel","id":"configOperationsId"}}
					]}
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
                    "plugins" : ["themes", "json_data", "ui", "crrm", "types","sort","state"]
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
					if(currentNode=='Admin'){
						showBackupHistoryView();
					}else if(parentNode=='Admin' && currentNode=='User Management'){
						showUserManagementView();
					}else if(parentNode=='Admin' && currentNode=='Data Backup/Reset'){
						showBackupPurgeView();
					}else if(parentNode=='Admin' && currentNode=='Save/Apply/Delete Config'){
						showConfigOperationsView();
					}else{
						getBackupHistory();
						showBackupHistoryView();
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

var showUserManagementView = function(){
	$('#changePasswordDiv').show();
	$('#backupPurgeDiv').hide();
	$('#backupHistoryDiv').hide();
	$('#configOperationsDiv').hide();
	//$('#operationType').text('Data Backup/Purge');
}

var showBackupHistoryView = function(){
	$('#changePasswordDiv').hide();
	$('#backupPurgeDiv').hide();
	$('#backupHistoryDiv').show();
	$('#configOperationsDiv').hide();
	//$('#operationType').text('Data Backup/Purge');
}

var showBackupPurgeView = function(){
	$('#changePasswordDiv').hide();
	$('#backupPurgeDiv').show();
	$('#backupHistoryDiv').hide();
	$('#configOperationsDiv').hide();
	//$('#operationType').text('Data Backup/Purge');
}

var showConfigOperationsView = function(){
	$('#changePasswordDiv').hide();
	$('#backupPurgeDiv').hide();
	$('#backupHistoryDiv').hide();
	$('#configOperationsDiv').show();
	getConfigProfiles();	
	//$('#operationType').text('Data Backup/Purge');
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
				var repStartTime=toUtcTime($("#startTime").val())+':00';
				var repEndTime=toUtcTime($("#endTime").val())+':00';
				$('#loadingBox').show();
				$('#backupPurgeDiv').hide();
        		$.ajax({
        			url:"../../service/common/databackup",
        			//url:"../resources/test.json",
        			data:{"startTime":repStartTime,"endTime":repEndTime},
        			type:"get",
        			success:function(data)
        			{
			if(data.result=="success"){
				$('#loadingBox').hide();
				$('#backupPurgeDiv').show();
				window.location.href='../../resources/reports/'+data.msg;
				//alert('Operation Stopped Successfully');
			//window.location.reload();
			}else{
				//alert("Problem in Stopping Operation");
			}						
        			}
					});
    }

function getSystemLogReport(){

		$('#loadingBox').show();
		$('#backupPurgeDiv').hide();
		$.ajax({
			url:"../../service/common/systemlogs",
			//url:"../resources/test.json",
			type:"get",
			success:function(data)
			{
	if(data.result=="success"){
		$('#loadingBox').hide();
		$('#backupPurgeDiv').show();
		window.location.href='../../resources/syslogs/'+data.msg;
		//alert('Operation Stopped Successfully');
	//window.location.reload();
	}else{
		//alert("Problem in Stopping Operation");
	}						
			}
			});
}

function fullDbBackup(){

	$('#loadingBox').show();
	$('#backupPurgeDiv').hide();
	$.ajax({
		url:"../../service/common/fulldbbackup",
		//url:"../resources/test.json",
		type:"get",
		success:function(data)
		{
if(data.result=="success"){
	$('#loadingBox').hide();
	$('#backupPurgeDiv').show();
	alert("Full backup taken successfully");
	//window.location.href='../../resources/syslogs/'+data.msg;
	//alert('Operation Stopped Successfully');
	//window.location.reload();
}else{
	alert("Problem in taking full backup");
	$('#loadingBox').hide();
}						
		}
		});
}
	
	function purgeData(){
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
				$('#backupPurgeDiv').hide();
        		$.ajax({
        			url:"../../service/common/datapurge",
        			//url:"../resources/test.json",
        			data:{"startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
        			type:"get",
        			success:function(data)
        			{
			if(data.result=="success"){
				$('#loadingBox').hide();
				$('#backupPurgeDiv').show();
				alert("Data resets successfully");
				//alert('Operation Stopped Successfully');
			//window.location.reload();
			}else{
				alert("Problem in reset");
				//alert("Problem in Stopping Operation");
			}						
        			}
					});
    }
	
	function resetAllData(){
		var eventDataType=$('#eventDataType').val();
		if(eventDataType=="select"){
			alert("Please select valid option");
			return;
		}
		 var resetAllConfirmStatus=confirm("Are you sure to reset data?");
		 if(resetAllConfirmStatus){
		    $('#loadingBox').show();
			$('#backupPurgeDiv').hide();
			
    		$.ajax({
    			url:"../../service/common/dataresetall",
    			data:{"eventDataType":eventDataType},
    			//url:"../resources/test.json",
    			type:"get",
    			success:function(data)
    			{
		if(data.result=="success"){
			$('#loadingBox').hide();
			$('#backupPurgeDiv').show();
			alert("Data resets successfully");
			//alert('Operation Stopped Successfully');
		//window.location.reload();
		}else{
			alert("Problem in reset");
			//alert("Problem in Stopping Operation");
		}						
    			}
				});
		}
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

	function getSysUsersBasedOnLevel(){
			$.ajax({
		url:"../../service/common/getsysuserbasedonlevel",
		type:'post',
		success:function(data)
		{
			$('#userDataSelect').html('');
			var options='';
			for(var i=0;i<data.length;i++){
				options+='<option value="'+data[i].id+'">'+data[i].user_name+'</option>';
			}
			$('#userDataSelect').html(options);
		}
	});
	}
	
	var getCurrentConfig =function(){
		$.ajax({
			url:"../../service/common/getcurrentconfig",
			type:'get',
			success:function(data)
			{
				displayConfig(data);
			}
		});
	}
	
	var getProfileConfig =function(){
		var profileId = $('#profileList').val();
		if(profileId=='Select'){
			alert("Please select Profile");
			return;
		}
		$.ajax({
			url:"../../service/common/getprofileconfig",
			type:'get',
			data:{"profileId":profileId},
			success:function(data)
			{
				displayConfig(data);
			}
		});
	}
	
	var getCurrentProfileConfig =function(){
		$.ajax({
			url:"../../service/common/getcurrentconfig",
			type:'get',
			success:function(data)
			{
				displayConfig(data);
			}
		});
	}
	
	var getConfigProfiles =function(){
		$.ajax({
			url:"../../service/common/getconfigprofiles",
			type:'get',
			success:function(data)
			{
				$('#profileList').html('');
				
				var options='<option value="Select">Select</option>';
				for(var i=0;i<data.length;i++){
					options+='<option value="'+data[i].id+'">'+data[i].prof_name+'</option>';
				}
				$('#profileList').html(options);
			}
		});
	}
		
		var saveProfile =function(){
			var profileName=$('#profileName').val();
			if(profileName==''){
				alert('Please provide valid Profile name');
				return;
			}
			
			if(profileName.length>20){
				alert('Profile name can have max length of 20');
				return;
			}
			
			$.ajax({
				url:"../../service/common/saveprofile",
				type:'post',
				data:{"profileName":profileName},
				success:function(data)
				{
					if(data.result=='success'){
						alert('Profile '+profileName+' saved successfully');
						$('#profileName').val("");
						$('#profileList').append('<option value="'+data.id+'">'+profileName+'</option>');
					}else{
						alert('Problem in saving profile '+profileName+'.'+data.message);
					}
				}
			});
		}
		
		var deleteProfile =function(){
			var profileId=$('#profileList').val();
			var profileName=$('#profileList option:selected').text();
			if(profileId=='Select'){
				alert("Please select Profile");
				return;
			}
			$.ajax({
				url:"../../service/common/deleteprofile",
				type:'post',
				data:{"profileId":profileId,"profileName":profileName},
				success:function(data)
				{
					if(data.result=='success'){
						alert('Profile '+profileName+' deleted successfully');
						$('#profileList option:selected').remove();
					}else{
						alert('Problem in deleting profile'+profileName+','+data.result);
					}
				}
			});
		}
		
		var applyProfile =function(){
			var profileId=$('#profileList').val();
			var profileName=$('#profileList option:selected').text();
			if(profileId=='Select'){
				alert("Please select Profile");
				return;
			}
			$.ajax({
				url:"../../service/common/applyprofile",
				type:'post',
				data:{"profileId":profileId,"profileName":profileName},
				success:function(data)
				{
					if(data.result=='success'){
						alert('Profile '+profileName+' applied successfully');
					}else{
						alert('Problem in applying profile '+profileName+','+data.msg);
					}
				}
			});
		}
		
		var displayConfig = function(data){
			
			$('#displayAntennaProfileTable_body').html('');
			var antennaData=data.antenna;
			var antennaRows='';
			for(var i=0;i<antennaData.length;i++){
				
				antennaRows+='<tr id="antennaProfileRow'+antennaData[i].id+'">';
				           if(antennaData[i].inscanning=='t'){
							   isChecked='checked';
						   }else{
							   isChecked='';
						   }
				antennaRows+='<td><input type="checkbox" value="'+antennaData[i].id+'" id="antennaScanning'+antennaData[i].id+'" name="scanningAntennaCheck" class="scanningAntennaCheck editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled '+isChecked+' /></td>';
						   if(antennaData[i].intracking=='t'){
							   isChecked='checked';
						   }else{
							   isChecked='';
						   }
			    antennaRows+='<td><input type="checkbox" value="'+antennaData[i].id+'" name="trackingAntennaCheck" class="trackingAntennaCheck editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled '+isChecked+' /></td>'+
						   '<td>'+antennaData[i].profile_name+'</td>'+
						   '<td>'+antennaData[i].type_name+'</td>'+
						   '<td>'+antennaData[i].angle_covered+'</td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].txpower+'</span></td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].band+'</span></td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].gain+'</span></td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].elevation+'</span></td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].hbw+'</span></td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].vbw+'</span></td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].tilt+'</span></td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].azimuth+'</span></td>'+
						   '<td><span class="editAntennaInput editSameAntennaInput'+antennaData[i].id+'" disabled >'+antennaData[i].terrain+'</span></td>'+
						   '</tr>';
						   $('#antennaTerrain'+antennaData[i].id).val(antennaData[i].terrain);
				if(antennaData[i].atype=='1'){
					$('#sectorAntennaAngleOffset').val(antennaData[i].angle_offset);
				}
			}
			$('#displayAntennaProfileTable_body').append(antennaRows);
			$('#displayTargetTable_body').html('');
			var targetData=data.target_list;
			var targetListRows='';
			for(var i=0;i<targetData.length;i++){
				targetListRows+='<tr><td>'+targetData[i].name+'</td><td>'+targetData[i].imsi+'</td><td>'+targetData[i].imei+'</td><td>'+targetData[i].type+'</td></tr>';
			}
			$('#displayTargetTable_body').html(targetListRows);
			
			$('#systemProperties_tbody').html('');
			var systemPropertiesData=data.system_properties;
			var systemPropertiesRows='';
			for(var i=0;i<systemPropertiesData.length;i++){
				if(systemPropertiesData[7].value=='Standalone'){
					if(i==2||i==3||i==4)
						continue;
				}

				systemPropertiesRows+='<tr><td>'+systemPropertiesData[i].display_name+'</td><td>'+systemPropertiesData[i].value+'</td></tr>';
			}
			$('#systemProperties_tbody').html(systemPropertiesRows);
			
			$('#configurationDisplayModal').modal('show');
			
				
			}
		
		
		
		
		
		
		
		
		
		
		
		
