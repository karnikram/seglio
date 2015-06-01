package com.astuetz.viewpager.extensions.sample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterActivity extends ActionBarActivity
{
    private TextView title;
    private Typeface titleFont;
    private Toolbar toolbar;

    private EditText email,password,confirmPassword,name;
    private Button register;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titleFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/TitleFont.otf");
        title = (TextView)findViewById(R.id.title);
        title.setTypeface(titleFont);


        email = (EditText)findViewById(R.id.email_register);
        password = (EditText)findViewById(R.id.password_register);
        confirmPassword = (EditText)findViewById(R.id.password_confirm);
        name = (EditText)findViewById(R.id.name);
        register = (Button)findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });
    }
}
