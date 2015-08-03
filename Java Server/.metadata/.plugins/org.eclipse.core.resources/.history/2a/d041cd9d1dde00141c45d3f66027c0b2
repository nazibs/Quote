package com.server.xmpp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.server.xmpp.Constants;
import com.server.xmpp.Message;




public class HTTPInterface {

	
	private static final Executor threadPool = Executors.newFixedThreadPool(5);	
	private static final String GOOGLE_SERVER_KEY = "AIzaSyB8XuNWeEgnluE9MQ5ezffPftBckJA_1iY";
	public static Sender sender;
	public static Logger logger1;
	
	
	//editing 
	
	public static void sendViaHttp(String name,String number,String level,String messageID,String time_to_hatch,String message,String message_type) throws ClassNotFoundException, SQLException
	{
		
		
		logger1= Logger.getLogger("HTTP Interface");
		
		logger1.info("name: "+name);
		logger1.info("number: "+number);
		logger1.info("level: "+level);
		logger1.info("messageid: "+messageID);
		logger1.info("time to hatch: "+time_to_hatch);
		logger1.info("message content: "+message);
		logger1.info("type: "+message_type);
		
		
		 ArrayList<String> devices = getRegIDs(number);
		 sender = new Sender(GOOGLE_SERVER_KEY);
		 logger1.info("Trying to send to  "+devices.size()+" devices");
		 
		 send(devices,name,number,level,messageID,message);	
		
	}
	
	public static void broadcastViaHttp(String name,String number,String level,String messageID,String time_to_hatch,String message,String message_type,String broadcast_number) throws ClassNotFoundException, SQLException
	{
		logger1= Logger.getLogger("Broadcast HTTP Interface");
		
		logger1.info("name: "+name);
		logger1.info("number: "+number);
		logger1.info("level: "+level);
		logger1.info("messageid: "+messageID);
		logger1.info("time to hatch: "+time_to_hatch);
		logger1.info("message content: "+message);
		logger1.info("type: "+message_type);
		logger1.info("broadcast_number: "+broadcast_number);
		
		ArrayList<String> devices = getRegIDs(broadcast_number);
		 sender = new Sender(GOOGLE_SERVER_KEY);
		 logger1.info("Trying to send to  "+devices.size()+" devices");
		 
		 send(devices,name,number,level,messageID,message);	
		
	}
	
	
	public static String get_broadcast_phone_number(String reg_id) throws ClassNotFoundException, SQLException
	{
		Connection conn= DatabaseConnector.getConnection("localhost:3306", "root", "abc");
		
		Logger logg1= Logger.getLogger("addNew");
		
		logg1.info("Getting broadcast_number for : "+reg_id);
		
		Statement cmd= conn.createStatement();
		
        ResultSet rs=cmd.executeQuery("select phone_number from hatch.users where gcm_reg_id='"+reg_id+"';");   
    	
        logg1.info("Returning broadcast_number : ");
        rs.next();
    	logg1.info(rs.getString(1));    
		return rs.getString(1);
	}
	
	public static void bookmark(String message_id) throws ClassNotFoundException, SQLException{
		
		Connection conn= DatabaseConnector.getConnection("localhost:3306", "root", "abc");
		
		Logger logg1= Logger.getLogger("addNew");
		logg1.info("Broadcasting message, message_id="+message_id);

        Statement cmd= conn.createStatement();
        
        cmd.executeUpdate("update hatch.messages set time_to_hatch= time_to_hatch-10 where message_id='"+message_id+"';");      
		
	}
	

	public static void addNew(String message_id,String sender_gcm_reg_id) throws ClassNotFoundException, SQLException{
		
		Connection conn= DatabaseConnector.getConnection("localhost:3306", "root", "abc");
		
		Logger logg= Logger.getLogger("addNew");
		
		logg.info("Adding message, message_id="+message_id);

        Statement cmd= conn.createStatement();
        
        cmd.executeUpdate("insert into hatch.messages values('"+sender_gcm_reg_id+"','"+message_id+"',200);");
		
	}
	
	
public static void addBroadcast(String message_id) throws ClassNotFoundException, SQLException{
		
		Connection conn= DatabaseConnector.getConnection("localhost:3306", "root", "abc");
		
		Logger logg2= Logger.getLogger("addNew");
		logg2.info("Message Bookmarked, message_id="+message_id);

        Statement cmd= conn.createStatement();
        
        cmd.executeUpdate("update hatch.messages set time_to_hatch= time_to_hatch-2 where message_id='"+message_id+"';");
        
	}
	
	
	
	public static ArrayList<String> getRegIDs(String num) throws ClassNotFoundException, SQLException{
		
		ArrayList<String> reg= new ArrayList<String>();
		
		Connection conn= DatabaseConnector.getConnection("localhost:3306", "root", "abc");
		
		
        Statement cmd= conn.createStatement();
		
		ResultSet rs= cmd.executeQuery("select contact_gcm_reg_id from hatch.contacts where phone_number="+num+";");
		
		
		
        while(rs.next()){
			
			String number= rs.getString(1);
			
			reg.add(number);
		}
		
        
        rs.close();
        cmd.close();
        conn.close();
        
        return reg;
        
        
	}
	
	

	
	
	
	public static void send(ArrayList<String> devices,String name,String number,String level,String messageID,String message)
	{
		
		
		 int total = devices.size();
	        List<String> partialDevices = new ArrayList<String>(total);
	        int counter = 0;
	        int tasks = 0;
	        for (String device : devices) {
	          counter++;
	          partialDevices.add(device);
	          int partialSize = partialDevices.size();
	          if (partialSize == 1000 || counter == total)
	          {
	        	  logger1.info("Async send to  "+partialDevices.size()+" devices");
	          //editing
	        	  asyncSend(partialDevices,name,number,level,messageID,message);
	            partialDevices.clear();
	            tasks++;
	          }
	        }
	
	        
	        
	}	
		
	
	
public static void asyncSend(List<String> partialDevices,final String name,final String number,final String level,final String messageID,final String message_to_send)
{
		
	
	final Logger logger = Logger.getLogger("Receiver");
	 final List<String> devices = new ArrayList<String>(partialDevices);
	    threadPool.execute(new Runnable() {

	      public void run()
	      {
	       
	    	  Message message = new Message.Builder().addData("sender_name",name).addData("sender_number",number).addData("sender_level",level).addData("message_id",messageID).addData("message",message_to_send).build();
	       
	        
	        MulticastResult multicastResult = null;
	        try 
	        {
	        	logger1.info("About to send to  "+devices.size()+" devices");
	          multicastResult = sender.send(message, devices, 5);
	        }
	        catch (IOException e) {
	          logger.log(Level.SEVERE, "Error posting messages", e);
	          return;
	        } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        List<Result> results = multicastResult.getResults();
	        // analyze the results
	        for (int i = 0; i < devices.size(); i++)
	        {
	          String regId = devices.get(i);
	          Result result = results.get(i);
	          String messageId = result.getMessageId();
	          
	          if (messageId != null) {
	            logger1.info("Succesfully sent message to device: " + regId +
	                "; messageId = " + messageId);
	            String canonicalRegId = result.getCanonicalRegistrationId();
	            if (canonicalRegId != null) {
	              // same device has more than on registration id: update it
	              logger1.info("canonicalRegId " + canonicalRegId);
	             // Datastore.updateRegistration(regId, canonicalRegId);
	            }
	          } else {
	            String error = result.getErrorCodeName();
	            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
	              // application has been removed from device - unregister it
	              logger1.info("Unregistered device: " + regId);
	             // Datastore.unregister(regId);
	            } else {
	              logger1.severe("Error sending message to " + regId + ": " + error);
	            }
	          }
	        }
	      }});
	  }
	
	
	
	
	
	
	
	
		
	}
	
	
	
	
	
	
