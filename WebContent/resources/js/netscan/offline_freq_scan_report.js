var serverData = [];
var colmodal = [];
var feqScanReport =     [
               			{ label: 'TECH', name: 'TECH', width: 75,search: false },       
              			{ label: 'TIMESTAMP_M', name: 'TIMESTAMP_M', width: 150,search: true },
             			{ label: 'FREQ', name: 'FREQ', width: 150,search: true },
             			{ label: 'RSSI', name: 'RSSI', width: 150,search: true },    					
             			{ label: 'TIMESTAMP', name: 'TIMESTAMP', width: 150,search: true },
             			{ label: 'LAT', name: 'LAT', width: 150,search: true },
             			{ label: 'LONG', name: 'LONG', width: 150,search: true }
                     
                 ];

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
	    pager: "#pager"
	});
	jQuery("#data_tab").jqGrid('navGrid','#pager',{edit:false,add:false,del:false,refresh:false});
}



var registerCellscanEvent = function() {

    $("#downloadReport").click(function() {
        getDataExcelExport();
    });
    $("#get_cdr_data").click(function() {
        if ($("#startTime").val() == null || $("#startTime").val() == "") {
            alert("Please Fill the Start Date");
            return;
        }
        if ($("#endTime").val() == null || $("#endTime").val() == "") {
            alert("Please Fill the End Date");
            return;
        }
        $.ajax({
            url: dirPath + "service/netscan/getfreqsreport",
            data: {
                "startTime": toUtcTime($("#startTime").val()),
                "endTime": toUtcTime($("#endTime").val())
            },
            type: "post",
            success: function(data) {
                
            	
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
            }
        });
    });
}

$(document).ready(function() {
    initilizeReportGrid();
    registerCellscanEvent();
});




var getDataExcelExport = function() {
    if (serverData.length <= 0) {
        alert("Please generate report first!!");
        return false;
    }
    var reportNameString = "Frequency Scan Data";

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
        dataset: serverData,
        columns: colArray,
        reportName: header
    });
}

/*function getColumnsData() {
    var collnames = {};
    collnames['ID'] = 'aid';
    collnames['CMD'] = 'cmd_code';
    collnames['origin'] = 'origin';
    collnames['sufi_id'] = 'sufi_id';
    collnames['alarm_type'] = 'alarm_type';
    collnames['cause'] = 'cause';
    collnames['T-Stmp'] = 'tstmp';
    return collnames;
}*/

function getColumnsData(){
    var collnames = {};
	for(var i=0;i<feqScanReport.length;i++){
	collnames[feqScanReport[i].name]=feqScanReport[i].name;
	}
    return collnames;
}