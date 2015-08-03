package all.em.hatch.hatchem;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lomesh on 4/6/2015.
 */
public class Blocked_Contacts_Store
{

    private ArrayList<Hatch_contact> mContacts;
    private Context mAppContext;
    private static Blocked_Contacts_Store sBlocked_Contacts_store;
    private Blocked_Json_Serializer mSerializer;



    private Blocked_Contacts_Store(Context appContext)
    {
        mAppContext= appContext;

        //mMessages= new ArrayList<BroadcastMessage>();

        Log.i("Blocked_Contact STORE", "IN THE CONSTRUCTOR");
        mSerializer= new Blocked_Json_Serializer(mAppContext, "blocked_contacts.json");


        try {
            Log.i("Blocked_Contact STORE","TRYING TO LOAD Contacts");
            mContacts = mSerializer.loadContacts();
        }
        catch (Exception e) {
            mContacts= new ArrayList<Hatch_contact>();
            Log.i("Blocked_Contact STORE","ERROR LOADING Contacts");
        }

    }


    public static Blocked_Contacts_Store get(Context appContext)
    {

        if(sBlocked_Contacts_store==null){
            Log.i("Blocked_Contact STORE","IN THE GET METHOD, Contact STORE IS NULL");
            sBlocked_Contacts_store= new Blocked_Contacts_Store(appContext.getApplicationContext());
        }

        return sBlocked_Contacts_store;
    }


    public ArrayList<Hatch_contact> getContacts()
    {

        Log.i("Blocked_Contact STORE","IN THE METHOD GET Contact");

        return mContacts;
    }

    public void addContact(Hatch_contact m){
        mContacts.add(m);

        Log.i("Blocked_Contact STORE","ADDING A MESSAGE TO Contact");
    }

    public boolean saveContacts(){

        try{

            Log.i("Blocked_Contact STORE","TRYING TO SAVE Contact..");

            mSerializer.saveContacts(mContacts);

            Log.i("Blocked_Contact STORE","Contact SAVED SUCCESSFULLY");
            return true;

        }
        catch (Exception e) {
            Log.i("Blocked_Contact STORE","ERROR SAVING Contact..");
            return false;
        }
    }

}
