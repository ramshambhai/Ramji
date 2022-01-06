package in.vnl.api.fourg;

import in.vnl.api.netscan.NetscanOperations;
import in.vnl.api.threeg.ThreegOperations;
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
import javax.servlet.http.HttpServletRequest;

@Path("/4g")
public class FourgService 
{
	static Logger fileLogger = Logger.getLogger("file");


	@POST
	@Path("/clientopr")
	@Produces(MediaType.APPLICATION_JSON)
	public Response reciveFromClient(@FormParam("cmdType") String cmdType, @FormParam("systemCode") String systemCode,
			@FormParam("systemId") String systemId, @FormParam("systemIp") String systemIp, @FormParam("id") String id,
			@Context HttpServletRequest req, @FormParam("data") String data) {
		fileLogger.info("Inside Function : reciveFromClient");
		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
		param.put("cmdType", cmdType);
		param.put("systemCode", systemCode);
		param.put("systemIP", systemIp);
		param.put("systemId", systemId);
		param.put("id", id);
		param.put("data", data);
		fileLogger.debug("@shasha" +param.toString());
		new Common().auditLog(new Common().convertToMapFor2g("omc", param.toString(), "S"));
		fileLogger.info("Exit Function : reciveFromClient");
		return new FourgOperations().executeActions(param);
	}
	
	@POST
	@Path("/gettype")
	@Produces(MediaType.APPLICATION_JSON)
	public Response gettype(@FormParam("band") String band) 
	{
		fileLogger.info("Inside Function : gettype");
		String type = (new ApiCommon().checkFddOrTdd(band, null)).get("type");
		JSONObject bandTypeObj= new JSONObject();
		try {
			bandTypeObj.put("type",type);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			fileLogger.error("error in gettype with message :"+e.getMessage());
		}
		fileLogger.info("Exit Function : gettype");
		return Response.status(201).entity(bandTypeObj.toString()).build();
	}
	
	
	@POST
	@Path("/opr")
	@Produces(MediaType.APPLICATION_JSON)
	public Response reciveFromDevice(@QueryParam("CMD_TYPE") String cmdType,
			@QueryParam("SYSTEM_CODE") String systemCode, @QueryParam("SYSTEM_ID") String systemId,
			@Context HttpServletRequest req, String data) {
		fileLogger.info("Inside Function : reciveFromDevice");
		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
		param.put("cmdType", cmdType);
		param.put("systemCode", systemCode);
		param.put("systemId", systemId);
		param.put("data", data);

		new Common().auditLog(new Common().convertToMapFor2g("node", param.toString(), "R"));
		fileLogger.info("Exit Function : reciveFromDevice");
		return new FourgOperations().executeActions(param);
	}
	
	
	@POST
	@Path("/updateConfigSubHold")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateConfigSubHold(@FormParam("ip") String ip, @FormParam("id") String id,
			@FormParam("config") String config) {
		fileLogger.info("Inside Function : updateConfigSubHold");
		// String query = "select
													// btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name
													// from btsmaster inner join
													// btsnetworktype on(typeid
													// = n_id)";
		Common co = new Common();
		String query = "update btsmaster set config = jsonb_set(config,'{SYS_PARAMS,SUB_INFO,HOLD_SUB}','" + config
				+ "') where ip='" + ip + "' and b_id=" + id;
		fileLogger.debug(query);
		new Common().executeDLOperation(query);
		fileLogger.info("Exit Function : updateConfigSubHold");
		return Response.status(201).entity("{}").build();
	}
	
	
	@GET
	@Path("/getdefaultsuficonfiguration")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDefaultSufiConfiguration() {
		String defaultSufiConfig = new ApiCommon().getESufiConfigurationWithDefaultValues();
		return Response.status(201).entity(defaultSufiConfig).build();
	}

}
