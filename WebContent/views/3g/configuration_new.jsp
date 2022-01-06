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
 	
 }
 .card
 {
 	width: auto !important;
 	height:210px;
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
								<option value=01>SYS_PARAMS</option>
								<option value=10>CELL_PARAMS</option>
								<option value=11>ALL</option>
							</select>
						</td>						
						<!--  <td><button class="btn-match" onclick="addBts()">Add</button></td>-->
					</tr>
					<tr>
						<td colspan=2><label>System Parameters</label></td>
						<td colspan=4>
							<select id="sys_para_btsmask">
								<option value=01>SUB_INFO</option>
								<option value=10>CELL_INFO</option>
								<option value=11>ALL</option>
							</select>
						</td>
					</tr>
					<tr>
							<td colspan=3>
								<label>Cell Parameters</label>
							</td>
							<td colspan=3>
								<select id="cell_para_btsmask">
									<option value=01>REDIRECTION_INFO</option>
									<option value=10>SIB_INFO</option>
									<option value=100>GENERAL</option>
									<option value=111>ALL</option>
								</select>
							</td>
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
							<button class="add_field_button" onclick="updateSibInfo()">Update</button>
						</td>
					</tr>
					<tr>
						<td colspan=1><label>Hold Subscriber Info</label></td>
						<td colspan=2>
							<div class="input__hold_fields_wrap">
							    <div>
							    	<table>							    		
										<tr>
							    			<td><label>SUB_ID : </label><input type="text" name="hold_sys_sub_id"></td>
							    			<td>
							    				<label>Type : </label>
							    				<select name=hold_sys_sub_type>
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
							<td colspan=3>MACRO_PSC</td>
							<td colspan=3><input type="text" id="macro_id" value="" readonly></td>
						</tr>
						<tr>
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
							<td colspan=3>LAC_POOL_START</td>
							<td colspan=3><input type="text" id="lac_pool_start" value="" ></td>
						</tr>
						<tr>
							<td colspan=3>LAC_POOL_END</td>
							<td colspan=3><input type="text" id="lac_pool_end" value="" ></td>
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
							<td colspan=3>CELL ID</td>
							<td colspan=3><input type="text" id="cell_id" value="" readonly></td>
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
							<td colspan=3><input type="text" id="dl_uarfcn" value="" readonly></td>
						</tr>
						<tr>
							<td colspan=3>UL_UARFCN</td>
							<td colspan=3><input type="text" id="ul_uarfcn" value="" ></td>
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
				</div>
				<div class="card">
				<table border=1 id="cell_redirection_info" class="tab_com">
					<thead>
						<th colspan=6>REDIRECTION INFO</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=3>SUFI OF REDIRECT FLAG</td>
							<td colspan=3>
								<input type="text" id="of_redirect_flag" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI OF RDL UARFCN</td>
							<td colspan=3>
								<input type="text" id="of_rdl_uarfcn" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI PPF REDIRECT FLAG</td>
							<td colspan=3>
								<input type="text" id="ppf_redirect_flag" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI PPF RDL UARFCN</td>
							<td colspan=3>
								<input type="text" id="ppf_rdl_uarfcn" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI SF REDIRECT FLAG</td>
							<td colspan=3>
								<input type="text" id="sf_redirect_flag" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>SUFI SF RDL UARFCN</td>
							<td colspan=3>
								<input type="text" id="sf_rdl_uarfcn" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>2G REDIRECT FLAG</td>
							<td colspan=3>
								<input type="text" id="2g_redirect_flag" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>2G FREQ BAND</td>
							<td colspan=3>
								<input type="text" id="2g_freq_band" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>2G BCCH ARFCN</td>
							<td colspan=3>
								<input type="text" id="2g_bcch_arfcn" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=6><button onclick="updateRedirectionInfo()">Update</button></td>
						</tr>
					</tbody>
				</table>
				</div>
				<div class="card">
				<table border=1 id="cell_info_of_inter_rat" class="tab_com">
					<thead>
						<th colspan=6>SUFI OF LIST INTER RAT(2G CELL)</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=3>RAT_Q_OFFSET1S_N</td>
							<td colspan=3>
								<input type="text" id="inter_rat_offset1s" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>Q_RXLEVMIN</td>
							<td colspan=3>
								<input type="text" id="inter_rat_rxlevmin" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>CELL INDIVIDUAL OFFSET</td>
							<td colspan=3>
								<input type="text" id="inter_rat_celloffset" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>NCC</td>
							<td colspan=3>
								<input type="text" id="inter_rat_ncc" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>BCC</td>
							<td colspan=3>
								<input type="text" id="inter_rat_bcc" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>FREQ_BAND</td>
							<td colspan=3>
								<input type="text" id="inter_rat_band" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>BCCH_ARFCN</td>
							<td colspan=3>
								<input type="text" id="inter_rat_bcch" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>CELL_ID</td>
							<td colspan=3>
								<input type="text" id="inter_rat_cellid" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=3>LAC</td>
							<td colspan=3>
								<input type="text" id="inter_rat_lac" value="" >
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<div class="card">
								<table  border=1 id="cell_info_sib_info" class="tab_com">
					<thead>
						<th colspan=6>SIB INFO</th>
					</thead>
					<tbody>
						<tr>
							<td colspan=3>CELL SELECTION QUALITY MEASURE</td>
							<td colspan=3>
								<input type="text" id="cell_selection_quality_measure" value="" >
							</td>
						</tr>
						<tr>
							<td colspan=6>
								<table id="inter_frq_list_tab" style="width:100%" border=1>
									<thead>
										<th></th>
										<th colspan=3>SUFI_OF_LIST</th>
										<th colspan=2>SUFI_PPF_LIST</th>
										<th colspan=2>SUFI_SPF_LIST</th>
									</thead>
									<tbody>
										<tr>
												<td></td>
												<td>INTRA_FREQ</td>
												<td colspan=2>INTER_FREQ</td>
												<td colspan=2>INTER_FREQ</td>
												<td colspan=2>INTER_FREQ</td>
										</tr>	
										<tr>
											<td></td>
											<td></td>
											<td>PPF</td>
											<td>SPF</td>
											<td>SPF</td>
											<td>OF</td>
											<td>PPF</td>
											<td>OF</td>
										</tr>
										<tr>
											<td>PSC</td>
											<td><input type=text id="intra_ppf_psc" value=""/></td>
											<td><input type=text id="of_ppf_psc" value=""/></td>
											<td><input type=text id="of_spf_psc" value=""/></td>
											<td><input type=text id="ppf_spf_psc" value=""/></td>
											<td><input type=text id="ppf_of_psc" value=""/></td>
											<td><input type=text id="spf_ppf_psc" value=""/></td>
											<td><input type=text id="spf_of_psc" value=""/></td>
										</tr>
										<tr>
											<td>UARFCN</td>
											<td></td>
											<td><input type=text id="of_ppf_uarfcn" value=""/></td>
											<td><input type=text id="of_spf_uarfcn" value=""/></td>
											<td><input type=text id="ppf_spf_uarfcn" value=""/></td>
											<td><input type=text id="ppf_of_uarfcn" value=""/></td>
											<td><input type=text id="spf_ppf_uarfcn" value=""/></td>
											<td><input type=text id="spf_of_uarfcn" value=""/></td>
										</tr>
										<tr>
											<td>PCPICH_TX_POWER</td>
											<td></td>
											<td><input type=text id="of_ppf_pcpich" value=""/></td>
											<td><input type=text id="of_spf_pcpich" value=""/></td>
											<td><input type=text id="ppf_spf_pcpich" value=""/></td>
											<td><input type=text id="ppf_of_pcpich" value=""/></td>
											<td><input type=text id="spf_ppf_pcpich" value=""/></td>
											<td><input type=text id="spf_of_pcpich" value=""/></td>
										</tr>
										<tr>
											<td>Q_OFFSET_1S</td>
											<td><input type=text id="intra_ppf_1s" value=""/></td>
											<td><input type=text id="of_ppf_1s" value=""/></td>
											<td><input type=text id="of_spf_1s" value=""/></td>
											<td><input type=text id="ppf_spf_1s" value=""/></td>
											<td><input type=text id="ppf_of_1s" value=""/></td>
											<td><input type=text id="spf_ppf_1s" value=""/></td>
											<td><input type=text id="spf_of_1s" value=""/></td>
										</tr>
										<tr>
											<td>Q_OFFSET_2S</td>
											<td><input type=text id="intra_ppf_2s" value=""/></td>
											<td><input type=text id="of_ppf_2s" value=""/></td>
											<td><input type=text id="of_spf_2s" value=""/></td>
											<td><input type=text id="ppf_spf_2s" value=""/></td>
											<td><input type=text id="ppf_of_2s" value=""/></td>
											<td><input type=text id="spf_ppf_2s" value=""/></td>
											<td><input type=text id="spf_of_2s" value=""/></td>
										</tr>
										<tr>
											<td>Q_QUALMIN</td>
											<td><input type=text id="intra_ppf_qualmin" value=""/></td>
											<td><input type=text id="of_ppf_qualmin" value=""/></td>
											<td><input type=text id="of_spf_qualmin" value=""/></td>
											<td><input type=text id="ppf_spf_qualmin" value=""/></td>
											<td><input type=text id="ppf_of_qualmin" value=""/></td>
											<td><input type=text id="spf_ppf_qualmin" value=""/></td>
											<td><input type=text id="spf_of_qualmin" value=""/></td>
										</tr>
										<tr>
											<td>Q_RXLEVMIN</td>
											<td><input type=text id="intra_ppf_rxlevmin" value=""/></td>
											<td><input type=text id="of_ppf_rxlevmin" value=""/></td>
											<td><input type=text id="of_spf_rxlevmin" value=""/></td>
											<td><input type=text id="ppf_spf_rxlevmin" value=""/></td>
											<td><input type=text id="ppf_of_rxlevmin" value=""/></td>
											<td><input type=text id="spf_ppf_rxlevmin" value=""/></td>
											<td><input type=text id="spf_of_rxlevmin" value=""/></td>
										</tr>
										<tr>
											<td>MCC</td>
											<td><input type=text id="intra_ppf_mcc" value=""/></td>
											<td><input type=text id="of_ppf_mcc" value=""/></td>
											<td><input type=text id="of_spf_mcc" value=""/></td>
											<td><input type=text id="ppf_spf_mcc" value=""/></td>
											<td><input type=text id="ppf_of_mcc" value=""/></td>
											<td><input type=text id="spf_ppf_mcc" value=""/></td>
											<td><input type=text id="spf_of_mcc" value=""/></td>
										</tr>
										<tr>
											<td>MNC</td>
											<td><input type=text id="intra_ppf_mnc" value=""/></td>
											<td><input type=text id="of_ppf_mnc" value=""/></td>
											<td><input type=text id="of_spf_mnc" value=""/></td>
											<td><input type=text id="ppf_spf_mnc" value=""/></td>
											<td><input type=text id="ppf_of_mnc" value=""/></td>
											<td><input type=text id="spf_ppf_mnc" value=""/></td>
											<td><input type=text id="spf_of_mnc" value=""/></td>
										</tr>
										<tr>
											<td>CELL_ID</td>
											<td><input type=text id="intra_ppf_cell" value=""/></td>
											<td><input type=text id="of_ppf_cell" value=""/></td>
											<td><input type=text id="of_spf_cell" value=""/></td>
											<td><input type=text id="ppf_spf_cell" value=""/></td>
											<td><input type=text id="ppf_of_cell" value=""/></td>
											<td><input type=text id="spf_ppf_cell" value=""/></td>
											<td><input type=text id="spf_of_cell" value=""/></td>
										</tr>
										<tr>
											<td>LAC</td>
											<td><input type=text id="intra_ppf_lac" value=""/></td>
											<td><input type=text id="of_ppf_lac" value=""/></td>
											<td><input type=text id="of_spf_lac" value=""/></td>
											<td><input type=text id="ppf_spf_lac" value=""/></td>
											<td><input type=text id="ppf_of_lac" value=""/></td>
											<td><input type=text id="spf_ppf_lac" value=""/></td>
											<td><input type=text id="spf_of_lac" value=""/></td>
										</tr>
									</tbody>
								
								
								</table>
								
								</td>
								</tr>
								</table>
								</div>
												<table style="width:100%;text-align:center">
									<tr><td><button id="upateConfig" onclick="setConfig()">Update</button><button onclick="getConfig()">Get Config</button><button onclick="mesTrigger(1)">MES Start</button><button onclick="mesTrigger(0)">MES Stop</button></td></tr>
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

</body>
</html>