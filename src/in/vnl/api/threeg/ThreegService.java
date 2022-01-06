package in.vnl.api.threeg;

import in.vnl.api.netscan.NetscanOperations;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.livescreens.ConfigOprServer;

import java.util.HashMap;
import java.util.LinkedHashMap;
//import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.json.*;

import javax.ws.rs.FormParam;
//import java.util.Calendar;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import javax.servlet.http.HttpServletRequest;



@Path("/3g")
public class ThreegService 
{
   static Logger fileLogger = Logger.getLogger("file");
   
   @GET
   @Path("/testService")
   @Produces(MediaType.APPLICATION_JSON)
   public Response activateBP(){
	   
	   LinkedHashMap<String,String> rs = new LinkedHashMap<String,String>();
	   rs.put("result", "success");
	   rs.put("message", "ok workinng");
	   return Response.status(201).entity(rs).build();
   }
   
   
   @GET
   @Path("/test")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBtsNetworktype(){
	   
	   LinkedHashMap<String,String> rs = new LinkedHashMap<String,String>();
	   rs.put("result", "success");
	   rs.put("message", "ok workinng");
	   return Response.status(201).entity(rs).build();
   }
   
   
   @GET
   @Path("/updatestatusofallbts")
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateStatusOfAllBts()
   {	   
	   new ThreegOperations().updateStatusOfAllBts();
	   return Response.status(201).entity("").build();
   } 
   
   @POST
   @Path("/opr")
   @Produces(MediaType.APPLICATION_JSON)
   public Response reciveFromDevice(@QueryParam("CMD_TYPE") String cmdType,@QueryParam("SYSTEM_CODE") String systemCode,@QueryParam("SYSTEM_ID") String systemId,@Context HttpServletRequest req,String data)
   {   
	   LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
	   param.put("cmdType", cmdType);
	   param.put("systemCode", systemCode);
	   param.put("systemId", systemId);
	   param.put("data", data);
	   
	   new Common().auditLog(new Common().convertToMapFor2g("node",param.toString(),"R"));
	   
	   return new ThreegOperations().executeActions(param);
   }
   
   @POST
   @Path("/clientopr")
   @Produces(MediaType.APPLICATION_JSON)
   public Response reciveFromClient(@FormParam("cmdType") String cmdType,@FormParam("systemCode") String systemCode,@FormParam("systemId") String systemId,@FormParam("systemIp") String systemIp,@FormParam("id") String id,@Context HttpServletRequest req,@FormParam("data") String data)
   {   
	   LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
	   param.put("cmdType", cmdType);
	   param.put("systemCode", systemCode);
	   param.put("systemIP", systemIp);
	   param.put("systemId", systemId);
	   param.put("id", id);
	   param.put("data", data);
	   
	   new Common().auditLog(new Common().convertToMapFor2g("omc",param.toString(),"S")); 
	   
	   return new ThreegOperations().executeActions(param);
   }
   
   
   @POST
   @Path("/addBts")
   @Produces(MediaType.APPLICATION_JSON)
   public Response addBts(@Context HttpServletRequest req,@FormParam("btsIp") String btsIp, @FormParam("btsType") String btsType,@FormParam("dtype") String dtype)
   {
	        fileLogger.info("Inside Function : addBts");
	   		fileLogger.debug("all data is :"+btsIp+" "+btsType);
			Common co = new Common();			
			String query = "insert into btsmaster(ip,typeid,devicetypeid) values('"+btsIp+"','"+btsType+"',"+dtype+")";
			co.executeDLOperation(query);
			HashMap<String,String> rs = new HashMap<String,String>();
			rs.put("result", "sucsses");
			 fileLogger.info("Exit Function : addBts");
            return Response.status(201).entity(rs).build();
    }
   
   
   @POST
   @Path("/add_3G")
   @Produces(MediaType.APPLICATION_JSON)
   public Response add_3G(@Context HttpServletRequest req,@FormParam("of_ip") String of_ip, @FormParam("spf_ip") String spf_ip,@FormParam("ppf_ip") String ppf_ip)
   {
	   		//fileLogger.debug("all data is :"+of_ip+" "+of_ip);
			Common co = new Common();			
			String query = "insert into threeg_ip(of_ip,spf_ip,ppf_ip) values('"+of_ip+"','"+spf_ip+"','"+ppf_ip+"')";
			co.executeDLOperation(query);
			HashMap<String,String> rs = new HashMap<String,String>();
			rs.put("result", "success");
            return Response.status(201).entity(rs).build();
    }
   
   
   @POST
   @Path("/add_2G_mob_loc")
   @Produces(MediaType.APPLICATION_JSON)
   public Response  add_2G_mob_loc(@Context HttpServletRequest req,@FormParam("ip_2G_mob_loc") String ip_2G_mob_loc)
   {
	   		//fileLogger.debug("all data is :"+of_ip+" "+of_ip);
			Common co = new Common();			
			String query = "insert into twog_mob_ip(ip_2G_mob_loc) values('"+ip_2G_mob_loc+"')";
			co.executeDLOperation(query);
			HashMap<String,String> rs = new HashMap<String,String>();
			rs.put("result", "success");
            return Response.status(201).entity(rs).build();
    }
   
   
 
   
   @POST
   @Path("/add_netscan")
   @Produces(MediaType.APPLICATION_JSON)
    public Response addnetscan(@Context HttpServletRequest req,@FormParam("application_id") String application_id, @FormParam("group_name") String group_name,@FormParam("ip") String ip)
   {
	   int groupId=createGroup(group_name);
	   int nid=getIdpostgre3G();
	   int did2G=deviceId2G();
	   Common co = new Common();   
	   String query = "insert into btsmaster(application_id,ip,grp_id,typeid,devicetypeid) values('"+application_id+"','"+ip+"','"+groupId+"','"+nid+"','"+did2G+"')";
	   co.executeDLOperation(query);
	   HashMap<String,String> rs = new HashMap<String,String>();
	   rs.put("result", "success");
       return Response.status(201).entity(rs).build();
	   
    }
   
   @POST
   @Path("/add_geo")
   @Produces(MediaType.APPLICATION_JSON)
    public Response geo(@Context HttpServletRequest req,@FormParam("application_id") String application_id, @FormParam("group_name") String group_name,@FormParam("ip") String ip)
   {
	   int groupId=createGroup(group_name);
	   int nid=getIdpostgre2G();
	   int didgeo=deviceIdgeo();
	   Common co = new Common();   
	   String query = "insert into btsmaster(application_id,ip,grp_id,typeid,devicetypeid) values('"+application_id+"','"+ip+"','"+groupId+"','"+nid+"','"+didgeo+"')";
	   co.executeDLOperation(query);
	   HashMap<String,String> rs = new HashMap<String,String>();
		rs.put("result", "success");
       return Response.status(201).entity(rs).build();
	   
    }
   
   @POST
   @Path("/add_mob")
   @Produces(MediaType.APPLICATION_JSON)
    public Response mob(@Context HttpServletRequest req,@FormParam("application_id") String application_id, @FormParam("group_name") String group_name,@FormParam("ip") String ip)
   {
	   int groupId=createGroup(group_name);
	   int nid=getIdpostgre2G();
	   int didmob=deviceIdmob();
	   Common co = new Common();   
	   String query = "insert into btsmaster(application_id,ip,grp_id,typeid,devicetypeid) values('"+application_id+"','"+ip+"','"+groupId+"','"+nid+"','"+didmob+"')";
	   co.executeDLOperation(query);
	   HashMap<String,String> rs = new HashMap<String,String>();
		rs.put("result", "success");
       return Response.status(201).entity(rs).build();
	   
    }
   
 /*  @POST
   @Path("/addsufi")
   @Produces(MediaType.APPLICATION_JSON)
   public Response add_3G(@Context HttpServletRequest req,@FormParam("application_id") String application_id, @FormParam("group_name") String group_name,@FormParam("ofIp") String ofIp,@FormParam("ppfIp") String ppfIp,@FormParam("spfIp") String spfIp)
   {
       ApiCommon apiCommon=new ApiCommon();
	   int groupId=createGroup(group_name);
	   int nid=getIdpostgre3G();
	   int didof=deviceIdof();
	   String configOf=apiCommon.getSufiConfigurationWithDefaultValues(1);
	   int didppf=deviceIdppf();
	   String configPpf=apiCommon.getSufiConfigurationWithDefaultValues(2);
	   int didspf=deviceIdspf();
	   String configSpf=apiCommon.getSufiConfigurationWithDefaultValues(3);
	   Common co = new Common();
	   
	   String query1 = "insert into btsmaster(application_id,ip,grp_id,typeid,devicetypeid,config) values('"+application_id+"','"+ofIp+"','"+groupId+"','"+nid+"','"+didof+"','"+configOf+"')";
	   String query2 = "insert into btsmaster(application_id,ip,grp_id,typeid,devicetypeid,config) values('"+application_id+"','"+ppfIp+"','"+groupId+"','"+nid+"','"+didppf+"','"+configPpf+"')";
	   String query3 = "insert into btsmaster(application_id,ip,grp_id,typeid,devicetypeid,config) values('"+application_id+"','"+spfIp+"','"+groupId+"','"+nid+"','"+didspf+"','"+configSpf+"')";
	   co.executeDLOperation(query1);
	   co.executeDLOperation(query2);
	   co.executeDLOperation(query3);
	    HashMap<String,String> rs = new HashMap<String,String>();
			rs.put("result", "success");
            return Response.status(201).entity(rs).build();
    }*/
  
   public int createGroup(String group_name)
   {
	   Common co = new Common();
	   String query1 = "insert into groups(grp_name) values('"+group_name+"') returning grp_id";
	   int id= co.executeQueryAndReturnId(query1);
	   return id;  
   }
   
      public int deleteGroup(String groupName)
   {
	   Common co = new Common();
	   String query1 = "delete from groups where grp_name='"+groupName+"' returning grp_id";
	   int id= co.executeQueryAndReturnId(query1);
	   return id;  
   }
  
   public int getIdpostgre2G()
   {
	   Common co = new Common();
	   String query1 = "select n_id from btsnetworktype where name='2G'";
	   int nid= co.executeQueryAndReturnId(query1);
	   return nid;  
   }
   
   public int getIdpostgre3G()
   {
	   Common co = new Common();
	   String query1 = "select n_id from btsnetworktype where name='3G'";
	   int nid= co.executeQueryAndReturnId(query1);
	   return nid;  
   }
  
   public int deviceIdof()
   {
	   Common co = new Common();
	   String query1 = "select d_id from devicetype where dname='SuFi OF'";	   
	   int didof= co.executeQueryAndReturnId(query1);
	   return didof;  
   }
   public int deviceIdppf()
   {
	   Common co = new Common();
	   String query1 = "select d_id from devicetype where dname='SuFi PPF'";	   
	   int didspf= co.executeQueryAndReturnId(query1);
	   return didspf;  
   }
   public int deviceIdspf()
   {
	   Common co = new Common();
	   String query1 = "select d_id from devicetype where dname='SuFi SPF'";	   
	   int didppf= co.executeQueryAndReturnId(query1);
	   return didppf;  
   }
   public int deviceId2G()
   {
	   Common co = new Common();
	   String query1 = "select d_id from devicetype where dname='Network Scanner'";	   
	   int did2G= co.executeQueryAndReturnId(query1);
	   return did2G;  
   }
   public int deviceIdgeo()
   {
	   Common co = new Common();
	   String query1 = "select d_id from devicetype where dname='Geo Locator'";	   
	   int didgeo= co.executeQueryAndReturnId(query1);
	   return didgeo;  
   }
   public int deviceIdmob()
   {
	   Common co = new Common();
	   String query1 = "select d_id from devicetype where dname='Mobile Locator'";	   
	   int didmob= co.executeQueryAndReturnId(query1);
	   return didmob;  
   }
   
   
   
   @POST
   @Path("/removeBts")
   @Produces(MediaType.APPLICATION_JSON)
   public Response removeBts(@Context HttpServletRequest req,@FormParam("btsIp") String btsIp, @FormParam("id") String id)
   {
	   	String query = "delete from btsmaster  where ip = '"+btsIp+"' and b_id = "+id;
		
	   	Common co = new Common();
		
		boolean result =  co.executeDLOperation(query);
		
		String rs = result == true?"{\"result\":\"success\"}":"{\"result\":\"fail\"}";	
        
		return Response.status(201).entity(rs).build();
    }
	
   @POST
   @Path("/removeBtsOfAGroup")
   @Produces(MediaType.APPLICATION_JSON)
   public Response removeBts(@Context HttpServletRequest req,@FormParam("groupName") String groupName)
   {
	        fileLogger.info("Inside Function : removeBts");
   	   		fileLogger.debug("all data is :"+groupName);
			int groupId=deleteGroup(groupName);
			Common co = new Common();	
			String query = "delete from btsmaster  where grp_id="+groupId;			
			boolean result =  co.executeDLOperation(query);
		
			String rs = result == true?"{\"result\":\"success\"}":"{\"result\":\"fail\"}";	
			 fileLogger.info("Exit Function : removeBts");
			return Response.status(201).entity(rs).build();
    }
   
   @POST
   @Path("/btsnetworktype")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getbtsnetworktype()
   {	  
	   fileLogger.info("Inside Function : getbtsnetworktype");
	   String query = "select * from btsnetworktype where status = 'A'";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		  fileLogger.info("Exit Function : getbtsnetworktype");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   
   @POST
   @Path("/btsdevicetype")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getbtsdevicetype()
   {	
	   fileLogger.info("Inside Function : getbtsdevicetype");
	   String query = "select * from deviceType where d_status = 'A' and code in(0,1,2)";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		  fileLogger.info("Exit Function : getbtsdevicetype");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   @POST
   @Path("/applicationType")
   @Produces(MediaType.APPLICATION_JSON)
   public Response abc()
   {	
	   fileLogger.info("Inside Function : abc");
	   String query = "select * from application_type where application_status='Y'";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : abc");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/getDeviceDetails")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getbtsdevicetype(@FormParam("ip") String btsIp)
   { 
	   fileLogger.info("Inside Function : getbtsdevicetype");
	   String query = "select * from btsmaster where ip = '"+btsIp+"'";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		   fileLogger.info("Exit Function : getbtsdevicetype");
        return Response.status(201).entity(rs.toString()).build();
    }
	
   
   /*
    * This Method will get all the bts present in to the btsmaster info
    * */
   @POST
   @Path("/btsinfo")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBTS()
   {	   
		fileLogger.info("Inside Function : getBTS");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select  * from view_btsinfo where code in(0,1,2) order by grp_name";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : getBTS");
        return Response.status(201).entity(rs.toString()).build();
    }
    
   @POST
   @Path("/alarms")
   @Produces(MediaType.APPLICATION_JSON)
   public Response alarms(@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {
		fileLogger.info("Inside Function : alarms");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select  * from alarms where tstmp between "+co.convertToMilliSec(startTime)+" and "+co.convertToMilliSec(endTime)+" order by tstmp desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : alarms");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/holdmesevent")
   @Produces(MediaType.APPLICATION_JSON)
   public Response holdMesEvent(@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {	 
		fileLogger.info("Inside Function : holdMesEvent");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select  * from mes_hold_event where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : holdMesEvent");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   @POST
   @Path("/redirectionevent")
   @Produces(MediaType.APPLICATION_JSON)
   public Response redirecholdmeseventtionEvnt(@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {	
		fileLogger.info("Inside Function : redirecholdmeseventtionEvnt");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select  * from redirection_event where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : redirecholdmeseventtionEvnt");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   @POST
   @Path("/lacchange")
   @Produces(MediaType.APPLICATION_JSON)
   public Response lacchange(@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {	
	   fileLogger.info("Inside Function : lacchange");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select  * from lac_change_event where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : lacchange");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/dedicatedmeasevent")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dedicatedMeasEvent(@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {	
		fileLogger.info("Inside Function : dedicatedMeasEvent");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select  * from DEDICATEDmeasevent where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : dedicatedMeasEvent");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/getconfig")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getsufiConfig(@FormParam("ip") String ip ,@FormParam("id") String id)
   {	
		fileLogger.info("Inside Function : getsufiConfig");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select  config where ip='"+ip+"' and id="+id;
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : getsufiConfig");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   @POST
   @Path("/updateConfig")
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateConfig(@FormParam("ip") String ip ,@FormParam("id") String id,@FormParam("config") String config)
   {	
		fileLogger.info("Inside Function : updateConfig");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "update btsmaster set config = '"+config+"',config_applied_status='y' where ip='"+ip+"' and b_id="+id;
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
			try{
			JSONObject configTree=new JSONObject(config);
			String mcc=configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").getString("MCC");
			String mnc=configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").getString("MNC");
			JSONArray ja=new Operations().getJson("select oprname("+mcc+mnc+"::numeric)");
			HashMap<String,String> tempHm=new HashMap<String,String>();
			tempHm.put(ip,ja.getJSONObject(0).getString("oprname"));
			new ConfigOprServer().sendText(tempHm);
			}catch(Exception E){
				fileLogger.error("Exception in updateConfig of class ThreegService with message:"+E.getMessage());
			}
			 fileLogger.info("Exit Function : updateConfig");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   @POST
   @Path("/updateConfigSibInfo")
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateConfigSibInfo(@FormParam("ip") String ip ,@FormParam("id") String id,@FormParam("config") String config)
   {	
		fileLogger.info("Inside Function : updateConfigSibInfo");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "update btsmaster set config = jsonb_set(config,'{CELL_PARAMS,SIB_INFO}','"+config+"') where ip='"+ip+"' and b_id="+id;
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : updateConfigSibInfo");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   @POST
   @Path("/updateConfigSubInfo")
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateConfigSubInfo(@FormParam("ip") String ip ,@FormParam("id") String id,@FormParam("config") String config)
   {	
		fileLogger.info("Inside Function : updateConfigSubInfo");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "update btsmaster set config = jsonb_set(config,'{SYS_PARAMS,SUB_INFO}','"+config+"') where ip='"+ip+"' and b_id="+id;
		fileLogger.debug(query);
		new Common().executeDLOperation(query);
		 fileLogger.info("Exit Function : updateConfigSubInfo");
        return Response.status(201).entity("{}").build();
    }
	
   @POST
   @Path("/updateConfigSubHold")
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateConfigSubHold(@FormParam("ip") String ip ,@FormParam("id") String id,@FormParam("config") String config)
   {	
		fileLogger.info("Inside Function : updateConfigSubHold");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "update btsmaster set config = jsonb_set(config,'{SYS_PARAMS,SUB_INFO,HOLD_SUB}','"+config+"') where ip='"+ip+"' and b_id="+id;
		fileLogger.debug(query);
		new Common().executeDLOperation(query);
		 fileLogger.info("Exit Function : updateConfigSubHold");
        return Response.status(201).entity("{}").build();
    }
	
@POST
   @Path("/getBtsOfAGroup")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBtsOfAGroup(@FormParam("groupName") String groupName)
   {	
		fileLogger.info("Inside Function : getBtsOfAGroup");
		String query = "select * from view_btsinfo where grp_name = '"+groupName+"' order by code";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : getBtsOfAGroup");
        return Response.status(201).entity(rs.toString()).build();
    }
	
	@POST
   @Path("/comopr")
   @Produces(MediaType.APPLICATION_JSON)
   public Response reciveFromClientForDirectCall(@FormParam("cmdType") String cmdType,@FormParam("systemCode") String systemCode,@FormParam("systemId") String systemId,@FormParam("systemIp") String systemIp,@FormParam("id") String id,@Context HttpServletRequest req,@FormParam("data") String data)
   {   
	   LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
	   param.put("cmdType", cmdType);
	   param.put("systemCode", systemCode);
	   param.put("systemIP", systemIp);
	   param.put("systemId", systemId);
	   param.put("id", id);
	   param.put("data", data);
	   return new ThreegOperations().sendToServer(param);
   }
	
   @GET
   @Path("/getdefaultsuficonfiguration")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getDefaultSufiConfiguration()
   {	   
	   	   String defaultSufiConfig=new ApiCommon().getSufiConfigurationWithDefaultValues();
		   return Response.status(201).entity(defaultSufiConfig).build();
   }
   
   @GET
   @Path("/getbanddluarfcnmap")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBandDluarfcnMap()
   {	   
	   	   String bandDluarfcnMap=new ApiCommon().getBandDluarfcnMap();
		   return Response.status(201).entity(bandDluarfcnMap).build();
   }
   
   	@GET
	@Path("/getbanddluarfcnmapLTE")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBandDluarfcnMapLTE() {
		String bandDluarfcnMap = new ApiCommon().getBandDluarfcnMapLTE();
		return Response.status(201).entity(bandDluarfcnMap).build();
	}
}
