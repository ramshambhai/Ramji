package in.vnl.sockets;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.vnl.api.common.ApiCommon;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;

public class UdpServerClient implements ServletContextListener
{
	static Logger fileLogger = Logger.getLogger("file");
	static boolean stopThread=false;
	public void startServer (int port){

			
			//createPaUdpServer cc = new createPaUdpServer(port);
			createBMSUdpServer bms=new createBMSUdpServer(port);
			//Thread t = new Thread(cc);
			Thread t2 = new Thread(bms);
			//t.start();
			t2.start();
		
	}
	
	
	public String send(String ip,int port,String msg,int numberOfRetries) 
	{
		fileLogger.info("Inside Function :send" );
		String response = null;
		fileLogger.debug("@send mesg is :"+msg);
		for(int i=0;i<numberOfRetries;i++) 
		{
			response = send(ip,port,msg);
			
			if(response != null) 
			{
				
				if(response.equalsIgnoreCase("config")) 
				{
					
					JSONArray aa = new Operations().getJson("select config from btsmaster where ip='"+ip+"'");
					try 
					{
						response = send(ip,port,(new JSONObject(aa.getJSONObject(0).getString("config"))).getString("controller_conf"));
						
						if(response != null && response.equalsIgnoreCase("success")) 
						{
							send(ip,port,msg,numberOfRetries);
						}
						
					}
					catch(Exception e) 
					{
						fileLogger.debug(e.getMessage());
					}
					
				} 
				
				
				break;
			}
		}
		fileLogger.info("Exit Function :send" );
		return response;
	}
	
	public String send(String ip,int port,String msg) 
	{
		fileLogger.info("Inside Function :send" );
		
		String response = null;
		DatagramSocket clientSocket = null;
		try 
	     {
		
			clientSocket = new DatagramSocket(8870);
			 fileLogger.debug("@sunil : udp :1");
			 fileLogger.debug(ip);
			 String[] ipStr=ip.split("\\.");
			 byte[] bb = new byte[4]; 
			 bb[0]=(byte)Integer.parseInt(ipStr[0]);
			 bb[1]=(byte)Integer.parseInt(ipStr[1]);
			 bb[2]=(byte)Integer.parseInt(ipStr[2]);
			 bb[3]=(byte)Integer.parseInt(ipStr[3]);
			  
			  
			  InetAddress IPAddress = InetAddress.getByAddress(bb);
			  
			  //old method
			  //byte[] sendData = new byte[1024];
			  //sendData = msg.getBytes();
			  
			  byte[] receiveData = new byte[1024];
			  
			  
			  fileLogger.debug("@sunil -- "+msg);
			  
			  byte[] sendData = new BigInteger(msg, 2).toByteArray();
			  
			  
			  
			  
			  
			  fileLogger.debug("@sunil : udp :2");
			  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			  
			  fileLogger.debug("@sunil : udp :3");
			  clientSocket.send(sendPacket);
			  clientSocket.setSoTimeout(15000);
			  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			  fileLogger.debug("@sunil : udp :3");
			  
			  clientSocket.receive(receivePacket);
			  
			  
			  StringBuilder sb = new StringBuilder();
			  
			  fileLogger.debug("@udp response recived : "+receivePacket.getData()[0]+"-"+receivePacket.getData()[1]);
			  
			  
			  
			  
			  /*for(byte bbb:receivePacket.getData())
					  {
				  		sb.append(Byte.toString(bbb ));
					  }
			  fileLogger.debug(sb.toString());
			  fileLogger.debug("@sunil : udp 5");
			  String modifiedSentence = new String(receivePacket.getData());
			  
			  int aa = (int)new BigInteger(receivePacket.getData()).intValue();
					  
			  fileLogger.debug("FROM SERVER:" + aa);
			  response = modifiedSentence;*/
	     //	int Resp= receivePacket.getData()[0];
	     	String ResponseHex=Integer.toHexString( receivePacket.getData()[0]);
	     	String Resp=ResponseHex.substring(ResponseHex.length()-1);
	     	String cmdId="0";
	     	if(ResponseHex.length()>1) {
	     		 cmdId=ResponseHex.substring(0,1);
	     	}
	     	
	     	
			//  response = ((byte)1 == receivePacket.getData()[1])?"success":"fail";
	     	response = (Resp.equalsIgnoreCase("1"))?"success":"fail";
	     	int i1 =-1; 
			  if(response.equalsIgnoreCase("fail")) 
			  {
				  response = ((byte)2 == receivePacket.getData()[1])?"config":"fail";
			  }
			  if(cmdId.equalsIgnoreCase("6")&&response.equalsIgnoreCase("success"))
			  {
				  for(byte bbb:receivePacket.getData())
				  {
			  		sb.append(Byte.toUnsignedInt(bbb ));
			  		i1 =bbb;
			  		//response=response+","+Byte.toUnsignedInt(bbb );
			  		response=response+","+i1;;
				  }
				  
				  
			  }
			
			  fileLogger.debug("@sunil : udp :6");
		} 
	     catch (SocketException e) 
	     {
	    	 fileLogger.debug("@sunil : udp :7");
	    	 e.printStackTrace();
	    	 //response = "fail";
	     } 
	     catch (UnknownHostException e) 
	     {
	    	 fileLogger.debug("@sunil : udp :8");
			e.printStackTrace();
			//response = "fail";
	     } 
	     catch (IOException e) 
	     {
	    	 fileLogger.debug("@sunil : udp :9");
	    	 e.printStackTrace();
	    	 //response = "fail";
	     }
		catch(Exception e) 
		{
			fileLogger.debug("@sunil : udp :10");
			e.getMessage();
			e.printStackTrace();
		}
		finally 
		{
			clientSocket.close();
		}
		fileLogger.info("Exit Function :send" );
		return response;
	}
	
	public static String sendBMS(String ip,int port,String data) 
	{
		String response = null;
		DatagramSocket clientSocket = null;
		fileLogger.debug("inside sendBMS");
		if(data=="")
		{
			return response;
		}
		fileLogger.debug("  ip= "+ip+"  port= "+port+"  data= "+data);
		
			
			try 
		    {
				clientSocket = new DatagramSocket();
				
				
				InetAddress IPAddress = InetAddress.getByName(ip);
				fileLogger.debug("  IPAddress= "+IPAddress);		
				byte[] receiveData = new byte[1024];
				
				//byte[] sendData = new BigInteger(cmd_btn, 2).toByteArray();
				byte[] sendData = data.getBytes();
				fileLogger.debug("  sendData= "+sendData);
				fileLogger.debug("  sendData.length= "+sendData.length+"  IPAddress= "+IPAddress+"  port= "+port);
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,port);
				 System.out.println("UDP DATA FROM BMS "+sendData);
				 fileLogger.debug("  sendPacket= "+sendPacket);
				 clientSocket.send(sendPacket);
				 clientSocket.setSoTimeout(5000);
				
				byte[] buf = new byte[1024];
				
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				clientSocket.receive(packet);
				response = new String(packet.getData(), 0, packet.getLength());
				
				
				
				//clientSocket.setSoTimeout(15000);
				
				/*DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				 
				clientSocket.receive(receivePacket);
				response = new String(receivePacket.getData());*/
				  
				System.out.println("@udp response recived : "+response);
				fileLogger.debug("  response= "+response);
				//dbserv.setJMData(response);
			}
		    catch(SocketException e) 
		    {
		    	e.printStackTrace();
		    	//response = "fail";
		    }
		    catch(UnknownHostException e) 
		    {
		    	e.printStackTrace();
				//response = "fail";
		    } 
		    catch(IOException e) 
		    {
		    	e.printStackTrace();
		    	 //response = "fail";
		    }
			catch(Exception e) 
			{ 
				e.getMessage();
				e.printStackTrace();
			}
			finally 
			{
				clientSocket.close();
			}
			return response;
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		UdpServerClient.stopThread=true;
		
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		String port = new Common().getDbCredential().get("udpserverport");
		fileLogger.debug("Starting UDP Server at :-"+port);
		startServer(Integer.parseInt(port));
		//createBMSUdpServer bms=new createBMSUdpServer(Integer.parseInt(port));
		//Thread t = new Thread(cc);
		// t2 = new Thread(bms);
		//t.start();
		//t2.start();
		
	}
	
}


class createPaUdpServer implements Runnable
{
	static Logger fileLogger = Logger.getLogger("file");
	int port = 90;
	
	
	
	createPaUdpServer(int port)
	{
		this.port = port;
	}
	
	public void run() 
	{
		try 
        {
			
			int port = 90;
			DatagramSocket dsocket = new DatagramSocket(this.port);
			 byte[] buffer = new byte[2048];
			 DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			 
			 while(true) 
			 {
				 	dsocket.receive(packet);
				 	String msg = new String(buffer, 0, packet.getLength());
				 	fileLogger.debug(packet.getAddress().getHostName() + ": "+ msg);
				 	
				 	/*packet.setLength(buffer.length);
				 
				    InetAddress inetAddress = InetAddress.getByName("239.255.42.99");
				    int PORT = 2225;
				    int BUFFER_SIZE = 10;
				    DatagramSocket datagramSocket = new DatagramSocket();
				    byte[] buf = new byte[BUFFER_SIZE];
				    String message = "0123456789";
				    buf = message.getBytes();
				    DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, inetAddress, PORT);

				    datagramSocket.send(datagramPacket);
				    datagramSocket.setSoTimeout(1010);*/
				 	
				 	
				 	
				 	InetAddress IPAddress = packet.getAddress();
	                int port1 = packet.getPort();
	                
	                //call the config
	                //String capitalizedSentence = sentence.toUpperCase();
	                String capitalizedSentence = getConfigPacket(IPAddress.toString());
	                
	                byte[] sendData = new byte[1024];
	                sendData = capitalizedSentence.getBytes();
	                
	                DatagramPacket sendPacket =
	                new DatagramPacket(sendData, sendData.length, IPAddress, port1);
	                dsocket.send(sendPacket);

			 }

        }
        catch(Exception e) 
        {
        	fileLogger.debug(e.getMessage());
        }
	}
	
	public String getConfigPacket(String ip) 
	{
		
		String config = null;
		try {
			JSONArray aa = new ApiCommon().getDeviceDetails(ip);
			
			JSONObject bb = aa.getJSONObject(0);
			
			config = bb.getString(config);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return config;
	} 
	
    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
	
}

class createBMSUdpServer implements Runnable
{
	static Logger fileLogger = Logger.getLogger("file");
	int port = 90;
	
	
	
	createBMSUdpServer(int port)
	{
		this.port = port;
	}
	
	public void run() 
	{
		try 
        {
			
			int port = 90;
			DatagramSocket dsocket = new DatagramSocket(this.port);
			 byte[] buffer = new byte[2048];
			 DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			 
			 while(!UdpServerClient.stopThread) 
			 {
				 try{
					 	dsocket.receive(packet);
						String response = new String(packet.getData(), 0, packet.getLength());
					 	//String msg = new String(buffer, 0, packet.getLength());
					 	fileLogger.debug(packet.getAddress().getHostName() + ": "+ response);
						//clientSocket.receive(packet);
						//response = new String(packet.getData(), 0, packet.getLength());
					 	InetAddress IPAddress = packet.getAddress();
		                String capitalizedSentence = getConfigPacket(IPAddress.toString());
		                String[] arrayIP=IPAddress.toString().split("/");
		                if(arrayIP.length>1){
		                	storeIntoDb(response,arrayIP[1]);
		                	
		                }
		                else{
		                	storeIntoDb(response,IPAddress.toString());
		                }
				 }
				 catch(Exception e) 
		        {
		        	fileLogger.debug(e.getMessage());
		        }
			 }

        }
        catch(Exception e) 
        {
        	fileLogger.debug(e.getMessage());
        }
	}
	
	public void storeIntoDb(String response,String ip){
		try{
			
			String[] resultData = response.replace("*", "").split(",");
			
			String query="insert into    bmsstatus(ip ,generationtime, bcv1 ,bcv2 , bcv3 , bcv4 ,bcv5 ,bcv6 ,bcv7 ,bcv8 ,bcv9 ,bcv10 ,bcv11 , bcv12 ,bcv13 ,bcv14 ,btv ,tbc ,soc ,btemp ,alarmword ) values('"+ip+"'";
			for(int i=2;i<22;i++){
				query+=","+resultData[i];
			}
			query+=");";
			Common co = new Common();
			co.executeDLOperation(query);
		}
		catch(Exception e){
			
			fileLogger.debug(e.getMessage());
		}
	}
	
	public String getConfigPacket(String ip) 
	{
		
		String config = null;
		try {
			JSONArray aa = new ApiCommon().getDeviceDetails(ip);
			
			JSONObject bb = aa.getJSONObject(0);
			
			config = bb.getString(config);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return config;
	} 
	
    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
	
}