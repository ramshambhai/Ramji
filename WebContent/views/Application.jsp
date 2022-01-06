<html>
<head>
<script type="text/javascript" src="../resources/js/jquery-1.11.3.min.js" ></script>
<link rel="stylesheet" type="text/css" href="../resources/css/style.css" />
<script type="text/javascript" src="../resources/lib/jquer_ui_1_11_4/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="../resources/lib/jquer_ui_1_11_4/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../resources/css/Application.css" />
<script type="text/javascript" src="../resources/js/Application.js"></script>
<style>
body{
  font-family: 'Open Sans', sans-serif;
  margin: 0 auto 0 auto;  
  width:100%; 
  text-align:center;
  margin: 20px 0px 20px 0px;   
}

h1{
  font-size:1.5em;
  color:#525252;
}

#myAddPopup{
  background:white;
  width:400px;
  border-radius:6px;
  margin: 20px auto 0 335px;
  padding:0px 0px 10px 0px;
  border: #2980b9 4px solid; 
}

input{
  background:#ecf0f1;
  border-radius:4px;
  border: #ccc 1px solid;
  padding: 8px;
  width:250px;
  font-size:1em;
  border-bottom: #ccc 2px solid;
  color:black;
  margin-top:0px; 
}

label{
margin-left: 10px;
}

.btn{
  background:#3498db;
    /* width: 125px; */
    padding-top: 5px;
    padding-bottom: 5px;
    color: white;
    border-radius: 4px;
    border: #27ae60 1px solid;
    margin-top: 10px;
    /* margin-bottom: 20px; */
    float: left;
    margin-left: 16px;
    font-weight: 800;
    font-size: 0.8em;
}

.btn:hover{
  background:#3594D2;  
}

.ui-resizable{
top: 40px;
left: 400px;
}

#ip_2G_mob_loc,#ip_2G_geo_loc,#of_ip,#ppf_ip,#spf_ip,#ip_3G_net_scan{
margin-left:25px;
}

.container{
height: 89px;
background-color: #426bc3;
cursor: pointer;
align:center;
}

#selectDeviceDiv{
    min-width: 250px;
    clear: left;
	float:left;
	margin-left: 400px;
	margin-top: 20px;
	margin-bottom: 20px;
}
</style>
</head>
<body>
<div id="applicationNames" style="margin-left:310px;">	
<div class="card" id="addDeviceDiv"><div id="containerDiv1" class="container"><h4><b>Add Device</b></h4></div></div>
<div class="card" id="addTargetDiv"><div id="containerDiv2" class="container"><h4><b>Target Operations</b></h4></div></div>				
				</div>

<div id="reload">
<div id="selectDeviceDiv"><label>Select Device : </label><select id="addDeviceSelect"></select></div>
	   <div id="myAddPopup" style="min-width: 250px;clear: left;">
		<div id="hiddenApplicationId" style="display: none;"></div>	   
	<br><br>	  
		
<h1 id="formHeading" style="margin-top: -15px;"></h1>
			<div id="firstPartPopup">
		    <div id="grp_name">
			   <table>
		        <tr>
			      <td><label>Group Name:</label></td>
				  <td><input type="text" id="group_name"></input></td>
                <tr>
               </table>				   
		     </div>
			 
		     <div id="G-3">			 
			   <table>
                 <tr> 
			      <td colspan=2><label>OF IP:</label></td>
			      <td colspan=2><input type="text" id="of_ip"></input></td> 
			     </tr>
		         <tr>  
			      <td colspan=2><label>PPF IP:</label></td>
			      <td colspan=2><input type="text" id="ppf_ip"></input></td>
			     </tr>
		          <tr>
                  <td colspan=2><label>SPF IP:</label></td>
				  <td colspan=2><input type="text" id="spf_ip"></input></td>
			     </tr>
			     <tr>
				  <td><button id="update_3G" class="btn">Submit</button></td>
				 </tr> 
			 </table>
              </div> 
	  
	          
			  
	          <div id="G2_mobile_loc">
			  <table>
			   <tr>
			     <td><label>IP:</label></td>
			     <td><input type="text" id="ip_2G_mob_loc"></input></td>
			   </tr>
		       <tr>
			     <td><button id="update_2G_mobile_loc" class="btn">Submit</button></td>
			   </tr>
              </table>
			  </div>
	  
	          <div id="G2_geo_loc">
		      <table>
			   <tr>
			     <td><label>IP:</label></td>
			     <td><input type="text" id="ip_2G_geo_loc"></input></td>
			   </tr>
			   <tr>
			     <td><button id="update_2G_geo_loc" class="btn">Submit</button></td>
			   </tr>
              </table>
			  </div>
			  
			   <div id="G3_net_scan">
		      <table>
			   <tr>
			     <td><label>IP:</label></td>
			     <td><input type="text" id="ip_3G_net_scan"></input></td>
			   </tr>
			   <tr>
			     <td><button id="update_3G_net_scan" class="btn">Submit</button></td>
			   </tr>
              </table>
			  </div>
			  </div>
		     <div id="addTargetId">				 
			   <table>
                 <tr> 
			      <td colspan=2><label>IMSI</label></td>
			      <td colspan=2><input type="text" id="targetImsi"></input></td> 
			     </tr>
		         <tr>  
			      <td colspan=2><label>IMEI</label></td>
			      <td colspan=2><input type="text" id="targetImei"></input></td>
			     </tr>
			     <tr>
				  <td><button id="addTargetButton" class="btn">Submit</button></td>
				 </tr> 
				 </table>
			 	 <table id="targetTable" border="1" class="table table-default tables_css" style="font-size: 10px;width: 90%;margin-left: 20px;margin-bottom: 20px;background-color: transparent;border-spacing: 0;border-collapse: collapse;">
			     <thead>
			     <tr>
				 <th>IMSI</th>	
				 <th>IMEI</th>	
				 <th>Delete</th>				
			     </tr>
			    </thead>
			    <tbody id="targetTableTbody" style="max-height: 200px;overflow: auto;">
			    </tbody>

			    </table>
                </div>

	  </div> 
	</div>	
</body>
</html>