var globalAllScheduledSubscribers=[];
var mapDataMarkers = [];
var neighbourDataMarkers = [];
var map;
var globalSubscriberValue="";
var globalTrackedValue="";
var globalGetPacketData={};
var statusMap =['UNKNOWN','IDLE','USER_BUSY','MS_PURGED','IMSI_DETACHED','NOT_REACHABLE_REASON_RESTRICTED_AREA','NOT_REGISTERED','NOT_PROVIDED_FROM_VLR'];

var searchSubscriber = function()
{
	var idType = $('input[name="type"]:checked').val();
	var reqType = $("#req_type").val();
	var cmdType = "GET_GEO_LOC_REQ";
	var vlr = "null";
	var hlr = "null";
	var ftn = "null";
	var msc = "null";
	var imsi = $("#type_value").val();
	globalTrackedValue=imsi;
	var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
	var count=0;
	for(var i=0;i<globalAllScheduledSubscribers.length;i++){
	if(globalAllScheduledSubscribers[i].type_value==$("#type_value").val()){
	count++;
	}
	}
	if(count==0){
	if($('#search').text().toLowerCase()=='get'){
	globalGetPacketData=packetData;
	callServer(packetData);
	setTimeout(checkTrackResponse,60000);
	}else{
	scheduleSubscriberSearch(packetData);
	}
	}else{
	alert('Job of this subscriber is already scheduled.Pls check Scheduler section for details');
	return false;
	}
	}
	
	var checkTrackResponse = function(){
	if($("#bottomdiv").css("display")=="none"){
	alert("error in getting response");
	//$("#topdiv").css("display","table");
	//$("#loadingBox").css("display","none");
	window.location.reload();
	}
	}

var createServerPacketData = function(cmdType,idType,reqType,vlr,hlr,ftn,imsi)
{
	var data =
	{
		"cmdType":cmdType,
		"reqType":reqType,
		"idType":idType,
		"type_value":imsi,
		"vlr":vlr,
		"hlr":hlr,
		"ftn":ftn,
		"msc":ftn,
		"fileName":"getGeoLoc.json"
	};
	return data;
	
}


var callServer = function(packetData)
{
	packetData.methodName = "getReq";
	
	$.ajax({
			url:"../Operations",
			data:packetData,
			datatype:"json",
			type:"post",
			success:function(data2)
			{
				data2 = JSON.parse(data2);
				console.log(data2);
				if((data2.STATUS.indexOf("ERR(")>-1))
				{
					alert("Operation Fail "+data2.STATUS);
				}
				else
				{
					if(packetData.cmdType == 'GET_GEO_LOC_REQ' &&  packetData.reqType == 2)
					{
						//parseLocData(data2);
					}
					else
					{
						$("#topdiv").css("display","none");
						$("#loadingBox").css("display","none");
						$("#bottomdiv").css("display","block");
						parseInt(data2.TAGS01) == 0?alert("success"):alert("Fail");
					}
				}
				
			}
		});
}

var parseLocData = function(data)
{
		
		hmcc = "";
		hmnc = "";	
		if(data.TAGS02 >=5)
		{
			hmcc = data.TAGS02.substring(0, 3);
			hmnc = data.TAGS02.substring(3, 5);
		}
		
		var today = new Date();
		year = today.getFullYear();
		month = (today.getMonth()+1) >9?(today.getMonth()+1):"0"+(today.getMonth()+1);
		date = (today.getDate())>9?(today.getDate()):"0"+(today.getDate());
		hour = (today.getHours())>9?(today.getHours()):"0"+(today.getHours());
		min = (today.getMinutes())>9?(today.getMinutes()):"0"+(today.getMinutes());
		sec = (today.getSeconds()) >9?(today.getSeconds()):"0"+(today.getSeconds());
		
		var dateTime = year+"-"+month+"-"+date+" "+hour+":"+min+":"+sec;
		console.log(dateTime)
		
	
	$("#res_MSISDN").val(data.TAGS03);
	$("#res_IMSI").val(data.TAGS02);
	$("#res_IMEI").val(data.TAGS04);
	$("#res_HMCC").val(hmcc);
	$("#res_HMNC").val(hmnc);
	$("#res_HOMCC").val(data.TAGS05);
	$("#HOMNC").val(data.TAGS06);
	$("#res_LAC").val(data.TAGS07);
	$("#res_CELL").val(data.TAGS08);
	$("#res_s_state").val(statusMap[data.TAGS16]);
	$("#res_date").val(dateTime);
	$("#res_cordinate").val(data.TAGS19);
	$("#res_hlr").val(data.TAGS18);
	$("#res_vlr").val(data.TAGS13);
	$("#res_ftn").val(data.TAGS17);
	$("#res_msc").val(data.TAGS14);
	$("#topdiv").css("display","none");
	$("#bottomdiv").css("display","block");
	saveLocDataOnSErver();	
	var cor = data.TAGS19.split(",");
	data = 
	{
			"MCC":data.TAGS05,
			"MNC":data.TAGS06,
			"LAC":data.TAGS07,
			"CELL":data.TAGS08,
			
	}
	var cellData = getLatLon(data);
	console.log(cellData);
	if(cellData.lat != -1 && cellData.lon != -1)
	{
		console.log("plotting the tower");
		//addMarker([parseFloat(cellData.lat),parseFloat(cellData.lon)],"bnsl1.png",null,null,"text");
		var marker = placeMarker(parseFloat(cellData.lat),parseFloat(cellData.lon),'../resources/images/bnsl1.png');
		google.maps.event.trigger(map, 'resize');
		map.setCenter(marker.getPosition());
		
	}
	if(data.TAGS19 != null && data.TAGS19 != "" && data.TAGS19 != "0.000000,0.000000" )
		{
			//addMarker([parseFloat(cor[1]),parseFloat(cor[0])],"ril.png",null,null,"text");
			//map.getView().setCenter([parseFloat(cor[1]),parseFloat(cor[0])]);
			//map.getView().setZoom(13.5);
			var marker = placeMarker(parseFloat(cor[0]),parseFloat(cor[1]),'../resources/images/'+"ril.png");
			google.maps.event.trigger(map, 'resize');
			map.setCenter(marker.getPosition());
			
			
		}
		
	//addMarker([25,25],"ril.png",null,null,"text");
	
	//map.getView().setZoom(13.5);
	//map.updateSize();
	//$("#map_type").trigger("change");
}

var saveLocDataOnSErver = function()
{
	var data = "'"+$("#res_MSISDN").val()+"', "+
	"'"+$("#res_IMSI").val()+"', "+
	"'"+$("#res_IMEI").val()+"', "+
	"'"+$("#res_HMCC").val()+"', "+
	"'"+$("#res_HMNC").val()+"', "+
	"'"+$("#res_HOMCC").val()+"', "+
	"'"+$("#HOMNC").val()+"', "+
	"'"+$("#res_LAC").val()+"', "+
	"'"+$("#res_CELL").val()+"', "+
	"'"+statusMap.indexOf($("#res_s_state").val())+"', "+	
	"'"+$("#res_cordinate").val()+"', "+
	"'"+$("#res_hlr").val()+"', "+
	"'"+$("#res_vlr").val()+"', "+
	"'"+$("#res_ftn").val()+"', "+
	"'"+$("#res_date").val()+"',"+
	"'"+$("#res_msc").val()+"'";
	var packetData = 
	{
		"methodName":"insertGeoLocData",
		"data":data
	};
	
	$.ajax({
		url:"../Operations",
		data:packetData,
		datatype:"json",
		type:"post",
		success:function(data2)
		{
			console.log(data2);							
		}
	});
}
var urlParam = function(name, w){
    w = w || window;
    var rx = new RegExp('[\&|\?]'+name+'=([^\&\#]+)'),
        val = w.location.search.match(rx);
    return !val ? '':val[1];
	}

$(document).ready(function()
{
valueSelected=urlParam('value');
if(valueSelected!="" && valueSelected!=null){
globalTrackedValue=valueSelected;
//if()
$("#topdiv").css("display","none");
$("#loadingBox").css("display","none");
$('#rescan').css("display","none");
$("#bottomdiv").css("display","block");
}else{
$('#rescan').css("display","block");
}
var ws = new WebSocket("ws://"+ipAddress+":8080/locator/geodata");
ws.onopen = function()
{
	console.log("connected to the server");
}
ws.onmessage = function(event)
{
var msg=event.data;
if(msg.toLowerCase()!="connection established"){
	var data = JSON.parse(msg);
	globalSubscriberValue=data.TAGS03;
	if(globalTrackedValue==data.TAGS02 || globalTrackedValue==data.TAGS03){
	if((data.STATUS.indexOf("ERR(")>-1))
	{
	alert("Operation Fail "+data.STATUS);
	}else{
	parseLocDataWithoutSave(data);
	}
	}
	console.log(data);
}else{
console.log(msg);
}	
}
ws.onclose = function()
{
	var r = confirm("Disconected From Server.Press ok to reconect");
	if (r == true) {
	    location.reload();
	} else {
	    
	}
}

	getAllScheduledSubscribers();
	$("#schedulerPeriodicity").keyup(function(){
	if($(this).val()!=''){
	if(isNaN($(this).val()) || parseFloat($(this).val())==0){
	alert('Please provide valid periodicity');
	}else{
	$('#search').text("Schedule");
	}
	}else{
	$('#search').text("Get");
	}
});
	registerEvents();
	//initMap();
	//getMapData();
	
	
	
	/***************************************************/
	
});

var registerEvents = function()
{
			
			$('#search').click(function()
			{		
				if($("#type_value").val().length  ==0 || isNaN($("#type_value").val()))
				{
					alert("Please provide valid value");
					return;
				}
				var idType = $('input[name="type"]:checked').val();
				if(idType == 2  && $("#type_value").val().length != 12)
				{
					alert("Please provide valid MSISDN");
					return;
				}
				if(idType == 1  && $("#type_value").val().length != 15)
				{
					alert("Please provide valid IMSI");
					return;
				}
				$("#loadingBox").css("display","table");
				$("#req_tab").css("display","none");
				searchSubscriber();
				
			});
			$('#block_service').click(function()
			{		
				
				
				//var idType = $('input[name="type"]:checked').val();
				var idType = 1;
				var reqType = 5;
				var cmdType = "GET_GEO_LOC_REQ";
				var vlr = $("#res_vlr").val();
				var hlr = $("#res_hlr").val();
				if(vlr==null||vlr==""||isNaN(vlr))
				{
					alert("VLR is not available");
					return;
				}
				if(hlr==null||hlr==""||isNaN(hlr))
				{
					alert("HLR is not available");
					return;
				}
				var ftn = $("#res_ftn").val();
				if(ftn==null||ftn=="")
				{
					ftn = "null";
				}
				var msc = $("#res_msc").val();
				if(msc==null||msc==""||isNaN(msc))
				{
					alert("msc is not available");
					return;
				}
					
				var imsi = $("#res_IMSI").val();								
				var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
				buttonLoading();
				callServer(packetData);
				
			});
			$('#diveret_service').click(function()
			{		
				
				//var idType = $('input[name="type"]:checked').val();
				var idType = 1;
				var reqType = 6;
				var cmdType = "GET_GEO_LOC_REQ";
				//var vlr = "null";
				//var hlr = "null";
				var vlr = $("#res_vlr").val();
				var hlr = $("#res_hlr").val();
				var ftn = $("#res_ftn").val();				
				if(vlr==null||vlr==""||isNaN(vlr))
				{
					alert("VLR is not available");
					return;
				}
				if(hlr==null||hlr==""||isNaN(hlr))
				{
					alert("HLR is not available");
					return;
				}
				if(ftn==null||ftn=="")
				{
					alert("FTN is not available");
					return;
				}
				var msc = $("#res_msc").val();
				if(msc==null||msc=="")
				{
					msc = "null";
				}
				var imsi = $("#res_IMSI").val();			
				
				var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
				buttonLoading();
				callServer(packetData);
				
			});
			
			$('#stop_call_divert').click(function()
					{		
						
						
						var idType = 1;
						var reqType = 10;
						var cmdType = "GET_GEO_LOC_REQ";
						//var vlr = "null";
						//var hlr = "null";
						var vlr = $("#res_vlr").val();
						var hlr = $("#res_hlr").val();
						var ftn = $("#res_ftn").val();				
						if(vlr==null||vlr==""||isNaN(vlr))
						{
							alert("VLR is not available");
							return;
						}
						if(hlr==null||hlr==""||isNaN(hlr))
						{
							alert("HLR is not available");
							return;
						}
						if(ftn==null||ftn=="")
						{
							alert("FTN is not available");
							return;
						}
						var msc = $("#res_msc").val();
						if(msc==null||msc=="")
						{
							msc = "null";
						}
						var imsi = $("#res_IMSI").val();			
						
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
						buttonLoading();
						callServer(packetData);
						
					});
			
			$('#sms_intercept').click(function()
					{		
						
				//var idType = $('input[name="type"]:checked').val();
						var idType = 1;
						var reqType = 8;
						var cmdType = "GET_GEO_LOC_REQ";
						//var vlr = "null";
						//var hlr = "null";
						var vlr = $("#res_vlr").val();
						var hlr = $("#res_hlr").val();
						var ftn = $("#res_ftn").val();				
						if(vlr==null||vlr==""||isNaN(vlr))
						{
							alert("VLR is not available");
							return;
						}
						if(hlr==null||hlr==""||isNaN(hlr))
						{
							alert("HLR is not available");
							return;
						}
						if(ftn==null||ftn=="")
						{
							ftn = "null";
						}
						var msc = $("#res_msc").val();
						if(msc==null||msc==""||isNaN(msc))
						{
							alert("msc is not available");
							return;
						}
							
						var imsi = $("#res_IMSI").val();								
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
						buttonLoading();
						//callServer(packetData);
						
						var redirectSmsInterCeptToliverScreen = function(data)
						{
							$("#topdiv").css("display","none");
							$("#loadingBox").css("display","none");
							$("#bottomdiv").css("display","block");
							parseInt(data2.TAGS01) == 0?alert("success"):alert("Fail");
							if(parseInt(data2.TAGS01))
							{
								var win = window.open("livesmsintercept.jsp?msisdn="+$("#res_MSISDN").val(), '_blank');
								win.focus();
							}
						}	
						serverCommands(packetData,"../Operations","redirectSmsInterCeptToliverScreen",'json');
						
						
					});
			
			
			$('#stop_sms_intercept').click(function()
					{		
						
				
						var idType = 1;
						var reqType = 9;
						var cmdType = "GET_GEO_LOC_REQ";
						//var vlr = "null";
						//var hlr = "null";
						var vlr = $("#res_vlr").val();
						var hlr = $("#res_hlr").val();
						var ftn = $("#res_ftn").val();				
						if(vlr==null||vlr==""||isNaN(vlr))
						{
							alert("VLR is not available");
							return;
						}
						if(hlr==null||hlr==""||isNaN(hlr))
						{
							alert("HLR is not available");
							return;
						}
						if(ftn==null||ftn=="")
						{
							ftn = "null";
						}
						var msc = $("#res_msc").val();
						if(msc==null||msc==""||isNaN(msc))
						{
							alert("msc is not available");
							return;
						}
							
						var imsi = $("#res_IMSI").val();								
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
						buttonLoading();
						callServer(packetData);
						
					});
			
			
			
			
			$('#trace_intercept').click(function()
					{		
						
				//var idType = $('input[name="type"]:checked').val();
						var idType = 1;
						var reqType = 7;
						var cmdType = "GET_GEO_LOC_REQ";
						//var vlr = "null";
						//var hlr = "null";
						var vlr = $("#res_vlr").val();
						var hlr = $("#res_hlr").val();
						var ftn = $("#res_ftn").val();				
						if(vlr==null||vlr==""||isNaN(vlr))
						{
							alert("VLR is not available");
							return;
						}
						if(hlr==null||hlr==""||isNaN(hlr))
						{
							alert("HLR is not available");
							return;
						}
						if(ftn==null||ftn=="")
						{
							ftn = "null";
						}
						var msc = $("#res_msc").val();
						if(msc==null||msc==""||isNaN(msc))
						{
							alert("msc is not available");
							return;
						}
							
						var imsi = $("#res_IMSI").val();								
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi);
						buttonLoading();
						callServer(packetData);
						
					});
			$('#stop_call_intercept').click(function()
					{		
						
				//var idType = $('input[name="type"]:checked').val();
						var idType = 1;
						var reqType = 9;
						var cmdType = "GET_GEO_LOC_REQ";
						//var vlr = "null";
						//var hlr = "null";
						var vlr = $("#res_vlr").val();
						var hlr = $("#res_hlr").val();
						var ftn = $("#res_ftn").val();				
						if(vlr==null||vlr==""||isNaN(vlr))
						{
							alert("VLR is not available");
							return;
						}
						if(hlr==null||hlr==""||isNaN(hlr))
						{
							alert("HLR is not available");
							return;
						}
						if(ftn==null||ftn=="")
						{
							ftn = "null";
						}
						var msc = $("#res_msc").val();
						if(msc==null||msc==""||isNaN(msc))
						{
							alert("msc is not available");
							return;
						}
							
						var imsi = $("#res_IMSI").val();								
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi,msc);
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi);
						buttonLoading();
						callServer(packetData);
						
					});
			
			$('#rescan').click(function(){
				//window.location.reload();
				callServer(globalGetPacketData);
				setTimeout(checkTrackResponse,60000);
			});		
			$(".type_check").click(function(){
				if($(this).val() ==2)
				{
					$("#type_value").attr({"placeholder":"MSISDN"});
				}
				else if($(this).val() ==1)
				{
					$("#type_value").attr({"placeholder":"IMSI"});
				}
			});
	
			$("#map_type").change(function(){
				if($(this).val() == 1)
				{
					imageLayer.setVisible(false);
					openSeaMapLayer.setVisible(true);
					localMap.setVisible(false);
				}
				if($(this).val() == 2)
				{
					imageLayer.setVisible(false);
					openSeaMapLayer.setVisible(false);
					localMap.setVisible(true);
				}
				if($(this).val() == 3)
				{
					imageLayer.setVisible(true);
					openSeaMapLayer.setVisible(false);
					localMap.setVisible(false);
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

}


	
	


//*********************************Openplayer **************************************************************
var extent = [-180.0000, -90.0000, 180.0000, 90.0000]



var projection = new ol.proj.Projection({
        code: 'EPSG:4326',
		extent:extent,
		units:'m',
		metersPerUnit:1
      });

//alert(mapServerIp);
	  
var  localMap = new ol.layer.Tile({
    source: new ol.source.OSM({
        url: 'http://'+mapServerIp+'/hot/{z}/{x}/{y}.png',
    	//url: new ol.source.OSM(),
   crossOrigin: null
     })
  });

var openSeaMapLayer = new ol.layer.Tile({
    source: new ol.source.OSM()
})

var imageLayer = new ol.layer.Image({
            source: new ol.source.ImageStatic({
              url: '../resources/images/bg.jpg',
              imageExtent: extent,			  
              projection: projection,
            })
          })
		  
var vectorSource = new ol.source.Vector({projection: projection});
var vectorLayer = new ol.layer.Vector({source: vectorSource});


var neighbourSource = new ol.source.Vector({
            projection: 'EPSG:4326'
        });
		
var neighbourSourceLayer = new ol.layer.Vector({
            source: neighbourSource
        });
		
		
var centerCircleLayerSource = new ol.source.Vector({
            projection: 'EPSG:4326'
        });
		
var centerCircleLayer = new ol.layer.Vector({
            source: centerCircleLayerSource
        });
var detectedCircleSource = new ol.source.Vector({
    projection: 'EPSG:4326'
});

var detectedCircleLayer = new ol.layer.Vector({
    source: detectedCircleSource
});

var axisSource = new ol.source.Vector({
    projection: 'EPSG:3857'
});

var axisSourceLayer = new ol.layer.Vector({
    source: axisSource
});


var routeSource = new ol.source.Vector({
    projection: 'EPSG:4326'
});

var routeSourceLayer = new ol.layer.Vector({
    source: routeSource
});

  

/*var map = new ol.Map({
        layers: [openSeaMapLayer,localMap,imageLayer,neighbourSourceLayer,centerCircleLayer,vectorLayer,axisSourceLayer,detectedCircleLayer,routeSourceLayer],
        target: 'map_div',
        view: new ol.View({
          projection: projection,
          center: ol.extent.getCenter(extent),
          //,maxZoom:22,
		  //minZoom:12,
          zoom: 5
		  
        })
      });*/



var drawCenterCircleTower = function(map, radius,center,markerNumber,centerCircleLayerSource) {
    var CircleStyle = new ol.style.Style({
		stroke: new ol.style.Stroke({
			color: '#00d600',
			width: 5
		}),
		fill: new ol.style.Fill({
			color: [0,0,0,0]
		})
    //,text:new ol.style.Text({text:markerNumber})
	});
	
	var view = map.getView();
    
	var projection = view.getProjection();    
	
	var resolutionAtEquator = view.getResolution();    
	
	var center = center;
	
	var pointResolution = projection.getPointResolution(resolutionAtEquator, center);
    
	var resolutionFactor = resolutionAtEquator/pointResolution;
    
	var radius = (radius / ol.proj.METERS_PER_UNIT.m) * resolutionFactor;
    
	var circle = new ol.geom.Circle(center, radius);
    
	var circleFeature = new ol.Feature(circle);
	
	circleFeature.setStyle(CircleStyle);	
	
	centerCircleLayerSource.addFeature(circleFeature); 		
}


var drawCenterHighlightedCircleCircleTower = function(map, ta,center1,color1) {
    
	var radius = (parseInt(ta)+1)*550;
	
	var CircleStyle = new ol.style.Style({
		stroke: new ol.style.Stroke({
			color: color1,
			width: 5
		}),
		fill: new ol.style.Fill({
			color: [0,0,0,0]
		})
    //,text:new ol.style.Text({text:markerNumber})
	});
	
	var view = map.getView();
    
	var projection = view.getProjection();    
	
	var resolutionAtEquator = view.getResolution();    
	
	var center = [parseFloat(center1[0]),parseFloat(center1[1])];
	
	var pointResolution = projection.getPointResolution(resolutionAtEquator, center);
    
	var resolutionFactor = resolutionAtEquator/pointResolution;
    
	var radius = (radius / ol.proj.METERS_PER_UNIT.m) * resolutionFactor;
    
	var circle = new ol.geom.Circle(center, radius);
    
	var circleFeature = new ol.Feature(circle);
	
	circleFeature.setStyle(CircleStyle);	
	
	centerCircleLayerSource.addFeature(circleFeature); 		
}


var drawCircle = function(map, ta,center1,color1) {
    
	var radius = ta;
	
	var CircleStyle = new ol.style.Style({
		stroke: new ol.style.Stroke({
			color: color1,
			width: 5
		}),
		fill: new ol.style.Fill({
			color: color1
		})
    //,text:new ol.style.Text({text:markerNumber})
	});
	
	var view = map.getView();
    
	var projection = view.getProjection();    
	
	var resolutionAtEquator = view.getResolution();    
	
	var center = [parseFloat(center1[0]),parseFloat(center1[1])];
	
	var pointResolution = projection.getPointResolution(resolutionAtEquator, center);
    
	var resolutionFactor = resolutionAtEquator/pointResolution;
    
	var radius = (radius / ol.proj.METERS_PER_UNIT.m) * resolutionFactor;
    
	var circle = new ol.geom.Circle(center, radius);
    
	var circleFeature = new ol.Feature(circle);
	
	circleFeature.setStyle(CircleStyle);	
	
	detectedCircleSource.addFeature(circleFeature); 		
}



//var drawNeighBourCircleTower = function(map, radius,center,markerNumber,neighbourSource) {
var drawNeighBourCircleTower = function(map, radius,center,markerNumber) {
        var CircleStyle = new ol.style.Style({
			stroke: new ol.style.Stroke({
				color: '#0000ff',
				width: 4
			}),
			fill: new ol.style.Fill({
				color: "#0000ff"
			}),
			text:new ol.style.Text({text:markerNumber,fill:new ol.style.Fill({color: "#ffffff"})})
		});
		
		var view = map.getView();
        var projection = view.getProjection();
        
		var resolutionAtEquator = view.getResolution();
        
		var center = center;
		var pointResolution = projection.getPointResolution(resolutionAtEquator, center);
        var resolutionFactor = resolutionAtEquator/pointResolution;
        var radius = (radius / ol.proj.METERS_PER_UNIT.m) * resolutionFactor;
		

        var circle = new ol.geom.Circle(center, radius);
        var circleFeature = new ol.Feature(circle);
		circleFeature.setStyle(CircleStyle);
		
		neighbourSource.addFeature(circleFeature); 		
    }

var addMarker = function(cordinate,imageType,vectorSource1,data,displayText)
{

	var iconStyle = new ol.style.Style({
		image: new ol.style.Icon
		(({
		  anchor: [0.5, 46],
		  anchorXUnits: 'fraction',
		  anchorYUnits: 'pixel'
		  ,src: '../resources/images/'+imageType
		}))/*,text: new ol.style.Text({
            text: ""+displayText,
            offsetY: -25,
            fill: new ol.style.Fill({
                color: '#fff'
            })
        })*/	
	  });

	var  cordinate1 = cordinate;
	
	console.log(parseFloat(cordinate[1]));
	
	var iconFeature = new ol.Feature({
		
		geometry: new ol.geom.Point([parseFloat(cordinate[0]),parseFloat(cordinate[1])]),
		name: 'Null Island',
		population: 4000,
		rainfall: 500
	  });
	  
	iconFeature.setStyle(iconStyle);	
	vectorSource.addFeature(iconFeature);
}

var distanceBetweenLatLon = function(lat1, lon1, lat2, lon2) {
	  var p = 0.017453292519943295;    // Math.PI / 180
	  var c = Math.cos;
	  var a = 0.5 - c((lat2 - lat1) * p)/2 + 
	          c(lat1 * p) * c(lat2 * p) * 
	          (1 - c((lon2 - lon1) * p))/2;

	  return ((12742 * Math.asin(Math.sqrt(a)))*1000).toFixed(2); // 2 * R; R = 6371 km
	}

function radians(n) 
{
	  return n * (Math.PI / 180);
	}
	function degrees(n) {
	  return n * (180 / Math.PI);
	}

	function getBearing(startLat,startLong,endLat,endLong){
	  startLat = radians(startLat);
	  startLong = radians(startLong);
	  endLat = radians(endLat);
	  endLong = radians(endLong);

	  var dLong = endLong - startLong;

	  var dPhi = Math.log(Math.tan(endLat/2.0+Math.PI/4.0)/Math.tan(startLat/2.0+Math.PI/4.0));
	  if (Math.abs(dLong) > Math.PI){
	    if (dLong > 0.0)
	       dLong = -(2.0 * Math.PI - dLong);
	    else
	       dLong = (2.0 * Math.PI + dLong);
	  }

	  return (degrees(Math.atan2(dLong, dPhi)) + 360.0) % 360.0;
}
	
	
var drawLine = function(cordinate1,cordinate2,vectorSource)
	{
		
	var lineStyle = new ol.style.Style({
	    stroke: new ol.style.Stroke({
	        color: '#00d600',
	        width: 3
	    }),
	    fill: new ol.style.Fill({
	        color: '#00d600'
	    })
	});
	var line = null;
	var a =1
	  for(var i=0;i<4;i++ )
	  {
		  if(i == 0)
		  {
			  line = new ol.Feature(new ol.geom.LineString([cordinate1,[cordinate1[0]+a,cordinate1[1]]]));  
		  }
		  if(i== 1)
		  {
			  line = new ol.Feature(new ol.geom.LineString([cordinate1,[cordinate1[0]-a,cordinate1[1]]]));
		  }
		  if(i == 2)
		  {
			  line = new ol.Feature(new ol.geom.LineString([cordinate1,[cordinate1[0],cordinate1[1]+a]]));  
		  }
		  if(i== 3)
		  {
			  line = new ol.Feature(new ol.geom.LineString([cordinate1,[cordinate1[0],cordinate1[1]-a]]));
		  }		  
		  line.setStyle(lineStyle);
		  axisSource.addFeature(line);
	  }
	  
}



//**********************************************************************************************************

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



/*var createRoute = function(polyline) {
    // route is ol.geom.LineString
    var route = new ol.format.Polyline({
      factor: 1e5
    }).readGeometry(polyline, {
      dataProjection: 'EPSG:4326',
      featureProjection: 'EPSG:3857'
    });
    var feature = new ol.Feature({
      type: 'route',
      geometry: route
    });
    feature.setStyle(styles.route);
    vectorSource.addFeature(feature);
  }*/

var getNearestPath = function(point1,point2)
{
	url_osrm_nearest = '//router.project-osrm.org/nearest/v1/driving/',
    url_osrm_route = '//router.project-osrm.org/route/v1/driving/',
	$.getJSON( (url_osrm_route + point1 + ';' + point2), function( json ) {
		  console.log( "JSON Data: " + json);
		  createRoute(json.routes[0].geometry);
		 });
	
}

var createRoute = function(polyline) {
	
	styles = {
		      route: new ol.style.Style({
		        stroke: new ol.style.Stroke({
		          width: 6, color: [256, 40, 40, 1]
		        })
		      })
		    };
	
    // route is ol.geom.LineString
    var route = new ol.format.Polyline({
      factor: 1e5
    }).readGeometry(polyline,{
        dataProjection: 'EPSG:4326',
        featureProjection: 'EPSG:4326'
      });
   
    var feature = new ol.Feature({
      type: 'route',
      geometry: route
    });
    feature.setStyle(styles.route);
    routeSource.addFeature(feature);
  }


var buttonLoading = function()
{
	$("#topdiv").css("display","table");
	$("#req_tab").css("display","none");
	$("#loadingBox").css("display","block");
	$("#bottomdiv").css("display","none");
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
	  
 mapDataMarkers.push(marker);
	
	return marker;
	
}

function clearMap(){
for(var i in mapDataMarkers){
mapDataMarkers[i].setMap(null);
}
}



var serverCommands = function(data,url,callBackFunction,type)
{
	
	
	data.methodName = "getReq";
	
	
	$.ajax
	({
			url:url,
			data:data,
			type:'post',
			dataType:type,
			success:function(data)
			{
				callBackFunction(data);
			}
	});

}

var scheduleSubscriberSearch = function(packetData){
	packetData.methodName = "scheduleSubscriberSearch";
	packetData.periodicity=""+parseInt($('#schedulerPeriodicity').val())*60*1000;
	packetData.nibIp=$('#nib_ip_val_head').text();
	packetData.firstDelayRequired="yes";
	
	
	$.ajax
	({
			url:"../Operations",
			data:packetData,
			type:'post',
			success:function(data)
			{
				console.log(data);
			}
	});	
}

var getAllScheduledSubscribers = function(){	
	$.ajax
	({
			url:"../Operations",
			data:{"methodName":"getAllScheduledSubscribers"},
			type:'post',
			async:false,
			success:function(data)
			{
			if(data.length!=0){
			var displayData=JSON.parse(data);
			globalAllScheduledSubscribers=displayData;
			}
			}
	});
	}
	
	var parseLocDataWithoutSave = function(data)
{
	    clearMap();
		hmcc = "";
		hmnc = "";	
		if(data.TAGS02 >=5)
		{
			hmcc = data.TAGS02.substring(0, 3);
			hmnc = data.TAGS02.substring(3, 5);
		}
		
		var today = new Date();
		year = today.getFullYear();
		month = (today.getMonth()+1) >9?(today.getMonth()+1):"0"+(today.getMonth()+1);
		date = (today.getDate())>9?(today.getDate()):"0"+(today.getDate());
		hour = (today.getHours())>9?(today.getHours()):"0"+(today.getHours());
		min = (today.getMinutes())>9?(today.getMinutes()):"0"+(today.getMinutes());
		sec = (today.getSeconds()) >9?(today.getSeconds()):"0"+(today.getSeconds());
		
		var dateTime = year+"-"+month+"-"+date+" "+hour+":"+min+":"+sec;
		console.log(dateTime)
		
	
	$("#res_MSISDN").val(data.TAGS03);
	$("#res_IMSI").val(data.TAGS02);
	$("#res_IMEI").val(data.TAGS04);
	$("#res_HMCC").val(hmcc);
	$("#res_HMNC").val(hmnc);
	$("#res_HOMCC").val(data.TAGS05);
	$("#HOMNC").val(data.TAGS06);
	$("#res_LAC").val(data.TAGS07);
	$("#res_CELL").val(data.TAGS08);
	$("#res_s_state").val(statusMap[data.TAGS16]);
	$("#res_date").val(dateTime);
	$("#res_cordinate").val(data.TAGS19);
	$("#res_hlr").val(data.TAGS18);
	$("#res_vlr").val(data.TAGS13);
	$("#res_ftn").val(data.TAGS17);
	$("#res_msc").val(data.TAGS14);
	$("#topdiv").css("display","none");
	$("#bottomdiv").css("display","block");	
	var cor = data.TAGS19.split(",");
	cellParam = 
	{
			"MCC":data.TAGS05,
			"MNC":data.TAGS06,
			"LAC":data.TAGS07,
			"CELL":data.TAGS08,
			
	}
	var cellData = getLatLon(cellParam);
	console.log(cellData);
	if(cellData.lat != -1 && cellData.lon != -1)
	{
		console.log("plotting the tower");
		//addMarker([parseFloat(cellData.lat),parseFloat(cellData.lon)],"bnsl1.png",null,null,"text");
		var marker = placeMarker(parseFloat(cellData.lat),parseFloat(cellData.lon),'../resources/images/bnsl1.png');
		google.maps.event.trigger(map, 'resize');
		map.setCenter(marker.getPosition());
		
	}
	if(data.TAGS19 != null && data.TAGS19 != "" && data.TAGS19 != "0.000000,0.000000" )
		{
			//addMarker([parseFloat(cor[1]),parseFloat(cor[0])],"ril.png",null,null,"text");
			//map.getView().setCenter([parseFloat(cor[1]),parseFloat(cor[0])]);
			//map.getView().setZoom(13.5);
			var marker = placeMarker(parseFloat(cor[0]),parseFloat(cor[1]),'../resources/images/'+"ril.png");
			google.maps.event.trigger(map, 'resize');
			map.setCenter(marker.getPosition());
			
			
		}
		
	//addMarker([25,25],"ril.png",null,null,"text");
	
	//map.getView().setZoom(13.5);
	//map.updateSize();
	//$("#map_type").trigger("change");
}