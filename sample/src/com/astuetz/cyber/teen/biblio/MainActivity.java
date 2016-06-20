package com.astuetz.cyber.teen.biblio;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.karnix.cyberteen.biblio.R;
import com.parse.ParseObject;

import at.markushi.ui.CircleButton;


public class MainActivity extends ActionBarActivity
{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private TextView title, navigationName, navigationEmail;
    private ListView navigationItems, loginItems;

    private Toolbar toolbar;

    CircleButton signOut;
    RippleView rippleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Biblio.userName = getSharedPreferences("pref", 0).getString("name", null);
        Biblio.userEmail = getSharedPreferences("pref", 0).getString("email", null);

//        Biblio.userName = "Ram";
//        Biblio.userEmail = "karnikram@gmail.com";

        if(Biblio.userName == null || Biblio.userEmail == null)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        else
        {
            if(getSharedPreferences("pref",0).getBoolean("save",false))
            {
                SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                editor.putBoolean("save",false);
                editor.commit();
                ParseObject user = new ParseObject("UserDetails");
                user.put("username",Biblio.userName);
                user.put("useremail",Biblio.userEmail);
                user.saveInBackground();
            }
            Toast.makeText(getApplicationContext(), "Hello, " + Biblio.userName + "!", Toast.LENGTH_LONG).show();
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationName = (TextView) findViewById(R.id.navigation_name);
        navigationEmail = (TextView) findViewById(R.id.navigation_email);

        navigationItems = (ListView) findViewById(R.id.navigation_items);
        loginItems = (ListView) findViewById(R.id.login_items);

        navigationItems.setAdapter(new NavigationAdapter(this,Biblio.nitems));
        loginItems.setAdapter(new NavigationAdapter(this,Biblio.litems));

        loginItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new AccountFragment()).commit();
                    drawerLayout.closeDrawers();
                } else {

                }

            }
        });

        navigationItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle args = new Bundle();
                MainFragment fragment = new MainFragment();
                if (i == 0) {
                    args.putInt("position", 0);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                    drawerLayout.closeDrawers();
                }

                if (i == 1) {
                    args.putInt("position", 1);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                    drawerLayout.closeDrawers();
                }

                if (i == 2) {
                    args.putInt("position", 2);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                    drawerLayout.closeDrawers();
                }
            }
        });

        navigationName.setText(Biblio.userName);
        navigationEmail.setText(Biblio.userEmail);

        signOut = (CircleButton) findViewById(R.id.blogout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };
        // Drawer Toggle Object Made
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


//        AdView mAdview = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdview.loadAd(adRequest);

        getSupportFragmentManager().beginTransaction().add(R.id.main_container, new MainFragment()).commit();


        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.tool_title);
        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/TitleFont.otf"));

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                new LoginActivity().signOut();

                Intent logIn = new Intent(MainActivity.this, LoginActivity.class);
                logIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logIn);
            }
        });

        rippleInfo = (RippleView) findViewById(R.id.rippleInfo);
        rippleInfo.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new AboutActivity()).addToBackStack(null).setCustomAnimations(R.anim.enter_left,R.anim.exit_right).commit();
            }
        });
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
}

class NavigationItem
{
    int imageId;
    String item;

    public NavigationItem(int imageId, String item)
    {
        this.imageId = imageId;
        this.item = item;
    }
}

