package com.astuetz.cyber.teen.biblio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.karnix.cyberteen.biblio.R;

public class EditActivity extends Activity
{

    EditText titleEdit, authorEdit, localityEdit, descriptionEdit, priceEdit, originalPriceEdit, phoneEdit;
    Spinner deptSpin, contactSpin;
    ImageView tablet;

    String title, author, locality, description, price, originalPrice, phone;
    boolean isPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

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

        titleEdit.setText("Testing");
    }
}