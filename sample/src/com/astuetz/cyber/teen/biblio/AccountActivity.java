package com.astuetz.cyber.teen.biblio;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.karnix.cyberteen.biblio.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.markushi.ui.CircleButton;


public class AccountActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        ResultCallback<People.LoadPeopleResult>,
        GoogleApiClient.OnConnectionFailedListener
{
    GoogleApiClient googleApiClient;
    CircleButton signoutButton;

    ListView booksPosted;
    ProgressBar progressBar;

    TextView none;

    ArrayList<HashMap<String,String>> booksResults = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        progressBar = (ProgressBar)findViewById(R.id.aprogress);
        booksPosted = (ListView) findViewById(R.id.abooks);
        none = (TextView) findViewById(R.id.none);

        new SearchAsyncTask().execute(Biblio.userEmail);

        if(booksResults.size()==0)
        {
            booksPosted.setVisibility(View.INVISIBLE);
            none.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            none.setLayoutParams(params);
        }

        else
        {
            booksPosted.setAdapter(new BooksAdapter(this,booksResults));
            none.setVisibility(View.INVISIBLE);
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        signoutButton = (CircleButton) findViewById(R.id.blogout);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                Boolean isGoogle = getSharedPreferences("pref",Context.MODE_PRIVATE).getBoolean("google", false);
                if(isGoogle)
                {
                    if(googleApiClient.isConnected())
                    {
                        Plus.AccountApi.clearDefaultAccount(googleApiClient);
                        googleApiClient.disconnect();
                    }
                }

                else
                {
                    new LoginActivity().signOut();
                }
                Intent logIn = new Intent(AccountActivity.this,LoginActivity.class);;
                logIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logIn);
            }
        });

    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Plus.PeopleApi.loadVisible(googleApiClient, null).setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        googleApiClient.connect();
    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(googleApiClient.isConnected())
        {
            googleApiClient.disconnect();
        }
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


                query.whereContains("useremail",userQuery);

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
           // BooksAdapter  adapter = new BooksAdapter(getApplicationContext(), booksResults);
           // booksPosted.setAdapter(adapter);
           booksPosted.setAdapter(new BooksAdapter(getApplication().getApplicationContext(), booksResults));
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
