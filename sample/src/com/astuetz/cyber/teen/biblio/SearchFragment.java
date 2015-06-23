package com.astuetz.cyber.teen.biblio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.karnix.cyberteen.biblio.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment
{

    ArrayList<HashMap<String, String>> booksResults = new ArrayList<>();

    SearchView searchView;
    ListView searchResults;
    Spinner deptSearch;
    TextView or;
    boolean dep;

    ProgressBar progressBar;

    public static SearchFragment newInstance()
    {
        SearchFragment f = new SearchFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.search_card, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        searchView = (SearchView) rootView.findViewById(R.id.search);
        searchView.setQueryHint("Search by keyword");

        deptSearch = (Spinner) rootView.findViewById(R.id.dept_search);
        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.departments, R.layout.spinner_search);
        deptSearch.setAdapter(deptAdapter);

        or = (TextView) rootView.findViewById(R.id.or);

        searchResults = (ListView) rootView.findViewById(R.id.resultslist);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (newText.length() > 3)
                {
                    searchResults.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    deptSearch.setVisibility(View.INVISIBLE);
                    or.setVisibility(View.INVISIBLE);
                    dep = false;
                    new SearchAsyncTask().execute(newText);
                }

                else
                {
                   booksResults.clear();
                    if(newText.length()==0)
                    {deptSearch.setVisibility(View.VISIBLE);
                    or.setVisibility(View.VISIBLE);}
                }
                return false;
            }
        });

        deptSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) deptSearch.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, 0); // You can't remove a rule
                deptSearch.setLayoutParams(params);

                if (position != 0)
                {
                    searchResults.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.INVISIBLE);
                    or.setVisibility(View.INVISIBLE);
                    dep = true;
                    new SearchAsyncTask().execute(getResources().getStringArray(R.array.departments)[position]);
                }

                else
                {
                    or.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    params.addRule(RelativeLayout.BELOW, R.id.or);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        return rootView;
    }

    public class SearchAsyncTask extends AsyncTask<String, Void, String>
    {
        String userQuery;

        @Override
        protected String doInBackground(String... params)
        {
            userQuery = params[0];
            booksResults.clear();
        /*
        * Get the parse objects based on the query entered.
        */

             ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TestBooks");

            if (dep==false)query.whereContains("Title",userQuery);
             else
             query.whereContains("dept",userQuery);

            try
            {
                    Log.w("Parse", "Inside getbooks()");
                  List<ParseObject> queryResult = query.find();
                    for (ParseObject book : queryResult) {

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

                        booksResults.add(test);


                    }


                } catch(ParseException e) {

                    Log.d("Books", "Error: " + e.getMessage());

                }

            return userQuery;
            }







        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            searchResults.setAdapter(new BooksAdapter(getActivity(), booksResults));
            searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                                 {
                                                     @Override
                                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                                     {
                                                         Intent book = new Intent(getActivity(), BookActivity.class);
                                                         book.putExtra("book", booksResults.get(position));
                                                         startActivity(book);
                                                     }
                                                 });

                    progressBar.setVisibility(View.INVISIBLE);
        }
    }
}

