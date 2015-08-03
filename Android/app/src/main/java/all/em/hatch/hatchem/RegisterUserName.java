package all.em.hatch.hatchem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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


public class RegisterUserName extends ActionBarActivity
{
    ImageButton next;
    EditText u_name;
    String user_name="";
    AlertDialog confirmDialog;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_name);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.custom_actionbar_next_username,
                null);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setElevation(0);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Toolbar parent =(Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        next = (ImageButton)findViewById(R.id.username_next_button);
        u_name=(EditText)findViewById(R.id.edit_user_name);
        status= (TextView)findViewById(R.id.invalid_user_name);

        confirmDialog= new AlertDialog.Builder(this).create();

        confirmDialog.setButton(Dialog.BUTTON_POSITIVE,"Ok",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                if(user_name!=null)
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Context context = getApplicationContext();
                            try
                            {
                                final int response_code=NetworkingManager.register_User_Name(user_name,context);
                                Log.i("response code",""+response_code);

                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if(response_code == 200)
                                        {

                                            Log.i("Inside response code:200","Successfully registered UserName");
                                            Intent i=new Intent(getApplicationContext(),Nest.class);
                                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("user_name",user_name).apply();
                                            startActivity(i);

                                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("is_app_registered",true).apply();

                                            finish();
                                        }
                                        else
                                        {
                                            Log.d("Inside else : ","Response Code:"+response_code);
                                            if(response_code == 409)
                                            {
                                                Log.d("Inside else then if  : ","Response Code:"+response_code);
                                                //Toast.makeText(getApplicationContext(), "Sorry,this username is already registered", Toast.LENGTH_SHORT).show();
                                                status.setText("This username is already registered.");

                                            }
                                            else
                                            {
                                                Log.i("Inside else than else","Response Code"+response_code);
                                                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
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


        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               user_name=u_name.getText().toString();
                confirmDialog.setMessage("Confirm registration with username "+user_name+"?");
                Log.i("user name",user_name);
                confirmDialog.show();
            }
        });
    }
}
