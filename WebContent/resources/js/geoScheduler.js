var globalAllScheduledSubscribers=[];
var mapDataMarkers = [];
var neighbourDataMarkers = [];
var map;
var statusMap =['UNKNOWN','IDLE','USER_BUSY','MS_PURGED','IMSI_DETACHED','NOT_REACHABLE_REASON_RESTRICTED_AREA','NOT_REGISTERED','NOT_PROVIDED_FROM_VLR'];

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

$(document).ready(function(){
	getNibs();
	getAllScheduledSubscribers();
	registerEvents();
	//initMap();
	//getMapData();
	
	
	
	/***************************************************/
	
});

function getNibs()
{
	
	data={"methodName":"getAllNibs"};
	$.ajax({
		url:"../Operations",
		data:data,
		type:'post',
		success:function(data)
		{
			updateNib(jQuery.parseJSON(data));
			
		}
	});
}

var updateNib = function(data)
{
	$('#nibIp').html('');
	$('#nibIp').append('<option value="select">select Nib</option>');
	for(var i in data)
	{
		$('#nibIp').append('<option value="'+data[i]+'">'+data[i]+'</option>');
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