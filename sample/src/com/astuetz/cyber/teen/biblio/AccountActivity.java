package com.astuetz.cyber.teen.biblio;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;

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

    ArrayList<HashMap<String,String>> booksResults;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        progressBar = (ProgressBar)findViewById(R.id.aprogress);
        booksPosted = (ListView) findViewById(R.id.abooks);
        none = (TextView) findViewById(R.id.none);

        new SearchAsyncTask().execute(Biblio.userName);

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
//            searchResults.setAdapter(new BookAdapter(getActivity(), booksResults));
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
