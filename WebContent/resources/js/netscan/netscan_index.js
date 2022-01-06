/**
 * 
 */

$(document).ready(function(){
	registerEvents();
	initData();
});

var initData = function()
{
	$("#scan_type").trigger("change");
	$("#report_action").trigger("change");
	
	
}
var registerEvents = function()
{
	showHideRelatedfields();
}

var getReports = function()
{
	
	switch(parseInt($("#report_type").val()))
	{
		case 0:
			getCellReport();
		break;
		case 1:
			getFreqReport();
		break;
	}
}

/*
 * 
 * */
var showHideRelatedfields = function()
{
	$("#scan_type").change(function(){
		$("#netscan-scanner tr").show();
		switch(parseInt($(this).val()))
		{
			case 0:
				$(".exe_scan_field").hide();
				$(".cell_scan_field").hide();
				$(".stop_scan_row").hide();
				$(".freq_scan_field").show();
			break;
			case 1:
				$(".exe_scan_field").hide();
				$(".freq_scan_field").hide();
				$(".stop_scan_row").hide();
				$(".cell_scan_field").show();
				break;
			case 2:
				$(".cell_scan_field").hide();
				$(".freq_scan_field").hide();
				$(".stop_scan_row").hide();
				$(".exe_scan_field").show();
				break;
			case 3:
				$("#netscan-scanner tr").hide();
				$(".cell_scan_field").hide();
				$(".freq_scan_field").hide();
				$(".exe_scan_field").hide();
				
				$(".stop_scan_row").show();
				$(".stop_scan_row").closest("tr").show();
				break;
		}
	});
	
	$("#report_action").change(function(){
		switch(parseInt($(this).val()))
		{
			case 0:
				$(".save-report").hide();
				$(".get-report").show();
				$(".get-report-btn").show();
			break;
			case 1:
				$(".get-report").hide();
				$(".get-report-btn").hide();
				$(".save-report").show();
				break;
		}
	});
	
}