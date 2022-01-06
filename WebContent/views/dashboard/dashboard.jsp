<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="css/lib/bootstrap.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="css/lib/fontawesome-all.min.css">
	<link rel="stylesheet" href="css/lib/bootstrap-colorpicker.min.css">
	<link rel="stylesheet" href="css/lib/leaflet.css">
	<link rel="stylesheet" href="css/lib/leaflet.fullscreen.css">
	<link rel="stylesheet" href="css/lib/popupwindow.css">
	<link href="css/dashboard.css" rel="stylesheet" />
	<title></title>
<script>
	var remoteAddr = '<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>';
</script>
</head>
<body>
<%@include file="header.jsp" %>
<div class=".container-fluid h-100_custom">
	<div class="row h-50 cont_row">
		<div class="col-xs-7">
			<div id="map_leaf" class="box_border"></div>
    	</div>
    	<div class="col-xs-5">
    		<div class="row h-100">
    			<div class="col-xs-7">
    				<div class="row h-50">
    					<div class="col-xs-12 box_border">
    					<div class="box_title">Alarm</div>
    					<div class="table_cont">
    						<table class="table e_tab" id="bl_table">
							  <thead>
							    <tr>
							      <th scope="col">Date</th>
							      <th scope="col">Time</th>
							      <th scope="col">Node</th>
							    </tr>
							  </thead>
							  <tbody>
							    
							  </tbody>
							</table>
    					</div>
    					</div>
    				</div>
    				<div class="row h-50">
    					<div class="col-xs-12  box_border">
    					<div class="box_title">Events</div>
    					<div class="table_cont">
    						<table class="table e_tab" id="el_table">
							  <thead>
							    <tr>
							      <th scope="col">DATE</th>
							      <th scope="col">TIME</th>
							      <th scope="col">NODE</th>
							      <th scope="col">EVENT</th>
							    </tr>
							  </thead>
							  <tbody>
							    
							  </tbody>
							</table>
    					</div>					
    					</div>
    				</div>	
    			</div>
    			<div class="col-xs-5">
					<div class="row h-100">
						<div class="col-xs-12  box_border">
							<div class="box_title">Status</div>
						
						</div>		
					</div>    				
    			</div>		
    		</div>		
    	</div>
  	</div>
  	<div class="row h-45 cont_row">
  		<div class="col-xs-4  box_border">
  		<div class="box_title">Humer</div>
  			<canvas id="canvas"></canvas>
  		</div>
  		<div class="col-xs-4">
  		<div class="box_title">Falcon</div>
  									<div class="table_cont box_border">
    						<table class="table e_tab" id="falcon_table">
							  <thead>
							    <tr>
							      <th scope="col">DATE</th>
							      <th scope="col">TIME</th>
							      <th scope="col">IMEI</th>
							      <th scope="col">IMSI</th>
							      <th scope="col">TA</th>
							      <th scope="col">FREQ</th>
							      <th scope="col">RXL</th>
							      <th scope="col">LOCATION</th>
							    </tr>
							  </thead>
							  <tbody>
							    
							  </tbody>
							</table>
    					</div>					
  		</div>
  		<div class="col-xs-4">
  			  			<div class="box_title">Oxfam</div>
  			  			<div class="table_cont box_border">
    						<table class="table  e_tab" id="oxfam_table">
							  <thead>
							    <tr>
							      <th scope="col">Date</th>
							      <th scope="col">Time</th>
							      <th scope="col">Location</th>
							      <th scope="col">Description</th>
							    </tr>
							  </thead>
							  <tbody>
										    
							  </tbody>
							</table>
    					</div>
  		</div>
  	</div>
  	<div class="row h-5">
  		<div class="col-12 box_border">
	  		<div class = "row" id="status_bar">
	  		<div class="col-xs-4 text-left font-weight-bold"><span  id="sys_location" class="status_elem">Not Available</span></div>
	  		<div class="col-xs-4 text-center font-weight-bold">
		  		<div id="UGS_legend" class="legend_block" ><span id="UGS_state" class="border rounded-circle sensor_status status_elem" style="background:red;">&nbsp;&nbsp;</span>UGS-C2</div>
		  		<span class="border"></span>
		  		<div id="TRGL_legend"  class="legend_block" ><span id="TRGL_state"  class="border rounded-circle sensor_status status_elem"  style="background:green;">&nbsp;&nbsp;</span>TRGL</div>
		  		<span class="border seprator"></span>
		  		<div id="TMDAS_legend"  class="legend_block" ><span id="TMDAS_state"  class="border rounded-circle sensor_status status_elem"  style="background:red;">&nbsp;&nbsp;</span>TMDAS</div>
	  		</div>
	  		<div class="col-xs-4 text-right font-weight-bold"><span id="current_time"></span></div>
	  		</div>
  		</div>
  	</div>
</div>
  



<!--  SCRIPT ATTACHMENT SECTION -->
<script src="js/lib/jquery-3.3.1.min.js" ></script>
<script src="js/lib/bootstrap.min.js" ></script>
<script src="js/lib/bootstrap-colorpicker.min.js" ></script>
<script src="js/lib/leaflet.js" ></script>
<script src="js/lib/Leaflet.GoogleMutant.js" ></script>
<script src="js/lib/Leaflet.fullscreen.min.js" ></script>
<script src="js/lib/popupwindow.min.js"></script>


<!--  SCRIPT ATTACHMENT SECTION -->
</body>
</html>