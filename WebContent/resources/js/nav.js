$(document).ready(function(){
getLocatorVersion();
});

var getLocatorVersion = function()
{
	$.ajax({
		url:"../../service/common/getLocatorVersion",
		type:"post",
		success:function(data)
		{
			$('#locatorVersion').html(data);
			bandFreqMap = data
			console.log(data);
			
		}
		
	});
}