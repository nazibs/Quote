package all.em.hatch.hatchem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by Lomesh on 3/18/2015.
 */
public class Contacts extends ActionBarActivity
{
    Fragment fragment;
    ContactListFragment list;
    ArrayList<Hatch_contact> refreshlist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.custom_actionbar_contacts,
                null);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setElevation(0);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Toolbar parent =(Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.contacts_container);

        if (fragment == null) {
            fragment = new ContactListFragment();
            fm.beginTransaction()
                    .add(R.id.contacts_container, fragment)
                    .commit();
        }

        ImageButton home=(ImageButton) findViewById(R.id.home_button_contacts);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showhome();
            }
        });

        ImageButton refresh =(ImageButton) findViewById(R.id.refresh_contacts);
        refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                refresh();
            }
        });

        ImageButton block_users = (ImageButton) findViewById(R.id.blocked_users);
        block_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                block_users();
            }
        });
    }

    public void showhome()
    {
        Intent i = new Intent(this,Nest.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void refresh()
    {
        Log.d("Refreshing","");
        refreshlist = Contacts_Store.get(getApplicationContext()).getContacts();
        Log.d("Favourite Contacts before refreshing ",refreshlist.toString());

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("refreshlist",refreshlist.toString()).apply();

        Intent j = new Intent(getApplicationContext(),ContactsService.class);
        startService(j);

        list = (ContactListFragment) fragment;
        //               list.clearadapter();
        //               list.getAdapter().notifyDataSetChanged();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                list.getAdapter().notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Your contact list has been updated.",Toast.LENGTH_SHORT).show();
            }
        },10000);
    }

    public void block_users()
    {
        Intent i = new Intent(this,Blocked_Contacts.class);
        startActivity(i);

    }

}
