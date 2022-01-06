

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
  #myAddPopup{
  background:white;
  width:400px;
  border-radius:6px;
  margin: 140px auto 0 335px;
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

#reportsMenu{
font-weight:900;
text-decoration:underline;
}
</style>
<link href="../resources/lib/assets/css/bootstrap.min.css" rel="stylesheet" />
<link href="../resources/lib/assets/css/paper-dashboard.css" rel="stylesheet"/>
<link href="http://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" src="../resources/js/administration.js"></script>
		<!-- <link rel="stylesheet" type="text/css" href="../resources/css/administration.css" /> -->
		<div>
		
		
		<div>
	<!--  <fieldset>
		<legend>Group Operations</legend>
		<table>
			<tr>
				<td>
					<table class="opr" id="addGroup">			
						<tr>
							<td colspan = 3>Add Group</td>					
						</tr>
						<tr>
							<td>Group Name</td>
							<td><input type="text"  id="group_name"/></td>
							<td><input type="button" value="Add" id="group_add_btn"/></td>
						</tr>				
					</table>		
				</td>
				<td>
					<table class="opr" id="deleteGroup">
						<tr>
							<td colspan = 2>Delete Group</td>					
						</tr>
						<tr>					
							<td><select name="group" id="group"></select></td>
							<td><input type="button" value="Delete" id="group_del_btn"/></td>
						</tr>				
					</table>		
				</td>
				</tr>
				<tr>
				<td>
					<table class="opr" id="assign">
						<tr>
								<td colspan = 5>Assign Number to group</td>					
							</tr>
						<tr>
							
							<td>Group</td>
							<td><select name="group_id" id="group_id" id="group_id"></select></td>
							<td>Number</td>
							<td><select type="number_id" value="number_id" id="number_id"></select></td>
							<td><input type="button" value="Assign" id="add_num_grp_btn"/></td>
						</tr>				
					</table>		
				</td>
				<td>
					<table class="opr" id="unassign">
						<tr>
								<td colspan = 5>Unassign Number From Group</td>					
							</tr>
						<tr>
							
							<td>Group</td>
							<td><select name="un_group_id" id="un_group_id" id="un_group_id"></select></td>
							<td>Number</td>
							<td><select type="un_number_id" value="un_number_id" id="un_number_id"></select></td>
							<td><input type="button" value="Delete" id="un_num_grp_btn"/></td>
						</tr>				
					</table>		
				</td>
			</tr>
		</table>
	</fieldset>-->
	<div id="myAddPopup" style="min-width: 250px;clear: left;height: 200px;">
		<table>
			<tr>
			   <td colspan="4"><h4 align="center" id="uploadType" style="margin-left: 80px;">Purge Data</h4></td>
			 </tr>
			<tr>
			<td colspan="2"><label style="margin-left: 80px;">Data Type</label></td>
			<td colspan="2"><select id="dataTypeSelect" style="margin-left: 40px;"></select></td>
			</tr>
			<tr>
			</tr>
			<tr>
			</tr>
			<tr>
			<td style="border-bottom: 0px solid black;" colspan="4"><input type="button" value="Purge" id="truncateDb" style="margin-left: 185px;margin-top: 20px;width: 145px;"></td>
			</tr>							
		</table>
		</div>
						
		</div>
		</div>
	</body>
</html>