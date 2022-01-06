<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
	<script>	
		 var deviceIp  = <%= request.getParameter("ip").equalsIgnoreCase("null")?"":"'"+request.getParameter("ip")+"'"%>
	 	var deviceId = <%= request.getParameter("id").equalsIgnoreCase("null")?"":request.getParameter("id")%>
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
 	max-height:208px;
 	overflow:auto;
 	display:block;
 	font-size:13px;
 }
 .card
 {
 	width: auto !important;
 	height:245px;
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
						Configuration parameters
					</th>
				</thead>
					<tr>
						<td colspan=2><label>Config Parameter</label></td>
						<td colspan=4>
							<select id="para_btsmask">
								<option value=3>ALL</option>
								<option value=1>SYS_PARAMS</option>
								<option value=2>CELL_PARAMS</option>
								
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>System Parameters</label></td>
						<td colspan=4>
							<select id="sys_para_btsmask">
								<option value=4>ALL</option>
								<option value=1>SUB_INFO</option>
								<option value=2>CELL_INFO</option>
								<option value=3>SUFI_PARAMS</option>
								
							</select>
						</td>
					</tr>
					<tr>
							<td colspan=2>
								<label>Cell Parameters</label>
							</td>
							<td colspan=4>
								<select id="cell_para_btsmask">
									<option value=7>ALL</option>
									<option value=1>REDIRECTION_INFO</option>
									<option value=2>SIB_INFO</option>
									<option value=4>GENERAL</option>
									
								</select>
							</td>
						</tr>
						<tr>
							<td colspan=2><br></td>
							<td colspan=4></td>
						</tr>
						<tr>
							<td colspan=2><br></td>
							<td colspan=4></td>
						</tr>
						<tr>
							<td colspan=2><br></td>
							<td colspan=4></td>
						</tr>
				</table>		     
					  
			</div>
			<div class="card">
					  
						<table border=1 class="tab_com" id="config_sufi_para">
				<thead>
					<th colspan=6>
						Sufi parameters
					</th>
				</thead>
					<tr>
						<td colspan=2><label>LOG_LEVEL</label></td>
						<td colspan=4>
							<select id=log_level>
									<option value=0>CRITICAL</option>
									<option value=1>ERROR</option>
									<option value=2>WARNING</option>
									<option value=3>DEBUG</option>
									<option value=4>INFO</option>
								</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>SUB_HOLD_TIMER(&mu;s)</label></td>
						<td colspan=4>
							<input type="text" id=sub_hold_timer value="">
						</td>
					</tr>
					<tr>
							<td colspan=2>
								<label>SUB_REDIR_TIMER(&mu;s)</label>
							</td>
							<td colspan=4>
								<input type="text" id=sub_redir_timer value="">
							</td>
						</tr>
						<tr>
						<td colspan=2><label>SUFI_OP_MODE</label></td>
						<td colspan=4>
							<select id=sufi_op_mode readonly>
									<option value=0>OF</option>
									<option value=1>PPF</option>
									<option value=2>SPF</option>
								</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
							<td colspan=2><br></td>
							<td colspan=4></td>
						</tr>
						<tr>
							<td colspan=2></td>
							<td colspan=4></td>
						</tr>
				</table>		     
					  
			</div>
			<div class="card">
					<table border=1 id="system_parameters" class="tab_com">
				<thead><th colspan=3>Subscriber Info</th></thead>
					
						<tr>
			    			<td colspan=1>
			    				<label>SUB MODE</label>
			    			</td>
			    			<td colspan=2>
			    				<select id=sys_sub_type>
									<option value=0>Inclusion</option>
									<option value=1>Exclusion</option>
								</select>
							</td>
						</tr>
					<tr>
						<td colspan=1><label>Subscriber Info</label></td>
						<td colspan=2>
							<div class="input_fields_wrap">
							    <div>
							    	<table>							    		
										<tr>
							    			<td><label>SUB_ID : </label><input type="text" name="sys_sub_id[]"></td>
							    			<td>
							    				<label>Type : </label>
							    				<select name="sys_sub_type[]">
													<option value=0>IMEI</option>
													<option value=1>IMSI</option>
												</select>
											</td>
							    			<td>&nbsp;</td>
							    		</tr>
							    	</table>
							    </div>
							</div>
							<button class="add_field_button">Add More Fields</button>
							<button onclick="updateSibInfo()">Update</button>
						</td>
					</tr>
					<tr>
						<td colspan=1><label>Hold Subscriber Info</label></td>
						<td colspan=2>
							<div class="input__hold_fields_wrap">
							    <div>
							    	<table>							    		
										<tr>
							    			<td><label>SUB_ID : </label><input type="text" id="hold_sys_sub_id" name="hold_sys_sub_id"></td>
							    			<td>
							    				<label>Type : </label>
							    				<select id = "hold_sys_sub_type" name=hold_sys_sub_type>
													<option value=0>IMEI</option>
													<option value=1>IMSI</option>
												</select>
											</td>
							    			<td><button onclick="updateSubHold()">Update</button></td>
							    		</tr>
							    	</table>
							    </div>
							</div>
							
						</td>
					</tr>
				</table>	
				</div>
								
				<div class="card">
								<table  border=1 id="cell_info_sib_info" class="tab_com">
					<thead>
						<th colspan=6>SIB INFO</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=2>CELL SELECTION QUALITY MEASURE</td>
							<!--  <td colspan=2><input type="text" id="cell_selection_quality_measure" value="" ></td>-->
							<td colspan=2>
								<select id="cell_selection_quality_measure">
									<option value="0">0</option>
									<option value="1">1</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan=2>MAXIMUMREPORTEDCELLSONRACH</td>
							<!--  <td colspan=2><input type="text" id="MAXIMUMREPORTEDCELLSONRACH" value="" ></td>-->
							<td colspan=2>
								<select id="MAXIMUMREPORTEDCELLSONRACH">
									<option value="0">0</option>
									<option value="1">1</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<td>INTRA_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="intera_sib_table" class="intra">							    		
										<tr style="font-size:10px">
											<td>PSC</td>
											<td>Q_OFFSET_1S</td>
											<td>Q_OFFSET_2S</td>
											<td>Q_QUALMIN</td>
											<td>Q_RXLEVMIN</td>
											<td>CELL_ID</td>
											<td>LAC</td>
											<td>MCC</td>
											<td>MNC</td>
											<td></td>
							    		</tr>
							    		<tr class="intera_input_row">
											<td data-param="PSC" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
											<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="intera_text" type="text" value="1"></td>
											<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="intera_text" type="text" value="1"></td>
											<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="intera_text" type="text" value="-18"></td>
											<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="intera_text" type="text" value="-24"></td>
											<td data-param="CELL_ID" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
											<td data-param="LAC" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
											<td data-param="MCC" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
											<td data-param="MNC" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
											<td></td>
							    		</tr>
							    	</table>
							    </div>
							</div>
							<button class="intera_add_field_button">Add More Fields</button>
							</td>
						</tr>
						<tr>
							<td>INTER_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_sib_table" class="intra">							    		
										<tr style="font-size:10px">
											<td>PSC</td>
											<td>DL_UARFCN</td>
											<td>PCPICH_TX_POWER</td>
											<td>Q_OFFSET_1S</td>
											<td>Q_OFFSET_2S</td>
											<td>Q_QUALMIN</td>
											<td>Q_RXLEVMIN</td>
											<td>CELL_ID</td>
											<td>LAC</td>
											<td>MCC</td>
											<td>MNC</td>
											<td></td>
							    		</tr>
							    		<tr class="inter_input_row">
											<td data-param="PSC" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="DL_UARFCN" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="PCPICH_TX_POWER" class="inter_opr_td"><input class="inter_text" type="text" value="10"></td>
											<td data-param="Q_OFFSET_1S" class="inter_opr_td"><input class="inter_text" type="text" value="1"></td>
											<td data-param="Q_OFFSET_2S" class="inter_opr_td"><input class="inter_text" type="text" value="1"></td>
											<td data-param="Q_QUALMIN" class="inter_opr_td"><input class="inter_text" type="text" value="-18"></td>
											<td data-param="Q_RXLEVMIN" class="inter_opr_td"><input class="inter_text" type="text" value="-24"></td>
											<td data-param="CELL_ID" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="LAC" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="MCC" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="MNC" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td></td>
							    		</tr>
							    	</table>
							    </div>
							</div>
							<button class="inter_add_field_button">Add More Fields</button>
							</td>
						</tr>
						<tr>
							<td>INTER_RAT</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="inter_rat_sib_table" class="intra">							    		
										<tr style="font-size:10px">
											<td>Q_RXLEVMIN</td>
											<td>LAC</td>
											<td>CELL_ID</td>
											<td>Q_OFFSET1S_N</td>
											<td>INDIVIDUAL OFFSET</td>
											<td>NCC</td>
											<td>BCC</td>
											<td>FREQ_BAND</td>
											<td>BCCH_ARFCN</td>
											
											<td></td>
							    		</tr>
							    		<tr class="inter_rat_input_row">
											<td data-param="Q_RXLEVMIN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="-24"></td>
											<td data-param="LAC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="CELL_ID" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="Q_OFFSET1S_N" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="1"></td>
											<td data-param="CELLINDIVIDUALOFFSET" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="1"></td>
											<td data-param="NCC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="BCC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="FREQ_BAND" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="BCCH_ARFCN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td></td>
							    		</tr>
							    		
							    	</table>
							    </div>
							</div>
							<button class="inter_rat_add_field_button">Add More Fields</button>
							</td>
						</tr>
						<tr><td colspan=4><button onclick="sibUpdate()">Update</button></td></tr>
				
				</tbody>
								
								</table>
								
								</td>
								</tr>
								</table>
								</div>		
				<div class="card">
				<table border=1 id="cell_info" class="tab_com">
					<thead>
						<th colspan=6>CELL INFO</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=3>SUFI_ID</td>
							<td colspan=3><input type="text" id="sufi_id" value="" readonly></td>
						</tr>
						<tr>
							<td colspan=3>PRI_SCRAM_CODE</td>
							<td colspan=3><input type="range" id="scram_code" value="0"  min="0" max="511" ><br>
							<label id="range1">0</label></td>
							</td>
						</tr>
						<tr>
							<td colspan=3>LAC_POOL_START</td>
							<td colspan=3>
								<input type="range" id="lac_pool_start" value="1"  min="1" max="65535" ><br>
								<label id="range2">1</label></td>
							</td>
						</tr>
						
						<tr>
							<td colspan=3>LAC_POOL_END</td>
							<td colspan=3><input type="range" id="lac_pool_end" value="1000" min="2" max="65535"><br>
						      <label id="range3">1000</label></td>
						</tr>
						<tr>
							<td colspan=3>CELL ID</td>
							<td colspan=3><input type="range" id="cell_id" value="1" min="1" max="65535"><br>
						<label id="range4">1</label></td>
						</tr>
						
						<tr>
							<td colspan=3>TOTAL_TX_POWER</td>
							<td colspan=3><input type="number" id="total_tx_power" value="10" ></td>
						</tr>
						<tr>
							<td colspan=3>PCPICH_POWER_PERC</td>
							<!--  <td colspan=3><input type="number" id="pcpich_power_perc" value="10" ></td>-->
							<td colspan=3>
								<select id="pcpich_power_perc">
									<option value="10">10</option>
									<option value="5">5</option>
								</select></td>
						</tr>						
						<tr>
							<td colspan=3>DL_UARFCN</td>
							<td colspan=3><input type="text" id="dl_uarfcn" value=""></td>
						</tr>
						<tr>
							<td colspan=3>UL_UARFCN</td>
							<td colspan=3><input type="text" id="ul_uarfcn" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>MCC</td>
							<td colspan=3><input type="text" id="cell_mcc" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>MNC</td>
							<td colspan=3><input type="text" id="cell_mnc" value="" ></td>
						</tr>
					</tbody>
				</table>
				</div>
				<div class="card">
				<table border=1 id="cell_params" class="tab_com">
					<thead>
						<th colspan=6>CELL PARAMS</th>
					</thead>
					<tbody>
					
						
						<tr>
							<td colspan=3>DPCCH POWER OFF</td>
							<td colspan=3>
								<input type="range" id="power_off" value="-25" min="-110" max="-35" ><br>
							<label id="range5" value="-45">-45</label></td>
							</td>
						</tr>
						<tr>
							<td colspan=3>DPCH FRAME OFF</td>
							<td colspan=3>
								<input type="range" id="frame_off" value="20" min="0" max="50" ><br>
							<label id="range6" value="20">20</label></td>	
								
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<div class="card">
				<table border=1 id="cell_redirection_info" class="tab_com">
					<thead>
						<th colspan=6>REDIRECTION INFO</th>
					</thead>
					<tbody>
						
						<tr>
							<td colspan=3>SUFI OF RDL UARFCN</td>
							<td colspan=3>
								<input type="text" id="of_rdl_uarfcn" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>FREQ BAND</td>
							<td colspan=3>
								<input type="text" id="2g_freq_band" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>BCCH ARFCN</td>
							<td colspan=3>
								<input type="text" id="2g_bcch_arfcn" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI PPF RDL UARFCN</td>
							<td colspan=3>
								<input type="text" id="ppf_rdl_uarfcn" value="" >
							</td>
						</tr>
					    <tr>
							<td colspan=3>SUFI SPF RDL UARFCN</td>
							<td colspan=3>
								<input type="text" id="sf_rdl_uarfcn" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI OPER REDIR INFO</td>
							<td colspan=3>
								<input type="text" id="oper_rdl_uarfcn" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=6><button onclick="updateRedirectionInfo()">Update</button></td>
						</tr>
					</tbody>
				</table>
				</div>

												<table style="width:100%;text-align:center">
									<tr><td><button id="upateConfig" onclick="setConfig()">Update</button><button onclick="getConfig()">Get Config</button><button onclick="mesTrigger(1)">MES Start</button><button onclick="mesTrigger(0)">MES Stop</button><button onclick="setCellLock(false);">Lock</button><button onclick="setCellUnLock();">UnLock</button></tr>
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
<div id="track_modal" style="display:none;">
	<table>
		<thead>
			<th>SUB-ID</th>
			<th>Type</th>
			<th>Mode</th>
			<th></th>
		</thead>
		<tbody>
			<td><input id="track_id" type=number value="123" /></td>
			<td>
				<select id="tarck_sub_type">
					<option value=0>IMEI</option>
					<option value=1>IMSI</option>
				</select>
			</td>
			<td>
				<select id=track_type_mode>
					<option value=1>Start</option>
					<option value=0>Stop</option>
				</select>
			</td>
			<td>
				<button onClick="setUpTrackMode()">Update</button>
			</td>
		</tbody>
	</table>
</div>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>

<script type="text/javascript" src="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<script type="text/javascript" src="../../resources/js/3g/3g_configuration.js"></script>
<script type="text/javascript" src="../../resources/js/3g/configurationStructure.js"></script>

</body>
</html>