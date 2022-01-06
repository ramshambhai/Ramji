/**
 *This is for the index page operations 
 */


var numbers = [];
var message = "";
var mapDataMarkers = [];
var neighbourDataMarkers = [];
var map;
var printFlag = 0;
var deliveryReport = "<table border=1 id='report'><tr><td>Number</td><td>Status</td></tr></table>";
/*This function will get the all the groups from the database*/

var getGroups = function()
{
	$.ajax({
		url:"../Operations",
		data:{"methodName":"getAllGroups"},
		type:"post",
		success:function(data)
		{
			data = eval(data);
			createSelectListForGroups(data);
		}	
	});
}
/*This funciton will append the options in to the select box for the groups*/
var createSelectListForGroups =function(data)
{
	var options = "";
	console.log(data);
	for(var i in data)
	{
		options += "<option value='"+data[i].id+"'>"+data[i].name+"</option>"
	}
	$("#groups").html("");
	$("#groups").html(options);
}

/*This function will get the phonenumbers for the selected groups*/
var getPhoneNumbersForGroups = function()
{
	console.log("'"+$("#groups").val().join("','")+"'");
	
	$.ajax({
		url:"../Operations",
		data:{"methodName":"getPhoneNumbersForGroupsWithName","groups":"'"+$("#groups").val().join("','")+"'"},
		type:"post",
		success:function(data)
		{
			data = eval(data);
			createSelectListForNumbers(data);
		}	
	});
}
/*This funciton will append the options in to the select box for the groups*/
var createSelectListForNumbers = function(data)
{
	var options = "";
	for(var i in data)
	{
		options += "<option value='"+data[i].number+"'>"+data[i].number+"-"+data[i].name+"</option>"
	}
	$("#numbers").html("");
	$("#numbers").append(options);
}


/*This funtion will place and onchange event on to the groups select box
 * Every time user will select a group it will send a request to the server to get the numbers for the 
 * selected groups
 * 
 * */

var getTheNumbersforGroupsEvent = function()
{f
	$("#groups").change(function(){
		
		getPhoneNumbersForGroups();
	});
}






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
	flag = 2;
	
	if(t_id != null )
	{
		flag = 1;
	}
	else
	{
		t_id= Date.now();
		flag = $('input[name=req_type]:checked').val();
	}
	
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","inline-block");
	$.ajax({
		url:"../Operations",
		data:
		{	
				"methodName":"locate",
				"imsi":""+$("#imsi_number").val(),
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
					//locate(t_id);
				}
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
		t_id= Date.now();
		flag = $('input[name=req_type]:checked').val();
	}
	
	var lengthOfNeigh = Object.keys(neighbourData).length;
	
	neighbourData.lengthOfNeigh = lengthOfNeigh;
	neighbourData.methodName = "locateWithNeighbour";
	neighbourData.imsi = $("#imsi_number").val();
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
				triggerSms(data.MSISDN,t_id);
				/*createSelfLoginTable(data);
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
				}*/
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
				data.NEIGH_DATA[i].lat = null;
				data.NEIGH_DATA[i].lon = null;
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
			placeSearchedCellsOverToMAp(data.NEIGH_DATA[i],data.NEIGH_DATA[i].MCC,data.NEIGH_DATA[i].MNC,data.NEIGH_DATA[i].LAC,data.NEIGH_DATA[i].CELL,data.NEIGH_DATA[i].lat,data.NEIGH_DATA[i].lon);
		}
	}
	placeMarker(data.sub.x,data.sub.y,"../resources/images/MobilePhone.png");
	
	placeMarker($("#lat").val(),$("#lon").val(),"../resources/images/green_old.png");
	data.lat = $("#lat").val();
	data.lon = $("#lon").val();
	createSelfTable(data);
	//$("#table_div").append("<fieldset id='neigh' style='width:630px;'><legend>Neighbour Cells</legend></fieldset>");
	$("#table_div").append("<fieldset id='neigh' style='width:610px;'><legend>Neighbour Cells</legend><table id='neigh_table' border=1>" +
			"<tr><td>Cell Name</td><td>MCC</td><td>MNC</td><td>LAC</td><td>CELL</td><td>BSIC</td><td>ARFCN</td><td>RXLVL</td><td>LAT</td><td>LON</td><tr>" +
			"</table>" +
			"</fieldset>");
		for(var i in data.NEIGH_DATA)
		{
			if(data.NEIGH_DATA[i].STATUS == 2 && data.NEIGH_DATA[i].lat != null && data.NEIGH_DATA[i].lon !=null )
			{
				createCellTable(data.NEIGH_DATA[i],i,"neigh_table");				
			}
		}
		
		//createDataToStoreOnServer(data);
		
		$("#sub_lat").text(data.sub.x);
		$("#sub_lon").text(data.sub.y);
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
        
		
        google.maps.event.trigger(map, 'resize');
		map.setCenter({lat:data.sub.x,lng:data.sub.y});
		map.setZoom(17);
		
		
}
var calulateSubscriberLocation = function(data)
{
	sum = parseInt(data["RXLVL"]);
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
			"<td>CELL</td><!--<td>LAT</td><td>LON</td>--><tr>" +
			"<tr><!--<td>"+data.TI+"</td>--><!--<td>"+data.IMSI+"</td>--><td>"+data.MSISDN+"</td><td>"+data.IMEI+"</td><td>"+data.TA+"</td><td>"+(parseInt(data["RXLVL"])-110)+"</td><td>"+data.MCC+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><!--<td>"+data.lat+"</td><td>"+data.lon+"</td>--><tr>" +
					"</table></fieldset>";
	
	$("#table_div").append(table);
}

var createSelfLoginTable  = function(data)
{
	mcc = data.MCC.substring(0,3);
	table = "<fieldset style='width:610px;background:white;text-align:center;margin:0 auto;'>" +
			"<legend><strong>Detected</strong></legend>" +
			"<table border=1 style='margin:0 auto;'>" +
			"<tr><!--<td>TI</td>-->" +
			"<!--<td>IMSI</td>--><td>MSISDN</td><td>IMEI</td><td>TA</td><td>RXLVL</td><td>MCC</td><td>MNC</td><td>LAC</td>" +
			"<td>CELL</td><!--<td>LAT</td><td>LON</td>--><tr>" +
			"<tr><!--<td>"+data.TI+"</td>--><!--<td>"+data.IMSI+"</td>--><td>"+data.MSISDN+"</td><td>"+data.IMEI+"</td><td>"+data.TA+"</td><td>"+(parseInt(data["RXLVL"])-110)+"</td><td>"+mcc+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><!--<td>"+data.lat+"</td><td>"+data.lon+"</td>--><tr>" +
					"</table></fieldset>";
	
	if(printFlag == 0)
	{
		printFlag = 1;
		$("#loadingBox").append(table);
	}
	
}

var createCellTable  = function(data,key,id)
{
	/*var htmt = "<table border=1>" +
			"<tr><td>Cell Name</td><td>MCC</td><td>MNC</td><td>LAC</td><td>CELL</td><td>BSIC</td><td>ARFCN</td><td>RXLVL</td><td>LAT</td><td>LON</td><tr>" +
					"<tr><td>"+key+"</td><td>"+data.MCC+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><td>"+data.BSIC+"</td><td>"+data.ARFCN+"</td><td>"+data.RXLVL+"</td><td>"+data.lat+"</td><td>"+data.lon+"</td><tr>" +
							"</table>";*/
	var htmt ="<tr><td>"+key+"</td><td>"+data.MCC+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><td>"+data.BSIC+"</td><td>"+data.ARFCN+"</td><td>"+(parseInt(data.RXLVL)-110)+"</td><td>"+data.lat+"</td><td>"+data.lon+"</td><tr>";
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

$(document).ready(function(){
	
	$('#search').click(function(){		
		if($("#imsi_number").val().trim() == "" || isNaN($("#imsi_number").val()) ||$("#imsi_number").val().trim().length != 15 )
		{
			alert("Please Provide Valid IMSI");
			return false;
		}
		if($("#lat").val().trim() == "" || isNaN($("#lat").val()) )
		{
			alert("Please Provide Valid Latitude");
			return false;
		}
		if($("#lon").val().trim() == "" || isNaN($("#lon").val()) )
		{
			alert("Please Provide Valid Longitude");
			return false;
		}
		locateSubscriber(null);
	});
	$('#rescan').click(function(){
		window.location.reload();
	});
	$('#cancelSearch').click(function(){
		window.location.reload();
	});
	
	/****************neighbour work*********************/
	$('#ne_flag').click(function(){	
		if($('#ne_flag').is(":checked"))
		{
			//$("#neigh_form").css('display','inline-block');
			$("#search").css('display','none');
			$("#searchWithNegh").css('display','block');
		}
		else
		{
			//$("#neigh_form").css('display','none');
			
			$(".neigh_flag_check").each(function(){				
				$(this).prop( "checked", false );
			});
			$(".neigh_flag_check").parent().parent().removeClass('selected');
			$("#search").css('display','block');
			$("#searchWithNegh").css('display','none');
		}
	});
	
	
	
	
	$("#searchWithNegh").click(function(){
		if($("#imsi_number").val().trim() == "" || isNaN($("#imsi_number").val()) ||$("#imsi_number").val().trim().length != 15 )
		{
			alert("Please Provide Valid IMSI");
			return false;
		}
		if($("#lat").val().trim() == "" || isNaN($("#lat").val()) )
		{
			alert("Please Provide Valid Latitude");
			return false;
		}
		if($("#lon").val().trim() == "" || isNaN($("#lon").val()) )
		{
			alert("Please Provide Valid Longitude");
			return false;
		}
		//if($(".selected").length >=3)
		if(true)
		{
			var data = createNeighbourServerData();
			locateWithNeighbour(null,data);
		}
		else
		{
			alert("Please select alteast 3 cells");
			return false;
		}
		
	});
	initMap();
	getMapData();
	
	
	
	/***************************************************/
	
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
				$("#lat").val(data.GPS_Data.GPSLAT);
				$("#lon").val(data.GPS_Data.GPSLONG);
				$("#ne_flag").removeAttr("disabled");
				parseCellData(data);
				//getCellsData();
				checkBoxClickEvent();
				//placeMapDataOverToMAp(data,true);
				placeCellsMapDataOverToMAp(data);
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
			
			if($(this).index() >1 && $(this).index() <6)
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
				
				if($(this).index() >1 && $(this).index() <6)
				{
					//console.log(nighbourFlagsCount);
					nighbourFlagsCount++;
					var value_data = $(this).text();
					if($(this).index() == 2)
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
	$("#neigh_form").html("<tr><td></td><td>Self</td><td style='display:none;'>STATUS</td><td>PLMN STR</td><td>LAC</td><td>CELL</td><td>Rx Lvl(dBm)</td><td>Latitude</td><td>Longitude</td></tr>");
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
	$("#neigh_form").css('height','477px');
	
}
var checkBoxClickEvent = function()
{
	$('.neigh_flag_check').click(function(){		
		
		var element = $(this).parent().parent().find("td:eq(1) input");
		/* if($(element).is(":checked"))
				   {
			   
			   			$(element).trigger("click");
			   			
				   }
		   if($(this).is(":checked"))
			{
			   $(element).attr({"disabled":"disabled"});
			 }
		   else
		   {
			   $(element).removeAttr("disabled");
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
		var chekdedCount = 0;
		$(".self_flag_check").each(function(){
			
			if($(this).is(":checked"))
			{
				chekdedCount++;
				$("#ne_flag").prop("checked",true);
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
				$("#ne_flag").prop("checked",false);
			}
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

  var bounds = new google.maps.LatLngBounds();	
	
  for(var i =1;i<=parseInt(data.BSNos);i++) 
  {
	  var check = matchCellsOnMap(data["Cell"+i].PLMN,data["Cell"+i].LAC,data["Cell"+i].CELL_ID);
	  if(!check)
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
				data["Cell"+i].lat = latLong.lat;
				data["Cell"+i].lon = latLong.lon;
				var content = "<table border=1><tr><td style='background: cornsilk' colspan=2>"+("Cell"+i)+"</td></tr><tr><td>PLMN</td><td>"+data["Cell"+i].PLMN+"</td></tr><!--<tr><td>MNC</td><td>"+data["Cell"+i].mnc+"</td></tr>--><tr><td>LAC</td><td>"+data["Cell"+i].LAC+"</td></tr><tr><td>CELL</td><td>"+data["Cell"+i].CELL_ID+"</td></tr><tr><td>Latitude</td><td>"+data["Cell"+i].lat+"</td></tr><tr><td>Longitude</td><td>"+data["Cell"+i].lon+"</td></tr></table>"
		  
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
	                  //console.log('Klick! Marker=' + this.content);
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
	    	  mapDataMarkers.push(marker);
			}
	  }
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
	          }));//(marker, i, infowindow));
	          bounds.extend(marker.position);
}

var createDataToStoreOnServer = function(data)
{
	var imsi = data.IMSI;
	var imei = $("#imsi_number").val();
	var data = [];
	var root = imsi+","+imei+","+data.TA+","+data.MCC+","+data.MNC+","+data.LAC+","+data.CELL+","+data.lat+","+data.lon+",'S'";
	data.push(root);
	for(var i in data.NEIGH_DATA)
	{
		if(data.NEIGH_DATA[1].status == 2 )
		{
			root= imsi+","+imei+",null,"+data.NEIGH_DATA[i].MCC+","+data.NEIGH_DATA[i].MNC+","+data.NEIGH_DATA[i].LAC+","+data.NEIGH_DATA[i].CELL+","+data.NEIGH_DATA[i].lat+","+data.NEIGH_DATA[i].lon,",'N'";
			data.push(root);
		}	
	}
	
	$.ajax({
		url:"../Operations",
		data:{"methodName":"InsertSearchData",data:data},
		type:"post",
		success:function(data)
		{
			
		}	
	});
	
	
}