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
					{ label: 'IP', name: 'ip', width: 150,search: true },
					{ label: 'count', name: 'count', width: 150,search: true },
					{ label: 'packet_type', name: 'packet_type', width: 150,search: true },
                    { label: 'Freq', name: 'freq', width: 150,search: true },
                    { label: 'Band', name: 'band', width: 150,search: true },
                    { label: 'arfcn', name: 'arfcn', width: 150,search: true },
                    { label: 'mcc', name: 'mcc', width: 150,search: true },
                    { label: 'country', name: 'country', width: 150,search: true },
                    { label: 'mnc', name: 'mnc', width: 150,search: true },
                    { label: 'oprname', name: 'opr', width: 150,search: true },
                    { label: 'lac', name: 'lac', width: 150,search: true },
                    { label: 'cell', name: 'cell', width: 150,search: true },
                    { label: 'ncc', name: 'ncc', width: 150,search: true },
                    { label: 'bcc', name: 'bcc', width: 150,search: true },
                    { label: 'rssi', name: 'rssi', width: 150,search: true },
                    { label: 'snr', name: 'snr', width: 150,search: true },
                    { label: 'ta', name: 'ta', width: 150,search: true },
                    { label: 'sysloc', name: 'sysloc', width: 150,search: true },
                    { label: 'tstmp', name: 'tstmp', width: 150,search: true },
                    { label: 'Log Time', name: 'inserttime', width: 150,search: true}
                    
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
        			url:"../Operations",
        			//url:"../resources/test.json",
        			data:{"methodName":"getoprlogsData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
        			type:"post",
        			success:function(cdrData)
        			{
        				serverData = JSON.parse(cdrData);
        				
        				
        				if(serverData[0].result != undefined && serverData[0].result == "fail")
        				{
        					alert("Limit Exceed Please try Short time span");
        				}
        				else
        				{	
        					$("#jqGrid").jqGrid('setGridParam', {
            			        datatype: 'local',
            			        data: serverData
            			    });
            				$("#jqGrid").trigger("reloadGrid");
        				}
        				
        				
        				
        			}
        		});
        	});
        }        
        
        
        var getDataExcelExport = function()
        {
                    	if(serverData.length <=0){alert("Please generate report first!!");return false;}
        				var reportNameString="Scanned Data";
              			
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
        	
        
        		collnames['IP']='ip';
        		collnames['Count']='count';
        	    collnames['packet_type']='packet_type';        	    
        		collnames['Freq']='freq';
        		collnames['band']='band';
        		collnames['arfcn']='arfcn';
        		collnames['mcc']='mcc';
        		collnames['country']='country';
        		collnames['mnc']='mnc';
        		collnames['opr']='opr';
        		collnames['lac']='lac';        	    
        		collnames['cell']='cell';
        		collnames['ncc']='ncc';
        		collnames['bcc']='bcc';
        		collnames['rssi']='rssi';
        		collnames['snr']='snr';
        		collnames['ta']='ta';
        	    collnames['sysloc']='sysloc';        	    
        		collnames['tstmp']='tstmp';
        		
        		return collnames;
        }
