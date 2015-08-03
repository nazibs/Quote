package all.em.hatch.hatchem;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;

/**
 * Created by Lomesh on 3/19/2015.
 */
public class GCM_Intent_Service extends IntentService
{

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    static final String TAG = "GCMDemo";

    public GCM_Intent_Service()
    {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);


        if (!extras.isEmpty())
        {  // has effect of unparcelling Bundle

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                sendNotification("Send error: " + extras.toString());
            }
            else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType))
            {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            }
            else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType))
            {

                String sender_user_name=extras.getString("sender_name");
                String sender_phone_number=extras.getString("sender_number");
                String message_id= extras.getString("message_id");
                String message_content= extras.getString("message");
                String sender_level = extras.getString("sender_level");

                Hatch_message msg = new Hatch_message(sender_user_name,sender_phone_number,sender_level,message_id,200,message_content,"");



                ArrayList<Hatch_contact> mContacts = Blocked_Contacts_Store.get(getApplicationContext()).getContacts();



                boolean blocked=false;


                        for (int i = 0; i < mContacts.size(); i++) {
                            Hatch_contact temp = mContacts.get(i);
                            if (temp.contact_name.equals(msg.sender_name)) {
                                {
                                    blocked = true;
                                }
                                Log.i("MESSAGE STORE", "ADDING A MESSAGE TO MESSAGES");
                            }
                        }

                    if(!blocked)
                    {
                        Message_Store.get(getApplicationContext()).addMessage(msg);

                        Message_Store.get(getApplicationContext()).saveMessages();


                        sendNotification("New message received.");
                    }




                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
           GCM_Broadcast_Receiver.completeWakefulIntent(intent);
    }


    private void sendNotification(String msg)
    {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon )
                        .setContentTitle("quote")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setVibrate(new long[]{1000,500,1000})
                        .setLights(Color.RED, 3000, 3000);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
