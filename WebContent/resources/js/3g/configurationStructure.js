var dirPath ="../";
/*var setConfigStructure = function(){
 var configStruc = '{\n\
                    "PARAMS":\n\
                    {\n\
                        "CMD_CODE":"SET_SUFI_CONFIG",\n\
                        "BITMASK":"'+$("#para_btsmask").val()+'",\n\
                        "SYS_PARAMS":\n\
                            {\n\
                                "BITMASK":"'+$("#sys_para_btsmask").val()+'",\n\
                                "SUB_INFO":{\n\
                                "SUB_LIST_MODE":"'+$("#sys_sub_type").val()+'",\n\
                                "SUB_LIST":\n\
                                ['+createSubscriderList()+'],\n\
                                 "HOLD_SUB":{"SUB_ID":"'+$("input[name=\"hold_sys_sub_id\"]").val()+'","SUB_ID_TYPE":"'+$("select[name=\"hold_sys_sub_type\"]").val()+'"}},\n\
                        "CELL_INFO":\n\
                                {"SUFI_ID":"'+$("#sufi_id").val()+'","MACRO_PSC":"'+$("#macro_id").val()+'","SUFI_OP_MODE":"'+$("#sufi_op_mode").val()+'",\n\
                                "LAC":"'+$("#lac").val()+'","LAC_POOL_START":"'+$("#lac_pool_start").val()+'","LAC_POOL_END":"'+$("#lac_pool_end").val()+'",\n\
                                "RAC_POOL_START":"'+$("#rac_pool_start").val()+'","RAC_POOL_END":"'+$("#rac_pool_end").val()+'","CELL_ID":"'+$("#cell_id").val()+'",\n\
                                "TOTAL_TX_POWER":"'+$("#total_tx_power").val()+'","PCPICH_POWER_PERC":"'+$("#pcpich_power_perc").val()+'",\n\
                                "DL_UARFCN":"'+$("#dl_uarfcn").val()+'","UL_UARFCN":"'+$("#ul_uarfcn").val()+'","2G_TRACKER_FREQ":"'+$("#2g_track_req").val()+'",\n\
                                "PRIVATE_FREQ":[{"FREQ":"'+$("#p_freq_1").val()+'"},{"FREQ":"'+$("#p_freq_2").val()+'"}],\n\
                                "PLMN_ID":{"MCC":"'+$("#cell_mcc").val()+'","MNC":"'+$("#cell_mnc").val()+'"}}},"CELL_PARAMS":\n\
                                {"BITMASK":"'+$("#cell_para_btsmask").val()+'","DCH":{"DPCCH_POWER_OFF":"'+$("#power_off").val()+'"},\n\
"RRC_SETUP":{"DPCH_FRAME_OFF":"'+$("#frame_off").val()+'"},"REDIRECTION_INFO":{"SUFI_OF_REDIR_INFO":{"REL_REDIRECT_FLAG":"'+$("#of_redirect_flag").val()+'",\n\
"RDL_UARFCN":"'+$("#of_rdl_uarfcn").val()+'"},"SUFI_PPF_REDIR_INFO":{"REL_REDIRECT_FLAG":"'+$("#ppf_redirect_flag").val()+'",\n\
"RDL_UARFCN":"'+$("#ppf_rdl_uarfcn").val()+'"},"SUFI_SPF_REDIR_INFO":{"REL_REDIRECT_FLAG":"'+$("#sf_redirect_flag").val()+'",\n\
"RDL_UARFCN":"'+$("#sf_rdl_uarfcn").val()+'"},"2G_REDIR_INFO":{"REL_REDIRECT_FLAG":"'+$("#2g_redirect_flag").val()+'",\n\
"FREQ_BAND":"'+$("#2g_freq_band").val()+'","BCCH_ARFCN":"'+$("#2g_bcch_arfcn").val()+'"}},\n\
"SIB_INFO":{"CELLSELECTIONQUALITYMEASURE":"'+$("#cell_selection_quality_measure").val()+'","NEIGH_CELL_LIST":{"SUFI_OF_LIST":{\n\
"INTRA_FREQ":[{"PSC":"'+$("#intra_ppf_psc").val()+'","Q_OFFSET_1S":"'+$("#intra_ppf_1s").val()+'","Q_OFFSET_2S":"'+$("#intra_ppf_2s").val()+'","Q_QUALMIN":"'+$("#intra_ppf_qualmin").val()+'","Q_RXLEVMIN":"'+$("#intra_ppf_rxlevmin").val()+'","PLMN_ID":{"MCC":"'+$("#intra_ppf_mcc").val()+'","MNC":"'+$("#intra_ppf_mnc").val()+'"},"CELL_ID":"'+$("#intra_ppf_cell").val()+'","LAC":"'+$("#intra_ppf_lac").val()+'"}],\n\
"INTER_FREQ":[{"PSC":"'+$("#of_ppf_psc").val()+'","UARFCN":"'+$("#of_ppf_uarfcn").val()+'","PCPICH_TX_POWER":"'+$("#of_ppf_pcpich").val()+'","Q_OFFSET_1S":"'+$("#of_ppf_1s").val()+'","Q_OFFSET_2S":"'+$("#of_ppf_2s").val()+'","Q_QUALMIN":"'+$("#of_ppf_qualmin").val()+'","Q_RXLEVMIN":"'+$("#of_ppf_rxlevmin").val()+'","PLMN_ID":{"MCC":"'+$("#of_ppf_mcc").val()+'","MNC":"'+$("#of_ppf_mnc").val()+'"},"CELL_ID":"'+$("#of_ppf_cell").val()+'","LAC":"'+$("#of_ppf_lac").val()+'"},\n\
{"PSC":"'+$("#of_spf_psc").val()+'","UARFCN":"'+$("#of_spf_uarfcn").val()+'","PCPICH_TX_POWER":"'+$("#of_spf_pcpich").val()+'","Q_OFFSET_1S":"'+$("#of_spf_1s").val()+'","Q_OFFSET_2S":"'+$("#of_spf_2s").val()+'","Q_QUALMIN":"'+$("#of_spf_qualmin").val()+'","Q_RXLEVMIN":"'+$("#of_spf_rxlevmin").val()+'","PLMN_ID":{"MCC":"'+$("#of_spf_mcc").val()+'","MNC":"'+$("#of_spf_mnc").val()+'"},"CELL_ID":"'+$("#of_spf_cell").val()+'","LAC":"'+$("#of_spf_lac").val()+'"}],\n\
"INTER_RAT":[{"RAT_Q_OFFSET1S_N":"'+$("#inter_rat_offset1s").val()+'","Q_RXLEVMIN":"'+$("#inter_rat_rxlevmin").val()+'","CELLINDIVIDUALOFFSET":"'+$("#inter_rat_celloffset").val()+'","NCC":"'+$("#inter_rat_ncc").val()+'","BCC":"'+$("#inter_rat_bcc").val()+'","FREQ_BAND":"'+$("#inter_rat_band").val()+'","BCCH_ARFCN":"'+$("#inter_rat_bcch").val()+'","CELL_ID":"'+$("#inter_rat_cellid").val()+'","LAC":"'+$("#inter_rat_lac").val()+'"}]},\n\
"SUFI_PPF_LIST":\n\
{"INTER_FREQ":[\n\
{"PSC":"'+$("#ppf_spf_psc").val()+'","UARFCN":"'+$("#ppf_spf_uarfcn").val()+'","PCPICH_TX_POWER":"'+$("#ppf_spf_pcpich").val()+'","Q_OFFSET_1S":"'+$("#ppf_spf_1s").val()+'","Q_OFFSET_2S":"'+$("#ppf_spf_2s").val()+'","Q_QUALMIN":"'+$("#ppf_spf_qualmin").val()+'","Q_RXLEVMIN":"'+$("#ppf_spf_rxlevmin").val()+'","PLMN_ID":{"MCC":"'+$("#ppf_spf_mcc").val()+'","MNC":"'+$("#ppf_spf_mnc").val()+'"},"CELL_ID":"'+$("#ppf_spf_cell").val()+'","LAC":"'+$("#ppf_spf_lac").val()+'"},\n\
{"PSC":"'+$("#ppf_of_psc").val()+'","UARFCN":"'+$("#ppf_of_uarfcn").val()+'","PCPICH_TX_POWER":"'+$("#ppf_of_pcpich").val()+'","Q_OFFSET_1S":"'+$("#ppf_of_1s").val()+'","Q_OFFSET_2S":"'+$("#ppf_of_2s").val()+'","Q_QUALMIN":"'+$("#ppf_of_qualmin").val()+'","Q_RXLEVMIN":"'+$("#ppf_of_rxlevmin").val()+'","PLMN_ID":{"MCC":"'+$("#ppf_of_mcc").val()+'","MNC":"'+$("#ppf_of_mnc").val()+'"},"CELL_ID":"'+$("#ppf_of_cell").val()+'","LAC":"'+$("#ppf_of_lac").val()+'"}]},\n\
"SUFI_SPF_LIST":{\n\
"INTER_FREQ":[\n\
{"PSC":"'+$("#spf_ppf_psc").val()+'","UARFCN":"'+$("#spf_ppf_uarfcn").val()+'","PCPICH_TX_POWER":"'+$("#spf_ppf_pcpich").val()+'","Q_OFFSET_1S":"'+$("#spf_ppf_1s").val()+'","Q_OFFSET_2S":"'+$("#spf_ppf_2s").val()+'","Q_QUALMIN":"'+$("#spf_ppf_qualmin").val()+'","Q_RXLEVMIN":"'+$("#spf_ppf_rxlevmin").val()+'","PLMN_ID":{"MCC":"'+$("#spf_ppf_mcc").val()+'","MNC":"'+$("#spf_ppf_mnc").val()+'"},"CELL_ID":"'+$("#spf_ppf_cell").val()+'","LAC":"'+$("#spf_ppf_lac").val()+'"},\n\
{"PSC":"'+$("#spf_of_psc").val()+'","UARFCN":"'+$("#spf_of_uarfcn").val()+'","PCPICH_TX_POWER":"'+$("#spf_of_pcpich").val()+'","Q_OFFSET_1S":"'+$("#spf_of_1s").val()+'","Q_OFFSET_2S":"'+$("#spf_of_2s").val()+'","Q_QUALMIN":"'+$("#spf_of_qualmin").val()+'","Q_RXLEVMIN":"'+$("#spf_of_rxlevmin").val()+'","PLMN_ID":{"MCC":"'+$("#spf_of_mcc").val()+'","MNC":"'+$("#spf_of_mnc").val()+'"},"CELL_ID":"'+$("#spf_of_cell").val()+'","LAC":"'+$("#spf_of_lac").val()+'"}]}}}}}}';
 
 return configStruc;
 
}*/


var  setConfigStructure = function()
{
	 // BuildMyString.com generated code. Please enjoy your string responsibly.

	 // BuildMyString.com generated code. Please enjoy your string responsibly.

	
	var configStruc = "{" +
	"  \"CMD_CODE\": \"SET_SUFI_CONFIG\"," +
	"  \"BITMASK\": \""+$("#para_btsmask").val()+"\"," +
	"  \"SYS_PARAMS\": {" +
	"    \"BITMASK\": \""+$("#sys_para_btsmask").val()+"\"," +
	"    \"SUFI_PARAMS\": {" +
	"      \"LOG_LEVEL\": \""+$("#log_level").val()+"\"," +
	"      \"SUB_HOLD_TIMER\": \""+$("#sub_hold_timer").val()+"\"," +
	"      \"SUB_REDIR_TIMER\": \""+$("#sub_redir_timer").val()+"\"," +
	"      \"SUFI_OP_MODE\": \""+$("#sufi_op_mode").val()+"\"" +
	",\"SUB_TRACK_MODE\": {\"TRACK_MODE\": \""+$("#sufi_track_mode").val()+"\"}"+
	"    },\"SUB_INFO\": {" +
	"      \"SUB_LIST_MODE\": \""+$("#sys_sub_type").val()+"\"," +
	"      \"SUB_LIST\": ["+createSubscriderList()+"]," +
	"      \"HOLD_SUB\": {" +
	"        \"SUB_ID\": \""+$("input[name=\"hold_sys_sub_id\"]").val()+"\"," +
	"        \"SUB_ID_TYPE\": \""+$("select[name=\"hold_sys_sub_type\"]").val()+"\"" +
	"      }" +
	"    }," +
	"    \"CELL_INFO\": {" +
	"      \"SUFI_ID\": \""+$("#sufi_id").val()+"\"," +
	"      \"PRI_SCRAM_CODE\": \""+$("#scram_code").val()+"\"," +
	"      \"LAC_POOL_START\": \""+$("#lac_pool_start").val()+"\"," +
	"      \"LAC_POOL_END\": \""+$("#lac_pool_end").val()+"\"," +
	"      \"CELL_ID\": \""+$("#cell_id").val()+"\"," +
	"      \"TOTAL_TX_POWER\": \""+$("#total_tx_power").val()+"\"," +
	"      \"PCPICH_POWER_PERC\": \""+$("#pcpich_power_perc").val()+"\"," +
	"      \"DL_UARFCN\": \""+$("#dl_uarfcn").val()+"\"," +
	"      \"UL_UARFCN\": \""+$("#ul_uarfcn").val()+"\"," +
	"      \"PLMN_ID\": {" +
	"        \"MCC\": \""+$("#cell_mcc").val()+"\"," +
	"        \"MNC\": \""+$("#cell_mnc").val()+"\"" +
	"      }" +
	"    }" +
	"  }," +
	"  \"CELL_PARAMS\": {" +
	"    \"BITMASK\": \""+$("#cell_para_btsmask").val()+"\"," +
	"    \"DCH\": {" +
	"      \"DPCCH_POWER_OFF\": \""+$("#power_off").val()+"\"" +
	"    }," +
	"    \"RRC_SETUP\": {" +
	"      \"DPCH_FRAME_OFF\": \""+$("#frame_off").val()+"\"" +
	"    }," +
	"    \"REDIRECTION_INFO\": {" +
	"      \"SUFI_OF_REDIR_INFO\": {" +
	"        \"RDL_UARFCN\": \""+$("#of_rdl_uarfcn").val()+"\"," +
			"\"FREQ_BAND\": \""+$("#2g_freq_band").val()+"\"," +
	"        \"BCCH_ARFCN\": \""+$("#2g_bcch_arfcn").val()+"\"" +
	"      }," +
	"      \"SUFI_PPF_REDIR_INFO\": {" +
	"        \"RDL_UARFCN\": \""+$("#ppf_rdl_uarfcn").val()+"\"" +
	"      }," +
	"      \"SUFI_SPF_REDIR_INFO\": {" +
	"        \"RDL_UARFCN\": \""+$("#sf_rdl_uarfcn").val()+"\"" +
	"      }," +	
	"      \"SUFI_OPER_REDIR_INFO\": {" +
	"        \"RDL_UARFCN\": \""+$("#oper_rdl_uarfcn").val()+"\"" +
	"      }" +
	"    }," +
	"    \"SIB_INFO\": {" +
	"      \"CELLSELECTIONQUALITYMEASURE\": \""+$("#cell_selection_quality_measure").val()+"\"," +
	"		\"MAXIMUMREPORTEDCELLSONRACH\":\""+$("#MAXIMUMREPORTEDCELLSONRACH").val()+"\"," +
	"      \"NEIGH_CELL_LIST\": {" +
	"        \"INTRA_FREQ\": "+getSIBIntraData()+","+
	"        \"INTER_FREQ\": "+getSIBInterData()+"," +
	"        \"INTER_RAT\": "+getSIBInterRatData()+""+
	"      }" +
	"    }" +
	"  }" +
	"}";
			
	console.log(configStruc);
						

	 return configStruc;		
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
    $("#sufi_track_mode").val(params.SYS_PARAMS.SUFI_PARAMS.SUB_TRACK_MODE.TRACK_MODE);
    
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

var setConfig = function()
{
	var isFieldBlank = false;
$("body input[type='number']").each(function(index){
		
		if($(this).val() == null || $(this).val().trim() == "")
		{
			alert("Please check if any filed left blank");
			isFieldBlank =  true;
			return false;
		}
		
		
	})
	if(isFieldBlank)
		return false;
	setCellLock(true);
}

var updateConfig = function()
{
	var data = {};
	data.cmdType="SET_SUFI_CONFIG";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	data.data =setConfigStructure();
        
	var callback = function(data)
	{
			setCellUnLock();
            //location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');	
}

var getConfig = function()
{
	var data = {};
	data.cmdType="GET_ACTIVE_CONFIG";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	data.data ='{"CMD_CODE": "GET_ACTIVE_CONFIG"}';
        
	var callback = function(data)
	{
            setConfigForm(data);
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');	
}

//Site Lock Value  
var setCellLock = function(isConfigCall)
{
	
	var data = {};
	data.cmdType="SET_CELL_LOCK";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	data.data ='{"CMD_CODE":"SET_CELL_LOCK","CELL_ID":"'+$("#cell_id").val()+'","LAC":"0"}';
	
	var callBack = function(data)
	{
		if(isConfigCall)
		{
			updateConfig();
		}
		else
		{	
			location.reload();
		}
		
	}	
	serverCommands(data,"service/3g/clientopr",callBack,'json');
}

//Site UnLock Value  
var setCellUnLock = function()
{
	var data = {};
	data.cmdType="SET_CELL_UNLOCK";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+$("#cell_id").val()+'","LAC":"0"}';
	
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');
}

var openTrackMode = function()
{
	
	$( "#track_modal" ).dialog(
				{
					modal:true,width:'auto',close: function( event, ui ) {$("#track_id").val(123)},open: function( event, ui ) {$("#track_id").val("")}
				}
			);
}


var setUpTrackMode = function()
{
	
	var data = {};
	
	data.cmdType="SET_TRACK_UPDATE";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	data.data ='{"CMD_CODE":"SET_TRACK_MODE","TRACK_MODE":"'+$("#track_type_mode").val()+'","SUB_ID":"'+$("#track_id").val()+'","SUB_ID_TYPE":"'+$("#tarck_sub_type").val()+'"}';
	
	var callback = function(data)
	{
		location.reload();
	}
	
	serverCommands(data,"service/3g/comopr",callback,'json');
}

var updateSibInfo = function()
{
	var data = {};
	data.cmdType="SET_TRACK_SUB_LIST";
	data.systemId = sufiId;
	data.systemCode = deviceCode;
	data.systemIp=deviceIp;
	data.id=deviceId
	//data.data ='{"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"'+$("#cell_id").val()+'","LAC":"'+$("#lac").val()+'"}';
	data.data ='{"CMD_CODE": "SET_TRACK_SUB_LIST","SUB_INFO":{"SUB_LIST_MODE":"'+$("#sys_sub_type").val()+'","SUB_LIST":['+createSubscriderList()+'],"HOLD_SUB":{"SUB_ID":"'+$("input[name=\"hold_sys_sub_id\"]").val()+'","SUB_ID_TYPE":"'+$("select[name=\"hold_sys_sub_type\"]").val()+'"}}}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');
}

var updateSubHold = function()
{
	
	var data = getDeviceDetailData("SET_HOLD_SUB");
	data.data ='{"CMD_CODE":"SET_HOLD_SUB","HOLD_SUB":{"SUB_ID":"'+$("input[name=\"hold_sys_sub_id\"]").val()+'","SUB_ID_TYPE":"'+$("select[name=\"hold_sys_sub_type\"]").val()+'"}}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');
}

var mesTrigger = function(val)
{
	
	var data = getDeviceDetailData("SET_MEAS_TRIGGER");
	data.data ='{"CMD_CODE":"SET_MEAS_TRIGGER","TRIGGER":"'+val+'"}';
	var callback = function(data)
	{
		location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');
}


var sibUpdate = function()
{
	var a = 	" {   \"CMD_CODE\": \"SET_SIB_DYN_UPDATE_EVENT\",\"SIB_INFO\": {" +
	"       \"CELLSELECTIONQUALITYMEASURE\": \""+$("#cell_selection_quality_measure").val()+"\",\"MAXIMUMREPORTEDCELLSONRACH\":\""+$("#MAXIMUMREPORTEDCELLSONRACH").val()+"\"," +
	"      \"NEIGH_CELL_LIST\": {" +
	"        \"INTRA_FREQ\": "+getSIBIntraData()+","+
	"        \"INTER_FREQ\": "+getSIBInterData()+"," +
	"        \"INTER_RAT\": "+getSIBInterRatData()+""+
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
            location.reload();
	}	
	serverCommands(data,"service/3g/clientopr",callback,'json');	
}

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





