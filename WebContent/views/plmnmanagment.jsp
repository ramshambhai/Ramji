<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='nodeheader.jsp' />
 <!-- ---------------------------------------- -->
  <script>
	 var autoFlag = false;	 
	 
	 	$("#nav").css("display","none");
	 
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
 <!-- ----------------------------------------- -->
 
 
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="pageloadBox"></div>
<div id="container">
	<div id="message_box">
		<div id="topdiv">
		<fieldset id="target_scan">
			<legend>Target Scan</legend>
			<table>
				<tr>					
					<td class="hide_box"><input type="checkbox" id="ne_flag" disabled=true></td>
					<td class="hide_box">Select Cells</td>
										
					<td><input id="detect_radio" type="radio" name = "req_type" value="2" id="trace_req" checked></td>
					<td>Detect</td>					
					<td><input id="chng_ngh_radio" type="radio" name = "req_type" value="3" id="re-trace_req" ></td>
					<td>Change Ngbr</td>					
					<td class="hide_box" ><input id="poll" type="radio" name = "req_type" value="1" id="poll_req"></td>
					<td class="hide_box" >Poll</td>
					<td class="hide_box" ><input id="page" type="radio" name = "req_type" value="4" id="page_req"></td>
					<td class="hide_box" >Page</td>
					<td><input style="border: 0px; width: 15px;" readonly></td>					
					<td class="hide_box">IMSI</td>
					<td class="hide_box"><input type="text" value="" id="imsi_number" max-lenght=15 name="imsi_number"></td>
					<td class="hide_box">IMEI</td>
					<td class="hide_box"><input type="text" value="" id="imei_number" max-lenght=15 name="imei_number"></td>
					<td>Location : </td>
					<td><input style="border: 0px; width: 80px;" type="text" value="" id="lat" max-lenght=15 name="lat"></td>
					<!--  <td>Longitude</td>-->
					<td>,<input style="border: 0px; width: 80px;" type="text" value="" id="lon" max-lenght=15 name="lon"></td>					
					<td>&nbsp;&nbsp;<input type="checkbox" id="show_map"></td>
					<td id="show_hide_map_label" >Show Map</td>
					<td colspan=1>&nbsp;&nbsp;<input type="button" class="btn btn-default" id="search" value="Submit">&nbsp;&nbsp;<input class="btn btn-default" type="button" id="searchWithNegh" value="Submit" style="display:none;"></td>
					<td colspan=1>&nbsp;&nbsp;<input type="button" class="btn btn-default" id="reload_button" value="Sync" onclick="myFunction()"></input>&nbsp;&nbsp;<input class="btn btn-default" type="button" id="reload_button" data-toggle="modal" data-target="#add_plmn" value="Add" ></input></td>
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
			<table class="table table-striped" id="neigh_form" border="1" style="display:inline-block;margin-top:7px;font-size:10px;width:551px !important;overflow: auto;">
				<tr>
					<td colspan="5">Neighbour Not Present</td>
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
	<input type="button" value="Cancel" id="cancelSearch"></div>
</div>
<div class="modal fade" id="add_plmn" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title"><strong>ADD CELL</strong></h4>
				</div>
				<div class="modal-body">
					<table style="border: 1px solid gray;" class="table table-striped">
						<thead>
							
						</thead>
						<tbody>
							<tr><td>MCC</td><td><input type="number" class="add_plmn_text" name="add_mcc" id="add_mcc" value=""/></td></tr>
							<tr><td>MNC</td><td><input type="number" class="add_plmn_text"  name="add_mnc" id="add_mnc" value=""/></td></tr>
							<tr><td>LAC</td><td><input type="number" class="add_plmn_text"  name="add_lac" id="add_lac" value=""/></td></tr>
							<tr><td>CELL</td><td><input type="number" class="add_plmn_text"  name="add_cell" id="add_cell" value=""/></td></tr>
							<tr><td>ARFCN</td><td><input type="number" class="add_plmn_text"  name="add_arfcn" id="add_arfcn" value=""/></td></tr>
							<tr><td>BSIC</td><td><input type="number" class="add_plmn_text"  name="add_bsic" id="add_bsic" value=""/></td></tr>
							<tr><td>RSSI</td><td><input type="number" class="add_plmn_text"  name="add_rssi" id="add_rssi" value=""/></td></tr>
							<tr><td colspan=2 style="text-align:right;"><button class="btn btn-default number"  btn-default" id="add_plmn_btn">ADD</button></td></tr>
						</tbody>
					</table>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
<script>
	/*if(autoFlag)
	{
		$("#pageloadBox").css("display","block");
	}*/
	$("#pageloadBox").css("display","block");
</script>
 <% if(request.getParameter("maptype") != null && request.getParameter("maptype").equalsIgnoreCase("offline")) 
 { 
 %>
 	<link rel="stylesheet" href="../resources/css/ol.css" />
 	<script type="text/javascript" src="../resources/js/ol.js"></script>
 		<script type="text/javascript" src="../resources/js/plmnmanagment.js"></script>
 <%
 }
 else
 {
 %>	
	<link rel="stylesheet" href="../resources/css/ol.css" />
 	<script type="text/javascript" src="../resources/js/ol.js"></script>
 		<script type="text/javascript" src="../resources/js/plmnmanagment.js"></script>
<%}%>
<script type="text/javascript" src="../resources/lib/tablesorter/jquery.tablesorter.js"></script>
<script src="../resources/lib/assets/js/bootstrap.min.js" type="text/javascript"></script>
<!-- Bootstrap core CSS     -->
<link href="../resources/lib/assets/css/bootstrap.min.css" rel="stylesheet" />

<script>
function myFunction() {
    location.reload();
}
</script>
<style>
#neigh_form>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th {
    padding: 3px;
    line-height: 1.42857143;
    vertical-align: top;
    border-top: 1px solid #ddd;
}

</style>
</body>
</html>