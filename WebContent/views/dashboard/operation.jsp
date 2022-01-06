<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
 <% String ip= "'"+request.getServerName()+"'"; %>
 
 <link rel="stylesheet" type="text/css" href="../../resources/css/bootstrap-toggle.min.css" />
 <script src="../../resources/js/bootstrap-toggle.min.js" ></script>
	
 <style>
#myAddPopup{
  background:while;
  width:400px;
  border-radius:6px;
  padding:0px 0px 10px 0px;
  border: #2980b9 4px solid; 
}

.input{
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



.btn:hover{
  /*background:#3594D2;*/  
}

#configurationMenu{
	font-weight:900;
	text-decoration:underline;
}

.addAntennaInput{
	width: 80px;
}

.editAntennaInput{
	width: 60px;
}

.editDisAntInput{
	width: 60px;
}

.table-target th{
	text-align: center;
}

.opr_button
{
	margin-right: 5px;
    margin-left: 5px;
    /* width: 10px; */
    padding-top: 1px;
    padding-bottom: 1px;
    padding-left: 5px;
    padding-right: 5px;
}

</style>
<script>
var ipAddress =<%= ip %>;
</script>
<div class="wrapper" style="background: white;">
<jsp:include page='nav.jsp' />
<div id="container">
<div id="message_box">
	<div id="myAddPopup" style="max-width: 350px;margin-top: 12%;margin-left: 40%;clear: left;display: none;">
		<div id="hiddenApplicationId" style="display: none;"></div>	   
	<br><br>
			     <div id="addOperationDiv" >			 
			   <table class="common_tables" style="text-align:center;">
			   <tr>
			   <td colspan="4"><h4 align="center" id="reportType" style="margin-top: -15px;margin-left: 10%;">Add Operation</h4></td>
			   </tr>
                 <tr> 
			      <td colspan="2"><label>Name</label></td>
			      <td colspan="2"><input type="text" id="oprName" class="input"></input></td> 
			     </tr>
		         <tr> 				 
			      <td colspan="2"><label>Note</label></td>
			      <td colspan="2"><input type="text" id="oprNote" class="input"></input></td>
			     </tr>
				  <tr>			  
				  <td colspan="4"><input class="btn btn-default" type="button" id="addOpr" value="Add"style="width: 100px;margin-left:68%;"></input></td>				  
				  </tr>				 
			 </table>
              </div>   
			  <div id="loadingBox" style="display: none;text-align:center;">
				<img src="../../resources/images/Loading_icon.gif"  style="margin-left: 0px;margin-top: -40px;width: 100%;" />	
			  </div>			  
	  </div> 
<div id="oprAndDeviceDetailsDiv" style="display: none;">
<div id="listStatusDiv" style="background:white; margin-top:12px; margin-left:23px;margin-right:25px;display: none;">	
	 				<table id="list_status" style="margin: 0 auto; border:white" class="table table-bordered">	
	<!-- 				<thead>
					<tr>
					<th style="text-align: center;">STATUS</th>
					</tr>
					</thead>-->
					<tbody>
                    <tr>
                    <td style="border-width: 0px;padding-left: 0px;padding-right: 0px;">
					<div>
					<div class="statusCard" style="margin: 0px;">
					  <div class="holder total_sufi">
					    <span class="largeHeading"><b>Total</b></span> 
					    <span class="dotClass" id="tot_bts">0</span> 
					  </div>
					</div>
					<div class="statusCard">
					  <div class="holder run_sufi">
					    <span class="largeHeading"><b>Running</b></span> 
					    <span class="dotClass" id="run_bts">0</span> 
					  </div>
					</div>
					<div class="statusCard">
					  <div class="holder wait_sufi">
					    <span class="largeHeading"><b>Waiting</b></span> 
					    <span class="dotClass" id="wait_bts">0</span> 
					  </div>
					</div>
					<div class="statusCard">
					  <div class="holder nor_sufi">
					    <span class="largeHeading"><b>Not Reachable</b></span> 
					    <span class="dotClass" id="nor_bts">0</span> 
					  </div>
					</div>
					<div class="statusCard">
					  <div class="holder down_sufi">
					    <span class="largeHeading"><b>Down</b></span> 
					    <span class="dotClass" id="down_bts">0</span> 
					  </div>
					</div>	
			</div>					
					</td>
			</tr>
			</tbody>			
			</table>
			</div>

	  <div id="operationAndDevicesDiv">
	  		<div id="left_box_section_2" style="border:white">
						<table border=1 id="list_operation" style="margin: 0 auto; border:white" class="table table-bordered">
						<thead>
						<tr>
						<th colspan="5" style="text-align: center;">Current Operation
						<span class="spanStyle ti-arrow-circle-up" id="currentOperationArrowSpan">
						<span class="arrowClass" style="display:none;">-</span>
						</span>
						</th>
						</tr>
						<tr class="spanStyleThead" style="background-color: #f5f5f5;color: rgb(51,51,51);">
							<th>Name</th>
							<th>Note</th>
							<th>Time</th>
							<th>GPS Accuracy</th>
							<th></th>
						</tr>
						</thead>
						<tbody id="list_operation_body">	
						</tbody>
					</table>
				</div>
				</div>

			<% if(session.getAttribute("role").toString().equalsIgnoreCase("superadmin")) {%>
				   
		     <div id="addSufiDiv" style="background:white; margin-top:12px; margin-left:23px;margin-right:25px;">	
						<table border=1 id="add_sufi_tab" class="table table-bordered" style="margin: 0 auto;">
						<thead>
						<tr>
						<th colspan="7" style="text-align: center;">Add Device
						<span class="spanStyle ti-arrow-circle-down" id="addDeviceArrowSpan">
							<span class="arrowClass" style="display:none;">+</span>
						</span>
						</th>
						</tr>
						</thead>
						<tbody class="spanStyleTbody">
						<tr id="addDeviceDetailsTr">
							<td colspan=1>Use : 
								<select style="width: 120px;" id="useType" onchange="openAddDeviceModal()">
								</select>
							</td>
							<td colspan=1>IP : <input type='text' style="width: 120px;" value='' id='deviceIp' /></td>
							<td colspan=1>HW Capability : 
								<select style="width: 220px;" id="hwCapabilityType">
								</select>
							</td>
							<td colspan=1>System Manager IP : 
								<input type='text' style="width: 120px;" value='' id='SystemManagerIpEntered' />
								
							</td>
							<td id="tr_device_pa_power" style="display:none" colspan=1 >PA Power : <input type='text' style="width: 50px;" value='' id='device_pa_power'/></td>
							<td id="tr_device_pa_gain" style="display:none" colspan=1>PA GAIN : <input type='text' style="width: 50px;" value='' id='device_pa_gain'/></td>
							<!--<td colspan=1 id="antennaProfileTd"> 
								<input type="text" value="Choose" style="width: 80px;background: white;border: none;" id="antennaProfileTextName2g" disabled />
								<input type="button"  class="btn btn-default"  value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile('2g')" />
								<input type="text" value="" id="antennaProfileTextId2g" style="display:none;" disabled />
							</td>-->
							<td>
							<button id="addDeviceButton" class="btn btn-default" onclick="addDevice()">Add</button>
							<div id="addDeviceLoadingBox" style="display: none;text-align: center;">
								<img  style="height: 30px;width: 50px;" src="../../resources/images/p_load.gif" />
							</div>
							</td>
						</tr>
						</tbody>
					</table>
					</div>
					
					<% }%>
					
					
					
					<br>
						<div class="modal fade" id="bmsmodal" tabindex="-1" role="dialog" aria-hidden="true">
					  		<div class="modal-dialog" style="right: 13%;"> role="document">
					   			 <div class="modal-content" id="bmsconfigModalResize" style="width: 1000px;  height: 353px;">
					    		  <div class="modal-header">
							        <h5 class="modal-title" id="bmsconfigurationTitle" style="height: 10px;	font-size: 18px;">BMS Configuration</h5>
							        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
							          <span aria-hidden="true">&times;</span>
					       		    </button>
					      		  </div>
								     
								     
								      <div class="modal-body">
								      

                                         <input type="hidden"  id="ipBms" />
								        <table id="action_table" class="table ">
										<thead>
										<th style="height: 10px;	font-size: 23px;">Parameter</th>
										<th></th>
										
										</thead>
										<tbody>
										<tr>
											<td style="height: 10px;	font-size: 15px;" >Periodicity (in mins) (5 min to 120 mins)</td>
											<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text" /></td>
											<td style="height: 10px; 	font-size: 15px;"><button class="btn btn-default ab" data-id="5" data-cmd="PR"  onclick="sendBtnData(this)">Save</button></td>
										</tr>
										<tr >
										<td style="height: 10px;	font-size: 15px;">System time</td>
									
										<td id="bms_sys_time">
										
										</td>
										
										<td></td>
										</tr>
										<tr>
											<td colspan=3>
											<button class="btn btn-default ab" data-name="System Reset" data-id="4" 	data-cmd="RESET"  	onclick="sendBtnData(this)">System Reset</button>
											<button class="btn btn-default ab" data-name="Power Reset" id="load_on"   data-cmd="LON"  data-id="1"  onclick="sendBtnData(this)">Power Reset</button>
											<button class="btn btn-default ab" data-name="Sync" data-id="10" 	data-cmd="SYNCH"  	onclick="sendBtnData(this)">Sync</button>
											<button class="btn btn-default ab" data-name="Status" id ="bmsModalStatusTableShow" data-id="6" 	data-cmd="GET_STATUS"  onclick="sendBtnData(this)">Status</button>
											<button class="btn btn-default ab" data-id="11" data-name="Time Sync"	data-cmd="SET_TIME"  onclick="sendBtnData(this)">Time Sync</button>
											</td>
										</tr>
										</tbody>
										</table>
										<div id="status_timeClick"></div>
										<br/>
										<table border=1 id="bms_modal_status_table" style="display:none;">
											<tbody>
											<tr><td>Cell Voltage 1</td><td id="stat_1">NA</td><td>Cell Voltage 10</td><td id="stat_10">NA</td></tr>
											<tr><td>Cell Voltage 2</td><td id="stat_2">NA</td><td>Cell Voltage 11</td><td id="stat_11">NA</td></tr>
											<tr><td>Cell Voltage 3</td><td id="stat_3">NA</td><td>Cell Voltage 12</td><td id="stat_12">NA</td></tr>
											<tr><td>Cell Voltage 4</td><td id="stat_4">NA</td><td>Cell Voltage 13</td><td id="stat_13">NA</td></tr>
											<tr><td>Cell Voltage 5</td><td id="stat_5">NA</td><td>Cell Voltage 14</td><td id="stat_14">NA</td></tr>
											<tr><td>Cell Voltage 6</td><td id="stat_6">NA</td><td>Total Voltage(V)</td><td id="stat_15">NA</td></tr>
											<tr><td>Cell Voltage 7</td><td id="stat_7">NA</td><td>Total Current(A)</td><td id="stat_16">NA</td></tr>
											<tr><td>Cell Voltage 8</td><td id="stat_8">NA</td><td>SOC(%)         </td><td id="stat_17">NA</td></tr>
											<tr><td>Cell Voltage 9</td><td id="stat_9">NA</td><td>Temperature(C) </td><td id="stat_18">NA</td></tr>
											
											</tbody>
										</table>
								      </div>


								      </div>
								      
					    		</div>
						  </div>
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  <div class="modal fade" id="JammerModal" style="#right: 10%;" tabindex="-1" role="dialog" aria-hidden="true">
					  		<div class="modal-dialog" style="right: 30%;"> role="document">
					   			 <div class="modal-content" id="JammerconfigModalResize" style="width: 1468px;  ">
					    		  <div class="modal-header">
							        <h5 class="modal-title" id="JammerconfigurationTitle" style="height: 10px;	font-size: 18px;">Spoiler Configuration</h5>
							        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
							          <span aria-hidden="true ">&times;</span>
					       		    </button>
					      		  </div>
								     
								     
								      <div class="modal-body">
								      

                                         <input type="hidden"  id="ipJammer" />
								       <!--  <table id="jammer_table" class="table "> -->
								       <table id="jammer_table" class="table "> 
											<thead>
												<th style="height: 10px;	font-size: 18px;">Select</th>
												<th style="height: 10px;	font-size: 18px;">Start Freq(MHz)</th>
												<th style="height: 10px;	font-size: 18px;">Stop Freq(MHz)</th>
												<th style="height: 10px;	font-size: 18px;">RAM Segment Start Address(0..1023)</th>
												<th style="height: 10px;	font-size: 18px;">RAM Segment End Address(0..1023)</th>
												<th style="height: 10px;	font-size: 18px;">DwTimeCtr(1..65535)</th>
												<th style="height: 10px;	font-size: 18px;">RAMOpMode(0..4)</th>
												<th style="height: 10px;	font-size: 18px;">DwBit</th>
											
											</thead>
											<tbody>
												
												<tr>
												
												   <td><input type="checkbox" id="FreqJammerSend1" ></td>
												
													<!-- <td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text1" /></td> -->
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="Pr_text11" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="Pr_text12" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="AddrStart_text1" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="AddrStop_text1" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="dwTimeCtr1" />
													</td>
														<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="ramOpMode1" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<!-- <input type="text" id="dwBit1" /> -->
														<label class="switch">
															  <input type="checkbox" id="dwBit1" checked>
															  <span class="slider round"></span>
														</label>
													</td>
													
													<!-- <td><input type="range" min="0" max="5" step="1"  class="form-range" id="customRange1"></td>-->
												</tr>
												<tr>
												<td><input type="checkbox" id="FreqJammerSend2" ></td>
												<!--	<td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text2" /></td> -->
													
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text21" /></td>
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text22" /></td>
													
														<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="AddrStart_text2" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="AddrStop_text2" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="dwTimeCtr2" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="ramOpMode2" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<!-- <input type="text" id="dwBit2" /> -->
														
														<label class="switch">
															  <input type="checkbox" id="dwBit2" checked>
															  <span class="slider round"></span>
														</label>
													</td>
													
													<!-- <td><input type="range" class="form-range" id="customRange2"></td>   -->
												</tr>
												<tr>
												  <td><input type="checkbox" id="FreqJammerSend3" ></td> 
												<!--	<td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text3" /></td> -->
													
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text31" /></td>
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text32" /></td>
													
														<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="AddrStart_text3" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="AddrStop_text3" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="dwTimeCtr3" />
													</td>
													<!-- <td><input type="range" class="form-range" id="customRange3"></td>   -->
													
														<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="ramOpMode3" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<!-- <input type="text" id="dwBit3" /> -->
														<label class="switch">
															  <input type="checkbox" id="dwBit3" checked>
															  <span class="slider round"></span>
														</label>
													</td>
												</tr>
												<tr>
												  <td><input type="checkbox" id="FreqJammerSend4" ></td>
												<!--	<td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text4" /></td> -->
													
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text41" /></td>
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text42" /></td>
													<!-- <td><input type="range" class="form-range" id="customRange4"></td>   -->
													
														<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="AddrStart_text4" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="AddrStop_text4" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="dwTimeCtr4" />
													</td>
														<td style="height: 10px;	font-size: 15px;" >
														<input type="text" id="ramOpMode4" />
													</td>
													<td style="height: 10px;	font-size: 15px;" >
													<!-- 	<input type="text" id="dwBit4" /> -->
														<label class="switch">
														  <input type="checkbox" id="dwBit4" checked>
														  <span class="slider round"></span>
													</label>
													</td>
													
												</tr>
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												
												<!-- 
												
												
												
												<tr>
												  <td><input type="checkbox" id="FreqJammerSend5" ></td>
												<!--	<td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text5" /></td> 
													
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text51" /></td>
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text52" /></td>
													<!-- <td><input type="range" class="form-range" id="customRange5"></td>   
												</tr>
												<tr>
												  <td><input type="checkbox" id="FreqJammerSend6" ></td>
												<!--    <td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text6" /></td> 
													
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text61" /></td>
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text62" /></td>
													<!-- <td><input type="range" class="form-range" id="customRange6"></td>   
												</tr>
												<tr>
												  <td><input type="checkbox" id="FreqJammerSend7" ></td>
												<!--	<td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text7" /></td> 
													
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text71" /></td>
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text72" /></td>
													<!-- <td><input type="range" class="form-range" id="customRange7"></td>   
												</tr>
												<tr>
												  <td><input type="checkbox" id="FreqJammerSend8" ></td>
													<!--<td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text8" /></td> 
													
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text81" /></td>
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text82" /></td>
													<!-- <td><input type="range" class="form-range" id="customRange8"></td>   
												</tr>
												<tr>
												<td><input type="checkbox" id="FreqJammerSend9" ></td>
												   <!-- <td style="height: 10px;	font-size: 15px;" ><input type="text" id="band_text9" /></td>
													
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text91" /></td>
													<td style="height: 10px;	font-size: 15px;" ><input type="text" id="Pr_text92" /></td>
													<!-- <td><input type="range" class="form-range" id="customRange9"></td> 
												</tr>

												
												-->
												
												<tr>
													<br>
														<td >
														</td>
														<td style="height: 10px;	font-size: 15px;" >
															TxPower(20..40dBm)
															<input type="text" id="power_sendJammer1" />

														</td>
														<td style="height: 10px;	font-size: 15px;" >
																MaxLoIfOffset(0..160)MHz
															<input type="text" id="loIfOffsetKhz" />

														</td>

													<td colspan=6	>
														<button class="btn btn-default ab"  data-height="75" data-width="100"  style="float: right;" data-name="Jammer-Save"	 onclick="sendJammerData()">Apply</button>
													</td>
													
												</tr>
												<tr>
													<td>
														</td>														
														<td style="height: 10px;	font-size: 15px;" >
														 Spoiler Mode
														<input type="checkbox" checked onchange="JammerOnOff('+$("#ipJammer").val()+')"  data-toggle="toggle" id="JammerOnOffToggle">
													</td>	
												
												</tr>
												</tbody>
										</table>
										
								      </div>


								      </div>
								      
					    		</div>
						  </div>
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
						  
					   </div>
					  
					
					<br>
					
						<div style="/*max-height: 200px;overflow-y: auto;overflow-x:hidden;*/margin-left:23px;margin-right:25px; background:white">
							<table border=1 id="list_table" style="margin: 0 auto;" class="table table-bordered">
							<thead>
							<tr>
							<th colspan="9" style="text-align: center;">Existing devices</th>    
							</tr>
							<tr>
								<th>Use</th>
								<th>Node IP</th>
								<th>Node Status</th>
								<th>Hardware Capability</th>
								<th>Software Version</th>
								<th style="display:none;">Release</th>
								<th>Network Configuration</th>
								
								<th>System Manager</th>
								<th colspan="2"></th>
							</tr>
							</thead>
							<tbody id="list_table_body">
								
							</tbody>
							</table>
						</div>
				</div>
				</div>
				<!--    <div id="addDeviceLoadingBox" style="display: none;text-align: center;">
				<img  style="width: 100%;height: 100%;" src="../../resources/images/p_load.gif" />
				</div> -->
				</div>
				</div>
				
		<div class="modal fade" id="addnewOperationModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: auto;margin: auto;">
			<div class="modal-content" >
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="addOperationLabel">Add Operation</h4>
				</div>
				<div class="modal-body">
						    <div id="addNewOprDiv">			 
				<table class="common_tables" style="text-align:left;">
					<tr>
						<td colspan="1">
							<label>Name</label>
						</td>
						<td colspan="1">
						<input type="text" id="newOprName" ></input>
						</td>					
						<td colspan="1">
							<label>Note</label>
						</td>
						<td colspan="1">
							<input type="text" id="newOprNote" ></input>
						</td>
					   <td colspan=1><label>Operation Type</label></td>
						<td colspan=1>
							<select id="newOprType" name="newOprType" style="width: 125px;">
							<option value="1">Scheduler</option>
							<option value="2">Continuous Mode</option>
							<!--<option value="3">External Trigger</option>-->
							</select>
						</td>
					<td><label>Tracking Periodicity(in mins)(in case of scheduler)</label></td>
					<td><input type="text" id="newDuration" ></input></td>
					</tr>
					<tr>
					<!--	
						<td colspan=1><label>GSM BAND</label></td>
						<td colspan=1>
							<select id="exe_scan_2g_tech" name="exe_scan_2g_tech" multiple size=4>
								
							</select>
						</td>
						<td colspan=1><label>UMTS BAND</label></td>
						<td colspan=1>
							<select id="exe_scan_umts_tech" name="exe_scan_2g_tech" multiple size=4>
								
							</select>
						</td>
						<td colspan=1><label>LTE BAND</label></td>
						<td colspan=1>
							<select id="exe_scan_lte_tech" name="exe_scan_2g_tech" multiple size=4>
								
							</select>
						</td>
						-->
						<td colspan=1><label>Operators</label></td>
						<td colspan=1>
							<select id="newOprPlmn" name="newOprPlmn" multiple size=4>
								
							</select>
						</td>
						<td ><button class="btn btn-default"  id="addNewOpr">Add</button></td>
						</tr>
						<tr>
						<!--  <td colspan="1">
						<label>Additional PLMN(comma separated)</label>
						</td>
						<td colspan="1">
						<input type="text" id="newOprAdditionalPlmn" class="input"></input>
						</td>-->
						
						<!--
						<td colspan="1">
						<label>Coverage Distance</label>
						</td>
						<td colspan="1">
						
						<select id="c_distance">
							<option value="1">1 Km</option>
							<option value="2">2 Km</option>
							<option value="3">3 Km</option>
							<option value="4">4 Km</option>
							<option value="5">5 Km</option>
							<option value="6">6 Km</option>
							<option value="7">7 Km</option>
							<option value="8">8 Km</option>
							<option value="9">9 Km</option>
							<option value="10">10 Km</option>
						</select>
						</td>
						-->
						<!--
						<td colspan="2">
						<label>Use Existing Scanned Data</label><input type="checkbox" style="width: 22px;" id="old_scanned_data"></input>
						</td>
						-->
						<!--  <td colspan="1">
						<label>PA Power</label>
						</td>
						<td colspan="1">
						<input type="text" id="pa_power" class="input"></input>
						</td>
						<td colspan="1">
						<label>PA Gain</label>
						</td>
						<td colspan="1">
						<input type="text" id="pa_gain" class="input"></input>
						</td>-->
						</tr>
						
									  
				</table>
            </div> 
					  <div id="oprAndDeviceDetailsDivo" style="margin-top: 20px;">
		<div style="/*max-height: 200px;overflow-y: auto;overflow-x:hidden;margin-left:23px;margin-right:25px;*/ background:white">
		<table border=1 id="list_table_operations" style="margin: 0 auto;" class="table table-bordered">
		<thead>
		<tr>
			<th colspan="11" style="text-align: center;color: black;">Operation History</th>    
		</tr>
		<tr>
			<th>Name</th>
			<th>Note</th>
			<th>Type</th>
			<th>Operator</th>
			<th>Periodicity(Mins)</th>
			<th>Distance(Kms)</th>
			<th>Status</th>
			<th>Start Date</th>
			<th>Stop Date</th>
			<th>Action<span id="oprAction" style="float:right;"></span></th>
			</tr>
		</thead>
		<tbody id="list_table_operations_body">
								
		</tbody>
		</table>
						</div>
				</div> 
				</div>
				<div class="modal-footer">
									<button type="button" class="btn btn-default"
										data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
			<div class="modal fade" id="setAntennaProfileModal" role="dialog" aria-labelledby="myModalLabel" style="width:1360px !important;">
		<div class="modal-dialog" style="width: 1320px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Antenna Profiles</h4>
				</div>
				<div class="modal-body">
<!-- 				<div id="addAntennaProfileDiv" >			 
				<table border=1 id="addAntennaProfileTable" style="margin: 0 auto 10px 10px;" class="table table-bordered" style="width: 98%;">
				<thead class="antenna_label_class">
				<tr>
				<td><label class="labelClass">Scanning</label></td>
				<td><label class="labelClass">Tracking</label></td>
				<td><label class="labelClass">Name</label></td> 
				<td><label class="labelClass">Type</label></td>
			    <td><label class="labelClass">TX Power<span class="parameter_units_class">(watt)</span></label></td>	
			    <td><label class="labelClass">Frequency<span class="parameter_units_class">(MHz)</span></label></td>	
			    <td><label class="labelClass">Gain<span class="parameter_units_class">(dBi)</span></label></td>	
			    <td><label class="labelClass">Height<span class="parameter_units_class">(meter)</span></label></td>	
			    <td><label class="labelClass">H-BW<span class="parameter_units_class">(degree)</span></label></td>	
			    <td><label class="labelClass">V-BW<span class="parameter_units_class">(degree)</span></label></td>
			    <td><label class="labelClass">Terrain</label></td>
			    <td><label class="labelClass">Tilt<span class="parameter_units_class">(degree)</span></label></td>	
			    <td><label class="labelClass">Azimuth<span class="parameter_units_class">(degree)</span></label></td>
				<td></td>
				</tr>
				</thead>
				<tbody>
               <tr> 
			   <td><input type="checkbox" name="scanningAntennaCheck" id="addAntennaScanning"></input></td>
			   <td><input type="checkbox" name="scanningAntennaCheck" id="addAntennaTracking"></input></td>
				<td><input type="text" id="addAntennaName" class="addAntennaInput"></input></td> 
				<td><select id="addAntennaType" class="addAntennaInput"></select></td>
			    <td><input type="text" id="addAntennaTxPower" class="addAntennaInput"></input></td>		
			    <td><select id="addAntennaBand" class="addAntennaInput"><option value="850">850</option><option value="900">900</option><option value="1800">1800</option><option value="2100">2100</option></select></td>	
			    <td><input type="text" id="addAntennaGain" class="addAntennaInput"></input></td>	
			    <td><input type="text" id="addAntennaElevation" class="addAntennaInput"></input></td>	
			    <td><input type="text" id="addAntennaHBW" class="addAntennaInput"></input></td>	
			    <td><input type="text" id="addAntennaVBW" class="addAntennaInput"></input></td>	
			    <td><select id="addAntennaTerrain" class="addAntennaInput"><option value="Rural">Rural</option><option value="Urban">Urban</option><option value="SubUrban">SubUrban</option><option value="Hilly">Hilly</option><option value="Desert">Desert</option></select></td>
			    <td><select id="ptzSelectionStatus" class="addAntennaInput"><option value="yes">Yes</option><option value="no" selected>No</option></select></td>
			    <td><input type="text" id="addAntennaTilt" class="addAntennaInput"></input></td>	
			    <td><input type="text" id="addAntennaAzimuth" class="addAntennaInput"></input></td>
				<td><input  type="button"  class="btn btn-default" value="Add" id="addProfileButton" onclick="addAntennaProfile()" /></td>				
			   </tr>
			   </tbody>
				</table>
				</div> -->
				<div>
				<label>Sector Antenna Type</label>
				<select id="antennaTypeRotate" name="antennaTypeRotate">
				<option value="1">Fixed</option>
				<option value="2">Rotating</option>
				</select>
				<label>S1 North Offset
				<span class="parameter_units_class">(degree)</span>
				</label>
				<input type="text" id="sectorAntennaAngleOffset" class="addAntennaInput"></input>
				<span class="parameter_units_class" id="antennaNote">(In case Sector Antenna has been selected)</span>
				<label>S1 Angle</label>
				<input type="text" id="s1Angle" class="addAntennaInput"></input>
				<button class="btn btn-default"  id="getS1Angle" onclick="getCurrentAndOffsetAngle()">Get North Offset</button>
				
				</div>
				<br>
				<div id="displayAntennaProfileDiv">
				<table border=1 id="displayAntennaProfileTable" style="font-size:10px;margin: 0 auto 10px 10px;" class="table table-bordered" style="width: 98%;">
				<thead class="antenna_label_class">
				<tr>
				<th><label class="labelClass">Scanning</label></th>
				<th><label class="labelClass">Tracking</label></th>
				<th><label class="labelClass">Name</label></th>
				<th><label class="labelClass">Type</label></th>
				<th><label class="labelClass">Angle</label></th>
				<th><label class="labelClass">TX Power<span class="parameter_units_class">(watt)</span></label></th>
				<th><label class="labelClass">Frequency<span class="parameter_units_class">(MHz)</span></label></th>
				<th><label class="labelClass">Gain<span class="parameter_units_class">(dBi)</span></label></th>
				<th><label class="labelClass">Height<span class="parameter_units_class">(meter)</span></label></th>
				<th><label class="labelClass">H-BW<span class="parameter_units_class">(degree)</span></label></th>
				<th><label class="labelClass">V-BW<span class="parameter_units_class">(degree)</span></label></th>
				<th><label class="labelClass">Tilt<span class="parameter_units_class">(degree)</span></label></th>
				<th><label class="labelClass">Azimuth<span class="parameter_units_class">(degree)</span></label></th>
				<th><label class="labelClass">Terrain</label></th>
				<th><label class="labelClass">Edit</label>&nbsp;&nbsp;<input type="checkbox" id="editAllAntennaCheck"></input></th>				
				</tr>
				</thead>

				<tbody id="displayAntennaProfileTable_body"></tbody>
				</table>
				</div>
				</div>
				<div class="modal-footer">
				<!-- <button class="btn btn-default" id="selectAntennaProfile" onclick="selectAntennaProfile()">Select</button> -->
				<button class="btn btn-default" id="updateProfileButton" onclick="updateAntennaProfile()">Update</button>
				<!-- <button class="btn btn-default"  id="selectAntennaProfile" onclick="deleteAntennaProfile()">Delete</button> -->
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
				<div class="modal fade" id="addTargetModal" role="dialog" aria-labelledby="myModalLabel" style="width:1360px !important;">
		<div class="modal-dialog" style="width: 900px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Targets</h4>
				</div>
				<div class="modal-body">
				<div id="addTargetDiv" >			 
				<table border=1 id="addTargetTable" style="font-size:10px;margin: 0 auto 10px auto; text-align:center;" class="table table-default table-bordered table-target">
				<thead>
				<tr>
				<th><label>NAME</label></th>
				<th><label>IMSI</label></th> 
			    <th><label>IMEI</label></th>	
				<th><label>TYPE</label></th>
				<th></th>
				</tr>
				</thead>
				<tbody>
               <tr> 
                <td><input type="text" id="targetName"></input></td>
			    <td><input type="text" id="targetImsi"></input></td> 
			    <td><input type="text" id="targetImei"></input></td>
				<td>
				<select id="targetType">
				<option value="Whitelist">Whitelist</option>
				<option value="Blacklist">Blacklist</option>
				</select>
				</td>				
				<td><input   class="btn btn-default"  type="button" value="Add Target" id="addTargetButton" onclick="addTarget()" /></td>				
			   </tr>
			   </tbody>
				</table>
				</div>
				<div id="displayTargetDiv">
				<table border=1 id="displayTargetTable" style="margin: 0 auto 10px auto;text-align:center;" class="table table-default table-bordered table-target">
				<thead>
				<tr>
				<th><input type ="checkbox" id ="checktarget" onclick="checkTarget()"/></th>	
				<th><label>NAME</label></th>
				<th><label>IMSI</label></th>
				<th><label>IMEI</label></th>
				<th><label>TYPE</label></th>
				</tr>
				</thead>
				<tbody id="displayTargetTable_body"></tbody>
				</table>
				</div>
				</div>
				<div class="modal-footer">
				<form name="targetAdditionFile" style="margin-right:50%;">
					<input type="file"  name="target_file" accept=".csv" onchange="checkExt()" style="display: inline-block;" class="btn btn-default targer_file" id="target_file"/>
					<input type="button" class="btn btn-default targer_file" style="display: inline-block; " value="Upload File"  onclick="addTargetByFile()"/>
				<a href="../../resources/SampleFiles/addTarget.csv"  download>
				  <img src="../../resources/images/textFile.png"  width="25" height="25" style="display: inline-block; " alt="sampleFile" >
			      <h6 style="display: inline-block;font-size:10px;margin-right: -16%;" alt="sampleFile">Sample File</h6>
				</a>
			     </form>
				<button class="btn btn-default"  id="deleteTarget" onclick="deleteTarget()" style="">Delete Target</button>
				<button  class="btn btn-default"  data-dismiss="modal" >Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
	
	    <div class="modal fade" id="purgeDataModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 27%;">
			<div class="modal-content">
				<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="purgeDataModalLabel">Purge Data</h4>
				</div>
				<div class="modal-body">
				<div id="purgeDataDiv">			 
				<table class="common_tables" style="margin: 0 auto;">
				<tr>
				<td><label style=" font-size:15px">Data Type</label></td>
				<td><select id="dataTypeSelect" style="font-weight: 500px;"></select></td>
				</tr>
				<tr>
				</tr>
				<tr>
				</tr>
				<tr>
				<td style="border-bottom: 0px solid black;" colspan="4"></td>
				</tr>							
				</table>
				</div>
				</div>
				<div class="modal-footer">
				<button class="btn btn-default"  id="truncateDb">Purge</button>
				<button  class="btn btn-default"  data-dismiss="modal" >Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
		    <div class="modal fade" id="sysConfigModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 40%;">
			<div class="modal-content">
				<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="sysConfigModalLabel">System Configurations</h4>
				</div>
				<div class="modal-body">			 
				<table class="common_tables" id="systemProperties" style="margin: 0 auto;">
				<tr>
				<td><label style=" font-size:15px" id="trackTimeLabel">Tracking Time(in ms)</label></td>
				<td><input type="text" id="trackTimeConfig" style="font-weight: 500px;width: 150px;"></input></td>
				<td><button class="btn btn-default"  id="updateTrackTimeConfig">Update</button></td>
				</tr>
				<tr>
				<td><label style=" font-size:15px" id="scannerPeriodicityLabel">Scanner Periodicity(in sec)</label></td>
				<td><input type="text" id="scannerPeriodicityConfig" style="font-weight: 500px;width: 150px;"></input></td>
				<td><button class="btn btn-default"  id="updateScannerPeriodicityConfig">Update</button></td>
				</tr>
				<tr>
				<td><label style=" font-size:15px" id="queueStatusLabel">HUMMER Trigger</label></td>
				<td>
				<select type="text" id="queueStatusConfig" style="font-weight: 500px;width: 150px;">
				<option value="Enable">Enable</option>
				<option value="Disable">Disable</option>
				</select>
				</td>
				<td><button class="btn btn-default"  id="updateQueueStatusConfig">Update</button></td>
				</tr>
				<tr>
				<td><label style=" font-size:15px" id="queueStatusLabel">OXFAM Trigger</label></td>
				<td>
				<select type="text" id="queueStatusConfig" style="font-weight: 500px;width: 150px;">
				<option value="Enable">Enable</option>
				<option value="Disable">Disable</option>
				</select>
				</td>
				<td><button class="btn btn-default"  id="updateQueueStatusConfig">Update</button></td>
				</tr>
				<!--
				<tr>
				<td><label style=" font-size:15px">Latitude</label></td>
				<td><input type="text" id="latConfig" style="font-weight: 500px;" value="28.485652"></input></td>
				</tr>
				<tr>
				<td><label style=" font-size:15px">Longitude</label></td>
				<td><input type="text" id="lonConfig" style="font-weight: 500px;" value="77.075046"></input></td>
				</tr>
				<tr>
				<td><label style=" font-size:15px">Timezone</label></td>
				<td><input type="text" id="timezoneConfig" style="font-weight: 500px;" value="IST"></input></td>
				</tr>
				-->
				<tr>
				</tr>
				<tr>
				</tr>
				<tr>
				<td style="border-bottom: 0px solid black;" colspan="4"></td>
				</tr>							
				</table>
				</div>
				<div class="modal-footer">
				<button  class="btn btn-default"  data-dismiss="modal" >Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->

				<div class="modal fade" id="add_plmn" role="dialog" aria-labelledby="myModalLabel" style="width:1360px !important;">
		<div class="modal-dialog" style="width: 1240px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Cell Management</h4>
				</div>
				<div class="modal-body">
				<div id="addCellDiv" >			 
				<table border=1 id="addCellTable" style="margin: 0 auto 10px 10px;" class="table table-bordered">
				<thead>
				<tr>
				<td><label>MCC</label></td> 
			    <td><label>MNC</label></td>		
			    <td><label>CELL</label></td>	
			    <td><label>LAT</label></td>	
			    <td><label>LONG</label></td>	
			    <td><label>RSSI</label></td>
				<td><label>TYPE</label></td>	
				<td><label>BAND</label></td>
				<td><label id="lacOrTacTdLabel">LAC</label></td>				
			    <td><label id="arfcnOrUarfcnTdLabel">ARFCN</label></td>	
			    <td><label id="bsicOrPscTdLabel">BSIC</label></td>
				<td><label id="antennaTdLabel">Antenna</label></td>
				<td></td>
				</tr>
				</thead>
				<tbody>
               <tr>
			    <td><input type="number" class="addAntennaInput add_plmn_text" name="add_mcc" id="add_mcc" value=""/></td> 
			    <td><input type="number" class="addAntennaInput add_plmn_text"  name="add_mnc" id="add_mnc" value=""/></td>		
			    <td><input type="number" class="addAntennaInput add_plmn_text"  name="add_cell" id="add_cell" value=""/></td>
			    <td><input type="number" class="addAntennaInput add_plmn_text"  name="add_lat" id="add_lat" value=""/></td>	
			    <td><input type="number" class="addAntennaInput add_plmn_text"  name="add_lon" id="add_lon" value=""/></td>	
				<td><input type="number" class="addAntennaInput add_plmn_text"  name="add_rssi" id="add_rssi" value=""/></td>
				<td><select name="add_type" id="add_type" class="addAntennaInput" style="width: 100px;"><option value="select">Select</option><option value="2G">2G</option><option value="3G">3G</option><option value="4G">4G</option></select></td>				
				<td><select name="add_band" id="add_band" class="addAntennaInput" style="width: 100px;"><option value="select">Select</option></select></td>
				<td><input type="number" class="addAntennaInput add_plmn_text"  name="add_lacOrTac" id="add_lacOrTac" value=""/></td>
				<td><input type="number" class="addAntennaInput add_plmn_text"  name="add_arfcn" id="add_arfcnOrUarfcn" value=""/></td>				
			    <td><input type="number" class="addAntennaInput add_plmn_text"  name="add_bsic" id="add_bsicOrPsc" value=""/></td>	
				<td><select class="addAntennaInput add_plmn_text"  id="addAllAntenna" multiple="" size="3"></select></td>				
				<td><input type="button" class="btn btn-default" id="add_plmn_btn" value="Add Cell"/></button></td>				
			   </tr>
			   </tbody>
				</table>
				</div>
				<div id="displayCellDiv">
				<table border=1 id="displayCellTable" style="font-size:10px;margin: 0 auto 10px 10px;" class="table table-bordered">
				<thead>
				<tr>
				<th><input type="checkbox" id="CheckAllCell"  onClick="CheckAllCell()"/></th>
				<th><label>Antenna</label></th>
				<th><label>MCC</label></th> 
			    <th><label>MNC</label></th>	
			    <th><label>LAC</label></th>	
			    <th><label>TAC</label></th>	
			    <th><label>CELL</label></th>	
			    <th><label>LAT</label></th>	
			    <th><label>LONG</label></th>	
			    <th><label>RSSI</label></th>
				<th><label>BAND</label></th>				
			    <th><label>ARFCN</label></th>	
			    <th><label>BSIC</label></th>
			    <th><label>UARFCN</label></th>	
			    <th><label>PSC</label></th>
			    <th><label>EARFCN</label></th>	
			    <th><label>PCI</label></th>					
				</tr>
				</thead>

				<tbody id="displayCellTable_body"></tbody>
				</table>
				</div>
				</div>
				<div class="modal-footer">
				<form name="targetAdditionFile_cell"  style="margin-right: 55%;">
				
					<input type="file" name="cell_file" accept=".csv" onchange="checkExt_cellFile()" style="cusrsor: pointer; display: inline-block;display: inline-block;" class="btn btn-default targer_file" id="cell_file"/>
					<input type="button" class="btn btn-default targer_file" style="display: inline-block; " 	 value="Upload File"  onclick="addCellsByFile()"/>
				<a href="../../resources/SampleFiles/addCell.csv" style="display: inline-block;" download >
					  <img src="../../resources/images/textFile.png" style="display: inline-block;" alt="sampleFile" width="25" height="25">
					  <h6 style="display: inline-block;font-size:10px;`" alt="sampleFile">Sample File</h6>
				</a>
				</form>
				<button class="btn btn-default"  id="deleteCell">Delete Cell</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
		<div class="modal fade" id="nCellsModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 500px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Cells Configuration</h4>
				</div>
				<div class="modal-body" style="padding:2px;">
				<div id="displayNCellsDiv">
				<table border=1 id="displayNCellsTable" style="font-size:10px;margin: 0 auto;" class="table table-default report_tables">
				<thead id="displayNCellsTable_head">
				</thead>
				<tbody id="displayNCellsTable_body"></tbody>
				</table>
				</div>
				</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
	
	
	
	<div class="modal fade" id="addGPSModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 250px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">System Location</h4>
				</div>
				<div class="modal-body">
				<div>
				<table border=1 id="sys_loc_table" style="font-size:10px;margin: 0 auto;" class="table table-default report_tables">
				
				<tbody id="sys_loc_table_body">
				<tr><td>Latitude</td><td><input type="text" id="sys_lat" /></td></tr>
				<tr><td>Logitude</td><td><input type="text" id="sys_lon" /></td></tr>
				
				</tbody>
				</table>
				</div>
				</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="addSystemLocation()">Update</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
	
		<div class="modal fade" id="selectedAntennaModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 350px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Antenna Profile</h4>
				</div>
				<div class="modal-body">
				<div class="selectedAntennaDiv">
				<table border=1 id="selectedAntennaTable" style="font-size:10px;margin: 0 auto;" class="table table-default report_tables">
				<thead>
				<tr>
				<th>Name</th>
				<th>Value</th>
				</tr>
				</thead>
				<tbody id="selectedAntennaTable_body"></tbody>
				</table>
				</div>
				</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
	</div><!-- modal-dialog -->
	</div><!-- modal -->
	
	
	    <div class="modal fade" id="3gmodal" role="dialog" aria-labelledby="myaddDeviceModalLabel" style="z-index: 1049;" data-value="Add 3G Device">
		<div class="modal-dialog" style="width: 1140px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title deviceModalLabel"  data-type="3g" id="myaddDeviceModalLabel"></h4>
				</div>
				<div class="modal-body">
				<div class="selectedAntennaDiv">
						<table style="font-size:12px;" border=1 id="add_sufi_tab" class="tab_com" style="">
						<tr>
							<td colspan=5 style="text-align: center;">Group : <input type="text" style="width: 200px;" value="" id="addGroupName3g" /></td>
						</tr>
						<tr>
							<td colspan=1>OF IP&nbsp;&nbsp;&nbsp;: <input type="text" style="width: 120px;" value="" id="addOfIp" /></td>
							<!--<td colspan=1> 
								<input type="text" value="Choose" style="width: 80px;background: white;border: none;" id="antennaProfileTextNameOf" disabled />
								<input type="button"  class="btn btn-default"  value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile('Of')" />
								<input type="text" value="" id="antennaProfileTextIdOf" style="display:none;" disabled />
							</td>-->
							<td colspan=1 style="text-align: center;">HW Capability : 
								<select style="width: 220px;" class="hwCapabilityTypeeOf"  id="addHwCapabilityTypeOf">
								</select>
							</td>
							
							<td id="3g_tr_device_pa_power" colspan=1>PA POWER : <input type='text' style="width: 50px;" value='' id='3g_device_pa_power'/></td>
							
							<td id="3g_tr_device_pa_gain" colspan=1>PA GAIN : <input type='text' style="width: 50px;" value='' id='3g_device_pa_gain'/></td>
						</tr>
						<tr>
							<td colspan=1>PPF IP : <input type="text" style="width: 200px;" value="0.0.0.0" id="addPpfIp" disabled />
							</td>
							<td colspan=1>System Manager&nbsp;&nbsp;&nbsp;: <input type="text" style="width: 120px;" value="" id="addSystemManagerIP" /></td>
							<!--<td colspan=1 style="display: none;" id="ppfAntennaProfileId"> 
								<input type="text" value="Choose" style="width: 80px;background: white;border: none;" id="antennaProfileTextNamePpf" disabled />
								<input type="button"  class="btn btn-default"  value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile('Ppf')" />
								<input type="text" value="" id="antennaProfileTextIdPpf" style="display:none;" disabled />
							</td>-->
							<td colspan=1 style="text-align: center;display: none;" id="ppfHwCapabilityId">HW Capability : 
								<select style="width: 220px;" class="hwCapabilityTypeePpf"  id="addHwCapabilityTypePpf">
								</select>
							</td>
						</tr>
						<tr>
							<td colspan=1>SPF IP : <input type="text" style="width: 200px;" value="1.1.1.1" id="addSpfIp" disabled />
							</td>
							<!--<td colspan=1 style="display: none;" id="spfAntennaProfileId"> 
								<input type="text" value="Choose" style="width: 80px;background: white;border: none;" id="antennaProfileTextNameSpf" disabled />
								<input type="button"  class="btn btn-default"  value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile('Spf')" />
								<input type="text" value="" id="antennaProfileTextIdSpf" style="display:none;" disabled />
							</td>-->
							<td colspan=1 style="text-align: center;display: none;" id="spfHwCapabilityId">HW Capability : 
								<select style="width: 220px;" class="hwCapabilityTypeSpf"  id="addHwCapabilityTypeSpf">
								</select>
							</td>
						</tr>
					</table>
				</div>
				</div>
				<div class="modal-footer">
				<button class="btn btn-default"  id="add3gDeviceFromModal">Add</button>
				<button type="button" class="btn btn-default" data-dismiss="modal" id="setDefaultSelectionId">Close</button>
				</div>
			</div><!-- modal-content -->
	</div><!-- modal-dialog -->
	</div><!-- modal -->
	
	
	
	<!-- ------------------------------------------------------------------@4g area ------------------------------------------------------------------>
		    <div class="modal fade" id="4gmodal" role="dialog" aria-labelledby="myaddDeviceModalLabel" style="z-index: 1049;" data-value="Add 4G Device">
		<div class="modal-dialog" style="width: 1140px !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title deviceModalLabel" data-type="4g"></h4>
				</div>
				<div class="modal-body">
				<div class="selectedAntennaDiv" >
						<table style="font-size:12px;" border=1 id="add_sufi_tab" class="tab_com" style="">
						<tr>
							<td colspan=5 style="text-align: center;">Group : <input type="text" style="width: 200px;" value="" id="addGroupName4g" /></td>
							
							<td colspan="5" style="text-align: center;">System Manager IP :<input type="text" style="width: 200px;" value="" id="SystemManagerIpEntered4g">
							</td>
						</tr>
						<tr>
							<td colspan=1>OF IP&nbsp;&nbsp;&nbsp;: <input type="text" style="width: 120px;" value="" id="addeOfIp" /></td>
							<!--<td colspan=1> 
								<input type="text" value="Choose" style="width: 80px;background: white;border: none;" id="antennaProfileTextNameOf" disabled />
								<input type="button"  class="btn btn-default"  value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile('Of')" />
								<input type="text" value="" id="antennaProfileTextIdOf" style="display:none;" disabled />
							</td>-->
							<td colspan=1 style="text-align: center;">HW Capability : 
								<select style="width: 220px;"  class="hwCapabilityTypeeOf"  id="addHwCapabilityTypeeOf">
								</select>
							</td>
							
							<td id="3g_tr_device_pa_power" colspan=1>PA POWER : <input type='text' style="width: 50px;" value='' id='4g_device_pa_power'/></td>
							
							<td id="3g_tr_device_pa_gain" colspan=1>PA GAIN : <input type='text' style="width: 50px;" value='' id='4g_device_pa_gain'/></td>
						</tr>
						<tr>
							<td colspan=1>PPF IP : <input type="text" style="width: 200px;" value="0.0.0.0" id="addePpfIp" disabled />
							</td>
							<!--<td colspan=1 style="display: none;" id="ppfAntennaProfileId"> 
								<input type="text" value="Choose" style="width: 80px;background: white;border: none;" id="antennaProfileTextNamePpf" disabled />
								<input type="button"  class="btn btn-default"  value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile('Ppf')" />
								<input type="text" value="" id="antennaProfileTextIdPpf" style="display:none;" disabled />
							</td>-->
							<td colspan=1 style="text-align: center;display: none;" id="ppfHwCapabilityId">HW Capability : 
								<select style="width: 220px;"  class="hwCapabilityTypeePpf"  id="addHwCapabilityTypeePpf">
								</select>
							</td>
						</tr>
						<tr>
							<td colspan=1>SPF IP : <input type="text" style="width: 200px;" value="1.1.1.1" id="addeSpfIp" disabled />
							</td>
							<!--<td colspan=1 style="display: none;" id="spfAntennaProfileId"> 
								<input type="text" value="Choose" style="width: 80px;background: white;border: none;" id="antennaProfileTextNameSpf" disabled />
								<input type="button"  class="btn btn-default"  value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile('Spf')" />
								<input type="text" value="" id="antennaProfileTextIdSpf" style="display:none;" disabled />
							</td>-->
							<td colspan=1 style="text-align: center;display: none;" id="spfHwCapabilityId">HW Capability : 
								<select style="width: 220px;" class="hwCapabilityTypeSpf" id="addHwCapabilityTypeeSpf">
								</select>
							</td>
						</tr>
					</table>
				</div>
				</div>
				<div class="modal-footer">
				<button class="btn btn-default"  id="add4gDeviceFromModal">Add</button>
				<button type="button" class="btn btn-default" data-dismiss="modal" id="setDefaultSelectionId">Close</button>
				</div>
			</div><!-- modal-content -->
	</div><!-- modal-dialog -->
	</div><!-- modal -->
	
	<!-- ------------------------------------------------------------------@4g area ------------------------------------------------------------------>
	
	
	
	
	  

<!--<script type="text/javascript" src="../../resources/js/consolidation.js" ></script>-->	
<!--<script type="text/javascript" src="../../resources/js/3g/3g_dashboard.js"></script>-->
<div class="modal fade" id="loading_modal" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Please Wait...</h4>
				</div>
				<div class="modal-body">
				<img width=100% id="status_loading" src="../../resources/images/Loading_icon.gif"></img>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
<jsp:include page='modal_footer.jsp' />
</body>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
<script type="text/javascript" src="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../../resources/lib/assets/css/fontawesome-all.min.css">
<link href="../../resources/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="../../resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/operation.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/main.css" />
<script type="text/javascript" src="../../resources/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../../resources/lib/jquery.battatech.excelexport.js"></script>
<script type="text/javascript" src="../../resources/lib/export.js"></script>
<script type="text/javascript" src="../../resources/js/operation.js"></script>
<style>
#displayTargetDiv
{
	height:150px;
	overflow-y:auto;
	overflow-x:hidden;
}

#displayAntennaProfileDiv
{
	/* width: 1220px; */
	/*height:180px;*/
	overflow-y:auto;
	overflow-x:hidden;
}

/*#container {
    /*height: 542px;*/
        height: auto;
	background: #66615B;
}*/

#message_box, #operationAndDevicesDiv {
    display: table;
    margin: 0 auto;
    background: #66615B;
    padding: 10px 10px 10px 10px;
    width: 98%;
    height: auto; 
}

#message_box, #operationAndDevicesDiv {
    padding: 0px 10px 10px 10px; 
}

.table-fixed{
/*  width: 100%;
  background-color: #f3f3f3;
  tbody{
    height:200px;
    overflow-y:auto;
    width: 100%;
    }
  thead,tbody,tr,td,th{
    display:block;
  }
  tbody{
    td{
      float:left;
    }
  }
  thead {
    tr{
      th{
        float:left;
       background-color: #f39c12;
       border-color:#e67e22;
      }
    }
  }*/	
}

.statusCard {
   
    width: 229px;
	margin-left:25px;
   
}
html{
	overflow-y:auto;
}

	b{
	font-size: 18px;
	}
	
.spanStyle{
	font-size: 14px;
	margin-left: 25px;
	float:right;
	cursor:pointer;
}

.spanStyleTbody{
display: none;
}

.holder{
height: 39px;
border-radius: 12px;
}

.largeHeading{
 color: black;
 float:left;
 padding-left: 10px;
 padding-top: 8px;
}

.dotClass{
	float:right;
	background-color: #0067b1;
	border-radius: 50%;
	height: 25px;
	width: 25px;
	margin-right: 10px;
	margin-top: 6px;
    padding-left: 9px;
    padding-top: 4px;
    color: white;
}

#list_table_operations tr td
{
  word-break: break-all;
  }
  
  
  #addNewOprDiv .common_tables input {
    width: 207px;
}

.antenna_label_class tr td label{
 /*font-size: 12px;*/
}

.parameter_units_class{
	font-size: 11px;
}

.labelClass{
	font-size: 12px;
}

.btn-operation{
	margin-top: 4px;
	margin-left: 4px;
}

/*#purgeData{
	display: none;
}

#addCell{
	display: none;
}*/

</style>
<style>
		<%= session.getAttribute("themeCss")%>
</style>
<style>
.table-bordered thead{
	background-color: #505b65
}
</style>
<style>
body{
	overflow: hidden;
}
</style>
</html>