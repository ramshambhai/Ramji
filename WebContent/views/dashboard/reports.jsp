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

#reportsMenu{
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



</style>
<div class="wrapper">
<jsp:include page='nav.jsp' />

<div class="main-panel">
<jsp:include page='sidereporttree.jsp' />        

<div id="reload" style="margin-left:101px;">
	   <div id="myAddPopup" style="min-width: 250px;clear: left;">
		<div id="hiddenApplicationId" style="display: none;"></div>	   
	<br><br>	   
		     <div id="reportsDiv" style="display: none;">			 
			   <table>
			   <tr>
			   <td colspan="4"><h4 align="center" id="reportType" style="margin-top: -15px;"></h4></td>
			   </tr>
                 <tr> 
			      <td colspan="2"><label>Start Time:</label></td>
			      <td colspan="2"><input class="dateSelect" type="text" id="startTime"></input></td> 
			     </tr>
		         <tr>  
			      <td colspan="2"><label>End Time:</label></td>
			      <td colspan="2"><input class="dateSelect"  type="text" id="endTime"></input></td>
			     </tr>
		          <tr id="widsFilter">
                  <td colspan="2"><label>WIDS Filter</label></td>
				  <td colspan="2">
				  <select id="widsFilterOptions" style="width: 251px;">
				  <option value="1" name="rpt_type" id="min_tyle">Min</option>
				  <option value="2" name="rpt_type" id="sec_tyle">Sec</option>
				  <option value="3" name="rpt_type" id="all_tyle" selected>All</option>
				  </select>
				  </td>
				  </tr>
				  <tr id="operation">
                  <td colspan="2"><label>Operation</label></td>
				  <td colspan="2">
				  <select id="operationOptions" style="width: 251px;">
				  </select>
				  </td>
				  </tr>
				  <tr>
				  <td colspan="3"></td>				  
				  <td><button style="width: 250px;" type="button" id="downloadReport" value="Download" class="btn-default">Download</button><div id="btn_dataExport"></div></td>				  
				  </tr>				 
			 </table>
              </div> 
			  
			   <div id="uploadDiv">			 
			   <table>
			   <tr>
			   <td colspan="4"><h4 align="center" id="uploadType" style="margin-top: -15px;margin-left: 30px;">Upload Data</h4></td>
			   </tr>
			   <tr>
			   <td colspan="4" style="font-weight: 800;"><label style="margin-left: 30px;">Upload GPS Data</label></td>
			   </tr>
			   <tr>
			   <td colspan="4">
							<form action = "../../Operations?methodName=uploadGpsData" method = "post" enctype = "multipart/form-data">
				         	<input type = "file" name = "file" id="fileUploadInput" class="btn-default btn-file"></input>
														
							<button type = "submit" value = "Upload File" class="btn-default" style="padding:8px;">Upload File</button>
							</form>
				</td>
				</tr>
			<tr>
			   <td colspan="4" style="font-weight: 800;"><label style="margin-left: 30px;">Upload Mobile Type Data</label></td>
			 </tr>
			 <tr>
			 <td colspan="4">
							<form action = "../../Operations?methodName=uploadCSV" method = "post" enctype = "multipart/form-data">
							
				         	<input type = "file" id="file" name = "file" size = "50" class="btn-default btn-file"></input> 
				         	
				         <button type = "submit" value = "Upload File" class="btn-default" style="padding:8px;">Upload File</button>
							</form>
              </td>					  
			</tr>					  
			 </table>
              </div> 
			  <div id="loadingBox" style="display: none;text-align:center;">
				<img src="../../resources/images/Loading_icon.gif"  style="margin-left: 0px;margin-top: -40px;width: 100%;" />	
			  </div>			  
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
	</div>	
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
<script type="text/javascript" src="../../resources/js/reports.js"></script>

<style>
		<%= session.getAttribute("themeCss")%>
	</style>


</html>