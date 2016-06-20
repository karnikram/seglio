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
import android.widget.Toast;

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

    SearchView searchView, authorSearch;
    ListView searchResults;
    Spinner deptSearch;
    TextView or, or_auth,noBooks;
    int flag;

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
        authorSearch = (SearchView) rootView.findViewById(R.id.authorsearch);
        authorSearch.setQueryHint("Search by Author");
        searchView.setQueryHint("Search by Title");

        deptSearch = (Spinner) rootView.findViewById(R.id.dept_search);
        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.departments, R.layout.spinner_search);
        deptSearch.setAdapter(deptAdapter);

        or = (TextView) rootView.findViewById(R.id.or);
        or_auth = (TextView) rootView.findViewById(R.id.orauth);
        noBooks = (TextView) rootView.findViewById(R.id.nobooks);

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
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) searchResults.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.search); // You can't remove a rule
                searchResults.setLayoutParams(params);

                noBooks.setVisibility(View.GONE);
                authorSearch.setVisibility(View.INVISIBLE);
                deptSearch.setVisibility(View.INVISIBLE);
                or.setVisibility(View.INVISIBLE);
                or_auth.setVisibility(View.INVISIBLE);


                RelativeLayout.LayoutParams sparams = (RelativeLayout.LayoutParams) searchView.getLayoutParams();
                sparams.setMargins(0,0,0,0);
                searchView.setLayoutParams(sparams);

                if (newText.length() > 3)
                {
                    searchResults.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    flag = 0;
                    new SearchAsyncTask().execute(newText);
                }

                else
                {
                    booksResults.clear();
                    if (newText.length() == 0)
                    {
                        authorSearch.setVisibility(View.VISIBLE);
                        deptSearch.setVisibility(View.VISIBLE);
                        or.setVisibility(View.VISIBLE);
                        or_auth.setVisibility(View.VISIBLE);
                        deptSearch.setEnabled(true);
                        searchResults.setVisibility(View.GONE);
                        params.addRule(RelativeLayout.BELOW, R.id.dept_search);
                        sparams.setMargins(0,55,0,0);
                    }
                }
                return false;
            }
        });

        authorSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) authorSearch.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, 0); // You can't remove a rule
                authorSearch.setLayoutParams(params);

                searchView.setVisibility(View.INVISIBLE);
                deptSearch.setVisibility(View.INVISIBLE);
                or.setVisibility(View.INVISIBLE);
                or_auth.setVisibility(View.INVISIBLE);

                noBooks.setVisibility(View.GONE);

                RelativeLayout.LayoutParams sparams = (RelativeLayout.LayoutParams) searchResults.getLayoutParams();
                sparams.addRule(RelativeLayout.BELOW, R.id.authorsearch); // You can't remove a rule
                searchResults.setLayoutParams(sparams);


                if (newText.length() > 3)
                {
                    searchResults.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    flag = 1;
                    new SearchAsyncTask().execute(newText);
                }

                else
                {
                    booksResults.clear();
                    if (newText.length() == 0)
                    {
                        searchView.setVisibility(View.VISIBLE);
                        deptSearch.setVisibility(View.VISIBLE);
                        or.setVisibility(View.VISIBLE);
                        deptSearch.setEnabled(true);
                        or_auth.setVisibility(View.VISIBLE);
                        searchResults.setVisibility(View.GONE);
                        params.addRule(RelativeLayout.BELOW, R.id.or);
                        sparams.addRule(RelativeLayout.BELOW,R.id.dept_search);
                    }
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
                noBooks.setVisibility(View.GONE);
                deptSearch.setLayoutParams(params);

                if (position != 0)
                {
                    searchResults.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.INVISIBLE);
                    authorSearch.setVisibility(View.INVISIBLE);
                    or.setVisibility(View.INVISIBLE);
                    or_auth.setVisibility(View.INVISIBLE);
                    flag = 2;
                    new SearchAsyncTask().execute(getResources().getStringArray(R.array.departments)[position]);
                }

                else
                {
                    or.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    authorSearch.setVisibility(View.VISIBLE);
                    or_auth.setVisibility(View.VISIBLE);
                    searchResults.setVisibility(View.GONE);
                    params.addRule(RelativeLayout.BELOW, R.id.orauth);
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

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Posted");

            if (flag == 0)
            {
                String lowTitle = userQuery.toLowerCase();
                query.whereContains("title", lowTitle);
            }
            else
                if (flag == 1)
                {
                    String lowAuthor = userQuery.toLowerCase();
                    query.whereContains("author", lowAuthor);
                }
                else

                    query.whereContains("dept", userQuery);


            try
            {
                Log.w("Parse", "Inside getbooks()");
                List<ParseObject> queryResult = query.find();
                for (ParseObject book : queryResult)
                {

                    HashMap<String, String> test = new HashMap<>();

                    String dept = book.getString("dept");
                    String title = book.getString("Title");
                    String author = book.getString("Author");
                    String price = book.getString("Price");
                    String place = book.getString("Place");
                    String desp = book.getString("Description");
                    String phone = book.getString("phone");
                    String oprice = book.getString("oprice");
                    String userName = book.getString("username");
                    String userEmail = book.getString("useremail");
                    String status = book.getString("status");


                    test.put("dept", dept);
                    test.put("title", title);
                    test.put("author", author);
                    test.put("price", price);
                    test.put("place", place);
                    test.put("description", desp);
                    test.put("phone", phone);
                    test.put("oprice", oprice);
                    test.put("username",userName);
                    test.put("useremail",userEmail);
                    test.put("status",status);

                    booksResults.add(test);
                }

            }
            catch (ParseException e)
            {
                Log.d("Books", "Error: " + e.getMessage());
            }

            return userQuery;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            searchResults.setAdapter(new BooksAdapter(getActivity().getApplicationContext(), booksResults));
            searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle args = new Bundle();
                    args.putSerializable("book", booksResults.get(position));
                    BookActivity fragment = new BookActivity();
                    fragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_left, R.anim.exit_right).replace(R.id.main_container, fragment).addToBackStack(null).commit();
                }
            });

            progressBar.setVisibility(View.INVISIBLE);
            if(booksResults.size()==0)
            {
                noBooks.setVisibility(View.VISIBLE);
            }
            else
                noBooks.setVisibility(View.GONE);
        }
    }
}

