package com.server.manager;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

public class DatabaseConnector {

	
	
	
	public static Connection getConnection(String URL,String username,String password) throws ClassNotFoundException{
		
		Connection conn=null;
		
		
		Class.forName("com.mysql.jdbc.Driver");
		
		
		StringBuilder ConnectionURL= new StringBuilder();
		
		ConnectionURL.append("jdbc:mysql://")
		.append(URL)
		.append("/?user=")
		.append(username)
		.append("&password=")
		.append(password);
		
		
		try {
			conn=DriverManager.getConnection(ConnectionURL.toString());
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		if(conn!=null)
		    return conn;
		else
			return null;
		
	}
		
	
public static void registerInDatabase(String phone_number,String gcm_reg_id,HttpServletResponse response) throws ClassNotFoundException{
		
		Connection conn= getConnection("localhost:3306", "root", "abc");
		
		Statement cmd=null;
		try{
		cmd= conn.createStatement();
		}
		catch(SQLException e){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
			try{
				cmd.executeUpdate("insert into hatch.users values('"+phone_number+"','"+gcm_reg_id+"',0,'default_user_name',0)");
				cmd.close();
				conn.close();
			}
			catch(SQLException e){
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				e.printStackTrace();
			}
		
	}
	
public static void addToContacts(String phone_number,String gcm_reg_id,Connection conn) throws SQLException {
	
	Statement cmd = conn.createStatement();
	
	cmd.execute("insert into hatch.contacts values('"+phone_number+"','"+gcm_reg_id+"',0);");
	
	cmd.close();	
	
}


public static void registerUsername(String user_name,String phone_number,HttpServletResponse response) throws ClassNotFoundException{
	
	Connection conn= getConnection("localhost:3306", "root", "abc");
	
	
	try {
		Statement cmd = conn.createStatement();
		
		ResultSet rs = cmd.executeQuery("select user_name from hatch.users where user_name='"+user_name+"';");
		boolean isPresent = rs.next();
		
		if(isPresent){
			Logger logger = Logger.getLogger("DUPLICATION");
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			logger.info("Duplicate User Name! Response code 1024");
		}
		else{
			
			cmd.executeUpdate("update hatch.users set user_name='"+user_name+"' where phone_number='"+phone_number+"';");
			response.setStatus(HttpServletResponse.SC_OK);		
		}
				
	} catch (SQLException e) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		e.printStackTrace();
	}
	
}




}
