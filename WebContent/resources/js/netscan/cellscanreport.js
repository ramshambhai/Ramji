var serverData = [];
var colmodal = [];
var cellScanColumn = 
	[
	 	{ label: 'ID', 			name: 'idofdatarow', 		width: 150,search: true },
	 	{ label: 'INDEX', 		name: 'INDEX', 		width: 150,search: true },
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
		{ label: 'RSCP', 		name: 'RSCP', 		width: 150,search: true,formatter:roundValues },
		{ label: 'RSSI', 		name: 'RSSI', 		width: 150,search: true,formatter:roundValues },	
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
	];

var roundValues = function(cellvalue, options, rowObject)
{
	if(cellvalue != null && cellvalue !="" && cellvalue != undefined)
	{
		return Math.ceil(cellvalue);
	}
	else
	{
		return "";
	}
}
var initilizeReportGrid = function() {

    
    $.jgrid.gridUnload('data_tab')
    $("#data_tab").jqGrid({
        datatype: "jsonstring",
        datastr: serverData,
        colModel: cellScanColumn,
        viewrecords: true,
        width: 1300,
        height: 250,
        rowNum: 20,
        onSelectRow: function(row_id) {
            $("#grd").toggleSubGridRow(row_id);
        },
        subGrid: true,
            "plusicon": "ui-icon-triangle-1-e",
            subGridOptions: {
            "minusicon": "ui-icon-triangle-1-s",
            "openicon": "ui-icon-arrowreturn-1-e",
            "reloadOnExpand": true,
            "selectOnExpand": true
        },

        subGridRowExpanded: function(subgrid_id, row_id) {

            var ret = $("#data_tab").jqGrid('getRowData', row_id); //get the selected row
            index = ret.INDEX; //get the data from selected row by column name
            id = ret.idofdatarow;
            console.log(index + ' : ' + id + ' : ');

            //<table class="tree" id=""><tbody></tbody></table>

            celldata = {
                "index": index,
                "id": id
            };
            $.ajax({
                url: dirPath + "service/netscan/neighbours",
                async: false,
                type: 'POST',
                dataType: 'json',
                data: celldata,
                async: false,
                success: function(data) {
                    $("#" + subgrid_id).html('<pre id="json-renderer"></pre>');
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
    jQuery("#data_tab").jqGrid('navGrid', '#pager', {
        edit: false,
        add: false,
        del: false,
        refresh: false
    });
}

var createCellScanReportGridData = function(data) {
    var gridData = [];
    for (var i in data) {


        //var data = JSON.parse(dataForCellReport[k].rpt_data);
        //for(var i in data)
        //{

        data[i].REPORT = JSON.parse(data[i].rpt_data).REPORT;


        for (var j in data[i].REPORT) {
            data[i].REPORT[j].INDEX = j;
            data[i].REPORT[j].btsip = data[i].btsip;
            //console.log(data[i].id);
            data[i].REPORT[j].idofdatarow = data[i].id;


            data[i].REPORT[j].UARFCN = data[i].REPORT[j].hasOwnProperty('UARFCN') ? data[i].REPORT[j].UARFCN : "";
            data[i].REPORT[j].ARFCN = data[i].REPORT[j].hasOwnProperty('ARFCN') ? data[i].REPORT[j].ARFCN : "";
            data[i].REPORT[j].EARFCN = data[i].REPORT[j].hasOwnProperty('EARFCN') ? data[i].REPORT[j].EARFCN : "";

            data[i].REPORT[j].SNR = data[i].REPORT[j].hasOwnProperty('SNR') ? data[i].REPORT[j].SNR : "";
            data[i].REPORT[j].TA = data[i].REPORT[j].hasOwnProperty('TA') ? data[i].REPORT[j].TA : "";
            data[i].REPORT[j].RSCP = data[i].REPORT[j].hasOwnProperty('RSCP') ? data[i].REPORT[j].RSCP : "";
            data[i].REPORT[j].ECNO = data[i].REPORT[j].hasOwnProperty('ECNO') ? data[i].REPORT[j].ECNO : "";
            data[i].REPORT[j].PD = data[i].REPORT[j].hasOwnProperty('PD') ? data[i].REPORT[j].PD : "";

            data[i].REPORT[j].PCI = data[i].REPORT[j].hasOwnProperty('PCI') ? data[i].REPORT[j].PCI : "";
            data[i].REPORT[j].RSRP = data[i].REPORT[j].hasOwnProperty('RSRP') ? data[i].REPORT[j].RSRP : "";
            data[i].REPORT[j].RSRQ = data[i].REPORT[j].hasOwnProperty('RSRQ') ? data[i].REPORT[j].RSRQ : "";

            if (data[i].REPORT[j].hasOwnProperty('PLMN')) {
                data[i].REPORT[j].MCC = data[i].REPORT[j].PLMN.MCC;
                data[i].REPORT[j].MNC = data[i].REPORT[j].PLMN.MNC;
            } else {
                data[i].REPORT[j].MCC = "";
                data[i].REPORT[j].MNC = "";
            }
            gridData.push(data[i].REPORT[j]);
        }
        //}
    }
    return gridData;
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
            url: dirPath + "service/netscan/cellscanreports",
            //url:"../resources/test.json",
            data: {
                "startTime": toUtcTime($("#startTime").val()),
                "endTime": toUtcTime($("#endTime").val())
            },
            type: "post",
            success: function(cdrData) {
                colmodal = cellScanColumn;
                serverData = createCellScanReportGridData(cdrData);
                initilizeReportGrid();
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
        dataset: serverData,
        columns: colArray,
        reportName: header
    });
}

function getColumnsData() 
{
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