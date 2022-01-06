<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
<script>
<% String ip= "'"+request.getServerName()+"'"; %>
var ipAddress =<%= ip %>;
console.log(ipAddress)
var ws = new WebSocket("ws://"+ipAddress+":8080/locator/dedicatedmesevent");
var color="#FFF";
ws.onopen = function()
{
	console.log("connected to the server");
	//ws.send("Hi i am sunil");
}
ws.onmessage = function(event)
{
	var msg = event.data;
	createTable(eval('(' + msg + ')'));
	console.log(msg);
	//document.getElementById("test2").value = msg
}
ws.onclose = function()
{
	var r = confirm("Disconected From Server.Press ok to reconect");
	if (r == true) {
	    location.reload();
	} else {
	    
	}
}

function createTable(data)
{
	
	console.log(data);
	color = (color == "#FFF")?"#e2eef3":"#FFF";	
	
	
	
	var cmdCode = data.CMD_CODE;
	var params = data.PARAMS;
	
	var eventCode = data.eventCode;
	
	var interFreqMeas = params.INTER_FREQ_MEAS;
	var intraFreqMeas = params.INTRA_FREQ_MEAS;
	var interRatMeas = params.INTER_RAT_MEAS;
	var subId = params.SUB_ID;
	
	for(var i in interFreqMeas)
	{
		var html = "<tr style='background:"+color+";font-size: 12px;'>";
		
		html += "<td>"+eventCode +"</td>";
		html += "<td>"+cmdCode+"</td>";
		html += "<td>"+subId+"</td>";
		html += "<td>INTER_FREQ_MEAS</td>";
		html += "<td>"+interFreqMeas[i].PSC+"</td>";
		html += "<td>"+interFreqMeas[i].CELL_ID+"</td>";
		html += "<td>"+interFreqMeas[i].ECNO+"</td>";
		html += "<td>"+interFreqMeas[i].RSCP+"</td>";
		html += "<td>"+interFreqMeas[i].RSSI+"</td>";
		html += "<td>"+interFreqMeas[i].LAC+"</td>";
		html += "<td>"+interFreqMeas[i].PLMN_ID.MCC+"</td>";
		html += "<td>"+interFreqMeas[i].PLMN_ID.MNC+"</td>";
		html += "<td></td>";
		html += "</tr>";
		if($("#packet_body tr").length > 20)
			$("#packet_body tr:last").remove();
		$("#packet_body").prepend(html);
		
	}
	
	for(var i in intraFreqMeas)
	{
		var html = "<tr style='background:"+color+";font-size: 12px;'>";
		
		html += "<td>"+eventCode +"</td>";
		html += "<td>"+cmdCode+"</td>";
		html += "<td>"+subId+"</td>";
		html += "<td>INTRA_FREQ_MEAS</td>";
		html += "<td>"+intraFreqMeas[i].PSC+"</td>";
		html += "<td>"+intraFreqMeas[i].CELL_ID+"</td>";
		html += "<td>"+intraFreqMeas[i].ECNO+"</td>";
		html += "<td>"+intraFreqMeas[i].RSCP+"</td>";
		html += "<td>"+intraFreqMeas[i].RSSI+"</td>";
		html += "<td>"+intraFreqMeas[i].LAC+"</td>";
		html += "<td>"+intraFreqMeas[i].PLMN_ID.MCC+"</td>";
		html += "<td>"+intraFreqMeas[i].PLMN_ID.MNC+"</td>";
		html += "<td></td>";
		html += "</tr>";
		if($("#packet_body tr").length > 20)
			$("#packet_body tr:last").remove();
		$("#packet_body").prepend(html);
		
	}
	
	for(var i in interRatMeas)
	{
		var html = "<tr style='background:"+color+";font-size: 12px;'>";
		
		html += "<td>"+eventCode +"</td>";
		html += "<td>"+cmdCode+"</td>";
		html += "<td>"+subId+"</td>";
		html += "<td>INTER_RAT_MEAS</td>";
		html += "<td></td>";
		html += "<td>"+interRatMeas[i].CELL_ID+"</td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td>"+interRatMeas[i].RSSI+"</td>";
		html += "<td>"+interRatMeas[i].LAC+"</td>";
		html += "<td>"+interRatMeas[i].PLMN_ID.MCC+"</td>";
		html += "<td>"+interRatMeas[i].PLMN_ID.MNC+"</td>";
		html += "<td>"+interRatMeas[i].BCCH_ARFCN+"</td>";
		
		html += "</tr>";
		if($("#packet_body tr").length > 20)
			$("#packet_body tr:last").remove();
		$("#packet_body").prepend(html);
		
	}
	

	
}

function send()
{
	ws.send(document.getElementById("test").value);
}
</script>
</script>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="container">
	<div id="message_box">
		<table border = 1 style="text-align: center;margin: 0 auto;min-width:900px;" >
			<thead style="    background: bisque;">
				<th>Event Code</th>				
				<th>Event Type</th>
				<th>Sub Id</th>
				<th>Freq Type</th>
				<th>PSC</th>
				<th>CELL</th>
				<th>ECNO</th>
				<th>RSCP</th>
				<th>RSSI</th>
				<th>LAC</th>
				<th>MCC</th>
				<th>MNC</th>
				<th>BCCH_ARFCN</th>				
			</thead>
			<tbody id="packet_body">				
			</tbody>
		</table>
	</div>		
</div>
</body>
</html>