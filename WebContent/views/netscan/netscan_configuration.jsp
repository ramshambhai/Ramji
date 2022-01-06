<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
	<script>	
		 var deviceIp  = <%= request.getParameter("ip").equalsIgnoreCase("null")?"":"'"+request.getParameter("ip")+"'"%>
	 	var deviceId = <%= request.getParameter("id").equalsIgnoreCase("null")?"":request.getParameter("id")%>
	 	var sysid = <%= request.getParameter("sysId").equalsIgnoreCase("null")?"":request.getParameter("sysId")%>
	 	
	 </script>
 <style>
	
 
 
 #inter_frq_list_tab input
 {
 	width:100px;
 }
 #left_box
 {
 	width:100% !important;
 }	
 .tab_com
 {
     margin: 0 0 0 0 !important;
 }
 .tab_com tbody 
 {
 	color:black !important;
 	max-height:330px;
 	overflow:auto;
 	display:block;
 }
 .card
 {
 	width: auto !important;
 	height:360px;
 }
 </style>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />
<div id="container">
	<div id="message_box">
		<div id="left_box">
			<div class="card">
					  
						<table border=1 class="tab_com" id="add_sufi_tab">
				<thead>
					<th colspan=6>
						Frequency Scan
					</th>
				</thead>
					<tr>
						<td colspan=2><label>RSSI_THRESHOLD(dbm)</label></td>
						<td colspan=4>
							<input type="number" value="" id="freq_scan_rssi_threshold" name="freq_scan_rssi_threshold" />
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>REPETITION_FLAG</label></td>
						<td colspan=4>
							<select id="freq_scan_rep_flag" name="freq_scan_rep_flag">
								<option value="1">Enable</option>
								<option value="0">Disable</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>REPITITION_FREQ</label></td>
						<td colspan=4>
							<input type="number" value="" id="freq_scan_rep_freq" name="freq_scan_rep_freq"/>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>TECH</label></td>
						<td colspan=4>
							<select id="freq_scan_tech" name="freq_scan_tech">
								<option value="GSM">GSM</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>BAND</label></td>
						<td colspan=4>
							<select id="freq_scan_band" multiple size=3>
								<option value=380>380</option>
								<option value=410>410</option>
								<option value=450>450</option>
								<option value=480>480</option>
								<option value=710>710</option>
								<option value=750>750</option>
								<option value=850>850</option>
								<option value=900>900</option>
								<option value=1800>1800</option>
								<option value=1900>1900</option>
								<option value=2100>2100</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan=6><button onclick="getFreqScan()">Start</button></td>
					</tr>
				</table>
			</div>
						<div class="card">
					  
						<table border=1 class="tab_com" id="cell_scan_tab">
				<thead>
					<th colspan=6>
						Cell Scan
					</th>
				</thead>
					<tr>
						<td colspan=2><label>TECH</label></td>
						<td colspan=4>
							<select id="cell_scan_tech" name="cell_scan_tech">
								<option value="GSM">GSM</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>RSSI_THRESHOLD</label></td>
						<td colspan=4>
							<input type="text" id="cell_scan_rssi_threshold" name="cell_scan_rssi_threshold" />
						</td>
					</tr>
					<tr>
						<td colspan=2><label>REPETITION_FLAG</label></td>
						<td colspan=4>
							<select id="cell_scan_rep_flag" name="cell_scan_rep_flag">
								<option value="1">Enable</option>
								<option value="0">Disable</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>REPITITION_FREQ</label></td>
						<td colspan=4>
							<input type="number" value="" id="cell_scan_rep_freq" name="cell_scan_rep_freq"/>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>NEIGHBOR_SCAN</label></td>
						<td colspan=4>
							<select id="cell_scan_neighbour" name="cell_scan_neighbour">
								<option value="1">Enable</option>
								<option value="0">Disable</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr id="band_freq_wrap">
						<td colspan=6>
						<div>
							<table>
								<tr>
									<td colspan=1><label>BAND</label></td>
									<td colspan=1>
										<select class='cell_scan_band' data-freqid='cell_scan_freq_0' onchange='ceateFreqList(this,"cell_scan_tech","cell_scan_freq_0")' id="cell_scan_band" name="cell_scan_band">
										</select>
									</td>
									<td colspan=1><label>Freq</label></td>
									<td colspan=1>
										<select class='cell_scan_freq' id="cell_scan_freq_0" multiple size=3></select>
									</td>
									<td>
										<button class='add_more_freq_band' id="add_more_freq_band">Add More</button>
									</td>
								</tr>
							</table>
						</div>
						</td>
					</tr>
					<tr>
						<td colspan=6><button onclick="startCellScan()">Start</button></td>
					</tr>
				</table>
			</div>
				<div class="card">
									<table border=1 class="tab_com" id="neigh_report_tab">
				<thead>
					<th colspan=6>
						Exhaustive Scan
					</th>
				</thead>
				<tbody>	
						<tr>
						<td colspan=2><label>RSSI_THRESHOLD</label></td>
						<td colspan=4>
							<input type="number" value="" id="exe_scan_rssi_thresold" name="exe_scan_rssi_thresold"/>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
						<tr>
						<td colspan=2><label>REPETITION_FLAG</label></td>
						<td colspan=4>
							<select id="exe_scan_rep_flag" name="exe_scan_rep_flag">
								<option value="1">Enable</option>
								<option value="0">Disable</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>REPITITION_FREQ</label></td>
						<td colspan=4>
							<input type="number" value="" id="exe_scan_rep_freq" name="exe_scan_rep_freq"/>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>GSM BAND</label></td>
						<td colspan=4>
							<select id="exe_scan_2g_tech" name="exe_scan_2g_tech" multiple size=2>
								
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>UMTS BAND</label></td>
						<td colspan=4>
							<select id="exe_scan_umts_tech" name="exe_scan_umts_tech"  multiple size=2>
								
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>LTE BAND</label></td>
						<td colspan=4>
							<select id="exe_scan_lte_tech" name="exe_scan_lte_tech"  multiple size=2>
								
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=6><button onclick="startExeScan()">Start</button></td>
					</tr>
				</tbody>
				</table>		
				</div>
				
				
				
				<!-- 					<div class="card">
					  
				<table border=1 class="tab_com" id="cell_report_tab">
				<thead><th>Cell Report</th></thead>
				<tr>
					<td><label>TECH</label></td>
					<td>
							<select id="cell_report_tech" name="cell_scan_tech" size="2" multiple>
								<option value="2G">2G</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
					</td>
						<td><button onclick="getCellReport()">Get</button></td>
					</tr>
				</table>
						<table border=1 class="tab_com" id="neigh_report_tab">
				<thead>
					<th colspan=6>
						Neighbor Report
					</th>
				</thead>
					
					<tr>
						<td colspan=2><label>TECH</label></td>
						<td colspan=2>
							<select id="neigh_report_tech" name="neigh_report_tech" size="2" multiple>
								<option value="2G">2G</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
						</td>
						<td><button onclick="getNeighReport()">Get</button></td>						
						</tr>
					
					
				</table>
							<table border=1 class="tab_com" id="neigh_report_tab">
				<thead>
					<th colspan=6>
						Saving reports
					</th>
				</thead>
					<tr>
						<td colspan=2><label>NUM_RECORDS_IN_FILE</label></td>
						<td colspan=4>
							<input type="number" id="save_file_num_of_records" />
						</td>
					</tr>
					<tr>
						<td colspan=2><label>MAX_NUM_FILES</label></td>
						<td colspan=4>
							<input type="number" id="save_file_max_num_files" />
						</td>
					</tr>
					<tr>
						<td colspan=6>
							<button onclick="saveReport(true)">Start Saving</button>
							<button onclick="saveReport(false)">Stop Saving</button>
						</td>
					</tr>
				</table>
			</div>-->
											<div class="card">
					  
				<table border=1 class="tab_com" id="cell_report_tab">
				<thead><th>Cell Report</th></thead><tbody>
				<tr>
					<td><label>TECH</label></td>
					<td>
							<select id="cell_report_tech" name="cell_scan_tech">
								<option value="GSM">GSM</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
					</td>
						<td><button onclick="getCellReport()">Get</button></td>
					</tr>
					</tbody>
				</table>
				</div>
				
				<div class="card">
					<table border=1 class="tab_com" id="fre_report_tab">
				<thead><th>Freq Report</th></thead><tbody>
				<tr>
					<td><label>TECH</label></td>
					<td>
							<select id="freq_report_tech" name="freq_report_tech">
								<option value="GSM">GSM</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
					</td>
						<td><button onclick="getFreqReport()">Get</button></td>
					</tr>
					</tbody>
				</table>
				</div>
				
				<!--  <div class="card">
								<table border=1 class="tab_com" id="neigh_report_tab">
				<thead>
					<th colspan=6>
						Neighbor Report
					</th>
				</thead>
					<tbody>
					<tr>
						<td colspan=2><label>TECH</label></td>
						<td colspan=2>
							<select id="neigh_report_tech" name="neigh_report_tech" size="2" multiple>
								<option value="2G">2G</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
						</td>
						<td><button onclick="getNeighReport()">Get</button></td>						
						
					</tr>
					
					</tbody>
				</table>
			
				</div>-->
					<div class="card">
						<table border=1 class="tab_com" id="stop_scan">
				<thead>
					<th>
						Stop Scan
					</th>
				</thead>
					<tbody>	
					<tr>
						<td><label>TECH</label></td>
						<td>
							<select id="stop_scan_tech" name="stop_scan_tech">
								<option value="GSM">2G</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
						</td>
												
						
					</tr>
					<tr>
						<td><label>SCAN_TYPE</label></td>
						<td>
							<select id="stop_scan_type" name="stop_scan_type">
								<option value="0">All</option>
								<option value="1">FREQUENCY SCAN</option>
								<option value="2">CELL SCAN</option>
								<option value="3">EXHAUSTIVE SCAN</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan=2><button onclick="stopScan()">Apply</button></td>
					</tr>
					
			</tbody>		
				</table>
			
				</div>
				<div class="card">
								<table border=1 class="tab_com" id="neigh_report_tab">
				<thead>
					<th colspan=6>
						Saving reports
					</th>
				</thead>
					<tbody>
					<tr>
						<td colspan=2><label>NUM_RECORDS_IN_FILE</label></td>
						<td colspan=4>
							<input type="number" id="save_file_num_of_records" />
						</td>
					</tr>
					<tr>
						<td colspan=2><label>MAX_NUM_FILES</label></td>
						<td colspan=4>
							<input type="number" id="save_file_max_num_files" />
						</td>
					</tr>
					<tr>
						<td colspan=6>
							<button onclick="saveReport(true)">Start Saving</button>
							<button onclick="saveReport(false)">Stop Saving</button>
						</td>
					</tr>
					</tbody>
				</table>
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
<div id="dialog" style="display:none">
	<table id="data_tab"></table>
	<div id="pager"></div>
</div>
</div>
 <link rel="stylesheet" type="text/css" href="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jsonviewer/jquery.json-viewer.css"/>
<script type="text/javascript" src="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jqgrid/css/ui.jqgrid.css" />
<script type="text/javascript" src="../../resources/lib/jqgrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="../../resources/lib/jqgrid/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="../../resources/js/netscan/netscan_configuration.js"></script>
<script type="text/javascript" src="../../resources/lib/jsonviewer/jquery.json-viewer.js"></script>
<!--  <script type="text/javascript" src="../../resources/js/3g/configurationStructure.js"></script>-->

</body>
</html>