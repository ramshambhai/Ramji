<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='ConHeader.jsp' />
<script type="text/javascript" src="../resources/js/jquery-1.11.3.min.js" ></script>
<script type="text/javascript" src="../resources/lib/jstree/jquery.jstree.js"></script>
<script type="text/javascript" src="../resources/js/3g/3g_dashboard.js" ></script>
<script type="text/javascript" src="../resources/js/consolidation.js" ></script>

<script>
$(document).ready(function(){
});
</script>
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
</style>
<body style="display: block;margin: 0px;">
<!--<div>Type</div>
<button id="first">click</button>-->

<div class="boxes" style="margin-top: 0px;margin-right :auto;margin-bottom :10px;margin-left :0px;width:220px;">
<div id="treeContainer">
<div id="leftTree" style="min-width: 220px;"></div>
</div>
</div>
<div class="boxes">
<iframe id="applicationType" src="Application.jsp" frameborder='0' height='640px' width='1120px' scrolling='yes' ></iframe>
</div>	
</body>
</html>