	package in.vnl.api.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;

public class PossibleConfigurations 
{
	static Logger fileLogger = Logger.getLogger("file");
	public String startTime;
	public String stopTime;
	int[] plmn;
	
	public String arfcnFilterValue = null;
	public String cellFilterValue = null;
	
	public String uarfcnFilterValue = null;
	public String earfcnFilterValue= null;
	public int antennaId = -1;
	public boolean isManualTrackingEnabled=false;
	public int tmpantennaId = -1;
	
	public PossibleConfigurations()
	{
		
	}
	
	PossibleConfigurations(String startTime,String stopTime ,int[] plmn)
	{
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.plmn = plmn;
	}
	
	/** 
	 * Entry Point for creating the configurations takes input as array of plmn
	 * Returns all the possible configurations
	 * */	
	public String startWithPlmns(ArrayList<String> oprs,int antennaId) 
	{
		fileLogger.info("Inside Function : startWithPlmns");
		//Holds the configurations for all the given cells
		HashMap<String,ArrayList<ConfigData>> returnConfiguration = new HashMap<String,ArrayList<ConfigData>>();
				
		//Will Collect all Tech(GSM,UMTS) packets and merge in one
		ArrayList<ConfigData> configurations = new ArrayList<ConfigData>();
		try 
		{
			
			ArrayList<String> PLMNS = oprs;
			
			//ids of for every config packet.It will start from one and increment by one for every packet
			int[] packetId = {1};
			
			//merging configuration packets in one list  
			configurations.addAll(create2gConfigurationPackets(packetId,PLMNS,antennaId));
			configurations.addAll(create3gConfigurationPackets(packetId,PLMNS,antennaId));
			configurations.addAll(create4gConfigurationPackets(packetId,PLMNS,antennaId));
			
			configurations=checkCellParamsOnOprlogsCurrent(configurations,antennaId);
			configurations=reorderListAsPerPriority(configurations);
			returnConfiguration.put("config_data", configurations);
			
			//Converting to Json and return 
			return convertToJson(returnConfiguration);
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = start");
			E.printStackTrace();
		//	fileLogger.debug("******************************************************");
		}
		
		//return blank
		fileLogger.info("Exit Function : startWithPlmns");
		return "[]";	
	}
	
	 public ArrayList<ConfigData> reorderListAsPerPriority(ArrayList<ConfigData> configurations){
			fileLogger.info("Inside Function : reorderListAsPerPriority");
		 ArrayList<ConfigData> reorderedConfigList = new ArrayList<ConfigData>();
		 ArrayList<ConfigData> highlightedConfigList = new ArrayList<ConfigData>();
		 ArrayList<ConfigData> nonHighlightedConfigList = new ArrayList<ConfigData>();
		 
		 try{
			String currentPlmn="";
		for(ConfigData confData:configurations){
			ArrayList< HashMap<String,String>> data=confData.getData();
			//confData.setCheck("no");
			
			for(HashMap<String,String> confMap:data){
				if(confMap.get("flag").equals("self")){
				String plmn=confMap.get("plmn");
				if(!currentPlmn.equals(plmn)){
					currentPlmn=plmn;
					reorderedConfigList.addAll(nonHighlightedConfigList);
					reorderedConfigList.addAll(highlightedConfigList);
					nonHighlightedConfigList.clear();
					highlightedConfigList.clear();
				}
				if(confData.getCheck().equals("yes")){
					highlightedConfigList.add(confData);
				}else{
					nonHighlightedConfigList.add(confData);
				}
				}
				
			}
			}
		reorderedConfigList.addAll(nonHighlightedConfigList);
		reorderedConfigList.addAll(highlightedConfigList);
		 }catch(Exception E){
			 	//fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = reorderListAsPerPriority");
				E.printStackTrace();
				//fileLogger.debug("******************************************************"); 
		 }
		 //fileLogger.debug("*******************************************************************");
		 fileLogger.debug("configuration size in end:"+reorderedConfigList.size());
		// fileLogger.debug("*******************************************************************");
			fileLogger.info("Exit Function : reorderListAsPerPriority");
		 return reorderedConfigList; 
	 }
	 
	/** 
	 * Entry Point for creating the configurations
	 * Rerurns All the posible configurations
	 * */	
	public String start(String[] oprs,String antennaList,String celllist) 
	{
		fileLogger.info("Inside Function : start");
		//Holds the configurations for all the given cells
		HashMap<String,ArrayList<ConfigData>> returnConfiguration = new HashMap<String,ArrayList<ConfigData>>();
		ArrayList<String> PLMNS = getPlmns(oprs);
		String[] antennaArr = antennaList.split(",");		
		//Will Collect all Tech(GSM,UMTS) packets and merge in one
		ArrayList<ConfigData> finalConfigurations =new ArrayList<ConfigData>();
		this.isManualTrackingEnabled=true;
		try 
		{
			
			//String[] PLMNS = {"40404"};			
			//ArrayList<String> PLMNS = getPlmns();
			//Getting all the plmns for the provided operator names
			
			//ids of for every config packet.It will start from one and increment by one for every packet
			int[] packetId = {1};
			for(String antennaId:antennaArr){
			int antenna=Integer.parseInt(antennaId);
			//merging configuration packets in one list
		    this.antennaId=antenna;
		    
		    if(celllist!=null  )
			{
				celllist=celllist.replace(",", "','");
		    }
		    this.cellFilterValue=celllist;
		    ArrayList<ConfigData> configurations = new ArrayList<ConfigData>();
			configurations.addAll(create2gConfigurationPackets(packetId,PLMNS,antenna));
			configurations.addAll(create3gConfigurationPackets(packetId,PLMNS,antenna));
			configurations.addAll(create4gConfigurationPackets(packetId,PLMNS,antenna));
			configurations=checkCellParamsOnOprlogsCurrent(configurations,antenna);
			finalConfigurations.addAll(configurations);
			}
			returnConfiguration.put("config_data", finalConfigurations);
			
			//Converting to Json and return 
			return convertToJson(returnConfiguration);
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = start");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}
		fileLogger.info("Exit Function : start");
		//return blank
		return "[]";	
	}
	
	/*
	 * Creates Possible configurations for 2G(GSM) packets 
	 * Input:-Current sequence(packet Id),ArrayList of PLMNS
	 * */
	public ArrayList<ConfigData> create2gConfigurationPackets(int[] packetId,ArrayList<String> PLMNS,int antennaId)
	{
		fileLogger.info("Inside Function : create2gConfigurationPackets");
		//Used to merge all the configurations in to one list for each selected cells(PLMN)
		ArrayList<ConfigData> configurations = new ArrayList<ConfigData>();
				
		try 
		{
			//Self reference of the class
			//PossibleConfigurations pb = new PossibleConfigurations();
			
			
			//Getting all the scanned cells for the current operation id 

			
			
			//if(scannedCells.size()>0) 
			//{
				for(String plmn:PLMNS) 
				{
					int actualAntennaId = this.antennaId;
					this.antennaId =-1;
					//String actualCellFilterValue=this.cellFilterValue;
					//this.cellFilterValue="-1";
					ArrayList<Cell> scannedCells = getScannedCells("GSM");
					//SAnjay and Vaibhav 31 Dec 2020
					ArrayList<Cell> scannedCellsUMTS =new ArrayList<Cell>();
					if(!this.isManualTrackingEnabled)
					{  
						 
						scannedCellsUMTS = getScannedCells("UMTS");
						
					}
					//this.cellFilterValue=actualCellFilterValue;
					this.antennaId =actualAntennaId;
					ArrayList<Cell> scannedSectorCells = getScannedCells("GSM");
					
					ArrayList<Integer> totalArfcn = getDistinctfcn("ARFCN",plmn);
					
					//Filtering all the cells for matching plmn from list of all scaned Cells
					ArrayList<Cell> cellsForGivenPlmn = getCellsForGivenPLMN(scannedCells,plmn);
					ArrayList<Cell> cellsForGivenPlmn2gNeigh = getCellsForGivenPLMN(scannedCellsUMTS,plmn);
					
					scannedSectorCells = getCellsForGivenPLMN(scannedSectorCells,plmn);
					
					cellsForGivenPlmn=getDistinct2GCell(cellsForGivenPlmn);
					
					
					
					ArrayList<Cell> tempList = new ArrayList<Cell>();
					
					
					for(Cell cell:scannedSectorCells)
					{
						for(Cell cell2:cellsForGivenPlmn)
						{
							if(cell.getMNC().equals(cell2.getMNC()) &&cell.getMCC().equalsIgnoreCase(cell2.getMCC()) && cell.getLAC().equals(cell2.getLAC())  && cell.getCELL().equals(cell2.getCELL()) && cell.getArfcn() == cell2.getArfcn())
							{
								//cells.add((Cell)cell.clone());
								tempList.add((Cell)cell2.clone());
							}
						}
					}
					scannedSectorCells = tempList;
					
					
					try
					{
						if(cellsForGivenPlmn.size() >0) 
						{
							//Getting list of all the present neighbours arfcns form the cells  for matching plmn
							
							NeighbourArfcn[] neigboursArfcn = getNeighbourArfcnList(cellsForGivenPlmn,cellsForGivenPlmn2gNeigh);
						    HashMap<String,NeighbourArfcn[]> neigboursArfcnMap= getNeighbourArfcnMap(neigboursArfcn,cellsForGivenPlmn);
						    //HashMap<String,NeighbourArfcn[]> neigboursArfcnMapRemoveDuplicate= getNeighbourArfcnMapRemoveDuplicate(neigboursArfcnMap);
							
						    //Sorting List of cells of matching plmn in descenting order of rssi
							Cell[] tempCells= new Cell[scannedSectorCells.size()];
							tempCells = scannedSectorCells.toArray(tempCells);
							//SANJAY AND VAIBHAV 31 Dec 2020 4 PM
							//Arrays.sort(tempCells, Cell.rssiDescending);
							Arrays.sort(tempCells, Cell.rssiAscending);
							scannedSectorCells = new ArrayList<>(Arrays.asList(tempCells));
							
							//Getting all the unique scanned cells from the all scanned cells list
							ArrayList<Cell> uniqueScanedCellsList = getUniqueCellsHavingMaxRssi(scannedCells);
							
							//Gelling all the Self Cells for configuration
							//ArrayList<Cell> selfCells = getSelfCellsForConfiguration(cellsForGivenPlmn, neigboursArfcn, plmn);
							//ArrayList<Cell> selfCells = getSelfCellsForConfiguration2G(cellsForGivenPlmn, neigboursArfcnMap, plmn);
							
							//ArrayList<Cell> selfCells = getSelfCellsForConfiguration2G(scannedSectorCells, neigboursArfcnMap, plmn,totalArfcn);
							ArrayList<Cell> selfCells =new ArrayList<Cell>();
							if(this.isManualTrackingEnabled)
							{
							
								 selfCells = getSelfCellsForConfiguration2GUpdated(scannedSectorCells, neigboursArfcnMap, plmn,totalArfcn);
							}
							else {
								 selfCells = getSelfCellsForConfiguration2G(scannedSectorCells, neigboursArfcnMap, plmn,totalArfcn);
							}
							//Getting current System Possition				
							JSONObject possition = getSystemPossition();
							
							//Getting neighbours for each self cell
							ArrayList<Cell> Configuration = getNeighbourCells(uniqueScanedCellsList, selfCells, Double.parseDouble(possition.getString("lat")), Double.parseDouble(possition.getString("lon")),"GSM");
							
							
							configurations.addAll(genrateJson(Configuration,packetId,"2G",antennaId));
							
							/*for(Cell cell:selfCells) 
							{
								fileLogger.debug(cell.getRssi()+"-"+cell.getArfcn());
							}*/
							
							fileLogger.debug("done 2g");
						}
						else 
						{
							throw new Exception("No Matching Cells Found For "+plmn+" ,TECH : GSM");
						}
					}
					catch(Exception E) 
					 {
					 	//fileLogger.debug("******************************************************");
						fileLogger.debug("Class = PossibleConfigurations  Method = create2gConfigurationPackets");
						E.printStackTrace();
						//fileLogger.debug("******************************************************");
					 }
					
					
				}
			//}
/*			else 
			{
				throw new Exception("No Scanned Cells Found For GSM");
			}*/
						
		}
		catch(Exception E) 
		 {
		 	//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = create2gConfigurationPackets");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		 }
		fileLogger.info("Exit Function : create2gConfigurationPackets");
		return configurations;
	}
	

	public HashMap<String,NeighbourArfcn[]> getNeighbourArfcnMap(NeighbourArfcn[] neigboursArfcn,ArrayList<Cell> cellsForGivenPlmn){
		HashMap<String,NeighbourArfcn[]> neighbourArfcnMap=new HashMap<String,NeighbourArfcn[]>();
		ArrayList<NeighbourArfcn> arfcnAndCountSelfCell = new ArrayList<NeighbourArfcn>();
		ArrayList<NeighbourArfcn> arfcnAndCountNeighbourCell = new ArrayList<NeighbourArfcn>(); 
		
		int[] statusArr = new int[neigboursArfcn.length];
		for(int statusCount=0;statusCount<neigboursArfcn.length;statusCount++){
			statusArr[statusCount]=0;
		}
		for(int i=0;i<neigboursArfcn.length;i++){
			for(Cell cell:cellsForGivenPlmn){
				if(neigboursArfcn[i].getArfcn()==cell.getArfcn()){
					arfcnAndCountSelfCell.add(neigboursArfcn[i]);
					statusArr[i]=1;
					break;
				}
			}
		}
		
		for(int y=0;y<neigboursArfcn.length;y++){
			if(statusArr[y]==0){
				arfcnAndCountNeighbourCell.add(neigboursArfcn[y]);
			}
		}
		
		NeighbourArfcn[] arfcnAndCountSelfCellArr= new NeighbourArfcn[arfcnAndCountSelfCell.size()];
		arfcnAndCountSelfCellArr = arfcnAndCountSelfCell.toArray(arfcnAndCountSelfCellArr);	
		NeighbourArfcn[] arfcnAndCountNeighbourCellArr= new NeighbourArfcn[arfcnAndCountNeighbourCell.size()];
		arfcnAndCountNeighbourCellArr = arfcnAndCountNeighbourCell.toArray(arfcnAndCountNeighbourCellArr);	
		
		neighbourArfcnMap.put("arfcnAndCountSelfCell", arfcnAndCountSelfCellArr);
		neighbourArfcnMap.put("arfcnAndCountNeighbourCell", arfcnAndCountNeighbourCellArr);
		
		return neighbourArfcnMap;
	}
	
	public HashMap<String,NeighbourUarfcn[]> getNeighbourUarfcnMap(NeighbourUarfcn[] neigboursUarfcn,ArrayList<Cell> cellsForGivenPlmn){
		HashMap<String,NeighbourUarfcn[]> neighbourUarfcnMap=new HashMap<String,NeighbourUarfcn[]>();
		ArrayList<NeighbourUarfcn> uarfcnAndCountSelfCell = new ArrayList<NeighbourUarfcn>();
		ArrayList<NeighbourUarfcn> uarfcnAndCountNeighbourCell = new ArrayList<NeighbourUarfcn>(); 
/*		int j=0;
		for(Cell cell:cellsForGivenPlmn)
		{
			for(int i=0;i<neigboursUarfcn.length;i++)
			{
				if(neigboursUarfcn[i].getUarfcn()==cell.getUarfcn() && neigboursUarfcn[i].getPsc()==cell.getPsc()){
					uarfcnAndCountSelfCell.add(neigboursUarfcn[i]);
					if(i>j)
						i=j+1;
					break;
				}
				else
				{
					if(i >= j)
						uarfcnAndCountNeighbourCell.add(neigboursUarfcn[i]);
					
					//break;
					
				}
			}
		}*/
		
		int[] statusArr=new int[neigboursUarfcn.length];
		
		
		for(int statusCount=0;statusCount<neigboursUarfcn.length;statusCount++){
			statusArr[statusCount]=0;
		}
		
		for(int i=0;i<neigboursUarfcn.length;i++)
		{
			for(Cell cell:cellsForGivenPlmn)
			{
				if(neigboursUarfcn[i].getUarfcn()==cell.getUarfcn() && neigboursUarfcn[i].getPsc()==cell.getPsc()){
					uarfcnAndCountSelfCell.add(neigboursUarfcn[i]);
					statusArr[i]=1;
					break;
				}
					
			}
		}
		
		for(int y=0;y<neigboursUarfcn.length;y++){
			if(statusArr[y]==0){
				uarfcnAndCountNeighbourCell.add(neigboursUarfcn[y]);
			}
		}
		
		NeighbourUarfcn[] uarfcnAndCountSelfCellArr= new NeighbourUarfcn[uarfcnAndCountSelfCell.size()];
		uarfcnAndCountSelfCellArr = uarfcnAndCountSelfCell.toArray(uarfcnAndCountSelfCellArr);	
		NeighbourUarfcn[] uarfcnAndCountNeighbourCellArr= new NeighbourUarfcn[uarfcnAndCountNeighbourCell.size()];
		uarfcnAndCountNeighbourCellArr = uarfcnAndCountNeighbourCell.toArray(uarfcnAndCountNeighbourCellArr);	
		
		neighbourUarfcnMap.put("uarfcnAndCountSelfCell", uarfcnAndCountSelfCellArr);
		neighbourUarfcnMap.put("uarfcnAndCountNeighbourCell", uarfcnAndCountNeighbourCellArr);
		
		return neighbourUarfcnMap;
	}
	
	public HashMap<String,NeighbourEarfcn[]> getNeighbourEarfcnMap(NeighbourEarfcn[] neigboursEarfcn,ArrayList<Cell> cellsForGivenPlmn){
		HashMap<String,NeighbourEarfcn[]> neighbourEarfcnMap=new HashMap<String,NeighbourEarfcn[]>();
		ArrayList<NeighbourEarfcn> earfcnAndCountSelfCell = new ArrayList<NeighbourEarfcn>();
		ArrayList<NeighbourEarfcn> earfcnAndCountNeighbourCell = new ArrayList<NeighbourEarfcn>(); 
/*		int j=0;
		for(Cell cell:cellsForGivenPlmn)
		{
			for(int i=0;i<neigboursUarfcn.length;i++)
			{
				if(neigboursUarfcn[i].getUarfcn()==cell.getUarfcn() && neigboursUarfcn[i].getPsc()==cell.getPsc()){
					uarfcnAndCountSelfCell.add(neigboursUarfcn[i]);
					if(i>j)
						i=j+1;
					break;
				}
				else
				{
					if(i >= j)
						uarfcnAndCountNeighbourCell.add(neigboursUarfcn[i]);
					
					//break;
					
				}
			}
		}*/
		
		int[] statusArr=new int[neigboursEarfcn.length];
		
		
		for(int statusCount=0;statusCount<neigboursEarfcn.length;statusCount++){
			statusArr[statusCount]=0;
		}
		
		for(int i=0;i<neigboursEarfcn.length;i++)
		{
			for(Cell cell:cellsForGivenPlmn)
			{
				if(neigboursEarfcn[i].getEarfcn()==cell.getEarfcn() && neigboursEarfcn[i].getPci()==cell.getPci()){
					earfcnAndCountSelfCell.add(neigboursEarfcn[i]);
					statusArr[i]=1;
					break;
				}
					
			}
		}
		
		for(int y=0;y<neigboursEarfcn.length;y++){
			if(statusArr[y]==0){
				earfcnAndCountNeighbourCell.add(neigboursEarfcn[y]);
			}
		}
		
		NeighbourEarfcn[] earfcnAndCountSelfCellArr= new NeighbourEarfcn[earfcnAndCountSelfCell.size()];
		earfcnAndCountSelfCellArr = earfcnAndCountSelfCell.toArray(earfcnAndCountSelfCellArr);	
		NeighbourEarfcn[] earfcnAndCountNeighbourCellArr= new NeighbourEarfcn[earfcnAndCountNeighbourCell.size()];
		earfcnAndCountNeighbourCellArr = earfcnAndCountNeighbourCell.toArray(earfcnAndCountNeighbourCellArr);	
		
		neighbourEarfcnMap.put("earfcnAndCountSelfCell", earfcnAndCountSelfCellArr);
		neighbourEarfcnMap.put("earfcnAndCountNeighbourCell", earfcnAndCountNeighbourCellArr);
		
		return neighbourEarfcnMap;
	}
	
	/*
	 * Creates Possible configurations for 3G(UMTS) packets 
	 * Input:-Current sequence(packet Id),ArrayList of PLMNS
	 * */
	public ArrayList<ConfigData> create3gConfigurationPackets(int[] packetId,ArrayList<String> PLMNS,int antennaId)
	{
		fileLogger.info("Inside Function : create3gConfigurationPackets");

		//Used to merge all the configurations in to one list for each selected cells
		ArrayList<ConfigData> configurations = new ArrayList<ConfigData>();
		
		try 
		{
			
			//Getting all the scanned cells for the current operation id 
			

			
			
			//if(scannedCells.size()>0) 
			//{
				for(String plmn:PLMNS) 
				{
					int actualAntennaId = this.antennaId;
					this.antennaId =-1;
					ArrayList<Cell> scannedCells = getScannedCells("UMTS");
					ArrayList<Cell> scannedCellsGSM = getScannedCells("GSM");
				//	fileLogger.debug("******************total cells*************************");
						fileLogger.debug(scannedCells.toString());
					//fileLogger.debug("******************total cells*************************");
					this.antennaId =actualAntennaId;
					ArrayList<Cell> scannedSectorCells = getScannedCells("UMTS");
				////	fileLogger.debug("******************total sectored cells*************************");
						fileLogger.debug(scannedSectorCells.toString());
					//fileLogger.debug("******************total sectored cells*************************");
					ArrayList<Integer> totalUarfcn = getDistinctfcn("UARFCN",plmn);
					
					//Filtering all the cells for matching plmn from list of all scaned Cells
					ArrayList<Cell> cellsForGivenPlmn = getCellsForGivenPLMN(scannedCells,plmn);
					ArrayList<Cell> cellsForGivenPlmn3gNeigh = getCellsForGivenPLMN(scannedCellsGSM,plmn);
					
					scannedSectorCells = getCellsForGivenPLMN(scannedSectorCells,plmn);
					
					cellsForGivenPlmn=getDistinct3GCell(cellsForGivenPlmn);
					
					ArrayList<Cell> tempList = new ArrayList<Cell>();
					
					//System.out.println("***********cells***********************");
					for(Cell cell:cellsForGivenPlmn)
					{
						System.out.println(cell.toString());
					}
					///System.out.println("***********cells***********************");
					
					//System.out.println("***********sector cells***********************");
					for(Cell cell:scannedSectorCells)
					{
						System.out.println(cell.toString());
					}
				//	System.out.println("***********sector cells***********************");
					
					for(Cell cell:scannedSectorCells)
					{
						for(Cell cell2:cellsForGivenPlmn)
						{
							if(cell.getMNC().equals(cell2.getMNC()) &&cell.getMCC().equalsIgnoreCase(cell2.getMCC()) && cell.getLAC().equals(cell2.getLAC())  && cell.getCELL().equals(cell2.getCELL()) && cell.getUarfcn() == cell2.getUarfcn() && cell.getPsc() == cell2.getPsc())
							{
								//cells.add((Cell)cell.clone());
								tempList.add((Cell)cell2.clone());
							}
						}
					}
					scannedSectorCells = tempList;
					
					
					try
					{
						if(cellsForGivenPlmn.size() >0) 
						{
							//Getting list of all the present neighbours uarfcns form the cells  for matching plmn
							NeighbourUarfcn[] neigboursUarfcn = getNeighbourUarfcn(cellsForGivenPlmn,cellsForGivenPlmn3gNeigh);
							
							for(NeighbourUarfcn na:neigboursUarfcn)
							{
								System.out.println(na.getUarfcn()+"-"+na.getPsc()+"-"+na.getCount());
							}
							
							//Creating two list of self and neighbout uarfcn+psc
							HashMap<String,NeighbourUarfcn[]> neigboursUarfcnMap= getNeighbourUarfcnMap(neigboursUarfcn,cellsForGivenPlmn);
							//HashMap<String,NeighbourUarfcn[]> neigboursUarfcnMap= getNeighbourUarfcnMap(neigboursUarfcn,scannedSectorCells);
							System.out.println("self list");
							for(NeighbourUarfcn na:neigboursUarfcnMap.get("uarfcnAndCountSelfCell"))
							{
								System.out.println(na.getUarfcn()+"-"+na.getPsc()+"-"+na.getCount());
							}
							System.out.println("ne list");
							for(NeighbourUarfcn na:neigboursUarfcnMap.get("uarfcnAndCountNeighbourCell"))
							{
								System.out.println(na.getUarfcn()+"-"+na.getPsc()+"-"+na.getCount());
							}
							
							//Sorting List of cells of matching plmn in descending order of rssi
							Cell[] tempCells= new Cell[scannedSectorCells.size()];
							tempCells = scannedSectorCells.toArray(tempCells);				
							//SANJAY AND VAIBHAV 31 DEC 2020 4 PM 
							//Arrays.sort(tempCells, Cell.rssiDescending);
							Arrays.sort(tempCells, Cell.rssiAscending);				
							scannedSectorCells = new ArrayList<>(Arrays.asList(tempCells));
							
							//Getting all the uniqe scanned cells from the all scanned cells list
							ArrayList<Cell> uniqueScanedCellsList = getUniqueCellsHavingMaxRssi3g(scannedCells);
							
							//Gelling all the Self Cells for configuration
							//ArrayList<Cell> selfCells = getSelfCellsFor3gConfiguration(cellsForGivenPlmn, neigboursUarfcn, plmn);
							//ArrayList<Cell> selfCells = getSelfCellsForConfiguration3G(cellsForGivenPlmn, neigboursUarfcnMap, plmn);
							
							//ArrayList<Cell> selfCells = getSelfCellsForConfiguration3G(scannedSectorCells, neigboursUarfcnMap, plmn,totalUarfcn);
							ArrayList<Cell> selfCells =new ArrayList<Cell>();
							if(this.isManualTrackingEnabled)
							{  
									selfCells = getSelfCellsForConfiguration3GUpdated(scannedSectorCells, neigboursUarfcnMap, plmn,totalUarfcn);
							}
							else {
								 
									 selfCells = getSelfCellsForConfiguration3G(scannedSectorCells, neigboursUarfcnMap, plmn,totalUarfcn);
									
							}
							//Getting current System Possition
							JSONObject possition = getSystemPossition();
							
							//Getting neighbours for each self cell
							ArrayList<Cell> Configuration = getNeighbourCells(uniqueScanedCellsList, selfCells, Double.parseDouble(possition.getString("lat")), Double.parseDouble(possition.getString("lon")),"UMTS");
							
										
							configurations.addAll(genrateJson(Configuration,packetId,"3G",antennaId));
							
							/*for(Cell cell:selfCells) 
							{
								fileLogger.debug(cell.getRssi()+"-"+cell.getArfcn());
							}*/
							fileLogger.debug("done 3g");
						}
						else 
						{
							throw new Exception("No Matching Cells Found For "+plmn+" ,TECH : UMTS");
						}
				
					}
					catch(Exception E) 
					{
					 	//fileLogger.debug("******************************************************");
						fileLogger.debug("Class = PossibleConfigurations  Method = create3gConfigurationPackets");
						E.printStackTrace();
						//fileLogger.debug("******************************************************");
					}
					
				}
/*			}
			else 
			{
				throw new Exception("No Scanned Cells Found For UMTS");
			}*/
			
				
		}
		catch(Exception E) 
		 {
		 	//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = create3gConfigurationPackets");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		 }
		fileLogger.info("Exit Function : create3gConfigurationPackets");
		return configurations;
	}
	
	
	/*
	 * Creates Possible configurations for 4G(LTE) packets 
	 * Input:-Current sequence(packet Id),ArrayList of PLMN'S
	 * */
	public ArrayList<ConfigData> create4gConfigurationPackets(int[] packetId,ArrayList<String> PLMNS,int antennaId)
	{
		fileLogger.info("Inside Function : create4gConfigurationPackets");
		//Used to merge all the configurations in to one list for each selected cells
		ArrayList<ConfigData> configurations = new ArrayList<ConfigData>();
		
		try 
		{
			
			//Getting all the scanned cells for the current operation id 
			

			
			
			//if(scannedCells.size()>0) 
			//{
				for(String plmn:PLMNS) 
				{
					int actualAntennaId = this.antennaId;
					this.antennaId =-1;
					ArrayList<Cell> scannedCells = getScannedCells("LTE");
					//ArrayList<Cell> scannedCellsGSM = getScannedCells("GSM");
					//fileLogger.debug("******************total cells*************************");
						fileLogger.debug(scannedCells.toString());
					//fileLogger.debug("******************total cells*************************");
					this.antennaId =actualAntennaId;
					ArrayList<Cell> scannedSectorCells = getScannedCells("LTE");
					//fileLogger.debug("******************total sectored cells*************************");
						fileLogger.debug(scannedSectorCells.toString());
					//fileLogger.debug("******************total sectored cells*************************");
					ArrayList<Integer> totalEarfcn = getDistinctfcn("EARFCN",plmn);
					
					//Filtering all the cells for matching plmn from list of all scaned Cells
					ArrayList<Cell> cellsForGivenPlmn = getCellsForGivenPLMN(scannedCells,plmn);
					//ArrayList<Cell> cellsForGivenPlmn3gNeigh = getCellsForGivenPLMN(scannedCellsGSM,plmn);
					
					scannedSectorCells = getCellsForGivenPLMN(scannedSectorCells,plmn);
					
					//cellsForGivenPlmn=getDistinct3GCell(cellsForGivenPlmn);
					cellsForGivenPlmn=getDistinct4GCell(cellsForGivenPlmn);
					
					ArrayList<Cell> tempList = new ArrayList<Cell>();
					
					//System.out.println("***********cells***********************");
					for(Cell cell:cellsForGivenPlmn)
					{
						System.out.println(cell.toString());
					}
					//System.out.println("***********cells***********************");
					
					//System.out.println("***********sector cells***********************");
					for(Cell cell:scannedSectorCells)
					{
						System.out.println(cell.toString());
					}
					//System.out.println("***********sector cells***********************");
					
					for(Cell cell:scannedSectorCells)
					{
						for(Cell cell2:cellsForGivenPlmn)
						{
							if(cell.getMNC().equals(cell2.getMNC()) &&cell.getMCC().equalsIgnoreCase(cell2.getMCC()) && cell.getTac().equals(cell2.getTac())  && cell.getCELL().equals(cell2.getCELL()) && cell.getEarfcn() == cell2.getEarfcn() && cell.getPci() == cell2.getPci())
							{
								//cells.add((Cell)cell.clone());
								tempList.add((Cell)cell2.clone());
							}
						}
					}
					scannedSectorCells = tempList;
					
					
					try
					{
						if(cellsForGivenPlmn.size() >0) 
						{
							//Getting list of all the present neighbours earfcn's from the cells  for matching plmn
							NeighbourEarfcn[] neigboursEarfcn = getNeighbourEarfcn(cellsForGivenPlmn);
							
							for(NeighbourEarfcn na:neigboursEarfcn)
							{
								System.out.println(na.getEarfcn()+"-"+na.getPci()+"-"+na.getCount());
							}
							
							//Creating two list of self and neighbout uarfcn+psc
							HashMap<String,NeighbourEarfcn[]> neigboursEarfcnMap= getNeighbourEarfcnMap(neigboursEarfcn,cellsForGivenPlmn);
							//HashMap<String,NeighbourUarfcn[]> neigboursUarfcnMap= getNeighbourUarfcnMap(neigboursUarfcn,scannedSectorCells);
							System.out.println("self list");
							for(NeighbourEarfcn na:neigboursEarfcnMap.get("earfcnAndCountSelfCell"))
							{
								System.out.println(na.getEarfcn()+"-"+na.getPci()+"-"+na.getCount());
							}
							System.out.println("ne list");
							for(NeighbourEarfcn na:neigboursEarfcnMap.get("earfcnAndCountNeighbourCell"))
							{
								System.out.println(na.getEarfcn()+"-"+na.getPci()+"-"+na.getCount());
							}
							
							//Sorting List of cells of matching plmn in descending order of rssi
							Cell[] tempCells= new Cell[scannedSectorCells.size()];
							tempCells = scannedSectorCells.toArray(tempCells);				
							//SANJAY AND VAIBHAV 31 DEC 2020 4 PM 
							//Arrays.sort(tempCells, Cell.rssiDescending
							Arrays.sort(tempCells, Cell.rssiAscending);		
							
											
							scannedSectorCells = new ArrayList<>(Arrays.asList(tempCells));
							
							//Getting all the uniqe scanned cells from the all scanned cells list
							ArrayList<Cell> uniqueScanedCellsList = getUniqueCellsHavingMaxRssi3g(scannedCells);
							
							//Gelling all the Self Cells for configuration
							//ArrayList<Cell> selfCells = getSelfCellsFor3gConfiguration(cellsForGivenPlmn, neigboursUarfcn, plmn);
							//ArrayList<Cell> selfCells = getSelfCellsForConfiguration3G(cellsForGivenPlmn, neigboursUarfcnMap, plmn);
							
							ArrayList<Cell> selfCells = getSelfCellsForConfiguration4G(scannedSectorCells, neigboursEarfcnMap, plmn,totalEarfcn);
							
							//Getting current System Possition
							JSONObject possition = getSystemPossition();
							
							//Getting neighbours for each self cell
							ArrayList<Cell> Configuration = getNeighbourCells(uniqueScanedCellsList, selfCells, Double.parseDouble(possition.getString("lat")), Double.parseDouble(possition.getString("lon")),"LTE");
							
										
							configurations.addAll(genrateJson(Configuration,packetId,"4G",antennaId));
							
							/*for(Cell cell:selfCells) 
							{
								fileLogger.debug(cell.getRssi()+"-"+cell.getArfcn());
							}*/
							fileLogger.debug("done 4g");
						}
						else 
						{
							throw new Exception("No Matching Cells Found For "+plmn+" ,TECH : LTE");
						}
				
					}
					catch(Exception E) 
					{
					 	//fileLogger.debug("******************************************************");
						fileLogger.debug("Class = PossibleConfigurations  Method = create4gConfigurationPackets");
						E.printStackTrace();
						//fileLogger.debug("******************************************************");
					}
					
				}
/*			}
			else 
			{
				throw new Exception("No Scanned Cells Found For UMTS");
			}*/
			
				
		}
		catch(Exception E) 
		 {
		 	//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = create4gConfigurationPackets");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		 }

		fileLogger.info("Inside Function : create4gConfigurationPackets");
		return configurations;
	}
	
	public Integer getrssi(String arfcn,String pci,String type)
	{
		String aa="";
		String bb="";
		//Integer ret =-999;
		if(arfcn != null && type.equalsIgnoreCase("gsm")) 
		{
			aa=" and arfcn ='"+arfcn+"' ";
		}
		else if(arfcn != null && type.equalsIgnoreCase("umts"))
		{
			aa=" and uarfcn='"+arfcn+"' and psc ='"+pci+"'";
		}else if(arfcn != null && type.equalsIgnoreCase("lte")){
			aa=" and earfcn ='"+arfcn+"'  and pci ='\"+pci+\"'\"; ";
		}
		//Could be an error in the below lines because aa hasn't been concatenated instead has been replaced
		
		String query2="select checkov1()";
		JSONArray result2 = new Operations().getJson(query2);
		String result3="-999";
		try {
			result3= result2.getJSONObject(0).getString("checkov1");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(this.antennaId != -1)
		{
			if(result3.equalsIgnoreCase("1") )
				bb=" and oprlogs_current.antenna_id = 23 ";
			else
				bb=" and oprlogs_current.antenna_id = "+this.antennaId;
			
		}
		
	String query ="select rssi from oprlogs_current where  (inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) or count = 'locator_count')  AND inserttime <= timezone('utc'::text, now())  "+aa+" "+bb+"  order by inserttime desc limit 1";                        
	JSONArray result4 = new Operations().getJson(query);
	if( result4.length()!= 0)
			{
		try {
			result3= result4.getJSONObject(0).getString("rssi");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			}
	else
	{
		query ="select rssi from oprlogs_current where  (inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) or count = 'locator_count')  AND inserttime <= timezone('utc'::text, now())  "+aa+"  order by rssi::integer limit 1";	
		 result4 = new Operations().getJson(query);
		if( result4.length()!= 0)
				{
			try {
				result3= result4.getJSONObject(0).getString("rssi");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

				}

	}
		return Integer.parseInt(result3);
		
	}
	public String getQueryForScannedCellsAccordingToGivenTechType(String type) 
	{
		String aa="";
		String bb="";
		String cc="";
		
		String lacOrTacQuery="";
		if(this.cellFilterValue!=null) {
			
			//this.cellFilterValue=this.cellFilterValue.replace(",", "','");
			
			if(this.cellFilterValue!=null && (!this.cellFilterValue.equalsIgnoreCase("-1") && this.cellFilterValue.length()!=0) )
			{
				cc=" and cell in ('"+this.cellFilterValue+"')";
			}
		}
		
		if(this.arfcnFilterValue != null && type.equalsIgnoreCase("gsm")) 
		{
			aa=" and arfcn in ("+this.arfcnFilterValue+") ";
		}
		else if(this.uarfcnFilterValue != null && type.equalsIgnoreCase("umts"))
		{
			aa=" and uarfcn in ("+this.uarfcnFilterValue+") ";
		}else if(this.earfcnFilterValue != null && type.equalsIgnoreCase("lte")){
			aa=" and earfcn in ("+this.earfcnFilterValue+") ";
		}
		//Could be an error in the below lines because aa hasn't been concatenated instead has been replaced
		
		String query2="select checkov1()";
		JSONArray result2 = new Operations().getJson(query2);
		String result3="";
		try {
			result3= result2.getJSONObject(0).getString("checkov1");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//@mighty raju no antena considration 
		if(this.antennaId != -1)
		{
			if(result3.equalsIgnoreCase("1") )
				aa=" and oprlogs_current.antenna_id = 23 ";
			else
				aa=" and oprlogs_current.antenna_id = "+this.antennaId;
			
		}
		
		//@mighty raju order by timestamp 
		
		if(1==1){
			bb=" order by  inserttime desc";
		}
		
		String tt = type;
		if(type.equalsIgnoreCase("gsm")) 
		{
			tt = "'"+type+"','Loc_2g'";
			lacOrTacQuery="lac";
		}
		
		if(type.equalsIgnoreCase("umts")) 
		{
			tt = "'"+type+"','Loc_3g'";
			lacOrTacQuery="lac";
		}
		
		if (type.equalsIgnoreCase("lte")) {
			tt = "'" + type + "','Loc_4g'";
			lacOrTacQuery="tac";
		}
		
		
		String query ="select oprlogs_current.*,rpt_data#>(ARRAY['REPORT'] || ARRAY[oprlogs_current.index_id::text]|| ARRAY['NEIGHBORS']) nbr,opr from oprlogs_current left join netscan_cell_scan_report ncsr on(oprlogs_current.packet_id::numeric = ncsr.id) left join view_current_scanned_opr_last_24_hours vcso on oprlogs_current.mcc||oprlogs_current.mnc=vcso.plmn where packet_type in( "+tt+") and (inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) or count = 'locator_count')  AND inserttime <= timezone('utc'::text, now()) and cell!='1' and   ( oprlogs_current.band, oprlogs_current.packet_type) in   (select name,tech1 from supported_band) and "+lacOrTacQuery+" != '0' and cell != '0' "+aa+" "+cc+ " "+bb;                        
		return query;
	}
	
	/*
	 * Get all the scanned data between the provided start and stop time;
	 * 
	 * */
	public ArrayList<Cell> getScannedCells(String packetType) 
	{

		fileLogger.info("Inside Function : getScannedCells");
		//String query = "select oprlogs_current.*,rpt_data#>'{REPORT,0,NEIGHBORS,INTRA_FREQ_NEIGH}' nbr from oprlogs left join netscan_cell_scan_report ncsr on(oprlogs.packet_id::numeric = ncsr.id) where packet_type = 'GSM' and inserttime >= '"+startTime+"' and inserttime <= '"+stopTime+"'";
		
		
		//String query = "select oprlogs_current.*,rpt_data#>(ARRAY['REPORT'] || ARRAY[oprlogs_current.index_id::text]|| ARRAY['NEIGHBORS']||ARRAY['INTRA_FREQ_NEIGH']) nbr from oprlogs_current left join netscan_cell_scan_report ncsr on(oprlogs_current.packet_id::numeric = ncsr.id) where packet_type in( "+packetType+") and inserttime >= (select inserttime from oprrationdata where id = (select max(id) from oprrationdata))";
		
		//String query = "select oprlogs_current.*,rpt_data#>'{REPORT,0,NEIGHBORS,INTRA_FREQ_NEIGH}' nbr from oprlogs_current left join netscan_cell_scan_report ncsr on(oprlogs_current.packet_id::numeric = ncsr.id) where packet_type = 'GSM' and oprlogs_current.id in ('116079','116143','116121','116162') and inserttime >= (select inserttime from oprrationdata where id = (select max(id) from oprrationdata)) ";
		String query = getQueryForScannedCellsAccordingToGivenTechType(packetType);
		Common co = new Common();
		ArrayList<Cell> cells =  new ArrayList<Cell>();
		
		Connection con =null;
		Statement smt =null;
		ResultSet rs = null;
		
		
		try 
		{
			con = co.getDbConnection();
			smt = con.createStatement();
			fileLogger.debug("query is :"+query);
			rs = smt.executeQuery(query);
			while(rs.next())
			{
				Cell cell = new Cell();
				cell.setPLMN(rs.getString("mcc")+""+rs.getString("mnc"));
				cell.setMCC(rs.getString("mcc"));
				cell.setMNC(rs.getString("mnc"));
				cell.setLAC(rs.getString("lac"));
				cell.setBand(rs.getString("band"));
				cell.setCELL(rs.getString("cell"));
				cell.setArfcn(rs.getInt("arfcn"));
				cell.setUarfcn(rs.getInt("uarfcn"));
				cell.setPsc(rs.getInt("psc"));
				cell.setEarfcn(rs.getInt("earfcn"));
				cell.setPci(rs.getInt("pci"));
				cell.setRssi(rs.getInt("rssi"));
				cell.setBsic(""+(rs.getInt("bcc"))+(rs.getInt("ncc")*8));
				cell.setOpr(rs.getString("opr"));
				cell.setOpr(rs.getString("opr"));
				cell.setTac(rs.getString("tac"));
				String[] sysloc = rs.getString("sysloc").split(",");
				
				cell.setLat(sysloc[0]);
				cell.setLon(sysloc[1]);
				
				JSONObject cellNbrObj = new JSONObject();
				if(rs.getString("nbr") != null) 
				{
					cellNbrObj=new JSONObject(rs.getString("nbr"));
					if(packetType.equalsIgnoreCase("lte")){
						
						JSONArray intraNeighArr=cellNbrObj.getJSONArray("INTRA_FREQ_NEIGH");
						
						if(intraNeighArr.length()==0){
							JSONArray newIntraNeighArr= new JSONArray(); 
							for(int pci=0;pci<=503;pci++){
								JSONObject tempObj=new JSONObject();
								tempObj.put("PCI", pci);
								newIntraNeighArr.put(tempObj);
							}
							cellNbrObj.put("INTRA_FREQ_NEIGH", newIntraNeighArr);
						}
						
						JSONArray interNeighArr=cellNbrObj.getJSONArray("INTER_FREQ_NEIGH");
						if(interNeighArr.length()!=0){
							JSONArray newInterNeighArr= new JSONArray();
							for(int i=0;i<interNeighArr.length();i++){
								JSONObject jsonObj= interNeighArr.getJSONObject(i);
								int earfcn=jsonObj.getInt("EARFCN");
								int pci=jsonObj.getInt("PCI");
								if(pci!=-1){
									JSONObject tempObj=new JSONObject();
									tempObj.put("EARFCN", earfcn);
									tempObj.put("PCI", pci);
									newInterNeighArr.put(tempObj);
								}else{
									for(int pciVal=0;pciVal<=503;pciVal++){
										JSONObject tempObj=new JSONObject();
										tempObj.put("EARFCN", earfcn);
										tempObj.put("PCI", pciVal);
										newInterNeighArr.put(tempObj);
									}
								}
							}
							cellNbrObj.put("INTER_FREQ_NEIGH", newInterNeighArr);
						}
					}
				}
				else 
				{
					cellNbrObj =null;
				}
				
				cell.setNeighbour(cellNbrObj);
				cells.add(cell);
				
			}
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getScannedCells");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}
		finally 
		{
			try
			{
				rs.close();
				smt.close();
				con.close();
			}
			catch(Exception e) {}
		}
		fileLogger.info("Exit Function : getScannedCells");
		return cells;
	}
	
	
	/*
	 * Returns all the cells for the given plmn
	 * */
	public ArrayList<Cell>getCellsForGivenPLMN(ArrayList<Cell> scannedCells,String PLMN)
	{
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for(Cell cell:scannedCells) 
		{
			if(cell.getPLMN().equals(PLMN)) 
			{
				cells.add(cell);
			}
		}
		return cells;
	}
	
	/*
	 * Retruns all the arfcns present in to the scanned cells in decinding order of there occurence count
	 * */
	public NeighbourArfcn[] getNeighbourArfcnList(ArrayList<Cell> cells,ArrayList<Cell> cells2gNeigh)
	{
		fileLogger.info("Inside Function : getNeighbourArfcnList");
		HashMap<Integer,Integer> arfcnList = new HashMap<Integer,Integer>();
		NeighbourArfcn[] arfcns2g = null;
		NeighbourArfcn[] arfcns = null;
		try 
		{
			
			for(Cell cell:cells) 
			{	
				   int selfArfcn=cell.getArfcn();
				   if(arfcnList.get(selfArfcn) != null)
					{
						int selfArfcnCount = arfcnList.get(selfArfcn);
						selfArfcnCount++;
						arfcnList.put(selfArfcn, selfArfcnCount);
					}
					else 
					{
						arfcnList.put(cell.getArfcn(), 1);
					}
				
				if(cell.getNeighbour() != null ) 
				{
					
					JSONArray neighbourARFCN = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
					if(neighbourARFCN.length() >=0) 
					{
						for (int i = 0; i < neighbourARFCN.length(); i++) 
						{
						   JSONObject arfcnObj = neighbourARFCN.getJSONObject(i);
						   int arfcn = arfcnObj.getInt("ARFCN");//@sunil might be string 
						   
						   if(arfcnList.get(arfcn) != null)
							{
								int value = arfcnList.get(arfcn);
									value++;
								arfcnList.put(arfcn, value);
							}
							else 
							{
								arfcnList.put(arfcn, 1);
							}
						}
					}
				}
				
				arfcns2g = new NeighbourArfcn[arfcnList.size()];
				
				
				int neighbourCount  =0;
				for (Entry<Integer, Integer> entry : arfcnList.entrySet()) {
					NeighbourArfcn na = new NeighbourArfcn();
					na.setArfcn(entry.getKey());
					na.setCount(entry.getValue());
					arfcns2g[neighbourCount] = na;
					neighbourCount++;
				}
			}
			
			NeighbourArfcn[] arfcns2gNeigh = null;
			HashMap<Integer,Integer> arfcn2gNeighList = new HashMap<Integer,Integer>();
			fileLogger.debug("1");
			
			
			for(Cell cell:cells2gNeigh) 
			{	
				if(cell.getNeighbour() != null ) 
				{
					JSONArray neighbourARFCN = cell.getNeighbour().getJSONArray("INTER_RAT_GSM_NEIGH");
					if(neighbourARFCN.length() >=0) 
					{
						for 
						(int i = 0; i < neighbourARFCN.length(); i++) 
						{
						   JSONObject arfcnObj = neighbourARFCN.getJSONObject(i);
						   int arfcn = arfcnObj.getInt("ARFCN");//@sunil might be string 
						   if(arfcn2gNeighList.get(arfcn) != null)
							{
								int value = arfcn2gNeighList.get(arfcn);
									value++;
									arfcn2gNeighList.put(arfcn, value);
							}
							
							else 
							{
								arfcn2gNeighList.put(arfcn, 1);
							}
						}
					}
					else 
					{
						arfcn2gNeighList.put(cell.getArfcn(), 1);
					}
				}
				else 
				{
					arfcn2gNeighList.put(cell.getArfcn(), 1);
				}
				
				arfcns2gNeigh = new NeighbourArfcn[arfcn2gNeighList.size()];
				
				int neighbourCount  =0;
				for (Entry<Integer, Integer> entry : arfcn2gNeighList.entrySet()) {
					NeighbourArfcn na = new NeighbourArfcn();
					na.setArfcn(entry.getKey());
					na.setCount(entry.getValue());
					arfcns2gNeigh[neighbourCount] = na;
					neighbourCount++;
				}
			}
			
			ArrayList<NeighbourArfcn> newArfcnList = new ArrayList<NeighbourArfcn>();
			newArfcnList.addAll(Arrays.asList(arfcns2g));
			if(arfcns2gNeigh!=null){
				newArfcnList.addAll(Arrays.asList(arfcns2gNeigh));
			}
			arfcns= new NeighbourArfcn[newArfcnList.size()];
			arfcns = newArfcnList.toArray(arfcns);
			if(arfcns != null && arfcns.length>1) 
			{
				Arrays.sort(arfcns,NeighbourArfcn.descending);
			}
			else 
			{
				fileLogger.debug("null");
			}
				
		}
		catch(Exception E) 
		{
		fileLogger.debug("28");
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getNeighbourArfcnList");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}
		fileLogger.info("Exit Function : getNeighbourArfcnList");
		return arfcns == null?new NeighbourArfcn[0]:arfcns;	
	}

	/*
	 * Retruns List of uarfcns present in to the scanned cells in descending order of there occurrence count
	 * Input:-ArrayList<Cell> cells
	 * */
	public NeighbourUarfcn[] getNeighbourUarfcn(ArrayList<Cell> cells,ArrayList<Cell> cells3gNeigh)
	{
		fileLogger.info("Inside Function : getNeighbourUarfcn");
		HashMap<String,NeighbourUarfcn> uarfcnList = new HashMap<String,NeighbourUarfcn>();
		NeighbourUarfcn[] uarfcns3g = null;
		NeighbourUarfcn[] uarfcns = null;
		
		try 
		{	
			for(Cell cell:cells) 
			{
				
				   int tempUarfcn = cell.getUarfcn();
				   int tempPsc = cell.getPsc();
				   
				   String tempKey = tempUarfcn+"-"+tempPsc;
				   
				   if(uarfcnList.get(tempKey) != null)
					{
					   		NeighbourUarfcn tempNaUarfcn  = uarfcnList.get(tempKey);
					   		int uarfcnValue = tempNaUarfcn.getCount();
					   		uarfcnValue++;
					   		tempNaUarfcn.setCount(uarfcnValue);
					}else 
					{
						NeighbourUarfcn tempNaUarfcn = new NeighbourUarfcn(tempUarfcn, tempPsc, 1);
						uarfcnList.put(tempKey, tempNaUarfcn);
					}
				   
				if(cell.getNeighbour() != null ) 
				{
					
					JSONArray neighbourUARFCN = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
					
					int cellUarfcn = cell.getUarfcn();
					
					if(neighbourUARFCN.length() >=0) 
					{
						for (int i = 0; i < neighbourUARFCN.length(); i++) 
						{
						   JSONObject uarfcnObj = neighbourUARFCN.getJSONObject(i);
						   
						   int uarfcn = cellUarfcn;
						   int psc = uarfcnObj.getInt("PSC");
						   
						   String key = uarfcn+"-"+psc;
						   
						   if(uarfcnList.get(key) != null)
							{
							   		NeighbourUarfcn temp  = uarfcnList.get(key);
							   		int value = temp.getCount();
									value++;
									temp.setCount(value);
							}
							else 
							{
								NeighbourUarfcn temp = new NeighbourUarfcn(uarfcn, psc, 1);
								uarfcnList.put(key, temp);
							}
						}
					}
										
					
					JSONArray neighbourUARFCN_2 = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
					if(neighbourUARFCN_2.length() >=0) 
					{
						for (int i = 0; i < neighbourUARFCN_2.length(); i++) 
						{
						   JSONObject uarfcnObj = neighbourUARFCN_2.getJSONObject(i);
						   
						   int uarfcn = uarfcnObj.getInt("UARFCN");
						   int psc = uarfcnObj.getInt("PSC");
						   
						   String key = uarfcn+"-"+psc;
						   
						   if(uarfcnList.get(key) != null)
							{
							   		NeighbourUarfcn temp  = uarfcnList.get(key);
							   		int value = temp.getCount();
									value++;
									temp.setCount(value);
							}
							else 
							{
								NeighbourUarfcn temp = new NeighbourUarfcn(uarfcn, psc, 1);
								uarfcnList.put(key, temp);
							}
						}
					}
					
					if(neighbourUARFCN_2.length() <=0 && neighbourUARFCN.length() <=0)
					{
						NeighbourUarfcn temp = new NeighbourUarfcn(cell.getUarfcn(), cell.getPsc(), 1);
						uarfcnList.put(cell.getUarfcn()+"-"+cell.getPsc(), temp);
					}
					
				}
				else 
				{
					NeighbourUarfcn temp = new NeighbourUarfcn(cell.getUarfcn(), cell.getPsc(), 1);
					uarfcnList.put(cell.getUarfcn()+"-"+cell.getPsc(), temp);
				}
				
				
				uarfcns3g = new NeighbourUarfcn[uarfcnList.size()];
				
				
				
				int neighbourCount  =0;
				for (Entry<String, NeighbourUarfcn> entry : uarfcnList.entrySet()) {
					uarfcns3g[neighbourCount] = entry.getValue();
					neighbourCount++;
				}
			}
			
			NeighbourUarfcn[] uarfcns3gNeigh = null;
			HashMap<String,NeighbourUarfcn> uarfcn3gNeighList = new HashMap<String,NeighbourUarfcn>();
			for(Cell cell:cells3gNeigh) 
			{	
				if(cell.getNeighbour() != null ) 
				{
					
					JSONArray neighbourUARFCN = cell.getNeighbour().getJSONArray("INTER_RAT_WCDMA_NEIGH");
					
					int cellUarfcn = cell.getUarfcn();
					
					if(neighbourUARFCN.length() >=0) 
					{
						for (int i = 0; i < neighbourUARFCN.length(); i++) 
						{
						   JSONObject uarfcnObj = neighbourUARFCN.getJSONObject(i);
						   
						   int uarfcn = cellUarfcn;
						   int psc = uarfcnObj.getInt("PSC");
						   
						   String key = uarfcn+"-"+psc;
						   
						   if(uarfcn3gNeighList.get(key) != null)
							{
							   		NeighbourUarfcn temp  = uarfcn3gNeighList.get(key);
							   		int value = temp.getCount();
									value++;
									temp.setCount(value);
							}
							else 
							{
								NeighbourUarfcn temp = new NeighbourUarfcn(uarfcn, psc, 1);
								uarfcn3gNeighList.put(key, temp);
							}
						}
					}
					
					if(neighbourUARFCN.length() <=0)
					{
						NeighbourUarfcn temp = new NeighbourUarfcn(cell.getUarfcn(), cell.getPsc(), 1);
						uarfcn3gNeighList.put(cell.getUarfcn()+"-"+cell.getPsc(), temp);
					}
					
				}
				else 
				{
					NeighbourUarfcn temp = new NeighbourUarfcn(cell.getUarfcn(), cell.getPsc(), 1);
					uarfcn3gNeighList.put(cell.getUarfcn()+"-"+cell.getPsc(), temp);
				}
				
				
				uarfcns3gNeigh = new NeighbourUarfcn[uarfcn3gNeighList.size()];
				
				
				
				int neighbourCount  =0;
				for (Entry<String, NeighbourUarfcn> entry : uarfcn3gNeighList.entrySet()) {
					uarfcns3gNeigh[neighbourCount] = entry.getValue();
					neighbourCount++;
				}
			}
			
			ArrayList<NeighbourUarfcn> newUarfcnList = new ArrayList<NeighbourUarfcn>();
			newUarfcnList.addAll(Arrays.asList(uarfcns3g));
			if(uarfcns3gNeigh!=null){
			newUarfcnList.addAll(Arrays.asList(uarfcns3gNeigh));
			}
			uarfcns= new NeighbourUarfcn[newUarfcnList.size()];
			uarfcns = newUarfcnList.toArray(uarfcns);
			
			if(uarfcns != null && uarfcns.length>1) 
			{
				Arrays.sort(uarfcns,NeighbourUarfcn.descending);
			}
			else 
			{
				fileLogger.debug("null");
			}
				
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getNeighbourUarfcn");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}
		fileLogger.info("Exit Function : getNeighbourUarfcn");
		return uarfcns == null?new NeighbourUarfcn[0]:uarfcns;	
	}
	
	/*
	 * Returns list of earfcn's present in the scanned cells in descending order of there occurrence count
	 * Input:-ArrayList<Cell> cells
	 * */
	public NeighbourEarfcn[] getNeighbourEarfcn(ArrayList<Cell> cells)
	{
		fileLogger.info("Inside Function : getNeighbourEarfcn");
		HashMap<String,NeighbourEarfcn> earfcnList = new HashMap<String,NeighbourEarfcn>();
		NeighbourEarfcn[] earfcns4g = null;
		NeighbourEarfcn[] earfcns = null;
		
		try 
		{	
			for(Cell cell:cells) 
			{
				
				   int tempEarfcn = cell.getEarfcn();
				   int tempPci = cell.getPci();
				   
				   String tempKey = tempEarfcn+"-"+tempPci;
				   
				   if(earfcnList.get(tempKey) != null)
					{
					   		NeighbourEarfcn tempNaEarfcn  = earfcnList.get(tempKey);
					   		int earfcnValue = tempNaEarfcn.getCount();
					   		earfcnValue++;
					   		tempNaEarfcn.setCount(earfcnValue);
					}else 
					{
						NeighbourEarfcn tempNaEarfcn = new NeighbourEarfcn(tempEarfcn, tempPci, 1);
						earfcnList.put(tempKey, tempNaEarfcn);
					}
				   
				if(cell.getNeighbour() != null ) 
				{
					
					JSONArray neighbourEARFCN = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
					
					int cellEarfcn = cell.getEarfcn();
					
					if(neighbourEARFCN.length() >=0) 
					{
						for (int i = 0; i < neighbourEARFCN.length(); i++) 
						{
						   JSONObject earfcnObj = neighbourEARFCN.getJSONObject(i);
						   
						   int earfcn = cellEarfcn;
						   int pci = earfcnObj.getInt("PCI");
						   
						   String key = earfcn+"-"+pci;
						   
						   if(earfcnList.get(key) != null)
							{
							   		NeighbourEarfcn temp  = earfcnList.get(key);
							   		int value = temp.getCount();
									value++;
									temp.setCount(value);
							}
							else 
							{
								NeighbourEarfcn temp = new NeighbourEarfcn(earfcn, pci, 1);
								earfcnList.put(key, temp);
							}
						}
					}/*else{
						int earfcn=cellEarfcn;
						for(int pci=0;pci<=503;pci++){
							String key = earfcn+"-"+pci;
							   if(earfcnList.get(key) != null)
								{
								   		NeighbourEarfcn temp  = earfcnList.get(key);
								   		int value = temp.getCount();
										value++;
										temp.setCount(value);
								}
								else 
								{
									NeighbourEarfcn temp = new NeighbourEarfcn(earfcn, pci, 1);
									earfcnList.put(key, temp);
								}
						}
					}*/
										
					
					JSONArray neighbourEARFCN_2 = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
					if(neighbourEARFCN_2.length() >=0) 
					{
						for (int i = 0; i < neighbourEARFCN_2.length(); i++) 
						{
						   JSONObject earfcnObj = neighbourEARFCN_2.getJSONObject(i);
						   
						   int earfcn = earfcnObj.getInt("EARFCN");
						   int pci = earfcnObj.getInt("PCI");
						   //if(pci!=-1){
								String key = earfcn+"-"+pci;
								   if(earfcnList.get(key) != null)
									{
									   		NeighbourEarfcn temp  = earfcnList.get(key);
									   		int value = temp.getCount();
											value++;
											temp.setCount(value);
									}
									else 
									{
										NeighbourEarfcn temp = new NeighbourEarfcn(earfcn, pci, 1);
										earfcnList.put(key, temp);
									}
						   //}
						   /*else{  
									for(int pciVal=0;pciVal<=503;pciVal++){
										String key = earfcn+"-"+pciVal;
										   if(earfcnList.get(key) != null)
											{
											   		NeighbourEarfcn temp  = earfcnList.get(key);
											   		int value = temp.getCount();
													value++;
													temp.setCount(value);
											}
											else 
											{
												NeighbourEarfcn temp = new NeighbourEarfcn(earfcn, pci, 1);
												earfcnList.put(key, temp);
											}
									}
							   
						   }*/
						   
						   
						   

						}
					}
					
					if(neighbourEARFCN_2.length() <=0 && neighbourEARFCN.length() <=0)
					{
						NeighbourEarfcn temp = new NeighbourEarfcn(cell.getEarfcn(), cell.getPci(), 1);
						earfcnList.put(cell.getEarfcn()+"-"+cell.getPci(), temp);
					}
					
				}
				else 
				{
					NeighbourEarfcn temp = new NeighbourEarfcn(cell.getEarfcn(), cell.getPci(), 1);
					earfcnList.put(cell.getEarfcn()+"-"+cell.getPci(), temp);
				}
				
				
				earfcns4g = new NeighbourEarfcn[earfcnList.size()];
				
				
				
				int neighbourCount  =0;
				for (Entry<String, NeighbourEarfcn> entry : earfcnList.entrySet()) {
					earfcns4g[neighbourCount] = entry.getValue();
					neighbourCount++;
				}
			}
			
			ArrayList<NeighbourEarfcn> newEarfcnList = new ArrayList<NeighbourEarfcn>();
			newEarfcnList.addAll(Arrays.asList(earfcns4g));
			earfcns= new NeighbourEarfcn[newEarfcnList.size()];
			earfcns = newEarfcnList.toArray(earfcns);
			
			if(earfcns != null && earfcns.length>1) 
			{
				Arrays.sort(earfcns,NeighbourEarfcn.descending);
			}
			else 
			{
				fileLogger.debug("@algo null");
			}
				
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getNeighbourUarfcn");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}
		fileLogger.info("Exit Function : getNeighbourEarfcn");
		return earfcns == null?new NeighbourEarfcn[0]:earfcns;	
	}
	
	
	/*
	 * This function will returns the unique cells from the scanned list having max rssi
	 * */
	public ArrayList<Cell> getUniqueCellsHavingMaxRssi(ArrayList<Cell> cells)
	{
		fileLogger.info("Inside Function : getUniqueCellsHavingMaxRssi");
		//@sunil must impliment cloning
		//Cell[] arrayOfCell = (Cell[])cells.toArray();
		
		//Arrays.sort(arrayOfCell, Cell.rssiAscending);
		
		
		//Arrays.sort(tempCells, Cell.rssiDescending);
		//cellsForGivenPlmn = new ArrayList<>(Arrays.asList(tempCells));
		
		ArrayList<Cell> uniqueCellsHavingMaxRssi = new ArrayList<Cell>();
		Cell[] arrayOfCell= new Cell[cells.size()];
		arrayOfCell = cells.toArray(arrayOfCell);
		Arrays.sort(arrayOfCell, Cell.arfcnAscending);
		
		LinkedHashMap<Integer,Cell> tempHashMap = new LinkedHashMap<Integer,Cell>();
		
		try 
		{
			
			int previousRssi = 0;
			int previourArfcn = 0;
			for(int i=arrayOfCell.length-1;i>=0;i--) 
			{
				if(tempHashMap.size() <=0) 
				{
					
					tempHashMap.put(arrayOfCell[i].getArfcn(), arrayOfCell[i]);
					previourArfcn = arrayOfCell[i].getArfcn();
				}
				else 
				{
					if(arrayOfCell[i].getArfcn() == previourArfcn) 
					{
						//uniqueCellsHavingMaxRssi.add(arrayOfCell[i]);
						if(tempHashMap.get(previourArfcn).getRssi() < arrayOfCell[i].getRssi())
						{
							tempHashMap.put(previourArfcn, arrayOfCell[i]);
						}
					}
					else 
					{
						tempHashMap.put(arrayOfCell[i].getArfcn(), arrayOfCell[i]);
					}
				}
				
				
				Cell[] tempCellsArray =  new Cell[tempHashMap.size()]; 
				tempCellsArray = tempHashMap.values().toArray(tempCellsArray);
				Arrays.sort(tempCellsArray, Cell.rssiAscending);
				uniqueCellsHavingMaxRssi = new ArrayList<>(Arrays.asList(tempCellsArray));
			}
			
		}
		catch(Exception E) 
		{
		//	fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getNeighbourArfcnList");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}
		fileLogger.info("Exit Function : getUniqueCellsHavingMaxRssi");
		return uniqueCellsHavingMaxRssi;	
	}
	
	
	
	
	/*
	 * This function will returns the unique cells from the scanned list having max rssi for UMTS(3G)
	 * */
	public ArrayList<Cell> getUniqueCellsHavingMaxRssi3g(ArrayList<Cell> cells)
	{
		fileLogger.info("Inside Function : getUniqueCellsHavingMaxRssi3g");
		ArrayList<Cell> uniqueCellsHavingMaxRssi = new ArrayList<Cell>();
		Cell[] arrayOfCell= new Cell[cells.size()];
		arrayOfCell = cells.toArray(arrayOfCell);
		Arrays.sort(arrayOfCell, Cell.uarfcnAscending);
		LinkedHashMap<Integer,Cell> tempHashMap = new LinkedHashMap<Integer,Cell>();
		
		
		
		
		try 
		{
			
			int previousRssi = 0;
			int previourUarfcn = 0;
			for(int i=arrayOfCell.length-1;i>=0;i--) 
			{
				if(tempHashMap.size() <=0) 
				{
					
					tempHashMap.put(arrayOfCell[i].getUarfcn(), arrayOfCell[i]);
					previourUarfcn = arrayOfCell[i].getUarfcn();
				}
				else 
				{
					if(arrayOfCell[i].getUarfcn() == previourUarfcn) 
					{
						//uniqueCellsHavingMaxRssi.add(arrayOfCell[i]);
						if(tempHashMap.get(previourUarfcn).getRssi() < arrayOfCell[i].getRssi())
						{
							tempHashMap.put(previourUarfcn, arrayOfCell[i]);
						}
					}
					else 
					{
						tempHashMap.put(arrayOfCell[i].getUarfcn(), arrayOfCell[i]);
					}
				}
				
				
				Cell[] tempCellsArray =  new Cell[tempHashMap.size()]; 
				tempCellsArray = tempHashMap.values().toArray(tempCellsArray);
				Arrays.sort(tempCellsArray, Cell.rssiAscending);
				uniqueCellsHavingMaxRssi = new ArrayList<>(Arrays.asList(tempCellsArray));
			}
			
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getUniqueCellsHavingMaxRssi3g");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}
		fileLogger.info("Exit Function : getUniqueCellsHavingMaxRssi3g");
		return uniqueCellsHavingMaxRssi;	
	}
	
	
	
	
	/*
	 * Create self Cells for configuration
	 * 
	 * */	
	public ArrayList<Cell> getSelfCellsForConfiguration(ArrayList<Cell> plmnCells,NeighbourArfcn[] NeighbourArfcnList,String PLMN)
	{
		
		//@sunil create cell clone
		fileLogger.info("Inside Function : getSelfCellsForConfiguration");
		ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
		
		ArrayList<Cell>selfCellsWithoutNeighbours = new ArrayList<Cell>();
		
		ArrayList<Cell> cells = new ArrayList<Cell>();		
		
		try 
		{
			for(Cell cell:plmnCells) 
			{
				cells.add((Cell)cell.clone());
			}
			
			for(NeighbourArfcn na:NeighbourArfcnList) 
			{
				for(Cell cell:cells) 
				{
					boolean flag=false;
					
					if(cell.getNeighbour() != null && cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0) 
					{
						JSONArray nbrArfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
						
						for(int i=0;i<nbrArfcn.length();i++) 
						{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if(na.getArfcn() ==  nbrArfcn.getJSONObject(i).getInt("ARFCN")) 
							{
								Cell cell1 = (Cell)cell.clone();
								cell1.setArfcn(na.getArfcn());
								cell1.setBand(calculateBand("gsm",na.getArfcn()));
								if(checkDuplicatesArfcn(selfCellsWithNeighbours,cell1)){
									selfCellsWithNeighbours.add(cell1);
									flag=true;
									break;
								}
							}
						}
						
					}
					else if(na.getArfcn() == cell.getArfcn()) 
					{
						Cell cell1 = (Cell)cell.clone();
						cell1.setBand(calculateBand("gsm",cell1.getArfcn()));
						selfCellsWithoutNeighbours.add(cell1);
					}
					
					if(flag) 
					{
						break;
					}
					
				}
			}
			
			if(selfCellsWithoutNeighbours.size()>0)
			{
				Cell[] tempCells = new Cell[selfCellsWithoutNeighbours.size()];
				tempCells = (Cell[])selfCellsWithoutNeighbours.toArray(tempCells);
				
				
				
				//SANJAY AND VAIBHAV 31 DEC 2020 4 PM 
				//Arrays.sort(tempCells, Cell.rssiDescending);
				Arrays.sort(tempCells, Cell.rssiAscending);		
				
				
				
				
				
				for(Cell cell:tempCells) 
				{
					if(checkDuplicatesArfcn(selfCellsWithNeighbours,cell)){
						selfCellsWithNeighbours.add(cell);
					}
				}
			}
			else if(selfCellsWithNeighbours.size() <=0)
			{
				selfCellsWithoutNeighbours.add(genrateDefaultCell(PLMN,"GSM"));
			}
			
			Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
			tempCells = selfCellsWithNeighbours.toArray(tempCells);
			Arrays.sort(tempCells, Cell.rssiAscending);
			selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
			
			
			
			
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration");
			E.printStackTrace();
			///fileLogger.debug("******************************************************");
		}	
		fileLogger.info("Exit Function : getSelfCellsForConfiguration");
		return selfCellsWithNeighbours;
	}
	
	/*
	 * Create self Cells for configuration
	 * 
	 * */	
	public ArrayList<Cell> getSelfCellsForConfiguration2G(ArrayList<Cell> plmnCells,HashMap<String,NeighbourArfcn[]> neighbourArfcnMap,String PLMN)
	{
		fileLogger.info("Inside Function : getSelfCellsForConfiguration2G");
		//@sunil create cell clone
		NeighbourArfcn[] arfcnAndCountSelfCell= neighbourArfcnMap.get("arfcnAndCountSelfCell");
		NeighbourArfcn[] arfcnAndCountNeighbourCell=neighbourArfcnMap.get("arfcnAndCountNeighbourCell");
		
		ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
		
		ArrayList<Cell>selfCellsWithoutNeighbours = new ArrayList<Cell>();
		
		ArrayList<Cell> cells = new ArrayList<Cell>();	
		
		HashMap<Cell,String> cellMap=new HashMap<Cell,String>();
		
		try 
		{
			for(Cell cell:plmnCells) 
			{
				cells.add((Cell)cell.clone());
				//cellMap.put((Cell)cell.clone(),"n");
			}
			String[] cellCoverStatus=new String[cells.size()];
			for(int k=0;k<cells.size();k++){
				cellCoverStatus[k]="n";
			}
			
			for(NeighbourArfcn na:arfcnAndCountNeighbourCell){
				boolean naMatch=false;
				for(int j=0;j<cells.size();j++){
					Cell cell=cells.get(j);
					if(cell.getNeighbour() != null && cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0) 
					{
						JSONArray nbrArfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
						
						for(int i=0;i<nbrArfcn.length();i++) 
						{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if((na.getArfcn()==nbrArfcn.getJSONObject(i).getInt("ARFCN") || na.getArfcn()==cell.getArfcn()) && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setArfcn(na.getArfcn());
								cell1.setBand(calculateBand("gsm",na.getArfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}
							}
						}
					}
				}
			}
			
			for(NeighbourArfcn na:arfcnAndCountSelfCell){
				boolean naMatch=false;
				for(int j=0;j<cells.size();j++){
					Cell cell=cells.get(j);
					if(cell.getNeighbour() != null && cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0) 
					{
						JSONArray nbrArfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
						
						for(int i=0;i<nbrArfcn.length();i++) 
						{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if((na.getArfcn()==nbrArfcn.getJSONObject(i).getInt("ARFCN") || na.getArfcn()==cell.getArfcn()) && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setArfcn(na.getArfcn());
								cell1.setBand(calculateBand("gsm",na.getArfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}
							}
						}
						
					}else{
						if(na.getArfcn()==cell.getArfcn() && cellCoverStatus[j]=="n") 
						{
							cellCoverStatus[j]="y";
							if(!naMatch){
							Cell cell1 = (Cell)cell.clone();
							cell1.setArfcn(na.getArfcn());
							cell1.setBand(calculateBand("gsm",na.getArfcn()));
							selfCellsWithNeighbours.add(cell1);
							naMatch=true;
							}
						}
						
					}
				}
			}
			
			for(int cellCount=0;cellCount<cells.size();cellCount++){
				if(cellCoverStatus[cellCount].equals("n")){
					Cell cell1 = (Cell)cells.get(cellCount).clone();
					cell1.setBand(calculateBand("gsm",cell1.getArfcn()));
					selfCellsWithNeighbours.add(cell1);
				}
			}
			
			Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
			tempCells = selfCellsWithNeighbours.toArray(tempCells);
			Arrays.sort(tempCells, Cell.rssiAscending);
			selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration2G");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}	
		fileLogger.info("Exit Function : getSelfCellsForConfiguration2G");
		return selfCellsWithNeighbours;
	}
	
	
	
	/*
	 * Create 3g self cells
	 * */
	public ArrayList<Cell> getSelfCellsFor3gConfiguration(ArrayList<Cell> plmnCells,NeighbourUarfcn[] NeighbourUarfcnList,String PLMN)
	{
		
		fileLogger.info("Inside Function : getSelfCellsFor3gConfiguration");
		for(Cell cc:plmnCells) 
		{
			fileLogger.debug(cc.getArfcn()+"-"+cc.getUarfcn()+"-"+cc.getPsc());
		}
		
		ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
		
		ArrayList<Cell>selfCellsWithoutNeighbours = new ArrayList<Cell>();
		
		ArrayList<Cell> cells = new ArrayList<Cell>();		
		try 
		{
			//Cloning the cells of plmn it will secure and will not allow updating the refrence inside the object
			for(Cell cell:plmnCells) 
			{
				cells.add((Cell)cell.clone());
			}
			
			for(NeighbourUarfcn na:NeighbourUarfcnList) 
			{
				for(Cell cell:cells) 
				{
					boolean flag=false;
					
					if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)) 
					{
						JSONArray nbrUarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
						
						for(int i=0;i<nbrUarfcn.length();i++) 
						{
							if(na.getPsc() ==  nbrUarfcn.getJSONObject(i).getInt("PSC")) 
							{
								Cell cell1 = (Cell)cell.clone();
								cell1.setPsc(na.getPsc());
								cell1.setBand(calculateBand("umts",na.getUarfcn()));//@sunil required umts band calculation
							//	if (! (ppsc==cell1.getPsc() && purfcn==cell1.getUarfcn()))
							//	{
								if(checkDuplicatesUarfcnPsc(selfCellsWithNeighbours,cell1)){
								selfCellsWithNeighbours.add(cell1);
								flag=true;
								break;
								}
							//}
							}
						}
						
						
						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
						
						for(int i=0;i<nbrUarfcn_inter.length();i++) 
						{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if(na.getUarfcn() ==  nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc() ==  nbrUarfcn_inter.getJSONObject(i).getInt("PSC")) 
							{
								Cell cell1 = (Cell)cell.clone();
								
								cell1.setPsc(na.getPsc());
								cell1.setUarfcn(na.getUarfcn());
								
								cell1.setBand(calculateBand("umts",na.getUarfcn()));
								if(checkDuplicatesUarfcnPsc(selfCellsWithNeighbours,cell1)){
								selfCellsWithNeighbours.add(cell1);
								flag=true;
								break;
							}
							}
						}
						
					}
					else if(na.getUarfcn() == cell.getUarfcn()) 
					{
						Cell cell1 = (Cell)cell.clone();
						cell1.setBand(calculateBand("umts",cell1.getUarfcn()));
						selfCellsWithoutNeighbours.add(cell1);
					}
					
					if(flag) 
					{
						break;
					}
				}
			}
			
			if(selfCellsWithoutNeighbours.size()>0)
			{
				Cell[] tempCells = new Cell[selfCellsWithoutNeighbours.size()];
				tempCells = (Cell[])selfCellsWithoutNeighbours.toArray(tempCells);
				
				
				
				
	
				
				//SANJAY AND VAIBHAV 31 DEC 2020 4 PM 
				//Arrays.sort(tempCells, Cell.rssiDescending);
				Arrays.sort(tempCells, Cell.rssiAscending);	
				
				
				for(Cell cell:tempCells) 
				{
					if(checkDuplicatesUarfcnPsc(selfCellsWithNeighbours,cell)){
					selfCellsWithNeighbours.add(cell);
				}
				}
			}
			else if(selfCellsWithNeighbours.size() <=0)
			{
				selfCellsWithoutNeighbours.add(genrateDefaultCell(PLMN,"UMTS"));
			}
			
			
			Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
			tempCells = selfCellsWithNeighbours.toArray(tempCells);
			Arrays.sort(tempCells, Cell.arfcnAscending);
			selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
			
			
		}
		catch(Exception E) 
		{
		//	fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsFor3gConfiguration");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}	
		fileLogger.info("Exit Function : getSelfCellsFor3gConfiguration");
		return selfCellsWithNeighbours;
	}
	
	
	/*
	 * Create 3g self cells
	 * */
	public ArrayList<Cell> getSelfCellsForConfiguration3G(ArrayList<Cell> plmnCells,HashMap<String,NeighbourUarfcn[]> neighbourUarfcnMap,String PLMN)
	{	
		fileLogger.info("Inside Function : getSelfCellsForConfiguration3G");
		for(Cell cc:plmnCells) 
		{
			fileLogger.debug(cc.getArfcn()+"-"+cc.getUarfcn()+"-"+cc.getPsc());
		}
		
		NeighbourUarfcn[] uarfcnAndCountSelfCell= neighbourUarfcnMap.get("uarfcnAndCountSelfCell");
		NeighbourUarfcn[] uarfcnAndCountNeighbourCell=neighbourUarfcnMap.get("uarfcnAndCountNeighbourCell");
		
		ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
		
		ArrayList<Cell> cells = new ArrayList<Cell>();		
		try 
		{
			//Cloning the cells of plmn it will secure and will not allow updating the refrence inside the object
			for(Cell cell:plmnCells) 
			{
				cells.add((Cell)cell.clone());
			}
			
			String[] cellCoverStatus=new String[cells.size()];
			for(int k=0;k<cells.size();k++){
				cellCoverStatus[k]="n";
			}
			
			for(NeighbourUarfcn na:uarfcnAndCountNeighbourCell){
				boolean naMatch=false;
				for(int j=0;j<cells.size();j++){
					Cell cell=cells.get(j);
					if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
						JSONArray nbrUarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
						for(int i=0;i<nbrUarfcn.length();i++) 
						{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if((na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC") || na.getPsc()==cell.getPsc()) && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPsc(na.getPsc());
								cell1.setBand(calculateBand("umts",na.getUarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}
							}
						}
						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
						for(int i=0;i<nbrUarfcn_inter.length();i++){
							if(((na.getUarfcn()==nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc()==nbrUarfcn_inter.getJSONObject(i).getInt("PSC"))||(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc())) && cellCoverStatus[j]=="n"){
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPsc(na.getPsc());
								cell1.setUarfcn(na.getUarfcn());	
								cell1.setBand(calculateBand("umts",na.getUarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}	
							}

						}
					}
				}
			}
			
			for(NeighbourUarfcn na:uarfcnAndCountSelfCell){
				boolean naMatch=false;
				for(int j=0;j<cells.size();j++){
					Cell cell=cells.get(j);
					if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
						JSONArray nbrUarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
						for(int i=0;i<nbrUarfcn.length();i++) 
						{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if((na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC") || na.getPsc()==cell.getPsc()) && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPsc(na.getPsc());
								cell1.setBand(calculateBand("umts",na.getUarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}
							}
						}
						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
						for(int i=0;i<nbrUarfcn_inter.length();i++){
							if(((na.getUarfcn()==nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc()==nbrUarfcn_inter.getJSONObject(i).getInt("PSC"))||(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc())) && cellCoverStatus[j]=="n"){
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPsc(na.getPsc());
								cell1.setUarfcn(na.getUarfcn());	
								cell1.setBand(calculateBand("umts",na.getUarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}	
							}

						}
					}else{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if( na.getPsc()==cell.getPsc() && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPsc(na.getUarfcn());
								cell1.setPsc(na.getPsc());
								cell1.setBand(calculateBand("umts",na.getUarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								}
							}

					}
				}
			}
			
			for(int cellCount=0;cellCount<cells.size();cellCount++){
				if(cellCoverStatus[cellCount].equals("n")){
					Cell cell1 = (Cell)cells.get(cellCount).clone();
					cell1.setBand(calculateBand("umts",cell1.getUarfcn()));
					selfCellsWithNeighbours.add(cell1);
				}
			}
			
			
			Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
			tempCells = selfCellsWithNeighbours.toArray(tempCells);
			Arrays.sort(tempCells, Cell.arfcnAscending);
			selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
			
			
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration3G");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}	
		fileLogger.info("Exit Function : getSelfCellsForConfiguration3G");
		return selfCellsWithNeighbours;
	}
	
	/*
	 * Create 3g self cells
	 * */
	public ArrayList<Cell> getSelfCellsForConfiguration4G(ArrayList<Cell> plmnCells,HashMap<String,NeighbourEarfcn[]> neighbourEarfcnMap,String PLMN)
	{	
		fileLogger.info("Inside Function : getSelfCellsForConfiguration4G");
		for(Cell cc:plmnCells) 
		{
			fileLogger.debug(cc.getEarfcn()+"-"+cc.getPci());
		}
		
		NeighbourEarfcn[] earfcnAndCountSelfCell= neighbourEarfcnMap.get("earfcnAndCountSelfCell");
		NeighbourEarfcn[] earfcnAndCountNeighbourCell=neighbourEarfcnMap.get("earfcnAndCountNeighbourCell");
		
		ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
		
		ArrayList<Cell> cells = new ArrayList<Cell>();		
		try 
		{
			//Cloning the cells of plmn it will secure and will not allow updating the refrence inside the object
			for(Cell cell:plmnCells) 
			{
				cells.add((Cell)cell.clone());
			}
			
			String[] cellCoverStatus=new String[cells.size()];
			for(int k=0;k<cells.size();k++){
				cellCoverStatus[k]="n";
			}
			
			for(NeighbourEarfcn na:earfcnAndCountNeighbourCell){
				boolean naMatch=false;
				for(int j=0;j<cells.size();j++){
					Cell cell=cells.get(j);
					if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
						JSONArray nbrEarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
						for(int i=0;i<nbrEarfcn.length();i++) 
						{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if((na.getPci()==nbrEarfcn.getJSONObject(i).getInt("PCI") || na.getPci()==cell.getPci()) && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPci(na.getPci());
								cell1.setBand(calculateBand("umts",na.getEarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}
							}
						}
						JSONArray nbrEarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
						for(int i=0;i<nbrEarfcn_inter.length();i++){
							if(((na.getEarfcn()==nbrEarfcn_inter.getJSONObject(i).getInt("EARFCN") && na.getPci()==nbrEarfcn_inter.getJSONObject(i).getInt("PCI"))||(na.getEarfcn()==cell.getEarfcn() && na.getPci()==cell.getPci())) && cellCoverStatus[j]=="n"){
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPci(na.getPci());
								cell1.setEarfcn(na.getEarfcn());	
								cell1.setBand(calculateBand("lte",na.getEarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}	
							}

						}
					}
				}
			}
			
			for(NeighbourEarfcn na:earfcnAndCountSelfCell){
				boolean naMatch=false;
				for(int j=0;j<cells.size();j++){
					Cell cell=cells.get(j);
					if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
						JSONArray nbrEarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
						for(int i=0;i<nbrEarfcn.length();i++) 
						{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if((na.getPci()==nbrEarfcn.getJSONObject(i).getInt("PCI") || na.getPci()==cell.getPci()) && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPci(na.getPci());
								cell1.setBand(calculateBand("lte",na.getEarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}
							}
						}
						JSONArray nbrEarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
						for(int i=0;i<nbrEarfcn_inter.length();i++){
							if(((na.getEarfcn()==nbrEarfcn_inter.getJSONObject(i).getInt("EARFCN") && na.getPci()==nbrEarfcn_inter.getJSONObject(i).getInt("PCI"))||(na.getEarfcn()==cell.getEarfcn() && na.getPci()==cell.getPci())) && cellCoverStatus[j]=="n"){
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPci(na.getPci());
								cell1.setEarfcn(na.getEarfcn());	
								cell1.setBand(calculateBand("lte",na.getEarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								break;
								}	
							}

						}
					}else{
							
							//nbrArfcn.getJSONObject(i).get("ARFCN");
							if( na.getPci()==cell.getPci() && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setPci(na.getEarfcn());
								cell1.setPci(na.getPci());
								cell1.setBand(calculateBand("lte",na.getEarfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								}
							}

					}
				}
			}
			
			for(int cellCount=0;cellCount<cells.size();cellCount++){
				if(cellCoverStatus[cellCount].equals("n")){
					Cell cell1 = (Cell)cells.get(cellCount).clone();
					cell1.setBand(calculateBand("lte",cell1.getEarfcn()));
					selfCellsWithNeighbours.add(cell1);
				}
			}
			
			
			Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
			tempCells = selfCellsWithNeighbours.toArray(tempCells);
			Arrays.sort(tempCells, Cell.earfcnAscending);
			selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
			
			
		}
		catch(Exception E) 
		{
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration4G");
			E.printStackTrace();
			//fileLogger.debug("******************************************************");
		}	
		fileLogger.info("Exit Function : getSelfCellsForConfiguration4G");
		return selfCellsWithNeighbours;
	}
	
	
	
	/*
	 * Calculate possible band
	 * Input :-  TECH,freq
	 * */
	public String calculateBand(String tech,int freq) 
	{
		String band = "";
		if(tech.equalsIgnoreCase("gsm")) 
		{
			if((freq >= 512) && (freq <= 885))
				band = "4";
			else if(((freq >= 1) && (freq <= 124)) || ((freq >= 975) && (freq <= 1023)))
				band = "2";
			else if((freq >= 128) && (freq <= 251))
				band = "1";
			else
				band = "NA";
		}else if(tech.equalsIgnoreCase("UMTS")) 
		{
			band = getBandForTheGivenUarfcn(freq);
		}else if(tech.equalsIgnoreCase("lte")){
			band = getBandForTheGivenEarfcn(freq);
		}
		return band;
	}
	
	public String getBandForTheGivenUarfcn(int uarfcn) 
	{
		fileLogger.info("Inside Function : getBandForTheGivenUarfcn");
		String band = "NA";
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		
		try
		{
			
			
		    File file = new File(absolutePath	+ "/resources/config/band_dluarfcn_map.json");
			String bandDluarfcnString = FileUtils.readFileToString(file);
			JSONObject js = new JSONObject(bandDluarfcnString);
			JSONArray umtsBandDluarfcnList=js.getJSONArray("umts");
			
			for(int i=0;i<umtsBandDluarfcnList.length();i++)
			{
				int lowerUarfcn = umtsBandDluarfcnList.getJSONObject(i).getJSONArray("freq").getInt(0);
				int UpperUarfcn = umtsBandDluarfcnList.getJSONObject(i).getJSONArray("freq").getInt(1);
				
				if(uarfcn>=lowerUarfcn &&uarfcn<=UpperUarfcn)
				{
					band = umtsBandDluarfcnList.getJSONObject(i).getString("name");
				}
			}
			
			
			
		}
		catch(Exception E)
		{
			fileLogger.debug("db.properties not found" + E.getMessage());
			
		}
		fileLogger.info("Exit Function : getBandForTheGivenUarfcn");
		return band;
	}
	
	public String getBandForTheGivenEarfcn(int earfcn) 
	{
		fileLogger.info("Inside Function : getBandForTheGivenEarfcn");
		String band = "NA";
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		
		try
		{
			
			
		    File file = new File(absolutePath	+ "/resources/config/band_dluarfcn_map.json");
			String bandDluarfcnString = FileUtils.readFileToString(file);
			JSONObject js = new JSONObject(bandDluarfcnString);
			JSONArray lteBandDluarfcnList=js.getJSONArray("lte");
			
			for(int i=0;i<lteBandDluarfcnList.length();i++)
			{
				int lowerEarfcn = lteBandDluarfcnList.getJSONObject(i).getJSONArray("freq").getInt(0);
				int upperEarfcn = lteBandDluarfcnList.getJSONObject(i).getJSONArray("freq").getInt(1);
				
				if(earfcn>=lowerEarfcn &&earfcn<=upperEarfcn)
				{
					band = lteBandDluarfcnList.getJSONObject(i).getString("name");
				}
			}
			
			
			
		}
		catch(Exception E)
		{
			fileLogger.error("Error in getBandForTheGivenEarfcn :" + E.getMessage());
			
		}
		fileLogger.info("Exit Function : getBandForTheGivenEarfcn");
		return band;
	}
	
	public int getFormulaForTheGivenUarfcn(int uarfcn) 
	{
		fileLogger.info("Inside Function : getFormulaForTheGivenUarfcn");
		int formula = 0;
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		
		try
		{
			   File file = new File(absolutePath	+ "/resources/config/band_dluarfcn_map.json");
			   String bandDluarfcnString = FileUtils.readFileToString(file);
			JSONObject js = new JSONObject(bandDluarfcnString);
			JSONArray umtsBandDluarfcnList=js.getJSONArray("umts");
			
			for(int i=0;i<umtsBandDluarfcnList.length();i++)
			{
				int lowerUarfcn = umtsBandDluarfcnList.getJSONObject(i).getJSONArray("freq").getInt(0);
				int UpperUarfcn = umtsBandDluarfcnList.getJSONObject(i).getJSONArray("freq").getInt(1);
				
				if(uarfcn>=lowerUarfcn &&uarfcn<=UpperUarfcn)
				{
					formula = umtsBandDluarfcnList.getJSONObject(i).getInt("formula");
				}
			}
			
			
			
		}
		catch(Exception E)
		{
			fileLogger.debug("db.properties not found" + E.getMessage());
			
		}
		fileLogger.info("Exit Function : getFormulaForTheGivenUarfcn");
		return formula;
	}
	
	
	public int getFormulaForTheGivenEarfcn(int earfcn) 
	{
		fileLogger.info("Inside Function : getFormulaForTheGivenEarfcn");
		int formula = 0;
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		
		try
		{
			   File file = new File(absolutePath	+ "/resources/config/band_dluarfcn_map.json");
			   String bandDluarfcnString = FileUtils.readFileToString(file);
			JSONObject js = new JSONObject(bandDluarfcnString);
			JSONArray lteBandDlearfcnList=js.getJSONArray("lte");
			
			for(int i=0;i<lteBandDlearfcnList.length();i++)
			{
				int lowerEarfcn = lteBandDlearfcnList.getJSONObject(i).getJSONArray("freq").getInt(0);
				int UpperEarfcn = lteBandDlearfcnList.getJSONObject(i).getJSONArray("freq").getInt(1);
				
				if(earfcn>=lowerEarfcn && earfcn<=UpperEarfcn)
				{
					formula =lteBandDlearfcnList.getJSONObject(i).getInt("formula");
				}
			}
			
			
			
		}
		catch(Exception E)
		{
			fileLogger.debug("db.properties not found" + E.getMessage());
			
		}
		fileLogger.info("Exit Function : getFormulaForTheGivenEarfcn");
		return formula;
	}
	
	
	/*
	 * Create Default Cell
	 * input :- PLMN
	 * */
	
	public Cell genrateDefaultCell(String PLMN,String tech)
	{
		fileLogger.info("Inside Function : genrateDefaultCell");
		String mcc = (PLMN.substring(0,4));
		String mnc = (PLMN.substring(4));
		String oprName="NA";
		JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
		try {
			if(oprNameArray.length()>0){
				oprName=oprNameArray.getJSONObject(0).getString("opr");
			}
		} catch (JSONException e) {
			fileLogger.debug("exception while getting oprname");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch(tech) 
		{
			case "GSM":
				
				Cell cell = new Cell(PLMN,mcc,mnc, "1234", "1", "2", 91, 8, "-1", "-1", new JSONObject(),0,0,null,-1,-1);
				cell.setOpr(oprName);
				return cell;
			case "UMTS":
				Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", "2", 91, 8, "-1", "-1", new JSONObject(),10710,511,null,-1,-1);
				cell_1.setOpr(oprName);
				return cell_1;
			default:
				fileLogger.info("Exit Function : genrateDefaultCell");
				return new Cell();
		}
		
	}
	
	/*
	 * This function will genrate the neighbours selected from each qurdant one by one
	 * MAX Neighbour :-6
	 * */
	public ArrayList<Cell> getNeighbourCells(ArrayList<Cell> cells,ArrayList<Cell> selfCells,Double selfLat,Double selfLon,String tech)
	{
		
		
		ArrayList<Cell> quad1 = new ArrayList<Cell>();
		ArrayList<Cell> quad2 = new ArrayList<Cell>();
		ArrayList<Cell> quad3 = new ArrayList<Cell>();
		ArrayList<Cell> quad4 = new ArrayList<Cell>();
		
		Cell[] scanedCells = new Cell[cells.size()];
		scanedCells = (Cell[])cells.toArray(scanedCells);
		
		
		//SANJAY AND VAIBHAV 31 DEC 2020 4 PM 
		//Arrays.sort(scanedCells, Cell.rssiDescending);
		Arrays.sort(scanedCells, Cell.rssiAscending);	
		
		
		//@mighty raju
		/*for(Cell cell:scanedCells) 
		{
			if(Integer.parseInt(cell.getLAC())!=0 && Integer.parseInt(cell.getCELL())!=0){
				if(((Double.parseDouble(cell.getLat()) - selfLat) > 0) && ((Double.parseDouble(cell.getLon()) - selfLon) > 0)) 
				{
					quad1.add(cell);
				}
				if(((Double.parseDouble(cell.getLat()) - selfLat) < 0) && ((Double.parseDouble(cell.getLon()) - selfLon) > 0)) 
				{
					quad2.add(cell);
				}
				if(((Double.parseDouble(cell.getLat()) - selfLat) < 0) && ((Double.parseDouble(cell.getLon()) - selfLon) < 0)) 
				{
					quad3.add(cell);
				}
				if(((Double.parseDouble(cell.getLat()) - selfLat) > 0) && ((Double.parseDouble(cell.getLon()) - selfLon) < 0)) 
				{
					quad4.add(cell);
				}
			}
		}*/
		
		if(tech.equalsIgnoreCase("gsm")) 
		{
			return calulateNighbourFor2g(selfCells,quad1,quad2,quad3,quad4);
		}
		else if(tech.equalsIgnoreCase("umts"))
		{
			return calulateNighbourFor3g(selfCells,quad1,quad2,quad3,quad4);
		}else if(tech.equalsIgnoreCase("lte")){
			return calulateNighbourFor3g(selfCells,quad1,quad2,quad3,quad4);
		}
		
		return null;
	}
	
	/*
	 * Calculate 2g neighbours
	 * */
	public ArrayList<Cell> calulateNighbourFor2g(ArrayList<Cell> selfCells,ArrayList<Cell> quad1,ArrayList<Cell> quad2,ArrayList<Cell> quad3,ArrayList<Cell> quad4)
	{
		ArrayList<Cell> configurationsCells = new  ArrayList<Cell>();
		
		for(Cell selfCell:selfCells) 
		{
			ArrayList<Integer> quadsLength = new ArrayList<Integer>();
			
			quadsLength.add(quad1.size());
			quadsLength.add(quad2.size());
			quadsLength.add(quad3.size());
			quadsLength.add(quad4.size());
			
			Collections.sort(quadsLength);
			
			int totalNeighbourCount =0;
			
			ArrayList<Cell> genratedNeighbours = new ArrayList<Cell>();
			
			for(int i=0;i<quadsLength.get(quadsLength.size()-1);i++) 
			{
				if(quad1.size()-1>=i) 
				{
					if(quad1.get(i).getArfcn() == selfCell.getArfcn()) 
					{	
						
					}
					else 
					{
						genratedNeighbours.add(quad1.get(i));
					}
				}
				if(quad2.size()-1>=i) 
				{
					if(quad2.get(i).getArfcn() == selfCell.getArfcn()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad2.get(i));
					}
				}
				if(quad3.size()-1>=i) 
				{
					if(quad3.get(i).getArfcn() == selfCell.getArfcn()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad3.get(i));
					}
				}
				if(quad4.size()-1>=i) 
				{
					if(quad4.get(i).getArfcn() == selfCell.getArfcn()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad4.get(i));
					}
				}
				
				if(genratedNeighbours.size() == 6) 
				{
					break;
				}
			}
			selfCell.setGenratedNeighbours(genratedNeighbours);
			configurationsCells.add(selfCell);
		}
		
		return configurationsCells;
	}
	
	
	/*
	 * Calculate neighbour for 3g
	 * */
	public ArrayList<Cell> calulateNighbourFor3g(ArrayList<Cell> selfCells,ArrayList<Cell> quad1,ArrayList<Cell> quad2,ArrayList<Cell> quad3,ArrayList<Cell> quad4)
	{
		ArrayList<Cell> configurationsCells = new  ArrayList<Cell>();
		
		for(Cell selfCell:selfCells) 
		{
			ArrayList<Integer> quadsLength = new ArrayList<Integer>();
			
			quadsLength.add(quad1.size());
			quadsLength.add(quad2.size());
			quadsLength.add(quad3.size());
			quadsLength.add(quad4.size());
			
			Collections.sort(quadsLength);
			
			int totalNeighbourCount =0;
			
			
			ArrayList<Cell> genratedNeighbours = new ArrayList<Cell>();
			
			for(int i=0;i<quadsLength.get(quadsLength.size()-1);i++) 
			{
				if(quad1.size()-1>=i) 
				{
					if(quad1.get(i).getUarfcn() == selfCell.getUarfcn() && quad1.get(i).getPsc() == selfCell.getPsc()) 
					{	
						
					}
					else 
					{
						genratedNeighbours.add(quad1.get(i));
					}
				}
				if(quad2.size()-1>=i) 
				{
					if(quad2.get(i).getUarfcn() == selfCell.getUarfcn() && quad2.get(i).getPsc() == selfCell.getPsc()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad2.get(i));
					}
				}
				if(quad3.size()-1>=i) 
				{
					if(quad3.get(i).getUarfcn() == selfCell.getUarfcn() && quad3.get(i).getPsc() == selfCell.getPsc()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad3.get(i));
					}
				}
				if(quad4.size()-1>=i) 
				{
					if(quad4.get(i).getUarfcn() == selfCell.getUarfcn() && quad4.get(i).getPsc() == selfCell.getPsc()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad4.get(i));
					}
				}
				
				if(genratedNeighbours.size() == 32) 
				{
					break;
				}
			}
			selfCell.setGenratedNeighbours(genratedNeighbours);
			configurationsCells.add(selfCell);
		}
		
		return configurationsCells;
	}
	
	/*
	 * Calculate neighbour for 3g
	 * */
	public ArrayList<Cell> calulateNighbourFor4g(ArrayList<Cell> selfCells,ArrayList<Cell> quad1,ArrayList<Cell> quad2,ArrayList<Cell> quad3,ArrayList<Cell> quad4)
	{
		ArrayList<Cell> configurationsCells = new  ArrayList<Cell>();
		
		for(Cell selfCell:selfCells) 
		{
			ArrayList<Integer> quadsLength = new ArrayList<Integer>();
			
			quadsLength.add(quad1.size());
			quadsLength.add(quad2.size());
			quadsLength.add(quad3.size());
			quadsLength.add(quad4.size());
			
			Collections.sort(quadsLength);
			
			int totalNeighbourCount =0;
			
			
			ArrayList<Cell> genratedNeighbours = new ArrayList<Cell>();
			
			for(int i=0;i<quadsLength.get(quadsLength.size()-1);i++) 
			{
				if(quad1.size()-1>=i) 
				{
					if(quad1.get(i).getEarfcn() == selfCell.getEarfcn() && quad1.get(i).getPci() == selfCell.getPci()) 
					{	
						
					}
					else 
					{
						genratedNeighbours.add(quad1.get(i));
					}
				}
				if(quad2.size()-1>=i) 
				{
					if(quad2.get(i).getEarfcn() == selfCell.getEarfcn() && quad2.get(i).getPci() == selfCell.getPci()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad2.get(i));
					}
				}
				if(quad3.size()-1>=i) 
				{
					if(quad3.get(i).getEarfcn() == selfCell.getEarfcn() && quad3.get(i).getPci() == selfCell.getPci()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad3.get(i));
					}
				}
				if(quad4.size()-1>=i) 
				{
					if(quad4.get(i).getEarfcn() == selfCell.getEarfcn() && quad4.get(i).getPci() == selfCell.getPci()) 
					{
						
					}
					else 
					{
						genratedNeighbours.add(quad4.get(i));
					}
				}
				
				if(genratedNeighbours.size() == 32) 
				{
					break;
				}
			}
			selfCell.setGenratedNeighbours(genratedNeighbours);
			configurationsCells.add(selfCell);
		}
		
		return configurationsCells;
	}
	
	/**
	 * Makes a deep copy of any Java object that is passed.
	 */
	 public Object deepCopy(ArrayList<Cell> object) {
	   try {
	     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	     ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
	     outputStrm.writeObject(object);
	     ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
	     ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
	     return objInputStream.readObject();
	   }
	   catch (Exception e) {
	     e.printStackTrace();
	     return null;
	   }
	 }
	 
	 /*
	  * Returns the position of system at the time of function called
	  * */	 
	 public JSONObject getSystemPossition() 
	 {
			fileLogger.info("Inside Function : getSystemPossition");
		 try 
		 {
			 String query = "select lat,lon from gpsdata order by id desc limit 1";
			 JSONArray ja = new Operations().getJson(query);
			 if(ja.length()!=0){
				return ja.getJSONObject(0);
			 }else{
				JSONObject gpsPosObj = new JSONObject();
				HashMap dbMap=new Common().getDbCredential();
				gpsPosObj.put("lat",dbMap.get("lat"));
				gpsPosObj.put("lon",dbMap.get("lon"));
				return gpsPosObj; 
			 }
		 }
		 catch(Exception E) 
		 {
			// fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = getSystemPossition");
				E.printStackTrace();
				//fileLogger.debug("******************************************************");
		 }
			fileLogger.info("Exit Function : getSystemPossition");
		 return null;
	 }
	 
	 
	 /*
	  * Get distinct plmn scaned from
	  * */	 
	 public ArrayList<String> getPlmns() 
	 {
			fileLogger.info("Inside Function : getPlmns");
		 String query = "select distinct concat(mcc,mnc) plmn from oprlogs_current where oprid  = (select max(id) from oprrationdata)";
		 JSONArray plmnJson = new Operations().getJson(query);
		 ArrayList<String> PLMNS = new ArrayList<String>();
		 try 
		 {
			 for(int i=0;i<plmnJson.length();i++) 
			 {
				 String PLMN = plmnJson.getJSONObject(i).getString("plmn");
				 PLMNS.add(PLMN);
			 }
		 }
		 catch(Exception E) 
		 {
			 //	fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = getPlmns");
				E.printStackTrace();
				//fileLogger.debug("******************************************************");
		 }
			fileLogger.info("Exit Function : getPlmns");
		 return PLMNS;
		 
	 }
	 
	 
	 
	 /*
	  * Returns PLMN for given operator names
	  * input:-Array of opertor names
	  * */
	 public ArrayList<String> getPlmns(String[] opr) 
	 {
			fileLogger.info("Inside Function : getPlmns");
		StringBuilder oprString  = new StringBuilder();
		
		for(int i=0;i<opr.length;i++) 
		{
			
			if(i==0) 
			{
				oprString.append("'"+opr[i]+"'");
			}
			else 
			{
				oprString.append(",'"+opr[i]+"'");
			}
		} 
		
		 String query = "select plmn from view_current_scanned_opr_last_24_hours where opr in ("+oprString.toString()+");";
		 JSONArray plmnJson = new Operations().getJson(query);
		 ArrayList<String> PLMNS = new ArrayList<String>();
		 try 
		 {
			 for(int i=0;i<plmnJson.length();i++) 
			 {
				 String PLMN = plmnJson.getJSONObject(i).getString("plmn");
				 PLMNS.add(PLMN);
			 }
		 }
		 catch(Exception E) 
		 {
			 //	fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = getPlmns");
				E.printStackTrace();
			//	fileLogger.debug("******************************************************");
		 }
			fileLogger.info("Exit Function : getPlmns");
		 return PLMNS;
		 
	 }
	 
	 
	 public String convertToJson(HashMap<String,ArrayList<ConfigData>> cell) throws Exception
	 {
		 ObjectMapper mapper = new ObjectMapper();
		 //mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		 
		 StringWriter ss = new StringWriter();
		 mapper.writeValue(ss, cell);
		 //String jsonInString = mapper.writeValueAsString(cell);		 
		 return ss.toString();
	 }
	 
	 
	 /*
	  * This will Convert List of Conifuration packet to Json,Genrated JSON  will be in format that client can consume
	  * */
	 public ArrayList<ConfigData> genrateJson(ArrayList<Cell> cells,int[] packetId,String tech,int antennaId) throws Exception
	 {
		 
		 ArrayList<ConfigData> data1 = new ArrayList<ConfigData>();
		 
		 int count = 1;
		 for(Cell cell:cells) 
		 {
			
			//HashMap<String,ArrayList< HashMap<String,String>>> data = new HashMap<String,ArrayList< HashMap<String,String>>>();
			ConfigData data = new ConfigData();
			
			ArrayList< HashMap<String,String>> configData = new ArrayList< HashMap<String,String>>();
			
			HashMap<String,String> config = convertCellDataToHashMap(cell,"self",tech);
			
			configData.add(config);
			
			for(Cell nbrCell: cell.getGenratedNeighbours()) 
			{
				configData.add(convertCellDataToHashMap(nbrCell,"neighbour",tech));
			}
			Operations operations = new Operations();
			String antennaName="";
			antennaName=operations.getJson("select profile_name from antenna where id="+antennaId).getJSONObject(0).getString("profile_name");
			
			data.setData(configData);
			data.setId(packetId[0]+"");
			data.setTech(tech);
			data.setDuration("3");
			data.setPlmn(cell.getPLMN());
			data.setAntennaId(Integer.toString(antennaId));
			data.setAntennaName(antennaName);
			data1.add(data);
			packetId[0]++;
		 }
		 
		 return data1;
	 } 
	 
	 /*
	  * Returns HashMap of Cell
	  * */
	 public HashMap<String,String> convertCellDataToHashMap(Cell cell,String flag,String tech)
	 {
		 HashMap<String,String> cellMap = new HashMap<String,String>();
			
		 cellMap.put("flag", flag);
		 cellMap.put("plmn", cell.getPLMN());
		 cellMap.put("lac",(!tech.equalsIgnoreCase("4G")?cell.getLAC()+"":""));
		 cellMap.put("cell", cell.getCELL());
		 cellMap.put("arfcn", ""+(tech.equalsIgnoreCase("2G")?cell.getArfcn()+"":""));
		 cellMap.put("bsic", (tech.equalsIgnoreCase("2G")?Integer.parseInt(cell.getBsic())+"":""));
		 cellMap.put("uarfcn", tech.equalsIgnoreCase("3G")?cell.getUarfcn()+"":"");
		 cellMap.put("psc", tech.equalsIgnoreCase("3G")?cell.getPsc()+"":"");
		 cellMap.put("earfcn", tech.equalsIgnoreCase("4G")?cell.getEarfcn()+"":"");
		 cellMap.put("pci", tech.equalsIgnoreCase("4G")?cell.getPci()+"":"");
		 cellMap.put("tac",(tech.equalsIgnoreCase("4G")?cell.getTac()+"":""));
		 cellMap.put("rssi", ""+cell.getRssi());
		 cellMap.put("opr", ""+cell.getOpr());
		 cellMap.put("band", ""+cell.getBand());
		 return cellMap;
	 }
	 
	 public ArrayList<ConfigData> checkCellParamsOnOprlogsCurrent(ArrayList<ConfigData> configurations,int antenna){
			fileLogger.info("Inside Function : checkCellParamsOnOprlogsCurrent");
		 String query="select arfcn,uarfcn,psc,earfcn,pci from oprlogs_current where antenna_id="+antenna+" and ((inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND inserttime <= timezone('utc'::text, now())) or ( count = 'locator_count'))";
		 JSONArray jsonArr=new Operations().getJson(query);
		 ArrayList<ConfigData> configList = new ArrayList<ConfigData>();
		 try{
		for(ConfigData confData:configurations){
			ArrayList< HashMap<String,String>> data=confData.getData();
			String tech=confData.getTech();
			confData.setCheck("no");
			String plmn="";
			String lac="";
			String cell="";
			String arfcn="";
			String uarfcn="";
			String psc="";
			String earfcn="";
			String pci="";
			boolean isPresent=false;
			for(HashMap<String,String> confMap:data){
				if(confMap.get("flag").equals("self")){
				plmn=confMap.get("plmn");
				lac=confMap.get("lac");
				cell=confMap.get("cell");
				arfcn=confMap.get("arfcn");
				uarfcn=confMap.get("uarfcn");
				psc=confMap.get("psc");
				earfcn=confMap.get("earfcn");
				pci=confMap.get("pci");
				for(int i=0;i<jsonArr.length();i++){
					JSONObject tempObj=jsonArr.getJSONObject(i);	
					String tempArfcn="";
					String tempUarfcn="";
					String tempPsc="";
					String tempEarfcn="";
					String tempPci="";
					if(tech.equalsIgnoreCase("2G")){
						tempArfcn=tempObj.getString("arfcn");
						if(arfcn.equalsIgnoreCase(tempArfcn)){
							isPresent=true;
						}
					}else if(tech.equalsIgnoreCase("3G")){
						tempUarfcn=tempObj.getString("uarfcn");
						tempPsc=tempObj.getString("psc");
						if(uarfcn.equalsIgnoreCase(tempUarfcn) && psc.equalsIgnoreCase(tempPsc)){
							isPresent=true;
						}
					}else if(tech.equalsIgnoreCase("4G")){
						tempEarfcn=tempObj.getString("earfcn");
						tempPci=tempObj.getString("pci");
						if(earfcn.equalsIgnoreCase(tempEarfcn) && pci.equalsIgnoreCase(tempPci)){
							isPresent=true;
						}
					}
				}
			}
			}
				if(isPresent)
					confData.setCheck("yes");
				else
					confData.setCheck("no");

				configList.add(confData);
		}
		 }catch(Exception E){
			 	//fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = checkCellParamsOnOprlogsCurrent");
				E.printStackTrace();
				//fileLogger.debug("******************************************************"); 
		 }
		 //fileLogger.debug("*******************************************************************");
		 fileLogger.debug("configuration size in end:"+configList.size());
		 //fileLogger.debug("*******************************************************************");
			fileLogger.info("Exit Function : checkCellParamsOnOprlogsCurrent");
		 return configList; 
	 }
	 
	 public boolean checkDuplicatesUarfcnPsc(ArrayList<Cell> selfCellsWithNeighbours,Cell cell1){
			fileLogger.info("Inside Function : checkDuplicatesUarfcnPsc");
		 for(Cell cell:selfCellsWithNeighbours){
			if(cell.getUarfcn()==cell1.getUarfcn() && cell.getPsc()==cell1.getPsc()){
				fileLogger.debug(cell.getUarfcn() +" psc"+cell.getPsc() );
				return false;
			}
		 }
		 return true; 
	 }
	 
	 public boolean checkDuplicatesArfcn(ArrayList<Cell> selfCellsWithNeighbours,Cell cell1){
		  
		 for(Cell cell:selfCellsWithNeighbours){
			if(cell.getArfcn()==cell1.getArfcn()){
				fileLogger.debug("arfcn in duplicacy is :"+ cell.getArfcn());
				return false;
			}
		 }	fileLogger.info("Exit Function : checkDuplicatesUarfcnPsc");

		 return true; 
	 }
	 
	 public ArrayList<Cell> getDistinct4GCell(ArrayList<Cell> cellsForGivenPlmn){
			fileLogger.info("Inside Function : getDistinct4GCell");

		 HashMap<String,Cell> cellWithNeighbour= new HashMap<String,Cell>();
		 HashMap<String,Cell> cellWithoutNeighbour= new HashMap<String,Cell>();
		 ArrayList<Cell> distinctCell=new ArrayList<Cell>();
		 try {
			for(Cell cell:cellsForGivenPlmn){
				String cgikey=cell.getMCC()+cell.getMNC()+cell.getLAC()+cell.getCELL();
				 if(cell.getNeighbour()!=null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length()>0 || cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length()>0)){
				   	 if(!cellWithNeighbour.containsKey(cgikey)){
				   		cellWithNeighbour.put(cgikey, cell);
				   		if(cellWithoutNeighbour.containsKey(cgikey)){
				   			cellWithoutNeighbour.remove(cgikey);
				   		}
				   	 }
			     }else{
			    	 
			    	 if(!cellWithNeighbour.containsKey(cgikey))
			    	 {
			    		 cellWithoutNeighbour.put(cgikey,cell);
					 }
			    	 
			     }
			 }
			for(String key:cellWithNeighbour.keySet()){
				distinctCell.add(cellWithNeighbour.get(key));
			}
			for(String key:cellWithoutNeighbour.keySet()){
				distinctCell.add(cellWithoutNeighbour.get(key));
			}
			
			for(Cell cell:distinctCell)
			{
				System.out.println(cell.getMNC()+"-"+cell.getMCC()+"-"+cell.getCELL()+"-"+cell.getLAC());
			} 
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block

		 	//fileLogger.debug("******************************************************"+e.getMessage());
			fileLogger.debug("Class = PossibleConfigurations  Method = getDistinct3GCell");
			e.printStackTrace();
			//fileLogger.debug("******************************************************"); 
		}
			fileLogger.info("Exit Function : getDistinct4GCell");
		 return distinctCell;
	 }
	 
	 public ArrayList<Cell> getDistinct3GCell(ArrayList<Cell> cellsForGivenPlmn){
			fileLogger.info("Inside Function : getDistinct3GCell");
		 HashMap<String,Cell> cellWithNeighbour= new HashMap<String,Cell>();
		 HashMap<String,Cell> cellWithoutNeighbour= new HashMap<String,Cell>();
		 ArrayList<Cell> distinctCell=new ArrayList<Cell>();
		 try {
			for(Cell cell:cellsForGivenPlmn){
				String cgikey=cell.getMCC()+cell.getMNC()+cell.getLAC()+cell.getCELL();
				 if(cell.getNeighbour()!=null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length()>0 || cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length()>0)){
				   	 if(!cellWithNeighbour.containsKey(cgikey)){
				   		cellWithNeighbour.put(cgikey, cell);
				   		if(cellWithoutNeighbour.containsKey(cgikey)){
				   			cellWithoutNeighbour.remove(cgikey);
				   		}
				   	 }
			     }else{
			    	 
			    	 if(!cellWithNeighbour.containsKey(cgikey))
			    	 {
			    		 cellWithoutNeighbour.put(cgikey,cell);
					 }
			    	 
			     }
			 }
			for(String key:cellWithNeighbour.keySet()){
				distinctCell.add(cellWithNeighbour.get(key));
			}
			for(String key:cellWithoutNeighbour.keySet()){
				distinctCell.add(cellWithoutNeighbour.get(key));
			}
			
			for(Cell cell:distinctCell)
			{
				System.out.println(cell.getMNC()+"-"+cell.getMCC()+"-"+cell.getCELL()+"-"+cell.getLAC());
			} 
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block

		 	//fileLogger.debug("******************************************************"+e.getMessage());
			fileLogger.debug("Class = PossibleConfigurations  Method = getDistinct3GCell");
			e.printStackTrace();
			//fileLogger.debug("******************************************************"); 
		}
			fileLogger.info("Exit Function : getDistinct3GCell");
		 return distinctCell;
	 }
	 
	 
	 
	 public ArrayList<Cell> getDistinct2GCell(ArrayList<Cell> cellsForGivenPlmn){
			fileLogger.info("Inside Function : getDistinct2GCell");
		 HashMap<String,Cell> cellWithNeighbour= new HashMap<String,Cell>();
		 HashMap<String,Cell> cellWithoutNeighbour= new HashMap<String,Cell>();
		 ArrayList<Cell> distinctCell=new ArrayList<Cell>();
		 try {
			for(Cell cell:cellsForGivenPlmn){
				String cgikey=cell.getMCC()+cell.getMNC()+cell.getLAC()+cell.getCELL();
				 if(cell.getNeighbour()!=null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length()>0)){
				   	 if(!cellWithNeighbour.containsKey(cgikey)){
				   		cellWithNeighbour.put(cgikey, cell);
				   		if(cellWithoutNeighbour.containsKey(cgikey)){
				   			cellWithoutNeighbour.remove(cgikey);
				   		}
				   	 }
			     }else{
			    	 
			    	 if(!cellWithNeighbour.containsKey(cgikey))
			    	 {
			    		 cellWithoutNeighbour.put(cgikey,cell);
					 }
			    	 
			     }
			 }
			for(String key:cellWithNeighbour.keySet()){
				distinctCell.add(cellWithNeighbour.get(key));
			}
			for(String key:cellWithoutNeighbour.keySet()){
				distinctCell.add(cellWithoutNeighbour.get(key));
			}
			
			for(Cell cell:distinctCell)
			{
				System.out.println(cell.getMNC()+"-"+cell.getMCC()+"-"+cell.getCELL()+"-"+cell.getLAC());
			} 
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block

		 	//fileLogger.debug("******************************************************"+e.getMessage());
			fileLogger.debug("Class = PossibleConfigurations  Method = getDistinct3GCell");
			e.printStackTrace();
			//fileLogger.debug("******************************************************"); 
		}
		 fileLogger.info("Exit Function : getDistinct2GCell");
		 return distinctCell;
	 }
	 
	 
	 public ArrayList<Integer> getDistinctfcn(String type,String plmn)
	 {
		 fileLogger.info("Inside Function : getDistinctfcn");
		 String query ="select distinct("+type+") as fcn from oprlogs_current  where mcc||mnc = '"+plmn+"'  and "+type+" is not null  and inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND inserttime <= timezone('utc'::text, now()) and cell!='1'";
		 JSONArray jo = new Operations().getJson(query);
		 ArrayList<Integer> fcns = new ArrayList<Integer>();
		 for(int i=0;i<jo.length();i++)
		 {
			 try 
			 {
				fcns.add(Integer.parseInt(jo.getJSONObject(i).getString("fcn")));
			 }
			 catch (Exception e) 
			 {
				//fileLogger.debug("******************************************************"+e.getMessage());
				fileLogger.debug("Class = PossibleConfigurations  Method = getDistinctfcn");
				e.printStackTrace();
				//fileLogger.debug("******************************************************");
			}
		 }
		 fileLogger.info("Exit Function : getDistinctfcn");
		 return fcns;
	 }
	 
	 public HashMap<String,NeighbourUarfcn[]> getNeighbourUarfcnMap(NeighbourUarfcn[] neigboursUarfcn,ArrayList<Cell> cellsForGivenPlmn,ArrayList<Cell> SectorCells)
	 {
		 HashMap<String,NeighbourUarfcn[]> neighbourUarfcnMap=new HashMap<String,NeighbourUarfcn[]>();
		 ArrayList<NeighbourUarfcn> uarfcnAndCountSelfCell = new ArrayList<NeighbourUarfcn>();
		 ArrayList<NeighbourUarfcn> uarfcnAndCountNeighbourCell = new ArrayList<NeighbourUarfcn>();
			
			int[] statusArr=new int[neigboursUarfcn.length];
			
			
			for(int statusCount=0;statusCount<neigboursUarfcn.length;statusCount++){
				statusArr[statusCount]=0;
			}
			
			for(int i=0;i<neigboursUarfcn.length;i++)
			{
				for(Cell cell:SectorCells)
				{
					if(neigboursUarfcn[i].getUarfcn()==cell.getUarfcn() && neigboursUarfcn[i].getPsc()==cell.getPsc()){
						uarfcnAndCountSelfCell.add(neigboursUarfcn[i]);
						statusArr[i]=1;
						break;
					}
						
				}
			}
			
			for(int y=0;y<neigboursUarfcn.length;y++){
				if(statusArr[y]==0){
					uarfcnAndCountNeighbourCell.add(neigboursUarfcn[y]);
				}
			}
			
			NeighbourUarfcn[] uarfcnAndCountSelfCellArr= new NeighbourUarfcn[uarfcnAndCountSelfCell.size()];
			uarfcnAndCountSelfCellArr = uarfcnAndCountSelfCell.toArray(uarfcnAndCountSelfCellArr);	
			NeighbourUarfcn[] uarfcnAndCountNeighbourCellArr= new NeighbourUarfcn[uarfcnAndCountNeighbourCell.size()];
			uarfcnAndCountNeighbourCellArr = uarfcnAndCountNeighbourCell.toArray(uarfcnAndCountNeighbourCellArr);	
			
			neighbourUarfcnMap.put("uarfcnAndCountSelfCell", uarfcnAndCountSelfCellArr);
			neighbourUarfcnMap.put("uarfcnAndCountNeighbourCell", uarfcnAndCountNeighbourCellArr);
			
			return neighbourUarfcnMap;
		 
	 } 
	 
		public ArrayList<Cell> getSelfCellsForConfiguration3G(ArrayList<Cell> plmnCells,HashMap<String,NeighbourUarfcn[]> neighbourUarfcnMap,String PLMN,ArrayList<Integer> totalUarfcn)
		{	 
			fileLogger.info("Inside Function : getSelfCellsForConfiguration3G");
			for(Cell cc:plmnCells) 
			{
				fileLogger.debug(cc.getArfcn()+"-"+cc.getUarfcn()+"-"+cc.getPsc());
			}
			
			NeighbourUarfcn[] uarfcnAndCountSelfCell= neighbourUarfcnMap.get("uarfcnAndCountSelfCell");
			NeighbourUarfcn[] uarfcnAndCountNeighbourCell=neighbourUarfcnMap.get("uarfcnAndCountNeighbourCell");
			
			ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
			
			ArrayList<Cell> cells = new ArrayList<Cell>();		
			try 
			{
				//Cloning the cells of plmn it will secure and will not allow updating the refrence inside the object
				for(Cell cell:plmnCells) 
				{
					cells.add((Cell)cell.clone());
				}
				
				String[] cellCoverStatus=new String[cells.size()];
				for(int k=0;k<cells.size();k++){
					cellCoverStatus[k]="n";
				}
				
				for(NeighbourUarfcn na:uarfcnAndCountNeighbourCell){
					boolean naMatch=false;
					for(int j=0;j<cells.size();j++){
						
						Cell cell=cells.get(j);
						//if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
							if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0)){
							JSONArray nbrUarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								//if((na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC") || na.getPsc()==cell.getPsc()) && cellCoverStatus[j]=="n")
								if(na.getUarfcn() == cell.getUarfcn() && (na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC")) && cellCoverStatus[j]=="n")
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}
								}
							}
	/*						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn_inter.length();i++){
								if(((na.getUarfcn()==nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc()==nbrUarfcn_inter.getJSONObject(i).getInt("PSC"))||(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc())) && cellCoverStatus[j]=="n"){
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									cell1.setUarfcn(na.getUarfcn());	
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}	
								}

							}*/
						}
					}
				}
				
				ArrayList<NeighbourUarfcn> uarfcnPSCcount = new ArrayList<NeighbourUarfcn>();
				
				for(Integer i :totalUarfcn)
				{
					for(NeighbourUarfcn na:uarfcnAndCountNeighbourCell)
					{
						if(na.getUarfcn()==i)
						{
							uarfcnPSCcount.add(na);
							break;
						}
					}
				}
				
				/****check with uarfcn distinct**/
				for(NeighbourUarfcn na :uarfcnPSCcount){
					boolean flag=false;
				for(Cell cell:selfCellsWithNeighbours){
						if(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc()){
						 flag=true;
						 break;
						}
					}
				/*if(!flag){
					String mcc = (PLMN.substring(0,3));
					String mnc = (PLMN.substring(3));
					String oprName="NA";
					JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
					try {
						if(oprNameArray.length()>0){
							oprName=oprNameArray.getJSONObject(0).getString("opr");
						}
					} catch (JSONException e) {
						fileLogger.error("exception while getting oprname");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("umts",na.getUarfcn()), 91, 8, "-1", "-1", new JSONObject(),na.getUarfcn(),na.getPsc(),null,-1,-1);
					cell_1.setOpr(oprName);
					selfCellsWithNeighbours.add(cell_1);
				}*/
				}
				
				for(NeighbourUarfcn na:uarfcnAndCountSelfCell){
					boolean naMatch=false;
					for(int j=0;j<cells.size();j++){
						Cell cell=cells.get(j);
						//if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
						if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0)){	
							JSONArray nbrUarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								if((na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC") || na.getPsc()==cell.getPsc()) && cellCoverStatus[j]=="n") 
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}
								}
							}
	/*						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn_inter.length();i++){
								if(((na.getUarfcn()==nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc()==nbrUarfcn_inter.getJSONObject(i).getInt("PSC"))||(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc())) && cellCoverStatus[j]=="n"){
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									cell1.setUarfcn(na.getUarfcn());	
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}	
								}

							}*/
						}else{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								if( na.getPsc()==cell.getPsc() && cellCoverStatus[j]=="n") 
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getUarfcn());
									cell1.setPsc(na.getPsc());
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									}
								}

						}
					}
				}
				
				for(int cellCount=0;cellCount<cells.size();cellCount++){
					if(cellCoverStatus[cellCount].equals("n")){
						Cell cell1 = (Cell)cells.get(cellCount).clone();
						cell1.setBand(calculateBand("umts",cell1.getUarfcn()));
						selfCellsWithNeighbours.add(cell1);
					}
				}
				
			 ArrayList<NeighbourUarfcn> uarfcnPSCcountSelf = new ArrayList<NeighbourUarfcn>();
				
				for(Integer i :totalUarfcn)
				{
					for(NeighbourUarfcn na:uarfcnAndCountSelfCell)
					{
						if(na.getUarfcn()==i)
						{
							uarfcnPSCcountSelf.add(na);
							break;
						}
					}
				}
				
				//****check with uarfcn distinct**//*
				for(NeighbourUarfcn na :uarfcnPSCcountSelf){
					boolean flag=false;
				for(Cell cell:selfCellsWithNeighbours){
						if(na.getUarfcn()==cell.getUarfcn()){
						 flag=true;
						 break;
						}
					}
				/*if(!flag){
					String mcc = (PLMN.substring(0,3));
					String mnc = (PLMN.substring(3));
					String oprName="NA";
					JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
					try {
						if(oprNameArray.length()>0){
							oprName=oprNameArray.getJSONObject(0).getString("opr");
						}
					} catch (JSONException e) {
						fileLogger.error("exception while getting oprname");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("umts",na.getUarfcn()), 91, 8, "-1", "-1", new JSONObject(),na.getUarfcn(),na.getPsc(),null,-1,-1);
					cell_1.setOpr(oprName);
					selfCellsWithNeighbours.add(cell_1);
				}*/
				}
				
				
				Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
				tempCells = selfCellsWithNeighbours.toArray(tempCells);
				Arrays.sort(tempCells, Cell.arfcnAscending);
				selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
				
				
			}
			catch(Exception E) 
			{
			//	fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration3G");
				E.printStackTrace();
				//fileLogger.debug("******************************************************");
			}	
			fileLogger.info("Exit Function : getSelfCellsForConfiguration3G");
			return selfCellsWithNeighbours;
		}
		
		public ArrayList<Cell> getSelfCellsForConfiguration3GUpdated(ArrayList<Cell> plmnCells,HashMap<String,NeighbourUarfcn[]> neighbourUarfcnMap,String PLMN,ArrayList<Integer> totalUarfcn)
		{	 
			fileLogger.info("Inside Function : getSelfCellsForConfiguration3GUpdated");
			String compare="";
			for(Cell cc:plmnCells) 
			{
				fileLogger.debug(cc.getArfcn()+"-"+cc.getUarfcn()+"-"+cc.getPsc());
			}
			
			NeighbourUarfcn[] uarfcnAndCountSelfCell= neighbourUarfcnMap.get("uarfcnAndCountSelfCell");
			NeighbourUarfcn[] uarfcnAndCountNeighbourCell=neighbourUarfcnMap.get("uarfcnAndCountNeighbourCell");
			
			ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
			
			ArrayList<Cell> cells = new ArrayList<Cell>();		
			try 
			{
				//Cloning the cells of plmn it will secure and will not allow updating the refrence inside the object
				for(Cell cell:plmnCells) 
				{
					cells.add((Cell)cell.clone());
				}
				
				String[] cellCoverStatus=new String[cells.size()];
				for(int k=0;k<cells.size();k++){
					cellCoverStatus[k]="n";
				}
				compare=",";
				for(NeighbourUarfcn na:uarfcnAndCountNeighbourCell){
					boolean naMatch=false;
					if(compare.contains(","+na.getUarfcn()+"-"+na.getPsc()+",")) {
						continue;
					}
					for(int j=0;j<cells.size();j++){
						
						Cell cell=cells.get(j);
						//if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
							if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0)){
							JSONArray nbrUarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								//if((na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC") || na.getPsc()==cell.getPsc()) && cellCoverStatus[j]=="n")
								if(na.getUarfcn() == cell.getUarfcn() && (na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC")))// && cellCoverStatus[j]=="n")
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									int rssi = getrssi(""+cell.getUarfcn(),""+na.getPsc(),"umts");
									if (rssi!=-999)
										cell1.setRssi(rssi);
									else 
										cell1.setRssi(999);
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									compare+=na.getUarfcn()+"-"+na.getPsc()+",";
									break;
									}
								}
							}
							if(naMatch){
								
								break;
							}
	/*						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn_inter.length();i++){
								if(((na.getUarfcn()==nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc()==nbrUarfcn_inter.getJSONObject(i).getInt("PSC"))||(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc())) && cellCoverStatus[j]=="n"){
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									cell1.setUarfcn(na.getUarfcn());	
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}	
								}

							}*/
						}
					}
				}
				
				
				ArrayList<NeighbourUarfcn> uarfcnPSCcount = new ArrayList<NeighbourUarfcn>();
				if(uarfcnAndCountNeighbourCell.length==0) {
					String uarfcnPSCtemp = ",";
					for(int j=0;j<cells.size();j++){
						Cell cell=cells.get(j);
						if(!(uarfcnPSCtemp.contains(","+cell.getUarfcn()+","))) {
							int pscReceived=calculateFreePsc(cell.getPsc());
							if(pscReceived!=-1)
							{
								Cell cell1 = (Cell)cell.clone();
								cell1.setPsc(pscReceived);
								cell1.setBand(calculateBand("umts",cell.getUarfcn()));
								selfCellsWithNeighbours.add(cell1);
								
								
							}
							
							uarfcnPSCtemp+=cell.getUarfcn()+",";
						

						}
					
					
						
					}	
				}
				
				for(Integer i :totalUarfcn)
				{
					for(NeighbourUarfcn na:uarfcnAndCountNeighbourCell)
					{
						if(na.getUarfcn()==i)
						{
							uarfcnPSCcount.add(na);
							break;
						}
					}
				}
				
				/****check with uarfcn distinct**/
				for(NeighbourUarfcn na :uarfcnPSCcount){
					boolean flag=false;
				for(Cell cell:selfCellsWithNeighbours){
						if(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc()){
						 flag=true;
						 break;
						}
					}
				/*if(!flag){
					String mcc = (PLMN.substring(0,3));
					String mnc = (PLMN.substring(3));
					String oprName="NA";
					JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
					try {
						if(oprNameArray.length()>0){
							oprName=oprNameArray.getJSONObject(0).getString("opr");
						}
					} catch (JSONException e) {
						fileLogger.error("exception while getting oprname");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("umts",na.getUarfcn()), 91, 8, "-1", "-1", new JSONObject(),na.getUarfcn(),na.getPsc(),null,-1,-1);
					cell_1.setOpr(oprName);
					selfCellsWithNeighbours.add(cell_1);
				}*/
				}
				
				/*
				 * for(int j=0;j<cells.size();j++){ Cell cell=cells.get(j);
				 * selfCellsWithNeighbours.add(cell); }
				 */
				for(NeighbourUarfcn na:uarfcnAndCountSelfCell){
					boolean naMatch=false;
					if(compare.contains(","+na.getUarfcn()+"-"+na.getPsc()+",")) {
						continue;
					}
					/*
					 * if(compare.contains(","+na.getUarfcn()+",")) { continue; }
					 */
					for(int j=0;j<cells.size();j++){
						Cell cell=cells.get(j);
						//if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
						if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0)){	
							JSONArray nbrUarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								if((na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC") || na.getPsc()==cell.getPsc()))// && cellCoverStatus[j]=="n") 
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									int rssi = getrssi(""+cell.getUarfcn(),""+na.getPsc(),"umts");
									if (rssi!=-999)
										cell1.setRssi(rssi);
									else 
										cell1.setRssi(999);
									cell1.setPsc(na.getPsc());
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									compare+=na.getUarfcn()+"-"+na.getPsc()+",";
									break;
									}
								}
							}
							if(naMatch==true) {
								break;
							}
	/*						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn_inter.length();i++){
								if(((na.getUarfcn()==nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc()==nbrUarfcn_inter.getJSONObject(i).getInt("PSC"))||(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc())) && cellCoverStatus[j]=="n"){
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									cell1.setUarfcn(na.getUarfcn());	
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}	
								}

							}*/
						}else{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								if( na.getPsc()==cell.getPsc() && cellCoverStatus[j]=="n") 
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getUarfcn());
									cell1.setPsc(na.getPsc());
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									}
								}

						}
					}
				}
				
				for(int cellCount=0;cellCount<cells.size();cellCount++){
					if(cellCoverStatus[cellCount].equals("n")){
						Cell cell1 = (Cell)cells.get(cellCount).clone();
						cell1.setBand(calculateBand("umts",cell1.getUarfcn()));
						selfCellsWithNeighbours.add(cell1);
					}
				}
				
			 ArrayList<NeighbourUarfcn> uarfcnPSCcountSelf = new ArrayList<NeighbourUarfcn>();
				
				for(Integer i :totalUarfcn)
				{
					for(NeighbourUarfcn na:uarfcnAndCountSelfCell)
					{
						if(na.getUarfcn()==i)
						{
							uarfcnPSCcountSelf.add(na);
							break;
						}
					}
				}
				
				//****check with uarfcn distinct**//*
				for(NeighbourUarfcn na :uarfcnPSCcountSelf){
					boolean flag=false;
				for(Cell cell:selfCellsWithNeighbours){
						if(na.getUarfcn()==cell.getUarfcn()){
						 flag=true;
						 break;
						}
					}
				/*if(!flag){
					String mcc = (PLMN.substring(0,3));
					String mnc = (PLMN.substring(3));
					String oprName="NA";
					JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
					try {
						if(oprNameArray.length()>0){
							oprName=oprNameArray.getJSONObject(0).getString("opr");
						}
					} catch (JSONException e) {
						fileLogger.error("exception while getting oprname");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("umts",na.getUarfcn()), 91, 8, "-1", "-1", new JSONObject(),na.getUarfcn(),na.getPsc(),null,-1,-1);
					cell_1.setOpr(oprName);
					selfCellsWithNeighbours.add(cell_1);
				}*/
				}
				
				
				Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
				tempCells = selfCellsWithNeighbours.toArray(tempCells);
				Arrays.sort(tempCells, Cell.arfcnAscending);
				selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
				
				
			}
			catch(Exception E) 
			{
			//	fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration3G");
				E.printStackTrace();
				//fileLogger.debug("******************************************************");
			}	
			fileLogger.info("Exit Function : getSelfCellsForConfiguration3G");
			return selfCellsWithNeighbours;
		}
		
		public ArrayList<Cell> getSelfCellsForConfiguration4G(ArrayList<Cell> plmnCells,HashMap<String,NeighbourEarfcn[]> neighbourEarfcnMap,String PLMN,ArrayList<Integer> totalEarfcn)
		{	
			fileLogger.info("Inside Function : getSelfCellsForConfiguration4G");
			for(Cell cc:plmnCells) 
			{
				fileLogger.debug(cc.getEarfcn()+"-"+cc.getPci());
			}
			
			NeighbourEarfcn[] earfcnAndCountSelfCell= neighbourEarfcnMap.get("earfcnAndCountSelfCell");
			NeighbourEarfcn[] earfcnAndCountNeighbourCell=neighbourEarfcnMap.get("earfcnAndCountNeighbourCell");
			
			ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
			
			ArrayList<Cell> cells = new ArrayList<Cell>();		
			try 
			{
				//Cloning the cells of plmn it will secure and will not allow updating the reference inside the object
				for(Cell cell:plmnCells) 
				{
					cells.add((Cell)cell.clone());
				}
				
				String[] cellCoverStatus=new String[cells.size()];
				for(int k=0;k<cells.size();k++){
					cellCoverStatus[k]="n";
				}
				
				for(NeighbourEarfcn na:earfcnAndCountNeighbourCell){
					boolean naMatch=false;
					for(int j=0;j<cells.size();j++){
						
						Cell cell=cells.get(j);
						//if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
							if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0)){
							JSONArray nbrEarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							for(int i=0;i<nbrEarfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								//if((na.getPsc()==nbrUarfcn.getJSONObject(i).getInt("PSC") || na.getPsc()==cell.getPsc()) && cellCoverStatus[j]=="n")
								if(na.getEarfcn() == cell.getEarfcn() && (na.getPci()==nbrEarfcn.getJSONObject(i).getInt("PCI")) && cellCoverStatus[j]=="n")
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPci(na.getPci());
									cell1.setBand(calculateBand("lte",na.getEarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}
								}
							}
	/*						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn_inter.length();i++){
								if(((na.getUarfcn()==nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc()==nbrUarfcn_inter.getJSONObject(i).getInt("PSC"))||(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc())) && cellCoverStatus[j]=="n"){
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									cell1.setUarfcn(na.getUarfcn());	
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}	
								}

							}*/
						}
					}
				}
				
				ArrayList<NeighbourEarfcn> earfcnPCIcount = new ArrayList<NeighbourEarfcn>();
				
				for(Integer i :totalEarfcn)
				{
					for(NeighbourEarfcn na:earfcnAndCountNeighbourCell)
					{
						if(na.getEarfcn()==i)
						{
							earfcnPCIcount.add(na);
							break;
						}
					}
				}
				
				/****check with earfcn distinct**/
				for(NeighbourEarfcn na :earfcnPCIcount){
					boolean flag=false;
				for(Cell cell:selfCellsWithNeighbours){
						if(na.getEarfcn()==cell.getEarfcn() && na.getPci()==cell.getPci()){
						 flag=true;
						 break;
						}
					}
				//commented to disabled dummy cell
/*				if(!flag){
					String mcc = (PLMN.substring(0,3));
					String mnc = (PLMN.substring(3));
					String oprName="NA";
					JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
					try {
						if(oprNameArray.length()>0){
							oprName=oprNameArray.getJSONObject(0).getString("opr");
						}
					} catch (JSONException e) {
						fileLogger.error("exception while getting oprname");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Cell cell_1 = new Cell(PLMN,mcc,mnc,null, "1", calculateBand("lte",na.getEarfcn()), 91, 8, "-1", "-1", new JSONObject(),-1,-1,"1234",na.getEarfcn(),na.getPci());
					cell_1.setOpr(oprName);
					selfCellsWithNeighbours.add(cell_1);
				}*/
				}
				
				for(NeighbourEarfcn na:earfcnAndCountSelfCell){
					boolean naMatch=false;
					for(int j=0;j<cells.size();j++){
						Cell cell=cells.get(j);
						//if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0 ||  cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH").length() >0)){
						if(cell.getNeighbour() != null && (cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0)){	
							JSONArray nbrEarfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							for(int i=0;i<nbrEarfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								if((na.getPci()==nbrEarfcn.getJSONObject(i).getInt("PCI") || na.getPci()==cell.getPci()) && cellCoverStatus[j]=="n") 
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPci(na.getPci());
									cell1.setBand(calculateBand("lte",na.getEarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}
								}
							}
	/*						JSONArray nbrUarfcn_inter = cell.getNeighbour().getJSONArray("INTER_FREQ_NEIGH");
							for(int i=0;i<nbrUarfcn_inter.length();i++){
								if(((na.getUarfcn()==nbrUarfcn_inter.getJSONObject(i).getInt("UARFCN") && na.getPsc()==nbrUarfcn_inter.getJSONObject(i).getInt("PSC"))||(na.getUarfcn()==cell.getUarfcn() && na.getPsc()==cell.getPsc())) && cellCoverStatus[j]=="n"){
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setPsc(na.getPsc());
									cell1.setUarfcn(na.getUarfcn());	
									cell1.setBand(calculateBand("umts",na.getUarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}	
								}

							}*/
						}else{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								if( na.getPci()==cell.getPci() && cellCoverStatus[j]=="n") 
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setEarfcn(na.getEarfcn());
									cell1.setPci(na.getPci());
									cell1.setBand(calculateBand("lte",na.getEarfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									}
								}

						}
					}
				}
				
				for(int cellCount=0;cellCount<cells.size();cellCount++){
					if(cellCoverStatus[cellCount].equals("n")){
						Cell cell1 = (Cell)cells.get(cellCount).clone();
						cell1.setBand(calculateBand("lte",cell1.getEarfcn()));
						selfCellsWithNeighbours.add(cell1);
					}
				}
				
			 ArrayList<NeighbourEarfcn> earfcnPCIcountSelf = new ArrayList<NeighbourEarfcn>();
				
				for(Integer i :totalEarfcn)
				{
					for(NeighbourEarfcn na:earfcnAndCountSelfCell)
					{
						if(na.getEarfcn()==i)
						{
							earfcnPCIcountSelf.add(na);
							break;
						}
					}
				}
				
				//****check with earfcn distinct**//*
				for(NeighbourEarfcn na :earfcnPCIcountSelf){
					boolean flag=false;
				for(Cell cell:selfCellsWithNeighbours){
						if(na.getEarfcn()==cell.getEarfcn()){
						 flag=true;
						 break;
						}
					}
				//commented 1234 tac
		/*		if(!flag){
					String mcc = (PLMN.substring(0,3));
					String mnc = (PLMN.substring(3));
					String oprName="NA";
					JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
					try {
						if(oprNameArray.length()>0){
							oprName=oprNameArray.getJSONObject(0).getString("opr");
						}
					} catch (JSONException e) {
						fileLogger.error("exception while getting oprname");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("lte",na.getEarfcn()), 91, 8, "-1", "-1", new JSONObject(),-1,-1,"1234",na.getEarfcn(),na.getPci());
					cell_1.setOpr(oprName);
					selfCellsWithNeighbours.add(cell_1);
				}*/
				}
				
				
				Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
				tempCells = selfCellsWithNeighbours.toArray(tempCells);
				Arrays.sort(tempCells, Cell.earfcnAscending);
				selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
				
				
			}
			catch(Exception E) 
			{
			///	fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration4G");
				E.printStackTrace();
			//.debug("******************************************************");
			}		
			fileLogger.info("Exit Function : getSelfCellsForConfiguration4G");
			return selfCellsWithNeighbours;
		}
		
		public ArrayList<Cell> getSelfCellsForConfiguration2GUpdated(ArrayList<Cell> plmnCells,HashMap<String,NeighbourArfcn[]> neighbourArfcnMap,String PLMN,ArrayList<Integer> totalArfcn)
		{

			fileLogger.info("Inside Function : getSelfCellsForConfiguration2G");
			//@sunil create cell clone
			NeighbourArfcn[] arfcnAndCountSelfCell= neighbourArfcnMap.get("arfcnAndCountSelfCell");
			NeighbourArfcn[] arfcnAndCountNeighbourCell=neighbourArfcnMap.get("arfcnAndCountNeighbourCell");
			String compare=",";
			ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
			
			ArrayList<Cell>selfCellsWithoutNeighbours = new ArrayList<Cell>();
			
			ArrayList<Cell> cells = new ArrayList<Cell>();	
			
			HashMap<Cell,String> cellMap=new HashMap<Cell,String>();
			


				Cell[] tempCells1= new Cell[plmnCells.size()];
				tempCells1 = plmnCells.toArray(tempCells1);
				//vaibha 28 dec 2020Arrays.sort(tempCells, Cell.rssiAscending);
				plmnCells = new ArrayList<>(Arrays.asList(tempCells1));
			
			try 
			{
				for(Cell cell:plmnCells) 
				{
					cells.add((Cell)cell.clone());
					//cellMap.put((Cell)cell.clone(),"n");
				}
				String[] cellCoverStatus=new String[cells.size()];
				for(int k=0;k<cells.size();k++){
					cellCoverStatus[k]="n";
				}
				
				
				//sirvaib bhai did here 29 Dec 1 AM
				for(int j=0;j<cells.size();j++){
					Cell cell=cells.get(j);
					cell.setBand(calculateBand("gsm",cell.getArfcn()));
					selfCellsWithNeighbours.add(cell);
				}
				
				for(NeighbourArfcn na:arfcnAndCountNeighbourCell){
					boolean naMatch=false;
					if(compare.contains(","+na.getArfcn()+",")) {
						continue;
					}
					for(int j=0;j<cells.size();j++){
						Cell cell=cells.get(j);
						if(cell.getNeighbour() != null && cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0) 
						{
							JSONArray nbrArfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							
							for(int i=0;i<nbrArfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								//if((na.getArfcn()==nbrArfcn.getJSONObject(i).getInt("ARFCN") || na.getArfcn()==cell.getArfcn()) && cellCoverStatus[j]=="n")
								if((na.getArfcn()==nbrArfcn.getJSONObject(i).getInt("ARFCN")) )//&& cellCoverStatus[j]=="n")
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									int rssi = getrssi(""+na.getArfcn(), "","gsm");
									if (rssi!=-999)
										cell1.setRssi(rssi);
									else 
										cell1.setRssi(999);
									cell1.setArfcn(na.getArfcn());
									cell1.setBand(calculateBand("gsm",na.getArfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									compare+=na.getArfcn()+",";
									
									break;
									}
								}
							}
							if(naMatch){
								
								break;
							}
						}
					}
				}
				
				
				
				ArrayList<NeighbourArfcn> arfcnCount = new ArrayList<NeighbourArfcn>();
				
				for(Integer i :totalArfcn)
				{
					for(NeighbourArfcn na:arfcnAndCountNeighbourCell)
					{
						if(na.getArfcn()==i)
						{
							arfcnCount.add(na);
							break;
						}
					}
				}
				
				
				for(NeighbourArfcn na :arfcnCount){
				boolean flag=false;
			for(Cell cell:selfCellsWithNeighbours){
					if(na.getArfcn()==cell.getArfcn()){
					 flag=true;
					 break;
					}
				}
			/*if(!flag){
				String mcc = (PLMN.substring(0,3));
				String mnc = (PLMN.substring(3));
				String oprName="NA";
				JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
				try {
					if(oprNameArray.length()>0){
						oprName=oprNameArray.getJSONObject(0).getString("opr");
					}
				} catch (JSONException e) {
					fileLogger.error("exception while getting oprname");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("gsm",na.getArfcn()),na.getArfcn(), 91, "-1", "-1", new JSONObject(),-1,-1,null,-1,-1);
				cell_1.setBsic("-1");
				cell_1.setOpr(oprName);
				selfCellsWithNeighbours.add(cell_1);
				break;
			}*/
			}
				
				
				//COmmented on 28Dec 2020 by Sanjay and Vaibhav 11:54 PM Not req below code
//				for(NeighbourArfcn na:arfcnAndCountSelfCell){
//					boolean naMatch=false;
//					for(int j=0;j<cells.size();j++){
//						Cell cell=cells.get(j);
//						
//						if(cell.getNeighbour() != null && cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0) 
//						{
//							JSONArray nbrArfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
//							
//							for(int i=0;i<nbrArfcn.length();i++) 
//							{
//								
//								//nbrArfcn.getJSONObject(i).get("ARFCN");
//								if((na.getArfcn()==nbrArfcn.getJSONObject(i).getInt("ARFCN") || na.getArfcn()==cell.getArfcn()) && cellCoverStatus[j]=="n") 
//								{
//									cellCoverStatus[j]="y";
//									if(!naMatch){
//									Cell cell1 = (Cell)cell.clone();
//									cell1.setArfcn(na.getArfcn());
//									cell1.setBand(calculateBand("gsm",na.getArfcn()));
//									selfCellsWithNeighbours.add(cell1);
//									naMatch=true;
//									break;
//									}
//								}
//							}
//							
//						}else{
//							if(na.getArfcn()==cell.getArfcn() && cellCoverStatus[j]=="n") 
//							{
//								cellCoverStatus[j]="y";
//								if(!naMatch){
//								Cell cell1 = (Cell)cell.clone();
//								cell1.setArfcn(na.getArfcn());
//								cell1.setBand(calculateBand("gsm",na.getArfcn()));
//								selfCellsWithNeighbours.add(cell1);
//								naMatch=true;
//								}
//							}
//							
//						}
//					}
//				}
				
//				for(int cellCount=0;cellCount<cells.size();cellCount++){
//					if(cellCoverStatus[cellCount].equals("n")){
//						Cell cell1 = (Cell)cells.get(cellCount).clone();
//						cell1.setBand(calculateBand("gsm",cell1.getArfcn()));
//						selfCellsWithNeighbours.add(cell1);
//					}
//				}
				
				ArrayList<NeighbourArfcn> arfcnCountSelf = new ArrayList<NeighbourArfcn>();
				
				for(Integer i :totalArfcn)
				{
					for(NeighbourArfcn na:arfcnAndCountSelfCell)
					{
						if(na.getArfcn()==i)
						{
							arfcnCountSelf.add(na);
							break;
						}
					}
				}
				
				/*
				//Commented because extra default cell data was coming  , which is not required
				
				for(NeighbourArfcn na :arfcnCountSelf){
				boolean flag=false;
			for(Cell cell:selfCellsWithNeighbours){
					if(na.getArfcn()==cell.getArfcn()){
					 flag=true;
					 break;
					}
				}
			if(!flag){
				String mcc = (PLMN.substring(0,3));
				String mnc = (PLMN.substring(3));
				String oprName="NA";
				JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
				try {
					if(oprNameArray.length()>0){
						oprName=oprNameArray.getJSONObject(0).getString("opr");
					}
				} catch (JSONException e) {
					fileLogger.debug("exception while getting oprname");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("gsm",na.getArfcn()),na.getArfcn(), 91, "-1", "-1", new JSONObject(),-1,-1,null,-1,-1);
				cell_1.setBsic("-1");
				cell_1.setOpr(oprName);
				selfCellsWithNeighbours.add(cell_1);
				break;
			}
			}*/
				
				Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
				tempCells = selfCellsWithNeighbours.toArray(tempCells);
				Arrays.sort(tempCells, Cell.rssiAscending);
				selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
				
			}
			catch(Exception E) 
			{
				//fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration2G");
				E.printStackTrace();
				//fileLogger.debug("******************************************************");
			}	
			fileLogger.info("Exit Function : getSelfCellsForConfiguration2G");
			return selfCellsWithNeighbours;
		
		}
		
		
		public ArrayList<Cell> getSelfCellsForConfiguration2G(ArrayList<Cell> plmnCells,HashMap<String,NeighbourArfcn[]> neighbourArfcnMap,String PLMN,ArrayList<Integer> totalArfcn)
		{
			fileLogger.info("Inside Function : getSelfCellsForConfiguration2G");
			//@sunil create cell clone
			NeighbourArfcn[] arfcnAndCountSelfCell= neighbourArfcnMap.get("arfcnAndCountSelfCell");
			NeighbourArfcn[] arfcnAndCountNeighbourCell=neighbourArfcnMap.get("arfcnAndCountNeighbourCell");
			
			ArrayList<Cell> selfCellsWithNeighbours = new ArrayList<Cell>();
			
			ArrayList<Cell>selfCellsWithoutNeighbours = new ArrayList<Cell>();
			
			ArrayList<Cell> cells = new ArrayList<Cell>();	
			
			HashMap<Cell,String> cellMap=new HashMap<Cell,String>();
			
			try 
			{
				for(Cell cell:plmnCells) 
				{
					cells.add((Cell)cell.clone());
					//cellMap.put((Cell)cell.clone(),"n");
				}
				String[] cellCoverStatus=new String[cells.size()];
				for(int k=0;k<cells.size();k++){
					cellCoverStatus[k]="n";
				}
				
				for(NeighbourArfcn na:arfcnAndCountNeighbourCell){
					boolean naMatch=false;
					for(int j=0;j<cells.size();j++){
						Cell cell=cells.get(j);
						if(cell.getNeighbour() != null && cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0) 
						{
							JSONArray nbrArfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							
							for(int i=0;i<nbrArfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								//if((na.getArfcn()==nbrArfcn.getJSONObject(i).getInt("ARFCN") || na.getArfcn()==cell.getArfcn()) && cellCoverStatus[j]=="n")
								if((na.getArfcn()==nbrArfcn.getJSONObject(i).getInt("ARFCN")) && cellCoverStatus[j]=="n")
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setArfcn(na.getArfcn());
									cell1.setBand(calculateBand("gsm",na.getArfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}
								}
							}
						}
					}
				}
				
				
				
				ArrayList<NeighbourArfcn> arfcnCount = new ArrayList<NeighbourArfcn>();
				
				for(Integer i :totalArfcn)
				{
					for(NeighbourArfcn na:arfcnAndCountNeighbourCell)
					{
						if(na.getArfcn()==i)
						{
							arfcnCount.add(na);
							break;
						}
					}
				}
				
				
				for(NeighbourArfcn na :arfcnCount){
				boolean flag=false;
			for(Cell cell:selfCellsWithNeighbours){
					if(na.getArfcn()==cell.getArfcn()){
					 flag=true;
					 break;
					}
				}
			/*if(!flag){
				String mcc = (PLMN.substring(0,3));
				String mnc = (PLMN.substring(3));
				String oprName="NA";
				JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
				try {
					if(oprNameArray.length()>0){
						oprName=oprNameArray.getJSONObject(0).getString("opr");
					}
				} catch (JSONException e) {
					fileLogger.error("exception while getting oprname");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("gsm",na.getArfcn()),na.getArfcn(), 91, "-1", "-1", new JSONObject(),-1,-1,null,-1,-1);
				cell_1.setBsic("-1");
				cell_1.setOpr(oprName);
				selfCellsWithNeighbours.add(cell_1);
				break;
			}*/
			}
				
				
				
				for(NeighbourArfcn na:arfcnAndCountSelfCell){
					boolean naMatch=false;
					for(int j=0;j<cells.size();j++){
						Cell cell=cells.get(j);
						
						if(cell.getNeighbour() != null && cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH").length() >0) 
						{
							JSONArray nbrArfcn = cell.getNeighbour().getJSONArray("INTRA_FREQ_NEIGH");
							
							for(int i=0;i<nbrArfcn.length();i++) 
							{
								
								//nbrArfcn.getJSONObject(i).get("ARFCN");
								if((na.getArfcn()==nbrArfcn.getJSONObject(i).getInt("ARFCN") || na.getArfcn()==cell.getArfcn()) && cellCoverStatus[j]=="n") 
								{
									cellCoverStatus[j]="y";
									if(!naMatch){
									Cell cell1 = (Cell)cell.clone();
									cell1.setArfcn(na.getArfcn());
									cell1.setBand(calculateBand("gsm",na.getArfcn()));
									selfCellsWithNeighbours.add(cell1);
									naMatch=true;
									break;
									}
								}
							}
							
						}else{
							if(na.getArfcn()==cell.getArfcn() && cellCoverStatus[j]=="n") 
							{
								cellCoverStatus[j]="y";
								if(!naMatch){
								Cell cell1 = (Cell)cell.clone();
								cell1.setArfcn(na.getArfcn());
								cell1.setBand(calculateBand("gsm",na.getArfcn()));
								selfCellsWithNeighbours.add(cell1);
								naMatch=true;
								}
							}
							
						}
					}
				}
				
				for(int cellCount=0;cellCount<cells.size();cellCount++){
					if(cellCoverStatus[cellCount].equals("n")){
						Cell cell1 = (Cell)cells.get(cellCount).clone();
						cell1.setBand(calculateBand("gsm",cell1.getArfcn()));
						selfCellsWithNeighbours.add(cell1);
					}
				}
				
				ArrayList<NeighbourArfcn> arfcnCountSelf = new ArrayList<NeighbourArfcn>();
				
				for(Integer i :totalArfcn)
				{
					for(NeighbourArfcn na:arfcnAndCountSelfCell)
					{
						if(na.getArfcn()==i)
						{
							arfcnCountSelf.add(na);
							break;
						}
					}
				}
				
				/*
				//Commented because extra default cell data was coming  , which is not required
				
				for(NeighbourArfcn na :arfcnCountSelf){
				boolean flag=false;
			for(Cell cell:selfCellsWithNeighbours){
					if(na.getArfcn()==cell.getArfcn()){
					 flag=true;
					 break;
					}
				}
			if(!flag){
				String mcc = (PLMN.substring(0,3));
				String mnc = (PLMN.substring(3));
				String oprName="NA";
				JSONArray oprNameArray=new Operations().getJson("select opr from plmn_opr where plmn="+PLMN+" limit 1");
				try {
					if(oprNameArray.length()>0){
						oprName=oprNameArray.getJSONObject(0).getString("opr");
					}
				} catch (JSONException e) {
					fileLogger.debug("exception while getting oprname");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Cell cell_1 = new Cell(PLMN,mcc,mnc, "1234", "1", calculateBand("gsm",na.getArfcn()),na.getArfcn(), 91, "-1", "-1", new JSONObject(),-1,-1,null,-1,-1);
				cell_1.setBsic("-1");
				cell_1.setOpr(oprName);
				selfCellsWithNeighbours.add(cell_1);
				break;
			}
			}*/
				
				Cell[] tempCells= new Cell[selfCellsWithNeighbours.size()];
				tempCells = selfCellsWithNeighbours.toArray(tempCells);
				Arrays.sort(tempCells, Cell.rssiAscending);
				selfCellsWithNeighbours = new ArrayList<>(Arrays.asList(tempCells));
			}
			catch(Exception E) 
			{
				//fileLogger.debug("******************************************************");
				fileLogger.debug("Class = PossibleConfigurations  Method = getSelfCellsForConfiguration2G");
				E.printStackTrace();
				//fileLogger.debug("******************************************************");
			}	
			fileLogger.info("Exit Function : getSelfCellsForConfiguration2G");
			return selfCellsWithNeighbours;
		}

		public int calculateFreePsc(int offset)
		{
			int minoffset=offset-20;
			int maxoffset = offset+20;
			String query="select distinct (psc) from oprlogs_current where psc::numeric between "+ minoffset+ " and  "+ maxoffset;
			JSONArray result2 = new Operations().getJson(query);
			int result3=-1;
			int  psc1 =-1, psc2=-1;
			boolean matched=false;
			JSONArray jsonArr=new Operations().getJson(query);
			ArrayList<Integer> pscarr=new ArrayList<Integer>();
			try {
				for (int j=offset-1; j >=minoffset;j--)
				{
					matched =false;
					for(int i=0;i<jsonArr.length();i++){
						JSONObject tempObj=jsonArr.getJSONObject(i);	
						result3= Integer.parseInt(tempObj.getString("psc"));
						if(j==result3)
						{
							matched =true;
							break;
						}
						
					}
					if(!matched) {
						psc1=j;
						break;
					}
					
				
				}
				
				
				for (int k=offset+1; k <=maxoffset;k++)
				{
					matched =false;
					for(int l=0;l<jsonArr.length();l++){
						JSONObject tempObj=jsonArr.getJSONObject(l);	
						result3= Integer.parseInt(tempObj.getString("psc"));
						if(k==result3)
						{
							matched =true;
							break;
						}
						
					}
					if(!matched) {
						psc2=k;
						break;
					}
					
				}
				
				
				
				if( psc2-offset<=offset-psc1)
					return psc2;
				else
					return psc1;
				
				
				
			}
				catch(Exception ex)
				{
					fileLogger.debug(ex.toString());
				}
			
			return -1;
		}
		
	}


