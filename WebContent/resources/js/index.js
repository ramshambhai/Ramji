/**
 *This is for the index page operations 
 */


var numbers = [];
var message = "";
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
	}
	
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","block");
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
			if(parseInt(data.STS) == 1)
			{
				
					locate(t_id);	
				
			}
			else
			{
				if(parseInt(data.NEIGH_FLAG) == 1)
				{
					triggerSms(data.MSISDN,t_id);
				}
				else if(parseInt(data.NEIGH_FLAG) == 2)
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
	}
	
	var lengthOfNeigh = Object.keys(neighbourData).length;
	
	neighbourData.lengthOfNeigh = lengthOfNeigh;
	neighbourData.methodName = "locateWithNeighbour";
	neighbourData.imsi = $("#imsi_number").val();
	neighbourData.t_id = t_id;
	neighbourData.flag = flag;
	console.log(neighbourData);
	$('#message_box').css("display","none");
	$('#loadingBox').css("display","block");
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
			if(parseInt(data.STS) == 1)
			{
				
					locate(t_id);	
				
			}
			else
			{
				if(parseInt(data.NEIGH_FLAG) == 1)
				{
					triggerSms(data.MSISDN,t_id);
				}
				else if(parseInt(data.NEIGH_FLAG) == 2)
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


/*var sendMessage = function()
{
	var number = null;
	number = numbers.splice(0,1);	
	status="done";
//console.log($('#msg'));
//	var msg=$("#msg_val").val();
	$.ajax({
		//url:"../Operations",
		url:"http://10.151.96.36:13013/cgi-bin/sendsms?username=tester&password=foobar&to="+number+"&text="+message,
		type:"get",
		success:function(data)
		{
			//console.log(data);
			$("#report").append("<tr><td>"+number+"</td><td>"+status+"</td></tr>");
			alert(numbers.length >= 1)
			if(numbers.length >= 1)
			{
				
				sendMessage();
			}
		},
	error:function()
	{
		if(numbers.length >= 1)
		{
			
			sendMessage();
		}
		else
		{
			$("#message_box").html("Done");
		}
		
	}
	});
}*/
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
	for(var i in data.NEIGH_DATA)
	{
		if(data.NEIGH_DATA[i].STATUS == 2 && data.NEIGH_DATA[i].lat != null && data.NEIGH_DATA[i].lon !=null )
		{
			placeMarker(data.NEIGH_DATA[i].lat,data.NEIGH_DATA[i].lon,"../resources/images/blue_old.png");
		}
	}
	placeMarker(data.sub.x,data.sub.y,"../resources/images/MobilePhone.png");
	
	placeMarker($("#lat").val(),$("#lon").val(),"../resources/images/green_old.png");
	createSelfTable(data);
	//$("#table_div").append("<fieldset id='neigh' style='width:630px;'><legend>Neighbour Cells</legend></fieldset>");
	$("#table_div").append("<fieldset id='neigh' style='width:630px;'><legend>Neighbour Cells</legend><table id='neigh_table' border=1>" +
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
		
		
		
		$("#sub_lat").text(data.sub.x);
		$("#sub_lon").text(data.sub.y);
		$("#sub_IMSI").text($("#imsi_number").val());
		
		$("#target_scan").css("display","none");
		$("#reTarget").css("display","block");
		$("#sub_det").css("display","block");
		//$("#map_div").css("display","block");
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
	table = "<fieldset style='width:630px;'>" +
			"<legend>NIB</legend>" +
			"<table border=1>" +
			"<tr><!--<td>TI</td>-->" +
			"<td>IMSI</td><!--<td>MSISDN</td>--><td>IMEI</td><td>TA</td><td>RXLVL</td><td>MCC</td><td>MNC</td><td>LAC</td>" +
			"<td>CELL</td><!--<td>LAT</td><td>LON</td>--><tr>" +
			"<tr><!--<td>"+data.TI+"</td>--><td>"+data.IMSI+"</td><!--<td>"+data.MSISDN+"</td>--><td>"+data.IMEI+"</td><td>"+data.TA+"</td><td>"+(parseInt(data["RXLVL"])-110)+"</td><td>"+data.MCC+"</td><td>"+data.MNC+"</td><td>"+data.LAC+"</td><td>"+data.CELL+"</td><!--<td>"+data.lat+"</td><td>"+data.lon+"</td>--><tr>" +
					"</table></fieldset>";
	
	$("#table_div").append(table);
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


var map;
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
        icon: image
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
			alert("Please Provide Valid Logitude");
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
			$("#neigh_form").css('display','inline-block');
			$("#search").css('display','none');
			$("#searchWithNegh").css('display','block');
		}
		else
		{
			$("#neigh_form").css('display','none');
			
			$(".neigh_flag_check").each(function(){				
				$(this).prop( "checked", false );
			});
			$(".neigh_flag_check").parent().parent().removeClass('selected');
			$("#search").css('display','block');
			$("#searchWithNegh").css('display','none');
		}
	});
	
	
	
	
	$("#searchWithNegh").click(function(){
		if($(".selected").length >=3)
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
	var createNeighbourServerData = function()
	{
		var nighbourFlagsCount = 3;
		var serverVar = {};
		var serverVarType1 = "TAGS0";
		var serverVarType2 = "TAGS";
		$(".selected").each(function(){			
			console.log($(this).find("td"));
			$(this).find("td").each(function(){
				
				if($(this).index() != 0 && $(this).index() <5)
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
		console.log(serverVar);
		return serverVar;
	}
	
	
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
					$("#ne_flag").removeAttr("disabled");
					parseCellData(data);
					//getCellsData();
					checkBoxClickEvent();
				}				
			},
			error:function()
			{
				
			}	
		});
	}
	
	var parseCellData = function(data)
	{
		var tr = ""
			$("#neigh_form").html("");
		$("#neigh_form").html("<tr><td></td><td style='display:none;'>STATUS</td><td>PLMN STR</td><td>LAC</td><td>CELL</td><td>RS Level</td></tr>");
		for(var i =1;i<=parseInt(data.BSNos);i++)
		{
			tr = '<tr><td><input type="checkbox" class="neigh_flag_check"></td><td style="display:none;">2</td><td>'+data["Cell"+i].PLMN+'</td><td>'+data["Cell"+i].LAC+'</td><td>'+data["Cell"+i].CELL_ID+'</td><td>'+data["Cell"+i].RSSI+'</td></tr>';
			$("#neigh_form").append(tr);
		}
		
	}
	var checkBoxClickEvent = function()
	{
		$('.neigh_flag_check').click(function(){	
			
			
			var	count = 0;		
			$(".neigh_flag_check").each(function(){			
				if($(this).is(":checked"))
				{
				   count++;
				   $(this).parent().parent().addClass("selected");
				}
				else
				{
					$(this).parent().parent().removeClass("selected");
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
		});
	}	
	getCellsData();
	
	/***************************************************/
	
});

