package com.server.xmpp;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
	
	
	
}
