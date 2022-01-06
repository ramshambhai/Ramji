<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
<script>
<% String ip= "'"+request.getServerName()+"'"; %>
var ipAddress =<%= ip %>;
console.log(ipAddress)
var ws = new WebSocket("ws://"+ipAddress+":8080/locator/mesevent");
var color="#FFF";
ws.onopen = function()
{
	console.log("connected to the server");
	//ws.send("Hi i am sunil");
}
ws.onmessage = function(event)
{
	var msg = event.data;
	createTable(msg);
	//console.log(msg);
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


function checkIfValueExist(data,index)
{
	if(data[index] === undefined)
	{
		return "";
	}
	else
	{
		return data[index];
	}
}

function createTable(data)
{
	console.log(data);
	var data = data.split(",");
	console.log(data);
	color = (color == "#FFF")?"#e2eef3":"#FFF";	
	if(data[3]=="SET_GEN_MEAS_EVENT")
	{	
			var html = "<tr style='background:"+color+";font-size: 12px;'>";
		
			html += "<td>"+checkIfValueExist(data,0) +"</td>";
			html += "<td>"+checkIfValueExist(data,1) +"</td>";
			html += "<td>"+checkIfValueExist(data,2)+"</td>";
			html += "<td>"+checkIfValueExist(data,4) +"</td>";
			html += "<td>"+checkIfValueExist(data,3)+"</td>";
			html += "<td>"+checkIfValueExist(data,5)+"</td>";
			html += "<td>"+checkIfValueExist(data,6)+"</td>";
			html += "<td>"+checkIfValueExist(data,7)+"</td>";
			html += "<td>"+checkIfValueExist(data,8)+"</td>";
			html += "<td>"+checkIfValueExist(data,9) +"</td>";
			html += "<td>"+checkIfValueExist(data,10)+"</td>";
			html += "<td>"+checkIfValueExist(data,11)+"</td>";
			html += "<td>"+checkIfValueExist(data,12)+"</td>";
			html += "<td>"+checkIfValueExist(data,13)+"</td>";
			html += "<td>"+checkIfValueExist(data,14)+"</td>";
			html += "<td>"+checkIfValueExist(data,15)+"</td>";
			html += "<td>"+checkIfValueExist(data,16)+"</td>";
			html += "<td>"+checkIfValueExist(data,17)+"</td>";
			html += "<td>"+checkIfValueExist(data,18)+"</td>";
		html += "</tr>";
	}
	else if(data[1] == 'SET_SUB_HOLD_EVENT')
	{
		var html = "<tr style='background:"+color+";font-size: 12px;'>";
		
		html += "<td>"+checkIfValueExist(data,0)+"</td>";
		html += "<td>"+checkIfValueExist(data,2)+"</td>";
		html += "<td>"+checkIfValueExist(data,3)+"</td>";
		html += "<td>"+checkIfValueExist(data,4)+"</td>";
		html += "<td>"+checkIfValueExist(data,1)+"</td>";
		html += "<td>"+checkIfValueExist(data,5)+"</td>";
		html += "<td>"+checkIfValueExist(data,6)+"</td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "</tr>";
		
	}
	if($("#packet_body tr").length > 20)
		$("#packet_body tr:last").remove();
	$("#packet_body").prepend(html);
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
				<th>SufiId</th>
				<th>IP</th>
				<th>Type</th>
				<th>SUB ID</th>				
				<th>Eevet Type</th>
				<th>C_ECNO</th>
				<th>C_RSCP</th>
				<th>M1_PSC</th>
				<th>M1_ECNO</th>
				<th>M2_PSC</th>
				<th>M2_ECNO</th>
				<th>M3_PSC</th>
				<th>M3_ECNO</th>
				<th>M4_PSC</th>
				<th>M4_ECNO</th>
				<th>M5_PSC</th>
				<th>M5_ECNO</th>
				<th>M6_PSC</th>
				<th>M6_ECNO</th>				
			</thead>
			<tbody id="packet_body">				
			</tbody>
		</table>
	</div>		
</div>
</body>
</html>