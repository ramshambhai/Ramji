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
					//{ label: 'S-Type', name: 'stype', width: 150,search: true },
					{ label: 'event_code', name: 'event_code', width: 150,search: true },
					{ label: 'event_type', name: 'event_type', width: 150,search: true },					
                    { label: 'sub_id', name: 'sub_id', width: 150,search: true },
                    { label: 'freq_type', name: 'freq_type', width: 150,search: true },
					{ label: 'psc', name: 'psc', width: 150,search: true },
                    { label: 'cell', name: 'cell', width: 150,search: true },
                    { label: 'ecno', name: 'ecno', width: 150,search: true },
                    { label: 'rscp', name: 'rscp', width: 150,search: true },
                    { label: 'rssi', name: 'rssi', width: 150,search: true },
                    { label: 'lac', name: 'lac', width: 150,search: true },
                    { label: 'mcc', name: 'mcc', width: 150,search: true },
                    { label: 'mnc', name: 'mnc', width: 150,search: true },
                    { label: 'BCCH_ARFCN', name: 'bcch_arfcn', width: 150,search: true },
                    { label: 'logtime', name: 'logtime', width: 150,search: true }
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
        	
        	$("#downloadReport").click(function(){
        		getDataExcelExport();
        	});
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
        			url:dirPath+"service/3g/dedicatedmeasevent",
        			//url:"../resources/test.json",
        			data:{"startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
        			type:"post",
        			success:function(cdrData)
        			{
        				//serverData = JSON.parse(cdrData);
        				serverData = cdrData;
        				//$("#jqGrid").trigger("reloadGrid", [{current: true}]);
        				$("#jqGrid").jqGrid('setGridParam', {
        			        datatype: 'local',
        			        data: serverData
        			    });
        				$("#jqGrid").trigger("reloadGrid");
        				//serverData = [];
        			}
        		});
        	});
        }        
        
        
        var getDataExcelExport = function()
        {
                    	if(serverData.length <=0){alert("Please generate report first!!");return false;}
        				var reportNameString="Dedicated Meas Data";
              			
        					var colArray=[];
                    		var exportColumnName = getColumnsData();
                    	 //for( var i=0; i<exportColumnName.length ; i++)
                    		 for( var i in exportColumnName)
                    	 {
                    			colData={};
                    		
                    			colData.headertext=i;
                    			colData.datatype="string";
                    			colData.datafield=exportColumnName[i];
                                //colData.width= "100px" ;
                    			colData.ishidden= "false"; 
                    			colArray.push(colData);	
                    	 }
        				 //console.log(alarmOrCdrData);
                    	 
                    		var headerText = '<br>'+reportNameString+'<br><br>Generated Time: ' + new Date()+'<br><br>';    
                    		var header = headerText.bold();		
                    		$("#btn_dataExport").battatech_excelexport({
                    						containerid: "btn_dataExport"
                    						, datatype: 'json'
                    						, dataset: serverData
                    						, columns: colArray
                    						, reportName:header 
                    					});	
        }
        
        function getColumnsData()
        {
        var collnames={};
        	
        
        		collnames['ID']='id';
        		collnames['event_code']='event_code';
        	    collnames['event_type']='event_type';
        	    collnames['sub_id']='sub_id';
        	    collnames['freq_type']='freq_type';
        	    collnames['psc']='psc';
        	    collnames['cell']='cell';
        	    collnames['ecno']='ecno';
        		collnames['rscp']='rscp';
        	    collnames['rssi']='rssi';
        	    collnames['lac']='lac';
        	    collnames['cell']='cell';
        	    collnames['mcc']='mcc';
        	    collnames['mnc']='mnc';
        	    collnames['bcch_arfcn']='bcch_arfcn';
        	    collnames['Logtime']='logtime';
        		return collnames;
        }
