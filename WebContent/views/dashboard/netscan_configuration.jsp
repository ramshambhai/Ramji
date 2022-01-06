<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
	<script>	
		 var deviceIp  = <%= request.getParameter("ip").equalsIgnoreCase("null")?"":"'"+request.getParameter("ip")+"'"%>
	 	var deviceId = <%= request.getParameter("id").equalsIgnoreCase("null")?"":request.getParameter("id")%>
	 	var sysid = <%= request.getParameter("sysId").equalsIgnoreCase("null")?"":request.getParameter("sysId")%>
	 	var dirPath = "../../";
	 </script>
 
<link rel="stylesheet" type="text/css" href="../../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />
<style>
#configurationMenu{
font-weight:900;
text-decoration:underline;
}
</style>
<div class="wrapper">
    <div class="main-panel">
		<jsp:include page='nav.jsp' />
		<div class="col-xs-6 col-xs-push-3" style="left: 0px;">
		<ul class="nav nav-tabs">
		  <li class="active"><a data-toggle="tab" href="#netscan_scanner_cont">Scan</a></li>
  		  <li><a data-toggle="tab" href="#cell_report_tab_cont">Report</a></li>
		</ul>
		<div class="tab-content" >
		<div class="tab-pane fade in active" id="netscan_scanner_cont"> 					  
			<table class="table table-bordered" id="netscan-scanner">
				<!-- <thead>	
					<th colspan=6>
						Scan
					</th>
				</thead>-->
				<tbody>
					<tr class="cell_scan_field freq_scan_field stop_scan_row exe_scan_field">
						<td colspan=2><label>Type</label></td>
						<td colspan=4>
							<select id="scan_type" name="scan_type">
								<option value="0">Frequency Scan</option>
								<option value="1">Cell Scan</option>
								<option value="2">Exhaustive Scan</option>
								<option value="3">Stop Scan</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan=2><label>RSSI_THRESHOLD(dbm)</label></td>
						<td colspan=4>
							<input type="number" value="" id="scan_rssi_threshold" name="scan_rssi_threshold" />
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>REPETITION_FLAG</label></td>
						<td colspan=4>
							<select id="scan_rep_flag" name="scan_rep_flag">
								<option value="1">Enable</option>
								<option value="0">Disable</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>Repeat Interval(sec)</label></td>
						<td colspan=4>
							<input type="number" value="" id="scan_rep_freq" name="scan_rep_freq"/>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr class="cell_scan_field freq_scan_field stop_scan_row">
						<td colspan=2><label>TECH</label></td>
						<td colspan=4>
							<select id="scan_tech" name="scan_tech">
								<option value="GSM">GSM</option>
								<option value="UMTS">UMTS</option>
								<option value="LTE">LTE</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr class="cell_scan_field">
						<td colspan=2><label>NEIGHBOR_SCAN</label></td>
						<td colspan=4>
							<select id="scan_neighbour" name="scan_neighbour">
								<option value="1">Enable</option>
								<option value="0">Disable</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr class="freq_scan_field" id="freq_scan_band_list">
						<td colspan=2><label>BAND</label></td>
						<td colspan=4>
							<select id="freq_scan_band" multiple size=3>
								
							</select>
						</td>
					</tr>
					<tr class="cell_scan_field" id="band_freq_wrap">
						<td colspan=6>
						<div>
							<table class="table table--bordered">
								<tr>
									<td width=100px><label>BAND</label></td>
									<td width=200px>
										<select class='cell_scan_band' data-freqid='cell_scan_freq_0' onchange='ceateFreqList(this,"scan_tech","cell_scan_freq_0")' id="cell_scan_band" name="cell_scan_band">
										</select>
									</td>
									<td width=50px><label>Freq</label></td>
									<td width=200px>
										<select class='cell_scan_freq' id="cell_scan_freq_0" multiple size=3></select>
									</td>
									<td width=100px>
										<button class='btn btn-default' id="add_more_freq_band">Add More</button>
									</td>
								</tr>
							</table>
						</div>
						</td>
					</tr>
					<tr class="exe_scan_field">
						<td colspan=2><label>GSM BAND</label></td>
						<td colspan=4>
							<select id="exe_scan_2g_tech" name="exe_scan_2g_tech" multiple size=2>
								
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr class="exe_scan_field">
						<td colspan=2><label>UMTS BAND</label></td>
						<td colspan=4>
							<select id="exe_scan_umts_tech" name="exe_scan_umts_tech"  multiple size=2>
								
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr class="exe_scan_field">
						<td colspan=2><label>LTE BAND</label></td>
						<td colspan=4>
							<select id="exe_scan_lte_tech" name="exe_scan_lte_tech"  multiple size=2>
								
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr class="stop_scan_row">
						<td colspan=2><label>SCAN_TYPE</label></td>
						<td colspan=4>
							<select id="stop_scan_type" name="stop_scan_type">
								<option value="0">All</option>
								<option value="1">FREQUENCY SCAN</option>
								<option value="2">CELL SCAN</option>
								<option value="3">EXHAUSTIVE SCAN</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan=6>
							<button class="cell_scan_field btn btn-default" onclick="startCellScan()">Start</button>
							<button class="freq_scan_field btn btn-default" onclick="getFreqScan()">Start</button>
							<button class="exe_scan_field btn btn-default" onclick="startExeScan()">Start</button>
							<button class="stop_scan_row btn btn-default" onclick="stopScan()">Stop</button>
						</td>
					</tr>
					
					</tbody>
				</table>
				</div>
				<div class="tab-pane fade" id="cell_report_tab_cont">
					<table border=1 class="table table-bordered" id="cell_report_tab">
					<tr>
						<td><label>Action</label></td>
						<td>
							<select id="report_action">
								<option value="0">Get Reports</option>
								<option value="1">Save Reports</option>
							</select>
						</td>
					</tr>
					<tr class="get-report">
						<td><label>Type</label></td>
						<td>
							<select id="report_type">
								<option value="0">Cell Report</option>
								<option value="1">Freq Report</option>
							</select>
						</td>
					</tr>
					<tr class="get-report">
						<td><label>TECH</label></td>
						<td>
								<select id="report_tech" name="cell_scan_tech">
									<option value="GSM">GSM</option>
									<option value="UMTS">UMTS</option>
									<option value="LTE">LTE</option>
								</select>
						</td>
						</tr>
						<tr class="save-report">
							<td><label>NUM_RECORDS_IN_FILE</label></td>
							<td>
								<input type="number" id="save_file_num_of_records">
							</td>
						</tr>
						<tr class="save-report">
							<td><label>MAX_NUM_FILES</label></td>
							<td>
								<input type="number" id="save_file_max_num_files">
							</td>
						</tr>
						<tr>
							<td>
								<button class="btn btn-default get-report-btn" onclick="getReports()">Get</button>
								<button class="btn btn-default save-report" onclick="saveReport(true)">Start Saving</button>
								<button class="btn btn-default save-report" onclick="saveReport(false)">Stop Saving</button>
							</td>
						</tr>
						</tbody>
					</table>
				</div>
			</div>		
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
<div id="dialog" style="display:none">
	<table id="data_tab"></table>
	<div id="pager"></div>
</div>
</div>

<link rel="stylesheet" type="text/css" href="../../resources/lib/jsonviewer/jquery.json-viewer.css"/>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jqgrid/css/ui.jqgrid.css" />
<script type="text/javascript" src="../../resources/lib/jqgrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="../../resources/lib/jqgrid/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="../../resources/js/netscan/netscan_configuration.js"></script>
<script type="text/javascript" src="../../resources/lib/jsonviewer/jquery.json-viewer.js"></script>
<script type="text/javascript" src="../../resources/js/netscan/netscan_index.js"></script>
<!--  <script type="text/javascript" src="../../resources/js/3g/configurationStructure.js"></script>-->
<style>
html
{
	margin:0 auto;
}
	.main-panel
	{
		width:100%;
		float:none;
	}
	.nav-tabs
	{
		margin-top:10px !important;
		background:#f4f3ef;
	}
	
	.nav-tabs>li>a
	{
		    border: 1px solid #00000008;
	}
	
	input[type=number]
	{
		width:100%;
	}
	
	.modal-backdrop {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 1;
    background-color: #000;
	}
	
</style>
<div class="modal fade" id="loading_modal" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Please Wait...</h4>
				</div>
				<div class="modal-body">
				<img width=100% id="status_loading" src="../../resources/images/Loading_icon.gif"></img>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
<jsp:include page='fotter.jsp' />

</body>
</html>