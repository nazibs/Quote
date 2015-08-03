package com.server.xmpp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.smack.XMPPException;


@WebServlet("/Start")
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String GOOGLE_SERVER_KEY = "AIzaSyB8XuNWeEgnluE9MQ5ezffPftBckJA_1iY";
	final String GOOGLE_USERNAME = "86340369265" + "@gcm.googleapis.com";
	
	
	
	
    public Start() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String doAction= request.getParameter("action");
		
	
			
			Receiver ccsClient = new Receiver();

			try {
				ccsClient.connect(GOOGLE_USERNAME, GOOGLE_SERVER_KEY);
			} catch (XMPPException e) {
				e.printStackTrace();
			}

			
		response.setStatus(HttpServletResponse.SC_FOUND);	
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
