<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
<script>var autoFlag = false;</script>



<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

 
<link rel="stylesheet" type="text/css" href="../../resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/index.css" />
<div class="wrapper">
<jsp:include page='nav.jsp' />
<div id="pageloadBox"></div>
<div id="container">
		<div id="topdiv" style="width: 100%;margin-bottom: 15px;">
			<div id="req_box">
			<table id="req_tab">
				<!--<tr>
					<td colspan="5">
						<label>PLMN Selection</label>
					</td>
				</tr>-->
				
			    <tr>
				<!--
					<td>
						<label id="countryLabel">Country</label>
						<select id="countrySelect"></select>
					</td>
					-->
					<td>
						<label id="allOperatorsLabel">List of Operators</label>
						<select id="allOperatorsSelect" multiple size="3">
						</select>
					</td>
					<td>
						<label id="allAntennaLabel">Antenna</label>
						<select id="addAllAntenna" multiple size="3">
						</select>
					</td>
					<td>
				       <input type="button" class="btn btn-default" id="findtheTechandCellId" onclick="getTechAndCellsFxn();" value="Show Cells" />
					</td>
					<td>
						<label id="TechAndCellsLabel">Cells</label>
						<select id="TechAndCellsSelect" multiple size="5" style=width:600px;>
						</select>
					</td>
					<td>
				       <input type="button" class="btn btn-default" id="generatePacket" value="GET SCANNED FILTERED CELLS" />
					</td>
					
					
					
					
					
				<!-- 	<td>
				     <a class="btn btn-default" href="operation.jsp">Close</a>
					</td>
				 -->	
				 
				 
				 
				</tr>
					<!--
					<td>
						<label id="plmnListLabel">PLMN List</label>
						<select id="plmnListSelect"></select>
					</td>
					
					<td>
						<label id="finalPlmnListLabel">Final PLMN List</label>
						<select id="finalPlmnListSelect"></select>
					</td>
					-->

				<!--<tr>
					<td colspan="5">
						<label>Scan Network</label>
					</td>
				</tr>
				<tr>			
					<td>
						<label>RSSI_THRESHOLD(dbm)</label>
						<input type="number" id="scan_rssi_threshold" name="scan_rssi_threshold" />
					</td>
					<td>
						<label>REPITITION_FREQ</label>
						<input type="number" id="scan_rep_freq" name="scan_rep_freq"/>
					</td>						
					<td>
						<label>Scan Duration</label>
						<input type="number" id="schedulerPeriodicity" class="input_type" name="periodicity"/>
					</td>
					<td>
						<button class="btn-match" id="startScan">Start Scan</button>
						<button class="btn-match" id="stopScan">Stop Scan</button>
					</td>
				</tr>	
				-->				



			</table>
			</div>
		</div>	
		
		
		<br/>
		
		
		<script type="text/javascript" src="../../resources/js/configupdate.js"></script>
		<script>
		
function updatePagerIcons(table) {
	var replacement =
	{
		'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
		'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
		'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
		'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
	   
		if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
	})
}

function updatePagerIcons(table) {
	var replacement =
	{
		'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
		'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
		'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
		'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
	   
		if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
	})
}
		
		

</script>



<style style="text/css">
		.ui-jqgrid .ui-jqgrid-view {
			z-index: auto;
		}
		.ui-jqgrid .ui-jqgrid-view, .ui-jqgrid .ui-paging-info, .ui-jqgrid .ui-pg-selbox, .ui-jqgrid .ui-pg-table {
			font-size: 13px;
		}
		.ui-jqgrid .ui-jqgrid-title {
			float: left;
			margin: 8px;
		}
		.ui-jqgrid .ui-jqgrid-title-rtl {
			float: right;
			margin: 8px;
		}
		.ui-jqgrid-view>.ui-jqgrid-titlebar {
			height: 40px;
			line-height: 24px;
			color: #FFF;
			background: #307ECC;
			padding: 0;
			font-size: 15px;
		}
		.ui-jqgrid tr.jqgrow.ui-row-rtl td:last-child {
			border-right: none;
			border-left: 1px solid #E1E1E1;
		}
		.ui-jqgrid .ui-jqgrid-hdiv {
			background-color: #EFF3F8;
			border: 1px solid #D3D3D3;
			border-width: 1px 0 0 1px;
			line-height: 15px;
			font-weight: 700;
			color: #777;
			text-shadow: none;
		}
		.ui-jqgrid .ui-jqgrid-htable thead {
			background-color: #EFF3F8;
		}
		.ui-jqgrid .ui-jqgrid-htable th span.ui-jqgrid-resize {
			height: 45px!important;
		}
		.ui-jqgrid .ui-jqgrid-htable th div {
			padding-top: 12px;
			padding-bottom: 12px;
			overflow: visible;
		}
		.ui-jqgrid-hdiv .ui-jqgrid-htable {
			border-top: 1px solid #E1E1E1;
		}
		.ui-jqgrid-titlebar {
			position: relative;
			top: 1px;
			z-index: 1;
		}
		.ui-jqgrid tr.jqgrow, .ui-jqgrid tr.ui-row-ltr, .ui-jqgrid tr.ui-row-rtl {
			border: none;
					height:auto;
					overflow:hidden;
					padding-right:4px;
					padding-top:2px;
					position:relative;
					vertical-align:text-top;
					white-space:normal !important;
		}
		.ui-jqgrid tr.ui-row-ltr td, .ui-jqgrid tr.ui-row-rtl td {
			border-bottom: 1px solid #E1E1E1;
			padding: 6px 4px;
			border-color: #E1E1E1 !important;
					white-space: normal !important;
					height:auto;
					vertical-align:text-top; 
					padding-top:2px;
		}
		.ui-jqgrid tr.ui-state-highlight.ui-row-ltr td {
			border-right-color: #C7D3A9;
		}
		.ui-jqgrid tr.ui-state-highlight.ui-row-rtl td {
			border-left-color: #C7D3A9;
		}
		.ui-jqgrid-btable .ui-widget-content.ui-priority-secondary {
			background-image: none;
			background-color: #F9F9F9;
			opacity: 1;
		}
		.ui-jqgrid-btable .ui-widget-content.ui-state-hover {
			background-image: none;
			background-color: #EFF4F7;
			opacity: 1;
		}
		.ui-jqgrid-btable .ui-widget-content.ui-state-highlight {
			background-color: #E4EFC9;
		}
		.ui-jqgrid .ui-jqgrid-pager {
			line-height: 15px;
			height: 55px;
			padding-top: 10px!important;
			padding-bottom: 10px!important;
			background-color: #EFF3F8!important;
			border-bottom: 1px solid #E1E1E1!important;
			border-top: 1px solid #E1E1E1!important;
		}
		.ui-jqgrid .ui-pg-input {
			font-size: inherit;
			width: 24px;
			height: 20px;
			line-height: 16px;
			-webkit-box-sizing: content-box;
			-moz-box-sizing: content-box;
			box-sizing: content-box;
			text-align: center;
			padding-top: 1px;
			padding-bottom: 1px;
		}
		.ui-jqgrid .ui-pg-selbox {
			display: block;
			height: 24px;
			width: 60px;
			margin: 0;
			padding: 1px;
			line-height: normal;
		}
		.ui-jqgrid .ui-pager-control {
			height: 50px;
			position: relative;
			padding-left: 9px;
			padding-right: 9px;
		}
		.ui-jqgrid .ui-jqgrid-toppager {
			height: auto!important;
			background-color: #EFF3F8;
			border-bottom: 1px solid #E1E1E1!important;
		}
		.ui-pg-table .navtable .ui-corner-all {
			border-radius: 0;
		}
		.ui-jqgrid .ui-pg-button .ui-separator {
			margin-left: 4px;
			margin-right: 4px;
			border-color: #C9D4DB;
		}
		.ui-jqgrid .ui-jqgrid-btable {
			border-left: 1px solid #E1E1E1;
		}
		.ui-jqgrid .ui-jqgrid-bdiv {
			border-top: 1px solid #E1E1E1;
			overflow-x: hidden;
		}
		.ui-jqgrid .loading {
			position: absolute;
			top: 45%;
			left: 45%;
			width: auto;
			height: auto;
			z-index: 111;
			padding: 6px;
			margin: 5px;
			text-align: center;
			font-weight: 700;
			font-size: 12px;
			background-color: #FFF;
			border: 2px solid #8EB8D1;
			color: #E2B018;
		}
		 
		.ui-jqgrid .ui-jqgrid-labels {
			border-bottom: none;
			background: repeat-x #F2F2F2;
			background-image: -webkit-linear-gradient(top, #F8F8F8 0, #ECECEC 100%);
			background-image: -o-linear-gradient(top, #F8F8F8 0, #ECECEC 100%);
			background-image: linear-gradient(to bottom, #F8F8F8 0, #ECECEC 100%);
			filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fff8f8f8',  endColorstr='#ffececec',  GradientType=0);
			padding: 0!important;
			border-left: 1px solid #E1E1E1!important;
		}
		.ui-jqgrid .ui-jqgrid-labels th {
			border-right: 1px solid #E1E1E1!important;
			text-align: left!important;
		}
		.ui-jqgrid-labels th[id*="_cb"]:first-child>div {
			padding-top: 0;
			text-align: center!important;
		}
		.ui-jqgrid-sortable {
			padding-left: 4px;
			font-size: 13px;
			color: #777;
			font-weight: 700;
		}
		.ui-jqgrid-sortable:hover {
			color: #547EA8;
		}
		th[aria-selected=true] {
			background-image: -webkit-linear-gradient(top, #EFF3F8 0, #E3E7ED 100%);
			background-image: -o-linear-gradient(top, #EFF3F8 0, #E3E7ED 100%);
			background-image: linear-gradient(to bottom, #EFF3F8 0, #E3E7ED 100%);
			background-repeat: repeat-x;
			filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffeff3f8',  endColorstr='#ffe3e7ed',  GradientType=0);
		}
		th[aria-selected=true] .ui-jqgrid-sortable {
			color: #307ECC;
		}
		.ui-jqgrid .ui-icon {
			text-indent: 0;
			color: #307ECC;
			float: none;
			right: 2px;
		}
		.rtl .ui-jqgrid .ui-icon {
			right: auto;
			left: 2px;
		}
		.ui-jqgrid .ui-icon.ui-state-disabled {
			color: #BBB;
		}
		.ui-jqgrid .ui-icon.ui-state-disabled:hover {
			padding: 0;
		}
		.ui-grid-ico-sort:before {
			display: inline;
			font-size: 12px;
		}
		.ui-icon-asc:before {
			content: "\f0d8"}
		.ui-pg-table>tbody>tr>.ui-pg-button>.ui-icon {
			display: inline-block;
			padding: 0;
			width: 24px;
			height: 24px;
			line-height: 22px;
			text-align: center;
			position: static;
			float: none;
			margin: 0 2px!important;
			color: grey;
			border: 1px solid #CCC;
			background-color: #FFF;
			border-radius: 100%}
		.ui-pg-table>tbody>tr>.ui-pg-button>.ui-icon:hover {
			color: #699AB5;
			border-color: #699AB5;
		}
		.ui-pg-table>tbody>tr>.ui-pg-button>.ui-icon:before {
			width: 20px;
			text-align: center;
			display: inline-block;
		}
		.ui-pg-table>tbody>tr>.ui-pg-button.ui-state-disabled .ui-icon {
			color: #B0B0B0;
			background-color: #F7F7F7;
			border-color: #DDD;
			-moz-transform: scale(.9);
			-webkit-transform: scale(.9);
			-o-transform: scale(.9);
			-ms-transform: scale(.9);
			transform: scale(.9);
		}
		.ui-jqgrid-btable input, .ui-jqgrid-btable select, .ui-jqgrid-btable textarea {
			padding: 2px;
			width: auto;
			max-width: 100%;
			margin-bottom: 0;
		}
		.ui-jqgrid-btable select {
			padding: 1px;
			height: 25px;
			line-height: 25px;
		}
		 
		.ui-pg-div .ui-icon {
			display: inline-block;
			width: 18px;
			float: none;
			position: static;
			text-align: center;
			opacity: .85;
			-webkit-transition: all .12s;
			-o-transition: all .12s;
			transition: all .12s;
			margin: 0 1px;
			vertical-align: middle;
			cursor: pointer;
			font-size: 17px;
		}
		.ui-pg-div .ui-icon:hover {
			-moz-transform: scale(1.2);
			-webkit-transform: scale(1.2);
			-o-transform: scale(1.2);
			-ms-transform: scale(1.2);
			transform: scale(1.2);
			opacity: 1;
			position: static;
			margin: 0 1px;
		}
		.ui-pg-div .ui-icon:before {
			font-family: FontAwesome;
			display: inline;
		}
		 
		 
		.ui-jqgrid .ui-pg-button:hover, .ui-jqgrid .ui-state-disabled:hover {
			padding: 0 1px;
					border-style: none !important;
		}
		.ui-jqgrid .ui-pg-table .ui-pg-button.ui-state-disabled:hover>.ui-pg-div>.ui-icon, .ui-jqgrid .ui-pg-table .ui-pg-button.ui-state-disabled:hover>.ui-separator {
			margin-left: 4px;
			margin-right: 4px;
		}
		@media only screen and (max-width:767px) {
			.ui-jqgrid .ui-jqgrid-pager {
			height: 90px;
		}
		.ui-jqgrid .ui-jqgrid-pager>.ui-pager-control {
			height: 85px;
			padding-top: 10px!important;
		}
		.ui-jqgrid .ui-jqgrid-pager>.ui-pager-control>.ui-pg-table>tbody>tr>td {
			vertical-align: top;
		}
		.ui-jqgrid .ui-jqgrid-pager>.ui-pager-control>.ui-pg-table>tbody>tr>td#grid-pager_center {
			width: 0!important;
			position: static;
		}
		.ui-jqgrid .ui-jqgrid-pager>.ui-pager-control>.ui-pg-table>tbody>tr>td#grid-pager_center>.ui-pg-table {
			margin: 36px auto 0;
			position: absolute;
			right: 0;
			left: 0;
			text-align: center;
		}
		.ui-jqgrid .ui-jqgrid-pager .navtable {
			height: auto;
		}
		}.dd
 
    </style>
		
		
		
		
		
		
		
		
<div id="bottom_div" style="width:100%;height:100%;display:table;">
<div id="req_box_bottom" style="height:75%;overflow:auto;">
<div style="float: left;width: 50%;">
		<table id="req_tab_bottom" border="1" style="text-align: center;min-width: 600px;"  border=1 style="margin: 0 auto 10px 10px;" class="table table-bordered">
			<thead id="req_tab_bottom_thead">
				<tr>
					<th style="text-align: center;" colspan="14">SCANNED FILTERED CELLS</th>					
				</tr>
			</thead>
			<tbody id="generatedPackets">				
			</tbody>
		</table>
		<img  id ="loading_screen" style="display: none; margin-left:15%" src="../../resources/images/Loading_icon.gif"/> 
		</div>
		<!--<label style="float: right;">Repitition Frequency</label>
		<input type="number" id="frequency" style="width: 100px;height: 20px;float: right;" />
		<label style="float: right;">Total Duration</label>
		<input type="number" id="duration" style="width: 100px;;height: 20px;float: right;" />
		<input class="btn btn-default" type="button" id="start" value="Start" style="float: right;" />
		<input class="btn btn-default" type="button" id="stop" value="Stop" style="float: right;" />-->
		<div id="bottom_right_div" style="float: right;width: 22%;">
		<table  class="table table-bordered">
		<thead id="operations_table_thead">
		<tr>
		<th colspan="2">
		Operations
		</th>
		</tr>
		</thead>
		<tbody id="operations_table_tbody" style="display: none;">
		<tr>
		<th>
		<label>No. of repetition</label>
		</th>
		<th>
		<input type="number" id="frequency" style="width: 100px;height: 20px;" value="1" />
		</th>
		</tr>
		<tr>
		<th>
		<label>Cell Active Time<span class="timeUnitClass">(sec)</span></label>
		</th>
		<th>
		<input type="number" id="duration" style="width: 100px;height: 20px;" value="60"  max ="900" min ="30"/>
		</th>
		</tr>
		<!--<label>Total Duration</label>
		<input type="number" id="duration" style="width: 100px;;height: 20px;" />-->
		<tr>
		<th colspan="2">
		<input class="btn btn-default" type="button" id="startConfig" value="Start" />
		<input class="btn btn-default" type="button" id="stopConfig" value="Stop" />
		</th>
		</tr>
		</tbody>
		</table>
		</div>
</div>
</div>	
	</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		<div class="modal fade" id="showNeighbours" role="dialog" aria-labelledby="myModalLabel" style="width:1360px !important;">
		<div class="modal-dialog" style="width: 1240px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="showNeighbourslLabel">NEIGHBOURS</h4>
				</div>
				<div class="modal-body">
				<div id="showNeighboursDiv" >			 
				<table border=1 style="margin: 0 auto 10px 10px;" class="table table-bordered">
				<thead>
				<tr>
				<td><label>SNo.</label></td>
				<td><label>TECH</label></td>
				<td><label>PLMN</label></td>
			    <td><label>LAC</label></td>	
			    <td><label>CELL</label></td>	
			    <td><label>ARFCN</label></td>
				<td><label>UARFCN</label></td>				
			    <td><label>BSIC</label></td>	
				<td><label>PSC</label></td>				
			    <td><label>RSSI</label></td>
				</tr>
				</thead>
				<tbody id="neighbourTableTbody">
			   </tbody>
				</table>
				</div>
				</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
<% String ip= "'"+request.getServerName()+"'"; %>
<script>
var ipAddress =<%= ip %>;
</script>
 	<script type="text/javascript" src="../../resources/js/configupdate.js"></script>
 	<style>
 		#container
 		{
 			height:93%;
 		}
 		
 		.timeUnitClass{
 			font-weight: 200;
 		}

 		.input_type
 		{
 			width: 116px;
    		margin-left: 3px;
    		border: 1px solid #3a5795;
    		border-radius: 2px;
		}
		
		#bottomTable
		{
			width: 80%;
    margin: 0 auto;
    padding: 10px;
    background: white;
    border: 4px solid #3a5795;
    border-radius: 8px;
		}
		#bottomTable td
		{
			width:50%;
		}
		
				#req_box
 		{
 			font-size: 16px;
		    font-weight: 500;
		    margin: 0 auto;
		    border: 1px solid #3a5795;
		    border-radius: 10px;
		    padding: 15px;
		    width:1330px;
		    background-color: rgba(255, 255, 255, 0.69);
 		}
 		#req_box select
 		{
 			width:150px;
 		}
 		#req_box td
 		{
 			padding:5px;
 		}
		 #req_box_bottom
 		{
 			font-size: 16px;
		    font-weight: 500;
			margin: 0 auto;
		    border: 1px solid #3a5795;
		    border-radius: 10px;
		    padding: 25px;
		    width:1330px;
			height:400px;
		    background-color: rgba(255, 255, 255, 0.69);
 		}
		
		#map_div {
			    
			    width: 100%;
			    height: 466px;
			    border: 3px solid #3a5795;
			    border-radius: 5px;
			}
			
		#loadingBox
 		{
 			font-size: 16px;
		    font-weight: 500;
		    margin: 0 auto;
		    border: 5px solid #3a5795;
		    border-radius: 9px;
		    padding: 30px;
		    width: 453px;
		    background-color: rgba(255, 255, 255, 0.69);
 		}
 		#opr_buttons button{font-size:10px;}
 		#opr_buttons_2 button{font-size:10px;}
		
		.operationButton{
		 background-color: #3a5795;
		color: white;
		border-radius: 5px;
		padding: 4px;
		margin-right: 3px;
		margin-left: 3px;
		border-color: #4365af;
		}
		body{
		overflow: hidden;
		}
		.table-tailored{
			margin: 0 auto 10px 10px;
			background: #FFF;
			border: 1px solid #ddd;
			margin-bottom: 20px;
			border-spacing: 0;
			border-collapse: collapse;
		}
		.table-tailored thead
		{
			background: #99ccfe;
			color: white;
		}
		
		.table-tailored tr ,.table-tailored th
		{
			font-size: 16px;
			font-weight: 500;
			padding: 0.5px;
			vertical-align: middle;
			border-bottom-width: 0;
			border-top: 1px solid #CCC5B9;
			border: 1px solid #ddd;
			line-height: 1.42857143;
		}
		.table-tailored  thead
		{
			font-size: 20px;
			font-weight: 900;
			padding: 0.5px;
			vertical-align: middle;
			border-bottom-width: 0;
			border-top: 1px solid #CCC5B9;
			border: 1px solid #ddd;
			line-height: 0.42857143;
		}
		.table-tailored td{
			padding: 1px;
		}
		th 
		{
		text-align: left;
		}
		.table-tailored > tbody > tr 
		{
			position: relative;
		}
		
		.showNeighbourClass
		{
			font-size: 12px;
			margin-top: -7px;
		 }
		/* .highlightPacket
		 {
			border: 3px solid #3b10e8;
		 }*/
		 .highlightPacket td:nth-child(2){
			font-weight: Override;
		 }
		<%= session.getAttribute("themeCss")%>
 	</style>	
</body>
</html>
