

	<jsp:include page='nodeheader.jsp' />
		<%
    		if(request.getParameter("up") != null){
			if (request.getParameter("up").equalsIgnoreCase("1")) 
    		{
    			out.println("<script>alert('Upload Successful');window.location.href='administration.jsp';</script>");
    		} 
    		else 
    		{
    			out.println("<script>alert('Upload Un-Successful');window.location.href='administration.jsp';</script>");
    		}}
		%>
		<script type="text/javascript" src="../resources/js/administration.js"></script>
		<link rel="stylesheet" type="text/css" href="../resources/css/administration.css" />
		<div id="container">
		
		
		<div id="operations">
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
	
		<table>
			<tr>
				<td>
					<fieldset>
						<legend>Monitoring Flag</legend>
					<table class="opr" id="deleteNumber">
						<tr>
							<td colspan = 2 >Invoke Trace</td>					
						</tr>
						<tr>							
							<td><input type="radio" value=1  name="mon_flag_trace" /> Enable</td>
							<td><input type="radio" value=0  name="mon_flag_trace" /> Disable</td>
						</tr>
						<tr>
							<td colspan = 2 style="border-bottom: 1px solid black;font-weight: bold;" >Subscriber Trace Mode</td>					
						</tr>
						<tr>							
							<td><input type="radio" value=1 name="mon_flag_imei_trace" />TRACE</td>
							<td><input type="radio" value=2 name="mon_flag_imei_trace" />Exclusion</td>
						</tr>
						<tr>
							<td colspan = 3 style="border-bottom: 1px solid black;font-weight: bold;">Auto Connect</td>					
						</tr>
						<tr>							
							<td><input type="radio" value="128" name="mon_flag_auto_trace" />Enable</td>
							<td><input type="radio" value="0" name="mon_flag_auto_trace" />Disable</td>
							<td>Duration &nbsp;<select id="mins_connect"></select></td>							
						</tr>
						<tr>
							<td colspan = 2 style="border-bottom: 1px solid black;font-weight: bold;" >Auto Paging/Alert</td>					
						</tr>
						<tr>							
							<td><input type="radio" value="0" name="mon_flag_rfu" />PAGING</td>
							<td><input type="radio" value="128" name="mon_flag_rfu" />ALERT</td>
						</tr>
						<tr>
							<td colspan = 2 style="border-bottom: 1px solid black;font-weight: bold;" >Auto Location</td>					
						</tr>
						<tr>							
							<td><input type="checkbox" name="loc_flag_rfu" />Location</td>
						</tr>
						<tr>
							<td colspan = 2 style="text-align:center;" ><input type="button" value="Update" id="update_mode"></td>					
						</tr>				
					</table>
					</fieldset>		
				</td>
			</tr>
		</table>
						
		</div>
		</div>
	</body>
</html>