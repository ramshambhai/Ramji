<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%><jsp:include page='geoheader.jsp' />
<script>var autoFlag = false;</script> 
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<script>
	 var idOfLocData = <%=request.getParameter("id").equalsIgnoreCase("null")?"":request.getParameter("id")%>
</script>
<div id="container">
	<div id="message_box" style="background: rgba(255, 255, 255, 0.5) url('../resources/images/back_map.jpg');background-size: cover;">
		<div id="topdiv" style="width:100%;height:100%;display:none;">
			<div id="req_box" style="width:500px;height:500px;margin:250px auto;display: table-cell;vertical-align: middle;">
								<table id="req_tab" style="margin:0 auto;">
									<tr>
										<td>
											<input type=radio id="type_check" class="type_check" name="type" value=2 checked /><label> : MSISDN</label>
											<input type=radio id="type_check" class="type_check" name="type" value=1 /><label> : IMSI</label>
											<input type="text" placeholder="MSISDN" id="type_value" class="input_type" name="type_value"/>
										</td>
									</tr>									
									<tr>
										<td>
											<label>Req Type</label>										
											<select class="input_type" id="req_type" name="req_type">
												<option value="2">TRACK_REQ</option>
											</select>
										</td>
									</tr>
									<tr>
										<td colspan=2><button class="btn-match" id="search" style="width:100%;">Get</button></td>
									</tr>
								</table>
				<div id="loadingBox" style="display:none; text-align:center;">
					<img src="../resources/images/loading_bar_animated.gif" />	
				</div>
			</div>
		</div>
		<div id="bottomdiv" style="display:block;">					
			
			<table id="bottomTable" style="width:80%;margin:0 auto;">
				<tr>
					<td>
						
						<table id="filter_table"><tr><td><button style="width:90%;margin:0 auto;" class="btn-match" id="rescan">Rescan</button></td><td><select id="map_type"><option value=1>Online Map</option><option value=2>Offline Map</option></select></td></tr></table>
						<table>							
							<tr><td></td><td></td></tr>
							<tr><td>MSISDN</td><td><input type="text" name="res_MSISDN" id="res_MSISDN" readonly/></td></tr>
							<tr><td>IMSI</td><td><input type="text" name="res_IMSI" id="res_IMSI" readonly/></td></tr>
							<tr><td>IMEI</td><td><input type="text" name="res_IMEI" id="res_IMEI" readonly/></td></tr>
							<tr><td>Home Operator /MCC</td><td><input type="text" name="res_HMCC" id="res_HMCC" readonly/></td></tr>
							<tr><td>Home Operator /MNC</td><td><input type="text" name="res_HMNC" id="res_HMNC" readonly/></td></tr>
							<tr><td>Host Country /MCC</td><td><input type="text" name="res_HOMCC" id="res_HOMCC" /></td></tr>
							<tr><td>Host Country /MNC</td><td><input type="text" name="HOMNC" id="HOMNC" readonly/></td></tr>
							<tr><td>LAC</td><td><input type="text" name="res_LAC" id="res_LAC" readonly/></td></tr>
							<tr><td>CELL</td><td><input type="text" name="res_CELL" id="res_CELL" readonly/></td></tr>
							<tr><td>Subscriber State</td><td><input type="text" name="res_s_state" id="res_s_state" readonly/></td></tr>
							<tr><td>Date</td><td><input type="text" name="res_date" id="res_date" readonly/></td></tr>
							<tr><td>Coordinate</td><td><input type="text" name="res_cordinate" id="res_cordinate" readonly/></td></tr>
							<tr><td>HLR address</td><td><input type="text" name="res_hlr" id="res_hlr" readonly/></td></tr>
							<tr><td>VLR address</td><td><input type="text" name="res_vlr" id="res_vlr" readonly/></td></tr>
							<tr><td>FTN</td><td><input type="text" name="res_ftn" id="res_ftn" /></td></tr>
							<tr><td></td><td></td></tr>
							<tr><td id="opr_buttons" colspan=2><button style="margin:0 auto;" class="btn-match" id="block_service">BLOCK SERVICES</button>&nbsp;<button class="btn-match" style="margin:0 auto;" id="diveret_service">CALL DIVERT</button>&nbsp;<button class="btn-match" style="margin:0 auto;" id="trace_intercept">CALL INTERCEPT</button>&nbsp;<button class="btn-match" style="margin:0 auto;" id="sms_intercept">SMS INTERCEPT</button></td></tr>
						</table>					
					</td>
					<td><div id="map_div"></div></td>
				</tr>
			</table>
		</div>			
	</div>	
</div> 

	<link rel="stylesheet" href="../resources/css/ol.css" />
 	<script type="text/javascript" src="../resources/js/ol.js"></script>
 	<script type="text/javascript" src="../resources/js/geoIndexOffline.js"></script>
 	<style>
 		#container
 		{
 			height:93%;
 		}
 		#req_tab
 		{
 			font-size: 16px;
		    font-weight: 500;
		    margin: 0 auto;
		    border: 5px solid #3a5795;
		    border-radius: 9px;
		    padding: 30px;
		    width: 453px;
		    background-color: rgba(255, 255, 255, 0.69);
 		}
 		#req_tab select
 		{
 			width:289px;
 		}
 		#req_tab td
 		{
 			padding:5px;
 		}
 		.input_type
 		{
 			width: 186px;
    		margin-left: 3px;
    		border: 1px solid #3a5795;
    		border-radius: 2px;
		}
		
		#bottomTable
		{
			width: 80%;
    margin: 0 auto;
    padding: 10px;
    background: white;
    border: 4px solid #3a5795;
    border-radius: 8px;
		}
		#bottomTable td
		{
			width:50%;
		}
		#map_div {
			    
			    width: 100%;
			    height: 466px;
			    border: 3px solid #3a5795;
			    border-radius: 5px;
			}
			
		#loadingBox
 		{
 			font-size: 16px;
		    font-weight: 500;
		    margin: 0 auto;
		    border: 5px solid #3a5795;
		    border-radius: 9px;
		    padding: 30px;
		    width: 453px;
		    background-color: rgba(255, 255, 255, 0.69);
 		}
 		#opr_buttons button{font-size:10px;}
 	</style>	
</body>
</html>