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
					{ label: 'oprid', name: 'oprid', width: 75,search: false,hidden:true },
					{ label: 'Name', name: 'name', width: 150,search: true },
					{ label: 'LOC', name: 'loc', width: 150,search: true },
					{ label: 'IP', name: 'ip', width: 150,search: true },
					{ label: 'Count', name: 'count', width: 150,search: true },
					{ label: 'IMEI', name: 'imei', width: 150,search: true },
					{ label: 'IMSI', name: 'imsi', width: 150,search: true },
					{ label: 'CGI', name: 'cgi', width: 150,search: true },
					{ label: 'Curr Opr', name: 'c_opr', width: 150,search: true },
					{ label: 'Curr Country', name: 'c_count', width: 150,search: true },
					{ label: 'Dlarfcn', name: 'dlarfcn', width: 150,search: true },
					{ label: 'Ularfcn', name: 'ulrfcn', width: 150,search: true },
					{ label: 'MCC', name: 'mcc', width: 150,search: true },
					{ label: 'MNC', name: 'mnc', width: 150,search: true },
					{ label: 'OPR', name: 'oprname', width: 150,search: true },
					{ label: 'Country', name: 'country', width: 150,search: true },
                    { label: 'Tac', name: 'tac', width: 150,search: true },
                    { label: 'RXL', name: 'rxl', width: 150,search: true },
                    { label: 'Act Rxl', name: 'realrxl', width: 150,search: true },
                    { label: 'TA', name: 'ta', width: 150,search: true },
                    { label: 'Power', name: 'power', width: 150,search: true },
                    { label: 'PSC', name: 'psc', width: 150,search: true },
                    { label: 'BTS', name: 'bts', width: 150,search: true },
                    { label: 'A-Type', name: 'atype', width: 150,search: true },
                    { label: 'A-Gain', name: 'again', width: 150,search: true },
                    { label: 'A-Height', name: 'aheight', width: 150,search: true },
                    { label: 'A-Elevation', name: 'aelevation', width: 150,search: true },
                    { label: 'A-Direction', name: 'adirection', width: 150,search: true },
                    { label: 'Terrain Type', name: 'ttype', width: 150,search: true },
                    { label: 'M-Type', name: 'mobile_type', width: 150,search: true },
                    { label: 'Tstmp', name: 'tstmp', width: 150,search: true },
                    { label: 'Lat-Lon', name: 'latlon', width: 150,search: true }
                    
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
        			data:{"methodName":"getoprreportData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
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
        				var reportNameString="Operations Report";
              			
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
        				 console.log(colArray);
                    	 
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
        
        		collnames['Name']='name';
        		collnames['LOC']='loc';
        		collnames['IP']='ip';
        		collnames['Count']='count';
        		collnames['IMSI']='imsi';
        		collnames['IMEI']='imei';
        	    collnames['MCC']='mcc';        	    
        		collnames['MNC']='mnc';        		
        		collnames['OPR']='oprname';
        		collnames['Country']='country';
        		collnames['CGI']='cgi';
        		collnames['Curr Opr']='c_count';
        		collnames['Curr Country']='c_opr';
        		collnames['Ularfcn']='ulrfcn';
        		collnames['Dlarfcn']='dlarfcn';
        		collnames['Tac']='tac';
        		collnames['Power']='power';
        		collnames['RXL']='rxl';
        		collnames['Act RXL']='realrxl';
        		collnames['TA']='ta';
        		collnames['PSC']='psc';
        	    collnames['BTS']='bts';        	    
        		collnames['A-Type']='atype';
        		collnames['A-Gain']='again';
        		collnames['A-Height']='aheight';
        		collnames['A-Elevation']='aelevation';
        		collnames['A-Direction']='adirection';
        		collnames['Terrain Type']='ttype';        		
        		collnames['LatLon']='latlon';
        		collnames['M-Type']='mobile_type';
        		collnames['Tstmp']='tstmp';        	    
        		
        		return collnames;
        }
