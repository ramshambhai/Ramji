<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='nodeheader.jsp' />
 
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="container">
	<div id="message_box">
	<table style="text-align: center;margin: 0 auto;" >
		<tr>
			<td>
				<table id="imsi-imei-tabl" border =1 style="width:100%;text-align: center;margin: 0 auto;">
					<thead style="background: navajowhite;">
						<th>IMSI</th>
						<th>IMEI</th>
						<th>ACTIONS</th>
					<thead>
					<tbody id="imsi-imei-tabl-body">
						<tr>
							<td><input type="text" name="imsi" id="add_imsi" placehoder="IMSI"/></td>
							<td><input type="text" name="imei" id="add_imei" placehoder="IMEI"/></td>
							<td><button class="btn-match" onclick="addImsiImei()">Add</button><button class="btn-match" onclick="resetAll()">Reset All</button></td>
						</tr>
					</tbody>
				</table>	
			</td>
		</tr>
		<tr><td></td></tr>
		<tr>
			<td>
					<table border =1 style="text-align: center;margin: 0 auto;" id="list_table">
			<thead style="background: navajowhite;">
				<th>IMSI</th>
				<th>MSISDN</th>
				<th>IMEI</th>
				<th>TA</th>
				<th>RX LVL(dbm)</th>
				<th>RREVENT</th>
				<th>TIMESTAMP</th>
				<th>STATUS</th>
				<th>ACTIONS</th>
			<thead>
			<tbody id="list_table_body">
				
			</tbody>
		</table>
			</td>
		</tr>
	</table>
		
	</div>
	<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
	<img src="../resources/images/loading_bar_animated.gif" />
	<br>
	<input type="hidden" id="MSISDN_VAL" value="" />
	<input type="button" value="Cancel" id="cancelSearch" onclick="cancelClick()">
	<input type="button" value="Back" id="backtodashboard" onclick="location.reload()">
</div>
			
</div>
	
<script type="text/javascript" src="../resources/js/dashboard.js"></script>
</body>
</html>