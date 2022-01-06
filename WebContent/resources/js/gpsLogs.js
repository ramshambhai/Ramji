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
					{ label: 'IP', name: 'ip', width: 150,search: true },
					{ label: 'Count', name: 'count', width: 150,search: true },
					{ label: 'T-Stmp', name: 'tstmp', width: 150,search: true },					
					{ label: 'S-Staus', name: 's_status', width: 150,search: true },
					{ label: 'Lat', name: 'lat', width: 150,search: true },
                    { label: 'Lat-O', name: 'lato', width: 150,search: true },
                    { label: 'Lon', name: 'lon', width: 150,search: true },
                    { label: 'Lon-O', name: 'lono', width: 75,search: true},       
					//{ label: 'S-Type', name: 'stype', width: 150,search: true },
					{ label: 'Speed', name: 'speed', width: 150,search: true },
					{ label: 'Course', name: 'course', width: 150,search: true },
					{ label: 'Satellites', name: 'satellites', width: 150,search: true },
                    { label: 'Elevation', name: 'elev', width: 150,search: true },
                    { label: 'E-Unit', name: 'eunit', width: 150,search: true },
                    { label: 'Roll', name: 'roll', width: 75,search: true},       
					//{ label: 'S-Type', name: 'stype', width: 150,search: true },
					{ label: 'Pressure', name: 'pres', width: 150,search: true },
					{ label: 'Acc', name: 'acc', width: 150,search: true },
					{ label: 'Comp', name: 'comp', width: 150,search: true },
                    { label: 'Temp-1', name: 'temp1', width: 150,search: true },
                    { label: 'Temp-2', name: 'temp2', width: 150,search: true },
                    { label: 'Gy-X', name: 'gyx', width: 150,search: true },
                    { label: 'Gy-Y', name: 'gyy', width: 150,search: true },
					{ label: 'Gy-Z', name: 'gyz', width: 150,search: true },
                    { label: 'Tilt-X', name: 'tiltx', width: 150,search: true },
                    { label: 'Tilt-Y', name: 'tilty', width: 150,search: true },
                    { label: 'Tilt-z', name: 'tiltz', width: 150,search: true }
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
        			data:{"methodName":"getGPSData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
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
        				//serverData = [];
        			}
        		});
        	});
        }        
        
        
        var getDataExcelExport = function()
        {
                    	if(serverData.length <=0){alert("Please generate report first!!");return false;}
        				var reportNameString="GPS Data";
              			
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
        		collnames['T-Stmp']='tstmp';
        		collnames['S-Staus']='s_status';        	    
        		collnames['Lat']='lat';
        		collnames['Lat-O']='lato';
        		collnames['Lon']='lon';
        		collnames['Lon-O']='lono';
        	    collnames['Speed']='speed';        	    
        		collnames['Course']='course';
        		collnames['Satellites']='satellites';
        		collnames['Elevation']='elev';
        		collnames['E-Unit']='eunit';
        		collnames['Roll']='roll';
        	    collnames['Pressure']='pres';        	    
        	    collnames['Acc']='acc';
        		collnames['Comp']='comp';
        	    collnames['Temp-1']='temp1';
        		collnames['Temp-2']='temp2';        		
        		collnames['Gy-X']='gyx';
        		collnames['Gy-Y']='gyy';
        		collnames['Gy-Z']='gyz';
        	    collnames['Tilt-X']='tiltx';        	    
        		collnames['Tilt-Y']='tilty';
        		collnames['Tilt-z']='tiltz';        		
        		return collnames;
        }
