<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' /> 
<link rel="stylesheet" type="text/css" href="../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
<script type="text/javascript" src="../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../resources/lib/jqgrid/css/ui.jqgrid.css" />
<script type="text/javascript" src="../resources/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../resources/lib/jqgrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="../resources/lib/jqgrid/js/i18n/grid.locale-en.js"></script>
<!--<script type="text/javascript" src="../resources/lib/jquery.battatech.excelexport.js"></script>-->
<script type="text/javascript" src="../resources/lib/export.js"></script>
<script type="text/javascript" src="../resources/js/oprreportcwids.js"></script>
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
						
						<td>
							[
							<input type="radio" value=1 name="rpt_type" id="min_tyle"> : Min 
							<input type="radio" value=2 name="rpt_type" id="sec_tyle"> : Sec
							<input type="radio" value=3 name="rpt_type" id="all_tyle" checked> : All
							]
						</td>
						<td colspan=2><input type="button" id="get_cdr_data" value="Get"></td>
						 	
					</tr>				
				</table>
				<table style="float:right;">
					<tr>						
						<td colspan=2><input type="button" id="downloadReport" value="Download"></td>					
					</tr>				
				</table>			
			</fieldset>
			<div id="tabs" style="padding: 0px;">
			<ul>
		    	<li><a href="#tabs-1" id="tab1" >CDR-Operations</a></li>
		    	<li><a href="#tabs-2">WIDS-Operations</a></li>
		    	<li><a href="#tabs-3" style="display:none;" id="tab3" >Map</a></li>
		  </ul>
  		<div id="tabs-1" style="padding: 0px;">
    		<fieldset id="sub_det" style='max-width:1348px;'>
				<legend>....</legend>
				<table id="jqGrid" style='max-width:1348px;'></table>
    			<div id="jqGridPager"></div>	
			</fieldset>
		  </div>
		  <div id="tabs-2" style="padding: 0px;">
		    <fieldset id="sub_det_2" style='max-width:1348px;'>
				<legend>....</legend>
				<table id="widsjqGrid" style='max-width:1348px;'></table>
    			<div id="widsjqGridPager"></div>	
			</fieldset>
		  </div>
		  <div id="tabs-3" style="padding: 0px;">
		    <fieldset id="sub_det_2" style='max-width:1348px;'>
				<legend>....</legend>
				<div id="mapid"></div>
    			<div id="jqGridPager_1"></div>	
			</fieldset>
		  </div>
		  </div>
		</div>
		
		<div id="bottomdiv" style="display:block;">
			
			<div id="table_div"></div>		
			
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
$( function() {
    $( "#tabs" ).tabs();
  } );
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