var sufiId=null;
var macro_id=null;
var cell_id=null;
var dl_uarfcn=null;
var deviceCode=null;
var deviceConfig={};
var deviceIp="";
var deviceId="";
var dirPath ="../../";
var groupName='';
var deviceDetails=[];
var powerVal="";
var frameVal="";
var pscVal="";
var lacPoolStartVal="";
var lacPoolEndVal="";
var cellIdVal="";
var serverData = [];
var defaultSufiConfig={};
var bandDluarfcnMap={};
var postApplySufiConfig="";
var globalTargetList=[];
var globalTargetImsiList=[];
var globalTargetImeiList=[];
var globalConfigStatus=true;
var globalConfigCount=0;
var returnDataCellScan = {"MCC":"","MNC":"","LAC":"","CELL":"","PSC":"","UARFCN":""};

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
			if(data[i].istarget='t'){
			tableRows+='<tr><td>'+data[i].imsi+'</td><td>'+data[i].imei+'</td></tr>';
			if(data[i].imsi!=""){
			globalTargetImsiList.push(data[i].imsi);
			}
			if(data[i].imei!=""){
			globalTargetImeiList.push(data[i].imei);
			}
			}
			}
			$('#displayTargetTable_body').html(tableRows);
			}
	});
}

var getDefaultSufiConfiguration = function(){	
		$.ajax({
			url:dirPath+"service/3g/getdefaultsuficonfiguration",
			async:false,
			type:'GET',
			success:function(data)
			{
			console.log(data);
			defaultSufiConfig=data;
			returnDataCellScan = {"MCC":"","MNC":"","LAC":defaultSufiConfig.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ[0].LAC,"CELL":"","PSC":"","UARFCN":""};			
			}
		});
}

var getBandDluarfcnMap = function(){	
		$.ajax({
			url:dirPath+"service/3g/getbanddluarfcnmap",
			async:false,
			type:'GET',
			success:function(data)
			{
			bandDluarfcnMap=data;	
			}
		});
}

var subsciberListIncrement = function()
{
	var max_fields      = 100; //maximum input boxes allowed
    var wrapper         = $(".input_fields_wrap"); //Fields wrapper
    var add_button      = $(".add_field_button"); //Add button ID
    
    var x = 1; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            //$(wrapper).append('<div><label>SUB_ID : </label><input type="text" name="sys_sub_id[]">&nbsp;<label>Type : </label><select name=sys_sub_type[]><option value=0>IMEI</option></select><a href="#" class="remove_field">Remove</a></div>'); //add input box
            $(wrapper).append('<div>'+
			    	'<table>'+
		    		'<tr>'+
		    			'<td><label>SUB_ID : </label><input type="text" name="sys_sub_id[]"></td>'+
		    			'<td>'+
		    				'<label>Type : </label>'+
		    				'<select name=sys_sub_type[]>'+
								'<option value=0>IMEI</option>'+
								'<option value=1>IMSI</option>'+
							'</select>'+
						'</td>'+
		    			'<td><a href="#" class="remove_field">Remove</a></td>'+
		    		'</tr>'+
		    	'</table>'+
		    '</div>'); //add input box
        }
    });
    
    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
        console.log( $(this).parent('div'));
    	e.preventDefault(); $(this).parent().closest('div').remove(); x--;
    })

}

var getBtsOfAGroup = function()
{
	var data = 
	{
			groupName:groupName
	}
	$.ajax({
		url:dirPath+"service/3g/getBtsOfAGroup",
		async:false,
		type:'POST',
		dataType:'json',
		data:data,
		success:function(data)
		{
		deviceDetails=data;	
		},
		error:function(data){
		alert('error');
		}
	});
}

var setDeviceUniqeIDs = function(data)
{
	sufiId=data.sytemid;
	macro_id=data.macropcs;
	cell_id=data.cellid;
	dl_uarfcn=data.dluarfcn;
	deviceCode=data.dcode;
	deviceConfig=JSON.parse(data.config);
	deviceIp=data.ip;
	deviceId=data.b_id;
}

var updateStatusOfAllBts = function()
{	
		$.ajax({
			url:dirPath+"service/3g/updatestatusofallbts",
			async:false,
			type:'GET',
			success:function(data)
			{
				
			}
		});
}

//updateStatusOfAllBts();

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

/*
 * update the status of the bts 
 * */
/*var getBtsStatus = function(ip,id,code,systemid)
{
	/*var data = {};
	data.cmdType="GET_CURR_STATUS";
	data.systemId = systemid;
	data.systemCode = code;
	data.systemIp=ip;
	data.id=id
	data.data ='{"CMD_CODE":"GET_CURR_STATUS"}';
	
	var updateBTSstatus = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",updateBTSstatus,'json');	*/
//}

/*
 * update the status of the bts 
 * */
var getBtsStatus = function(ip,id,code,systemid)
{
	var data = {};
	data.cmdType="GET_CURR_STATUS";
	data.systemId = systemid;
	data.systemCode = code;
	data.systemIp=ip;
	data.id=id
	data.data ='{"CMD_CODE":"GET_CURR_STATUS"}';
	
	var updateBTSstatus = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",updateBTSstatus,'json');	
}

var updateRedirectionInfo = function()
{
	var data = {};
	data.cmdType="SET_REDIRECTION_INFO";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	//data.data ='{"REDIRECTION_INFO":{"SUFI_OF_REDIR_INFO":{"RDL_UARFCN":"'+$("#of_rdl_uarfcn").val()+'"},"SUFI_PPF_REDIR_INFO":{"RDL_UARFCN":"'+$("#ppf_rdl_uarfcn").val()+'"},"SUFI_SPF_REDIR_INFO":{"RDL_UARFCN":"'+$("#sf_rdl_uarfcn").val()+'"},"2G_REDIR_INFO":{"FREQ_BAND":"'+$("#2g_freq_band").val()+'","BCCH_ARFCN":"'+$("#2g_bcch_arfcn").val()+'"}}}';
	//data.data ='{"CMD_CODE": "SET_REDIRECTION_INFO","REDIRECTION_INFO":{"SUFI_OF_REDIR_INFO":{"RDL_UARFCN":"'+$("#of_rdl_uarfcn").val()+'"},"SUFI_PPF_REDIR_INFO":{"RDL_UARFCN":"'+$("#ppf_rdl_uarfcn").val()+'"},"SUFI_SPF_REDIR_INFO":{"RDL_UARFCN":"'+$("#sf_rdl_uarfcn").val()+'"},"2G_REDIR_INFO":{"FREQ_BAND":"'+$("#2g_freq_band").val()+'","BCCH_ARFCN":"'+$("#2g_bcch_arfcn").val()+'"}}}';
	data.data ='{"CMD_CODE": "SET_REDIRECTION_INFO","REDIRECTION_INFO":{"SUFI_OF_REDIR_INFO":{"RDL_UARFCN":"'+$("#of_rdl_uarfcn").val()+'","FREQ_BAND":"'+$("#2g_freq_band").val()+'","BCCH_ARFCN":"'+$("#2g_bcch_arfcn").val()+'"},"SUFI_PPF_REDIR_INFO":{"RDL_UARFCN":"'+$("#ppf_rdl_uarfcn").val()+'"},"SUFI_SPF_REDIR_INFO":{"RDL_UARFCN":"'+$("#sf_rdl_uarfcn").val()+'"},"SUFI_OPER_REDIR_INFO":{"RDL_UARFCN":"'+$("#oper_rdl_uarfcn").val()+'"}}}';
	
	var callBack = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callBack,'json');
}
/********mrinalini*********/
function rangeChangeEvent(){
	 
    $("#scram_code").change(function(){
    $("#range1").text($(this).val());
   });
    

    $("#lac_pool_start").change(function(){
    $("#range2").text($(this).val());
   });
    

    $("#lac_pool_end").change(function(){
    $("#range3").text($(this).val());
   });
    

    $("#cell_id").change(function(){
    $("#range4").text($(this).val());
   });
    

    $("#power_off").change(function(){
    $("#range5").text($(this).val());
   });
    

    $("#frame_off").change(function(){
    $("#range6").text($(this).val());
   });
}
/****************mrinalini**************/




$(document).ready(function()
{	
	groupName=urlParam('grp');
	getBtsOfAGroup();
	console.log(deviceDetails[0]);
	setBandsOfGsmCellBand();
	setConfigValues();
	subsciberListIncrement();
	rangeChangeEvent();
	getDefaultSufiConfiguration();
	getBandDluarfcnMap();
	getTargetList();
	//$("input[type='text']").attr({"type":"number"});
	//setSubscriberInfo();
	$('.dpcchPowerClass').focusout(function(){
	var value=parseInt($(this).val());
	if(value< -110 || value > -35){
	$(this).val(powerVal);
	alert('Please enter the value in inclusive range of -110 to -35');
	}
	});
	$('.dpchFrameClass').focusout(function(){
	var value=parseInt($(this).val());
	if(value< 0 || value > 50){
	$(this).val(frameVal);
	alert('Please enter the value in inclusive range of 0 to 50');
	}
	});
	$('#pscPPF').focusout(function(){
	var value=parseInt($(this).val());
	if(value< 0 || value> 511){
	$(this).val(pscVal);
	alert('Please enter the value in inclusive range of 0 to 511');
	}else{
	setSibInfoPPf();
	}
	});
	
	$('#pscSPF').focusout(function(){
	var value=parseInt($(this).val());
	if(value< 0 || value> 511){
	$(this).val(pscVal);
	alert('Please enter the value in inclusive range of 0 to 511');
	}else{
	setSibInfoSPf();
	}
	});
	
	$('.lacPoolStartClass').focusout(function(){
	var value=parseInt($(this).val());
	if(value< 1 || value> 65535){
	$(this).val(lacPoolStartVal);
	alert('Please enter the value in inclusive range of 1 to 65535');
	}
	});
	$('.lacPoolEndClass').focusout(function(){
	var value=parseInt($(this).val());
	if(value< 2 || value> 65535){
	$(this).val(lacPoolEndVal);
	alert('Please enter the value in inclusive range of 2 to 65535');
	}
	});
	
	$('#cellIdPPF').focusout(function(){
	var value=parseInt($(this).val());
	if(value< 1 || value> 65535){
	$(this).val(cellIdVal);
	alert('Please enter the value in inclusive range of 1 to 65535');
	}else{
	setSibInfoPPf();
	}
	});
	
	$('#cellIdSPF').focusout(function(){
	var value=parseInt($(this).val());
	if(value< 1 || value> 65535){
	$(this).val(cellIdVal);
	alert('Please enter the value in inclusive range of 1 to 65535');
	}else{
	setSibInfoSPf();
	}
	});
	
	$('.dpcchPowerClass').focusin(function(){
	  powerVal=$(this).val();
	});
	
	$('.dpchFrameClass').focusin(function(){
	frameVal=$(this).val();
	});
	
	$('.pscClass').focusin(function(){
	pscVal=$(this).val();
	});
	
	$('.lacPoolStartClass').focusin(function(){
	lacPoolStartVal=$(this).val();
	});
	
	$('.lacPoolEndClass').focusin(function(){
	lacPoolEndVal=$(this).val();
	});
	
	$('.cellIdClass').focusin(function(){
	cellIdVal=$(this).val();
	});
	
	$('#systemConfig').on("click",function(){
	openCloseDialog(this);
	});

	$('#cellConfig').on("click",function(){
	openCloseDialog(this,'scannedListButton');
	});

	$('#sibInfoConfig').on("click",function(){
	openCloseDialog(this,'cellScanButton');
	});
	
	$('#subInfoConfig').on("click",function(){
	openCloseDialog(this,'subscriberInfoButton','tfoot');
	});
	
	initializeReportGrid();
	initializeCellScanReportGrid();
});

var setBandsOfGsmCellBand = function(){
	$.ajax({
		url:"../../resources/config/band_freq_map.json",
		datetype:"json",
		async:false,
		success:function(data)
		{
		$('#band2G').html("");
         var options=createGsmBandList(data.GSM.BAND);
		$('#band2G').html(options);
		}	
	});
}

var createGsmBandList = function(data)
{
	var options = "";
	for(var i in data)
	{
		options +='<option value="'+data[i]+'">'+data[i]+'</option>';
		
	}
	options+='<option value="255">255</option>';
	return options;
	//$("#cell_scan_band").append(option);
	
	
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

var getCdrDataForSibInfo = function(){
        $.ajax({
            url: dirPath + "service/netscan/getcdrdataforsibinfo",
            type: "post",
            success: function(cdrData) {
                serverData = createCellScanReportGridData(cdrData);
				initializeCellScanReportGrid();
            }
        });
}

function getScannedTable(){
registerCellscanEvent();
getUmtsData();
//initializeReportGrid();
$("#myPopup").modal('show');
}

function setConfigValues(){
for(var i=0;i<deviceDetails.length;i++){
setSufiConfigValues(deviceDetails[i]);
}
}

function setConfiguration(){
globalConfigCount=0;
globalConfigStatus=true;
var dlUarfcnOF=$('#dlUarfcnOF').text().trim();
var dlUarfcnPPF=$('#dlUarfcnPPF').val().trim();
var dlUarfcnSPF=$('#dlUarfcnSPF').val().trim();
var pscOF=$('#pscOF').text().trim();
var pscPPF=$('#pscPPF').val().trim();
var pscSPF=$('#pscSPF').val().trim();
if(dlUarfcnOF==dlUarfcnPPF || dlUarfcnPPF==dlUarfcnSPF ||  dlUarfcnSPF==dlUarfcnOF){
alert('DL UARFCN value should be unique');
return false;
}
if(pscOF==pscPPF || pscPPF==pscSPF ||  pscSPF==pscOF){
alert('PSC value should be unique');
return false;
}

var subMode=$('#subMode option:selected').text().toLowerCase();
var holdSubscriberInfoNumber=$('#hold_sys_sub_id').val();
var holdSubscriberInfoType=$('#hold_sys_sub_type option:selected').text().toLowerCase();
var count=0;
if(subMode=="inclusion"){
	if(holdSubscriberInfoType=="imei"){
		for(var i=0;i<globalTargetImeiList.length;i++){
			if(holdSubscriberInfoNumber==globalTargetImeiList[i]) count++;
		}
	}else{
		for(var i=0;i<globalTargetImsiList.length;i++){
			if(holdSubscriberInfoNumber==globalTargetImsiList[i]) count++;
		}
	}
	if(count==0){
	alert("Hold Subscriber ID and type should be present in Target List");
	globalConfigStatus=false;
	return false;
	}
	}else{
	if(holdSubscriberInfoType=="imei"){
	for(var i=0;i<globalTargetImeiList.length;i++){
	if(holdSubscriberInfoNumber==globalTargetImeiList[i]) count++;
	}
	}else{
	for(var i=0;i<globalTargetImsiList.length;i++){
	if(holdSubscriberInfoNumber==globalTargetImsiList[i]) count++;
	}
	}
	if(count!=0){
	alert("Hold Subscriber ID and type should not be present in Target List");
	globalConfigStatus=false;
	return false;
	}
	}

for(var i=0;i<deviceDetails.length;i++){
if(globalConfigStatus){
setDeviceUniqeIDs(deviceDetails[i])
setSufiConfigForDevice(deviceDetails[i]);
}
}
/*if(globalConfigCount==2){
//alert('Configuration Applied Successfully');
//location.reload();
}else{
alert('Problem in applying Configuration');
}*/
}

function setSubscriberInfo(){
var params=JSON.parse(deviceDetails[0].config);
setSubscriberList(params.SYS_PARAMS.SUB_INFO.SUB_LIST);
$('#hold_sys_sub_id').val(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID);
$('#hold_sys_sub_type').val(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID_TYPE);
}


/*var openTrackMode = function()
{
 $( "#track_modal" ).dialog({modal:true,width:'auto'});
}*/

var updateTrackModeOnClick = function()
{
var deviceInfo=deviceDetails;
for(var i=0;i<deviceInfo.length;i++){
setUpTrackMode(deviceInfo[i].sytemid,deviceInfo[i].dcode,deviceInfo[i].ip,deviceInfo[i].b_id);
}
}

var setUpTrackMode = function(sufiId,deviceCode,deviceIp,deviceId)
{
 
 var data = {};
 
 data.cmdType="SET_TRACK_MODE";
 data.systemId = sufiId;
 data.systemCode = deviceCode;
 data.systemIp=deviceIp;
 data.id=deviceId;
 data.data ='{"CMD_CODE":"SET_TRACK_MODE","TRACK_MODE":"'+$("#track_type_mode").val()+'","SUB_ID":"'+$("#track_id").val()+'","SUB_ID_TYPE":"'+$("#track_sub_type").val()+'"}';
 
 var callback = function(data)
 {
  location.reload();
 }
 
 serverCommands(data,"service/3g/comopr",callback,'json');
}

var urlParam = function(name, w){
    w = w || window;
    var rx = new RegExp('[\&|\?]'+name+'=([^\&\#]+)'),
        val = w.location.search.match(rx);
		
    return !val ? '':decodeURI(val[1]);
}

function selectRowsCellScanData()
{
	var cellScanRowArray=[];
	if($("#cellScanDataTable").jqGrid("getGridParam", "data").length == 0){
		alert('There is no data selected');
		return false;
	}
	var myData = $("#cellScanDataTable").jqGrid("getGridParam", "data");
	ids = [];
	
	console.log(myData);
	
	for(var i in myData)
	{
	if($('#cellScanRowCheck'+myData[i].ID+'index'+myData[i].INDEX).is(':checked')){
	cellScanRowArray.push(myData[i]);
	}
	}
	
	console.log(cellScanRowArray);
	setSibInfoPpfAndSpf(cellScanRowArray);
	$('#cellScanSibInfoModal').modal('hide');
}

var setSibInfoPpfAndSpf = function(cellScanRowArray){
$('#inter_rat_sib_table_PPF tbody').html('');
$('#inter_sib_table_PPF tbody').html('');
$('#intera_sib_table_PPF tbody').find("tr:gt(0)").remove();
$('#inter_rat_sib_table_SPF tbody').html('');
$('#inter_sib_table_SPF tbody').html('');
$('#intera_sib_table_SPF tbody').find("tr:gt(0)").remove();
$('#inter_rat_sib_table_OF tbody').html('');
$('#inter_sib_table_OF tbody').html('');
$('#intera_sib_table_OF tbody').find("tr:gt(0)").remove();
	
/*$('#inter_rat_sib_table_PPF tbody').find("tr:gt(0)").remove();
$('#inter_sib_table_PPF tbody').find("tr:gt(0)").remove();
$('#intera_sib_table_PPF tbody').find("tr:gt(0)").remove();
$('#inter_rat_sib_table_SPF tbody').find("tr:gt(0)").remove();
$('#inter_sib_table_SPF tbody').find("tr:gt(0)").remove();
$('#intera_sib_table_SPF tbody').find("tr:gt(0)").remove();*/

var row='';
for(var i=0;i<cellScanRowArray.length;i++){
if(cellScanRowArray[i].TECH=='UMTS'){
	if(cellScanRowArray[i].UARFCN==$('#dlUarfcnPPF').val()){
    		row ='<tr class="intera_input_row">'
			    +'<td data-param="PSC" class="intera_opr_td">'+cellScanRowArray[i].PSC+'</td>'
    			+'<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-18"></td>'
    			+'<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-24"></td>'
    			+'<td data-param="CELL_ID" class="intera_opr_td">'+cellScanRowArray[i].CELL_ID+'</td>'
    			+'<td data-param="LAC" class="intera_opr_td">'+cellScanRowArray[i].LAC+'</td>'
    			+'<td data-param="MCC" class="intera_opr_td">'+cellScanRowArray[i].MCC+'</td>'
				+'<td data-param="MNC" class="intera_opr_td">'+cellScanRowArray[i].MNC+'</td>'
				+'</tr>';
			$('#intera_sib_table_PPF tbody').append(row);
	}else{
    		row ='<tr class="inter_input_row">'
			    +'<td data-param="PSC" class="inter_opr_td">'+cellScanRowArray[i].PSC+'</td>'
    			+'<td data-param="DL_UARFCN" class="inter_opr_td">'+cellScanRowArray[i].UARFCN+'</td>'
    			+'<td data-param="PCPICH_TX_POWER" class="inter_opr_td"><input class="inter_text" type="text" value="10"></td>'
    			+'<td data-param="Q_OFFSET_1S" class="inter_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_OFFSET_2S" class="inter_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_QUALMIN" class="inter_opr_td"><input class="inter_text" type="text" value="-18"></td>'
    			+'<td data-param="Q_RXLEVMIN" class="inter_opr_td"><input class="inter_text" type="text" value="-24"></td>'
    			+'<td data-param="CELL_ID" class="inter_opr_td">'+cellScanRowArray[i].CELL_ID+'</td>'
    			+'<td data-param="LAC" class="inter_opr_td">'+cellScanRowArray[i].LAC+'</td>'
    			+'<td data-param="MCC" class="inter_opr_td">'+cellScanRowArray[i].MCC+'</td>'
				+'<td data-param="MNC" class="inter_opr_td">'+cellScanRowArray[i].MNC+'</td>'
				+'</tr>';
			$('#inter_sib_table_PPF tbody').append(row);
	}
	
	if(cellScanRowArray[i].UARFCN==$('#dlUarfcnSPF').val()){
    		row ='<tr class="intera_input_row">'
			    +'<td data-param="PSC" class="intera_opr_td">'+cellScanRowArray[i].PSC+'</td>'
    			+'<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-18"></td>'
    			+'<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-24"></td>'
    			+'<td data-param="CELL_ID" class="intera_opr_td">'+cellScanRowArray[i].CELL_ID+'</td>'
    			+'<td data-param="LAC" class="intera_opr_td">'+cellScanRowArray[i].LAC+'</td>'
    			+'<td data-param="MCC" class="intera_opr_td">'+cellScanRowArray[i].MCC+'</td>'
				+'<td data-param="MNC" class="intera_opr_td">'+cellScanRowArray[i].MNC+'</td>'
				+'</tr>';
				$('#intera_sib_table_SPF tbody').append(row);
	}else{
    		row ='<tr class="inter_input_row">'
				+'<td data-param="PSC" class="inter_opr_td">'+cellScanRowArray[i].PSC+'</td>'
    			+'<td data-param="DL_UARFCN" class="inter_opr_td">'+cellScanRowArray[i].UARFCN+'</td>'
    			+'<td data-param="PCPICH_TX_POWER" class="inter_opr_td"><input class="inter_text" type="text" value="10"></td>'
    			+'<td data-param="Q_OFFSET_1S" class="inter_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_OFFSET_2S" class="inter_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_QUALMIN" class="inter_opr_td"><input class="inter_text" type="text" value="-18"></td>'
    			+'<td data-param="Q_RXLEVMIN" class="inter_opr_td"><input class="inter_text" type="text" value="-24"></td>'
    			+'<td data-param="CELL_ID" class="inter_opr_td">'+cellScanRowArray[i].CELL_ID+'</td>'
    			+'<td data-param="LAC" class="inter_opr_td">'+cellScanRowArray[i].LAC+'</td>'
    			+'<td data-param="MCC" class="inter_opr_td">'+cellScanRowArray[i].MCC+'</td>'
				+'<td data-param="MNC" class="inter_opr_td">'+cellScanRowArray[i].MNC+'</td>'
				+'</tr>';
			$('#inter_sib_table_SPF tbody').append(row);
	}
	
	if(cellScanRowArray[i].UARFCN==$('#dlUarfcnOF').text()){
		  row ='<tr class="intera_input_row">'
			  +'<td data-param="PSC" class="intera_opr_td">'+cellScanRowArray[i].PSC+'</td>'
			  +'<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
			  +'<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
			  +'<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-18"></td>'
			  +'<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-24"></td>'
			  +'<td data-param="CELL_ID" class="intera_opr_td">'+cellScanRowArray[i].CELL_ID+'</td>'
			  +'<td data-param="LAC" class="intera_opr_td">'+cellScanRowArray[i].LAC+'</td>'
			  +'<td data-param="MCC" class="intera_opr_td">'+cellScanRowArray[i].MCC+'</td>'
			  +'<td data-param="MNC" class="intera_opr_td">'+cellScanRowArray[i].MNC+'</td>'
			  +'</tr>';
			  $('#intera_sib_table_OF tbody').append(row);
	}else{
		row ='<tr class="inter_input_row">'
			+'<td data-param="PSC" class="inter_opr_td">'+cellScanRowArray[i].PSC+'</td>'
			+'<td data-param="DL_UARFCN" class="inter_opr_td">'+cellScanRowArray[i].UARFCN+'</td>'
			+'<td data-param="PCPICH_TX_POWER" class="inter_opr_td"><input class="inter_text" type="text" value="10"></td>'
			+'<td data-param="Q_OFFSET_1S" class="inter_opr_td"><input class="inter_text" type="text" value="1"></td>'
			+'<td data-param="Q_OFFSET_2S" class="inter_opr_td"><input class="inter_text" type="text" value="1"></td>'
			+'<td data-param="Q_QUALMIN" class="inter_opr_td"><input class="inter_text" type="text" value="-18"></td>'
			+'<td data-param="Q_RXLEVMIN" class="inter_opr_td"><input class="inter_text" type="text" value="-24"></td>'
			+'<td data-param="CELL_ID" class="inter_opr_td">'+cellScanRowArray[i].CELL_ID+'</td>'
			+'<td data-param="LAC" class="inter_opr_td">'+cellScanRowArray[i].LAC+'</td>'
			+'<td data-param="MCC" class="inter_opr_td">'+cellScanRowArray[i].MCC+'</td>'
			+'<td data-param="MNC" class="inter_opr_td">'+cellScanRowArray[i].MNC+'</td>'
			+'</tr>';
		$('#inter_sib_table_OF tbody').append(row);
	}
	}else{
      		row ='<tr class="inter_rat_input_row">'
        		+'<td data-param="Q_RXLEVMIN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="-24"></td>'
				+'<td data-param="LAC" class="inter_rat_opr_td">'+cellScanRowArray[i].LAC+'</td>'
				+'<td data-param="CELL_ID" class="inter_rat_opr_td">'+cellScanRowArray[i].CELL_ID+'</td>'
				+'<td data-param="Q_OFFSET1S_N" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="1"></td>'
				+'<td data-param="CELLINDIVIDUALOFFSET" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="1"></td>'
				+'<td data-param="NCC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+cellScanRowArray[i].NCC+'"></td>'
				+'<td data-param="BCC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+cellScanRowArray[i].BCC+'"></td>'
				+'<td data-param="FREQ_BAND" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="0"></td>'
				+'<td data-param="BCCH_ARFCN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+cellScanRowArray[i].ARFCN+'"></td>'
				+'<td data-param="MCC" class="inter_rat_opr_td">'+cellScanRowArray[i].MCC+'</td>'
				+'<td data-param="MNC" class="inter_rat_opr_td">'+cellScanRowArray[i].MNC+'</td>'
				+'</tr>';
			$('#inter_rat_sib_table_PPF tbody').append(row);
			$('#inter_rat_sib_table_SPF tbody').append(row);
			$('#inter_rat_sib_table_OF tbody').append(row);
	}
	}
}

var showSibInfoModal = function(deviceType){
if(deviceType=="of"){
$('#sibSufiType').val('OF');
$('#sibInfoLabel').text('SIB INFO NEIGHBOURS (OF)');
$('#SibInfoOfDiv').show();
$('#SibInfoPpfDiv').hide();
$('#SibInfoSpfDiv').hide();
}else if(deviceType=="ppf"){
$('#sibSufiType').val('PPF');
$('#sibInfoLabel').text('SIB INFO NEIGHBOURS (PPF)');
$('#SibInfoOfDiv').hide();
$('#SibInfoPpfDiv').show();
$('#SibInfoSpfDiv').hide();
}else{
$('#sibSufiType').val('SPF');
$('#sibInfoLabel').text('SIB INFO NEIGHBOURS (SPF)');
$('#SibInfoOfDiv').hide();
$('#SibInfoPpfDiv').hide();
$('#SibInfoSpfDiv').show();
}
$('#sibInfoModal').modal('show');
}

var getCellScanSibInfoModal = function(){
//initializeCellScanReportGrid();
getCdrDataForSibInfo();
$('#cellScanSibInfoModal').modal('show');
}

var initializeCellScanReportGrid = function()
{    
	//$("#data_tab").jqGrid('GridUnload');
	$.jgrid.gridUnload('cellScanDataTable');
	$("#cellScanDataTable").jqGrid({
	    datatype: "jsonstring",
	    datastr: serverData,
	    colModel:getCellScanColumn(),
		viewrecords: true,
	    width: 1160,				
	    height: 350,
		rowNum : 10000000,
	    onSelectRow: function (row_id) {
	        $("#grd").toggleSubGridRow(row_id);
	    },
	    subGrid : true,
	    subGridOptions: { "plusicon" : "ui-icon-triangle-1-e",
	        "minusicon" :"ui-icon-triangle-1-s",
	        "openicon" : "ui-icon-arrowreturn-1-e",
	        "reloadOnExpand" : true,
	        "selectOnExpand" : true },
	    
	    subGridRowExpanded: function(subgrid_id, row_id)
	    {
	    	
	    	var ret = $("#cellScanDataTable").jqGrid('getRowData',row_id);   //get the selected row
	        var index = ret.INDEX;  //get the data from selected row by column name
	        var id = ret.ID;
			var techid = ret.TECHID;
	        console.log(index +' : ' + id +' : '+' : '+techid);
	    	
	        //<table class="tree" id=""><tbody></tbody></table>
			if(techid != "9999")
	    	{celldata={"index":index,"id":id,"tech_id":techid};
	    	$.ajax({
	    		url:dirPath+"service/netscan/neighbours",
	    		async:false,
	    		type:'POST',
	    		dataType:'json',
	    		data:celldata,
	    		success:function(data)
	    		{
	    			$("#"+subgrid_id).html('<pre id="json-renderer"></pre>');
	    			var options = {
	    				      collapsed: $('#collapsed').is(':checked'),
	    				      withQuotes: $('#with-quotes').is(':checked')
	    				    };
	    				    $('#json-renderer').jsonViewer(JSON.parse(data[0].neigh), options);
	    		}
	    	});}
	    	
	    },
	    pager: "#cellScanDataPager"
	});
	jQuery("#cellScanDataTable").jqGrid('navGrid','#pager',{edit:false,add:false,del:false,refresh:false});
}

var createCellScanReportGridData = function(data){
	
	var returnData = [];
	for(var i in data)
	{
			var gridData = {};
			gridData["ID"] = data[i].packet_id;
	    	gridData["INDEX"] = data[i].index_id;
	    	gridData["TECH"] = data[i].packet_type;
	    	gridData["TECHID"] = data[i].tech_id;
	    	gridData["btsip"] = data[i].ip;
	    	gridData["BAND"] = data[i].band;
	    	gridData["CELL_ID"] = data[i].cell;
	    	gridData["LAT"] = data[i].lat;
	    	gridData["LONG"] = data[i].lon;
	    	gridData["MCC"] = data[i].mcc;
	    	gridData["MNC"] = data[i].mnc;
	    	gridData["PSC"] = data[i].psc;
	    	gridData["UARFCN"] = data[i].uarfcn;
	    	gridData["EARFCN"] = data[i].earfcn;
	    	gridData["ARFCN"] = data[i].arfcn;
	    	
	    	gridData["NCC"] = data[i].ncc;
	    	gridData["BCC"] = data[i].bcc;
	    	
	    	gridData["LAC"] = data[i].lac;
	    	gridData["COUNTRY"] = data[i].country;
	    	gridData["OPR"] = data[i].operators;
	    	
	    	returnData.push(gridData);
	}
	return returnData;
}

function getCellScanColumn(){
var cellScanColumn = 
	[
	 	{ label: 'ID', 			name: 'ID', width: 150,search: true,hidden:true },
	 	{ label: 'INDEX', 		name: 'INDEX', 		width: 150,search: true,hidden:true },
		{ label: 'TECH', 	name: 'TECH', 	width: 150,search: true },
		{ label: 'TECH ID', 	name: 'TECHID', 	width: 150,search: true,hidden:true },
		{ label: 'IP', 			name: 'btsip', 		width: 150,search: true },
		{ label: 'BAND', 		name: 'BAND', 		width: 150,search: true },    					
		//{ label: 'ECNO', 		name: 'ECNO', 		width: 150,search: true },
		//{ label: 'FREQ', 		name: 'FREQ', 		width: 150,search: true },      
		//{ label: 'PD', 			name: 'PD', 		width: 150,search: true },
		{ label: 'MCC', 		name: 'MCC', 		width: 100,search: true },
		{ label: 'MNC', 		name: 'MNC', 		width: 100,search: true }, 
		{ label: 'LAC', 		name: 'LAC', 		width: 150,search: true },	
		{ label: 'CELL_ID', 	name: 'CELL_ID', 	width: 100,search: true },
		{ label: 'LAT', 		name: 'LAT', 		width: 100,search: true },
		{ label: 'LONG', 		name: 'LONG', 		width: 100,search: false }, 		
		{ label: 'PSC', 		name: 'PSC', 		width: 150,search: true },
		//{ label: 'RSCP', 		name: 'RSCP', 		width: 150,search: true },
		//{ label: 'RSSI', 		name: 'RSSI', 		width: 150,search: true },	
		//{ label: 'TIMESTAMP', 	name: 'TIMESTAMP', 	width: 150,search: true },
		{ label: 'UARFCN', 		name: 'UARFCN', 	width: 150,search: true },
		{ label: 'EARFCN', 		name: 'EARFCN', 	width: 150,search: true },
		{ label: 'ARFCN', 		name: 'ARFCN', 		width: 150,search: true },
		
		{ label: 'NCC', 		name: 'NCC', 		width: 150,search: true },
		{ label: 'BCC', 		name: 'BCC', 		width: 150,search: true },
		{ label: 'COUNTRY', 	name: 'COUNTRY', 	width: 150,search: true },
		{ label: 'OPR', 		name: 'OPR', 		width: 150,search: true },
		//{ label: 'SNR', 		name: 'SNR', 		width: 150,search: true },
		//{ label: 'TA', 			name: 'TA', 		width: 150,search: true },
		//{ label: 'PCI', 		name: 'PCI', 		width: 150,search: true },
		//{ label: 'RSRP', 		name: 'RSRP', 		width: 150,search: true },
		//{ label: 'RSRQ', 		name: 'RSRQ', 		width: 150,search: true }
		{ label: 'Check/Uncheck', 		name: 'Check/Uncheck', 		width: 150,formatter: function (cellvalue, options, rowObject,iRow,iCol){
                 return displayCheckBox(rowObject,options.rowId);

             }}
		//{ label: 'SNR', 		name: 'SNR', 		width: 150,search: true },
		//{ label: 'TA', 			name: 'TA', 		width: 150,search: true },
		//{ label: 'RSRP', 		name: 'RSRP', 		width: 150,search: true },
		//{ label: 'RSRQ', 		name: 'RSRQ', 		width: 150,search: true }
	];
	return cellScanColumn;
}

function displayCheckBox(rowObject,gridRowId){
var checkBox='<input type="checkbox" id="cellScanRowCheck'+rowObject.ID+'index'+rowObject.INDEX+'"></input>';
  return checkBox;
}



var mesTrigger = function(val)
{
 
 globalConfigCount=0;
 globalConfigStatus=true;
 for(var i=0;i<deviceDetails.length;i++)
 {
  if(globalConfigStatus)
  {
   
   setDeviceUniqeIDs(deviceDetails[i]);
   if(deviceDetails[i].code != 0)
   {
    var data = getDeviceDetailData("SET_MEAS_TRIGGER");
    data.data ='{"CMD_CODE":"SET_MEAS_TRIGGER","TRIGGER":"'+val+'"}';
    var callback = function(data)
    {
     if(globalConfigCount ==  1)
     {
      location.reload();
     }
     else
     {
      globalConfigCount++;
     }
    } 
    serverCommands(data,"service/3g/clientopr",callback,'json');
   }
   
  }
 }
 
}


var getDeviceDetailData = function(command,deviceData)
{
 var data = {};
 data.cmdType=command;
 data.systemId = sufiId;
 data.systemCode = deviceCode;
 data.systemIp=deviceIp;
 data.id=deviceId
 return data;
}







