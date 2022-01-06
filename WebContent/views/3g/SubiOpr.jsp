<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='GroupHomeHeader.jsp' />
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />
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
	 max-height: 510px;
     
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
 	width: auto;
 	height:auto;
	margin-left: 220px;
	max-height: 520px;
 }
 .secondCard
 {
 	width: auto;
 	height:auto;
	margin-left: 220px;
	max-height: 520px;
	font-family: arial;
    font-size: 15px;
	box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
    transition: 0.3s;
    float: left;
    margin: 0 auto 10px 10px;
    color: white;
 }
 </style>
<div id="container">
	<div id="message_box">
		<div id="left_box">
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
							<button onclick="updateSibInfoOnClick()">Update</button>
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
							    			<td><button onclick="updateSubHoldOnClick()">Update</button></td>
							    		</tr>
							    	</table>
							    </div>
							</div>
							
						</td>
					</tr>				
				</table>	
				</div>	
				
				
				<div class="card" style="width: 615px;">
				<table border=1 id="system_parameters" class="tab_com">
				<thead><th colspan=3>Track Mode Info</th></thead>
					
					<tr>
						<td colspan=1><label>Hold Track Info</label></td>
						<td colspan=2>
							<div class="input__hold_fields_wrap">
							    <div>
							    	<table>							    		
										<tr>
							    			<td><label>SUB_ID : </label><input type="text" id="track_id" name="track_id"></td>
											<td><label>TRACK_MODE : </label><select id="track_type_mode">
											<option value="0">0</option>
											<option value="1">1</option>
											</select></td>
							    			<td>
							    				<label>Type : </label>
							    				<select id = "track_sub_type" name=track_sub_type>
													<option value=0>IMEI</option>
													<option value=1>IMSI</option>
												</select>
											</td>
							    			<td><button onclick="updateTrackModeOnClick()">Update</button></td>
							    		</tr>
							    	</table>
							    </div>
							</div>
							
						</td>
					</tr>				
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
	
<script type="text/javascript" src="../../resources/js/3g/3g_GroupConfiguration.js"></script>
<script type="text/javascript" src="../../resources/js/3g/GroupConfigurationStructure.js"></script>

</body>
</html>