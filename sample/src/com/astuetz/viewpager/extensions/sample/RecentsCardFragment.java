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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


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

        //Sample data being fed into the arraylist

        test1.put("dept","Electronics and Communication Engineering");
        test1.put("title","Control Systems Engineering");
        test1.put("author","Nagoor Kani");
        test1.put("price", "200");
        test1.put("place","xcf");
        test1.put("description","dfdf");
        items.add(test1);


        test2.put("dept","Electronics and Communication Engineering");
        test2.put("title","Linear Integrated Circuits");
        test2.put("author","Roy Chaudhari");
        test2.put("price", "150");
        test2.put("place","xcv");
        test2.put("description","dfsdff");
        items.add(test2);


        RecentsAdapter adapter = new RecentsAdapter(getActivity().getApplicationContext(), items);
        recentsList.setAdapter(adapter);


    }
}