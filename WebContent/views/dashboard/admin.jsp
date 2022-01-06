<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
		<%
    		if(request.getParameter("up") != null){
			if (request.getParameter("up").equalsIgnoreCase("1")) 
    		{
    			out.println("<script>alert('Upload Successful');window.location.href='dashboard/reports.jsp';</script>");
    		} 
    		else 
    		{
    			out.println("<script>alert('Upload Un-Successful');window.location.href='administration.jsp';</script>");
    		}}
		%>
<style>


.boxes {
    		transition: 0.3s;
    		float:left;
    		margin: 0 auto 10px 10px;
    		color:white;
		}
		
		

#leftTree{
background-color: white;
    height: 640px;
    overflow: auto;
    position: absolute;
    display: block;
    visibility: visible;
}
#leftTree li[rel="1stLevel"] a{
color:black;
}
#leftTree li[rel="2ndLevel"] a{
color:red;
}
#leftTree li[rel="3rdLevel"] a{
color:green;
}
#leftTree li[rel="4thLevel"] a{
color:blue;
}

    
#hiddenApplicationId
{
	display: none;
}

#eventDataType
{
	width: 150px;
	height: 28px;
}

.threecards1> div{
  //background:#29B5BA;
  //padding:1em;
  margin:1%;
  grid-column-gap:12%
}
 .container2 {
  position: relative;
  text-align: center;
  color: white;
  }

.centered2 {
  position: absolute;
  top: 50%;
  left: 20%;
  transform: translate(-50%, -50%);
  }
		
.centered {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  }
  
  .admin-select{
  	width: 150px;
  }
  
  .admin-password{
  	width: 150px;
  }
  
  .admin-button{
 	width: 150px;
  }
  
    .cp-admin-select{
  	width: 250px;
  }
  
  .cp-admin-password{
  	width: 250px;
  }
  
  cp-.admin-button{
 	width: 250px;
  }
 

</style>
<style>
#myAddPopup{
  background:white;
  width:450px;
  border-radius:6px;
  margin: 80px auto 0;
  padding:0px 0px 10px 0px;
  border: 1px solid grey; 
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

/*.btn{
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
}*/

.btn:hover{
  background:#3594D2;  
}

#adminMenu{
font-weight:900;
text-decoration:underline;
}

.main-panel {
    
    width: 100%;
}


.navbar {
    margin-bottom: 0px;
}
.logo
{
	display:none;
}

html
{
	overflow-x:auto;	
}
    
.btn-file{
	display:inline; 
	margin-left: 30px;
	width: 270px;
}

input[type=file]{
display: inline;
}

.backupButton{
 width: 100px;
}


.reload2
{
	//height:1000px;
	margin-left:24%;
	//background-color: coral;
}

.falogo1
{
	//font-size:89px;
	font-size:25px;
	color: white;
}


.cardimagebackground1
{
	//height:261px;
	height:70px;
}

.main-panel-admin{
	background: white !important;
	width: 100%;
	overflow: auto;
    max-height: 100%;
    height: 100%;
    -webkit-transition-property: top,bottom;
    transition-property: top,bottom;
    -webkit-transition-duration: .2s,.2s;
    transition-duration: .2s,.2s;
    -webkit-transition-timing-function: linear,linear;
    transition-timing-function: linear,linear;
    -webkit-overflow-scrolling: touch;
    background-color: #f4f3ef;
    position: relative;
    float: right;
    /* width: calc(100% - 260px); */
    min-height: 100%;
}

.addAntennaInput{
	width: 80px;
}

.editAntennaInput{
	width: 60px;
}

</style>

<div class="wrapper">
<jsp:include page='nav.jsp' />
<div class="side-panel">
<jsp:include page='sideadmintree.jsp' />        

<div id="reload" class="reload2">
	   
		<div id="hiddenApplicationId" ></div>	   
	<br><br>	    
			  
			  <div id="changePasswordDiv">
			  <div class="threecards1">
			  <div class="card" style="width: 20%;">
			  </div>
			  <div class="card" style="width: 50%;">	
			  <div class="container2">
		  	<img src="../../resources/images/imageforboxes_black.png" class="card-img-top cardimagebackground1" alt="Snow" style="width:100%;">
		 	<i class="centered2 undologo fa  falogo1 card-img-top  fa-key" style="font-size:21px;"></i>
		  	<div class="centered "><div align="center"  >Change Password</div>
		  	</div>
			</div>	
			
			 <div class="card-body" style="padding:0.2px; height: 348px ;">
			  <br/>	 
			   <table>
			   <tr>
			   <td><label>User</label></td>
			   <td colspan="3">
                <select id="userDataSelect" class="cp-admin-select">
				</select>
				</td>
			   </tr>
			   <tr>
			   <td><label>New Password</label></td>
			   <td colspan="3">
				<input type="password" id="newAdminPassword" class="cp-admin-password" />
				</td>
			   </tr>
			   	<tr>
			   <td><label>Confirm Password</label></td>
			   <td colspan="3">
				<input type="password" id="confirmAdminPassword" class="cp-admin-password" />
				</td>
			   </tr>
			   	<tr>
				<td colspan="2">
				</td>
			   <td colspan="2">
				<input type="button" class="btn-default cp-admin-button" id="changePasswordButton" value="Change" />
				</td>
			   </tr>					  
			 </table>
			 </div>
              </div>
              <div class="card" style="width: 20%;">
              </div>
              </div>
              </div>
              
			  <div id="backupPurgeDiv"  style="display: none;" >
			  <div class="threecards1">
		  <div class="card" style="width: 30%">		
				
		<div class="container2">
		  <img src="../../resources/images/imageforboxes_black.png" class="card-img-top cardimagebackground1" alt="Snow" style="width:100%;">
		 <i class="centered2 undologo fa  falogo1 card-img-top  fa-file" style="font-size:21px;"></i>
		  <div class="centered "><div align="center">Reports</div>
		  
		  </div>
		</div>
		
		<!--   <img class="card-img-top" src="imageforboxes_black.png" alt="Card image cap"> -->
			  <div class="card-body" style="height: 50% ;">
			  <br />

			  	 
			    <table >
				   <tr>
				   <td colspan="3"></td>
				   </tr>
		                <tr> 
				      <td colspan="1"  ><label >Start Time:</label></td>
				      <td colspan="1"><input class="dateSelect" style="width:150px;" type="text" id="startTime"></input></td> 
				     </tr>
			         <tr>  
				      <td colspan="1"><label ">End Time:</label></td>
				      <td colspan="1"><input class="dateSelect" style="width:150px;" type="text" id="endTime"></input></td>
				     </tr>
					  <tr>
					 				  
					  <td colspan="2"><button style="width: 100%;;" type="button" id="downloadReport" value="Download" class="btn-default btn-lg btn">Generate</button></td>
					  </tr>
					  <!-- <tr>
					  <td colspan="2"><button style="width: 100%;" type="button" id="purgeData" class="btn-default btn-lg btn">Purge</button></td>

					  </tr> -->				 
				 </table>
			    </div>
			      </div>
			    
				
		
			
		
		<div class="card" style="width: 30%;">
		<div class="container2">
		  <img src="../../resources/images/imageforboxes_black.png" class="card-img-top cardimagebackground1" alt="Snow" style="width:100%;">
		 
				  
				  
				  
				  <i class="centered2 undologo fa  falogo1 card-img-top fa-trash   " style="font-size:21px;"></i>
		  <div class="centered ">Data Reset</div>
				  
				  
				  
		</div>
		
		<!--  <img class="card-img-top" src="imageforboxes_black.png"  alt="Card image cap">-->
		  <div class="card-body" style="height: 50%;">
		  <br />
		  <br />
		 
		  
		  
		    <!-- <h5 class="card-title" align="center" style="color:black;"></h5> -->
		    
		 <div class="div-reset-all" style="height: 17%;">
		
									<table >
										<tr>
											  <td colspan="1">Type</td>
										      <td colspan="1"><select id="eventDataType" class="dateSelect"  >
										 <option value="select" selected>select</option>
										 <option value="all">All</option>
										 <option value="cell_infrastructure">Cell Infrastructure</option>
										 <option value="data_events">Data Events</option>
										 <option value="audit_logs">Audit Logs</option>
										 <option value="gps_data">GPS Data</option>
										 </select></td> 
										</tr>
									<tr>
									
									<td colspan="2"><buton  type="button" id="resetAllData" class="btn-default btn btn-default btn-md btn-block" style="width: 250px;">Reset All</button></td>
										
									</tr>
									</table>
		
									</div> 
									<br><br>
									<br><br>
									<br><br>
									<br><br>
									<br>
									
		
		  </div>
		</div>
		
		<div class="card" style="width: 30%;" >
		  
		
		
			<div class="container2">
				  <img src="../../resources/images/imageforboxes_black.png" class="card-img-top cardimagebackground1" alt="Snow" style="width:100%;">
				  
				  <i class="centered2 undologo fa  falogo1 card-img-top  fa-hdd-o" style="font-size:21px;"></i>
		  <div class="centered ">Backup & Logs
		  </div>
				  	  
        </div>
		<div class="card-body" style="height: 50%;">
		
		 <div class="div-reset-all" style="height: 17%;">
								 <br><br>
							<button type="button" style="    width: 90%;    margin: 3%;" class="btn-default btn-lg btn " id="fullDbBackup">Full Backup</button>
							<br/><br>
							<button type="button" style="    width: 90%;    margin: 3%;" class="btn-default btn-lg btn " id="systemLogReport">System Logs</button>
							<br/><br> 	
							<button type="button" style="    width: 90%;    margin: 3%;" class="btn-default btn-lg btn " id="factoryResetConfig">Factory Reset Configuration</button>
							<br><br><br><br>
									</div>
		<br><br><br><br><br><br><br><br><br>
		  </div>
		</div>
		



		</div>
              
              
              
              
              
              
     </div>    
            
            
    			  <div id="configOperationsDiv" style="display: none;">
			  <div class="threecards1">	
			  <div class="card" style="width: 47%;">	
			  <div class="container2">
		  	<img src="../../resources/images/imageforboxes_black.png" class="card-img-top cardimagebackground1" alt="Snow" style="width:100%;">
		 	<i class="centered2 undologo fa  falogo1 card-img-top  fa-save" style="font-size:21px;"></i>
		  	<div class="centered "><div align="center"  >Save Configurations</div>
		  	</div>
			</div>	
			
			 <div class="card-body" style="padding:0.2px; height: 348px ;">
			  <br/>	 
			   <table>
			    <tr>
			   <td colspan="2">
				<input type="textbox" id="profileName" style="width:150px;" placeholder="profile name" />
				</td>
				<td colspan="2">
				<input type="button" class="btn-default cp-admin-button" id="saveProfBtn" value="Save Profile" />
				</td>
			   </tr>
			   <tr>
			   <td colspan="2">
			   </td>
			   <td colspan="2">
                <input type="button" class="btn-default cp-admin-button" id="showCurrProfBtn" value="Show Current Configurations" />
				</td>
			   </tr>					  
			 </table>
			 </div>
              </div> 
              <div class="card" style="width: 47%;">
              			  <div class="container2">
		  	<img src="../../resources/images/imageforboxes_black.png" class="card-img-top cardimagebackground1" alt="Snow" style="width:100%;">
		 	<i class="centered2 undologo fa  falogo1 card-img-top  fa-tasks" style="font-size:21px;"></i>
		  	<div class="centered "><div align="center"  >Apply/Delete Configurations</div>
		  	</div>
			</div>	
			
			 <div class="card-body" style="padding:0.2px; height: 348px ;">
			  <br/>	 
			   <table  class="admin-table">
			   	<tr>
			   <td colspan="1">
				<select id="profileList" class="cp-admin-password" /></select>
				</td>
				</tr>
				<tr>
				<td colspan="1">
				<input type="button" class="btn-default cp-admin-button" id="showProfBtn" value="Show Configurations" />
				</td>
				</tr>
				<tr>
				<td colspan="1">
				<input type="button" class="btn-default cp-admin-button" id="applyProfBtn" value="Apply Configuration" />
				</td>
				</tr>
				<tr>
				<td colspan="1">
				<input type="button" class="btn-default cp-admin-button" id="deleteProfBtn" value="Delete Configuration" />
				</td>
				</tr>					  
			 </table>
			 </div>
              </div>
              </div>
              </div>  
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
			  
			   <div id="backupHistoryDiv" style="display: none;">
			   			  <div class="threecards1">
			   <div class="card" style="width: 10%;">
			   </div>	
			  <div class="card" style="width: 76%;">	
			  <div class="container2">
		  	<img src="../../resources/images/imageforboxes_black.png" class="card-img-top cardimagebackground1" alt="Snow" style="width:100%;">
		 	<i class="centered2 undologo fa  falogo1 card-img-top  fa-history" style="font-size:21px;"></i>
		  	<div class="centered "><div align="center">Backup History</div>
		  	</div>
			</div>	
			
			 <div class="card-body" style="padding:0.2px; /* height: 348px ; */">			 
			   <table class="table table-default report_tables">
			   <thead>
			   <tr>
			   <td>File</td>
			   <td>Download</td>
			   <td>Delete</td>
			   </tr>
			   </thead>
			   <tbody id="backupHistoryTableTbody">
			   </tbody>					  
			 </table>
			 </div>
			 
              </div>
              <div class="card" style="width: 10%;">
              </div>
              </div>
              </div>
              
              
              
			  <div id="loadingBox" style="display: none;text-align:center;">
				<img src="../../resources/images/Loading_icon.gif"  style="margin-left: 0px;margin-top: -40px;width: 100%;" />	
			  </div>			  
	  
	  <table id="tableUnion" style="display: none;">
	  <tr>
	  <td>
	  					<table border=1 id="detail_tab_1" class="table table-default report_tables">
							<thead>
								<tr><th colspan=13>Node Wise</th></tr>
							</thead>
							<tbody id="detail_tab_1_tbody">
								<tr>
									<td></td>
									<td>Count  - Mobile Identity</td>
									<td colspan=6>Distance</td>
									<td colspan=5>Signal Strength</td>
								</tr>
								
								<tr>
									<td></td>
									<td>Total IMSI, IMEI</td>
									<td>0</td>
									<td>1</td>
									<td>2</td>
									<td>&lt;5</td>
									<td>&gt;5</td>
									<td>NA</td>
									<td>&gt;-55</td>
									<td>&gt;-75</td>
									<td>&gt;-95</td>
									<td>&lt;-95</td>
									<td>NA</td>
									
								</tr>
							</tbody>
						</table>
						</td>
						</tr>
						<tr><td></td></tr>
						<tr><td></td></tr>
						<tr>
						<td>
						<table border=1 id="detail_tab_2" class="table table-default report_tables">
							<thead>
								<tr><th colspan=14>Country wise</th></tr>
							</thead>
							<tbody id="detail_tab_2_tbody">	
								<tr>
									<td></td>
									<td></td>
									<td>Count  - Mobile Identity</td>
									<td colspan=6>Distance</td>
									<td colspan=5>Signal Strength</td>
								</tr>
								<tr>
									<td>Country</td>
									<td>Operator</td>
									<td>Total IMSI, IMEI</td>
									<td>0</td>
									<td>1</td>
									<td>2</td>
									<td>&lt;5</td>
									<td>&gt;5</td>
									<td>NA</td>
									<td>&gt;-55</td>
									<td>&gt;-75</td>
									<td>&gt;-95</td>
									<td>&lt;-95</td>
									<td>NA</td>
									
									
									
									
									
									
									
									
								</tr>
								
							</tbody>
						</table>
						</td>
						</tr>
						</table>
						<div class="modal fade" id="configurationDisplayModal" role="dialog" aria-labelledby="myModalLabel" style="width:1360px !important;">
		<div class="modal-dialog" style="width: 1320px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Configurations</h4>
				</div>
				<div class="modal-body">
				<div>
				<label>Angle Offset
				<span class="parameter_units_class">(degree)</span>
				</label>
				<input type="text" id="sectorAntennaAngleOffset" class="addAntennaInput"></input>
				<span class="parameter_units_class">(In case Sector Antenna has been selected)</span>
				</div>
				<br>
				<div id="displayAntennaProfileDiv">
				<table border=1 id="displayAntennaProfileTable" style="font-size:10px;margin: 0 auto 10px 10px;" class="table table-bordered" style="width: 98%;">
				<thead class="antenna_label_class">
				<tr>
				<th><label class="labelClass">Scanning</label></th>
				<th><label class="labelClass">Tracking</label></th>
				<th><label class="labelClass">Name</label></th>
				<th><label class="labelClass">Type</label></th>
				<th><label class="labelClass">Angle</label></th>
				<th><label class="labelClass">TX Power<span class="parameter_units_class">(watt)</span></label></th>
				<th><label class="labelClass">Frequency<span class="parameter_units_class">(MHz)</span></label></th>
				<th><label class="labelClass">Gain<span class="parameter_units_class">(dBi)</span></label></th>
				<th><label class="labelClass">Height<span class="parameter_units_class">(meter)</span></label></th>
				<th><label class="labelClass">H-BW<span class="parameter_units_class">(degree)</span></label></th>
				<th><label class="labelClass">V-BW<span class="parameter_units_class">(degree)</span></label></th>
				<th><label class="labelClass">Tilt<span class="parameter_units_class">(degree)</span></label></th>
				<th><label class="labelClass">Azimuth<span class="parameter_units_class">(degree)</span></label></th>
				<th><label class="labelClass">Terrain</label></th>				
				</tr>
				</thead>

				<tbody id="displayAntennaProfileTable_body"></tbody>
				</table>
				</div>

				<div id="displayTargetDiv">
				<table border=1 id="displayTargetTable" style="max-height: 30%;height: 30%;overflow: auto;"  style="margin: 0 auto 10px 10px;text-align:center;" class="table table-default table-bordered table-target">
				<thead>
				<tr>
				<th><label>NAME</label></th>
				<th><label>IMSI</label></th>
				<th><label>IMEI</label></th>
				<th><label>TYPE</label></th>
				</tr>
				</thead>
				<tbody id="displayTargetTable_body"></tbody>
				</table>
				</div>
				<div id="displaySystemProperties">
				<table id="systemProperties" border=1 class="table table-bordered" style="width: 98%;font-size:10px;margin: 0 auto 10px 10px;"  style="max-height: 30%;height: 30%;overflow: auto;">
				 
				<thead>
				<tr>
				<th>Parameter</th><th>Value</th>
				</tr>
				</thead>
				<tbody id="systemProperties_tbody"></tbody>
				</table>
				</div>
				<!--
				<tr>
				<td><label style=" font-size:15px">Latitude</label></td>
				<td><input type="text" id="latConfig" style="font-weight: 500px;" value="28.485652"></input></td>
				</tr>
				<tr>
				<td><label style=" font-size:15px">Longitude</label></td>
				<td><input type="text" id="lonConfig" style="font-weight: 500px;" value="77.075046"></input></td>
				</tr>
				<tr>
				<td><label style=" font-size:15px">Timezone</label></td>
				<td><input type="text" id="timezoneConfig" style="font-weight: 500px;" value="IST"></input></td>
				</tr>
				-->							

				
				
				
				</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
						
	</div>
	</div>
				
	
<jsp:include page='modal_footer.jsp' />
</body>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
<script type="text/javascript" src="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="../../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/style.css" />
<script type="text/javascript" src="../../resources/lib/jstree/jquery.jstree.js"></script>
<script type="text/javascript" src="../../resources/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../../resources/lib/jquery.battatech.excelexport.js"></script>
<link rel="stylesheet" type="text/css" href="../../resources/css/reports.css" />
<script type="text/javascript" src="../../resources/lib/export.js"></script>
<script type="text/javascript" src="../../resources/js/admin.js"></script>
<link href="../../resources/css/font-awesome.min.css" rel="stylesheet">





























<style>
		<%= session.getAttribute("themeCss")%>
	</style>
<style>
	.div-reset-all{
		//border-top: 0.5px groove black;
    	//height: 10%;
	}
	
	.div-factory_reset{
		//border-top: 0.5px groove black;
    	height: 10%;
	}

	.btn-reset-all{
	    margin-top: 5%;
    	width: 56%;
    	margin-left: 24%;
    }
    
    .admin-table{
    	width: 50%;
    	margin-left: 25%;
    	margin-right: 25%;
	}
	
	.sidebar{
	 top: 50px;
	 bottom:-50px;
	}
	</style>


</html>