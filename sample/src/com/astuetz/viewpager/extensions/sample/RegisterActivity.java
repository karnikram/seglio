package com.astuetz.viewpager.extensions.sample;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterActivity extends ActionBarActivity
{
    private TextView title;
    private Typeface titleFont;
    private Toolbar toolbar;

    private EditText emailedit,passwordedit,confirmPasswordedit,nameedit;
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
        title = (TextView)findViewById(R.id.tool_title);
        title.setTypeface(titleFont);


        emailedit = (EditText)findViewById(R.id.email_register);
        passwordedit = (EditText)findViewById(R.id.password_register);
        confirmPasswordedit = (EditText)findViewById(R.id.password_confirm);
        nameedit = (EditText)findViewById(R.id.name);
        register = (Button)findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v)
            {
                String email = emailedit.getText().toString();
                String password = passwordedit.getText().toString();
                String confirmPassword = confirmPasswordedit.getText().toString();
                String name = nameedit.getText().toString();

               // to remove white spaces
                email = email.trim();
                password = password.trim();
                confirmPassword = confirmPassword.trim();
                name = name.trim();

               setSupportProgressBarIndeterminateVisibility(true);

                ParseUser newUser = new ParseUser();
                newUser.setUsername(email);
                newUser.setPassword(password);
                newUser.setEmail(email);
                newUser.put("Name",name);





                if(email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()||name.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("One or more fields are empty!!")
                            .setTitle("Error!!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                if (!(password.equals(confirmPassword))) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Enter the password again to confirm")
                                .setTitle("Password Error!!")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        confirmPasswordedit.requestFocus();

                }

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        setSupportProgressBarIndeterminateVisibility(false);
                        if(e==null)//If signup successfull
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Your account was successfully created!")
                                    .setTitle("Success!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();

                            /*Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            //show home activity
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);*/
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage(e.getMessage())
                                    .setTitle("Sign up Failed!!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });



            }
        });
    }
}
