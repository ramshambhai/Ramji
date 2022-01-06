
var sufiId=null;
var macro_id=null;
var cell_id=null;
var dl_uarfcn=null;
var deviceCode=null;
var colmodal=[];
var serverData = [];
var feqScanReport =     [
              			{ label: 'TECH', name: 'TECH', width: 75,search: false },       
             			{ label: 'TIMESTAMP_M', name: 'TIMESTAMP_M', width: 150,search: true },
            			{ label: 'FREQ', name: 'FREQ', width: 150,search: true },
            			{ label: 'RSSI', name: 'RSSI', width: 150,search: true },    					
            			{ label: 'TIMESTAMP', name: 'TIMESTAMP', width: 150,search: true },
            			{ label: 'LAT', name: 'LAT', width: 150,search: true },
            			{ label: 'LONG', name: 'LONG', width: 150,search: true }
                    
                ];


var cellScanColumn = 
	[
	 	{ label: 'ID', 			name: 'id', 		width: 150,search: true,hidden:true },
	 	{ label: 'INDEX', 		name: 'INDEX', 		width: 150,search: true,hidden:true },
		{ label: 'IP', 			name: 'btsip', 		width: 75,search: true },
		{ label: 'BAND', 		name: 'BAND', 		width: 150,search: true },
		{ label: 'CELL_ID', 	name: 'CELL_ID', 	width: 150,search: true },    					
		{ label: 'ECNO', 		name: 'ECNO', 		width: 150,search: true },
		{ label: 'FREQ', 		name: 'FREQ', 		width: 150,search: true },
		{ label: 'LAT', 		name: 'LAT', 		width: 150,search: true },
		{ label: 'LONG', 		name: 'LONG', 		width: 75,search: false },       
		{ label: 'PD', 			name: 'PD', 		width: 150,search: true },
		{ label: 'MCC', 		name: 'MCC', 		width: 150,search: true },
		{ label: 'MNC', 		name: 'MNC', 		width: 150,search: true },    					
		{ label: 'PSC', 		name: 'PSC', 		width: 150,search: true },
		{ label: 'RSCP', 		name: 'RSCP', 		width: 150,search: true },
		{ label: 'RSSI', 		name: 'RSSI', 		width: 150,search: true },	
		{ label: 'TIMESTAMP', 	name: 'TIMESTAMP', 	width: 150,search: true },
		{ label: 'UARFCN', 		name: 'UARFCN', 	width: 150,search: true },
		{ label: 'EARFCN', 		name: 'EARFCN', 	width: 150,search: true },
		{ label: 'ARFCN', 		name: 'ARFCN', 		width: 150,search: true },
		{ label: 'ECNO', 		name: 'ECNO', 		width: 150,search: true },
		{ label: 'SNR', 		name: 'SNR', 		width: 150,search: true },
		{ label: 'TA', 			name: 'TA', 		width: 150,search: true },
		{ label: 'PCI', 		name: 'PCI', 		width: 150,search: true },
		{ label: 'RSRP', 		name: 'RSRP', 		width: 150,search: true },
		{ label: 'RSRQ', 		name: 'RSRQ', 		width: 150,search: true }
	]


var neighCoulmn = 
	[
	 	{ label: 'TECH', name: 'TECH', width: 150,search: true },
		{ label: 'LAT', name: 'LAT', width: 75,search: false },
		{ label: 'LONG', name: 'LONG', width: 150,search: true },
		{ label: 'TIMESTAMP', name: 'TIMESTAMP', width: 150,search: true },
		{ label: 'PCI', name: 'PCI', width: 150,search: true },    					
		{ label: 'EARFCN', name: 'EARFCN', width: 150,search: true },
		{ label: 'MCC', name: 'MCC', width: 75,search: false },       
		{ label: 'MNC', name: 'MNC', width: 150,search: true },
		{ label: 'PSC', name: 'PSC', width: 150,search: true },
		{ label: 'BCC', name: 'BCC', width: 150,search: true },    					
		{ label: 'NCC', name: 'NCC', width: 150,search: true },
		{ label: 'ARFCN', name: 'ARFCN', width: 150,search: true }
	]

//var cellScanReport = [asdfadsf];


var initilizeReportGrid = function()
{
	
//$("#data_tab").jqGrid('GridUnload');
$.jgrid.gridUnload('data_tab')
$("#data_tab").jqGrid({
   // url: '../resources/test.json',
    //mtype: "GET",
    datatype: "jsonstring",
    datastr: serverData,
   // loadonce:true,
    colModel:colmodal,
	viewrecords: true,
    width: 1300,				
    height: 250,
    rowNum: 20,
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
    	
    	var ret = $("#data_tab").jqGrid('getRowData',row_id);   //get the selected row
        index = ret.INDEX;  //get the data from selected row by column name
        id = ret.id;
        console.log(index +' : ' + id +' : ');
    	
        //<table class="tree" id=""><tbody></tbody></table>
		
    	celldata={"index":index,"id":id};
    	$.ajax({
    		url:dirPath+"service/netscan/neighbours",
    		async:false,
    		type:'POST',
    		dataType:'json',
    		data:celldata,
    		async:false,
    		success:function(data)
    		{
    			$("#"+subgrid_id).html('<pre id="json-renderer"></pre>');
    			var options = {
    				      collapsed: $('#collapsed').is(':checked'),
    				      withQuotes: $('#with-quotes').is(':checked')
    				    };
    				    $('#json-renderer').jsonViewer(JSON.parse(data[0].neigh), options);
    		}
    	});
    	
    },
    pager: "#pager"
});
jQuery("#data_tab").jqGrid('navGrid','#pager',{edit:false,add:false,del:false,refresh:false});
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



var intraSibIncrement = function()
{
	var max_fields      = 100; //maximum input boxes allowed
    var wrapper         = $("#intera_sib_table"); //Fields wrapper
    var add_button      = $(".intera_add_field_button"); //Add button ID
    
    var x = 1; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            //$(wrapper).append('<div><label>SUB_ID : </label><input type="text" name="sys_sub_id[]">&nbsp;<label>Type : </label><select name=sys_sub_type[]><option value=0>IMEI</option></select><a href="#" class="remove_field">Remove</a></div>'); //add input box
            $(wrapper).append('<tr class="intera_input_row">'
					+'<td data-param="PSC" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td data-param="CELL_ID" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td data-param="LAC" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td data-param="MCC" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td data-param="MNC" class="intera_opr_td"><input class="intera_text" type="text" value=""></td>'
					+'<td><a href="#" class="intra_remove_field">Remove</a></td>'
					+'</tr>'); 
        }
    });
    
    $(wrapper).on("click",".intra_remove_field", function(e){ //user click on remove text
        console.log( $(this).parent('tr'));
    	e.preventDefault(); $(this).parent().parent().remove(); x--;
    })
}

var getSIBIntraData = function()
{
	var data=[];
	$(".intera_input_row").each(function(index){
		var a={};
		var plmn={};
		$(this).find(".intera_opr_td").each(function(){
			
			if($(this).data("param") == "MCC" || $(this).data("param") == "MNC")
			{
				plmn[$(this).data("param")] = $(this).find('input').val();
			}
			else
			{
				a[($(this).data("param"))] = $(this).find('input').val();
			}
		});
		a.INTRA_PLMN_ID = plmn;
		data.push(a);
	});
	console.log(JSON.stringify(data));
	return JSON.stringify(data);
}



var interSibIncrement = function()
{
	var max_fields      = 100; //maximum input boxes allowed
    var wrapper         = $("#inter_sib_table"); //Fields wrapper
    var add_button      = $(".inter_add_field_button"); //Add button ID
    
    var x = 1; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            //$(wrapper).append('<div><label>SUB_ID : </label><input type="text" name="sys_sub_id[]">&nbsp;<label>Type : </label><select name=sys_sub_type[]><option value=0>IMEI</option></select><a href="#" class="remove_field">Remove</a></div>'); //add input box
            $(wrapper).append('<tr class="inter_input_row">'
					+'<td data-param="PSC" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="UARFCN" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="PCPICH_TX_POWER" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="Q_OFFSET_1S" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="Q_OFFSET_2S" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="Q_QUALMIN" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="Q_RXLEVMIN" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="CELL_ID" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="LAC" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="MCC" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td data-param="MNC" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
					+'<td><a href="#" class="inter_remove_field">Remove</a></td>'
					+'</tr>'); 
        }
    });
    
    $(wrapper).on("click",".inter_remove_field", function(e){ //user click on remove text
        console.log( $(this).parent('tr'));
    	e.preventDefault(); $(this).parent().parent().remove(); x--;
    })
}

var getSIBInterData = function()
{
	var data=[];
	$(".inter_input_row").each(function(index){
		var a={};
		var plmn={};
		$(this).find(".inter_opr_td").each(function(){
			//a[($(this).data("param"))] = $(this).find('input').val();
			if($(this).data("param") == "MCC" || $(this).data("param") == "MNC")
			{
				plmn[$(this).data("param")] = $(this).find('input').val();
			}
			else
			{
				a[($(this).data("param"))] = $(this).find('input').val();
			}
		});
		a.INTER_PLMN_ID = plmn;
		data.push(a);
	});
	console.log(JSON.stringify(data));
	return JSON.stringify(data);
	
}

var interRatSibIncrement = function()
{
	var max_fields      = 100; //maximum input boxes allowed
    var wrapper         = $("#inter_rat_sib_table"); //Fields wrapper
    var add_button      = $(".inter_rat_add_field_button"); //Add button ID
    
    var x = 1; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            //$(wrapper).append('<div><label>SUB_ID : </label><input type="text" name="sys_sub_id[]">&nbsp;<label>Type : </label><select name=sys_sub_type[]><option value=0>IMEI</option></select><a href="#" class="remove_field">Remove</a></div>'); //add input box
            $(wrapper).append('<tr class="inter_rat_input_row">'
            		+'<td data-param="Q_RXLEVMIN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td data-param="LAC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td data-param="CELL_ID" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td data-param="Q_OFFSET1S_N" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td data-param="CELL INDIVIDUAL OFFSET" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td data-param="NCC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td data-param="BCC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td data-param="FREQ_BAND" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td data-param="BCCH_ARFCN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
					+'<td><a href="#" class="inter_rat_remove_field">Remove</a></td>'
					+'</tr>'); 
        }
    });
    
    $(wrapper).on("click",".inter_rat_remove_field", function(e){ //user click on remove text
        console.log( $(this).parent('tr'));
    	e.preventDefault(); $(this).parent().parent().remove(); x--;
    })
}

var getSIBInterRatData = function()
{
	var data=[];
	$(".inter_rat_input_row").each(function(index){
		var a={};
		$(this).find(".inter_rat_opr_td").each(function(){
			a[($(this).data("param"))] = $(this).find('input').val();
		});
		data.push(a);
	});
	console.log(JSON.stringify(data));
	return JSON.stringify(data);
	
}





var getDeviceDetails = function()
{
	var data = 
	{
			ip:deviceIp
	}
	$.ajax({
		url:dirPath+"service/3g/getDeviceDetails",
		async:false,
		type:'POST',
		dataType:'json',
		data:data,
		async:false,
		success:function(data)
		{
			console.log(data);
			setDeviceUniqeIDs(data);
		}
	});
}


var setDeviceUniqeIDs = function(data)
{
	$("#sufi_id").val(data[0].sytemid);
	//$("#scram_code").val(data[0].macropcs);
	//$("#cell_id").val(data[0].cellid);
	//$("#dl_uarfcn").val(data[0].dluarfcn);
	
	
	sufiId=data[0].sytemid;
	macro_id=data[0].macropcs;
	cell_id=data[0].cellid;
	dl_uarfcn=data[0].dluarfcn;
	deviceCode=data[0].devicetypeid;
		

}


updateStatusOfAllBts = function()
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
		},
		error:function(err){
		$( "#loading_modal" ).modal('hide');
		console.log(err);   
		}
	});
}

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
	data.data ='{"REDIRECTION_INFO":{"CMD_CODE": "SET_REDIRECTION_INFO","SUFI_OF_REDIR_INFO":{"RDL_UARFCN":"'+$("#of_rdl_uarfcn").val()+'"},"SUFI_PPF_REDIR_INFO":{"RDL_UARFCN":"'+$("#ppf_rdl_uarfcn").val()+'"},"SUFI_SPF_REDIR_INFO":{"RDL_UARFCN":"'+$("#sf_rdl_uarfcn").val()+'"},"2G_REDIR_INFO":{"FREQ_BAND":"'+$("#2g_freq_band").val()+'","BCCH_ARFCN":"'+$("#2g_bcch_arfcn").val()+'"}}}';
	
	var callBack = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callBack,'json');
}





$(document).ready(function()
{	
	registerManEvents();
	subsciberListIncrement();
	intraSibIncrement();
	interSibIncrement();
	interRatSibIncrement();
	getDeviceDetails();
	initilizeReportGrid();
	eventRegistriaon();
	getBandFreqMap();
	bandfreqincrement();
});

var registerManEvents = function(){
	    $("#scan_rep_flag").change(function() {
        if($(this).val()=="0"){
			$("#scan_rep_freq").prop("disabled",true);
		}else{
			$("#scan_rep_freq").prop("disabled",false);
		}
    });
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

var getFreqScan = function()
{	
	
	var data = {};
	data.cmdType="START_FREQ_SCAN";
	data.systemId = sysid;
	data.systemCode = 3;
	data.systemIp=deviceIp;
	data.id=deviceId;
	
	if(!validateBeforeScanStart("freq"))
	{
		return false;
	}
	
	var freqData =
	{
			"CMD_CODE": "START_FREQ_SCAN",
			"RSSI_THRESHOLD": parseInt($("#scan_rssi_threshold").val()),
			"REPETITION_FLAG": parseInt($("#scan_rep_flag").val()),
			"REPITITION_FREQ": parseInt($("#scan_rep_freq").val()),
			"TECH": $("#scan_tech").val()
	}
	
	var bandlist = [];
	var bandlistdata =  $("#freq_scan_band").val();
	for(var i in bandlistdata )
	{
		var band = {};
		//band.BAND = parseInt(bandlistdata[i]);
		band.BAND = bandlistdata[i];
		bandlist.push(band);
	}
	freqData.BAND_LIST = bandlist;
	data.data=JSON.stringify(freqData);
	//data1.data = freqData;
	var getFreqData = function(data)
	{
	    alert('Frequency Scan started successfully');
		console.log(data);
		
		for(var i in data.REPORT)
		{
			data.REPORT[i].TIMESTAMP_M = data.TIMESTAMP;
			data.REPORT[i].TECH = data.TECH;
		}
		colmodal = feqScanReport;
		serverData = data.REPORT;
		initilizeReportGrid();
		$( "#dialog" ).dialog({modal:true,width:'auto'});
	}	
	serverCommands(data,"service/netscan/clientopr",getFreqData,'json');
}

var startCellScan = function()
{
	var data = {};
	data.cmdType="START_CELL_SCAN";
	data.systemId = sysid;
	data.systemCode = 3;
	data.systemIp=deviceIp;
	data.id=deviceId;
	
	if(!validateBeforeScanStart("cell"))
	{
		return false;
	}
	
	
	var bandcheck = 0;
	$(".cell_scan_band").each(function(){
		if($(this).val() == "null")
		{
			bandcheck++;
		}
	});
	
	if(bandcheck > 0)
	{
		alert("Please select band");
		return false;
	}
	
	
	
	var freqData =
	{
			"CMD_CODE": "START_CELL_SCAN",
			"NEIGHBOR_SCAN": parseInt($("#scan_neighbour").val()),
			"RSSI_THRESHOLD":parseInt($("#scan_rssi_threshold").val()),
			"REPETITION_FLAG": parseInt($("#scan_rep_flag").val()),
			"REPITITION_FREQ": parseInt($("#scan_rep_freq").val()),
			"TECH": $("#scan_tech").val()
	}
	
	var freqList = [];
	//var listdata =  $("#cell_scan_band").val().split(",");
	var bandList = $(".cell_scan_band");
	
	for(var k=0;k<bandList.length;k++)
	{	
		if($(bandList[k]).val() != null && $(bandList[k]).val() != "null")
		{
			var a ={"BAND":$(bandList[k]).val()};
			
			var b =[];
			
			var thisBandfreq = $("#"+$(bandList[k]).data("freqid")).val();
			
			if(thisBandfreq != null)
			{
				for(var j in thisBandfreq)
				{
					var c =  parseInt(thisBandfreq[j]);
					b.push({"FREQ":c});
				}
			}
			a.FREQ_LIST = b;
			freqList.push(a);
		}
		
	}
	
	freqData.FREQ_INFO = freqList;
	data.data=JSON.stringify(freqData);
	var getCellData = function(data)
	{
		console.log(data);
		alert('Cell Scan started successfully');
		//alert(data);
		window.location.reload();
	}	
	serverCommands(data,"service/netscan/clientopr",getCellData,'json');
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




var bandFreqMap = null;
var getBandFreqMap = function()
{
	$.ajax({
		url:"../../resources/config/band_freq_map.json",
		datetype:"json",
		success:function(data)
		{
			bandFreqMap = data
			console.log(data);
			//alert(data);
			$("#scan_tech").trigger("change");
			$("#scan_tech").trigger("change");
			createBandListOption(true,'GSM',bandFreqMap,'exe_scan_2g_tech');
			createBandListOption(true,'UMTS',bandFreqMap,'exe_scan_umts_tech');
			createBandListOption(true,'LTE',bandFreqMap,'exe_scan_lte_tech');
			
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


$( document ).ajaxStart(function() {
  $( "#loading_modal" ).modal('show');
});

$( document ).ajaxStop(function() {
  $( "#loading_modal" ).modal('hide');
});