<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='NodeHomeHeader.jsp' />
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
<script>
var deviceIp=window.parent.$('#leftTree .jstree-clicked').text().trim();
var deviceId=window.parent.$('#leftTree').jstree('get_selected').attr('id');
</script>
<div id="container">
	<div id="message_box">
		<div id="left_box">			
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
						<tr><td colspan=4><button onclick="sibUpdateOnClick()">Update</button></td></tr>
				
				</tbody>
								
								</table>
								
								</td>
								</tr>
								</table>
								</div>		

							
		</div>	
	</div>
			
</div>
	
<script type="text/javascript" src="../../resources/js/3g/3g_NodeConfiguration.js"></script>
<script type="text/javascript" src="../../resources/js/3g/NodeConfigurationStructure.js"></script>

</body>
</html>