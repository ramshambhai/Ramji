<html lang="en">
<head>
	<meta charset="utf-8" />	
	<link rel="apple-touch-icon" sizes="76x76" href="../../resources/lib/assets/img/apple-icon.png">
	<link rel="icon" type="image/png" sizes="96x96" href="../../resources/lib/assets/img/tracker.png">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

	<title>Falcon</title>

	<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />


    <!-- Bootstrap core CSS     -->
    <link  rel="stylesheet" href="../../resources/lib/assets/css/bootstrap.min.css" />
	<link rel="stylesheet" href="../../resources/lib/assets/css/fontawesome-all.min.css" />
	<link rel="stylesheet" href="../../resources/lib/assets/css/bootstrap-colorpicker.min.css" />

    <!-- Animation library for notifications   -->
    <link href="../../resources/lib/assets/css/animate.min.css" rel="stylesheet"/>

    <!--  Paper Dashboard core CSS    -->
    <link href="../../resources/lib/assets/css/paper-dashboard.css" rel="stylesheet"/>


    <!--  CSS for Demo Purpose, don't include it in your project     -->
    <link href="../../resources/lib/assets/css/demo.css" rel="stylesheet" />


    <!--  Fonts and icons     -->
    <!--<link href="http://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" rel="stylesheet">
    <link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>-->
    <link href="../../resources/lib/assets/css/themify-icons.css" rel="stylesheet">
	
	 <link href="../../resources/css/main.css" rel="stylesheet">
	 
	    <!--   Core JS Files   -->
    <script src="../../resources/js/jquery-1.11.3.min.js" type="text/javascript"></script>
	<script src="../../resources/lib/assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../../resources/lib/assets/js/bootstrap-colorpicker.min.js"></script>

	<!--  Checkbox, Radio & Switch Plugins -->
	<!-- <script src="../../resources/lib/assets/js/bootstrap-checkbox-radio.js"></script> -->

	<!--  Charts Plugin -->
	<script src="../../resources/lib/assets/js/chartist.min.js"></script>

    <!--  Notifications Plugin    -->
    <script src="../../resources/lib/assets/js/bootstrap-notify.js"></script>

    <!--  Google Maps Plugin    -->
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key="></script>

    <!-- Paper Dashboard Core javascript and methods for Demo purpose -->
	<script src="../../resources/lib/assets/js/paper-dashboard.js"></script>

	<!-- Paper Dashboard DEMO methods, don't include it in your project! -->
	<script src="../../resources/lib/assets/js/demo.js"></script>
	
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
	var getMapServerIp = function()
	{
	var data={"methodName":"getMapServerIp"};
	$.ajax({
		url:"../../Operations",
		data:data,
		type:'post',
		async:false,
		success:function(data)
		{
			mapServerIp = data;
		}
	});	
	}
	
	var systemTypeCode="";
	var systemTypeValue="";
	var getSystemType = function()
	{
	$.ajax({
		url:"../../service/common/getsystemtype",
		type:'post',
		async:false,
		success:function(data)
		{
			systemTypeCode = data[0].code;
			systemTypeValue = data[0].value;
		},
		error:function(err){
			alert(err);
		}
	});	
	}
	
	var systemMode="";
	var getSystemMode = function()
	{
	$.ajax({
		url:"../../service/common/getsystemmode",
		type:'post',
		async:false,
		success:function(data)
		{
			systemMode = data[0].code;
		},
		error:function(err){
			alert(err);
		}
	});	
	}
	
	var octasicPowerStatus=true;
	
	var getOctasicPowerStatus = function()
	{
	$.ajax({
		url:"../../service/common/getoctasicpowerstatus",
		type:'post',
		async:false,
		success:function(data)
		{
			octasicPowerStatus=data.octasicPowerStatus;
		},
		error:function(err){
			alert(err);
		}
	});	
	}
	
	var sessionParams={};
	var getSessionParams = function()
	{
		$.ajax({
			url:"../../service/common/getsessionparams",
			type:'post',
			async:false,
			success:function(data)
			{
				sessionParams = data;
			}
		});	
	}
	
	var thresholdUsedSpace=-1;
	
	var getThresholdUsedSpace = function()
	{
		$.ajax({
			url:"../../service/common/getthresholdusedspace",
			type:'post',
			async:false,
			success:function(data)
			{
				thresholdUsedSpace = data.usedspacelimit;
			}
		});	
	}
	<% String ipVal= "'"+request.getServerName()+"'"; %>
	var ipAdd =<%= ipVal %>;
	
	var wsAlarmData = null;

	var captureAlarmDataEvent = function()
	{
		wsAlarmData = new WebSocket("ws://"+ipAdd+":8080/locator/alarmdata");
		var color="#FFF";
		wsAlarmData.onopen = function()
		{
			console.log("connected to the Alarm Data server");
		}
		wsAlarmData.onmessage = function(event)
		{
				//loadEvents();
				var data = JSON.parse(event.data);
				var eventType  = data.event_type;
				if(eventType=="TEMP_THRESHOLD_EXCEEDED"){
					if(data.status=='u'){
						$('#eventMsgSpan').text("OCTASIC TEMPERATURE THRESHOLD EXCEEDED");
						//$('#eventMsgDiv').addClass('blink_me').removeClass('hide_me');
						$('#eventMsgDiv').addClass('blink_me');
						//$('#eventMsgDiv').css('display','block');
						$("#eventMsgDiv").css("cssText", "background-color: white;color: red;display: block !important;");
						
					}else{
						$('#eventMsgSpan').text("");
						//$('#eventMsgDiv').removeClass('blink_me').addClass('hide_me');
						$('#eventMsgDiv').removeClass('blink_me');
						//$('#eventMsgDiv').css('display','none');
						$("#eventMsgDiv").css("cssText", "background-color: white;color: red;display: none !important;");
					}
				}
				//console.log(msg);
				/* if(eventMsg.msg.toLowerCase().indexOf("processing")!=-1){
					$('#operation_events_table_tbody').html('');
				} */			
				/*if(jsonMsg.msg=='started'){
					window.location.href='maps.jsp';
				}*/
				//getNodesInfo();
			
			
			
		}
		wsAlarmData.onclose = function()
		{
			/*var r = confirm("Disconected From Server.Press ok to reconect");
			if (r == true) {
			    location.reload();
			} else {
			    
			}*/
		}
	}
	
	var changePassword =function(type)
	{
	var changedPassword =  $("#newPassword").val();
	var confirmPassword = $("#confirmPassword").val();
	var match = false;
	var oldPassword = $("#oldPassword").val();
	var userId = "-1";
	var userName="-1";
    if(oldPassword=="" || changedPassword=="" || confirmPassword=="")
    {
       alert("Password cannot be blank");
    }
    else if(oldPassword == changedPassword && type!='admin')
	{
		alert("New password is same as Old password!!!");
	}
	else if(changedPassword == confirmPassword)
	{
		match = true;
	}
	else
	{
		alert("Confirm New Password mismatch");
	}
	if(match==true)
	{
		$.ajax
				({
					url:"../../service/common/changepassword",
					async:true,
					type: "post",
					data:
					{
						"type":type,
						"userId":userId,
						"userName":userName,
						"oldPassword":oldPassword,
						"changedPassword":changedPassword,
						"confirmPassword":confirmPassword
					},
					success:function(data)
					{  
                                                if(data.result=="success")
                                                {
													alert("Password Changed Successfully");
												}else{
													alert(data.message);
												}
					},
					error:function()
						{
							alert("Change password failed", function() {	});
						}
				});
	}
}

var displayChangePasswordModal = function(){
    $('#changePasswordModal').modal('show');
}

	getSessionParams();
	getMapServerIp();
	getSystemType();
	getSystemMode();
	getOctasicPowerStatus();
	getThresholdUsedSpace();
	$(document).ready(function(){
		captureAlarmDataEvent();
		if(!octasicPowerStatus){
			$('#eventMsgSpan').text("OCTASIC TEMPERATURE THRESHOLD EXCEEDED");
			//$('#eventMsgDiv').addClass('blink_me').removeClass('hide_me');
			$('#eventMsgDiv').addClass('blink_me');
			//$('#eventMsgDiv').css('display','block');
			$("#eventMsgDiv").css("cssText", "background-color: white;color: red;display: block !important;");
		}
	});
	</script>
<style>
.btn {
    display: inline-block;
    padding: 6px 12px;
    margin-bottom: 0;
    font-size: 12px;
    font-weight: 400;
    line-height: 1.42857143;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    -ms-touch-action: manipulation;
    touch-action: manipulation;
    cursor: pointer;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    background-image: none;
    border: 1px solid transparent;
    border-radius: 4px;
}

.btn-default {
    color: white;
    background-color: #337ab7;
    border-color: #ccc;
}

@media (min-width: 992px){
.navbar {
    min-height: 43px;

}
}

nav.navbar.navbar-initial {
    height: 50px;
}

.navbar .navbar-brand {
    font-weight: 600;
    margin: 5px 0px;
    padding: 8px 15px;
    font-size: 18px;
}

.navbar .navbar-nav > li > a {
    line-height: 1.42857;
    margin: 5px 0px;
    padding: 0px 15px;
}

.navbar-header{
height: 50px;
}

@media (min-width: 768px){
.navbar-collapse.collapse{
    height: 40px !important;
    display: inline-block;
    float: right;
}
}

.nav .navbar-right{
    margin-right: 10px;
}

@media (min-width: 992px){
.navbar .navbar-header {
    margin-left: 32px;
}
}

@media (min-width: 768px){
.navbar-right {
    margin-right: 2px;
}
}

.navbar .btn{
	margin: 0px 3px;
    font-size: 14px;
}

.nav .navbar-mid{
    margin-right: 10px;
}

</style>

</head>
<body>