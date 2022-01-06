var serverDataWIDS = [];
        $(document).ready(function () {
            $("#jqGrid_1").
            jqGrid({
               
                datatype: "jsonstring",
                datastr: serverDataWIDS,
               
                colModel: 
                [
					{ label: 'Id', name: 'id', width: 75,search: false,hidden:true },       
					//{ label: 'S-Type', name: 'stype', width: 150,search: true },
					{ label: 'IP', name: 'ip', width: 150,search: true },
					{ label: 'count', name: 'count', width: 150,search: true },
					{ label: 'Power', name: 'power', width: 150,search: true },
                    { label: 'Freq', name: 'freq', width: 150,search: true },
                    { label: 'T-Angle', name: 'angle', width: 150,search: true },
                    { label: 'P-Angle', name: 'anglet', width: 150,search: true },
                    { label: 'T-Stmp', name: 'tstmp', width: 150,search: true },
                    { label: 'Lat', name: 'lat', width: 150,search: true },
                    { label: 'Lon', name: 'lon', width: 150,search: true },
                    { label: 'Log Time', name: 'logtime', width: 150,search: true }
                    
                ],
				viewrecords: true,
                width: 1300,				
                height: 250,
                rowNum: 20,
                pager: "#jqGridPager_1"
            });
            jQuery("#jqGrid_1").jqGrid('navGrid','#jqGridPager_1',{edit:false,add:false,del:false,refresh:false});
           
        });        
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
                    { label:'PSC', name: 'psc', width: 150,search: true },
                    { label:'tstmp', name: 'tstmp', width: 150,search: true },
                    { label: 'T-Angle', name: 'angle', width: 150,search: true },
                    { label: 'P-Angle', name: 'anglet', width: 150,search: true },
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
        	$("#enableTrace").click(function(){
        		
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
        				cdrData = JSON.parse(cdrData);
        				//console.log(cdrData);
        				var path = [];
        				for(var i in cdrData)
        				{
        					var a = [];
        					a[0]=cdrData[i].lat;
        					a[1]=cdrData[i].lon;
        					path.push(a);
        					
        				}
        				$("#tab3").show();
    					$("#tab3").trigger("click");
        				console.log(path);
        				
        				var polyline = null;
        				if(polyline != null)
        				{
        					polyline.remove();
        				}
        				if(startMaker != null)
        				{
        					startMaker.remove();
        				}
        				if(endMaker != null)
        				{
        					endMaker.remove();
        				}
        				
        				var polyline = L.polyline(path, {color: 'blue'}).addTo(mymap);
        				var startMaker = L.marker(path[0]).addTo(mymap).bindTooltip("Start").openTooltip();;
        				var endMaker = L.marker(path[path.length-1]).addTo(mymap).bindTooltip("End").openTooltip();
        				//startMaker.remove();
        				//endMaker.remove();
        				//polyline.remove();
        				mymap.setView(path[0], 18) ;
        				mymap.invalidateSize();
        				$("#enableTrace").hide();
        			}
        		});
        		
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
            				//serverData = [];
            				$("#tab1").trigger("click");
            				$("#enableTrace").show();
        				}
        				
        				
        				
        				
        			}
        		});
        		getWidsData();
        	});
        }        
        
        
        var getDataExcelExport = function()
        {
                    	//if(serverData.length <=0){alert("Please generate report first!!");return false;}
        				var reportNameString="CDR GPS Data";
              			
        					var colArray=[];
                    		var cdrGPSColumnName = getColumnsData();
                    	    var widsColData = getColumnsWIDSData();
                    	    
                    	    colArray[0]=cdrGPSColumnName;
                    	    colArray[1]=widsColData;
                    	    
                    	    var xlData = [];
                    	    xlData[0] = serverData;
                    	    xlData[1]=serverDataWIDS;              	    
                    	    
                    	    
                    		tablesToExcel(colArray,xlData,['CDR','WIDS'], 'Data.xls', 'Excel');
                    		
        
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
        		collnames['T-Angle']='angle';
        		collnames['P-Angle']='anglet';
        	    collnames['ULARFCN']='ulrfcn';
        	    collnames['DLARFCN']='dlarfcn';
        	    collnames['OUT POW']='outpow';
        	    collnames['PSC']='psc';
        	    collnames['TSTMP']='tstmp';
        	    //collnames['F-TYP']='ftyp';
        	    //collnames['Recorded Time']='inserttime';
				collnames['Lat']='lat';
				collnames['Lon']='lon';
				collnames['Mobile Type']='mobile_type';
        		
        		return collnames;
        }
        
        
        function getWidsData()
        {
        	$.ajax({
    			url:"../Operations",
    			//url:"../resources/test.json",
    			data:{"methodName":"getSpectrumData","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
    			type:"post",
    			success:function(cdrData)
    			{
    				serverDataWIDS = JSON.parse(cdrData);
    				if(serverDataWIDS[0].result != undefined && serverDataWIDS[0].result == "fail")
    				{
    					alert("Limit Exceed Please try Short time span");
    				}
    				else
    				{	
        				$("#jqGrid_1").jqGrid('setGridParam', {
        			        datatype: 'local',
        			        data: serverDataWIDS
        			    });
        				$("#jqGrid_1").trigger("reloadGrid");
    				}
    				
    			}
    		});
        }
        function getColumnsWIDSData()
        {
        var collnames={};
        	
        
        		collnames['IP']='ip';
        		collnames['Count']='count';
        	    collnames['Power']='power';        	    
        		collnames['Freq']='freq';
        		collnames['T-Angle']='angle';
        		collnames['P-Angle']='anglet';
        		collnames['T-Stmp']='tstmp';
        		collnames['Lat']='lat';
        		collnames['Lon']='lon';
        		
        		return collnames;
        }
        
        
        
        function calculateRange(TH, power, gain, level) 
        {
            var MB = 1.5;
            var EIRP = power + gain - 1;

            var correctionfactor = (1.1 * Math.log(band) / Math.LN10 - 0.7) * MB - (1.56 * Math.log(band) / Math.LN10 - 0.8);
            var pathloss = 69.55 + 26.26 * Math.log(band) / Math.LN10 - 13.82 * Math.log(TH) / Math.LN10 - correctionfactor;
            var slope = (44.9 - 6.55 * Math.log(TH) / Math.LN10);
            var mid = (EIRP - level - pathloss) / slope;
            return ((Math.pow(10, mid) * 1000) -10);
        }