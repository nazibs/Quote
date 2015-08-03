package all.em.hatch.hatchem;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Lomesh on 3/15/2015.
 */
public class ContactsService extends IntentService{

    private static final String TAG="ContactService";
    String jsonString;
    JSONArray jsonArray;
    ArrayList<String> array1;

    public ContactsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        String refresh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("refreshlist", null);
        Log.d("Refresh string ", "" + refresh);


        ArrayList<HashMap<String, String>> contactData = new ArrayList<HashMap<String, String>>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", name);
                        map.put("number", phoneNumber);
                        contactData.add(map);
                    }
                    phones.close();
                }
            } catch (Exception e) {
            }
        }

        ArrayList<String> numbers = new ArrayList<String>();

        for (int i = 0; i < contactData.size(); i++) {

            HashMap<String, String> hm = contactData.get(i);
            String num = hm.get("number");
            numbers.add(num);
        }


        ArrayList<String> new_numbers = new ArrayList<String>();

        for (int i = 0; i < numbers.size(); i++) {
            String p = numbers.get(i);
            p = p.replaceAll("[^0-9]", "");
            if (p.charAt(0) == '0') {
                p = p.replaceFirst("0", "");
            }
            if (p.length() > 10 && p.charAt(0) == '9' && p.charAt(1) == '1') {
                p = p.substring(2, p.length());
            }

            if(refresh!=null)
            {

            if (!refresh.contains(p))
                    new_numbers.add("\"" + p + "\"");
            }
            else
                new_numbers.add("\"" + p + "\"");

        }


        Log.d("List to send ", new_numbers.toString());

        HashSet<String> numbers_final = new HashSet<String>(new_numbers);

        HttpURLConnection conn;

        String phone_number = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("phone_number", null);
        Log.d("Phone number : ", phone_number);
        if (phone_number != null) {
            try {
                URL url = new URL("http://103.229.26.75:6789/ContactsManager/ReturnContacts?phone_number=" + phone_number);
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(numbers_final.toString());
                wr.flush();
                wr.close();

                int responseCode = conn.getResponseCode();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                 String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                conn.disconnect();
                //Log.i("return string",response.toString());
                jsonString = response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

       if(jsonString!=null)
        {
            Log.d("Json String : ", "" + jsonString);
            try {
            jsonArray = new JSONArray(jsonString);
            array1 = new ArrayList<String>();

            Log.i("json array", jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                array1.add(jsonArray.getString(i));

            }

            ArrayList<Hatch_contact> contacts = Contacts_Store.get(getApplicationContext()).getContacts();

            for (int i = 0; i < array1.size(); i++)
                for (int j = 0; j < contactData.size(); j++) {

                    HashMap<String, String> map = contactData.get(j);

                    String number = map.get("number");

                    number = number.replaceAll(" ", "");

                    if (number.contains(array1.get(i))) {
                        String name = map.get("name");

                        Hatch_contact contact = new Hatch_contact(name, number);

                        contacts.add(contact);

                        j = contactData.size() + 1;

                    }
                }

            Log.i("CONTACTS:", contacts.toString());
            Contacts_Store.get(getApplicationContext()).saveContacts();

            Thread.currentThread().sleep(5000);

            ArrayList<Hatch_contact> contacts1 = Contacts_Store.get(getApplicationContext()).getContacts();

            Log.i("from file:", contacts1.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    }
}
