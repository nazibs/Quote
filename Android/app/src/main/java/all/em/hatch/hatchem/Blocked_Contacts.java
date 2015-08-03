package all.em.hatch.hatchem;

import android.content.Intent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class Blocked_Contacts extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_contacts);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_actionbar_blocked_user,null);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Toolbar parent =(Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.blocked_users_container);
        if (fragment == null)
        {
            fragment = new BlockedListFragment();
            fm.beginTransaction()
                    .add(R.id.blocked_users_container, fragment)
                    .commit();
        }


        ImageButton back = (ImageButton) findViewById(R.id.back_button_blocked);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goback();
            }
        });

    }

    public void goback()
    {
        Intent i = new Intent(this,Contacts.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }


    protected void onPause(){
        super.onPause();
        Blocked_Contacts_Store.get(getApplicationContext()).saveContacts();
    }


}
