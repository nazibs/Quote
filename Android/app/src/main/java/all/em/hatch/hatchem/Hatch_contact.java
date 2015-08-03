package all.em.hatch.hatchem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lomesh on 3/15/2015.
 */
public class Hatch_contact {

    String contact_name;
    String contact_phone_number;


    public Hatch_contact()
    {

    }

    public Hatch_contact(String contact_name, String contact_phone_number) {
        this.contact_name = contact_name;
        this.contact_phone_number = contact_phone_number;

    }


    public  Hatch_contact(JSONObject obj) throws JSONException {


        this.contact_name=obj.getString("contact_name");

        this.contact_phone_number=obj.getString("contact_phone_number");


    }


    public JSONObject toJson() throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put("contact_name",this.contact_name);
        obj.put("contact_phone_number",this.contact_phone_number);


        return obj;
    }


    @Override
    public String toString(){
        return "Number is"+this.contact_phone_number+"Name is"+this.contact_name;

    }


}
