var dirPath='../../';
var colourOfManuallyAddedNode='#C4CCC4';
var dataOfNodeWiseReport=[];
var dataOfCountryWiseReport=[];
var dataOfNetworkScanReport=[];
var globalType="-1";
var globalValue="[-1]";
var globalFilter="-1";
var globalBts="-1";
var globalCountry="-1";
var globalOprname="-1";
var globalCdrCount=0;
$(document).ready(function(){

createNodeWiseReport();
createCountryWiseReport();
createNetworkScanReport();
$('#nodeWiseExcelExportId').click(function(){
nodeWiseExcelExport();
});
$('#countryWiseExcelExportId').click(function(){
countryWiseExcelExport();
});
$('#networkScanExcelExportId').click(function(){
networkScanExcelExport();
});

//loadEvents();

/*$('#detail_tab_3 tbody tr td').click(function(){
displayTableDetailsModal(this);
});*/

$( "#bl_table tbody" ).on( "dblclick","tr", function() {
    displayTableDetailsModal($(this),'Detected Devices');
});

$( "#el_table tbody" ).on( "dblclick","tr", function() {
    displayTableDetailsModal($(this),'Events');
});

$( "#list_table tbody" ).on( "dblclick","tr", function() {
    displayTableDetailsModal($(this),'Device Status');
});

$( "#detail_tab_2 tbody" ).on( "dblclick","tr", function() {
    displayTableDetailsModal($(this),'Mobile Information');
});

$( "#detail_tab_3 tbody" ).on( "dblclick","tr", function() {
    displayTableDetailsModal($(this),'Cell Information');
});

getManualOverrideStatus();
$('#manual_check').click(function(){
	startActionOnManualCheck();
});


var intervalId = window.setInterval(function(){
//	document.getElementById("JammerChart").style.height = "162%";
//	document.getElementById("JammerChart").style.height = "141%";
	document.getElementById("myChart").style.height = "190px";
	document.getElementById("myChart").style.width = "100%";

	  /// call your function here
	}, 1000);

//document.getElementById("detail_tab_3").style.height = "21%";

});


function showNeighbours(packetId,tech,ueafrcn,antenna){

	$('#neighbourTableTbody').html('');
	
	$("#showNeighbours").css("z-index",99999);
	

	
	data={"tech":tech,"packetID":packetId,"ueafrcn": ueafrcn,"ant":antenna};
	$.ajax({
		url:"../../service/common/getNeighboursDataa",
		data:data,
		type:'post',
		success:function(data)
		{
			
          for(i = 0; i < data.length; i++){
				$('#neighbourTableTbody').append('<tr><td>'+(i+1)+'</td><td>'+data[i].tech+'</td><td>'+data[i].plmn+'</td><td>'+data[i].lactac+'</td><td>'+data[i].cell+'</td><td>'+data[i].auearfcn+'</td><td>'+data[i].bscipscpci+'</td><td>'+data[i].rssi+'</td></tr>');
			
			}
            
				
		},
		error:function(){
			
		}
	});

	

	$('#showNeighbours').modal('show');
}
	
var startActionOnManualCheck = function(){
	var manualCheck=$("#manual_check").prop("checked");
	var manualConfirm="";
	if(manualCheck){
		manualConfirm = confirm("Are you sure to enable Manual Override");
	}else{
		manualConfirm = confirm("Are you sure to disable Manual Override");
	}
	
	if(manualConfirm){
	$.ajax({
		url:dirPath+"service/common/updatemanualstatus",
		data:{"manualCheck":manualCheck},
		type:'get',
		success:function(data)
		{	
			if((data=="true" || data=="t") && (manualCheck)){
				window.location.href = 'configupdate.jsp';
			}
			$('#text_btn').text("Off");
			$('#text_btn').css("margin","30px");
		},
		error:function(txx)
		{
			alert("Error in changing Manual Override");
		}
	});
	}else{
		if(manualCheck){
			$("#manual_check").prop("checked",false);
			$('#text_btn').text("Off");
			$('#text_btn').css("margin","30px")
		}else{
			$("#manual_check").prop("checked",true);
			$('#text_btn').text("On");
			$('#text_btn').css("margin","6px");
		}
	}
}

var getManualOverrideStatus = function(){
		 $.ajax({
			url:dirPath+"service/common/manualstatus",
			async:false,
			type:'get',
			success:function(data)
			{
				if(data=="true" || data=="t"){
					$('#manual_check').prop("checked",true);
					$('#text_btn').text("On");
					$('#text_btn').css("margin","6px");
					
				}else{
					$('#manual_check').prop("checked",false);
					$('#text_btn').text("Off");
					$('#text_btn').css("margin","30px");
				}
					
				
			},
			error:function(txx)
			{
				alert("Error in getting Manual Override Status");
			}
		});
}

var displayCapturedEvents = function(){
 globalCapturedEventShowStatus="true";
 $('#captured_events_modal').modal('show');
 loadEvents();
}

var actionOnClick = function(){
	globalCapturedEventShowStatus="false";
}
function displayTableDetailsModal(elem,modalHeader){
/*elem.find("td").each(function(){
	alert($(this).text());
});*/
/*elem.parent().parent().find("thead").find("tr").find("th").each(function(){
 alert($(this).text());
});*/
	var columnElem=elem.parent().parent().find("thead").find("tr");
	var dataElem=elem.find("td");
	var totalColumns=dataElem.length;
	$("#selectedTableDetailsLabel").text(modalHeader);
	var rows="";
	$('#selectedTableDetailsLabel').text();
	for(var i=1;i<=totalColumns;i++){
		rows+='<tr><td>'+columnElem.find("th:nth-child("+i+")").text()+'</td><td>'+elem.find("td:nth-child("+i+")").text()+'</td><tr>';
	}
	$("#selectedTableDetails_body").html(rows);
	$('#selectedTableDetailsModal').modal('show');
}


var getType = function(type)
{
	if(type == 'subTotal' || type == 'total' || type==undefined)
	{
		return "-1";
	}
	else
	{
		return type;
	}
}

var getCountryWiseReport = function(countryName,oprName,mainFilter,subFilter){
	 $.ajax({
		url:dirPath+"Operations",
		async:false,
		type:'post',
		data:{methodName:"getCountryWiseReport",countryName:countryName,oprName:oprName,mainFilter:mainFilter,subFilter:subFilter,operationStartTime:operationStartTIme},
		beforeSend:function()
		{
			$("#loading_modal").modal('show');
		},
		success:function(data)
		{
			dataOfCountryWiseReport=eval(data);
			reloadCountryWiseReport();
			globalType=mainFilter;
			globalBts="-1";
			globalCountry=countryName;
			globalOprname=oprName;
			globalValue="[-1]";
			globalFilter="-1";
			
			if(mainFilter=="ta"){
			if(subFilter=="lessthan5"){
			globalValue="[2,5]";
			globalFilter="less";
			}else if(subFilter=="morethan5"){
			globalValue="[5]";
			globalFilter="more";
			}else{
			globalValue="["+subFilter+"]";
			globalFilter="equal";
			}
		}else{
			if(subFilter=="morethan-55"){
			globalValue="[55]";
			globalFilter="more";
			}else if(subFilter=="morethan-75"){
			globalValue="[55,75]";
			globalFilter="more";
			}else if(subFilter=="morethan-95"){	
			globalValue="[75,95]";
			globalFilter="more";
			}else if(subFilter=="lessthan-95"){
			globalValue="[95]";
			globalFilter="less";
			}else{
			globalValue="["+subFilter+"]";
			globalFilter="equal";	
			}
		}
			$("#countryWiseReportModal").modal('show');
			$("#loading_modal").modal('hide');
			
		},
		error:function(txx)
		{
			$("#loading_modal").modal('hide');
		}
	});
}

var getNetworkScanReport = function(){
	 $.ajax({
		url:dirPath+"Operations",
		async:false,
		type:'post',
		data:{methodName:"getNetworkScanReport"},
		success:function(data)
		{
			//$("#detail_tab_3 .count_row_net").remove("");
			$("#detail_tab_3 tbody").html("");
			dataOfNetworkScanReport=eval(data);
			var rows='';
			var manualCells=[];
			for(var i=0;i<dataOfNetworkScanReport.length;i++){
				var oprKey=dataOfNetworkScanReport[i].mcc+"_"+dataOfNetworkScanReport[i].mnc+"_"+dataOfNetworkScanReport[i].lac+"_"+dataOfNetworkScanReport[i].cell+"_"+dataOfNetworkScanReport[i].profile_name;
				globalOpr.push(oprKey);
				var bandwdthrow="";
			 	if((dataOfNetworkScanReport[i].bandwidth.localeCompare("null")==0) || (dataOfNetworkScanReport[i].bandwidth.localeCompare("")==0))
			 	{
			 		 bandwdthrow='<td></td>'
			 	}
		 		else
			 	{
			   		bandwdthrow='<td>'+dataOfNetworkScanReport[i].bandwidth+'</td>';
			   	
			   	}
			 	
			  	
			   	var bsicValue="";
			   	if((dataOfNetworkScanReport[i].packet_type).localeCompare("GSM")==0){
			   		
			   		var ncc=dataOfNetworkScanReport[i].ncc;
			   		ncc=ncc*8;
			   		bsicValue=dataOfNetworkScanReport[i].bcc+""+ncc;
			   	}
			   	
			 	
			 	
				var dateTimeArray = dataOfNetworkScanReport[i].insert_time.split(" "); 
				rows+='<tr id="opr_'+oprKey+'">'+
				   	'<td>'+dateTimeArray[0]+'</td>'+
				   	'<td>'+dateTimeArray[1]+'</td>'+
				   	'<td>'+dataOfNetworkScanReport[i].packet_type+'</td>'+
				   	'<td>'+dataOfNetworkScanReport[i].profile_name+'</td>'+
				   	
				   	'<td>'+dataOfNetworkScanReport[i].freq+'</td>'+  
				   	//'<td>'+dataOfNetworkScanReport[i].earfcn+dataOfNetworkScanReport[i].arfcn+dataOfNetworkScanReport[i].uarfcn+'</td>'+  
				   	
				   	'<td><button class="btn btn-default" style="height: 25px;" onclick="showNeighbours('+dataOfNetworkScanReport[i].packet_id+',\''+dataOfNetworkScanReport[i].packet_type+'\' , \''+ dataOfNetworkScanReport[i].earfcn+dataOfNetworkScanReport[i].arfcn+dataOfNetworkScanReport[i].uarfcn+'\'      , \''+dataOfNetworkScanReport[i].profile_name + '\' )"><div class="showNeighbourClass">'+	dataOfNetworkScanReport[i].arfcn+dataOfNetworkScanReport[i].uarfcn+dataOfNetworkScanReport[i].earfcn+'</div></td>'+
				   	
				   	'<td>'+dataOfNetworkScanReport[i].name+'</td>'+
				   	'<td>'+dataOfNetworkScanReport[i].mcc+'</td>'+
				   	'<td>'+dataOfNetworkScanReport[i].mnc+'</td>'+
				   	'<td>'+dataOfNetworkScanReport[i].lac+dataOfNetworkScanReport[i].tac+'</td>'+
				   	'<td>'+dataOfNetworkScanReport[i].cell+'</td>'+
				   	'<td>'+dataOfNetworkScanReport[i].operators+'</td>'+
				   	'<td>'+dataOfNetworkScanReport[i].rssi+'</td>'+
				   	
				   	'<td>'+dataOfNetworkScanReport[i].pci+dataOfNetworkScanReport[i].psc+bsicValue+'</td>'+
				   	bandwdthrow+
				   //	'<td><button class="btn btn-default" style="height: 22px;" onclick="showNeighbours('+dataOfNetworkScanReport[i].packet_id+',\''+dataOfNetworkScanReport[i].packet_type+'\' , \''+ dataOfNetworkScanReport[i].earfcn+dataOfNetworkScanReport[i].arfcn+dataOfNetworkScanReport[i].uarfcn+'\')"><div class="showNeighbourClass">Show</div></button></td>'+
				  

				   	'</tr>';
				if(dataOfNetworkScanReport[i].count=='locator_count'){
					manualCells.push("opr_"+oprKey);
				}
					
			}
			$("#detail_tab_3 tbody").append(rows);
			resizeTable("detail_tab_3");
			
			console.log(manualCells);
			
			manualCells.forEach(colourManualCells);
			var id;
			function colourManualCells(value, index, array) {
				$('#detail_tab_3 > tbody > tr').each(function () {
					id = $(this).attr('id');
					if(id==value){
						$(this).css("background-color", colourOfManuallyAddedNode);
						return false;
					}
						
					});
			}
			
		},
		error:function(tx)
		{
			alert('Error in loading Network Scan Data');
		},
	});
}

var getNodeWiseReport_unused = function(){
	 $.ajax({
		url:dirPath+"Operations",
		async:false,
		type:'post',
		data:{methodName:"getNodeWiseReport"},
		success:function(data)
		{
				$("#detail_tab_2 .count_row_net").remove("");
				$("#detail_tab_2 tbody").html("");
				$("#bl_table tbody").html("");
				
				//@sunil 
				globalCdrCount = 0;
				
			dataOfNodeWiseReport=eval(data);
						var rows='';
						var rowsAlarm='';
						var inventoryData=getInventoryOnly();
			for(var i=0;i<dataOfNodeWiseReport.length;i++){
				globalCdr.push(dataOfNodeWiseReport[i].imsi+"_"+dataOfNodeWiseReport[i].profile_name+"_"+dataOfNodeWiseReport[i].trans_id);
				var dateTimeArray = dataOfNodeWiseReport[i].insert_time.split(" "); 
				var mapMsloc="";
				var triggerSourceCueId='';
				if(dataOfNodeWiseReport[i].show_cue_st=='t' || dataOfNodeWiseReport[i].show_cue_st=='true'){
					triggerSourceCueId='('+dataOfNodeWiseReport[i].cue_id+')';
				}
				if(dataOfNodeWiseReport[i].msloc.toLowerCase().indexOf("na")==-1){
					mapMsloc=dataOfNodeWiseReport[i].msloc;
				}
				
				var distancePerTech=0;
/*				if(mapMsloc==""){
					//console.log("show mobile on map according to ta");
					var techType=dataOfNodeWiseReport[i].stype;
					var ta=parseInt(dataOfNodeWiseReport[i].ta);

					if(techType=='2G'){
						distancePerTech = ta*550+550;
					}else{
						distancePerTech = ta*235+235;
					}
				}else{
					var mapMslocArr=mapMsloc.split(",");
					var absLat=mapMslocArr[0];
					var absLon=mapMslocArr[1];
					var sysLoc=$('#sys_location').text();
					var compSysLoc=sysLoc.split(",");
					distancePerTech=getDistanceFromLatLon(absLat,absLat,compSysLoc[0],compSysLoc[i],'K')*1000;
				}*/
			 rows+='<tr id="cdr_'+dataOfNodeWiseReport[i].imsi+'_'+dataOfNodeWiseReport[i].profile_name+'_'+dataOfNodeWiseReport[i].trans_id+'">'+
				   '<td>'+dateTimeArray[0]+'</td>'+
				   '<td>'+dateTimeArray[1]+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].trigger_source+triggerSourceCueId+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].profile_name+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].operator+'('+dataOfNodeWiseReport[i].country+')</td>'+
				   '<td>'+dataOfNodeWiseReport[i].imsi+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].imei+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].distance+'('+dataOfNodeWiseReport[i].calc_basis+')</td>';
				   if(dataOfNodeWiseReport[i].rxl==255){
					   rows+='<td>NA</td>';
				   }
				   else{
					   rows+='<td>'+dataOfNodeWiseReport[i].rxl+'</td>';
				   }
				   rows+='<td>'+dataOfNodeWiseReport[i].stype+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].band+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].freq+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].ulrfcn+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].dlarfcn+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].cgi+'</td>'+
				   '<td>'+dataOfNodeWiseReport[i].traget_type+'</td>'+
				   '</tr>';
				   globalCdrCount++;
				   if(dataOfNodeWiseReport[i].traget_type.toLowerCase()=='blacklist'){
					   globalAlarm.push(dataOfNodeWiseReport[i].imsi+"_"+dataOfNodeWiseReport[i].profile_name+"_"+dataOfNodeWiseReport[i].trans_id);
						var mapData={};
						mapData.imsi=dataOfNodeWiseReport[i].imsi;
						mapData.imei=dataOfNodeWiseReport[i].imei;
						//mapData.power=dataOfNodeWiseReport[i].rxl;
						mapData.target_name=dataOfNodeWiseReport[i].target_name;
						mapData.type=dataOfNodeWiseReport[i].traget_type;
						mapData.profile_name=dataOfNodeWiseReport[i].profile_name;
						mapData.tech=dataOfNodeWiseReport[i].stype;
						if (dataOfNodeWiseReport[i].rxl==255){
							mapData.power="NA";
						}
						else{
							mapData.power=dataOfNodeWiseReport[i].rxl;
						}
						var distance=dataOfNodeWiseReport[i].distance;
						if(mapMsloc==""){
							console.log("show mobile on map according to ta");
							var techType=dataOfNodeWiseReport[i].stype;
							var ta=parseInt(dataOfNodeWiseReport[i].ta);
							var techDistance=0;
							
/*							if(techType=='2G'){
								techDistance=550;
								distance = ta*550+550;
							}else{
								techDistance=235;
								distance = ta*235+235;
							}*/
							var deviceDataArr = inventoryData.DeviceInfo;
							var deviceData = deviceDataArr[0];
							var offset =parseInt(deviceData.OFFSET);
							var sectorAngle=getSectorAngle(offset,dataOfNodeWiseReport[i].profile_name);
							mapData.ta=distance;
							//var lineEndPoint = calulateLatLongAtGivenAngleAndDistance(deviceData.LATITUDE,deviceData.LONGITUDE,sectorAngle,distance);
							mapMsloc=dataOfNodeWiseReport[i].prob_msloc;
							var latLon = dataOfNodeWiseReport[i].prob_msloc.split(",");
							mapData.lat= latLon[0];
							mapData.lon= latLon[1];
							mapData.pos="no";
							addmobile(mapData,'../../resources/images/mob_org_red.png');
						}else{
							console.log("show mobile on map according to position");
							var mapMslocArr=mapMsloc.split(",");
							mapData.lat=mapMslocArr[0];
							mapData.lon=mapMslocArr[1];
							var sysLoc=$('#sys_location').text();
							var compSysLoc=sysLoc.split(",");
							mapData.ta=distance;
							mapData.pos="yes";
							addmobile(mapData,'../../resources/images/mob_org_red.png');
						}
						rowsAlarm+='<tr id="alarm_'+dataOfNodeWiseReport[i].imsi+'_'+dataOfNodeWiseReport[i].profile_name+'_'+dataOfNodeWiseReport[i].trans_id+'">'+
						'<td>'+dateTimeArray[0]+'</td>'+
						'<td>'+dateTimeArray[1]+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].imsi+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].imei+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].target_name+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].distance+'('+dataOfNodeWiseReport[i].calc_basis+')</td>'+
						'<td>'+mapMsloc+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].stype+'</td>'+
						'</tr>';
				   }
					
			}
			$("#detail_tab_2 tbody").append(rows);
			//resizeTable("detail_tab_2");
			$("#bl_table tbody").append(rowsAlarm);
			resizeTable("bl_table");
			resizeTable("detail_tab_2");
		},
		error:function(tx)
		{
			alert('Error in loading Node Wise Data');
		},
	});
}


var displayComingCdrData = function(cdrData){
		
		console.log("displaycomingcdrdata :"+cdrData);	
		var cdrKey=cdrData.imsi+"_"+cdrData.profile_name+"_"+cdrData.trans_id;
		var finalDataGrid=[];
		var dupCdrCount=0;
		var dupAlarmCount=0;
		var inventoryData=getInventoryOnly();
		for(var i=0;i<globalCdr.length;i++){
			if(cdrKey==globalCdr[i]){
				console.log("about to delete old cdr");
				$('#cdr_'+cdrKey).remove();
				dupCdrCount++;
				break;
			}
		}
		
		for(var k=0;k<globalAlarm.length;k++){
			if(cdrKey==globalAlarm[k]){
				console.log("about to delete old alarm");
				$('#alarm_'+cdrKey).remove();
				dupAlarmCount++;
				break;
			}
		}
		
		for(var j=0;j<mobileMarkers.length;j++){
			if(cdrKey==mobileMarkers[j].cdrKey){
				console.log("about to remode old marker");
				mobileMarkers[j].remove();
				mobileMarkers.splice(j,1);
			}
		}
		
		if(dupCdrCount==0){
			globalCdr.push(cdrKey);
		}
		
		if(dupAlarmCount==0){
			globalAlarm.push(cdrKey);
		}
		
		
		 var rows='';
		 var mapMsloc="";
				var dateTimeArray = cdrData.insert_time.split(" "); 
				
				if(cdrData.msloc.toLowerCase().indexOf("na")==-1){
					mapMsloc=cdrData.msloc;
				}
				
				//var distancePerTech=0;
/*				if(mapMsloc==""){
					//console.log("show mobile on map according to ta");
					var techType=cdrData.stype;
					var ta=parseInt(cdrData.ta);

					if(techType=='2G'){
						distancePerTech = ta*550+550;
					}else{
						distancePerTech = ta*235+235;
					}
				}else{
					var mapMslocArr=mapMsloc.split(",");
					var absLat=mapMslocArr[0];
					var absLon=mapMslocArr[1];
					var sysLoc=$('#sys_location').text();
					var compSysLoc=sysLoc.split(",");
					distancePerTech=getDistanceFromLatLon(absLat,absLat,compSysLoc[0],compSysLoc[i],'K')*1000;
				}*/
			var triggerSourceCueId='';
			if(cdrData.show_cue_st=='t' || cdrData.show_cue_st=='true'){
				triggerSourceCueId='('+cdrData.cueId+')';
			}	
			
			
			

			var curr_rxl;
			if(cdrData.rxl==255){
	 			curr_rxl='NA';
	 		}
	 		else{
	 			curr_rxl=cdrData.rxl;
	 		}
			
			finalDataGrid.push(
					{
					"date":dateTimeArray[0],
					"time":dateTimeArray[1],
					
					"trigger":cdrData.trigger_source+triggerSourceCueId,
					"antenna":cdrData.profile_name,
					"operator": cdrData.operator+'('+cdrData.country+')',
					"IMSI":cdrData.imsi,
					"IMEI":cdrData.imei,
					"distance":cdrData.distance+'('+cdrData.calc_basis+')',
					"rxL":curr_rxl,
					"tech":cdrData.stype,
					"band":cdrData.band,
					"ulfreq":cdrData.freq,
					"ularfcn":cdrData.ulrfcn,
					"dlarfcn":cdrData.dlarfcn,
					"cgi":cdrData.cgi,
					"target":cdrData.target_type,
			
			}
			);
			
			
			$("#grid-table").jqGrid("addRowData", 1, finalDataGrid, "first");
			
			
				
	if(globalCdrCount<1000){
		//$("#detail_tab_2 tbody").prepend(rows);
		//resizeTable("detail_tab_2");

		globalCdrCount++;
	}else{
//		$("#detail_tab_2 tbody").prepend(rows);
//		$("#detail_tab_2 tbody tr:last").remove();
//		resizeTable("detail_tab_2");
	}
	
	   if(cdrData.target_type.toLowerCase()=='blacklist'){
			var rowsAlarm='<tr id="alarm_'+cdrData.imsi+'_'+cdrData.profile_name+'_'+cdrData.trans_id+'">'+
				'<td>'+dateTimeArray[0]+'</td>'+
				'<td>'+dateTimeArray[1]+'</td>'+
				'<td>'+cdrData.imsi+'</td>'+
				'<td>'+cdrData.imei+'</td>'+
				'<td>'+cdrData.target_name+'</td>'+
				'<td>'+cdrData.distance+'('+cdrData.calc_basis+')</td>';
			if(cdrData.msloc.toLowerCase().indexOf("na")==-1){
				mapMsloc=cdrData.msloc;
			}
			var mapData={};
			mapData.imsi=cdrData.imsi;
			mapData.imei=cdrData.imei;
			//mapData.power=cdrData.rxl;
			mapData.target_name=cdrData.target_name;
			mapData.type=cdrData.target_type;
			mapData.tech=cdrData.stype;;
			mapData.profile_name=cdrData.profile_name;
			//var mapMsloc=cdrData.msloc;
			if(cdrData.rxl==255){
				mapData.power="NA";
			}
			else{
				mapData.power=cdrData.rxl;
			}
			var distance=cdrData.distance;
			if(mapMsloc==""){
				console.log("show mobile on map according to ta");
				var techType=cdrData.stype;
				var ta=parseInt(cdrData.ta);
				//var techDistance=0;
				
/*				if(techType=='2G'){
					techDistance=550;
					distance = ta*550+550;
				}else{
					techDistance=235;
					var distance = ta*235+235;
				}*/
				
				var deviceDataArr = inventoryData.DeviceInfo;
				var deviceData = deviceDataArr[0];
				var offset =parseInt(deviceData.OFFSET);
				var sectorAngle=getSectorAngle(offset,cdrData.profile_name);
				mapData.ta=distance;
				//var lineEndPoint = calulateLatLongAtGivenAngleAndDistance(deviceData.LATITUDE,deviceData.LONGITUDE,sectorAngle,distance);
				mapMsloc = cdrData.prob_msloc;
				var latLon = cdrData.prob_msloc.split(",");
				mapData.lat= latLon[0];
				mapData.lon= latLon[1];
				mapData.pos="no";
				addmobile(mapData,'../../resources/images/mob_org_red.png');
			}else{
				console.log("show mobile on map according to position");
				var mapMslocArr=mapMsloc.split(",");
				mapData.lat=mapMslocArr[0];
				mapData.lon=mapMslocArr[1];
				var sysLoc=$('#sys_location').text();
				var compSysLoc=sysLoc.split(",");
				mapData.ta=distance;
				mapData.pos="yes";
				addmobile(mapData,'../../resources/images/mob_org_red.png');
			}
			rowsAlarm+='<td>'+mapMsloc+'</td>'+
			   '<td>'+cdrData.stype+'</td>'+
			   '</tr>';
	   }
	   $("#bl_table tbody").prepend(rowsAlarm);		
		resizeTable("bl_table");

		//getNodeWiseReport();
		
}

/*var getFormattedNetworkScanData = function(dataOfNetworkScanReport){
dataOfNetworkScanReport.rssi=Math.trunc(parseFloat(dataOfNetworkScanReport.rssi)).toString();
dataOfNetworkScanReport.snr=Math.trunc(parseFloat(dataOfNetworkScanReport.snr)).toString();
var sysloc=dataOfNetworkScanReport.snr;
var latlon=sysloc.split(",");
var lat =(Math.round(parseFloat(latLon[0])*1000000.0)/1000000.0).toString();
var lon =(Math.round(parseFloat(latLon[1])*1000000.0)/1000000.0).toString();
dataOfNetworkScanReport.snr=lat+','+lon;
return dataOfNetworkScanReport;
}*/

var createNodeWiseReport = function()
{
		jQuery(document).ready(function() {
	        $("#nodeWiseReportGrid").jqGrid({
	                data : dataOfNodeWiseReport,
	                datatype : "local",
	                colNames : ['S-Type', 'IP','Count', 'IMSI', 'IMEI','TA','RxLev','Sys Loc','UL ARFCN','DL ARFCN','PSC','BAND','O/P Pwr','CGI','Generation Time'],
	                colModel : [ {
	                        name : 'stype',
	                        index : 'stype',
							width: 57
	                },{
	                        name : 'ip',
	                        index : 'ip'
							//,width: 'auto'
	                        ,hidden:true
	                },{
	                        name : 'count',
	                        index : 'count'
							//,width: 'auto'
	                        	,hidden:true
	                },{
							name : 'imsi',
							index : 'imsi'
							,width: 130
					},{
	                        name : 'imei',
	                        index :'imei'
							,width: 130
	                },{
                        name : 'ta',
                        index :'ta',
                        width:55
						//,width: 'auto'
                },{
                        name : 'rxl',
                        index :'rxl'
                        	,width:60
						//,width: 'auto'
                        //editable : true
                },{
	                        name : 'sysloc',
	                        index :'sysloc'
	                        ,width: 166
	                        ,hidden:true
	                },{
	                        name : 'ulrfcn',
	                        index :'ulrfcn'
	                        	,width:78
	                        //,width: 'auto'
	                },{
	                        name : 'dlarfcn',
	                        index :'dlarfcn'
	                        	,width:78
	                        //,width: 'auto'
	                },{
	                        name : 'psc',
	                        index :'psc'
	                        ,width: 60
	                },{
	                        name : 'band',
	                        index :'band'
	                        ,width: 85
	                },{
	                        name : 'outpow',
	                        index :'outpow'
	                        	,width:65
	                        //,width: 'auto'
	                }/*,{
	                        name : 'tstmp',
	                        index :'tstmp'
	                        ,width: 120
	                }*/,{
                        name : 'cgi',
                        index :'cgi'
						//,width: 'auto'
	                },{
	                        name : 'inserttime',
	                        index :'inserttime'
	                        ,width: 136
	                }],
	                pager : '#nodeWiseReportPager',
	                rowNum : 30,
					//width:'auto',
					height: 370,
	                rowList : [10, 20,30],
					loadonce: true,
	                viewrecords : true,
	                gridview : true,
	                caption : 'Node Wise Details',
					gridComplete: initGrid
	        });
	        jQuery("#nodeWiseReportGrid").jqGrid('navGrid', '#nodeWiseReportPager', {
	                edit : false,
	                add : false,
	                del : false,
	                search : true
	        });     
	});
}

var createCountryWiseReport = function()
{
		jQuery(document).ready(function() {
	        $("#countryWiseReportGrid").jqGrid({
	                data : dataOfCountryWiseReport,
	                datatype : "local",
	                colNames : ['S-Type', 'IP','Count', 'IMSI', 'IMEI', 'TA','RxLev','Sys Loc','UL ARFCN','DL ARFCN','PSC','BAND','O/P Pwr','CGI','Generation Time'],
					colModel : [ {	
	                        name : 'stype',
	                        index : 'stype',
							width : 57
	                },{
	                        name : 'ip',
	                        index : 'ip'
	                        	,hidden:true
	                },{
	                        name : 'count',
	                        index : 'count'
	                        	,hidden:true
	                },{
							name : 'imsi',
							index : 'imsi',
							width : 130
					},{
	                        name : 'imei',
	                        index :'imei',
							width : 130
	                },{
	                        name : 'ta',
	                        index :'ta',	
	                        width:55
	                },{
                        name : 'rxl',
                        index :'rxl',
                        width:60
	                },{
	                        name : 'sysloc',
	                        index :'sysloc'
	                        //editable : true
	                        	,hidden:true	
							,width:166
	                },{
	                        name : 'ulrfcn',
	                        index :'ulrfcn',
	                        editable : true
	                        ,width:78
	                },{
	                        name : 'dlarfcn',
	                        index :'dlarfcn',
	                        editable : true
	                        ,width:78
	                },{
                        name : 'psc',
                        index :'psc'
                        ,width:60//editable : true
	                },{
	                        name : 'band',
	                        index :'band'
	                        //editable : true
							,width:85
	                },{
	                        name : 'outpow',
	                        index :'outpow',
	                        editable : true
	                        ,width:65
	                }/*,{
	                        name : 'tstmp',
	                        index :'tstmp',
	                        editable : true,
							width: 120
	                }*/,{
                        name : 'cgi',
                        index :'cgi',
                        editable : true
	                },{
	                        name : 'inserttime',
	                        index :'inserttime'
	                        ,width:136//editable : true
	                }],
	                pager : '#countryWiseReportPager',
	                rowNum : 30,
					height: 370,
	                rowList : [10, 20,30],
					loadonce: true,
	                viewrecords : true,
	                gridview : true,
	                caption : 'Country Wise Details'
	        });
	        jQuery("#countryWiseReportGrid").jqGrid('navGrid', '#countryWiseReportPager', {
	                edit : false,
	                add : false,
	                del : false,
	                search : true
	        });     
	});
}

var createNetworkScanReport = function()
{
		jQuery(document).ready(function() {
	        $("#networkScanReportGrid").jqGrid({
	                data : dataOfNetworkScanReport,
	                datatype : "local",
	                colNames : ['IP','Count','S-Type','DL_FREQ','BAND','ARFCN','DL_UARFCN','DL_EARFCN','PSC','PCI','MCC','MNC','OPERATOR','LAC','CELL ID','NCC','BCC','RSSI','TA','RSCP','ECNO','RSRP','RSRQ','SNR','Sysloc','TimeStamp'],          				
					colModel : [ {
	                        name : 'ip',
	                        index : 'ip',
							hidden : true,
							width : 125
	                },{
	                        name : 'count',
	                        index : 'count',
							hidden : true
	                },{
	                        name : 'packet_type',
	                        index : 'packet_type',
							width : 70
							
	                },{
							name : 'freq',
							index : 'freq',
							width : 70
					},{
	                        name : 'band',
	                        index :'band',
	                        editable : true,
							width : 80
	                }
					,{
                        name : 'arfcn',
                        index :'arfcn',
                        editable : true,
							width : 75
					}
					,{
	                        name : 'uarfcn',
	                        index :'uarfcn',
	                        editable : true,
							width : 85
	                },{
	                        name : 'earfcn',
	                        index :'earfcn',
	                        editable : true,
							width : 85
	                },{
	                        name : 'psc',
	                        index :'psc',
	                        editable : true,
							width : 55
	                },{
	                        name : 'pci',
	                        index :'pci',
	                        editable : true,
							width : 55
	                },{
	                        name : 'mcc',
	                        index :'mcc',
	                        editable : true,
							width : 65
	                },{
	                        name : 'mnc',
	                        index :'mnc',
	                        editable : true,
							width : 65
	                },{
	                        name : 'operators',
	                        index :'operators',
	                        editable : true,
							width : 80
	                },{
	                        name : 'lac',
	                        index :'lac',
	                        editable : true,
							width : 80
	                },{
	                        name : 'cell',
	                        index :'cell',
	                        editable : true,
							width : 80
	                },{
	                        name : 'ncc',
	                        index :'ncc',
	                        editable : true,
							width : 65
	                },{
	                        name : 'bcc',
	                        index :'bcc',
							width:65
	                },{
	                        name : 'rssi',
	                        index :'rssi',
	                        width:65
	                },{
	                        name : 'ta',
	                        index :'ta',
							width:65
	                },{
	                        name : 'rscp',
	                        index :'rscp',
							width:92
	                },{
	                        name : 'ecno',
	                        index :'ecno',
							width:92
	                },{
	                        name : 'rsrp',
	                        index :'rsrp',
							width:92
	                },{
	                        name : 'rsrq',
	                        index :'rsrq',
							width:92
	                },{
	                        name : 'snr',
	                        index :'snr',
							width:65
	                },{
	                        name : 'sysloc',
	                        index :'sysloc',
							hidden : true,
							width:120
	                },{
	                        name : 'inserttime',
	                        index :'inserttime',
	                        width:136//editable : true
	                }/*,{
	                        name : 'tstmp',
	                        index :'tstmp',
	                        width:136//editable : true
	                }*/],
	                
	                pager : '#networkScanReportPager',
	                rowNum : 30,
					height: 370,
	                rowList : [10, 20,30],
					loadonce: true,
	                viewrecords : true,
	                gridview : true,
	                caption : 'Network Scan Details'
	        });
	        jQuery("#networkScanReportGrid").jqGrid('navGrid', '#networkScanReportPager', {
	                edit : false,
	                add : false,
	                del : false,
	                search : true
	        });     
	});
}


var nodeWiseExcelExport = function(){
var colArray=[];
if(dataOfNodeWiseReport.length <=0){alert("Please generate report first!!");return false;}
	var exportColumnName = getNodeWiseColumnNames();
	 for( var i in exportColumnName)
 {
		colData={};
	
		colData.headertext=i;
		colData.datatype="string";
		colData.datafield=exportColumnName[i];
		colData.width= "100px" ;
		colData.ishidden= "false"; 
		colArray.push(colData);	
 }
 
	var headerText ='';
	headerText='<br>Node Wise Data Report';
	headerText+='<br><br>Generated Time: ' + new Date();    
	var header = headerText.bold();		
	$("#nodeWiseExcelExportDiv").battatech_excelexport({
					containerid: "nodeWiseExcelExportDiv"
					, datatype: 'json'
					, dataset: dataOfNodeWiseReport
					, columns: colArray
					, reportName:header 
				});	
}

var countryWiseExcelExport = function(){
var colArray=[];
if(dataOfCountryWiseReport.length <=0){alert("Please generate report first!!");return false;}
	var exportColumnName = getNodeWiseColumnNames();
	 for( var i in exportColumnName)
 {
		colData={};
	
		colData.headertext=i;
		colData.datatype="string";
		colData.datafield=exportColumnName[i];
		colData.width= "100px" ;
		colData.ishidden= "false"; 
		colArray.push(colData);	
 }
 
	var headerText ='';
	headerText='<br>Country Wise Data Report';
	headerText+='<br><br>Generated Time: ' + new Date();    
	var header = headerText.bold();		
	$("#countryWiseExcelExportDiv").battatech_excelexport({
					containerid: "countryWiseExcelExportDiv"
					, datatype: 'json'
					, dataset: dataOfCountryWiseReport
					, columns: colArray
					, reportName:header 
				});	
}

var networkScanExcelExport = function(){
var colArray=[];
if(dataOfNetworkScanReport.length <=0){alert("Please generate report first!!");return false;}
	var exportColumnName = getNetworkScanColumnNames();
	 for( var i in exportColumnName)
 {
		colData={};
	
		colData.headertext=i;
		colData.datatype="string";
		colData.datafield=exportColumnName[i];
		colData.width= "100px" ;
		colData.ishidden= "false"; 
		colArray.push(colData);	
 }
 
	var headerText ='';
	headerText='<br>Network Scan Data Report';
	headerText+='<br><br>Generated Time: ' + new Date();    
	var header = headerText.bold();		
	$("#networkScanExcelExportDiv").battatech_excelexport({
					containerid: "networkScanExcelExportDiv"
					, datatype: 'json'
					, dataset: dataOfNetworkScanReport
					, columns: colArray
					, reportName:header 
				});	
}

var getNodeWiseColumnNames = function(){
var colNames={};
	colNames["S-Type"]="stype";
	colNames["IP"]="ip";
	colNames["Count"]="count";
	colNames["imsi"]="imsi";
	colNames["imei"]="imei";
	colNames["ta"]="ta";
	colNames["rxl"]="rxl";
	colNames["CGI"]="cgi";
	colNames["Sys Loc"]="sysloc";
	colNames["Band"]="band";
	colNames["UlArfcn"]="ulrfcn";
	colNames["DlArfcn"]="dlarfcn";
	colNames["OutPow"]="outpow";
	colNames["tstmp"]="tstmp";
	colNames["psc"]="psc";
	colNames["inserttime"]="inserttime";
	return colNames;
}

var getNetworkScanColumnNames = function(){
var colNames={};
	colNames["IP"]="ip";
	colNames["Count"]="count";
	colNames["Packet Type"]="packet_type";
	colNames["Freq"]="freq";
	colNames["Band"]="band";
	colNames["Arfcn"]="arfcn";
	colNames["MCC"]="mcc";
	colNames["MNC"]="mnc";
	colNames["Lac"]="lac";
	colNames["Cell"]="cell";
	colNames["NCC"]="ncc";
	colNames["BCC"]="bcc";
	colNames["RSSI"]="rssi";
	colNames["SNR"]="snr";
	colNames["TA"]="ta";
	colNames["Sysloc"]="sysloc";
	colNames["Tstmp"]="tstmp";
	colNames["Logtime"]="inserttime";
	return colNames;
}

function initGrid() {
            $(".jqgrow", "#nodeWiseReportGrid").contextMenu('contextMenu', {
                bindings: {
                    'edit': function (t) {
                        editRow();
                    },
                    'add': function (t) {
                        addRow();
                    },
                    'del': function (t) {
                        delRow();
                    }
                },
                onContextMenu: function (event, menu) {
                    var rowId = $(event.target).parent("tr").attr("id")
                    var grid = $("#nodeWiseReportGrid");
                    grid.setSelection(rowId);

                    return true;
                }
            });

            function addRow() {
                var grid = $("#nodeWiseReportGrid");
                grid.editGridRow("new", { closeAfterAdd: true});
            }

            function editRow() {
                var grid = $("#nodeWiseReportGrid");
                var rowKey = grid.getGridParam("selrow");
                if (rowKey) {
                    grid.editGridRow(rowKey, {closeAfterEdit: true});
                }
                else {
                    alert("No rows are selected");
                }
            }

            function delRow() {
                var grid = $("#nodeWiseReportGrid");
                var rowKey = grid.getGridParam("selrow");
                if (rowKey) {
                    grid.delGridRow(rowKey);
                }
                else {
                    alert("No rows are selected");
                }
            }
        }
		
		


var reloadNodeWiseReport = function(){
							$("#nodeWiseReportGrid").jqGrid('clearGridData');
        					$("#nodeWiseReportGrid").jqGrid('setGridParam', {
            			        datatype: 'local',
            			        data: dataOfNodeWiseReport
            			    });
            				$("#nodeWiseReportGrid").trigger("reloadGrid");
}

var reloadCountryWiseReport = function(){
							$("#countryWiseReportGrid").jqGrid('clearGridData');
        					$("#countryWiseReportGrid").jqGrid('setGridParam', {
            			        datatype: 'local',
            			        data: dataOfCountryWiseReport
            			    });
            				$("#countryWiseReportGrid").trigger("reloadGrid");
}

var reloadNetworkScanReport = function(oprName,nodeName){
	//'IP','Count','S-Type','FREQ','BAND','ARFCN','DL_UARFCN','DL_EARFCN','PSC','PCI','MCC','MNC','OPERATOR','LAC','CELL ID','NCC','BCC','RSCP','ECNO','RSRP','RSRQ','RSSI','SNR','TA','Sysloc','TimeStamp'
	$("#networkScanReportGrid").jqGrid('clearGridData');
    $("#networkScanReportGrid").jqGrid('setGridParam', {
        datatype: 'local',
       data: dataOfNetworkScanReport
    });
	$("#networkScanReportGrid").trigger("reloadGrid");
	if(nodeName==null){
		$('#networkScanReportModal .modal-dialog').css('width','1228px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["arfcn","ncc","bcc","snr","uarfcn","earfcn","psc","pci","rscp","ecno","rsrp","rsrq"]);
	}else if(nodeName.toLowerCase().indexOf("gsm")!=-1 || nodeName.toLowerCase().indexOf("loc_2g")!=-1){
	$('#networkScanReportModal .modal-dialog').css('width','1170px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["arfcn","ncc","bcc","snr"]);
		jQuery("#networkScanReportGrid").jqGrid('hideCol',["uarfcn","earfcn","psc","pci","rscp","ecno","rsrp","rsrq"]);
		//jQuery("#networkScanReportGrid").jqGrid('setLabel', 3, 'FREQ');
	}else if(nodeName.toLowerCase().indexOf("umts")!=-1 || nodeName.toLowerCase().indexOf("loc_3g")!=-1){
		$('#networkScanReportModal .modal-dialog').css('width','1255px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["uarfcn","psc","rscp","ecno","snr","snr"]);
		jQuery("#networkScanReportGrid").jqGrid('hideCol',["arfcn","earfcn","pci","ncc","bcc","rsrp","rsrq","snr"]);
		//jQuery("#networkScanReportGrid").jqGrid('setLabel', 3, 'DL_FREQ');
	}else if(nodeName.toLowerCase().indexOf("lte")!=-1){
		$('#networkScanReportModal .modal-dialog').css('width','1255px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["earfcn","pci","rsrp","rsrq"]);
		jQuery("#networkScanReportGrid").jqGrid('hideCol',["arfcn","uarfcn","psc","ncc","bcc","rscp","ecno","snr"]);
		//jQuery("#networkScanReportGrid").jqGrid('setLabel', 3, 'DL_FREQ');
	}else if(nodeName.toLowerCase().indexOf("umts")!=-1 || nodeName.toLowerCase().indexOf("loc_4g")!=-1){
		$('#networkScanReportModal .modal-dialog').css('width','1255px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["earfcn","pci","rsrp","rsrq"]);
		jQuery("#networkScanReportGrid").jqGrid('hideCol',["arfcn","uarfcn","psc","ncc","bcc","rscp","ecno","snr"]);
		//jQuery("#networkScanReportGrid").jqGrid('setLabel', 3, 'DL_FREQ');
	}
}

function drawPlotOnNewWindow(){
	if(operationEndTime == -1)
	{
		operationEndTime = toUtcTime(new Date());
	}
	
var url="maps_popup.jsp?startTime="+operationStartTIme+"&endTime="+operationEndTime+"&type="+globalType+"&value="+globalValue+"&filter="+globalFilter+"&bts="+globalBts+"&country="+globalCountry+"&oprname="+globalOprname+"";
var windowName="Plot Window";
openNewPlotWindow(url,windowName);
}

function openNewPlotWindow(url,windowName) 
{
       newwindow=window.open(url,windowName,'height=1000,width=1000');
       if (window.focus) {newwindow.focus()}
       return false;
}



var loadEvents = function()
{
	$.ajax({
		url:"../../service/common/getCurrentEvents",
		async:false,
		type:'post',
		success:function(data)
		{
		$('#operation_events_table_tbody').html("");	
		for(var i in data)
			{
				
				var msg  = JSON.parse(data[i].event_msg).msg;
				if(isNaN(msg)) {  // test if msg doesn't contain a number returns false if contains a number
					$('#operation_events_table_tbody').append('<tr><td class="logtimeTd">'+data[i].logtime1+'</td><td>'+msg+'</td></tr>');
				}
				else{
				//	$('#operation_events_table_tbody').append('<tr><td class="logtimeTd">'+data[i].logtime1+'</td><td>Aprrox Time Left:'+secondsToHms(msg)+'</td></tr>');
				}
				
			}
		}
	});
}

var getSectorAngle = function(offset,sector){
	sector = sector.toLowerCase();
	var angle=0;
	if(sector=='s1'){
		angle=60;
	}else if(sector=='s2'){
		angle=120;
	}else if(sector=='s3'){
		angle=180;
	}else if(sector=='ov1'){
		angle=0;
	}else{
		angle=0;
	}
	angle+=offset;
	
	angle-=30;
	return angle;
}

function getDistanceFromLatLon(lat1, lon1, lat2, lon2, unit) {
	if ((lat1 == lat2) && (lon1 == lon2)) {
		return 0;
	}
	else {
		var radlat1 = Math.PI * lat1/180;
		var radlat2 = Math.PI * lat2/180;
		var theta = lon1-lon2;
		var radtheta = Math.PI * theta/180;
		var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
		if (dist > 1) {
			dist = 1;
		}
		dist = Math.acos(dist);
		dist = dist * 180/Math.PI;
		dist = dist * 60 * 1.1515;
		if (unit=="K") { dist = dist * 1.609344 }
		if (unit=="N") { dist = dist * 0.8684 }
		return dist;
	}
}

var stopRunningCue = function()
{
	$.ajax({
		url:"../../service/common/stopcueoperation",
		async:false,
		type:'post',
		success:function(data)
		{
			if(data.result=="success"){
				alert("Cue stopped successfully");
			}else{
				alert("Problem in stopping cue");
			}
		}
	});
}

function secondsToHms(d) {
    d = Number(d);
    var h = Math.floor(d / 3600);
    var m = Math.floor(d % 3600 / 60);
    var s = Math.floor(d % 3600 % 60);

    var hDisplay = h > 0 ? h + (h == 1 ? " hour " : " hours, ") : "";
    var mDisplay = m > 0 ? m + (m == 1 ? " minute " : " minutes, ") : "";
    var sDisplay =(( s > 0)||(s == 0))  ? s + (s == 1 ? " second" : " seconds") : "";
    return hDisplay + mDisplay + sDisplay; 
}





















var freqStart1;
var freqEnd1;

var freqStart2;
var freqEnd2;

var freqStart3;
var freqEnd3;

var freqStart4;
var freqEnd4;

var txPower1;
var data1= [];
var labels1=[];
	
function makeGraph() {
	$.ajax({
		url:"../../service/common/makeGraph",
		data:{"temp":1},
		type:'post',
		async: "false",
		success:function(data)
		{
			var mapData;
			
			
			
			
			for(var k=0;k<data.length;k++)
			{
				
				mapData=JSON.parse(data[k].config);
				freqStart1=mapData[0].freqStart/1000;
				freqEnd1=mapData[0].freqEnd/1000;;
				
				freqStart2=mapData[1].freqStart/1000;
				freqEnd2=mapData[1].freqEnd/1000;
				
				freqStart3=mapData[2].freqStart/1000;
				freqEnd3=mapData[2].freqEnd/1000;
				
				freqStart4=mapData[3].freqStart/1000;
				freqEnd4=mapData[3].freqEnd/1000;
			
				labels1.push(0);
				labels1.push(freqStart1);
				labels1.push(freqEnd1);
				labels1.push(freqStart2);
				labels1.push(freqStart3);
				labels1.push(freqStart4);
				labels1.push(freqEnd2);
				labels1.push(freqEnd3);
				labels1.push(freqEnd4);
				labels1.push(60);
				
				
			}
			
			
			
			
			for(var k=0;k<data.length;k++)
			{
				mapData=JSON.parse(data[k].config);
				freqStart1=mapData[0].freqStart/1000;
				freqEnd1=mapData[0].freqEnd/1000;;
				
				freqStart2=mapData[1].freqStart/1000;
				freqEnd2=mapData[1].freqEnd/1000;
				
				freqStart3=mapData[2].freqStart/1000;
				freqEnd3=mapData[2].freqEnd/1000;
				
				freqStart4=mapData[3].freqStart/1000;
				freqEnd4=mapData[3].freqEnd/1000;
				
				mode1=mapData[0].mode;
				mode2=mapData[1].mode;
				mode3=mapData[2].mode;
				mode4=mapData[3].mode;
				
				txPower1=mapData[4].txPower;
				callLater(k+1);
			}
			CallFinalGraphMake( labels1,data1);
			//	mapData
//			$('#operationOptions').empty();
//			var options='<option value="-1">All</option>';
//			for(var i=0;i<allOperations.length;i++){
//				options+='<option value="'+allOperations[i].id+'">'+allOperations[i].name+'</option>';
//			}
//			$('#operatons_list').append(options);
		}
	});

	
}



makeGraph() ;














function callLater(JammerNo) {
//	freqStart1=26;
//	freqEnd1=28;
	var color1=getRandomColor();
	
	
//	labels1.push(0);
//	labels1.push(freqStart1);
//	labels1.push(freqEnd1);
//	labels1.push(freqStart2);
//	labels1.push(freqStart3);
//	labels1.push(freqStart4);
//	labels1.push(freqEnd2);
//	labels1.push(freqEnd3);
//	labels1.push(freqEnd4);
//	labels1.push(60);
	
	labels1 = [...new Set(labels1)];
	
	labels1.sort();
	var dataReq_1= [];
	var dataReq_2= [];
	var dataReq_3= [];
	var dataReq_4= [];
	var startPos=labels1.indexOf(freqStart1);
	var stopPos=labels1.indexOf(freqEnd1);
	for(var k=0;k< labels1.length; k++)
	{
		if(k>=startPos && k <=stopPos)
		{
			dataReq_1.push(txPower1);
		}
		else
		{
			dataReq_1.push(undefined);
		}
	}
	
	
	
	
	
	startPos=labels1.indexOf(freqStart2);
	stopPos=labels1.indexOf(freqEnd2);
	for(var k=0;k< labels1.length; k++)
	{
		if(k>=startPos && k <=stopPos)
		{
			dataReq_2.push(txPower1);
		}
		else
		{
			dataReq_2.push(undefined);
		}
	}
	
	
	
	startPos=labels1.indexOf(freqStart3);
	stopPos=labels1.indexOf(freqEnd3);
	for(var k=0;k< labels1.length; k++)
	{
		if(k>=startPos && k <=stopPos)
		{
			dataReq_3.push(txPower1);
		}
		else
		{
			dataReq_3.push(undefined);
		}
	}
	
	
	
	startPos=labels1.indexOf(freqStart4);
	stopPos=labels1.indexOf(freqEnd4);
	for(var k=0;k< labels1.length; k++)
	{
		if(k>=startPos && k <=stopPos)
		{
			dataReq_4.push(txPower1);
		}
		else
		{
			dataReq_4.push(undefined);
		}
	}
	
	
	
	//dataReq_1= [undefined,txPower1,txPower1,undefined,undefined,];
	var tempData1=
		{
	        label: 'Jammer ' +JammerNo+'a',
	        data: dataReq_1,
	        backgroundColor: [
	            'transparent',
	        ],
	        borderColor: [
	        	color1,
	        ],
	        borderWidth: 2
	    }
	;
	var tempData2=
		{
	        label: 'Jammer ' +JammerNo+'b',
	        data: dataReq_2,
	        backgroundColor: [
	        	'transparent',
	        ],
	        borderColor: [
	        	color1,
	        ],
	        borderWidth: 2
		}
	;
	var tempData3=		{
	        label: 'Jammer ' +JammerNo+'c',
	        data: dataReq_3,
	        backgroundColor: [
	        	'transparent',
	        ],
	        borderColor: [
	        	color1,
	        ],
	        borderWidth: 2
			}
		;
	var tempData4=
		{
	        label: 'Jammer ' +JammerNo+'d',
	        data: dataReq_4,
	        backgroundColor: [
	        	'transparent',
	        ],
	        borderColor: [
	        	color1,
	        ],
	        borderWidth: 2
	    }	;
	
	
	
	
	if(mode1.localeCompare("1")==0)
	{
		data1.push(tempData1);
	}
	if(mode2.localeCompare("1")==0)
	{
		data1.push(tempData2);
	}
	if(mode3.localeCompare("1")==0)
	{
		data1.push(tempData3);
	}
	if(mode4.localeCompare("1")==0)
	{
		data1.push(tempData4);
	}
    	
		
     
    
    
    
    
    
   
    
    
    
}
function CallFinalGraphMake(labels1,data1) {
	
		
		var ctx = document.getElementById('myChart').getContext('2d');
		var myChart = new Chart(ctx, {
		    type: 'line',
		    data: {
		        //labels: [0, 10000, 20000, 30000, 40000, 50000, 60000 ],
		        labels: labels1,
		        datasets:data1,
		    },
		    options: {
		        scales: {
		            y: {
		                beginAtZero: false
		            }
		        },
		        parsing: {
		            xAxisKey: 'id',
		            yAxisKey: 'asd.value'
		        },
		        //Min: 0,
		       // Max: 60000,
		
		    }
		});

}


	


function getRandomColor() {
	  var letters = '0123456789ABCDEF';
	  var color = '#';
	  for (var i = 0; i < 6; i++) {
	    color += letters[Math.floor(Math.random() * 16)];
	  }
	  return color;
	}



var getNodeWiseReport = function(){
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";              


var finalDataGrid=[];

 $.ajax({
		url:dirPath+"Operations",
		
		async:false,
		type:'post',
		data:{methodName:"getNodeWiseReport"},
		success:function(data)
		{
				$("#bl_table tbody").html("");
				
			
			globalCdrCount = 0;
			
			dataOfNodeWiseReport=eval(data);
			var rows='';
			var rowsAlarm='';
			var inventoryData=getInventoryOnly();
			for(var i=0;i<dataOfNodeWiseReport.length;i++){
			globalCdr.push(dataOfNodeWiseReport[i].imsi+"_"+dataOfNodeWiseReport[i].profile_name+"_"+dataOfNodeWiseReport[i].trans_id);
			var dateTimeArray = dataOfNodeWiseReport[i].insert_time.split(" "); 
			var mapMsloc="";
			var triggerSourceCueId='';
			if(dataOfNodeWiseReport[i].show_cue_st=='t' || dataOfNodeWiseReport[i].show_cue_st=='true'){
				triggerSourceCueId='('+dataOfNodeWiseReport[i].cue_id+')';
			}
			if(dataOfNodeWiseReport[i].msloc.toLowerCase().indexOf("na")==-1){
				mapMsloc=dataOfNodeWiseReport[i].msloc;
			}
			
			var distancePerTech=0;


			var curr_rxl;
			if(dataOfNodeWiseReport[i].rxl==255)
			{
					curr_rxl="NA";
			}
			else{
				curr_rxl=dataOfNodeWiseReport[i].rxl;
			}

			finalDataGrid.push(
					{
					"date":dateTimeArray[0],
					"time":dateTimeArray[1],
					"trigger":dataOfNodeWiseReport[i].trigger_source+triggerSourceCueId,
					"antenna":dataOfNodeWiseReport[i].profile_name,
					"operator":dataOfNodeWiseReport[i].operator+'('+dataOfNodeWiseReport[i].country+')',
					"IMSI":dataOfNodeWiseReport[i].imsi,
					"IMEI":dataOfNodeWiseReport[i].imei,
					"distance":dataOfNodeWiseReport[i].distance+'('+dataOfNodeWiseReport[i].calc_basis+')',
					"rxL":curr_rxl,
					"tech":dataOfNodeWiseReport[i].stype,
					"band":dataOfNodeWiseReport[i].band,
					"ulfreq":dataOfNodeWiseReport[i].freq,
					"ularfcn":dataOfNodeWiseReport[i].ulrfcn,
					"dlarfcn":dataOfNodeWiseReport[i].dlarfcn,
					"cgi":dataOfNodeWiseReport[i].cgi,
					"target":dataOfNodeWiseReport[i].traget_type,
			
			}
			);

				
			   
				   globalCdrCount++;
				   if(dataOfNodeWiseReport[i].traget_type.toLowerCase()=='blacklist'){
					   globalAlarm.push(dataOfNodeWiseReport[i].imsi+"_"+dataOfNodeWiseReport[i].profile_name+"_"+dataOfNodeWiseReport[i].trans_id);
						var mapData={};
						mapData.imsi=dataOfNodeWiseReport[i].imsi;
						mapData.imei=dataOfNodeWiseReport[i].imei;
						//mapData.power=dataOfNodeWiseReport[i].rxl;
						mapData.target_name=dataOfNodeWiseReport[i].target_name;
						mapData.type=dataOfNodeWiseReport[i].traget_type;
						mapData.profile_name=dataOfNodeWiseReport[i].profile_name;
						mapData.tech=dataOfNodeWiseReport[i].stype;
						if (dataOfNodeWiseReport[i].rxl==255){
							mapData.power="NA";
						}
						else{
							mapData.power=dataOfNodeWiseReport[i].rxl;
						}
						var distance=dataOfNodeWiseReport[i].distance;
						if(mapMsloc==""){
							console.log("show mobile on map according to ta");
							var techType=dataOfNodeWiseReport[i].stype;
							var ta=parseInt(dataOfNodeWiseReport[i].ta);
							var techDistance=0;
						
							var deviceDataArr = inventoryData.DeviceInfo;
							var deviceData = deviceDataArr[0];
							var offset =parseInt(deviceData.OFFSET);
							var sectorAngle=getSectorAngle(offset,dataOfNodeWiseReport[i].profile_name);
							mapData.ta=distance;
							
							mapMsloc=dataOfNodeWiseReport[i].prob_msloc;
							var latLon = dataOfNodeWiseReport[i].prob_msloc.split(",");
							mapData.lat= latLon[0];
							mapData.lon= latLon[1];
							mapData.pos="no";
							addmobile(mapData,'../../resources/images/mob_org_red.png');
						}else{
							console.log("show mobile on map according to position");
							var mapMslocArr=mapMsloc.split(",");
							mapData.lat=mapMslocArr[0];
							mapData.lon=mapMslocArr[1];
							var sysLoc=$('#sys_location').text();
							var compSysLoc=sysLoc.split(",");
							mapData.ta=distance;
							mapData.pos="yes";
							addmobile(mapData,'../../resources/images/mob_org_red.png');
						}
						rowsAlarm+='<tr id="alarm_'+dataOfNodeWiseReport[i].imsi+'_'+dataOfNodeWiseReport[i].profile_name+'_'+dataOfNodeWiseReport[i].trans_id+'">'+
						'<td>'+dateTimeArray[0]+'</td>'+
						'<td>'+dateTimeArray[1]+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].imsi+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].imei+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].target_name+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].distance+'('+dataOfNodeWiseReport[i].calc_basis+')</td>'+
						'<td>'+mapMsloc+'</td>'+
						'<td>'+dataOfNodeWiseReport[i].stype+'</td>'+
						'</tr>';
						
						
						
				   }

					
			}


			$("#bl_table tbody").append(rowsAlarm);
			resizeTable("bl_table");
			
		},
		error:function(tx)
		{
			alert('Error in loading Node Wise Data');
		},
	});

	jQuery("#grid-table").jqGrid({
		
		datatype: "json",
		
		data: finalDataGrid,
	    datatype: "local",
	    type: 'POST',
		height: 200,
		colNames: ['Date', 'Time','Trigger','Antenna','Operator','IMSI','IMEI','Distance(m)','RxL','Tech','Band','UL Freq','UL ARFCN','DL ARFCN','CGI','Target'],
		colModel:[
		{name:'date',index:'date', width:95},
		{name:'time',index:'time', width:95},
		{name:'trigger',index:'trigger', width:80},
		{name:'antenna',index:'antenna', width:80} ,
		{name:'operator',index:'operator', width:80} ,
		{name:'IMSI',index:'IMSI', width:120} ,
		{name:'IMEI',index:'IMEI', width:120} ,
		{name:'distance',index:'distance', width:80} ,
		{name:'rxL',index:'rxL', width:80} ,
		{name:'tech',index:'tech', width:80} ,
		{name:'band',index:'band', width:80} ,
		{name:'ulfreq',index:'ulfreq', width:80} ,
		{name:'ularfcn',index:'ularfcn', width:80} ,
		{name:'dlarfcn',index:'dlarfcn', width:80} ,
		{name:'cgi',index:'cgi', width:130} ,
		{name:'target',index:'target', width:80} ,
		
		
		
		],
		
		loadonce: true,
		rowNum: 35,
		//rowList:[10,20,30],
		pager: '#grid-pager',
		altRows: true,                                                 
		multiselect: false,
		multiboxonly: false,
		shrinkToFit:false,
		caption: "Captured Mobile Information",
		loadComplete : function() {
				var table = this;
				setTimeout(function(){
							updatePagerIcons(table);
				}, 0);
		}
	});
		
	jQuery("#grid-table").jqGrid('navGrid','#grid-pager',
		{edit:false,add:false,del:false,   search: true,},
		{},
		{}, 
		{},
		{multipleSearch:true, multipleGroup:true}
		);

	 $('#grid-table').jqGrid('setGridWidth', '680');

};


