<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
<script>
var globalModalStatus=false;
var globalCdrData=[];
var globalComingCellsModalStatus=false;
var globalComingCellsData=[];
function toUtcTime(dateTime) {
	   // var d = new Date("2017-05-15 14:39");
		 var d = new Date(dateTime);
	    var n = d.toUTCString().replace("GMT","");    
	    var dd = new Date(n);
	    var year = dd.getFullYear();
	   //alert(dd.getHours());
	    var month = ((dd.getMonth()+1).toString().length<=1)?"0"+(parseInt(dd.getMonth())+1):(parseInt(dd.getMonth())+1);
	    var date = (dd.getDate().toString().length<=1)?"0"+(parseInt(dd.getDate())):(parseInt(dd.getDate()));
	    var hour = (dd.getHours().toString().length<=1)?"0"+(parseInt(dd.getHours())):(parseInt(dd.getHours()));
	    var min = (dd.getMinutes().toString().length<=1)?"0"+(parseInt(dd.getMinutes())):(parseInt(dd.getMinutes()));
	    //console.log(dd.getDate());
	    return (year+"-"+month+"-"+date+" "+hour+":"+min);
	    
	}
<% String ip= "'"+request.getServerName()+"'"; %>
var accuracyGPS = 50;
var getGpsAcc  = function()
{
		//var val = $("#gps_acc").val();
		$.ajax
		({
			url:"../../service/common/getgpsaccuracy",
			//data:{"accuracy":val},
			async:true,
			type:"post",
			dataType:"json",
			success:function(data)
			{
				accuracyGPS = parseInt(data[0].accuracy);
			}
		});
}
getGpsAcc()

var ipAddress =<%= ip %>;
var operationStartTIme = null;
var operationEndTime = -1;
var realoperationStartTIme = null;
var path = [];
var counterForLoading = 0;
console.log(ipAddress);

/***********************Event Capture Section**********************************/
var ws = null;
var ws1 = null;
var ws2 = null;
var captureDeviceStatusEvent = function()
{
	ws4 = new WebSocket("ws://"+ipAddress+":8080/locator/devicestatus");
	var color="#FFF";
	ws4.onopen = function()
	{
		console.log("connected to the device status server");
	}
	ws4.onmessage = function(event)
	{
		var msg = event.data;
		console.log(msg);
		getNodesInfo();
	}
	ws4.onclose = function()
	{
		/*var r = confirm("Disconected From Server.Press ok to reconect");
		if (r == true) {
		    location.reload();
		} else {
		    
		}*/
	}
}

var captureAutoOprEvent = function()
{
	ws4 = new WebSocket("ws://"+ipAddress+":8080/locator/autopr");
	var color="#FFF";
	ws4.onopen = function()
	{
		console.log("connected to the device status server");
	}
	ws4.onmessage = function(event)
	{
		/*var msg = event.data;
		console.log(msg);
		jsonMsg=JSON.parse(msg);
		if(jsonMsg.result=="success"){
			$('#operation_events_table_tbody').append('<tr><td>'+jsonMsg.msg+'</td></tr>');
		}else{
			$('#operation_events_table_tbody').append('<tr><td>'+jsonMsg.msg+'</td></tr>');
		}
		
		if(jsonMsg.msg=='started'){
			window.location.href='maps.jsp';
		}
		//getNodesInfo();*/
		loadEvents();
		
		
	}
	ws4.onclose = function()
	{
		/*var r = confirm("Disconected From Server.Press ok to reconect");
		if (r == true) {
		    location.reload();
		} else {
		    
		}*/
	}
}

var captureCdrEvent = function()
{
	ws = new WebSocket("ws://"+ipAddress+":8080/locator/cdr");
	var color="#FFF";
	ws.onopen = function()
	{
		console.log("connected to the server");
	}
	ws.onmessage = function(event)
	{
		var msg = event.data;
		console.log(msg);
		getData();
	}
	ws.onclose = function()
	{
		/*var r = confirm("Disconected From Server.Press ok to reconect");
		if (r == true) {
		    location.reload();
		} else {
		    
		}*/
	}
}

var firstvar = null;
var lastEventPt = [];
var captureGPSEvent = function()
{
	ws1 = new WebSocket("ws://"+ipAddress+":8080/locator/gps");
	
	ws1.onopen = function()
	{
		console.log("connected to the GPS server");
	}
	ws1.onmessage = function(event)
	{
		var msg = event.data;
		console.log(msg);
		msg = eval(msg);
		
		if(!isNaN(msg[4]) && !isNaN(msg[6]) && parseInt(msg[4])>0 && parseInt(msg[6])>0)
		{
			
			if(lastEventPt.length>1)
			{
				if(distanceBetweenLatLon(parseFloat(lastEventPt[lastEventPt.length-1][0]),parseFloat(lastEventPt[lastEventPt.length-1][1]),parseFloat(msg[4]),parseFloat(msg[6])) > accuracyGPS)
				{
					lastEventPt.push([msg[4],msg[6]]);
					drawLine([lastEventPt[lastEventPt.length-2],[msg[4],msg[6]]],map);
					addTower([msg[4],msg[6]],'../../resources/images/tower.png');	
				}
			}else
			{
				lastEventPt.push([msg[4],msg[6]]);
				addTower([msg[4],msg[6]],'../../resources/images/tower.png');
			}			
		}
		
		//drawLine([path[path.length-1],[msg[4],msg[6]]],map);
		//path.push([data[4],data[6]]);
		//addTower(path[path.lenght-1],'../../resources/images/tower.png');
	}
	ws1.onclose = function()
	{
		               
	}
}

var captureImsiEvent = function()
{
	ws2 = new WebSocket("ws://"+ipAddress+":8080/locator/imsitracker");
	
	ws2.onopen = function()
	{
		console.log("connected to the imsi tracking serve");
	}
	ws2.onmessage = function(event)
	{
		var msg = event.data;
		getImsiData();
	}
	ws2.onclose = function()
	{
		
	}
}


var captureNetworkEvent = function()
{
	ws3 = new WebSocket("ws://"+ipAddress+":8080/locator/networkdata");
	
	ws3.onopen = function()
	{
		console.log("connected to the network tracking tracking server");
	}
	ws3.onmessage = function(event)
	{
		var msg = event.data;
		getNetworkRelatedData();
	}
	ws3.onclose = function()
	{
		
	}
}

/***********************Event Capture Section End**********************************/

/***********************Data Call section******************************************/
var getData = function()
{
	getNodeWiseDataStats();
	getCountryWiseDataStats();
	getcurrentcdrdata();
}

var getNetworkRelatedData = function()
{
	getNetworkData();
	getCurrentComingNetworkData();
}

var getNodeWiseDataStats = function()
{
	postData={"startTime":operationStartTIme};
	$.ajax({
		url:"../../service/2g/nodewisedatastats",
		data:postData,
		type:'post',
		success:function(data)
		{
			console.log(data);
			$("#detail_tab_1 .node_row").remove("");
			$("#detail_tab_1 tbody").append(data);
			//$("#detail_tab_1 tbody").html(data);
			
			checkCheckCounter();
			
		}
	});	
}

var getcurrentcdrdata = function()
{
	var ueDisplayListSize=$('#ueDisplayListSelect').val();
	var searchKey=$('#ueSearchText').val();
	
	$.ajax({
		url:"../../service/common/getcurrentcdrdata",
		data:{"size":ueDisplayListSize,"searchKey":searchKey},
		type:'get',
		success:function(data)
		{
			console.log(data);
			globalCdrData=data;	
			if(globalModalStatus){
				displayDetectedMobiles(data);
			}
		}
	});	
}

var getCurrentComingNetworkData = function()
{
	
	$.ajax({
		url:"../../service/common/getcurrentcomingnetworkdata",
		data:postData,
		success:function(data)
		{
			console.log(data);
			globalComingCellsData=data;	
			if(globalComingCellsModalStatus)
			{
				displayComingCells(data);
			}
		}
	});	
}

var getdetailsOfModbile  =  function(element,mcc,mnc,realrxl,stype,imsi,imei,ta,time,name,o1,c1,o2,c2)
{
	var taOrPd='PD';
	if(stype.toLowerCase()=='2g' || stype.toLowerCase()=='gsm'){
		taOrPd='TA';
	}
	$("#mobileDataDetails .modal-body").html("");
	$("#mobileDataDetails .modal-body").html("<table class='table table-default report_tables' style='margin:0px;'>"+
	"<tbody>"+((name !="")?"<tr><td>Name</td><td>"+name+"</td></tr>":"")+	
	"<tr><td>IMSI</td><td>"+imsi+"</td></tr><tr><td>IMEI</td><td>"+imei+"</td></tr><tr><td>RxL</td><td>"+realrxl+"</td></tr><tr><td>"+taOrPd+"</td><td>"+ta+"</td></tr><tr><td>Tech</td><td>"+stype+"</td></tr>"+
		"<tr><td>Home Network</td><td>"+o1+"-"+c1+"</td></tr>"+
	"<!--<tr><td>Operator</td><td>"+o1+"</td></tr>-->"+
		"<tr><td>Current Network</td><td>"+o2+" :"+mcc+mnc+"</td></tr>"+
		"<!--<tr><td>Current Operator</td><td>"+o2+"</td></tr>-->"+
		"<tr><td colspan=2 style='text-align:center;'>"+time+"</td></tr>"+
	"</table></tbody>");
	$('#dataDetailsHeader').html("Mobile Details");
	
	$(element).addClass("selected_mobile");
	
	$("#mobileDataDetails").modal("show");
}

var getdetailsOfCell  =  function(element,operators,mcc,mnc,lacOrTac,cell,tech,band,freqChannelNum,freq,scramblingCode,rssi,snr,timestamp,packetId,indexId)
{
var neighbourDataOfCell={};
tech=tech.toLowerCase();
	$.ajax({
		url:"../../service/netscan/neighbours",
		data:{index:indexId,id:packetId},
		type:'post',
		success:function(data)
		{
		$("#mobileDataDetails .modal-body").html("");		
		var freqChannelType='';
		var scramblingType='';
		var lacOrTacTr="<tr><td>LAC</td><td>"+lacOrTac+"</td></tr>";
		var tr='';
		if(tech=='gsm'){
		neighbourDataOfCell=jQuery.parseJSON(data[0].neigh);
		freqChannelType='ARFCN';
		scramblingType='BSIC';
		var intraFreqNeigh=neighbourDataOfCell.INTRA_FREQ_NEIGH;
		var interRatWcdmaNeigh=neighbourDataOfCell.INTER_RAT_WCDMA_NEIGH;
		var interRatLteNeigh=neighbourDataOfCell.INTER_RAT_LTE_NEIGH;
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTRA_FREQ_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<intraFreqNeigh.length;i++){
		tr+="<div>ARFCN:"+intraFreqNeigh[i].ARFCN+"</div>";
		}
		tr+="</div></div></div></td></tr>";
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTER_RAT_WCDMA_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<interRatWcdmaNeigh.length;i++){
		tr+="<div>UARFCN:"+interRatWcdmaNeigh[i].UARFCN+",PSC:"+interRatWcdmaNeigh[i].PSC+"</div>";
		}
		tr+="</div></div></div></td></tr>";
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTER_RAT_LTE_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<interRatLteNeigh.length;i++){
		tr+="<div>EARFCN:"+interRatLteNeigh[i].EARFCN+",PCI:"+interRatLteNeigh[i].PCI+"</div>";
		}
		tr+="</div></div></div></td></tr>";
	}else if(tech=='umts'){
		neighbourDataOfCell=jQuery.parseJSON(data[0].neigh);
	    freqChannelType='UARFCN';
		scramblingType='PSC';
		var intraFreqNeigh=neighbourDataOfCell.INTRA_FREQ_NEIGH;
		var interFreqNeigh=neighbourDataOfCell.INTER_FREQ_NEIGH;
		var interRatGsmNeigh=neighbourDataOfCell.INTER_RAT_GSM_NEIGH;
		var interRatLteNeigh=neighbourDataOfCell.INTER_RAT_LTE_NEIGH;
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTRA_FREQ_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<intraFreqNeigh.length;i++){
		tr+="<div>PSC:"+intraFreqNeigh[i].PSC+"</div>";
		}
		tr+="</div></div></div></td></tr>";
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTER_FREQ_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<interFreqNeigh.length;i++){
		tr+="<div>UARFCN:"+interFreqNeigh[i].UARFCN+",PSC:"+interFreqNeigh[i].PSC+"</div>";
		}
		tr+="</div></div></div></td></tr>";
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTER_RAT_GSM_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<interRatGsmNeigh.length;i++){
		tr+="<div>ARFCN:"+interRatGsmNeigh[i].ARFCN+",BCC:"+interRatGsmNeigh[i].BCC+",NCC:"+interRatGsmNeigh[i].NCC+"</div>";
		}
		tr+="</div></div></div></td></tr>";
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTER_RAT_LTE_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<interRatLteNeigh.length;i++){
		tr+="<div>EARFCN:"+interRatLteNeigh[i].EARFCN+"</div>";
		}
		tr+="</div></div></div></td></tr>";
	}else if(tech=='lte'){
		neighbourDataOfCell=jQuery.parseJSON(data[0].neigh);
		freqChannelType='EARFCN';
		scramblingType='PCI';
		lacOrTacTr="<tr><td>TAC</td><td>"+lacOrTac+"</td></tr>";
		var intraFreqNeigh=neighbourDataOfCell.INTRA_FREQ_NEIGH;
		var interFreqNeigh=neighbourDataOfCell.INTER_FREQ_NEIGH;
		var interRatGsmNeigh=neighbourDataOfCell.INTER_RAT_GSM_NEIGH;
		var interRatWcdmaNeigh=neighbourDataOfCell.INTER_RAT_WCDMA_NEIGH;
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTRA_FREQ_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<intraFreqNeigh.length;i++){
		tr+="<div>PCI:"+intraFreqNeigh[i].PCI+"</div>";
		}
		tr+="</div></div></div></td></tr>";
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTER_FREQ_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<interFreqNeigh.length;i++){
		tr+="<div>EARFCN:"+interFreqNeigh[i].EARFCN+",PCI:"+interFreqNeigh[i].PCI+"</div>";
		}
		tr+="</div></div></div></td></tr>";
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTER_RAT_GSM_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<interRatGsmNeigh.length;i++){
		tr+="<div>ARFCN:"+interRatGsmNeigh[i].ARFCN+"</div>";
		}
		tr+="</div></div></div></td></tr>";
		
		tr+="<tr><td colspan=\"2\"><div class=\"col c1\">";
		tr+="<div id=\"default-example\" data-collapse>";
		tr+="<h3 class=\"expand\">INTER_RAT_WCDMA_NEIGH</h3>";
		tr+="<div>";
		for(var i=0;i<interRatWcdmaNeigh.length;i++){
		tr+="<div>UARFCN:"+interRatWcdmaNeigh[i].UARFCN+"</div>";
		}
		tr+="</div></div></div></td></tr>";
	}else if(tech=='loc_2g'){
		freqChannelType='ARFCN';
		scramblingType='BSIC';
	}else{
		freqChannelType='UARFCN';
		scramblingType='PSC';
	}
	
	$("#mobileDataDetails .modal-body").html("<table class='table table-default report_tables' style='margin:0px;'>"+
	"<tbody><tr><td>Operator</td><td>"+operators+"("+mcc+"-"+mnc+")</td></tr>"+lacOrTacTr+
	"<tr><td>CELL_ID</td><td>"+cell+"</td></tr>"+
	"<tr><td>BAND</td><td>"+band+"</td></tr>"+
	"<tr><td>"+freqChannelType+"</td><td>"+freqChannelNum+"</td></tr>"+
	"<tr><td>Frequency</td><td>"+freq+"</td></tr>"+
	"<tr><td>"+scramblingType+"</td><td>"+scramblingCode+"</td></tr>"+
	"<tr><td>RSSI</td><td>"+rssi+"</td></tr>"+
	"<tr><td>SNR</td><td>"+snr+"</td></tr>"+tr+
	"<tr><td colspan=2 style='text-align:center;'>"+timestamp+"</td></tr>"+
	"</table></tbody>");
	$('#dataDetailsHeader').html("Scan Details");
	$.fn.collapse(false, true);
	
	$(element).addClass("selected_mobile");
	
	$("#mobileDataDetails").modal("show");
		}
	});	
}

var getCountryWiseDataStats = function()
{
	postData={"startTime":operationStartTIme};
	$.ajax({
		url:"../../service/2g/countrywisedatastats",
		data:postData,
		type:'post',
		success:function(data)
		{
			console.log(data);
			$("#detail_tab_2 .count_row").remove("");
			$("#detail_tab_2 tbody").append(data);
			
			checkCheckCounter();
		}
	});	
}

var getGpsData = function()
{
	
		postData={"startTime":operationStartTIme};
		$.ajax({
			url:"../../service/2g/gpsdata",
			data:postData,
			type:'post',
			success:function(data)
			{
				console.log(data);
				path = [];
				for(var i in data)
				{
					
					
					if(path.length >= 1 && distanceBetweenLatLon(parseFloat(path[path.length-1][0]),parseFloat(path[path.length-1][1]),parseFloat(data[i].l),parseFloat(data[i].n)) > accuracyGPS )
					{
						path.push([data[i].l,data[i].n]);	
					}
					else if(path.length==0)
					{
						path.push([data[i].l,data[i].n]);	
					}
					
					
				}
				for(var k in lines)
				{
					lines[k].remove();
				}
				for(var j in lastEventPt)
				{
					
					if(path.length > 1 && distanceBetweenLatLon(parseFloat(path[path.length-1][0]),parseFloat(path[path.length-1][1]),parseFloat(lastEventPt[j][0]),parseFloat(lastEventPt[j][1])) > accuracyGPS )
					{
						path.push(lastEventPt[j]);	
					}
					else if(path.length==0)
					{
						path.push(lastEventPt[j]);	
					}
					
					
					
				}
				
				
				if(path.length>1)
				{
					
					drawLine(path,map);
				}
				
				if(path.length>=1)
				{
					addTower(path[(path.length)-1],'../../resources/images/tower.png');
				}
				
				try
				{
					//map.panTo(new L.LatLng(path[(path.length)-1][0], path[(path.length)-1][1]));	
				}
				catch(err)
				{
					
				}
				
				checkCheckCounter();
			}
		});	
	
		
}

var getImsiData = function()
{
	postData={"startTime":operationStartTIme};
	$.ajax({
		url:"../../service/2g/getsubscriberdata",
		data:postData,
		type:'post',
		success:function(data)
		{
			console.log(data);
			for(var i in mobileMarkers)
			{
				mobileMarkers[i].remove();
			}
			mobileMarkers = [];
			
			updateImsiList(data);
			checkCheckCounter();
		}
	});	
}


var getSubscriberPath = function(imsi)
{
	postData={"imsi":imsi,"startTime":operationStartTIme};
	$.ajax({
		url:"../../service/2g/getsubscrberpath",
		data:postData,
		type:'post',
		success:function(data)
		{

			var subcriberpatharray = [];
			for(var i in data)
			{
				subcriberpatharray.push([data[i].lat,data[i].lon]);
			}
			if(subcriberpatharray.length >1)
			{
				drawSubscriberPath(subcriberpatharray);	
			}else if(subcriberpatharray.length ==1)
			{
				map.setView(subcriberpatharray[0]);
			}
			
		}
	});	
}

var getNetworkData = function()
{
	postData={"startTime":operationStartTIme};
	$.ajax({
		url:"../../service/2g/getnetorkinfonodewise",
		data:postData,
		type:'post',
		success:function(data)
		{
			
			$("#detail_tab_3 .count_row_net").remove("");
			$("#detail_tab_3 tbody").append(data);
			checkCheckCounter();
		}
	});	
}

var updateNodesStatus = function()
{
	
	$.ajax({
		url:"../../service/2g/updatestatusofallbts",
		type:'get',
		beforeSend:function()
		{
		
			$("#status_loading").show();
			$("#updateStatusBtn").attr({"disabled":"disabled"});
			$("#device_status_table").hide();
		},
		success:function(data)
		{
			console.log(data);
			$("#device_status_table tbody").html("");
			$("#device_status_table tbody").append(data);
			$("#status_loading").hide();
			$("#updateStatusBtn").removeAttr("disabled");
			$("#device_status_table").show();
			
		},
		error:function(xhr)
		{
			$("#updateStatusBtn").removeAttr("disabled");
			$("#status_loading").hide();
			$("#device_status_table").show();
		}
		
	});	
}


var getNodesInfo = function(imsi)
{
	
	$.ajax({
		url:"../../service/2g/getNodesStatus",
		
		type:'post',
		success:function(data)
		{
			$("#device_status_table tbody").html("");
			$("#device_status_table tbody").append(data);
		}
	});	
}


/***********************Data Call section END******************************************/
var filterData = function(type,val)
{
	//alert(type+"--"+val);
	
	
	var d = new Date();
	
	operationEndTime =  toUtcTime(d);
	d.setMinutes(d.getMinutes() - val);
	if(val != -1)
	{
	 	operationStartTIme = toUtcTime(d);
	}
	else
	{
		operationStartTIme = realoperationStartTIme;
		operationEndTime = -1;
	}
	addsessionparam("endTime",operationEndTime);
	addsessionparam("startTime",operationStartTIme);
	counterForLoading = 5;
	$('#loading_modal').modal('show');
	if(type == 'history')
	{
		 
		 if(ws !=null)
		 {
			 ws.close();
			 ws = null;	 
		 }
		 if(ws1 !=null)
		 {
			 ws1.close();
			 ws1 = null;	 
		 }
		 if(ws2 !=null)
		 {
			 ws2.close();
			 ws2 = null;	 
		 }
		 if(ws3 !=null)
		 {
			 ws3.close();
			 ws3 = null;	 
		 }
		 
		 getData();
		 getImsiData();
		 getNetworkRelatedData();
		 getGpsData();
		 $('#historyfiltermodal').modal('hide');
	}
	else
	{
		captureCdrEvent();
		captureImsiEvent();
		captureNetworkEvent();
		getGpsData();
		captureGPSEvent();
		$('#restfiltermodal').modal('hide');
	}	
}

var addsessionparam = function(name,val)
{
	var postData={"name":name,"val":val};
	$.ajax({
		url:"../../service/2g/addsessionparam",
		data:postData,
		type:'post',
		async:'false',
		success:function(data)
		{
			console.log("session update"+data);
		}
	});	
}

var initData = function()
{
	$.ajax({
		url:"../../service/2g/getoperationstarttime",
		//data:postData,
		type:'post',
		success:function(data)
		{
			console.log(data);
			realoperationStartTIme = operationStartTIme = data[0].inserttime;
			var val = <%= session.getAttribute("startTime")==null?null:"'"+session.getAttribute("startTime")+"'" %>;
			if( val == null)
			{
				addsessionparam("startTime",operationStartTIme)
			}
			else
			{
				operationStartTIme = val;
			}
			captureCdrEvent();
			captureImsiEvent();
			captureNetworkEvent();
			getGpsData();
			captureGPSEvent();
			captureDeviceStatusEvent();
			getData();
			getNetworkRelatedData();
		}
	});
}

$("document").ready(function(){
	//initData();
	startTime();
});

</script>
<style>
#dashboardMenu{
font-weight:900;
text-decoration:underline;
}

#detected_mobile_modal .modal-dialog,#coming_cells_modal .modal-dialog
{
	width: 100%;
  margin: 0;
  padding: 0;
}


#detected_mobile_modal .modal-dialog,#coming_cells_modal .modal-dialog
{
	font-family: Arial, Helvetica, sans-serif;
}



#detected_mobile_modal .modal-content,#coming_cells_modal .modal-dialog
{
	height: auto;
  	min-height: 100%;
  	border-radius: 0;
}
.table-mob-dtect
{
    margin: 0 auto;
}
.table-mob-dtect thead th
{
	text-align:center;
}

.selected_mobile
{
	background:red;
}
#coming_cells_modal .modal-header{
 /*height: 8.46%;*/
 border-bottom: 0px;
}

#coming_cells_modal .modal-header,#detected_mobile_modal .modal-header{
	height: 7%;
}

#coming_cells_modal_body,#detected_mobile_modal_body{
	height: 80%;
    padding-left: 6.22%;
    padding-right: 6.22%;
    overflow: auto;
    padding-top: 2px;
    padding-bottom: 15px;
}

#coming_cells_modal .modal-body,#detected_mobile_modal .modal-body{
	padding: 0px;
}

.opr_net{
	margin-left: 3px;
	border-radius: 12px;
	margin-top:10px;
	margin-right:7px;
	margin-left:7px;
	margin-bottom:7px;
	width:187px;
	height:108px;
	position:relative;
	/*background-color:rgb(198,235,251);*/
}

.mobile_net{
	cursor:pointer;
	padding: 5px;
 	float:left;
 	/*background-image: url("../../resources/images/user4.png");*/
	margin-left: 3px;
	margin-bottom:3px;
	margin-top:10px;
	margin-right:7px;
	border-radius: 12px;
	width:183px;
	position:relative;
	/*background-color:rgb(198,235,251);*/
}

.mobtxta
{
	font-size: 11px;
	margin-bottom: 0px;
	text-align: left;
	font-family:arial;
     white-space: nowrap;
     overflow: hidden;
     text-overflow: ellipsis;
}

.mobtxtb,.mobtxtc,.mobtxtd,.mobtxte
{
	font-size: 11px;
	margin-bottom: 0px;
	text-align: left;
	width: 130px;
	font-family:arial;
     white-space: nowrap;
     overflow: hidden;
     text-overflow: ellipsis;
	     margin-left: 13px;
}

.mobtxtd{
font-weight: 700;
}

.ll > tbody > tr > td {
    font-size: 12px;
}
</style>

<div class="wrapper">
	

    <div class="main-panel">
		<jsp:include page='nav.jsp' />
<div id="map_leaf" style="width:100%;height:calc(100% - 81px);"></div>
<div class="card card-map"  id="clientsDropDown" style="margin-bottom: 0px;margin-top: 3px;border-top-left-radius:10px;border-top-right-radius: 7px;box-shadow: 0px 2px 15px #000000;display: grid;">
	<div style="border-bottom: 1px dotted;padding: 5px;padding-left: 13px;font-size: 12px;font-weight: 900;">
		<span style="float: left;">
			<h4 class="title" style="font-weight:700;">Detected Devices</h4>
		</span>
		<span id="toggle_device_tab" style="float: right;">
			<img src="../../resources/images/up_arrow.png">
		</span>
		<span style="float: right;">
			<h4 class="title" style="font-weight:700;">Total:<a onclick="" id="total_d">0</a>&nbsp;&nbsp;&nbsp;&nbsp;Detected : <a onclick="" id="total_d_d">0</a></h4>
		</span>
		
	</div>
	<div id="tracker_tab" style="display:none;height:185px;overflow:auto;padding-left: 10px;padding-right: 10px;">
	     <table style="font-size:15px;font-weight:900;" class="table table-hover">
	           <thead>
	               <th><strong>Name</strong></th>
	               <th><strong>IMSI</strong></th>
		           	<th><strong>IMEI<strong></th>
					<th><strong>Type<strong></th>
		           	<th><strong>TA</strong></th>
		           	<th><strong>POWER</strong></th>
		           	<th><strong>ACC</strong></th>
		           	<th><strong>Position</strong></th>
		           	<th><strong>Distance</strong></th>
		           	<th><strong></strong></th>
	           </thead>
	           <tbody>
	           </tbody>
	       </table>
	   </div>
</div>
		<button type="button" class="btn btn-demo" data-toggle="modal" data-target="#myModal" style="padding:0px;position: fixed;bottom: calc(100% - 137px);left: -16px;z-index: 1400;float: left;">
			<img src="../../resources/images/left_arrow.png"/>
		</button>
		<button type="button" class="btn btn-demo" data-toggle="modal" data-target="#device_status_Modal" style="padding:0px;position: fixed;bottom: calc(100% - 137px);left: calc(100% - 35px);z-index: 1400;float: right;">
			<img src="../../resources/images/right_arrow.png"/>
		</button>
	</div>
</div>

<style>
	
	
	.fillter_container
	{
		padding-bottom:5px;
		overflow: auto;
	}
	
 .spanstyle {
  font-size: 14px;
  float:right;
 cursor:pointer;
}
	
</style>
<div class="modal left fade" id="myModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<!--<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Summary</h4>
				</div>-->
				<div class="modal-body">
					
					<div id="report_table">
					<div class="fillter_container">
					
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<button type="button" data-toggle="modal" data-target="#restfiltermodal" class="btn btn-default filter_button">Restart</button>
						
						<button type="button" data-toggle="modal" data-target="#historyfiltermodal" class="btn btn-default filter_button">History- Time Interval</button>
						
					</div>
					<fieldset style="margin-top:-21px;">
						<legend class="legendheader">Identity Data Statistics</legend>
					
					<table border=1 id="detail_tab_1" class="table table-default report_tables">
							<thead>
							<tr>
								<th style="text-align:center" colspan=13>Node Wise
								<span class="spanStyle ti-arrow-circle-up" id="nodeDetails"><span class="arrowClass" style="display:none;">-</span></span></th>
							</tr>
							</thead>
							<tbody>
								<tr>
									<td></td>
									<td></td>
									<td colspan=6>Distance</td>
									<td colspan=5>Signal Strength</td>
								</tr>
								
								<tr>
									<td></td>
									<td>Total IMSI, IMEI</td>
									<td>0</td>
									<td>1</td>
									<td>2</td>
									<td>&lt;5</td>
									<td>&gt;5</td>
									<td>NA</td>
									<td>&gt;-55</td>
									<td>&gt;-75</td>
									<td>&gt;-95</td>
									<td>&lt;-95</td>
									<td>NA</td>
									
								</tr>
							</tbody>
						</table>
						<table border=1 id="detail_tab_2" class="table table-default report_tables">
							<thead>
							<tr>
								<th colspan=14>Country Wise
								<span class="spanStyle ti-arrow-circle-up" id="countryDetails"><span class="arrowClass" style="display:none;">-</span></span></th>
							</tr>
							</thead>
							<tbody>
								
								
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td colspan=6>Distance</td>
									<td colspan=5>Signal Strength</td>
								</tr>
								<tr>
									<td>Country</td>
									<td>Operator</td>
									<td>Total IMSI, IMEI</td>
									<td>0</td>
									<td>1</td>
									<td>2</td>
									<td>&lt;5</td>
									<td>&gt;5</td>
									<td>NA</td>
									<td>&gt;-55</td>
									<td>&gt;-75</td>
									<td>&gt;-95</td>
									<td>&lt;-95</td>
									<td>NA</td>
									
								</tr>
								
							</tbody>
						</table>
						</fieldset>
						<fieldset>
							<legend class="legendheader">Network Scan Information</legend>
						<!-- <div class="fillter_container">
						<button type="button" class="btn btn-default filter_button">Reset</button>
						<button type="button" class="btn btn-default filter_button">Start Monitoring</button>
						<button type="button" class="btn btn-default filter_button">History- Time interval</button>
						<button type="button" class="btn btn-default filter_button">Latest 5 min interval</button>
					</div> -->
						<table border=1 id="detail_tab_3" class="table table-default report_tables">
							<thead>
							<tr>
								<th colspan=10>Network Information (Based on Region)
								<span class="spanStyle ti-arrow-circle-up" id="networkDetails"><span class="arrowClass" style="display:none;">-</span></span></th>
							</tr>
							</thead>
							<tbody>
								<tr>
									<td>Operator</td>
									<td>Node</td>
									<td>Cell</td>
								</tr>
								
								
							</tbody>
						</table>
						</fieldset>
						
					</div>
					
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	<!--  Right Modal -->
<div class="modal right fade" id="device_status_Modal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<!--  <h4 class="modal-title" id="myModalLabel">System Nodes Info</h4>-->
				</div>
				<div class="modal-body">
					<!--<div class="fillter_container">
						<button type="button" id = "updateStatusBtn" onclick="updateNodesStatus()" data-toggle="modal" class="btn btn-default filter_button">Update Status</button>
					</div>-->
						<table border=1 id="device_status_table" class="table table-default table-bordered table-text-align">
							<thead>
								<th>IP</th>
								<th>Cell Status</th>
								<th>Admin Status</th>
							</thead>
							<tbody>
							</tbody>
						</table>
						<table border=1 id="operation_events_table" class="table table-default table-bordered table-text-align">
							<thead>
								<th colspan="2" style="text-align: center;">Events</th>
							</thead>
							<tbody id="operation_events_table_tbody">
							</tbody>
						</table>
						<img width=100% id="status_loading" src="../../resources/images/Loading_icon.gif" style="display:none;"></img>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->	
	<!--  Right Modal -->
	
	<!-- reset filter modal -->
	
<div class="modal fade" id="restfiltermodal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Restart From</h4>
				</div>
				<div class="modal-body">
				<table class="table">
					<tbody>
					 <tr>
						<td><a class="filterDataButton"  onclick="filterData('restart',1)">last 1 Min</a></td>
						<td><a class="filterDataButton"  onclick="filterData('restart',5)">last 5 Min</a></td>
						<td><a class="filterDataButton"  onclick="filterData('restart',10)">last 10 Min</a></td>
						<td><a class="filterDataButton"  onclick="filterData('restart',20)">last 20 Min</a></td>
						<td><a class="filterDataButton"  onclick="filterData('restart',-1)">Beginning</a></td>
					</tr>
					</tbody>
				</table>
				
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
<div class="modal fade" id="detected_mobile_modal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content" style="overflow:auto">
				<div class="modal-header" style="text-align:center;padding-bottom:0px;background-color: rgb(0,103,177);">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true" style="color:white;">&times;</span></button>
					<button type="button" class="btn btn-default" style="float: right; margin-right: 40px;background-color:white;color:black;" onclick="filterMobDataData()"><span aria-hidden="true">Reset</span></button>
					<h4 style ="font-family:arial;color:white;" class="modal-title" id="myModalLabel" >Mobile Detected</h4>

				</div>
				<div class="modal-body">
				<div id="searchDiv" style="float: right;padding-top: 6px;padding-right: 9.5%;">
				<table class="report-tables">
				<tr>
				<th>
				<input type="text" id="ueSearchText" style="margin: 4px;height: 31px;"></input>
				</th>
				<th>
				<button id="ueSearchButton" class="btn btn-default">Search</button>
				</th>
				<th>
				<select id="ueDisplayListSelect" style="height: 31px;margin-left: 4px;">
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200" selected>200</option>
				<option value="all">All</option>
				</select>
				</th>
				<tr>
				</table>
				</div>
				<div id="detected_mobile_modal_body" style="overflow: auto;clear: both;"></div>
								<div class="card card-map" style="height:5.6%;margin-bottom: 0px;margin-top: 3px;border-top-left-radius:10px;border-top-right-radius: 7px;box-shadow: 0px 2px 15px #000000;display: grid;">
	<div style="border-bottom: 1px dotted;padding: 5px;padding-left: 13px;font-size: 12px;font-weight: 900;">
	<!-- <span style="font-weight: 900;font-size: 12px;font-family:arial;float:left;">Users</span>-->
		<span id="toggle_device_tab" style="padding-left:28%;">
        <span>Technology :</span>
		&nbsp;&nbsp;&nbsp;		
		<span><img src="../../resources/images/box3.png" class="imgClass"></span>        
		<span>2G</span>
		&nbsp;&nbsp;&nbsp;
		<span><img src="../../resources/images/box6.png" class="imgClass"></span>
		<span>3G</span>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <span>Subscriber :</span>
		&nbsp;&nbsp;&nbsp;
		<span><img src="../../resources/images/user2.png" class="imgClass"></span>
		<span>Mapped</span>
		&nbsp;&nbsp;&nbsp;
		<span><img src="../../resources/images/user1.png" class="imgClass"></span>
		<span>Unmapped</span>
		</span>
		<span style="font-weight: 900;font-size: 12px;font-family:arial;float:right;" id="clock_time"></span>
	</div>	
</div>
				
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
	<div class="modal fade" id="coming_cells_modal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content" style="overflow:auto">
				<div class="modal-header" style="text-align:center; padding-bottom:0px;background-color:rgb(0,103,177);">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close" style="color:white;"><span aria-hidden="true">&times;</span></button>
					<!--  <button type="button" class="btn btn-default" style="float: right; margin-right: 40px;background-color:white;color:black;" onclick="filterMobDataData()">--><!-- <span style="float: right; margin-right: 40px;background-color:white;color:black;" "aria-hidden="true"></span> Reset--><!--  </button>-->
					<h4 style ="font-family:arial;color:white;" class="modal-title" id="comingCellModalLabel" >Cells Scanned</h4>					
					<h4 style="text-align: right;margin: 10px 38px 0px 0px;font-weight: 900;font-size: 12px;font-family:arial;" id="clock_time_cells_detected"></h4>
				</div>
				<div class="modal-body">
				<div id="coming_cells_modal_body" style="overflow:auto;"></div>
				<div class="card card-map" style="height:5.6%;margin-bottom: 0px;margin-top: 3px;border-top-left-radius:10px;border-top-right-radius: 7px;box-shadow: 0px 2px 15px #000000;display: grid;">
	<div style="border-bottom: 1px dotted;padding: 5px;padding-left: 13px;font-size: 12px;font-weight: 900;">
	<span style="font-weight: 900;font-size: 12px;font-family:arial;float:left;">Technologies</span>
		<span id="toggle_device_tab" style="padding-left:42%;">
		<span><img src="../../resources/images/box3.png" class="imgClass"></span>
		<span>GSM</span>
		&nbsp;&nbsp;&nbsp;
		<span><img src="../../resources/images/box2.png" class="imgClass"></span>
		<span>UMTS</span>
		&nbsp;&nbsp;&nbsp;
		<span><img src="../../resources/images/box6.png" class="imgClass"></span>
		<span>LTE</span>
		</span>
	</div>	
</div>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->



<div class="modal fade" id="loading_modal" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Please Wait...</h4>
				</div>
				<div class="modal-body">
				<img width=100% id="status_loading" src="../../resources/images/Loading_icon.gif"></img>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->




	<!-- history filter modal -->
	
<div class="modal fade" id="historyfiltermodal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Show History From</h4>
				</div>
				<div class="modal-body">
				<table class="table">
					<tbody>
						<tr>
							<td><a class="filterDataButton" onclick="filterData('history',1)">last 1 Min</a></td>
							<td><a class="filterDataButton"  onclick="filterData('history',5)">last 5 Min</a></td>
							<td><a class="filterDataButton"  onclick="filterData('history',10)">last 10 Min</a></td>
							<td><a class="filterDataButton"  onclick="filterData('history',20)">last 20 Min</a></td>
							<td><a class="filterDataButton"  onclick="filterData('history',-1)">Beginning</a></td>
						</tr>
					</tbody>
				</table>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->


<div class="modal fade" id="mobileDataDetails" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width:325px;" role="document">
			<div class="modal-content">
				<div class="modal-header" style="padding: 5px;">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="dataDetailsHeader" style="font-size:16px;padding: 7px;">Scan Details</h4>
				</div>
				<div class="modal-body">
				<table>
				<tr>
				<td>
				    <div class="col c1">
      <h2>Default Example</h2>
      <div id="default-example23" data-collapse>
        <h3>Fruits</h3>
        <div>I like fruits. This <a href="#work">link should work</a></div>
        <h3>Info</h3>
        <div>This is some information</div>
      </div>
    </div>
	</td>
	</tr>
	</table>
					
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->

	
	
	<div class="modal fade" id="nodeWiseReportModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 1120px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Node Wise Report</h4>
				</div>
				<div class="modal-body" style="overflow: auto;">
				<div>
					<table id="nodeWiseReportGrid"></table>
					<div id="nodeWiseReportPager" style="text-align: center;"></div>
				</div>
				</div>
				<div class="modal-footer">
				<button class="btn btn-primary" onclick="drawPlotOnNewWindow()">Plot</button>
				<button class="btn btn-primary"  id="nodeWiseExcelExportId">Export</button>
									<button type="button" class="btn btn-default"
										data-dismiss="modal">Close</button>
										<div id="nodeWiseExcelExportDiv"></div>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
			<div class="modal fade" id="countryWiseReportModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 1120px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Country Wise Report</h4>
				</div>
				<div class="modal-body" style="overflow: auto;">
				<div>
				<table id="countryWiseReportGrid"></table>
				<div id="countryWiseReportPager" style="text-align: center;"></div>
				<div class="contextMenu" id="contextMenu" style="display:none; width:400px;">
        <ul style="width: 400px; font-size: 65%;">
            <li id="add">
                <span class="ui-icon ui-icon-plus" style="float:left"></span>
                <span style="font-size:130%; font-family:arial">Add Row</span>
            </li>
            <li id="edit">
                <span class="ui-icon ui-icon-pencil" style="float:left"></span>
                <span style="font-size:130%; font-family:arial">Edit Row</span>
            </li>                
            <li id="del">
                <span class="ui-icon ui-icon-trash" style="float:left"></span>
                <span style="font-size:130%; font-family:arial">Delete Row</span>
            </li>                
        </ul>
    </div>
				</div>
				</div>
				<div class="modal-footer">
				<button class="btn btn-primary" onclick="drawPlotOnNewWindow()">Plot</button>
				<!--<button class="btn btn-primary" onclick="drawPlotOnNewWindow()">Plot</button>-->
				<button class="btn btn-primary"  id="countryWiseExcelExportId">Export</button>
									<button type="button" class="btn btn-default"
										data-dismiss="modal">Close</button>
										<div id="countryWiseExcelExportDiv"></div>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
			<div class="modal fade" id="networkScanReportModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 1170px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Network Scan Report</h4>
				</div>
				<div class="modal-body" style="overflow:auto;">
				<div>
				<table id="networkScanReportGrid"></table>
				<div id="networkScanReportPager" style="text-align: center;"></div>
				<div class="contextMenu" id="contextMenu" style="display:none; width:400px;">
        <ul style="width: 400px; font-size: 65%;">
            <li id="add">
                <span class="ui-icon ui-icon-plus" style="float:left"></span>
                <span style="font-size:130%; font-family:arial">Add Row</span>
            </li>
            <li id="edit">
                <span class="ui-icon ui-icon-pencil" style="float:left"></span>
                <span style="font-size:130%; font-family:arial">Edit Row</span>
            </li>                
            <li id="del">
                <span class="ui-icon ui-icon-trash" style="float:left"></span>
                <span style="font-size:130%; font-family:arial">Delete Row</span>
            </li>                
        </ul>
    </div>
				</div>
				</div>
				<div class="modal-footer">
				<button class="btn btn-primary"  id="networkScanExcelExportId">Export</button>
									<button type="button" class="btn btn-default"
										data-dismiss="modal">Close</button>
										<div id="networkScanExcelExportDiv"></div>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->

	

<style>
#detail_tab_1 tbody tr:nth-child(1){
	 font-weight: bold;	
}
#detail_tab_1 tbody tr:nth-child(2){
	 font-weight: bold;	
}
#detail_tab_2 tbody tr:nth-child(1){
	 font-weight: bold;
}
#detail_tab_2 tbody tr:nth-child(2){
	 font-weight: bold;	
}
#detail_tab_3 tbody tr:nth-child(1){
	 font-weight: bold;
}
#device_status_table thead th{
	font-weight: bold;
	padding: 5px !important;
	font-size:15px !important;
}

.filterDataButton
{
	cursor: pointer;
}
.main-panel {
    background-color: #f4f3ef;
    position: relative;
    z-index: 2;
    float: none;
    width: 100%;
    min-height: 100%;
}


/*******************************
* MODAL AS LEFT/RIGHT SIDEBAR
* Add "left" or "right" in modal parent div, after class="modal".
* Get free snippets on bootpen.com
*******************************/
	.modal.left .modal-dialog,
	.modal.right .modal-dialog {
		position: fixed;
		margin: auto;
		width: auto;
		height: 100%;
		-webkit-transform: translate3d(0%, 0, 0);
		    -ms-transform: translate3d(0%, 0, 0);
		     -o-transform: translate3d(0%, 0, 0);
		        transform: translate3d(0%, 0, 0);
	}

	.modal.left .modal-content,
	.modal.right .modal-content {
		height: 100%;
		overflow-y: auto;
	}
	
	.modal.left .modal-body,
	.modal.right .modal-body {
		padding: 15px 15px 80px;
	}

/*Left*/
	.modal.left.fade .modal-dialog{
		left: -320px;
		-webkit-transition: opacity 0.3s linear, left 0.3s ease-out;
		   -moz-transition: opacity 0.3s linear, left 0.3s ease-out;
		     -o-transition: opacity 0.3s linear, left 0.3s ease-out;
		        transition: opacity 0.3s linear, left 0.3s ease-out;
	}
	
	.modal.left.fade.in .modal-dialog{
		left: 0;
	}
        
/*Right*/
	.modal.right.fade .modal-dialog {
		right: -320px;
		-webkit-transition: opacity 0.3s linear, right 0.3s ease-out;
		   -moz-transition: opacity 0.3s linear, right 0.3s ease-out;
		     -o-transition: opacity 0.3s linear, right 0.3s ease-out;
		        transition: opacity 0.3s linear, right 0.3s ease-out;
	}
	
	.modal.right.fade.in .modal-dialog {
		right: 0;
	}

/* ----- MODAL STYLE ----- */
	.modal-content {
		border-radius: 0;
		border: none;
	}

	.modal-header {
		border-bottom-color: #EEEEEE;
		background-color: #FAFAFA;
	}

/* ----- v CAN BE DELETED v ----- */
body {
	background-color: #78909C;
}

.demo {
	padding-top: 60px;
	padding-bottom: 110px;
}

.btn-demo {
	margin: 15px;
	padding: 10px 15px;
	border-radius: 0;
	font-size: 16px;
	background-color: #FFFFFF;
}

.btn-demo:focus {
	outline: 0;
}

.demo-footer {
	position: fixed;
	bottom: 0;
	width: 100%;
	padding: 15px;
	background-color: #212121;
	text-align: center;
}

.demo-footer > a {
	text-decoration: none;
	font-weight: bold;
	font-size: 16px;
	color: #fff;
}

#clientsDropDown {
  position:absolute;
  bottom:0;
  width: 100%;
  z-index: 1200;
}
#clientsDropDown #tracker_tab {
  display: none;
}

#tracker_tab table td 
{
	    padding: 4px;
}
#tracker_tab table th
{
	    padding: 4px;
}


.showme
{ 
	display: none;
}
.showhim
{
	z-index: 900 !important;
	background:white;
	padding:5px;
}
.showhim:hover .showme
{
			display : block;
			position:absolute;
			background:white;
			padding:5px;
			width:115px;
			right:0px;
}


.ui-jqgrid tr.ui-row-ltr td {
    text-align: center !important;
    }
	
	#nodeWiseReportModal thead,#nodeWiseReportModal tbody,#countryWiseReportModal thead,#countryWiseReportModal tbody,#networkScanReportModal thead,#networkScanReportModal tbody{
	font-size: 14px;
	}

	#nodeWiseReportModal .modal-header,#countryWiseReportModal .modal-header,#networkScanReportModal .modal-header{
	height: 50px;
	}
	
	legend{
	font-size: 18px;
	}
	
	.title{
		font-size: 16px;
	}
	
	.report_tables,.report_tables tr,.report_tables th,.report_tables td{
		border: 1px solid black;
	}
	
	.report_tables thead tr th{
		border: 1px solid black;
	}
	
	#detail_tab_1 tbody tr td:nth-child(n+3){
		text-align: center;
	}
	#detail_tab_2 tbody tr td:nth-child(n+4){
		text-align: center;
	}
	#detail_tab_3 tbody tr td:last-child{
		text-align: center;
	}
	 
	.report_tables thead{
	    background: #64a6e6;
    	color: white;
	}
	
	.report_tables th {
		padding-bottom: 4px;
	}
	
	.report_tables tbody tr:nth-last-child(1){
	 background-color: rgb(242,242,242);
	}
	

	
	.serialNoClass {
    width: 25px;
    height: 20px;
    background-color: rgb(0,103,177);
    position: absolute;
    bottom: 0px;
    right: 0px;
    border-bottom-right-radius: 12px;
    }
    
    .serialNoClass p {
    color: white;
    font-family: Arial;
    font-size: 12px;
    text-align: center;
    margin-top: 8%;
	}
	
	#coming_cells_modal {
	 overflow-x: hidden;
	 overflow-y: auto;
	}
	
	.imageClass {
	 width: 30px;
	 height: 35px;
	 display:block;
	 float: left;
	 margin-top: 12%;
     margin-left: 1.1%;
	}
	
	.cellDetailClass{
		float:right;
		margin-top: 4%;
	}
	
	.table-text-align th{
		text-align: center;
	}
	
   .table-text-align td{
		text-align: center;
	}
	
	.imgClass{
		height: 15px;
	}
	
	#operation_events_table_tbody td{
		text-align: left;
	}
	
	
	.logtimeTd{
		width: 123px;
	}
	
	#operation_events_table > tbody > tr > td {
		font-size: 11px;
		font-weight: 700;
	}
	
</style>

<link rel="stylesheet" href="../../resources/lib/leaflet/leaflet.css" />
<link rel="stylesheet" href="../../resources/lib/expand/demo.css">
<script src="../../resources/lib/leaflet/leaflet.js" ></script>
<script src="../../resources/lib/leaflet/Leaflet.GoogleMutant.js" ></script>
<script>

var map = null;
var mobileMarkerLayer =null;
var towerMarkerLayer =null;
var pathLayer =null;
var subscriberPathLayer =null;
var subscriberPathCircle =null;
var	opratorTowersLayer =null;
var taCircleLayers =null;
var mobileMarkers = [];
var towerMarker = null;
var lines = [];
var subscriberPath = null;

/***************************************************************************/
	
$('#nodeDetails').on("click",function(){
openCloseDialog(this);
});

$('#countryDetails').on("click",function(){
openCloseDialog(this);
});

$('#networkDetails').on("click",function(){
openCloseDialog(this);
});

var openCloseDialog = function(selector){
	var sign=$(selector).closest('table').find('.arrowClass').html();
	if(sign=="-"){
		var thwidth=$(selector).closest('th').width() ;
		//th set min width
		$(selector).closest('th').width(thwidth);
		$(selector).closest('table').find('tbody').hide();
		$(selector).removeClass("ti-arrow-circle-up").addClass("ti-arrow-circle-down");
		$(selector).closest('table').find('.arrowClass').html("+");
	}
	else{
		var thwidth=$(selector).closest('th').width() ;
		$(selector).closest('th').width(thwidth);
		$(selector).closest('table').find('tbody').show();
		$(selector).removeClass("ti-arrow-circle-down").addClass("ti-arrow-circle-up");
		$(selector).closest('table').find('.arrowClass').html("-");
	}
}
	
/***********************************************************/

function loadLeafMap()
{
	//var mymap = L.map('map_leaf').setView([51.505, -0.09], 13);
	// map = new L.Map('map_leaf', {center: new L.LatLng(28.7041, 77.1025),  zoom: 14, minZoom: 0,maxZoom: 18});
 map = new L.Map('map_leaf', {center: new L.LatLng(28.7041, 77.1025),  zoom: 14});

    map.zoomControl.setPosition('bottomright');
	
	var googleMaplayer = L.gridLayer.googleMutant({
			type: 'roadmap' // valid values are 'roadmap', 'satellite', 'terrain' and 'hybrid'
		}).addTo(map);

     
	//var openstreatMapLayer = L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	  var openstreatMapLayer = L.tileLayer('http://'+mapServerIp+'/hot/{z}/{x}/{y}.png', {
		attribution: 'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'mapbox.streets'
	}).addTo(map);
	
	
	
	towerMarkerLayer = L.layerGroup().addTo(map);
	pathLayer = L.layerGroup().addTo(map);
	taCircleLayers = L.layerGroup().addTo(map);
	subscriberPathLayer = L.layerGroup().addTo(map);
	subscriberPathCircle = L.layerGroup().addTo(map);
	mobileMarkerLayer = L.layerGroup().addTo(map);
	//mobileMarkerLayer.bringToFront()

	
	opratorTowersLayer =  L.layerGroup().addTo(map);
	
	
	
		var showHideControl  = L.Control.extend({
		options: {
			position: 'bottomright'
		  },
		  onAdd: function (map) 
		  {
				var container = L.DomUtil.create('div');
				$(container).addClass("showhim");
				$(container).html('HOVER ME'
									+'<div class="showme" style="width: 125px;">'
										+'<ul style="padding-left: 5px;">'
											+'<li><input type="checkbox" onclick="showhide(1,this)" checked>Tower</li>'
											+'<li><input type="checkbox" onclick="showhide(2,this)" checked>Path</li>'
											+'<li><input type="checkbox" onclick="showhide(3,this)" checked>Mobile</li>'
											+'<li><input type="checkbox" onclick="showhide(4,this)" checked>TA</li>'
											+'<li><input id="open_detected_screen" type="checkbox" onclick="showhide(5,this)" >UE Detected</li>'
											+'<li><input id="open_coming_cells_screen" type="checkbox" onclick="showhide(6,this)" >Cells Scanned</li>'
											+'<li><input id="showHideTowers" type="checkbox" onclick="showhide(7,this)" >Operators</li>'
										+'</ul'
									+'</div>');
				return container;
		  }
		  
	}); 
 


	var customControl =  L.Control.extend({

		  options: {
			position: 'bottomright'
		  },

		  onAdd: function (map) {
			var container = L.DomUtil.create('img');
			//container.type="checkbox";
			container.id="map_type";
			container.title="Offline";
            container.src="../../resources/images/offline.png"  
			container.style.backgroundColor = 'white';
			$(container).addClass("offline");			
			//container.style.backgroundImage = "url(https://t1.gstatic.com/images?q=tbn:ANd9GcR6FCUMW5bPn8C4PbKak2BJQQsmC-K9-mbYBeFZm1ZM2w2GRy40Ew)";
			/*container.style.backgroundSize = "30px 30px";*/
			container.style.width = '33px';
			container.style.height = '33px';
			
			container.onclick = function(){
			  console.log($(container));
			  //$(container)
			  if($(container).hasClass('offline')){
				 map.removeLayer(openstreatMapLayer);
				 //map.addLayer(googleMapLayer);
				 container.title="Online";
				 $(container).removeClass("offline");	
				 $(container).addClass("online");	
				 container.src="../../resources/images/online.png"  
			}
			else {
				//map.removeLayer(googleMapLayer);
				map.addLayer(openstreatMapLayer);
				container.title="Offline";
				$(container).addClass("offline");	
				 $(container).removeClass("online");
				 container.src="../../resources/images/offline.png"  
			}
			
			}
			return container;
		  }
		});
	 
	 map.addControl(new customControl());
	 map.addControl(new showHideControl());

}



var drawLine = function(path,map)
{
	
	//var polyline = L.polyline(path, {color: 'red'}).addTo(map);
	try
	{
		var polyline = L.polyline(path, {color: 'red'});
		//var polyline = L.polyline([[0,0]], {color: 'red'}).addTo(map);
		lines.push(polyline);
		pathLayer.addLayer(polyline);	
	}
	catch(err)
	{
		
	}
	
}



var addmobile = function(data,iconUrl)
{
	//var mob = L.marker([28.7041, 77.1025],{icon:L.icon({iconUrl: '../../resources/images/mob_org.png'})}).addTo(map);
	
	try
	{
		var myIcon = L.icon
		({
				iconUrl: iconUrl,
				iconSize: [15, 15],
				//iconSize: [30, 30],
				//iconAnchor: [15, 28]
				iconAnchor: [7, 7]
		})
		//var mob = L.marker([data.lat,data.lon],{icon:L.icon({iconUrl: iconUrl})}).addTo(map);
		//var mob = L.marker([data.lat,data.lon],{icon:myIcon}).addTo(map);	
		var mob = L.marker([data.lat,data.lon],{icon:myIcon,zIndexOffset:1000});
		mobileMarkerLayer.addLayer(mob);
		mob.bindPopup("<table border=1 class='table table-default'>"+
						"<thead></thead><tbody>"+
						"<tr><td>Name</td><td>"+data.target_name+"</td></tr>"+
						"<tr><td>IMEI</td><td>"+data.imei+"</td></tr>"+
						"<tr><td>IMSI</td><td>"+data.imsi+"</td></tr>"+
						"<tr><td>Type</td><td>"+data.type+"</td></tr>"+
						"<tr><td>TA</td><td>"+data.ta+"</td></tr>"+
						"<tr><td>POWER</td><td>"+data.power+"</td></tr>"+
						"<tr><td>Position</td><td>"+data.lat+","+data.lon+"</td></tr>"+
						"</tbody></table>");
		/*mob.on('mouseover', function(e){
	    mob.openPopup();
		
		});
		mob.on('mouseout', function(e){
		    mob.closePopup();
			
		});*/
		mob.on('dblclick', function(e){
		    mob.openPopup();
			
			});
		 
		mobileMarkers.push(mob);	
	}
	catch(err)
	{
		
	}
	
}

var addTower = function(position,url)
{
	try
	{
		
		var myIcon = L.icon
		({
				iconUrl: url,
				//iconSize: [15, 15],
				iconSize: [30, 30],
				iconAnchor: [15, 28]
				//iconAnchor: [7, 7]
		})
		//var tower_layer_array = towerMarkerLayer.getLayers();
		towerMarkerLayer.clearLayers();
		
		towerMarker = L.marker(position,{icon:myIcon}).addTo(towerMarkerLayer);
		
		var Talayers = taCircleLayers.getLayers();
		
		if(Talayers.length <= 0)
		{
			drawTaCircles();
			map.setZoom(14);
			
			
			
		}
		
		
		try
				{
					//map.panTo(new L.LatLng(path[(path.length)-1][0], path[(path.length)-1][1]));
				map.panTo(position);					
				}
				catch(err)
				{
					
				}
			
		setTaCirclesCenter(position);
	
	}
	catch(err)
	{
		
	}
		
}



var addOprTower = function(position,url,ddd)
{
	try
	{
		
		var myIcon = L.icon
		({
				iconUrl: url,
				//iconSize: [15, 15],
				iconSize: [30, 30],
				iconAnchor: [15, 28]
				//iconAnchor: [7, 7]
		});
		
		
		//var tower_layer_array = towerMarkerLayer.getLayers();
		//opratorTowersLayer.clearLayers();
		
		var aa = L.marker(position,{icon:myIcon}).addTo(opratorTowersLayer);
				aa.bindPopup(ddd);
		/*mob.on('mouseover', function(e){
	    mob.openPopup();
		
		});
		mob.on('mouseout', function(e){
		    mob.closePopup();
			
		});*/
		aa.on('dblclick', function(e){
		    aa.openPopup();
			
			});
		
		
		
		//var Talayers = opratorTowersLayer.getLayers();
		
		/*if(Talayers.length <= 0)
		{
			drawTaCircles();
			map.setZoom(14);
		}*/
		
		
		try
				{
					//map.panTo(new L.LatLng(path[(path.length)-1][0], path[(path.length)-1][1]));
				//map.panTo(position);					
				}
				catch(err)
				{
					
				}
			
		//setTaCirclesCenter(position);
	
	}
	catch(err)
	{
		
	}
		
}




var showhide = function(type,element)
{
	val = $(element).is(':checked');
	switch(type)
	{
		case 3:
			if(!val)
			{
				mobileMarkerLayer.remove();
			}
			else
			{
				mobileMarkerLayer.addTo(map);
			}
			break;
		case 1:
			if(!val)
			{
				towerMarkerLayer.remove();
			}
			else
			{
				towerMarkerLayer.addTo(map);
			}
			break;
		case 2:
			if(!val)
			{
				pathLayer.remove();
			}
			else
			{
				pathLayer.addTo(map);
			}
			break;
		case 4:
			if(!val)
			{
				taCircleLayers.remove();
			}
			else
			{
				taCircleLayers.addTo(map);
			}
			break;
		case 5:
			if(!val)
			{
				$("#detected_mobile_modal").modal("hide");
			}
			else
			{
				$("#detected_mobile_modal").modal("show");
				displayDetectedMobiles(globalCdrData);
				globalModalStatus=true;
			}
			break;
		case 6:
			if(!val)
			{
				$("#coming_cells_modal").modal("hide");
			}
			else
			{
				$("#coming_cells_modal").modal("show");
				displayComingCells(globalComingCellsData);
				globalComingCellsModalStatus=true;
			}
			break;
		case 7:
			if(!val)
			{
				opratorTowersLayer.remove();
			}
			else
			{
				
				opratorTowersLayer.addTo(map);
				displayComingCells(globalComingCellsData);
				globalComingCellsModalStatus=true;
				
			}
			break;
	
	}
}

var displayDetectedMobiles = function(data){
			$("#detected_mobile_modal_body").html("");
			var html ="<table class='table-mob-dtect'>";
			html +="<thead><th></th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th><th>F</th><th>G</th><th>H</th><th>I</th></thead><tbody>";
			var count = 0;
			var totalUeDetected=parseInt(data[data.length-1].count);
			for(var i=0;i<(data.length-1);i++)
			{
				
				//count ++ 
				//var str = data[i].inserttime; 
			   //var res = str.replace(" ", "_").split(".")[0];
			   var name = data[i].mobile_type;
			   var image="user2.png";
			   var backgroundColor="rgb(251, 201, 142)";
			   
			   
				
				/*if(count == 1)
				{
					html +="<tr><td>"+count+"</td>";	
				}
					
				html +='<td><div onclick=\'getdetailsOfModbile(this,"'+data[i].imsi+'","'+data[i].imei+'","'+data[i].ta+'","'+res+'","'+name+'","'+data[i].oprname.replace(" ", "_")+'","'+data[i].country.replace(" ", "_")+'","'+data[i].c_opr.replace(" ", "_")+'","'+data[i].c_count.replace(" ", "_")+'")\' style="cursor:pointer; padding: 5px;border:1px solid black;float:left;margin-left:3px;margin-bottom:3px;"><img  src="../../resources/images/mobile.png" style="width: 20px;height: 25px;display:block;margin: 0 auto;"><strong><p class="mobtxt">'+data[i].imsi+'</p><p class="mobtxt" >'+data[i].imei+'</p><p class="mobtxt">'+(name==""?"&nbsp;":name)+'</p></<strong></div></td>'; 
				
				if(count !=1 && (count%9==0 || count==(data.length)))
				{
					if(count != data.length)
					{
						html +="<tr><td>"+(parseInt(count/9)+1)+"</td>";	
					}
					else
					{
						html +="</tr>";	
					}
				}
				
				
				if((count==1) && (count==(data.length)))
				{
					html +="</tr>";
					break;
					
				}*/

				var taOrPd='PD';
				if (data[i].imsi!=null)
				{ 
				   if (data[i].imei!="000000000000000")
					{
					   if(data[i].stype.toLowerCase()=='2g' || data[i].stype.toLowerCase()=='gsm'){
						   taOrPd='TA';
                           backgroundColor="rgb(198,227,159)";
					}
						if(data[i].mobile_type == "0")
				{
				   name="UNMAPPED";
				   image="user1.png";
					}
							$("#detected_mobile_modal_body").append('<div   style="background-color: '+backgroundColor+';" class="mobile_net" onclick=\'getdetailsOfModbile(this,"'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].realrxl+'","'+data[i].stype+'","'+data[i].imsi+'","'+data[i].imei+'","'+data[i].ta+'","'+data[i].tt.split(".")[0]+'","'+name+'","'+data[i].oprname.replace(" ", "_")+'","'+data[i].country.replace(" ", "_")+'","'+data[i].c_opr.replace(" ", "_")+'","'+data[i].c_count.replace(" ", "_")+'")\' ><div class="serialNoClass"><strong><p class="mobtxta">'+(totalUeDetected--)+'</p></strong></div><img  src="../../resources/images/'+image+'" style="width: 30px;height: 35px;display:block;margin: 8px auto;float: left;"><div class="cellDetailClass"><strong><p class="mobtxtb">'+(name==""?"&nbsp;":name)+'</p></strong><p class="mobtxtc" >'+data[i].imei+'</p><p class="mobtxtc" >'+data[i].imsi+'</p><p class="mobtxtc" >RxL '+data[i].realrxl+','+taOrPd+' '+data[i].ta+',Tech '+data[i].stype+'</p><p class="mobtxtd">'+data[i].oprname.replace(" ", "_")+' :'+data[i].mcc+'-'+data[i].mnc+'</p><p class="mobtxte">'+data[i].tt.split(".")[0]+'</p></div></div>');  
					}
					}
				}
			
			//html+="</tbody></table>";
			
			//$("#detected_mobile_modal .modal-body").append(html);
}

var displayComingCells = function(data){
			$("#coming_cells_modal_body").html("");
			opratorTowersLayer.clearLayers();
			var html ="<table class='table-mob-dtect'>";
			html +="<thead><th></th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th><th>F</th><th>G</th><th>H</th><th>I</th></thead><tbody>";
			var count = 0;
			var tech;
			for(var i in data)
			{
			tech=data[i].packet_type.toLowerCase();
			if(tech=='umts' || tech=='loc_3g'){
				$("#coming_cells_modal_body").append('<div class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].lac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].uarfcn+'","'+data[i].freq+'","'+data[i].psc+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].tt.split(".")[0]+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(198,235,251); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass" src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">UARFCN '+data[i].uarfcn+'</p><p class="mobtxte">PSC '+data[i].psc+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].tt.split(".")[0]+'</p></div></div>');
			}else if(tech=='lte'){
				$("#coming_cells_modal_body").append('<div class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].tac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].earfcn+'","'+data[i].freq+'","'+data[i].pci+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].tt.split(".")[0]+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(251,201,142); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass" src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">EARFCN '+data[i].earfcn+'</p><p class="mobtxte">PCI '+data[i].pci+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].tt.split(".")[0]+'</p></div></div>');		
			}else{
				var bsic=parseInt(data[i].bcc)+(parseInt(data[i].ncc)*8);
				$("#coming_cells_modal_body").append('<div class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].lac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].arfcn+'","'+data[i].freq+'","'+bsic+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].tt.split(".")[0]+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(198,227,159); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass"  src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">ARFCN '+data[i].arfcn+'</p><p class="mobtxte">BSIC '+bsic+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].tt.split(".")[0]+'</p></div></div>');	
			}
				//var ddd = '<div class="opr_net" style="background-color:rgb(198,235,251); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass" src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">Rxl '+data[i].rssi+'</p></div></div>'
				
				var ddd = "<table border=1 class='ll table table-default' style='width: 160px !important;'>"+
						"<thead></thead><tbody>"+
						"<tr><td>Name</td><td>"+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')'+"</td></tr>"+
						"<tr><td>BAND</td><td>"+data[i].band+"</td></tr>"+
						"<tr><td>RxL</td><td>"+data[i].rssi+"</td></tr>"+
						"<tr><td>Tech</td><td style='text-transform: uppercase;'>"+tech+"</td></tr>"+
						"</tbody></table>";
				
				addOprTower([data[i].lat,data[i].lon],'../../resources/images/tower.png',ddd);
				
				
				}
			//html+="</tbody></table>";
			
			//(parseInt(data[i].bcc)+(parseInt(data[i].ncc)*8))
			//$("#detected_mobile_modal .modal-body").append(html);
			
			
			
}

var drawSubscriberPath = function(subscrinberPatharray)
{
	try
	{
		var letters = '0123456789ABCDEF';
		  var color_custom = '#';
		  for (var i = 0; i < 6; i++) {
			  color_custom += letters[Math.floor(Math.random() * 16)];
		  }
                color_custom="black";
		subscriberPath = L.polyline(subscrinberPatharray, {color: color_custom,dashArray: '10,5'});
		//var polyline = L.polyline([[0,0]], {color: 'red'}).addTo(map);
		//lines.push(polyline);
		subscriberPathLayer.addLayer(subscriberPath);
		var ttcount =0;
		for(var i in subscrinberPatharray)
		{
			//var aaaa = L.circle(subscrinberPatharray[i], {radius: 50}).addTo(map);
			var myIcon = L.icon
		({
				iconUrl: (ttcount ==0)?'../../resources/images/green.png':'../../resources/images/blue.png',
				//iconSize: [15, 15],
				iconSize: [12, 12],
				iconAnchor: [7, 7]
				//iconAnchor: [7, 7]
		});
		
                var option = {};
                if(ttcount ==0)
		{
			option = {icon:myIcon,zIndexOffset:999}
		}
		else
		{
			option = {icon:myIcon}

		}
                	
		
		var aaaa = L.marker(subscrinberPatharray[i],option).addTo(subscriberPathCircle);

		ttcount++;

		}
		
		
		//map.flyToBounds([subscrinberPatharray[0]]);	
		map.fitBounds([[subscrinberPatharray[0]],[subscrinberPatharray[subscrinberPatharray.length-1]]]);
                //map.setZoom(17); 

	}
	catch(err)
	{
		
	}
	
}


var drawTaCircles = function()
{
	for(var i=550;i<=2750;i=i+550  )
	{
		taCircleLayers.addLayer(L.circle([28.7041, 77.1025], {radius: i,color:"black",opacity:1,fillColor:"#fff59f",fillOpacity:0.1}));	
	}
}

var setTaCirclesCenter = function(latlng)
{
	var Talayers = taCircleLayers.getLayers();
	for(var i in Talayers)
	{
		Talayers[i].setLatLng(latlng)
	}
}



        $(document).ready(function(){
            //demo.initGoogleMaps();
			loadLeafMap();
			//drawTaCircles();
			getNodesInfo();
			initData();
			$('#detected_mobile_modal').on('hidden.bs.modal', function () 
			{
					
				$("#open_detected_screen").prop( "checked", false );
				globalModalStatus=false;
			});
			$('#mobileDataDetails').on('hidden.bs.modal', function () 
					{
							
						$(".selected_mobile").removeClass("selected_mobile");
					});
			$('#coming_cells_modal').on('hidden.bs.modal', function () 
			{
					
				$("#open_coming_cells_screen").prop( "checked", false );
				globalComingCellsModalStatus=false;
			});
			captureAutoOprEvent();
			$('#ueDisplayListSelect').change(function(){
				getcurrentcdrdata();
			});
			$('#ueSearchButton').click(function(){
				getcurrentcdrdata();
			});
        });
		
$('#toggle_device_tab').click(function () {
    $('#clientsDropDown #tracker_tab').slideToggle('slow', function(){
			console.log($("#tracker_tab").is(":hidden"));
			if($("#tracker_tab").is(":hidden"))
			{
					$("#toggle_device_tab").find('img').attr({"src":"../../resources/images/up_arrow.png"});
			}
			else
			{
					
					$("#toggle_device_tab").find('img').attr({"src":"../../resources/images/down_arrow.png"})
			}
	});
	
  });
  
  
  /******************prsing section of json incase we do not recive the lat long********************************/
  
  /******************prsing section of json incase we do not recive the lat long END********************************/
  
  var checkCheckCounter = function()
  {
	  counterForLoading --;
	  if(counterForLoading == 0)
	  {
		  $("#loading_modal").modal('hide');
	  }
	  
  }
  
  
  var distanceBetweenLatLon = function(lat1, lon1, lat2, lon2) {
	  var p = 0.017453292519943295;    // Math.PI / 180
	  var c = Math.cos;
	  var a = 0.5 - c((lat2 - lat1) * p)/2 + 
	          c(lat1 * p) * c(lat2 * p) * 
	          (1 - c((lon2 - lon1) * p))/2;

	  return ((12742 * Math.asin(Math.sqrt(a)))*1000).toFixed(2); // 2 * R; R = 6371 km
	}
  
  function startTime() {
	    var today = new Date();
	    var dd = today.getDate();
	    var mm = today.getMonth()+1;
	    var yy = today.getFullYear();
	    
	    var h = today.getHours();
	    var m = today.getMinutes();
	    var s = today.getSeconds();
	    m = checkTime(m);
	    s = checkTime(s);
	    document.getElementById('clock_time').innerHTML =
	    dd+"-"+mm+"-"+yy +" "+h + ":" + m + ":" + s;
	    var t = setTimeout(startTime, 500);
	}
	function checkTime(i) {
	    if (i < 10) {i = "0" + i};  // add zero in front of numbers < 10
	    return i;
	}
  

  
    </script>
	<script src="../../resources/lib/contextMenu.min.js" type="text/javascript"></script>
	<script src="../../resources/lib/expand/jquery.collapse.js"></script>
    <script src="../../resources/js/imsiUpdate.js" type="text/javascript"></script>
	<script src="../../resources/js/maps.js" type="text/javascript"></script>
	<!-- <script src="https://unpkg.com/jspdf@latest/dist/jspdf.min.js"></script>
	 <script src="https://github.com/niklasvh/html2canvas/releases/download/0.4.1/html2canvas.js"></script>
	<script src="../../resources/lib/canvas2image.js"></script>-->
	
<jsp:include page='fotter.jsp' />