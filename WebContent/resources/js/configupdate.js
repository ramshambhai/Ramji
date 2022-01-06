var globalGeneratedPacketList={};
//var celllist="";
var globalPacketIdArr=[];
var globalFrequency="";
var globalDuration="";
var globalAllScheduledSubscribers=[];
var mapDataMarkers = [];
var neighbourDataMarkers = [];
var map;
var alreadyrunning="0";
var statusMap =['UNKNOWN','IDLE','USER_BUSY','MS_PURGED','IMSI_DETACHED','NOT_REACHABLE_REASON_RESTRICTED_AREA','NOT_REGISTERED','NOT_PROVIDED_FROM_VLR'];
var globalColorArr=['lightgreen','lightpink'];

var scheduleSearch = function()
{
	var idType = $('input[name="type"]:checked').val();
	var reqType = $("#req_type").val();
	var cmdType = "GET_GEO_LOC_REQ";
	var vlr = "null";
	var hlr = "null";
	var ftn = "null";
	var msc = "null"
	var imsi = $("#type_value").val();
	var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
	scheduleSubscriberSearch(packetData);
}

var scheduleSubscriberSearch = function(packetData){
	packetData.methodName = "scheduleSubscriberSearch";
	packetData.periodicity=""+parseInt($('#schedulerPeriodicity').val())*60*1000;
	packetData.nibIp=$('#nibIp').val();
	packetData.firstDelayRequired="no";
	
	
	$.ajax
	({
			url:"../Operations",
			data:packetData,
			type:'post',
			success:function(data)
			{
			if(data=="successful"){
			alert("Job Scheduled Successfully");
			console.log(data);
			window.location.reload();
			}else{
			alert("Problem in Scheduling job");
			}
			}
	});
}


function sleep(ms) {
	  return new Promise(resolve => setTimeout(resolve, ms));
	}

	async function sleepforSomeTime(time) {

	      await sleep(time);
	    
	}

var createServerPacketData = function(cmdType,idType,reqType,vlr,hlr,ftn,imsi)
{
	var data =
	{
		"cmdType":cmdType,
		"reqType":reqType,
		"idType":idType,
		"type_value":imsi,
		"vlr":vlr,
		"hlr":hlr,
		"ftn":ftn,
		"msc":ftn,
		"fileName":"getGeoLoc.json"
	};
	return data;
	
}

var captureAutoStateEvent = function()
{
	ws4 = new WebSocket("ws://"+ipAddress+":8080/locator/autostate");
	var color="#FFF";
	ws4.onopen = function()
	{
		console.log("connected to the device status server");
	}
	ws4.onmessage = function(event)
	{
		var msg = event.data;
		console.log(msg);
		if(msg=="start"){
		    alert("Started Successfully");
			$('#startConfig').prop("disabled",true);
			$('#stopConfig').prop("disabled",false);
			$('#generatePacket').prop("disabled",true);
			$('#startConfig').css("background-color","grey");
			$('#stopConfig').css("background-color","#337ab7");
			$('#generatePacket').css("background-color","grey");
			setSelectedConfigOnDb();
		}
		if(msg=="stop"){
			alert('Stopped Successfully');
			$('#startConfig').prop("disabled",false);
			$('#stopConfig').prop("disabled",true);
			$('#generatePacket').prop("disabled",false);
			$('#startConfig').css("background-color","#337ab7");
			$('#stopConfig').css("background-color","grey");
			$('#generatePacket').css("background-color","#337ab7");
		}
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

$(document).ready(function(){

	getCurrentConfigData();
	$('#generatePacket').click(function(){
		getGeneratedPackets();
	});
	$('#startConfig').click(function(){
		startConfig();
	});
	$('#stopConfig').click(function(){
		stopConfig();
	});
	//getAllDistinctPlmn();
	//getAllCountryList();
	//getAllScheduledSubscribers();
	captureAutoStateEvent();
	registerEvents();
	//initMap();
	//getMapData();
	getCurrentScannedOpr();
	getAntennaProfileForAddPlmn();
});

var getAntennaProfileForAddPlmn = function(){
	$.ajax
	({
			url:"../../service/common/getAntennaProfiles",
			type:'post',
			success:function(data)
			{
				$('#addAllAntenna').html();
				var rowsAntenna='';
				for(var i=0;i<data.length;i++){
					if(data[i].intracking=='t'){
						rowsAntenna+='<option value='+data[i].id+'>'+data[i].profile_name+'</option>';
					}
				}
				$('#addAllAntenna').html(rowsAntenna);
			}
	});
}

function getCurrentScannedOpr(){
	$.ajax({
		url:"../../service/common/getcurrentscannedopr",
		type:'get',
		success:function(data)
		{
			$('#allOperatorsSelect').html('');
			var options='';
			for(var i=0;i<data.length;i++){
				//options+='<option val="'+data[i].opr+'">'+data[i].opr+'</option>';
				options+='<option value="'+data[i].opr+'">'+data[i].opr+'</option>';
			}
			$('#allOperatorsSelect').append(options);
		}
	});
}


function getTechAndCellsFxn(){
	var currentScannedOprArr=$('#allOperatorsSelect').val();
	if(currentScannedOprArr==null){
     alert("Please select Operator");
	 return false;
	}	
	var addAllAntenna = $('#addAllAntenna').val();
	if(addAllAntenna==null){
     alert("Please select Antenna");
	 return false;
	}
	var currentScannedOprStr=currentScannedOprArr.join(",");
	var addAllAntennaStr=addAllAntenna.join(",");
	$('#generatedPackets').html('');
	
		
	$.ajax({
		url:"../../service/common/getTechAndCells",
		data:{oprList:currentScannedOprStr,"antennaList":addAllAntennaStr},
		type:'post',
		success:function(data)
		{
			$('#TechAndCellsSelect').html('');
			var options='';
			options+='<option value="All">All</option>';
			for(var i=0;i<data.length;i++){
				var bsci=data[i].bcc+""+((data[i].ncc)*8);
				if(data[i].bcc==null||data[i].bcc==""||data[i].ncc==null||data[i].ncc==""){
					bsci="";
				}
				//options+='<option value="'+data[i].packet_type+'-'+data[i].cell+'">'+data[i].packet_type+'-'+data[i].cell+'</option>';
				options+='<option value="'+data[i].packet_type+'-'+data[i].cell+'">'+data[i].opr+'('+data[i].mcc + data[i].mnc+') ' +data[i].packet_type+'  LAC/TAC:'+data[i].lac+data[i].tac+' E/U/ARFCN:'+data[i].arfcn+data[i].uarfcn+data[i].earfcn+' Cell:'+data[i].cell+' BSIC/PSC/PCI:'+bsci+data[i].psc+data[i].pci+'</option>';
			
			}
			$('#TechAndCellsSelect').append(options);
		}
	});
}

function getCurrentConfigData(){
	$.ajax({
		url:"../../service/common/getcurrentconfigdata",
		type:'get',
		success:function(data)
		{
		var currentPlmn='';
		var currentColorIndex=-1;
		if(data[0].status=="0"){
			$('#startConfig').prop("disabled",false);
			$('#stopConfig').prop("disabled",true);
			$('#startConfig').css("background-color","#337ab7");
			$('#stopConfig').css("background-color","grey");
		}else if(data[0].status=="1"){
			$('#startConfig').prop("disabled",true);
			$('#stopConfig').prop("disabled",false);
			$('#generatePacket').prop("disabled",true);
			$('#startConfig').css("background-color","grey");
			$('#stopConfig').css("background-color","#337ab7");
			$('#generatePacket').css("background-color","grey");
			$('#operations_table_tbody').css("display","table-row-group");
			var generatedPacketList=JSON.parse(data[0].config_data);
			$('#frequency').val(data[0].frequency);
			$('#duration').val(data[0].duration);
			globalGeneratedPacketList=generatedPacketList;
			var generatedPacketArray=generatedPacketList.config_data;
			var packetIdArr=data[0].ids.split(",");
			var rows='';
			var id=1;
		if(generatedPacketArray.length>0){
		
			//$("#req_tab_bottom_thead").find("tr:gt(0)").remove();
		//$('#req_tab_bottom_thead').append('<tr><th style="display: none;"></th><th style="text-align:center;">SNo.</th><th style="text-align:center;">TECH</th><th style="text-align:center;">PLMN(OPR)</th><th style="text-align:center;">Antenna</th><th style="text-align:center;">Band</th><th style="text-align:center;">LAC</th><th style="text-align:center;">CELL</th><th style="text-align:center;">ARFCN</th><th style="text-align:center;">UARFCN</th><th style="text-align:center;">BSIC</th><th style="text-align:center;">PSC</th><th style="text-align:center;">RSSI</th><th style="text-align:center;">Neighbours</th><th style="text-align:center;"><label>All</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="allCellSelect" onclick="selectOrUnselectPackets()" /></th></tr>');
			//$('#req_tab_bottom_thead').append('<tr><th style="display: none;"></th><th style="text-align:center;">SNo.</th><th style="text-align:center;">TECH</th><th style="text-align:center;">PLMN(OPR)</th><th style="text-align:center;">Antenna</th><th style="text-align:center;">Band</th><th style="text-align:center;">LAC</th><th style="text-align:center;">TAC</th><th style="text-align:center;">CELL</th><th style="text-align:center;">ARFCN</th><th style="text-align:center;">BSIC</th><th style="text-align:center;">UARFCN</th><th style="text-align:center;">PSC</th><th style="text-align:center;">EARFCN</th><th style="text-align:center;">PCI</th><th style="text-align:center;">RSSI</th><th style="text-align:center;">Neighbours</th><th style="text-align:center;"><label>All</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="allCellSelect" onclick="selectOrUnselectPackets()" /></th></tr>');
			
			$('#req_tab_bottom_thead').append('<tr><th style="display: none;"></th><th style="text-align:center;">SNo.</th><th style="text-align:center;">TECH</th><th style="text-align:center;">PLMN(OPR)</th><th style="text-align:center;">Antenna</th><th style="text-align:center;">Band</th><th style="text-align:center;">LAC/TAC</th><th style="text-align:center;">CELL</th><th style="text-align:center;">E/U/ARFCN</th><th style="text-align:center;">BSIC/PSC/PCI</th><th style="text-align:center;">RSSI</th><th style="text-align:center;"><label>All</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="allCellSelect" onclick="selectOrUnselectPackets()" /></th></tr>');
	var color='';
	for(var i=0;i<generatedPacketArray.length;i++){
		var singlePacketArray=generatedPacketArray[i].data;
		//var packetId=generatedPacketArray[i].id;
		var packetId=id++;
		//if(generatedPacketArray[i].tech.toLowerCase()=='2g')
		if(true)
		{
		
			var neighbours='';
		
		for(var j=0;j<singlePacketArray.length;j++){
			if(singlePacketArray[j].flag=="self"){
			var packetMatchCount=0;
			for(var packetCount=0;packetCount<packetIdArr.length;packetCount++){
			if(packetIdArr[packetCount]==generatedPacketArray[i].id){
			packetMatchCount=1;
			}
			}
			if(currentPlmn!=singlePacketArray[j].plmn){
				currentPlmn=singlePacketArray[j].plmn;
				color=getRandomColor();
				}
			if(packetMatchCount==1){
				//$('#generatedPackets').append('<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td>'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+'</td><td>'+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+'</td><td>'+singlePacketArray[j].bsic+'</td><td>'+singlePacketArray[j].uarfcn+'</td><td>'+singlePacketArray[j].psc+'</td><td>'+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].pci+'</td><td>'+singlePacketArray[j].rssi+'</td><td><button class="btn btn-default" style="height: 15px;" onclick="showNeighbours(\''+generatedPacketArray[i].id+'\')"><div class="showNeighbourClass">show</div></button></td><td><input type="checkbox" id="packetCheck'+packetId+'" checked /></td></tr>');
			

			if (generatedPacketArray[i].check=="yes")
				$('#generatedPackets').append('<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td>'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+singlePacketArray[j].earfcn+singlePacketArray[j].uarfcn+'</td><td>'+singlePacketArray[j].bsic+singlePacketArray[j].psc+singlePacketArray[j].pci+'</td><td>'+singlePacketArray[j].rssi+'</td><td><input type="checkbox" id="packetCheck'+packetId+'" checked /></td></tr>');
			else
				$('#generatedPackets').append('<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td>'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+singlePacketArray[j].earfcn+singlePacketArray[j].uarfcn+'</td><td>'+singlePacketArray[j].bsic+singlePacketArray[j].psc+singlePacketArray[j].pci+'</td><td>NR</td><td><input type="checkbox" id="packetCheck'+packetId+'" checked /></td></tr>');

	
			}else{
				//$('#generatedPackets').append('<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td>'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+'</td><td>'+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+'</td><td>'+singlePacketArray[j].bsic+'</td><td>'+singlePacketArray[j].uarfcn+'</td><td>'+singlePacketArray[j].psc+'</td><td>'+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].pci+'</td><td>'+singlePacketArray[j].rssi+'</td><td><button class="btn btn-default" style="height: 15px;" onclick="showNeighbours(\''+generatedPacketArray[i].id+'\')"><div class="showNeighbourClass">show</div></button></td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>');
				//$('#generatedPackets').append('<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td>'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+'</td><td>'+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+'</td><td>'+singlePacketArray[j].bsic+'</td><td>'+singlePacketArray[j].uarfcn+'</td><td>'+singlePacketArray[j].psc+'</td><td>'+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].pci+'</td><td>'+singlePacketArray[j].rssi+'</td><td><div class="showNeighbourClass">show</div></button></td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>');
			if (generatedPacketArray[i].check=="yes")
				$('#generatedPackets').append('<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td>'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+singlePacketArray[j].uarfcn+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].bsic+singlePacketArray[j].psc+singlePacketArray[j].pci+'</td><td>'+singlePacketArray[j].rssi+'</td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>');
else
				$('#generatedPackets').append('<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td>'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+singlePacketArray[j].uarfcn+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].bsic+singlePacketArray[j].psc+singlePacketArray[j].pci+'</td><td>NR</td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>');

			}
			//<td id="packetNeighbours'+packetId+'"></td>
			}
			/*else{
			neighbours+='{';
			neighbours+=singlePacketArray[j].plmn+','+singlePacketArray[j].lac+','+singlePacketArray[j].cell+','+singlePacketArray[j].arfcn+','+singlePacketArray[j].bsic+','+singlePacketArray[j].rssi;
			neighbours+='}';
			}*/
		}
		//$('#packetNeighbours'+packetId).text(neighbours);
		}
	}
	}	
		}
		}
	});
}

function setColorIndex(currentColorIndex){
	if((globalColorArr.length-1)>=(currentColorIndex+1)){
		return ++currentColorIndex;
	}else{
		return 0;
	}
}

function selectOrUnselectPackets(){
	var count=0;
	if($('#allCellSelect').prop('checked')){
		$('#generatedPackets').find('tr').each(function(){
		$('#packetCheck'+(++count)).prop("checked",true);
	});
	}else{
		$('#generatedPackets').find('tr').each(function(){
		$('#packetCheck'+(++count)).prop("checked",false);
	});
	}
}

function startConfig(){
/*	var manualCheck=$('#manual_check').prop("checked");
	if(!manualCheck){
		alert("Manual Override is not selected.");
		return false;
	}*/
	var selectCount=0;
	var count=0;
	var packetIdArr=[];
	var packetId=-1;
	$('#generatedPackets:first tr').each(function(){
		count++;
		packetId=$(this).find('td:first-child').text();
		if($('#packetCheck'+packetId).prop("checked")){
			packetIdArr.push(packetId);
			selectCount++;
		}
		/*if($('#packetCheck'+count).prop("checked")){
packetIdArr.push($('#packetId'+count).text());
selectCount++;
}*/
	});
	var frequency=$('#frequency').val();
	if(frequency==""){
		alert("Please select the Repitition Frequency");
		return false;
	}
	if(selectCount===0){
		alert("Please select atleast 1 packet");
		return false;
	}
	var duration=$('#duration').val();
	if(duration==""){
		alert("Please select the Configuration Duration");
		return false;
	}
	else if(duration>=901 || duration<=29){
		alert("Cell Active Time(sec) range is 30 second to 900 second, please check value entered");
		return false;
		}
		
		if (alreadyrunning == 1) 
		{
			alert ("Operation already in progress please wait...")
			return false;
		}
		alreadyrunning=1;


	var finalSelecetedPacketObj={};
	finalSelecetedPacketObj.repitition_freq=frequency;
	finalSelecetedPacketObj.duration=duration;
	var packetDataArr=[];
	var generatedPacketListData=globalGeneratedPacketList.config_data;
	globalPacketIdArr=packetIdArr;
	globalFrequency=frequency;
	globalDuration=duration;
	for(var i=0;i<packetIdArr.length;i++){
		for(var j=0;j<generatedPacketListData.length;j++){
			if(packetIdArr[i]==generatedPacketListData[j].id){
				generatedPacketListData[j].duration=duration;
				packetDataArr.push(generatedPacketListData[j]);
			}
		}
	}
	finalSelecetedPacketObj.config_data=packetDataArr;
	var finalSelecetedPacketJson=JSON.stringify(finalSelecetedPacketObj);
	//sleep added to prevent this function being called multiple times with milliseconds gap with faulty mouse
	
	console.log("Yo");
	sleepforSomeTime(2000);
	console.log("Yo2");
	

	$.ajax({
		url:"../../service/common/startconfigondevices",
		data:{configData:finalSelecetedPacketJson},
		type:'post',
		success:function(data){
		if(data=="SUCCESS"){
			alreadyrunning=0;
		 }else{
			 var dataArr=data.split("&");
			 if(dataArr.length==1){
				alert("Problem in starting");
				alreadyrunning=0;
			}else{
				alert(dataArr[1]);
				alreadyrunning=0;
			}
		 
		}
		
		}
	});
}

function setSelectedConfigOnDb(){
	$.ajax({
		url:"../../service/common/setselectedconfigondb",
		data:{fullConfigData:JSON.stringify(globalGeneratedPacketList),packetIds:globalPacketIdArr.join(','),repititionFreq:globalFrequency,cellActiveTime:globalDuration},
		type:'post',
		success:function(data)
		{
		}
	});
}

function stopConfig(){
	$('#stopConfig').prop("disabled",true);
	$('#stopConfig').css("background-color","grey");
	$.ajax({
		url:"../../service/common/stopconfigondevices",
		type:'post',
		success:function(data)
		{
			//alert('Stopped Successfully');
			$('#startConfig').prop("disabled",false);
			$('#stopConfig').prop("disabled",true);
			$('#generatePacket').prop("disabled",false);
			$('#startConfig').css("background-color","#337ab7");
			$('#stopConfig').css("background-color","grey");
			$('#generatePacket').css("background-color","#337ab7");
		}
	});
}

function getGeneratedPackets(){
	var currentScannedOprArr=$('#allOperatorsSelect').val();
	var celllist=$('#TechAndCellsSelect').val();
	
	if(celllist!=null)
	{
		var n= celllist.includes("All");
		if(n){
			celllist="";
		}
		else{
			celllist=celllist.join(",");
			//CellListArray.forEach(myFunction);
		}
	
	}
	else{
		celllist="";
	}
	
	
	
	if(currentScannedOprArr==null){
     alert("Please select Operator");
	 return false;
	}	
	var addAllAntenna = $('#addAllAntenna').val();
	if(addAllAntenna==null){
     alert("Please select Antenna");
	 return false;
	}
	var currentScannedOprStr=currentScannedOprArr.join(",");
	var addAllAntennaStr=addAllAntenna.join(",");
	$('#generatedPackets').html('');
	$.ajax({
		url:"../../service/common/genrateConfiguration",
		data:{oprList:currentScannedOprStr,"antennaList":addAllAntennaStr,"celllist":celllist},
		type:'post',
		success:function(data)
		{
            if(data.config_data.length>0){
			var generatedPacketList=data;
			globalGeneratedPacketList=generatedPacketList;
			displayGeneratedPacketList(generatedPacketList);
			}else{
             alert("No Scanned Cells Found");
             document.getElementById("loading_screen").style.display = "none";
			}
		}
	});
	//displayGeneratedPacketList('{}');
}

function displayGeneratedPacketList(generatedPacketList){
	document.getElementById("loading_screen").style.display = "none";
	$('#generatedPackets').html('');
	var displayList=generatedPacketList;
	var currentPlmn='';
	var currentColorIndex=-1;
	/*var displayList='{"repitition_freq":"3","config_data":[{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"1","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"2","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"3","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"4","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"5","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"6","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"7","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"8","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"9","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"10","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"11","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"12","duration":"3","tech":"2G"},{"data":[{"flag":"self","plmn":"40411","lac":"1","cell":"1","arfcn":"22","bsic":"23","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40410","lac":"1","cell":"1","arfcn":"22","bsic":"24","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40511","lac":"3","cell":"201","arfcn":"22","bsic":"27","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"4","cell":"202","arfcn":"22","bsic":"25","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40510","lac":"5","cell":"203","arfcn":"22","bsic":"26","uarfcn":"","psc":"","rssi":"-80"},{"flag":"neighbour","plmn":"40611","lac":"6","cell":"204","arfcn":"22","bsic":"28","uarfcn":"","psc":"","rssi":"-80"}],"id":"13","duration":"3","tech":"2G"}]}';*/
	var colorCount=1;
	var nonHighlightedPackets='';
	var highlightedPackets='';
	generatedPacketList=displayList;
	globalGeneratedPacketList=generatedPacketList;
	var generatedPacketArray=generatedPacketList.config_data;
	var rows='';
	var id=1;
	var packetCount=0;
	if(generatedPacketArray.length>0){
		$("#req_tab_bottom_thead").find("tr:gt(0)").remove();
		//Sanjay and Vaibhav 4 Jan 2021 3 PM
		//$('#req_tab_bottom_thead').append('<tr><th style="display: none;"></th><th style="text-align:center;">SNo.</th></th><th style="text-align:center;">TECH</th><th style="text-align:center;">PLMN(OPR)</th><th style="text-align:center;">Antenna</th><th style="text-align:center;">Band</th><th style="text-align:center;">LAC</th><th style="text-align:center;">TAC</th><th style="text-align:center;">CELL</th><th style="text-align:center;">ARFCN</th><th style="text-align:center;">BSIC</th><th style="text-align:center;">UARFCN</th><th style="text-align:center;">PSC</th><th style="text-align:center;">EARFCN</th><th style="text-align:center;">PCI</th><th style="text-align:center;">RSSI</th><th style="text-align:center;">Neighbours</th><th style="text-align:center;"><label>All</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="allCellSelect" onclick="selectOrUnselectPackets()" /></th></tr>');
		$('#req_tab_bottom_thead').append('<tr><th style="display: none;"></th><th style="text-align:center;">SNo.</th></th><th style="text-align:center;">TECH</th><th style="text-align:center;">PLMN(OPR)</th><th style="text-align:center;">Antenna</th><th style="text-align:center;">Band</th><th style="text-align:center;">LAC/TAC</th><th style="text-align:center;">CELL</th><th style="text-align:center;">E/U/ARFCN</th><th style="text-align:center;">BSIC/PSC/PCI</th><th style="text-align:center;">RSSI</th><th style="text-align:center;"><label>All</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="allCellSelect" onclick="selectOrUnselectPackets()" /></th></tr>');
	var color='';
	for(var i=0;i<generatedPacketArray.length;i++){
		var singlePacketArray=generatedPacketArray[i].data;
		var packetId=id++;
		//if(generatedPacketArray[i].tech.toLowerCase()=='2g')
		var neighbours='';
		for(var j=0;j<singlePacketArray.length;j++){
			if(singlePacketArray[j].flag=="self"){
			if(currentPlmn!=singlePacketArray[j].plmn){
				currentPlmn=singlePacketArray[j].plmn;
				color=getRandomColor();
				$('#generatedPackets').append(nonHighlightedPackets);
				$('#generatedPackets').append(highlightedPackets);
				$('.classPacket'+colorCount+'b').each(function(){
					$(this).text(++packetCount);
				});
				$('.classPacket'+colorCount+'a').each(function(){
					$(this).text(++packetCount);
				});
				colorCount++;
				nonHighlightedPackets='';
				highlightedPackets='';
			}
			if(generatedPacketArray[i].check=="yes"){

				//Sanjay and Vaibhav 4 Jan 2021 3 PM
				//highlightedPackets+='<tr class="highlightPacket" id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td class="classPacket'+colorCount+'a">'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+'</td><td>'+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+'</td><td>'+singlePacketArray[j].bsic+'</td><td>'+singlePacketArray[j].uarfcn+'</td><td>'+singlePacketArray[j].psc+'</td><td>'+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].pci+'</td><td>'+singlePacketArray[j].rssi+'</td><td><button class="btn btn-default" style="height: 15px;" onclick="showNeighbours(\''+generatedPacketArray[i].id+'\')"><div class="showNeighbourClass">show</div></button></td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>';
					highlightedPackets+='<tr class="highlightPacket" id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td class="classPacket'+colorCount+'a">'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+singlePacketArray[j].uarfcn+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].bsic+singlePacketArray[j].psc+singlePacketArray[j].pci+'</td><td>'+singlePacketArray[j].rssi+'</td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>';
			
			}else{
				//Sanjay and Vaibhav 4 Jan 2021 3 PM
				//nonHighlightedPackets+='<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td class="classPacket'+colorCount+'b">'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+'</td><td>'+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+'</td><td>'+singlePacketArray[j].bsic+'</td><td>'+singlePacketArray[j].uarfcn+'</td><td>'+singlePacketArray[j].psc+'</td><td>'+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].pci+'</td><td>'+singlePacketArray[j].rssi+'</td><td><button class="btn btn-default" style="height: 15px;" onclick="showNeighbours(\''+generatedPacketArray[i].id+'\')"><div class="showNeighbourClass">show</div></button></td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>';
			//	nonHighlightedPackets+='<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td class="classPacket'+colorCount+'b">'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+'</td><td>'+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+'</td><td>'+singlePacketArray[j].bsic+'</td><td>'+singlePacketArray[j].uarfcn+'</td><td>'+singlePacketArray[j].psc+'</td><td>'+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].pci+'</td><td>NR</td><td><button class="btn btn-default" style="height: 15px;" onclick="showNeighbours(\''+generatedPacketArray[i].id+'\')"><div class="showNeighbourClass">show</div></button></td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>';
				nonHighlightedPackets+='<tr id="packetRow'+packetId+'" style="background-color: '+color+';"><td id="packetId'+packetId+'" style="display: none;">'+generatedPacketArray[i].id+'</td><td class="classPacket'+colorCount+'b">'+packetId+'</td><td>'+generatedPacketArray[i].tech+'</td><td>'+singlePacketArray[j].plmn+'('+singlePacketArray[j].opr+')</td><td>'+generatedPacketArray[i].antennaName+'</td><td>'+singlePacketArray[j].band+'</td><td>'+singlePacketArray[j].lac+singlePacketArray[j].tac+'</td><td>'+singlePacketArray[j].cell+'</td><td>'+singlePacketArray[j].arfcn+singlePacketArray[j].uarfcn+singlePacketArray[j].earfcn+'</td><td>'+singlePacketArray[j].bsic+singlePacketArray[j].psc+singlePacketArray[j].pci+'</td><td>NR</td><td><input type="checkbox" id="packetCheck'+packetId+'" /></td></tr>';
			}
			}
		}
	}
	$('#generatedPackets').append(nonHighlightedPackets);
	$('#generatedPackets').append(highlightedPackets);
	$('.classPacket'+colorCount+'b').each(function(){
		$(this).text(++packetCount);
		});
	$('.classPacket'+colorCount+'a').each(function(){
		$(this).text(++packetCount);
		});
	}
	$('#operations_table_tbody').css("display","table-row-group");
}

function showNeighbourCells(){
alert('roots');
}

function getAllCountryList()
{
	

	$.ajax({
		url:"../Operations",
		data:{"methodName":"getAllCountryList"},
		type:'post',
		success:function(data)
		{
			updateAllCountryList(jQuery.parseJSON(data));
			
		}
	});
}

var updateAllCountryList = function(data)
{
	$('#country').html('');
	var options='';
	options+='<option value="select">select country</option>';
	for(var i in data)
	{
		options+='<option value="'+data[i]+'">'+data[i]+'</option>';
	}
}

var registerEvents = function()
{
			
			$('#search').click(function()
			{		
				if($("#type_value").val().length  ==0 || isNaN($("#type_value").val()))
				{
					alert("Please provide valid value");
					return;
				}
				var idType = $('input[name="type"]:checked').val();
				if(idType == 2  && $("#type_value").val().length != 12)
				{
					alert("Please provide valid MSISDN");
					return;
				}
				if(idType == 1  && $("#type_value").val().length != 15)
				{
					alert("Please provide valid IMSI");
					return;
				}
				if($('#nibIp').val()=="select"){
				alert("Please select NIB IP");
				return;
				}
				if($('#schedulerPeriodicity').val()==""){
                alert("Please provide periodicity");
				return;				
				}
				if(isNaN($(this).val())){
                alert("Please provide valid periodicity");
				return;
				}
                if(parseFloat($(this).val())==0){
				alert("Periodicity cann't be zero");
				return;
				}
				for(var i=0;i<globalAllScheduledSubscribers.length;i++){
				if(globalAllScheduledSubscribers[i].type_value==$("#type_value").val()){
				alert('Job of this subscriber is already scheduled.');
				return;
				}
				}
				scheduleSearch();
			});
			
			$(".type_check").click(function(){
				if($(this).val() ==2)
				{
					$("#type_value").attr({"placeholder":"MSISDN"});
				}
				else if($(this).val() ==1)
				{
					$("#type_value").attr({"placeholder":"IMSI"});
				}
			});
}



//**********************************************************************************************************

function sortTable(table_id, sortColumn){
    var tableData = document.getElementById(table_id).getElementsByTagName('tbody').item(0);
    var rowData = tableData.getElementsByTagName('tr');            
    for(var i = 0; i < rowData.length - 1; i++){
        for(var j = 0; j < rowData.length - (i + 1); j++){
            console.log(parseInt(rowData.item(j).getElementsByTagName('td').item(sortColumn).innerHTML.replace(/[^0-9\.]+/g, "")) < parseInt(rowData.item(j+1).getElementsByTagName('td').item(sortColumn).innerHTML.replace(/[^0-9\.]+/g, "")));
        	if(parseInt(rowData.item(j).getElementsByTagName('td').item(sortColumn).innerHTML.replace(/[^0-9\.]+/g, "")) < parseInt(rowData.item(j+1).getElementsByTagName('td').item(sortColumn).innerHTML.replace(/[^0-9\.]+/g, ""))){
                tableData.insertBefore(rowData.item(j+1),rowData.item(j));
            }
        }
    }
}

function getAllScheduledSubscribers(){
	$.ajax
	({
			url:"../Operations",
			data:{"methodName":"getAllScheduledSubscribers"},
			type:'post',
			async:false,
			success:function(data)
			{
			if(data.length!=0){
			var displayData=JSON.parse(data);
			globalAllScheduledSubscribers=displayData;
			displayAllScheduledSubscribers(displayData);
			}
			}
	});
	}
	
	function displayAllScheduledSubscribers(data){
	$('#scheduledSubscribers').html('');
	var scheduledSubscriberRow='';
	for(var i=0;i<data.length;i++){
	if(data[i].status=='stop'){
	scheduledSubscriberRow+='<tr><td>'+data[i].type_value+'</td><td><input type="text" id="changePeriodicity'+data[i].id+'" value="'+(parseFloat(data[i].periodicity))/60000+'"></input></td><td style="background-color: red;" id="status'+data[i].id+'">'+data[i].status+'</td><td><span><select style="width: 30%;" id="select'+data[i].id+'"><option value="select">select</option><option value="start">Start</option><option value="changePeriodicity">Change Periodicity</option><option value="delete">Delete</option></select></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn-match" onclick="performOperation('+data[i].id+')">Perform</button>&nbsp;&nbsp;<button class="btn-match" onclick="performTracking('+data[i].type_value+')">Track</button></td></tr>';
	}else{
	scheduledSubscriberRow+='<tr><td>'+data[i].type_value+'</td><td><input type="text" id="changePeriodicity'+data[i].id+'" value="'+(parseFloat(data[i].periodicity))/60000+'"></input></td><td style="background-color:green;" id="status'+data[i].id+'">'+data[i].status+'</td><td><span><select style="width: 30%;" id="select'+data[i].id+'"><option value="select">select</option><option value="stop">Stop</option><option value="changePeriodicity">Change Periodicity</option><option value="delete">Delete</option></select></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn-match" onclick="performOperation('+data[i].id+')">Peform</button>&nbsp;&nbsp;<button class="btn-match" onclick="performTracking('+data[i].type_value+')">Track</button></td></tr>';
	}
	}
	$('#scheduledSubscribers').html(scheduledSubscriberRow);
}

function performTracking(type_value){
window.location.href="geoIndex.jsp?value="+type_value;
}

function performOperation(id){
var operationName=$('#select'+id).val();
if(operationName=='select'){
alert('Please perform valid operation');
return;
}else if(operationName=="start"){
	$.ajax
	({
			url:"../Operations",
			data:{"methodName":"startScheduledTask","id":id,"periodicity":parseFloat($('#changePeriodicity'+id).val())*60000},
			type:'post',
			async:false,
			success:function(data)
			{
			if(data=="successful"){
			$("#status"+id).text("start");
			$("#status"+id).css("background-color","green");
			var options='<option value="select">select</option><option value="stop">Stop</option><option value="changePeriodicity">Change Periodicity</option><option value="delete">Delete</option>';
			$("#select"+id).html(options);
			alert("Job started successfully");
			}else{
			alert("Problem in starting job");
			}
			}
	});
}else if(operationName=="stop"){
	$.ajax
	({
			url:"../Operations",
			data:{"methodName":"stopScheduledTask","id":id},
			type:'post',
			async:false,
			success:function(data)
			{
			if(data=="successful"){
			$("#status"+id).text("stop");
			$("#status"+id).css("background-color","red");
			var options='<option value="select">select</option><option value="start">Start</option><option value="changePeriodicity">Change Periodicity</option><option value="delete">Delete</option>';
			$("#select"+id).html(options);
			alert("Job stopped Successfully");
			}else{
			alert("Problem in stopping job");
			}
			}
	});
}else if(operationName=="changePeriodicity"){
	$.ajax
	({
			url:"../Operations",
			data:{"methodName":"changePeriodicityOfScheduledTask","id":id,"periodicity":parseFloat($('#changePeriodicity'+id).val())*60000,status:$('#status'+id).text()},
			type:'post',
			async:false,
			success:function(data)
			{
				if(data=="successful"){
				alert("Periodicity changed successfully");
				}else{
					alert("problem in changing periodicity");
				}
			}
	});
}else{
	$.ajax
	({
			url:"../Operations",
			data:{"methodName":"deleteScheduledTask","id":id,status:$('#status'+id).text()},
			type:'post',
			async:false,
			success:function(data)
			{
			if(data=="successful"){
			alert("Job deleted successfully");
			window.location.reload();
			}else{
			alert("Problem in deleting job");
			}
			}
	});
}
}

function showNeighbours(packetId){
var generatedPacketListData=globalGeneratedPacketList.config_data;
for(var i=0;i<generatedPacketListData.length;i++){
if(packetId==generatedPacketListData[i].id){
var tempData=generatedPacketListData[i].data;
$('#neighbourTableTbody').html('');
var count=0;
for(var j=0;j<tempData.length;j++){
if(tempData[j].flag!="self"){
var tech='2G';
if(tempData[j].uarfcn!="")
	tech='3G';
$('#neighbourTableTbody').append('<tr><td>'+(++count)+'</td><td>'+tech+'</td><td>'+tempData[j].plmn+'</td><td>'+tempData[j].lac+'</td><td>'+tempData[j].cell+'</td><td>'+tempData[j].arfcn+'</td><td>'+tempData[j].uarfcn+'</td><td>'+tempData[j].bsic+'</td><td>'+tempData[j].psc+'</td><td>'+tempData[j].rssi+'</td></tr>');
}
}
}
}
$('#showNeighbours').modal('show');
}

function getRandomColor() {
                var letters = 'BCDEF'.split('');
                var color = '#';
                for (var i = 0; i < 6; i++ ) {
                    color += letters[Math.floor(Math.random() * letters.length)];
                }
                return color;
            }

//Function to show loading screen 	  
$(document).ready(function(){
	  $("#generatePacket").click(function(){
	  var addAllAntenna = $('#addAllAntenna').val();
	  if(addAllAntenna!=null){
		  $("#loading_screen").css("display" , "block");
	  }
	});
	  
	  
	  $("#allOperatorsSelect").click(function(){
		    
			document.getElementById("TechAndCellsSelect").options.length = 0;
		  });
	  
	  
});

