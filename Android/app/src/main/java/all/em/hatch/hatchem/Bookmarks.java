package all.em.hatch.hatchem;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class Bookmarks extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_actionbar_bookmarks,null);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Toolbar parent =(Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.bookmarks_container);
        if (fragment == null)
        {
            fragment = new BookmarksListFragment();
            fm.beginTransaction()
                    .add(R.id.bookmarks_container, fragment)
                    .commit();
        }


        ImageButton home=(ImageButton) findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showhome();
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

    protected void onPause(){
        super.onPause();
        //t.interrupt();
        Bookmarks_Store.get(getApplicationContext()).saveBookmarks();
    }

}