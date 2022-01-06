package in.vnl.api.twog;
import java.util.HashMap;
import java.util.LinkedHashMap;
//import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
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

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.AuditHandler;
import in.vnl.api.common.OperationCalculations;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.GeoSchedulerServer;
import in.vnl.msgapp.Operations;
import in.vnl.api.common.livescreens.ConfigOprServer;

@Path("/2g")
public class TowgServices 
{
   static Logger fileLogger = Logger.getLogger("file");
   static JSONArray listData;
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
   @Path("/btsnetworktype")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBtsNetworktype(){
	   
	   LinkedHashMap<String,String> rs = new LinkedHashMap<String,String>();
	   rs.put("result", "success");
	   rs.put("message", "ok workinng");
	   return Response.status(201).entity(rs).build();
   }
   
   
   
   @POST
   @Path("/addBts")
   @Produces(MediaType.APPLICATION_JSON)
   public Response postExample(@Context HttpServletRequest req,@FormParam("btsIp") String btsIp, @FormParam("btsType") String btsType)
   {
	     	fileLogger.info("Inside Function : postExample");		
	   		fileLogger.debug("all data is :"+btsIp+" "+btsType);	   			   
	        String rs = "{\"result\":\"success\",\"data\":\"Hello\"}";
	     	fileLogger.info("Exit Function : postExample");	
            return Response.status(201).entity(rs).build();
    }
	
   @POST
   @Path("/insertgeolocdata")
   @Produces(MediaType.APPLICATION_JSON)
   public Response insertGeoLocData(String data)
   {
    	fileLogger.info("Inside Function : insertGeoLocData");	
	  // fileLogger.debug("in insertgeolocdata");
	   		data=data.split("&")[2].split("=")[1];
	   		fileLogger.debug("response data is :"+data);
	   		String[] statusArr =new String[]{"UNKNOWN","IDLE","USER_BUSY","MS_PURGED","IMSI_DETACHED","NOT_REACHABLE_REASON_RESTRICTED_AREA","NOT_REGISTERED","NOT_PROVIDED_FROM_VLR"};
	   		HashMap<String,String> rs = new HashMap<String,String>();
	   		try
			{
	   		String hmcc="";
	   		String hmnc="";
	   		fileLogger.debug("0.2");
	   		JSONObject jo = new JSONObject(data);
	   		if((jo.getString("STATUS").indexOf("ERR(")>-1))
	   		{
	   		fileLogger.debug("Operation Fail "+jo.getString("STATUS"));
	   		rs.put("result","fail");
	   		return Response.status(201).entity(rs).build();
	   		}
	   		if(Long.parseLong(jo.getString("TAGS02"))>=5){
				hmcc = jo.getString("TAGS02").substring(0, 3);
				fileLogger.debug("0.5");
				hmnc = jo.getString("TAGS02").substring(3, 5);;
	   		}
	   		String msisdn=jo.getString("TAGS03");
	   		String imsi=jo.getString("TAGS02");
	   		String imei=jo.getString("TAGS04");
	   		fileLogger.debug("1");
	   		String homcc=jo.getString("TAGS05");
	   		String homnc=jo.getString("TAGS06");
	   		String lac=jo.getString("TAGS07");
	   		fileLogger.debug("2");
	   		String cell=jo.getString("TAGS08");
	   		String state=statusArr[Integer.parseInt(jo.getString("TAGS16"))];
	   		String coordinate=jo.getString("TAGS19");
	   		fileLogger.debug("3");
	   		String hlr=jo.getString("TAGS18");
	   		String vlr=jo.getString("TAGS13");
	   		String ftn=jo.getString("TAGS17");
	   		fileLogger.debug("4");
	   		String msc=jo.getString("TAGS14");
	   		Date date=new Date();
	   		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   		String currDate=sdf.format(date);
			
			Common co = new Common();	
			String query = "INSERT INTO geolocdata(msisdn, imsi, imei, hmcc, hmnc, mcc, mnc, lac, cell, state,coordinate, hlr, vlr, ftn, logtime,mscaddr)"+
			"values('"+msisdn+"','"+imsi+"','"+imei+"','"+hmcc+"','"+hmnc+"','"+homcc+"','"+homnc+"','"+lac+"','"+cell+"','"+state+"','"+coordinate+"','"+hlr+"','"+vlr+"','"+ftn+"','"+currDate+"','"+msc+"')";
			fileLogger.debug("query is :"+query);
			boolean insertStatus=co.executeDLOperation(query);
			if(insertStatus){
			rs.put("result", "sucsses");
			new GeoSchedulerServer().sendText(jo.toString());
			}else{
			rs.put("result","fail");	
			}
			}catch(Exception e){
				e.printStackTrace();
				
			}

	    	fileLogger.info("Exit Function : insertGeoLocData");	
            return Response.status(201).entity(rs).build();
    }
   
   
   
   @POST
   @Path("/nodewisedatastats")
   @Produces(MediaType.TEXT_HTML)
   public Response getNodeWiseDataStats(@Context HttpServletRequest req,@FormParam("startTime") String startTime)
   {

     	fileLogger.info("Inside Function : getNodeWiseDataStats");	
	   	String query = "select * from getnodewisedatastats('"+startTime+"');";
		fileLogger.debug(query);
		String rs =  new TwogOperations().converToHtmlForNodeWiseData(query);
	 	fileLogger.info("Exit Function : getNodeWiseDataStats");	
		return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/countrywisedatastats")
   @Produces(MediaType.TEXT_HTML)
   public Response getCountryWiseDataStats(@Context HttpServletRequest req,@FormParam("startTime") String startTime)
   {
	 	fileLogger.info("Inside Function : getCountryWiseDataStats");	
	   	String query = "select * from courntrywisedatastats('"+startTime+"');";
		fileLogger.debug(query);
		String rs =  new TwogOperations().converToHtmlForCountryWiseData(query);
	 	fileLogger.info("Exit Function : getCountryWiseDataStats");	
		return Response.status(201).entity(rs.toString()).build();
   }
   
   
   @POST
   @Path("/getoperationstarttime")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getOperationStartTime()
   {	
	 	fileLogger.info("Inside Function : getOperationStartTime");	
	   String query = "select inserttime from oprrationdata order by id desc limit 1";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		fileLogger.info("Exit Function : getOperationStartTime");	
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/addsessionparam")
   public String addSessionParam(@FormParam("name") String name,@FormParam("val") String val, @Context HttpServletRequest request)
	{
		HttpSession ses = request.getSession();;
		ses.setAttribute(name, val);
		return "ok";
	}
   
   
   
   @POST
   @Path("/gpsdata")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getgpsdata(@Context HttpServletRequest req,@FormParam("startTime") String startTime)
   { 	
	   fileLogger.info("Inside Function : getgpsdata");	
	   	//String query = "select lat as l ,lon as n  from gpsdata where logtime >= '"+startTime+"' order by logtime asc;";
	   	String query = "select lat as l ,lon as n  from gpsdata where logtime >= (timezone('utc'::text, now()) - '00:10:00'::interval) AND logtime <= timezone('utc'::text, now()) order by logtime";
	   fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		if(rs.length()==0){
			query="select lat as l ,lon as n  from gpsdata order by logtime desc limit 1";
			rs =  new Operations().getJson(query);
		}
		   fileLogger.info("Exit Function : getgpsdata");	 
		return Response.status(201).entity(rs.toString()).build();
   }
   
   
   @POST
   @Path("/gpsdatahistory")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getgpsdataHistory(@Context HttpServletRequest req,@FormParam("startTime") String startTime,@FormParam("endTime") String endTime)
   {
	   fileLogger.info("Inside Function : getgpsdataHistory");	
	   	//startTime re	
	   	String query = "select lat as l ,lon as n  from gpsdata where logtime >= '"+startTime+"' and logtime <= '"+endTime+"' order by logtime asc;";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		   fileLogger.info("Exit Function : getgpsdataHistory");	
		return Response.status(201).entity(rs.toString()).build();
   }
   
   @POST
   @Path("/getsubscriberdata")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getSubscriberData(@Context HttpServletRequest req,@FormParam("startTime") String startTime)
   {
	   fileLogger.info("Inside Function : getSubscriberData");	
	   //	String query = "select ti.*,tl.istarget from tracked_imsi ti left join target_list tl on (ti.imsi=tl.imsi)  where (ti.imsi,ti.logtime) in (select imsi,max(logtime) from tracked_imsi group by imsi)where logtime >= '"+startTime+"' group by ti.imsi);";
		
	   String query="select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,cc.band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(imsi) as operator,getcountry(substr(imsi::text, 1, 3)::numeric) as country,trigger_source,TRUNC(freq::numeric,1) as freq,msloc,target_name,profile_name,trans_id,distance,calc_basis,prob_msloc,self_loc from cdrlogs_current cc left join antenna ant on cc.ant_id=ant.id where inserttime >= ('"+startTime+"'::timestamp without time zone - '05:30:00'::interval) and imsi != 'null' and traget_type='Blacklist' order by inserttime desc limit 2000";
	   //String query = "select ti.imsi,ti.imei,ti.ta,ti.power,ti.lat,ti.lon,ti.acc,date_trunc('second',ti.logtime + '05:30:00'::interval) arrival_time,tl.istarget,tl.name target_name,tl.type from tracked_imsi ti left join target_list tl on (ti.imsi=tl.imsi or ti.imei=tl.imei)  where (ti.imsi,ti.logtime) in (select imsi,max(logtime) from tracked_imsi group by imsi) and logtime >= ('"+startTime+"'::timestamp without time zone - '05:30:00'::interval) and tl.type='Blacklist'";
	   fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		   fileLogger.info("Exit Function : getSubscriberData");	
		return Response.status(201).entity(rs.toString()).build();
   }
   
   @POST
   @Path("/getnetorkinfonodewise")
   @Produces(MediaType.TEXT_HTML)
   public Response getNetworkInfoNodeWise(@Context HttpServletRequest req,@FormParam("startTime") String startTime)
   {
	   fileLogger.info("Inside Function : getNetworkInfoNodeWise");	
	   	//String query = "select oprname((mcc||mnc)::numeric),concat(packet_type,'-',band) bts,count(*) from oprlogs where (mcc,mnc,lac,cell,inserttime) in (select mcc,mnc,lac,cell,max(inserttime) from oprlogs where inserttime >= '"+startTime+"' group by mcc,mnc,lac,cell) group by oprname,concat(packet_type,'-',band) order by oprname;";
	   //String query = "select opr as oprname,concat(packet_type,'-',band) bts,count(*) from oprlogs left join plmn_opr on(concat(mcc,mnc)::numeric = plmn) where (mcc,mnc,lac,cell,inserttime) in (select mcc,mnc,lac,cell,max(inserttime) from oprlogs where inserttime >= '"+startTime+"' group by mcc,mnc,lac,cell) group by oprname,concat(packet_type,'-',band) order by oprname;";
		String query = "select operators as oprname,concat(packet_type,'-',band) bts,count(*) from oprlogs_current where inserttime >= '"+startTime+"' group by operators,concat(packet_type,'-',band) order by oprname;";
	   fileLogger.debug(query);
		String rs =  new TwogOperations().converToHtmlForNetworkInfoNodeWiseData(query);
		   fileLogger.info("Exit Function : getNetworkInfoNodeWise");	
		return Response.status(201).entity(rs.toString()).build();
   }
   
   @POST
   @Path("/getsubscrberpath")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getSubscriberPath(@Context HttpServletRequest req,@FormParam("startTime") String startTime,@FormParam("imsi") String imsi)
   {
	   fileLogger.info("Inside Function : getSubscriberPath");	
	   	//String query = "select * from cdrlogs inner join gpsdata on (cdrlogs.tstmp = gpsdata.tstmp) where imsi ='"+imsi+"' and inserttime >= '"+startTime+"' order by inserttime asc";
	    String query="select * from tracked_imsi  where imsi ='"+imsi+"' and logtime >= '"+startTime+"' order by logtime asc";
	    fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		  fileLogger.info("Exit Function : getSubscriberPath");
		return Response.status(201).entity(rs.toString()).build();
   }
   
   
   @POST
   @Path("/getNodesStatus")
   @Produces(MediaType.TEXT_HTML)
   public Response getNodesStatus()
   {	  fileLogger.info("Inside Function : getNodesStatus");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select  * from view_btsinfo where code in(0,1,2,5)";
	   fileLogger.debug(query);
	   String rs =  new TwogOperations().converToHtmlForNodesInfo(query);
	   fileLogger.info("Exit Function : getNodesStatus");
	   return Response.status(201).entity(rs.toString()).build();
   }
   
   @POST
   @Path("/lockUnlockCell")
   @Produces(MediaType.APPLICATION_JSON)
   public Response lockUnlockCell(@Context HttpServletRequest req,@FormParam("flag") String flag,@FormParam("ip") String ip)
   {
	   String rs =  new TwogOperations().setLockUnlock(ip,flag);
	   return Response.status(201).entity(rs.toString()).build();
   }
   
   @GET
   @Path("/updatestatusofallbts")
   @Produces(MediaType.TEXT_HTML)
   public Response updateStatusOfAllBts()
   {	   
	   new TwogOperations().updateStatusOfBts("all");
	   return getNodesStatus();
   }
   
   @POST
   @Path("/getdatafordetectedmobiles")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getDataForDetectedMobiles(@Context HttpServletRequest req,@FormParam("type") String type,@FormParam("filter") String filter,@FormParam("value[]") List<String> value,@FormParam("bts") String bts,@FormParam("country") String country,@FormParam("oprname") String oprname,@FormParam("startTime") String startTime,@FormParam("endTime") String endTime)
   {	
	   fileLogger.info("Inside Function : getDataForDetectedMobiles");
	   HttpSession session = req.getSession();
	   //String operationTime = session.getAttribute("startTime").toString();
	   
	   //String query = "select cdroprreport.ta,cdroprreport.power,cdroprreport.imsi,cdroprreport.imei,case when (substring(cdroprreport.msloc, 1, 3) = 'Err' or cdroprreport.msloc = 'NA,NA') then concat(lat,',',lon) else cdroprreport.msloc end as  loc from cdrlogs_current inner join cdroprreport on(cdrlogs_current.imsi = cdroprreport.imsi and cdrlogs_current.inserttime = cdroprreport.logtime) ";
	   String query = "select cdroprreport.ta,cdroprreport.power,cdroprreport.imsi,cdroprreport.imei,case when ((cdroprreport.msloc) is null or textregexeq(substring(cdroprreport.msloc,1,1),'^[[:digit:]]+(\\.[[:digit:]]+)?$')='f') then concat(lat,',',lon) else cdroprreport.msloc end as  loc from cdrlogs_current inner join cdroprreport on(cdrlogs_current.imsi = cdroprreport.imsi and cdrlogs_current.inserttime = cdroprreport.logtime) ";
    
	   StringBuilder where = new StringBuilder("where cdroprreport.logtime >=  '"+startTime+"' and cdroprreport.logtime <=  '"+endTime+"' ");
	   String and = " and ";
	   if(!bts.equalsIgnoreCase("-1") )
	   {   
		   where.append(and+" concat(stype,'-',band) = '"+bts+"' ");
	   }
	   if(!type.equalsIgnoreCase("-1") )
	   {   
		   if(value.size()>1)
		   {
			   where.append(and+" cdrlogs_current."+type+"!='NA' and cdrlogs_current."+type+"::numeric"+">"+value.get(0)+" and cdrlogs_current."+type+"::numeric"+" <= "+value.get(1));
		   }
		   else
		   {
			   
			   where.append(and+" cdrlogs_current."+type+"!='NA' and cdrlogs_current."+type+"::numeric"+getFilterSymbol(filter)+value.get(0));
		   }
	   }
	   if(!country.equalsIgnoreCase("-1"))
		   where.append(and+" country='"+country+"'");
	   if(!oprname.equalsIgnoreCase("-1"))
		   where.append(and+" oprname='"+oprname+"'");
	   
	   query = query+where.toString();
	   fileLogger.debug(query);
	   JSONArray rs =  new Operations().getJson(query);
	   fileLogger.info("Exit Function : getDataForDetectedMobiles");
	   return Response.status(201).entity(rs.toString()).build();
	   //return Response.status(201).entity("").build();
   }
   
   public String getFilterSymbol(String filter)
   {
	   switch(filter)
	   {
	   		case "more":
	   			return ">";
	   		case "less":
   				return "<";
	   		case "equal":
   				return "=";
	   }

	   return "=";	   
   }
   
   
	/**
	 * @throws JSONException **************/
	@POST
	@Path("/addMultiplePLMN")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public Response addMultiplePLMNData(String data) throws JSONException {
		 fileLogger.info("Inside Function : addMultiplePLMNData");
		String selfLoc=null;
		double selfLat=-1.0;
		double selfLon=-1.0;
		JSONArray gpsArr=new Operations().getJson("select lat,lon from gpsdata order by logtime desc limit 1");
		try {
			if(gpsArr.length()>0){
			JSONObject gpsObj=gpsArr.getJSONObject(0);
			selfLat=gpsObj.getDouble("lat");
			selfLon=gpsObj.getDouble("lon");
			}
			
			if(selfLat==-1.0 || selfLon==-1.0){
				selfLoc="NA";
			}else{
				selfLat=Math.round(selfLat*1000000.0)/1000000.0;
				selfLon=Math.round(selfLon*1000000.0)/1000000.0;
				selfLoc=selfLat+","+selfLon;
			}
			} catch (JSONException e) {
				fileLogger.error("Exception In Getting GPS Coordinates At Cell Incoming With Message :"+e.getMessage());
			}
		
		
		JSONObject rs = new JSONObject();
		JSONArray jArr = new JSONArray(data);	
		Common co = new Common();
		for (int i = 0; i < jArr.length(); i++)
        {
            JSONObject jo = jArr.getJSONObject(i);
            try {

				//JSONObject jo = new JSONObject(data);
				String mcc = jo.getString("mcc");
				String mnc = jo.getString("mnc");
				String lacOrTac = jo.getString("lacOrTac");
				String cell = jo.getString("cell");
				String lat = jo.getString("lat");
				String lon = jo.getString("lon");
				String antena_ids="";
				
				String allAntennaStr = jo.getString("allAntennaStr");
				boolean allAntennaStatus = false;
				int angleOffset=DBDataService.getAngleOffset();
				JSONArray antennaDetailsArr = new Operations().getJson("select id from antenna where id !=22");
				if (allAntennaStr.equals("-1")) {
					allAntennaStatus = true;
					for (int antennaCount = 0; antennaCount < antennaDetailsArr.length(); antennaCount++) {
					if (antennaCount==0)
					antena_ids =antennaDetailsArr.getJSONObject(antennaCount).getInt("id")+"" ;
					else
						antena_ids =antennaDetailsArr.getJSONObject(antennaCount).getInt("id")  +","+ antena_ids;
					}
				}
				else
				{
					String[] allAntennaArr1 = allAntennaStr.split(",");
					for (int antennaCount = 0; antennaCount < allAntennaArr1.length; antennaCount++) {
						if (antennaCount==0)
							antena_ids =allAntennaArr1[antennaCount]+"" ;
							else
								antena_ids =allAntennaArr1[antennaCount] +","+ antena_ids;
					}
				}
				JSONArray latLonCountOpr = new Operations().getLatLon(jo.getString("mcc"), jo.getString("mnc"),
						jo.getString("lacOrTac"), jo.getString("cell"));
	
				String colLacOrTac="lac";
				ArrayList<String> query4 = new ArrayList<String>();
				
				if (jo.getString("cellType").equalsIgnoreCase("2g")) {
					double calculatedFreq = new OperationCalculations().calulateFreqFromArfcn(jo.getInt("arfcnOrUarfcn"));
					if (allAntennaStatus) {
						for (int antennaCount = 0; antennaCount < antennaDetailsArr.length(); antennaCount++) {
							String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,arfcn,mcc,mnc,lac,cell,ncc,bcc,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
									+ "values('Locator','locator_count','Loc_2g','" + jo.getString("band") + "','"
									+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
									+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
									+ "','" + (jo.getInt("bsicOrPsc") / 8) + "','" + (jo.getInt("bsicOrPsc") % 8) + "','"
									+ jo.getInt("rssi") + "','" + jo.getString("lat") + "," + jo.getString("lon") + "','"
									+ jo.getString("lat") + "','" + jo.getString("lon") + "','"
									+ latLonCountOpr.getJSONObject(0).getString("count") + "','"
									+ latLonCountOpr.getJSONObject(0).getString("opr") + "',"
									+ antennaDetailsArr.getJSONObject(antennaCount).getInt("id") + ",'" + calculatedFreq
									+ "',"+angleOffset+",'"+selfLoc+"')";
							query4.add(tempQuery);
							if (antennaCount==0)
							antena_ids =antennaDetailsArr.getJSONObject(antennaCount).getInt("id")+"" ;
							else
								antena_ids =antennaDetailsArr.getJSONObject(antennaCount).getInt("id")  +","+ antena_ids;
						}
					} else {
						String[] allAntennaArr = allAntennaStr.split(",");
						for (int antennaCount = 0; antennaCount < allAntennaArr.length; antennaCount++) {
							String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,arfcn,mcc,mnc,lac,cell,ncc,bcc,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
									+ "values('Locator','locator_count','Loc_2g','" + jo.getString("band") + "','"
									+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
									+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
									+ "','" + (jo.getInt("bsicOrPsc") / 8) + "','" + (jo.getInt("bsicOrPsc") % 8) + "','"
									+ jo.getInt("rssi") + "','" + jo.getString("lat") + "," + jo.getString("lon") + "','"
									+ jo.getString("lat") + "','" + jo.getString("lon") + "','"
									+ latLonCountOpr.getJSONObject(0).getString("count") + "','"
									+ latLonCountOpr.getJSONObject(0).getString("opr") + "'," + allAntennaArr[antennaCount]
									+ ",'" + calculatedFreq + "',"+angleOffset+",'"+selfLoc+"')";
							query4.add(tempQuery);
						}
					}
	
				}else if(jo.getString("cellType").equalsIgnoreCase("4g")){
					colLacOrTac="tac";
					double calculatedFreq = new OperationCalculations().calulateFreqFromArfcn(jo.getInt("arfcnOrUarfcn"));
					
					if (allAntennaStatus) {
						for (int antennaCount = 0; antennaCount < antennaDetailsArr.length(); antennaCount++) {
							String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,earfcn,mcc,mnc,tac,cell,pci,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
									+ "values('Locator','locator_count','Loc_4g','" + jo.getString("band") + "','"
									+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
									+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
									+ "','" + jo.getString("bsicOrPsc") + "','" + jo.getInt("rssi") + "','"
									+ jo.getString("lat") + "," + jo.getString("lon") + "','" + jo.getString("lat") + "','"
									+ jo.getString("lon") + "','" + latLonCountOpr.getJSONObject(0).getString("count")
									+ "','" + latLonCountOpr.getJSONObject(0).getString("opr") + "',"
									+ antennaDetailsArr.getJSONObject(antennaCount).getInt("id") + ",'" + calculatedFreq
									+ "',"+angleOffset+",'"+selfLoc+"')";
							query4.add(tempQuery);
						}
					} else {
						String[] allAntennaArr = allAntennaStr.split(",");
						for (int antennaCount = 0; antennaCount < allAntennaArr.length; antennaCount++) {
							String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,earfcn,mcc,mnc,tac,cell,pci,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
									+ "values('Locator','locator_count','Loc_4g','" + jo.getString("band") + "','"
									+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
									+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
									+ "','" + jo.getString("bsicOrPsc") + "','" + jo.getInt("rssi") + "','"
									+ jo.getString("lat") + "," + jo.getString("lon") + "','" + jo.getString("lat") + "','"
									+ jo.getString("lon") + "','" + latLonCountOpr.getJSONObject(0).getString("count")
									+ "','" + latLonCountOpr.getJSONObject(0).getString("opr") + "',"
									+ allAntennaArr[antennaCount] + ",'" + calculatedFreq + "',"+angleOffset+",'"+selfLoc+"')";
							query4.add(tempQuery);
						}
	
					}
				} 
				else {
					double calculatedFreq = new OperationCalculations().calulateFreqFromArfcn(jo.getInt("arfcnOrUarfcn"));
					if (allAntennaStatus) {
						for (int antennaCount = 0; antennaCount < antennaDetailsArr.length(); antennaCount++) {
							String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,uarfcn,mcc,mnc,lac,cell,psc,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
									+ "values('Locator','locator_count','Loc_3g','" + jo.getString("band") + "','"
									+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
									+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
									+ "','" + jo.getString("bsicOrPsc") + "','" + jo.getInt("rssi") + "','"
									+ jo.getString("lat") + "," + jo.getString("lon") + "','" + jo.getString("lat") + "','"
									+ jo.getString("lon") + "','" + latLonCountOpr.getJSONObject(0).getString("count")
									+ "','" + latLonCountOpr.getJSONObject(0).getString("opr") + "',"
									+ antennaDetailsArr.getJSONObject(antennaCount).getInt("id") + ",'" + calculatedFreq
									+ "',"+angleOffset+",'"+selfLoc+"')";
							query4.add(tempQuery);
						}
					} else {
						String[] allAntennaArr = allAntennaStr.split(",");
						for (int antennaCount = 0; antennaCount < allAntennaArr.length; antennaCount++) {
							String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,uarfcn,mcc,mnc,lac,cell,psc,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
									+ "values('Locator','locator_count','Loc_3g','" + jo.getString("band") + "','"
									+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
									+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
									+ "','" + jo.getString("bsicOrPsc") + "','" + jo.getInt("rssi") + "','"
									+ jo.getString("lat") + "," + jo.getString("lon") + "','" + jo.getString("lat") + "','"
									+ jo.getString("lon") + "','" + latLonCountOpr.getJSONObject(0).getString("count")
									+ "','" + latLonCountOpr.getJSONObject(0).getString("opr") + "',"
									+ allAntennaArr[antennaCount] + ",'" + calculatedFreq + "',"+angleOffset+",'"+selfLoc+"')";
							query4.add(tempQuery);
						}
	
					}
				}
				
				String query1 = "delete from cell_data where mcc=" + mcc + " and net=" + mnc + " and area=" + lacOrTac + " and cell=" + cell;
				//String query2 = "delete from oprlogs_current where mcc='" + mcc + "' and mnc='" + mnc + "' and "+colLacOrTac+"='" + lacOrTac + "' and cell='" + cell + "' and antenna_id in ("+ antena_ids +")";
						
				String query3 = "INSERT INTO cell_data(mcc,net,area,cell,lat,lon)" + "values(" + mcc + "," + mnc + "," + lacOrTac
						+ "," + cell + "," + lat + "," + lon + ")";
				co.executeDLOperation(query1);
			//	co.executeDLOperation(query2);
				co.executeDLOperation(query3);
				co.executeBatchOperation(query4);
				LinkedHashMap<String, String> log = new LinkedHashMap<String, String>();
				log.put("action", "Add Cell");
				log.put("mcc", mcc);
				log.put("mnc", mcc);
				log.put(colLacOrTac, lacOrTac);
				log.put("cell", cell);
				log.put("angle offset",Integer.toString(angleOffset));
				new AuditHandler().auditCellAction(log);
				
				rs.put("result", "SUCCESS");
				rs.put("id", "1");

			} catch (Exception e) {
				e.printStackTrace();
			}
			
	        }
		 fileLogger.info("Exit Function : addMultiplePLMNData");
			return Response.status(201).entity(rs.toString()).build();
		}
   
	/****************/
	@POST
	@Path("/addPLMN")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public Response addPLMNData(String data) {
		 fileLogger.info("Inside Function : addPLMNData");
		JSONObject rs = new JSONObject();
		try {
			JSONArray gpsArr=new Operations().getJson("select lat,lon from gpsdata order by logtime desc limit 1");
			String selfLoc=null;
			double selfLat=-1.0;
			double selfLon=-1.0;
			try {
				if(gpsArr.length()>0){
					JSONObject gpsObj=gpsArr.getJSONObject(0);
					selfLat=gpsObj.getDouble("lat");
					selfLon=gpsObj.getDouble("lon");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				fileLogger.error("exception in getting gps coordinates at cell incoming with message :"+e.getMessage());
			}
			
			if(selfLat==-1.0 || selfLon==-1.0){
				selfLoc="NA";
			}else{
				selfLat=Math.round(selfLat*1000000.0)/1000000.0;
				selfLon=Math.round(selfLon*1000000.0)/1000000.0;
				selfLoc=selfLat+","+selfLon;
			}	
			JSONObject jo = new JSONObject(data);

			Common co = new Common();
			String mcc = jo.getString("mcc");
			String mnc = jo.getString("mnc");
			String lacOrTac = jo.getString("lacOrTac");
			String cell = jo.getString("cell");
			String lat = jo.getString("lat");
			String lon = jo.getString("lon");

			String allAntennaStr = jo.getString("allAntennaStr");
			boolean allAntennaStatus = false;
			int angleOffset=DBDataService.getAngleOffset();
			if (allAntennaStr.equals("-1")) {
				allAntennaStatus = true;
			}
			JSONArray latLonCountOpr = new Operations().getLatLon(jo.getString("mcc"), jo.getString("mnc"),
					jo.getString("lacOrTac"), jo.getString("cell"));
			// String query = "INSERT INTO
			// oprlogs_current(ip,count,packet_type,arfcn,mcc,mnc,lac,cell,ncc,bcc,rssi,sysloc,lat,lon,country,operators)"+
			// "values('Locator','locator_count','Loc_2g','"+jo.getString("arfcn")+"','"+jo.getString("mcc")+"','"+jo.getString("mnc")+"','"+jo.getString("lac")+"','"+jo.getString("cell")+"','"+(jo.getInt("bsic")/8)+"','"+(jo.getInt("bsic")%8)+"','"+jo.getInt("rssi")+"','NA,NA','"+latLonCountOpr.getJSONObject(0).getString("lat")+"','"+latLonCountOpr.getJSONObject(0).getString("lon")+"','"+latLonCountOpr.getJSONObject(0).getString("count")+"','"+latLonCountOpr.getJSONObject(0).getString("opr")+"')";
			// String query4="";
			String colLacOrTac="lac";
			ArrayList<String> query4 = new ArrayList<String>();
			JSONArray antennaDetailsArr = new Operations().getJson("select id from antenna where id !=22");
			if (jo.getString("cellType").equalsIgnoreCase("2g")) {
				double calculatedFreq = new OperationCalculations().calulateFreqFromArfcn(jo.getInt("arfcnOrUarfcn"),"2G");
				if (allAntennaStatus) {
					for (int antennaCount = 0; antennaCount < antennaDetailsArr.length(); antennaCount++) {
						String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,arfcn,mcc,mnc,lac,cell,ncc,bcc,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
								+ "values('Locator','locator_count','Loc_2g','" + jo.getString("band") + "','"
								+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
								+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
								+ "','" + (jo.getInt("bsicOrPsc") / 8) + "','" + (jo.getInt("bsicOrPsc") % 8) + "','"
								+ jo.getInt("rssi") + "','" + jo.getString("lat") + "," + jo.getString("lon") + "','"
								+ jo.getString("lat") + "','" + jo.getString("lon") + "','"
								+ latLonCountOpr.getJSONObject(0).getString("count") + "','"
								+ latLonCountOpr.getJSONObject(0).getString("opr") + "',"
								+ antennaDetailsArr.getJSONObject(antennaCount).getInt("id") + ",'" + calculatedFreq
								+ "',"+angleOffset+",'"+selfLoc+"')";
						query4.add(tempQuery);
					}
				} else {
					String[] allAntennaArr = allAntennaStr.split(",");
					for (int antennaCount = 0; antennaCount < allAntennaArr.length; antennaCount++) {
						String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,arfcn,mcc,mnc,lac,cell,ncc,bcc,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
								+ "values('Locator','locator_count','Loc_2g','" + jo.getString("band") + "','"
								+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
								+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
								+ "','" + (jo.getInt("bsicOrPsc") / 8) + "','" + (jo.getInt("bsicOrPsc") % 8) + "','"
								+ jo.getInt("rssi") + "','" + jo.getString("lat") + "," + jo.getString("lon") + "','"
								+ jo.getString("lat") + "','" + jo.getString("lon") + "','"
								+ latLonCountOpr.getJSONObject(0).getString("count") + "','"
								+ latLonCountOpr.getJSONObject(0).getString("opr") + "'," + allAntennaArr[antennaCount]
								+ ",'" + calculatedFreq + "',"+angleOffset+",'"+selfLoc+"')";
						query4.add(tempQuery);
					}
				}

			}else if(jo.getString("cellType").equalsIgnoreCase("4g")){
				colLacOrTac="tac";
				double calculatedFreq = new OperationCalculations().calulateFreqFromArfcn(jo.getInt("arfcnOrUarfcn"),"4G");
				if (allAntennaStatus) {
					for (int antennaCount = 0; antennaCount < antennaDetailsArr.length(); antennaCount++) {
						String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,earfcn,mcc,mnc,tac,cell,pci,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
								+ "values('Locator','locator_count','Loc_4g','" + jo.getString("band") + "','"
								+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
								+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
								+ "','" + jo.getString("bsicOrPsc") + "','" + jo.getInt("rssi") + "','"
								+ jo.getString("lat") + "," + jo.getString("lon") + "','" + jo.getString("lat") + "','"
								+ jo.getString("lon") + "','" + latLonCountOpr.getJSONObject(0).getString("count")
								+ "','" + latLonCountOpr.getJSONObject(0).getString("opr") + "',"
								+ antennaDetailsArr.getJSONObject(antennaCount).getInt("id") + ",'" + calculatedFreq
								+ "',"+angleOffset+",'"+selfLoc+"')";
						query4.add(tempQuery);
					}
				} else {
					String[] allAntennaArr = allAntennaStr.split(",");
					for (int antennaCount = 0; antennaCount < allAntennaArr.length; antennaCount++) {
						String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,earfcn,mcc,mnc,tac,cell,pci,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
								+ "values('Locator','locator_count','Loc_4g','" + jo.getString("band") + "','"
								+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
								+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
								+ "','" + jo.getString("bsicOrPsc") + "','" + jo.getInt("rssi") + "','"
								+ jo.getString("lat") + "," + jo.getString("lon") + "','" + jo.getString("lat") + "','"
								+ jo.getString("lon") + "','" + latLonCountOpr.getJSONObject(0).getString("count")
								+ "','" + latLonCountOpr.getJSONObject(0).getString("opr") + "',"
								+ allAntennaArr[antennaCount] + ",'" + calculatedFreq + "',"+angleOffset+",'"+selfLoc+"')";
						query4.add(tempQuery);
					}

				}
			} 
			else {
				double calculatedFreq = new OperationCalculations().calulateFreqFromArfcn(jo.getInt("arfcnOrUarfcn"),"3G");
				if (allAntennaStatus) {
					for (int antennaCount = 0; antennaCount < antennaDetailsArr.length(); antennaCount++) {
						String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,uarfcn,mcc,mnc,lac,cell,psc,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
								+ "values('Locator','locator_count','Loc_3g','" + jo.getString("band") + "','"
								+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
								+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
								+ "','" + jo.getString("bsicOrPsc") + "','" + jo.getInt("rssi") + "','"
								+ jo.getString("lat") + "," + jo.getString("lon") + "','" + jo.getString("lat") + "','"
								+ jo.getString("lon") + "','" + latLonCountOpr.getJSONObject(0).getString("count")
								+ "','" + latLonCountOpr.getJSONObject(0).getString("opr") + "',"
								+ antennaDetailsArr.getJSONObject(antennaCount).getInt("id") + ",'" + calculatedFreq
								+ "',"+angleOffset+",'"+selfLoc+"')";
						query4.add(tempQuery);
					}
				} else {
					String[] allAntennaArr = allAntennaStr.split(",");
					for (int antennaCount = 0; antennaCount < allAntennaArr.length; antennaCount++) {
						String tempQuery = "INSERT INTO oprlogs_current(ip,count,packet_type,band,uarfcn,mcc,mnc,lac,cell,psc,rssi,sysloc,lat,lon,country,operators,antenna_id,freq,off_angle,self_loc)"
								+ "values('Locator','locator_count','Loc_3g','" + jo.getString("band") + "','"
								+ jo.getString("arfcnOrUarfcn") + "','" + jo.getString("mcc") + "','"
								+ jo.getString("mnc") + "','" + jo.getString("lacOrTac") + "','" + jo.getString("cell")
								+ "','" + jo.getString("bsicOrPsc") + "','" + jo.getInt("rssi") + "','"
								+ jo.getString("lat") + "," + jo.getString("lon") + "','" + jo.getString("lat") + "','"
								+ jo.getString("lon") + "','" + latLonCountOpr.getJSONObject(0).getString("count")
								+ "','" + latLonCountOpr.getJSONObject(0).getString("opr") + "',"
								+ allAntennaArr[antennaCount] + ",'" + calculatedFreq + "',"+angleOffset+",'"+selfLoc+"')";
						query4.add(tempQuery);
					}

				}
			}
			
			String query1 = "delete from cell_data where mcc=" + mcc + " and net=" + mnc + " and area=" + lacOrTac
					+ " and cell=" + cell;
			/*
			 * String query2 = "delete from oprlogs_current where mcc='" + mcc +
			 * "' and mnc='" + mnc + "' and "+colLacOrTac+"='" + lacOrTac + "' and cell='" +
			 * cell + "'";
			 */
			String query3 = "INSERT INTO cell_data(mcc,net,area,cell,lat,lon)" + "values(" + mcc + "," + mnc + "," + lacOrTac
					+ "," + cell + "," + lat + "," + lon + ")";
			co.executeDLOperation(query1);
			//co.executeDLOperation(query2);
			co.executeDLOperation(query3);
			co.executeBatchOperation(query4);
			// if(id!=0){
			LinkedHashMap<String, String> log = new LinkedHashMap<String, String>();
			log.put("action", "Add Cell");
			log.put("mcc", mcc);
			log.put("mnc", mcc);
			log.put(colLacOrTac, lacOrTac);
			log.put("cell", cell);
			log.put("angle offset",Integer.toString(angleOffset));
			new AuditHandler().auditCellAction(log);

			rs.put("result", "SUCCESS");
			rs.put("id", "1");
			// }
			/*
			 * else{ rs.put("result", "FAIL"); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		 fileLogger.info("Exit Function : addPLMNData");
		return Response.status(201).entity(rs.toString()).build();
	}
   
   /****************/
   @POST
   @Path("/getPLMN")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes("application/json") 
   public Response getPLMNData(String data)
   {
	   fileLogger.info("Inside Function : getPLMNData");
		String query = "select * from oprlogs_current left join antenna on oprlogs_current.antenna_id=antenna.id where ip='Locator' order by inserttime";
		fileLogger.debug(query);
		JSONArray plmnList=new Operations().getJson(query);
		listData=plmnList;
		   fileLogger.info("Exit Function : getPLMNData");
		return Response.status(201).entity(plmnList.toString()).build();
    }
   
   @POST
   @Path("/deletePLMN")
   @Produces(MediaType.APPLICATION_JSON)
   public Response deletePLMN(@Context HttpServletRequest req,@FormParam("targetIds") String targetIds)
   {
	   fileLogger.info("Inside Function : deletePLMN");
	   		Common co = new Common();	
	   		JSONObject rs = new JSONObject();
	   		LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
	   		log.put("action", "Delete Cell");
	   		try{
	   		String selectQuery = "select mcc,mnc,lac,cell from oprlogs_current where id in ("+targetIds+")";
	   		JSONArray cellList=new Operations().getJson(selectQuery);
	   		for(int i=0;i<cellList.length();i++){
	   			JSONObject tempJson = cellList.getJSONObject(i);
	   			String mcc=tempJson.getString("mcc");
	   			String mnc=tempJson.getString("mnc");
	   			String lac=tempJson.getString("lac");
	   			String cell=tempJson.getString("cell");
	   			log.put("Cell-"+i,mcc+"-"+mnc+"-"+lac+"-"+cell);
	   		}
	   		String query1 = "delete from cell_data where (mcc,net,area,cell) in(select mcc::integer,mnc::integer,lac::integer,cell::integer from oprlogs_current where id in ("+targetIds+"))";
			String query2 = "delete from oprlogs_current where id in ("+targetIds+")";
				co.executeDLOperation(query1);
				boolean status=co.executeDLOperation(query2);
				if(status){
					//log.put("imsi", targetImsi);
		   			new AuditHandler().auditCellAction(log);
				rs.put("result", "SUCCESS");
				}else{
				rs.put("result", "FAIL");	
				}
	   		}catch(Exception E){
	   			fileLogger.error("Exception coming in deletePLMN of TowgServices:"+E.getMessage());
	   		}
	   		fileLogger.info("Exit Function : deletePLMN");
	            return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/insertNcellsData")
   @Produces(MediaType.APPLICATION_JSON)
   public Response insertNcellsData(@Context HttpServletRequest req,@FormParam("cellsData") String cellsData,@FormParam("flag") String flag)
   {
	   
	   		
	   		Common co = new Common();
	   		String[] aa = cellsData.split("#");
	   		String[] neighbourData = aa[0].split("\\$");
	   		String ip = req.getSession().getAttribute("currentNib").toString();
	   		try{
	   		if(flag.equals("2")){
	   			if(aa.length==1){
		   			String insertData = "'"+aa[0].replaceAll(",","','")+"'";
		   			String deletequery = "delete from n_cells_data where ip = '"+ip+"'";
		   			co.executeDLOperation(deletequery);
		   			String query = "INSERT INTO n_cells_data(ip,flag,plmn,lac,cell,arfcn,bsic,rxs,type) values('"+ip+"',"+insertData+",'S')";
		   			if(co.executeDLOperation(query)){
		   				query="update btsmaster set config_applied_status='y' where ip='"+ip+"'";
		   				co.executeDLOperation(query);
		   			}
		   			String plmn=aa[0].split(",")[1];
		   			JSONArray ja=new Operations().getJson("select oprname("+plmn+"::numeric)");
		   			HashMap<String,String> tempHm=new HashMap<String,String>();
		   			tempHm.put(ip,ja.getJSONObject(0).getString("oprname"));
		   			new ConfigOprServer().sendText(tempHm);
	   			}else{
			   		int count =0;
		   			for(String val :neighbourData) 
			   		{
			   			String insertData = "'"+val.replaceAll(",","','")+"'";
			   			if(count ==0){
			   				String deletequery = "delete from n_cells_data where ip = '"+ip+"' and type='N'";
				   			co.executeDLOperation(deletequery);
				   			count++;
			   			}
			   			String query = "INSERT INTO n_cells_data(ip,flag,plmn,lac,cell,arfcn,bsic,rxs,type) values('"+ip+"',"+insertData+",'N')";
			   			co.executeDLOperation(query);
			   		}
			   			String insertData = "'"+aa[1].replaceAll(",","','")+"'";
			   			String deletequery = "delete from n_cells_data where ip = '"+ip+"' and type='S'";
			   			co.executeDLOperation(deletequery);
			   			String query = "INSERT INTO n_cells_data(ip,flag,plmn,lac,cell,arfcn,bsic,rxs,type) values('"+ip+"',"+insertData+",'S')";
			   			if(co.executeDLOperation(query)){
			   				query="update btsmaster set config_applied_status='y' where ip='"+ip+"'";
			   				co.executeDLOperation(query);
			   			}
			   			String plmn=aa[1].split(",")[1];
			   			JSONArray ja=new Operations().getJson("select oprname("+plmn+"::numeric)");
			   			HashMap<String,String> tempHm=new HashMap<String,String>();
			   			tempHm.put(ip,ja.getJSONObject(0).getString("oprname"));
			   			new ConfigOprServer().sendText(tempHm);
	   			}
	   		}else{
		   		int count =0;
	   			for(String val :neighbourData){
		   			String insertData = "'"+val.replaceAll(",","','")+"'";
		   			if(count ==0){
		   				String deletequery = "delete from n_cells_data where ip = '"+ip+"' and type='N'";
			   			co.executeDLOperation(deletequery);
			   			count++;
		   			}
		   			String query = "INSERT INTO n_cells_data(ip,flag,plmn,lac,cell,arfcn,bsic,rxs,type) values('"+ip+"',"+insertData+",'N')";
		   			co.executeDLOperation(query);
		   		}
	   		}	
			}catch(Exception e)
	   		{
				e.printStackTrace();	
			}
            return Response.status(201).entity("{}").build();
    }
   
   @POST
   @Path("/gettriggercuedata")
   @Produces(MediaType.APPLICATION_JSON)
   public Response gettTiggerCueData(@Context HttpServletRequest req,@FormParam("startTime") String startTime)
   {    
	   fileLogger.info("Inside Function : gettTiggerCueData");
	   	String query = "select date_trunc('second',process_date + '05:30:00'::interval) process_date,trigger_source,trigger_type,detail from trigger_cue order by process_date desc limit 2000";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		fileLogger.info("Exit Function : gettTiggerCueData");
		return Response.status(201).entity(rs.toString()).build();
    }
 
}
