

	<jsp:include page='header.jsp' />
		<link rel="stylesheet" type="text/css" href="../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
	<script type="text/javascript" src="../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
		<link rel="stylesheet" type="text/css" href="../resources/css/cdrOperations.css" />
		<link rel="stylesheet" type="text/css" href="../resources/lib/jqgrid/css/ui.jqgrid.css" />
		
		<script type="text/javascript" src="../resources/lib/jqgrid/js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="../resources/lib/jqgrid/js/i18n/grid.locale-en.js"></script>
		
		<script type="text/javascript" src="../resources/js/cdrOperations.js"></script>
		<div id="container">
		
		
		<div id="operations">

		<table style="margin:0 auto;">
			<tr>
				<td>
					<fieldset style="height: 101px;">
						<legend>Add Operations</legend>
					<table class="opr" id="addoprations">						
						<tr>
							<td style="border-bottom: 0px solid black;">
								Name
							</td>
							<td style="border-bottom: 0px solid black;">
								<input type='text' name="oname" id="oname">
							</td>
							
						</tr>
						<tr>
							<td style="border-bottom: 0px solid black;">
								Location
							</td>
							<td style="border-bottom: 0px solid black;">
								<input type='text' name="oaddr" id="oaddr">
							</td>
							
						</tr>

						<tr>							
							<td style="border-bottom: 0px solid black;"><input type="button" value="Save" id=saveOpr></td>
						</tr>									
					</table>					
					</fieldset>
					<fieldset style="height: 101px;">
						<legend>Empty operations</legend>
					<table class="opr" id="addoprations">						
						<tr>							
							<td style="border-bottom: 0px solid black;"><input type="button" value="Clear Operations" id=deleteOpr></td>
						</tr>									
					</table>					
					</fieldset>	
				</td>
				<td>
					<fieldset style="height: 240px;">
						<legend>BTS</legend>
					<table class="opr" id="btsopr">
						<tr>
							<td>Operation Name</td>
							<td><select id="baoprname"></select></td>							
						</tr>
						<tr>
							<td>BTS</td>
							<td><select id="baopbts"></select></td>							
						</tr>
						<tr>
							<td style="border-bottom: 0px solid black;">
								Antena Type
							</td>
							<td style="border-bottom: 0px solid black;">
								<input type='text' name="batype" id="batype">
							</td>							
						</tr>
						<tr>
							<td style="border-bottom: 0px solid black;">
								Antena Gain
							</td>
							<td style="border-bottom: 0px solid black;">
								<input type='text' name="bagain" id="bagain">
							</td>							
						</tr>
						<tr>
							<td style="border-bottom: 0px solid black;">
								Antena Height
							</td>
							<td style="border-bottom: 0px solid black;">
								<input type='text' name="baheight" id="baheight">
							</td>							
						</tr>
						<tr>
							<td style="border-bottom: 0px solid black;">
								Antena Elevation
							</td>
							<td style="border-bottom: 0px solid black;">
								<input type='text' name="baelevation" id="baelevation">
							</td>							
						</tr>
						<tr>
							<td style="border-bottom: 0px solid black;">
								Antena Direction
							</td>
							<td style="border-bottom: 0px solid black;">
								<input type='text' name="badirection" id="badirection">
							</td>							
						</tr>
						<tr>
						<td style="border-bottom: 0px solid black;">
								Terrain Type
						</td>
						<td style="border-bottom: 0px solid black;">
							<SELECT id="ttype">
								<option value="Rural">Rural</option>
								<option value="Urban">Urban</option>
								<option value="Semi-Urban">Semi-Urban</option>
								<option value="Dense-Forest">Dense-Forest</option>
							</SELECT>
							</td>							
						</tr>
						<tr>							
							<td style="border-bottom: 0px solid black;"><input type="button" value="Save" id=savebaopr></td>
						</tr>										
					</table>
					</fieldset>		
				</td>
				
			</tr>
			
		</table>
		<table>
			<tr>
				<td>
					<table id="jqGrid" style='max-width:1348px;'></table>
    				<div id="jqGridPager"></div>
				</td>
			</tr>
		</table>				
		</div>
		</div>
	</body>
</html>