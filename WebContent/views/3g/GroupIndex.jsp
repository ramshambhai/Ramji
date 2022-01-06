<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='GroupHeader.jsp' />
 <style>
  #add_sufi_tab{	
display:none;
 }
 
.operationButton {
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
 </style>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />
<div id="container">
	<div id="message_box">
		<!--	<div id="left_box">
					<div class="card">
					  <div class="container total_sufi">
					    <h4><b>TOTAL</b></h4> 
					    <p id="tot_bts">0</p> 
					  </div>
					</div>
					<div class="card">
					  <div class="container run_sufi">
					    <h4><b>Running</b></h4> 
					    <p id="run_bts">0</p> 
					  </div>
					</div>
					<div class="card">
					  <div class="container wait_sufi">
					    <h4><b>Waiting</b></h4> 
					    <p id="wait_bts">0</p> 
					  </div>
					</div>
					<div class="card">
					  <div class="container nor_sufi">
					    <h4><b>Not Reachable</b></h4> 
					    <p id="nor_bts">0</p> 
					  </div>
					</div>
					<div class="card">
					  <div class="container down_sufi">
					    <h4><b>Down</b></h4> 
					    <p id="down_bts">0</p> 
					  </div>
					</div>
										
				</div> -->
				<div id="left_box_section_2">
					<!--  <table border=1 id="add_sufi_tab" style="margin: 0 auto 10px 10px;width:100%;text-align:center;">-->
					<table border=1 id="add_sufi_tab" class="tab_com"style="">
						<thead ><th colspan=5>Add BTS</th></thead>
						<tr>
							<td colspan=2>IP : <input type='text' value='' id='btsIp' /></td>
							<td colspan=1>System Type : 
								<select id="btsDeviceType">
								</select>
							</td>
							<td colspan=1>Network Type : 
								<select id="btsType">
									<option value="3g">3G</option>
								</select>
							</td>
							<td><button class="btn-match" onclick="addBts()">Add</button></td>
						</tr>
					</table>
					<!--  <table border =1 style="font-size:13px;margin: 0 auto 10px 10px;width:100%;text-align:center;" id="list_table">-->
						<table border=1 id="list_table" style="font-size:13px;margin: 0 auto 10px -100px;" class="tab_com">
						<thead>
							<th>IP</th>
							<th>STATUS</th>
							<th>CELL STATUS</th>
							<th>TYPE</th>
							<th>S-Type</th>
							<th></th>
							<th></th>
						<thead>
						<tbody id="list_table_body">
							
						</tbody>
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
<script type="text/javascript" src="../../resources/js/consolidation.js" ></script>	
<script type="text/javascript" src="../../resources/js/3g/3g_GroupDashboard.js"></script>

</body>
</html>