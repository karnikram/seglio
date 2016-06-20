package com.astuetz.cyber.teen.biblio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.karnix.cyberteen.biblio.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.markushi.ui.CircleButton;


public class AccountFragment extends Fragment
{
    GoogleApiClient googleApiClient;
    CircleButton signoutButton;

    ListView booksPosted;
    ProgressBar progressBar;

    TextView none, user, title;

    ArrayList<HashMap<String, String>> booksResults = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_fragment, container, false);

        progressBar = (ProgressBar) v.findViewById(R.id.aprogress);
        booksPosted = (ListView) v.findViewById(R.id.abooks);
        none = (TextView) v.findViewById(R.id.none);

//        AdView mAdview = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdview.loadAd(adRequest);

        new SearchAsyncTask().execute(Biblio.userEmail);


        if (booksResults.size() != 0) {
            booksPosted.setVisibility(View.VISIBLE);
            booksPosted.setAdapter(new BooksAdapter(getActivity(), booksResults));
            none.setVisibility(View.GONE);
        }

//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API, Plus.PlusOptions.builder().build())
//                .addScope(Plus.SCOPE_PLUS_PROFILE)
//                .build();
//
//        signoutButton = (CircleButton) v.findViewById(R.id.blogout);
//        signoutButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
//                editor.clear();
//                editor.commit();
//                Boolean isGoogle = getSharedPreferences("pref", Context.MODE_PRIVATE).getBoolean("google", false);
//                if (isGoogle)
//                {
//                    if (googleApiClient.isConnected())
//                    {
//                        Plus.AccountApi.clearDefaultAccount(googleApiClient);
//                        googleApiClient.disconnect();
//                    }
//                }
//
//                else
//                {
//                    new LoginActivity().signOut();
//                }
//                Intent logIn = new Intent(AccountFragment.this, LoginActivity.class);
//                logIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(logIn);
//            }
//        });

//        rippleInfo = (RippleView) findViewById(R.id.rippleInfo);
//
//        rippleInfo.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener()
//        {
//            @Override
//            public void onComplete(RippleView rippleView)
//            {
//
//                startActivity(new Intent(AccountFragment.this, AboutActivity.class));
//            }
//        });


//    }
//
//    @Override
//    public void onConnected(Bundle bundle)
//    {
//        Plus.PeopleApi.loadVisible(googleApiClient, null).setResultCallback(this);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i)
//    {
//        googleApiClient.connect();
//    }
//
//    @Override
//    public void onResult(People.LoadPeopleResult loadPeopleResult)
//    {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult)
//    {
//
//    }
//
//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//        googleApiClient.connect();
//    }
//
//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        if (googleApiClient.isConnected())
//        {
//            googleApiClient.disconnect();
//        }
//    }
        return v;
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
            booksPosted.setAdapter(new BooksAdapter(getActivity(), booksResults));
            booksPosted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    EditFragment fragment = new EditFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("book", booksResults.get(position));
                    fragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left, R.anim.exit_right,R.anim.enter_left, R.anim.exit_right).replace(R.id.main_container, fragment).addToBackStack(null).commit();

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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK)
//        {
//            startActivity(getIntent());
//            finish();
//        }
//    }
}
