var  getConfigStructureAsString = function(params)
{

	var configStruc = "{" +
	"  \"CMD_CODE\": \"SET_SUFI_CONFIG\"," +
	"  \"BITMASK\": \""+params.BITMASK+"\"," +
	"  \"SYS_PARAMS\": {" +
	"    \"BITMASK\": \""+params.SYS_PARAMS.BITMASK+"\"," +
	"    \"SUFI_PARAMS\": {" +
	"      \"LOG_LEVEL\": \""+params.SYS_PARAMS.SUFI_PARAMS.LOG_LEVEL+"\"," +
	"      \"SUB_HOLD_TIMER\": \""+params.SYS_PARAMS.SUFI_PARAMS.SUB_HOLD_TIMER+"\"," +
	"      \"SUB_REDIR_TIMER\": \""+params.SYS_PARAMS.SUFI_PARAMS.SUB_REDIR_TIMER+"\"," +
	"      \"SUFI_OP_MODE\": \""+params.SYS_PARAMS.SUFI_PARAMS.SUFI_OP_MODE+"\"" +
	",\"SUB_TRACK_MODE\": {\"TRACK_MODE\": \""+params.SYS_PARAMS.SUFI_PARAMS.SUB_TRACK_MODE.TRACK_MODE+"\"},"+
	"\"SUFI_OFFLINE_MODE_CONFIG\":{"+
	"\"SUFI_OFFLINE_MODE\":\""+params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_MODE+"\","+
	"\"SUFI_OFFLINE_FILE_CONF\":{"+
	"\"FILE_SIZE\":\""+params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_FILE_CONF.FILE_SIZE+"\","+
	"\"MEMORY_THRESHOLD\":\""+params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_FILE_CONF.MEMORY_THRESHOLD+"\""+
	"}"+
	"},"+
	"\"SUFI_REDIR_RAT_PREFER\":\""+params.SYS_PARAMS.SUFI_PARAMS.SUFI_REDIR_RAT_PREFER+"\""+
	"    }" +
	",\"SUB_INFO\": {" +
	"      \"SUB_LIST_MODE\": \""+params.SYS_PARAMS.SUB_INFO.SUB_LIST_MODE+"\"," +
	"      \"SUB_LIST\": "+JSON.stringify(params.SYS_PARAMS.SUB_INFO.SUB_LIST)+"," +
	"      \"HOLD_SUB\": " +JSON.stringify(params.SYS_PARAMS.SUB_INFO.HOLD_SUB)+
	"      " +
	"    }," +
	"    \"CELL_INFO\": {" +
	"      \"SUFI_ID\": \""+params.SYS_PARAMS.CELL_INFO.SUFI_ID+"\"," +
	"      \"PHY_CELL_ID\": \""+params.SYS_PARAMS.CELL_INFO.PHY_CELL_ID+"\"," +
	"      \"TAC_POOL_START\": \""+params.SYS_PARAMS.CELL_INFO.TAC_POOL_START+"\"," +
	"      \"TAC_POOL_END\": \""+params.SYS_PARAMS.CELL_INFO.TAC_POOL_END+"\"," +
	//"      \"CELL_ID\": \""+params.SYS_PARAMS.CELL_INFO.CELL_ID+"\"," +
	"      \"CELL_ID\": \"1\"," +
	"      \"Freq_Band_Indicator\": \""+params.SYS_PARAMS.CELL_INFO.Freq_Band_Indicator+"\"," +
	"      \"L1_ATT\": \""+params.SYS_PARAMS.CELL_INFO.L1_ATT+"\"," +
	"      \"Reference_Signal_Power\": \""+params.SYS_PARAMS.CELL_INFO.Reference_Signal_Power+"\"," +
	"      \"p-b\": \""+params.SYS_PARAMS.CELL_INFO["p-b"]+"\"," +
	//"      \"PCPICH_POWER_PERC\": \""+params.SYS_PARAMS.CELL_INFO.PCPICH_POWER_PERC+"\"," +
	"      \"DL_EARFCN\": \""+params.SYS_PARAMS.CELL_INFO.DL_EARFCN+"\"," +
	"      \"UL_EARFCN\": \""+params.SYS_PARAMS.CELL_INFO.UL_EARFCN+"\"," +
	"      \"DL_Channel_BandWidth\": \""+params.SYS_PARAMS.CELL_INFO.DL_Channel_BandWidth+"\"," +
	"      \"PLMN_ID\": {" +
	"        \"MCC\": \""+params.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC+"\"," +
	"        \"MNC\": \""+params.SYS_PARAMS.CELL_INFO.PLMN_ID.MNC+"\"" +
	"      }" +
	"    }" +
	"  }," +
	"  \"CELL_PARAMS\": {" +
	"    \"BITMASK\": \""+params.CELL_PARAMS.BITMASK+"\"," +
	"    \"REDIRECTION_INFO\": {" +
	"      \"SUFI_OF_REDIR_INFO\": {" +
	"        \"RDL_EARFCN\": \""+params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.RDL_EARFCN+"\"," +
	"        \"RDL_UARFCN\": \""+params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.RDL_UARFCN+"\"," +
			"\"FREQ_BAND\": \""+params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.FREQ_BAND+"\"," +
	"        \"BCCH_ARFCN\": \""+params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.BCCH_ARFCN+"\"" +
	"      }," +
	"      \"SUFI_PPF_REDIR_INFO\": {" +
	"        \"RDL_EARFCN\": \""+params.CELL_PARAMS.REDIRECTION_INFO.SUFI_PPF_REDIR_INFO.RDL_EARFCN+"\"" +
	"      }," +
	"      \"SUFI_SPF_REDIR_INFO\": {" +
	"        \"RDL_EARFCN\": \""+params.CELL_PARAMS.REDIRECTION_INFO.SUFI_SPF_REDIR_INFO.RDL_EARFCN+"\"" +
	"      }," +	
	"      \"SUFI_OPER_REDIR_INFO\": {" +
	"        \"RDL_EARFCN\": \""+params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OPER_REDIR_INFO.RDL_EARFCN+"\"" +
	"      }" +
	"    }," +
	"    \"SIB_INFO\": {" +
	//"      \"CELLSELECTIONQUALITYMEASURE\": \""+params.CELL_PARAMS.SIB_INFO.CELLSELECTIONQUALITYMEASURE+"\"," +
	//"		\"MAXIMUMREPORTEDCELLSONRACH\":\""+params.CELL_PARAMS.SIB_INFO.MAXIMUMREPORTEDCELLSONRACH+"\"," +
	"      \"NEIGH_CELL_LIST\": {" +
	"        \"INTRA_FREQ\": "+JSON.stringify(params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ)+","+
	"        \"INTER_FREQ\": "+JSON.stringify(params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ)+"," +
	"        \"INTER_RAT_2G\": "+JSON.stringify(params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_2G)+","+
	"        \"INTER_RAT_3G\": "+JSON.stringify(params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_3G)+""+
	"      }" +
	"    }" +
	"  }" +
	"}";
			
	console.log(configStruc);
	return configStruc;		
}

var  setConfigStructure = function(singleDeviceInfo)
{
	var sufiType=singleDeviceInfo.dname.split(" ")[2];
    var sufiConfig = singleDeviceInfo.config;
	var params=JSON.parse(sufiConfig);
	var umtsBandDluarfcnList=bandDluarfcnMap.umts;
	params.SYS_PARAMS.SUFI_PARAMS.LOG_LEVEL=$("#log_level_"+sufiType).val();
	params.SYS_PARAMS.SUFI_PARAMS.SUB_HOLD_TIMER=$("#holdTimer"+sufiType).val();
	params.SYS_PARAMS.SUFI_PARAMS.SUB_REDIR_TIMER=$("#redirectionTimer"+sufiType).val();
	params.SYS_PARAMS.SUFI_PARAMS.SUB_TRACK_MODE.TRACK_MODE=$("#trackMode").val();
	
	params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_MODE=$("#offline_mode"+sufiType).val();
	params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_FILE_CONF.FILE_SIZE=$("#fileSize"+sufiType).val();	
	params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_FILE_CONF.MEMORY_THRESHOLD=$("#memoryThreshold"+sufiType).val();
	
	params.SYS_PARAMS.SUFI_PARAMS.SUFI_REDIR_RAT_PREFER=$("#SUFI_REDIR_RAT_PREFER"+sufiType).val();
	
	params.SYS_PARAMS.SUB_INFO.SUB_LIST_MODE=$("#subMode").val();
	params.SYS_PARAMS.SUB_INFO.SUB_LIST=getSubscriberList();
	params.SYS_PARAMS.SUB_INFO.HOLD_SUB=getHoldSubList();
	
	params.SYS_PARAMS.CELL_INFO.SUFI_ID=singleDeviceInfo.sytemid;
	params.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC=$("#mcc"+sufiType).text();
	params.SYS_PARAMS.CELL_INFO.PLMN_ID.MNC=$("#mnc"+sufiType).text();
	params.SYS_PARAMS.CELL_INFO.TAC_POOL_START=$("#lacPoolStart"+sufiType).val();
	params.SYS_PARAMS.CELL_INFO.TAC_POOL_END=$("#lacPoolEnd"+sufiType).val();
	params.SYS_PARAMS.CELL_INFO.Freq_Band_Indicator=$("#band"+sufiType).val();
	params.SYS_PARAMS.CELL_INFO.Reference_Signal_Power=$("#Signal_Power"+sufiType).val();
	params.SYS_PARAMS.CELL_INFO.L1_ATT=$("#L1_ATT"+sufiType).val();
	params.SYS_PARAMS.CELL_INFO["P-B"]=$("#pb"+sufiType).val();
	params.SYS_PARAMS.CELL_INFO.DL_Channel_BandWidthOF=$("#DL_Channel_BandWidth"+sufiType).val();
	
	
	
	if(sufiType=='OF')
	{
		params.SYS_PARAMS.CELL_INFO.PHY_CELL_ID=$("#psc"+sufiType).text();
		params.SYS_PARAMS.CELL_INFO.DL_EARFCN=$("#dlUarfcn"+sufiType).text();
		params.SYS_PARAMS.CELL_INFO.CELL_ID=$("#cellId"+sufiType).text();
	}
	else
	{
		params.SYS_PARAMS.CELL_INFO.PHY_CELL_ID=$("#psc"+sufiType).val();
		params.SYS_PARAMS.CELL_INFO.DL_EARFCN=$("#dlUarfcn"+sufiType).val();
		params.SYS_PARAMS.CELL_INFO.CELL_ID=$("#cellId"+sufiType).val();
	}
	params.SYS_PARAMS.CELL_INFO.UL_EARFCN=(calulateULearfcn($("#dlUarfcn"+sufiType).text(),$("#band"+sufiType).val()));
	
	/*var dluarfcn=params.SYS_PARAMS.CELL_INFO.DL_UARFCN;
	var uluarfcn=0;
	var formula=0;
	var count=0;
	for(var i=0;i<umtsBandDluarfcnList.length;i++)
	{
		if(dluarfcn>=umtsBandDluarfcnList[i].freq[0] &&dluarfcn<=umtsBandDluarfcnList[i].freq[1])
		{
			uluarfcn=parseInt(params.SYS_PARAMS.CELL_INFO.DL_UARFCN)+parseInt(umtsBandDluarfcnList[i].formula);
			count++;
		}
	}
	if(count==0)
	{
		uluarfcn=parseInt(params.SYS_PARAMS.CELL_INFO.DL_UARFCN)+parseInt((umtsBandDluarfcnList[umtsBandDluarfcnList.length-1].formula));
	}
	params.SYS_PARAMS.CELL_INFO.UL_UARFCN=uluarfcn.toString();*/
	
	
	
	params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.RDL_EARFCN=$("#dlUarfcnOF").text();
	params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.RDL_UARFCN=$("#RDL_UARFCN"+sufiType).val();
	params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.FREQ_BAND=$("#band2G").val();
	params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.BCCH_ARFCN=$("#arfcn2G").val();	
	params.CELL_PARAMS.REDIRECTION_INFO.SUFI_PPF_REDIR_INFO.RDL_EARFCN=$("#dlUarfcnPPF").val();
	params.CELL_PARAMS.REDIRECTION_INFO.SUFI_SPF_REDIR_INFO.RDL_EARFCN=$("#dlUarfcnSPF").val();
	params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OPER_REDIR_INFO.RDL_EARFCN=$("#dlUarfcnOF").text();
	
	params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ=JSON.parse(getSIBIntraDataLTE(sufiType));
	params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ=JSON.parse(getSIBInterDataLTE(sufiType));
	params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_2G=JSON.parse(getSIBInterRatData2g(sufiType));
	params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_3G=JSON.parse(getSIBInterRatData3g(sufiType));

	
	//params.SYS_PARAMS.CELL_INFO.PCPICH_POWER_PERC=$("#pcpich_power_perc_"+sufiType).val();
	//params.SYS_PARAMS.CELL_INFO.TOTAL_TX_POWER=$("#txPower"+sufiType).val();
	
	//params.CELL_PARAMS.DCH.DPCCH_POWER_OFF=$("#dpcchPower"+sufiType).val();
	//params.CELL_PARAMS.RRC_SETUP.DPCH_FRAME_OFF=$("#dpchFrame"+sufiType).val();
	
	//params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.RDL_UARFCN=params.SYS_PARAMS.CELL_INFO.DL_UARFCN;
	
	//params.CELL_PARAMS.SIB_INFO.CELLSELECTIONQUALITYMEASURE=$("#cell_selection_quality_measure_"+sufiType).val();
	//params.CELL_PARAMS.SIB_INFO.MAXIMUMREPORTEDCELLSONRACH=$("#MAXIMUMREPORTEDCELLSONRACH"+sufiType).val();
	
	//var configStruc=JSON.stringify(params);
	var configStruc=getConfigStructureAsString(params); 	
	console.log(configStruc);
	 return configStruc;		
}

var getSubscriberList = function()
{
	var subscriberList=[];
	
	for(var i=0;i<globalTargetImeiList.length;i++)
	{
		var imeiSubscriberObj={};
		imeiSubscriberObj.SUB_ID=globalTargetImeiList[i];
		imeiSubscriberObj.SUB_ID_TYPE="0";
		subscriberList.push(imeiSubscriberObj);
	}
	
	for(var i=0;i<globalTargetImsiList.length;i++)
	{
		var imsiSubscriberObj={};
		imsiSubscriberObj.SUB_ID=globalTargetImsiList[i];
		imsiSubscriberObj.SUB_ID_TYPE="1";
		subscriberList.push(imsiSubscriberObj);
	}
	return subscriberList;
}

var getHoldSubList = function()
{
	var subscriberList=[];
	$(".hold_value").each(function()
	{
		var temp = $(this).val().split("_");
		var tempObj={};
		tempObj.SUB_ID=temp[1];
		tempObj.SUB_ID_TYPE=temp[0];
		subscriberList.push(tempObj);
	});
	return subscriberList;
}

var createSubscriderList = function()
{
    var ids = $('input[name="sys_sub_id[]"]');
    var type = $('select[name="sys_sub_type[]"]');
    var json = "";
    for(var i =0;i<ids.length;i++)
    {
        json +='{"SUB_ID":"'+$(ids[i]).val()+'","SUB_ID_TYPE":"'+$(type[i]).val()+'"}'
        if(i < (ids.length-1))
        {
            json +=",";
        }
    }
    return json;
}


var setConfigForm = function(config)
{
    params = config;
    
    $("#para_btsmask").val(params.BITMASK);
    $("#sys_para_btsmask").val(params.SYS_PARAMS.BITMASK);
    $("#sys_sub_type").val(params.SYS_PARAMS.SUB_INFO.SUB_LIST_MODE);
    
    $("#log_level").val(params.SYS_PARAMS.SUFI_PARAMS.LOG_LEVEL);
    $("#sub_hold_timer").val(params.SYS_PARAMS.SUFI_PARAMS.SUB_HOLD_TIMER);
    $("#sub_redir_timer").val(params.SYS_PARAMS.SUFI_PARAMS.SUB_REDIR_TIMER);
    $("#sufi_op_mode").val(params.SYS_PARAMS.SUFI_PARAMS.SUFI_OP_MODE);
    
    setSubscriberList(params.SYS_PARAMS.SUB_INFO.SUB_LIST);
    
    $("#hold_sys_sub_id").val(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID);
    $("#hold_sys_sub_type").val((params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID_TYPE));
    
    $("#sufi_id").val((params.SYS_PARAMS.CELL_INFO.SUFI_ID));
    $("#scram_code").val((params.SYS_PARAMS.CELL_INFO.PRI_SCRAM_CODE));
    //$("#sufi_op_mode").val((params.SYS_PARAMS.CELL_INFO.SUFI_OP_MODE));
    
    $("#lac_pool_start").val((params.SYS_PARAMS.CELL_INFO.LAC_POOL_START));
    $("#lac_pool_end").val((params.SYS_PARAMS.CELL_INFO.LAC_POOL_END));
    
    $("#cell_id").val((params.SYS_PARAMS.CELL_INFO.CELL_ID));
    $("#total_tx_power").val((params.SYS_PARAMS.CELL_INFO.TOTAL_TX_POWER));
    $("#pcpich_power_perc").val((params.SYS_PARAMS.CELL_INFO.PCPICH_POWER_PERC));
    $("#dl_uarfcn").val((params.SYS_PARAMS.CELL_INFO.DL_UARFCN));
    $("#ul_uarfcn").val((params.SYS_PARAMS.CELL_INFO.UL_UARFCN));
    
    $("#cell_mcc").val((params.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC));
    $("#cell_mnc").val((params.SYS_PARAMS.CELL_INFO.PLMN_ID.MNC));
    
    
    $("#cell_para_btsmask").val(params.CELL_PARAMS.BITMASK);
    $("#power_off").val(params.CELL_PARAMS.DCH.DPCCH_POWER_OFF);
    $("#frame_off").val(params.CELL_PARAMS.RRC_SETUP.DPCH_FRAME_OFF);
    
    
    $("#of_rdl_uarfcn").val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.RDL_UARFCN);    
    $("#2g_freq_band").val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.FREQ_BAND);
    $("#2g_bcch_arfcn").val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.BCCH_ARFCN);
    
    $("#ppf_rdl_uarfcn").val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_PPF_REDIR_INFO.RDL_UARFCN);    
    $("#sf_rdl_uarfcn").val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_SPF_REDIR_INFO.RDL_UARFCN);
    $("#oper_rdl_uarfcn").val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OPER_REDIR_INFO.RDL_UARFCN);
    $("#cell_selection_quality_measure").val(params.CELL_PARAMS.SIB_INFO.CELLSELECTIONQUALITYMEASURE);
    $("#MAXIMUMREPORTEDCELLSONRACH").val(params.CELL_PARAMS.SIB_INFO.MAXIMUMREPORTEDCELLSONRACH);
    
    $('.inter_rat_input_row').remove();
    var inter_Rat_val = params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT;
    if(inter_Rat_val.length >= 1)
    {
    	for(var i in params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT)
    	{
    		var removeCol = (i>0)?'<td><a href="#" class="inter_rat_remove_field">Remove</a></td>':"";
    		var a ='<tr class="inter_rat_input_row">'
        		+'<td data-param="Q_RXLEVMIN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].Q_RXLEVMIN+'"></td>'
				+'<td data-param="LAC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].LAC+'"></td>'
				+'<td data-param="CELL_ID" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].CELL_ID+'"></td>'
				+'<td data-param="Q_OFFSET1S_N" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].Q_OFFSET1S_N+'"></td>'
				+'<td data-param="CELLINDIVIDUALOFFSET" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].CELLINDIVIDUALOFFSET+'"></td>'
				+'<td data-param="NCC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].NCC+'"></td>'
				+'<td data-param="BCC" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].BCC+'"></td>'
				+'<td data-param="FREQ_BAND" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].FREQ_BAND+'"></td>'
				+'<td data-param="BCCH_ARFCN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].BCCH_ARFCN+'"></td>'
				+removeCol
				+'</tr>';
    		
    		$("#inter_rat_sib_table").append(a);
    	}
    }
    
    $('.inter_input_row').remove();
    var inter_val = params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ;
    if(inter_val.length >= 1)
    {
    	for(var i in params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ)
    	{
    		var removeCol = (i>0)?'<td><a href="#" class="inter_remove_field">Remove</a></td>':"";
    		var a ='<tr class="inter_input_row"><td data-param="PSC" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].PSC+'"></td>'
    			+'<td data-param="DL_UARFCN" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].DL_UARFCN+'"></td>'
    			+'<td data-param="PCPICH_TX_POWER" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].PCPICH_TX_POWER+'"></td>'
    			+'<td data-param="Q_OFFSET_1S" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].Q_OFFSET_1S+'"></td>'
    			+'<td data-param="Q_OFFSET_2S" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].Q_OFFSET_2S+'"></td>'
    			+'<td data-param="Q_QUALMIN" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].Q_QUALMIN+'"></td>'
    			+'<td data-param="Q_RXLEVMIN" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].Q_RXLEVMIN+'"></td>'
    			+'<td data-param="CELL_ID" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].CELL_ID+'"></td>'
    			+'<td data-param="LAC" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].LAC+'"></td>'
    			+'<td data-param="MCC" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].INTER_PLMN_ID.MCC+'"></td>'
				+'<td data-param="MNC" class="inter_opr_td"><input class="inter_text" type="text" value="'+inter_val[i].INTER_PLMN_ID.MNC+'"></td>'
				+removeCol
				+'</tr>';
    			
    		$("#inter_sib_table").append(a);
    	}
    }
    
    
    $('.intera_input_row').remove();
    var intera_val = params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ;
    if(intera_val.length >= 1)
    {
    	for(var i in params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ)
    	{
    		var removeCol = (i>0)?'<td><a href="#" class="intra_remove_field">Remove</a></td>':"";
    		var a ='<tr class="intera_input_row"><td data-param="PSC" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].PSC+'"></td>'
    			+'<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].Q_OFFSET_1S+'"></td>'
    			+'<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].Q_OFFSET_2S+'"></td>'
    			+'<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].Q_QUALMIN+'"></td>'
    			+'<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].Q_RXLEVMIN+'"></td>'
    			+'<td data-param="CELL_ID" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].CELL_ID+'"></td>'
    			+'<td data-param="LAC" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].LAC+'"></td>'
    			+'<td data-param="MCC" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].INTRA_PLMN_ID.MCC+'"></td>'
				+'<td data-param="MNC" class="intera_opr_td"><input class="inter_text" type="text" value="'+intera_val[i].INTRA_PLMN_ID.MNC+'"></td>'
				+removeCol
				+'</tr>';
    		$("#intera_sib_table").append(a);
    	}
    }
$("input[type='range']").trigger("change");
        
}

var setSubscriberList = function(subList)
{
    var wrapper         = $(".input_fields_wrap"); //Fields wrapper
    $(wrapper).find("div").each(function(i)
    {
    	if(i>0)
    	{
    		$(this).remove();
    	}
    });
    
    for(var i in subList)
    {
        
        if(i == 0)
        {
            $('input[name="sys_sub_id[]"]').val(subList[i].SUB_ID);
            $('select[name="sys_sub_type[]"]').val(subList[i].SUB_ID_TYPE);
        }
        else
        {
            var option = "";
            if(subList[i].SUB_ID_TYPE == 0)
            {
                var option ='<option  value=0>IMEI</option><option value=1>IMSI</option>';
            }
            else
            {
                var option ='<option  value=1>IMSI</option><option value=0>IMEI</option>';
            }

                $(wrapper).append('<div>'+
                                    '<table>'+
                                    '<tr>'+
                                            '<td><label>SUB_ID : </label><input type="text" name="sys_sub_id[]" value="'+subList[i].SUB_ID+'"></td>'+
                                            '<td>'+
                                                    '<label>Type : </label>'+
                                                        '<select name=sys_sub_type[]>'+option+'</select>'+
                                                    '</td>'+
                                            '<td><a href="#" class="remove_field">Remove</a></td>'+
                                    '</tr>'+
                            '</table>'+
                        '</div>'); //add input box
            }
        
        
    }
    
}

var updateConfig = function(singleDeviceInfo,postApplySufiConfig,lockUnlockStatus)
{
	var data = {};
	data.cmdType="SET_SUFI_CONFIG";
	data.systemId = singleDeviceInfo.sytemid;
	data.systemCode = singleDeviceInfo.code;
	data.systemIp=singleDeviceInfo.ip;
	data.id=singleDeviceInfo.b_id;
	data.data =postApplySufiConfig;
	
	var infoForDevice = {"deviceData":singleDeviceInfo,"lockUnlockStatus":lockUnlockStatus};
        
	var callback = function(successData)
	{
		var lockUnlockStatus = getLockUnlockStatus(singleDeviceInfo.ip);
	if(lockUnlockStatus == "locked"){
			setCellUnLock(infoForDevice.deviceData);
	}
            //location.reload();
	}	
	serverCommands(data,"service/4g/clientopr",callback,'json');	
}

//Site Lock Value  
var setCellLock = function(isConfigCall,singleDeviceInfo,postApplySufiConfig)
{
	var sufiType=singleDeviceInfo.dname.split(" ")[2];
	var data = {};
	data.cmdType="SET_CELL_LOCK";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId;
	//data.data ='{"CMD_CODE":"SET_CELL_LOCK","CELL_ID":"'+deviceConfig.SYS_PARAMS.CELL_INFO.CELL_ID+'","LAC":"0"}';
	var cellId="";
	if(sufiType=='OF'){
	cellId=$("#cellId"+sufiType).text();
	}else{
	cellId=$("#cellId"+sufiType).val();
	}
	data.data ='{"CMD_CODE":"SET_CELL_LOCK","CELL_ID":"'+cellId+'"}';
	
	var infoForDevice = {"deviceData":singleDeviceInfo,"postConfig":postApplySufiConfig};
	
	
	var callBack = function(data)
	{
		
		var singleDeviceInfo = infoForDevice.deviceData;
		var postApplySufiConfig = infoForDevice.postConfig;
		
		if(isConfigCall)
		{
			updateConfig(singleDeviceInfo,postApplySufiConfig,"unlocked");
		}
		else
		{	
			location.reload();
		}
		
	}	
	serverCommands(data,"service/4g/clientopr",callBack,'json');
}

//Site UnLock Value  
var setCellUnLock = function(singleDeviceInfo)
{
	var sufiType=singleDeviceInfo.dname.split(" ")[2];
	var data = {};
	data.cmdType="SET_CELL_UNLOCK";
	/*data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId;*/
	data.systemId = singleDeviceInfo.sytemid;
	data.systemCode = singleDeviceInfo.code;
	data.systemIp=singleDeviceInfo.ip;
	data.id=singleDeviceInfo.b_id;
	
	var cellId="";
	if(sufiType=='OF'){
	cellId=$("#cellId"+sufiType).text();
	}else{
	cellId=$("#cellId"+sufiType).val();
	}
	data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+cellId+'"}';
	
	var callback = function(data)
	{
		
		if(globalConfigCount == 2)
		{
	        alert('Configuration Applied Successfully');	
			location.reload();
		}
		else
		{
			globalConfigCount++;
		}
		
	}	
	serverCommands(data,"service/4g/clientopr",callback,'json');
}

var updateSibInfoOnClick = function(){
if($('#hold_sys_sub_id').val()==''){
alert('Subscriber ID cannot be blank');
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

var deviceInfo=deviceDetails;
for(var i=0;i<deviceInfo.length;i++){
var subscriberListJson=JSON.stringify(getSubscriberList());
updateSibInfoOnDevice(deviceInfo[i].sytemid,deviceInfo[i].dcode,deviceInfo[i].ip,deviceInfo[i].b_id,subscriberListJson);
updateSibInfoOnDb(deviceInfo[i].sytemid,deviceInfo[i].dcode,deviceInfo[i].ip,deviceInfo[i].b_id,subscriberListJson);
}
}

var updateSibInfoOnDevice = function(sufiId,deviceCode,deviceIp,deviceId,subscriberListJson)
{
	var data = {};
	data.cmdType="SET_TRACK_SUB_LIST";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId;
	//data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+$("#cell_id").val()+'","LAC":"'+$("#lac").val()+'"}';
	data.data ='{"CMD_CODE": "SET_TRACK_SUB_LIST","SUB_INFO":{"SUB_LIST_MODE":"'+$("#subMode").val()+'","SUB_LIST":'+subscriberListJson+',"HOLD_SUB":{"SUB_ID":"'+$("input[name=\"hold_sys_sub_id\"]").val()+'","SUB_ID_TYPE":"'+$("select[name=\"hold_sys_sub_type\"]").val()+'"}}}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');
}

var updateSibInfoOnDb = function(sufiId,deviceCode,deviceIp,deviceId,subscriberListJson)
{
	var data = {};
	data.ip=deviceIp;
	data.id=deviceId;        
	//data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+$("#cell_id").val()+'","LAC":"'+$("#lac").val()+'"}';
	data.config ='{"SUB_LIST_MODE":"'+$("#subMode").val()+'","SUB_LIST":'+subscriberListJson+',"HOLD_SUB":{"SUB_ID":"'+$("input[name=\"hold_sys_sub_id\"]").val()+'","SUB_ID_TYPE":"'+$("select[name=\"hold_sys_sub_type\"]").val()+'"}}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/updateConfigSubInfo",callback,'json');
}


var clearHoldSub = function()
{
	$("#displayHold_body").html("");
}

var updateSubHoldOnClick = function(){
/*if($('#hold_sys_sub_id').val()==''){
alert('Subscriber ID cannot be blank');
return false;
}*/

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

	var deviceInfo=deviceDetails;
	for(var i=0;i<deviceInfo.length;i++)
	{
		
		if(deviceInfo[i].use_type_name == "4G Locator")
		{
			updateSubHoldOnDevice(deviceInfo[i].sytemid,deviceInfo[i].dcode,deviceInfo[i].ip,deviceInfo[i].b_id);
			updateSubHoldOnDb(deviceInfo[i].sytemid,deviceInfo[i].dcode,deviceInfo[i].ip,deviceInfo[i].b_id);
		}
	}
}


var updateSubHoldOnDb = function(sufiId,deviceCode,deviceIp,deviceId)
{
	var data = {};
	data.ip=deviceIp;
	data.id=deviceId;        
	//data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+$("#cell_id").val()+'","LAC":"'+$("#lac").val()+'"}';
	//data.config ='{"SUB_ID":"'+$("input[name=\"hold_sys_sub_id\"]").val()+'","SUB_ID_TYPE":"'+$("select[name=\"hold_sys_sub_type\"]").val()+'"}';
	data.config =JSON.stringify(getHoldSubList());
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/4g/updateConfigSubHold",callback,'json');
}

var updateSubHoldOnDevice = function(sufiId,deviceCode,deviceIp,deviceId)
{
		var data = {};
		data.cmdType="SET_HOLD_SUB";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId;
	data.data ='{"CMD_CODE":"SET_HOLD_SUB","HOLD_SUB":'+JSON.stringify(getHoldSubList())+'}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/4g/clientopr",callback,'json');
}

var getHoldSubData = function()
{
	
	var data = [];
	$(".hold_value").each(function(){
		var temp = $(this).val().split("_");
		data.push(
				{
					"SUB_ID":parseInt(temp[1]),
					"SUB_ID_TYPE":parseInt(temp[0])
				});
	});
	return data;
}



/*var mesTrigger = function(val)
{
	
	var data = getDeviceDetailData("SET_MEAS_TRIGGER");
	data.data ='{"CMD_CODE":"SET_MEAS_TRIGGER","TRIGGER":"'+val+'"}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');
}*/

var getDeviceDetailData = function(command)
{
	var data = {};
	data.cmdType=command;
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	return data;
}

var setSufiConfigValues = function(singleDeviceInfo)
{
	
	try
	{
		var sufiType=singleDeviceInfo.dname.split(" ")[2];
	    var sufiConfig = singleDeviceInfo.config;
		var params=JSON.parse(sufiConfig);
		$("#log_level_"+sufiType).val(params.SYS_PARAMS.SUFI_PARAMS.LOG_LEVEL);
		$("#holdTimer"+sufiType).val(params.SYS_PARAMS.SUFI_PARAMS.SUB_HOLD_TIMER);
		$("#redirectionTimer"+sufiType).val(params.SYS_PARAMS.SUFI_PARAMS.SUB_REDIR_TIMER);		
		$("#mcc"+sufiType).text((params.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC));
		$("#mnc"+sufiType).text((params.SYS_PARAMS.CELL_INFO.PLMN_ID.MNC));
		$("#band2G").val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.FREQ_BAND);
		$("#arfcn2G").val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.BCCH_ARFCN);
		if(sufiType=='OF')
		{
			$("#psc"+sufiType).text((params.SYS_PARAMS.CELL_INFO.PHY_CELL_ID));
			$("#dlUarfcn"+sufiType).text((params.SYS_PARAMS.CELL_INFO.DL_EARFCN));
			$("#cellId"+sufiType).text((params.SYS_PARAMS.CELL_INFO.CELL_ID));
		}
		else
		{
			$("#psc"+sufiType).val((params.SYS_PARAMS.CELL_INFO.PHY_CELL_ID));
			$("#dlUarfcn"+sufiType).val((params.SYS_PARAMS.CELL_INFO.DL_EARFCN));
			$("#cellId"+sufiType).val((params.SYS_PARAMS.CELL_INFO.CELL_ID));
		}
		$("#lacPoolStart"+sufiType).val((params.SYS_PARAMS.CELL_INFO.TAC_POOL_START));
		$("#lacPoolEnd"+sufiType).val((params.SYS_PARAMS.CELL_INFO.TAC_POOL_END));
		$("#band"+sufiType).val((params.SYS_PARAMS.CELL_INFO.Freq_Band_Indicator));
		
		//$("#txPower"+sufiType).val((params.SYS_PARAMS.CELL_INFO.TOTAL_TX_POWER));
		//$("#dpcchPower"+sufiType).val(params.CELL_PARAMS.DCH.DPCCH_POWER_OFF);
		//$("#dpchFrame"+sufiType).val(params.CELL_PARAMS.RRC_SETUP.DPCH_FRAME_OFF);
		
		$("#pcpich_power_perc_"+sufiType).val((params.SYS_PARAMS.CELL_INFO.PCPICH_POWER_PERC));
		$("#trackMode").val(params.SYS_PARAMS.SUFI_PARAMS.SUB_TRACK_MODE.TRACK_MODE);		
		$("#subMode").val(params.SYS_PARAMS.SUB_INFO.SUB_LIST_MODE);
		
		
		
		$("#offline_mode"+sufiType).val(params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_MODE);
		$("#fileSize"+sufiType).val(params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_FILE_CONF.FILE_SIZE);
		$("#memoryThreshold"+sufiType).val(params.SYS_PARAMS.SUFI_PARAMS.SUFI_OFFLINE_MODE_CONFIG.SUFI_OFFLINE_FILE_CONF.MEMORY_THRESHOLD);
		$("#SUFI_REDIR_RAT_PREFER"+sufiType).val(params.SYS_PARAMS.SUFI_PARAMS.SUFI_REDIR_RAT_PREFER);
		
		$("#Signal_Power"+sufiType).val(params.SYS_PARAMS.CELL_INFO.Reference_Signal_Power);
		
		$("#pb"+sufiType).val(params.SYS_PARAMS.CELL_INFO["p-b"]);
		$("#DL_Channel_BandWidth"+sufiType).val(params.SYS_PARAMS.CELL_INFO.DL_Channel_BandWidth);
		$("#RDL_UARFCN"+sufiType).val(params.CELL_PARAMS.REDIRECTION_INFO.SUFI_OF_REDIR_INFO.RDL_UARFCN);
		
		
		var holdData = params.SYS_PARAMS.SUB_INFO.HOLD_SUB;
		$("#displayHoldTargetTable tbody").html("");
		for(var k=0;k<holdData.length;k++)
		{
			//$("#hold_sys_sub_id").val(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID);
			//$("#hold_sys_sub_type").val(params.SYS_PARAMS.SUB_INFO.HOLD_SUB.SUB_ID_TYPE);
			
			var temp  = holdData[k];
			addHoldSbscribersIntoList(temp.SUB_ID,temp.SUB_ID_TYPE);
		}
		
		setSibInfo(singleDeviceInfo);
	}
	catch(err)
	{
		
	}
	
}

function setSibInfo(singleDeviceInfo){
var params=JSON.parse(singleDeviceInfo.config);
var sufiType=singleDeviceInfo.dname.split(" ")[2];
$('#intera_sib_table_'+sufiType+' tbody').html('');
$('#inter_sib_table_'+sufiType+' tbody').html('');
$('#inter_rat_sib_table_'+sufiType+' tbody').html('');
//$("#cell_selection_quality_measure_"+sufiType).val(params.CELL_PARAMS.SIB_INFO.CELLSELECTIONQUALITYMEASURE);
//$("#MAXIMUMREPORTEDCELLSONRACH"+sufiType).val(params.CELL_PARAMS.SIB_INFO.MAXIMUMREPORTEDCELLSONRACH);
var inter_Rat_val = params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_2G;
 if(inter_Rat_val.length >= 1)
    {
    	for(var i in params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_2G)
    	{
    		var a ='<tr class="inter_rat_input_row">'
        		+'<td data-param="Starting_ARFCN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].Starting_ARFCN+'"></td>'
        		+'<td data-param="NCC_Permitted" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].NCC_Permitted+'"></td>'
				+'<td data-param="Band_Indicator" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].Band_Indicator+'"></td>'
				+'<td data-param="Priority" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].Priority+'"></td>'
				+'<td data-param="CELL_ID" class="inter_rat_opr_td">'+inter_Rat_val[i].CELL_ID+'</td>'
				+'<td data-param="LAC" class="inter_rat_opr_td">'+inter_Rat_val[i].LAC+'</td>'
				
				
				//+'<td data-param="ARFCN1" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].Explicit_List_ARFCN.ARFCN1+'"></td>'
				//+'<td data-param="ARFCN2" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].Explicit_List_ARFCN.ARFCN2+'"></td>'
				//+'<td data-param="FREQ_BAND" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].FREQ_BAND+'"></td>'
				//+'<td data-param="BCCH_ARFCN" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].BCCH_ARFCN+'"></td>'
				+'<td data-param="MCC" class="inter_rat_opr_td">'+inter_Rat_val[i].INTER_RAT_PLMN_ID.MCC+'</td>'
				+'<td data-param="MNC" class="inter_rat_opr_td">'+inter_Rat_val[i].INTER_RAT_PLMN_ID.MNC+'</td>'
				+'</tr>';
			$('#inter_rat_2g_sib_table_'+sufiType+' tbody').append(a);
    	}
    }
 var inter_Rat_val = params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_3G;
 if(inter_Rat_val.length >= 1)
    {
    	for(var i in params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_3G)
    	{
    		var a ='<tr class="inter_rat_input_row">'
        		+'<td data-param="DL_Carrier_Frequency" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].DL_Carrier_Frequency+'"></td>'
        		+'<td data-param="Priority" class="inter_rat_opr_td"><input class="inter_rat_text" type="text" value="'+inter_Rat_val[i].Priority+'"></td>'
        		+'<td data-param="LAC" class="inter_rat_opr_td">'+inter_Rat_val[i].LAC+'</td>'
				+'<td data-param="CELL_ID" class="inter_rat_opr_td">'+inter_Rat_val[i].CELL_ID+'</td>'
				+'<td data-param="MCC" class="inter_rat_opr_td">'+inter_Rat_val[i].INTER_RAT_PLMN_ID.MCC+'</td>'
				+'<td data-param="MNC" class="inter_rat_opr_td">'+inter_Rat_val[i].INTER_RAT_PLMN_ID.MNC+'</td>'
				+'</tr>';
			$('#inter_rat_3g_sib_table_'+sufiType+' tbody').append(a);
    	}
    }
    
    var inter_val = params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ;
    if(inter_val.length >= 1)
    {
    	for(var i in params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ)
    	{
    		//var a ='<td data-param="PCI1" class="inter_opr_td">'+inter_val[i].PCI_List.PCI1+'</td>'
    		//	+'<td data-param="PCI2" class="inter_opr_td">'+inter_val[i].PCI_List.PCI2+'</td>'
    		var a =	'<tr class="inter_input_row"><td data-param="DL_Carrier_Frequency" class="inter_opr_td">'+inter_val[i].DL_Carrier_Frequency+'</td>'
    			+'<td data-param="Priority" class="inter_opr_td">'+inter_val[i].Priority+'</td>'
    			+'<td data-param="CELL_ID" class="inter_opr_td">'+inter_val[i].CELL_ID+'</td>'
    			+'<td data-param="TAC" class="inter_opr_td">'+inter_val[i].TAC+'</td>'
    			+'<td data-param="MCC" class="inter_opr_td">'+inter_val[i].INTER_PLMN_ID.MCC+'</td>'
				+'<td data-param="MNC" class="inter_opr_td">'+inter_val[i].INTER_PLMN_ID.MNC+'</td>'
				+'</tr>';
			$('#inter_sib_table_'+sufiType+' tbody').append(a);
    	}
    }
	
    var intera_val = params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ;
    if(intera_val.length >= 1)
    {
    	for(var i in params.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ)
    	{
    		var a ='<tr class="intera_input_row"><td data-param="PCI" class="intera_opr_td">'+intera_val[i].PCI+'</td>'
    			+'<td data-param="CELL_ID" class="intera_opr_td">'+intera_val[i].CELL_ID+'</td>'
    			+'<td data-param="TAC" class="intera_opr_td">'+intera_val[i].TAC+'</td>'
    			+'<td data-param="MCC" class="intera_opr_td">'+intera_val[i].INTRA_PLMN_ID.MCC+'</td>'
				+'<td data-param="MNC" class="intera_opr_td">'+intera_val[i].INTRA_PLMN_ID.MNC+'</td>'
				+'</tr>';
				$('#intera_sib_table_'+sufiType+' tbody').append(a);
    	}
    }
}

var setSufiConfigForDevice = function(singleDeviceInfo){
	var isFieldBlank = false;
	$("body input[type='number']").each(function(index){
		
		if(($(this).attr("id") !="hold_sys_sub_id") && ($(this).val() == null || $(this).val().trim() == ""))
		{
			alert("Please check if any field left blank");
			isFieldBlank =  true;
			return false;
		}	
	})
	if(isFieldBlank){
		globalConfigStatus=false;
		return false;
	}
	//postApplySufiConfig=setConfigStructure(singleDeviceInfo);
	var postApplySufiConfig=setConfigStructure(singleDeviceInfo);
	setConfig(singleDeviceInfo,postApplySufiConfig);
	setConfigOnDb(singleDeviceInfo,postApplySufiConfig);
}

var setConfig = function(singleDeviceInfo,postApplySufiConfig)
{
	var lockUnlockStatus = getLockUnlockStatus(singleDeviceInfo.ip);
	if(lockUnlockStatus=="locked")
	{
	updateConfig(singleDeviceInfo,postApplySufiConfig,'locked');
	}else{
	setCellLock(true,singleDeviceInfo,postApplySufiConfig);
	}
}

var getLockUnlockStatus = function(ip){
	var lockUnlockStatus='';
		$.ajax({
		url:"../../service/common/getlockunlockstatus",
		data:{ip:ip},
		type:'post',
		dataType:"json",
		async:false,
		success:function(data)
		{
		if(data.result=="successful"){
			lockUnlockStatus=data.message;
		}
		},
		error:function(ERR){
		lockUnlockStatus="Problem in getting lock unlock data";
		}
		});
	return lockUnlockStatus;
}

var setConfigOnDb = function(singleDeviceInfo,postApplySufiConfig){
	var data={ip:singleDeviceInfo.ip,id:singleDeviceInfo.b_id,config:postApplySufiConfig};
		$.ajax({
		url:dirPath+"service/3g/updateConfig",
		async:false,
		type:'POST',
		dataType:'json',
		data:data,
		async:false,
		success:function(data)
		{
		//globalConfigCount++;
		},
		error:function(data){
		alert('error');
		}
	});
}

var sibUpdateOnClick = function(){
sibUpdate();
}

var sibUpdate = function()
{
var sufiType=$('#sibSufiType').val();
var sufiId='';
var deviceCode='';
var deviceIp='';
var deviceId='';
if(sufiType.toLowerCase()=='of'){
sufiId=deviceDetails[0].sytemid;
deviceCode=deviceDetails[0].dcode;
deviceIp=deviceDetails[0].ip;
deviceId=deviceDetails[0].b_id;
}else if(sufiType.toLowerCase()=='ppf'){
sufiId=deviceDetails[1].sytemid;
deviceCode=deviceDetails[1].dcode;
deviceIp=deviceDetails[1].ip;
deviceId=deviceDetails[1].b_id;
}else{
sufiId=deviceDetails[2].sytemid;
deviceCode=deviceDetails[2].dcode;
deviceIp=deviceDetails[2].ip;
deviceId=deviceDetails[2].b_id;
}

	var a = 	" {   \"CMD_CODE\": \"SET_SIB_DYN_UPDATE_EVENT\",\"SIB_INFO\": {" +
	"       \"CELLSELECTIONQUALITYMEASURE\": \""+$("#cell_selection_quality_measure_"+sufiType).val()+"\",\"MAXIMUMREPORTEDCELLSONRACH\":\""+$("#MAXIMUMREPORTEDCELLSONRACH"+sufiType).val()+"\"," +
	"      \"NEIGH_CELL_LIST\": {" +
	"        \"INTRA_FREQ\": "+getSIBIntraData(sufiType)+","+
	"        \"INTER_FREQ\": "+getSIBInterData(sufiType)+"," +
	"        \"INTER_RAT\": "+getSIBInterRatData(sufiType)+""+
	"      }" +
	"    }}";
	
	var data = {};
	data.cmdType="SET_SIB_DYN_UPDATE_EVENT";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	data.data =a;
        
	var callback = function(data)
	{
			//setCellUnLock();
            //location.reload();
			nodeSibUpdate();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');	
}

var nodeSibUpdate = function()
{
var sufiType=$('#sibSufiType').val();
var deviceIp='';
var deviceId='';
if(sufiType.toLowerCase()=='of'){
deviceIp=deviceDetails[0].ip;
deviceId=deviceDetails[0].b_id;
}else if(sufiType.toLowerCase()=='ppf'){
deviceIp=deviceDetails[1].ip;
deviceId=deviceDetails[1].b_id;
}else{
deviceIp=deviceDetails[2].ip;
deviceId=deviceDetails[2].b_id;
}
	var a = 	"{" +
	"       \"CELLSELECTIONQUALITYMEASURE\": \""+$("#cell_selection_quality_measure_"+sufiType).val()+"\",\"MAXIMUMREPORTEDCELLSONRACH\":\""+$("#MAXIMUMREPORTEDCELLSONRACH"+sufiType).val()+"\"," +
	"      \"NEIGH_CELL_LIST\": {" +
	"        \"INTRA_FREQ\": "+getSIBIntraData(sufiType)+","+
	"        \"INTER_FREQ\": "+getSIBInterData(sufiType)+"," +
	"        \"INTER_RAT\": "+getSIBInterRatData(sufiType)+""+
	"      }" +
	"    }";
	
	var data = {};
	data.ip=deviceIp;
	data.id=deviceId;
	data.config =a;
        
	var callback = function(data)
	{
			//setCellUnLock();
            alert('SIB Info Updated Successfully');
	}	
	serverCommands(data,"service/3g/updateConfigSibInfo",callback,'json');	
}

var getSIBIntraDataLTE = function(sufiType)
{
	var data=[];
	$("#intera_sib_table_"+sufiType+" tbody .intera_input_row").each(function(index)
	{
		var a={};
		var plmn={};
		$(this).find(".intera_opr_td").each(function(){
			
			if($(this).data("param") == "MCC" || $(this).data("param") == "MNC")
			{
				plmn[$(this).data("param")] = $(this).text();
			}
			else
			{
			   
				a[($(this).data("param"))] = $(this).text();
				
				/*if($(this).data("param") == "PCI" || $(this).data("param") == "CELL_ID" || $(this).data("param") == "TAC"){
					a[($(this).data("param"))] = $(this).text();
			   }else{
					a[($(this).data("param"))] = $(this).find('input').val();
			   }*/
			}
		});
		a.INTRA_PLMN_ID = plmn;
		data.push(a);
	});
	
	if(data.length==0)
	{
		data=defaultSufiConfig.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTRA_FREQ;
	}
	
	console.log(JSON.stringify(data));
	return JSON.stringify(data);
}

var getSIBInterDataLTE = function(sufiType)
{
	var data=[];
	$("#inter_sib_table_"+sufiType+" tbody .inter_input_row").each(function(index){
		var a={};
		var plmn={};
		a.PCI_List=	{"PCI1":"1"};
		$(this).find(".inter_opr_td").each(function(){
			//a[($(this).data("param"))] = $(this).find('input').val();
			if($(this).data("param") == "MCC" || $(this).data("param") == "MNC")
			{
				plmn[$(this).data("param")] = $(this).text();
			}
			else
			{
				a[($(this).data("param"))] = $(this).text();
			/*if($(this).data("param") == "DL_UARFCN" || $(this).data("param") == "CELL_ID" || $(this).data("param") == "LAC"){
				a[($(this).data("param"))] = $(this).text();
			}else{
				a[($(this).data("param"))] = $(this).find('input').val();
			}*/
			}
		});
		a.INTER_PLMN_ID = plmn;
		
		data.push(a);
	});
	if(data.length==0){
	data=defaultSufiConfig.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ;
	}
	console.log(JSON.stringify(data));
	return JSON.stringify(data);	
}

var getSIBInterRatData2g = function(sufiType)
{
	var data=[];
	$("#inter_rat_2g_sib_table_"+sufiType+" tbody .inter_rat_input_row").each(function(index)
	{
		var a={};
		var plmn={};
		$(this).find(".inter_rat_opr_td").each(function(){
		if($(this).data("param") == "MCC" || $(this).data("param") == "MNC")
		{
				plmn[$(this).data("param")] = $(this).text();
		}
		else
		{
		if($(this).data("param") == "LAC" || $(this).data("param") == "CELL_ID"){
			a[($(this).data("param"))] = $(this).text();
		}
		else
		{	a[($(this).data("param"))] = $(this).find('input').val();
			if($(this).data("param")=="Priority")
			{
				a.Explicit_List_ARFCN={"ARFCN1":"881"}
			}
		}
		}
		});
		a.INTER_RAT_PLMN_ID = plmn;
		
		data.push(a);
	});
	if(data.length==0){
	data=defaultSufiConfig.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_2G;
	}
	console.log(JSON.stringify(data));
	return JSON.stringify(data);	
}


var getSIBInterRatData3g = function(sufiType)
{
	var data=[];
	$("#inter_rat_3g_sib_table_"+sufiType+" tbody .inter_rat_input_row").each(function(index)
	{
		var a={};
		var plmn={};
		$(this).find(".inter_rat_opr_td").each(function(){
		if($(this).data("param") == "MCC" || $(this).data("param") == "MNC")
		{
				plmn[$(this).data("param")] = $(this).text();
		}
		else
		{
		if($(this).data("param") == "LAC" || $(this).data("param") == "CELL_ID"){
			a[($(this).data("param"))] = $(this).text();
		}else{
			a[($(this).data("param"))] = $(this).find('input').val();
		}
		}
		});
		a.INTER_RAT_PLMN_ID = plmn;
		data.push(a);
	});
	if(data.length==0){
	data=defaultSufiConfig.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_RAT_3G;
	}
	console.log(JSON.stringify(data));
	return JSON.stringify(data);	
}

var cellAndSibUpdateLTE = function(){
	$('#mccOF').text(returnDataCellScan.MCC);
	$('#mccPPF').text(returnDataCellScan.MCC);
	$('#mccSPF').text(returnDataCellScan.MCC);
	$('#mncOF').text(returnDataCellScan.MNC);
	$('#mncPPF').text(returnDataCellScan.MNC);
	$('#mncSPF').text(returnDataCellScan.MNC);
	$('#dlUarfcnOF').text(returnDataCellScan.EARFCN);
	$('#pscOF').text(returnDataCellScan.PCI);
	$('#cellIdOF').text(returnDataCellScan.CELL);
	$('#bandOF').val(returnDataCellScan.BAND);
	$('#bandPPF').val(returnDataCellScan.BAND);
	$('#bandSPF').val(returnDataCellScan.BAND);
	setSibInfoOfLTE();
	setSibInfoPPfLTE();
	setSibInfoSPfLTE();
	$("#myPopup").modal("hide");
}

var setSibInfoOfLTE = function()
{
	//$('#intera_sib_table_OF tbody').html('');
	$('#intera_sib_table_OF tbody').find("tr:eq(0)").remove();
		//$('#intera_sib_table_OF tbody').append(row);
	
	var a ='<tr class="intera_input_row"><td data-param="PCI" class="intera_opr_td">'+returnDataCellScan.PCI+'</td>'
	+'<td data-param="CELL_ID" class="intera_opr_td">'+returnDataCellScan.CELL+'</td>'
	+'<td data-param="TAC" class="intera_opr_td">'+returnDataCellScan.TAC+'</td>'
	+'<td data-param="MCC" class="intera_opr_td">'+returnDataCellScan.MCC+'</td>'
	+'<td data-param="MNC" class="intera_opr_td">'+returnDataCellScan.MNC+'</td>'
	+'</tr>';
	$('#intera_sib_table_OF tbody').prepend(a);
}

var setSibInfoPPfLTE = function(){
//$('#intera_sib_table_PPF tbody').html('');
$('#intera_sib_table_PPF tbody').find("tr:eq(0)").remove();

var a ='<tr class="intera_input_row"><td data-param="PCI" class="intera_opr_td">'+$("#pscPPF").val()+'</td>'
+'<td data-param="CELL_ID" class="intera_opr_td">'+$("#cellIdPPF").val()+'</td>'
+'<td data-param="TAC" class="intera_opr_td">'+returnDataCellScan.TAC+'</td>'
+'<td data-param="MCC" class="intera_opr_td">'+$("#mccPPF").text()+'</td>'
+'<td data-param="MNC" class="intera_opr_td">'+$("#mncPPF").text()+'</td>'
+'</tr>';

$('#intera_sib_table_PPF tbody').prepend(a);
}

var setSibInfoSPfLTE = function(){
//$('#intera_sib_table_SPF tbody').html('');
$('#intera_sib_table_SPF tbody').find("tr:eq(0)").remove();

var a ='<tr class="intera_input_row"><td data-param="PCI" class="intera_opr_td">'+$("#pscSPF").val()+'</td>'
+'<td data-param="CELL_ID" class="intera_opr_td">'+$("#cellIdSPF").val()+'</td>'
+'<td data-param="TAC" class="intera_opr_td">'+returnDataCellScan.TAC+'</td>'
+'<td data-param="MCC" class="intera_opr_td">'+$("#mccSPF").text()+'</td>'
+'<td data-param="MNC" class="intera_opr_td">'+$("#mncSPF").text()+'</td>'
+'</tr>';
$('#intera_sib_table_SPF tbody').prepend(a);
}


var chekcConfigIsTddOrFdd = function(band)
{
	var type="FDD";
	$.ajax({
		url:dirPath+"service/4g/gettype",
		async:false,
		data:{"band":band},
		type:'POST',
		dataType:"json",
		success:function(data)
		{
			type=data.type;
		}	
	});
	return type;
}

var calulateULearfcn = function(DL_EARFCN,band)
{
	var type = chekcConfigIsTddOrFdd(band);
	if(type == "FDD")
	{
		return (parseInt(DL_EARFCN) + 18000)
	}
	else
	{
		return DL_EARFCN;
	}
} 