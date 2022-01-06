var cellScanServerData = [];
var createReportGridData = function(data){
	
	var returnReportGridData = [];
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
	    	gridData["PCI"] = data[i].pci;
	    	gridData["TAC"] = data[i].tac;
	    	gridData["UARFCN"] = data[i].uarfcn;
	    	gridData["EARFCN"] = data[i].earfcn;
	    	gridData["ARFCN"] = data[i].arfcn;
	    	gridData["LAC"] = data[i].lac;
	    	gridData["COUNTRY"] = data[i].country;
	    	gridData["OPR"] = data[i].operators;
	    	
	    	returnReportGridData.push(gridData);
	}
	return returnReportGridData;
}




var registerCellscanEvent = function() {
}


var getUmtsData= function(){
        $.ajax({
            url: dirPath + "service/netscan/getumtsdata",
            //url:"../resources/test.json",
            data: {
				"techName":"UMTS",
            },
            type: "post",
            success: function(cdrData) {
                //colmodal = cellScanColumn;
                cellScanServerData = createReportGridData(cdrData);
                /*var data=[];
				data.push({MCC:"40",MNC:"42",PSC:"44",UARFCN:"46"});
				data.push({MCC:"50",MNC:"52",PSC:"54",UARFCN:"56"});
				data.push({MCC:"60",MNC:"62",PSC:"64",UARFCN:"66"});
				data.push({MCC:"70",MNC:"72",PSC:"74",UARFCN:"76"});
				cellScanServerData=data;*/
				initializeReportGrid();
            }
        });
}


var getLTEData= function(){
    $.ajax({
        url: dirPath + "service/netscan/getltedata",
        //url:"../resources/test.json",
        data: {
			"techName":"LTE",
        },
        type: "post",
        success: function(cdrData) {
            //colmodal = cellScanColumn;
            cellScanServerData = createReportGridData(cdrData);
            /*var data=[];
			data.push({MCC:"40",MNC:"42",PSC:"44",UARFCN:"46"});
			data.push({MCC:"50",MNC:"52",PSC:"54",UARFCN:"56"});
			data.push({MCC:"60",MNC:"62",PSC:"64",UARFCN:"66"});
			data.push({MCC:"70",MNC:"72",PSC:"74",UARFCN:"76"});
			cellScanServerData=data;*/
			initializeReportGrid();
        }
    });
}

/*var getbtsinfo = function()
{
$('#netscanIp').html('');	
var option='';
	$.ajax({
		url:dirPath+"service/netscan/scanerInfo",
		type:'post',
		success:function(data)
		{
		for(var i=0;i<data.length;i++){
		option+='<option>'+data[i].ip+'</option>';
		}
		$('#netscanIp').append(option);
		}
	});
}*/

$(document).ready(function() {
});




var getDataExcelExport = function() {
    if (cellScanServerData.length <= 0) {
        alert("Please generate report first!!");
        return false;
    }
    var reportNameString = "Alarm Data";

    var colArray = [];
    var exportColumnName = getColumnsData();
    //for( var i=0; i<exportColumnName.length ; i++)
    for (var i in exportColumnName) {
        colData = {};

        colData.headertext = i;
        colData.datatype = "string";
        colData.datafield = exportColumnName[i];
        //colData.width= "100px" ;
        colData.ishidden = "false";
        colArray.push(colData);
    }
    //console.log(alarmOrCdrData);

    var headerText = '<br>' + reportNameString + '<br><br>Generated Time: ' + new Date() + '<br><br>';
    var header = headerText.bold();
    $("#btn_dataExport").battatech_excelexport({
        containerid: "btn_dataExport",
        datatype: 'json',
        dataset: cellScanServerData,
        columns: colArray,
        reportName: header
    });
}

function getColumnsData() {
    var collnames = {};


    collnames['ID'] = 'aid';
    collnames['CMD'] = 'cmd_code';
    collnames['origin'] = 'origin';
    collnames['sufi_id'] = 'sufi_id';
    collnames['alarm_type'] = 'alarm_type';
    collnames['cause'] = 'cause';
    collnames['T-Stmp'] = 'tstmp';
    return collnames;
}

var initializeReportGrid = function()
{    
 //$("#data_tab").jqGrid('GridUnload');
 $.jgrid.gridUnload('data_tab')
 $("#data_tab").jqGrid({
    // url: '../resources/test.json',
     //mtype: "GET",
     datatype: "jsonstring",
     datastr: cellScanServerData,
    // loadonce:true,
     colModel:getCellScanColumnList(),
	 viewrecords: true,
     width: 1160,    
     height: 350,
     rowNum: 10000000,
     onSelectRow: function (row_id) {
         $("#grd").toggleSubGridRow(row_id);
         createDataforOf('jqgridrow','data_tab',row_id);
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
         var index = ret.INDEX;  //get the data from selected row by column name
         var id = ret.ID;
         var techid = ret.TECHID;
         
         if(techid != "9999")
         {
             
             //<table class="tree" id=""><tbody></tbody></table>
       
          celldata={"index":index,"id":id,"tech_id":techid};
          $.ajax({
           url:dirPath+"service/netscan/neighbours",
           async:false,
           type:'POST',
           dataType:'json',
           data:celldata,
           async:false,
           success:function(data)
           {
            /*$("#"+subgrid_id).html('<pre id="json-renderer"></pre>');
            var options = {
                   collapsed: $('#collapsed').is(':checked'),
                   withQuotes: $('#with-quotes').is(':checked')
                 };
                 $('#json-renderer').jsonViewer(JSON.parse(data[0].neigh), options);*/
            
            
            
            
            console.log(data);
            
            var aa = jQuery.parseJSON(data[0].neigh);
            
            $("#" + subgrid_id).append(createArfcAndPscSelectList(techid,aa,row_id));
            
           }
          });
         }
         console.log(index +' : ' + id +' : '+' : '+techid);

      
     },
     pager: "#pager"
 });
 jQuery("#data_tab").jqGrid('navGrid','#pager',{edit:false,add:false,del:false,refresh:false});
}

function getCellScanColumnList(){
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
		{ label: 'TAC', 		name: 'TAC', 		width: 150,search: true },
		{ label: 'CELL_ID', 	name: 'CELL_ID', 	width: 100,search: true },
		{ label: 'LAT', 		name: 'LAT', 		width: 100,search: true },
		{ label: 'LONG', 		name: 'LONG', 		width: 100,search: false },   		
		{ label: 'PSC', 		name: 'PSC', 		width: 150,search: true },
		{ label: 'PCI', 		name: 'PCI', 		width: 150,search: true },
		//{ label: 'RSCP', 		name: 'RSCP', 		width: 150,search: true },
		//{ label: 'RSSI', 		name: 'RSSI', 		width: 150,search: true },	
		//{ label: 'TIMESTAMP', 	name: 'TIMESTAMP', 	width: 150,search: true },
		{ label: 'UARFCN', 		name: 'UARFCN', 	width: 150,search: true },
		{ label: 'EARFCN', 		name: 'EARFCN', 	width: 150,search: true },
		{ label: 'ARFCN', 		name: 'ARFCN', 		width: 150,search: true },
		{ label: 'COUNTRY', 	name: 'COUNTRY', 	width: 150,search: true },
		{ label: 'OPR', 		name: 'OPR', 		width: 150,search: true }
		//{ label: 'SNR', 		name: 'SNR', 		width: 150,search: true },
		//{ label: 'TA', 			name: 'TA', 		width: 150,search: true },
		//{ label: 'PCI', 		name: 'PCI', 		width: 150,search: true },
		//{ label: 'RSRP', 		name: 'RSRP', 		width: 150,search: true },
		//{ label: 'RSRQ', 		name: 'RSRQ', 		width: 150,search: true }
	];
	return cellScanColumn;
}

var createArfcAndPscSelectList = function(techid,data,row_id)
{
 
 var html = "";
 if(techid == 1)
 {
  console.log(data);
  
  html +="<div><label>INTER_RAT_WCDMA_NEIGH</label><br>";
  for(var i in data.INTER_RAT_WCDMA_NEIGH)
  {
	  html +="[<input type='checkbox' onclick=\"createDataforOf('checkbox','data_tab',"+row_id+",this,'"+data.INTER_RAT_WCDMA_NEIGH[i].PSC+"','"+data.INTER_RAT_WCDMA_NEIGH[i].UARFCN+"')\" class='ar_psc' /> PSC:"+data.INTER_RAT_WCDMA_NEIGH[i].PSC+" , UARFCN : "+data.INTER_RAT_WCDMA_NEIGH[i].UARFCN+"],";
  }
  html +="</div>";
  
  /*html +="<div><label>INTRA_FREQ_NEIGH</label><br>";
  for(var i in data.INTRA_FREQ_NEIGH)
  {
   html +="[<input type='checkbox' class='ar_psc' /> ARFCN:"+data.INTRA_FREQ_NEIGH[i].ARFCN+"],";
  }
  html +="</div>";*/
  
  
  
 }
 else if(techid == 2)
 {
  html +="<div><label>INTER_FREQ_NEIGH</label><br>";
  for(var i in data.INTER_FREQ_NEIGH)
  {
	  html +="[<input type='checkbox' onclick=\"createDataforOf('checkbox','data_tab',"+row_id+",this,'"+data.INTER_FREQ_NEIGH[i].PSC+"','"+data.INTER_FREQ_NEIGH[i].UARFCN+"')\" class='ar_psc' /> PSC:"+data.INTER_FREQ_NEIGH[i].PSC+" , UARFCN : "+data.INTER_FREQ_NEIGH[i].UARFCN+"],";
  }
  html +="</div>";
  
  html +="<div><label>INTRA_FREQ_NEIGH</label><br>";
  for(var i in data.INTRA_FREQ_NEIGH)
  {
	  html +="[<input type='checkbox' onclick=\"createDataforOf('checkbox','data_tab',"+row_id+",this,'"+data.INTRA_FREQ_NEIGH[i].PSC+"',null)\" class='ar_psc' /> PSC:"+data.INTRA_FREQ_NEIGH[i].PSC+"],";
  }
  html +="</div>";
 }
 else if(techid == 3)
 {
	 html +="<div><label>INTER_FREQ_NEIGH</label><br>";
	 for(var i in data.INTER_FREQ_NEIGH)
	 {
		 html +="[<input type='checkbox' onclick=\"createDataforOf('checkbox','data_tab',"+row_id+",this,'"+data.INTER_FREQ_NEIGH[i].PCI+"','"+data.INTER_FREQ_NEIGH[i].EARFCN+"')\" class='ar_psc' /> PCI:"+data.INTER_FREQ_NEIGH[i].PCI+" , EARFCN : "+data.INTER_FREQ_NEIGH[i].EARFCN+"],";
	 }
	 html +="</div>";
	 html +="<div><label>INTRA_FREQ_NEIGH</label><br>";
	 for(var i in data.INTRA_FREQ_NEIGH)
	 {
		 html +="[<input type='checkbox' onclick=\"createDataforOf('checkbox','data_tab',"+row_id+",this,'"+data.INTRA_FREQ_NEIGH[i].PCI+"',null)\" class='ar_psc' /> PCI:"+data.INTRA_FREQ_NEIGH[i].PCI+"],";
	 }
	 html +="</div>";
 }
 
 return html;
}
/*var returnDataCellScan = {"MCC":defaultSufiConfig.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC,"MNC":defaultSufiConfig.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC,"LAC":defaultSufiConfig.CELL_PARAMS.SIB_INFO.NEIGH_CELL_LIST.INTER_FREQ[0].LAC,"CELL":defaultSufiConfig.SYS_PARAMS.CELL_INFO.CELL_ID,"PSC":defaultSufiConfig.SYS_PARAMS.CELL_INFO.DL_UARFCN,"UARFCN":defaultSufiConfig.SYS_PARAMS.CELL_INFO.PLMN_ID.MCC};
*/

var createDataforOf =  function(actionPoint,jqgridId,rowid,element,psc,uarfcn)
{
 
 var ret = $("#"+jqgridId).jqGrid('getRowData',rowid);
 
 
 if(actionPoint=='checkbox')
 {
  if($(element).is(":checked"))
  {
   
   $(".ar_psc").each(function(){
    $(this).prop('checked', false);
   });
   
   $(element).prop('checked', true);
   
   //$("#"+jqgridId).setSelection(rowid, true);
   returnDataCellScan = {"MCC":ret.MCC,"MNC":ret.MNC,"LAC":ret.LAC,"CELL":ret.CELL_ID,"PSC":psc,"UARFCN":uarfcn,"EARFCN":uarfcn,"PCI":psc,"TAC":ret.TAC,"BAND":ret.BAND};
      if(psc == null)
      {
       returnDataCellScan.PSC = ret.PSC;
       returnDataCellScan.PCI = ret.PCI;
      }
      if(uarfcn == null)
      {
    	  returnDataCellScan.EARFCN = ret.EARFCN;
    	  returnDataCellScan.UARFCN = ret.UARFCN
    	  
      }
  }
     console.log(returnDataCellScan);
 }else{
  $(".ar_psc").each(function(){
   $(this).prop('checked', false);
  });
  returnDataCellScan = {"MCC":ret.MCC,"MNC":ret.MNC,"LAC":ret.LAC,"CELL":ret.CELL_ID,"PSC":ret.PSC,"UARFCN":ret.UARFCN,"EARFCN":ret.EARFCN,"PCI":ret.PCI,"TAC":ret.TAC,"BAND":ret.BAND};
 }
}

var cellAndSibUpdate = function(){
			$('#mccOF').text(returnDataCellScan.MCC);
			$('#mccPPF').text(returnDataCellScan.MCC);
			$('#mccSPF').text(returnDataCellScan.MCC);
			$('#mncOF').text(returnDataCellScan.MNC);
			$('#mncPPF').text(returnDataCellScan.MNC);
			$('#mncSPF').text(returnDataCellScan.MNC);
			$('#dlUarfcnOF').text(returnDataCellScan.UARFCN);
			$('#pscOF').text(returnDataCellScan.PSC);
			$('#cellIdOF').text(returnDataCellScan.CELL);
			setSibInfoOf();
			setSibInfoPPf();
			setSibInfoSPf();
			$("#myPopup").modal("hide");
}

var setSibInfoOf = function(){
			//$('#intera_sib_table_OF tbody').html('');
			$('#intera_sib_table_OF tbody').find("tr:eq(0)").remove();
    		var row ='<tr class="intera_input_row">'
			    +'<td data-param="PSC" class="intera_opr_td">'+returnDataCellScan.PSC+'</td>'
    			+'<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
    			+'<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-18"></td>'
    			+'<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-24"></td>'
    			+'<td data-param="CELL_ID" class="intera_opr_td">'+returnDataCellScan.CELL+'</td>'
    			+'<td data-param="LAC" class="intera_opr_td">'+returnDataCellScan.LAC+'</td>'
    			+'<td data-param="MCC" class="intera_opr_td">'+returnDataCellScan.MCC+'</td>'
				+'<td data-param="MNC" class="intera_opr_td">'+returnDataCellScan.MNC+'</td>'
				+'</tr>';
				//$('#intera_sib_table_OF tbody').append(row);
    		$('#intera_sib_table_OF tbody').prepend(row);
}

var setSibInfoPPf = function(){
	//$('#intera_sib_table_PPF tbody').html('');
	$('#intera_sib_table_PPF tbody').find("tr:eq(0)").remove();
	var row ='<tr class="intera_input_row">'
	    +'<td data-param="PSC" class="intera_opr_td">'+$("#pscPPF").val()+'</td>'
		+'<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
		+'<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
		+'<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-18"></td>'
		+'<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-24"></td>'
		+'<td data-param="CELL_ID" class="intera_opr_td">'+$("#cellIdPPF").val()+'</td>'
		+'<td data-param="LAC" class="intera_opr_td">'+returnDataCellScan.LAC+'</td>'
		+'<td data-param="MCC" class="intera_opr_td">'+$("#mccPPF").text()+'</td>'
		+'<td data-param="MNC" class="intera_opr_td">'+$("#mncPPF").text()+'</td>'
		+'</tr>';
		$('#intera_sib_table_PPF tbody').prepend(row);
}

var setSibInfoSPf = function(){
	//$('#intera_sib_table_SPF tbody').html('');
	$('#intera_sib_table_SPF tbody').find("tr:eq(0)").remove();
	var row ='<tr class="intera_input_row">'
	    +'<td data-param="PSC" class="intera_opr_td">'+$("#pscSPF").val()+'</td>'
		+'<td data-param="Q_OFFSET_1S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
		+'<td data-param="Q_OFFSET_2S" class="intera_opr_td"><input class="inter_text" type="text" value="1"></td>'
		+'<td data-param="Q_QUALMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-18"></td>'
		+'<td data-param="Q_RXLEVMIN" class="intera_opr_td"><input class="inter_text" type="text" value="-24"></td>'
		+'<td data-param="CELL_ID" class="intera_opr_td">'+$("#cellIdSPF").val()+'</td>'
		+'<td data-param="LAC" class="intera_opr_td">'+returnDataCellScan.LAC+'</td>'
		+'<td data-param="MCC" class="intera_opr_td">'+$("#mccSPF").text()+'</td>'
		+'<td data-param="MNC" class="intera_opr_td">'+$("#mncSPF").text()+'</td>'
		+'</tr>';
	$('#intera_sib_table_SPF tbody').append(row);
}

