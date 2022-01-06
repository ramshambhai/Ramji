<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
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
     margin: 0 0 0 0;
     
 }
 .tab_com tbody 
 {
 	color:black !important;
 	overflow:auto;
 	font-size:13px;
 }
 
 .block {
    		box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
    		transition: 0.3s;
    		margin: 0 auto 10px 125px;
			width: 1000px;
    		color:white;
		}
		
.block:hover {
		    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
		}

.block .container {
		    padding: 2px 16px;
		}

   button {
    background-color: #008CBA; 
    border: none;
	border-radius: 6px;
    color: white;
    padding: 15px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer;
}

  #configButton {
    background-color: #008CBA; 
    border: none;
	border-radius: 6px;
    color: white;
    padding: 15px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
    width: 309px;
}

#configurationMenu{
	font-weight:900;
	text-decoration:underline;
}

.spanStyle{
	font-size: 16px;
	margin-left: 25px;
	float:right;
	cursor:pointer;
}

.spanStyleThead{
display: none;
}

.spanStyleTbody{
display: none;
}

#scannedListButton,#cellScanButton,#subscriberInfoButton{
display: none;
}

.table-nested tbody tr td{
 	border: none;
}
 </style>
<link rel="stylesheet" type="text/css" href="../../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />
<style>

html{
	width: auto;
 }
 
table select{
	width: 150px;
}

.table-bordered tbody tr label{
	font-weight: 500;
	font-size: 12px;
}
</style>
<script>	
</script>
<div class="wrapper">
<jsp:include page='nav.jsp' />
<div id="container">
	<div id="message_box">
		<div id="left_box">
			<div class="block" style="margin: 0 auto;">
					  
						<table border=1 class="table table-bordered" id="add_sufi_tab">
				<thead style="color: white;">
				<tr>
				<th colspan="4">E-SUFI System Configuration<span class="spanStyle ti-arrow-circle-down" id="systemConfig"><span class="arrowClass" style="display:none;">+</span></span></th>
				</tr>
				<tr class="spanStyleThead">
					<th>Parameters</th>
					<th>OF</th>
					<th>PPF</th>
					<th>SPF</th>
				</tr>
				</thead>
				<tbody class="spanStyleTbody">
					<tr>
						<td><label>Log Level</label></td>
						<td><select id=log_level_OF>
									<option value=0>CRITICAL</option>
									<option value=1>ERROR</option>
									<option value=2>WARNING</option>
									<option value=3>DEBUG</option>
									<option value=4 selected>INFO</option>
								</select></td>
						<td><select id=log_level_PPF>
									<option value=0>CRITICAL</option>
									<option value=1>ERROR</option>
									<option value=2>WARNING</option>
									<option value=3>DEBUG</option>
									<option value=4 selected>INFO</option>
								</select></td>
						<td><select id=log_level_SPF>
									<option value=0>CRITICAL</option>
									<option value=1>ERROR</option>
									<option value=2>WARNING</option>
									<option value=3>DEBUG</option>
									<option value=4 selected>INFO</option>
								</select></td>
					</tr>
					<tr>
						<td><label>Hold Timer(ms)</label></td>
						<td><input type="number" value="" id="holdTimerOF"></input></td>
						<td><input type="number" value="" id="holdTimerPPF"></input></td>
						<td><input type="number" value="" id="holdTimerSPF"></input></td>
					</tr>
					<tr>
						<td><label>Redirection Timer(ms)</label></td>
						<td><input type="number" value="" id="redirectionTimerOF"></input></td>
						<td><input type="number" value="" id="redirectionTimerPPF"></input></td>
						<td><input type="number" value="" id="redirectionTimerSPF"></input></td>
					</tr>
					<tr style="display: none;">
						<td><label>PCPICH_POWER_PERC</label></td>
						<td><select id="pcpich_power_perc_OF">
									<option value="10">10</option>
									<option value="5">5</option>
								</select></td>
						<td><select id="pcpich_power_perc_PPF">
									<option value="10">10</option>
									<option value="5">5</option>
								</select></td>
						<td><select id="pcpich_power_perc_SPF">
									<option value="10">10</option>
									<option value="5">5</option>
								</select></td>
					</tr>
					<!-- <tr>
						<td><label>Total Tx Power(dB)</label></td>
						<td><input type="number" value="" id="txPowerOF"></input></td>
						<td><input type="number" value="" id="txPowerPPF"></input></td>
						<td><input type="number" value="" id="txPowerSPF"></input></td>
					</tr>
					<tr>
						<td><label>DPCCH Power Off</label></td>
						<td><input type="text" value="" id="dpcchPowerOF" class="dpcchPowerClass"></input></td>
						<td><input type="text" value="" id="dpcchPowerPPF" class="dpcchPowerClass"></input></td>
						<td><input type="text" value="" id="dpcchPowerSPF" class="dpcchPowerClass"></input></td>
					</tr>
					<tr>
						<td><label>DPCH Frame Off</label></td>
						<td><input type="text" value="" id="dpchFrameOF" class="dpchFrameClass"></input></td>
						<td><input type="text" value="" id="dpchFramePPF" class="dpchFrameClass"></input></td>
						<td><input type="text" value="" id="dpchFrameSPF" class="dpchFrameClass"></input></td>
					</tr>-->
					<tr>
						<td><label>Track Mode</label></td>
						<td colspan="3"><select id="trackMode">
									<option value="0">stop</option>
									<option value="1">start</option>
									<!-- <option value="2">Locate</option> -->
								</select></td>
					</tr>
					<tr>
						<td><label>GSM Cell Band</label></td>
						<td colspan="3">
						<select id="band2G"></select>
						</td>
					</tr>
					<tr>
						<td><label>ARFCN</label></td>
						<td colspan="3"><input type="text" value="" id="arfcn2G"></input></td>
					</tr>
					<tr>
						<td><label>Offline Mode</label></td>
						<td>
							<select id="offline_modeOF">
								<option value="0">Disable</option>
								<option value="1">Enable</option>
							</select>
						</td>
						<td>
							<select id="offline_modePPF">
								<option value="0">Disable</option>
								<option value="1">Enable</option>
							</select>
						</td>
						<td>
							<select id="offline_modeSPF">
								<option value="0">Disable</option>
								<option value="1">Enable</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><label>Offline File Size</label></td>
						<td><input type="text" value="" id="fileSizeOF" class=""></input></td>
						<td><input type="text" value="" id="fileSizePPF" class=""></input></td>
						<td><input type="text" value="" id="fileSizeSPF" class=""></input></td>
					</tr>
					<tr>
						<td><label>Offline Memory Threshold</label></td>
						<td><input type="text" value="" id="memoryThresholdOF" class=""></input></td>
						<td><input type="text" value="" id="memoryThresholdPPF" class=""></input></td>
						<td><input type="text" value="" id="memoryThresholdSPF" class=""></input></td>
					</tr>
					<tr>
						<td><label>Redirection Rat</label></td>
						<td><input type="text" value="" id="SUFI_REDIR_RAT_PREFEROF" class=""></input></td>
						<td><input type="text" value="" id="SUFI_REDIR_RAT_PREFERPPF" class=""></input></td>
						<td><input type="text" value="" id="SUFI_REDIR_RAT_PREFERSPF" class=""></input></td>
					</tr>
					<tr>
						<td><label>Signal Power</label></td>
						<td><input type="text" value="" id="Signal_PowerOF" class=""></input></td>
						<td><input type="text" value="" id="Signal_PowerPPF" class=""></input></td>
						<td><input type="text" value="" id="Signal_PowerSPF" class=""></input></td>
					</tr>
						<tr>
						<td><label>L1 ATTN</label></td>
						<td><input type="text" value="" id="L1_ATTOF" class=""></input></td>
						<td><input type="text" value="" id="L1_ATTPPF" class=""></input></td>
						<td><input type="text" value="" id="L1_ATTSPF" class=""></input></td>
						
						
						
					</tr>
					<tr>
						<td><label>P-B</label></td>
						<td><input type="text" value="" id="pbOF" class=""></input></td>
						<td><input type="text" value="" id="pbPPF" class=""></input></td>
						<td><input type="text" value="" id="pbSPF" class=""></input></td>
					</tr>
					<tr>
						<td><label>BandWidth</label></td>
						<td><input type="text" value="" id="DL_Channel_BandWidthOF" class=""></input></td>
						<td><input type="text" value="" id="DL_Channel_BandWidthPPF" class=""></input></td>
						<td><input type="text" value="" id="DL_Channel_BandWidthSPF" class=""></input></td>
					</tr>
					
					<tr>
						<td><label>RDL UARFCN</label></td>
						<td><input type="text" value="" id="RDL_UARFCNOF" class=""></input></td>
						<td><input type="text" value="" id="RDL_UARFCNPPF" class=""></input></td>
						<td><input type="text" value="" id="RDL_UARFCNSPF" class=""></input></td>
					</tr>
					</tbody>
				</table>
				</div>	
<div class="block" style="margin: 0 auto;">				
<table border="1" class="table table-bordered" id="add_sufi_cell_config">
				<thead style="color: white;">
				<tr>
				<th colspan="4">
				<span style="float: left;">E-SUFI Cell Configuration
				</span>
				<span class="spanStyle ti-arrow-circle-down" id="cellConfig">
				<span class="arrowClass" style="display:none;">+</span>
				</span>
				<span style="float: right;"><input type="button" id="scannedListButton"  class="btn btn-default" onclick="getScannedTable()" value="Select Operator from Scanned List"></input>
				</span>
				</th>
				</tr>
				<tr class="spanStyleThead">
					<th>Parameters</th>
					<th>OF</th>
					<th>PPF</th>
					<th>SPF</th>
				</tr>
				</thead>
					<tbody class="spanStyleTbody">
					<tr>
						<td><label>MCC</label></td>
						<td><span id="mccOF"></span></td>
						<td><span id="mccPPF"></span></td>
						<td><span id="mccSPF"></span></td>
					</tr>
					<tr>
						<td><label>MNC</label></td>
						<td><span id="mncOF"></span></td>
						<td><span id="mncPPF"></span></td>
						<td><span id="mncSPF"></span></td>
					</tr>
					<tr>
						<td><label>DL EARFCN</label></td>
						<td><span id="dlUarfcnOF"></span></td>
						<td><input type="text" value="" id="dlUarfcnPPF"></td>
						<td><input type="text" value="" id="dlUarfcnSPF"></td>
					</tr>
					<tr>
						<td><label>PCI</label></td>
						<td><span id="pscOF"></span></td>
						<td><input type="text" value="" id="pscPPF" min="0" max="511" class="pscClass"></td>
						<td><input type="text" value="" id="pscSPF" min="0" max="511" class="pscClass"></td>
					</tr>
					<tr>
						<td><label>Cell ID</label></td>
						<td><span id="cellIdOF"></span></td>
						<td><input type="text" value="" id="cellIdPPF" class="cellIdClass"></td>
						<td><input type="text" value="" id="cellIdSPF" class="cellIdClass"></td>
					</tr>
					<tr>
						<td><label>TAC Pool Start</label></td>
						<td><input type="text" value="" id="lacPoolStartOF" class="lacPoolStartClass"></td>
						<td><input type="text" value="" id="lacPoolStartPPF" class="lacPoolStartClass"></td>
						<td><input type="text" value="" id="lacPoolStartSPF" class="lacPoolStartClass"></td>
					</tr>
					<tr>
						<td><label>TAC Pool End</label></td>
						<td><input type="text" value="" id="lacPoolEndOF" class="lacPoolEndClass"></td>
						<td><input type="text" value="" id="lacPoolEndPPF" class="lacPoolEndClass"></td>
						<td><input type="text" value="" id="lacPoolEndSPF" class="lacPoolEndClass"></td>
					</tr>
					<tr>
						<td><label>Freq Band</label></td>
						<td><input type="text" value="" id="bandOF" class="cellIdClass"></td>
						<td><input type="text" value="" id="bandPPF" class="cellIdClass"></td>
						<td><input type="text" value="" id="bandSPF" class="cellIdClass"></td>
					</tr>
				</tbody>
				</table>
</div>	
<div class="block" style="margin: 0 auto;">				
<table border="1" class="table table-bordered" id="add_sufi_cell_config">
				<thead style="color: white;">
				<tr>
				<th colspan="4">
				<span style="float: left;">SIB Information</span>
				<span class="spanStyle ti-arrow-circle-down" id="sibInfoConfig">
				<span class="arrowClass" style="display:none;">+</span>
				</span>
				<span style="float: right;"><input type="button" id="cellScanButton"  class="btn btn-default" onclick="getCellScanSibInfoModal()" value="Select Cell Scan for Sib Info"></input>
				</span>
				</th>
				</tr>
				<tr class="spanStyleThead">
					<th>Param</th>
					<th>OF</th>
					<th>PPF</th>
					<th>SPF</th>
				</tr>
				</thead>
					<tbody class="spanStyleTbody">
						<!-- <tr>
						<td><label>Cell Select Quality Measure</label></td>
						<td><select id="cell_selection_quality_measure_OF">
									<option value="0">0</option>
									<option value="1">1</option>
								</select></td>
						<td><select id="cell_selection_quality_measure_PPF">
									<option value="0">0</option>
									<option value="1">1</option>
								</select></td>
						<td><select id="cell_selection_quality_measure_SPF">
									<option value="0">0</option>
									<option value="1">1</option>
								</select>
						</td>
					</tr>
					<tr>
						<td><label>Max. Report Cell On RACH</label></td>
						<td><select id="MAXIMUMREPORTEDCELLSONRACHOF">
									<option value="0">0</option>
									<option value="1">1</option>
								</select></td>
						<td><select id="MAXIMUMREPORTEDCELLSONRACHPPF">
									<option value="0">0</option>
									<option value="1">1</option>
								</select></td>
						<td><select id="MAXIMUMREPORTEDCELLSONRACHSPF">
									<option value="0">0</option>
									<option value="1">1</option>
								</select></td>
					</tr> -->
					<tr>
						<td><label>SIB Info Neighbours</label></td>
						<td><input type="button" value="Show/Update" id="sibInfoOf" class="btn btn-default" onclick="showSibInfoModal('of')"></td>
						<td><input type="button" value="Show/Update" id="sibInfoPpf" class="btn btn-default" onclick="showSibInfoModal('ppf')"></td>
						<td><input type="button" value="Show/Update" id="sibInfoSpf" class="btn btn-default" onclick="showSibInfoModal('spf')"></td>
					</tr>
				</tbody>
				</table>
</div>

<div class="block" style="margin: 0 auto;">				
<table border="1" class="table table-bordered" id="add_sufi_cell_config">
				<thead style="color: white;">
				<tr>
				<th colspan="4">
				<span style="float: left;">Subscriber Information</span>
				<span class="spanStyle ti-arrow-circle-down" id="subInfoConfig">
				<span class="arrowClass" style="display:none;">+</span>
				</span>
				</th>
				</tr>
				<tr class="spanStyleThead">
					<th>Parameters</th>
                    <th colspan="3"></th>
				</tr>
				</thead>
				<tbody class="spanStyleTbody">
					<tr>
						<td>
							<label>Subscriber Info</label>
						</td>
						<td colspan="3">
							<table border="1" id="displayTargetTable" style="margin: 0 auto 10px auto;text-align:center;" class="table table-default report_tables">
								<thead>
									<tr>
										<th>IMSI</th>
										<th>IMEI</th>
									</tr>
								</thead>
								<tbody id="displayTargetTable_body">
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<td><label>Subscriber Mode</label></td>
						<td colspan="3">
							<select id="subMode">
								<option value="0">Inclusion</option>
								<option value="1">Exclusion</option>
								<option value="2">Locate</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>
						<label>Hold Subscriber Info</label>
						</td>
						<td colspan="3">
							    	<table border="1" id="displayHoldTargetTable" style="margin: 0 auto 10px auto;text-align:center;" class="table table-default report_tables">
								<thead>
									<tr>
										<th>IMSI</th>
										<th>IMEI</th>
									</tr>
								</thead>
								<tbody id="displayHold_body">
								</tbody>
							</table>
							<br>
							    	<table style="width: 100%" class="table table-nested">							    		
									<tbody>
										<tr>
							    		<td>
										<label>SUB ID : </label>
										<input type="number" id="hold_sys_sub_id" name="hold_sys_sub_id"></input>
										</td>
							    		<td>
							    		<label>Type : </label>
							    		<select id="hold_sys_sub_type" name="hold_sys_sub_type">
										<option value="0">IMEI</option>
										<option value="1">IMSI</option>
										</select>
										</td>
							    		<td>
							    		<button id="addHodlSubBtn"class="btn btn-default">Add</button>
										<button onclick="updateSubHoldOnClick()" class="btn btn-default">Update Hold Info</button>
										<button onclick="clearHoldSub()" class="btn btn-default">Clear</button>
										</td>
							    		</tr>
							    	</tbody>
								</table>
						</td>
					</tr>
				</tbody>
				<tfoot id="subscriberInfoTfoot">
					<tr id="subscriberInfoTr">
					<th colspan="4">
					<!-- <button onclick="updateSibInfoOnClick()" class="btn btn-default" id="subscriberInfoButton" style="float: right;">Update Subscriber Info</button> -->
					</th>
					</tr>
					</tfoot>
				</table>
</div>
<div class="block" style="margin: 0 auto;">
<button onclick="setConfigValues()" style="width: 200px;height: 50px;float: right;" class="btn btn-default">Refresh</button>
<button onclick="setConfiguration()" style="width: 200px;height: 50px;float: right;" class="btn btn-default">Apply Configuraton</button>
<button onclick="mesTrigger(1)" style="width: 200px;height: 50px;float: right;" class="btn btn-default">MES Start</button>
<button onclick="mesTrigger(0)" style="width: 200px;height: 50px;float: right;" class="btn btn-default">MES Stop</button>
</div>  
</div>
</div>	
</div>
		<div class="modal fade" id="myPopup" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 1180px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myPopupLabel">Cell Scan Data</h4>
				</div>
				<div class="modal-body">
				<div id="topdiv">			
			<div id="sub_det" style='max-width:1348px;'>
				<table id="data_tab" style='max-width:1348px;'></table>
    			<div id="pager"></div>	
			</div>
		</div>				
				</div>
				<div class="modal-footer">
				<button class="btn btn-default" onclick="cellAndSibUpdateLTE()">Update</button>
				<button class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
	</div><!-- modal-dialog -->
	</div><!-- modal -->  
		

<div class="modal fade" id="sibInfoModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 1100px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="sibInfoLabel">SIB INFO NEIGHBOURS</h4>
				</div>
				<div class="modal-body">
				<input type="hidden" id="sibSufiType"></input>
				<div id="SibInfoOfDiv">
					<div class="block" style="margin: 0 auto;">						
					<table  border=1 id="cell_info_sib_info_OF" class="table table-bordered">
					<thead style="color: white;">
						<th colspan=6>SIB INFO</th>
					</thead>
					<tbody>
						<tr>
							<td>INTRA_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="intera_sib_table_OF" class="intra">	
									<thead>						    		
										
							    		<tr style="font-size:10px">
											<td>PCI</td>
											<td>CELL_ID</td>
											<td>TAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
									
									</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="intera_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						<tr>
							<td>INTER_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_sib_table_OF" class="intra">		
									<thead>					    		
											<tr style="font-size:10px">
											<td>DL Carrier Frequency</td>
											<td>Priority</td>
											<td>CELL_ID</td>
											<td>TAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
										
									</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						<tr>
							<td>INTER_RAT_2G</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_rat_2g_sib_table_OF" class="intra">		
									<thead>	
										<tr style="font-size:10px">
											<td>Starting_ARFCN</td>
											<td>NCC Permitted</td>
											<td>Band Indicator</td>
											<td>Priority</td>
											<td>CELL_ID</td>
											<td>LAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
							    	</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_rat_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						<tr>
							<td>INTER_RAT_3G</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_rat_3g_sib_table_OF" class="intra">		
									<thead>					    		
										<tr style="font-size:10px">
											<td>DL_Carrier_Frequency</td>
											<td>Priority</td>
											<td>LAC</td>
											<td>CELL_ID</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
							    	</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_rat_add_field_button">Add More Fields</button>-->
							</td>
						</tr>	
					</tbody>
								
				</table>
								
			</td>
		</tr>
	</table>		
</div>	
</div>
				<div id="SibInfoPpfDiv">
					<div class="block" style="margin: 0 auto;">		
					<table  border=1 id="cell_info_sib_info_PPF" class="table table-bordered">
					<thead style="color: white;">
						<th colspan=6>SIB INFO</th>
					</thead>
					<tbody>	
						<tr>
							<td>INTRA_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="intera_sib_table_PPF" class="intra">	
									<thead>						    		
										
							    		<tr style="font-size:10px">
											<td>PCI</td>
											<td>CELL_ID</td>
											<td>TAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
									</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="intera_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						<tr>
							<td>INTER_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_sib_table_PPF" class="intra">		
									<thead>					    		
										<tr style="font-size:10px">
											<!-- <td>PCI1</td>
											<td>PCI2</td> -->
											<td>DL Carrier Frequency</td>
											<td>Priority</td>
											<td>CELL_ID</td>
											<td>TAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
			
									</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						<tr>
							<td>INTER_RAT_2G</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_rat_2g_sib_table_PPF" class="intra">		
									<thead>	
										<tr style="font-size:10px">
											<td>Starting_ARFCN</td>
											<td>NCC Permitted</td>
											<td>Band Indicator</td>
											<td>Priority</td>
											<td>CELL_ID</td>
											<td>LAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
							    	</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_rat_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						<tr>
							<td>INTER_RAT_3G</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_rat_3g_sib_table_PPF" class="intra">		
									<thead>					    		
										<tr style="font-size:10px">
											<td>DL_Carrier_Frequency</td>
											<td>Priority</td>
											<td>LAC</td>
											<td>CELL_ID</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
							    	</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_rat_add_field_button">Add More Fields</button>-->
							</td>
						</tr>		
					</tbody>
								
				</table>
								
			</td>
		</tr>
	</table>		
</div>	
</div>
				<div id="SibInfoSpfDiv">
					<div class="block" style="margin: 0 auto;">		
					<table  border=1 id="cell_info_sib_info_SPF" class="table table-bordered">
					<thead style="color: white;">
						<th colspan=6>SIB INFO</th>
					</thead>
					<tbody>		
						<tr>
							<td>INTRA_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="intera_sib_table_SPF" class="intra">	
									<thead>						    		
										<tr style="font-size:10px">
											<td>PCI</td>
											<td>CELL_ID</td>
											<td>TAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
									</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="intera_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						<tr>
							<td>INTER_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_sib_table_SPF" class="intra">		
									<thead>					    		
										
							    		<tr style="font-size:10px">
											<!-- <td>PCI1</td>
											<td>PCI2</td> -->
											<td>DL Carrier Frequency</td>
											<td>Priority</td>
											<td>CELL_ID</td>
											<td>TAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
			
									</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						
						<tr>
							<td>INTER_RAT_2G</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_rat_2g_sib_table_SPF" class="intra">		
									<thead>	
										<tr style="font-size:10px">
											<td>Starting_ARFCN</td>
											<td>NCC Permitted</td>
											<td>Band Indicator</td>
											<td>Priority</td>
											<td>CELL_ID</td>
											<td>LAC</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
							    	</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_rat_add_field_button">Add More Fields</button>-->
							</td>
						</tr>
						<tr>
							<td>INTER_RAT_3G</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_rat_3g_sib_table_SPF" class="intra">		
									<thead>					    		
										<tr style="font-size:10px">
											<td>DL_Carrier_Frequency</td>
											<td>Priority</td>
											<td>LAC</td>
											<td>CELL_ID</td>
											<td>MCC</td>
											<td>MNC</td>
							    		</tr>
									</thead>
									<tbody>
							    	</tbody>
							    	</table>
							    </div>
							</div>
							<!--<button class="inter_rat_add_field_button">Add More Fields</button>-->
							</td>
						</tr>	
					</tbody>
								
				</table>
								
			</td>
		</tr>
	</table>		
</div>	
</div>
</div>
				<div class="modal-footer">
				<!-- <button class="btn btn-default" onclick="sibUpdateOnClick()">Update</button> -->
				<button class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
	</div><!-- modal-dialog -->
	</div><!-- modal -->  
	
	<div class="modal fade" id="cellScanSibInfoModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 1180px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="cellScanSibInfoLabel">Cell Scan Data</h4>
				</div>
				<div class="modal-body">
				<fieldset id="target_scans">			
			</fieldset>
				<table id="cellScanDataTable" style='max-width:1348px;'></table>
    			<div id="cellScanDataPager"></div>			
				</div>
				<div class="modal-footer">
				<button class="btn btn-default" onclick="selectRowsCellScanData()">Select</button>
				<button class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
	</div><!-- modal-dialog -->
	</div><!-- modal -->  	
	<script>
/*$(function() {
    $(".dateSelect").datetimepicker({
    	dateFormat: "yy-mm-dd",
    	timeFormat: 'HH:mm'
    });
  });*/
</script>
<script type="text/javascript" src="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jquer_ui_1_11_4/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jqgrid/css/ui.jqgrid.css" />
<script type="text/javascript" src="../../resources/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../../resources/lib/jqgrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="../../resources/lib/jqgrid/js/i18n/grid.locale-en.js"></script>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jsonviewer/jquery.json-viewer.css"/>
<script type="text/javascript" src="../../resources/lib/jsonviewer/jquery.json-viewer.js"></script>
<script type="text/javascript" src="../../resources/js/4g/groupConfiguration.js"></script>
<script type="text/javascript" src="../../resources/js/4g/GroupConfigurationStructure.js"></script>
<script type="text/javascript" src="../../resources/js/netscan/umtsreport.js"></script>
<!-- <script type="text/javascript" src="../../resources/js/3g/3g_NodeConfiguration.js"></script> -->
<script type="text/javascript" src="../../resources/js/4g/NodeConfigurationStructure.js"></script>

<script type="text/javascript"> 

   </script>
   <style>
   .ui-datepicker
   {
   	z-index:1051 !important;
   }
   <%= session.getAttribute("themeCss")%>
   </style>
</body>
</html>