package com.astuetz.cyber.teen.biblio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;

public class SearchFragment extends Fragment
{

    ArrayList<HashMap<String, String>> booksResults;

    SearchView searchView;
    ListView searchResults;
    Spinner deptSearch;
    TextView or;

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
                    new SearchAsyncTask().execute(newText);
                }

                else
                {
                    deptSearch.setVisibility(View.VISIBLE);
                    or.setVisibility(View.VISIBLE);
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
        String query;

        @Override
        protected String doInBackground(String... params)
        {
            query = params[0];
        /*
        * Get the parse objects based on the query entered.
        */
            return query;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
//            searchResults.setAdapter(new BooksAdapter(getActivity(), booksResults));
//            searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener()
//                                                 {
//                                                     @Override
//                                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                                                     {
//                                                         Intent book = new Intent(getActivity(), BookActivity.class);
//                                                         book.putExtra("book", booksResults.get(position));
//                                                         startActivity(book);
//                                                     }
//                                                 });

                    progressBar.setVisibility(View.INVISIBLE);
        }
    }
}

