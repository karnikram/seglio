package com.astuetz.cyber.teen.biblio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.karnix.cyberteen.biblio.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
 import com.parse.ParseQuery;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RecentCardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    ListView  recentsList;

    private SwipeRefreshLayout refreshBooks;

    ArrayList<HashMap<String,String>> books = new ArrayList<>();



    public static RecentCardFragment newInstance() {
        RecentCardFragment f = new RecentCardFragment();
            return f;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recent_card,container,false);
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
       // getBooks();



        return rootView;
    }



    private ArrayList<HashMap<String,String>> getBooks()
    {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("TestBooks");
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {


            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {


                if (e == null){

                    Log.w("Parse","Inside getbooks()");
                    for (ParseObject book : parseObjects) {

                        HashMap<String, String> test = new HashMap<>();

                        String dept = book.getString("dept");
                        String title = book.getString("Title");
                        String author = book.getString("Author");
                        Number price_num = book.getNumber("oprice");
                        String price = String.valueOf(price_num);
                        String place = book.getString("Place");
                        String desp = book.getString("Description");

                        test.put("dept", dept);
                        test.put("title", title);
                        test.put("author", author);
                        test.put("price", price);
                        test.put("place", place);
                        test.put("description", desp);

                        books.add(test);
                        BooksAdapter  adapter = new BooksAdapter(getActivity().getApplicationContext(), books);
                        recentsList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();





                    }


                } else {

                    Log.d("Books", "Error: " + e.getMessage());

                }

            }

        });


       return books;
    }


    @Override
    public void onRefresh(){

        refreshBooks.setRefreshing(true);
        updateBooks();
        //handler.post(refreshing);



}

    private void updateBooks(){

        Toast.makeText(getActivity(),"List updated..!", Toast.LENGTH_SHORT).show();

        books.clear();
        books.addAll(getBooks());



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

        Log.w("Check", "Inside onCreated()");
        getBooks();

        progress.dismiss();
//        HashMap<String,String> book = new HashMap<String,String>();
//        book.put("title", "Signals & Systems");
//        book.put("author", "Oppenheim, Willsky & Nawab");
//        book.put("dept", "ECE");
//        book.put("price", "200");
//        book.put("oprice", "400");
//        book.put("description", "Pretty much unused.");
//        book.put("locality","Mylapore");
//        book.put("phone","9940049947");
//        book.put("useremail",Biblio.userEmail);
//        book.put("username",Biblio.userName);
//
//        books.add(book);


        BooksAdapter adapter = new BooksAdapter(getActivity().getApplicationContext(), books);
        recentsList.setAdapter(adapter);
        recentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent book = new Intent(getActivity(), BookActivity.class);
                book.putExtra("book", books.get(position));
                startActivity(book);
            }
        });

    }



}