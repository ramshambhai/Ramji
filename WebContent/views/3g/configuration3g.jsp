<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='GroupHomeHeader.jsp' />
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
 	overflow:auto;
 	font-size:13px;
 }
 
 .block {
    		box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
    		transition: 0.3s;
    		margin: 0 auto 10px 125px;
			width: 800px;
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
    margin: 4px 2px;
    cursor: pointer;
	margin-left: 148px;
    margin-top: 338px;
    width: 309px;
    height: 48px;
}


 </style>
<link rel="stylesheet" type="text/css" href="../../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />
<script>	
</script>
<div id="container">
	<div id="message_box">
		<div id="left_box">
			<div class="block">
					  
						<table border=1 class="tab_com" id="add_sufi_tab">
				<thead>
				<th colspan="4">SuFi System Configuration</th>
				</thead>
				<thead>
					<th>Param</th>
					<th>OF</th>
					<th>PPF</th>
					<th>SPF</th>
				</thead>
				<tbody>
					<tr>
						<td><label>LOG LEVEL</label></td>
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
						<td><label>HOLD TIMER(&mu;s)</label></td>
						<td><input type="number" value="" id="holdTimerOF"></input></td>
						<td><input type="number" value="" id="holdTimerPPF"></input></td>
						<td><input type="number" value="" id="holdTimerSPF"></input></td>
					</tr>
					<tr>
						<td><label>REDIRECTION TIMER(&mu;s)</label></td>
						<td><input type="number" value="" id="redirectionTimerOF"></input></td>
						<td><input type="number" value="" id="redirectionTimerPPF"></input></td>
						<td><input type="number" value="" id="redirectionTimerSPF"></input></td>
					</tr>
					<tr>
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
					<tr>
						<td><label>TOTAL TX POWER</label></td>
						<td><input type="number" value="" id="txPowerOF"></input></td>
						<td><input type="number" value="" id="txPowerPPF"></input></td>
						<td><input type="number" value="" id="txPowerSPF"></input></td>
					</tr>
					<tr>
						<td><label>DPCCH POWER OFF</label></td>
						<td><input type="text" value="" id="dpcchPowerOF" class="dpcchPowerClass"></input></td>
						<td><input type="text" value="" id="dpcchPowerPPF" class="dpcchPowerClass"></input></td>
						<td><input type="text" value="" id="dpcchPowerSPF" class="dpcchPowerClass"></input></td>
					</tr>
					<tr>
						<td><label>DPCH FRAME OFF</label></td>
						<td><input type="text" value="" id="dpchFrameOF" class="dpchFrameClass"></input></td>
						<td><input type="text" value="" id="dpchFramePPF" class="dpchFrameClass"></input></td>
						<td><input type="text" value="" id="dpchFrameSPF" class="dpchFrameClass"></input></td>
					</tr>
				    <tr>
						<td><label>CELL SELECT QUAL MEASURE</label></td>
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
								</select></td>
					</tr>
					<tr>
						<td><label>MAX REPORT CELL ON RACH</label></td>
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
					</tr>
					<tr>
						<td><label>TRACK_MODE</label></td>
						<td colspan="3"><select id="trackMode" style="width: 507px;">
									<option value="0">0</option>
									<option value="1">1</option>
								</select></td>
					</tr>
					<tr>
						<td><label>GSM CELL BAND</label></td>
						<td colspan="3"><input type="text" value="" id="band2G" style="width: 507px;"></input></td>
					</tr>
					<tr>
						<td><label>ARFCN</label></td>
						<td colspan="3"><input type="text" value="" id="arfcn2G" style="width: 507px;"></input></td>
					</tr>
					</tbody>
				</table>
				</div>	
<div class="block">				
<div id="scannedListDiv"><button id="scannedListButton" onclick="getScannedTable()">Select Operator from Scanned List</button></div>
<table border="1" class="tab_com" id="add_sufi_cell_config">
				<thead>
				<th colspan="4">SuFi Cell Configuration</th>
				</thead>
				<thead>
					<tr>
					<th>Param</th>
					<th>OF</th>
					<th>PPF</th>
					<th>SPF</th>
				</tr>
				</thead>
					<tbody>
					<tr>
						<td><label>MCC ()</label></td>
						<td><span id="mccOF"></span></td>
						<td><span id="mccPPF"></span></td>
						<td><span id="mccSPF"></span></td>
					</tr>
					<tr>
						<td><label>MNC ()</label></td>
						<td><span id="mncOF"></span></td>
						<td><span id="mncPPF"></span></td>
						<td><span id="mncSPF"></span></td>
					</tr>
					<tr>
						<td><label>DL UARFCN ()</label></td>
						<td><span id="dlUarfcnOF"></span></td>
						<td><input type="text" value="" id="dlUarfcnPPF"></td>
						<td><input type="text" value="" id="dlUarfcnSPF"></td>
					</tr>
					<tr>
						<td><label>PSC ()</label></td>
						<td><span id="pscOF"></span></td>
						<td><input type="text" value="" id="pscPPF" min="0" max="511" class="pscClass"></td>
						<td><input type="text" value="" id="pscSPF" min="0" max="511" class="pscClass"></td>
					</tr>
					<tr>
						<td><label>LAC POOL START ()</label></td>
						<td><input type="text" value="" id="lacPoolStartOF" class="lacPoolStartClass"></td>
						<td><input type="text" value="" id="lacPoolStartPPF" class="lacPoolStartClass"></td>
						<td><input type="text" value="" id="lacPoolStartSPF" class="lacPoolStartClass"></td>
					</tr>
					<tr>
						<td><label>LAC POOL END ()</label></td>
						<td><input type="text" value="" id="lacPoolEndOF" class="lacPoolEndClass"></td>
						<td><input type="text" value="" id="lacPoolEndPPF" class="lacPoolEndClass"></td>
						<td><input type="text" value="" id="lacPoolEndSPF" class="lacPoolEndClass"></td>
					</tr>
					<tr>
						<td><label>CELL ID ()</label></td>
						<td><input type="text" value="" id="cellIdOF" class="cellIdClass"></td>
						<td><input type="text" value="" id="cellIdPPF" class="cellIdClass"></td>
						<td><input type="text" value="" id="cellIdSPF" class="cellIdClass"></td>
					</tr>
				</tbody>
				</table>
</div>			
					  
			</div>
							
		</div>	
	</div>
<button id="configButton" onclick="setConfiguration()">Set Configuraton</button>
<button onclick="setConfigValues()" style="width: 309px;">Get Configuration</button>
  
	   <div data-role="popup" id="myPopup">
		<div id="topdiv">
			<fieldset id="target_scan">
				<legend>Time Filter</legend>
				<table style="float:left;">
					<tr>
						<td>Select IP</td>
						<td><select id="netscanIp" style="width: 160px;"></select></td>
						<td>Start Time</td>
						<td><input class="dateSelect" type="text" id="startTime" style="width: 160px;"></td>
						<td>End Time</td>
						<td><input class="dateSelect"  type="text" id="endTime" style="width: 160px;"></td>
						<td colspan=2><input type="button" id="get_cdr_data" value="Get"></td>					
					</tr>				
				</table>			
			</fieldset>
			<fieldset id="sub_det" style='max-width:1348px;'>
				<legend>....</legend>
				<table id="data_tab" style='max-width:1348px;'></table>
    			<div id="pager"></div>	
			</fieldset>
		</div>
		
		<div id="bottomdiv" style="display:block;">
			
			<div id="data_tab"></div>		
			
		</div>			
	    </div> 
<script type="text/javascript" src="../../resources/js/3g/3g_GroupConfiguration.js"></script>
<script type="text/javascript" src="../../resources/js/3g/GroupConfigurationStructure.js"></script>
<script type="text/javascript" src="../../resources/js/netscan/umtsreport.js"></script>
<script>

$(function() {
    $( ".dateSelect" ).datetimepicker({
    	dateFormat: "yy-mm-dd",
    	timeFormat: 'HH:mm'
    });
  });
</script>
<script type="text/javascript"> 

   </script>
   <style>
   .ui-datepicker
   {
   	z-index:999 !important;
   }
   <%= session.getAttribute("themeCss")%>
   </style>
   
</body>
</html>