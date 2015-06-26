package com.astuetz.cyber.teen.biblio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

    TextView none, user, title;
    RippleView rippleInfo;

    ArrayList<HashMap<String, String>> booksResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        title = (TextView) findViewById(R.id.tool_title);
        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/TitleFont.otf"));

        progressBar = (ProgressBar) findViewById(R.id.aprogress);
        booksPosted = (ListView) findViewById(R.id.abooks);
        none = (TextView) findViewById(R.id.none);
        user = (TextView) findViewById(R.id.auser);

        user.setText(Biblio.userName);

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        new SearchAsyncTask().execute(Biblio.userEmail);


        if(booksResults.size()!=0)
        {
            booksPosted.setVisibility(View.VISIBLE);
            booksPosted.setAdapter(new BooksAdapter(this, booksResults));
            none.setVisibility(View.GONE);
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        signoutButton = (CircleButton) findViewById(R.id.blogout);
        signoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                Boolean isGoogle = getSharedPreferences("pref", Context.MODE_PRIVATE).getBoolean("google", false);
                if (isGoogle)
                {
                    if (googleApiClient.isConnected())
                    {
                        Plus.AccountApi.clearDefaultAccount(googleApiClient);
                        googleApiClient.disconnect();
                    }
                }

                else
                {
                    new LoginActivity().signOut();
                }
                Intent logIn = new Intent(AccountActivity.this, LoginActivity.class);
                logIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logIn);
            }
        });

        rippleInfo = (RippleView) findViewById(R.id.rippleInfo);

        rippleInfo.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener()
        {
            @Override
            public void onComplete(RippleView rippleView)
            {

                startActivity(new Intent(AccountActivity.this, AboutActivity.class));
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
        if (googleApiClient.isConnected())
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

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Posted");


            query.whereContains("useremail", userQuery);

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
                    String objId = book.getObjectId();
                    String status = book.getString("status");


                    test.put("dept", dept);
                    test.put("title", title);
                    test.put("author", author);
                    test.put("price", price);
                    test.put("place", place);
                    test.put("description", desp);
                    test.put("phone", phone);
                    test.put("oprice", oprice);
                    test.put("objectId",objId);
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
            booksPosted.setVisibility(View.VISIBLE);
            booksPosted.setAdapter(new BooksAdapter(getApplication().getApplicationContext(), booksResults));
            booksPosted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent edit = new Intent(AccountActivity.this,EditActivity.class);
                    edit.putExtra("book",booksResults.get(position));
                    startActivityForResult(edit, 1);

                }
            });

            progressBar.setVisibility(View.GONE);

            if(booksResults.size()==0)
            {
                none.setVisibility(View.VISIBLE);
            }
            else
            {
                none.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            startActivity(getIntent());
            finish();
        }
    }
}
