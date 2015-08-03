package all.em.hatch.hatchem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class RegisterActivity extends ActionBarActivity
{

    String gcm_reg_id=null;
    GoogleCloudMessaging gcm;
    Context context;
    String SENDER_ID = "86340369265";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    TextView status;
    ProgressBar network_status;
    ImageButton refresh_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm_register);
        getSupportActionBar().hide();

        status=(TextView)findViewById(R.id.gcm_network_status);
        network_status=(ProgressBar)findViewById(R.id.network_status);
        refresh_button=(ImageButton)findViewById(R.id.button_refresh);
        refresh_button.setVisibility(View.INVISIBLE);

        if(isNetworkAvailable())
        {
            if (checkPlayServices())
            {
                gcm = GoogleCloudMessaging.getInstance(this);

               // final String id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("gcm_reg_id", null);

                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                    int i =0;
                    while(i<5)
                    {
                        if(gcm_reg_id == null)
                            registerInBackground();
                        else break;
                        try
                        {
                            Thread.currentThread().sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        i++;
                    }
                            status.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if(gcm_reg_id==null)
                                    {
                                        status.setText("Unable to connect");
                                        network_status.setVisibility(View.INVISIBLE);
                                        refresh_button.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        String phone_number=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("phone_number",null);
                                        boolean is_app_registered=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("is_app_registered",false);
                                        if(phone_number!=null&&is_app_registered)
                                        {
                                            Intent i= new Intent(getApplicationContext(),Nest.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else  if(phone_number==null)
                                        {
                                            Intent i= new Intent(getApplicationContext(),Register_phone_number.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                        {
                                            Intent i= new Intent(getApplicationContext(),RegisterUserName.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                }
                            });
                    }
                }).start();
           }
        }
        else
        {
            refresh_button.setVisibility(View.VISIBLE);
            network_status.setVisibility(View.INVISIBLE);
            status.setText("Check your Internet connection.");
        }


        refresh_button.setClickable(true);
        //network_status.setVisibility(View.VISIBLE);
        refresh_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                network_status.setVisibility(View.VISIBLE);
                refresh_button.setVisibility(View.INVISIBLE);
                status.setText("");
                if(isNetworkAvailable())
                {
                    if (checkPlayServices())
                    {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

           //             final String id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("gcm_reg_id", null);

                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                int i =0;
                                while(i<5)
                                {
                                    if(gcm_reg_id == null)
                                        registerInBackground();
                                    else break;
                                    try
                                    {
                                        Thread.currentThread().sleep(2000);
                                    }
                                    catch (InterruptedException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    i++;
                                }
                                status.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if(gcm_reg_id==null)
                                        {
                                            status.setText("Unable to connect");
                                            network_status.setVisibility(View.INVISIBLE);
                                            refresh_button.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            String phone_number=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("phone_number",null);
                                            boolean is_app_registered=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("is_app_registered",false);
                                            if(phone_number!=null&&is_app_registered)
                                            {
                                                Intent i= new Intent(getApplicationContext(),Nest.class);
                                                startActivity(i);
                                                finish();
                                            }
                                            else  if(phone_number==null)
                                            {
                                                Intent i= new Intent(getApplicationContext(),Register_phone_number.class);
                                                startActivity(i);
                                                finish();
                                            }
                                            else
                                            {
                                                Intent i= new Intent(getApplicationContext(),RegisterUserName.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                }
                else
                {
                    refresh_button.setVisibility(View.VISIBLE);
                    network_status.setVisibility(View.INVISIBLE);
                    status.setText("Check your Internet connection.");
                }

            }
        });

    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(this,"Sorry,your device is not supported..",Toast.LENGTH_SHORT);
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerInBackground()
    {
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    if (gcm == null)
                    {
                        Log.d("GCM before ","");
                        gcm = GoogleCloudMessaging.getInstance(context);
                        Log.d("GCM after ","");
                    }
                    Log.d("before registering","");

                    gcm_reg_id = gcm.register(SENDER_ID);
                    Log.d("after registering",gcm_reg_id);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("gcm_reg_id",gcm_reg_id).apply();

                      Log.i("REG ID", gcm_reg_id);

                }
                catch (IOException ex)
                {
                        ex.printStackTrace();
                }
            return null;
            }
        }.execute(null, null, null);
    }
}
