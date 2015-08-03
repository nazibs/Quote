package com.server.manager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterPhoneNumber")
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	Logger logger;
   
    public RegisterUser() {
        super();
       
    } 

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
        /*
         * Get the number of the client and the ID to register, from the
         * request parameters.
         */
		String phone_number=request.getParameter("phone_number");
		String gcm_reg_id=request.getParameter("gcm_reg_id");
		//String user_name=request.getParameter("user_name");
		
		
		logger=Logger.getLogger("Register UserPhone Servlet");
		
	
		try {
			
			/*
			 * Register the ID and number in the database.
			 */
			
			logger.info("Attempting to register user...");
			logger.info("Phone number="+phone_number);
			
			DatabaseConnector.registerInDatabase(phone_number,gcm_reg_id,response);
			
			logger.info("Registration Successful.");
			
			
			
			
			
		} catch (ClassNotFoundException e) {
			logger.info("Class not found!");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			
			
		} 
	}

	
	
	
	/*
	 * 
	 * Do nothing in post(do get instead)
	 * 
	 *
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
}
