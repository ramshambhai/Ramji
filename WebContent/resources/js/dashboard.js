/**
 *This is for the index page operations 
 */
var STATUS_ACTION_MAP = [];
STATUS_ACTION_MAP["0"]=["UNDEF"];
STATUS_ACTION_MAP["1"]=["IDLE"];
STATUS_ACTION_MAP["2"]=["DETACHED"];
STATUS_ACTION_MAP["3"]=["CONNECTED"];

/**********RREVENT********************/
var RREVENTArray = [];
RREVENTArray["0"]=["0"];
RREVENTArray["1"]=["CHR"];
RREVENTArray["2"]=["MES"];

/**********RREVENT********************/


var pollManualFlag = false;
var numbers = [];
var message = "";
var mapDataMarkers = [];
var neighbourDataMarkers = [];
var map;
var printFlag = 0;
var cancelValue = "reload";
var canCelReleaseElement = null;
var msisdn = "";
var getLatLon = function(data)
{
	var data1;
	$.ajax({
		url:"../Operations",
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

var getLatLonCells = function(data)
{
	var data1;
	$.ajax({
		url:"../Operations",
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




function locateSubscriber()
{	
	locate();		
}


/*This function will send the message*/
var locate = function(t_id)
{
	console.log("asking for request");
	console.log("IMSI"+$("#imsi_number").val());
	/*flag = 2;
	
	if(t_id != null )
	{
		flag = 1;
	}
	else
	{
		flag = $('input[name=req_type]:checked').val();
		
		if(t_id == null && pollManualFlag == false && flag == 1)
		{
			pollManualFlag = true;
		}
		if( flag==4)
		{
			triggerPage(flag);
			return false;
		}
		t_id= Date.now();
		
	}*/
	
	t_id= Date.now();
	var flag=1;
	
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","inline-block");
	$.ajax({
		url:"../Operations",
		data:
		{	
				"methodName":"livepoll",
				"imsi":""+$("#imsi_number").val(),
				"imei":""+$("#imei_number").val(),
				"t_id":t_id,
				"flag":flag
		},
		datatype:"json",
		type:"post",
		success:function(data)
		{			
			var data = jQuery.parseJSON(data);
			
			
			if(parseInt(data.STATUS) == 3 || (data.STATUS.indexOf("ERR(")>-1))
			{	
					locate(t_id);				
			}
			else if(parseInt(data.STS) == 1)
			{		
					locate(t_id);				
			}
			else
			{
				
				createSelfLoginTable(data);
				triggerSms(data.MSISDN,t_id);
				/*if(parseInt(data.NEIGH_FLAG) == 1)
				{
					triggerSms(data.MSISDN,t_id);
				}
				else if(parseInt(data.NEIGH_FLAG) == 2)
				{					
					
					//createSelfLoginTable(data);
					
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
					   //parseData(data);
					   triggerSms(data.MSISDN,t_id);
					}
					else
					{
						triggerSms(data.MSISDN,t_id);
					}				
				}
				else
				{
					//locate(t_id);
				}*/
			}
		},
		error:function()
		{
			
		}	
	});
}



var locateWithNeighbour = function(t_id,neighbourData)
{
	console.log("asking for request");
	console.log("IMSI"+$("#imsi_number").val());
	flag = 2;
	if(t_id != null )
	{
		flag = 1;
	}
	else
	{
		
		flag = $('input[name=req_type]:checked').val();		
		
		if(t_id == null && pollManualFlag == false && flag == 1)
		{
			pollManualFlag = true;
		}		
		t_id= Date.now();
		
		if( flag==4)
		{
			triggerPage(flag);
			return false;
		}
		//flag = $('input[name=req_type]:checked').val();
	}
	
	var lengthOfNeigh = Object.keys(neighbourData).length;
	
	neighbourData.lengthOfNeigh = lengthOfNeigh;
	neighbourData.methodName = "locateWithNeighbour";
	neighbourData.imsi = $("#imsi_number").val();
	neighbourData.imei = $("#imei_number").val();
	neighbourData.t_id = t_id;
	neighbourData.flag = flag;
	console.log(neighbourData);
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","inline-block");
	$.ajax({
		url:"../Operations",
		data:neighbourData,
		datatype:"json",
		type:"post",
		success:function(data)
		{			
			
			
			if(flag == 2)
			{
				alert("Network created");
				location.reload();				
			}
			if(flag == 3)
			{
				alert("Neighbour Successfully Changed");
				location.reload();				
			}
			if(pollManualFlag)
			{
				triggerSms(data.MSISDN,t_id);
				pollManualFlag = false;				
				return;
			}
			
			
			var data = jQuery.parseJSON(data);
			
			if(parseInt(data.STATUS) == 3 || (data.STATUS.indexOf("ERR(")>-1))
			{	
				locate(t_id);				
			}
			else if(parseInt(data.STS) == 1)
			{
				
				//locate(t_id);
				triggerSms(data.MSISDN,t_id);
			}
			else
			{
				//triggerSms(data.MSISDN,t_id);
				createSelfLoginTable(data);
				if(parseInt(data.NEIGH_FLAG) == 1)
				{
					triggerSms(data.MSISDN,t_id);
				}
				else if(parseInt(data.NEIGH_FLAG) == 2)
				{					
					//createSelfLoginTable(data);
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
					   parseData(data);	
					}
					else
					{
						triggerSms(data.MSISDN,t_id);
					}					
				}
				else
				{
					//parseData(data);
				}
			}
		},
		error:function()
		{
			
		}	
	});
}

var parseData = function(data,t_id)
{
	var breakLoop = false;
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
			else
			{
				//data.NEIGH_DATA[i].lat = latLong.lat;
				//data.NEIGH_DATA[i].lon = latLong.lon;
				breakLoop = true;
			}				
		}
	}
	if(cellLatCount <3)
	{
		alert("Cell Data lat lon is not present");
		location.reload();		
	}
	var sublc = calulateSubscriberLocation(data);
	data.sub = sublc;
	changeNeighbourTowerImages();
	for(var i in data.NEIGH_DATA)
	{
		if(data.NEIGH_DATA[i].STATUS == 2 && data.NEIGH_DATA[i].lat != null && data.NEIGH_DATA[i].lon !=null )
		{
			//placeMarker(data.NEIGH_DATA[i].lat,data.NEIGH_DATA[i].lon,"../resources/images/blue_old.png");
			//matchTowerOnMap(data.NEIGH_DATA[i].MCC,data.NEIGH_DATA[i].MNC,data.NEIGH_DATA[i].LAC,data.NEIGH_DATA[i].CELL,data.NEIGH_DATA[i].lat,data.NEIGH_DATA[i].lon);
			//placeSearchedCellsOverToMAp(data.NEIGH_DATA[i],data.NEIGH_DATA[i].MCC,data.NEIGH_DATA[i].MNC,data.NEIGH_DATA[i].LAC,data.NEIGH_DATA[i].CELL,data.NEIGH_DATA[i].lat,data.NEIGH_DATA[i].lon);
			//addMarker();
			var dataaa = 
			{
				"name":	data.NEIGH_DATA[i],
				"mcc":data.NEIGH_DATA[i].MCC,
				"mnc":data.NEIGH_DATA[i].MNC,
				"lac":data.NEIGH_DATA[i].LAC,					
			}
			addMarker([parseFloat(data.NEIGH_DATA[i].lon),parseFloat(data.NEIGH_DATA[i].lat)],"blink_tower.gif",vectorSource,dataaa,i);
		}
	}
	//placeMarker(data.sub.x,data.sub.y,"../resources/images/MobilePhone.png");
	
	//addMarker([data.sub.y,data.sub.x],"mob_org.png",vectorSource,null);
	drawCircle(map,8,[data.sub.y,data.sub.x],"#FF0000");
	var sub_distance = distanceBetweenLatLon(parseFloat($("#lat").val()),parseFloat($("#lon").val()),parseFloat(data.sub.x),parseFloat(data.sub.y));
	//console.log("distance"+distance);
	drawCenterHighlightedCircleCircleTower(map,data.TA,[$("#lon").val(),$("#lat").val()],"#ff0000");
	//placeMarker($("#lat").val(),$("#lon").val(),"../resources/images/green_old.png");
	addMarker([$("#lon").val(),$("#lat").val()],"green_old.png",vectorSource,null,"");
	data.lat = $("#lat").val();
	data.lon = $("#lon").val();
	data.nibIp = $("#nib_ip_val_head").text();
	createSelfTable(data);
	
	//$("#table_div").append("<fieldset id='neigh' style='width:630px;'><legend>Neighbour Cells</legend></fieldset>");
	$("#table_div").append("<fieldset id='neigh' style='width:610px;'><legend>Neighbour Cells</legend><table id='neigh_table' border=1>" +
			"<tr><td>Cell Name</td><td>MCC</td><td>MNC</td><td>LAC</td><td>CELL</td><td>BSIC</td><td>Dst(m)</td><td>RXLVL</td><td>LAT,LON</td><tr>" +
			"</table>" +
			"</fieldset>");
		for(var i in data.NEIGH_DATA)
		{
			if(data.NEIGH_DATA[i].STATUS == 2 && data.NEIGH_DATA[i].lat != null && data.NEIGH_DATA[i].lon !=null )
			{
				createCellTable(data.NEIGH_DATA[i],i,"neigh_table");				
			}
		}
		data.t_id = Date.now();
		createDataToStoreOnServer(data);
		
		$("#sub_lat").text(data.sub.x);
		
		$("#sub_lon").text(","+data.sub.y);
		
		$("#sub_distance").text(sub_distance+"mtr");
		
		$("#sub_IMSI").text($("#imsi_number").val());
		$("#neigh_form").css("display","none");
		
		$("#target_scan").css("display","none");
		
		$("#reTarget").css("display","block");
		$("#sub_det").css("display","block");
		$("#map_div").css("width","49%");
		
		
		
		$("#bottomdiv").css("display","block");
		$("#topdiv").css("display","flex");
        
		
		$("#loadingBox").css("display","none");
        
        $("#message_box").css("display","block");
        
		
        map.updateSize();
        //google.maps.event.trigger(map, 'resize');
		//map.setCenter({lat:data.sub.x,lng:data.sub.y});
		//map.setZoom(17);
		
		
}
var calulateSubscriberLocation = function(data)
{
	sum = parseInt(data["RXLVL"]);
	//sum = 0;
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
	
	x=x+data.work*parseFloat(data.lat);
	y=y+data.work*parseFloat(data.lon);
	
	return {"x":x,"y":y};
}


var createSelfTable  = function(data)
{
	table = "<fieldset style='width:610px;'>" +
			"<legend>NIB</legend>" +
			"<table border=1>" +
			"<tr><!--<td>TI</td>-->" +
			"<!--<td>IMSI</td>--><td>MSISDN</td><td>IMEI</td><td>TA</td><td>RXLVL</td><td>MCC</td><td>MNC</td><td>LAC</td>" +
			"<td>CELL</td><td>LAT,LON</td><tr>" +
			"<tr><!--<td>"+data.TI+"</td>--><!--<td>"+data.IMSI+"</td>--><td>"+data.MSISDN+"</td><td>"+data.IMEI+"</td><td>"+data.TA+"</td><td>"+(parseInt(data["RXLVL"])-110)+"</td><td>"+data.MCC+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><td>"+data.lat+","+data.lon+"</td><tr>" +
					"</table></fieldset>";
	
	$("#table_div").append(table);
}

/*var createSelfLoginTable  = function(data)
{
	mcc = data.MCC.substring(0,3);
	table = "<fieldset style='width:610px;background:white;text-align:center;margin:0 auto;'>" +
			"<legend><strong>Detected</strong></legend>" +
			"<table border=1 style='margin:0 auto;'>" +
			"<thead><!--<th>TI</th>-->" +
			"<th>IMSI</th><th>MSISDN</th><th>IMEI</th><th>TA</th><th>RXLVL</th><th>MCC</th><th>MNC</th><th>LAC</th>" +
			"<th>CELL</th><!--<th>LAT</th><th>LON</th>--><thead>" +
			"<tbody id='cellinfoBody'><tr style='background-color:#eee;'><!--<td>"+data.TI+"</td>--><td>"+data.IMSI+"</td><td>"+data.MSISDN+"</td><td>"+data.IMEI+"</td><td>"+data.TA+"</td><td>"+(parseInt(data["RXLVL"])-110)+"</td><td>"+mcc+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><!--<td>"+data.lat+"</td><td>"+data.lon+"</td>--><tr>" +
					"</tbody></table></fieldset>";
	
	//$("#loadingBox").append(table);
	if(printFlag == 0)
	{
		printFlag = 1;
		$("#loadingBox").append(table);
	}else
	{
		var topColor = $("#cellinfoBody tr:eq(0)").css("background-color");
		
		var color = (topColor =="rgb(238, 238, 238)")?"rgb(255, 255, 255)":"rgb(238, 238, 238)";
		
		$("#cellinfoBody").prepend("<tr style='background-color:"+color+";' ><!--<td>"+data.TI+"</td>--><td>"+data.IMSI+"</td><td>"+data.MSISDN+"</td><td>"+data.IMEI+"</td><td>"+data.TA+"</td><td>"+(parseInt(data["RXLVL"])-110)+"</td><td>"+mcc+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><!--<td>"+data.lat+"</td><td>"+data.lon+"</td>--><tr>");
	}
	
}*/

var createCellTable  = function(data,key,id)
{
	/*var htmt = "<table border=1>" +
			"<tr><td>Cell Name</td><td>MCC</td><td>MNC</td><td>LAC</td><td>CELL</td><td>BSIC</td><td>ARFCN</td><td>RXLVL</td><td>LAT</td><td>LON</td><tr>" +
					"<tr><td>"+key+"</td><td>"+data.MCC+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><td>"+data.BSIC+"</td><td>"+data.ARFCN+"</td><td>"+data.RXLVL+"</td><td>"+data.lat+"</td><td>"+data.lon+"</td><tr>" +
							"</table>";*/
	
	var htmt ="<tr><td>"+key+"</td><td>"+data.MCC+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><td>"+data.BSIC+"</td><td>"+distanceBetweenLatLon($("#lat").val(),$("#lon").val(),data.lat,data.lon)+"</td><td>"+(parseInt(data.RXLVL)-110)+"</td><td>"+data.lat+","+data.lon+"</td><tr>";
	if(data.lat != "-1" && data.lon != "-1")
		$("#"+id).append(htmt);
}

var triggerSms = function(MSISDN,t_id)
{
	$.ajax({
		url:"../Operations",
		data:{	
				"methodName":"triggerSms",
				"msisdn":""+MSISDN
			},
			datatype:"json",
		type:"post",
		success:function(data)
		{			
			
			//var data = jQuery.parseJSON(data);
			
			locate(t_id);
		},
		error:function()
		{
			
		}	
	});
}



function initMap() {
  map = new google.maps.Map(document.getElementById('map_div'), {
   center: {lat: -34.397, lng: 150.644},
    zoom: 8
  });
}

function placeMarker(lat,lng,image)
{
	console.log(lat+" "+lng);
	//uluru = {lat: -25.363, lng: 131.044};
	var marker = new google.maps.Marker({
        position: {lat: parseFloat(lat), lng:parseFloat(lng)},
        map: map,
        icon: image,
        optimized: false
      });
	
}

$(document).ready(function()
{
	getListForImsiImei();
		
});



var getCellsData = function(MSISDN,t_id)
{
	$.ajax({
		url:"../Operations",
		data:{	
				"methodName":"getCellsData"
			},
			datatype:"json",
		type:"post",
		success:function(data)
		{				
			var data = jQuery.parseJSON(data);
			if(parseInt(data.BSNos) <= 0)
			{
				$("#ne_flag").attr("disabled", true);
			}
			else
			{
				
				if(data.GPS_Data.GPSLAT == "NOT_AVAILABLE" || data.GPS_Data.GPSLONG == "NOT_AVAILABLE" || data.GPS_Data.GPSLAT == "" || data.GPS_Data.GPSLONG == "")
				{
					$("#lat").val("28.486766");
					$("#lon").val("77.075107");
				}
				else
				{
					$("#lat").val(data.GPS_Data.GPSLAT);
					$("#lon").val(data.GPS_Data.GPSLONG);
				}
				
				
				//$("#imsi_number").val("404119104773536");
				
				$("#ne_flag").removeAttr("disabled");
				//parseCellData(data);
				//getCellsData();
				
				//placeMapDataOverToMAp(data,true);
				placeCellsMapDataOverToMAp(data);
				checkBoxClickEvent();
				//$("#ne_flag").trigger("click");
				$("#ne_flag").attr({"disabled":"disabled"});
				//sortTable("neigh_form",4)
				$("#neigh_form").prepend("<tr><td>PID</td><td>Neg</td><td>Self</td><td style='display:none;'>STATUS</td><td>PLMN</td><td>LAC</td><td>CELL</td><td>Rx</td><td>Latitude</td><td>Longitude</td><td>Distance</td><td>ARFCN</td><td>BSIC</td></tr>");
			}				
		},
		error:function()
		{
			
		}	
	});
}

var createNeighbourServerData = function()
{
	var nighbourFlagsCount = 3;
	var serverVar = {};
	var serverVarType1 = "TAGS0";
	var serverVarType2 = "TAGS";
	
	$(".selected").each(function(){			
		console.log($(this).find("td"));
		$(this).find("td").each(function(){
			
			if($(this).index() >2 && $(this).index() <7)
			{
				//console.log(nighbourFlagsCount);
				nighbourFlagsCount++;
				if(nighbourFlagsCount<=9)
				{
					
					serverVar[serverVarType1+""+nighbourFlagsCount] = $(this).text();
				}
				else
				{
					serverVar[serverVarType2+""+nighbourFlagsCount] = $(this).text();
				}						
				//console.log(nighbourFlagsCount+"-"+$(this).index());
			}				
		});
	});
	
	$(".self_flag_check").each(function(){
		if($(this).is(":checked"))
		{
			var tr = $(this).parent().parent();
			$(tr).find("td").each(function(){
				
				if($(this).index() >2 && $(this).index() <7)
				{
					//console.log(nighbourFlagsCount);
					nighbourFlagsCount++;
					var value_data = $(this).text();
					if($(this).index() == 3)
					{
						value_data =3;
					}
					if(nighbourFlagsCount<=9)
					{						
						
						serverVar[serverVarType1+""+nighbourFlagsCount] = value_data;
					}
					else
					{
						serverVar[serverVarType2+""+nighbourFlagsCount] = value_data;
					}						
					//console.log(nighbourFlagsCount+"-"+$(this).index());
				}				
			});
			
		}
	});
	
	console.log(serverVar);
	return serverVar;
}




var parseCellData = function(data)
{
	var tr = ""
		$("#neigh_form").html("");
	$("#neigh_form").html("<tr><td></td><td>Self</td><td style='display:none;'>STATUS</td><td>PLMN</td><td>LAC</td><td>CELL</td><td>Rx Lvl(dBm)</td><td>Latitude</td><td>Longitude</td></tr>");
	for(var i =1;i<=parseInt(data.BSNos);i++)
	{
		
		 var aaa = { 
				  	"MCC" :""+data["Cell"+i].PLMN.substring(0, 3),
				  	"MNC":""+data["Cell"+i].PLMN.substring(3, data["Cell"+i].PLMN.length),		  
				  	"LAC":data["Cell"+i].LAC,
				  	"CELL":data["Cell"+i].CELL_ID
		  			}
		  
		  
		  	var latLong = getLatLonCells(aaa);
		 if(latLong.lat != "-1" && latLong.lon != "-1" )
			{	
		
				tr = '<tr id="'+data["Cell"+i]+'"><td><input type="checkbox" class="neigh_flag_check"></td><td><input type="checkbox" class="self_flag_check"></td><td style="display:none;">2</td><td>'+data["Cell"+i].PLMN+'</td><td>'+data["Cell"+i].LAC+'</td><td>'+data["Cell"+i].CELL_ID+'</td><td>'+data["Cell"+i].RSSI+'</td><td>'+latLong.lat+'</td><td>'+latLong.lon+'</td></tr>';
				$("#neigh_form").append(tr);
		}
		else
		{
			tr = '<tr id="'+data["Cell"+i]+'"><td><input type="checkbox" class="neigh_flag_check"></td><td><input type="checkbox" class="self_flag_check"></td><td style="display:none;">2</td><td>'+data["Cell"+i].PLMN+'</td><td>'+data["Cell"+i].LAC+'</td><td>'+data["Cell"+i].CELL_ID+'</td><td>'+data["Cell"+i].RSSI+'</td><td>NA</td><td>NA</td></tr>';
			$("#neigh_form").append(tr);
		}		
	}	
}



var parseCellDatawithMap = function(data,i,latLong,distance)
{

	
	//for(var i =1;i<=parseInt(data.BSNos);i++)
	//{
		
		 var aaa = { 
				  	"MCC" :""+data["Cell"+i].PLMN.substring(0, 3),
				  	"MNC":""+data["Cell"+i].PLMN.substring(3, data["Cell"+i].PLMN.length),		  
				  	"LAC":data["Cell"+i].LAC,
				  	"CELL":data["Cell"+i].CELL_ID
		  			}
		  
		  
		if(i <=2)
		{
				var style="style='background-color:"+((i==1)?"red;'":"orange;'");
				var name =  (i==1)?"Self":"Pushed";
			    tr = '<tr '+style+' id="'+data["Cell"+i]+'"><td>'+i+'</td><td colspan="2">'+name+'</td><td style="display:none;">2</td><td>'+data["Cell"+i].PLMN+'</td><td>'+data["Cell"+i].LAC+'</td><td>'+data["Cell"+i].CELL_ID+'</td><td>'+data["Cell"+i].RSSI+'</td><td>NA</td><td>NA</td><td>'+distance+'</td><td>'+data["Cell"+i].ARFCN+'</td><td>'+data["Cell"+i].BSIC+'</td></tr>';
				$("#neigh_form").append(tr);
		}
		else if(latLong.lat != "-1" && latLong.lon != "-1" )
		{	
		
				tr = '<tr id="'+data["Cell"+i]+'"><td>'+i+'</td><td><input type="checkbox" class="neigh_flag_check"></td><td><input type="checkbox" class="self_flag_check"></td><td style="display:none;">2</td><td>'+data["Cell"+i].PLMN+'</td><td>'+data["Cell"+i].LAC+'</td><td>'+data["Cell"+i].CELL_ID+'</td><td>'+data["Cell"+i].RSSI+'</td><td>'+latLong.lat+'</td><td>'+latLong.lon+'</td><td>'+distance+'</td><td>'+data["Cell"+i].ARFCN+'</td><td>'+data["Cell"+i].BSIC+'</td></tr>';
				$("#neigh_form").append(tr);
		}
		else
		{
				tr = '<tr id="'+data["Cell"+i]+'"><td>'+i+'</td><td><input type="checkbox" class="neigh_flag_check"></td><td><input type="checkbox" class="self_flag_check"></td><td style="display:none;">2</td><td>'+data["Cell"+i].PLMN+'</td><td>'+data["Cell"+i].LAC+'</td><td>'+data["Cell"+i].CELL_ID+'</td><td>'+data["Cell"+i].RSSI+'</td><td>NA</td><td>NA</td><td>Unknown</td><td>'+data["Cell"+i].ARFCN+'</td><td>'+data["Cell"+i].BSIC+'</td></tr>';
				$("#neigh_form").append(tr);
		}		
	//}	
}
var checkBoxClickEvent = function()
{
	$('.neigh_flag_check').click(function(){		
		/*if($(this).is(":checked"))
		{		   
		   var lat = $(this).parent().parent().find("td:eq(8)").text();
		   var lon = $(this).parent().parent().find("td:eq(9)").text();
		   if(lat == "NA" || lon=="NA")
		   {
			   $(this).trigger("click");
			   return false;
		   }		   
		}
		else
		{
			
		}*/
		   
		var	count = 0;		
		$(".neigh_flag_check").each(function(){			
			var element = $(this).parent().parent().find("td:eq(1) input");
			if($(this).is(":checked"))
			{
			   count++;
			   $(this).parent().parent().addClass("selected");
			   
			   
			   
			   
			}
			else
			{
				$(this).parent().parent().removeClass("selected");
				$(element).removeAttr("disabled");
				
			}
		});
	
		if(count>=6)
		{
			$(".neigh_flag_check").attr("disabled", true);
			$(".neigh_flag_check").each(function(){			
				if($(this).is(":checked"))
				{
					$(this).removeAttr("disabled");
				}			
			});
		}
		else
		{
			$(".neigh_flag_check").removeAttr("disabled");
		}
		if(count > 0)
		{
			if(!$("#ne_flag").is(":checked"))
			{
				$("#ne_flag").trigger("click");
			}
			
			
		}else
		{
			if($("#ne_flag").is(":checked"))
			{
				$("#ne_flag").trigger("click");
			}
		}
	});
	
	$(".self_flag_check").click(function(){
		
		/*if($(this).is(":checked"))
		{		   
		   var lat = $(this).parent().parent().find("td:eq(8)").text();
		   var lon = $(this).parent().parent().find("td:eq(9)").text();
		   if(lat == "NA" || lon=="NA")
		   {
			   $(this).trigger("click");
			   return false;
		   }		   
		}
		else
		{
			
		}*/
		
		var chekdedCount = 0;
		$(".self_flag_check").each(function(){
			
			if($(this).is(":checked"))
			{
				chekdedCount++;
				//$("#ne_flag").prop("checked",true);
				$(this).parent().parent().addClass("selected_neigh");
			}
			else
			{
				$(this).attr({"disabled":"disabled"});
			}
		});
		if(chekdedCount ==0)
		{
			$(".self_flag_check").each(function(){
				$(this).removeAttr("disabled");
			});
			
			var	countNeCheck = 0;		
			
			$(".neigh_flag_check").each(function(){			
				if($(this).is(":checked"))
				{
					countNeCheck++;				   
				}
			});
			
			if(countNeCheck > 0)
			{
				
			}
			else
			{
				//$("#ne_flag").prop("checked",false);
			}
		}
	});
	
	$("#show_map").click(function()
	{		
		if($(this).is(":checked"))
		{
			imageLayer.setVisible(false);
			$("#show_hide_map_label").html("");
			$("#show_hide_map_label").html("Hide Map");
			//centerCircleLayer.setVisible(false);
			//axisSourceLayer.setVisible(false);
			$("#show_map_after").prop("checked",true);
		}
		else
		{
			imageLayer.setVisible(true);
			$("#show_hide_map_label").html("");
			$("#show_hide_map_label").html("Show Map");
			//centerCircleLayer.setVisible(true);
			//axisSourceLayer.setVisible(true);
			$("#show_map_after").prop("checked",false);
			
		}
	});
	$("#show_map_after").click(function()
			{		
				if($(this).is(":checked"))
				{
					imageLayer.setVisible(false);
					$("#show_hide_map_label").html("");
					$("#show_hide_after_map_label").html("Hide Map");
					//centerCircleLayer.setVisible(false);
					//axisSourceLayer.setVisible(false);
				}
				else
				{
					imageLayer.setVisible(true);
					$("#show_hide_map_label").html("");
					$("#show_hide_after_map_label").html("Show Map");
					//centerCircleLayer.setVisible(true);
					//axisSourceLayer.setVisible(true);
					
				}
			});
	
	$("#detect_radio").click(function(){
		$("#imsi_number").removeAttr("disabled");
		$("#imei_number").removeAttr("disabled");
		if(!$("#ne_flag").is(":checked"))
		{
			$("#ne_flag").trigger("click");
			$("#ne_flag").attr({"disabled":"disabled"});
		}
	});
	$("#chng_ngh_radio").click(function(){
		$("#imsi_number").attr({"disabled":"disabled"});
		$("#imei_number").attr({"disabled":"disabled"});
		if(!$("#ne_flag").is(":checked"))
		{
			$("#ne_flag").trigger("click");
			$("#ne_flag").attr({"disabled":"disabled"});
		}
	});
	$("#poll").click(function(){
		$("#imsi_number").removeAttr("disabled");
		$("#imei_number").attr({"disabled":"disabled"});
		if($("#ne_flag").is(":checked"))
		{
			$("#ne_flag").removeAttr("disabled");
			$("#ne_flag").trigger("click");
			
		}
	});
	$("#page").click(function(){
		$("#imsi_number").removeAttr("disabled");
		$("#imei_number").attr({"disabled":"disabled"});
		if($("#ne_flag").is(":checked"))
		{
			$("#ne_flag").removeAttr("disabled");
			$("#ne_flag").trigger("click");
			
		}
	});
	
}



//load map first

/*var getMapData =  function()
{
	$.ajax({
			url:"../Operations",
			data:{	
					"methodName":"getMapData"
				},
				datatype:"json",
				type:"post",
				async:true,
				success:function(data)
				{				
					var data = jQuery.parseJSON(data);
					placeMapDataOverToMAp(data,false);
					
					getCellsData();
				},
				error:function()
				{
					
				}	
		});
}*/

var getMapData =  function()
{
	getCellsData();
}

var placeMapDataOverToMAp = function(data,neflag)
{  

	var bounds = new google.maps.LatLngBounds();
  
  for (var i in data) 
  {
	  var content = "<table border=1><tr><td style='background: cornsilk' colspan=2>"+data[i].name+"</td></tr><tr><td>MCC</td><td>"+data[i].mcc+"</td></tr><tr><td>MNC</td><td>"+data[i].mnc+"</td></tr><tr><td>LAC</td><td>"+data[i].lac+"</td></tr><tr><td>CELL</td><td>"+data[i].cell+"</td></tr><tr><td>Latitude</td><td>"+data[i].lat+"</td></tr><tr><td>Longitude</td><td>"+data[i].lon+"</td></tr></table>"
	  var marker = new google.maps.Marker({
			position: {lat: parseFloat(data[i].lat), lng:parseFloat(data[i].lon)},
			map: map,
			title: data[i].name,
			icon:"../resources/images/"+data[i].img,
			//icon:"../resources/images/blink_tower.gif",			
			content:content,
			optimized: false
		});
	  
          var infowindow = new google.maps.InfoWindow();
          google.maps.event.addListener(marker, 'click', (function (marker, i, infowindow) {
              return function () {
                  //console.log('Klick! Marker=' + this.content);
                  infowindow.setContent(this.content);
                  infowindow.open(map, this);
              };
          })(marker, i, infowindow));
          bounds.extend(marker.position);

          //google.maps.event.trigger(marker, 'click');
          
          marker.cell_lat = data[i].lat;
    	  marker.cell_lon = data[i].lon;
    	  marker.cell_mcc = data[i].mcc;
    	  marker.cell_mnc = data[i].mnc;
    	  marker.cell_cell = data[i].cell;
    	  marker.cell_lac = data[i].lac;
    	  marker.cell_img = data[i].img;
    	  marker.isNeigh = neflag;
    	  mapDataMarkers.push(marker);
   }
  
    //google.maps.event.trigger(map, 'resize');
	map.setCenter({lat:21.2618911,lng:81.5490329});
	map.setZoom(13);
	//testicong();
}


var placeCellsMapDataOverToMAp = function(data)
{  

  //var bounds = new google.maps.LatLngBounds();	
	
  
	var tr = ""
		$("#neigh_form").html("");
	//$("#neigh_form").html("<tr><td>#</td><td>Neg</td><td>Self</td><td style='display:none;'>STATUS</td><td>PLMN</td><td>LAC</td><td>CELL</td><td>Rx</td><td>Latitude</td><td>Longitude</td><td>Distance</td></tr>");	
  for(var i =1;i<=parseInt(data.BSNos);i++) 
  {
	  //var check = matchCellsOnMap(data["Cell"+i].PLMN,data["Cell"+i].LAC,data["Cell"+i].CELL_ID);
	  
	  if(true)
	  {
		  
		  
		  var aaa = { 
				  	"MCC" :""+data["Cell"+i].PLMN.substring(0, 3),
				  	"MNC":""+data["Cell"+i].PLMN.substring(3, data["Cell"+i].PLMN.length),		  
				  	"LAC":data["Cell"+i].LAC,
				  	"CELL":data["Cell"+i].CELL_ID
		  			}
		  
		  
		  	var latLong = getLatLonCells(aaa);
		    
		    var distance = null;
		    if(latLong.lat != "-1" && latLong.lon != '-1')
		    {
		    	distance = distanceBetweenLatLon($("#lat").val(),$("#lon").val(),latLong.lat,latLong.lon)
		    }
		    else
		    {
		    	distance="unkown";
		    }
		    
		  	
		    parseCellDatawithMap(data,i,latLong,distance);
			if(latLong.lat != "-1" && latLong.lon != "-1" )
			{			
				data["Cell"+i].lat = latLong.lat;
				data["Cell"+i].lon = latLong.lon;
				/*var content = "<table border=1><tr><td style='background: cornsilk' colspan=2>"+("Cell"+i)+"</td></tr><tr><td>PLMN</td><td>"+data["Cell"+i].PLMN+"</td></tr><!--<tr><td>MNC</td><td>"+data["Cell"+i].mnc+"</td></tr>--><tr><td>LAC</td><td>"+data["Cell"+i].LAC+"</td></tr><tr><td>CELL</td><td>"+data["Cell"+i].CELL_ID+"</td></tr><tr><td>Latitude</td><td>"+data["Cell"+i].lat+"</td></tr><tr><td>Longitude</td><td>"+data["Cell"+i].lon+"</td></tr></table>"
		  
				var marker = new google.maps.Marker({
					position: {lat: parseFloat(data["Cell"+i].lat), lng:parseFloat(data["Cell"+i].lon)},
					map: map,
					title: "Cell"+i,
					icon:"../resources/images/blink_tower.gif",			
					content:content,
					optimized:false,
				});
				if(i == parseInt(data.BSNos))
					map.setCenter({lat: parseFloat(data["Cell"+i].lat), lng:parseFloat(data["Cell"+i].lon)});
	          var infowindow = new google.maps.InfoWindow();
	          google.maps.event.addListener(marker, 'click', (function (marker, i, infowindow) {
	              return function () {	                 
	                  infowindow.setContent(this.content);
	                  infowindow.open(map, this);
	              };
	          })(marker, i, infowindow));
	          bounds.extend(marker.position);          
	          
	          marker.cell_lat = data["Cell"+i].lat;
	    	  marker.cell_lon = data["Cell"+i].lon;
	    	  marker.cell_mcc = aaa.mcc;
	    	  marker.cell_mnc = aaa.mnc;
	    	  marker.cell_cell = data["Cell"+i].CELL_ID;
	    	  marker.cell_lac = data["Cell"+i].LAC;
	    	  marker.cell_img = "blink_tower.gif";
	    	  marker.isNeigh = true;
	    	  neighbourDataMarkers.push(marker);
	    	  mapDataMarkers.push(marker);*/
				
				//drawNeighBourCircleTower(map,10,[parseFloat(data["Cell"+i].lon),parseFloat(data["Cell"+i].lat)],"'"+i+"'",neighbourSource,neighbourSourceLayer);
				drawNeighBourCircleTower(map,10,[parseFloat(data["Cell"+i].lon),parseFloat(data["Cell"+i].lat)],"'"+i+"'");
				
				//drawCenterCircleTower(map,1000,[parseFloat(data["Cell"+i].lon),parseFloat(data["Cell"+i].lat)],"2",centerCircleLayerSource)
			}
	  }
	}
  $("#neigh_form").css('height','477px');
  for(var i=550;i<=2750;i=i+550)
	{
		if($("#lat").val() == "" || $("#lon").val()== "")
		{
			alert("Self Latitude and Longitide is not present");
		}
		else
		{
			drawCenterCircleTower(map,i,[parseFloat(($("#lon").val())),parseFloat($("#lat").val())],i,centerCircleLayerSource);
		}
		drawCenterCircleTower(map,2,[parseFloat(($("#lon").val())),parseFloat($("#lat").val())],i,centerCircleLayerSource);
		
		map.getView().setCenter([parseFloat(($("#lon").val())),parseFloat($("#lat").val())]);
		
		var newlatLon = translateCoordinates(100,parseFloat(($("#lat").val())),parseFloat(($("#lon").val())),60);		
		drawLine([parseFloat(($("#lon").val())),parseFloat($("#lat").val())],newlatLon,vectorSource);
		
		map.getView().setZoom(13.5);
	}
  
  
}

var matchTowerOnMap = function(mcc,mnc,lac,cell,lat,lon)
{
	/*for(var i in mapDataMarkers)
	{
		//if(mapDataMarkers[i].cell_lat == lat && mapDataMarkers[i].cell_lon == lon)
		if(mapDataMarkers[i].cell_mcc == mcc && mapDataMarkers[i].cell_mnc == mnc && mapDataMarkers[i].cell_lac == lac && mapDataMarkers[i].cell_cell == cell)
		{
			//mapDataMarkers[i].setIcon("../resources/images/blink_tower.gif");
			mapDataMarkers[i].setOptions({
            	icon :"../resources/images/blink_tower.gif",
            	optimized:false,
			});
		}		
		else
		{
			
			if(mapDataMarkers[i].isNeigh)
			{
				mapDataMarkers[i].setOptions({
	            	icon :"../resources/images/blue_old.png"
				});
			}
			placeMarker(lat,lon,"../resources/images/blink_tower.gif");
		}
	}*/
	placeMarker(lat,lon,"../resources/images/blink_tower.gif");
}

var matchCellsOnMap = function(PLMN,lac,cell)
{
	/*var f = false;
	//var len = Object.keys(mapDataMarkers).length;
	var b = 0;
	for(var i in mapDataMarkers)
	{
		b++;
		if((mapDataMarkers[i].cell_mcc+""+mapDataMarkers[i].cell_mnc == PLMN) && mapDataMarkers[i].cell_lac == lac && mapDataMarkers[i].cell_cell == cell)
		{
			
			mapDataMarkers[i].setOptions({
            	icon :"../resources/images/blink_tower.gif",
            	optimized:false
			});
			f=true;
			
			
		}	
	}*/
	return false;
}

var changeNeighbourTowerImages = function()
{
	for(var i in neighbourDataMarkers)
	{
		neighbourDataMarkers[i].setOptions({
        	icon :"../resources/images/blue_old.png"
		});
	}
}



var placeSearchedCellsOverToMAp = function(title,mcc,mnc,lac,cell,lat,lon)
{  
		var bounds = new google.maps.LatLngBounds();
		var content = "<table border=1><tr><td style='background: cornsilk' colspan=2>"+title+"</td></tr><tr><td>MCC</td><td>"+mcc+"</td></tr><tr><td>MNC</td><td>"+mnc+"</td></tr><tr><td>LAC</td><td>"+lac+"</td></tr><tr><td>CELL</td><td>"+cell+"</td></tr><tr><td>Latitude</td><td>"+lat+"</td></tr><tr><td>Longitude</td><td>"+lon+"</td></tr></table>"
		var marker = new google.maps.Marker({
					position: {lat: parseFloat(lat), lng:parseFloat(lon)},
					map: map,
					title: title,
					icon:"../resources/images/blink_tower.gif",			
					content:content,
					optimized:false,
				});				
	          var infowindow = new google.maps.InfoWindow();
	          google.maps.event.addListener(marker, 'click', (function (marker, i, infowindow) {
	              return function () {	                  
	                  infowindow.setContent(this.content);
	                  infowindow.open(map, this);
	              };
	          })(marker, i, infowindow));
	          bounds.extend(marker.position);
}



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

/*var triggerPage = function(element)
{
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","inline-block");
	
var row = $(element).parent().parent();
	
	var imsi = $(row).data("imsi");
	
	var msisdn = $(row).data("msisdn");
	
	//console.log(imsi+" - "+imei);
	if((msisdn == null || msisdn == ""))
	{
		alert("MSISDN Not Present");
		$('#message_box').css("display","block");
		$('#loadingBox').css("display","none");
		
		return false;
	}	
	cancelValue = "release";
	msisdn = msisdn;
	$.ajax({
		url:"../Operations",
		data:
		{	
				"methodName":"triggerPage",
				"msisdn":msisdn,
				"param":"PAGING",
				"flag":"4"
		},
		datatype:"json",
		type:"post",
		success:function(data2)
		{
			
		}
	});
}*/


var triggerPage = function(element,val)
{
	
	var row = $(element).parent().parent();
	
	if(parseInt($(row).data("status")) != 1)
	{
		alert("Call Is Already Active");
		return false;
	}
	
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","inline-block");
	

	
	var imsi = $(row).data("imsi");
	
	var msisdn = $(row).data("msisdn");
	var imsi = $(row).data("imsi");
	var imei = $(row).data("imei");
	//console.log(imsi+" - "+imei);
	if((msisdn == null || msisdn == ""))
	{
		alert("MSISDN Not Present");
		$('#message_box').css("display","block");
		$('#loadingBox').css("display","none");
		
		return false;
	}	
	cancelValue = "release";
	canCelReleaseElement = element;
	msisdn = msisdn;
	
	$.ajax({
		url:"../Operations",
		data:
		{	
				"methodName":"triggerPage",
				"msisdn":msisdn,
				"param":val,
				"flag":"4"
		},
		datatype:"json",
		type:"post",
		success:function(data2)
		{
			locatePoll(null,imei,imsi);
		}
	});
}



var triggerRelease = function(flag)
{
	var row = null;
	if(flag == null)
	{
		row = $(canCelReleaseElement).parent().parent();
	}
	else
	{
		row = $(flag).parent().parent();
	}
	
	var msisdn = $(row).data("msisdn");	
	
	if((msisdn == null || msisdn == ""))
	{
		alert("MSISDN Not Present");
		$('#message_box').css("display","block");
		$('#loadingBox').css("display","none");
		
		return false;
	}
	
	
	$.ajax({
			url:"../Operations",
			data:
			{	
					"methodName":"triggerPage",
					"msisdn":msisdn,
					"param":"RELEASE",
					"flag":"4"
			},
			datatype:"json",
			type:"post",
			success:function(data2)
			{
				window.location.reload();
			}
		});
		
}

var getListForImsiImei = function()
{
	$.ajax({
		url:"../Operations",
		data:
		{	
				"methodName":"getListForImsiImei",
				"cmd":"GETALL_SUBS_RADIO_DATA",
				"fileName":"radioData.json",
		},
		datatype:"json",
		type:"post",
		success:function(data2)
		{	
			
			data2 = jQuery.parseJSON(data2);
			console.log(data2);
			if(parseInt(data2.STATUS) == 3)
			{
				alert("Data Not Present");
			}
			else
			{
				createListTable(data2);
			}
			
		}
	});
}

var createListTable =  function(data)
{
	count=0;
	for(var i in data)
	{
		count++;
		if(i.indexOf("Rec")>=0)
		{
			var style = "";
			if(data[i].IMSI == "" || data[i].IMEI == "" || data[i].MSISDN == "" )
			{
				style="style='background-color:red;color:white;'"
			}
			var imsi = data[i].IMSI.trim() == ""?"null":data[i].IMSI.trim();
			var MSISDN = data[i].MSISDN.trim() == ""?"null":data[i].MSISDN.trim();
			var IMEI = data[i].IMEI.trim() == ""?"null":data[i].IMEI.trim();
			var TA = data[i].TA.trim() == ""?"null":data[i].TA.trim();
			var RXLVL = data[i].RXLVL.trim() == ""?"null":data[i].RXLVL.trim();
			var RREVENT = data[i].RREVENT.trim() == ""?"null":data[i].RREVENT.trim();
			
			var TIMESTAMP = data[i].TIMESTAMP.trim() == ""?"null":data[i].TIMESTAMP.trim();
			
			var STATUS = data[i].STATUS.trim() == ""?"null":data[i].STATUS.trim();
			
			console.log(data[i].STATUS);
			var statusPoll = "";
			
			if((data[i].STATUS) != 1 && (data[i].STATUS) != 0 )
			{
				statusPoll = "<option value=7>Status Poll</option>"
			}
			var d = new Date(parseInt(data[i].TIMESTAMP) * 1000);
			console.log(d.getDate()+"-"+d.getDate()+"-"+d.getFullYear()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds());
			var table =  "<tr "+style+" data-imsi="+imsi+" data-msisdn='"+MSISDN+"' data-imei='"+IMEI+"' data-ta='"+TA+"'" +
					"data-rxlvl="+RXLVL+" data-rrevent="+RREVENT+" data-timestamp="+TIMESTAMP+" data-status="+STATUS+">" +
					"<td>"+data[i].IMSI+"</td>" +
					"<td>"+data[i].MSISDN+"</td>" +
					"<td>"+data[i].IMEI+"</td>" +
					"<td>"+data[i].TA+"</td>" +
					"<td>"+(parseInt(data[i].RXLVL)-110)+"</td>" +
					"<td>"+RREVENTArray[data[i].RREVENT]+"</td>" +					
					"<td>"+d.getDate()+"-"+d.getDate()+"-"+d.getFullYear()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+"</td>" +
					"<td>"+STATUS_ACTION_MAP[data[i].STATUS]+"</td>" +
					"<!--<td><button class='btn-match' onclick='livePoll(this)'>Live Poll</button><button class='btn-match' onclick='triggerPage(this)'>Page</button><button class='btn-match' onclick='poll(this)'>Poll</button></td>-->" +
					"<td>" +
					"<button class='btn-match' onclick='livePoll(this)'>Live Poll</button>" +
					"<button class='btn-match' onclick='poll(this)'>Poll</button>" +
					"<select id='"+i+count+"' style='width: 131px;'>" +
						"<option value=-1>Select</option>" +						
						"<option value=2>Silent Call</option>" +
						"<option value=3>Alert</option>" +
						"<option value=4>Silent Connect</option>" +
						"<option value=6>Release</option>" +
						statusPoll+""+
						"<option value=8>Locate</option>" +
						//"<option value=5>Poll</option>" +
						//"<option value=1>Live Poll</option>" +
					"</select>" +
					"<button class='btn-match' onclick=actions(this,'"+i+count+"')>GO</button></td>"+
			"</tr>";
		}
		$("#list_table_body").append(table);
	}
}


var actions = function(self,id)
{
	var element = $("#"+id).parent().parent();
	switch(parseInt($("#"+id).val()))
	{
		case -1:
			alert("Please Select Action to perform");
		break;
		case 1:
			livePoll(self);
		break;
		case 2:			
			triggerPage(self,"PAGING");
		break;
		case 3:
			triggerPage(self,"ALERT");
		break;
		case 4:
			triggerPage(self,"CONNECT");
		break;
		case 5:
			poll(self);
		break;
		case 6:
			if(parseInt($(element).data("status")) == 1)
			{
				alert("Call Is Not Active");
			}
			else
			{
				
				triggerRelease(self);
			}
			
		break;
		case 7:
			statusPoll(self,null);
		case 8:
			triggerPage(self,"RRLP");
		break;
	}
}

var livePoll = function(element)
{	
	var row = $(element).parent().parent();	
	var imsi = $(row).data("imsi");	
	var imei = $(row).data("imei");
	
	console.log(imsi+" - "+imei);
	if((imsi == null || imsi == "") && (imei == null || imei == ""))
	{
		alert("IMSI & IMEI Are Not Present");
	}
	else
	{
		window.location ="../views/livepoll.jsp?imsi="+imsi+"&imei="+imei;
	}
}

var poll = function(element)
{	
	var row = $(element).parent().parent();	
	var imsi = $(row).data("imsi");	
	var imei = $(row).data("imei");
	
	console.log(imsi+" - "+imei);
	if((imsi == null || imsi == ""))
	{
		alert("IMSI Is Not Present");
	}
	else
	{
		window.location ="../views/target.jsp?imsi="+imsi;
	}
}

var cancelClick = function()
{
	switch (cancelValue)
	{
		case "reload":
			location.reload();
		break;
		case "release":
			triggerRelease(null);
			break;
	}
}

var addImsiImei =  function()
{
	if(($("#add_imsi").val().trim() == "" && $("#add_imei").val().trim() == ""))
	{
		alert("Please Provide Valid IMSI OR IMEI");
		return false;
	}
	else
	{
		if($("#add_imsi").val().trim().length >=1  )
		{
			if(isNaN($("#add_imsi").val()) ||$("#add_imsi").val().trim().length != 15 )
			{
				alert("Please Provide Valid IMSI");
				return false;
			}
		}
		if($("#add_imei").val().trim().length >=1  )
		{
			if(isNaN($("#add_imei").val()) ||$("#add_imei").val().trim().length != 15 )
			{
				alert("Please Provide Valid IMEI");
				return false;
			}
		}
	}
	serverCalldata = 
	{
			"cmd":"GETALL_SUB_TRACK_REQ",
			"fileName":"target.json",
			"flag":"5",
			"imsi":$("#add_imsi").val(),
			"imei":$("#add_imei").val(),
			"methodName":"serverCall"
	}
	$.ajax({
		url:"../Operations",
		data:serverCalldata,
		datatype:"json",
		type:"post",
		beforeSend :function()
		{
			showLoading();
		},
		success:function(data2)
		{
			data2 = jQuery.parseJSON(data2);
			if(parseInt(data2.STATUS) == 3 || (data2.STATUS.indexOf("ERR(")>-1))
			{
				alert("Error while saving");
			}
			hideLoading();
			location.reload();
		},
		error:function()
		{
			hideLoading();
		},
		complete:function()
		{
			hideLoading();
		}
	});	
}


var resetAll =  function()
{	
	serverCalldata = 
	{
			"cmd":"GETALL_SUB_TRACK_REQ",
			"fileName":"target.json",
			"flag":"6",
			"imsi":$("#add_imsi").val(),
			"imei":$("#add_imei").val(),
			"methodName":"serverCall"
	}
	$.ajax({
		url:"../Operations",
		data:serverCalldata,
		datatype:"json",
		type:"post",
		beforeSend :function()
		{
			showLoading();
		},
		success:function(data2)
		{
			data2 = jQuery.parseJSON(data2);
			if(parseInt(data2.STATUS) == 3 || (data2.STATUS.indexOf("ERR(")>-1))
			{
				alert("Error while Reset");
			}
			hideLoading();
			location.reload();
		},
		error:function()
		{
			hideLoading();
		},
		complete:function()
		{
			hideLoading();
		}
	});	
}

var showLoading = function()
{
	var html = "<div id='ageloadBox'></div>";
	$("#container").prepend(html);
	$("#ageloadBox").css("dispaly","block");
}

var hideLoading = function()
{
	//var html = "<div id='ageloadBox'></div>";
	$("#ageloadBox").remove();
}


var locatePoll = function(t_id,imei,imsi)
{
	t_id= Date.now();
	var flag=1;
	
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","inline-block");
	$.ajax({
		url:"../Operations",
		data:
		{	
				"methodName":"livepoll",
				"imsi":imsi,
				"imei":imei,
				"t_id":t_id,
				"flag":flag
		},
		datatype:"json",
		type:"post",
		success:function(data)
		{			
			var data = jQuery.parseJSON(data);
			
			
			//sleep(1);
		
			if(parseInt(data.STATUS) == 3 || (data.STATUS.indexOf("ERR(")>-1))
			{	
				locatePoll(t_id,imei,imsi);				
			}
			else if(parseInt(data.STS) == 1)
			{		
				locatePoll(t_id,imei,imsi);				
			}
			else
			{
				
				createSelfLoginTable(data);
				locatePoll(t_id,imei,imsi);
				//triggerSms(data.MSISDN,t_id);
			}
		},
		error:function()
		{
			
		}	
	});
}


var createSelfLoginTable  = function(data)
{
	mcc = data.MCC.substring(0,3);
	table = "<fieldset style='width:610px;background:white;text-align:center;margin:0 auto;'>" +
			"<legend><strong>Detected</strong></legend>" +
			"<table border=1 style='margin:0 auto;max-height:500px;overflow-y:scroll;'>" +
			"<thead><!--<th>TI</th>-->" +
			"<th>IMSI</th><th>MSISDN</th><th>IMEI</th><th>TA</th><th>RXLVL</th><th>MCC</th><th>MNC</th><th>LAC</th>" +
			"<th>CELL</th><!--<th>LAT</th><th>LON</th>--><thead>" +
			"<tbody id='cellinfoBody'><tr><!--<td>"+data.TI+"</td>--><td>"+data.IMSI+"</td><td>"+data.MSISDN+"</td><td>"+data.IMEI+"</td><td>"+data.TA+"</td><td>"+(parseInt(data["RXLVL"])-110)+"</td><td>"+mcc+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><!--<td>"+data.lat+"</td><td>"+data.lon+"</td>--></tr>" +
					"</tbody></table></fieldset>";
	
	//$("#loadingBox").append(table);
	if(printFlag == 0)
	{
		printFlag = 1;
		$("#loadingBox").append(table);
	}else
	{
var topColor = $("#cellinfoBody tr:eq(0)").css("background-color");
		
		var color = (topColor =="rgb(238, 238, 238)")?"rgb(255, 255, 255)":"rgb(238, 238, 238)";
		
		$("#cellinfoBody").prepend("<tr style='background-color:"+color+";' ><!--<td>"+data.TI+"</td>--><td>"+data.IMSI+"</td><td>"+data.MSISDN+"</td><td>"+data.IMEI+"</td><td>"+data.TA+"</td><td>"+(parseInt(data["RXLVL"])-110)+"</td><td>"+mcc+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><!--<td>"+data.lat+"</td><td>"+data.lon+"</td>--><tr>");
	}
	
}

var statusPoll = function(element,val)
{	
	var row = $(element).parent().parent();
	/*if(parseInt($(row).data("status")) != 1)
	{
		alert("Call Is Already Active");
		return false;
	}*/
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","inline-block");
	
	var imsi = $(row).data("imsi");
	var msisdn = $(row).data("msisdn");
	var imsi = $(row).data("imsi");
	var imei = $(row).data("imei");
	
	if((msisdn == null || msisdn == ""))
	{
		alert("MSISDN Not Present");
		$('#message_box').css("display","block");
		$('#loadingBox').css("display","none");
		
		return false;
	}	
	cancelValue = "release";
	canCelReleaseElement = element;
	msisdn = msisdn;
	locatePoll(null,imei,imsi);
}


function sleep(seconds)
{
    var waitUntil = new Date().getTime() + seconds*1000;
    while(new Date().getTime() < waitUntil) true;
}