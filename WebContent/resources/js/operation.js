var globalOprList=[];
var jammingMode=1;
var globalProfileRowCount=0;
var dirPath ="../../";
var globalAntennaProfileList=[];
var status_success=false;
var globalTargetList=[];
var globalDeviceName="";
var globalDeviceIp="";
var globalPLMNList=[];
var globalBtsDevices=[];
var globalDeviceData=[];
var globalCell=[];
var globalBtsDevicesStatus=[];
var jammer_hw_capability_name;
var JammerBand;
var statusCount={};
statusCount.run=0;
statusCount.wait=0;
statusCount.reachable=0;
statusCount.down=0;
var color = {};
color.fontColor = "white";
color.backgroundColor = "white";
var globalNetscannerIp = "";
var globalStatus={};
globalStatus.color =color;
globalStatus.count =statusCount;
var globalAntennaAngleOffset='';
var globalScanningAntenna=[];
var globalS1AntennaAngle='';
var PTAntennaRotated='';

var captureDeviceStatusEvent = function()
{
	ws4 = new WebSocket("ws://"+ipAddress+":8080/locator/devicestatus");
	var color="#FFF";
	ws4.onopen = function()
	{
		console.log("connected to the device status server");
	}
	ws4.onmessage = function(event)
	{
		var msg = event.data;
		console.log(msg);
		if(msg!="Connection Established"){
		if(msg=='ok'){
			getbtsinfo();
		}else if(msg=='all'){
			updateNodesStatusAtLockUnlock("all");	
		}else{
			updateNodesStatusAtLockUnlock(msg);
		}
		}
	}
	ws4.onclose = function()
	{
		/*var r = confirm("Disconected From Server.Press ok to reconect");
		if (r == true) {
		    location.reload();
		} else {
		    
		}*/
	}
}

var captureAutoOprEvent = function()
{
	ws4 = new WebSocket("ws://"+ipAddress+":8080/locator/autopr");
	var color="#FFF";
	ws4.onopen = function()
	{
		console.log("connected to the captured events server");
	}
	ws4.onmessage = function(event)
	{
		var msg = event.data;
		console.log(msg);
		jsonMsg=JSON.parse(msg);
		jsonMsg=jsonMsg.msg.toLowerCase();
		if(jsonMsg.includes('operation created') || jsonMsg.includes('operation resumed')){
			window.location.href='maps.jsp';
		}
		//$("#list_table_body").html();
		getbtsinfo();
	}
	ws4.onclose = function()
	{
		/*var r = confirm("Disconected From Server.Press ok to reconect");
		if (r == true) {
		    location.reload();
		} else {
		    
		}*/
	}
}

var captureCurrentConfigOprEvent = function()
{
	ws4 = new WebSocket("ws://"+ipAddress+":8080/locator/configopr");
	var color="#FFF";
	ws4.onopen = function()
	{
		console.log("connected to the device status server");
	}
	ws4.onmessage = function(event)
	{
		var msg = event.data;
		console.log(msg);
		displayCurrentConfigOpr(JSON.parse(msg));
		//$("#list_table_body").html();
		//getbtsinfo();
	}
	ws4.onclose = function()
	{
		/*var r = confirm("Disconected From Server.Press ok to reconect");
		if (r == true) {
		    location.reload();
		} else {
		    
		}*/
	}
}

var displayCurrentConfigOpr = function(currentConfigOpr){
for (var ip in currentConfigOpr) {
    if (currentConfigOpr.hasOwnProperty(ip)) {
        //console.log(ip + " -> " + currentConfigOpr[ip]);
		//alert(ip + " -> " + currentConfigOpr[ip]);
		$('#networkConfig_'+ip.split('.').join("")).text(currentConfigOpr[ip]);
    }
}
}

var COLOR = {
             	'run':"rgb(198,227,159)",
             	'wait':"rgb(251,201,142)",
             	'nor':"rgb(255,229,130)",
             	'down':"rgb(240,141,144)"
			};

var updateCardsColor = function(){
	$(".nor_sufi").css("background",COLOR['nor']);
	$(".run_sufi").css("background",COLOR['run']);
	$(".wait_sufi").css("background",COLOR['wait']);
	$(".down_sufi").css("background",COLOR['down']);
}

$(document).ready(function(){
//sleep(10000);
/*	if(sessionParams.role.toLowerCase()=='superadmin'){
		$('#addSufiDiv').css("display","none");
	}*/

$("#listStatusDiv").css("display","block");
$('#oprAndDeviceDetailsDiv').css("display","block");
updateCardsColor();
registerEvent();
getbtsinfo();
getAllOperations();	
  /*  $('.checkAntennaProfileClass').click(function() {
        $(this).siblings('input:checkbox').prop('checked', false);
    });*/
captureDeviceStatusEvent();
captureAutoOprEvent();
captureCurrentConfigOprEvent();
plmnClickEvent();
techTypeSelectEvent();
getAllCells();
CheckAllCell();
checkTarget();
checkBoxAntennaProfile();
getAntennaProfileForAddPlmn();
getCurrentScannedOperators();
$('#restartSystemButton').css('display','none');

$('#addDeviceArrowSpan').on("click",function(){
	openCloseDialog(this);
	});
$('#currentOperationArrowSpan').on("click",function(){
	openCloseDialog(this);
	});

$('#addPpfIp').keyup(function(){
	keyUpActionOnAddingPpfAndSpf(this,'ppf');
	});
	
$('#addSpfIp').keyup(function(){
	keyUpActionOnAddingPpfAndSpf(this,'spf');
	});
	
$('#editAllAntennaCheck').click(function(){
	editAllAntennas();
	});

/*$('#ptzSelectionStatus').change(function(){
	checkPtzSelection(this);
	});*/
});

/*var checkPtzSelection = function(element){
	if($(element).val()=="yes"){
		$('#addAntennaTilt').prop('disabled',true);
		$('#addAntennaAzimuth').prop('disabled',true);
	}else{
		$('#addAntennaTilt').prop('disabled',false);
		$('#addAntennaAzimuth').prop('disabled',false);
	}
}*/

function getCurrentScannedOperators(){
	$.ajax({
		url:"../../service/common/getcurrentscannedoperators",
		type:'get',
		success:function(data)
		{
			$('#newOprPlmn').html("");
			$('#newOprPlmn').append('<option value="all">All</option>');
			
			var options='';
			var opr="";
			for(var i=0;i<data.length;i++){
				if(data[i].opr.toLowerCase()!='na'){
					if(opr!=data[i].opr){
					options+='<option value="'+data[i].opr+'">'+data[i].opr+'</option>';
					}
					opr=data[i].opr;
				}
			}
			$('#newOprPlmn').append(options);
		}
	});
}

var editAllAntennas = function(){
	if($('#editAllAntennaCheck').prop('checked')){
		if(systemTypeCode==0)
		{

			$('#antennaScanning23 ').prop('disabled',false);
			$('#antennaTracking23').prop('disabled',false);
			$('#editSameAntennaCheck23').prop('checked',true);
			
			
			$('#antennaScanning1 ').prop('disabled',false);
			$('#antennaTracking1').prop('disabled',false);
			$('#editSameAntennaCheck1').prop('checked',true);
			
	
		}
		else{
			  $('.editSameAntennaCheck').prop('checked',true);
			  $('.editAntennaInput').prop('disabled',false);
		}
	}else{
		  $('.editSameAntennaCheck').prop('checked',false);
		  $('.editAntennaInput').prop('disabled',true);
	}
}

var editSameAntennaCheck = function(antennaId){
 	if($('#editSameAntennaCheck'+antennaId).prop('checked')){
		  $('.editSameAntennaInput'+antennaId).prop('disabled',false);
	}else{
		  $('#editAllAntennaCheck').prop('checked',false);
		  $('.editSameAntennaInput'+antennaId).prop('disabled',true);
	}
	var checkedBoxCount=0;
	$('.editSameAntennaCheck').each(function(){
		if($(this).prop('checked')){
			checkedBoxCount++;
		}
	});
	if($('.editSameAntennaCheck').length==checkedBoxCount){
		$('#editAllAntennaCheck').prop('checked',true);	
	}
}

function keyUpActionOnAddingPpfAndSpf(element,deviceType){
	var Ip=$(element).val();
	if(Ip!='0.0.0.0' && Ip!='1.1.1.1'){
	if(validateIp(Ip)){
		$('#'+deviceType+'AntennaProfileId').css('display','table-cell');
		$('#'+deviceType+'HwCapabilityId').css('display','table-cell');
	}
	}else{
		$('#'+deviceType+'AntennaProfileId').css('display','none');
		$('#'+deviceType+'HwCapabilityId').css('display','none');
}
}

function sleep(milliseconds) {
alert("in sleep for "+(milliseconds/1000)+' seconds');
  var start = new Date().getTime();
  alert(start);
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
  alert("over from sleep");
}

function getAllCells(){
$('#displayCellTable_body').html('');
	$.ajax
	({
			url:"../../service/2g/getPLMN",
			type:'post',
			success:function(data)
			{
				globalCell=data;
			var tableRows='';
			for(var i=0;i<data.length;i++){
				globalPLMNList.push(data[i].id);
				if(data[i].packet_type=='Loc_2g')
				{
					tableRows+='<tr id="cellTableRow'+data[i].id+'"><td><input type="checkbox" value="'+data[i].id+'" name="checkCellList" class="checkCellListClass"></input></td><td id="profile_name_'+data[i].id+'">'+data[i].profile_name+'</td><td id="mcc_'+data[i].id+'">'+data[i].mcc+'</td><td id="mnc_'+data[i].id+'">'+data[i].mnc+'</td><td id="lac_'+data[i].id+'">'+data[i].lac+'</td><td id="tac_'+data[i].id+'"></td><td id="cell_'+data[i].id+'">'+data[i].cell+'</td><td>'+data[i].lat+'</td><td>'+data[i].lon+'</td><td>'+data[i].rssi+'</td><td>'+data[i].band+'</td><td>'+data[i].arfcn+'</td><td>'+((parseInt(data[i].ncc)*8)+parseInt(data[i].bcc))+'</td><td></td><td></td><td></td><td></td></tr>';
				}
				else if(data[i].packet_type=='Loc_3g')
				{
					tableRows+='<tr id="cellTableRow'+data[i].id+'"><td><input type="checkbox" value="'+data[i].id+'" name="checkCellList" class="checkCellListClass"></input></td><td id="profile_name_'+data[i].id+'">'+data[i].profile_name+'</td><td id="mcc_'+data[i].id+'">'+data[i].mcc+'</td><td id="mnc_'+data[i].id+'">'+data[i].mnc+'</td><td id="lac_'+data[i].id+'">'+data[i].lac+'</td><td id="tac_'+data[i].id+'"></td><td id="cell_'+data[i].id+'">'+data[i].cell+'</td><td>'+data[i].lat+'</td><td>'+data[i].lon+'</td><td>'+data[i].rssi+'</td><td>'+data[i].band+'</td><td></td><td></td><td>'+data[i].uarfcn+'</td><td>'+data[i].psc+'</td><td></td><td></td></tr>';
				}

				else if(data[i].packet_type=='Loc_4g')
				{
					tableRows+='<tr id="cellTableRow'+data[i].id+'"><td><input type="checkbox" value="'+data[i].id+'" name="checkCellList" class="checkCellListClass"></input></td><td id="profile_name_'+data[i].id+'">'+data[i].profile_name+'</td><td id="mcc_'+data[i].id+'">'+data[i].mcc+'</td><td id="mnc_'+data[i].id+'">'+data[i].mnc+'</td><td id="lac_'+data[i].id+'"></td><td id="tac_'+data[i].id+'">'+data[i].tac+'</td><td id="cell_'+data[i].id+'">'+data[i].cell+'</td><td>'+data[i].lat+'</td><td>'+data[i].lon+'</td><td>'+data[i].rssi+'</td><td>'+data[i].band+'</td><td></td><td></td><td></td><td></td><td>'+data[i].earfcn+'</td><td>'+data[i].pci+'</td></tr>';
				}
			}
			$('#displayCellTable_body').html(tableRows);
			}
	});
} 

var deleteCell = function()
{
var targetIds='';
$('.checkCellListClass').each(function() {
        if ($(this).prop("checked")) {
		targetIds+=$(this).val()+',';
	}
    });
	
	
	if(targetIds==''){
	alert('Please select Target first');
	return false;
	}
		
	targetIds=targetIds.substring(0,targetIds.length-1);
	data={"targetIds":targetIds};
	$.ajax({
		url:"../../service/2g/deletePLMN",
		data:data,
		type:'post',
		success:function(data)
		{
			if(data.result=="SUCCESS"){
            alert('Cell Deleted Successfully');
			var targetIdArray=targetIds.split(",");
			for(var i in targetIdArray){
			var row=document.getElementById('cellTableRow'+targetIdArray[i]);
			var table=row.parentNode.parentNode;
			table.deleteRow(row.rowIndex);
			}
			var plmnList=globalPlmnList;
			for(var i in plmnList) {
			var id = plmnList[i];
			if(targetIds.indexOf(id) !== -1) {
			globalPLMNList.splice(i, 1);
			}
			}
			getTargetList();
			}else{
			alert('problem in Deleting Cell');
			}
		},
		error:function(){
			alert("Problem in Deleting Cell");
		}
	});
}

var techTypeSelectEvent = function(){
	$("#add_type").change(function(){
	$('#add_band').html('');
	var options='<option value="select">Select</option>';
	var selectedValue=$(this).val();
	$('#lacOrTacTdLabel').text('LAC');
	if(selectedValue=="2G")
	{
		options+='<option value="2">2</option>';
		options+='<option value="4">4</option>';
		$('#lacOrTacTdLabel').text('LAC');
		$('#arfcnOrUarfcnTdLabel').text('ARFCN');
		$('#bsicOrPscTdLabel').text('BSIC');
	}
	else if(selectedValue=="3G")
	{
		options+='<option value="1">1</option>';
		//options+='<option value="3">3</option>';
		options+='<option value="8">8</option>';
		$('#lacOrTacTdLabel').text('LAC');
		$('#arfcnOrUarfcnTdLabel').text('UARFCN');
		$('#bsicOrPscTdLabel').text('PSC');
	}
	else if(selectedValue=="4G")
	{
		options+='<option value="1">1</option>';
		options+='<option value="3">3</option>';
		options+='<option value="5">5</option>';
		options+='<option value="8">8</option>';
		options+='<option value="40">40</option>';
		options+='<option value="41">41</option>';
		$('#lacOrTacTdLabel').text('TAC');
		$('#arfcnOrUarfcnTdLabel').text('EARFCN');
		$('#bsicOrPscTdLabel').text('PCI');
	}
	else
	{
		$('#lacOrTacTdLabel').text('LAC');
		$('#arfcnOrUarfcnTdLabel').text('ARFCN');
		$('#bsicOrPscTdLabel').text('BSIC');
	}
	$('#add_band').html(options);
	});
}


var plmnClickEvent = function()
{
	//$("#")
	
	$("#add_plmn_btn").click(function(){
		var returnFlag  = false;
		
		$(".add_plmn_text").each(function()
		{
			
			if($(this).val() == "")
			{
				returnFlag = true;
			}
		});
		
		if(returnFlag)
		{
			alert("Please Fill All The Values");
			return false;
		}
		
		var rssi = $("#add_rssi").val();
		var lacOrTac = $("#add_lacOrTac").val();
		var arfcnOrUarfcn = $("#add_arfcnOrUarfcn").val();
		var bsicOrPsc = $("#add_bsicOrPsc").val();
		var addAllAntenna=$("#addAllAntenna").val();
		
		var cellType=$('#add_type').val();
		if(cellType=="select"){
		alert("Please Select the Cell Type");
		return false;
		}

		var band = $("#add_band").val();
		if(band=="select"){
		alert("Please Select Band");
		return false;
		}

		var mcc = $("#add_mcc").val();
		if(mcc.length!=3){
			alert("Please enter valid MCC");
			return false;
		}
		var mnc = $("#add_mnc").val();
		if(mnc.length!=2 && mnc.length!=3){
			alert("Please enter valid MNC");
			return false;
		}
		//var lac = $("#add_lacOrTac").val();
		if(lacOrTac.length==0 || lacOrTac<=0 ){
			alert("LAC/TAC is Invalid");
			return false;			
		}
		var cell = $("#add_cell").val();
		if(cell.length=='0' || cell<2 ){
			alert("Cell ID cannot be "+cell);
			return false;
		}
		var lat = $("#add_lat").val();
		if(lat==''){
		 alert("Please enter latitude");
		 return false;
		}
		var lon = $("#add_lon").val();
		if(lon==''){
		 alert("Please enter longitude");
		 return false;
		}
		
		if(arfcnOrUarfcn.length=='0' || arfcnOrUarfcn<0 ){
			alert("E/U/Arfcn cannot be "+arfcnOrUarfcn);
			return false;
		}
		
		//var cell = $("#add_cell").val();
		if(bsicOrPsc.length=='0' || bsicOrPsc<0 ){
			alert("BSIC/PSC/PCI cannot be "+bsicOrPsc);
			return false;
		}
	
		if(addAllAntenna==null){
			alert("Please select Antenna");
			return false;
		}
		 for(var i=0;i<globalCell.length;i++){
				if(globalCell[i].mcc==mcc &&globalCell[i].mnc==mnc && globalCell[i].cell==cell && (globalCell[i].lac==lacOrTac ||globalCell[i].tac==lacOrTac)&&globalCell[i].antenna_id==addAllAntenna){
					alert("Duplicates cell And Antenna in MCC,MNC,CELL,LAC&TAC And Antenna");
					return false;
				}
				}
		var allAntennaStr=addAllAntenna.join(",");
		var selectedAntennaArr=[];
		
		if(allAntennaStr.indexOf("-1")!=-1){
			allAntennaStr="-1";
			$('#addAllAntenna option').each(function(){
				if($(this).val()!="-1"){
					selectedAntennaArr.push($(this).text());
				}
			});
		}else{
			$('#addAllAntenna option:selected').each(function(){
				selectedAntennaArr.push($(this).text());
			});
		}
		
		var postData = 
		
				{
					"mcc":mcc,
					"mnc":mnc,
					"lacOrTac":lacOrTac,
					"cell":cell,
					"lat":lat,
					"lon":lon,
					"rssi":rssi,
					"band":band,
					"arfcnOrUarfcn":arfcnOrUarfcn,
					"bsicOrPsc":bsicOrPsc,
					"allAntennaStr":allAntennaStr,
					"cellType":cellType

				}
				
		
		$.ajax({
			
			url:"../../service/2g/addPLMN",
			data:JSON.stringify(postData),
			type:"post",
			contentType: "application/json",
			success:function(data)
			{
				if(data.result == "SUCCESS")
				{
					alert("Added Successfully");
					var lacOrTacText="lac";
					if(cellType=='4G'){
						lacOrTacText="tac";
					}
					for(var i in globalPLMNList){
					var id=globalPLMNList[i];
					if(mcc==$('#mcc_'+id).text() && mnc==$('#mnc_'+id).text() && lacOrTac==$('#'+lacOrTacText+'_'+id).text() && cell==$('#cell_'+id).text()){
						var row=document.getElementById('cellTableRow'+id);
						var table=row.parentNode.parentNode;
						table.deleteRow(row.rowIndex);
					}
					}
					
					var tableRow='';
					if(cellType=='2G'){
						for(var antennaCount=0;antennaCount<selectedAntennaArr.length;antennaCount++){
							tableRow+='<tr id="cellTableRow'+data.id+'"><td><input type="checkbox" value="'+data.id+'" name="checkCellList" class="checkCellListClass"></input></td><td>'+selectedAntennaArr[antennaCount]+'</td><td>'+mcc+'</td><td>'+mnc+'</td><td>'+lacOrTac+'</td><td></td><td>'+cell+'</td><td>'+lat+'</td><td>'+lon+'</td><td>'+rssi+'</td><td>'+band+'</td><td>'+arfcnOrUarfcn+'</td><td>'+bsicOrPsc+'</td><td></td><td></td><td></td><td></td></tr>';
						}
					}else if(cellType=='3G'){
						for(var antennaCount=0;antennaCount<selectedAntennaArr.length;antennaCount++){	
							tableRow+='<tr id="cellTableRow'+data.id+'"><td><input type="checkbox" value="'+data.id+'" name="checkCellList" class="checkCellListClass"></input></td><td>'+selectedAntennaArr[antennaCount]+'</td><td>'+mcc+'</td><td>'+mnc+'</td><td>'+lacOrTac+'</td><td></td><td>'+cell+'</td><td>'+lat+'</td><td>'+lon+'</td><td>'+rssi+'</td><td>'+band+'</td><td></td><td></td><td>'+arfcnOrUarfcn+'</td><td>'+bsicOrPsc+'</td><td></td><td></td></tr>';
	                    }				
					}else if(cellType=='4G'){
						for(var antennaCount=0;antennaCount<selectedAntennaArr.length;antennaCount++){
							tableRow+='<tr id="cellTableRow'+data.id+'"><td><input type="checkbox" value="'+data.id+'" name="checkCellList" class="checkCellListClass"></input></td><td>'+selectedAntennaArr[antennaCount]+'</td><td>'+mcc+'</td><td>'+mnc+'</td><td></td><td>'+lacOrTac+'</td><td>'+cell+'</td><td>'+lat+'</td><td>'+lon+'</td><td>'+rssi+'</td><td>'+band+'</td><td></td><td></td><td></td><td></td><td>'+arfcnOrUarfcn+'</td><td>'+bsicOrPsc+'</td></tr>';
						}					
					}
						globalPLMNList.push(data.id);
						$("#displayCellTable_body").append(tableRow);
						$("#add_mcc").val("");
						$("#add_mnc").val("");
						$("#add_lacOrTac").val("");
						$("#add_cell").val("");
						$("#add_lat").val("");
						$("#add_lon").val("");
						$("#add_rssi").val("");
						$("#add_band").val("");
						$("#add_arfcnOrUarfcn").val("");
						$("#add_bsicOrPsc").val("");
						$('#add_type').val("select");
						$('#arfcnOrUarfcnTdLabel').text("ARFCN");
						$('#bsicOrPscTdLabel').text("BSIC");
						location.reload();
				}
				else
				{
					alert("Unable to Add Cell");
				}
			}
		});
	});
	
	
	$('#deleteCell').click(function(){
	  deleteCell();
	});
}

var registerEvent = function(){
	$('#addOpr').click(function(){
		addOperation($('#oprName').val(),$('#oprNote').val());
	});
	
	$('#addNewOpr').click(function(){
		addOperation();
	});

	$("#truncateDb").click(function(){		
		var a = confirm("Are you sure to Purge Data?");
		if(a)
		{
			truncateDb();
		}	
	});
		
	$("#list_operation").on('click','#update_gps_acc',function(){
		updateGpsAcc();
	});	

	$('#add3gDeviceFromModal').click(function(){
		add3gDevice();	
	});
	
	
	$('#add4gDeviceFromModal').click(function(){
		add4gDevice();	
	});

	$('#setDefaultSelectionId').click(function(){
		setSelectionToDefault();	
	});


/*
$( document ).ajaxSend(function() {
	  $( "#loading_modal" ).modal("show");
	});
$( document ).ajaxComplete(function() {
	$( "#loading_modal" ).modal("hide");
	});
$( document ).ajaxError(function() {
	$( "#loading_modal" ).modal("hide");
	});*/

}

function truncateDb()
{
	var dataTables=$('#dataTypeSelect').val();
	data={"methodName":"truncateDb","dataTables":dataTables};
	$.ajax({
		url:"../../Operations",
		data:data,
		type:'post',
		success:function(data)
		{
			alert('Data Purged Successfully');
			
		}
	});
}

function updateSysConfig(sysConfigId)
{
	var name=$('#configButton'+sysConfigId).val();
	var value='';
	var code="-1";
	if(name=="system_type" || name=="system_mode" || name=="gps_node"){
		value=$('#configInput'+sysConfigId+' option:selected').text();
		code=$('#configInput'+sysConfigId).val();
	}else{
		value=$('#configInput'+sysConfigId).val();
	}
	
	if(sysConfigId=="3" && value<10){
		alert("Minimum Interval Sholud Be 10");
		return;
	}

	 if((name =="tracktime") && (value>=901 || value<=29) ){
	        //$('#configButton1').prop('disabled', true);
		 alert("Cell Active Time(sec) range is 30 second to 900 second, please check value entered");
	       return false;
	     }
	    
	
	var postData={"name":name,"value":value,"code":code};
	$.ajax({
		url:"../../service/common/updatesysconfig",
		data:postData,
		type:'post',
		success:function(data)
		{
			if(data.result=='success'){
				alert('System Configurations Updated Successfully');
				if(name=="system_type"){
					systemTypeCode=code;
					systemTypeValue=value;
					getbtsinfo();
					getHwCapabilityTypes();
					getUseTypes();
					if(code==0 || code==1){
						$('#sysprop_cue_hummer_to_falcon').css('display','none');
						$('#sysprop_timeout').css('display','none');
						$('#sysprop_validity').css('display','none');
						$('#td_text_schedulertime input').prop('disabled',false);
						$('#td_button_schedulertime button').prop('disabled',false);
						$('#td_button_schedulertime button').css({"background-color": "#337ab7;", "border-color": "#2e6da4;"});
					}else{
						$('#sysprop_cue_hummer_to_falcon').css('display','table-row');
						$('#sysprop_timeout').css('display','table-row');
						$('#sysprop_validity').css('display','table-row');
						$('#td_text_schedulertime input').prop('disabled',false);
						$('#td_button_schedulertime button').prop('disabled',false);
						$('#td_button_schedulertime button').css({"background-color": "#337ab7;", "border-color": "#2e6da4;"});
					}
				}else if(name=="system_mode"){
					systemMode=code;
				}
				else if(name=="operation_mode"){
					location.reload();
				}
			}else if(data.result=='not qualified'){
				alert("Problem in updating configuration."+data.msg);
			}else{
				alert("Problem in updating configuration");
			}
		}
	});
}

var createBandListForExeScan = function(selectorId,tech)
{
	
		var arr = $("#"+selectorId).val();
		var tempObj = {};
		tempObj.TECH=tech;
		var bandList = [];
		for(var i in arr)
		{
			var band = {}
			band.BAND=arr[i];
			bandList.push(band);
		}
		tempObj.BAND_LIST = bandList;
	
	return tempObj;
}


var addOperation = function(){
//var oprName = $('#oprName').val();
	var oprName=$('#newOprName').val();
	if(oprName==""){
		alert("Please provide Operation name");
		return;
	}
	
	var oprNote=$('#newOprNote').val();
	if(oprNote==""){
		alert("Please provide Operation note");
		return;
	}
	
	var oprType=$('#newOprType').val();
	
	var oprDuration=$('#newDuration').val();
	if(oprDuration=="" && oprType=="1"){
		alert("Please provide Periodicity");
		return;
	}
	
	if(oprType=="1" && parseInt(oprDuration)!=undefined && parseInt(oprDuration)<=2){
		alert("Please provide at least 3 minutes in duration");
		return;
	}
		
	var oprPlmn=$('#newOprPlmn').val();
	var oprPlmnFull='';
	
	if(oprPlmn==null && $('#newOprPlmn > option').length!=0){
     alert("Please select Operator");
	 return false;
	}	

	if(oprPlmn!=null){
		oprPlmnFull+=oprPlmn.join(',');
	}
	
	/*var scanListGsm = [];
	var scanListUmts = [];
	var scanListLte = [];

	if($("#exe_scan_2g_tech").val() != null)
	{
		scanListGsm.push(createBandListForExeScan('exe_scan_2g_tech','GSM'));
	}
	if($("#exe_scan_umts_tech").val() != null)
	{
		scanListUmts.push(createBandListForExeScan('exe_scan_umts_tech','UMTS'));
	}
	if($("#exe_scan_lte_tech").val() != null)
	{
		scanListLte.push(createBandListForExeScan('exe_scan_lte_tech','LTE'));
	}

	exeData.SCAN_LIST = scanList;
*/

for(var i=0;i<globalOprList.length;i++){
if(globalOprList[i].name==oprName){
alert("Operation Name should be unique");
return;
}
}

/*for(var i=0;i<globalBtsDevices.length;i++){
	var deviceIp=globalBtsDevices[i];
	if(globalBtsDevices[i]!='0.0.0.0' && globalBtsDevices[i]!='1.1.1.1' && globalBtsDevices[i]!=globalNetscannerIp){
		if(globalBtsDevicesStatus[deviceIp].toLowerCase() == "not reachable" || globalBtsDevicesStatus[deviceIp].toLowerCase() == "system down"){
			alert("All Devices should be reachable");
			return;
		}
	}
}*/

var isChecked = 'true';
/*if ($('#old_scanned_data').is(":checked"))
{
	var isChecked = 'true';
}*/


$.ajax
({
		url:"../../service/common/validateOperation",
		data:{"oprName":oprName,"oprType":oprType,"old_data":isChecked},
		type:'post',
		dataType:"text",
		asycn:false,
		success:function(data)
		{
			if(data=="OK")
			{
				
				$.ajax
				({
						url:"../../service/common/addOperation",
						data:{"oprName":oprName,"oprNote":oprNote,"oprType":oprType,"oprDuration":oprDuration,"oprPlmnFull":oprPlmnFull,"oldData":isChecked},
						type:'post',
						success:function(data)
						{
							if(data.result=="success")
							{
								
								alert('Operation Added Successfully');
								window.location.reload();
							}
							else if(data.result=="failure")
							{
								alert(data.message);
							}
							else
							{
								alert("Problem in Starting Operation");
							}
						}
				});
			}
			else 
			{
				alert(data);
			}
		}
});

}

var stopOperation = function(){

		$.ajax
		({
				url:"../../service/common/stopOperation",
				type:'post',
				success:function(data)
				{
				if(data.result=="success"){
	            alert('Operation Stopped Successfully');
				window.location.reload();
				}else{
				alert("Problem in Stopping Operation");
				}
				}
		});

	}

var stopNetscanOperation = function(){

		$.ajax
		({
				url:"../../service/common/stopNetscanOperation",
				type:'post',
				success:function(data)
				{
				if(data.result=="success"){
	            alert('Operation Stopped Successfully');
				window.location.reload();
				}else{
				alert("Problem in Stopping Operation");
				}
				}
		});

	}




var downloadOperationData = function(oprId){
	$.ajax
	({
			url:"../../service/common/reportserver",
			type:'get',
			data:{"oprId":oprId},
			success:function(data)
			{
			if(data.result=="success"){
				window.location.href='../../resources/reports/'+data.msg;
				//alert('Operation Stopped Successfully');
			//window.location.reload();
			}else{
				//alert("Problem in Stopping Operation");
			}
			}
	});

}


var deleteOperation = function(oprId){

	$.ajax
	({
			url:"../../service/common/deleteoperation",
			data:{"oprId":oprId},
			type:'post',
			success:function(data)
			{
			if(data.result=="success"){
            alert('Operation Deleted Successfully');
			window.location.reload();
			}else{
			alert("Problem in Deleting Operation");
			}
			}
	});

}

var deleteAllOperation = function(){
	var r = confirm("Are you sure to clear all Operations?");
	if (r == true) {
		$.ajax
		({
				url:"../../service/common/deletealloperation",
				type:'post',
				success:function(data)
				{
				if(data.result=="success"){
	            alert('Operation Deleted Successfully');
				window.location.reload();
				}else{
				alert("Problem in Deleting Operation");
				}
				}
		});
	} else {
	    
	}
	
	


}

var restartOperation = function(oprId){

	
	for(var i=0;i<globalBtsDevices.length;i++){
		var deviceIp=globalBtsDevices[i];
		if(globalBtsDevices[i]!='0.0.0.0' && globalBtsDevices[i]!='1.1.1.1'){
			if(globalBtsDevicesStatus[deviceIp].toLowerCase() == "not reachable" || globalBtsDevicesStatus[deviceIp].toLowerCase() == "system down"){
				alert("All Devices should be reachable");
				return;
			}
		}
	}
	
	$.ajax
	({
			url:"../../service/common/restartoperation",
			type:'post',
			data:{"oprId":oprId},
			success:function(data)
			{
			if(data.result=="success"){
            //alert('Operation Stopped Successfully');
			//window.location.reload();
			}else{
			alert("Problem in Starting Operation");
			}
			}
	});

}

var getAllOperations = function(){
	$.ajax
	({
			url:"../../service/common/getAllOperations",
			type:'post',
			success:function(data)
			{
			console.log(data);
			globalOprList=data;
			//if(data.length!=0){
			showOperationAndDeviceDetails();
			//}
			/*else{
			$('#oprAndDeviceDetailsDiv').hide();
			$('#myAddPopup').css("display","block");
			}*/
			}
	});
}

var showOperationAndDeviceDetails = function(){
	if(globalOprList.length!=0){
		$('#list_operation_body').append('<tr><td>'+globalOprList[0].name+'</td><td>'+globalOprList[0].note+'</td><td>'+globalOprList[0].inserttime+'</td><td><input type="text" id="gps_acc" value="50" style="width: 40px;margin-top: 4px;">(mtr)&nbsp;<button class="btn btn-default btn-operation" id="update_gps_acc">Update</button></td><td><button class="btn btn-default btn-operation" id="sysConfigButton" onclick="displaySysConfigModal()">System Configuration</button><input type="button" class="btn btn-default btn-operation" value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile()" /><button class="btn btn-default btn-operation" id="addNewOprButton" onclick="displayAddOperationModal()">Start/Stop Operation</button><button  class="btn btn-default btn-operation" id="addTarget" onclick="displayAddTargetModal()">Add Target</button><button  class="btn btn-default btn-operation" id="addCell" onclick="displayAddCellModal()">Add Cell</button><button  class="btn btn-default btn-operation" id="scannedCells" onclick="displayScannedCellPage()">Manual Tracking</button><button class="btn btn-default btn-operation" id="system_location_button" onclick="showSyslocModal()">System Location</button> </td></tr>');
	    $('#oprAction').append('<input type="button" class = "btn btn-default opr_button" value="Clear" id="stopOpr" onclick="deleteAllOperation()">');
	}else{
		$('#list_operation_body').append('<tr><td></td><td></td><td></td><td><input type="text" id="gps_acc" value="50" style="width: 40px;margin-top: 4px;">(mtr)&nbsp;<button class="btn btn-default btn-operation" id="update_gps_acc">Update</button></td><td><button class="btn btn-default btn-operation" id="sysConfigButton" onclick="displaySysConfigModal()">System Configuration</button><input type="button" class="btn btn-default btn-operation" value="Antenna Profile" id="antennaProfileButton" onclick="getAndDisplayAntennaProfile()" /><button class="btn btn-default btn-operation" id="addNewOprButton" onclick="displayAddOperationModal()">Start/Stop Operation</button><button  class="btn btn-default btn-operation" id="addTarget" onclick="displayAddTargetModal()">Add Target</button><button  class="btn btn-default btn-operation" id="addCell" onclick="displayAddCellModal()">Add Cell</button><button  class="btn btn-default btn-operation" id="scannedCells" onclick="displayScannedCellPage()">Manual Tracking</button><button class="btn btn-default btn-operation" id="system_location_button" onclick="showSyslocModal()">System Location</button> </td></tr>');
	}
var rows='';
var oprType='';
var oprDuration='';
for(var i=0;i<globalOprList.length;i++){
if(globalOprList[i].opr_type=='1'){
	oprType='Scheduler';
	oprDuration=globalOprList[i].duration;
}else{
	oprType='Automatic';
	oprDuration='NA';
}
if(globalOprList[i].status=='0'){
	rows+='<tr><td>'+globalOprList[i].name+'</td><td>'+globalOprList[i].note+'</td><td>'+oprType+'</td>';
	rows+='<td>'+globalOprList[i].opr+'</td><td>'+oprDuration+'</td><td>'+globalOprList[i].distance+'</td><td>Stopped</td><td>'+globalOprList[i].inserttime+'</td><td>'+globalOprList[i].stoptime+'</td><td><input class = "btn btn-default opr_button" type="button" value="Delete" id="stopOpr" onclick="deleteOperation(\''+globalOprList[i].id+'\')"></input><input class = "btn btn-default opr_button" type="button" value="Download" id="downloadOprData" onclick="downloadOperationData(\''+globalOprList[i].id+'\')"></input></td></tr>';
}else if(globalOprList[i].status=='1'){
	rows+='<tr><td>'+globalOprList[i].name+'</td><td>'+globalOprList[i].note+'</td><td>'+oprType+'</td>';
	rows+='<td>'+globalOprList[i].opr+'</td><td>'+oprDuration+'</td><td>'+globalOprList[i].distance+'</td><td>Started</td><td>'+globalOprList[i].inserttime+'</td><td>'+globalOprList[i].stoptime+'</td><td><input class = "btn btn-default opr_button" type="button" value="Stop" id="stopOpr" onclick="stopOperation()"></input><input class = "btn btn-default opr_button" type="button" value="Download" id="downloadOprData" onclick="downloadOperationData(\''+globalOprList[i].id+'\')"></input></td></tr>';	
}else{
	rows+='<tr><td>'+globalOprList[i].name+'</td><td>'+globalOprList[i].note+'</td><td>'+oprType+'</td>';
	rows+='<td>'+globalOprList[i].opr+'</td><td>'+oprDuration+'</td><td>'+globalOprList[i].distance+'</td><td>Interrupted</td><td>'+globalOprList[i].inserttime+'</td><td>'+globalOprList[i].stoptime+'</td><td><input type="button" value="Resume" class = "btn btn-default opr_button" id="resumeOpr" onclick="restartOperation(\''+globalOprList[i].id+'\')"></input><input class = "btn btn-default opr_button" type="button" value="Delete" id="stopOpr" onclick="deleteOperation(\''+globalOprList[i].id+'\')"></input><input type="button" class = "btn btn-default opr_button" value="Download" id="downloadOprData" onclick="downloadOperationData(\''+globalOprList[i].id+'\')"></input></td></tr>';	
	
}
}
$('#list_table_operations_body').html(rows);
getHwCapabilityTypes();
getUseTypes();
getGpsAcc();
}

var displayScannedCellPage = function(){
window.location.href = 'configupdate.jsp';
}

var updateGpsAcc  = function()
{
	var val = $("#gps_acc").val();
	
	if(!isNaN(val) && parseInt(val)>=0)
	{
		$.ajax
		({
			url:"../../service/common/addGPSAccurecyNumber",
			data:{"accuracy":val},
			type:"post",
			dataType:"json",
			success:function(data)
			{
				if(data.result =="success")
				{
					alert("GPS accuracy updated successfully");
					location.reload();
				}
				else
				{
					alert("Unable to update GPS accuracy");
					location.reload();
				}
			}
		});
	
	}
	else
	{
		alert("Please Provide Valid Value");
	}
	
	
}



var getGpsAcc  = function()
{
		//var val = $("#gps_acc").val();
		$.ajax
		({
			url:"../../service/common/getgpsaccuracy",
			//data:{"accuracy":val},
			type:"post",
			dataType:"json",
			success:function(data)
			{
				$("#gps_acc").val(data[0].accuracy);
			}
		});
}

var displayPurgeDataModal = function(){
getAllDataTypes();
}

function getAllDataTypes(){
	$.ajax({
		url:"../../Operations",
		data:{"methodName":"getAllDataTypes"},
		type:'post',
		success:function(data)
		{
		var allDataTypes=JSON.parse(data);
		$('#dataTypeSelect').empty();
		var options='';
		for(var i=0;i<allDataTypes.length;i++){
		options+='<option value="'+allDataTypes[i].related_tables+'">'+allDataTypes[i].name+'</option>';
		}
		$('#dataTypeSelect').append(options);
		$('#purgeDataModal').modal('show');
		}
	});
}


var displaySysConfigModal = function(){
	getSectorAntennaStatusForScanning();
}

var getSectorAntennaStatusForScanning =function(){
	$.ajax
	({
			url:"../../service/common/getsectorantennastatusforscanning",
			type:'get',
			success:function(data)
			{
				getAllSysConfigs(data.secAntStatusForScan);
			}
	});
}

function getAllSysConfigs(secAntStatusForScan){
	$.ajax({
		url:"../../service/common/getallsysconfigs",
		data:{"methodName":"getAllDataTypes"},
		type:'post',
		success:function(data)
		{
			$('#systemProperties').html('');
			var isIntegrated=true;
			var rows='';
			for(var i=0;i<data.length;i++){
				if(data[i].status=="t"){
				rows+='<tr id="sysprop_'+data[i].key+'">'+
					'<td><label style="font-size: 15px;">'+data[i].display_name+'</label></td>';
					
				if(data[i].key=='cue_hummer_to_falcon'){
					rows+='<td>'+
					'<select type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;">';
					if(data[i].value.toLowerCase()=='enable'){
						rows+='<option value="Enable" selected>Enable</option>'+
							'<option value="Disable">Disable</option>';
					}else{
						rows+='<option value="Enable">Enable</option>'+
							'<option value="Disable" selected>Disable</option>';
					}
					rows+='</select></td>';
				}else if(data[i].key=='coverage' || data[i].key=='timeout' || data[i].key=='validity'){
					rows+='<td><select type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;">';
					for(var kmCount=1;kmCount<=10;kmCount++){
						if(kmCount==parseInt(data[i].value)){
							rows+='<option value="'+kmCount+'" selected>'+kmCount+'</option>';
						}else{
							rows+='<option value="'+kmCount+'">'+kmCount+'</option>';
						}
					}
					rows+='</select></td>';
				}else if(data[i].key=='system_mode'){
					rows+='<td>'+
					'<select type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;">';
					//if(data[i].code=='0'){
						rows+='<option value="0" selected>Stationary</option>';
							//'<option value="1">Moving</option>';
					//}
					/*else{
						rows+='<option value="0">Stationary</option>'+
							'<option value="1" selected>Moving</option>';
					}*/
					rows+='</select></td>';
				}else if(data[i].key=='system_type'){
					rows+='<td>'+
					'<select type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;">';
					if(data[i].code=='0'){
						rows+='<option value="0" selected>Standalone</option>'+
							//'<option value="1">Falcon only</option>'+
							'<option value="2">Integrated</option>';
						isIntegrated=false;
					}else if(data[i].code=='1'){
						rows+='<option value="0">Standalone</option>'+
						//'<option value="1" selected>Falcon only</option>'+
						'<option value="2">Integrated</option>';
						isIntegrated=false;
					}else{
						rows+='<option value="0">Standalone</option>'+
						//'<option value="1">Falcon only</option>'+
						'<option value="2" selected>Integrated</option>';
					}
					rows+='</select></td>';
				}else if(data[i].key=='gps_node'){
					rows+='<td>'+
					'<select type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;">';
					//if(systemTypeCode==0){
						if(data[i].code=='0'){
							rows+='<option value="0" selected>System Manager</option>'+
								  '<option value="1">STRU</option>'+
								  '<option value="2">Hummer</option>';
						}else if(data[i].code=='1'){
							rows+='<option value="0">System Manager</option>'+
								  '<option value="1" selected>STRU</option>'+
								  '<option value="2">Hummer</option>';
						}else{
							rows+='<option value="0">System Manager</option>'+
							  '<option value="1">STRU</option>'+
							  '<option value="2" selected>Hummer</option>';
						}
/*					}else{
						if(data[i].code=='0'){
							rows+='<option value="0" selected>System Manager</option>'+
								'<option value="2">Hummer</option>';
						}else{
							rows+='<option value="0">System Manager</option>'+
							'<option value="2" selected>Hummer</option>';
						}
					}*/
					rows+='</select></td>';
				}else if(data[i].key=='schedulertime'){
					if(secAntStatusForScan==true){
						rows+='<td id="td_text_'+data[i].key+'"><input type="number" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;" value="'+data[i].value+'"></input></td>';	
					}else{
						rows+='<td id="td_text_'+data[i].key+'"><input type="number" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;" value="'+data[i].value+'" disabled></input></td>';	
					}
				}
				
				else if(data[i].key.localeCompare("operation_mode")==0)
				{
					rows+='<td><select type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;">';
					if(data[i].value==1){
						rows+='<option value="1" selected >Only Tracking</option>';
						rows+='<option value="2">Tracking With GPS</option>';
					}
					else{
						rows+='<option value="1">Only Tracking</option>';
						rows+='<option value="2" selected>Tracking With GPS</option>';
					}
					rows+='</select></td>';
					//rows+='<td id="td_button_'+data[i].key+'"><button class="btn btn-default"   id="configButton'+data[i].id+'" onclick="updateSysConfig(\''+data[i].id+'\')" value="'+data[i].key+'" style="" >Update</button></td>';
					
					
				}
				
				else if(data[i].key.localeCompare("hold_time")==0)
				{
					
					if(data[10].value==1)
						{
						rows+='<td><select disabled type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;">';
						rows+='<option value="0" >0</option>';
						}
					else{
						rows+='<td><select type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;">';
						for(k=10;k<65;k=k+5){
							if(k==parseInt(data[i].value)){
								rows+='<option value="'+k+'" selected>'+k+'</option>';
							}else
								{
							rows+='<option value="'+k+'">'+k+'</option>';
						}
						}
					}
					rows+='</select></td>';
					//rows+='<td id="td_button_'+data[i].key+'"><button class="btn btn-default"   id="configButton'+data[i].id+'" onclick="updateSysConfig(\''+data[i].id+'\')" value="'+data[i].key+'" style="" >Update</button></td>';
					
					
				}
				else{
					rows+='<td><input type="number" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 260px;" max="900" min="30" value="'+data[i].value+'" ></input></td>';
					                		
				}
				
				
				if(data[i].key=='schedulertime' && secAntStatusForScan==false){
					rows+='<td id="td_button_'+data[i].key+'"><button class="btn btn-default"   id="configButton'+data[i].id+'" onclick="updateSysConfig(\''+data[i].id+'\')" value="'+data[i].key+'" style="background-color: grey; border-color: grey;" disabled>Update</button></td>';//+
					'</tr>';
				}else{
					rows+='<td id="td_button_'+data[i].key+'"><button class="btn btn-default"   id="configButton'+data[i].id+'" onclick="updateSysConfig(\''+data[i].id+'\')" value="'+data[i].key+'">Update</button></td>';//+
					'</tr>';
				}
				}
			}
			/*$('#trackTimeLabel').text(data[0].display_name);
			$('#trackTimeConfig').val(data[0].value);
			$('#scannerPeriodicityLabel').text(data[1].display_name);
			$('#scannerPeriodicityConfig').val(data[1].value);
			$('#queueStatusLabel').text(data[2].display_name);
			$('#queueStatusConfig').val(data[2].value);*/
			$('#systemProperties').html(rows);
			if(!isIntegrated){
				$('#sysprop_cue_hummer_to_falcon').css('display','none');
				$('#sysprop_timeout').css('display','none');
				$('#sysprop_validity').css('display','none');
			}
		$('#sysConfigModal').modal('show');
		}
	});
}

var displayAddCellModal = function(){
	$('#add_plmn').modal('show');
}

var getHwCapabilityTypes = function(){
$('#hwCapabilityType').empty();

$('.hwCapabilityTypeeOf').empty();
$('.hwCapabilityTypeePpf').empty();
$('.hwCapabilityTypeSpf').empty();




var options='<option value="select">Select</option>';
	$.ajax
	({
			url:"../../service/common/getHwCapabilityTypes",
			type:'post',
			success:function(data)
			{
			for(var i=0;i<data.length;i++){
				if(systemTypeCode!=2 &&(data[i].name.toLowerCase()=='hummer' || data[i].name.toLowerCase()=='finley')){
					continue;
				}
				if(systemTypeCode==2 && data[i].name.toLowerCase()=='stru'){
					continue;
				}
				if(systemTypeCode==0 && data[i].name.toLowerCase()=='switch'){
					continue;
				}
			options+='<option value="'+data[i].id+'">'+data[i].name+'</option>';
			}
			$('#hwCapabilityType').append(options);
			$('.hwCapabilityTypeeOf').append(options);
			$('.hwCapabilityTypeePpf').append(options);
			$('.hwCapabilityTypeSpf').append(options);
			
			
			}
	});
}

var getUseTypes = function(){
$('#useType').empty();
var options='<option value="select">Select</option>';
	$.ajax
	({
			url:"../../service/common/getUseTypes",
			type:'post',
			success:function(data)
			{
			for(var i=0;i<data.length;i++){
				if(systemTypeCode!=2 && (data[i].name=='Finley' || data[i].name=='Hummer')){
					continue;
				}
				
				/*if(systemTypeCode==2 && data[i].name=='PTZ'){
					continue;
				}*/
				if(systemTypeCode==0 && data[i].name=='Switch Controller'){
					continue;
				}
					options+='<option value="'+data[i].id+'" data-type="'+data[i].type+'" data-modal_id="'+data[i].modal_id+'" data-use_name="'+data[i].name+'">'+data[i].show_name+'</option>';
				
				}
			$('#useType').append(options);
			}
	});
}


var displayAddOperationModal = function(){
$('#addnewOperationModal').modal('show');
}


/*
 * This method will get all the bts present in to the system along with there
 * Staus (UP/Down)
 * */
var getbtsinfo = function()
{	
	$.ajax(
	{
		url:dirPath+"service/common/btsinfo",
		type:'post',
		success:function(data)
		{
			createRowsForBtsInfo(data);
			getScanSchedulerStatus();
		}
	});
}

var getScanSchedulerStatus = function(){
	$.ajax(
			{
				url:dirPath+"service/common/getscanschedulerstatus",
				type:'get',
				success:function(data)
				{
					if(data.result=="success"){
						if(data.message=="started"){
							$('#scanStatus').removeClass('fa fa-play-circle');
							$('#scanStatus').addClass('fa fa-stop-circle');
							$('#scanStatus').attr('data-status','started');
						}else if(data.message=="stopped"){
							$('#scanStatus').removeClass('fa fa-stop-circle');
							$('#scanStatus').addClass('fa fa-play-circle');
							$('#scanStatus').attr('data-status','stopped');
						}
					}
				}
			});
}

createRowsForBtsInfo =function(data)
{
	
	$("#list_table_body").html("");
	var total = data.length;
	//$("#tot_bts").html(total);
	$("#run_bts").html("0");
	$("#wait_bts").html("0");
	$("#nor_bts").html("0");
	$("#down_bts").html("0");
	var up = 0;
	
	var down = 0;
	var adminstate=["NA","LOCK","UNLOCK"];
	var groupNodeCount=0;
	
	var row="";
	var addedRow="";
	globalBtsDevices=[];
	globalBtsDevicesStatus["1.1.1.1"]="NOT REACHABLE";
	globalBtsDevicesStatus["2.2.2.2"]="NOT REACHABLE";
	globalDeviceData=[];
	for(var i=0;i<data.length;i++)
	{	
		globalBtsDevices.push(data[i].ip);
		globalDeviceData.push(data[i]);
		
		globalBtsDevicesStatus[data[i].ip]=data[i].status;
		
		if(systemTypeCode!=2 && (data[i].use_type_name.toLowerCase()=="finley" || data[i].use_type_name.toLowerCase()=="hummer"))
		{
			continue;
		}
		
		//if(systemTypeCode==2 && data[i].use_type_name.toLowerCase()=="ptz"){
//		
//			continue;
//		}
		
		if(systemTypeCode==0 && data[i].use_type_name.toLowerCase()=="switch controller"){
			continue;
		}
		
		if(data[i].use_type_name.toLowerCase().indexOf("3g")!=-1 || data[i].use_type_name.toLowerCase().indexOf("4g")!=-1)
		{
			for(var j=1;j<=3;j++)
			{
				var colorStaus={};
				if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
					colorStaus = colorAndStatusForStatusCodeRest(parseInt(data[i].statuscode));
				}else{
					colorStaus=globalStatus;
				}
				var lockUnlockImageObj=getLockUnlockImage(data[i].adminstate);
				if(j==1)
				{
					row+="<tr style='z-index:999;'>";	
				}
				else
				{
					//if(data[i].ip == "1.1.1.1" || data[i].ip == "0.0.0.0")
					if(false)
					{
						row+="<tr style='display:none;'>";
					}
					else
					{
						row+="<tr>";
					}	
				}
		
		
				if(j==1)
				{
					row+="<td rowspan=\"3\">"+data[i].show_name+"</td>";
				}
				if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
					var rowOprName=data[i].config_applied_status=='y'?data[i].oprname:'NA';
				row+="<td>"+data[i].ip+" ("+data[i].dname+")</td>"+
						"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
						"<td>"+data[i].hw_capability_name+"</td>"+
						"<td>"+data[i].sw_version+"</td>"+
						"<td style=\"display:none;\"></td>"+
						"<td><span id=\"networkConfig_"+data[i].ip.split('.').join("")+"\">"+rowOprName+"</span>(<a href=\"#\" onclick=\"getNCells('"+data[i].ip+"','"+data[i].code+"')\" style=\"color: #337ab7;\">Show Cells</a>)</td>";
						//"<td><img title='"+lockUnlockImageObj.title+"'src=\""+lockUnlockImageObj.image+"\" onclick=\"setLockUnlockOperation('"+data[i].ip+"','"+lockUnlockImageObj.flag+"','3g')\" style=\"width: 22px;height: 22px;cursor: pointer;vertical-align: top;\"></img></td>";
						row+="<td>"+data[i].systemmanager+"</td>";
						if(data[i].use_type_name.toLowerCase().indexOf("4g")!=-1)
							row+="<td><img title='"+lockUnlockImageObj.title+"'src=\""+lockUnlockImageObj.image+"\" onclick=\"setLockUnlockOperation('"+data[i].ip+"','"+lockUnlockImageObj.flag+"','4g')\" style=\"width: 22px;height: 22px;cursor: pointer;vertical-align: top;\"></img></td>";
						else
							row+="<td><img title='"+lockUnlockImageObj.title+"'src=\""+lockUnlockImageObj.image+"\" onclick=\"setLockUnlockOperation('"+data[i].ip+"','"+lockUnlockImageObj.flag+"','3g')\" style=\"width: 22px;height: 22px;cursor: pointer;vertical-align: top;\"></img></td>";
				}else{
				var rowOprName=data[i].config_applied_status=='y'?data[i].oprname:'NA';
				row+="<td style=\"display: none;\">"+data[i].ip+" ("+data[i].dname+")</td>"+
				"<td style='display: none;background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
				"<td style=\"display: none;\">"+data[i].hw_capability_name+"</td>"+
				"<td style=\"display: none;\">"+data[i].sw_version+"</td>"+
				"<td style=\"display:none;\"></td>"+
			    "<td style=\"display: none;\"><span id=\"networkConfig_"+data[i].ip.split('.').join("")+"\">"+rowOprName+"</span>(<a href=\"#\" onclick=\"getNCells('"+data[i].ip+"','"+data[i].code+"')\" style=\"color: #337ab7;\">Show Cells</a>)</td>";
				if(data[i].use_type_name.toLowerCase().indexOf("4g")!=-1)
					row+="<td style=\"display: none;\"><img title='"+lockUnlockImageObj.title+"'src=\""+lockUnlockImageObj.image+"\" onclick=\"setLockUnlockOperation('"+data[i].ip+"','"+lockUnlockImageObj.flag+"','4g')\" style=\"width: 22px;height: 22px;cursor: pointer;vertical-align: top;\"></img></td>";
				else
					row+="<td style=\"display: none;\"><img title='"+lockUnlockImageObj.title+"'src=\""+lockUnlockImageObj.image+"\" onclick=\"setLockUnlockOperation('"+data[i].ip+"','"+lockUnlockImageObj.flag+"','3g')\" style=\"width: 22px;height: 22px;cursor: pointer;vertical-align: top;\"></img></td>";
				}
				
				if(j==1)
				{
					if(data[i].use_type_name.toLowerCase().indexOf("4g")!=-1)
						row+="<td rowspan=\"3\"><i style='cursor:pointer;' title='Configuration' class=\"ti-settings\" onclick=\"get4gConfigurationPage('"+data[i].grp_name+"','"+data[i].ip+"')\"></i>";
					else
						row+="<td rowspan=\"3\"><i style='cursor:pointer;' title='Configuration' class=\"ti-settings\" onclick=\"get3gConfigurationPage('"+data[i].grp_name+"','"+data[i].ip+"')\"></i>";
					if(sessionParams.role.toLowerCase()=='superadmin'){
						row+="&nbsp;|&nbsp;<i style='cursor: pointer;color: red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i>";
					}
					row+="</td>";
				}
				row+="</tr>";
				if(j!=3)
				{
					i++;
				}
				$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
				$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
				$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
				$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);
			}
		}else if(data[i].use_type_name.toLowerCase().indexOf("network scanner")!=-1){
			globalNetscannerIp = data[i].ip;
			var colorStaus={};
			if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
				colorStaus = colorAndStatusForStatusCodeNetscan(parseInt(data[i].statuscode));
			}else{
				colorStaus=globalStatus;
			}
			var scanImage='';
		if(data[i].soft_state=='2')
		{
			scanImage='<img title="Scanning" src="../../resources/images/scanner2.gif" style="width: 26px;height:26px;float: right;"></img>';
		}
		//style=\"width: 26px;height: 26px;cursor: pointer;vertical-align: top;\"
		var lockUnlockImageObj=getLockUnlockImage(data[i].adminstate);
		var rowOprName=data[i].config_applied_status=='y'?data[i].oprname:'NA';
	    row+="<tr>";
		row+="<td>"+data[i].show_name+"</td>"+
			"<td>"+data[i].ip+"</td>"+
			"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+""+scanImage+"</td>"+
			"<td>"+data[i].hw_capability_name+"</td>"+
			"<td>"+data[i].sw_version+"</td>"+
			"<td style=\"display:none;\"></td>"+
			"<td>NA</td>"+
			"<td>"+data[i].systemmanager+"</td>"+
			//"<td colspan=\"2\"><i style='cursor:pointer;font-size:23px' title='Configuration' class=\"ti-settings\" onclick=\"getNetworkScannerConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>&nbsp;|&nbsp;<i style='cursor:pointer;font-size:23px;color:red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i></td></tr>";
			"<td></td><td colspan=\"2\"><i style='cursor: pointer;' title='Configuration' class=\"ti-settings\" onclick=\"getNetworkScannerConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>&nbsp;|&nbsp;<i id='scanStatus' data-status='' style='cursor: pointer;font-size: 22px;' class=\"\"  onclick=\"doScanOperations(this)\"></i>";
			
	
		if(sessionParams.role.toLowerCase()=='superadmin'){
			row+="&nbsp;|&nbsp;<i style='cursor: pointer;color: red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i>";
		}
		
		row+="</td></tr>";
		
		$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
		$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
		$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
		$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);
		}else if(data[i].code==10){
			var colorStaus={};
			if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
				colorStaus = colorAndStatusForStatusCodeNetscan(parseInt(data[i].statuscode));
			}else{
				colorStaus=globalStatus;
			}
		//style=\"width: 26px;height: 26px;cursor: pointer;vertical-align: top;\"
	    row+="<tr>";
		row+="<td>"+data[i].show_name+"</td>"+
			"<td>"+data[i].ip+"</td>"+
			"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
			"<td>"+data[i].hw_capability_name+"</td>"+
			"<td>"+data[i].sw_version+"</td>"+
			"<td style=\"display:none;\"></td>"+
			"<td>NA</td>"+
			"<td>"+data[i].systemmanager+"</td>"+
			//"<td colspan=\"2\"><i style='cursor:pointer;font-size:23px' title='Configuration' class=\"ti-settings\" onclick=\"getNetworkScannerConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>&nbsp;|&nbsp;<i style='cursor:pointer;font-size:23px;color:red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i></td></tr>";
	//"<td></td><td colspan=\"2\">";
	//	"<td></td><td colspan=\"2\">"+

			"<td></td><td colspan=\"2\">" +
			"<span style='cursor: pointer;bottom: 4px;' class=\"fa-stack\">"+
			"<i style='cursor: pointer; ' title='Reboot'  class=\"fa fa-circle fa-stack-2x icon-4x\" ></i> <i class=\"fa fa-undo fa-stack-1x fa-inverse\" title='Reboot'    onclick=\"restartOctasic('"+data[i].ip+"');\" ></i>" +
			"</span>"+
	    	"&nbsp;|&nbsp;<i style='cursor: pointer;color: red;' title='Shutdown' class=\"ti-power-off\"  onclick=\"shutdownoctasic('"+data[i].ip+"')\"></i>";
	    	/*"<td>" +
    		"<span style='cursor: pointer;' class=\"fa-stack\">"+
     		"<i style='cursor: pointer;' title='shutdown'  class=\"fa fa-circle fa-stack-2x icon-4x\" ></i> <i class=\"fa fa-undo fa-stack-1x fa-inverse\" title='Shutdown'    onclick=\"shutdownoctasic();\" ></i>" ;
	    	*/
		
		if(sessionParams.role.toLowerCase()=='superadmin'){
			row+="&nbsp;|&nbsp;<i style='cursor: pointer;color: red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i>";
		}
		
		row+="</td></tr>";
		$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
		$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
		$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
		$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);

		}
		else if(data[i].use_type_name.toLowerCase().indexOf("ptz")!=-1){
			var colorStaus={};
			if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
				colorStaus = colorAndStatusForStatusCodeNetscan(parseInt(data[i].statuscode));
			}else{
				colorStaus=globalStatus;
			}
		//style=\"width: 26px;height: 26px;cursor: pointer;vertical-align: top;\"
		var lockUnlockImageObj=getLockUnlockImage(data[i].adminstate);
		var rowOprName=data[i].config_applied_status=='y'?data[i].oprname:'NA';
	    row+="<tr>";
		row+="<td>"+data[i].show_name+"</td>"+
			"<td>"+data[i].ip+"</td>"+
			"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
			"<td>"+data[i].hw_capability_name+"</td>"+
			"<td>"+data[i].sw_version+"</td>"+
			"<td style=\"display:none;\"></td>"+
			"<td>NA</td>"+
			"<td>"+data[i].systemmanager+"</td>"+
			//"<td colspan=\"2\"><i style='cursor:pointer;font-size:23px' title='Configuration' class=\"ti-settings\" onclick=\"getNetworkScannerConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>&nbsp;|&nbsp;<i style='cursor:pointer;font-size:23px;color:red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i></td></tr>";
			"<td></td><td colspan=\"2\">";
		
		row+="<i style='cursor: pointer;' title='Configuration' class=\"ti-settings\" onclick=\"getPTZConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>";

		
		if(sessionParams.role.toLowerCase()=='superadmin'){
			row+="&nbsp;|&nbsp;<i style='cursor: pointer;color: red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i>";
		}
		
		row+="</td></tr>";
		
		$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
		$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
		$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
		$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);
		}
		else if(data[i].use_type_name.toLowerCase().indexOf("bms")!=-1){
			var colorStaus={};
			if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
				colorStaus = colorAndStatusForStatusCodeNetscan(parseInt(data[i].statuscode));
			}else{
				colorStaus=globalStatus;
			}
			//style=\"width: 26px;height: 26px;cursor: pointer;vertical-align: top;\"
			var lockUnlockImageObj=getLockUnlockImage(data[i].adminstate);
			var rowOprName=data[i].config_applied_status=='y'?data[i].oprname:'NA';
		    row+="<tr>";
			row+="<td>"+data[i].show_name+"</td>"+
				"<td>"+data[i].ip+"</td>"+
				"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
				"<td>"+data[i].hw_capability_name+"</td>"+
				"<td>"+data[i].sw_version+"</td>"+
				"<td style=\"display:none;\"></td>"+
				"<td>NA</td>"+
				"<td>"+data[i].systemmanager+"</td>"+
				//"<td colspan=\"2\"><i style='cursor:pointer;font-size:23px' title='Configuration' class=\"ti-settings\" onclick=\"getNetworkScannerConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>&nbsp;|&nbsp;<i style='cursor:pointer;font-size:23px;color:red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i></td></tr>";
				"<td></td><td colspan=\"2\">";
			
			row+="<i style='cursor: pointer;' title='Configuration' class=\"ti-settings\" onclick=\"getBMSConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>";
	
			
			if(sessionParams.role.toLowerCase()=='superadmin'){
				row+="&nbsp;|&nbsp;<i style='cursor: pointer;color: red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i>";
			}
			
			row+="</td></tr>";
			
			$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
			$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
			$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
			$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);
		}
		
		else if(data[i].use_type_name.toLowerCase().indexOf("jammer")!=-1){
			var colorStaus={};
			jammingMode=data[i].statuscode;
			if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
				colorStaus = colorAndStatusForStatusCodeNetscan(parseInt(data[i].statuscode));
			}else{
				colorStaus=globalStatus;
			}
			//style=\"width: 26px;height: 26px;cursor: pointer;vertical-align: top;\"
			var lockUnlockImageObj=getLockUnlockImage(data[i].adminstate);
			var rowOprName=data[i].config_applied_status=='y'?data[i].oprname:'NA';
			jammer_hw_capability_name=data[i].hw_capability_name;
			JammerBand = jammer_hw_capability_name.split("-")[1];
		    row+="<tr>";
			row+="<td>"+data[i].show_name+"</td>"+
				"<td>"+data[i].ip+"</td>"+
				"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
				"<td>"+data[i].hw_capability_name+"</td>"+
				"<td>"+data[i].sw_version+"</td>"+
				"<td style=\"display:none;\"></td>"+
				"<td>NA</td>"+
				"<td>"+data[i].systemmanager+"</td>"+
				//"<td colspan=\"2\"><i style='cursor:pointer;font-size:23px' title='Configuration' class=\"ti-settings\" onclick=\"getNetworkScannerConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>&nbsp;|&nbsp;<i style='cursor:pointer;font-size:23px;color:red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i></td></tr>";
				"<td></td><td colspan=\"2\">";
			
			row+="<i style='cursor: pointer;' title='Configuration' class=\"ti-settings\" onclick=\"getJammerConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>";
	
			
			if(sessionParams.role.toLowerCase()=='superadmin'){
				row+="&nbsp;|&nbsp;<i style='cursor: pointer;color: red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i>";
			}
			
			row+="</td></tr>";
			
			$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
			$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
			$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
			$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);
		}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		else if(data[i].use_type_name.toLowerCase().indexOf("controller")!=-1 || data[i].use_type_name.toLowerCase().indexOf("ptz")!=-1 || data[i].use_type_name.toLowerCase().indexOf("finley")!=-1 || data[i].use_type_name.toLowerCase().indexOf("hummer")!=-1){
			var colorStaus={};
			if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
				colorStaus = colorAndStatusForStatusCodeNetscan(parseInt(data[i].statuscode));
			}else{
				colorStaus=globalStatus;
			}
		//style=\"width: 26px;height: 26px;cursor: pointer;vertical-align: top;\"
		var lockUnlockImageObj=getLockUnlockImage(data[i].adminstate);
		var rowOprName=data[i].config_applied_status=='y'?data[i].oprname:'NA';
	    row+="<tr>";
		row+="<td>"+data[i].show_name+"</td>"+
			"<td>"+data[i].ip+"</td>"+
			"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
			"<td>"+data[i].hw_capability_name+"</td>"+
			"<td>"+data[i].sw_version+"</td>"+
			"<td style=\"display:none;\"></td>"+
			"<td>NA</td>"+
			"<td>"+data[i].systemmanager+"</td>"+
			//"<td colspan=\"2\"><i style='cursor:pointer;font-size:23px' title='Configuration' class=\"ti-settings\" onclick=\"getNetworkScannerConfigurationPage('"+data[i].ip+"','"+data[i].b_id+"','"+data[i].sytemid+"')\"></i>&nbsp;|&nbsp;<i style='cursor:pointer;font-size:23px;color:red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i></td></tr>";
			"<td></td><td style=\"text-align: center\" colspan=\"2\">";
		
		if(sessionParams.role.toLowerCase()=='superadmin'){
			row+="<i style='cursor: pointer;color: red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i>";
		}
		
		row+="</td></tr>";
		
		$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
		$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
		$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
		$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);
		}else{
			var colorStaus={};
			if(data[i].ip!='0.0.0.0' && data[i].ip!='1.1.1.1'){
				colorStaus = colorAndStatusForStatusCodeRest(parseInt(data[i].statuscode));
			}else{
				colorStaus = globalStatus;
			}
		var lockUnlockImageObj=getLockUnlockImage(data[i].adminstate);
		var rowOprName=data[i].config_applied_status=='y'?data[i].oprname:'NA';
	    row+="<tr>";
		row+="<td>"+data[i].show_name+"</td>"+
			"<td>"+data[i].ip+"</td>"+
			"<td style='background:"+colorStaus.color.backgroundColor+";color:"+colorStaus.color.fontColor+"'>"+data[i].status+"</td>"+
			"<td>"+data[i].hw_capability_name+"</td>"+
			"<td>"+data[i].sw_version+"</td>"+
			"<td style=\"display:none;\"></td>"+
			"<td><span id=\"networkConfig_"+data[i].ip.split('.').join("")+"\">"+rowOprName+"</span>(<a href=\"#\" onclick=\"getNCells('"+data[i].ip+"','"+data[i].code+"')\" style=\"color: #337ab7;\">Show Cells</a>)</td>"+
			//"<td colspan=\"2\"><img title='"+lockUnlockImageObj.title+"'src=\""+lockUnlockImageObj.image+"\" onclick=\"setLockUnlockOperation('"+data[i].ip+"','"+lockUnlockImageObj.flag+"','2g')\" style=\"width: 26px;height: 26px;cursor: pointer;vertical-align: top;\"></img>&nbsp;|&nbsp;<i style='cursor:pointer;font-size:23px' title='Configuration' class=\"ti-settings\" onclick=\"get2gConfigurationPage('"+data[i].ip+"')\"></i>&nbsp;|&nbsp;<i style='cursor:pointer;font-size:23px;color:red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i></td></tr>";
			"<td>"+data[i].systemmanager+"</td>"+
			"<td><img title='"+lockUnlockImageObj.title+"'src=\""+lockUnlockImageObj.image+"\" onclick=\"setLockUnlockOperation('"+data[i].ip+"','"+lockUnlockImageObj.flag+"','2g')\" style=\"width: 22px;height: 22px;cursor: pointer;vertical-align: top;\"></img></td><td colspan=\"2\"><i style='cursor: pointer;' title='Configuration' class=\"ti-settings\" onclick=\"get2gConfigurationPage('"+data[i].ip+"')\"></i>";
		
		if(sessionParams.role.toLowerCase()=='superadmin'){
			row+="&nbsp;|&nbsp;<i style='cursor: pointer;color: red;' title='Delete' class=\"ti-trash\"  onclick=\"deleteDevice('"+data[i].grp_name+"')\"></i>";
		}
		
		row+="</td></tr>";
		
		$("#run_bts").html(parseInt($("#run_bts").text())+colorStaus.count.run);
		$("#wait_bts").html(parseInt($("#wait_bts").text())+colorStaus.count.wait);
		$("#nor_bts").html(parseInt($("#nor_bts").text())+colorStaus.count.reachable);
		$("#down_bts").html(parseInt($("#down_bts").text())+colorStaus.count.down);
		}
	}
	total=parseInt($("#run_bts").text())+parseInt($("#wait_bts").text())+parseInt($("#nor_bts").text())+	parseInt($("#down_bts").text());
	$("#tot_bts").html(total);
	$("#list_table_body").append(row);
	
	/*$("#run_bts").html(colorStaus.count.run);
	$("#wait_bts").html(colorStaus.count.wait);
	$("#nor_bts").html(colorStaus.count.reachable);
	$("#down_bts").html(colorStaus.count.down);*/
}

/*
 * This function will return the color of cell and statuscount to idicate that if cell is up 
 * down ,runnning ,not rechable for Network Scanner Device
 * */
var colorAndStatusForStatusCodeNetscan = function(code)
{
	
	
	var color = {};
	color.fontColor = "black";
	color.backgroundColor = "white";
	var statusCount={};
	
	statusCount.run=0;
	statusCount.wait=0;
	statusCount.reachable=0;
	statusCount.down=0;
	
	var status={};
	
	switch(code)
	{
		case 0:
			color.backgroundColor = COLOR['wait'];
			statusCount.wait++;
			break;
		case 1:
			color.backgroundColor = COLOR['run'];
			statusCount.run++; 
			break;
		case 2:
			color.backgroundColor = COLOR['nor'];
			statusCount.reachable++;
			break;
		case 3:
			color.backgroundColor = COLOR['down'];
			statusCount.down++;
			break;
	
	}	
	status.color =color;
	status.count =statusCount;
	return status;
}

var colorAndStatusForStatusCodeSystemManager = function(code)
{
	var color = {};
	color.fontColor = "black";
	color.backgroundColor = "white";
	var statusCount={};
	
	statusCount.run=0;
	statusCount.wait=0;
	statusCount.reachable=0;
	statusCount.down=0;
	
	var status={};
	
	switch(code)
	{
		case 2:
			color.backgroundColor = COLOR['run'];
			statusCount.run++; 
			break;
		case 3:
			color.backgroundColor = COLOR['nor'];
			statusCount.reachable++;
			break;
		case 4:
			color.backgroundColor = COLOR['down'];
			statusCount.down++;
			break;
	
	}	
	status.color =color;
	status.count =statusCount;
	return status;
}

/*
 * This function will return the color of cell and statuscount to idicate that if cell is up 
 * down ,runnning ,not rechable for all devices except network scanner
 * */
var colorAndStatusForStatusCodeRest = function(code)
{
	
	
	var color = {};
	color.fontColor = "black";
	color.backgroundColor = "white";
	var statusCount={};
	statusCount.run=0;
	statusCount.wait=0;
	statusCount.reachable=0;
	statusCount.down=0;
	
	var status={};
	
	switch(code)
	{
		case 0:
			color.backgroundColor = COLOR['run'];
			statusCount.run++; 
			break;
		case 1:
			color.backgroundColor = COLOR['wait'];
			statusCount.wait++;
			break;
		case 2:
			color.backgroundColor = COLOR['nor'];
			statusCount.reachable++;
			break;
		case 3:
			color.backgroundColor = COLOR['down'];
			statusCount.down++;
			break;
	
	}	
	status.color =color;
	status.count =statusCount;
	return status;
}

var getAndDisplayAntennaProfile = function(deviceName){
globalDeviceName=deviceName;
getAntennaTypes();
getAntennaProfiles();
}

var getAntennaTypes = function(){
var options='';
$('#addAntennaType').html('');
	$.ajax
	({
			url:"../../service/common/getantennatypes",
			type:'post',
			success:function(data)
			{
				for(var i in data){
					options+='<option value="'+data[i].type_id+'">'+data[i].type_name+'</option>';
				}
				$('#addAntennaType').html(options);
				//getAntennaProfiles();
			}
	});
}

var getAntennaProfiles = function(){
var tableRow='';
if(systemTypeCode==0){
	$('#addAntennaProfileTable').css('display','table');
	$('#displayAntennaProfileTable').css('display','table');
	$('#selectAntennaProfile').css('display','inline-block');
	$('#antennaNote').css('display','inline');
	//hide table 
	//$('#addAntennaProfileTable').css('display','none');
	//$('#displayAntennaProfileTable').css('display','none');
	//$('#selectAntennaProfile').css('display','none');
	//$('#antennaNote').css('display','none');
}else{
	$('#addAntennaProfileTable').css('display','table');
	$('#displayAntennaProfileTable').css('display','table');
	$('#selectAntennaProfile').css('display','inline-block');
	$('#antennaNote').css('display','inline');
}
$('#setAntennaProfileModal').modal('show');
$('#displayAntennaProfileTable_body').html('');
$('#')
	$.ajax
	({
			url:"../../service/common/getAntennaProfiles",
			type:'post',
			success:function(data)
			{
				console.log('getantennaprofiles');
				console.log(data);
				var isChecked='';
				var isDisabled="disabled";
				$('#addAllAntenna').html();
				for(var i=0;i<data.length;i++){
					isDisabled="disabled";
					if((systemTypeCode==0 && (data[i].profile_name=="OV1" ||data[i].profile_name=="S1")) || systemTypeCode!=0){
						//'<td><input type="checkbox" value="'+data[i].id+'" name="editSameAntennaCheck" class="editSameAntennaCheck" id="editSameAntennaCheck'+data[i].id+'" onclick="editSameAntennaCheck(\''+data[i].id+'" disabled /)"></input></td>'
						isDisabled="enabled";	  
						}	
					tableRow='';
					tableRow='<tr id="antennaProfileRow'+data[i].id+'">';
					           if(data[i].inscanning=='t'){
								   isChecked='checked';
								   globalScanningAntenna.push(data[i].id);
							   }else{
								   isChecked='';
							   }
					           
					           tableRow+='<td><input type="checkbox" value="'+data[i].id+'" id="antennaScanning'+data[i].id+'" name="scanningAntennaCheck" class="scanningAntennaCheck editAntennaInput editSameAntennaInput'+data[i].id+'" onclick="checkBoxAntennaProfile(\''+data[i].id+'\')" disabled '+isChecked+' /></td>';
					           
							   if(data[i].intracking=='t'){
								   isChecked='checked';
							   }else{
								   isChecked='';
							   }
					tableRow+='<td><input type="checkbox" value="'+data[i].id+'" id="antennaTracking'+data[i].id+'" name="trackingAntennaCheck" class="trackingAntennaCheck editAntennaInput editSameAntennaInput'+data[i].id+'" disabled '+isChecked+' /></td>'+
							   '<td id="antennaName'+data[i].id+'">'+data[i].profile_name+'</td>'+
							   '<td id="antennaType'+data[i].id+'">'+data[i].type_name+'</td>'+
							   '<td id="antennaAngleCovered'+data[i].id+'">'+data[i].angle_covered+'</td>'+
							   '<td><input type="text" value="'+data[i].txpower+'" id="antennaTxPower'+data[i].id+'" class="editDisAntInput editSameAntennaInput'+data[i].id+'" disabled /></td>'+
							   '<td><input type="text" value="'+data[i].band+'" id="antennaBand'+data[i].id+'" class="editDisAntInput editSameAntennaInput'+data[i].id+'" disabled /></td>'+
							   '<td><input type="text" value="'+data[i].gain+'" id="antennaGain'+data[i].id+'" class="editDisAntInput editSameAntennaInput'+data[i].id+'" disabled /></td>'+
							   '<td><input type="text" value="'+data[i].elevation+'" id="antennaElevation'+data[i].id+'" class="editDisAntInput editSameAntennaInput'+data[i].id+'" disabled /></td>'+
							   '<td><input type="text" value="'+data[i].hbw+'" id="antennaHbw'+data[i].id+'" class="editDisAntInput editSameAntennaInput'+data[i].id+'" disabled /></td>'+
							   '<td><input type="text" value="'+data[i].vbw+'" id="antennaVbw'+data[i].id+'" class="editDisAntInput editSameAntennaInput'+data[i].id+'" disabled /></td>'+
							   '<td><input type="text" value="'+data[i].tilt+'" id="antennaTilt'+data[i].id+'" class="editDisAntInput editSameAntennaInput'+data[i].id+'" disabled /></td>'+
							   '<td><input type="text" value="'+data[i].azimuth+'" id="antennaAzimuth'+data[i].id+'" class="editDisAntInput editSameAntennaInput'+data[i].id+'" disabled /></td>'+
							   '<td>'+
							   '<select class="editDisAntInput editSameAntennaInput'+data[i].id+'" id="antennaTerrain'+data[i].id+'" disabled>'+
							   '<option value="Rural">Rural</option>'+
							   '<option value="Urban">Urban</option>'+
							   '<option value="SubUrban">SubUrban</option>'+
							   '<option value="Hilly">Hilly</option>'+
							   '<option value="Desert">Desert</option>'+
							   '</select>'+
							   '</td>'+
							   
							   '<td><input type="checkbox" '+ isDisabled +' " value="'+data[i].id+'" name="editSameAntennaCheck" class="editSameAntennaCheck" id="editSameAntennaCheck'+data[i].id+'" onclick="editSameAntennaCheck(\''+data[i].id+'\')"></input></td>'+
					 		   '</tr>';
					
					
							   $('#displayAntennaProfileTable_body').append(tableRow);
							   $('#antennaTerrain'+data[i].id).val(data[i].terrain);
					if(data[i].atype=='1'){
						$('#sectorAntennaAngleOffset').val(data[i].angle_offset);
						$('#s1Angle').val(data[i].antenna_angle);
					//	$('#antennaTypeRotate').val(data[i].antenna_type);
						if (data[i].antenna_type==1)
							$('#antennaTypeRotate').val(data[i].antenna_type);
							else
								$('#antennaTypeRotate').val(data[i].antenna_type);
							
					}
				}
				globalScanningAntenna.sort();
			globalAntennaProfileList=data;
			globalAntennaAngleOffset=$('#sectorAntennaAngleOffset').val();
			}
	});
}

var getAntennaProfileForAddPlmn = function(){
	$.ajax
	({
			url:"../../service/common/getAntennaProfiles",
			type:'post',
			success:function(data)
			{
				$('#addAllAntenna').html();
				var rowsAntenna='<option value="-1">All</option>';
				for(var i=0;i<data.length;i++){
					if (data[i].id!=22){
					rowsAntenna+='<option value='+data[i].id+'>'+data[i].profile_name+'</option>';
                   }
				}
				$('#addAllAntenna').html(rowsAntenna);
			}
	});
}

var updateAntennaProfile = function(){
		$.ajax
		({
			url:"../../service/common/checkoperation",
			type:'post',
			success:function(data)
			{
				if(data.result=="success"){
					updateAntennaProfileList();
				}
				else{
					alert("An Operation is in Started or Interrupted State");
				}
			}
		});
}

var updateAntennaProfileList = function(){
	var selectedScanningAntenna=[];
	var antennaSelectedCount=0;
	var editSameAntennaClass=$('.editSameAntennaCheck');
	var antennaAngleOffset=$('#sectorAntennaAngleOffset').val();
	var antennaTypeRotate=$('#antennaTypeRotate').val();
    var s1Angle=$('#s1Angle').val();
    
 //   if(s1Angle == 0){
 //   	antennaTypeRotate = 1;
 //   }else {
 //   	antennaTypeRotate = 2;
 //   }
    
		var antennaProfileJsonObj={};
		var antennaProfileList=[];
		var trackingAllowedOnSector=false;
		var trackingAllowedOnHorOmni=false;
		var trackingAllowedOnVerOmni=false;
		editSameAntennaClass.each(function(){
			var antennaProfileId=$(this).val();
			var antennaInTracking=$('#antennaTracking'+antennaProfileId).prop('checked');
			if(antennaInTracking){
				if($('#antennaType'+antennaProfileId).text()=='Sector'){
					trackingAllowedOnSector=true;
				}else if($('#antennaType'+antennaProfileId).text()=='V-Omni'){
					trackingAllowedOnVerOmni=true;
				}else{
					trackingAllowedOnHorOmni=true;
				}
			}
			if($(this).prop("checked")){
			var antennaProfileData={};
			antennaProfileData.id=antennaProfileId;
			
			
			antennaProfileData.inScanning=$('#antennaScanning'+antennaProfileId).prop('checked');
			if(antennaProfileData.inScanning){
				selectedScanningAntenna.push(antennaProfileId);
			}

			antennaProfileData.name=$('#antennaName'+antennaProfileId).text();
			antennaProfileData.inTracking=$('#antennaTracking'+antennaProfileId).prop('checked');
			antennaProfileData.txPower=$('#antennaTxPower'+antennaProfileId).val();
			antennaProfileData.band=$('#antennaBand'+antennaProfileId).val();
			antennaProfileData.gain=$('#antennaGain'+antennaProfileId).val();
			antennaProfileData.elevation=$('#antennaElevation'+antennaProfileId).val();
			antennaProfileData.hbw=$('#antennaHbw'+antennaProfileId).val();
			antennaProfileData.vbw=$('#antennaVbw'+antennaProfileId).val();
			antennaProfileData.tilt=$('#antennaTilt'+antennaProfileId).val();
			antennaProfileData.azimuth=$('#antennaAzimuth'+antennaProfileId).val();
			antennaProfileData.terrain=$('#antennaTerrain'+antennaProfileId).val();
			//antennaProfileData.antennaTypeRotate=antennaTypeRotate;
			//antennaProfileData.s1Angle=s1Angle;
			antennaProfileList.push(antennaProfileData);
			antennaSelectedCount++;
			}			
		});
		
		if(antennaSelectedCount==0 && antennaAngleOffset==globalAntennaAngleOffset){
			alert("No Antenna profile selected.Please select Antenna profile");
			return;
		}

		if((trackingAllowedOnSector && trackingAllowedOnVerOmni) || (trackingAllowedOnVerOmni && trackingAllowedOnHorOmni) || (trackingAllowedOnHorOmni && trackingAllowedOnSector)){
			alert("Tracking is only allowed on either Sector or Omni.Please check Antenna Profile and try again.");
			return;
		}
		var scanAntChangeStatus=false;
		selectedScanningAntenna=selectedScanningAntenna.sort();
		
		if(globalScanningAntenna.length!=selectedScanningAntenna.length){
			scanAntChangeStatus=true;
		}else{
			for(var i=0;i<globalScanningAntenna.length;i++){
				if(globalScanningAntenna[i]!=selectedScanningAntenna[i]){
					scanAntChangeStatus=true;
					break;
				}
			}
		}
		
		antennaProfileJsonObj.profileData=antennaProfileList;
		antennaProfileJsonObj.angleOffset=antennaAngleOffset;
		antennaProfileJsonObj.antennaTypeRotate=antennaTypeRotate;
		antennaProfileJsonObj.s1Angle=s1Angle;
		antennaProfileJsonObj.oldAngleOffset=globalAntennaAngleOffset;
		antennaProfileJsonObj.scanAntChangeStatus=scanAntChangeStatus;
		var antennaProfileJsonStr=JSON.stringify(antennaProfileJsonObj);
		
		$.ajax
		({
			url:"../../service/common/updateantennaprofile",
			data:{"antennaProfileData":antennaProfileJsonStr},
			type:'post',
			success:function(data)
			{
				if(data.result=="success"){
					alert('Antenna Profile updated Successfully');
				}
				else if(data.result=="error")
				{
					alert("Max/Min Angle Exceeded/Below for Antenna selected");
				}
				else{
					alert('Problem in updating Antenna Profile');
				}
			}
	});
}

var addAntennaProfile = function(){
	$.ajax
	({
			url:"../../service/common/checkoperation",
			type:'post',
			success:function(data)
			{
			if(data.result=="success"){
				var antennaScanning=$('#addAntennaScanning').prop('checked');
				var antennaTracking=$('#addAntennaTracking').prop('checked');
				var antennaName=$('#addAntennaName').val();
				var antennaTxPower=$('#addAntennaTxPower').val();
				var antennaType=$('#addAntennaType').val();
				var antennaBand=$('#addAntennaBand').val();
				var antennaGain=$('#addAntennaGain').val();
				var antennaElevation=$('#addAntennaElevation').val();
				var antennaHBW=$('#addAntennaHBW').val();
				var antennaVBW=$('#addAntennaVBW').val();
				var antennaTerrain=$('#addAntennaTerrain').val();
				var antennaTilt="";
				var antennaAzimuth="";
				antennaTilt=$('#addAntennaTilt').val();	
				antennaAzimuth=$('#addAntennaAzimuth').val();
				if(antennaName=="" || antennaTxPower=="" || antennaType=="" || antennaBand=="" || antennaGain=="" || antennaElevation=="" || antennaHBW=="" || antennaVBW=="" || antennaTilt=="" || antennaAzimuth=="" || antennaTerrain==""){
					alert("Please fill all the values");
					return;
				}
				for(var i in globalAntennaProfileList){
					if(globalAntennaProfileList[i].profile_name==antennaName){
						alert('Profile Name already present');
						return;
					}
				}

			var antennaData={"antennaScanning":antennaScanning,"antennaTracking":antennaTracking,"antennaName":antennaName,"antennaTxPower":antennaTxPower,"antennaType":antennaType,"antennaBand":antennaBand,"antennaGain":antennaGain,"antennaElevation":antennaElevation,"antennaHBW":antennaHBW,"antennaVBW":antennaVBW,"antennaTilt":antennaTilt,"antennaAzimuth":antennaAzimuth,"antennaTerrain":antennaTerrain};
		
		$.ajax
		({
			url:"../../service/common/addAntennaProfile",
			data:antennaData,
			type:'post',
			success:function(data)
			{
			if(data.result=="success"){
			alert('Antenna Profile added successfully');
			var tableRow='<tr><td><input type="checkbox" value="'+data.id+'" name="checkAntennaProfile" class="checkAntennaProfileClass" onclick="checkSingleChecked(\''+data.id+'\')"></input></td><td id="profileName'+data.id+'">'+antennaName+'</td><td>'+antennaTxPower+'</td><td>'+antennaType+'</td><td>'+antennaBand+'</td><td>'+antennaGain+'</td><td>'+antennaElevation+'</td><td>'+antennaHBW+'</td><td>'+antennaVBW+'</td><td>'+antennaTilt+'</td><td>'+antennaAzimuth+'</td><td>'+antennaTerrain+'</td></tr>';
			$('#displayAntennaProfileTable_body').append(tableRow);
			globalAntennaProfileList.push({"id":data.id,"profile_name":antennaName,"txpower":antennaTxPower,"atype":antennaType,"band":antennaBand,"gain":antennaGain,"elevation":antennaElevation,"hbw":antennaHBW,"vbw":antennaVBW,"tilt":antennaTilt,"azimuth":antennaAzimuth,"terrain":antennaTerrain});
			$('#addAntennaName').val("");
			$('#addAntennaTxPower').val("");
			$('#addAntennaType').val("");
			$('#addAntennaBand').val("");
			$('#addAntennaGain').val("");
			$('#addAntennaElevation').val("");
			$('#addAntennaHBW').val("");
			$('#addAntennaVBW').val("");
			$('#addAntennaTilt').val("");
			$('#addAntennaAzimuth').val("");
			$('#addAntennaTerrain').val("");
			}else{
			alert('problem in adding antenna profile');
			}
			}
	});
			}else{
				alert("An Operation is in Started or Interrupted State");
			}
			}
	});
}


var getCurrentAndOffsetAngle = function(){

	 var keepRunnig=true;
	 $.ajax(
				{
					url:dirPath+"service/common/scanandtrackmode",
					type:'post',
					async: false,
					success:function(data)
					{
						for(var i=0;i<data.length;i++){	
							if(data[i].mode_type=='scan'){
								if(data[i].mode_status.toLowerCase()=='idle'){
									
								}else{
									alert("Please stop Scanning and Tracking");
									keepRunnig=false;
								}
							}else{
								if(data[i].mode_status.toLowerCase()=="idle"){
									//keepRunnig=true;
									
								}else{
									if(data[i].applied_antenna==null || data[i].applied_antenna==undefined || data[i].applied_antenna==''){
										alert("Please stop Scanning and Tracking");
										keepRunnig=false;
										
									}else{
										alert("Please stop Scanning and Tracking");
										keepRunnig=false;
									}
								}
								}
						}
					}
				});
	 
	 if(!keepRunnig)
	 {
		 return;
	 }
	 
	$.ajax({
		url:dirPath+"service/ptz/getnorthheading",
		type:'GET',
		dataType:'json',
		data:"",
		success:function(data)
		{

			if(isEmpty(data)){
				alert("No response from device , Please check connection ");
				return;
			}
			
			if(data.type=="stabilization" && data.status=="0"){
				alert("North Heading stabilization is under progress,please wait.");
			}else if(data.type=="connection" && data.status=="0"){
				alert("Device is not connected,please check");
			}else{
				var offset=data.offset;
				var current_angle=data.ptAngle;
				var lat=data.latitude;
				var long=data.longitude;
				$("#sectorAntennaAngleOffset").val(offset);
				$("#s1Angle").val(current_angle);
			}

		}
	
	});

}



/*var selectAntennaProfile = function(){
$('#setAntennaProfileModal').modal('hide');
$('.checkAntennaProfileClass').each(function() {
        if ($(this).prop("checked")) {
		var value=$(this).val();
			$('#antennaProfileTextName'+globalDeviceName).val($('#profileName'+value).text());
			$('#antennaProfileTextId'+globalDeviceName).val(value);
        }
    });

}*/

var checkSingleChecked = function(value){
		$('.checkAntennaProfileClass').each(function() {
        if ($(this).prop("checked") && $(this).val()!=value) {
        $(this).prop("checked",false);
		}
    });
}

var validateIp = function(value) {
    var split = value.split('.');
    if (split.length != 4) 
        return false;
            
    for (var i=0; i<split.length; i++) {
        var s = split[i];
        if (s.length==0 || isNaN(s) || s<0 || s>255)
            return false;
    }
    return true;
}
var validateSystemManagerIp = function(value) {
    if(value.localeCompare("")==0 || value == null)
	{
    	return true;
	}
	var split = value.split('.');
    if (split.length != 4) 
        return false;
            
    for (var i=0; i<split.length; i++) {
        var s = split[i];
        if (s.length==0 || isNaN(s) || s<0 || s>255)
            return false;
    }
    return true;
}
var addDevice = function(){
	var deviceIp=$('#deviceIp').val().trim();
	if(!validateIp(deviceIp)){
		alert('Please provide valid IP');
		return;
	}

	var SystemManagerIP=$('#SystemManagerIpEntered').val().trim();
	if(!validateSystemManagerIp(SystemManagerIP)){
		alert('Please provide valid System Manager IP');
		return;
	}
	
	
	if(SystemManagerIP.localeCompare(deviceIp)==0)
	{
		alert("System Manager IP and IP entered are same");
		return false;
	}

	var hwCapabilityTypeId=$('#hwCapabilityType').val();
	if(hwCapabilityTypeId=="select"){
		alert('Please select Hardware Capability Type');
		return;
	}
	
	var useTypeId=$('#useType').val();
	var useTypeText=$('#useType').find(":selected").text();	 
	if(useTypeId=="select"){
		alert('Please select Node Name');
		return;
	}
	
	
//var antennaProfileId=$('#antennaProfileTextId2g').val();
var useTypeSelected=$('#useType option:selected').attr('data-use_name').toLowerCase();
if(useTypeSelected.indexOf("controller")!=-1 || useTypeSelected.indexOf("ptz")!=-1){
}/*else{
	if(antennaProfileId==""){
		alert('Please select Antenna Profile');
		return false;
		}
}*/

for(var ips in globalBtsDevices)
{
	if(globalBtsDevices[ips] == deviceIp)
	{
		alert("Device already present kindly remove first.");
		return false;
	}
}




if(useTypeText.localeCompare("LSU")==0 || useTypeText.localeCompare("USU")==0 ||useTypeText.localeCompare("GSU") == 0 ||useTypeText.localeCompare("Scanner") == 0)
{
	for(var k1 in globalDeviceData)
	{
		if(globalDeviceData[k1].show_name.localeCompare(useTypeText)==0){
			if(globalDeviceData[k1].systemmanager.localeCompare(SystemManagerIP)==0  &&  globalDeviceData[k1].systemmanager.localeCompare("")!=0){
				alert("System Manager can't be same for same devices ");
				return false;
			}
		}

		
		
		
		
	}
}



if($('#useType').val() !=3)
{
	device_pa_power = "null";
	device_pa_gain = "null";
}
else
{
	device_pa_power = $("#device_pa_power").val();
	device_pa_gain = $("#device_pa_gain").val();
}



deviceData={"deviceIp":deviceIp,"SystemManagerIP":SystemManagerIP,"hwCapabilityTypeId":hwCapabilityTypeId,"useTypeId":useTypeId,"paPower":device_pa_power,"paGain":device_pa_gain};
	$.ajax
	({
			url:"../../service/common/addDevice",
			data:deviceData,
			type:'post',
			beforeSend:function()
			{
				$('#addDeviceButton').css("display","none");
				$('#addDeviceLoadingBox').css("display","block");
			},
			success:function(data)
			{
			if(data.result=="success"){
			alert('Device Added Successfully');
			//updateNodesStatus(deviceIp);
			location.reload();
			
			}else{
			alert('Problem in Adding Device');
			$('#addDeviceButton').css("display","block");
			$('#addDeviceLoadingBox').css("display","none");
			}
			},
			error:function(){
			$('#addDeviceButton').css("display","block");
			$('#addDeviceLoadingBox').css("display","none");
			}
	});
}

var get2gConfigurationPage = function(selectedNibIp){
	
	if(globalBtsDevicesStatus[selectedNibIp] == "NOT REACHABLE")
	{
		alert("No operations allowed when Node is in not reachable state");
		return false;
	}
	
	setSelectedNibIp(selectedNibIp);
}
var get3gConfigurationPage = function(groupName,ip){
	if(globalBtsDevicesStatus[ip] == "NOT REACHABLE")
	{
		alert("No operations allowed when Node is in not reachable state");
		return false;
	}
window.open("configuration3g.jsp?grp="+groupName,"_self");
}

var getNetworkScannerConfigurationPage = function(ip,id,sysId)
{
	
	
	if(globalBtsDevicesStatus[ip] == "NOT REACHABLE")
	{
	alert("No operations allowed when Node is in not reachable state");
	return false;
	}
	window.open("netscan_configuration.jsp?ip="+ip+"&id="+id+"&sysId="+sysId,"_self");
}


var getPTZConfigurationPage = function(ip,id,sysId)
{
	
	
	if(globalBtsDevicesStatus[ip] == "NOT REACHABLE")
	{
	alert("No operations allowed when Node is in not reachable state");
	return false;
	}
	window.open("ptz_configuration.jsp?ip="+ip+"&id="+id+"&sysId="+sysId,"_self");
}



function deleteDevice(groupName){
	var confirmStatus = confirm("Are you sure to delete?");
	if(confirmStatus)
	{
		$.ajax({
		url:dirPath+"service/common/deleteDevice",
		data:{"groupName":groupName},
		type:'post',
		success:function(data)
		{
			if(data.result=="success"){
				alert("Device Successfully Deleted");
				location.reload();
			}
			else if(data.result=="fail"){
				if(data.message=="auto_scanned_cells_running"){
					alert("Please stop the running manual tracking");
				}
				else if(data.message=="unlocked"){
					alert("Please Lock the Device first");
				}
				else{
					alert("Problem in deleting device");
				}
			}
		}
});
}
}

var setLockUnlockOperation = function(ip,flag,deviceUtility){
	var type = flag==2?"Unlock":"Lock";
	var confirmStatus = confirm("Are you sure to "+type+" Device?");
	
	if(globalBtsDevicesStatus[ip] == "NOT REACHABLE")
	{
		alert("No operations allowed when Node is in not reachable state");
		return false;
	}
	if(confirmStatus)
	{
    if(deviceUtility=='2g'){
	lockUnlock2g(flag,ip);
	}else{
	lockUnlockOther(flag,ip,deviceUtility);
	}
	}
}

var lockUnlock2g = function(flag,ip){
	var type = flag==2?"Unlock":"Lock";
	$.ajax({
	url:dirPath+"service/common/lockUnlockCell",
	data:{flag:flag,"ip":ip},
	type:'post',
	success:function(data)
	{
		if((data.STATUS.indexOf("ERR(")>-1))
		{
			alert("Error  while performing "+type);
		}
		else if(data.STATUS == "FAIL")
		{
			alert("Connectivity Lost");
			updateNodesStatus(ip);
		}
		else
		{
			updateNodesStatus(ip);
			alert(type+"ed Successfully");
		}
	}
});
}


var lockUnlockOther = function(flag,ip,type){
var deviceDetailsList=getDeviceDetails(ip);
if (flag=="2"){
setCellUnLockOther(deviceDetailsList[0],type);
}else{
setCellLockOther(deviceDetailsList[0],type);
}
}

var setCellLockOther = function(singleDeviceInfo,type)
{
	//var sufiType=singleDeviceInfo.dname.split(" ")[1];
	var data = {};
	data.cmdType="SET_CELL_LOCK";
	data.systemId = singleDeviceInfo.sytemid;
	data.systemCode = singleDeviceInfo.dcode;
	data.systemIp=singleDeviceInfo.ip;
	data.id=singleDeviceInfo.b_id;
	globalDeviceIp=singleDeviceInfo.ip;
	//data.data ='{"CMD_CODE":"SET_CELL_LOCK","CELL_ID":"'+deviceConfig.SYS_PARAMS.CELL_INFO.CELL_ID+'","LAC":"0"}';
	
	var params=JSON.parse(singleDeviceInfo.config);
	var cellId=params.SYS_PARAMS.CELL_INFO.CELL_ID;

	data.data ='{"CMD_CODE":"SET_CELL_LOCK","CELL_ID":"'+cellId+'","LAC":"0"}';
	if(type=='4g')
	{
		data.data ='{"CMD_CODE":"SET_CELL_LOCK","CELL_ID":"'+cellId+'"}';	
	}
	var callBack = function(data)
	{
		if(data.result=="successful")
		{
			alert("Device Locked Successfully");
			updateNodesStatus(globalDeviceIp);
		}
		else
		{
			alert("Unable to Lock Device");
		}
	}
	if(type == "3g")
		serverCommands(data,"service/3g/clientopr",callBack,'json');
	if(type == "4g")
		serverCommands(data,"service/4g/clientopr",callBack,'json');
}

//Site UnLock Value  
var setCellUnLockOther = function(singleDeviceInfo,type)
{	
	//var sufiType=singleDeviceInfo.dname.split(" ")[1];
	var data = {};
	
	
	data.cmdType="SET_CELL_UNLOCK";
	data.systemId = singleDeviceInfo.sytemid;
	data.systemCode = singleDeviceInfo.dcode;
	data.systemIp=singleDeviceInfo.ip;
	data.id=singleDeviceInfo.b_id;
	globalDeviceIp=singleDeviceInfo.ip;
	var params=JSON.parse(singleDeviceInfo.config);
	var cellId=params.SYS_PARAMS.CELL_INFO.CELL_ID;
	data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+cellId+'","LAC":"0"}';
	if(type=='4g')
	{
		data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+cellId+'"}';	
	}
	var callback = function(data)
	{
		  
		if(data.result=="successful"){
			alert("Device Unlocked Successfully");
			updateNodesStatus(globalDeviceIp);
		}else{
		alert("Unable to Unlock Device");
		}		  
	}	

	if(type == "3g")
		serverCommands(data,"service/3g/clientopr",callback,'json');
	if(type == "4g")
		serverCommands(data,"service/4g/clientopr",callback,'json');
}

var serverCommands = function(data,url,callBackFunction,type)
{
	$.ajax({
		url:dirPath+url,
		data:data,
		type:'post',
		dataType:type,
		success:function(data)
		{
			callBackFunction(data);
		}
	});
}

var getDeviceDetails = function(ip){
var deviceDetails={};
	$.ajax({
		url:"../../service/common/getDeviceDetails",
		async:false,
		type:'POST',
		dataType:'json',
		data:{ip:ip},
		success:function(data)
		{
		deviceDetails=data;	
		},
		error:function(data){
		alert('error');
		}
	});
	return deviceDetails;
}

var updateNodesStatus = function(ip)
{
	$.ajax({
		url:"../../service/common/updateStatusOfGivenBts",
		data:{"ip":ip},
		type:'get',
		success:function(data)
		{
			console.log(data);
			location.reload();
			
		},
		error:function(xhr)
		{
		alert("error in updating data");
		}
		
	});	
}

var updateNodesStatusAtLockUnlock = function(devices)
{
	$.ajax({
		url:"../../service/common/updateStatusAtLockUnlock",
		data:{"devices":devices},
		type:'get',
		success:function(data)
		{
			console.log(data);
			getbtsinfo();
			//location.reload();
			
		},
		error:function(xhr)
		{
		alert("error in updating data");
		}
		
	});	
}

var getLockUnlockImage = function(adminState){
switch(adminState){
case "0" :
return {"image":"../../resources/images/na.png","flag":"1","title":"LOCK"};
case "1" :
return {"image":"../../resources/images/lock.png","flag":"2","title":"UNLOCK"};
case "2" :
return {"image":"../../resources/images/unlock.png","flag":"1","title":"LOCK"};
}
}

var displayAddTargetModal = function(){
getTargetList();
}

var getTargetList = function(){
var tableRows='';
$('#displayTargetTable_body').html('');
	$.ajax
	({
			url:"../../service/common/getTargetList",
			type:'post',
			success:function(data)
			{
			globalTargetList=data;
			for(var i=0;i<data.length;i++){
			tableRows+='<tr id="targetTableRow'+data[i].id+'"><td><input type="checkbox" value="'+data[i].id+'" name="checkTargetList" class="checkTargetListClass"></input></td><td>'+data[i].name+'</td><td id="targetRowImsi'+data[i].id+'">'+data[i].imsi+'</td><td id="targetRowImei'+data[i].id+'">'+data[i].imei+'</td><td>'+data[i].type+'</td></tr>';
			}
			$('#displayTargetTable_body').html(tableRows);
			$('#addTargetModal').modal('show');
			}
	});
}

var addTarget = function()
{
	var targetImsi=$("#targetImsi").val();
	var targetImei=$("#targetImei").val();
	var targetName=$("#targetName").val();
	
	if(targetName == "")
	{
		alert("Please add valid target name");
		return false;
	}
	
	if(targetName.length>=20){
		alert("Target Name length should not exceed 20 characters");
		return false;
	}
	
	if(targetImsi !="" && targetImsi.length != 15)
	{
		alert("Please add valid IMSI");
		return false;
	}
	if(targetImei != "" && targetImei.length != 15){
		alert("Please add valid IMEI");
		return false;
	}
	
	if(targetImsi=="" && targetImei==""){
		alert("Please add IMSI,IMEI or both of them");
		return false;
	}
	
	for(var i=0;i<globalTargetList.length;i++){
		if(globalTargetList[i].name.toLowerCase()==targetName.toLowerCase()){
			alert("Target with this name already present");
			return false;
		}
		if(globalTargetList[i].imsi==targetImsi && globalTargetList[i].imei==targetImei){
			alert("IMSI and IMEI combination already present");
			return false;
		}
	}

/*	var all3gDeviceList=getAll3gDeviceList();
	var allRunningStatus=getAllRunningStatus(all3gDeviceList);
	if(!allRunningStatus.status){
	alert(allRunningStatus.ip+" is not reachable or down.Please run all the 3G devices first");
	return false;
	}
	for(var i=0;i<all3gDeviceList.length;i++){
	var params=JSON.parse(all3gDeviceList[i].config);
	if(params.SYS_PARAMS.SUB_INFO.SUB_LIST_MODE=="1"){
	if(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID_TYPE=="0" && params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID==targetImei){
	alert(targetImei+" is present in Exclusion Mode in Hold Subsciber of device ip:"+all3gDeviceList[i].ip);
	return false;
	}
	if(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID_TYPE=="1" && params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID==targetImsi){
	alert(targetImsi+" is present in Exclusion Mode in Hold Subsciber of device ip:"+all3gDeviceList[i].ip);
	return false;
	}
	}
	}*/
	
	var targetType=$('#targetType').val();
	
	data={"targetImsi":targetImsi,"targetImei":targetImei,"targetName":targetName,"targetType":targetType};
	$.ajax({
	url:"../../service/common/addTarget",
		data:data,
		type:'post',
		success:function(data)
		{
			if(data.result=="success"){
            alert('Target Added Successfully');
			var tableRow='<tr id="targetTableRow'+data.id+'"><td><input type="checkbox" value="'+data.id+'" name="checkTargetList" class="checkTargetListClass"></input></td><td>'+targetName+'</td><td  id="targetRowImsi'+data.id+'">'+targetImsi+'</td><td  id="targetRowImei'+data.id+'">'+targetImei+'</td><td>'+targetType+'</td></tr>';
			$('#displayTargetTable_body').append(tableRow);
			globalTargetList.push({"id":data.id,"name":targetName,"imsi":targetImsi,"imei":targetImei});
			$('#targetImsi').val("");
			$('#targetImei').val("");
			$('#targetName').val("");
			
/*			for(var i=0;i<all3gDeviceList.length;i++){
				var params=JSON.parse(all3gDeviceList[i].config);
				var subscriberListJson=params.SYS_PARAMS.SUB_INFO.SUB_LIST;
				if(targetImei!=""){
				subscriberListJson.push({SUB_ID:targetImei,SUB_ID_TYPE:"0"});
				}
				if(targetImsi!=""){
				subscriberListJson.push({SUB_ID:targetImsi,SUB_ID_TYPE:"1"});
				}
				
				var subInfo=params.SYS_PARAMS.SUB_INFO;
				subInfo.SUB_LIST=subscriberListJson;
				//subInfoJsonString=JSON.stringify(subInfo);
				updateSibInfoOnDevice(all3gDeviceList[i].sytemid,all3gDeviceList[i].dcode,all3gDeviceList[i].ip,all3gDeviceList[i].b_id,subInfo);
				updateSibInfoOnDb(all3gDeviceList[i].sytemid,all3gDeviceList[i].dcode,all3gDeviceList[i].ip,all3gDeviceList[i].b_id,subInfo);
				}*/

			}else{
			alert('problem in adding Target');
			}
		},
		error:function(){
			alert("Problem in adding Target");
		}
	});
}

var deleteTarget = function()
{
	
	var all3gDeviceList=getAll3gDeviceList();	
	var targetIds='';
	var targetIdArray=[];
	$('.checkTargetListClass').each(function() {
        if ($(this).prop("checked")) {
		targetIds+=$(this).val()+',';
		targetIdArray.push($(this).val());
	}
    });
	
	
	if(targetIds==''){
	alert('Please select Target first');
	return false;
	}
	
	
	/*if(targetIdArray.length!=globalTargetList.length){
		
		for(var i=0;i<all3gDeviceList.length;i++){
		var params=JSON.parse(all3gDeviceList[i].config);
		for(var j=0;j<targetIdArray.length;j++){
		var targetImsi=$("#targetRowImsi"+targetIdArray[j]).text();
		var targetImei=$("#targetRowImei"+targetIdArray[j]).text();
	
		if(params.SYS_PARAMS.SUB_INFO.SUB_LIST_MODE=="0"){
			if(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID_TYPE=="0" && params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID==targetImei){
				alert(targetImei+" is present in Inclusion Mode in Hold Subsciber of device ip:"+all3gDeviceList[i].ip);
				return false;
			}
			if(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID_TYPE=="1" && params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID==targetImsi){
				alert(targetImsi+" is present in Inclusion Mode in Hold Subsciber of device ip:"+all3gDeviceList[i].ip);
				return false;
			}
		}
		}
	}
	}*/
		
	targetIds=targetIds.substring(0,targetIds.length-1);
	data={"targetIds":targetIds};
	$.ajax({
		url:"../../service/common/deleteTarget",
		data:data,
		type:'post',
		success:function(data)
		{
			if(data.result=="success"){
            alert('Target Deleted Successfully');
/*			if(targetIdArray.length==globalTargetList.length){
			var defaultSufiConfigObj=JSON.parse(getDefaultSufiConfiguration());
			var defaultSubInfo=defaultSufiConfigObj.SYS_PARAMS.SUB_INFO;
			for(var i=0;i<all3gDeviceList.length;i++){
				//subInfoJsonString=JSON.stringify(defaultSubInfo);
				updateSibInfoOnDevice(all3gDeviceList[i].sytemid,all3gDeviceList[i].dcode,all3gDeviceList[i].ip,all3gDeviceList[i].b_id,defaultSubInfo);
				updateSibInfoOnDb(all3gDeviceList[i].sytemid,all3gDeviceList[i].dcode,all3gDeviceList[i].ip,all3gDeviceList[i].b_id,defaultSubInfo);
				}
			}else{
				for(var i=0;i<all3gDeviceList.length;i++){
				var params=JSON.parse(all3gDeviceList[i].config);
				var subscriberListJson=params.SYS_PARAMS.SUB_INFO.SUB_LIST;
				var finalSubscriberListJson=subscriberListJson;
				for(var j=0;j<targetIdArray.length;j++){
				var targetImsi=$("#targetRowImsi"+targetIdArray[j]).text();
				var targetImei=$("#targetRowImei"+targetIdArray[j]).text();
				for(var k=0;k<subscriberListJson.length;k++){
				if((subscriberListJson[k].SUB_ID==targetImei && subscriberListJson[k].SUB_ID_TYPE=="0") || (subscriberListJson[k].SUB_ID==targetImsi && subscriberListJson[k].SUB_ID_TYPE=="1")){
				finalSubscriberListJson.splice(k,1);
				}
				}
				}
				var subInfo=params.SYS_PARAMS.SUB_INFO;
				subInfo.SUB_LIST=finalSubscriberListJson;
				//subInfoJsonString=JSON.stringify(subInfo);
				updateSibInfoOnDevice(all3gDeviceList[i].sytemid,all3gDeviceList[i].dcode,all3gDeviceList[i].ip,all3gDeviceList[i].b_id,subInfo);
				updateSibInfoOnDb(all3gDeviceList[i].sytemid,all3gDeviceList[i].dcode,all3gDeviceList[i].ip,all3gDeviceList[i].b_id,subInfo);
				}
			}*/
			
			
			
			for(var i in targetIdArray){
			var row=document.getElementById('targetTableRow'+targetIdArray[i]);
			var table=row.parentNode.parentNode;
			table.deleteRow(row.rowIndex);
			}
			for(var i in globalTargetList) {
			var targetObj = globalTargetList[i];

			if(targetIdArray.indexOf(targetObj.id) !== -1) {
			globalTargetList.splice(i, 1);
			}
			}
			getTargetList();
			}else{
			alert('problem in deleting Target');
			}
		},
		error:function(){
			alert("Problem in deleting Target");
		}
	});
}


var deleteAntennaProfile = function()
{
	var antennaProfileId='';
	$('.checkAntennaProfileClass').each(function() {
        if ($(this).prop("checked")) {
			antennaProfileId=$(this).val();
        }
    });	
	
	data={"id":antennaProfileId};
	$.ajax({
		url:"../../service/common/deleteantennaprofile",
		data:data,
		type:'post',
		success:function(data)
		{
           if(data.result=="success"){
			alert("Antenna Profile Deleted Successfully");
			var antennaProfileIdArray=antennaProfileId.split(",");
			for(var i in antennaProfileIdArray){
			var row=document.getElementById('antennaProfileRow'+antennaProfileIdArray[i]);
			var table=row.parentNode.parentNode;
			table.deleteRow(row.rowIndex);
			}
			var antennaProfileList=globalAntennaProfileList;
			for(var i in antennaProfileList) {
			var id = plmnList[i].id;
			if(antennaProfileId.indexOf(id) !== -1) {
			globalAntennaProfileList.splice(i, 1);
			}
			}
		   }else{
				if(data.message=="used error"){
					alert("Antenna Profile already used");
				}else{
					alert("Problem in deleting Antenna Profile");
				}
			
		   }
		},
		error:function(){
			alert("Problem in deleting Antenna Profile");
		}
	});
}

var setSelectedNibIp = function(selectedNibIp)
{
	$.ajax({
		url:"../../service/common/setSelectedNibIp",
		data:{"selectedNibIp":selectedNibIp},
		type:'post',
		success:function(data)
		{
			window.open("plmnmanagment.jsp","_self");
			
		},
		error:function(xhr)
		{
			alert("error in updating data");
		}
		
	});	
}

var getNCells = function(ip,code){
$('#displayNCellsTable_body').html('');
	$.ajax
	({
			url:"../../service/common/getNCells",
			data:{ip:ip,code:code},
			type:'post',
			success:function(data)
			{
			var rows='';
			var nCellsCounter=0;
			if(code=='5'){
			var tableSelfRows='';
			var tableRows='';
			$('#displayNCellsTable_head').html('<tr><th>CELLS</th><th>MCC</th><th>MNC</th><th>LAC</th><th>CELL</th><th>ARFCN</th></tr>');
			if(data.length==0){
			$('#displayNCellsTable_body').html('<tr><td colspan="6" style="text-align: center;">No Cells Configured</td></tr>');
			}else{
			for(var i=0;i<data.length;i++){
			if(data[i].type=='N'){
			tableRows+='<tr><td>NCell'+(++nCellsCounter)+'</td><td>'+data[i].plmn.substring(0,3)+'</td><td>'+data[i].plmn.substring(3)+'</td><td>'+data[i].lac+'</td><td>'+data[i].cell+'</td><td>'+data[i].arfcn+'</td></tr>';
			}else{
			tableSelfRows='<tr><td>Self</td><td>'+data[i].plmn.substring(0,3)+'</td><td>'+data[i].plmn.substring(3)+'</td><td>'+data[i].lac+'</td><td>'+data[i].cell+'</td><td>'+data[i].arfcn+'</td></tr>';
			}
			}
			rows=tableSelfRows+tableRows;
			}
			}else if(code=='0' || code=='1' || code=='2'){
				var configTree=JSON.parse(data[0].config);
				$('#displayNCellsTable_head').html('<tr><th>CELLS</th><th>MCC</th><th>MNC</th><th>LAC</th><th>CELL</th><th>ARFCN</th><th>UARFCN</th><th>PSC</th></tr>');
			    var dluarfcn=configTree.SYS_PARAMS.CELL_INFO.DL_UARFCN;
				rows+='<tr><td>Self</td><td>'+configTree.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC+'</td><td>'+configTree.SYS_PARAMS.CELL_INFO.PLMN_ID.MNC+'</td><td>'+data[0].lac+'</td><td>'+configTree.SYS_PARAMS.CELL_INFO.CELL_ID+'</td><td></td><td>'+dluarfcn+'</td><td>'+configTree.SYS_PARAMS.CELL_INFO.PRI_SCRAM_CODE+'</td></tr>';
				
				var intraNeigh=configTree.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ;
				var interNeigh=configTree.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ;
				var interRatNeigh=configTree.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT;
				
				for(var i=0;i<intraNeigh.length;i++){
					rows+='<tr><td>NCell'+(++nCellsCounter)+'</td><td>'+intraNeigh[i].INTRA_PLMN_ID.MCC+'</td><td>'+intraNeigh[i].INTRA_PLMN_ID.MNC+'</td><td>'+intraNeigh[i].LAC+'</td><td>'+intraNeigh[i].CELL_ID+'</td><td></td><td >'+dluarfcn+'</td><td>'+intraNeigh[i].PSC+'</td></tr>';
				}
				for(var i=0;i<interNeigh.length;i++){
					rows+='<tr><td>NCell'+(++nCellsCounter)+'</td><td>'+interNeigh[i].INTER_PLMN_ID.MCC+'</td><td>'+interNeigh[i].INTER_PLMN_ID.MNC+'</td><td>'+interNeigh[i].LAC+'</td><td>'+interNeigh[i].CELL_ID+'</td><td></td><td>'+interNeigh[i].DL_UARFCN+'</td><td>'+interNeigh[i].PSC+'</td></tr>';
				}
				for(var i=0;i<interRatNeigh.length;i++){
					rows+='<tr><td>NCell'+(++nCellsCounter)+'</td><td>'+interRatNeigh[i].RAT_PLMN_ID.MCC+'</td><td>'+interRatNeigh[i].RAT_PLMN_ID.MNC+'</td><td>'+interRatNeigh[i].LAC+'</td><td>'+interRatNeigh[i].CELL_ID+'</td><td>'+interRatNeigh[i].BCCH_ARFCN+'</td><td></td><td></td></tr>';
				}
			
			}else if(code=='13' || code=='14' || code=='15'){
				var configTree=JSON.parse(data[0].config);
				$('#displayNCellsTable_head').html('<tr><th>CELLS</th><th>MCC</th><th>MNC</th><th>LAC</th><th>CELL</th><th>ARFCN</th><th>UARFCN</th><th>PSC</th><th>PCI</th><th>EUARFCN</th></tr>');
			    var dleuarfcn=configTree.SYS_PARAMS.CELL_INFO.DL_EARFCN;
				rows+='<tr><td>Self</td><td>'+configTree.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC+'</td><td>'+configTree.SYS_PARAMS.CELL_INFO.PLMN_ID.MNC+'</td><td>'+data[0].lac+'</td><td>'+configTree.SYS_PARAMS.CELL_INFO.CELL_ID+'</td><td></td><td></td><td></td><td>'+configTree.SYS_PARAMS.CELL_INFO.PHY_CELL_ID+'</td><td>'+dleuarfcn+'</td></tr>';
				
				var intraNeigh=configTree.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ;
				var interNeigh=configTree.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ;
				var interRatNeigh3G=configTree.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_3G;
				var interRatNeigh2G=configTree.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_2G;
				
				

				for(var i=0;i<intraNeigh.length;i++){
					rows+='<tr><td>NCell'+(++nCellsCounter)+'</td><td>'+intraNeigh[i].INTRA_PLMN_ID.MCC+'</td><td>'+intraNeigh[i].INTRA_PLMN_ID.MNC+'</td><td>'+intraNeigh[i].TAC+'</td><td>'+intraNeigh[i].CELL_ID+'</td><td></td><td ></td><td></td><td>'+intraNeigh[i].PCI+'</td><td >'+dleuarfcn+'</td></tr>';
				}
				for(var i=0;i<interNeigh.length;i++){
					rows+='<tr><td>NCell'+(++nCellsCounter)+'</td><td>'+interNeigh[i].INTER_PLMN_ID.MCC+'</td><td>'+interNeigh[i].INTER_PLMN_ID.MNC+'</td><td>'+interNeigh[i].TAC+'</td><td>'+interNeigh[i].CELL_ID+'</td><td></td><td></td><td></td><td>'+interNeigh[i].PCI_List.PCI1+'</td><td >'+interNeigh[i].DL_Carrier_Frequency+'</td></tr>';
				}
				for(var i=0;i<interRatNeigh3G.length;i++){
					rows+='<tr><td>NCell'+(++nCellsCounter)+'</td><td>'+interRatNeigh3G[i].INTER_RAT_PLMN_ID.MCC+'</td><td>'+interRatNeigh3G[i].INTER_RAT_PLMN_ID.MNC+'</td><td>'+interRatNeigh3G[i].LAC+'</td><td>'+interRatNeigh3G[i].CELL_ID+'</td><td>'+interRatNeigh3G[i].DL_Carrier_Frequency+'</td><td></td><td></td><td ></td><td></td></tr>';
				}
				for(var i=0;i<interRatNeigh2G.length;i++){
					rows+='<tr><td>NCell'+(++nCellsCounter)+'</td><td>'+interRatNeigh2G[i].INTER_RAT_PLMN_ID.MCC+'</td><td>'+interRatNeigh2G[i].INTER_RAT_PLMN_ID.MNC+'</td><td>'+interRatNeigh2G[i].LAC+'</td><td>'+interRatNeigh2G[i].CELL_ID+'</td><td>'+interRatNeigh2G[i].Explicit_List_ARFCN.ARFCN1+'</td><td></td><td></td><td ></td><td></td></tr>';
				}
			}
			$('#displayNCellsTable_body').html(rows);
			$('#nCellsModal').modal('show');
			}
	});
}

var getSelectedAntennaProfile = function(antennaProfileName){
$('#selectedAntennaTable_body').html('');
	$.ajax
	({
			url:"../../service/common/getSelectedAntennaProfile",
			data:{"antennaProfileName":antennaProfileName},
			type:'post',
			success:function(data)
			{
			var tableRows='';
			tableRows+='<tr><td>TX Power</td><td>'+data[0].txpower+'</td></tr>';
			tableRows+='<tr><td>Antenna Type</td><td>'+data[0].atype+'</td></tr>';
			tableRows+='<tr><td>Band</td><td>'+data[0].band+'</td></tr>';
			tableRows+='<tr><td>Gain</td><td>'+data[0].gain+'</td></tr>';
			tableRows+='<tr><td>Elevation</td><td>'+data[0].elevation+'</td></tr>';
			tableRows+='<tr><td>HBW</td><td>'+data[0].hbw+'</td></tr>';
			tableRows+='<tr><td>VBW</td><td>'+data[0].vbw+'</td></tr>';
			tableRows+='<tr><td>Tilt</td><td>'+data[0].tilt+'</td></tr>';
			tableRows+='<tr><td>Azimuth</td><td>'+data[0].azimuth+'</td></tr>';
			tableRows+='<tr><td>Terrain</td><td>'+data[0].terrain+'</td></tr>';
			$('#selectedAntennaTable_body').html(tableRows);
			$('#selectedAntennaModal').modal('show');
			}
});
}

var openAddDeviceModal = function(){
if($('#useType option:selected').attr('data-type')=="modal"){
//$('#myaddDeviceModalLabel').text($('#'+$('#useType option:selected').attr('data-modal_id')).attr('data-value'));
	$('.deviceModalLabel').text($('#'+$('#useType option:selected').attr('data-modal_id')).attr('data-value'));
$('#'+$('#useType option:selected').attr('data-modal_id')).modal('show');
}else{
	var useTypeSelected=$('#useType option:selected').attr('data-use_name').toLowerCase();
	if(useTypeSelected.indexOf("controller")!=-1 || useTypeSelected.indexOf("ptz")!=-1){
		$('#antennaProfileTd').css('display','none');
	}else{
		$('#antennaProfileTd').css('display','table-cell');	
	}	
	
	if(useTypeSelected == "2G locator".toLowerCase())
	{
		$("#tr_device_pa_power").css("display","table-cell");
		$("#tr_device_pa_gain").css("display","table-cell");
	}
	else
	{
		$("#tr_device_pa_power").css("display","none");
		$("#tr_device_pa_gain").css("display","none");
	}
}
}

var add3gDevice = function()
{
	var useTypeId=$('#useType').val();
	var useTypeText=$('#useType').find(":selected").text();	 
	var SystemManagerIP=$('#addSystemManagerIP').val().trim();
	var ofIp = $("#addOfIp").val().trim();

	var groupName =$("#addGroupName3g").val();
	if(groupName=="" || !isNaN(groupName))
	{
		alert("Group Name should not be blank");
		return false;
	}
	

	if(SystemManagerIP.localeCompare(ofIp)==0)
	{
		alert("System Manager IP and IP entered are same");
		return false;
	}










	if(!validateSystemManagerIp(SystemManagerIP)){
		alert('Please provide valid System Manager IP');
		return;
	}





















	/*var antennaProfileIdOf = $('#antennaProfileTextIdOf').val();
	if(antennaProfileIdOf==""){
		alert('Please select Antenna Profile for OF');
		return false;
	}*/
		
	var hwCapabilityTypeIdOf=$('#addHwCapabilityTypeOf').val();
	if(hwCapabilityTypeIdOf=="select"){
		alert('Please select Hardware Capability Type for OF');
		return false;
	}
	
	if(!validateIp(ofIp))
	{
		alert("OF IP is not valid");
		return false;
	}
	
	var ppfIp =$("#addPpfIp").val().trim();
	if(!validateIp(ppfIp))
	{
		alert("PPF IP is not valid");
		return false;
	}
	
	//var antennaProfileIdPpf = $('#antennaProfileTextIdPpf').val();
	var hwCapabilityTypeIdPpf=$('#addHwCapabilityTypePpf').val();
	if(ppfIp=='0.0.0.0' || ppfIp=='1.1.1.1'){
		//antennaProfileIdPpf=antennaProfileIdOf;
		hwCapabilityTypeIdPpf=hwCapabilityTypeIdOf;
	}else{
		/*if(antennaProfileIdPpf==""){
			alert('Please select Antenna Profile for PPF');
			return false;
		}*/
	if(hwCapabilityTypeIdPpf=="select"){
	alert('Please select Hardware Capability Type for PPF');
	return false;
	}
	}
		

	
	var spfIp =$("#addSpfIp").val().trim();
	if(!validateIp(spfIp))
	{
		alert("SPF IP is not valid");
		return false;
	}
	
	//var antennaProfileIdSpf = $('#antennaProfileTextIdSpf').val();
	var hwCapabilityTypeIdSpf=$('#addHwCapabilityTypeSpf').val();
	if(spfIp=='0.0.0.0' || spfIp=='1.1.1.1'){
		//antennaProfileIdSpf=antennaProfileIdOf;
		hwCapabilityTypeIdSpf=hwCapabilityTypeIdOf;
	}else{
		/*if(antennaProfileIdSpf==""){
			alert('Please select Antenna Profile for SPF');
			return false;
		}*/
	if(hwCapabilityTypeIdSpf=="select"){
	alert('Please select Hardware Capability Type for SPF');
	return false;
	}
	}
	
	for(var ips in globalBtsDevices)
	{
		if(globalBtsDevices[ips] == ofIp || globalBtsDevices[ips] == ppfIp || globalBtsDevices[ips] == spfIp)
		{
			alert("Deivce already present kindly remove first.");
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	


	if(useTypeText.localeCompare("LSU")==0 || useTypeText.localeCompare("USU")==0 ||useTypeText.localeCompare("GSU") == 0 ||useTypeText.localeCompare("Scanner") == 0)
	{
		for(var k1 in globalDeviceData)
		{
			if(globalDeviceData[k1].show_name.localeCompare(useTypeText)==0){
				if(globalDeviceData[k1].systemmanager.localeCompare(SystemManagerIP)==0  &&  globalDeviceData[k1].systemmanager.localeCompare("")!=0){
					alert("System Manager can't be same for same devices ");
					return false;
				}
			}

			
			
			
			
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	data={"groupName":groupName,"SystemManagerIP":SystemManagerIP,"useTypeId":useTypeId,"ofIp":ofIp,"ppfIp":ppfIp,"spfIp":spfIp,"hwCapabilityTypeIdOf":hwCapabilityTypeIdOf,"hwCapabilityTypeIdPpf":hwCapabilityTypeIdPpf,"hwCapabilityTypeIdSpf":hwCapabilityTypeIdSpf,"paGain":$("#3g_device_pa_gain").val(),"paPower":$("#3g_device_pa_power").val()};
	$.ajax({
		url:dirPath+"service/common/add3gDevice",
		data:data,
		type:'post',
		success:function(data)
		{
		alert("Devices Added Successfully");
		location.reload();
		},
		error:function()
		{
			alert("Request failed");
		}
	});
}


var add4gDevice = function()
{

	var useTypeId=$('#useType').val();
	var useTypeText=$('#useType').find(":selected").text();	
	var ofIp = $("#addeOfIp").val().trim();

	var SystemManagerIP=$('#SystemManagerIpEntered4g').val().trim();
	if(!validateSystemManagerIp(SystemManagerIP)){
		alert('Please provide valid System Manager IP');
		return;
	}
	

	if(SystemManagerIP.localeCompare(ofIp)==0)
	{
		alert("System Manager IP and IP entered are same");
		return false;
	}





	var groupName =$("#addGroupName4g").val();
	if(groupName=="" || !isNaN(groupName))
	{
		alert("Group Name should not be blank");
		return false;
	}
	
	/*var antennaProfileIdOf = $('#antennaProfileTextIdOf').val();
	if(antennaProfileIdOf==""){
		alert('Please select Antenna Profile for OF');
		return false;
	}*/
		
	var hwCapabilityTypeIdOf=$('#addHwCapabilityTypeeOf').val();
	if(hwCapabilityTypeIdOf=="select"){
	alert('Please select Hardware Capability Type for OF');
	return false;
	}
	
	if(!validateIp(ofIp))
	{
		alert("OF IP is not valid");
		return false;
	}
	
	var ppfIp =$("#addePpfIp").val().trim();
	if(!validateIp(ppfIp))
	{
		alert("PPF IP is not valid");
		return false;
	}
	
	//var antennaProfileIdPpf = $('#antennaProfileTextIdPpf').val();
	var hwCapabilityTypeIdPpf=$('#addHwCapabilityTypeePpf').val();
	if(ppfIp=='0.0.0.0' || ppfIp=='1.1.1.1'){
		//antennaProfileIdPpf=antennaProfileIdOf;
		hwCapabilityTypeIdPpf=hwCapabilityTypeIdOf;
	}else{
		/*if(antennaProfileIdPpf==""){
			alert('Please select Antenna Profile for PPF');
			return false;
		}*/
	if(hwCapabilityTypeIdPpf=="select"){
	alert('Please select Hardware Capability Type for PPF');
	return false;
	}
	}
		

	
	var spfIp =$("#addeSpfIp").val().trim();
	if(!validateIp(spfIp))
	{
		alert("SPF IP is not valid");
		return false;
	}
	
	//var antennaProfileIdSpf = $('#antennaProfileTextIdSpf').val();
	var hwCapabilityTypeIdSpf=$('#addHwCapabilityTypeeSpf').val();
	if(spfIp=='0.0.0.0' || spfIp=='1.1.1.1'){
		//antennaProfileIdSpf=antennaProfileIdOf;
		hwCapabilityTypeIdSpf=hwCapabilityTypeIdOf;
	}else{
		/*if(antennaProfileIdSpf==""){
			alert('Please select Antenna Profile for SPF');
			return false;
		}*/
	if(hwCapabilityTypeIdSpf=="select"){
	alert('Please select Hardware Capability Type for SPF');
	return false;
	}
	}
	
	for(var ips in globalBtsDevices)
	{
		if(globalBtsDevices[ips] == ofIp || globalBtsDevices[ips] == ppfIp || globalBtsDevices[ips] == spfIp)
		{
			alert("Deivce already present kindly remove first.");
			return false;
		}
	}
	
	







if(useTypeText.localeCompare("LSU")==0 || useTypeText.localeCompare("USU")==0 ||useTypeText.localeCompare("GSU") == 0 ||useTypeText.localeCompare("Scanner") == 0)
{
	for(var k1 in globalDeviceData)
	{
		if(globalDeviceData[k1].show_name.localeCompare(useTypeText)==0){
			if(globalDeviceData[k1].systemmanager.localeCompare(SystemManagerIP)==0  &&  globalDeviceData[k1].systemmanager.localeCompare("")!=0){
				alert("System Manager can't be same for same devices ");
				return false;
			}
		}
	//chacha
		
		
		
		
	}
}

















	
	
	
	
	data={"groupName":groupName,"SystemManagerIP":SystemManagerIP,"useTypeId":useTypeId,"ofIp":ofIp,"ppfIp":ppfIp,"spfIp":spfIp,"hwCapabilityTypeIdOf":hwCapabilityTypeIdOf,"hwCapabilityTypeIdPpf":hwCapabilityTypeIdPpf,"hwCapabilityTypeIdSpf":hwCapabilityTypeIdSpf,"paGain":$("#4g_device_pa_gain").val(),"paPower":$("#4g_device_pa_power").val()};
	$.ajax({
		url:dirPath+"service/common/add4gDevice",
		data:data,
		type:'post',
		success:function(data)
		{
		alert("Devices Added Successfully");
		location.reload();
		},
		error:function()
		{
			alert("Request failed");
		}
	});
}




var setSelectionToDefault = function(){
   $('#useType').val('select');
   $('#deviceIp').val('');
}

var getAll3gDeviceList = function()
{	
    var all3gDeviceList=[];
		$.ajax({
			url:dirPath+"service/common/getall3gdevicelist",
			type:'post',
			async:false,
			success:function(data)
			{
			all3gDeviceList=data;	
			}
			});
		return all3gDeviceList;
}

var getAllRunningStatus = function(all3gDeviceList){
 for(var i=0;i<all3gDeviceList.length;i++){
	if(all3gDeviceList[i].ip!='0.0.0.0' && all3gDeviceList[i].ip!='1.1.1.1' && (all3gDeviceList[i].status.toLowerCase()=="not reachable" ||all3gDeviceList[i].status.toLowerCase()=="system down")){
		return {status:false,ip:all3gDeviceList[i].ip};  
	}
 }
 return {status:true};
}

var updateSibInfoOnDevice = function(sufiId,deviceCode,deviceIp,deviceId,subInfo)
{
	var data = {};
	data.cmdType="SET_TRACK_SUB_LIST";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId;
	//data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+$("#cell_id").val()+'","LAC":"'+$("#lac").val()+'"}';
	data.data ='{"CMD_CODE": "SET_TRACK_SUB_LIST","SUB_INFO":{"SUB_LIST_MODE":"'+subInfo.SUB_LIST_MODE+'","SUB_LIST":'+JSON.stringify(subInfo.SUB_LIST)+',"HOLD_SUB":{"SUB_ID":"'+subInfo.HOLD_SUB.SUB_ID+'","SUB_ID_TYPE":"'+subInfo.HOLD_SUB.SUB_ID_TYPE+'"}}}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');
}

var updateSibInfoOnDb = function(sufiId,deviceCode,deviceIp,deviceId,subInfo)
{
	var data = {};
	data.ip=deviceIp;
	data.id=deviceId;        
	//data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+$("#cell_id").val()+'","LAC":"'+$("#lac").val()+'"}';
	data.config ='{"SUB_LIST_MODE":"'+subInfo.SUB_LIST_MODE+'","SUB_LIST":'+JSON.stringify(subInfo.SUB_LIST)+',"HOLD_SUB":{"SUB_ID":"'+subInfo.HOLD_SUB.SUB_ID+'","SUB_ID_TYPE":"'+subInfo.HOLD_SUB.SUB_ID_TYPE+'"}}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/updateConfigSubInfo",callback,'json');
}

var getDefaultSufiConfiguration = function(){
var defaultSufiConfig='';	
		$.ajax({
			url:dirPath+"service/3g/getdefaultsuficonfiguration",
			async:false,
			type:'GET',
			success:function(data)
			{
			console.log(data);
			defaultSufiConfig=data;	
			}
		});
		return defaultSufiConfig;
}

var getConfig = function(id,code,ip,sufiId)
{
	var data = {};
	data.cmdType="GET_ACTIVE_CONFIG";
	data.systemId = sufiId;
	data.systemCode = code;
	data.systemIp=ip;
	data.id=id
	data.data ='{"CMD_CODE": "GET_ACTIVE_CONFIG"}';
        
	var callback = function(data)
	{
            setConfigForm(data);
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');	
}

var openCloseDialog = function(selector,buttonId,parentElement){
	var sign=$(selector).closest('table').find('.arrowClass').html();
	if(sign=="-"){
		var thwidth=$(selector).closest('th').width() ;
		//th set min width
		$(selector).closest('th').width(thwidth);
		$(selector).closest('table').find('tbody').hide();
		$(selector).closest('table').find('.spanStyleThead').hide();
		$(selector).removeClass("ti-arrow-circle-up").addClass("ti-arrow-circle-down");
		$(selector).closest('table').find('.arrowClass').html("+");
		if(buttonId!=undefined){
		$('#'+buttonId).css('display','none');
		}
	}
	else{
		var thwidth=$(selector).closest('th').width() ;
		$(selector).closest('th').width(thwidth);
		$(selector).closest('table').find('tbody').show();
		$(selector).closest('table').find('.spanStyleThead').show();
		$(selector).removeClass("ti-arrow-circle-down").addClass("ti-arrow-circle-up");
		$(selector).closest('table').find('.arrowClass').html("-");
		if(buttonId!=undefined){
		if(parentElement!='tfoot'){
		$('#'+buttonId).css('display','block');
		}else{
		$('#'+buttonId).css('display','table-footer-group');
		}
		}
	}
}

$(document).ready(function()
{	
	getBandFreqMap();
	//getCurrentAndOffsetAngle();
	//getOperators();
	
});

var bandFreqMap = null;
var getBandFreqMap = function()
{
	$.ajax({
		url:"../../resources/config/band_freq_map.json",
		datatype:"json",
		success:function(data)
		{
			bandFreqMap = data
			console.log(data);
			createBandListOption(true,'GSM',bandFreqMap,'exe_scan_2g_tech');
			createBandListOption(true,'UMTS',bandFreqMap,'exe_scan_umts_tech');
			createBandListOption(true,'LTE',bandFreqMap,'exe_scan_lte_tech');
			
		}
		
	});
}

/*var getCurrentAndOffsetAngle = function()
{
	$.ajax({
		url:"../../service/common/getCurrentAndOffsetAngle",
		datatype:"json",
		success:function(data)
		{
			
			
		}
		
	});
}*/
var getOperators = function()
{
	$.ajax({
		url:"../../service/common/getoperators",
		datatype:"json",
		type:"post",
		success:function(data)
		{
			$('#newOprPlmn').html('');
			var options="";
			for(var i=0;i<data.length;i++){
				options+='<option value="'+data[i].plmn_list+'">'+data[i].opr+'</option>';
			}
			$('#newOprPlmn').html(options);
			
		}
		
	});
}

var createBandListOption= function(removeSelect,tech,data,returnOrAppend)
{
	var htmlData = ceateBandList(data[tech].BAND);
	if(returnOrAppend == true)
	{	
		return htmlData;
	}
	else
	{
		$("#"+returnOrAppend).html("");
		$("#"+returnOrAppend).html(htmlData)
		if(removeSelect == true)
		{
			$("#"+returnOrAppend+" option:eq(0)").remove();	
		}
	}
}

var validateBeforeScanStart = function()
{
	if(isNaN(parseInt($("#scan_rssi_threshold").val())))
	{
		alert("Please fill threshold value");
		return false;
	}
	
	if(parseInt($("#scan_rep_flag").val()) == 1 && isNaN(parseInt($("#scan_rep_freq").val())))
	{
		alert("Please fill REPITITION FREQ  value");
		return false;
	}
	
	if(type == "freq")
	{
		if($("#freq_scan_band").val() == null || ($("#freq_scan_band").val()).length <=0 )
		{
			alert("Please fill select band");
			return false;
			
		}
	}
	
	return true;
}

var startExeScan = function()
{
	var data = {};
	data.cmdType="START_EXHAUSTIVE_SCAN";
	data.systemId = sysid;
	data.systemCode = 3;
	data.systemIp=deviceIp;
	data.id=deviceId;
	
	if(!validateBeforeScanStart("exe"))
	{
		return false;
	}
	
	
	var exeData =
	{
			"CMD_CODE": "START_EXHAUSTIVE_SCAN",
			"RSSI_THRESHOLD": parseInt($("#scan_rssi_threshold").val()),
			"REPETITION_FLAG": parseInt($("#scan_rep_flag").val()),
			"REPITITION_FREQ": parseInt($("#scan_rep_freq").val())
	}

	var scanList = [];
	var createBandListForExeScan = function(selectorId,tech)
	{
		
			var arr = $("#"+selectorId).val();
			var tempObj = {};
			tempObj.TECH=tech;
			var bandList = [];
			for(var i in arr)
			{
				var band = {}
				band.BAND=arr[i];
				bandList.push(band);
			}
			tempObj.BAND_LIST = bandList;
		
		return tempObj;
	}
	
	if($("#exe_scan_2g_tech").val() != null)
	{
		scanList.push(createBandListForExeScan('exe_scan_2g_tech','GSM'));
	}
	if($("#exe_scan_umts_tech").val() != null)
	{
		scanList.push(createBandListForExeScan('exe_scan_umts_tech','UMTS'));
	}
	if($("#exe_scan_lte_tech").val() != null)
	{
		scanList.push(createBandListForExeScan('exe_scan_lte_tech','LTE'));
	}
	
	exeData.SCAN_LIST = scanList;
	
	
	data.data=JSON.stringify(exeData);
	var getCellData = function(data)
	{
		alert('Exhaustive Scan started successfully');
		//console.log(data);
		window.location.reload();
	}	
	serverCommands(data,"service/netscan/comopr",getCellData,'json');
}




var getCellReport = function()
{
	var data = {};
	data.cmdType="GET_CELL_REPORT";
	data.systemId = sysid;
	data.systemCode = 3;
	data.systemIp=deviceIp;
	data.id=deviceId;
	var cellReport =
	{
			"CMD_CODE": "GET_CELL_REPORT",
			"TECH": $("#report_tech").val(),
	}
	//below mentioned code is for multiple techlist
	/*var cellReport =
	{
			"CMD_CODE": "GET_CELL_REPORT"
	}
	
	var Techlist = [];
	var techListData =  $("#cell_report_tech").val();
	for(var i in techListData )
	{
		var tech = {};
		tech.TECH = techListData[i];
		Techlist.push(tech);
	}
	cellReport.TECH_LIST = Techlist;*/
	data.data=JSON.stringify(cellReport);
	var getCellData = function(data)
	{
		//var gridData = createCellScanReportGridData(data);
		colmodal = cellScanColumn;
		serverData = createCellScanReportGridData(data);		
		initilizeReportGrid();		
		$( "#dialog" ).dialog({modal:true,width:'auto'});
	}	
	serverCommands(data,"service/netscan/clientopr",getCellData,'json');
}


var getFreqReport = function()
{
	var data = {};
	data.cmdType="GET_FREQ_REPORT";
	data.systemId = sysid;
	data.systemCode = 3;
	data.systemIp=deviceIp;
	data.id=deviceId;
	var cellReport =
	{
			"CMD_CODE": "GET_FREQ_REPORT",
			"TECH": $("#report_tech").val(),
	}
	data.data=JSON.stringify(cellReport);
	var getFreqData = function(data)
	{
		console.log(data);
		reportData=[];
		for(var j in data)
		{
			rptData = JSON.parse(data[j].rpt_data);
			for(var i in rptData.REPORT)
			{
				rptData.REPORT[i].TIMESTAMP_M = rptData.TIMESTAMP;
				rptData.REPORT[i].TECH = rptData.TECH;
				reportData.push(rptData.REPORT[i]);
				
			}
		}
		
		colmodal = feqScanReport;
		serverData = reportData;
		initilizeReportGrid();
		$( "#dialog" ).dialog({modal:true,width:'auto'});
		$("#data_tab").setGridParam({}).trigger("reloadGrid");
	}	
	serverCommands(data,"service/netscan/clientopr",getFreqData,'json');
}






var createCellScanReportGridData = function(data)
{
	var gridData = [];
	for(var i in data)
	{
		
	
	//var data = JSON.parse(dataForCellReport[k].rpt_data);
	//for(var i in data)
	//{
		
		data[i].REPORT = JSON.parse(data[i].rpt_data).REPORT;
		
		
		for(var j in data[i].REPORT)
		{
			data[i].REPORT[j].INDEX=j;
			data[i].REPORT[j].btsip = data[i].btsip;
			data[i].REPORT[j].id = data[i].id;
			
			
			data[i].REPORT[j].UARFCN = data[i].REPORT[j].hasOwnProperty('UARFCN')?data[i].REPORT[j].UARFCN:"";
			data[i].REPORT[j].ARFCN = data[i].REPORT[j].hasOwnProperty('ARFCN')?data[i].REPORT[j].ARFCN:"";
			data[i].REPORT[j].EARFCN = data[i].REPORT[j].hasOwnProperty('EARFCN')?data[i].REPORT[j].EARFCN:"";
			
			data[i].REPORT[j].SNR = data[i].REPORT[j].hasOwnProperty('SNR')?data[i].REPORT[j].SNR:"";
			data[i].REPORT[j].TA = data[i].REPORT[j].hasOwnProperty('TA')?data[i].REPORT[j].TA:"";
			data[i].REPORT[j].RSCP = data[i].REPORT[j].hasOwnProperty('RSCP')?data[i].REPORT[j].RSCP:"";
			data[i].REPORT[j].ECNO = data[i].REPORT[j].hasOwnProperty('ECNO')?data[i].REPORT[j].ECNO:"";
			data[i].REPORT[j].PD = data[i].REPORT[j].hasOwnProperty('PD')?data[i].REPORT[j].PD:"";
			
			data[i].REPORT[j].PCI = data[i].REPORT[j].hasOwnProperty('PCI')?data[i].REPORT[j].PCI:"";
			data[i].REPORT[j].RSRP = data[i].REPORT[j].hasOwnProperty('RSRP')?data[i].REPORT[j].RSRP:"";
			data[i].REPORT[j].RSRQ = data[i].REPORT[j].hasOwnProperty('RSRQ')?data[i].REPORT[j].RSRQ:"";
			
			if(data[i].REPORT[j].hasOwnProperty('PLMN'))
			{
				data[i].REPORT[j].MCC = data[i].REPORT[j].PLMN.MCC;
				data[i].REPORT[j].MNC = data[i].REPORT[j].PLMN.MNC;
			}
			else
			{
				data[i].REPORT[j].MCC = "";
				data[i].REPORT[j].MNC = "";
			}
			gridData.push(data[i].REPORT[j]);
		}
	//}
	}
	return gridData;
}

var getNeighReport = function()
{
	var data = {};
	data.cmdType="GET_NEIGHBOR_REPORT";
	data.systemId = sysid;
	data.systemCode = 3;
	data.systemIp=deviceIp;
	data.id=deviceId;
	var cellReport =
	{
			"CMD_CODE": "GET_NEIGHBOR_REPORT"
	}
	
	var Techlist = [];
	var techListData =  $("#neigh_report_tech").val();
	for(var i in techListData )
	{
		var tech = {};
		tech.TECH = techListData[i];
		Techlist.push(tech);
	}
	cellReport.TECH_LIST = Techlist;
	data.data=JSON.stringify(cellReport);
	var getCellData = function(data)
	{
		//var gridData = createCellScanReportGridData(data);
		colmodal = neighCoulmn;
		serverData = createNeighScanReportGridData(data);		
		initilizeReportGrid();		
		$( "#dialog" ).dialog({modal:true,width:'auto'});
	}	
	serverCommands(data,"service/netscan/comopr",getCellData,'json');
}


var createNeighScanReportGridData = function(data)
{
	var gridData = [];
	for(var i in data)
	{
		for(var j in data[i].REPORT)
		{
			
			data[i].REPORT[j].UARFCN = data[i].REPORT[j].hasOwnProperty('UARFCN')?data[i].REPORT[j].UARFCN:"";
			data[i].REPORT[j].ARFCN = data[i].REPORT[j].hasOwnProperty('ARFCN')?data[i].REPORT[j].ARFCN:"";
			data[i].REPORT[j].EARFCN = data[i].REPORT[j].hasOwnProperty('EARFCN')?data[i].REPORT[j].EARFCN:"";
			data[i].REPORT[j].PCI = data[i].REPORT[j].hasOwnProperty('PCI')?data[i].REPORT[j].PCI:"";
			data[i].REPORT[j].MCC = data[i].REPORT[j].hasOwnProperty('MCC')?data[i].REPORT[j].MCC:"";
			data[i].REPORT[j].MNC = data[i].REPORT[j].hasOwnProperty('MNC')?data[i].REPORT[j].MNC:"";
			data[i].REPORT[j].PSC = data[i].REPORT[j].hasOwnProperty('PSC')?data[i].REPORT[j].MNC:"";
			data[i].REPORT[j].NCC = data[i].REPORT[j].hasOwnProperty('NCC')?data[i].REPORT[j].NCC:"";
			data[i].REPORT[j].BCC = data[i].REPORT[j].hasOwnProperty('BCC')?data[i].REPORT[j].BCC:"";		
			gridData.push(data[i].REPORT[j]);
		}
	}
}


var saveReport = function(isSave)
{
	var data = {};
	data.cmdType=isSave?"START_SAVING_REPORTS":"STOP_SAVING_REPORTS";
	data.systemId = sysid;
	data.systemCode = 3;
	data.systemIp=deviceIp;
	data.id=deviceId;
	if(isSave)
	if(isNaN(parseInt($("#save_file_num_of_records").val()) ) || isNaN(parseInt($("#save_file_max_num_files").val())) )
	{
		alert("Please fill the values");
		return false;
		
	}
	
	
	var startSaveReport =
	{
			"CMD_CODE": "START_SAVING_REPORTS",
			"NUM_RECORDS_IN_FILE": parseInt($("#save_file_num_of_records").val()),
			"MAX_NUM_FILES": parseInt($("#save_file_max_num_files").val())
	}
	
	var stopSaveReport = {
			"CMD_CODE": "STOP_SAVING_REPORTS"
	}
	
	data.data=JSON.stringify(isSave?startSaveReport:stopSaveReport);
	var getCellData = function(data)
	{
		//alert(data);
		window.location.reload();
	}	
	serverCommands(data,"service/netscan/comopr",getCellData,'json');
}



var stopScan = function()
{
	var data = {};
	data.cmdType="STOP_SCAN";
	data.systemId = sysid;
	data.systemCode = 3;
	data.systemIp=deviceIp;
	data.id=deviceId;
	var stopScanreport =
	{
			"CMD_CODE": "STOP_SCAN",
			"TECH": $("#scan_tech").val(),
			"SCAN_TYPE": parseInt($("#stop_scan_type").val())
	}
	
	data.data=JSON.stringify(stopScanreport);
	var getCellData = function(data)
	{
		alert('Scan stopped successfully');
		window.location.reload();
	}	
	serverCommands(data,"service/netscan/comopr",getCellData,'json');
}


var createBandListOption= function(removeSelect,tech,data,returnOrAppend)
{
	var htmlData = ceateBandList(data[tech].BAND);
	if(returnOrAppend == true)
	{	
		return htmlData;
	}
	else
	{
		$("#"+returnOrAppend).html("");
		$("#"+returnOrAppend).html(htmlData)
		if(removeSelect == true)
		{
			$("#"+returnOrAppend+" option:eq(0)").remove();	
		}
	}
}

var createBandList = function(data)
{
	var option = "<option value=null>Select</option>"
	for(var i in data)
	{
		option +="<option value='"+data[i]+"'>"+data[i]+"</option>"
		
	}
	return option;
	//$("#cell_scan_band").append(option);
	
	
}


var eventRegistriaon = function()
{
	$("#scan_tech").change(function(){
		var tech = $(this).val();
		
		console.log(bandFreqMap[tech].BAND);
		$(".more_band").remove();
		$("#cell_scan_band").html("");
		$("#cell_scan_band").append(ceateBandList(bandFreqMap[tech].BAND));
		$("#cell_scan_freq_0").html("");
		
		$("#freq_scan_band").html("");
		$("#freq_scan_band").append(ceateBandList(bandFreqMap[tech].BAND));
		$("#freq_scan_band option:eq(0)").remove();
		
	});
}

var ceateBandList = function(data)
{
	var option = "<option value=null>Select</option>"
	for(var i in data)
	{
		option +="<option value='"+data[i]+"'>"+data[i]+"</option>"
		
	}
	return option;
	//$("#cell_scan_band").append(option);
	
	
}


var ceateFreqList = function(data)
{
	var option = ""
	for(var i in data)
	{
		option +="<option value='"+data[i]+"'>"+data[i]+"</option>"
	}
	return option;
	//$("#cell_scan_freq").append(option);
}



var ceateFreqList = function(bandObj,techid,freqId)
{
	
	
	var option = ""
	var freq = bandFreqMap[$("#"+techid).val()][$(bandObj).val()];
	
	if(freq != undefined)
	{
		if(freq.length > 0)
		{
			for(var i in freq)
			{
				if(Array.isArray(freq[i]))
				{
					for(var j=freq[i][0];j<=freq[i][1];j++ )
					{
						option +="<option value='"+j+"'>"+j+"</option>"
					}
				}
				else
				{
					option +="<option value='"+freq[i]+"'>"+freq[i]+"</option>"
				}
			}
			
		}
	}
	
	$("#"+freqId).html("");
	$("#"+freqId).append(option);
}


var bandfreqincrement = function()
{
	var max_fields      = 100; //maximum input boxes allowed
    var wrapper         = $("#band_freq_wrap td:eq(0)"); //Fields wrapper
    var add_button      = $("#add_more_freq_band"); //Add button ID
    
    var x = 1; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            //$(wrapper).append('<div><label>SUB_ID : </label><input type="text" name="sys_sub_id[]">&nbsp;<label>Type : </label><select name=sys_sub_type[]><option value=0>IMEI</option></select><a href="#" class="remove_field">Remove</a></div>'); //add input box
            
            var html = "<div class='more_band'>"+
							"<table  class='table table--bordered'>"+
								"<tr>"+
									"<td width=100px><label>BAND</label></td>"+
									"<td width=200px>"+
										"<select class='cell_scan_band' data-freqid='cell_scan_freq_"+x+"' name='cell_scan_band' onchange='ceateFreqList(this,"+"\"scan_tech\""+","+"\"cell_scan_freq_"+x+"\")'>"+ceateBandList(bandFreqMap[$("#scan_tech").val()].BAND)+"</select>"+
									"</td>"+
									"<td width=50px><label>Freq</label></td>"+
									"<td width=200px>"+
										"<select id='cell_scan_freq_"+x+"' class='cell_scan_freq' multiple size=3></select>"+
									"</td>"+
									"<td width=100px>"+
										"<a href='#' class='remove_field'>Remove</a>"+
									"</td>"+
								"</tr>"+
							"</table>"+
						"</div>";
            $(wrapper).append(html); //add input box
        }
    });
    
    $(wrapper).on("click",".remove_field", function(e)
    { 
    	//user click on remove text
        console.log( $(this).parent('div'));
    	e.preventDefault(); $(this).parent().closest('div').remove(); x--;
    });
}


var showSyslocModal = function()
{
$("#addGPSModal").modal("show");	
}

var addSystemLocation = function()
{
	var lat = $("#sys_lat").val();
	var lon = $("#sys_lon").val();
	
	if(lat == "" &&  lon == "")
	{
		alert("Please provide both latitude annd longitude");
		return false;
	}
	
	$.ajax({
				url:"../../service/common/addGPS",
				dataType:"text",
				type:"post",
				data:{"lat":lat,"lon":lon},
				success:function(data)
				{
					//alert(data);
					if(data == 'ok')
					{
						$("#addGPSModal").modal("hide");
					}
				}
		});
	
}


/*$( document ).ajaxStart(function() {
  $( "#loading_modal" ).modal('show');
});

$( document ).ajaxStop(function() {
  $( "#loading_modal" ).modal('hide');
});*/


var restartSystem = function()
{	
    var confirmStatus = confirm("Are you sure to Restart System?");
    if(confirmStatus){
	$.ajax({
				url:"../../service/cms/RESTART",
				dataType:"json",
				type:"post",
				success:function(data)
				{
					alert("The System is going to reboot");
				}
		});	
    }
}

var stopScanning = function(){
	$.ajax({
				url:"../../service/netscan/stopscanning",
				dataType:"json",
				type:"post",
				success:function(data)
				{
					if(data.result=="success"){
						$('#scanStatus').removeClass('fa fa-stop-circle');
						$('#scanStatus').addClass('fa fa-play-circle');
						$('#scanStatus').attr('data-status','stopped');
						alert("Scanning stopped successfully");
					}else{
						alert(data.message);
					}
				}
		});	
}

var startScanning = function(){
	$.ajax({
				url:"../../service/netscan/startscanning",
				dataType:"json",
				type:"post",
				success:function(data)
				{
					if(data.result=="success"){
						$('#scanStatus').removeClass('fa fa-play-circle');
						$('#scanStatus').addClass('fa fa-stop-circle');
						$('#scanStatus').attr('data-status','started');
						alert("Scanning started successfully");
					}else{
						alert(data.message);
					}
				}
		});	
}

var doScanOperations = function(elem){
	var scanStatus=$(elem).attr('data-status');
	var confirmStatus=false;
	if(scanStatus=='stopped'){
		confirmStatus = confirm("Are you sure to start Scanning Scheduler?");
		if(confirmStatus){
			startScanning();
		}
	}else if(scanStatus=='started'){
		confirmStatus = confirm("Scanning Scheduler in progress.Are you sure to stop?");
		if(confirmStatus){
			stopScanning();
		}
	}
}

var get4gConfigurationPage = function(groupName,ip){
	if(globalBtsDevicesStatus[ip] == "NOT REACHABLE")
	{
		alert("No operations allowed when Node is in not reachable state");
		return false;
	}
window.open("configuration4g.jsp?grp="+groupName,"_self");
}

/*
	This function is used to add blackList phones using a file ,instead of adding Manually
*/
var addTargetByFile = function()
{
	var fileSize = 0;
	 var theFile = document.getElementById("target_file").files[0];
	 if (theFile) {
		 var myReader = new FileReader();
		 myReader.onload = function(e) {
			 //myReader.onload = function(e) {
			 var content = myReader.result;
			 var lines = content.split("\r\n");
			 var dataList =[];
			 var dataCount = 0;
			 var errorList =[];
			 var errorCount = 1;
			 errorList[0] = "Error in the Following Line :\n ";
			 for (var count = 0; count < lines.length; count++) {
				 var errorFlag = true;
				 var rowContent = lines[count].split(",");
				 if(rowContent.length!=1){
					 if(count!=0){	
					    var targetImsi=rowContent[1];
					    var targetImei=rowContent[2];
						var targetName=rowContent[0];
						var targetType=rowContent[3];
						var lineNumer = count+1;
						
						if(targetName == "") 
						{
							errorList[errorCount++]= "Line = "+ lineNumer +" Name Empty";
							errorFlag = false;
							//alert("ERROR AT LINE "+lineNumer+" Please add valid target name");
							//return false;
						}
						
						else if(targetName.length>=20){
							errorList[errorCount++] ="Line = "+ lineNumer+" Name length greater than 20";
							errorFlag = false;
							//alert("ERROR AT LINE "+lineNumer+" Target Name length should not exceed 20 characters");
							//return false;
						}
						
						else if(targetImsi !="" && targetImsi.length != 15){
							errorList[errorCount++] = "Line = "+ lineNumer +" IMSI invalid";
							errorFlag = false;
							//alert("ERROR AT LINE "+lineNumer+" Please add valid IMSI");
							//return false;
						}
						else if(targetImei != "" && targetImei.length != 15){
							errorList[errorCount++] = "Line = "+ lineNumer + " IMEI invalid";
							errorFlag = false;
							//alert("ERROR AT LINE "+lineNumer+" Please add valid IMEI");
							//return false;
						}
						
						else if(targetImsi=="" && targetImei==""){
							errorList[errorCount++] = "Line = "+ lineNumer + " IMEI and IMSI invalid";
							errorFlag = false;
							//alert("ERROR AT LINE "+lineNumer+" Please add IMSI,IMEI or both of them");
							//return false;
						}
						else if(targetType==""|| targetType==undefined ||(targetType.toLowerCase()!="blacklist"&&targetType.toLowerCase()!="whitelist")){
							errorList[errorCount++]="Line = "+  lineNumer + " TYPE invalid";
							errorFlag = false;

						}
						else{
							targetType=targetType.charAt(0).toUpperCase() + targetType.slice(1).toLowerCase();
							for(var i=0;i<globalTargetList.length;i++){
								if(globalTargetList[i].name.toLowerCase()==targetName.toLowerCase()){
									errorList[errorCount++] = "Line = "+ lineNumer+ "- NAME already exists";
									errorFlag = false;
									//alert("ERROR AT LINE "+lineNumer+" Target with this name already present");
									//return false;
								}
								else if(globalTargetList[i].imsi==targetImsi && globalTargetList[i].imei==targetImei){
									errorList[errorCount++] = "Line = "+ lineNumer+ " IMEI and IMSI already present";
									errorFlag = false;
									//alert("ERROR AT LINE "+lineNumer+" IMSI and IMEI combination already present");
									//return false;
								}
							}
						 }
						if(errorFlag==true){
							dataList[dataCount++]={"targetName":rowContent[0],"targetImsi":rowContent[1],"targeImei":rowContent[2],"targetType":targetType};
						}
					 }
				 }
			}
			if(errorList.length>1){
				alert(errorList);
			}
			dataList = JSON.stringify(dataList);
			$.ajax({
			url:"../../service/common/addMultipleTarget",
				data:{"data":dataList},
				type:'post',
				success:function(data)
				{
					if(data.result=="success"){
		            alert('Target Added Successfully');
					/*var tableRow='<tr id="targetTableRow'+data.id+'"><td><input type="checkbox" value="'+data.id+'" name="checkTargetList" class="checkTargetListClass"></input></td><td>'+targetName+'</td><td  id="targetRowImsi'+data.id+'">'+targetImsi+'</td><td  id="targetRowImei'+data.id+'">'+targetImei+'</td><td>'+targetType+'</td></tr>';
					$('#displayTargetTable_body').append(tableRow);
					globalTargetList.push({"id":data.id,"name":targetName,"imsi":targetImsi,"imei":targetImei});
					$('#targetImsi').val("");
					$('#targetImei').val("");
					$('#targetName').val("");*/
		            location.reload();
					}else{
					alert('problem in adding Target');
					}
				},
				error:function(){
					alert("Problem in adding Target");
				}
			});
		 };
		 myReader.onerror = function(e) {
			 alert("File Append fail");
		 };
		 myReader.readAsText(theFile);
	 }
	 return false;
}



var addCellsByFile = function()
{
	var fileSize = 0;
	var theFile = document.getElementById("cell_file").files[0];
	 if (theFile) {
		 var myReader = new FileReader();
		 myReader.onload = function(e) {
			 var content = myReader.result;
			 var lines = content.split("\r");
			 var dataList=[];
			 var dataCount=0;
			 var errorList =[];
			 var duplicateCell=[];
			 var errorCount = 1;
			 errorList[0] = "Error in the Following Line :\n ";
			 for (var count=1; count<lines.length; count++) {
				 
				 var errorFlag = true;
				 var rowContent = lines[count].split(",");
				 if(rowContent.length!=1){
					 
					 var mcc_temp=rowContent[0];
					 var mnc=rowContent[1];
					 var lacOrTac=rowContent[2];
					 var cell=rowContent[3];
					 var lat=rowContent[4];
					 var lon=rowContent[5];
					 var rssi=rowContent[6];
					 var band=rowContent[7];
					 var arfcnOrUarfcn=rowContent[8];
					 var bsicOrPsc=rowContent[9];
					 var lineNumer = count+1;
					 
					 
					 //Validating MCC
					 var mcc_temp=rowContent[0];
					 mcc = mcc_temp.replace(/(\r\n|\n|\r)/gm, "");
					 if(mcc.length!=3){
						//alert("Please enter valid MCC At Row Number" + count);
						errorList[errorCount++] = "MCC At Line:"+ lineNumer+ " Not Valid";
						errorFlag =false;
						//return false;
						}
					 
					 //Validation MNC
					 var mnc=rowContent[1];
					 if(mnc.length!=2 && mnc.length!=3){
						// alert("Please enter valid MNC" );
						 errorList[errorCount++] = "MNC At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
						 }
					 					 
					 //Validation Cell
					 if(cell.length==0 || cell< '0'){
						// alert("Please enter valid MNC" );
						 errorList[errorCount++] = "Cell At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
						 }
					 
					 //Validation LAC/TAC
					 if(lacOrTac.length==0 || lacOrTac<0){
						// alert("Please enter valid MNC" );
						 errorList[errorCount++] = "LAC/TAC At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
						 }					 
					 //Validating Latitude
					 var lat=rowContent[4];
					 if(lat==''){
						// alert("Please enter latitude");
						 errorList[errorCount++] = "Lat At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
						 }
					    
					 //Validating Longitude   
					 var lon=rowContent[5];
					 if(lon==''){
						// alert("Please enter longitude");
						 errorList[errorCount++] = "Lon At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
						 }

					 //Validating Band
					 var band=rowContent[7];
					 if(band=="select"){
						// alert("Please Select Band");
						 errorList[errorCount++] = "Band At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
						 }

					 //Validating E/U/Arfcn
					 if(arfcnOrUarfcn.length==0 || arfcnOrUarfcn<0){
						// alert("Please Select Band");
						 errorList[errorCount++] = "E/U/Arfcn At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
						 }
					 
					 //Validating BSIC/PSC/PCI
					 if(bsicOrPsc.length==0 || bsicOrPsc<0){
						// alert("Please Select Band");
						 errorList[errorCount++] = "BSIC/PSC/PCI At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
						 }
					 
					 //Validagtin Type
					 var type1=rowContent[11];
					 if(type1=="2G"){
						 	if (band !=2 && band!=4)
						 	{
						 		// alert("Please Select Band");
								 errorList[errorCount++] = "Band At Line:"+ lineNumer+ " Not Valid";
								 errorFlag =false;
								 //return false;
						 	}
						 }
					 else  if(type1=="3G") 
					 {
						 	if (band !=1 && band!=8)
						 	{
						 		// alert("Please Select Band");
								 errorList[errorCount++] = "Band At Line:"+ lineNumer+ " Not Valid";
								 errorFlag =false;
								 //return false;
						 	}
					 } else if(type1=="4G"){
						if(band !=3 &&band!=8 &&band !=5 && band!=1 ){
							 errorList[errorCount++] = "Band At Line:"+ lineNumer+ " Not Valid";
							 errorFlag =false;
						}
					 }
					 else
					 {
							// alert("Please Select Band");
						 errorList[errorCount++] = "Type At Line:"+ lineNumer+ " Not Valid";
						 errorFlag =false;
						 //return false;
					 }
					 
						
					 
					 //Validaitng Antenna
					 //var antennaMapping = {"OH1": 22, "OV1": 23, "S3": 20, "S2": 18, "S1": 1};
					 var antennaMapping = {"OV1": 23, "S3": 20, "S2": 18, "S1": 1};
					 var antennaTempArray=rowContent[10];
					 if(antennaTempArray=="All"){
					 var allAntennaStr = "23,20,18,1";
					 }
					 
					 else{
						 var antennaArray = antennaTempArray.split("-");
						 var newTempAntennaArray = [];
						 for(var i=0; i<antennaArray.length; i++){
							 newTempAntennaArray[i]=antennaMapping[antennaArray[i]];
							 }
						 var allAntennaStr=newTempAntennaArray.join(",");
					    }
		    
					    
					    var selectedAntennaArr=[];
						if(allAntennaStr.indexOf("-1")!=-1){
							allAntennaStr="-1";
							$('#addAllAntenna option').each(function(){
								if($(this).val()!="-1"){
									selectedAntennaArr.push($(this).text());
								}
							});
						}else{
							$('#addAllAntenna option:selected').each(function(){
								selectedAntennaArr.push($(this).text());
							});
						}
						 for(var i=0;i<globalCell.length;i++){
							 if(globalCell[i].mcc==mcc &&globalCell[i].mnc==mnc && globalCell[i].cell==cell && (globalCell[i].lac==lacOrTac ||globalCell[i].tac==lacOrTac)&&globalCell[i].antenna_id==allAntennaStr){
									 errorList[errorCount++] = "Type At Line:"+ lineNumer+ "Duplicates cell And Antenna in MCC,MNC,CELL,LAC&TAC And Antenna";
									 errorFlag =false;
								}
								}
								 for(var i=0;i<duplicateCell.length;i++){
										if(duplicateCell[i]==mcc+"-"+mnc+"-"+cell+"-"+lacOrTac+"-"+allAntennaStr){
											 errorList[errorCount++] = "Type At Line:"+ lineNumer+ " Duplicates cell And Antenna in MCC,MNC,CELL,LAC&TAC And Antenna";
											 errorFlag =false;
										}
										}
	
					    
/*						var cellType=rowContent[11];
					    if(cellType=="select"){
							//alert("Please Select the Cell Type");
							errorList[errorCount++] = "Cell Type At Line:"+ lineNumer+ " Not Valid";
							errorFlag =false;
							//return false;
							}*/
					
						if(errorFlag==true){
							dataList[dataCount++]={"mcc":mcc,"mnc":mnc,"lacOrTac":lacOrTac,"cell":cell,"lat":lat,"lon":lon,"rssi":rssi,"band":band,"arfcnOrUarfcn":arfcnOrUarfcn,"bsicOrPsc":bsicOrPsc,"allAntennaStr":allAntennaStr,"cellType":type1};
							duplicateCell[dataCount]=(mcc+"-"+mnc+"-"+cell+"-"+lacOrTac+"-"+allAntennaStr);
						}
				 }
			}
			 
			 if(errorList.length>1){
					alert(errorList);
				}
			 
				$.ajax({
					
					url:"../../service/2g/addMultiplePLMN",
					data:JSON.stringify(dataList),
					type:"post",
					contentType: "application/json",
					success:function(data)
					{
						if(data.result == "SUCCESS")
						{
							alert("Data Inserted Successfully");
							location.reload();
					/*		var lacOrTacText="lac";
							if(cellType=='4G'){
								lacOrTacText="tac";
							}
							for(var i in globalPLMNList){
							var id=globalPLMNList[i];
							if(mcc==$('#mcc_'+id).text() && mnc==$('#mnc_'+id).text() && lacOrTac==$('#'+lacOrTacText+'_'+id).text() && cell==$('#cell_'+id).text()){
								var row=document.getElementById('cellTableRow'+id);
								var table=row.parentNode.parentNode;
								table.deleteRow(row.rowIndex);
							}
							}
							
							var tableRow='';
							if(cellType=='2G'){
								for(var antennaCount=0;antennaCount<selectedAntennaArr.length;antennaCount++){
									tableRow+='<tr id="cellTableRow'+data.id+'"><td><input type="checkbox" value="'+data.id+'" name="checkCellList" class="checkCellListClass"></input></td><td>'+selectedAntennaArr[antennaCount]+'</td><td>'+mcc+'</td><td>'+mnc+'</td><td>'+lacOrTac+'</td><td></td><td>'+cell+'</td><td>'+lat+'</td><td>'+lon+'</td><td>'+rssi+'</td><td>'+band+'</td><td>'+arfcnOrUarfcn+'</td><td>'+bsicOrPsc+'</td><td></td><td></td><td></td><td></td></tr>';
								}
							}else if(cellType=='3G'){
								for(var antennaCount=0;antennaCount<selectedAntennaArr.length;antennaCount++){	
									tableRow+='<tr id="cellTableRow'+data.id+'"><td><input type="checkbox" value="'+data.id+'" name="checkCellList" class="checkCellListClass"></input></td><td>'+selectedAntennaArr[antennaCount]+'</td><td>'+mcc+'</td><td>'+mnc+'</td><td>'+lacOrTac+'</td><td></td><td>'+cell+'</td><td>'+lat+'</td><td>'+lon+'</td><td>'+rssi+'</td><td>'+band+'</td><td></td><td></td><td>'+arfcnOrUarfcn+'</td><td>'+bsicOrPsc+'</td><td></td><td></td></tr>';
			                    }				
							}else if(cellType=='4G'){
								for(var antennaCount=0;antennaCount<selectedAntennaArr.length;antennaCount++){
									tableRow+='<tr id="cellTableRow'+data.id+'"><td><input type="checkbox" value="'+data.id+'" name="checkCellList" class="checkCellListClass"></input></td><td>'+selectedAntennaArr[antennaCount]+'</td><td>'+mcc+'</td><td>'+mnc+'</td><td></td><td>'+lacOrTac+'</td><td>'+cell+'</td><td>'+lat+'</td><td>'+lon+'</td><td>'+rssi+'</td><td>'+band+'</td><td></td><td></td><td></td><td></td><td>'+arfcnOrUarfcn+'</td><td>'+bsicOrPsc+'</td></tr>';
								}					
							}
								globalPLMNList.push(data.id);
								$("#displayCellTable_body").append(tableRow);
								$("#add_mcc").val("");
								$("#add_mnc").val("");
								$("#add_lacOrTac").val("");
								$("#add_cell").val("");
								$("#add_lat").val("");
								$("#add_lon").val("");
								$("#add_rssi").val("");
								$("#add_band").val("");
								$("#add_arfcnOrUarfcn").val("");
								$("#add_bsicOrPsc").val("");
								$('#add_type').val("select");
								$('#arfcnOrUarfcnTdLabel').text("ARFCN");
								$('#bsicOrPscTdLabel').text("BSIC");*/
						}
						else
						{
							alert("Error While Inserting Data");
						}
					}
				});			
		 };
		 myReader.onerror = function(e) {
			 alert("File Append fail");
		 };
		 myReader.readAsText(theFile);
	 }
	 return false;
}

function checkExt() {
	 if(document.targetAdditionFile.target_file.value.lastIndexOf(".csv")==-1) {
	    alert("Please upload .csv extention file only");
	    document.targetAdditionFile.target_file.value=null;
	    return false;
	 }
}
function checkExt_cellFile() {
	 if(document.targetAdditionFile_cell.cell_file.value.lastIndexOf(".csv")==-1) {
	    alert("Please upload .csv extention file only");
	    document.targetAdditionFile_cell.cell_file.value=null;
	    return false;
	 }
}

var CheckAllCell = function(){
	 var  selectAllCheckbox=document.getElementById("CheckAllCell");
	 var checkbox =   $("#displayCellTable_body").val();
	
	   if(selectAllCheckbox.checked==true){
	    var checkboxes =  document.getElementsByName("checkCellList");
	     for(var i=0, n=checkboxes.length;i<n;i++) {
	      checkboxes[i].checked = true;
	     }
	    }else {
	     var checkboxes =  document.getElementsByName("checkCellList");
	     for(var i=0, n=checkboxes.length;i<n;i++) {
	      checkboxes[i].checked = false;
	        }
	     } 
	   }
var checkTarget=function(){
	var checkAllTarget= document.getElementById("checktarget");
	if (checkAllTarget.checked==true){
		var allTarget = document.getElementsByName("checkTargetList");
		  for(var i=0, n=allTarget.length;i<n;i++){
			  allTarget[i].checked=true;
			}
		  }
		  else {
			  var allTargetchk = document.getElementsByName("checkTargetList");
		  for(var i=0, n=allTargetchk.length;i<n;i++){
			  allTargetchk[i].checked=false;
			}
     	}
	}


var checkBoxAntennaProfile = function(antennaId){
	 
	 if(systemTypeCode==0){
		if ($('#antennaScanning23').prop('checked') && antennaId==23){
			$('#antennaScanning1').prop('checked',false);
			//alert("Only one Antenna can be selected for scanning in case of Standalone system");
			$('#editSameAntennaCheck1').prop('checked',true);
		}
		if ($('#antennaScanning1').prop('checked') && antennaId==1){
			$('#antennaScanning23').prop('checked',false);
			$('#editSameAntennaCheck23').prop('checked',true);
			
			//alert("Only one Antenna can be selected for scanning in case of Standalone system");
		}
	   }
}

 function sendBtnData(obj){		
		var btnData =
	    {
	            "CMD":  $(obj).data("cmd"),
				"ID" : $(obj).data("id"),
				"ip" : $("#ipBms").val(),
				"pr_key" :"",
				"name": $(obj).data("name")
	    }
		if(btnData.CMD.localeCompare("RESET")==0||btnData.CMD.localeCompare("LON")==0||btnData.CMD.localeCompare("SET_TIME")==0){
			 var sure = confirm("Are you sure you want to "+btnData.name + "?");
			  if (sure == true) {
				  
			  } else {
			    	
			    	return ;
			  }
		}
		
//		if(btnData.CMD.localeCompare("SYNCH")==0||btnData.CMD.localeCompare("SET_TIME")==0)
//		{
//			
//		}
		if(($(obj).data("cmd"))=="PR")
			{
			
			btnData.pr_key=$("#Pr_text").val();
			}
		var btnDataString = JSON.stringify(btnData);
		var pr_key=$("#Pr_text").val();
           if((pr_key>120) || (pr_key<5)){
        	   alert("Periodicity Time (in mins)  range is 5 minute to 120 minute, please check value entered");
        	   return false
           } 
           
           
		
		
		$.ajax({
			url:"../../service/common/bmsData",
		    data:{btnData:btnDataString},
	        type:"post",
		        
		        success:function(data)
		        {
		        	if(data.result.localeCompare("fail")==0){
		        		alert("Failure ,command couldn't execute , please check Node status");
		        	}
		        	else{
			            
			    		if(btnData.CMD.localeCompare("SYNCH")==0||btnData.CMD.localeCompare("SET_TIME")==0)
			    		{
			            fetchSystemTime(data);
			    			
			    		}
			    		afterCommandSend($(obj).data("id"),data.data);
			    		if($(obj).data("id")!=10){
			    			alert("Success , Command succesfully executed at BMS");
			    		}
		        	}
		        }

		    });
				}			
					
 
 
 
 
 
 
 
 
 function getPeriodictyonButtonClick(){
	 

		
		$.ajax({
			url:"../../service/common/getPeriodicity",
		    data:{},
	        type:"post",
		        
		        success:function(data)
		        {
			           
			            document.getElementById("Pr_text").value = data;
			            
		        	}


		    });
		
		
		
	
//		var unixTimestamp = 1553617238;
//		var date = new Date(unixTimestamp*1000);


	 
 }
 
 
 
 function getBMSConfigurationPage(ip){
//	 var currentdate = new Date(); 
//	    var datetime = "<div style='font-weight:700;'>Current Date : " + currentdate.getDate() + "/"
//	                + (currentdate.getMonth()+1)  + "/" 
//	                + currentdate.getFullYear() + " Current Time :  "  
//	                + currentdate.getHours() + ":"  
//	                + currentdate.getMinutes() + ":" 
//	                + currentdate.getSeconds()+"</div>" ;
//	document.getElementById("bms_sys_time").innerHTML = datetime;
	
	
	 getPeriodictyonButtonClick();
	 $("#bmsmodal").modal('show');
	$("#ipBms").val(ip);				

 }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 var substart = [];
 var subend = [];
// fruits.push("Kiwi");
 

 
 
 function getJammerConfigurationPage(ip){
	 
	 $("#ipJammer").val(ip);	
	
	 
	 if(globalBtsDevicesStatus[$("#ipJammer").val()] == "NOT REACHABLE")
		{
			alert("No operations allowed when Spoiler is in not reachable state");
			//return false;
		}
		
	
	 getPeriodictyonButtonClick();
	 $("#JammerModal").modal('show');
	
	if(!$( "#JammerOnOffToggle" ).parent().hasClass( "off" ))
	{
		$("#JammerOnOffToggle").parent().css("background-color","#3c7ab7");
	}
	
	
	$("#JammerconfigurationTitle").text("Spoiler Configuration - "+JammerBand);
	$.ajax({
		url:"../../service/Jammer/getJammerDataDB",
	    data:{"ip":ip},
        type:"post",
	        
	        success:function(data)
	        {
	        	var configData = JSON.parse(data[0].config);
        		if(jammingMode==1||jammingMode==2){
        				
        			//	$('#JammerOnOffToggle').bootstrapToggle('on');	
    			}
    			else{
    				$('#JammerOnOffToggle').bootstrapToggle('off');
    			}
        		
        		
	        	for (i = 0; i < 4; i++) {
	        		substart.push((configData[i].freqStart/1000).toFixed(0));
	        		subend.push((configData[i].freqEnd/1000).toFixed(0));
	        		//$("#action_table  tbody tr:nth-child("+(i+1)+") td:last-child").html("<input type=\"range\" min="+substart[i]+ " max = "+subend[i]+ " step=\"1\"  class=\"form-range\" id=\"customRange"+(i+1)+"\">");
	        		document.getElementById("Pr_text"+(i+1)+"1").value = substart[i]//( data[0].dl_frq_start + $("#customRangewa1").val() );
		        	document.getElementById("Pr_text"+(i+1)+"2").value = subend[i] ;//+ $("#band_text"+i+"").val() ;
	        		document.getElementById("AddrStart_text"+(i+1)).value = (configData[i].addrStart);//+ $("#band_text"+i+"").val() ;
	        		document.getElementById("AddrStop_text"+(i+1)).value = (configData[i].addrEnd);
	        		document.getElementById("dwTimeCtr"+(i+1)).value = (configData[i].dwTimeCtr);
	        		if(configData[i].profileMode.localeCompare("1")==0){
	        			document.getElementById("FreqJammerSend"+(i+1)).checked = true ;
	        		}
	        		else{
	        			document.getElementById("FreqJammerSend"+(i+1)).checked = false;
	        		}
	        		document.getElementById("ramOpMode"+(i+1)).value = (configData[i].ramOpMode);
	        		if(configData[i].dwBit.localeCompare("1")==0)
        			{
	        			document.getElementById("dwBit"+(i+1)).checked  =true;
        			}
	        		else{
	        			document.getElementById("dwBit"+(i+1)).checked  =false;
	        		}
	        		//document.getElementById("dwBit"+(i+1)).value = (configData[i].dwBit);
	        		
	        		
	        	}
	        	document.getElementById("power_sendJammer1").value = configData[4].txPower;
	        	document.getElementById("loIfOffsetKhz").value = configData[5].loIfOffsetKhz/1000;
	        	
	        	
	        	
	        	// document.getElementById("Pr_text11").value = data[0].dl_frq_start;
	        	// document.getElementById("Pr_text12").value = data[0].dl_frq_end;
	        //	data.get(0);
		           
		            //document.getElementById("Pr_text").value = data;
		            
	        	}


	    });
	

 }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 var afterCommandSend = function(id,data)
 {
	 
 	switch(id)
 	{
 		case 1:
// 			$('#bmsmodal').modal('hide');
 			
 		break;
 		case 2:
// 			$('#bmsmodal').modal('hide');
 		break;
 		case 4:
// 			$('#bmsmodal').modal('hide');
 		break;
 		case 5:
// 			$('#bmsmodal').modal('hide');
 		break;
 		case 6:
 			data = data.split(",");
 			var i =2;
 			$("#bms_modal_status_table tr td").each(function(){
 				
 					//$(this).find("td:eq(1)").text(data[i]);
 					if(i<18)
 						data[i] = (parseFloat(data[i])/1000).toFixed(2);
 					$("#stat_"+(i-1)).text(data[i]);
 				
 					i++;
 			});
 			
 			
 			for(var i in data)
 			{
 				
 			}
 			
 			
 			
 			var currentdate = new Date(); 
 			var datetime = " <div style='font-weight:700;'>   Fetch Time  " + currentdate.getDate() + "/"
 			+ (currentdate.getMonth()+1)  + "/" 
 			+ currentdate.getFullYear() + "     " 
 			+ currentdate.getHours() + ":"  
 			+ currentdate.getMinutes() + ":" 
 			+ currentdate.getSeconds() +"</div";
 			
 			document.getElementById("status_timeClick").innerHTML = datetime;
 			status_success=true;
 			document.getElementById("bms_modal_status_table").style.display = "inline-table";
			document.getElementById("bmsconfigModalResize").style.height = "590px";
			
 			
// 			$("#bms_modal_status_table").show();
 		break;
 		case 10:
// 			$("#bmsmodal").modal('hide');
 			
 		break;
 		case 11:
 		
// 			$('#bmsmodal').modal('hide');
 		break;
 	}
 }

function fetchSystemTime(data){
	
	var res = data.data.split(",");
	var unixTimestamp=res[1];
	var date = new Date(unixTimestamp*1000);
	var datetime = "Current Date : " + date.getDate() + "/"
    + (date.getMonth()+1)  + "/" 
    + date.getFullYear() + " Time :  "  
    + date.getHours() + ":"  
    + date.getMinutes() + ":" 
    + date.getSeconds() ;
	document.getElementById('bms_sys_time').innerHTML=datetime;
	
}		
		
	function restartOctasic(ip){		
		
		var confirmStatus = confirm("Are you sure you want to Restart ?");
		if(confirmStatus)
		{
			$.ajax({
				url:"../../service/common/OctasicPowerOffOnCall",
			    data:{"ip":ip},
		        type:"post",
			        
			        success:function(data)
			        {
			        	
			        			alert(data);
				    		
			        	
			        }
	
			    });
		}	
	}	
function shutdownoctasic(ip){		
		
		var confirmStatus = confirm("Are you sure you want to Shutdown ?");
		if(confirmStatus)
		{
			$.ajax({
				url:"../../service/Jammer/OctasicShutDown",
			    data:{},
		        type:"get",
		        success:function(data)
		        {
	     			alert(data);
     	        }
	
		   });
		}	
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}


function sendJammerData() {
    //do something
	
	//console.log();
	 var sub1start= $("#Pr_text11").val();
	 var sub1end=   $("#Pr_text12").val();
	 var AddrStart_text1=   $("#AddrStart_text1").val();
	 var AddrStop_text1=   $("#AddrStop_text1").val();
	 var dwTimeCtr1=   $("#dwTimeCtr1").val();
	 var mode1=document.getElementById("FreqJammerSend1").checked;
	 var ramOpMode1=   $("#ramOpMode1").val();
	 var dwBit1=   $('#dwBit1').prop('checked');
	 if(dwBit1){
		 dwBit1=1;
	 }
	 else{
		 dwBit1=0;
	 }
	 if(mode1)
	 {
		 mode1=1;
	 }
	 else{
		 mode1=0;
	 }
	 
	 
	 var sub2start= $("#Pr_text21").val();
	 var sub2end=   $("#Pr_text22").val();
	 var AddrStart_text2=   $("#AddrStart_text2").val();
	 var AddrStop_text2=   $("#AddrStop_text2").val();
	 var dwTimeCtr2=   $("#dwTimeCtr2").val();
	 var mode2=document.getElementById("FreqJammerSend2").checked;
	 var ramOpMode2=   $("#ramOpMode2").val();
	 var dwBit2=   $('#dwBit2').prop('checked');
	 if(dwBit2){
		 dwBit2=1;
	 }
	 else{
		 dwBit2=0;
	 }
	 if(mode2)
	 {
		 mode2=1;
	 }
	 else{
		 mode2=0;
	 }
	 

	
	 var sub3start= $("#Pr_text31").val();
	 var sub3end=   $("#Pr_text32").val();
	 var AddrStart_text3=   $("#AddrStart_text3").val();
	 var AddrStop_text3=   $("#AddrStop_text3").val();
	 var dwTimeCtr3=   $("#dwTimeCtr3").val();
	 var mode3=document.getElementById("FreqJammerSend3").checked;
	 var ramOpMode3=   $("#ramOpMode3").val();
	 var dwBit3=   $('#dwBit3').prop('checked');
	 if(dwBit3){
		 dwBit3=1;
	 }
	 else{
		 dwBit3=0;
	 }
	 
	 if(mode3)
	 {
		 mode3=1;
	 }
	 else{
		 mode3=0;
	 }
	 

	
	 var sub4start= $("#Pr_text41").val();
	 var sub4end=   $("#Pr_text42").val();
	 var AddrStart_text4=   $("#AddrStart_text4").val();
	 var AddrStop_text4=   $("#AddrStop_text4").val();
	 var dwTimeCtr4=   $("#dwTimeCtr4").val();
	 var mode4=document.getElementById("FreqJammerSend4").checked;
	 var ramOpMode4=   $("#ramOpMode4").val();
	 var dwBit4=   $('#dwBit4').prop('checked');
	 if(dwBit4){
		 dwBit4=1;
	 }
	 else{
		 dwBit4=0;
	 }
	 if(mode4)
	 {
		 mode4=1;
	 }
	 else{
		 mode4=0;
	 }
	
//	 var sub5start= $("#Pr_text51").val();
//	 var sub5end=   $("#Pr_text52").val();
//	
//	 var sub6start= $("#Pr_text61").val();
//	 var sub6end=   $("#Pr_text62").val();
//	
//	 var sub7start= $("#Pr_text71").val();
//	 var sub7end=   $("#Pr_text72").val();
//	
//	 var sub8start= $("#Pr_text81").val();
//	 var sub8end=   $("#Pr_text82").val();
//	
//	 var sub9start= $("#Pr_text91").val();
//	 var sub9end=   $("#Pr_text92").val();
	
	var inputToSend="";
	inputToSend+=sub1start+"-"+sub1end+ "-" +AddrStart_text1 + "-" + AddrStop_text1  + "-" + dwTimeCtr1 + "-" + ramOpMode1 + "-" + dwBit1 + "-" + mode1 + " ";
	inputToSend+=sub2start+"-"+sub2end+ "-" +AddrStart_text2 + "-" + AddrStop_text2  + "-" + dwTimeCtr2 + "-" + ramOpMode2 + "-" + dwBit2 + "-" + mode2 + " ";
	inputToSend+=sub3start+"-"+sub3end+ "-" +AddrStart_text3 + "-" + AddrStop_text3  + "-" + dwTimeCtr3 + "-" + ramOpMode3 + "-" + dwBit3 + "-" + mode3 + " ";
	inputToSend+=sub4start+"-"+sub4end+ "-" +AddrStart_text4 + "-" + AddrStop_text4  + "-" + dwTimeCtr4 + "-" + ramOpMode4 + "-" + dwBit4 + "-" + mode4 + " ";
	//inputToSend+=sub5start+"-"+sub5end+ "-" +AddrStart_text4 + "-" + AddrStop_text4  + "-" + dwTimeCtr4 + " ";
//	inputToSend+=sub6start+"-"+sub6end+ " ";
//	inputToSend+=sub7start+"-"+sub7end+ " ";
//	inputToSend+=sub8start+"-"+sub8end+ " ";
//	inputToSend+=sub9start+"-"+sub9end+ " ";
	
	var jsonwa=[];
	for(k=0;k<4	;k++)
	{
		var tempJson= document.getElementById("FreqJammerSend"+(k+1)); ;
		
		if(tempJson.checked)
		{
			jsonwa.push("1");
	
		}
		else{
			jsonwa.push("0");
		}
	
	}
	
	
	
	var txPower= $("#power_sendJammer1").val();
	var loIfOffsetKhz= ($("#loIfOffsetKhz").val()*1000);
	
	//var jsonwa=["1","2","3","4","5","6","7","8","9"];
	var appliedFrequency= JSON.stringify(jsonwa);
	
	
	
	
	
	
	
	if(mode1==false && mode2==false && mode3==false && mode4==false)
	{
		alert("Atleast 1 profile needs to be selected");
		return;
	}
	
	
		
	 if((AddrStart_text1<0 || AddrStart_text1 >1023 ||isNaN(AddrStart_text1))&& mode1)
	 {
		 alert("Addr Start is incorrect");
		 return;
	 }
	 if((AddrStart_text2<0 || AddrStart_text2 >1023 ||isNaN(AddrStart_text2))&&mode2)
	 {
		 alert("Addr Start is incorrect");
		 return;
	 }
	 if((AddrStart_text3<0 || AddrStart_text3 >1023 ||isNaN(AddrStart_text3))&&mode3)
	 {
		 alert("Addr Start is incorrect");
		 return;
	 }
	 if((AddrStart_text4<0 || AddrStart_text4 >1023 ||isNaN(AddrStart_text4)) && mode4)
	 {
		 alert("Addr Start is incorrect");
		 return;
	 }
	 
	 
	 
	 
	 if((AddrStop_text1<0 || AddrStop_text1 >1023 ||isNaN(AddrStop_text1) ) &&mode1)
	 {
		 alert("Addr End is incorrect");
		 return;
	 }

	 if((AddrStop_text2<0 || AddrStop_text2 >1023 ||isNaN(AddrStop_text2)) &&mode2)
	 {
		 alert("Addr End is incorrect");
		 return;
	 }
	 if((AddrStop_text3<0 || AddrStop_text3 >1023 ||isNaN(AddrStop_text3) ) &&mode3)
	 {
		 alert("Addr End is incorrect");
		 return;
	 }
	 if((AddrStop_text4<0 || AddrStop_text4 >1023 ||isNaN(AddrStop_text4)) &&mode4)
	 {
		 alert("Addr End is incorrect");
		 return;
	 }
	 
	 
	 
	 
	
	 if((dwTimeCtr1<0 || dwTimeCtr1 >65535 ||isNaN(dwTimeCtr1)) &&mode1)
	 {
		 alert("dwTimeCtr is incorrect");
		 return;
	 }
	 
	 if((dwTimeCtr2<0 || dwTimeCtr2 >65535 ||isNaN(dwTimeCtr2)) &&mode2)
	 {
		 alert("dwTimeCtr is incorrect");
		 return;
	 }
	 
	 if((dwTimeCtr3<0 || dwTimeCtr3 >65535 ||isNaN(dwTimeCtr3)) &&mode3)
	 {
		 alert("dwTimeCtr is incorrect");
		 return;
	 }
	 
	 if((dwTimeCtr4<0 || dwTimeCtr4 >65535 ||isNaN(dwTimeCtr4)) &&mode4)
	 {
		 alert("dwTimeCtr is incorrect");
		 return;
	 }
	 
	 
	
	 ///checking for range \frequency collapse
	 
	 var startFreqs=[];
	 var stopFreqs=[];
	 
	 
	 startFreqs.push(sub1start);
	 startFreqs.push(sub2start);
	 startFreqs.push(sub3start);
	 startFreqs.push(sub4start);
	 
	 stopFreqs.push(sub1end);
	 stopFreqs.push(sub2end);
	 stopFreqs.push(sub3end);
	 stopFreqs.push(sub4end);
	 
	 var a1=false;	 
	 var a2=false;
	 var a3=false;
	 var a4=false;
	 var a5=false;
	 var a6=false;
	 
	 if(mode1==true&&mode2==true)
	 {
		  a1=check_overlapRange(AddrStart_text1,AddrStop_text1,AddrStart_text2,AddrStop_text2);
	 }
	 if(mode1==true&&mode3==true)
	 {
		  a2=check_overlapRange(AddrStart_text1,AddrStop_text1,AddrStart_text3,AddrStop_text3);
	 }
	 if(mode1==true&&mode4==true)
	 {
		  a3=check_overlapRange(AddrStart_text1,AddrStop_text1,AddrStart_text4,AddrStop_text4);
	 }
	 if(mode2==true&&mode3==true)
	 {
		  a4=check_overlapRange(AddrStart_text2,AddrStop_text2,AddrStart_text3,AddrStop_text3);
	 }
	 if(mode2==true&&mode4==true)
	 {
		  a5=check_overlapRange(AddrStart_text2,AddrStop_text2,AddrStart_text4,AddrStop_text4);
	 }
	 if(mode3==true&&mode4==true)
	 {
		  a6=check_overlapRange(AddrStart_text3,AddrStop_text3,AddrStart_text4,AddrStop_text4);
	 }
	 if(a1==true||a2==true||a3==true||a4==true||a5==true||a6==true)
	 {
 		alert("RAM Segment Start & Stop Addresses should not overlap ");	
 		return;
	 }
	
	 if(AddrStart_text1>AddrStop_text1 || AddrStart_text2>AddrStop_text2 ||AddrStart_text3>AddrStop_text3 ||AddrStart_text4>AddrStop_text4 )
	 {
		 alert("Addr Start is greater than Addr Stop");
		 return;
	 }
	 
	 
	 
	 if(sub1start> sub1end||sub2start> sub2end||sub3start> sub3end||sub4start> sub4end )
	 {
		 alert("Start Frequency is greater than Stop Frequency");
		 return;
	 }	 
	 

	 if(dwTimeCtr1<1 ||dwTimeCtr2<1||dwTimeCtr3<1||dwTimeCtr4<1|| dwTimeCtr1>65535|| dwTimeCtr2>65535|| dwTimeCtr3>65535|| dwTimeCtr4>65535)
	 {
		 alert("Invalid dwTimeCtr");
		 return;
	 }
	 if(ramOpMode1<0||ramOpMode2<0||ramOpMode3<0||ramOpMode4<0||ramOpMode1>4||ramOpMode2>4||ramOpMode3>4||ramOpMode4>4)
	 {
		 alert("Invalid ramOpMode");
		 return;
	 }
	 
	 
	 if(txPower<20||txPower >40){
		 alert("Invalid TxPower");
		 return;
	 }
	 
	 
	 if(loIfOffsetKhz<0||loIfOffsetKhz >160000){
		 alert("Invalid MaxLoIfOffset");
		 return;
	 }
	 	 
	
	//=["1","1"]
	$.ajax({
		url:"../../service/Jammer/JammerSave",
		data:{"data":inputToSend,"ip":$("#ipJammer").val(),"txPower":txPower ,"loIfOffsetKhz":loIfOffsetKhz, "appliedFrequency" : appliedFrequency},
        type:"post",
        success:function(data)
        {
        	if(data==1){
        		alert("Completed Successfully");

        	}
        	else{
        		alert(data);


	        }
        }
   });
	
	
	
	$('#JammerOnOffToggle').bootstrapToggle('on')  ;
//	$.ajax({
//		url:"../../service/Jammer/JammerOnOff",
//	    data:{"startStop":"1"},
//        type:"post",
//        success:function(data)
//        {
//        	if(data==-1){
//        		alert("Error occurred while Start/Stop Jammer");
//    		}
//        	else{
//        		alert("Start/Stop Jammer Successfull");
//        	}
//        }
//
//   });
	
	
//	$.ajax({
//		url:"../../service/common/JammertxPowerSave",
//	    data:{"txPower":txPower },
//        type:"post",
//        success:function(data)
//        {
//        	if(data==-1){
//        		alert("Error occurred while Sending TxPower");
//        	}
//        	else{
//        		alert("TxPower Sent Successfully");
//
//
//	        }
//        }
//   });
	
	
	
	return ;
}




function JammerOnOff(ip) {
 	if(!$( "#JammerOnOffToggle" ).parent().hasClass( "off" ))
	{
		$("#JammerOnOffToggle").parent().css("background-color","#3c7ab7");
	}
	var startStop = $('#JammerOnOffToggle').prop('checked'); //1 = enable , 0 = disable
	var startStopvalue;
	if (startStop==false)
	{
		startStopvalue=0;
	}
	else{
		startStopvalue=1;
	}
	$.ajax({
		url:"../../service/Jammer/JammerOnOff",
	    data:{"startStop":startStopvalue,"ip":ip},
        type:"post",
        success:function(data)
        {
        	if(data==-1){
        		alert("Error occurred while Start/Stop Jammer");
        	}
        	else{
        		alert("Start/Stop Jammer Successfull");
        	}
        }

   });
	
	
}
    



//
//$(document).on('input change', '#customRangewaa1', function() {
//	//alert($(this).val() );
//	alert(+substart[1]);
//	alert("hiya");
//	document.getElementById("Pr_text11").value = +$(this).val();
//	
//	//$('#Pr_text11').html( $(this).val() +$('#Pr_text11').val() );
//});

	$(document).on('input change', '#customRange1', function() {
		document.getElementById("Pr_text11").value = +$(this).val();
		
	});
	
	
	
	jQuery('#band_text1').on('input', function() {
		alert(document.getElementById("band_text1").value);
		document.getElementById("Pr_text12").value = +subend[0] + +$(this).val();
		
	});
	
	

	
	$(document).on('input change', '#customRange2', function() {
		document.getElementById("Pr_text21").value = +$(this).val();
		
	});
	jQuery('#band_text2').on('input', function() {
		alert(document.getElementById("band_text2").value);
		document.getElementById("Pr_text22").value = +subend[0] + +$(this).val();
		
	});
	
	
	
	
	$(document).on('input change', '#customRange3', function() {
		document.getElementById("Pr_text31").value = +$(this).val();
		
	});
	jQuery('#band_text3').on('input', function() {
		alert(document.getElementById("band_text3").value);
		document.getElementById("Pr_text32").value = +subend[0] + +$(this).val();
		
	});
	
	
	
	
	$(document).on('input change', '#customRange4', function() {
		document.getElementById("Pr_text41").value = +$(this).val();
		
	});
	
	jQuery('#band_text4').on('input', function() {
		alert(document.getElementById("band_text4").value);
		document.getElementById("Pr_text42").value = +subend[0] + +$(this).val();
		
	});
	
	
	
	$(document).on('input change', '#customRange5', function() {
		document.getElementById("Pr_text51").value = +$(this).val();
	});
	jQuery('#band_text5').on('input', function() {
		alert(document.getElementById("band_text5").value);
		document.getElementById("Pr_text52").value = +subend[0] + +$(this).val();
		
	});
	
		
	
	
	
	jQuery('#band_text6').on('input', function() {
		alert(document.getElementById("band_text6").value);
		document.getElementById("Pr_text62").value = +subend[0] + +$(this).val();
		
	});
	
	$(document).on('input change', '#customRange6', function() {
		document.getElementById("Pr_text61").value = +$(this).val();
		
	});
	
	
	
	$(document).on('input change', '#customRange7', function() {
		document.getElementById("Pr_text71").value = +$(this).val();
		
	});
	jQuery('#band_text7').on('input', function() {
		alert(document.getElementById("band_text7").value);
		document.getElementById("Pr_text72").value = +subend[0] + +$(this).val();
		
	});
	
	
	
	
	$(document).on('input change', '#customRange8', function() {
		document.getElementById("Pr_text81").value = +$(this).val();
		
	});
	jQuery('#band_text8').on('input', function() {
		alert(document.getElementById("band_text8").value);
		document.getElementById("Pr_text82").value = +subend[0] + +$(this).val();
		
	});
	
	
	

	$(document).on('input change', '#customRange9', function() {
		document.getElementById("Pr_text91").value = +$(this).val();
		
	});
	jQuery('#band_text9').on('input', function() {
		alert(document.getElementById("band_text9").value);
		document.getElementById("Pr_text92").value = +subend[0] + +$(this).val();
		
	});
	
	
	
	function check_overlapRange( a1, a2,  b1, b2){
		if (Math.max(a2, b2) - Math.min(a1, b1) <= (a2 - a1) + (b2 - b1)) {
			return true;
		}
		else{
			return false;
		}
	}



$('#useType').change(function(){

	$('#SystemManagerIpEntered').val("");
	$('#SystemManagerIpEntered4g').val("");
	var useType = $( "#useType option:selected" ).text();
	if(!(useType.localeCompare("GSU")==0 || useType.localeCompare("USU")==0 || useType.localeCompare("LSU")==0 || useType.localeCompare("Scanner")==0  ))
	{
		  $("#SystemManagerIpEntered").attr("disabled", "disabled"); 
	}
	else{
		
        $("#SystemManagerIpEntered").removeAttr("disabled"); 
	            
	        
	}

})
	
	




