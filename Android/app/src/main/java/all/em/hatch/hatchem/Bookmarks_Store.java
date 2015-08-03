package all.em.hatch.hatchem;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Lomesh on 3/28/2015.
 */
public class Bookmarks_Store
{
    private ArrayList<Hatch_message> mBookmarks;
    private Context mAppContext;
    private static Bookmarks_Store sBookmarks_store;
    private Bookmarks_Json_Serializer mSerializer;

    private Bookmarks_Store(Context appContext)
    {
        mAppContext= appContext;

        //Log.i("BOOKMMARK STORE", "IN THE CONSTRUCTOR");
        mSerializer= new Bookmarks_Json_Serializer(mAppContext, "bookmarks.json");



        try {
            //Log.i("MESSAGE STORE","TRYING TO LOAD MESSAGES");
            mBookmarks = mSerializer.loadBookmarks();
        } catch (Exception e) {
            mBookmarks= new ArrayList<Hatch_message>();
            //Log.i("MESSAGE STORE","ERROR LOADING MESSAGES");
        }

    }

    public static Bookmarks_Store get(Context appContext){

        if(sBookmarks_store==null){
            //Log.i("MESSAGE STORE","IN THE GET METHOD, MESSAGE STORE IS NULL");
            sBookmarks_store= new Bookmarks_Store(appContext.getApplicationContext());
        }

        return sBookmarks_store;
    }

    public ArrayList<Hatch_message> getBookmarks()
    {

        //Log.i("MESSAGE STORE","IN THE METHOD GET MESSAGES");

        return mBookmarks;
    }

    public void addBookmarks(Hatch_message m)
    {
        mBookmarks.add(m);
        //Log.i("MESSAGE STORE","ADDING A MESSAGE TO MESSAGES");
    }

    public boolean saveBookmarks()
    {
        try
        {
            //Log.i("MESSAGE STORE","TRYING TO SAVE MESSAGES..");
            mSerializer.saveBookmarks(mBookmarks);
            //Log.i("MESSAGE STORE","MESSAGES SAVED SUCCESSFULLY");
            return true;
        }
        catch (Exception e)
        {
            // Log.i("MESSAGE STORE","ERROR SAVING MESSAGES..");
            return false;
        }
    }



}
