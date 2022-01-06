/**
 * 
 */


function truncateDb()
{
	
	data={"methodName":"truncateDb"};
	$.ajax({
		url:"../Operations",
		data:data,
		type:'post',
		success:function(data)
		{
			location.reload();
			
		}
	});
}
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

function updateNibVal(nib)
{
	
	data={"methodName":"updateNib","nib":nib};
	$.ajax({
		url:"../Operations",
		data:data,
		type:'post',
		success:function(data)
		{
			alert(data);
			$("#nib_ip_val_head").html("");
			$("#nib_ip_val_head").text(nib);
		}
	});
}

function updateMotoFlag(nib)
{
	
	data={"methodName":"setStatus",
			"cmdType":"SET_MONIT_MODE",
			"MONITMODE":runningModeGlobe.MONITMODE,
			"INVOKETRACE":parseInt($("input[name=mon_flag_trace]:checked").val()),
			"IMEITRACEMODE":parseInt($("input[name=mon_flag_imei_trace]:checked").val()),
			"RFU2":parseInt($("input[name=mon_flag_rfu]:checked").val()),
			"RFU3":parseInt(($("input[name=mon_flag_rfu]").is(":checked"))?"1":"0"),
			"RFU4":0,
			"AUTOCONNNECT":(parseInt($("input[name=mon_flag_auto_trace]:checked").val()) == 0)?0:parseInt($("input[name=mon_flag_auto_trace]:checked").val())+parseInt($("#mins_connect").val())
			};
	$.ajax({
		url:"../Operations",
		data:data,
		type:'post',
		success:function(data)
		{
			window.location = "../views/administration.jsp"
		}
	});
}


var updateNib = function(data)
{
	$("#nibs").html("");
	$("#nibs").append("<option value=-1>select Nib</option>");
	for(var i in data)
	{
		$("#nibs").append("<option value="+data[i]+">"+data[i]+"</option>");
	}
}
var eventRegister = function()
{
	$("#updateNib").click(function(){
		if($("#nibs").val() == -1)
		{	
			alert("Please Select Nib");
		}
		else
		{
			updateNibVal($("#nibs").val());
		}
	});
	
	$("#update_mode").click(function(){
		updateMotoFlag();
	});
	$("#truncateDb").click(function(){
		
		var a = confirm("Are you sure!");
		if(a)
		{
			truncateDb();
		}
		
		
	});
	
}
var setMonitoringFlag = function()
{
	var autoConnect = parseInt(runningModeGlobe.AUTOCONNNECT) == 0?0:128
	var min = 1;
	if(autoConnect != 0)
	{
		min = parseInt(runningModeGlobe.AUTOCONNNECT) - 128;
	}		
	$("input[name=mon_flag_trace][value=" + parseInt(runningModeGlobe.INVOKETRACE) + "]").prop('checked', true);
	$("input[name=mon_flag_imei_trace][value=" + parseInt(runningModeGlobe.IMEITRACEMODE) + "]").prop('checked', true);
	$("input[name=mon_flag_auto_trace][value=" + ""+autoConnect + "]").prop('checked', true);
	//$("input[name=mon_flag_auto_trace][value=128]").prop('checked', true);
	$("input[name=mon_flag_rfu][value=" + ""+parseInt(runningModeGlobe.RFU2) + "]").prop('checked', true);
	(parseInt(runningModeGlobe.RFU3)==1)?$("input[name=loc_flag_rfu]").prop('checked', true):$("input[name=loc_flag_rfu]").prop('checked', false);
	$("#mins_connect").val(min).change();
}

$(document).ready(function()
{	
	getNibs();
	eventRegister();
	//getNumbers();
	//setClickEventsOnElements();
	
	
	for(var i = 1;i<=120;i++)
	{
		$("#mins_connect").append("<option value="+i+">"+i+"</option>");
	}
	setMonitoringFlag();
	
});


