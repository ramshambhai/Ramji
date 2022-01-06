
var targetTableData=[];
var updateImsiList = function(data)
{
	globalAlarm=[];
	$("#bl_table tbody").html("");
	
	//@sunil 
	globalCdrCount = 0;
	
dataOfNodeWiseReport=data;
			var rowsAlarm='';
			var inventoryData=getInventoryOnly();
for(var i=0;i<dataOfNodeWiseReport.length;i++){
	var dateTimeArray = dataOfNodeWiseReport[i].insert_time.split(" "); 
	var mapMsloc="";
	if(dataOfNodeWiseReport[i].msloc.toLowerCase().indexOf("na")==-1){
		mapMsloc=dataOfNodeWiseReport[i].msloc;
	}
	if(mapMsloc==""){
		mapMsloc=dataOfNodeWiseReport[i].prob_msloc;
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
		   globalAlarm.push(dataOfNodeWiseReport[i].imsi+"_"+dataOfNodeWiseReport[i].profile_name+"_"+dataOfNodeWiseReport[i].trans_id);
		   
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
			var mapData={};
			mapData.imsi=dataOfNodeWiseReport[i].imsi;
			mapData.imei=dataOfNodeWiseReport[i].imei;
			mapData.power=dataOfNodeWiseReport[i].rxl;
			mapData.target_name=dataOfNodeWiseReport[i].target_name;
			mapData.type=dataOfNodeWiseReport[i].traget_type;
			mapData.profile_name=dataOfNodeWiseReport[i].profile_name;
			mapData.tech=dataOfNodeWiseReport[i].stype;
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
				var lineEndPoint = calulateLatLongAtGivenAngleAndDistance(deviceData.LATITUDE,deviceData.LONGITUDE,sectorAngle,distance);
				mapData.lat= lineEndPoint.lat;
				mapData.lon= lineEndPoint.lng;
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
		
}
$("#bl_table tbody").append(rowsAlarm);
resizeTable("bl_table");
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






