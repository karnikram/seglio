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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ListView;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RecentsCardFragment extends Fragment
{

    ListView recentsList;

    /*

    array list of hashmaps that's going to hold all the data
    fetch the data from parse and load it onto this

    Each hashmap represents one post.

    */

    ArrayList<HashMap<String,String>> items = new ArrayList<>();







    //sample hashmaps
    HashMap<String,String> test1 = new HashMap<>();
    HashMap<String,String> test2 = new HashMap<>();



    public static RecentsCardFragment newInstance() {
        RecentsCardFragment f = new RecentsCardFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recents_card,container,false);
        ViewCompat.setElevation(rootView,50);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        recentsList = (ListView)getActivity().findViewById(R.id.recents_list);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posted");
        query.orderByDescending("createdAt");

          query.findInBackground(new FindCallback<ParseObject>() {
              @Override
              public void done(List<ParseObject> parseObjects, ParseException e) {

                  if (e == null) {
                      for (ParseObject book : parseObjects) {

                          HashMap<String, String> test = new HashMap<>();

                          String dept = book.getString("Department");
                          String title = book.getString("Title");
                          String author = book.getString("Author");
                          Number price_num= book.getNumber("Price");
                          String price = String.valueOf(price_num);
                          String place = book.getString("Place");
                          String desp = book.getString("Description");

                          test.put("dept", dept);
                          test.put("title", title);
                          test.put("author", author);
                          test.put("price",price);
                          test.put("place", place);
                          test.put("description", desp);

                          items.add(test);




                          }

                  } else {
                      Log.d("score", "Error: " + e.getMessage());

                  }


                  RecentsAdapter adapter = new RecentsAdapter(getActivity().getApplicationContext(), items);
                  recentsList.setAdapter(adapter);





              }
    });
}}