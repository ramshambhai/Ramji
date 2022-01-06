<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
 <style>
 #add_sufi_tab{	
display:none;
 }	
 </style>
 
 <link rel="stylesheet" type="text/css" href="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
<script type="text/javascript" src="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />
<div id="container">
	<div id="message_box">
	<div id="left_box">
				<div>
					<div class="card">
					  <div class="container total_sufi">
					    <h4><b>TOTAL</b></h4> 
					    <p id="tot_bts">0</p> 
					  </div>
					</div>
					<div class="card">
					  <div class="container run_sufi">
					    <h4><b>Running</b></h4> 
					    <p id="run_bts">0</p> 
					  </div>
					</div>
					<div class="card">
					  <div class="container wait_sufi">
					    <h4><b>Setting Up</b></h4> 
					    <p id="seting_up_bts">0</p> 
					  </div>
					</div>
					<div class="card">
					  <div class="container nor_sufi">
					    <h4><b>Not Reachable</b></h4> 
					    <p id="nor_bts">0</p> 
					  </div>
					</div>
					<div class="card">
					  <div class="container down_sufi">
					    <h4><b>Down</b></h4> 
					    <p id="down_bts">0</p> 
					  </div>
					</div>			
				</div>
				<div>
					<!--  <table border=1 id="add_sufi_tab" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">-->
					<table border=1 id="add_sufi_tab" class="tab_com" style="">
						<thead ><th colspan=5>Add Network Scanner</th></thead>
						<tr>
							<td colspan=2>IP : <input type='text' value='' id='btsIp' /></td>
							<td colspan=1>System Type : 
								<select id="btsDeviceType"></select>
							</td>
							<td colspan=1>Network Type : 
								<select id="btsType">
									<option value="3g">3G</option>
								</select>
							</td>
							<td><button class="btn-match" onclick="addBts()">Add</button></td>
						</tr>
					</table>
					<!--  <table border =1 style="font-size:13px;margin: 0 auto 10px 10px;width:100%;text-align:center;" id="list_table">-->
						<table border=1 id="list_table" style="font-size:13px;margin: 0 auto 10px 10px;" class="tab_com">
						<thead>
							<th>IP</th>
							<th>STATUS</th>
							<th>TYPE</th>
							<th></th>
						<thead>
						<tbody id="list_table_body"></tbody>
					</table>
				</div>
		</div>	
	</div>
	<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
	<img src="../resources/images/loading_bar_animated.gif" />
	<br>
	<input type="hidden" id="MSISDN_VAL" value="" />
	<input type="button" value="Cancel" id="cancelSearch" onclick="cancelClick()">
	<input type="button" value="Back" id="backtodashboard" onclick="location.reload()">
</div>
			
</div>
<div id="dialog" title="Basic dialog" style="display:none;">
  <table>
  	<tr>
  		<td>STATUS</td>
  		<td id="netscan_status_details"></td>
  	</tr>
  	<tr>
  		<td>SOFT_STATE</td>
  		<td id="netscan_soft_state_details"></td>
  	</tr>
  	<tr>
  		<td>TIMESTAMP</td>
  		<td id="netscan_timestamp_details"></td>
  	</tr>
  	<tr>
  		<td>GPS_STATUS</td>
  		<td id="netscan_gps_status_details"></td>
  	</tr>
  	<tr>
  		<td>DPS_STATUS</td>
  		<td id="netscan_dps_status_details"></td>
  	</tr>
  	<tr>
  		<td>REPETITION_FLAG</td>
  		<td id="netscan_rep_flag_details"></td>
  	</tr>
  	<tr>
  		<td>REPETITION_FREQ</td>
  		<td id="netscan_rep_freq_details"></td>
  	</tr>
  	<tr>
  		<td>OP_TECH</td>
  		<td id="netscan_op_tech_details"></td>
  	</tr>
  </table>
</div>	
<script type="text/javascript" src="../../resources/js/netscan/netscan_dashboard.js"></script>

</body>
</html>