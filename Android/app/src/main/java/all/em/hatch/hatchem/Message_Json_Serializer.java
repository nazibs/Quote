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
 * Created by Lomesh on 3/15/2015.
 */
public class Message_Json_Serializer
{

    private Context mContext;
    private String mFileName;


    public Message_Json_Serializer(Context mContext, String mFileName) {
        this.mContext = mContext;
        this.mFileName = mFileName;
    }



    public void saveMessages(ArrayList<Hatch_message> messages) throws JSONException, IOException {


        JSONArray array = new JSONArray();

        Log.i("Message Serializer,Arraylist of messages",""+messages.toString());

        for(Hatch_message b: messages){

            array.put(b.toJson());
        }

        Writer writer= null;

        try {


            OutputStream out = mContext.openFileOutput("messages.json", Context.MODE_PRIVATE);

            writer= new OutputStreamWriter(out);


            Log.i("Save messages",""+array.toString());

            writer.write(array.toString());




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


    public ArrayList<Hatch_message> loadMessages() throws IOException, JSONException {

        Log.i("SERIALIZER","IN THE METHOD LOAD MESSAGES");


        ArrayList<Hatch_message> messages = new ArrayList<Hatch_message>();
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            Log.i("SERIALIZER","OPENING FILE TO READ");

            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            Log.i("Seerializer","the input is"+jsonString);

            // Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            // Build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                messages.add(new Hatch_message(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // Ignore this one; it happens when starting fresh
            Log.i("SERIALIZER","ERROR IN IO");
            e.printStackTrace();

        } finally {
            if (reader != null)
                reader.close();
        }
        return messages;
    }

}
