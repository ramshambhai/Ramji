var dirPath ="../";
$(document).ready(function(){
getBtsInfoPerApplication();
});

var getBtsInfoPerApplication = function()
{	
		var currentNodeLevel=window.parent.$('#leftTree').jstree('get_selected').attr('rel');
		var currentNodeText=window.parent.$('#leftTree .jstree-clicked').text().trim();
	$.ajax({
		url:dirPath+"Operations",
		type:'post',
		data:{methodName:"getBtsInfoPerApplication",applicationName:currentNodeText},
		success:function(data)
		{
			data=eval(data);
			createRowsForBtsInfoPerApplication(data);
			
			
		}
	});
}

var createRowsForBtsInfoPerApplication = function(data)
{
$("#tab_status_body").html('');
var options='';
for(var i=0;i<data.length;i++){
options+='<tr><td style="padding: 8px;">'+data[i].grp_name+'</td><td style="padding: 8px;">'+data[i].ip+'</td style="padding: 8px;"><td>2G</td></tr>';
}
$("#tab_status_body").append(options);
}