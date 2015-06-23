package com.astuetz.cyber.teen.biblio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.karnix.cyberteen.biblio.R;

import java.util.HashMap;

import at.markushi.ui.CircleButton;

public class BookActivity extends Activity
{
    HashMap<String,String> book;
    TextView title, author, dept, description, oprice, price, user, locality;
    CircleButton call, email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        title = (TextView) findViewById(R.id.tool_title);
        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/TitleFont.otf"));

        book = (HashMap<String,String>)getIntent().getSerializableExtra("book");

        title = (TextView) findViewById(R.id.btitle);
        title.setText(book.get("title"));

        author = (TextView)findViewById(R.id.bauthor);
        author.setText(book.get("author"));

        description = (TextView) findViewById(R.id.bdescription);
        description.setText(book.get("description"));

        dept = (TextView) findViewById(R.id.bdept);
        dept.setText(book.get("dept"));

        oprice = (TextView) findViewById(R.id.boprice);
        oprice.setText(book.get("oprice"));

        price = (TextView) findViewById(R.id.bprice);
        price.setText(book.get("price"));

        user = (TextView) findViewById(R.id.busername);
        user.setText(book.get("username"));

        locality = (TextView) findViewById(R.id.blocality);
        locality.setText(book.get("locality"));

        call = (CircleButton) findViewById(R.id.bcall);
        if(book.get("phone")!=null)
        {
            call.setVisibility(View.VISIBLE);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + book.get("phone").trim())));
                }
            });
        }

        email = (CircleButton)findViewById(R.id.bemail);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:"));
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{book.get("useremail").trim()});
                email.putExtra(Intent.EXTRA_SUBJECT, "Your Biblio Post");
                email.putExtra(Intent.EXTRA_TEXT, "Hey, I came across your post on Biblio about the " + book.get("title") + " book that you are selling. " +
                        "I'm interested in it and want to talk to you about it.");
                startActivity(Intent.createChooser(email, "Send your email using.."));
            }
        });

    }
}
