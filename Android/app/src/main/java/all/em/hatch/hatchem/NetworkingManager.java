package all.em.hatch.hatchem;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lomesh on 3/15/2015.
 */
public class NetworkingManager {

    public static int register_User(String phone_number,String gcm_reg_id) throws IOException
    {

        HttpURLConnection conn;
        int response_code=0;
        URL url = new URL("http://103.229.26.75:6789/ContactsManager/RegisterPhoneNumber?phone_number="+phone_number+"&gcm_reg_id="+gcm_reg_id);

        conn = (HttpURLConnection) url.openConnection();


            response_code = conn.getResponseCode();
            Log.i("inside"+response_code,"Phone Number "+phone_number+" Gcm_reg_id "+gcm_reg_id);

            conn.disconnect();

        return response_code;

    }


    public static int register_User_Name(String user_name,Context context) throws IOException
    {

        int response_code=0;
        String phone_number = PreferenceManager.getDefaultSharedPreferences(context).getString("phone_number",null);

        if(phone_number!=null)
        {
            HttpURLConnection conn;

            URL url = new URL("http://103.229.26.75:6789/ContactsManager/RegisterUserName?phone_number=" + phone_number + "&user_name=" + user_name);

            conn = (HttpURLConnection) url.openConnection();
             response_code = conn.getResponseCode();
                Log.i("in http", "response code is " + response_code);

            conn.disconnect();

        }
        return response_code;
    }




}
