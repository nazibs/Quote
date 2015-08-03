package all.em.hatch.hatchem;


import android.app.NotificationManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class Nest extends ActionBarActivity
{
    EditText message_to_send;
    ImageButton button_send;
    String message_content_to_send;
    static Fragment fragment;
static ListView listV;
    GoogleCloudMessaging gcm;
    String SENDER_ID = "86340369265";

    @Override



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.custom_actionbar_nest,
                null);


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setElevation(0);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Toolbar parent =(Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0,0);

        ImageButton bookmarks = (ImageButton) findViewById(R.id.bookmark_button);
        ImageButton profile = (ImageButton) findViewById(R.id.profile_button);
        ImageButton contacts = (ImageButton) findViewById(R.id.contacts_button);

        bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBookmarks();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfile();
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startContacts();
            }
        });


        NotificationManager nmanager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        nmanager.cancelAll();




        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.message_container);
        if (fragment == null) {
            fragment = new MessageListFragment();

            fm.beginTransaction()
                    .add(R.id.message_container, fragment)
                    .commit();
        }

        message_to_send = (EditText) findViewById(R.id.message_to_send);
        button_send = (ImageButton) findViewById(R.id.button_send);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        sendMessage(message_to_send.getText().toString());

                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {

                               message_to_send.setText("");
                               Toast.makeText(getApplicationContext(),"Message sent!",Toast.LENGTH_SHORT).show();
                           }
                       });

                    }
                }).start();

            }

        });
    }


    protected void onPause(){
        super.onPause();
        //t.interrupt();
        Message_Store.get(getApplicationContext()).saveMessages();

    }


     void sendMessage(String message){

        String phone_number = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("phone_number",null);
        String user_name = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_name",null);

        Hatch_message my_message = new Hatch_message(phone_number,user_name,message);

        addAndSave(my_message);

        Bundle gcm_message = new Bundle();

        gcm_message.putString("sender_name",my_message.sender_name);
        gcm_message.putString("sender_phone_number", my_message.sender_phone_number);
        gcm_message.putString("message_id",my_message.message_id);
        gcm_message.putString("message_content",my_message.message_content);
        gcm_message.putString("sender_level",my_message.sender_level);
        gcm_message.putString("time_to_hatch",my_message.message_time_to_hatch.toString());
        gcm_message.putString("message_type","new");


        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

        if(gcm!=null) {
            try {
                Log.i("GCM SEND","Trying to send..");
                gcm.send(SENDER_ID+"@gcm.googleapis.com", my_message.message_id, 500, gcm_message);
                Log.i("GCM SEND","Message sent..");
            } catch (IOException e) {

                Log.i("GCM SEND", "CANNOT SEND,IO ERROR");

                e.printStackTrace();
            }

        }
        else
        {
            Log.i("GCM SEND","GCM is null");
        }

        if(fragment!=null){

           final MessageListFragment list = (MessageListFragment) fragment;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    list.getAdapter().notifyDataSetChanged();

                }
            });
        }
    }


    void addAndSave(Hatch_message m)
    {
        MyMessage_Store.get(getApplicationContext()).addMessage(m);
        MyMessage_Store.get(getApplicationContext()).saveMessages();
       // Message_Store.get(getApplicationContext()).addMessage(m);
        //Message_Store.get(getApplicationContext()).saveMessages();
    }




    public void startContacts()
    {
        Intent i = new Intent(this,Contacts.class);
        Message_Store.get(getApplicationContext()).saveMessages();
        startActivity(i);

    }

    public void startBookmarks()
    {
        Intent i = new Intent(this,Bookmarks.class);
        Message_Store.get(getApplicationContext()).saveMessages();
        startActivity(i);
    }

    public void startProfile()
    {
        Intent i = new Intent(this,Profile.class);
        Message_Store.get(getApplicationContext()).saveMessages();
        startActivity(i);
    }
}
