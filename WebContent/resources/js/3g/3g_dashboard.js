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
	serverCommands(data,"service/3g/clientopr",updateBTSstatus,'json');	
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
		url:dirPath+"service/3g/addBts",
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
		url:dirPath+"service/3g/removeBts",
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
		url:dirPath+"service/3g/btsdevicetype",
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
		url:dirPath+"service/3g/btsinfo",
		type:'post',
		success:function(data)
		{
		var currentNodeLevel=window.parent.$('#leftTree').jstree('get_selected').attr('rel');
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
}
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
	var adminstate=["NA","CELL_DOWN","CELL_UP"];
	var groupNodeCount=0;
	
	
	for(var i in data)
	{	
		
		var colorStaus = colorAndStatusForStatusCode(parseInt(data[i].statuscode));
		var row="<tr>";
		groupNodeCount=i%3;
		if(groupNodeCount==0){
		row='<td rowspan="3">'+data[i].grp_name+'</td>';
		}
		row+="<td>"+data[i].ip+"</td>"+
		"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
		"<td>"+data[i].cellstatus+"</td>"+
		"<td>"+adminstate[parseInt(data[i].adminstate)]+"</td>"+
		"<td>"+data[i].typename+"</td>"+
		"<td>"+data[i].dname+"</td>"+
		'<td><button class="btn-match" onclick="getBtsStatus(\''+data[i].ip+'\','+data[i].b_id+','+data[i].dcode+','+data[i].sytemid+')">Check Status</button></td>';
	
		
		$("#list_table_body").append(row);
		
		$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
		$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
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
			color.backgroundColor = COLOR['run'];
			statusCount.run++; 
			break;
		case 1:
			color.backgroundColor = COLOR['wait'];
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
	location.href = "configuration.jsp?ip="+ip+"&id="+id;
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