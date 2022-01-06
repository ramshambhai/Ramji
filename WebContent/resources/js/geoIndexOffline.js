
var mapDataMarkers = [];
var neighbourDataMarkers = [];
var map;
var statusMap =['UNKNOWN','IDLE','USER_BUSY','MS_PURGED','IMSI_DETACHED','NOT_REACHABLE_REASON_RESTRICTED_AREA','NOT_REGISTERED','NOT_PROVIDED_FROM_VLR'];

var searchSubscriber = function()
{
	var idType = $('input[name="type"]:checked').val();
	var reqType = $("#req_type").val();
	var cmdType = "GET_GEO_LOC_REQ";
	var vlr = "null";
	var hlr = "null";
	var ftn = "null";
	var imsi = $("#type_value").val();
	var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi);
	callServer(packetData);
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
		"fileName":"getGeoLoc.json"
	};
	return data;
	
}


var callServer = function(packetData)
{
	
	
	$.ajax({
			url:"../Operations",
			data:packetData,
			datatype:"json",
			type:"post",
			success:function(data2)
			{
				console.log(data2);
				if(data2 != "")
					data2 = JSON.parse(data2);
				if(packetData.cmdType == 'GET_GEO_LOC_REQ' &&  packetData.reqType == 2)
				{
					parseLocData(data2)
				}
				else
				{
					$("#topdiv").css("display","none");
					$("#req_tab").css("display","none");
					$("#loadingBox").css("display","none");
					$("#bottomdiv").css("display","block");
					parseInt(data2.TAGS01) == 0?alert("success"):alert("Fail");
				}
			}
		});
}

var parseLocData = function(data)
{

	$("#res_MSISDN").val(data[0].msisdn);
	$("#res_IMSI").val(data[0].imsi);
	$("#res_IMEI").val(data[0].imei);
	$("#res_HMCC").val(data[0].hmcc);
	$("#res_HMNC").val(data[0].hmnc);
	$("#res_HOMCC").val(data[0].mcc);
	$("#HOMNC").val(data[0].mnc);
	$("#res_LAC").val(data[0].lac);
	$("#res_CELL").val(data[0].cell);
	$("#res_s_state").val(statusMap[data[0].state]);
	$("#res_date").val(data[0].logtime);
	$("#res_cordinate").val(data[0].coordinate);
	$("#res_hlr").val(data[0].hlr);
	$("#res_vlr").val(data[0].vlr);
	$("#res_ftn").val(data[0].ftn);
	var cor = data[0].coordinate.split(",");
	if(data[0].coordinate != null || data[0].coordinate != "")
	{
		addMarker([parseFloat(cor[1]),parseFloat(cor[0])],"ril.png",null,null,"text");
		map.getView().setCenter([parseFloat(cor[1]),parseFloat(cor[0])]);
		map.getView().setZoom(13.5);
	}
	
	//addMarker([25,25],"ril.png",null,null,"text");
	data = 
	{
			"MCC":data[0].mcc,
			"MNC":data[0].mnc,
			"LAC":data[0].lac,
			"CELL":data[0].cell,
			
	}
	var cellData = getLatLon(data);
	console.log(cellData);
	if(cellData.lat != -1 && cellData.lon != -1)
	{
		console.log("plotting the tower");
		addMarker([parseFloat(cellData.lat),parseFloat(cellData.lon)],"bnsl1.png",null,null,"text");
	}
	
	
	
	map.getView().setCenter([parseFloat(cor[1]),parseFloat(cor[0])]);
	map.getView().setZoom(13.5);
	map.updateSize();
	
	$("#map_type").trigger("change");
}
$(document).ready(function(){
	
	registerEvents();
	var packetData = {};
	packetData.methodName = "getGeoLocdataLocal";
	packetData.cmdType = 'GET_GEO_LOC_REQ';
	packetData.id = idOfLocData;
	packetData.reqType = 2;	
	callServer(packetData);
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
				var imsi = $("#res_IMSI").val();				
				var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi);
				packetData.methodName = "getReq";
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
				if(ftn==null||ftn=="")
				{
					alert("FTN is not available");
					return;
				}
				var imsi = $("#res_IMSI").val();			
				
				
				var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi);
				packetData.methodName = "getReq";
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
						var imsi = $("#res_IMSI").val();			
						
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi);
						packetData.methodName = "getReq";
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
						var imsi = $("#res_IMSI").val();			
						
						var packetData = createServerPacketData(cmdType,idType,reqType,vlr,hlr,ftn,imsi);
						packetData.methodName = "getReq";
						buttonLoading();
						callServer(packetData);
						
					});
			$('#rescan').click(function(){
				window.location.reload();
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

  

var map = new ol.Map({
        layers: [openSeaMapLayer,localMap,imageLayer,neighbourSourceLayer,centerCircleLayer,vectorLayer,axisSourceLayer,detectedCircleLayer,routeSourceLayer],
        target: 'map_div',
        view: new ol.View({
          projection: projection,
          center: ol.extent.getCenter(extent),
          //,maxZoom:22,
		  //minZoom:12,
          zoom: 5
		  
        })
      });



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

var buttonLoading = function()
{
	$("#topdiv").css("display","table");
	$("#req_tab").css("display","none");
	$("#loadingBox").css("display","block");
	$("#bottomdiv").css("display","none");
}
