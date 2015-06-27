package com.astuetz.cyber.teen.biblio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andexert.library.RippleView;
import com.astuetz.PagerSlidingTabStrip;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.InterstitialAd;
import com.karnix.cyberteen.biblio.R;
import com.parse.Parse;
import com.parse.ParseObject;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends ActionBarActivity
{
    private Toolbar toolbar;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private TextView title;
    private ImageView iconLogin;


    RippleView rippleLogin, rippleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Biblio.userName = getSharedPreferences("pref", 0).getString("name", null);
        Biblio.userEmail = getSharedPreferences("pref", 0).getString("email", null);

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

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        rippleLogin = (RippleView) findViewById(R.id.rippleUser);
        rippleInfo = (RippleView) findViewById(R.id.rippleInfo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(1);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title = (TextView) findViewById(R.id.tool_title);
        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/TitleFont.otf"));

        rippleLogin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener()
        {
            @Override
            public void onComplete(RippleView rippleView)
            {
                startActivity(new Intent(MainActivity.this, AccountActivity.class));


            }
        });

        rippleInfo.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener()
        {
            @Override
            public void onComplete(RippleView rippleView)
            {

                startActivity(new Intent(MainActivity.this, AboutActivity.class));

            }
        });
    }

    public class MyPagerAdapter extends FragmentPagerAdapter
    {

        private final String[] TITLES = {"SEARCH", "RECENT", "POST"};

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return TITLES[position];
        }

        @Override
        public int getCount()
        {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment temp = null;
            switch (position)
            {
                case 0:
                    temp = SearchFragment.newInstance();
                    break;

                case 1:
                    temp = RecentCardFragment.newInstance();
                    break;

                case 2:
                    temp = PostCardFragment.newInstance();
                    break;
            }
            return temp;
        }
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
        super.onStop();
        AppEventsLogger.deactivateApp(this);
    }

}