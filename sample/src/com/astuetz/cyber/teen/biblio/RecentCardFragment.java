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

    private SwipeRefreshLayout refreshBooks;

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
                        bookItem.put("username",userName);
                        bookItem.put("useremail",userEmail);
                        bookItem.put("locality", place);
                        bookItem.put("description", desp);
                        bookItem.put("phone", phone);
                        bookItem.put("oprice", op);
                        bookItem.put("status",status);

                        books.add(bookItem);
                        BooksAdapter adapter = new BooksAdapter(getActivity().getApplicationContext(), books);
                        recentsList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);

        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Fetching for recently posted books..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);

        progress.show();

        getBooks();

        progress.dismiss();

        BooksAdapter adapter = new BooksAdapter(getActivity().getApplicationContext(), books);
        recentsList.setAdapter(adapter);
        recentsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
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