/**
 * 
 */

$(document).ready(function(){
	registerEvents();
	initData();
});

var initData = function()
{
	$("#scan_type").trigger("change");
	$("#report_action").trigger("change");
	//getCurrentAngle();
	getStepAngle();
	getAngle();
	
}
var registerEvents = function()
{
	showHideRelatedfields();
}

var getReports = function()
{
	
	switch(parseInt($("#report_type").val()))
	{
		case 0:
			getCellReport();
		break;
		case 1:
			getFreqReport();
		break;
	}
}

/*
 * 
 * */
var showHideRelatedfields = function()
{
	$("#scan_type").change(function(){
		$("#netscan-scanner tr").show();
		switch(parseInt($(this).val()))
		{
			case 0:
				$(".exe_scan_field").hide();
				$(".cell_scan_field").hide();
				$(".stop_scan_row").hide();
				$(".freq_scan_field").show();
			break;
			case 1:
				$(".exe_scan_field").hide();
				$(".freq_scan_field").hide();
				$(".stop_scan_row").hide();
				$(".cell_scan_field").show();
				break;
			case 2:
				$(".cell_scan_field").hide();
				$(".freq_scan_field").hide();
				$(".stop_scan_row").hide();
				$(".exe_scan_field").show();
				break;
			case 3:
				$("#netscan-scanner tr").hide();
				$(".cell_scan_field").hide();
				$(".freq_scan_field").hide();
				$(".exe_scan_field").hide();
				
				$(".stop_scan_row").show();
				$(".stop_scan_row").closest("tr").show();
				break;
		}
	});
	
	$("#report_action").change(function(){
		switch(parseInt($(this).val()))
		{
			case 0:
				$(".save-report").hide();
				$(".get-report").show();
				$(".get-report-btn").show();
			break;
			case 1:
				$(".get-report").hide();
				$(".get-report-btn").hide();
				$(".save-report").show();
				break;
		}
	});
	
}


var updateMoveAngle = function()
{
	var data = 
	{
			angle:$("#ptz_move_angle").val(),
			ip:deviceIp,
			id:deviceId
	}
	$.ajax({
		url:dirPath+"service/ptz/updateMoveStepAngle",
		type:'POST',
		dataType:'json',
		data:data,
		success:function(data)
		{
			console.log(data);
		}
	});
}



var getStepAngle = function()
{
	var data = 
	{
			ip:deviceIp,
			id:deviceId
	}
	$.ajax({
		url:dirPath+"service/ptz/setp_angle",
		type:'GET',
		dataType:'json',
		data:data,
		success:function(data)
		{
			console.log();
			var temp = jQuery.parseJSON(data[0].config)
			
			$("#ptz_move_angle").val(temp.step);
		}
	});
}


var getAngle = function()
{
	var data = 
	{
			ip:deviceIp,
			id:deviceId
	}
	$.ajax({
		url:dirPath+"service/ptz/angle",
		type:'GET',
		dataType:'json',
		data:data,
		success:function(data)
		{
			
			var temp = jQuery.parseJSON(data[0].angle_offset);
			console.log(temp);
			$("#curr_ptz_angle").text(temp);
		}
	});
}

var getCurrentAngle = function()
{
	var data = 
	{
			ip:deviceIp,
			id:deviceId
	}
	$.ajax({
		url:dirPath+"service/ptz/getcurrentangle",
		type:'GET',
		data:data,
		success:function(data)
		{
			console.log(data);
			$("#ptz_current_angle_txt").val(data.angle);
			$("#ptz_north_tilt_txt").val(data.tilt);
		},error:function(err){
			console.log("error in current angle :"+err);
		}
	});
}

var getNorthHeading = function()
{
	var data = 
	{
			ip:deviceIp,
			id:deviceId
	}
	
	
	 var keepRunnig=true;
	 $.ajax(
				{
					url:dirPath+"service/common/scanandtrackmode",
					type:'post',
					async: false,
					success:function(data)
					{
						for(var i=0;i<data.length;i++){	
							if(data[i].mode_type=='scan'){
								if(data[i].mode_status.toLowerCase()=='idle'){
									
								}else{
									alert("Please stop Scanning and Tracking");
									keepRunnig=false;
								}
							}else{
								if(data[i].mode_status.toLowerCase()=="idle"){
									//keepRunnig=true;
									
								}else{
									if(data[i].applied_antenna==null || data[i].applied_antenna==undefined || data[i].applied_antenna==''){
										alert("Please stop Scanning and Tracking");
										keepRunnig=false;
										
									}else{
										alert("Please stop Scanning and Tracking");
										keepRunnig=false;
									}
								}
								}
						}
					}
				});
	 
	 if(!keepRunnig)
	 {
		 return;
	 }
	
	$.ajax({
		url:dirPath+"service/ptz/getnorthheading",
		type:'GET',
		dataType:'json',
		data:data,
		success:function(data)
		{

			if(isEmpty(data)){
				alert("No response from device , Please check connection ");
				return;
			}
			
			if(data.type=="stabilization" && data.status=="0"){
				alert("North Heading stabilization is under progress,please wait.");
			}else if(data.type=="connection" && data.status=="0"){
				alert("Device is not connected,please check");
			}else{
				var offset=data.offset;
				var lat=data.latitude;
				var long=data.longitude;
				$("#ptz_north_offset_txt").val(offset);
				$("#coordinate_gps").val(lat+","+long);
			}

		}
	
		
	
	
	
	
	
	});
}




function getDisplay(first, second){
	//$("#"+first).show();
	if(first.localeCompare("getNorthOffset")==0)
	{
		

		document.getElementById("getNorthOffset1").style.removeProperty("display");
		document.getElementById("getNorthOffset2").style.removeProperty("display");
		document.getElementById("getNorthOffset3").style.removeProperty("display");
		document.getElementById("getNorthOffset4").style.removeProperty("display");
		document.getElementById("getNorthOffset5").style.removeProperty("display");
		
		
		document.getElementById("ptsOperations1").style.display = "none";
		document.getElementById("ptsOperations2").style.display = "none";
		document.getElementById("ptsOperations3").style.display = "none";
		document.getElementById("ptsOperations4").style.display = "none";
		
		
	}
	
	else{	
			
			document.getElementById("getNorthOffset1").style.display = "none";
			document.getElementById("getNorthOffset2").style.display = "none";
			document.getElementById("getNorthOffset3").style.display = "none";
			document.getElementById("getNorthOffset4").style.display = "none";
			document.getElementById("getNorthOffset5").style.display = "none";
			
		
			document.getElementById("ptsOperations1").style.removeProperty("display");
			document.getElementById("ptsOperations2").style.removeProperty("display");
			document.getElementById("ptsOperations3").style.removeProperty("display");
			document.getElementById("ptsOperations4").style.removeProperty("display");
		
		
		
		
	}
	


}


function isEmpty(obj) {
    for(var prop in obj) {
        if(obj.hasOwnProperty(prop))
            return false;
    }

    return true;
}
var move = function(dir)
{

	 var keepRunnig=true;
	 $.ajax(
				{
					url:dirPath+"service/common/scanandtrackmode",
					type:'post',
					async: false,
					success:function(data)
					{
						for(var i=0;i<data.length;i++){	
							if(data[i].mode_type=='scan'){
								if(data[i].mode_status.toLowerCase()=='idle'){
									
								}else{
									alert("Please stop Scanning and Tracking");
									keepRunnig=false;
								}
							}else{
								if(data[i].mode_status.toLowerCase()=="idle"){
									//keepRunnig=true;
									
								}else{
									if(data[i].applied_antenna==null || data[i].applied_antenna==undefined || data[i].applied_antenna==''){
										alert("Please stop Scanning and Tracking");
										keepRunnig=false;
										
									}else{
										alert("Please stop Scanning and Tracking");
										keepRunnig=false;
									}
								}
								}
						}
					}
				});
	 
	 if(!keepRunnig)
	 {
		 return;
	 }
	 
	var ptxMoveAngle = $('#ptz_move_angle').val();
	if(dir!="-1"){
	if(ptxMoveAngle<1 || ptxMoveAngle >300){
		alert("Please provide valid value in range ");
		return;
	}
	}
	var data = 
	{
			ip:deviceIp,
			direction:dir,
			angle:$("#ptz_move_angle").val()
	}
	$.ajax({
		url:dirPath+"service/ptz/move",
		type:'GET',
		dataType:'json',
		data:data,
		success:function(data)
		{
			if(data.result.includes("error---"))
			{
				var Max_Min_to_show = data.result.split("---");
				alert("Angle is out of range "+Max_Min_to_show[1]);
			}
			if(data.result.localeCompare("error")==0)
			{
				
				alert("Error occured while Performing Operation");
			}
			if(data.result.localeCompare("success")==0)
			{
				
				alert("Operation Successfully Completed");
			}
/*			getCurrentAngle();
			getNorthHeading();*/
			/*console.log(jQuery.parseJSON(data[0].config));
			var temp = jQuery.parseJSON(data[0].config);
			$("#curr_ptz_angle").text(temp);*/
		}
	});
}




