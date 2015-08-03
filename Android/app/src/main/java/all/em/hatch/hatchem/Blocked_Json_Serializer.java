package all.em.hatch.hatchem;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Lomesh on 4/6/2015.
 */

public class Blocked_Json_Serializer
{
    private Context mContext;
    private String mFileName;


    public Blocked_Json_Serializer(Context mContext, String mFileName)
    {
        this.mContext = mContext;
        this.mFileName = mFileName;
    }


    public void saveContacts(ArrayList<Hatch_contact> contacts) throws JSONException, IOException
    {

        JSONArray array = new JSONArray();

        for(Hatch_contact b: contacts){

            array.put(b.toJson());
        }

        Writer writer= null;

        try {

            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);

            writer= new OutputStreamWriter(out);

            writer.write(array.toString());

            Log.i("SERIALIZER", "FILE SAVED SUCCESSFULLY");


        } catch (FileNotFoundException e) {

            if(writer!=null){
                Log.i("SERIALIZER","ERROR IN WRITING, WRITER IS NOT NULL");
                writer.close();
            }
            Log.i("SERIALIZER","ERROR IN WRITING, WRITER IS NULL");
            e.printStackTrace();
        }
        finally{
            writer.close();
        }

    }


    public ArrayList<Hatch_contact> loadContacts() throws IOException, JSONException {

        //Log.i("SERIALIZER","IN THE METHOD LOAD MESSAGES");


        ArrayList<Hatch_contact> contacts = new ArrayList<Hatch_contact>();
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            // Log.i("SERIALIZER","OPENING FILE TO READ");

            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            // Build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                contacts.add(new Hatch_contact(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // Ignore this one; it happens when starting fresh
            Log.i("SERIALIZER", "ERROR IN IO");

        } finally {
            if (reader != null)
                reader.close();
        }
        return contacts;
    }


}
