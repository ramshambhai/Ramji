<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />


<style>
 		.boxes {
    		transition: 0.3s;
    		float:left;
    		margin: 0 auto 10px 10px;
    		color:white;
		}
		
#leftTree{
background-color: white;
    height: 640px;
    overflow: auto;
    position: absolute;
    display: block;
    visibility: visible;
}
#leftTree li[rel="1stLevel"] a{
color:black;
}
#leftTree li[rel="2ndLevel"] a{
color:red;
}
#leftTree li[rel="3rdLevel"] a{
color:green;
}
#leftTree li[rel="4thLevel"] a{
color:blue;
}
#configurationMenu{
font-weight:900;
text-decoration:underline;
}
</style>

<div class="wrapper">
<jsp:include page='sidebar.jsp' />
<div class="main-panel">
        <jsp:include page='nav.jsp' />

        <div class="content">
            <div class="container-fluid">
			<div class="row">
				<div class="col-lg-12 col-xs-21">
	<iframe id="applicationType" frameborder="0" scrolling="no"  onload="resizeIframe(this)" src="../Application.jsp" frameborder='0' style="height:800px;"></iframe>
				</div>
			</div>
			</div>
			</div>
                  
<script>
  function resizeIframe(obj) {
    obj.style.height = obj.contentWindow.document.body.scrollHeight+50 + 'px';
	obj.style.width = obj.contentWindow.document.body.scrollWidth + 'px';
  }
</script>
<jsp:include page='fotter.jsp' />