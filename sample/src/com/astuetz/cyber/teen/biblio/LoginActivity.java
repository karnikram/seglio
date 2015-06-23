package com.astuetz.cyber.teen.biblio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.karnix.cyberteen.biblio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient googleApiClient;
    private boolean isResolving = false;
    private boolean shouldResolve = false;
    Button gloginButton;

    LoginButton fbloginButton;
    CallbackManager callbackManager;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        title = (TextView) findViewById(R.id.login_title);
        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/TitleFont.otf"));

        gloginButton = (Button) findViewById(R.id.google_login);
        gloginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                /*User cllicked the sign in button , begin the process
                    and automatically try to resolve any errors*/

                shouldResolve = true;
                googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                        .addConnectionCallbacks(LoginActivity.this)
                        .addOnConnectionFailedListener(LoginActivity.this)
                        .addApi(Plus.API, Plus.PlusOptions.builder().build())
                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .build();

                googleApiClient.connect();
            }
        });

        fbloginButton = (LoginButton) findViewById(R.id.facebook_login);
        fbloginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        fbloginButton.setReadPermissions(Arrays.asList("public_profile, email"));
        callbackManager = CallbackManager.Factory.create();
        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback()
                        {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse)
                            {
                                try
                                {
                                    SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                                    editor.putString("name", jsonObject.getString("name"));
                                    editor.putString("email", jsonObject.getString("email"));
                                    editor.commit();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onError(FacebookException e)
            {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            if (resultCode != RESULT_OK)
            {
                shouldResolve = false;
            }

            isResolving = false;
            googleApiClient.connect();
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnected(Bundle bundle)
    {

        shouldResolve = false;
        //signed in
        SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
        editor.putString("name", Plus.PeopleApi.getCurrentPerson(googleApiClient).getDisplayName());
        editor.putString("email", Plus.AccountApi.getAccountName(googleApiClient));
        editor.putBoolean("google", true);
        editor.commit();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (!isResolving && shouldResolve)
        {
            if (connectionResult.hasResolution())
            {
                try
                {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    isResolving = true;
                }
                catch (IntentSender.SendIntentException e)
                {
                    e.printStackTrace();
                    isResolving = false;
                    googleApiClient.connect();
                }
            }
        }
    }


    public void signOut()
    {
        LoginManager.getInstance().logOut();
    }


}
