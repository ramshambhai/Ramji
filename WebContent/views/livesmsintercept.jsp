<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
<script>
<% String ip= "'"+request.getServerName()+"'"; %>
<script>	
	var msisdn  = <%= request.getParameter("msisdn").equalsIgnoreCase("null")?"":"'"+request.getParameter("msisdn")+"'"%>
</script>
var ipAddress =<%= ip %>;
console.log(ipAddress)
var ws = new WebSocket("ws://"+ipAddress+":8080/locator/smsintercept");
var color="#FFF";
ws.onopen = function()
{
	console.log("connected to the server");
	//ws.send("Hi i am sunil");
}
ws.onmessage = function(event)
{
	var msg = event.data.split("$");
	if(data[0] == msisdn)
	{
		createTable(msg);	
	}
	
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
		
			html += "<td>"+data[0]+"</td>"
			html += "<td>"+data[1]+"</td>"
			html += "<td>"+data[2]+"</td>"
			html += "<td>"+data[3]+"</td>"
			html += "<td>"+data[4]+"</td>"
			html += "<td>"+data[5]+"</td>"
			html += "<td>"+data[6]+"</td>"
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
				<th>IP</th>				
				<th>Count</th>
				<th>Calling Party</th>
				<th>SMSC Num</th>
				<th>SMSC Timestamp</th>
				<th>System Timestamp</th>
				<th>SMS MT data</th>				
			</thead>
			<tbody id="packet_body">				
			</tbody>
		</table>
	</div>		
</div>
</body>
</html>