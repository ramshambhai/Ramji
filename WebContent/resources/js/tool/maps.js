var dirPath='../../';
var dataOfNodeWiseReport=[];
var dataOfCountryWiseReport=[];
var dataOfNetworkScanReport=[];
var globalType="-1";
var globalValue="[-1]";
var globalFilter="-1";
var globalBts="-1";
var globalCountry="-1";
var globalOprname="-1";
$(document).ready(function(){

createNodeWiseReport();
createCountryWiseReport();
createNetworkScanReport();
$('#nodeWiseExcelExportId').click(function(){
nodeWiseExcelExport();
});
$('#countryWiseExcelExportId').click(function(){
countryWiseExcelExport();
});
$('#networkScanExcelExportId').click(function(){
networkScanExcelExport();
});
});
var getType = function(type)
{
	if(type == 'subTotal' || type == 'total' || type==undefined)
	{
		return "-1";
	}
	else
	{
		return type;
	}
}

var getNodeWiseReport = function(nodeName,mainFilter,subFilter){
	 $.ajax({
		url:dirPath+"Operations",
		async:false,
		type:'post',
		data:{methodName:"getNodeWiseReport",nodeName:nodeName,mainFilter:mainFilter,subFilter:subFilter,operationStartTime:operationStartTIme},
		beforeSend:function()
		{
		
			$("#loading_modal").modal('show');
			//$("#updateStatusBtn").attr({"disabled":"disabled"});
			//$("#device_status_table").hide();
		},
		success:function(data)
		{
			dataOfNodeWiseReport=eval(data);
			reloadNodeWiseReport();
			
			globalType=getType(mainFilter);
			globalBts=getType(nodeName);
			globalCountry="-1";
			globalOprname="-1";
		  if(mainFilter=="ta")
		  {
			globalValue = null;
			if(subFilter=="lessthan5"){
			globalValue="[2,5]";
			globalFilter="less";
		  }else if(subFilter=="morethan5"){
			globalValue="[5]";
			globalFilter="more";
			}else{
			globalValue="["+subFilter+"]";
			globalFilter="equal";
			}
			
			
		}else{
			
			globalValue = null;
			if(subFilter=="morethan-55"){
			globalValue="[-55]";
			globalFilter="more";
			}else if(subFilter=="morethan-75"){
			globalValue="[-75,-55]";
			globalFilter="more";
			}else if(subFilter=="morethan-95"){	
			globalValue="[-95,-75]";
			globalFilter="more";
			}else if(subFilter=="lessthan-95"){
			globalValue="[-95]";
			globalFilter="less";
			}else{
			globalValue="["+subFilter+"]";
			globalFilter="equal";	
			}
			
			
		}
		  if(globalValue == null)
			{
				globalValue ="[-1]";
				globalFilter= "-1";
			}
			$("#nodeWiseReportModal").modal('show');
			
			$("#loading_modal").modal('hide');
		
		},
		error:function(txx)
		{
			$("#loading_modal").modal('hide');
		}
	});
}

var getCountryWiseReport = function(countryName,oprName,mainFilter,subFilter){
	 $.ajax({
		url:dirPath+"Operations",
		async:false,
		type:'post',
		data:{methodName:"getCountryWiseReport",countryName:countryName,oprName:oprName,mainFilter:mainFilter,subFilter:subFilter,operationStartTime:operationStartTIme},
		beforeSend:function()
		{
			$("#loading_modal").modal('show');
		},
		success:function(data)
		{
			dataOfCountryWiseReport=eval(data);
			reloadCountryWiseReport();
			globalType=mainFilter;
			globalBts="-1";
			globalCountry=countryName;
			globalOprname=oprName;
			globalValue="[-1]";
			globalFilter="-1";
			
			if(mainFilter=="ta"){
			if(subFilter=="lessthan5"){
			globalValue="[2,5]";
			globalFilter="less";
			}else if(subFilter=="morethan5"){
			globalValue="[5]";
			globalFilter="more";
			}else{
			globalValue="["+subFilter+"]";
			globalFilter="equal";
			}
		}else{
			if(subFilter=="morethan-55"){
			globalValue="[55]";
			globalFilter="more";
			}else if(subFilter=="morethan-75"){
			globalValue="[55,75]";
			globalFilter="more";
			}else if(subFilter=="morethan-95"){	
			globalValue="[75,95]";
			globalFilter="more";
			}else if(subFilter=="lessthan-95"){
			globalValue="[95]";
			globalFilter="less";
			}else{
			globalValue="["+subFilter+"]";
			globalFilter="equal";	
			}
		}
			$("#countryWiseReportModal").modal('show');
			$("#loading_modal").modal('hide');
			
		},
		error:function(txx)
		{
			$("#loading_modal").modal('hide');
		}
	});
}

var getNetworkScanReport = function(oprName,nodeName){
	 $.ajax({
		url:dirPath+"Operations",
		async:false,
		type:'post',
		data:{methodName:"getNetworkScanReport",oprName:oprName,nodeName:nodeName,operationStartTime:operationStartTIme},
		beforeSend:function()
		{
			$("#loading_modal").modal('show');
		},
		success:function(data)
		{
			dataOfNetworkScanReport=eval(data);
			//dataOfNetworkScanReport=getFormattedNetworkScanData(dataOfNetworkScanReport)
			reloadNetworkScanReport(oprName,nodeName);
			$("#networkScanReportModal").modal('show');
			$("#loading_modal").modal('hide');
		},
		error:function(tx)
		{
			$("#loading_modal").modal('hide');
		},
	});
}

/*var getFormattedNetworkScanData = function(dataOfNetworkScanReport){
dataOfNetworkScanReport.rssi=Math.trunc(parseFloat(dataOfNetworkScanReport.rssi)).toString();
dataOfNetworkScanReport.snr=Math.trunc(parseFloat(dataOfNetworkScanReport.snr)).toString();
var sysloc=dataOfNetworkScanReport.snr;
var latlon=sysloc.split(",");
var lat =(Math.round(parseFloat(latLon[0])*1000000.0)/1000000.0).toString();
var lon =(Math.round(parseFloat(latLon[1])*1000000.0)/1000000.0).toString();
dataOfNetworkScanReport.snr=lat+','+lon;
return dataOfNetworkScanReport;
}*/

var createNodeWiseReport = function()
{
		jQuery(document).ready(function() {
	        $("#nodeWiseReportGrid").jqGrid({
	                data : dataOfNodeWiseReport,
	                datatype : "local",
	                colNames : ['S-Type', 'IP','Count', 'IMSI', 'IMEI','TA','RxLev','Sys Loc','UL ARFCN','DL ARFCN','PSC','BAND','O/P Pwr','CGI','Generation Time'],
	                colModel : [ {
	                        name : 'stype',
	                        index : 'stype',
							width: 57
	                },{
	                        name : 'ip',
	                        index : 'ip'
							//,width: 'auto'
	                        ,hidden:true
	                },{
	                        name : 'count',
	                        index : 'count'
							//,width: 'auto'
	                        	,hidden:true
	                },{
							name : 'imsi',
							index : 'imsi'
							,width: 130
					},{
	                        name : 'imei',
	                        index :'imei'
							,width: 130
	                },{
                        name : 'ta',
                        index :'ta',
                        width:55
						//,width: 'auto'
                },{
                        name : 'rxl',
                        index :'rxl'
                        	,width:60
						//,width: 'auto'
                        //editable : true
                },{
	                        name : 'sysloc',
	                        index :'sysloc'
	                        ,width: 166
	                        ,hidden:true
	                },{
	                        name : 'ulrfcn',
	                        index :'ulrfcn'
	                        	,width:78
	                        //,width: 'auto'
	                },{
	                        name : 'dlarfcn',
	                        index :'dlarfcn'
	                        	,width:78
	                        //,width: 'auto'
	                },{
	                        name : 'psc',
	                        index :'psc'
	                        ,width: 60
	                },{
	                        name : 'band',
	                        index :'band'
	                        ,width: 85
	                },{
	                        name : 'outpow',
	                        index :'outpow'
	                        	,width:65
	                        //,width: 'auto'
	                }/*,{
	                        name : 'tstmp',
	                        index :'tstmp'
	                        ,width: 120
	                }*/,{
                        name : 'cgi',
                        index :'cgi'
						//,width: 'auto'
	                },{
	                        name : 'inserttime',
	                        index :'inserttime'
	                        ,width: 136
	                }],
	                pager : '#nodeWiseReportPager',
	                rowNum : 30,
					//width:'auto',
					height: 370,
	                rowList : [10, 20,30],
					loadonce: true,
	                viewrecords : true,
	                gridview : true,
	                caption : 'Node Wise Details',
					gridComplete: initGrid
	        });
	        jQuery("#nodeWiseReportGrid").jqGrid('navGrid', '#nodeWiseReportPager', {
	                edit : false,
	                add : false,
	                del : false,
	                search : true
	        });     
	});
}

var createCountryWiseReport = function()
{
		jQuery(document).ready(function() {
	        $("#countryWiseReportGrid").jqGrid({
	                data : dataOfCountryWiseReport,
	                datatype : "local",
	                colNames : ['S-Type', 'IP','Count', 'IMSI', 'IMEI', 'TA','RxLev','Sys Loc','UL ARFCN','DL ARFCN','PSC','BAND','O/P Pwr','CGI','Generation Time'],
					colModel : [ {	
	                        name : 'stype',
	                        index : 'stype',
							width : 57
	                },{
	                        name : 'ip',
	                        index : 'ip'
	                        	,hidden:true
	                },{
	                        name : 'count',
	                        index : 'count'
	                        	,hidden:true
	                },{
							name : 'imsi',
							index : 'imsi',
							width : 130
					},{
	                        name : 'imei',
	                        index :'imei',
							width : 130
	                },{
	                        name : 'ta',
	                        index :'ta',	
	                        width:55
	                },{
                        name : 'rxl',
                        index :'rxl',
                        width:60
	                },{
	                        name : 'sysloc',
	                        index :'sysloc'
	                        //editable : true
	                        	,hidden:true	
							,width:166
	                },{
	                        name : 'ulrfcn',
	                        index :'ulrfcn',
	                        editable : true
	                        ,width:78
	                },{
	                        name : 'dlarfcn',
	                        index :'dlarfcn',
	                        editable : true
	                        ,width:78
	                },{
                        name : 'psc',
                        index :'psc'
                        ,width:60//editable : true
	                },{
	                        name : 'band',
	                        index :'band'
	                        //editable : true
							,width:85
	                },{
	                        name : 'outpow',
	                        index :'outpow',
	                        editable : true
	                        ,width:65
	                }/*,{
	                        name : 'tstmp',
	                        index :'tstmp',
	                        editable : true,
							width: 120
	                }*/,{
                        name : 'cgi',
                        index :'cgi',
                        editable : true
	                },{
	                        name : 'inserttime',
	                        index :'inserttime'
	                        ,width:136//editable : true
	                }],
	                pager : '#countryWiseReportPager',
	                rowNum : 30,
					height: 370,
	                rowList : [10, 20,30],
					loadonce: true,
	                viewrecords : true,
	                gridview : true,
	                caption : 'Country Wise Details'
	        });
	        jQuery("#countryWiseReportGrid").jqGrid('navGrid', '#countryWiseReportPager', {
	                edit : false,
	                add : false,
	                del : false,
	                search : true
	        });     
	});
}

var createNetworkScanReport = function()
{
		jQuery(document).ready(function() {
	        $("#networkScanReportGrid").jqGrid({
	                data : dataOfNetworkScanReport,
	                datatype : "local",
	                colNames : ['IP','Count','S-Type','DL_FREQ','BAND','ARFCN','DL_UARFCN','DL_EARFCN','PSC','PCI','MCC','MNC','OPERATOR','LAC','CELL ID','NCC','BCC','RSSI','TA','RSCP','ECNO','RSRP','RSRQ','SNR','Sysloc','TimeStamp'],          				
					colModel : [ {
	                        name : 'ip',
	                        index : 'ip',
							hidden : true,
							width : 125
	                },{
	                        name : 'count',
	                        index : 'count',
							hidden : true
	                },{
	                        name : 'packet_type',
	                        index : 'packet_type',
							width : 70
							
	                },{
							name : 'freq',
							index : 'freq',
							width : 70
					},{
	                        name : 'band',
	                        index :'band',
	                        editable : true,
							width : 80
	                }
					,{
                        name : 'arfcn',
                        index :'arfcn',
                        editable : true,
							width : 75
					}
					,{
	                        name : 'uarfcn',
	                        index :'uarfcn',
	                        editable : true,
							width : 85
	                },{
	                        name : 'earfcn',
	                        index :'earfcn',
	                        editable : true,
							width : 85
	                },{
	                        name : 'psc',
	                        index :'psc',
	                        editable : true,
							width : 55
	                },{
	                        name : 'pci',
	                        index :'pci',
	                        editable : true,
							width : 55
	                },{
	                        name : 'mcc',
	                        index :'mcc',
	                        editable : true,
							width : 65
	                },{
	                        name : 'mnc',
	                        index :'mnc',
	                        editable : true,
							width : 65
	                },{
	                        name : 'operators',
	                        index :'operators',
	                        editable : true,
							width : 80
	                },{
	                        name : 'lac',
	                        index :'lac',
	                        editable : true,
							width : 80
	                },{
	                        name : 'cell',
	                        index :'cell',
	                        editable : true,
							width : 80
	                },{
	                        name : 'ncc',
	                        index :'ncc',
	                        editable : true,
							width : 65
	                },{
	                        name : 'bcc',
	                        index :'bcc',
							width:65
	                },{
	                        name : 'rssi',
	                        index :'rssi',
	                        width:65
	                },{
	                        name : 'ta',
	                        index :'ta',
							width:65
	                },{
	                        name : 'rscp',
	                        index :'rscp',
							width:92
	                },{
	                        name : 'ecno',
	                        index :'ecno',
							width:92
	                },{
	                        name : 'rsrp',
	                        index :'rsrp',
							width:92
	                },{
	                        name : 'rsrq',
	                        index :'rsrq',
							width:92
	                },{
	                        name : 'snr',
	                        index :'snr',
							width:65
	                },{
	                        name : 'sysloc',
	                        index :'sysloc',
							hidden : true,
							width:120
	                },{
	                        name : 'inserttime',
	                        index :'inserttime',
	                        width:136//editable : true
	                }/*,{
	                        name : 'tstmp',
	                        index :'tstmp',
	                        width:136//editable : true
	                }*/],
	                
	                pager : '#networkScanReportPager',
	                rowNum : 30,
					height: 370,
	                rowList : [10, 20,30],
					loadonce: true,
	                viewrecords : true,
	                gridview : true,
	                caption : 'Network Scan Details'
	        });
	        jQuery("#networkScanReportGrid").jqGrid('navGrid', '#networkScanReportPager', {
	                edit : false,
	                add : false,
	                del : false,
	                search : true
	        });     
	});
}


var nodeWiseExcelExport = function(){
var colArray=[];
if(dataOfNodeWiseReport.length <=0){alert("Please generate report first!!");return false;}
	var exportColumnName = getNodeWiseColumnNames();
	 for( var i in exportColumnName)
 {
		colData={};
	
		colData.headertext=i;
		colData.datatype="string";
		colData.datafield=exportColumnName[i];
		colData.width= "100px" ;
		colData.ishidden= "false"; 
		colArray.push(colData);	
 }
 
	var headerText ='';
	headerText='<br>Node Wise Data Report';
	headerText+='<br><br>Generated Time: ' + new Date();    
	var header = headerText.bold();		
	$("#nodeWiseExcelExportDiv").battatech_excelexport({
					containerid: "nodeWiseExcelExportDiv"
					, datatype: 'json'
					, dataset: dataOfNodeWiseReport
					, columns: colArray
					, reportName:header 
				});	
}

var countryWiseExcelExport = function(){
var colArray=[];
if(dataOfCountryWiseReport.length <=0){alert("Please generate report first!!");return false;}
	var exportColumnName = getNodeWiseColumnNames();
	 for( var i in exportColumnName)
 {
		colData={};
	
		colData.headertext=i;
		colData.datatype="string";
		colData.datafield=exportColumnName[i];
		colData.width= "100px" ;
		colData.ishidden= "false"; 
		colArray.push(colData);	
 }
 
	var headerText ='';
	headerText='<br>Country Wise Data Report';
	headerText+='<br><br>Generated Time: ' + new Date();    
	var header = headerText.bold();		
	$("#countryWiseExcelExportDiv").battatech_excelexport({
					containerid: "countryWiseExcelExportDiv"
					, datatype: 'json'
					, dataset: dataOfCountryWiseReport
					, columns: colArray
					, reportName:header 
				});	
}

var networkScanExcelExport = function(){
var colArray=[];
if(dataOfNetworkScanReport.length <=0){alert("Please generate report first!!");return false;}
	var exportColumnName = getNetworkScanColumnNames();
	 for( var i in exportColumnName)
 {
		colData={};
	
		colData.headertext=i;
		colData.datatype="string";
		colData.datafield=exportColumnName[i];
		colData.width= "100px" ;
		colData.ishidden= "false"; 
		colArray.push(colData);	
 }
 
	var headerText ='';
	headerText='<br>Network Scan Data Report';
	headerText+='<br><br>Generated Time: ' + new Date();    
	var header = headerText.bold();		
	$("#networkScanExcelExportDiv").battatech_excelexport({
					containerid: "networkScanExcelExportDiv"
					, datatype: 'json'
					, dataset: dataOfNetworkScanReport
					, columns: colArray
					, reportName:header 
				});	
}

var getNodeWiseColumnNames = function(){
var colNames={};
	colNames["S-Type"]="stype";
	colNames["IP"]="ip";
	colNames["Count"]="count";
	colNames["imsi"]="imsi";
	colNames["imei"]="imei";
	colNames["ta"]="ta";
	colNames["rxl"]="rxl";
	colNames["CGI"]="cgi";
	colNames["Sys Loc"]="sysloc";
	colNames["Band"]="band";
	colNames["UlArfcn"]="ulrfcn";
	colNames["DlArfcn"]="dlarfcn";
	colNames["OutPow"]="outpow";
	colNames["tstmp"]="tstmp";
	colNames["psc"]="psc";
	colNames["inserttime"]="inserttime";
	return colNames;
}

var getNetworkScanColumnNames = function(){
var colNames={};
	colNames["IP"]="ip";
	colNames["Count"]="count";
	colNames["Packet Type"]="packet_type";
	colNames["Freq"]="freq";
	colNames["Band"]="band";
	colNames["Arfcn"]="arfcn";
	colNames["MCC"]="mcc";
	colNames["MNC"]="mnc";
	colNames["Lac"]="lac";
	colNames["Cell"]="cell";
	colNames["NCC"]="ncc";
	colNames["BCC"]="bcc";
	colNames["RSSI"]="rssi";
	colNames["SNR"]="snr";
	colNames["TA"]="ta";
	colNames["Sysloc"]="sysloc";
	colNames["Tstmp"]="tstmp";
	colNames["Logtime"]="inserttime";
	return colNames;
}

function initGrid() {
            $(".jqgrow", "#nodeWiseReportGrid").contextMenu('contextMenu', {
                bindings: {
                    'edit': function (t) {
                        editRow();
                    },
                    'add': function (t) {
                        addRow();
                    },
                    'del': function (t) {
                        delRow();
                    }
                },
                onContextMenu: function (event, menu) {
                    var rowId = $(event.target).parent("tr").attr("id")
                    var grid = $("#nodeWiseReportGrid");
                    grid.setSelection(rowId);

                    return true;
                }
            });

            function addRow() {
                var grid = $("#nodeWiseReportGrid");
                grid.editGridRow("new", { closeAfterAdd: true});
            }

            function editRow() {
                var grid = $("#nodeWiseReportGrid");
                var rowKey = grid.getGridParam("selrow");
                if (rowKey) {
                    grid.editGridRow(rowKey, {closeAfterEdit: true});
                }
                else {
                    alert("No rows are selected");
                }
            }

            function delRow() {
                var grid = $("#nodeWiseReportGrid");
                var rowKey = grid.getGridParam("selrow");
                if (rowKey) {
                    grid.delGridRow(rowKey);
                }
                else {
                    alert("No rows are selected");
                }
            }
        }
		
		


var reloadNodeWiseReport = function(){
							$("#nodeWiseReportGrid").jqGrid('clearGridData');
        					$("#nodeWiseReportGrid").jqGrid('setGridParam', {
            			        datatype: 'local',
            			        data: dataOfNodeWiseReport
            			    });
            				$("#nodeWiseReportGrid").trigger("reloadGrid");
}

var reloadCountryWiseReport = function(){
							$("#countryWiseReportGrid").jqGrid('clearGridData');
        					$("#countryWiseReportGrid").jqGrid('setGridParam', {
            			        datatype: 'local',
            			        data: dataOfCountryWiseReport
            			    });
            				$("#countryWiseReportGrid").trigger("reloadGrid");
}

var reloadNetworkScanReport = function(oprName,nodeName){
	//'IP','Count','S-Type','FREQ','BAND','ARFCN','DL_UARFCN','DL_EARFCN','PSC','PCI','MCC','MNC','OPERATOR','LAC','CELL ID','NCC','BCC','RSCP','ECNO','RSRP','RSRQ','RSSI','SNR','TA','Sysloc','TimeStamp'
	$("#networkScanReportGrid").jqGrid('clearGridData');
    $("#networkScanReportGrid").jqGrid('setGridParam', {
        datatype: 'local',
       data: dataOfNetworkScanReport
    });
	$("#networkScanReportGrid").trigger("reloadGrid");
	if(nodeName==null){
		$('#networkScanReportModal .modal-dialog').css('width','1228px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["arfcn","ncc","bcc","snr","uarfcn","earfcn","psc","pci","rscp","ecno","rsrp","rsrq"]);
	}else if(nodeName.toLowerCase().indexOf("gsm")!=-1 || nodeName.toLowerCase().indexOf("loc_2g")!=-1){
	$('#networkScanReportModal .modal-dialog').css('width','1170px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["arfcn","ncc","bcc","snr"]);
		jQuery("#networkScanReportGrid").jqGrid('hideCol',["uarfcn","earfcn","psc","pci","rscp","ecno","rsrp","rsrq"]);
		//jQuery("#networkScanReportGrid").jqGrid('setLabel', 3, 'FREQ');
	}else if(nodeName.toLowerCase().indexOf("umts")!=-1 || nodeName.toLowerCase().indexOf("loc_3g")!=-1){
		$('#networkScanReportModal .modal-dialog').css('width','1255px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["uarfcn","psc","rscp","ecno","snr","snr"]);
		jQuery("#networkScanReportGrid").jqGrid('hideCol',["arfcn","earfcn","pci","ncc","bcc","rsrp","rsrq","snr"]);
		//jQuery("#networkScanReportGrid").jqGrid('setLabel', 3, 'DL_FREQ');
	}else if(nodeName.toLowerCase().indexOf("lte")!=-1){
		$('#networkScanReportModal .modal-dialog').css('width','1255px');
		jQuery("#networkScanReportGrid").jqGrid('showCol',["earfcn","pci","rsrp","rsrq"]);
		jQuery("#networkScanReportGrid").jqGrid('hideCol',["arfcn","uarfcn","psc","ncc","bcc","rscp","ecno","snr"]);
		//jQuery("#networkScanReportGrid").jqGrid('setLabel', 3, 'DL_FREQ');
	}
}

function drawPlotOnNewWindow(){
	if(operationEndTime == -1)
	{
		operationEndTime = toUtcTime(new Date());
	}
	
var url="maps_popup.jsp?startTime="+operationStartTIme+"&endTime="+operationEndTime+"&type="+globalType+"&value="+globalValue+"&filter="+globalFilter+"&bts="+globalBts+"&country="+globalCountry+"&oprname="+globalOprname+"";
var windowName="Plot Window";
openNewPlotWindow(url,windowName);
}

function openNewPlotWindow(url,windowName) {
       newwindow=window.open(url,windowName,'height=1000,width=1000');
       if (window.focus) {newwindow.focus()}
       return false;
     }




