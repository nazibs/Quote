package httppost;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.json.*;
public class test {

	public static void main(String args[]) throws IOException{
		
	
	HttpURLConnection conn;
	
    URL url = new URL("https://android.googleapis.com/gcm/send");
	
    conn=(HttpURLConnection) url.openConnection();
    
    conn.setDoOutput(true);
    conn.setUseCaches(false);
    
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Authorization", "key=AIzaSyBtD6Fg6twpvoY_aI0ZF3-tcC8Ae6oStpQ");
    OutputStream out = conn.getOutputStream();
    
    int rc = conn.getResponseCode();
    HashMap<String,String> json = new HashMap<String,String>();
    
    
    
    json.put("time_to_live","108");
    json.put("registration_ids","[\"APA91bGXtT9XqQ9ThabWFActcnzcshq8QghNvKzt-g9pPJEVpYbzKuHbKJF3YeiWjiOML1Maoe3AEE8dM9N4ZdNMaVanJPMjkFFlbJVUwPtmGVFkaELYRZAlT-K4TWzAAKQO62ApdDnQ-FjiI8kG_S_crCQdJqGU_QIwdDK4rSjbOrAW1zGqM3c\"]");
    
   String js= json.toString();
   
   System.out.println(rc);
    
   byte[] bytes= js.getBytes();
   
   out.write(bytes);
   
   
   out.close();
    
	}
	
}
