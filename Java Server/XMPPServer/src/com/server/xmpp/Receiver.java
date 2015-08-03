package com.server.xmpp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.json.simple.JSONValue;
import org.xmlpull.v1.XmlPullParser;







public class Receiver
{
	
	Logger logger = Logger.getLogger("Receiver");
	
	
	public static final String GCM_SERVER = "gcm.googleapis.com";
	public static final int GCM_PORT = 5235;

	public static final String GCM_ELEMENT_NAME = "gcm";
	public static final String GCM_NAMESPACE = "google:mobile:data";

	XMPPConnection connection;
	ConnectionConfiguration config;
	
	
	
	
class GcmPacketExtension extends DefaultPacketExtension {

		String json;

		public GcmPacketExtension(String json) {
			super(GCM_ELEMENT_NAME, GCM_NAMESPACE);
			this.json = json;
		}

		public String getJson() {
			return json;
		}

		@Override
		public String toXML() {
			return String.format("<%s xmlns=\"%s\">%s</%s>", GCM_ELEMENT_NAME,
					GCM_NAMESPACE, json, GCM_ELEMENT_NAME);
		}

		@SuppressWarnings("unused")
		public Packet toPacket() {
			return new Message() {
				// Must override toXML() because it includes a <body>
				@Override
				public String toXML() {

					StringBuilder buf = new StringBuilder();
					buf.append("<message");
					if (getXmlns() != null) {
						buf.append(" xmlns=\"").append(getXmlns()).append("\"");
					}
					if (getLanguage() != null) {
						buf.append(" xml:lang=\"").append(getLanguage())
								.append("\"");
					}
					if (getPacketID() != null) {
						buf.append(" id=\"").append(getPacketID()).append("\"");
					}
					if (getTo() != null) {
						buf.append(" to=\"")
								.append(StringUtils.escapeForXML(getTo()))
								.append("\"");
					}
					if (getFrom() != null) {
						buf.append(" from=\"")
								.append(StringUtils.escapeForXML(getFrom()))
								.append("\"");
					}
					buf.append(">");
					buf.append(GcmPacketExtension.this.toXML());
					buf.append("</message>");
					return buf.toString();
				}
			};
		}
	}
	
	
	
	
	
	
public Receiver() {
	
	ProviderManager.getInstance().addExtensionProvider(GCM_ELEMENT_NAME,
			GCM_NAMESPACE, new PacketExtensionProvider() {

				public PacketExtension parseExtension(XmlPullParser parser)
						throws Exception {
					String json = parser.nextText();
					GcmPacketExtension packet = new GcmPacketExtension(json);
					return packet;
				}
			});
}

	
	
	
public void handleIncomingDataMessage(Map<String, Object> jsonObject) {
	
	logger.info("JSONOBJECT :"+jsonObject.toString());
	String from = jsonObject.get("from").toString();
	logger.info("From : "+from);
	
	// PackageName of the application that sent this message.
	
	String category = jsonObject.get("category").toString();			
    String message_id = jsonObject.get("message_id").toString();
    logger.info("MEssage_ID :"+message_id);
    // Use the packageName as the collapseKey in the echo packet
	
    String collapseKey = "echo:CollapseKey";
	@SuppressWarnings("unchecked")
	Map<String, String> payload = (Map<String, String>) jsonObject
			.get("data");

	//editing
	
	String name=payload.get("sender_name");																
	String number=payload.get("sender_phone_number");	
	String level=payload.get("sender_level");	
	String messageID=payload.get("message_id");
	String time_to_hatch=payload.get("time_to_hatch");
	String message_content=payload.get("message_content");
	String message_type=payload.get("message_type");
	
	
	logger.info("Received a message..");
	
	String ack = createJsonAck(from,message_id);
	
	send(ack);
	
	
	if(message_type.equals("bookmark"))
	{
		try 
		{
			HTTPInterface.bookmark(messageID);
		}
		catch (ClassNotFoundException e)
		{	
			e.printStackTrace();
		}
		catch (SQLException e)
		{	
			e.printStackTrace();
		}
	}
	else
	{
		try 
		    {
			logger.info("Trying to send via HTTP..");
				
			logger.info("messageID is:"+messageID);
			
			if(message_type.equals("new"))
			{
				HTTPInterface.addNew(messageID,from);
				HTTPInterface.sendViaHttp(name,number,level,messageID,time_to_hatch,message_content,message_type);
			}
			else 
			{
				logger.info("value of from : "+from);
				String broadcast_number = HTTPInterface.get_broadcast_phone_number(from);
				logger.info("value of broadcast_number : "+broadcast_number);
				HTTPInterface.addBroadcast(messageID);	
				HTTPInterface.broadcastViaHttp(name,number,level,messageID,time_to_hatch,message_content,message_type, broadcast_number);
			}
					//editing
		
			logger.info("HTTP Successfull!");
			}  
	    catch (ClassNotFoundException e) 
	        {
		
	    	logger.log(Level.INFO,"Error sending Via http interface");
	    	e.printStackTrace();
	        }
	    catch (SQLException e) 
	        {
		
	    	logger.log(Level.INFO,"Error sending Via http interface");
	    	e.printStackTrace();
	        }
	
	}
}	




public void handleAckReceipt(Map<String, Object> jsonObject) {
	String messageId = jsonObject.get("message_id").toString();
	String from = jsonObject.get("from").toString();
	logger.log(Level.INFO, "handleAckReceipt() from: " + from
			+ ", messageId: " + messageId);
}



public void handleNackReceipt(Map<String, Object> jsonObject) {
	String messageId = jsonObject.get("message_id").toString();
	String from = jsonObject.get("from").toString();
	logger.log(Level.INFO, "handleNackReceipt() from: " + from
			+ ", messageId: " + messageId);
}



public static String createJsonMessage(String to, String messageId,
		Map<String, String> payload, String collapseKey, Long timeToLive,
		Boolean delayWhileIdle) {
	Map<String, Object> message = new HashMap<String, Object>();
	message.put("to", to);
	if (collapseKey != null) {
		message.put("collapse_key", collapseKey);
	}
	if (timeToLive != null) {
		message.put("time_to_live", timeToLive);
	}
	if (delayWhileIdle != null && delayWhileIdle) {
		message.put("delay_while_idle", true);
	}
	message.put("message_id", messageId);
	message.put("data", payload);
	return JSONValue.toJSONString(message);
}
	
	



public static String createJsonAck(String to, String messageId) {
	Map<String, Object> message = new HashMap<String, Object>();
	message.put("message_type", "ack");
	message.put("to", to);
	message.put("message_id", messageId);
	return JSONValue.toJSONString(message);
}





public void connect(String username, String password) throws XMPPException {
	config = new ConnectionConfiguration(GCM_SERVER, GCM_PORT);
	config.setSecurityMode(SecurityMode.enabled);
	config.setReconnectionAllowed(true);
	config.setRosterLoadedAtLogin(false);
	config.setSendPresence(false);
	config.setSocketFactory(SSLSocketFactory.getDefault());

	// NOTE: Set to true to launch a window with information about packets
	// sent and received
	config.setDebuggerEnabled(true);

	// -Dsmack.debugEnabled=true
	XMPPConnection.DEBUG_ENABLED = true;

	connection = new XMPPConnection(config);
	connection.connect();

	connection.addConnectionListener(new ConnectionListener() {

		@Override
		public void reconnectionSuccessful() {
			logger.info("Reconnecting..");
		}

		@Override
		public void reconnectionFailed(Exception e) {
			logger.log(Level.INFO, "Reconnection failed.. ", e);
		}

		@Override
		public void reconnectingIn(int seconds) {
			logger.log(Level.INFO, "Reconnecting in %d secs", seconds);
		}

		@Override
		public void connectionClosedOnError(Exception e) {
			logger.log(Level.INFO, "Connection closed on error.");
		}

		@Override
		public void connectionClosed() {
			logger.info("Connection closed.");
		}
	});

	// Handle incoming packets
	connection.addPacketListener(new PacketListener() {

		@Override
		public void processPacket(Packet packet) {
			logger.log(Level.INFO, "Received: " + packet.toXML());
			Message incomingMessage = (Message) packet;
			GcmPacketExtension gcmPacket = (GcmPacketExtension) incomingMessage
					.getExtension(GCM_NAMESPACE);
			String json = gcmPacket.getJson();
			try {
				@SuppressWarnings("unchecked")
				Map<String, Object> jsonObject = (Map<String, Object>) JSONValue
						.parseWithException(json);

				// present for "ack"/"nack", null otherwise
				Object messageType = jsonObject.get("message_type");

				if (messageType == null) {
					// Normal upstream data message
					handleIncomingDataMessage(jsonObject);

					// Send ACK to CCS
					String messageId = jsonObject.get("message_id")
							.toString();
					String from = jsonObject.get("from").toString();
					String ack = createJsonAck(from, messageId);
					//send(ack);
				} else if ("ack".equals(messageType.toString())) {
					// Process Ack
					handleAckReceipt(jsonObject);
				} else if ("nack".equals(messageType.toString())) {
					// Process Nack
					handleNackReceipt(jsonObject);
				} else {
					logger.log(Level.WARNING,
							"Unrecognized message type (%s)",
							messageType.toString());
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Couldn't send echo.", e);
			}
		}
	}, new PacketTypeFilter(Message.class));

	// Log all outgoing packets
	connection.addPacketInterceptor(new PacketInterceptor() {
		@Override
		public void interceptPacket(Packet packet) {
			logger.log(Level.INFO, "Sent: {0}", packet.toXML());
		}
	}, new PacketTypeFilter(Message.class));

	connection.login(username, password);
}








	public void send(String jsonRequest) {
		Packet request = new GcmPacketExtension(jsonRequest).toPacket();
		connection.sendPacket(request);
	}

	
	
}
