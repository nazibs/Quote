package com.server.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.*;


@WebServlet("/ReturnContacts")
public class ReturnAvailableContacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public ReturnAvailableContacts() {
        super();
        
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		doPost(request, response);		
		
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String JSONArrayString= StreamManager.toStringFromRequestStream(request);
		
		JsonArray JSONArray=StreamManager.toJsonArrayFromJsonArrayString(JSONArrayString);
		
		
		String phone_number=request.getParameter("phone_number");
		
		try {
			JsonArray toReturn=returnAvailable(JSONArray,phone_number);
			
			String toSend= toReturn.toString();
			
			PrintWriter pr= response.getWriter();
			
			pr.println(toSend);
			
			pr.close();
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	
	
	
	public JsonArray returnAvailable(JsonArray json,String phone_number) throws ClassNotFoundException, SQLException{
		
		Connection conn=DatabaseConnector.getConnection("localhost:3306","root","abc");
		
		Statement cmd= conn.createStatement();
		
		ResultSet rs=null;
		
		JsonArrayBuilder builder= Json.createArrayBuilder();
		
		for(int i=0;i<json.size();i++){
			
			String number=json.getString(i);
			String gcm_reg_id;
			rs=cmd.executeQuery("select gcm_reg_id from hatch.users where phone_number="+number+";");
			
			boolean isPresent=rs.next();
			
			if(isPresent)
				{
				builder.add(number);
				gcm_reg_id=rs.getString(1);
				
				if(number.equals(phone_number)){
				
				Logger logger= Logger.getLogger("returnAvailable");
				
				logger.info("Equals phone_number:"+phone_number+"number:"+number);
					
				
				}
				else{
					DatabaseConnector.addToContacts(phone_number,gcm_reg_id,conn);
					Logger logger= Logger.getLogger("returnAvailable");
					
					logger.info("phone_number:"+phone_number+"number:"+number);
						
				}
				}
			
		}
		
		rs.close();
		cmd.close();
		conn.close();
		
		return builder.build();
		
		
	}
	
	

}
