package all.em.hatch.hatchem;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Lomesh on 3/18/2015.
 */
public class MessageListFragment extends ListFragment {

    private ArrayList<Hatch_message> mMessages,Messages;
    public static MessageAdapter adapter;
    String SENDER_ID = "86340369265";
    AlertDialog confirmDialog;
    Hatch_message selected_message;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Nest");

        Log.i("MessageListFrag","in oncreate");

        mMessages=Message_Store.get(getActivity()).getMessages();

        adapter= new MessageAdapter(mMessages);

        setListAdapter(adapter);
        confirmDialog= new AlertDialog.Builder(getActivity()).create();
    }


    @Override
    public void onViewCreated (final View view, Bundle savedInstanceState){

        super.onViewCreated(view,savedInstanceState);

        ListView listView = getListView();

        listView.setDivider(null);

        NotificationManager man= (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        man.cancelAll();
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapter.remove(adapter.getItem(position));
                                }
                                adapter.notifyDataSetChanged();
                                Message_Store.get(getActivity()).saveMessages();
                            }
                        });
        listView.setOnTouchListener(touchListener);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {

                selected_message = (Hatch_message) parent.getItemAtPosition(position);

              //  confirmDialog.setMessage("");
                confirmDialog.show();




                return true;
            }
        });

        confirmDialog.setButton(Dialog.BUTTON_POSITIVE,"Bookmark",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                TextView message_content=(TextView)view.findViewById(R.id.message_content);
                TextView sender_name=(TextView)view.findViewById(R.id.sender_user_name);

                Log.i("LONG PRESS",message_content.getText().toString());

                bookmark(selected_message);


                Bookmarks_Store.get(getActivity()).addBookmarks(selected_message);
                Bookmarks_Store.get(getActivity()).saveBookmarks();

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getActivity(),"Bookmarked!",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        confirmDialog.setButton(Dialog.BUTTON_NEGATIVE,"Block user",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Hatch_contact block_con = new Hatch_contact();
                block_con.contact_name = selected_message.sender_name;
                block_con.contact_phone_number = selected_message.sender_phone_number;

                Blocked_Contacts_Store.get(getActivity()).addContact(block_con);
                Blocked_Contacts_Store.get(getActivity()).saveContacts();

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getActivity(),"User blocked!",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("ON CLICK", "CLICKED!");
                Hatch_message toBroadcast = (Hatch_message) parent.getItemAtPosition(position);
                broadcast(toBroadcast);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "Broadcasted!", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


        listView.setOnScrollListener(touchListener.makeScrollListener());

    }





    public void bookmark(Hatch_message message){


       final Bundle gcm_message = new Bundle();


        gcm_message.putString("sender_name",message.sender_name);
        gcm_message.putString("sender_phone_number", message.sender_phone_number);
        gcm_message.putString("message_id",message.message_id);
        gcm_message.putString("message_content",message.message_content);
        gcm_message.putString("sender_level",message.sender_level);
        gcm_message.putString("time_to_hatch",message.message_time_to_hatch.toString());
        gcm_message.putString("message_type","bookmark");


        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getActivity());

        Random rand = new Random();

        final Long message_id =rand.nextLong();


        if(gcm!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("GCM SEND","Trying to send..");
                        gcm.send(SENDER_ID+"@gcm.googleapis.com",message_id.toString(), 500, gcm_message);
                        Log.i("GCM SEND","Message sent..");
                    } catch (IOException e) {

                        Log.i("GCM SEND", "CANNOT SEND,IO ERROR");

                        e.printStackTrace();
                    }

                }
            }).start();
        }
        else
        {
            Log.i("GCM SEND","GCM is null");
        }


    }

    public void broadcast(Hatch_message message){


       final Bundle gcm_message = new Bundle();


        gcm_message.putString("sender_name",message.sender_name);
        gcm_message.putString("sender_phone_number", message.sender_phone_number);
        gcm_message.putString("message_id",message.message_id);
        gcm_message.putString("message_content",message.message_content);
        gcm_message.putString("sender_level",message.sender_level);
        gcm_message.putString("time_to_hatch",message.message_time_to_hatch.toString());
        gcm_message.putString("message_type","broadcast");


        Random rand = new Random();

        final Long message_id =rand.nextLong();


        final   GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getActivity());

        if(gcm!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("GCM SEND","Trying to send..");
                        gcm.send(SENDER_ID+"@gcm.googleapis.com",message_id.toString(), 500, gcm_message);
                        Log.i("GCM SEND","Message sent..");
                    } catch (IOException e) {

                        Log.i("GCM SEND", "CANNOT SEND,IO ERROR");

                        e.printStackTrace();
                    }

                }
            }).start();
        }
        else
        {
            Log.i("GCM SEND","GCM is null");
        }


    }

public MessageAdapter getAdapter(){

    return adapter;
}


    class MessageAdapter extends ArrayAdapter<Hatch_message>{


        public MessageAdapter(ArrayList<Hatch_message> messages) {
            super(getActivity(), 0, messages);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.fragment_message_nest, null);
            }

            final Hatch_message c = getItem(position);



            Handler h = new Handler();

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                remove(c);
                }
            },10000
            );


            TextView SenderName=(TextView)convertView.findViewById(R.id.sender_user_name);

            TextView Message=(TextView)convertView.findViewById(R.id.message_content);

            SenderName.setText("- "+c.sender_name);

            Message.setText(c.message_content);

        //  Rating.setText(c.sender_level);

            return convertView;

        }

    }

}


