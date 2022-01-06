
var targetTableData=[];
var updateImsiList = function(data)
{
	
	var countTotalMobile = 0;
	var countTargetMobile = 0;
	$("#tracker_tab table tbody tr").remove();
	for(var i in data)
		{
			
		if(data[i].lat != "-1.0" && data[i].lon != "-1.0")
		{
			if(data[i].istarget == 't' ){
				$("#tracker_tab table tbody").append("<tr>"+
														"<td>"+data[i].target_name+"</td>"+
														"<td>"+data[i].imsi+"</td>"+
														"<td>"+data[i].imei+"</td>"+
														"<td>"+data[i].ta+"</td>"+
														"<td>"+data[i].power+"</td>"+
														"<td>"+data[i].acc+"</td>"+
														"<td>"+data[i].lat+","+data[i].lon+"</td>"+
														"<td><button data-imsi="+data[i].imsi+" class='showhistorybutton'>History</button></td>"+
													"</tr>");
				addmobile(data[i],'../../resources/images/mob_org.png');
				
				//$("#detected_mobile_modal .modal-body").append('<div style="padding: 5px;border:1px solid black;float:left;margin-left:3px;"><img onclick=getdetailsOfModbile("'+data[i].target_name+'","'+data[i].imsi+'","'+data[i].imei+'","'+data[i].ta+'","'+data[i].power+'") src="../../resources/images/mobile.png" style="width: 20px;height: 25px;display:block;margin: 0 auto;"><p style="font-size: 14px;    margin-bottom: 0px;">'+data[i].imsi+'</p><p style="font-size: 14px;    margin-bottom: 0px;">'+data[i].imei+'</p></div>');
				
				targetTableData.push[data[i]];
				countTargetMobile++;
			}
			else
			{
				addmobile(data[i],'../../resources/images/mob_org_black.jpg');
				//$("#detected_mobile_modal .modal-body").append('<div style="padding: 5px;border:1px solid black;float:left;margin-left:3px;"><img onclick=getdetailsOfModbile("'+data[i].target_name+'","'+data[i].imsi+'","'+data[i].imei+'","'+data[i].ta+'","'+data[i].power+'") src="../../resources/images/mobile.png" style="width: 20px;height: 25px;display:block;margin: 0 auto;"><p style="font-size: 14px;    margin-bottom: 0px;">'+data[i].imsi+'</p><p style="font-size: 14px;    margin-bottom: 0px;">'+data[i].imei+'</p></div>');
			}
		}
			
				
				countTotalMobile++;
		}
	
		console.log(countTotalMobile+"-"+countTargetMobile);
		$("#total_d").text(countTotalMobile);
		$("#total_d_d").text(countTargetMobile);
}


var getLatitudeLongitude = function(data)
{
	if(!(data.STATUS.indexOf("ERR(")>-1) && parseInt(data.NEIGH_FLAG) == 2)
		{					
			count=0;					
			for(var i in data.NEIGH_DATA)
			{
				if(data.NEIGH_DATA[i].STATUS == 2)
				{
					count++;
				
				}
			}					
			//alert(count);
			if(count >= 3)
			{
			   return parseData(data);	
			}
			else
			{
				return false;
			}					
		}
}

var parseData = function(data)
{
	var cellLatCount = 0;
	for(var i in data.NEIGH_DATA)
	{
		if(data.NEIGH_DATA[i].STATUS == 2)
		{
			var latLong = getLatLon(data.NEIGH_DATA[i]);
			if(latLong.lat != "-1" && latLong.lon != "-1" )
			{
				data.NEIGH_DATA[i].lat = latLong.lat;
				data.NEIGH_DATA[i].lon = latLong.lon;
				cellLatCount++;
			}				
		}
	}
	if(cellLatCount <3)
	{
		return false;
	}
	var sublc = calulateSubscriberLocation(data);
	return sublc;
}

var calulateSubscriberLocation = function(data)
{
	//sum = parseInt(data["RXLVL"]);
	sum = 0;
	for(var i in data.NEIGH_DATA)
	{
		if(data.NEIGH_DATA[i].STATUS == 2 && data.NEIGH_DATA[i].lat != null && data.NEIGH_DATA[i].lon !=null )
		{
			sum = sum+parseInt(data.NEIGH_DATA[i].RXLVL);				
		}
	}
	
	for(var i in data.NEIGH_DATA)
	{
		if(data.NEIGH_DATA[i].STATUS == 2 && data.NEIGH_DATA[i].lat != null && data.NEIGH_DATA[i].lon !=null )
		{
			data.NEIGH_DATA[i].work = parseInt(data.NEIGH_DATA[i].RXLVL)/sum;				
		}
	}
	
	data.work = parseInt(data["RXLVL"])/sum;
	
	var x = 0;
	var y = 0;
	
	for(var i in data.NEIGH_DATA)
	{
		if(data.NEIGH_DATA[i].STATUS == 2 && data.NEIGH_DATA[i].lat != null && data.NEIGH_DATA[i].lon !=null )
		{
			x=x+data.NEIGH_DATA[i].work*parseFloat(data.NEIGH_DATA[i].lat);
			y=y+data.NEIGH_DATA[i].work*parseFloat(data.NEIGH_DATA[i].lon);
		}
	}
	data.lat = $("#lat").val();
	data.lon = $("#lon").val();
	
	return {"x":x,"y":y};
}


var getLatLon = function(data)
{
	var data1;
	$.ajax({
		url:"../../Operations",
		data:{	
				"methodName":"getLatLon",
				"MCC":""+data.MCC,
				"MNC":""+data.MNC,
				"LAC":""+data.LAC,
				"CELL":""+data.CELL
			},
			datatype:"json",
		type:"post",
		async:false,
		success:function(data2)
		{			
			
		data1 = jQuery.parseJSON(data2);
			//locate(t_id);
		},
		error:function()
		{
			
		}	
	});
	return data1[0];
}


var   getRandomColor = function () 
{
	  var letters = '0123456789ABCDEF';
	  var color = '#';
	  for (var i = 0; i < 6; i++) {
	    color += letters[Math.floor(Math.random() * 16)];
	  }
	  return color;
}


$("document").ready(function(){
	$("#tracker_tab table tbody").on("mousedown",".showhistorybutton",function(){
		
		subscriberPathLayer.clearLayers();
		subscriberPathLayer.addTo(map);
		
		subscriberPathCircle.clearLayers();
		subscriberPathCircle.addTo(map);
		
		getSubscriberPath($(this).data("imsi"));
		
	});
	$("#tracker_tab table tbody").on("mouseup",".showhistorybutton",function(){
		subscriberPathLayer.remove();
		subscriberPathCircle.remove();
	});
	
	
});


var filterMobDataData = function(type,val)
{
	var d = new Date();
	operationStartTIme =  toUtcTime(d);
	addsessionparam("mobtime",operationStartTIme);
	getcurrentcdrdata();
}






