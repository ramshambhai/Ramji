<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
 
 	<script>
	 var autoFlag = false;	 
	 </script>
 <% if(request.getParameter("imei") != null && !request.getParameter("imei").equalsIgnoreCase("null")) 
 { 
 %>
 	<script>
	 var re_imei = <%=request.getParameter("imei").equalsIgnoreCase("null")?"":request.getParameter("imei")%>
	 autoFlag = true;
	 </script>	
 <%
 }
 else
 {
 %>
 <script>
	 var re_imei = "";
	 </script>
 	
<%}%>
<% if(request.getParameter("imsi") != null && !request.getParameter("imsi").equalsIgnoreCase("null")) 
 { 
 %>
 	<script>	
	 var re_imsi = <%=request.getParameter("imsi").equalsIgnoreCase("null")?"":request.getParameter("imsi")%>
	 autoFlag = true;
	 </script>	
 <%
 }
 else
 {
 %>
 
 <script>
	 var re_imsi = "";
	 </script>
<%}%>  
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="container">
	<div id="message_box">
		<div id="topdiv">
		<fieldset id="target_scan">
			<legend>Live Polling</legend>
			<table>
				<tr>					
					<!--  <td><input type="checkbox" id="ne_flag" disabled=true></td>
					<td>Select Cells</td>
										
					<td><input id="detect_radio" type="radio" name = "req_type" value="2" id="trace_req" checked></td>
					<td>Detect</td>					
					<td><input id="chng_ngh_radio" type="radio" name = "req_type" value="3" id="re-trace_req" ></td>
					<td>Change Ngbr</td>					
					<td><input id="poll" type="radio" name = "req_type" value="1" id="poll_req"></td>
					<td>Poll</td>
					<td><input id="page" type="radio" name = "req_type" value="4" id="page_req"></td>
					<td>Page</td>
					<td><input style="border: 0px; width: 30px;"></td>-->					
					<td>IMSI</td>
					<td><input style="display:none;" type="checkbox" id="ne_flag" disabled=true><input style="display:none;" id="poll" type="radio" name = "req_type" value="1" id="poll_req"><input type="text" value="" id="imsi_number" max-lenght=15 name="imsi_number"></td>
					<td>IMEI</td>
					<td><input type="text" value="" id="imei_number" max-lenght=15 name="imei_number"></td>
					<!--  <td>Location</td>
					<td><input style="border: 0px; width: 58px;" type="text" value="" id="lat" max-lenght=15 name="lat"></td>
					<td>Longitude</td>
					<td>,<input style="border: 0px; width: 60px;" type="text" value="" id="lon" max-lenght=15 name="lon"></td>					
					<td><input type="checkbox" id="show_map"></td>
					<td id="show_hide_map_label" >Show Map</td>-->
					<td colspan=2><input type="button" id="search" value="Poll"><input type="button" id="searchWithNegh" value="Search" style="display:none;"></td>
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
		<div id="bottomdiv" style="display:none;">
			<table class="tablesorter" id="neigh_form" border=1 style="display:inline-block;margin-top:7px;font-size:13px;">
				<tr>
					<td colspan=5>Neighbour Not Present</td>
				</tr>			
			</table>
			<div id="table_div"></div>		
			<div id="map_div" style="width:56%"></div>
		</div>			
	</div>
	<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
	<img src="../resources/images/loading_bar_animated.gif" />
	<br>
	<input type="hidden" id="MSISDN_VAL" value="" />
	<input type="button" value="Cancel" id="cancelSearch" onclick="checkRelaod()"></div>
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
 		<script type="text/javascript" src="../resources/js/livepoll.js"></script>
<%}%>



</body>
</html>