package all.em.hatch.hatchem;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lomesh on 3/15/2015.
 */
public class Contacts_Store {



    private ArrayList<Hatch_contact> mContacts;
    private Context mAppContext;
    private static Contacts_Store sContacts_store;
    private Contacts_Json_Serializer mSerializer;



    private Contacts_Store(Context appContext)
    {
        mAppContext= appContext;

        //mMessages= new ArrayList<BroadcastMessage>();

        Log.i("Contact STORE", "IN THE CONSTRUCTOR");
        mSerializer= new Contacts_Json_Serializer(mAppContext, "contacts.json");



        try {
            Log.i("Contact STORE","TRYING TO LOAD Contacts");
            mContacts = mSerializer.loadContacts();
        }
        catch (Exception e) {
            mContacts= new ArrayList<Hatch_contact>();
            Log.i("Contact STORE","ERROR LOADING Contacts");
        }

    }



    public static Contacts_Store get(Context appContext)
    {

        if(sContacts_store==null){
            Log.i("Contact STORE","IN THE GET METHOD, Contact STORE IS NULL");
            sContacts_store= new Contacts_Store(appContext.getApplicationContext());
        }

        return sContacts_store;
    }



    public ArrayList<Hatch_contact> getContacts()
    {

        Log.i("Contact STORE","IN THE METHOD GET Contact");

        return mContacts;
    }

    public void addContact(Hatch_contact m){
        mContacts.add(m);

        Log.i("Contact STORE","ADDING A MESSAGE TO Contact");
    }


    public boolean saveContacts(){

        try{

            Log.i("Contact STORE","TRYING TO SAVE Contact..");

            mSerializer.saveContacts(mContacts);

            Log.i("Contact STORE","Contact SAVED SUCCESSFULLY");
            return true;

        }
        catch (Exception e) {
             Log.i("Contact STORE","ERROR SAVING Contact..");
            return false;
        }
    }




}
