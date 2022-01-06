var dirPath ="../../";

var COLOR = {
             	'run':"#4caf50",
             	'wait':"#e91e63",
             	'nor':"#ff9800",
             	'down':"#f44336"
			};
updateStatusOfAllBts = function()
{	
		$.ajax({
			url:dirPath+"service/3g/updatestatusofallbts",
			async:false,
			type:'GET',
			success:function(data)
			{
				
			}
		});
}

//updateStatusOfAllBts();

var serverCommands = function(data,url,callBackFunction,type)
{
	$.ajax({
		url:dirPath+url,
		data:data,
		type:'post',
		dataType:type,
		success:function(data)
		{
			callBackFunction(data);
		}
	});
}

/*
 * update the status of the bts 
 * */
var getBtsStatus = function(ip,id,code,systemid)
{
	var data = {};
	data.cmdType="GET_CURR_STATUS";
	data.systemId = systemid;
	data.systemCode = code;
	data.systemIp=ip;
	data.id=id
	data.data ='{"CMD_CODE":"GET_CURR_STATUS"}';
	
	var updateBTSstatus = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/netscan/clientopr",updateBTSstatus,'json');	
}

var addBts = function()
{
	
	var isIpValid = validateIp($("#btsIp").val())
	if(!isIpValid)
	{
		alert("Ip is not valid");
		return false
	}
		
	
	
	data={"btsIp":$("#btsIp").val(),"btsType":$("#btsType").val(),"dtype":$("#btsDeviceType").val()};
	$.ajax({
		url:dirPath+"service/netscan/addScanner",
		data:data,
		type:'post',
		success:function(data)
		{
			location.reload();
		}
	});
}

/*This function will take input ip and id of the bts in database and send it to the server for 
 * deleteion
 * */
var removeBts = function(ip,id)
{
	data={"btsIp":ip,"id":id};
	$.ajax({
		url:dirPath+"service/netscan/removeScanner",
		data:data,
		type:'post',
		success:function(data)
		{
			location.reload();
		}
	});
}


var getbtsnetworktype = function()
{	
	$.ajax({
		url:dirPath+"service/3g/btsnetworktype",
		type:'post',
		success:function(data)
		{
			createSelectListbtsNetworkType(data);
		}
	});
}
var getbtsDevicetype = function()
{	
	$.ajax({
		url:dirPath+"service/netscan/btsdevicetype",
		type:'post',
		success:function(data)
		{
			createSelectListbtsDeviceType(data);
		}
	});
}

/*
 * This method will get all the bts present in to the system along with there
 * Staus (UP/Down)
 * */
var getbtsinfo = function()
{	
	$.ajax({
		url:dirPath+"service/netscan/scanerInfo",
		type:'post',
		success:function(data)
		{
/*		var currentNodeLevel=window.parent.$('#leftTree').jstree('get_selected').attr('rel');
		var currentNodeText=window.parent.$('#leftTree .jstree-clicked').text().trim();
		//alert("currGroup is :"+currentNodeLevel);
		if(currentNodeLevel=="3rdLevel"){
		var d=[];
		for(var i=0;i<data.length;i++){
		if(data[i].grp_name==currentNodeText){
		d.push(data[i]);
		}		
}
data=d;
}else if(currentNodeLevel=="4thLevel"){
		var d=[];
		for(var i=0;i<data.length;i++){
		if(data[i].ip==currentNodeText){
		d.push(data[i]);
		}		
}
data=d;
}*/
			createRowsForBtsInfo(data);
		}
	});
}


createRowsForBtsInfo =function(data)
{
	$("#list_table_body").html("");
	var total = data.length;
	$("#tot_bts").html(total);
	var up = 0;
	
	var down = 0;
	
	
	
	for(var i in data)
	{	
		var colorStaus = colorAndStatusForStatusCode(parseInt(data[i].statuscode));
		var row="<tr>"+
		"<td>"+data[i].ip+"</td>"+
		"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>" +
				"<a style='cursor:pointer;' onclick=\"detailsStatus('"+data[i].ip.trim()+"',"+data[i].b_id+")\">"+data[i].status+"</a></td>"+
		"<td>"+data[i].typename+"</td>"+
		'<td><button class="btn-match" onclick="getConfig(\''+data[i].ip+'\','+data[i].b_id+','+data[i].dcode+','+data[i].sytemid+')">Configuration</button><button class="btn-match" onclick="removeBts(\''+data[i].ip+'\','+data[i].b_id+','+data[i].dcode+','+data[i].sytemid+')">Remove</button><button class="btn-match" onclick="getBtsStatus(\''+data[i].ip+'\','+data[i].b_id+','+data[i].dcode+','+data[i].sytemid+')">Check Staus</button></td>';
	
		
		$("#list_table_body").append(row);
		
		$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.wait);
		$("#seting_up_bts").html(parseInt($("#seting_up_bts").text())+colorStaus.count.run);
		$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
		$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);
	}
	
	/*$("#run_bts").html(colorStaus.count.run);
	$("#wait_bts").html(colorStaus.count.wait);
	$("#nor_bts").html(colorStaus.count.reachable);
	$("#down_bts").html(colorStaus.count.down);*/
	
}
/*
 * This function will return the color of cell and statuscount to idicate that if cell is up 
 * down ,runnning ,not rechable
 * */
var colorAndStatusForStatusCode = function(code)
{
	
	
	var color = {};
	color.fontColor = "white";
	color.backgroundColor = "white";
	var statusCount={};
	
	statusCount.run=0;
	statusCount.wait=0;
	statusCount.reachable=0;
	statusCount.down=0;
	
	var status={};
	
	switch(code)
	{
		case 0:
			color.backgroundColor = COLOR['wait'];
			statusCount.run++; 
			break;
		case 1:
			color.backgroundColor = COLOR['run'];
			statusCount.wait++;
			break;
		case 2:
			color.backgroundColor = COLOR['nor'];
			statusCount.reachable++;
			break;
		case 3:
			color.backgroundColor = COLOR['down'];
			statusCount.down =statusCount.down+1;
			break;
	
	}	
	status.color =color;
	status.count =statusCount;
	return status;
}


/*
 * This function will create the select list for the bts network type 
 * in form for the addtion of the new bts
 * */ 
var createSelectListbtsNetworkType = function(data)
{
	var option = "";
	for(var i in data)
	{
		option += "<option value="+data[i].n_id+">"+data[i].name+"</option>";
	}
	$("#btsType").html("");
	$("#btsType").html(option);
	
}


var createSelectListbtsDeviceType = function(data)
{
	var option = "";
	for(var i in data)
	{
		option += "<option value="+data[i].d_id+">"+data[i].dname+"</option>";
	}
	$("#btsDeviceType").html("");
	$("#btsDeviceType").html(option);
}


var updateCardsColor = function(){
	$(".nor_sufi").css("background",COLOR['nor']);
	$(".run_sufi").css("background",COLOR['run']);
	$(".wait_sufi").css("background",COLOR['wait']);
	$(".down_sufi").css("background",COLOR['down']);
}

var getConfig = function(ip,id,code,sysid)
{
	location.href = "netscan_configuration.jsp?ip="+ip+"&id="+id+"&sysId="+sysid;
}

$(document).ready(function()
{	
	getbtsnetworktype();
	getbtsDevicetype();
	getbtsinfo();
	updateCardsColor();
});


var validateIp = function(value) {
    var split = value.split('.');
    if (split.length != 4) 
        return false;
            
    for (var i=0; i<split.length; i++) {
        var s = split[i];
        if (s.length==0 || isNaN(s) || s<0 || s>255)
            return false;
    }
    return true;
}

var detailsStatus = function(ip,id)
{
	
	/*if($( "#dialog" ).dialog( "isOpen" ))
	{
		$( "#dialog" ).dialog( "close" );
	}*/
	getDetailStatusOfNetworkScanner(ip,id);
		
}

var getDetailStatusOfNetworkScanner = function(ip,id)
{
	data={"ip":ip,"id":id};
	
	console.log(data);
	$.ajax({
		url:dirPath+"service/netscan/networkScannerDetailInfo",
		data:data,
		type:'post',
		success:function(data)
		{
			//location.reload();
			setDialogData(data);
			$( "#dialog" ).dialog({modal: true});
		}
	});
}


var setDialogData = function(data)
{
	console.log(data);
	for(var i in data)
	{
		
		$("#netscan_status_details").text(data[i].status_text);
		$("#netscan_soft_state_details").text(data[i].soft_status_text);
		$("#netscan_timestamp_details").text(data[i].timestamp);
		$("#netscan_gps_status_details").text(data[i].gps_status_text);
		$("#netscan_dps_status_details").text(data[i].dps_status_text);
		$("#netscan_rep_flag_details").text(data[i].repetition_flag_text);
		$("#netscan_rep_freq_details").text(data[i].repetition_freq);
		$("#netscan_op_tech_details").text(data[i].op_tech);
	}
}


