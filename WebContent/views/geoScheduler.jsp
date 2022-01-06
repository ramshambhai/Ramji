<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<jsp:include page='geoheader.jsp' />
<script>var autoFlag = false;</script> 
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="container">
		<div id="topdiv" style="width: 100%;margin-bottom: -15px;">
			<div id="req_box" style="width:500px;height:175px;">
								<table id="req_tab" style="margin:0 auto;">
									<tr>
									<td colspan="4" style="text-align: center;">Schedule Subscriber</td>
									</tr>
									<tr>
										<td>
											<input type=radio id="type_check" class="type_check" name="type" value=2 checked /><label> : MSISDN (with CC)</label>
											<input type=radio id="type_check" class="type_check" name="type" value=1 /><label> : IMSI</label>
											<input type="text" placeholder="MSISDN" id="type_value" class="input_type" name="type_value"/>
										</td>
										<td>
											<label>Req Type</label>										
											<select class="input_type" id="req_type" name="req_type">
												<option value="2">TRACK_REQ</option>
											</select>
										</td>
										<td>
											<label>NIB</label>										
											<select class="input_type" id="nibIp" name="nibIp">
											</select>
										</td>
										<td>
										<label>Periodicity(In Mins)</label>
										<input type="text" id="schedulerPeriodicity" class="input_type" name="periodicity"/>
										</td>
									</tr>									
									<tr>
									<td colspan="4" style="text-align: center;width: 100%;"><button class="btn-match" id="search">Schedule</button></td>
									</tr>
								</table>
			</div>
		</div>	
<div id="bottom_div" style="width:100%;height:100%;display:table;">
<div id="req_box_bottom" style="height:360px;overflow:auto;">
<table id="req_tab_bottom" border="1" style="text-align: center;margin: 0 auto;min-width:1200px;">
			<thead style="background: bisque;">
				<tr><th>MSISDN/IMSI</th>				
				<th>Periodicity(In Mins)</th>
                <th>Status</th>
				<th>Operations</th>			
			</tr></thead>
			<tbody id="scheduledSubscribers">				
			</tbody>
		</table>
</table>
</div>
</div>		
	</div>

 	<script type="text/javascript" src="../resources/js/geoScheduler.js"></script>
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
		    padding: 15px;
		    width:1330px;
		    background-color: rgba(255, 255, 255, 0.69);
 		}
 		#req_tab select
 		{
 			width:150px;
 		}
 		#req_tab td
 		{
 			padding:5px;
 		}
 		.input_type
 		{
 			width: 116px;
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
 		#opr_buttons_2 button{font-size:10px;}
		
		 #req_box_bottom
 		{
 			font-size: 16px;
		    font-weight: 500;
		    border: 5px solid #3a5795;
		    border-radius: 9px;
		    padding: 25px;
		    width:1270px;
			height:400px;
		    background-color: rgba(255, 255, 255, 0.69);
 		}
		
		.operationButton{
		 background-color: #3a5795;
		color: white;
		border-radius: 5px;
		padding: 4px;
		margin-right: 3px;
		margin-left: 3px;
		border-color: #4365af;
		}
		body{
		overflow: hidden;
		}
 	</style>	
</body>
</html>
