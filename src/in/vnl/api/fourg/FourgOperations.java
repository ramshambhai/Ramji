package in.vnl.api.fourg;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.msgapp.SocketServer;
import in.vnl.api.common.*;
import in.vnl.api.common.livescreens.ConfigOprServer;

import in.vnl.api.threeg.livescreens.*;
import in.vnl.api.twog.livescreens.TrackedImsiServer;

public class FourgOperations {
	public static Integer NoOfRetriesLockUnlock;
	//public static Integer octasicPowerOffToOnTime;
	static Logger fileLogger = Logger.getLogger("file");
	static Logger statusLogger = Logger.getLogger("status");
	public static String url = "";
	static {
		url = new Common().getDbCredential().get("4gserviceurl");
	}

	public Response executeActions(LinkedHashMap<String, String> data) {
		String operation = CommandTypes.getFunctionName(data.get("cmdType"));
		if (operation == null) {
			return Response.status(201).entity("{}").build();
		}
		switch (operation) {
		case "getCurrentStausOfBts":
			return getCurrentStatusOfBts(data);
		case "setCurrentStausOfBts":
			return setCurrentStausOfBts(data);
		case "setSufiConfig":
			return setSufiConfig(data);
		case "getSufiConfig":
			return getSufiConfig(data);
		case "setCellLock":
			return setCellLock(data);
		case "setCellUnlock":
			return setCellUnlock(data);
		case "setRedirectionInfo":
			return setRedirectionInfo(data);
		case "alarmData":
			return alarmData(data);
		case "updateLac":
			return updateLac(data);
		case "udpateSubscriberTrackList":
			return udpateSubscriberTrackList(data);
		case "updateSubHold":
			return updateSubHold(data);
		case "triggerMes":
			return triggerMes(data);
		case "setSubHoldEvent":
			return setSubHoldEvent(data);
		case "setGEBMesEvent":
			return setGEBMesEvent(data);
		case "dedicatedMeasEvent":
			return dedicatedMeasEvent(data);
		case "setSubRedirectionEvent":
			return setSubRedirectionEvent(data);
		case "updateSibInfo":
			return updateSibInfo(data);
		case "releaseEvent":
			return releaseEvent(data);
		}

		return Response.status(201).entity("{}").build();
	}

	public Response setCurrentStausOfBts(LinkedHashMap<String, String> data) {
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		updateBtsStatus(data.get("data"), data.get("systemId"));
		return Response.status(201).entity("").build();
	}

	public Response getCurrentStatusOfBts(LinkedHashMap<String, String> data) {
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		// fileLogger.debug("sunil");
		Common.log(data.get("data"));
		Response rs = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,
				data.get("data"));
		String resultData = "";
		if (rs != null) {
			resultData = rs.readEntity(String.class);
			updateBtsStatus(resultData, data.get("systemIP"), data.get("id"));
		}
		Common.log(resultData);
		return Response.status(201).entity("{}").build();
		// return Response.status(201).entity("").build();
	}

	public Response setSufiConfig(LinkedHashMap<String, String> data) {
		fileLogger.info("Inside Function : setSufiConfig");
		ApiCommon ac  = new ApiCommon();
		String cmdForDsp = "fdd_lte";
		
		try {
			JSONObject js = new JSONObject(data.get("data"));
			if (!ac.checkFddOrTdd(
					js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("Freq_Band_Indicator"), null)
					.get("type").equalsIgnoreCase("FDD")) {
				cmdForDsp = "tdd_lte";
			} 
		} catch (Exception e) {
			fileLogger.error("Exception in setSufiConfig of class FourgOperations with message:" + e.getMessage());
		}
		
		if(new Operations().getJson("select * from view_btsinfo where code = 10").length() > 0)
		{

			ac.switchDsp(cmdForDsp,0,0,0,"");
			fileLogger.debug("Getting Status After System Manager Swith in case of 4g Four line 133");
			ac.updateStatusOfBts4g(data.get("systemIP"));
			
		}
		
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		Common.log(data.get("data"));
		Response rs = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,
				data.get("data"));
		String resultData = "{}";
		Common.log(resultData);
		fileLogger.info("Exit Function : setSufiConfig");
		return Response.status(201).entity(resultData).build();
		// return Response.status(201).entity("").build();
	}

	public Response setDefaultSufiConfig(HashMap<String, String> data) {
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		Common.log(data.get("data"));
		Response rs = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,
				data.get("data"));
		String resultData = "{}";
		Common.log(resultData);
		return Response.status(201).entity(resultData).build();
		// return Response.status(201).entity("").build();
	}

	public Response getSufiConfig(LinkedHashMap<String, String> data) {
		fileLogger.info("Inside Function : getSufiConfig");
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		Common.log(data.get("data"));
		Response rs = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,
				data.get("data"));
		String resultData = "{}";

		if (rs != null) {
			resultData = rs.readEntity(String.class);

			String query = "update btsmaster set config = '" + resultData + "' where sytemid =" + data.get("systemId");
			new Common().executeDLOperation(query);
			try {
				JSONObject configTree = new JSONObject(resultData);
				String mcc = configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID")
						.getString("MCC");
				String mnc = configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID")
						.getString("MNC");
				JSONArray ja = new Operations().getJson("select oprname(" + mcc + mnc + "::numeric)");
				HashMap<String, String> tempHm = new HashMap<String, String>();
				tempHm.put(data.get("systemIP"), ja.getJSONObject(0).getString("oprname"));
				new ConfigOprServer().sendText(tempHm);
			} catch (Exception E) {
				fileLogger.error("Exception in setSufiConfigOnDb of class Operations with message:" + E.getMessage());
			}
		}
		Common.log(resultData);
		fileLogger.info("Exit Function : getSufiConfig");
		return Response.status(201).entity(resultData).build();
	}

	public Response setCellLock(LinkedHashMap<String, String> data) {
		String resultData = "";
		boolean lockStatus = setCellLockUnlockDdp(data);// device driven part
		if (!lockStatus) {
			resultData = "{\"result\":\"failed\",\"message\":\"Unable to Lock Device\"}";
		} else {
			new CommonService().updateStatusOfGivenBts(data.get("systemIP"));
			resultData = "{\"result\":\"successful\",\"message\":\"Device Locked Successfully\"}";
		}
		return Response.status(201).entity(resultData).build();
		// return Response.status(201).entity("").build();
	}

	public Response setCellUnlock(LinkedHashMap<String, String> data) {
		String resultData = "";
		boolean unlockStatus = setCellLockUnlockDdp(data);// device driven part
		if (!unlockStatus) {
			
			resultData = "{\"result\":\"failed\",\"message\":\"Unable to Lock Device\"}";
		} else {
			new CommonService().updateStatusOfGivenBts(data.get("systemIP"));
			resultData = "{\"result\":\"successful\",\"message\":\"Device Locked Successfully\"}";
		}
		return Response.status(201).entity(resultData).build();
		// return Response.status(201).entity("").build();
	}

	public boolean setCellLockUnlockDdp(LinkedHashMap<String, String> data) {
		Response rs = createServerCallQueryParam(data);
		boolean lockUnlockStatus = true;
		if (rs == null) {
			lockUnlockStatus = false;
		}
		else{
			if (data.get("cmdType").equalsIgnoreCase("SET_CELL_UNLOCK"))
				new AuditHandler().audit_configuration(data.get("systemIP"));
			//{cmdType=SET_CELL_UNLOCK, systemCode=1, systemId=1125, systemIP=192.168.5.5, data={"CMD_CODE":"SET_CELL_UNLOCK","CELL_ID":"0","LAC":"0"}}
		}
		return lockUnlockStatus;
	}

	public Response setRedirectionInfo(LinkedHashMap<String, String> data) {
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
		// return Response.status(201).entity("").build();
	}

	public Response udpateSubscriberTrackList(LinkedHashMap<String, String> data) {
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
	}

	public Response updateSubHold(LinkedHashMap<String, String> data) {
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
	}

	public Response triggerMes(LinkedHashMap<String, String> data) {
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
	}

	public Response alarmData(LinkedHashMap<String, String> msgdata) {
		boolean result = new ApiCommon().storeAlarm(msgdata, "sufi");
		return new ApiCommon().createResponseMsg(result, null);
	}

	public Response updateSibInfo(LinkedHashMap<String, String> data) {
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
	}

	public Response setSubRedirectionEvent(LinkedHashMap<String, String> msgdata) {
		fileLogger.info("Inside Function : setSubRedirectionEvent");
		try {

			String jsonData = msgdata.get("data");
			Common.log("set_sub_redirection_event : " + jsonData);
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());

			String cmdCode = jo.getString("CMD_CODE");
			String subId = jo.getString("SUB_ID");
			
			float LAT = Float.parseFloat(jo.getString("LAT"));
			float LONG = Float.parseFloat(jo.getString("LONG"));
			
			int dest = jo.getInt("DEST");
			new RedirectionEvent().sendText(jsonData);
			Common co = new Common();
			String query = "insert into redirection_event (sub_id, dest,tech,lat,lon)values" + "(" + subId + "," + dest + "'lte',"+LAT+","+LONG+")";
			co.executeDLOperation(query);
		} catch (Exception E) {
			fileLogger.debug("Parsing Redirection Event Data Exception msg : " + E.getMessage());
		}
		String resultData = "{}";
		fileLogger.info("Exit Function : setSubRedirectionEvent");
		return Response.status(201).entity(resultData).build();
	}

	public Response setSubHoldEvent(LinkedHashMap<String, String> msgdata) {
		fileLogger.info("Inside Function : setSubHoldEvent");
		try {
			String jsonData = msgdata.get("data");
			Common.log("setSubHoldEvent : " + jsonData);
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());

			String cmdCode = jo.getString("CMD_CODE");
			String subId = jo.getString("SUB_ID");
			int RSRQ = jo.getInt("RSRQ");
			int RSRP = jo.getInt("RSRP");
			float lat = Float.parseFloat(jo.getString("LAT"));
			float lon = Float.parseFloat(jo.getString("LONG"));
			Common co = new Common();

			ApiCommon aco = new ApiCommon();
			JSONArray btsDetail = aco.getSufiDetail(msgdata.get("systemId"));
			String btsIp = btsDetail.getJSONObject(0).get("ip").toString();
			String deviceName = btsDetail.getJSONObject(0).get("dname").toString();

			/*new HoldAndMesEvent().sendText(msgdata.get("systemId") + "," + cmdCode + "," + btsIp + "," + deviceName
					+ "," + subId + "," + ecno + "," + rscp);*/

			String query = "insert into mes_hold_event (event_type, sub_id, RSRQ, RSRP,ip,lat,lon,tech) values" + "('" + cmdCode
					+ "'," + subId + "," + RSRQ + "," + RSRP + ",'" + btsIp + "',"+lat+","+lon+",'lte')";
			co.executeDLOperation(query);
		} catch (Exception E) {
			fileLogger.debug("Parsing setSubHoldEvent Data Exception msg : " + E.getMessage());
		}
		String resultData = "{}";
		fileLogger.info("Exit Function : setSubHoldEvent");
		return Response.status(201).entity(resultData).build();
	}

	public Response releaseEvent(LinkedHashMap<String, String> msgdata) {
		fileLogger.info("Inside Function : releaseEvent");
		try {
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();

			String jsonData = msgdata.get("data");
			Common.log("releaseEvent : " + jsonData);
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());

			String cmdCode = jo.getString("CMD_CODE");
			String subId = jo.getString("SUB_ID");
			String cause = jo.getString("REL_CAUSE");

			ApiCommon aco = new ApiCommon();
			JSONArray btsDetail = aco.getSufiDetail(msgdata.get("systemId"));
			String btsIp = btsDetail.getJSONObject(0).get("ip").toString();
			String deviceName = btsDetail.getJSONObject(0).get("dname").toString();

			// new
			// HoldAndMesEvent().sendText(msgdata.get("systemId")+","+cmdCode+","+btsIp+","+deviceName+","+subId+","+ecno+","+rscp);
			String query = "insert into release_event (ip, subid, subid_type, cause,tech) values ('" + btsIp + "','" + subId
					+ "',0,'" + cause + "','lte') returning rlease_event_id";
			int id = co.executeQueryAndReturnId(query);

			if (id <= 0) {

			} else {
				String query1 = "select * from releaseEvent(" + id + ");";

				fileLogger.debug(query1);

				smt = con.createStatement();

				ResultSet rs = smt.executeQuery(query1);
				JSONObject js = new JSONObject();
				JSONObject ja = new JSONObject();
				String sub_id = "";
				int i = 0;
				String RXLVL = "0";
				String TA = "0";
				String POWER = "0";
				String IMEI = "0";

				while (rs.next()) {

					JSONObject neighData = new JSONObject();
					JSONObject recData = new JSONObject();

					recData.put("STATUS", "2");
					recData.put("MCC", rs.getString("mcc"));
					recData.put("MNC", rs.getString("mnc"));
					recData.put("LAC", rs.getString("lac"));
					recData.put("CELL", rs.getString("cell"));
					recData.put("RXLVL", rs.getString("rssi"));

					sub_id = rs.getString("sub_id");

					RXLVL = rs.getString("rxlvl");
					TA = rs.getString("ta");
					POWER = rs.getString("POWER");
					IMEI = rs.getString("IMEI");

					ja.put("" + i++, recData);
					i++;
				}

				if (i > 2) {
					js.put("NEIGH_FLAG", "2");
					js.put("LDFLAG", "0");
					js.put("STATUS", "2");

					js.put("IMSI", sub_id);
					js.put("IMEI", IMEI);
					js.put("TA", TA);
					js.put("RXLVL", RXLVL);
					js.put("POWER", POWER);

					js.put("NEIGH_DATA", ja);
					new Operations().pollData(js, btsIp, "NA");

				}

			}

		} catch (Exception E) {
			fileLogger.error("Parsing releaseEvent Data Exception msg : " + E.getMessage());
		}
		String resultData = "{}";
		fileLogger.info("Exit Function : releaseEvent");
		return Response.status(201).entity(resultData).build();
	}

	public Response dedicatedMeasEvent(LinkedHashMap<String, String> msgdata) {
		fileLogger.info("Inside Function : dedicatedMeasEvent");
		try {
			String jsonData = msgdata.get("data");
			Common.log("dedicatedMeasEvent : " + jsonData);

			JSONObject jo = new JSONObject(jsonData);

			fileLogger.debug(jo.toString());

			String cmdCode = jo.getString("CMD_CODE");
			JSONObject params = jo.getJSONObject("PARAMS");
			String subId = params.getString("SUB_ID");
			
			int pd = params.getInt("PD");
			float lat = Float.parseFloat(params.getString("LAT"));
			float lon = Float.parseFloat(params.getString("LONG"));

			JSONArray interFreqMeas = params.getJSONArray("INTER_FREQ_MEAS");
			JSONArray intraFreqMeas = params.getJSONArray("INTRA_FREQ_MEAS");
			JSONArray interRatMeas = params.getJSONArray("INTER_RAT_MEAS");

			Common co = new Common();
			List<String> la = new ArrayList<String>();
			Long eventCode = System.currentTimeMillis();
			jo.append("eventCode", eventCode);
			new DedicatedMesEvent().sendText(jo.toString());

			ApiCommon aco = new ApiCommon();
			JSONArray btsDetail = aco.getSufiDetail(msgdata.get("systemId"));
			String btsIp = btsDetail.getJSONObject(0).get("ip").toString();
			String deviceName = btsDetail.getJSONObject(0).get("dname").toString();

			for (int i = 0; i < interFreqMeas.length(); i++) {

				String query = "INSERT INTO DEDICATEDmeasevent("
						+ "event_code, event_type, sub_id, freq_type, psc, cell, ecno,rscp, rssi, lac, mcc, mnc,ip,tech,lat,lon,pd)"
						+ "values(" + eventCode + ",'" + cmdCode + "'," + subId + ",'INTER_FREQ_MEAS'," + ""
						+ interFreqMeas.getJSONObject(i).getInt("PCI") + "," + ""
						+ interFreqMeas.getJSONObject(i).getInt("CELL_ID") + "," + ""
						+ interFreqMeas.getJSONObject(i).getInt("RSRQ") + "," + ""
						+ interFreqMeas.getJSONObject(i).getInt("RSRP") + "," + ""
						+ interFreqMeas.getJSONObject(i).getInt("RSSI") + "," + ""
						+ interFreqMeas.getJSONObject(i).getInt("TAC") + "," + ""
						+ interFreqMeas.getJSONObject(i).getJSONObject("PLMN_ID").getInt("MCC") + "," + ""
						+ interFreqMeas.getJSONObject(i).getJSONObject("PLMN_ID").getInt("MNC") + "" + ",'" + btsIp
						+ "','lte',"+lat+","+lon+","+pd+");";

				la.add(query);
			}

			for (int i = 0; i < intraFreqMeas.length(); i++) {

				String query = "INSERT INTO DEDICATEDmeasevent("
						+ "event_code, event_type, sub_id, freq_type, psc, cell, ecno,rscp,rssi,lac, mcc, mnc,ip,tech,lat,lon,pd)"
						+ "values(" + eventCode + ",'" + cmdCode + "'," + subId + ",'INTRA_FREQ_MEAS'," + ""
						+ intraFreqMeas.getJSONObject(i).getInt("PCI") + "," + ""
						+ intraFreqMeas.getJSONObject(i).getInt("CELL_ID") + "," + ""
						+ intraFreqMeas.getJSONObject(i).getInt("RSRQ") + "," + ""
						+ intraFreqMeas.getJSONObject(i).getInt("RSRP") + "," + ""
						+ intraFreqMeas.getJSONObject(i).getInt("RSSI") + "," + ""
						+ intraFreqMeas.getJSONObject(i).getInt("TAC") + "," + ""
						+ intraFreqMeas.getJSONObject(i).getJSONObject("PLMN_ID").getInt("MCC") + "," + ""
						+ intraFreqMeas.getJSONObject(i).getJSONObject("PLMN_ID").getInt("MNC") + "" + ",'" + btsIp
						+ "','lte',"+lat+","+lon+","+pd+");";

				la.add(query);
			}

			for (int i = 0; i < interRatMeas.length(); i++) {

				String query = "INSERT INTO DEDICATEDmeasevent("
						+ "event_code, event_type, sub_id, freq_type,cell,RSSI,lac,mcc,mnc,bcch_arfcn,ip,tech,lat,lon,pd,rat)" + "values("
						+ eventCode + ",'" + cmdCode + "'," + subId + ",'INTER_RAT_MEAS',"
						// +
						// ""+interRatMeas.getJSONObject(i).getString("PSC")+","
						+ "" + interRatMeas.getJSONObject(i).getInt("CELL_ID") + ","
						// +
						// ""+interRatMeas.getJSONObject(i).getString("ECNO")+","
						// +
						// ""+interRatMeas.getJSONObject(i).getString("RSCP")+","
						+ "" + interRatMeas.getJSONObject(i).getInt("RSSI") + "," + ""
						+ interRatMeas.getJSONObject(i).getInt("LAC") + "," + ""
						+ interRatMeas.getJSONObject(i).getJSONObject("PLMN_ID").getInt("MCC") + "," + ""
						+ interRatMeas.getJSONObject(i).getJSONObject("PLMN_ID").getInt("MNC") + "," + ""
						+ interRatMeas.getJSONObject(i).getInt("BCCH_ARFCN") + "" + ",'" + btsIp + "','lte',"+lat+","+lon+","+pd+","+interRatMeas.getJSONObject(i).getInt("RAT")+");";

				la.add(query);
			}

			co.executeBatchOperation(la);

		} catch (Exception E) {
			fileLogger.error("Parsing setSubHoldEvent Data Exception msg : " + E.getMessage());
		}
		String resultData = "{}";
		fileLogger.info("Exit Function : dedicatedMeasEvent");
		return Response.status(201).entity(resultData).build();
	}

	// setSubRedirectionEvent

	public Response setGEBMesEvent(LinkedHashMap<String, String> genMeasData) {
		fileLogger.info("Inside Function : setGEBMesEvent");
		final LinkedHashMap<String, String> msgdata = genMeasData;
		new Thread() {
			public void run() {

				try {
					String jsonData = msgdata.get("data");
					Common.log("setGEBMesEvent" + jsonData);
					JSONObject jo = new JSONObject(jsonData);
					fileLogger.debug(jo.toString());

					String cmdCode = jo.getString("CMD_CODE");
					JSONObject params = jo.getJSONObject("PARAMS");
					JSONObject cdrParams = jo.getJSONObject("CDR_PARAMS");

					String ta 	= Integer.toString(cdrParams.getInt("PD"));
					String imei = cdrParams.getString("IMEI");
					String power = Integer.toString(cdrParams.getInt("OUT_POWER"));
					String rxlvl = Integer.toString(cdrParams.getInt("RX_LEVEL"));

					String subId = params.getString("SUB_ID");
					placeCdrData(cdrParams, subId);
					
					JSONObject currCells = params.getJSONObject("CURRENT_CELL_MEAS");

					int rssi = currCells.getInt("RSSI");
					float lat = Float.parseFloat(params.getString("LAT"));
					float lon = Float.parseFloat(params.getString("LONG"));
					/*
					 * commented due to change in document will work after
					 * finalization of it.
					 */
					// JSONArray monitorCells =
					// params.getJSONArray("MONITORED_CELL_MEAS");
					JSONArray monitorCells = new JSONArray();
					StringBuilder columnString = new StringBuilder();
					StringBuilder valueString = new StringBuilder();

					for (int i = 0; i < monitorCells.length(); i++) {
						columnString.append(",m" + (i + 1) + "_psc,m" + (i + 1) + "_ecno");

						valueString.append("," + monitorCells.getJSONObject(i).get("PSC") + ","
								+ monitorCells.getJSONObject(i).get("ECNO"));
					}

					ApiCommon aco = new ApiCommon();
					JSONArray btsDetail = aco.getSufiDetail(msgdata.get("systemId"));
					String btsIp = btsDetail.getJSONObject(0).get("ip").toString();
					String deviceName = btsDetail.getJSONObject(0).get("dname").toString();

					/*new HoldAndMesEvent().sendText(msgdata.get("systemId") + "," + btsIp + "," + deviceName + ","
							+ cmdCode + "," + subId + "," + ecno + "," + rscp + valueString.toString());*/

					Common co = new Common();
					String query = "insert into mes_hold_event (event_type, sub_id, rssi, lat,lon,tech"
							+ columnString.toString() + ",ip,ta,imei,power,rxlvl) values" + "('" + cmdCode + "',"
							+ subId + "," + rssi + "," + lat +  "," + lon + ",'lte'" +valueString.toString() + ",'" + btsIp + "'," + ta + ",'"
							+ imei + "'," + power + "," + rxlvl + ")";
					co.executeDLOperation(query);
				} catch (Exception E) {
					fileLogger.error("Parsing setGEBMesEvent Data Exception msg : " + E.getMessage());
				}

			}
		}.start();

		String resultData = "{}";
		fileLogger.info("Exit Function : setGEBMesEvent");
		return Response.status(201).entity(resultData).build();
	}

	public void placeCdrData(JSONObject params, String imsi) {
		fileLogger.info("Inside Function : placeCdrData");
		fileLogger.debug("cdr imsi is :" + imsi);
		StringBuilder packet = new StringBuilder("4G Normal_LU ");
		try {
			packet.append("IMSI:" + imsi + " ");
			packet.append("IMEI:" + params.get("IMEI") + " ");
			packet.append("PTMSI:" + params.get("PTMSI") + " ");
			packet.append("MSISDN:NA ");
			packet.append("TMSI:" + params.get("TMSI") + " ");
			packet.append("OL:" + params.get("OLD_TAC") + " ");
			packet.append("Ta:" + params.get("PD") + " ");
			packet.append("Rxl:" + params.get("RX_LEVEL") + " ");
			packet.append("CGI:" + params.get("CGI") + " ");
			packet.append("SysLoc:" + params.get("SYSTEM_LOCATION") + " ");
			packet.append("MsLoc:" + params.get("MS_LOCATION") + " ");
			String bandNum4g=Integer.toString(params.getInt("BAND"));
			String bandName4g="1800";
			switch(bandNum4g){
			case "1":
				bandName4g="2100";
				break;
			case "3":
				bandName4g="1800";
				break;
			case "5":
				bandName4g="850";
				break;
			case "8":
				bandName4g="900";
				break;
			case "40":
				bandName4g="2300";
				break;
			case "41":
				bandName4g="2600+";
				break;
			}
			packet.append("BAND:" + bandName4g + " ");
			packet.append("UlArfcn:" + params.get("ULEARFCN") + " ");
			packet.append("DlArfcn:" + params.get("DLEARFCN") + " ");
			packet.append("OutPow:" + params.get("OUT_POWER") + " ");
			packet.append("TStmp:" + params.get("TIME_STAMP") + " ");
			packet.append("FTyp:" + params.get("FTYPE") + " ");
			packet.append("PSC:" + params.get("PCI") + " ");
			new Operations().cdrData(packet.toString(), params.get("SUFI_IP").toString(),
					params.get("COUNT").toString());
			if (!params.getString("MS_LOCATION").equalsIgnoreCase("na")) {
				String[] latLon = params.getString("MS_LOCATION").split(",");
				new Common().executeDLOperation(
						"insert into tracked_imsi(imsi,imei,ta,power,lat,lon,acc,packet,stype) values('"
								+ params.getString("IMEI") + "','" + params.getString("IMEI") + "',"
								+ params.getString("PD") + "," + params.getString("RX_LEVEL") + "," + latLon[0] + ","
								+ latLon[1] + ",'Y','','3G');");
				// new TrackedImsiServer().sendText("recived");
				fileLogger.debug("@tracked_imsi");
			}
			// insert into tracked_imsi
			// (imsi,imei,ta,power,lat,lon,acc,packet,stype)
			// values(new.imsi,new.imei,new.ta::numeric,new.rxl::numeric,split_part(new.msloc,',',1)::numeric,split_part(new.msloc,',',2)::numeric,'Y','',new.stype);

		} catch (Exception e) {
			fileLogger.error(" Funciton placeCdrData Error : " + e.getMessage());
		}
		fileLogger.info("Exit Function : placeCdrData");
	}

	public void updateStatusOfAllBts() {
		fileLogger.info("Inside Function : updateStatusOfAllBts");
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();

		try {
			smt = con.createStatement();
			String query = "select * from btsmaster";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);

			while (rs.next()) {
				LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
				param.put("cmdType", "GET_CURR_STATUS");
				param.put("systemCode", rs.getString("devicetypeid"));
				param.put("systemIP", rs.getString("ip"));
				param.put("systemId", rs.getString("sytemid"));
				param.put("id", rs.getString("b_id"));
				param.put("data", "{{\"CMD_CODE\":\"GET_CURR_STATUS\"}}");
				getCurrentStatusOfBts(param);
			}
		} catch (Exception E) {
			fileLogger.error("Exeption  updateStatusOfAllBts :" + E.getMessage());
		} finally {
			try {
				smt.close();
				con.close();
			} catch (Exception E) {

			}
		}fileLogger.info("Exit Function : updateStatusOfAllBts");
	}

	public Response updateLac(LinkedHashMap<String, String> data) {
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		Common.log("updateLac" + data.get("data"));
		updateLacForSufi(data.get("data"), data.get("systemId"));
		return Response.status(201).entity("").build();
	}

	public void updateLacForSufi(String jsonData, String id) {
		fileLogger.info("Inside Function : updateLacForSufi");
		try {

			Common.log("updateLacForSufi" + jsonData);
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());
			jo.append("SUB_ID", id);
			//new LacChangeEvent().sendText(jo.toString());
			String cmdCode = jo.getString("CMD_CODE");
			int lac = jo.getInt("TAC");
			Common co = new Common();
			// String query ="update configurations set lac="+lac+" where
			// systemid = "+id;
			// co.executeDLOperation(query);
			String query = "insert into lac_change_event (lac,sufi_id,tech) values(" + lac + "," + id + ",'lte');";
			co.executeDLOperation(query);
		} catch (Exception E) {
			fileLogger.error("Parsing Json updateBtsStatus Exception msg : " + E.getMessage());
		}
		fileLogger.info("Exit Function : updateLacForSufi");
	}

	public void updateBtsStatus(String jsonData, String id) {
		fileLogger.info("Inside Function : updateBtsStatus");
		try {
			Common.log("updateBtsStatus" + jsonData);

			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());
			String cmdCode = jo.getString("CMD_CODE");
			int nodeType = jo.getInt("NODE_TYPE");
			int nodeId = jo.getInt("NODE_ID");
			int systemStatus = jo.getInt("SYSTEM_STATUS");
			int cellStatus = jo.getInt("CELL_OP_STATE");
			int adminState = jo.getInt("CELL_ADM_STATE");
			Common co = new Common();
			String query = "update btsmaster set status=" + systemStatus + " ,cellstatus = " + cellStatus
					+ ",adminstate=" + adminState + " where sytemid = " + id;
			co.executeDLOperation(query);

		} catch (Exception E) {
			fileLogger.error("Parsing Json updateBtsStatus Exception msg : " + E.getMessage());
		}
		fileLogger.info("Exit Function : updateBtsStatus");
	}

	public void updateBtsStatus(String jsonData, String ip, String id) {
		fileLogger.info("Inside Function : updateBtsStatus");
		try {
			Common.log("updateBtsStatus" + jsonData);
			JSONObject jo = new JSONObject(jsonData);
			statusLogger.debug(jo.toString());

			String cmdCode = jo.getString("CMD_CODE");
			int nodeType = jo.getInt("NODE_TYPE");
			int nodeId = jo.getInt("NODE_ID");
			int systemStatus = jo.getInt("SYSTEM_STATUS");
			int cellStatus = jo.getInt("CELL_OP_STATE");
			int adminState = jo.getInt("CELL_ADM_STATE");
			int L1Attn = jo.getInt("L1_ATT");
			Common co = new Common();
			String query = "update btsmaster set status=" + systemStatus + " ,cellstatus = " + cellStatus
					+ ",adminstate=" + adminState +  ",tmp=" + L1Attn +" where ip = '" + ip + "' and b_id=" + id;
			statusLogger.debug(query);
			co.executeDLOperation(query);
		} catch (Exception E) {
			statusLogger.error("Parsing Json updateBtsStatus Exception msg : " + E.getMessage());
		}
		fileLogger.info("Exit Function : updateBtsStatus");
	}

	public void updateDeviceSoftVer(String jsonData, String ip, String id) {
		fileLogger.info("Inside Function : updateDeviceSoftVer");
		try {
			fileLogger.debug("updateDeviceSoftVer" + jsonData);
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());

			String swVersion = jo.getString("SW_VERSION");
			Common co = new Common();
			String query = "update btsmaster set sw_version='" + swVersion + "' where ip = '" + ip + "' and b_id=" + id;
			fileLogger.debug(query);
			co.executeDLOperation(query);
		} catch (Exception E) {
			fileLogger.debug("Parsing Json updateDeviceSoftVer Exception msg : " + E.getMessage());
		}
		fileLogger.info("Exit Function : updateDeviceSoftVer");
	}

	public void updateSystemManagerStatus(String jsonData, String ip, String id) {
		fileLogger.info("Inside Function : updateSystemManagerStatus");
		//fileLogger.debug("in updateSystemManagerStatus");
		try {
			Common.log("updateBtsStatus" + jsonData);
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());
			int systemStatus = jo.getInt("STATUS_CODE");
			Common co = new Common();
			String query = "update btsmaster set status=" + systemStatus + " where ip = '" + ip + "' and b_id=" + id;
			co.executeDLOperation(query);
		} catch (Exception E) {
			fileLogger.error("Parsing Json updateSystemManagerStatus Exception msg : " + E.getMessage());
		}
		//fileLogger.debug("out updateSystemManagerStatus");
		fileLogger.info("Exit Function : updateSystemManagerStatus");
	}

	public Response createServerCallQueryParam(LinkedHashMap<String, String> data) {
		fileLogger.info("Inside Function : createServerCallQueryParam");
		if (NoOfRetriesLockUnlock==null)
		     NoOfRetriesLockUnlock=0;
	   int numberRetry=0;
	   String resp="";
	   Response rs =null;
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		Common.log(data.get("data"));
		//Response rs = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,data.get("data"));
		try
		{
		  while(rs==null && numberRetry <=NoOfRetriesLockUnlock) {
			  fileLogger.debug("Retrying.. . Retry Number /Total Retries= " + numberRetry + " / "+ NoOfRetriesLockUnlock);
			  //res =   webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(data));
			  try
			  {
			  rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
			  }
			  catch (Exception e)
			  {
				  fileLogger.error("Parsing Json updateSystemManagerStatus Exception msg : "+e.getMessage());
			  }
			  numberRetry++;
			  }
		}
		catch (Exception e)
		{
			fileLogger.error("Parsing Json updateSystemManagerStatus Exception msg : "+e.getMessage());
		}
		//Response r = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,
				//data.get("data"));
		fileLogger.info("Exit Function : createServerCallQueryParam");
		return rs;

	}

	public void set2gHoldStatus(String sub_id) {
		fileLogger.info("Inside Function : set2gHoldStatus");
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();

		try {
			smt = con.createStatement();
			String query = "select * from btsmaster where devicetypeid = 1";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);

			while (rs.next()) {
				LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
				param.put("cmdType", "SET_SUB_2G_HOLD_STATUS");
				param.put("systemCode", rs.getString("devicetypeid"));
				param.put("systemIP", rs.getString("ip"));
				param.put("systemId", rs.getString("sytemid"));
				param.put("id", rs.getString("b_id"));
				param.put("data", "{\"CMD_CODE\":\"SET_SUB_2G_HOLD_STATUS\",\"SUB_ID\":\"" + sub_id + "\"}");

				send2gHoldStatus(param);
			}
		} catch (Exception E) {
			fileLogger.error("Exeption  updateStatusOfAllBts :" + E.getMessage());
		} finally {
			try {
				smt.close();
				con.close();
			} catch (Exception E) {

			}
		}	fileLogger.info("Exit Function : set2gHoldStatus");

	}

	public Response send2gHoldStatus(LinkedHashMap<String, String> data) {
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		Common.log("send2gHoldStatus" + data.get("data"));
		Response rs = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,
				data.get("data"));
		return rs;
	}

	public Response sendToServer(LinkedHashMap<String, String> data) {
		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();
		queryParam.put("CMD_TYPE", data.get("cmdType"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));
		Response rs = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,
				data.get("data"));
		String resultData = "";
		if (rs == null) {
			return Response.status(201).entity(rs.readEntity(String.class)).build();
		} else {
			return Response.status(201).entity(resultData).build();
		}

		// return Response.status(201).entity("").build();
	}
	
	public Response setSufiConfigAuto(LinkedHashMap<String,String> data)
	{
		fileLogger.info("Inside Function : setSufiConfigAuto");
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		Common.log(data.get("data"));
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "{}";
		Common.log(resultData);
		fileLogger.info("Exit Function : setSufiConfigAuto");
		return Response.status(201).entity(resultData).build();
		//return Response.status(201).entity("").build();
	}

}
