package all.em.hatch.hatchem;

import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class Splash_Screen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        getSupportActionBar().hide();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {


                boolean is_app_registered= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("is_app_registered",false);

                if(is_app_registered)
                {
                    Intent i = new Intent(getApplicationContext(),Nest.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, 1000 );

    }

}
