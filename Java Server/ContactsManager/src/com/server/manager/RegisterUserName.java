package com.server.manager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/RegisterUserName")
public class RegisterUserName extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    public RegisterUserName() {
    	
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String user_name=request.getParameter("user_name");
		String phone_number= request.getParameter("phone_number");
		
		try {
			DatabaseConnector.registerUsername(user_name,phone_number,response);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
	}

}
