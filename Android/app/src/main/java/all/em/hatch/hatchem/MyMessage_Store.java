package all.em.hatch.hatchem;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lomesh on 3/30/2015.
 */
public class MyMessage_Store
{

    private ArrayList<Hatch_message> mMessages;
    private Context mAppContext;
    private static MyMessage_Store sMessageStore;
    private MyMessage_Json_Serializer mSerializer;

    private MyMessage_Store(Context appContext)
    {
        mAppContext= appContext;

        //mMessages= new ArrayList<BroadcastMessage>();

        Log.i("MYMESSAGE STORE", "IN THE CONSTRUCTOR");
        mSerializer= new MyMessage_Json_Serializer(mAppContext, "mymessages.json");

        try {
            Log.i("MYMESSAGE STORE","TRYING TO LOAD MESSAGES");
            mMessages = mSerializer.loadMessages();
        } catch (Exception e) {
            mMessages = new ArrayList<Hatch_message>();
            Log.i("MYMESSAGE STORE","ERROR LOADING MESSAGES");
            e.printStackTrace();
        }
    }


    public static MyMessage_Store get(Context appContext){

        if(sMessageStore==null){
            Log.i("MYMESSAGE STORE","IN THE GET METHOD, MESSAGE STORE IS NULL");
            sMessageStore= new MyMessage_Store(appContext.getApplicationContext());
        }
        return sMessageStore;
    }

    public ArrayList<Hatch_message> getMessages(){

        Log.i("MYMESSAGE STORE","IN THE METHOD GET MESSAGES");

        return mMessages;
    }

    public void addMessage(Hatch_message m){
        mMessages.add(m);

        Log.i("MYMESSAGE STORE","ADDING A MESSAGE TO MESSAGES");
    }


    public boolean saveMessages(){

        try{

            Log.i("MYMESSAGE STORE","TRYING TO SAVE MESSAGES..");

            mSerializer.saveMessages(mMessages);

            Log.i("MYMESSAGE STORE","MESSAGES SAVED SUCCESSFULLY");
            return true;

        }
        catch (Exception e) {
            Log.i("MYMESSAGE STORE","ERROR SAVING MESSAGES..");
            return false;
        }
    }
}
