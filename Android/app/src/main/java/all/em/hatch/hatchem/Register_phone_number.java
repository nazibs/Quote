package all.em.hatch.hatchem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class Register_phone_number extends ActionBarActivity
{
    EditText p_number;
    ImageButton next;
    String phone_number,gcm_reg_id;
    TextView status;
    AlertDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone_number);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.custom_actionbar_next_number,
                null);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setElevation(0);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Toolbar parent =(Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);


        p_number = (EditText) findViewById(R.id.user_phone_number);
        next = (ImageButton)findViewById(R.id.contacts_next_button);
        status= (TextView)findViewById(R.id.invalid_phone_number);


        confirmDialog= new AlertDialog.Builder(this).create();


        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                phone_number = p_number.getText().toString();
                gcm_reg_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("gcm_reg_id", null);

                Log.i("Phone Number " +phone_number,"GCM_REG_ID "+gcm_reg_id);

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("gcm_reg_id",gcm_reg_id).apply();

                confirmDialog.setMessage("Confirm registration with number  " + phone_number + "?");

                confirmDialog.show();
            }
        });


        confirmDialog.setButton(Dialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                if (Is_phone_number_verified() && phone_number != null)
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try {
                                final int response_code = NetworkingManager.register_User(phone_number, gcm_reg_id);
                                Log.i("Response Code : ", "" + response_code);

                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if (response_code == 200)
                                        {
                                            Intent j = new Intent(getApplicationContext(),ContactsService.class);
                                            startService(j);

                                            Log.i("Inside response code:200", "Successfully registered phone number");
                                            Intent i = new Intent(getApplicationContext(), RegisterUserName.class);
                                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("phone_number",phone_number).apply();
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                        {
                                            Log.d("Inside else : ", "Response Code:" + response_code);
                                            if (response_code == 409)
                                            {
                                                Log.d("Inside else then if  : ", "Response Code:" + response_code);
                                                // Toast.makeText(getApplicationContext(), "Sorry! this number is already registered", Toast.LENGTH_SHORT).show();
                                                status.setText("This number is already registered.");
                                            }
                                            else
                                            {
                                                Log.i("Inside else than else", "Response Code" + response_code);
                                                Toast.makeText(getApplicationContext(), "Please try again.. ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

        confirmDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                confirmDialog.cancel();
            }
        });

    }

    private boolean Is_phone_number_verified()
    {
        //Verfication code here;

        return true;
    }
}
