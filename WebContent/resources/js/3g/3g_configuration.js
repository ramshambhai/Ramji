
var sufiId=null;
var macro_id=null;
var cell_id=null;
var dl_uarfcn=null;
var deviceCode=null;
var dirPath ="../";




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
					+'<td data-param="DL_UARFCN" class="inter_opr_td"><input class="inter_text" type="text" value=""></td>'
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
					+'<td data-param="CELLINDIVIDUALOFFSET" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value=""></td>'
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
	subsciberListIncrement();
	intraSibIncrement();
	interSibIncrement();
	interRatSibIncrement();
	getDeviceDetails();
	rangeChangeEvent();
	$(".inter_add_field_button").trigger("click");
	$("input[type='text']").attr({"type":"number"});
	getConfig();
});






