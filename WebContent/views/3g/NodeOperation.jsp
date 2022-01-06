<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='NodeHomeHeader.jsp' />

 <style>

 
 #inter_frq_list_tab input
 {
 	width:100px;
 }
 #left_box
 {
 	width:100% !important;
 }	
 .tab_com
 {
     margin: 0 0 0 0 !important;
     
 }
 .tab_com tbody 
 {
 	color:black !important;
 	max-height:208px;
 	overflow:auto;
 	display:block;
 	font-size:13px;
 }
 .card
 {
 	width: auto !important;
 	height:245px;
 }
 
    button {
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
		<div id="left_box">

												<table style="width:100%;text-align:center;margin-left: 250px;">
									<tr><td><button onclick="mesTrigger(1)">MES Start</button><button onclick="mesTrigger(0)">MES Stop</button><button onclick="setCellLock(false);">Lock</button><button onclick="setCellUnLock();">UnLock</button></tr>
								</table>
							
		</div>	
	</div>
			
</div>
	
<script type="text/javascript" src="../../resources/js/3g/3g_NodeConfiguration.js"></script>
<script type="text/javascript" src="../../resources/js/3g/NodeConfigurationStructure.js"></script>

</body>
</html>