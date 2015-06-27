package com.astuetz.cyber.teen.biblio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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
    ListView recentsList;
    Button retry;
    ProgressBar progressBar;

    private SwipeRefreshLayout refreshBooks;

    private InterstitialAd interstitial;

    ArrayList<HashMap<String, String>> books = new ArrayList<>();

    public static RecentCardFragment newInstance()
    {
        RecentCardFragment f = new RecentCardFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.recent_card, container, false);
        refreshBooks = (SwipeRefreshLayout) rootView.findViewById(R.id.container);
        refreshBooks.setOnRefreshListener(this);
        recentsList = (ListView) rootView.findViewById(R.id.recents_list);

        retry = (Button) rootView.findViewById(R.id.retry);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        interstitial = new InterstitialAd((getActivity()));
        interstitial.setAdUnitId(getResources().getString(R.string.full_ad_unit_id));
        requestNewInterstitial();


        recentsList.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount)
            {
                int topRowVerticalPosition =
                        (recentsList == null || recentsList.getChildCount() == 0) ?
                                0 : recentsList.getChildAt(0).getTop();
                refreshBooks.setEnabled(firstVisibleItem == 0 &&
                        topRowVerticalPosition >= 0);
            }
        });

        return rootView;
    }


    private ArrayList<HashMap<String, String>> getBooks()
    {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Posted");
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>()
        {


            @Override
            public void done(List<ParseObject> parseObjects, ParseException e)
            {

                if (e == null)
                {
                    for (ParseObject book : parseObjects)
                    {
                        HashMap<String, String> bookItem = new HashMap<>();

                        String dept = book.getString("dept");
                        String title = book.getString("Title");
                        String author = book.getString("Author");
                        String price = book.getString("Price");
                        String place = book.getString("Place");
                        String desp = book.getString("Description");
                        String phone = book.getString("phone");
                        String oprice = book.getString("oprice");
                        String op = String.valueOf(oprice);
                        String userName = book.getString("username");
                        String userEmail = book.getString("useremail");
                        String status = book.getString("status");


                        bookItem.put("dept", dept);
                        bookItem.put("title", title);
                        bookItem.put("author", author);
                        bookItem.put("price", price);
                        bookItem.put("username", userName);
                        bookItem.put("useremail", userEmail);
                        bookItem.put("locality", place);
                        bookItem.put("description", desp);
                        bookItem.put("phone", phone);
                        bookItem.put("oprice", op);
                        bookItem.put("status", status);

                        books.add(bookItem);
                        BooksAdapter adapter = new BooksAdapter(getActivity().getApplicationContext(), books);
                        recentsList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                }
                else
                {
                    Log.d("Books", "Error: " + e.getMessage());
                }

            }

        });

        return books;
    }


    @Override
    public void onRefresh()
    {
        refreshBooks.setRefreshing(true);
        updateBooks();
    }

    private void updateBooks()
    {
        books.clear();
        books.addAll(getBooks());

        refreshBooks.setRefreshing(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void checkConnectionExecute()

    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            progressBar.setVisibility(View.VISIBLE);
            getBooks();
        }
        else
        {
            Toast.makeText(getActivity(), "Connect to the Internet!", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
            retry.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    retry.setVisibility(View.GONE);
                    checkConnectionExecute();
                }
            });
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);

        checkConnectionExecute();

        BooksAdapter adapter = new BooksAdapter(getActivity().getApplicationContext(), books);
        recentsList.setAdapter(adapter);
        recentsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Biblio.iad++;
                Log.w("RecentsCount", String.valueOf(Biblio.iad));

                if (Biblio.iad % 3 == 0)
                {
                    if (interstitial.isLoaded()) ;
                    interstitial.show();
                }

                else
                {
                    Intent book = new Intent(getActivity(), BookActivity.class);
                    book.putExtra("book", books.get(position));
                    startActivity(book);
                }

            }

        });


    }

    private void requestNewInterstitial()
    {
        AdRequest adRequest = new AdRequest.Builder()
                // .addTestDevice("57B298692E0EE4C277D1A2528A83D15B")
                .build();

        interstitial.loadAd(adRequest);
    }

}
