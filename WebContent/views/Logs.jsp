<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' /> 
<link rel="stylesheet" type="text/css" href="../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
<script type="text/javascript" src="../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<script type="text/javascript" src="../resources/lib/jquery-ui-timepicker-addon.js"></script>
<div id="container">
	<div id="message_box">
		<div id="topdiv">
		<fieldset id="target_scan">
			<legend>Target Scan</legend>
			<table>
				<tr>
					<td>IMSI</td>
					<td><select id="imsi_number"></select></td>
					<td>Start Time</td>
					<td><input class="dateSelect" type="text" id="startTime" ></td>
					<td>End Time</td>
					<td><input class="dateSelect"  type="text" id="endTime"></td>
					<td colspan=2><input type="button" id="get_data" value="Get"></td>
					<td colspan=2><input type="button" id="calculate_data" value="Calculate"></td>
					<!--  <td>Latitude</td>
					<td><input type="text" value="" id="lat" max-lenght=15 name="lat"></td>
					<td>Longitude</td>
					<td><input type="text" value="" id="lon" max-lenght=15 name="lon"></td>
					<td>Select Cells</td>
					<td><input type="checkbox" id="ne_flag" disabled=true></td>
					
					<td><input type="radio" name = "req_type" value="2" id="trace_req" checked></td>
					<td>Detect</td>
					
					<td><input type="radio" name = "req_type" value="3" id="re-trace_req" ></td>
					<td>Change Ngbr</td>
					
					<td><input type="radio" name = "req_type" value="1" id="poll_req"></td>
					<td>Poll</td>-->
					
					<td><input type="checkbox" id="show_map"></td>
					<td id="show_hide_map_label" >Show Map</td>
				</tr>				
			</table>
			<!--  <table id="neigh_form" border=1 style="display:none;">
				<tr>
					<td>USE </td>
					<td style="display:none;">STATUS</td>
					<td>PLMN STR</td>
					<td>LAC</td>
					<td>CELL</td>
				</tr>			
			</table>-->	
		</fieldset>
		<fieldset id="reTarget" style="display:none;width:372px;float:left;">
			<legend>Target Scan</legend>
			<table>
				<tr>
					<td><input type="button" id="rescan" value="Scan Again"></td>
				</tr>				
			</table>	
		</fieldset>
		<fieldset id="sub_det" style="display:none;width:867px;float:left;mar">
			<legend>Subscriber Position</legend>
			<table>
				<tr>
					<td class="makeStrong">IMSI : </td>
					<td id="sub_IMSI"></td>
					<td class="makeStrong">Latitude,Longitude:</td>
					<td id="sub_lat"></td>
					<td id="sub_lon"></td>
					<td class="makeStrong" id="">Distance:</td>
					<td id="sub_distance"></td>
					<td><input type="checkbox" id="show_map_after"></td>
					<td class="makeStrong" id="show_hide_after_map_label" >Show Map</td>
				</tr>				
			</table>	
		</fieldset>
		</div>
		<div id="bottomdiv" style="display:block;">
			<table>
			<tr>
				<td>
					<table class="tablesorter" id="neigh_form" border=1 style="display:inline-block;margin-top:7px;font-size:13px;">
						<tr>
							<td colspan=5>Neighbour Not Present</td>
						</tr>			
					</table>
				</td>
			<tr>
			<tr>
				<td>
					<table class="tablesorter" id="neigh_form_cal" border=1 style="max-height:200px;overflow:auto;display:inline-block;margin-top:7px;font-size:13px;">
						<tr>
							<td>ID</td>
							<td>LAT,LON</td>
							<td>DISTANCE</td>
						</tr>			
					</table>
				</td>
			<tr>
			
			<div id="table_div"></div>		
			<div id="map_div" style="width:54%"></div>
		</div>			
	</div>
	<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
	<img src="../resources/images/loading_bar_animated.gif" />
	<br>
	<input type="button" value="Cancel" id="cancelSearch"></div>
</div>
 <% if(request.getParameter("maptype") != null && request.getParameter("maptype").equalsIgnoreCase("offline")) 
 { 
 %>
 	<link rel="stylesheet" href="../resources/css/ol.css" />
 	<script type="text/javascript" src="../resources/js/ol.js"></script>
 		<script type="text/javascript" src="../resources/js/index3_offline.js"></script>
 <%
 }
 else
 {
 %>	
	<link rel="stylesheet" href="../resources/css/ol.css" />
 	<script type="text/javascript" src="../resources/js/ol.js"></script>
 		<script type="text/javascript" src="../resources/js/logs.js"></script>
<%} %>
<script type="text/javascript" src="../resources/lib/tablesorter/jquery.tablesorter.js"></script>
<script>
$(function() {
    $( ".dateSelect" ).datetimepicker({
    	dateFormat: "yy-mm-dd",
    	timeFormat: 'HH:mm'
    });
  });
</script>
</body>
</html>