<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
<script>
<% String ip= "'"+request.getServerName()+"'"; %>
var ipAddress =<%= ip %>;
console.log(ipAddress)
var ws = new WebSocket("ws://"+ipAddress+":8080/locator/alarms");
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
	if(true)
	{		
		
		var html = "<tr style='background:"+color+";font-size: 12px;'>";
		
		html += "<td>"+data.ORIGIN +"</td>"
		html += "<td>"+data.SUFI_ID+"</td>"
		html += "<td>"+data.ALARM_TYPE+"</td>"
		html += "<td>"+data.CAUSE+"</td>"
		html += "<td>"+data.DESCRIPTION+"</td>"
		html += "<td>"+data.TIME_STAMP+"</td>"
		
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
				<th>Origin</th>				
				<th>Sufi-Id</th>
				<th>Alarm-Type</th>
				<th>Cause</th>
				<th>Description</th>
				<th>Time Stamp</th>				
			</thead>
			<tbody id="packet_body">				
			</tbody>
		</table>
	</div>		
</div>
</body>
</html>