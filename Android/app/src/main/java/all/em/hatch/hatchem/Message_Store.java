package all.em.hatch.hatchem;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lomesh on 3/15/2015.
 */
public class Message_Store {


    private ArrayList<Hatch_message> mMessages;
    private Context mAppContext;
    private static Message_Store sMessageStore;
    private Message_Json_Serializer mSerializer;



    private Message_Store(Context appContext){
        mAppContext= appContext;

        //mMessages= new ArrayList<BroadcastMessage>();

        Log.i("MESSAGE STORE", "IN THE CONSTRUCTOR");
        mSerializer= new Message_Json_Serializer(mAppContext, "messages.json");



        try {
            Log.i("MESSAGE STORE","TRYING TO LOAD MESSAGES");
            mMessages = mSerializer.loadMessages();
        } catch (Exception e) {
            mMessages = new ArrayList<Hatch_message>();
            Log.i("MESSAGE STORE","ERROR LOADING MESSAGES");
            e.printStackTrace();
        }

    }



    public static Message_Store get(Context appContext){

        if(sMessageStore==null){
            Log.i("MESSAGE STORE","IN THE GET METHOD, MESSAGE STORE IS NULL");
            sMessageStore= new Message_Store(appContext.getApplicationContext());
        }

        return sMessageStore;
    }



    public ArrayList<Hatch_message> getMessages(){

        Log.i("MESSAGE STORE","IN THE METHOD GET MESSAGES");

        return mMessages;
    }

    public void addMessage(Hatch_message m) {



        mMessages.add(m);

    }

    public boolean saveMessages(){

        try{

            Log.i("MESSAGE STORE","TRYING TO SAVE MESSAGES..");

            mSerializer.saveMessages(mMessages);

            Log.i("MESSAGE STORE","MESSAGES SAVED SUCCESSFULLY");
            return true;

        }
        catch (Exception e) {
             Log.i("MESSAGE STORE","ERROR SAVING MESSAGES..");
            return false;
        }
    }





}
