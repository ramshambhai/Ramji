/**
 * 
 */


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
					{ label: 'oprid', name: 'id', width: 75,search: false,hidden:true },
					{ label: 'Name', name: 'oprid', width: 75,search: false,hidden:true },
					{ label: 'Location', name: 'name', width: 150,search: true },					
                    { label: 'BTS', name: 'bts', width: 150,search: true },
                    { label: 'A-Type', name: 'atype', width: 150,search: true },
                    { label: 'A-Gain', name: 'again', width: 150,search: true },
                    { label: 'A-Height', name: 'aheight', width: 150,search: true },
                    { label: 'A-Elevation', name: 'aelevation', width: 150,search: true },
                    { label: 'A-Direction', name: 'adirection', width: 150,search: true },
                    { label: 'Terrain Type', name: 'ttype', width: 150,search: true }
                    
                ],
				viewrecords: true,
                width: 1300,				
                height: 250,
                rowNum: 20,
                pager: "#jqGridPager"
            });
            jQuery("#jqGrid").jqGrid('navGrid','#jqGridPager',{edit:false,add:false,del:false,refresh:false});
            getCurrentActiveOperation();
        });
        var getCurrentActiveOperation = function()
        {
        
        		$.ajax
        		({
        			url:"../Operations",
        			data:{"methodName":"getCurrentActiveOperation","startTime":toUtcTime($("#startTime").val()),"endTime":toUtcTime($("#endTime").val())},
        			type:"post",
        			success:function(cdrData)
        			{
        				serverData = JSON.parse(cdrData);
        				$("#jqGrid").jqGrid('setGridParam', 
        				{
        			        datatype: 'local',
        			        data: serverData
        			    });
        				$("#jqGrid").trigger("reloadGrid");
        				var grid = jQuery("#jqGrid"),
        			    ids = grid.jqGrid("getDataIDs");
        			if(ids && ids.length > 0)
        			    grid.jqGrid("setSelection", ids[0]);
        			}
        		});
        }

function saveCdrOperations()
{
	
	requestData=
	{
			"oname":$("#oname").val(),
			"oaddr":$("#oaddr").val(),
			"methodName":"saveCdrOperations"
	};
	
	$.ajax({
		url:"../Operations",
		data:requestData,
		type:'post',
		success:function(data)
		{
			if(data == "true")
			{
				alert("saved");
			}
			else
			{
				alert("failed");
			}
			location.reload();
		}
	});
}

function saveBcdrOperations()
{
	
	resdata=
	{
			"methodName":"saveBcdrOperations",
			"baoprname":$("#baoprname").val(),
			"baopbts":$("#baopbts").val(),
			"batype":$("#batype").val(),
			"bagain":$("#bagain").val(),
			"baheight":$("#baheight").val(),
			"baelevation":$("#baelevation").val(),
			"badirection":$("#badirection").val(),
			"ttype":$("#ttype").val()			
	};
	
	$.ajax({
		url:"../Operations",
		data:resdata,
		type:'post',
		success:function(data)
		{
			if(data == "true")
			{
				alert("saved");
			}
			else
			{
				alert("failed");
			}
			location.reload();
			
		}
	});
}

function getLocOprations()
{	reqdata={"methodName":"getLocOprations"};
	$.ajax({
		url:"../Operations",
		data:reqdata,
		type:'post',
		success:function(data)
		{
			//alert(data);
			data = jQuery.parseJSON(data);
			createSelectList(data);
		}
	});
}
function getLocBTStype()
{
	
	reqdata={"methodName":"getLocBTStype"};
	$.ajax({
		url:"../Operations",
		data:reqdata,
		type:'post',
		success:function(data)
		{
			//alert(data);
			data = jQuery.parseJSON(data);
			createSelectListBaopbts(data);
		}
	});
}

function createSelectList(data)
{
	var html="";
	for(var i in data)
	{
		html+="<option value="+data[i].id+">"+data[i].name+"</option>";
	}	
	$("#baoprname").append(html);
}

function createSelectListBaopbts(data)
{
	var html="";
	for(var i in data)
	{
		html+="<option value="+data[i].id+">"+data[i].name+"</option>";
	}	
	$("#baopbts").append(html);
}



var updateNib = function(data)
{
	$("#nibs").html("");
	$("#nibs").append("<option value=-1>select Nib</option>");
	for(var i in data)
	{
		$("#nibs").append("<option value="+data[i]+">"+data[i]+"</option>");
	}
}

var eventRegister = function()
{
	$("#saveOpr").click(function(){
		if(($("#oname").val() == "") || ($("#oaddr").val() == "") )
		{	
			alert("Please Provide Name and location");
			return;
		}
		else
		{
			saveCdrOperations();
		}
	});
	
	$("#savebaopr").click(function()
	{	
		var a =true;
		$("#btsopr input").each(function()
		{
			if($(this).val() =="")
			{
				
				a=false;			
			}
		});
		if(!a)
		{
			alert("Please Provide all the fields");
			return false;
		}
		else
		{
			saveBcdrOperations($("#nibs").val());
		}

	});
	
	
	$("#deleteOpr").click(function(){
		
		var a = confirm("Are you sure!");
		if(a)
		{
			truncateDb();
		}
		
		
	});
	
}


$(document).ready(function()
{	
	eventRegister();
	getLocBTStype();
	getLocOprations();
});

function truncateDb()
{
	
	data={"methodName":"truncateDbOpr"};
	$.ajax({
		url:"../Operations",
		data:data,
		type:'post',
		success:function(data)
		{
			location.reload();
			
		}
	});
}


