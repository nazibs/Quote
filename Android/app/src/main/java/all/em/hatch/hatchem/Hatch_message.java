package all.em.hatch.hatchem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Lomesh on 3/15/2015.
 */
public class Hatch_message {

    String sender_name;
    String sender_phone_number;
    String sender_level;
    String message_id;
    Integer message_time_to_hatch;
    String message_content;
    String message_type;


    public Hatch_message(String sender_name, String sender_phone_number, String sender_level, String message_id, Integer message_time_to_hatch, String message_content, String message_type)
    {
        this.sender_name = sender_name;
        this.sender_phone_number = sender_phone_number;
        this.sender_level = sender_level;
        this.message_id = message_id;
        this.message_time_to_hatch = message_time_to_hatch;
        this.message_content = message_content;
        this.message_type = message_type;
    }

    public Hatch_message(String phone_number,String user_name,String content){
        Random rand = new Random();

        Long phone= Long.parseLong(phone_number);

        Long message_id =rand.nextLong();

        message_id+=(phone+ content.length());



        this.sender_name=user_name;
        this.sender_phone_number=phone_number;
        this.message_content=content;
        this.message_id= message_id.toString();
        this.message_time_to_hatch=200;
        this.sender_level="0";
        this.message_type="new_message";



    }


    public Hatch_message(JSONObject obj) throws JSONException
    {

        this.sender_name =obj.getString("sender_name");
        this.sender_phone_number = obj.getString("sender_phone_number");
        this.sender_level = obj.getString("sender_level");
        this.message_id = obj.getString("message_id");
        this.message_time_to_hatch =obj.getInt("message_time_to_hatch");
        this.message_content = obj.getString("message_content");
        this.message_type = obj.getString("message_type");

    }

    public JSONObject toJson() throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put("sender_name",this.sender_name);
        obj.put("sender_phone_number",this.sender_phone_number);
        obj.put("sender_level",this.sender_level);
        obj.put("message_id",this.message_id);
        obj.put("message_time_to_hatch",this.message_time_to_hatch);
        obj.put("message_content",this.message_content);
        obj.put("message_type",this.message_type);


        return obj;

    }




}
