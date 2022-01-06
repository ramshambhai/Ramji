        var serverData = [];
        $(document).ready(function () {
            $("#jqGrid").
            jqGrid({
               // url: '../resources/test.json',
                //mtype: "GET",
                datatype: "jsonstring",
                datastr: serverData,
               // loadonce:true,
                colModel: 
                [
					{ label: 'Id', name: 'id', width: 75,search: false,hidden:true },       
                    { label: 'MSISDN', name: 'msisdn', width: 150,search: true },
                    { label: 'IMSI', name: 'imsi', width: 150,search: true },
                    { label: 'IMEI', name: 'imei', width: 150,search: true },
                    { label: 'H-MCC', name: 'hmcc', width: 150,search: true },
                    { label: 'H-MNC', name: 'hmnc', width: 150,search: true },
                    { label: 'MCC', name: 'mcc', width: 150,search: true },
                    { label: 'MNC', name: 'mnc', width: 150,search: true },
                    { label:'LAC', name: 'lac', width: 150,search: true },
                    { label: 'CELL', name: 'cell', width: 150,search: true },       
                    { label: 'Subscriber State', name: 'state', width: 150,search: true },
                    { label: 'Date', name: 'logtime', width: 150,search: true },
                    { label: 'Coordinate', name: 'coordinate', width: 150,search: true },
                    { label: 'HLR', name: 'hlr', width: 150,search: true },
                    { label:'VLR', name: 'vlr', width: 150,search: true },
                    { label:'MSC Addr', name: 'mscaddr', width: 150,search: true },
                    { label:'FTN', name: 'ftn', width: 150,search: true }
                ],
				viewrecords: true,
                width: 1300,
				
                height: 250,
                rowNum: 20,
                pager: "#jqGridPager"
            });
            jQuery("#jqGrid").jqGrid('navGrid','#jqGridPager',{edit:false,add:false,del:false,refresh:false});
            registerEvents();
        });
        var registerEvents = function()
        {
        	
        	$("#get_cdr_data").click(function(){
        		if($("#startTime").val() == null || $("#startTime").val() == "")
            	{
            		alert("Please Fill the Start Date");
            		return;
            	}
            	if($("#endTime").val() == null || $("#endTime").val() == "")
            	{
            		alert("Please Fill the End Date");
            		return;
            	}
        		$.ajax({
        			url:"../Operations",
        			//url:"../resources/test.json",
        			data:{"methodName":"getGeoLocdata","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
        			type:"post",
        			success:function(cdrData)
        			{
        				serverData = JSON.parse(cdrData);
        				//serverData = cdrData;
        				//$("#jqGrid").trigger("reloadGrid", [{current: true}]);
        				$("#jqGrid").jqGrid('setGridParam', {
        			        datatype: 'local',
        			        data: serverData
        			    });
        				$("#jqGrid").trigger("reloadGrid");
        			}
        		});
        	});
        $("#perform_opr").click(function()
        {
        	var oprType = $("#opr").val();
        	var selectedRowId = $('#list').jqGrid ('getGridParam', 'selrow');
        	var columnName = $('#list').jqGrid('getCell', selectedRowId, 'columnName');
        	var selectedRowId = $('#jqGrid').jqGrid ('getGridParam', 'selrow');
        	var value = $('#jqGrid').jqGrid('getCell', selectedRowId, 'id');        	
        	
        });
        
        $("#trace").click(function(){
        	var selectedRowId = $('#jqGrid').jqGrid ('getGridParam', 'selrow');
        	
        	if(selectedRowId == null)
        	{
        		alert("Please select a row");
        		return;
        	}
        	else
        	{
        		var value = $('#jqGrid').jqGrid('getCell', selectedRowId, 'id');
        		//alert(value);
        		window.location.href = "geoIndexOffline.jsp?id="+value;
        	}
        	
        });
        
        	
        }