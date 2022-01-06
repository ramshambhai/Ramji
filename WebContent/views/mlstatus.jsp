<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
	<jsp:include page='header.jsp' />
	 <style>
    text-align: left;
    padding: 8px;
	</style>
	<link rel="stylesheet" type="text/css" href="../resources/css/mlstatus.css" />
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

		<div id="operations2">
		<div style="margin:80px auto auto 480px;font-size: 26px;">NIB Details</div>
						<table border=1 id="list_table" class="tab_status" style="border-collapse: collapse;width: 60%;margin: 20px auto 10px 140px;text-align: center;box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);font-size:10px;">
						<thead style="background-color: #3a5795;color: white;">
							<th style="padding: 8px;">GROUP</th>
							<th style="padding: 8px;">IP</th>
							<th style="padding: 8px;">TYPE</th>
						</thead>
						<tbody id="tab_status_body" style="background-color:white;display: table-row-group;vertical-align: middle;border-color: inherit;">
						</tbody>
					</table>	
		</div>
			<script type="text/javascript" src="../resources/js/mlstatus.js"></script>
	</body>
</html>