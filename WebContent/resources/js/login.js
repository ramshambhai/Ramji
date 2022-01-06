function authenticateUser()
{
	$.ajax({
		url:"../AuthenticateUser",
		type:"post",
		data:{"username":$('#username').val(),"password":$("#password").val()},
		success:function(data)
		{
			if(data == "1")
			{
					window.location="dashboard/maps.jsp";
			}
			else
			{
				alert("User name or password is Incorrect Please check");
			}
			
		}
		
	});	
}

$(document).ready(function(){
$("#signin").click(function(){
	authenticateUser();
})
});

$(document).keydown(function(event) 
{
	if (event.keyCode == 13) 
	{ 
		$('#signin').trigger("click");
	}		
});