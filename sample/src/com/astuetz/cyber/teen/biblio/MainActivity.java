/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.astuetz.cyber.teen.biblio;

import android.content.Intent;
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

    InterstitialAd mIadd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Biblio.userName = getSharedPreferences("pref",0).getString("name",null);
//        Biblio.userEmail = getSharedPreferences("pref",0).getString("email",null);

        Biblio.userEmail = "karnikram@gmail.com";
        Biblio.userName= "Karnik";

        if(Biblio.userName == null || Biblio.userEmail == null)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        else
        {
            Toast.makeText(this,"Howdy, " + Biblio.userName +"!",Toast.LENGTH_LONG).show();
        }



        Parse.initialize(this,getResources().getString(R.string.app_id),getResources().getString(R.string.client_key));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

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

        iconLogin = (ImageView) findViewById(R.id.action_login);
        iconLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
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
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
}