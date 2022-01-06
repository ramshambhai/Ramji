var currGroup="";
$(document).ready(function(){
var iFrames = $('iframe');
      
    	
getAllBtsInfo();
});

var dirpath= "../../";

var getAllBtsInfo = function()
{
	$.ajax({
		url:dirPath+"Operations",
		data:{"methodName":"getAllBtsInfo"},
		type:"post",
		success:function(data)
		{
			data = eval(data);
			console.log(data);
			
			
			var appType=[];
			var groupType=[];
			var tempGroupType=[];
			var rootObj={};
			rootObj.data="Application";
			var rootAttr={};
			rootAttr.rel="1stLevel";
			rootAttr.id="default";
			rootObj.attr=rootAttr;
			rootObj.state="open";
			var rootChildArray=[];
			for(var i=0;i<data.length;i++){
			if((appType.indexOf(data[i].application_name)== -1)){
			appType.push(data[i].application_name);
			}
			if((tempGroupType.indexOf(data[i].grp_name)== -1 && data[i].grp_name!=null)){
			var tempObj={};
			tempObj.application_name=data[i].application_name;
			tempObj.grp_name=data[i].grp_name;
			groupType.push(tempObj);
			tempGroupType.push(data[i].grp_name);
			}
			}
			
			for(var l=0;l<appType.length;l++){
			var topObj={};
			topObj.data=appType[l];
			var topAttr={};
			topAttr.rel="2ndLevel";
			topAttr.id="tree_"+appType[l];
			topObj.attr=topAttr;
			topObj.state="closed";
			var topChildArray=[];
			for(var m=0;m<groupType.length;m++){
			if(appType[l]==groupType[m].application_name){
			var groupObj={};
			groupObj.data=groupType[m].grp_name;
			var groupAttr={};
			groupAttr.rel="3rdLevel";
			groupAttr.id="tree_"+groupType[m].grp_name;
			groupObj.attr=groupAttr;
			groupObj.state="closed";
			var groupChildArray=[];
			for(var n=0;n<data.length;n++){
			if(data[n].application_name==appType[l] && data[n].grp_name==groupType[m].grp_name){
			var lastObj={};
			lastObj.data=data[n].ip;
			lastAttr={};
			lastAttr.rel="4thLevel";
			lastAttr.id=data[n].b_id;
			lastObj.attr=lastAttr;
			lastObj.state="closed";
			groupChildArray.push(lastObj);
			}
			groupObj.children=groupChildArray;
			}
			topChildArray.push(groupObj);
			topObj.children=topChildArray;
			}
			}
			rootChildArray.push(topObj);
			}
			rootObj.children=rootChildArray;
			var completeArray=[];
			completeArray.push(rootObj);
			var treeObj={};
			treeObj.data=completeArray;
			loadJSTree(treeObj);
			//createSelectListForGroups(data);
		}	
	});
}

function findType(appType)
{
for(var i=0;i<appType.length;i++){

}
}

function loadJSTree(treeObj){
//var sufiData=getSufiData();

 $("#leftTree").jstree({
                    "json_data" :treeObj,
					"contextmenu":{
    "items": function(node) {
	                 var list = node.parents('ul');
                 var nodeName = $(node).data('name');
                 var nodeTxt = $(node).text().trim();
                 var parentNode = ($(list[0]).prev('a')).text().trim();
                 var grandParentNode = ($(list[1]).prev('a')).text().trim();
				 if(parentNode=="" || grandParentNode==""){
				 return false;
				 }
				 var labelToApply="";;
				 var currentNodeText=$('#leftTree .jstree-clicked').text().trim();
				 if((parentNode=='3G' || parentNode=='3G-Network Scanner' || parentNode=='2G-Geo Locator' || parentNode=='2G-Mobile Locator') && grandParentNode=='Application'){
				 labelToApply="Remove Group";
				 }else{
				 labelToApply="Remove BTS";
				 }

        return {
            deleteItem : {
                 "label" : labelToApply,
                 "action" : function(obj) { 
                 deleteNode(obj.context.text.trim(),parentNode,grandParentNode,obj,this);		 
				 }
            },
            lockCell : {
                "label" : "Lock",
                "action" : function(obj) { 
                	console.log(obj.context.text.trim());
                	setLockUnlockOperation(obj.context.text.trim(),1)
				 }
           },
           unLockCell : {
               "label" : "Unlock",
               "action" : function(obj) { 
               	console.log(obj.context.text.trim());
               	setLockUnlockOperation(obj.context.text.trim(),2)
				 }
          }
        };
    }
},
         "types": {
             'types': {
                 '1stLevel': {
                     'icon': {
                         'image': dirPath+"resources/images/RootContainer.gif"
                     }
                 },
                 "2ndLevel": {
                     "icon": {
                         "image": dirPath+"resources/images/job.gif"
                     }
                 },
                 "3rdLevel": {
                     'icon': {
                         'image': dirPath+"resources/images/ne.gif"
                     }
                 },
				 "4thLevel": {
                     "icon": {
                         "image": dirPath+"resources/images/ViewNode.gif"
                     }
                 }
             }
         },
                    "plugins" : ["themes", "json_data", "ui", "contextmenu", "crrm", "types","sort","state"]
                }).bind("select_node.jstree", function(e, data)
				{
				console.log("select node jstree");
				//var cuid=$('#leftTree').jstree('get_selected').attr('id');
				//var cuattr=$('#leftTree').jstree('get_selected').attr('rel');
				    var leftTreeSelectedNode = data.rslt.obj;
                	var list = data.rslt.obj.parents('ul');
					var parentNode = ($(list[0]).prev('a')).text();
					parentNode=parentNode.trim();
					var grandParentNode = ($(list[1]).prev('a')).text();
					grandParentNode=grandParentNode.trim();
					//var currentNode=$("#leftTree").jstree("get_selected").text();
					var currentNode=$('#leftTree .jstree-clicked').text();
					currentNode=currentNode.trim();
					console.log('curr is :'+currentNode);
					var currentNodeLevel=$('#leftTree').jstree('get_selected').attr('rel');
					if(parentNode=='Application' && currentNode=='2G-Mobile Locator'){
					$('#applicationType').attr('src','../mlstatus.jsp');
					}else if(parentNode=='Application' && currentNode=='2G-Geo Locator'){
					$('#applicationType').attr('src','../geoIndex.jsp');
					}else if(parentNode=='Application' && currentNode=='3G'){
					$('#applicationType').attr('src','../3g/index.jsp');
					}else if(parentNode=='Application' && currentNode=='3G-Network Scanner'){
					$('#applicationType').attr('src','../netscan/index.jsp');
					}
					if(grandParentNode=="Application" && parentNode=="3G" && currentNodeLevel=="3rdLevel"){
					$('#applicationType').attr('src','../3g/GroupIndex.jsp');
					}else if(grandParentNode=="3G" && currentNodeLevel=="4thLevel"){
					$('#applicationType').attr('src','../3g/NodeIndex.jsp');
					}else if((grandParentNode=="Application" && parentNode=="3G-Network Scanner") || (grandParentNode=="3G-Network Scanner")){
					$('#applicationType').attr('src','../netscan/index.jsp');
					}
					
					if(currentNode=='Application'){
					$('#applicationType').attr('src','../Application.jsp');
					}
					
				    if(grandParentNode=="2G-Geo Locator" && currentNodeLevel=="4thLevel"){
					$('#applicationType').attr('src','../geoIndex.jsp');
					}
					
					if(grandParentNode=="2G-Mobile Locator" && currentNodeLevel=="4thLevel"){
					setSelectedNibIp(currentNode,'../plmnmanagment.jsp');
					}

				});
				
	    $('#leftTree').bind("loaded.jstree", function (event, data) {
		
		$(this).jstree("open_all");
					/*		        	$(data.rslt.obj).find('li').find.each(function (i);
	{console.log(data);
       		data.inst.open_node($(this));
	});*/
    });
	
	     $("#leftTree").delegate("a", "dblclick", function (e) {
         $("#leftTree").jstree("toggle_node", this);
     });
	$('#leftTree li ul').css("color","red");
	$('#leftTree li a').css("background-color","green");

}

function setSelectedNibIp(selectedNibIp,targetSrc){
	$.ajax({
		url:dirPath+"Operations",
		data:{"methodName":"setSelectedNibIp","selectedNibIp":selectedNibIp},
		type:"post",
		//data:{"username":$('#username').val(),"password":$("#password").val()},
		success:function(data)
		{
		if(data=="successful"){
        console.log('nib ip is :'+selectedNibIp);
		$('#applicationType').attr('src',targetSrc);
		//var iFrameDOM = $("#applicationType").contents();
		//iFrameDOM.find("#nib_ip_val_head").text(selectedNibIp);
		}		
		}
	});	
}

function getSufiData(){
	$.ajax({
		url:dirPath+"AuthenticateUser",
		type:"get",
		//data:{"username":$('#username').val(),"password":$("#password").val()},
		success:function(data)
		{
			
		}
		
	});	
}

function deleteNode(currentNodeText,parentNode,grandParentNode,obj,remObj){
var currentNodeLevel=$(obj).attr('rel');
var currentNodeId=$(obj).attr('id');
if(currentNodeLevel=='4thLevel'){
	data={"btsIp":currentNodeText,"id":currentNodeId};
	$.ajax({
		url:dirPath+"service/3g/removeBts",
		data:data,
		type:'post',
		success:function(data)
		{
		remObj.remove(obj);
		}
	});
}

if(currentNodeLevel=='3rdLevel'){
	data={groupName:currentNodeText};
	$.ajax({
		url:dirPath+"service/3g/removeBtsOfAGroup",
		data:data,
		type:'post',
		success:function(data)
		{
		remObj.remove(obj);
		}
	});
}
}

function iResize() {
    	
    		for (var i = 0, j = iFrames.length; i < j; i++) {
    		  iFrames[i].style.height = iFrames[i].contentWindow.document.body.offsetHeight + 'px';}
    	    }

function setLockUnlockOperation(ip,flag){
	var type = flag==2?"Unlocked":"Locked";
	
	$.ajax({
	url:dirPath+"service/2g/lockUnlockCell",
	data:{flag:flag,"ip":ip},
	type:'post',
	success:function(data)
	{
		if((data.STATUS.indexOf("ERR(")>-1))
		{
			alert("Error  while performing Lock/Unlock");
		}
		else
		{
			alert(type+" Successfully");
		}
	}
});
}