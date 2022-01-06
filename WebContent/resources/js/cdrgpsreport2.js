        var serverData = [];
        $(document).ready(function () {
            $("#jqGrid").
            jqGrid({
               // url: '../resources/test.json',
                //mtype: "GET",
                datatype: "jsonstring",
                datastr: serverData,
               // loadonce:true,
                //stype,ip,count,packet_type,imsi,imei,ta,rxl,cgi,sysloc,band,ulrfcn,dlarfcn,outpow,tstmp,inserttime,	lat,lon
				colModel: 
                [
					//{ label: 'Id', name: 'id', width: 75,search: false,hidden:true },       
					{ label: 'S-Type', name: 'stype', width: 150,search: true },
					{ label: 'IP', name: 'ip', width: 150,search: true },
					{ label: 'count', name: 'count', width: 150,search: true },
					{ label: 'Operation', name: 'packet_type', width: 150,search: true },
                    { label: 'imsi', name: 'imsi', width: 150,search: true },
                    { label: 'imei', name: 'imei', width: 150,search: true },
                    //{ label: 'MSISDN', name: 'msisdn', width: 150,search: true },
                    //{ label: 'ptmsi', name: 'ptmsi', width: 150,search: true },
                    //{ label:'tmsi', name: 'tmsi', width: 150,search: true },
                    //{ label: 'ol', name: 'ol', width: 150,search: true },       
                    //{ label: 'nl', name: 'nl', width: 150,search: true },
                    //{ label: 'cid', name: 'cid', width: 150,search: true },
                    { label: 'ta', name: 'ta', width: 150,search: true },
                    { label: 'rxl', name: 'rxl', width: 150,search: true },
                    { label: 'CGI', name: 'cgi', width: 150,search: true },
                    { label: 'Sys Loc', name: 'sysloc', width: 150,search: true },
                    //{ label:'MsLoc', name: 'msloc', width: 150,search: true },
                    { label:'Band', name: 'band', width: 150,search: true },
                    { label:'UlArfcn', name: 'ulrfcn', width: 150,search: true },
                    { label:'DlArfcn', name: 'dlarfcn', width: 150,search: true },
                    { label:'OutPow', name: 'outpow', width: 150,search: true },
                    { label:'tstmp', name: 'tstmp', width: 150,search: true },
                    //{ label:'ftype', name: 'ftyp', width: 150,search: true },
                    //{ label:'inserttime', name: 'inserttime', width: 150,search: true },
					{ label:'lat', name: 'lat', width: 150,search: true },
                    { label:'lon', name: 'lon', width: 150,search: true },
					{ label:'Mobile Type', name: 'mobile_type', width: 150,search: true }
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
        			data:{"methodName":"getCDRGPSData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
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
        				var reportNameString="CDR GPS Data";
              			
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
        	
        
        		//collnames['ID']='id';
        		collnames['S-Type']='stype';
        		collnames['IP']='ip';
        		collnames['Count']='count';
        	    collnames['Operations']='packet_type';
        		collnames['IMSI']='imsi';
        		collnames['IMEI']='imei';
        		//collnames['MSISDN']='msisdn';
        		//collnames['OL']='ol';
        	    //collnames['TA']='ta';
        	    collnames['RXL']='rxl';
        		collnames['CGI']='cgi';
        		collnames['Sys Loc']='sysloc';
        		//collnames['MS loc']='msloc';
        		collnames['BAND']='band';
        	    collnames['ULRAFCN']='ulrfcn';
        	    collnames['DLARFCN']='dlarfcn';
        	    collnames['OUT POW']='outpow';
        	    collnames['TSTMP']='tstmp';
        	    //collnames['F-TYP']='ftyp';
        	    //collnames['Recorded Time']='inserttime';
				collnames['Lat']='lat';
				collnames['Lon']='lon';
				collnames['Mobile Type']='mobile_type';
        		
        		return collnames;
        }
