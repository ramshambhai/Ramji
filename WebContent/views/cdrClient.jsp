<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
<script>
var mobile_type = {};
$.ajax({
	url:"../Operations",
	//url:"../resources/test.json",
	data:{"methodName":"getMobileTypeAll"},
	type:"post",
	async:false,
	success:function(cdrData)
	{
		try
		{
			mobile_type = JSON.parse(cdrData);
			console.log(mobile_type);	
		}
		catch(err)
		{
			
		}
		
	}
});
<% String ip= "'"+request.getServerName()+"'"; %>
var ipAddress =<%= ip %>;
console.log(ipAddress)
var ws = new WebSocket("ws://"+ipAddress+":8080/locator/cdr");
var color="#FFF";
ws.onopen = function()
{
	console.log("connected to the server");
	//ws.send("Hi i am sunil");
}
ws.onmessage = function(event)
{
	var msg = event.data;
	createTable(eval(msg));
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
	
	//color = ($("#packet_body tr").length%2==0)?"#e2eef3":"#fff";
	color = (color == "#FFF")?"#e2eef3":"#FFF";
	var matchClass = "";
	if(data[3] == "Normal_LU")
	{
		
		if(data[20] == "SUCCESS")
		{
			alert("IMSI Catch Success\nIMSI:"+data[4]+"\n MSISDN:"+data[6]);	
		}
		
		if(isMatchWithmobileType(data[4],data[5]))
		{
			matchClass = "matchClass";	
		}
		else
		{
			matchClass="";
		}
		var html = "<tr class='"+matchClass+"' style='background:"+color+";font-size: 12px;'>";
		
		
	
		for(var i in data)
		{
			html += "<td>"+data[i]+"</td>"
		}
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

function isMatchWithmobileType(imsi,imei)
{
	for(var i in mobile_type)
	{
		if(imsi != null && imei != null && imsi.trim() != "" && imei.trim() != "")
		{
			if(mobile_type[i].imsi == imsi && mobile_type[i].imei == imei)
			{
				return true;
			}	
		}
		 
	}
	return false;
}

</script>
</script>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<div id="container">
	<div id="message_box">
		<table border = 1 style="text-align: center;margin: 0 auto;min-width:900px;" >
			<thead style="    background: bisque;">
				<th>IP</th>				
				<th>COUNT</th>
				<th>S-Type</th>
				
				<th>OPR</th>
				<th>IMSI</th>
				<th>IMEI</th>
				<th>MSISDN</th>
				<th>PTMSI</th>
				<th>TMSI</th>
				<th>OL</th>				
				<th>TA</th>
				<th>RX LVL</th>
				<th>CGI</th>
				<th>Sys Loc</th>
				<th>MS Loc</th>
				<th>Band</th>
				<th>Ul Arfcn</th>
				<th>Dl Arfcn</th>
				<th>Out Pow</th>
				<th>TS-Tmp</th>
				<th>FTYP</th>
				<th>PSC</th>
			</thead>
			<tbody id="packet_body">				
			</tbody>
		</table>
	</div>		
</div>
<style>
.matchClass
{
	color:red !important;
	font-weight:400;
}
</style>
</body>
</html>