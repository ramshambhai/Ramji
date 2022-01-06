<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
 <script type="text/javascript" src="../resources/js/index.js"></script>

<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="container">
	<div id="message_box">
		<div id="topdiv">
		<fieldset id="target_scan">
			<legend>Target Scan</legend>
			<table>
				<tr>
					<td>IMSI</td>
					<td><input type="text" value="404100504835285" id="imsi_number" max-lenght=15 name="imsi_number"></td>
					<td>Latitude</td>
					<td><input type="text" value="28.4876248" id="lat" max-lenght=15 name="lat"></td>
					<td>Logitude</td>
					<td><input type="text" value="77.076312" id="lon" max-lenght=15 name="lon"></td>
					<!--  <td>Fill Neighbour Data</td>
					<td><input type="checkbox" id="ne_flag"></td>-->
					<td colspan=2><input type="button" id="search" value="Search"></td>
				</tr>				
			</table>
			<!--  <table id="neigh_form">
				<tr>
					<td>Status</td>
					<td>plmnStr</td>
					<td>lac</td>
					<td>cell</td>
				</tr>				
			</table>-->	
		</fieldset>
		<fieldset id="reTarget" style="display:none;width:630px;float:left;">
			<legend>Target Scan</legend>
			<table>
				<tr>
					<td><input type="button" id="rescan" value="Scan Again"></td>
				</tr>				
			</table>	
		</fieldset>
		<fieldset id="sub_det" style="display:none;width:630px;float:left;mar">
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
		<div id="bottomdiv" style="display:none;">
			<div id="table_div"></div>		
			<div id="map_div"></div>
		</div>			
	</div>
	<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
	<img src="../resources/images/loading_bar_animated.gif" />
	<br>
	<input type="button" value="Cancel" id="cancelSearch"></div>
</div>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBeTG_r0W7ykpYN0e0M4igI3s220vSVqBo&callback=initMap" async defer></script>

</body>
</html>