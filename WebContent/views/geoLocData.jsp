<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='geoheader.jsp' /> 
<link rel="stylesheet" type="text/css" href="../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
<script type="text/javascript" src="../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../resources/lib/jqgrid/css/ui.jqgrid.css" />
<script type="text/javascript" src="../resources/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../resources/lib/jqgrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="../resources/lib/jqgrid/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="../resources/js/geolocdata.js"></script>
<div id="container">
	<div id="message_box">
		<div id="topdiv">
			<fieldset id="target_scan">
				<legend>Time Filter</legend>
				<table style="float:left;">
					<tr>
						<td>Start Time</td>
						<td><input class="dateSelect" type="text" id="startTime" ></td>
						<td>End Time</td>
						<td><input class="dateSelect"  type="text" id="endTime"></td>
						<td colspan=2><input type="button" id="get_cdr_data" value="Get"></td>					
					</tr>				
				</table>
				<table style="float:right;">
					<tr>
						<td><button type="button" class="" id="trace">Trace</button></td>
					</tr>
					<!--<tr>
						<td>Operations</td>
						<td>
							<Select id="opr" >
								<option value='5'>Block Services</option>
								<option value='6'>Call Divert</option>
							</Select>
						</td>						
						<td><input type="button" id="perform_opr" value="Get"></td>					
					</tr>-->				
				</table>			
			</fieldset>
			<fieldset id="sub_det" style='max-width:1348px;'>
				<legend>....</legend>
				<table id="jqGrid" style='max-width:1348px;'></table>
    			<div id="jqGridPager"></div>	
			</fieldset>
		</div>					
	</div>	
		<div id="loadingBox" style="width:100%;height:100%;display:none; text-align:center;">
			<img src="../resources/images/loading_bar_animated.gif" />
			<br>
			<input type="button" value="Cancel" id="cancelSearch">
		</div>
	</div>
<script>

$(function() {
    $( ".dateSelect" ).datetimepicker({
    	dateFormat: "yy-mm-dd",
    	timeFormat: 'HH:mm'
    });
  });
</script>
<script type="text/javascript"> 

   </script>
   <style>
   .ui-datepicker
   {
   	z-index:999 !important;
   }
   </style>
</body> 
</html>