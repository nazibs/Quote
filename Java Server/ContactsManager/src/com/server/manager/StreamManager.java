package com.server.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;

public class StreamManager {

	
 public static String toStringFromRequestStream(HttpServletRequest request) throws IOException{
	 
	 BufferedReader br= new BufferedReader(new InputStreamReader(request.getInputStream()));
		
		String ip;
		
		StringBuilder sb= new StringBuilder();
		
		while((ip=br.readLine())!=null){
			
			sb.append(ip);
			
		}
		
		return sb.toString();
	 
 }
	
 public static JsonArray toJsonArrayFromJsonArrayString(String array){
	 
	 JsonArray json;
	 
	 JsonReader rdr= Json.createReader(new StringReader(array));
	 
	 json=rdr.readArray();
	 
	 return json;
 }
 
	
	
}
