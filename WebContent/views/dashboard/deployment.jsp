<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:include page='header.jsp' />
 <% String ip= "'"+request.getServerName()+"'"; %>
 <style>
#myAddPopup{
  background:while;
  width:400px;
  border-radius:6px;
  padding:0px 0px 10px 0px;
  border: #2980b9 4px solid; 
}

.input{
  background:#ecf0f1;
  border-radius:4px;
  border: #ccc 1px solid;
  padding: 8px;
  width:250px;
  font-size:1em;
  border-bottom: #ccc 2px solid;
  color:black;
  margin-top:0px; 
}

label{
margin-left: 10px;
}



.btn:hover{
  /*background:#3594D2;*/  
}

#configurationMenu{
	font-weight:900;
	text-decoration:underline;
}

.addAntennaInput{
	width: 80px;
}

.editAntennaInput{
	width: 60px;
}

.table-target th{
	text-align: center;
}

.opr_button
{
	margin-right: 5px;
    margin-left: 5px;
    /* width: 10px; */
    padding-top: 1px;
    padding-bottom: 1px;
    padding-left: 5px;
    padding-right: 5px;
}

</style>
<script>
var ipAddress =<%= ip %>;
</script>
<div class="wrapper" style="background: white;">
<jsp:include page='nav.jsp' />
<div id="container">
<div id="message_box">
	<div id="myAddPopup" style="max-width: 350px;margin-top: 12%;margin-left: 40%;clear: left;display: none;">
		<div id="hiddenApplicationId" style="display: none;"></div>	   
	<br><br>
			     <div id="addOperationDiv" >			 
			   <table class="common_tables" style="text-align:center;">
			   <tr>
			   <td colspan="4"><h4 align="center" id="reportType" style="margin-top: -15px;margin-left: 10%;">Add Operation</h4></td>
			   </tr>
                 <tr> 
			      <td colspan="2"><label>Name</label></td>
			      <td colspan="2"><input type="text" id="oprName" class="input"></input></td> 
			     </tr>
		         <tr> 				 
			      <td colspan="2"><label>Note</label></td>
			      <td colspan="2"><input type="text" id="oprNote" class="input"></input></td>
			     </tr>
				  <tr>			  
				  <td colspan="4"><input class="btn btn-default" type="button" id="addOpr" value="Add"style="width: 100px;margin-left:68%;"></input></td>				  
				  </tr>				 
			 </table>
              </div>   
			  <div id="loadingBox" style="display: none;text-align:center;">
				<img src="../../resources/images/Loading_icon.gif"  style="margin-left: 0px;margin-top: -40px;width: 100%;" />	
			  </div>			  
	  </div> 
					
					
					
				<div style="/*max-height: 200px;overflow-y: auto;overflow-x:hidden;*/margin-left:23px;margin-right:25px; background:white">
				
				<table border=1 id="deplConfig" style="margin: 0 auto;" class="table table-bordered">
				<thead>
							<tr>
							<th colspan="9" style="text-align: center;">Deployment Configurations</th>    
							</tr>
							<tr>
								<th>Properties</th>
								<th>Value</th>
								<th></th>
							</tr>
			    </thead>
			    <tbody>
				</tbody>							
				</table>
						</div>
				</div>
				</div>
				<!--    <div id="addDeviceLoadingBox" style="display: none;text-align: center;">
				<img  style="width: 100%;height: 100%;" src="../../resources/images/p_load.gif" />
				</div> -->
				</div>
				</div>
<jsp:include page='modal_footer.jsp' />
</body>
<link rel="stylesheet" type="text/css" href="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.css"/>
<script type="text/javascript" src="../../resources/lib/jquer_ui_1_11_4/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../../resources/lib/assets/css/fontawesome-all.min.css">
<link href="../../resources/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="../../resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/operation.css" />
<link rel="stylesheet" type="text/css" href="../../resources/css/main.css" />
<script type="text/javascript" src="../../resources/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../../resources/lib/jquery.battatech.excelexport.js"></script>
<script type="text/javascript" src="../../resources/lib/export.js"></script>
<script type="text/javascript" src="../../resources/js/deployment.js"></script>
<style>
#displayTargetDiv
{
	height:150px;
	overflow-y:auto;
	overflow-x:hidden;
}

#displayAntennaProfileDiv
{
	width: 1220px;
	/*height:180px;*/
	overflow-y:auto;
	overflow-x:hidden;
}

/*#container {
    /*height: 542px;*/
        height: auto;
	background: #66615B;
}*/

#message_box, #operationAndDevicesDiv {
    display: table;
    margin: 0 auto;
    background: #66615B;
    padding: 10px 10px 10px 10px;
    width: 98%;
    height: auto; 
}

#message_box, #operationAndDevicesDiv {
    padding: 0px 10px 10px 10px; 
}

.table-fixed{
/*  width: 100%;
  background-color: #f3f3f3;
  tbody{
    height:200px;
    overflow-y:auto;
    width: 100%;
    }
  thead,tbody,tr,td,th{
    display:block;
  }
  tbody{
    td{
      float:left;
    }
  }
  thead {
    tr{
      th{
        float:left;
       background-color: #f39c12;
       border-color:#e67e22;
      }
    }
  }*/	
}

.statusCard {
   
    width: 229px;
	margin-left:25px;
   
}
html{
	overflow-y:auto;
}

	b{
	font-size: 18px;
	}
	
.spanStyle{
	font-size: 14px;
	margin-left: 25px;
	float:right;
	cursor:pointer;
}

.spanStyleTbody{
display: none;
}

.holder{
height: 39px;
border-radius: 12px;
}

.largeHeading{
 color: black;
 float:left;
 padding-left: 10px;
 padding-top: 8px;
}

.dotClass{
	float:right;
	background-color: #0067b1;
	border-radius: 50%;
	height: 25px;
	width: 25px;
	margin-right: 10px;
	margin-top: 6px;
    padding-left: 9px;
    padding-top: 4px;
    color: white;
}

#list_table_operations tr td
{
  word-break: break-all;
  }
  
  
  #addNewOprDiv .common_tables input {
    width: 207px;
}

.antenna_label_class tr td label{
 /*font-size: 12px;*/
}

.parameter_units_class{
	font-size: 11px;
}

.labelClass{
	font-size: 12px;
}

.btn-operation{
	margin-top: 4px;
	margin-left: 4px;
}

/*#purgeData{
	display: none;
}

#addCell{
	display: none;
}*/

</style>
<style>
		<%= session.getAttribute("themeCss")%>
</style>
<style>
.table-bordered thead{
	background-color: #505b65
}
</style>
<style>
body{
	overflow: hidden;
}
</style>
</html>