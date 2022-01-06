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
					{ label: 'Sufi_Id', name: 'sufi_id', width: 150,search: true },
					{ label: 'LAC', name: 'lac', width: 150,search: true },
					{ label: 'Logtime', name: 'logtime', width: 150,search: true }
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
        			url:dirPath+"service/3g/lacchange",
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
        				var reportNameString="Alarm Data";
              			
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
        	
        
        		//collnames['ID']='aid';
        		collnames['SUFI_ID']='sufi_id';
        	    collnames['LAC']='lac';
        	    collnames['Logtime']='logtime';
        		return collnames;
        }
