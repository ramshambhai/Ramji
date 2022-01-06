var globalOprList=[];
var globalProfileRowCount=0;
var dirPath ="../../";
var globalAntennaProfileList=[];
var globalTargetList=[];
var globalDeviceName="";
var globalDeviceIp="";
var globalPLMNList=[];
var globalBtsDevices=[];
var globalBtsDevicesStatus=[];
var statusCount={};
statusCount.run=0;
statusCount.wait=0;
statusCount.reachable=0;
statusCount.down=0;
var color = {};
color.fontColor = "white";
color.backgroundColor = "white";
var globalNetscannerIp = "";
var globalStatus={};
globalStatus.color =color;
globalStatus.count =statusCount;
var globalAntennaAngleOffset='';
var globalScanningAntenna=[];





$(document).ready(function(){
	getAllDeplConfigs();
});

function getAllDeplConfigs(){
	$.ajax({
		url:"../../service/common/getalldeplconfigs",
		data:{"methodName":"getAllDataTypes"},
		type:'post',
		success:function(data)
		{
			$('#deplConfig tbody').html('');
			var rows='';
			for(var i=0;i<data.length;i++){
				if(data[i].status=="t"){
				rows+='<tr>'+
					'<td><label style="font-size: 15px;">'+data[i].display_name+'</label></td>';
				rows+='<td>'+
				'<select type="text" id="configInput'+data[i].id+'" style="font-weight: 500px;width: 150px;">';
				if(data[i].value=='Standalone Mode without Rx Controller'){
					rows+='<option value="Standalone Mode without Rx Controller" selected>Standalone Mode without Rx Controller</option>'+
							'<option value="Standalone Mode with Rx Controller">Standalone Mode with Rx Controller</option>'+
							'<option value="Integrated Mode">Integrated Mode</option>';
				}else if(data[i].value=='Standalone Mode with Rx Controller'){
					rows+='<option value="Standalone Mode without Rx Controller">Standalone Mode without Rx Controller</option>'+
					'<option value="Standalone Mode with Rx Controller" selected>Standalone Mode with Rx Controller</option>'+
					'<option value="Integrated Mode">Integrated Mode</option>';
				}else{
					rows+='<option value="Standalone Mode without Rx Controller">Standalone Mode without Rx Controller</option>'+
					'<option value="Standalone Mode with Rx Controller">Standalone Mode with Rx Controller</option>'+
					'<option value="Integrated Mode" selected>Integrated Mode</option>';
				}
				rows+='</select></td>';
				rows+='<td><button class="btn btn-default"   id="configButton'+data[i].id+'" onclick="updateDeplConfig(\''+data[i].id+'\')" value="'+data[i].key+'">Update</button></td>'+
					'</tr>';
				}
			}
			/*$('#trackTimeLabel').text(data[0].display_name);
			$('#trackTimeConfig').val(data[0].value);
			$('#scannerPeriodicityLabel').text(data[1].display_name);
			$('#scannerPeriodicityConfig').val(data[1].value);
			$('#queueStatusLabel').text(data[2].display_name);
			$('#queueStatusConfig').val(data[2].value);*/
			$('#deplConfig tbody').html(rows);
		}
	});
}

function updateDeplConfig(sysConfigId)
{
	var name=$('#configButton'+sysConfigId).val();
	var value=$('#configInput'+sysConfigId).val();
	var postData={"name":name,"value":value};
	$.ajax({
		url:"../../service/common/updatedeplconfig",
		data:postData,
		type:'post',
		success:function(data)
		{
			if(data.result=='success'){
				alert('Deployment Configurations Updated Successfully');
			}else{
				alert('Problem in updating Deployment Configurations');
			}
		}
	});
}
