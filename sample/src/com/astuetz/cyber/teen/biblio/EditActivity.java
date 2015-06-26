package com.astuetz.cyber.teen.biblio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.dd.CircularProgressButton;
import com.karnix.cyberteen.biblio.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

import at.markushi.ui.CircleButton;

public class EditActivity extends Activity
{

    HashMap<String, String> book = new HashMap<String, String>();

    EditText titleEdit, authorEdit, localityEdit, descriptionEdit, priceEdit, originalPriceEdit, phoneEdit;
    Spinner deptSpin, contactSpin;
    ImageView tablet;

    boolean isPhone;

    CircleButton edit, sold;

    RippleView rippleInfo;

    String title, author, locality, description, price, originalPrice, phone, objId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        book = (HashMap<String, String>) getIntent().getSerializableExtra("book");

        sold = (CircleButton) findViewById(R.id.bsold);
        edit = (CircleButton) findViewById(R.id.bedit);


        titleEdit = (EditText) findViewById(R.id.title);
        authorEdit = (EditText) findViewById(R.id.author);
        descriptionEdit = (EditText) findViewById(R.id.description);
        localityEdit = (EditText) findViewById(R.id.locality);
        priceEdit = (EditText) findViewById(R.id.price);
        originalPriceEdit = (EditText) findViewById(R.id.original_price);
        phoneEdit = (EditText) findViewById(R.id.contact_no);
        deptSpin = (Spinner) findViewById(R.id.dept_spinner);
        contactSpin = (Spinner) findViewById(R.id.cont_spinner);
        tablet = (ImageView) findViewById(R.id.tablet);


        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.departments, R.layout.spinner_item);
        deptSpin.setAdapter(deptAdapter);

        ArrayAdapter<CharSequence> contAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.contact, R.layout.spinner_item);
        contactSpin.setAdapter(contAdapter);

        titleEdit.setText(book.get("title"));
        authorEdit.setText(book.get("author"));
        descriptionEdit.setText(book.get("description"));
        deptSpin.setSelection(deptAdapter.getPosition(book.get("dept")));
        priceEdit.setText(book.get("price"));
        originalPriceEdit.setText(book.get("oprice"));
        localityEdit.setText(book.get("place"));
        objId = book.get("objectId");

        if (!(book.get("phone").equals("")))
        {
            contactSpin.setSelection(1);
            phoneEdit.setText(book.get("phone"));
        }
        else
        {
            contactSpin.setSelection(0);
        }

        if (book.get("status").equals("sold"))
        {
            sold.setImageResource(R.drawable.sell);
            sold.setColor(getResources().getColor(R.color.accentColor));
        }

        contactSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    phoneEdit.setVisibility(View.INVISIBLE);
                    tablet.setVisibility(View.INVISIBLE);
                    isPhone = false;
                }
                if (position == 1)
                {
                    phoneEdit.setVisibility(View.VISIBLE);
                    tablet.setVisibility(View.VISIBLE);
                    isPhone = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        sold.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (book.get("status").equals("sell"))
                {
                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Posted");
                    query.whereEqualTo("objectId", objId);
                    query.findInBackground(new FindCallback<ParseObject>()
                    {


                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e)
                        {

                            if (e == null)
                            {
                                for (ParseObject book : parseObjects)
                                {
                                    book.put("status", "sold");
                                    book.saveInBackground();
                                    Toast.makeText(getApplicationContext(), "Marked as sold!", Toast.LENGTH_LONG).show();
                                    sold.setImageResource(R.drawable.sell);
                                    sold.setColor(getResources().getColor(R.color.accentColor));
                                    setResult(RESULT_OK, null);
                                    finish();

                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Connect to the internet!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

                else
                {

                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Posted");
                    query.whereEqualTo("objectId", objId);
                    query.findInBackground(new FindCallback<ParseObject>()
                    {


                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e)
                        {

                            if (e == null)
                            {
                                for (ParseObject book : parseObjects)
                                {
                                    book.put("status", "sell");
                                    book.saveInBackground();
                                    Toast.makeText(getApplicationContext(), "Marked to sell!", Toast.LENGTH_SHORT).show();
                                    sold.setImageResource(R.drawable.sold);
                                    sold.setColor(getResources().getColor(R.color.soldColor));
                                    setResult(RESULT_OK, null);
                                    finish();

                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Connect to the internet!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });


                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                title = titleEdit.getText().toString();
                final String lowTitle = title.toLowerCase();
                author = authorEdit.getText().toString();
                final String lowAuthor = author.toLowerCase();
                description = descriptionEdit.getText().toString();
                locality = localityEdit.getText().toString();
                price = priceEdit.getText().toString();
                originalPrice = originalPriceEdit.getText().toString();
                phone = phoneEdit.getText().toString();

                if (title.isEmpty() || author.isEmpty() || description.isEmpty() || locality.isEmpty() || price.isEmpty() || originalPrice.isEmpty() || (isPhone && phone.isEmpty()))
                {
                    Toast.makeText(getApplicationContext(), "One or more fields are empty.", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Posted");
                    query.whereEqualTo("objectId", objId);
                    query.findInBackground(new FindCallback<ParseObject>()
                    {


                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e)
                        {

                            if (e == null)
                            {
                                for (ParseObject book : parseObjects)
                                {
                                    book.put("username", Biblio.userName);
                                    book.put("useremail", Biblio.userEmail);
                                    book.put("Title", title);
                                    book.put("Author", author);
                                    book.put("Description", description);
                                    book.put("Place", locality);
                                    book.put("Price", price);
                                    book.put("oprice", originalPrice);
                                    book.put("dept", deptSpin.getSelectedItem().toString());
                                    book.put("phone", phone);
                                    book.put("title", lowTitle);
                                    book.put("author", lowAuthor);
                                    book.put("status","sell");
                                    book.saveInBackground();

                                    Toast.makeText(getApplicationContext(), "Changes pushed!", Toast.LENGTH_LONG).show();
                                    finish();


                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Connect to the internet!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        rippleInfo = (RippleView) findViewById(R.id.rippleInfo);


        rippleInfo.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener()
        {
            @Override
            public void onComplete(RippleView rippleView)
            {

                startActivity(new Intent(EditActivity.this, AboutActivity.class));
            }
        });
    }
}




