<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
	<script>	
		 var deviceIp  = <%= request.getParameter("ip").equalsIgnoreCase("null")?"":"'"+request.getParameter("ip")+"'"%>
	 	var deviceId = <%= request.getParameter("id").equalsIgnoreCase("null")?"":request.getParameter("id")%>
	 </script>
 <style>
 	#tab_container
 	{
 		width:100%;
 	}
 		.card {
    	/* Add shadows to create the "card" effect */
    		box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
    		transition: 0.3s;
    		width:212px;
    		float:left;
    		margin: 0 auto 10px 10px;
    		color:white;
		}

		/* On mouse-over, add a deeper shadow */
		.card:hover {
		    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
		}

		/* Add some padding inside the card container */
		.card .container {
		    padding: 2px 16px;
		}
		.up_sufi
		{
			background:#CDDC39;
			color:white;
			
		}
		.down_sufi
		{
			background:#F44336;
			color:white;
			
		}
		.total_sufi
		{
			background:#00BCD4;
			color:white;
			
		}
		
		#left_box {
     width: 80%;
     background-color: white;
     padding: 0;
     margin: 0;
     display: block;
     border: 1px solid white;
     	margin:0 auto;
     }

#right_box {
     width: 49%px;
     background-color: #777;
     display: block;
     float: left;
     border: 1px solid white;
}	
 
 
 #inter_frq_list_tab input
 {
 	width:100px;
 }	
 </style>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="container">
	<div id="message_box">
		<div id="left_box">
			<table border=1 id="add_sufi_tab" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">
				<thead style="background: navajowhite;"><th colspan=6>SET SUFI CONFIG</th></thead>
					<tr>
						<td colspan=2><label>BTSMASK</label></td>
						<td colspan=4>
							<select id="para_btsmask">
								<option value=1>SYS_PARAMS</option>
								<option value=2>CELL_PARAMS</option>
								<option value=3>ALL</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
				</table>
				<table border=1 id="system_parameters" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">
				<thead style="background: navajowhite;"><th colspan=3>System Parameters</th></thead>
					<tr>
						<td colspan=1><label>BTSMASK</label></td>
						<td colspan=2>
							<select id="sys_para_btsmask">
								<option value=1>SUB_INFO</option>
								<option value=2>CELL_INFO</option>
								<option value=3>SUFI_PARAMS</option>
								<option value=4>ALL</option>
							</select>
						</td>
					</tr>
					<tr>
			    			<td colspan=1>
			    				<label>LOG_LEVEL</label>
			    			</td>
			    			<td colspan=2>
			    				<select id=log_level>
									<option value=0>CRITICAL</option>
									<option value=1>ERROR</option>
									<option value=2>WARNING</option>
									<option value=3>DEBUG</option>
									<option value=4>INFO</option>
								</select>
							</td>
						</tr>
						<tr>
			    			<td colspan=1>
			    				<label>SUB_HOLD_TIMER</label>
			    			</td>
			    			<td colspan=2>
			    				<input type="text" id=sub_hold_timer value="">
							</td>
						</tr>
						<tr>
			    			<td colspan=1>
			    				<label>SUB_REDIR_TIMER</label>
			    			</td>
			    			<td colspan=2>
			    				<input type="text" id=sub_redir_timer value="">
							</td>
						</tr>
						
						<tr>
			    			<td colspan=1>
			    				<label>SUFI_OP_MODE</label>
			    			</td>
			    			<td colspan=2>
			    				<select id=sufi_op_mode>
									<option value=0>OF</option>
									<option value=1>PPF</option>
									<option value=2>SPF</option>
								</select>
							</td>
						</tr>
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
				<table border=1 id="cell_info" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">
					<thead style="background: navajowhite;">
						<th colspan=6>CELL INFO</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=3>SUFI_ID</td>
							<td colspan=3><input type="text" id="sufi_id" value="" readonly></td>
						</tr>
						<tr>
							<td colspan=3>PRI_SCRAM_CODE</td>
							<td colspan=3><input type="text" id="scram_code" value=""></td>
						</tr>
						<!-- <tr>
							<td colspan=3>SUFI_OP_MODE</td>
							<td colspan=3>
								<select id="sufi_op_mode">
									<option value=0>OF</option>
									<option value=1>PPF</option>
									<option value=2>SPF</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan=3>LAC</td>
							<td colspan=3><input type="text" id="lac" value="" ></td>
						</tr>
							<tr>
							<td colspan=3>RAC_POOL_START</td>
							<td colspan=3><input type="text" id="rac_pool_start" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>RAC_POOL_END</td>
							<td colspan=3><input type="text" id="rac_pool_end" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>2G_TRACKER_FREQ</td>
							<td colspan=3><input type="text" id="2g_track_req" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>PRIVATE_FREQ 1</td>
							<td colspan=3><input type="text" id="p_freq_1" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>PRIVATE_FREQ 2</td>
							<td colspan=3><input type="text" id="p_freq_2" value="" ></td>
						</tr>-->
						<tr>
							<td colspan=3>LAC_POOL_START</td>
							<td colspan=3><input type="text" id="lac_pool_start" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>LAC_POOL_END</td>
							<td colspan=3><input type="text" id="lac_pool_end" value="" ></td>
						</tr>
						
						<tr>
							<td colspan=3>CELL ID</td>
							<td colspan=3><input type="text" id="cell_id" value=""></td>
						</tr>
						<tr>
							<td colspan=3>TOTAL_TX_POWER</td>
							<td colspan=3><input type="text" id="total_tx_power" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>PCPICH_POWER_PERC</td>
							<td colspan=3><input type="text" id="pcpich_power_perc" value="" ></td>
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
				<table border=1 id="cell_info" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">
					<thead style="background: navajowhite;">
						<th colspan=6>CELL PARAMS</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=3>
								<label>BTSMASK</label>
							</td>
							<td colspan=3>
								<select id="cell_para_btsmask">
									<option value=1>REDIRECTION_INFO</option>
									<option value=2>SIB_INFO</option>
									<option value=4>GENERAL</option>
									<option value=7>ALL</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan=3>DPCCH POWER OFF</td>
							<td colspan=3>
								<input type="text" id="power_off" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>DPCH FRAME OFF</td>
							<td colspan=3>
								<input type="text" id="frame_off" value="" >
							</td>
						</tr>
					</tbody>
				</table>
				<table border=1 id="cell_info" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">
					<thead style="background: navajowhite;">
						<th colspan=6>REDIRECTION INFO</th>
					</thead>
					<tbody>
						<!-- <tr>
							<td colspan=3>SUFI OF REDIRECT FLAG</td>
							<td colspan=3>
								<input type="text" id="of_redirect_flag" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI PPF REDIRECT FLAG</td>
							<td colspan=3>
								<input type="text" id="ppf_redirect_flag" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI SF REDIRECT FLAG</td>
							<td colspan=3>
								<input type="text" id="sf_redirect_flag" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>2G REDIRECT FLAG</td>
							<td colspan=3>
								<input type="text" id="2g_redirect_flag" value="" >
							</td>
						</tr>
						-->
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
							<td colspan=3>SUFI SF RDL UARFCN</td>
							<td colspan=3>
								<input type="text" id="sf_rdl_uarfcn" value="" >
							</td>
						</tr>
						
						<tr>
							<td colspan=6><button onclick="updateRedirectionInfo()">Update</button></td>
						</tr>
					</tbody>
				</table>
				<table  border=1 id="cell_info" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">
					<thead style="background: navajowhite;">
						<th colspan=4>SIB INFO</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=2>CELL SELECTION QUALITY MEASURE</td>
							<td colspan=2><input type="text" id="cell_selection_quality_measure" value="" ></td>
						</tr>
						<tr>
							<td colspan=2>MAXIMUMREPORTEDCELLSONRACH</td>
							<td colspan=2><input type="text" id="MAXIMUMREPORTEDCELLSONRACH" value="" ></td>
						</tr>
						
						<tr>
							<td>INTRA_FREQ</td>
							<td colspan=3>
							<div>
							    <div>
							    	<table border=1 id="intera_sib_table">							    		
										<tr>
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
											<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
											<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
											<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
											<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>
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
							    	<table border=1 id="inter_sib_table">							    		
										<tr>
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
											<td data-param="PCPICH_TX_POWER" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="Q_OFFSET_1S" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="Q_OFFSET_2S" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="Q_QUALMIN" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
											<td data-param="Q_RXLEVMIN" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>
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
							    	<table border=1 id="inter_rat_sib_table">							    		
										<tr>
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
											<td data-param="Q_RXLEVMIN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="LAC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="CELL_ID" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="Q_OFFSET1S_N" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
											<td data-param="CELLINDIVIDUALOFFSET" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>
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
				</tbody></table>
				<!-- <table  border=1 id="cell_info" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">
					<thead style="background: navajowhite;">
						<th colspan=4>SIB INFO</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=2>CELL SELECTION QUALITY MEASURE</td>
							<td colspan=2><input type="text" id="cell_selection_quality_measure" value="" ></td>
						</tr>
						
							<tr>
												<td></td>
												<td>INTRA_FREQ</td>
												<td>INTER_FREQ</td>
												<td>INTER RAT</td>												
										</tr>
										<tr>
											<td>PSC</td>
											<td><input type=text id="intra_ppf_psc" value=""/></td>
											<td><input type=text id="of_ppf_psc" value=""/></td>
											<td></td>											
										</tr>
										<tr>
											<td>UARFCN</td>
											<td></td>
											<td><input type=text id="of_ppf_uarfcn" value=""/></td>
											<td></td>
										</tr>
										<tr>
											<td>PCPICH_TX_POWER</td>
											<td></td>
											<td><input type=text id="of_ppf_pcpich" value=""/></td>
											<td></td>
										</tr>
										<tr>
											<td>Q_OFFSET_1S</td>
											<td><input type=text id="intra_ppf_1s" value=""/></td>
											<td><input type=text id="of_ppf_1s" value=""/></td>
											<td></td>
										</tr>
										<tr>
											<td>Q_OFFSET_2S</td>
											<td><input type=text id="intra_ppf_2s" value=""/></td>
											<td><input type=text id="of_ppf_2s" value=""/></td>
											<td></td>
										</tr>
										<tr>
											<td>Q_QUALMIN</td>
											<td><input type=text id="intra_ppf_qualmin" value=""/></td>
											<td><input type=text id="of_ppf_qualmin" value=""/></td>
											<td></td>
										</tr>
										<tr>
											<td>Q_RXLEVMIN</td>
											<td><input type=text id="intra_ppf_rxlevmin" value=""/></td>
											<td><input type=text id="of_ppf_rxlevmin" value=""/></td>
											<td><input type="text" id="inter_rat_rxlevmin" value="" ></td>
											
										</tr>
										<tr>
											<td>MCC</td>
											<td><input type=text id="intra_ppf_mcc" value=""/></td>
											<td><input type=text id="of_ppf_mcc" value=""/></td>
											<td></td>
										</tr>
										<tr>
											<td>LAC</td>
											<td><input type=text id="intra_ppf_lac" value=""/></td>
											<td><input type=text id="of_ppf_lac" value=""/></td>
											<td><input type="text" id="inter_rat_lac" value="" ></td>
										</tr>
										<tr>
											<td>MNC</td>
											<td><input type=text id="intra_ppf_mnc" value=""/></td>
											<td><input type=text id="of_ppf_mnc" value=""/></td>
											<td></td>
										</tr>
										<tr>
											<td>CELL_ID</td>
											<td><input type=text id="intra_ppf_cell" value=""/></td>
											<td><input type=text id="of_ppf_cell" value=""/></td>
											<td><input type="text" id="inter_rat_cellid" value="" ></td>
										</tr>
										<tr>
											<td>Q_OFFSET1S_N</td>
											<td></td>
											<td></td>
											<td>
												<input type="text" id="inter_rat_offset1s" value="" >
											</td>
										</tr>
										<tr>
										
											<td>CELL INDIVIDUAL OFFSET</td>
											<td></td>
											<td></td>
											<td>
												<input type="text" id="inter_rat_celloffset" value="" >
											</td>
										</tr>
										<tr>
											
											<td>NCC</td>
											<td></td>
											<td></td>
											<td>
												<input type="text" id="inter_rat_ncc" value="" >
											</td>
										</tr>
										<tr>
											<td>BCC</td>
											<td></td>
											<td></td>
											<td>
												<input type="text" id="inter_rat_bcc" value="" >
											</td>
										</tr>
										<tr>
											<td>FREQ_BAND</td>
											<td></td>
											<td></td>
											<td><input type="text" id="inter_rat_band" value="" ></td>
										</tr>
										<tr>
											<td>BCCH_ARFCN</td>
											<td></td>
											<td></td>
											<td><input type="text" id="inter_rat_bcch" value="" ></td>
										</tr>
						
						
								</table> -->
				
												<table style="width:100%;text-align:center">
									<tr><td><button id="upateConfig" onclick="setConfig()">Update</button><button onclick="getConfig()">Get Config</button><button onclick="mesTrigger(1)">MES Start</button><button onclick="mesTrigger(0)">MES Stop</button><button onclick="setCellLock(false);">Lock</button><button onclick="setCellUnLock();">UnLock</button></td></tr>
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
	
<script type="text/javascript" src="../../resources/js/3g/3g_configuration.js"></script>
<script type="text/javascript" src="../../resources/js/3g/configurationStructure.js"></script>
<style>
.intera_text
{
	width:50px;
}
.inter_text
{
	width:50px;
}
.inter_rat_text
{
	width:50px;
}
</style>
</body>
</html>