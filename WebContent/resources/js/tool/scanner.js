var opratorTowerMarkers = [];
var opreator_cells=[];
var oprGridData = [];
var oprCellData=[];
var utcStartTime=""
var utcEndTime=""
var hisOprdatatableFlag = false;
//var oprGridData = {id:"sunil",date:"22:22:22",time:"11:11:11",packet_type:"123",type:"hello",freq:"234",band:"e_900",mcc:"123",mnc:"456",lac:"789",cell:"101112",operators:"airtell",rssi:"-110"};
var oprdatatable = function()
{
	var outerwidth=$("#oprgrid").parent().width();

	
	$("#oprgrid").jqGrid({
	                data : oprGridData,
	                datatype : "local",
	                colNames : ['Date', 'Time', 'Trigger', 'Antenna','Operator', 'IMSI', 'IMEI', 'Distance(m)', 'RxL', 'Tech','Band', 'UL Freq','UL ARFCN'],
	                	          				
					colModel : [ 
						
					{
	                        name : 'insert_time',
	                        index : 'insert_time',
	                        	formatter: function(cellvalue, options, rowObject){
							        return cellvalue.split(" ")[0];
							    }
	                },
	                {
	                        name : 'insert_time',
	                        index : 'insert_time',
	                        formatter: function(cellvalue, options, rowObject){
	                        	return cellvalue.split(" ")[1];
						    }
	                },
	                {
	                        name : 'Trigger',
	                        index : 'Trigger'
	                },
	                {
							name : 'Tech',
							index : 'Tech',
							width:20,
							formatter: function(cellvalue, options, rowObject){
						        if(cellvalue == "TRACKER_CELL")
						            return '<span style="background-color: red; display: block; width: 100%; height: 100%; ">' + "Suspect" + '</span>';
						        else
						            return '<span style="background-color: rgb(198,227,159); display: block; width: 100%; height: 100%; ">' + "Normal" + '</span>';
						    }
					},
					{
	                        name : 'Operator',
	                        index :'Operator'
	                },
					{
                        name : 'IMSI',
                        index :'IMSI'
					}
					,{
	                        name : 'IMEI',
	                        index :'IMEI'
	                },{
	                        name : 'Distance(m)',
	                        index :'Distance(m)'
	                },{
	                        name : 'RxL',
	                        index :'RxL',
                        	formatter: function(cellvalue, options, rowObject)
                        	{
                        		console.log(rowObject);
                        		if(rowObject.packet_type == "LTE")
                        		{
                        			return rowObject.tac	
                        		}
                        		else
                        		{
                        			return rowObject.lac
                        		}
						    }
	                },{
	                        name : 'Band',
	                        index :'Band',
	                        width:20
	                },{
	                        name : 'UL Freq',
	                        index :'UL Freq'
	                },{
	                        name : 'UL ARFCN',
	                        index :'UL ARFCN'
	                }
	                ],
	                ondblClickRow:function(rowid,iRow,iCol,e)
	                {
	                	console.log(rowid);
	                	selectTower(rowid);
	                },
	                afterInsertRow:function(rowid,rowdata,rowelem)
	                {
	                	console.log(rowdata);
	                	console.log(rowelem);
	                	opreator_cells[rowelem.mcc+"-"+rowelem.mnc+"-"+rowelem.lac+"-"+rowelem.cell]=rowelem.id;
	                	console.log(opreator_cells);
	                },
	                pager : '#oprGridReportPager',
	                rowNum : 30,
					height: ($("#oprgrid").parent().height())-80,
	                rowList : [10, 20,30],
					loadonce: true,
	                viewrecords : true,
	                gridview : false,
	                caption : 'Cell Detected'
	        }).setGridWidth(outerwidth);
			//jQuery("#oprgrid").jqGrid('filterToolbar',options);
			/*$("#oprgrid").jqGrid('filterToolbar', {
                autosearch: true,
                stringResult: false,
                searchOnEnter: true,
                defaultSearch: "cn",
            });*/
	        jQuery("#oprgrid").jqGrid('navGrid', '#oprGridReportPager', {
	                edit : false,
	                add : false,
	                del : false,
	                search : true
	        });
}



var hisOprdatatable = function(data)
{
	var outerwidth=$("#his_oprgrid").parent().width();
	
	$("#his_oprgrid").jqGrid({
	                data : data,
	                shrinkToFit: false,
	                datatype : "local",
	                colNames : ['id','Date', 'Time', 'Trigger','Operator', 'IMSI', 'IMEI', 'Distance(m)','Sys Location', 'RxL', 'Tech','Band', 'UL Freq','UL ARFCN','Target', 'Antenna','Calc_Basis'],          				
	            	colModel : [ 
	            			{ 
	            				//var  row= jQuery("#his_oprgrid").jqGrid('getRowData',id);
	    	                	
    	                	
                            name : 'id',
                            index : 'id',
                            key:true,
                            hidden:true,
                            //	name: 'CanDo', width: 50, index: 'CanDo', 
	            			 editable: false,
	            			 edittype: 'checkbox', editoptions: { value: "True:False" }, 
	            			 formatter: "checkbox", formatoptions: { disabled: true} ,
	            			 width:20
	            			},
	            			
	            		
						{
		                        name : 'insert_time',
		                        index : 'insert_time',
					width:120
//		                        	formatter: function(cellvalue, options, rowObject){
//								        return cellvalue.split(" ")[0];
//								    }
		                },
		                {
		                        name : 'insert_time',
		                        index : 'insert_time',
		                        hidden:true,
		                        formatter: function(cellvalue, options, rowObject){
		                        	return cellvalue.split(" ")[1];
					    }
		                	},
		                	{
		                        name : 'trigger_source',
		                        index : 'trigger_source',
		                        hidden:true
		                	},
		                	{
		                        name : 'operator',
		                        index :'operator',
		                        hidden:true
			                },
					{
		                        name : 'imsi',
		                        index :'imsi',
					width:110
					}
					,{
		                        name : 'imei',
		                        index :'imei',
					width:110
					},{
		                        name : 'distance',
		                        index :'distance',
		                        width:70
	                		},
                			{
                			name : 'self_loc',
                			index : 'self_loc'
                					
                			},
                			{
		                        name : 'rxl',
		                        index :'rxl',
		                        hidden:true
	                        	
		                	},
		                	{
		                	name : 'stype',
		                	index : 'stype',
					width:50
		                	},
		                	{   name : 'band',
		                        index :'band',
					width:50
		                        
		                	},{
		                        name : 'freq',
		                        index :'freq',
		                        hidden:true
		                	},{
		                        name : 'ulrfcn',
		                        index :'ulrfcn',
		                        hidden:true
		                	},
		               	 	{
		                        name : 'traget_type',
		                        index : 'traget_type',
					width:50
			                },
			                {
		                	name : 'profile_name',
		                	index : 'profile_name',
					width:60
			                },
			                {
		                	name : 'calc_basis',
		                	index : 'calc_basis',
					width:60
			                }       ],
		                
		            
	                ondblClickRow:function(id,rowid,iRow,iCol,e)
	                {
//	                	var  row= jQuery("#his_oprgrid").jqGrid('getRowData',id);
//	                	row.id=true;
//	                	var ch =  jQuery(this).find('#'+id+' input[type=checkbox]').prop('checked');
//	                	if(ch) {
//	                	      jQuery(this).find('#'+id+' input[type=checkbox]').prop('checked',true);
//	                	} else {
//	                	      jQuery(this).find('#'+id+'input[type=checkbox]').prop('checked',false);                       
//	                	}
	                	//row.id
	                	console.log(rowid);
	                	selectTower(rowid);
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	jQuery(this).find('#'+id+' input[type=checkbox]').prop('checked',true);
	                	
	                	
	                	
	                	
	                	
	                    var  row= jQuery("#his_oprgrid").jqGrid('getRowData',id);
//	                    if(row.id.localeCompare("True")==0){

			if(row.calc_basis.localeCompare("GPS")!=0){
	                    	
	                    	
	                    	//var Talayers = taCircleLayers.getLayers();
	                    	//setTaCirclesCenter(row.self_loc);
	                    	
	                    	addOprTower2([row.self_loc.split(',')[0],row.self_loc.split(',')[1]],'../../resources/images/tower.png','',row.distance,id);
	                    	//taCircleLayers.addLayer(L.circle(row.self_loc, {radius: row.distance,color:"#00000096",opacity:0.2,fillColor:"#fff59f",fillOpacity:0}));
	                    	
	                    	
	                    	
	                		var oprTower = {};
	                		var ddd = "<table border=1 class='ll table table-default' style='width: 160px !important;'>"+
	                		"<thead></thead><tbody>"+
	                		"<tr><td>ReordNumber</td><td>"+id+"</td></tr>"+
	                		"<tr><td>IMSI</td><td>"+row.imsi+"</td></tr>"+
	                		"<tr><td>IMEI</td><td>"+row.imei+"</td></tr>"+
	                		"<tr><td>Distance(m)</td><td>"+row.distance+"</td></tr>"+
	                		"<tr><td>Sys Location</td><td>"+row.self_loc+"</td></tr>"+
	                		"<tr><td>Tech</td><td>"+row.stype+"</td></tr>"+
	                		"<tr><td>Band</td><td>"+row.band+"</td></tr>"+
	                		"<tr><td>Date</td><td>"+row.insert_time+"</td></tr>"+
	                		"</tbody></table>";
	                			oprTower = addOprTower([row.self_loc.split(',')[0],row.self_loc.split(',')[1]],'../../resources/images/tower.png',ddd);
	                		
	                		}
	                		
//	                    }
//	                   else{
//	                    	var  row= jQuery("#his_oprgrid").jqGrid('getRowData',id);
//	                    	removeOprTower2("Name_"+id);
//	                    }
//	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	  
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                	
	                },
	                //onCellSelect:function(rowid,iRow,iCol,e){
	                onCellSelect: function(id,cellidx,cellvalue,e) {     
	                	
//	                	
//	                	
//	                	var $self = $(this),
//	                	iCol = $.jgrid.getCellIndex($(e.target).closest("td")[0]),
//	                	cm = $self.jqGrid("getGridParam", "colModel"),
//	                	localData = $self.jqGrid("getLocalRow", id);
//	                	if (cm[iCol].name === "id" && e.target.tagName.toUpperCase() === "INPUT") {
//	                		// set local grid data
//	                		localData.id = $(e.target).is(":checked");
//	                		alert(JSON.stringify(localData));
//	                	};
	                	
	                	 var  row= jQuery("#his_oprgrid").jqGrid('getRowData',id);
	                   // if(row.id.localeCompare("True")==0){
	                    	
	                    	
	                    	//var Talayers = taCircleLayers.getLayers();
	                    	//setTaCirclesCenter(row.self_loc);
	                    	
	                    //	addOprTower2([row.self_loc.split(',')[0],row.self_loc.split(',')[1]],'../../resources/images/tower.png','',row.distance,"Name_"+id);
	                    	//taCircleLayers.addLayer(L.circle(row.self_loc, {radius: row.distance,color:"#00000096",opacity:0.2,fillColor:"#fff59f",fillOpacity:0}));
	                    	
	                    
	                 //   loadLeafMap();
	                //    }
	               //    else{
	                    //	var  row= jQuery("#his_oprgrid").jqGrid('getRowData',id);
	                    //	removeOprTower2("Name_"+id);
	                 //   }

	                },
//	                afterInsertRow:function(rowid,rowdata,rowelem)
//	                {
//	                	console.log(rowdata);
//	                	console.log(rowelem);
//	                //	opreator_cells[rowelem.mcc+"-"+rowelem.mnc+"-"+rowelem.lac+"-"+rowelem.cell]=rowelem.id;
//	                	console.log(opreator_cells);
//	                },
	                pager : '#his_oprGridReportPager',
	                rowNum : 30,
					height: ($("#his_oprgrid").parent().height())-80,
	                rowList : [10, 20,30],
					loadonce: true,
	                viewrecords : true,
	                rownumbers: true,
	                gridview : true,
	                caption : 'Captured Mobiles'
	        }).setGridWidth(outerwidth);
			//jQuery("#his_oprgrid").jqGrid('filterToolbar',options);
			/*$("#his_oprgrid").jqGrid('filterToolbar', {
                autosearch: true,
                stringResult: false,
                searchOnEnter: true,
                defaultSearch: "cn",
            });*/
	        jQuery("#his_oprgrid").jqGrid('navGrid', '#his_oprGridReportPager', {
	                edit : false,
	                add : false,
	                del : false,
	                search : true
	        });

}

var  clearfunction = function(){
	var grid = $("#his_oprgrid");
	
	
	grid.jqGrid('resetSelection');

}
var getOprGridDataReport = function(oprName,nodeName)
{
	 $.ajax({
		url:dirPath+"Operations",
		async:false,
		type:'post',
		dataType:"json",
		data:{methodName:"getNetworkScanReport",oprName:"total",nodeName:nodeName,operationStartTime:operationStartTIme},
		success:function(data)
		{
			oprGridData=eval(data);
			//dataOfNetworkScanReport=getFormattedNetworkScanData(dataOfNetworkScanReport)
			reloadOprGridReport(oprName,nodeName);
			addOprOnMap(data);
			addCardCells(data);
		}
	});
}

var addOprOnMap = function(data)
{
	for(var i in data)
	{
		var oprTower = {};
		var ddd = "<table border=1 class='ll table table-default' style='width: 160px !important;'>"+
		"<thead></thead><tbody>"+
		"<tr><td>Name</td><td>"+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')'+"</td></tr>"+
		"<tr><td>LAC</td><td>"+data[i].lac+"</td></tr>"+
		"<tr><td>CELL</td><td>"+data[i].cell+"</td></tr>"+
		"<tr><td>BAND</td><td>"+data[i].band+"</td></tr>"+
		"<tr><td>RxL</td><td>"+data[i].rssi+"</td></tr>"+
		"<tr><td>DL Freq</td><td>"+data[i].freq+"</td></tr>"+
		"<tr><td>Tech</td><td style='text-transform: uppercase;'>"+data[i].packet_type+"</td></tr>"+
		"<tr><td>Time</td><td>"+data[i].inserttime+"</td></tr>"+
		"</tbody></table>";
		if(data[i].cell_type != "TRACKER_CELL")
		{
			oprTower = addOprTower([data[i].lat,data[i].lon],'../../resources/images/tower.png',ddd);
		}
		else
		{
			oprTower = addOprTower([data[i].lat,data[i].lon],'../../resources/images/tower_susspect.png',ddd);
		}
	
		opratorTowerMarkers[data[i].id] = oprTower;
		
	}
}

var addCardCells = function(data){
	//$("#coming_cells_modal_body").html("");
	//opratorTowersLayer.clearLayers();
	//var html ="<table class='table-mob-dtect'>";
	//html +="<thead><th></th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th><th>F</th><th>G</th><th>H</th><th>I</th></thead><tbody>";
	var count = 0;
	var tech;
	for(var i in data)
	{
		tech=data[i].packet_type.toLowerCase();
		if(tech=='umts' || tech=='loc_3g'){
			//Changed now $("#coming_cells_modal_body").prepend('<div id="card_'+data[i].id+'" class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].lac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].uarfcn+'","'+data[i].freq+'","'+data[i].psc+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].inserttime+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(198,235,251); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass" src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">UARFCN '+data[i].uarfcn+'</p><p class="mobtxte">PSC '+data[i].psc+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].inserttime+'</p></div></div>');
			$("#coming_cells_modal_body").prepend('<div id="card_'+data[i].id+'" class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].lac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].uarfcn+'","'+data[i].freq+'","'+data[i].psc+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].inserttime+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(198,235,251); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass" src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">UARFCN '+data[i].uarfcn+'</p><p class="mobtxte">PSC '+data[i].psc+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].inserttime+'</p></div></div>');
		}else if(tech=='lte'){
			//Changed now $("#coming_cells_modal_body").prepend('<div id="card_'+data[i].id+'" class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].tac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].earfcn+'","'+data[i].freq+'","'+data[i].pci+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].inserttime+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(251,201,142); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass" src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">EARFCN '+data[i].earfcn+'</p><p class="mobtxte">PCI '+data[i].pci+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].inserttime+'</p></div></div>');		
			$("#coming_cells_modal_body").prepend('<div id="card_'+data[i].id+'" class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].tac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].earfcn+'","'+data[i].freq+'","'+data[i].pci+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].inserttime+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(251,201,142); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass" src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">EARFCN '+data[i].earfcn+'</p><p class="mobtxte">PCI '+data[i].pci+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].inserttime+'</p></div></div>');
		}else{
			var bsic=parseInt(data[i].bcc)+(parseInt(data[i].ncc)*8);
			//Changed now $("#coming_cells_modal_body").prepend('<div id="card_'+data[i].id+'" class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].lac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].arfcn+'","'+data[i].freq+'","'+bsic+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].inserttime+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(198,227,159); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass"  src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">ARFCN '+data[i].arfcn+'</p><p class="mobtxte">BSIC '+bsic+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].inserttime+'</p></div></div>');
			$("#coming_cells_modal_body").prepend('<div id="card_'+data[i].id+'" class="opr_net" onclick=\'getdetailsOfCell(this,"'+data[i].operators+'","'+data[i].mcc+'","'+data[i].mnc+'","'+data[i].lac+'","'+data[i].cell+'","'+data[i].packet_type+'","'+data[i].band+'","'+data[i].arfcn+'","'+data[i].freq+'","'+bsic+'","'+data[i].rssi+'","'+data[i].snr+'","'+data[i].inserttime+'","'+data[i].packet_id+'","'+data[i].index_id+'")\' style="background-color:rgb(198,227,159); cursor:pointer; padding: 5px;float:left;background-image: url("../../resources/images/user4.png");"><div class="serialNoClass"><strong><p class="mobtxta">'+(parseInt(i)+1)+'</p></strong></div><img class="imageClass"  src="../../resources/images/tower.png"><div class="cellDetailClass"><strong><p class="mobtxtb">'+data[i].operators+'('+data[i].mcc+'-'+data[i].mnc+')</p></strong><p class="mobtxtc" >'+data[i].packet_type+',BAND '+data[i].band+'</p><p class="mobtxte">ARFCN '+data[i].arfcn+'</p><p class="mobtxte">BSIC '+bsic+',Rxl '+data[i].rssi+'</p><p class="mobtxte">'+data[i].inserttime+'</p></div></div>');
		}
	}
}



var reloadOprGridReport = function(oprName,nodeName)
{
	$("#oprgrid").jqGrid('clearGridData');
    $("#oprgrid").jqGrid('setGridParam', {
        datatype: 'local',
       data: oprGridData
    })
	$("#oprgrid").trigger("reloadGrid");
}

var reloadOprHisGridReport = function(data)
{
	$("#his_oprgrid").jqGrid('clearGridData');
    $("#his_oprgrid").jqGrid('setGridParam', {
        datatype: 'local',
       data: data
    });
	$("#his_oprgrid").trigger("reloadGrid");
}

var addRealTimeRow = function(data)
{
	var dateTime =""; 
	data.date="";
	data.time="";
	jQuery("#oprgrid").addRowData( data.id, data, "first", "" );
	addOprOnMap([data]);
	addCardCells([data]);
}
var selectTower = function(id)
{
	for(var i in opratorTowerMarkers)
	{
		if(i==id){
		var position = opratorTowerMarkers[i].getLatLng();
		map.panTo(position);
		//map.flyTo([13.87992, 45.9791], 12);
		map.setZoom(10);
		}
	}
}

var removeAlreadyPresentOperator = function(key)
{
	$("#oprgrid").jqGrid("delRowData",opreator_cells[key]);
	opratorTowerMarkers[opreator_cells[key]].remove();
	$("#card_"+opreator_cells[key]).remove();
}

var events = function()
{
	$('#his_opr_tab').on('shown.bs.tab', function (e) 
	{
			if(hisOprdatatableFlag == false)
			{
				hisOprdatatable();
				hisOprdatatableFlag = true;
			}
	});
	
	// $(".date_picker").datetimepicker({
	    	//dateFormat: "yy-mm-dd",
	    	//timeFormat: 'HH:mm'
	//    });
	 $("#gen_report").click(function(){
			getReport();
			});
}

var  getAllOperations = function(){
	$.ajax({
		url:"../../Operations",
		data:{"methodName":"getAllOperations"},
		type:'post',
		success:function(data)
		{
		var allOperations=JSON.parse(data);
		$('#operationOptions').empty();
		var options='<option value="-1">All</option>';
		for(var i=0;i<allOperations.length;i++){
		options+='<option value="'+allOperations[i].id+'">'+allOperations[i].name+'</option>';
		}
		$('#operatons_list').append(options);
		}
	});
}

/***********report section*******/


var getReport = function(){
	if($("#start_date").val() == null || $("#start_date").val() == "")
    	{
    	alert("Please Fill the Start Date");
    	return;
    	}
   if($("#end_date").val() == null || $("#end_date").val() == "")
    	{
    	alert("Please Fill the End Date");
    	return;
    	}
		utcStartTime=toUtcTime($("#start_date").val());
		utcEndTime=toUtcTime($("#end_date").val());
	
		$.ajax({
			url:"../../Operations",
			//url:"../resources/test.json",
			data:{"methodName":"getCdrReports","start_date":toUtcTime($("#start_date").val()),"end_date":toUtcTime($("#end_date").val()),"operationId":$('#operatons_list').val()},
			dataType:"JSON",
			type:"post",
			success:function(data)
			{
				
				console.log(data);
				
				reloadOprHisGridReport(data);
				oprCellData = data;
				/*var cdrDetailedData = JSON.parse(cdrData);
				var nodeWiseDetailedDataTable=cdrDetailedData.nodeWiseDetailedDataTable;
				var countryWiseDetailedDataTable=cdrDetailedData.countryWiseDetailedDataTable;
				$("#detail_tab_1 .node_row").remove("");
				$("#detail_tab_1 tbody").append(nodeWiseDetailedDataTable);
				$("#detail_tab_2 .count_row").remove("");
				$("#detail_tab_2 tbody").append(countryWiseDetailedDataTable);
				try{
				if(cdrDetailedData[0].result != undefined && cdrDetailedData[0].result == "fail")
				{
					alert("Limit Exceed Please try Short time span");
				}else{
				generateCdrReport();
				}
				}catch(err){
				generateCdrReport();
				}*/						
			}
			});
	}


function downloadOperationsExcelReport()
{		
            		var reportNameString="Operation Data";
          			
					var colArray=[];
            		var cellColData = getCellColumnData();
            	    
            	    colArray[0]=cellColData;
            	    
            	    var xlData = [];
            	    xlData[0] = oprCellData;
            	    utcStartTime=toUtcTime($("#start_date").val());
            	    utcEndTime=toUtcTime($("#end_date").val());
            	    
            		tablesToExcel(colArray,xlData,['CELL_REPORT'], 'CELL Report.xls', 'Excel');
            		

}

function getCellColumnData()
{

    
		var collnames={};

		collnames['ID']='id';
		collnames['DATE']='inserttime';
		collnames['TECH']='packet_type';
		collnames['TYPE OF CELL']='cell_type';
		collnames['DL FREQ']='freq';
	    collnames['MCC']='mcc';        	    
		collnames['MNC']='mnc';
		collnames['LAC']='lac';        	    
		collnames['CELL']='cell';
		collnames['RxL']='rssi';
		collnames['OPERATOR']='operators';
		collnames['SIB']='sib_data';
		
		return collnames;
}


var tablesToExceli = (function() {
    var uri = 'data:application/vnd.ms-excel;base64,'
    , tmplWorkbookXML = '<?xml version="1.0"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">'
      + '<DocumentProperties xmlns="urn:schemas-microsoft-com:office:office"><Author>Axel Richter</Author><Created>{created}</Created></DocumentProperties>'
      + '<Styles>'
      + '<Style ss:ID="Currency"><NumberFormat ss:Format="Currency"></NumberFormat></Style>'
      + '<Style ss:ID="Date"><NumberFormat ss:Format="Medium Date"></NumberFormat></Style>'
      + '</Styles>' 
      + '{worksheets}</Workbook>'
    , tmplWorksheetXML = '<Worksheet ss:Name="{nameWS}"><Table>{rows}</Table></Worksheet>'
    , tmplCellXML = '<Cell{attributeStyleID}{attributeFormula}><Data ss:Type="{nameType}">{data}</Data></Cell>'
    , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
    , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
    return function(tables, wsnames, wbname, appname) {
      var ctx = "";
      var workbookXML = "";
      var worksheetsXML = "";
      var rowsXML = "";

      for (var i = 0; i < tables.length; i++) {
	  		var th = document.querySelectorAll('#'+tables[i]+' thead tr:first-child th');
		var cols = [].reduce.call(th, function (p, c) {
		var colspan = c.getAttribute('colspan') || 1;
		return p + +colspan;
		}, 0);
        if (!tables[i].nodeType) tables[i] = document.getElementById(tables[i]);
        for (var j = 0; j < tables[i].rows.length; j++) {
          rowsXML += '<Row>'
          for (var k = 0; k < cols; k++) {
            //var dataValue = tables[i].rows[j].cells[k].getAttribute("data-value");
			if(tables[i].rows[j].cells[k]==undefined){
            dataValue="";
			}else{
			dataValue = tables[i].rows[j].cells[k].innerHTML;
			}
            ctx = {  attributeStyleID: ''
                   , nameType: 'String'
                   , data:dataValue
                   , attributeFormula:''
                  };
            rowsXML += format(tmplCellXML, ctx);
          }
          rowsXML += '</Row>'
        }
        ctx = {rows: rowsXML, nameWS: wsnames[i] || 'Sheet' + i};
        worksheetsXML += format(tmplWorksheetXML, ctx);
        rowsXML = "";
      }

      ctx = {created: (new Date()).getTime(), worksheets: worksheetsXML};
      workbookXML = format(tmplWorkbookXML, ctx);



      var link = document.createElement("A");
      link.href = uri + base64(workbookXML);
      link.download = wbname || 'Workbook.xls';
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  })();

/********************************/

$(document).ready(function()
{
   // oprdatatable();
//	
	  $(".dateSelect").datetimepicker({
//	    	dateFormat: "yy-mm-dd",
//	    	timeFormat: "HH:mm",
	    		
		    	
	    });
	  
	//getOprGridDataReport();
	//getAllOperations();
	events();
	/*var data ={id:"sunil",date:"22:22:22",time:"11:11:11",packet_type:"123",type:true,freq:"234",band:"e_900",mcc:"123",mnc:"456",lac:"789",cell:"101112",operators:"airtell",rssi:"-110"};
	jQuery("#oprgrid").addRowData( data.id, data, "first", "" );
	data ={id:"ANIL",date:"22:22:22",time:"11:11:11",packet_type:"123",type:false,freq:"234",band:"e_900",mcc:"123",mnc:"456",lac:"789",cell:"101112",operators:"airtell",rssi:"-110"};
	jQuery("#oprgrid").addRowData( data.id, data, "first", "" );*/
});
