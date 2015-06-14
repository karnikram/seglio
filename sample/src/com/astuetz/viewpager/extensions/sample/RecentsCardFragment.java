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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class RecentsCardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    ListView  recentsList;

    private SwipeRefreshLayout refreshBooks;

    ArrayList<HashMap<String,String>> items = new ArrayList<>();

    // Handler handler;


    public static RecentsCardFragment newInstance() {
        RecentsCardFragment f = new RecentsCardFragment();
            return f;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recents_card,container,false);
        ViewCompat.setElevation(rootView,50);
        refreshBooks = (SwipeRefreshLayout) rootView.findViewById(R.id.container);
        refreshBooks.setOnRefreshListener(this);
        recentsList = (ListView)rootView.findViewById(R.id.recents_list);


        recentsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (recentsList == null || recentsList.getChildCount() == 0) ?
                                0 : recentsList.getChildAt(0).getTop();
                refreshBooks.setEnabled(firstVisibleItem == 0 &&
                        topRowVerticalPosition >= 0);
            }
        });

       /* refreshBooks.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
*/
     //   getBooks();



        return rootView;
    }



    private ArrayList<HashMap<String,String>> getBooks(){

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Posted");
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {


            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {


                if (e == null){

                    Log.w("Parse","Inside getbooks()");
                    for (ParseObject book : parseObjects) {

                        HashMap<String, String> test = new HashMap<>();

                        String dept = book.getString("Department");
                        String title = book.getString("Title");
                        String author = book.getString("Author");
                        Number price_num = book.getNumber("Price");
                        String price = String.valueOf(price_num);
                        String place = book.getString("Place");
                        String desp = book.getString("Description");

                        test.put("dept", dept);
                        test.put("title", title);
                        test.put("author", author);
                        test.put("price", price);
                        test.put("place", place);
                        test.put("description", desp);

                        items.add(test);
                        RecentsAdapter  adapter = new RecentsAdapter(getActivity().getApplicationContext(), items);
                        recentsList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();





                    }


                } else {

                    Log.d("Books", "Error: " + e.getMessage());

                }

            }

        });


       return items;
    }


    @Override
    public void onRefresh(){

        refreshBooks.setRefreshing(true);
        updateBooks();
        //handler.post(refreshing);



}

    private void updateBooks(){

        Toast.makeText(getActivity(),"List updated..!", Toast.LENGTH_SHORT).show();

        items.clear();
        items.addAll(getBooks());



      refreshBooks.setRefreshing(false);
    }



   /* private final Runnable refreshing = new Runnable(){
        public void run(){
            try {
                if(refreshBooks.isRefreshing()){
                    // RE-Run after 1 Second
                    handler.postDelayed(this, 2000);
                }else{
                    // Stop the animation once we are done fetching data.
                    refreshBooks.setRefreshing(false);
                    updateBooks();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Please wait! Fetching Recently posted books...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);

        progress.show();

        Log.w("Check","Inside onCreated()");
        getBooks();

        progress.dismiss();

        //  RecentsAdapter  adapter = new RecentsAdapter(getActivity().getApplicationContext(), items);
        // recentsList.setAdapter(adapter);




    }



}