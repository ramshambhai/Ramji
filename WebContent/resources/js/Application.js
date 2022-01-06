var dirPath ="../";
var globalAllTargetImsi=[];
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
		"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
		"<td>"+data[i].cellstatus+"</td>"+
		"<td>"+data[i].typename+"</td>"+
		"<td>"+data[i].dname+"</td>"+
		'<td><button class="btn-match" onclick="getConfig(\''+data[i].ip+'\','+data[i].b_id+','+data[i].dcode+','+data[i].sytemid+')">Configuration</button><button class="btn-match" onclick="removeBts(\''+data[i].ip+'\','+data[i].b_id+','+data[i].dcode+','+data[i].sytemid+')">Remove</button><button class="btn-match" onclick="getBtsStatus(\''+data[i].ip+'\','+data[i].b_id+','+data[i].dcode+','+data[i].sytemid+')">Check Staus</button></td>';
	
		
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
	//updateCardsColor();
	update_on_submit();
	getdevicetype1();
	getAllTargetImsi();
				 		         
				 $('#grp_name').hide();
				 $('#G-3').hide();
				 $('#G2_mobile_loc').hide();
				 $('#G2_geo_loc').hide();
				 $('#G3_net_scan').hide();
				 $('#myAddPopup').hide();
				 $('#selectDeviceDiv').hide();
				 
				 
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

/*********mrinalini*******/

	

	

	

function update_on_submit() {
	
    $( "#addDeviceDiv" ).click(function()
	
		{
				  addDeviceDiv();	
		});
		
	$( "#addTargetDiv" ).click(function()
	
		{
				  addTargetDiv();	
		});
	
    $( "#update_3G" ).click(function()
	
		{
			    
				  add_3G();
				  
				
		});


	
	$( "#update_2G_mobile_loc" ).click(function()
	
		{
			 	  
				  add_2G_mob_loc();
				  
		});



	
	$( "#update_2G_geo_loc" ).click(function()
	
		{
			      
				  add_2G_geo_loc();
				  
		});
		
		$( "#update_3G_net_scan" ).click(function()
	
		{
			      
				  add_3G_net_scan();
		});
		
		$( "#addTargetButton" ).click(function()
	
		{
			      
				  addTarget();
		});
		
		$( "#addDeviceSelect" ).change(function()
	
		{
			      
				  showGroupAndIp();
		});
		
		
		

}		


var getdevicetype1 = function()
{	
	$.ajax({
		url:dirPath+"service/3g/applicationType",
		type:'post',
		success:function(data)
		{
			createDeviceType(data);
		}
	});
}




var createDeviceType = function(data)
{	
	var options = "";
	options+='<option value="select">select device</option>';
	//$("#applicationNames").html('');
	for(var i in data)
	{
	options+='<option value="'+data[i].application_id+'">'+data[i].application_name+'</option>';
	  //htmlContent+='<div class="card" onclick="showGroupAndIp(\''+data[i].application_id+'\',\''+data[i].application_name+'\')"><div class="container"><h4><b>'+data[i].application_name+'</b></h4></div></div>';
	}
	$('#addDeviceSelect').append(options);
	//$("#applicationNames").html(htmlContent);
}

var showGroupAndIp = function(){
var selectedAppId=$("#addDeviceSelect").val();
if(selectedAppId=="select"){
$('#myAddPopup').hide(function(){alert("Please select Device");});
return;
}
$('#myAddPopup').show();
var selectedAppName=$("#addDeviceSelect option:selected").text();
			$('#formHeading').text(selectedAppName);
			$('#hiddenApplicationId').text(selectedAppId);
			if(selectedAppName == "2G-Mobile Locator")
		   
			{
				 $('#group_name').val("");
				 $('#ip_2G_mob_loc').val("");
				 $('#grp_name').show();
				 $('#firstPartPopup').show();
				 $('#G2_mobile_loc').show();
				 $('#G2_geo_loc').hide();
				 $('#G-3').hide();
				 $('#G3_net_scan').hide();
				 $('#addTargetId').hide();
			}
			else if(selectedAppName == "2G-Geo Locator")
				 {
				 $('#group_name').val("");
				 $('#ip_2G_geo_loc').val("");
				 $('#grp_name').show();
				 $('#G2_mobile_loc').hide();
				 $('#G2_geo_loc').show();
				 $('#firstPartPopup').show();
				 $('#G-3').hide();
				 $('#G3_net_scan').hide();
				 $('#addTargetId').hide();
			}
			else if(selectedAppName == "3G")
				 {
				 $('#group_name').val("");
				 $('#of_ip').val("");
				 $('#ppf_ip').val("");
				 $('#spf_ip').val("");
				 $('#grp_name').show();
				 $('#G2_mobile_loc').hide();
				 $('#G2_geo_loc').hide();
				 $('#G-3').show();
				 $('#firstPartPopup').show();
				 $('#G3_net_scan').hide();
				 $('#addTargetId').hide();
			}
			else if(selectedAppName == "3G-Network Scanner")
				 {
				 $('#group_name').val("");
				 $('#ip_3G_net_scan').val("");
				 $('#grp_name').show();
				 $('#G2_mobile_loc').hide();
				 $('#G2_geo_loc').hide();
				 $('#G-3').hide();
				 $('#G3_net_scan').show();
				 $('#firstPartPopup').show();
				 $('#addTargetId').hide();
}
}

var addDeviceDiv = function(){
$('#myAddPopup').hide();
$('#addDeviceSelect').val("select");
$('#selectDeviceDiv').show();
$('#containerDiv1').css("background-color","rgb(0, 49, 154)");
$('#containerDiv2').css("background-color","#426bc3");
}

var addTargetDiv = function(){
displayTargetImsi();
$('#group_name').val("");
$('#formHeading').text('Add Target');
$('#myAddPopup').show();
$('#firstPartPopup').hide();
$('#addTargetId').show();
$('#selectDeviceDiv').hide();
$('#containerDiv1').css("background-color","#426bc3");
$('#containerDiv2').css("background-color","rgb(0, 49, 154)");
}

var add_3G = function()
{
	var ofIp = validateIp($("#of_ip").val());
	var ppfIp = validateIp($("#ppf_ip").val());
	var spfIp = validateIp($("#spf_ip").val());
	
	
	if(!ofIp)
	{
		alert("Ip is not valid");
		return false
	}
	if(!ppfIp)
	{
		alert("Ip is not valid");
		return false
	}
	if(!spfIp)
	{
		alert("Ip is not valid");
		return false
	}
	
	 ofIp = ($("#of_ip").val());
	 ppfIp =($("#ppf_ip").val());
	 spfIp =($("#spf_ip").val());		
	 application_id =$('#hiddenApplicationId').text();
	 group_name =($("#group_name").val());
    
	
	data={"application_id":application_id,"group_name":group_name,"ofIp":ofIp,"ppfIp":ppfIp,"spfIp":spfIp};
	$.ajax({
		url:dirPath+"service/3g/addsufi",
		data:data,
		type:'post',
		success:function(data)
		{
		alert("group created successfully");
		window.parent.$('leftTree').jstree("destroy");
		window.parent.getAllBtsInfo();
		location.reload();
		},
		error:function()
		{
			alert("Request failed");
		}
	});
}


var add_2G_mob_loc = function()
{
	var ip_2G_mob_locValid = validateIp($("#ip_2G_mob_loc").val());
		
	if(!ip_2G_mob_locValid)
	{
		alert("Ip is not valid");
		return false
	}
			
	var application_id =$('#hiddenApplicationId').text();
	var group_name =($("#group_name").val());
	var ip =$("#ip_2G_mob_loc").val();
	
	
	data={"application_id":application_id,"group_name":group_name,"ip":ip};
	$.ajax({
		url:dirPath+"service/3g/add_mob",
		data:data,
		type:'post',
		success:function(data)
		{
		 alert("group created successfully");
		 window.parent.$('leftTree').jstree("destroy");
		 window.parent.getAllBtsInfo();
		 location.reload();
		},
		error:function()
		{
			alert("Request failed");
		}	
	});	
}

var add_2G_geo_loc = function()
{
	
	var ip_2G_geo_locValid = validateIp($("#ip_2G_geo_loc").val());
		
	if(!ip_2G_geo_locValid)
	{
		alert("Ip is not valid");
		return false
	}
			
	var application_id =$('#hiddenApplicationId').text();
	var group_name =($("#group_name").val());
	var ip =$("#ip_2G_geo_loc").val();

	
	
	data={"application_id":application_id,"group_name":group_name,"ip":ip};
	$.ajax({
		url:dirPath+"service/3g/add_geo",
		data:data,
		type:'post',
		success:function(data)
		{
		alert("group created successfully");
		window.parent.$('leftTree').jstree("destroy");
		window.parent.getAllBtsInfo();
		location.reload();
		},
		error:function()
		{
			alert("Request failed");
		}
	});
	
}

var add_3G_net_scan = function()
{
	
	var ip_3G_net_scanValid = validateIp($("#ip_3G_net_scan").val());
		
	if(!ip_3G_net_scanValid)
	{
		alert("Ip is not valid");
		return false
	}
			
	var application_id =$('#hiddenApplicationId').text();;
	var group_name =($("#group_name").val());
	var ip=($("#ip_3G_net_scan").val());
	
	
	
	data={"application_id":application_id,"group_name":group_name,"ip":ip};
	$.ajax({
		url:dirPath+"service/3g/add_netscan",
		data:data,
		type:'post',
		success:function(data)
		{
		alert("group created successfully");
		window.parent.$('leftTree').jstree("destroy");
		window.parent.getAllBtsInfo();
		location.reload();
		},
		error:function()
		{
			alert("Request failed");
		}
	});
}

var addTarget = function()
{
	
	var targetImsi=$("#targetImsi").val();
	var targetImei=$("#targetImei").val();
	if(targetImsi !="" && targetImsi.length != 15)
	{
		alert("Please add valid IMSI");
		return false;
	}
	if(targetImei != "" && targetImei.length != 16){
		alert("Please add valid IMEI");
		return false;
	}
	
	data={methodName:"addTarget",targetImsi:targetImsi,targetImei:targetImei};
	$.ajax({
		url:dirPath+"Operations",
		data:data,
		type:'post',
		success:function(data)
		{
		alert("Target added successfully");
		location.reload();
		},
		error:function()
		{
			alert("Problem in adding target");
		}
	});
}

var getAllTargetImsi = function()
{
	
	$.ajax({
		url:"../Operations",
		data:{methodName:"getAllTargetImsi"},
		type:'post',
		success:function(data)
		{
			console.log(data);
			globalAllTargetImsi=eval(data);
		}
		
	});	
}

var displayTargetImsi = function(){
$('#targetTableTbody').html("");
var options='';
for(var i=0;i<globalAllTargetImsi.length;i++){
options+='<tr><td>'+globalAllTargetImsi[i].imsi+'</td><td>'+globalAllTargetImsi[i].imei+'</td><td><button onclick="deleteTargetImsi('+globalAllTargetImsi[i].imsi+')">Delete</button></td></tr>';
}
$('#targetTableTbody').append(options);
}

var deleteTargetImsi = function(targetImsi){
	$.ajax({
		url:"../Operations",
		data:{methodName:"deleteTargetImsi",targetImsi:targetImsi},
		type:'post',
		success:function(data)
		{
			if(data=="true"){
			alert('Target deleted successfully');
			location.reload();
			}else{
			alert('Problem in deleting target');
			}
			
		}
		
	});	
} 