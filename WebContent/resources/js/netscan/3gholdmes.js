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
					{ label: 'SUB_ID', name: 'sub_id', width: 150,search: true },
					{ label: 'event_type', name: 'event_type', width: 150,search: true },
					{ label: 'c_ecno', name: 'c_ecno', width: 150,search: true },					
                    { label: 'c_RSCP', name: 'c_rscp', width: 150,search: true },
                    { label: 'm1_psc', name: 'm1_psc', width: 150,search: true },
					{ label: 'm2_psc', name: 'm2_ecno', width: 150,search: true },
                    { label: 'm3_psc', name: 'm3_psc', width: 150,search: true },
                    { label: 'm3_ecno', name: 'm3_ecno', width: 150,search: true },
                    { label: 'm4_psc', name: 'm4_psc', width: 150,search: true },
                    { label: 'm4_ecno', name: 'm4_ecno', width: 150,search: true },
                    { label: 'm5_psc', name: 'm5_psc', width: 150,search: true },
                    { label: 'm5_ecno', name: 'm5_ecno', width: 150,search: true },
                    { label: 'm6_psc', name: 'm6_psc', width: 150,search: true },
                    { label: 'm6_ecno', name: 'm6_ecno', width: 150,search: true },
                    { label: 'time', name: 'logtime', width: 150,search: true }
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
        			url:dirPath+"service/3g/holdmesevent",
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
        				var reportNameString="HOlD and MES Event Data";
              			
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
        		collnames['event_type']='event_type';
        	    collnames['sub_id']='sub_id';
        	    collnames['c_ecno']='c_ecno';
        	    collnames['c_RSCP']='c_rscp';
        	    collnames['m1_psc']='m1_psc';
        	    collnames['m1_ecno']='m1_ecno';
        	    collnames['m2_psc']='m2_psc';
        	    collnames['m2_ecno']='m2_ecno';
        	    collnames['m3_psc']='m3_psc';
        	    collnames['m3_ecno']='m3_ecno';
        	    collnames['m4_psc']='m4_psc';
        	    collnames['m4_ecno']='m4_ecno';
        	    collnames['m5_psc']='m5_psc';
        	    collnames['m5_ecno']='m5_ecno';
        	    collnames['m6_psc']='m6_psc';
        	    collnames['m6_ecno']='m6_ecno';
        	    collnames['logtime']='logtime';
        		return collnames;
        }
