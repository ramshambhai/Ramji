<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
<script>

var type = <%= "'"+request.getParameter("type")+"'" %>
var filter = <%= "'"+request.getParameter("filter")+"'" %>
var value = <%= request.getParameter("value") %>
var bts = <%= "'"+request.getParameter("bts")+"'" %>
var country = <%= "'"+request.getParameter("country")+"'" %>
var oprname = <%= "'"+request.getParameter("oprname")+"'" %>
var startDate = <%= "'"+request.getParameter("startTime")+"'" %>
var endDate = <%= "'"+request.getParameter("endTime")+"'" %>
</script>
<style>
.main-panel {
    background-color: #f4f3ef;
    position: relative;
    z-index: 2;
    float: none;
    width: 100%;
    min-height: 100%;
}
</style>
<div class="wrapper">
    <div class="main-panel">
    	<div id="map_leaf" style="width:100%;height:100%"></div>
    </div>
</div>
<link rel="stylesheet" href="../../resources/lib/leaflet/leaflet.css" />
<script src="../../resources/lib/leaflet/leaflet.js" ></script>
<script src="../../resources/lib/leaflet/Leaflet.GoogleMutant.js" ></script>
<script>

var map = null;
var mobileMarkerLayer =null;
var mobileMarkers = [];
var taCircleLayers = null;

function loadLeafMap()
{
	//var mymap = L.map('map_leaf').setView([51.505, -0.09], 13);
	map = new L.Map('map_leaf', {center: new L.LatLng(28.7041, 77.1025),  zoom: 13, minZoom: 0,maxZoom: 18});
	map.zoomControl.setPosition('bottomright');
	var googleMaplayer = L.gridLayer.googleMutant({
			type: 'roadmap' // valid values are 'roadmap', 'satellite', 'terrain' and 'hybrid'
		}).addTo(map);
	
	var openstreatMapLayer = L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: 'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'mapbox.streets'
	}).addTo(map);
	
	mobileMarkerLayer = L.layerGroup().addTo(map);
	taCircleLayers = L.layerGroup();
	
	var customControl =  L.Control.extend({

		  options: {
			position: 'bottomright'
		  },

		  onAdd: function (map) {
			var container = L.DomUtil.create('img');
			//container.type="checkbox";
			container.id="map_type";
			container.title="Online";
            container.src="../../resources/images/offline.png"  
			container.style.backgroundColor = 'white';
			$(container).addClass("offline");			
			
			container.style.width = '33px';
			container.style.height = '33px';
			
			container.onclick = function(){
			  console.log($(container));
			  //$(container)
			  if($(container).hasClass('offline')){
				 map.removeLayer(openstreatMapLayer);
				 //map.addLayer(googleMapLayer);
				 container.title="Offline";
				 $(container).removeClass("offline");	
				 $(container).addClass("online");	
				 container.src="../../resources/images/online.png"  
			}
			else {
				//map.removeLayer(googleMapLayer);
				map.addLayer(openstreatMapLayer);
				container.title="offline";
				$(container).addClass("offline");	
				 $(container).removeClass("online");
				 container.src="../../resources/images/offline.png"  
			}
			
			}
			return container;
		  }
		});
	 
	 map.addControl(new customControl());
}

var addmobile = function(data,iconUrl)
{
	//var mob = L.marker([28.7041, 77.1025],{icon:L.icon({iconUrl: '../../resources/images/mob_org.png'})}).addTo(map);
	var myIcon = L.icon
	({
			iconUrl: iconUrl,
			iconSize: [15, 15],
			//iconSize: [30, 30],
			//iconAnchor: [15, 28]
			iconAnchor: [7, 7]
	})
	//var mob = L.marker([data.lat,data.lon],{icon:L.icon({iconUrl: iconUrl})}).addTo(map);
	//var mob = L.marker([data.lat,data.lon],{icon:myIcon}).addTo(map);
	var latlng = data.loc.split(",");
	data.lat=latlng[0];
	data.lon=latlng[1];
	if(data.lat != null && data.lat != '')
	{
		
		
		var mob = L.marker([data.lat,data.lon],{icon:myIcon});
		mobileMarkerLayer.addLayer(mob);
		mob.bindPopup("<table border=1 class='table table-default'>"+
						"<thead></thead><tbody>"+
						"<tr><td>IMEI</td><td>"+data.imei+"</td></tr>"+
						"<tr><td>IMSI</td><td>"+data.imsi+"</td></tr>"+
						"<tr><td>TA</td><td>"+data.ta+"</td></tr>"+
						"<tr><td>POWER</td><td>"+data.power+"</td></tr>"+
						"<!--<tr><td>Position</td><td>"+data.lat+","+data.lon+"</td></tr>-->"+
						"</tbody></table>");
		mob.on('mouseover', function(e){
	    mob.openPopup();
		
		});
		mob.on('mouseout', function(e){
		    mob.closePopup();
			
		});
		mobileMarkers.push(mob);	
	}
	
	
}

var getdatafordetectedmobiles = function()
{
	postData={
			
			"type":type,
			"filter":filter,
			"value":value,
			"bts":bts,
			"country":country,
			"oprname":oprname,
			"startTime":startDate,
			"endTime":endDate
			};
	$.ajax({
		url:"../../service/2g/getdatafordetectedmobiles",
		data:postData,
		type:'post',
		success:function(data)
		{
			console.log(data);			
			for(var i in data)
			{
				addmobile(data[i],'../../resources/images/mob_org.png');	
			}
			for(var i in data)
			{
				map.panTo(new L.LatLng(data[i].lat, data[i].lon));
				break;
			}	
		}
	});	
}



var getGpsData = function()
{
	postData={"startTime":startDate,"endTime":endDate};
	$.ajax({
		url:"../../service/2g/gpsdatahistory",
		data:postData,
		type:'post',
		success:function(data)
		{
			
			var path = [];
			for(var i in data)
			{
				path.push([data[i].l,data[i].n]);
			}
			drawLine(path,map);
			map.panTo(new L.LatLng(path[(path.length)-1][0], path[(path.length)-1][1]));
		}
	});	
}


var drawLine = function(path,map)
{
	var polyline = L.polyline(path, {color: 'red'}).addTo(map);
	
	
	polyline.on('mousedown', function(e){
			taCircleLayers.addTo(map);
			setTaCirclesCenter([e.latlng.lat,e.latlng.lng]);
		
		});
			polyline.on('mouseout', function(e){
			taCircleLayers.remove();	
		});
	
}

var drawTaCircles = function()
{
	for(var i=550;i<=2750;i=i+550  )
	{
		taCircleLayers.addLayer(L.circle([28.7041, 77.1025], {radius: i,color:"black",opacity:1,fillColor:"#fff59f",fillOpacity:0.1}));	
	}
}

var setTaCirclesCenter = function(latlng)
{
	var Talayers = taCircleLayers.getLayers();
	for(var i in Talayers)
	{
		Talayers[i].setLatLng(latlng)
	}
}
$(document).ready(function(){
    loadLeafMap();
    getGpsData();
    drawTaCircles();
    taCircleLayers.addTo(map);
    getdatafordetectedmobiles();
});


</script>


