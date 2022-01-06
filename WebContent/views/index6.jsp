<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
 <script type="text/javascript" src="../resources/js/index2.js"></script>

<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="container">
	<div id="message_box">
		<div id="topdiv">
		<fieldset id="target_scan">
			<legend>Target Scan</legend>
			<table>
				<tr>
					<td>IMSI</td>
					<td><input type="text" value="" id="imsi_number" max-lenght=15 name="imsi_number"></td>
					<td>Latitude</td>
					<td><input type="text" value="" id="lat" max-lenght=15 name="lat"></td>
					<td>Longitude</td>
					<td><input type="text" value="" id="lon" max-lenght=15 name="lon"></td>
					<td>Select Cells</td>
					<td><input type="checkbox" id="ne_flag" disabled=true></td>
					<td>Trace</td>
					<td><input type="radio" name = "req_type" value="2" id="trace_req" checked></td>
					<td>Re-Trace</td>
					<td><input type="radio" name = "req_type" value="3" id="re-trace_req" ></td>
					<td>Poll</td>
					<td><input type="radio" name = "req_type" value="1" id="poll_req"></td>
					<td colspan=2><input type="button" id="search" value="Search"><input type="button" id="searchWithNegh" value="Search" style="display:none;"></td>
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
		<fieldset id="reTarget" style="display:none;width:610px;float:left;">
			<legend>Target Scan</legend>
			<table>
				<tr>
					<td><input type="button" id="rescan" value="Scan Again"></td>
				</tr>				
			</table>	
		</fieldset>
		<fieldset id="sub_det" style="display:none;width:620px;float:left;mar">
			<legend>Subscriber Position</legend>
			<table>
				<tr>
					<td>IMSI : </td>
					<td id="sub_IMSI"></td>
					<td>Latitude : </td>
					<td id="sub_lat"></td>
					<td>Longitude : </td>
					<td id="sub_lon"></td>
				</tr>				
			</table>	
		</fieldset>
		</div>
		<div id="bottomdiv" style="display:block;">
			<table id="neigh_form" border=1 style="display:inline-block;margin-top:7px;font-size:13px;">
				<tr>
					<td colspan=5>Neighbour Not Present</td>
				</tr>			
			</table>
			<div id="table_div"></div>		
			<div id="map_div" style="width:67%"></div>
		</div>			
	</div>
	<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
	<img src="../resources/images/loading_bar_animated.gif" />
	<br>
	<input type="button" value="Cancel" id="cancelSearch"></div>
</div>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBeTG_r0W7ykpYN0e0M4igI3s220vSVqBo&callback=initMap" defer></script>

</body>
</html>