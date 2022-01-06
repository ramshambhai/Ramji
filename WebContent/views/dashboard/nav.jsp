<nav class="navbar navbar-initial" style="border-bottom: 1px solid #DDDDDD;background-color: rgb(52, 58, 64);">
<script type="text/javascript" src="../../resources/js/nav.js"></script>
            <div class="container-fluid">
                <div class="navbar-header">
                    <!-- 
                    <button type="button" class="navbar-toggle">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar bar1"></span>
                        <span class="icon-bar bar2"></span>
                        <span class="icon-bar bar3"></span>
                    </button>
                   -->
                    <a class="navbar-brand" href="maps.jsp" style="color: white;"><i class="ti-panel"></i>&nbsp;<span id="dashboardMenu">Dashboard</span></a>
					<% if(!session.getAttribute("role").toString().equalsIgnoreCase("user")) {%>
					<a class="navbar-brand" href="operation.jsp" style="color: white;"><i class="ti-settings"></i>&nbsp;<span id="configurationMenu">Operation</span></a>
					
					<a class="navbar-brand" href="admin.jsp" style="color: white;"><i class="ti-support"></i>&nbsp;<span id="adminMenu">Admin</span></a>
					<% }%>
					<!--  
					<%-- <% if(session.getAttribute("role").toString().equalsIgnoreCase("superadmin")) {%>
					<a class="navbar-brand" href="deployment.jsp" style="color: white;"><i class="ti-support"></i>&nbsp;<span id="deploymentMenu">Deployment</span></a>
					<% }%> --%>
					-->
                </div>

                <div class="collapse navbar-collapse navbar-nav navbar-right">
				
				  <div class="dropdown" style="float: left;">
    <button style="color: white;border-color: white;" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><%= session.getAttribute("userName").toString()%>
    <span class="caret"></span></button>
                    <ul class="dropdown-menu" style="width: 180px;">
							<li>
                            <a href="#" id="changePassword" onclick="displayChangePasswordModal()">
								<i class="ti-direction-alt" style="float: left;margin-right: 4%;"></i>
								<p>Change Password</p>
                            </a>
                        </li>
                        <li>
                            <a href="locationTool.jsp" style="float: left;margin-right: 4%;" >
								
								<i class="ti-settings"></i>&nbsp;<span id="toolMenu">Analysis Tool</span>
								
								
                            </a>
                        </li>
                        <li>
                            <a href="../../logout">
								<i class="ti-power-off" style="float: left;margin-right: 4%;"></i>
								<p>logout</p>
                            </a>
                        </li>
                    </ul>

  </div>
  <div style="display: inline;float: left;margin-left: 10px;margin-top: -10px;">
  					<li>
							<a>
								<p style="color: #70a2bb;" id="locatorVersion">v1.1.5</p>
                            </a>
                      </li>

</div>
  
</div>
	                <div id= "eventMsgDiv" class="collapse navbar-collapse navbar-nav navbar-right" style="background-color: white;color: red;display: none !important;">
                <span id="eventMsgSpan">OCTASIC TEMPERATURE THRESHOLD EXCEEDED</span>
                </div>			
				


                </div>
        </nav>