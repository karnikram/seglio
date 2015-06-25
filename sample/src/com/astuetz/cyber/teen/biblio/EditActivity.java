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
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.karnix.cyberteen.biblio.R;

import java.util.HashMap;

import at.markushi.ui.CircleButton;

public class EditActivity extends Activity
{

    HashMap<String,String> book = new HashMap<String,String>();

    EditText titleEdit, authorEdit, localityEdit, descriptionEdit, priceEdit, originalPriceEdit, phoneEdit;
    Spinner deptSpin, contactSpin;
    ImageView tablet;

    CircleButton edit,sold;
    int flag = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        book = (HashMap<String,String>)getIntent().getSerializableExtra("book");

        sold = (CircleButton) findViewById(R.id.bsold);
        edit = (CircleButton)findViewById(R.id.bedit);


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

        if(!(book.get("phone").equals("")))
        {
            contactSpin.setSelection(1);
            phoneEdit.setText(book.get("phone"));
        }

        else
        {
            contactSpin.setSelection(0);
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
            }
            if (position == 1)
            {
                phoneEdit.setVisibility(View.VISIBLE);
                tablet.setVisibility(View.VISIBLE);
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
                if (flag % 2 == 0)
                {
                    Toast.makeText(getApplicationContext(), "Marked as sold!", Toast.LENGTH_LONG).show();
                    finish();
//                    sold.setImageResource(R.drawable.sell);
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Marked to sell!", Toast.LENGTH_SHORT).show();
                    finish();
//                    sold.setImageResource(R.drawable.sold);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(),"Changes pushed!",Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}