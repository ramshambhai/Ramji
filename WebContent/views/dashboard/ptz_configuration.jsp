<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
	<script>	
		 var deviceIp  = <%= request.getParameter("ip").equalsIgnoreCase("null")?"":"'"+request.getParameter("ip")+"'"%>
	 	var deviceId = <%= request.getParameter("id").equalsIgnoreCase("null")?"":request.getParameter("id")%>
	 	var sysid = <%= request.getParameter("sysId").equalsIgnoreCase("null")?"":request.getParameter("sysId")%>
	 	var dirPath = "../../";
	 </script>
 
<link rel="stylesheet" type="text/css" href="../../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/3g/common.css" />
<style>
#configurationMenu{
font-weight:900;
text-decoration:underline;
}
.control_btn tbody tr > td
{
	border-top:none;
}
</style>
<div class="wrapper">
    <div class="main-panel">
		<jsp:include page='nav.jsp' />
		<div style="width: 100%;height: 90%;display: table;">
	<div style="width: 100%;height: 90%;display: table-cell;vertical-align: middle;">
		<div class="col-xs-6 col-xs-push-3">
			<ul class="nav nav-tabs" style="display:none">
			  <li class="active"><a data-toggle="tab" href="#netscan_scanner_cont">Operation</a></li>
			</ul>
			<div class="tab-content">
			<div class="tab-pane fade in active" id="netscan_scanner_cont"> 					  
				<table class="table table-bordered" id="netscan-scanner">
					<tbody class="control_btn">
						<tr style="display:none;"><td colspan=2></td></tr>
						<tr ><td colspan=3 style="text-align: center;font-weight: 700;font-size: 25px;">STRU Operation</td></tr>
						<tr class="cell_scan_field freq_scan_field stop_scan_row exe_scan_field">
							<tr >
								<td colspan=3 >
									<ul class="nav nav-tabs" id="myTab1" role="tablist">
									<li class="nav-item active"><a class="nav-link active"
										id="ptz-tab" data-toggle="tab" href="getNorthOffset"
										onclick="getDisplay('getNorthOffset','ptz_operation')"
										role="tab" aria-controls="profile" aria-selected="false"><strong>Get North Offset</strong></a>
									</li>
									
									<li class="nav-item"><a class="nav-link"
										id="profile-tab" data-toggle="tab" href="#ptz_operation"
										onclick="getDisplay('ptz_operation','getNorthOffset')"
										role="tab" aria-controls="home" aria-selected="true"><strong>Operation</strong></a>
									</li>
								
								</ul>
							</td>
						</tr>
							
							
							
							
							
							
							
							
							
							
								<tr id="getNorthOffset1" >
								
								<td colspan="3"  style="height :48px;" >
									
									
								</td>
								
								</tr > 
								<tr id="getNorthOffset2" >
								
									
									<td colspan="3">
										
										<label style="margin-left: 42px;">North Heading (deg) </label>
										
										<input type="text" id="ptz_north_offset_txt" value="" disabled="" style="margin-left: 124px;width: 170px;">
									</td>
									
								</tr>
								<tr id="getNorthOffset5" >
									<td colspan="3" style="height :10px;">	
									</td>
								
								</tr > 
								<tr  id="getNorthOffset3">
									
									<td colspan="3">
											
											<label style="margin-left: 41px;">GPS Coordinates</label>
											<input type="text" id="coordinate_gps" style="margin-left: 149px;width: 170px;" "="" value="" disabled="">
										</td>
									
								</tr>
								<tr id="getNorthOffset4" >
								
								<td colspan="3" id="getNorthOffset" style="height :48px;" >
									
									<button class="btn btn-default btn-operation" id="ptz_north_offset_btn" onclick="getNorthHeading()" style="margin-left: 371px;">Get North Offset</button>
								</td>
								
								
								</tr > 
							
							
							
							
							
							
							
							
							
									
								<tr class="cell_scan_field freq_scan_field stop_scan_row exe_scan_field" id="ptsOperations3" style="display:none;">
									
									<td>
										<label>Current Angle (deg)</label>
										<input type="text" id="ptz_current_angle_txt" value="" disabled></input>
									</td>
									<td>
										<label>Tilt (deg)</label>
										<input type="text" id="ptz_north_tilt_txt" value="" disabled></input>
									</td>
									
									<td colspan="3">
										<button class="btn btn-default btn-operation" id="ptz_current_angle_btn" onclick="getCurrentAngle()">Get Current Angle</button>
									</td>
									</tr>
							
								<tr class="cell_scan_field freq_scan_field stop_scan_row exe_scan_field" id="ptsOperations2" style="display:none;">
									
									
								</tr>
								
								<tr id="ptsOperations1" style="display:none;">
									
									<td colspan="3">
										<label>Rotation Step Angle (deg)</label>
										<input type="number" id="ptz_move_angle">
									</td>
								</tr>
									
									
								<tr id="ptsOperations4" style="display:none;">
										<td colspan="3" style="text-align: center;">							
											<span style="">
												<table style="margin: 0 auto;width: 30%;">
													<tbody>
														<tr>
															<td></td>
															<td><i style="cursor:pointer;font-size: 40px;" onclick="move(2)" class="fa fa-arrow-up"></i></td>
															<td></td>
														</tr>
														<tr>
															<td><i style="cursor:pointer;font-size: 40px;" onclick="move(4)" class="fa fa-arrow-left"></i></td>
															<td><i style="cursor:pointer;font-size: 40px;" onclick="move(-1)" class="fa fa-sync"></i></td>
															<td><i style="cursor:pointer;font-size: 40px;" onclick="move(5)" class="fa fa-arrow-right"></i></td>
														</tr>
														<tr>
															<td></td>
															<td><i style="cursor:pointer;font-size: 40px;" onclick="move(7)" class="fa fa-arrow-down"></i></td>
															<td></td>
														</tr>
													</tbody>
												</table>
											</span>
										</td>
								</tr>
								
						</tbody>
					</table>
					</div>
				</div>		
			</div>
	</div>
</div>
	</div>	
	</div>
	<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
	<img src="../resources/images/loading_bar_animated.gif" />
	<br>
	<input type="hidden" id="MSISDN_VAL" value="" />
	<input type="button" value="Cancel" id="cancelSearch" onclick="cancelClick()">
	<input type="button" value="Back" id="backtodashboard" onclick="location.reload()">
</div>
			
</div>
</div>










































					<div id="dialog" style="display:none">
						<table id="data_tab"></table>
						<div id="pager"></div>
					</div>
				</div>


<link rel="stylesheet" type="text/css" href="../../resources/lib/jsonviewer/jquery.json-viewer.css"/>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" href="../../resources/lib/assets/css/fontawesome-all.min.css">
<link rel="stylesheet" href="../../resources/css/font-awesome.min.css">
<script type="text/javascript" src="../../resources/lib/jqgrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="../../resources/lib/jqgrid/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="../../resources/js/netscan/netscan_configuration.js"></script>
<script type="text/javascript" src="../../resources/lib/jsonviewer/jquery.json-viewer.js"></script>

<script type="text/javascript" src="../../resources/js/ptz/ptz_index.js"></script>
<!--  <script type="text/javascript" src="../../resources/js/3g/configurationStructure.js"></script>-->
<style>
html
{
	margin:0 auto;
}
	.main-panel
	{
		width:100%;
		float:none;
	}
	.nav-tabs
	{
		margin-top:10px !important;
		background:#f4f3ef;
	}
	
	.nav-tabs>li>a
	{
		    border: 1px solid #00000008;
	}
	
	input[type=number]
	{
		width:100%;
	}
	
	.modal-backdrop {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 1;
    background-color: #000;
	}
	
</style>
<!-- 
<div  class="modal fade" id="loading_modal" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Please Wait...</h4>

				</div>
				<div class="modal-body">
				<img width=100% id="status_loading" src="../../resources/images/Loading_icon.gif"></img>
				</div>
			</div>
		</div>
	</div> modal -->
<jsp:include page='fotter.jsp' />

</body>
</html>