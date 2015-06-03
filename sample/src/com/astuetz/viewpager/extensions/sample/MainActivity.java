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

package com.astuetz.viewpager.extensions.sample;

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

import com.astuetz.PagerSlidingTabStrip;
import com.parse.Parse;
import com.parse.ParseObject;


public class MainActivity extends ActionBarActivity {


    private Toolbar toolbar;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private TextView title;
    private Typeface titleFont;
    private ImageView iconLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "5EDgjGMNvj8Eb5CEjW9uPWB9wARKTSNIJwAN1v2K", "6LF6Mnm2t2H8DSiLWtPCwbBVHJw8PBR55BXHrmPq");

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        pager = (ViewPager)findViewById(R.id.pager);


        setSupportActionBar(toolbar);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(1);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titleFont = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/TitleFont.otf");
        title = (TextView)findViewById(R.id.title);
        title.setTypeface(titleFont);

        iconLogin = (ImageView)findViewById(R.id.action_login);
        iconLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        //for testing purpose
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("Project", "Biblio");
        testObject.saveInBackground();
    }


    public class MyPagerAdapter extends FragmentPagerAdapter
    {

        private final String[] TITLES = {"RECENTS","SEARCH","POST"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return SuperAwesomeCardFragment.newInstance(position);
        }
    }
}