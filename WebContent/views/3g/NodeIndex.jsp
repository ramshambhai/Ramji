<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Locator</title>
<script type="text/javascript" src="../../resources/js/jquery-1.11.3.min.js" ></script>
<link rel="stylesheet" type="text/css" href="../../resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />

 <style>
  #add_sufi_tab{	
display:none;
 } 
 #inter_frq_list_tab input
 {
 	width:100px;
 }
 #left_box
 {
 	width:100% !important;
 }	
 .sib_table
 {
     margin: 0 0 0 0 !important;
     
 }
 .sib_table tbody 
 {
 	color:black !important;
 	overflow:auto;
 	display:block;
 	font-size:13px;
 }
 .card
 {
 	width: auto !important;
 	height:245px;
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
<script>
var dirPath ="../../";
function toUtcTime(dateTime) {
	   // var d = new Date("2017-05-15 14:39");
		 var d = new Date(dateTime);
	    var n = d.toUTCString().replace("GMT","");    
	    var dd = new Date(n);
	    var year = dd.getFullYear();
	   //alert(dd.getHours());
	    var month = (dd.getMonth().toString().length<=1)?"0"+(parseInt(dd.getMonth())+1):(parseInt(dd.getMonth())+1);
	    var date = (dd.getDate().toString().length<=1)?"0"+(parseInt(dd.getDate())):(parseInt(dd.getDate()));
	    var hour = (dd.getHours().toString().length<=1)?"0"+(parseInt(dd.getHours())):(parseInt(dd.getHours()));
	    var min = (dd.getMinutes().toString().length<=1)?"0"+(parseInt(dd.getMinutes())):(parseInt(dd.getMinutes()));
	    //console.log(dd.getDate());
	    return (year+"-"+month+"-"+date+" "+hour+":"+min);
	    
	}
var mapServerIp = null;
var runningModeGlobe = "null";
var getMapServerIp = function()
{
	var data={"methodName":"getMapServerIp" };
	$.ajax({
		url:"../Operations",
		data:data,
		type:'post',
		async:false,
		success:function(data)
		{
			mapServerIp = data;
		}
	});	
}
var getRunningMode = function()
{
	var data={"methodName":"getRunningMode","cmd":"GET_MONIT_MODE","cmd":"GET_MONIT_MODE","fileName":"nsFlagList.json" };
	$.ajax({
		url:"../Operations",
		data:data,
		type:'post',
		async:false,
		success:function(data)
		{
			console.log
			
			 try {
				 runningModeGlobe = runningMode = jQuery.parseJSON(data);
			    } catch (e) {
			        return false;
			    }
			
			var mode = parseInt(runningMode.IMEITRACEMODE) == 1?"Trace":"Exclusion";
			$("#runningMode").html("");
			$("#runningMode").text(mode);
		}
	});	
}

getMapServerIp();

var getNIBForHeader = function()
{
	var data={"methodName":"getNibIp" };
	$.ajax({
		url:"../Operations",
		data:data,
		type:'post',
		success:function(data)
		{
			//alert(data);
			$("#nib_ip_val_head").html("");
			$("#nib_ip_val_head").text(data);
		}
	});
}

$(document).ready(function()
{
	getNIBForHeader();
	getRunningMode();
});

</script>
</head>
<body>
<% String role = (String)session.getAttribute("role"); %>

<!--<div id="nav">
<table style="float:left">
	<tr>		
		<td style="width: 110px;"><a href="index.jsp">Home</a></td>
		<td class="dropdown" style="margin-top:14px;width: 100px;"><a class="dropbtn" href="NodeConfiguration.jsp">Configuration</a></td>
		<td>>></td>
		<td class="dropdown" style="margin-top:14px;width: 100px;"><a class="dropbtn" href="NodeOperation.jsp">Operations</a></td>
		
		<!-- <td style="width: 110px;"><a href="Offline3gAlarms.jsp">Alarms</a></td>
		<td style="width: 160px;"><a href="subredirectionevetn.jsp">Redirection Event</a></td>
		<td style="width: 160px;"><a href="Offlineholdmesevent.jsp">Hold And Meas Event</a></td>
		<td style="width: 160px;"><a href="dedicatedMeasEvent.jsp">Dedicated Meas Event</a></td>-->
		
		<!-- <td><a href="../../logout">Logout</a></td> -->
	<!--</tr>
</table>
</div>-->

<div id="container">
	<div id="message_box">
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
						<table border=1 id="list_table" style="font-size:13px;margin: 0 auto 10px 10px;" class="tab_com">
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
	<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
	<img src="../resources/images/loading_bar_animated.gif" />
	<br>
	<input type="hidden" id="MSISDN_VAL" value="" />
	<input type="button" value="Cancel" id="cancelSearch" onclick="cancelClick()">
	<input type="button" value="Back" id="backtodashboard" onclick="location.reload()">
</div>


		<div id="left_box">			
				<div class="card">
								<table  border=1 id="cell_info_sib_info" class="tab_com sib_table">
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
<script type="text/javascript" src="../../resources/js/consolidation.js" ></script>	
<script type="text/javascript" src="../../resources/js/3g/3g_NodeDashboard.js"></script>
<script type="text/javascript" src="../../resources/js/3g/3g_NodeConfiguration.js"></script>
<script type="text/javascript" src="../../resources/js/3g/NodeConfigurationStructure.js"></script>

</body>
</html>