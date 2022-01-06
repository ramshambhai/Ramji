<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Locator</title>
<script type="text/javascript" src="../resources/js/jquery-1.11.3.min.js" ></script>
<link rel="stylesheet" type="text/css" href="../resources/css/style.css" />
<script>
function toUtcTime(dateTime) {
	   // var d = new Date("2017-05-15 14:39");
		 var d = new Date(dateTime);
	    var n = d.toUTCString().replace("GMT","");    
	    var dd = new Date(n);
	    var year = dd.getFullYear();
	   //alert(dd.getHours());
	    var month = ((dd.getMonth()+1).toString().length<=1)?"0"+(parseInt(dd.getMonth())+1):(parseInt(dd.getMonth())+1);
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
			
			 try {
				 data = jQuery.parseJSON(data);
				 runningModeGlobe = runningMode = data;
			    } catch (e) {
			        return false;
			    }
			
				if((data.STATUS.indexOf("ERR(")>-1) )
				{
					alert("Operation Fail "+data2.STATUS);
				}
				else
				{
					var mode = parseInt(runningMode.IMEITRACEMODE) == 1?"Trace":"Exclusion";
					$("#runningMode").html("");
					$("#runningMode").text(mode);
				}
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

<div id="nav">
<table style="float:left">
	<tr>		
		<!-- <td style="width: 110px;"><a href="index.jsp">MS Dashboard</a></td> -->
		<!-- <td class="dropdown" style="margin-top:14px;width: 80px;"><a class="dropbtn" href="#">PLMN</a>
			<div class="dropdown-content">
			    <a href="plmnmanagment.jsp">Managment</a>
			  </div>
		</td> -->
		
		<td class="dropdown" style="margin-top:14px;width: 80px;"><a href="plmnmanagment.jsp">CELL</a></td>
		
		<!--  <td style="width: 90px;"><a href="livepoll.jsp">Live Poll</a></td>-->
		
		<% if(role.equalsIgnoreCase("admin")) { 
		%>
		
		  <!--  <td class="dropdown" style="margin-top:14px;width: 80px;"><a class="dropbtn" href="#">Tracking</a>
		  	<div class="dropdown-content">
			   <a href="cdrClient.jsp">Live</a>
			   <a href="OfflineCDR.jsp">History</a>
			</div>
		</td>
		<td class="dropdown" style="margin-top:14px;width:80px;"><a class="dropbtn" href="#">WIDS</a>
		  	<div class="dropdown-content">
			   <a href="wsdiClient.jsp">Live</a>
			   <a href="OfflineWSDI.jsp">History</a>
			</div>
		</td>-->	  
		 <!--<td class="dropdown" style="margin-top:14px;width: 142px;">
		 <a class="dropbtn" href="#">Administration</a>
		  	<div class="dropdown-content">
			   <a href="nodeAdministration.jsp">Nib Config</a>
			</div>
		</td> -->
		 <!--  <td style="width:109px;"><a href="administration.jsp" >Administration</a></td>-->
		 
		<!--  <td style="width: 150px;"><a href="userOperations.jsp">User Management</a></td>
		<td style="width: 80px;"><a href="Logs.jsp">Report</a></td>-->
		<% } %>
		
		<!-- <td><a href="../logout">Logout</a></td> -->
		 <!-- <td class="dropdown" style="margin-top:14px;width: 142px;">
		 <a href="nodeOperation.jsp">Operations</a>
		</td> -->
	</tr>
</table>
<table style="float:right;display: inherit;"><tbody><tr><td style="padding: 13px 0 0 0;">Connected NIB-IP : </td><td style="padding: 13px 0 0 0;" id="nib_ip_val_head"></td><td style="padding: 13px 0 0 30px;">Subscriber Mode : </td><td style="padding: 13px 0 0 0;" id="runningMode"></td></tr></tbody></table>
</div>