package com.astuetz.cyber.teen.biblio;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.karnix.cyberteen.biblio.R;
import com.parse.ParseObject;


public class PostCardFragment extends Fragment
{

    EditText titleEdit, authorEdit, localityEdit, descriptionEdit, priceEdit, originalPriceEdit,phoneEdit;
    Spinner deptSpin, contactSpin;
    ImageView tablet;
    CircularProgressButton uploadButton;

    String title, author, locality, description, price, originalPrice, phone;
    TextView message;
    boolean isPhone;

    public static PostCardFragment newInstance()
    {
        PostCardFragment f = new PostCardFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.post_card, container, false);
        message = (TextView) rootView.findViewById(R.id.message);
        titleEdit = (EditText) rootView.findViewById(R.id.title);
        authorEdit = (EditText) rootView.findViewById(R.id.author);
        descriptionEdit = (EditText) rootView.findViewById(R.id.description);
        localityEdit = (EditText) rootView.findViewById(R.id.locality);
        priceEdit = (EditText) rootView.findViewById(R.id.price);
        originalPriceEdit = (EditText) rootView.findViewById(R.id.original_price);
        phoneEdit = (EditText)rootView.findViewById(R.id.contact_no);
        deptSpin = (Spinner) rootView.findViewById(R.id.dept_spinner);
        contactSpin = (Spinner) rootView.findViewById(R.id.cont_spinner);
        tablet = (ImageView)rootView.findViewById(R.id.tablet);
        uploadButton = (CircularProgressButton) rootView.findViewById(R.id.circularButton1);

        phoneEdit.setVisibility(View.INVISIBLE);
        tablet.setVisibility(View.INVISIBLE);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);
        message.setText("You are posting as " + Biblio.userName);

        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.departments, R.layout.spinner_item);
        deptSpin.setAdapter(deptAdapter);

        ArrayAdapter<CharSequence> contAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.contact, R.layout.spinner_item);
        contactSpin.setAdapter(contAdapter);

        contactSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(position == 0)
                {
                    phoneEdit.setVisibility(View.INVISIBLE);
                    tablet.setVisibility(View.INVISIBLE);
                    isPhone = false;
                }
                if(position == 1)
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

        uploadButton.setIndeterminateProgressMode(true);
        uploadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (uploadButton.getProgress() == 0)
                {
                    checkConnectionExecute();
                }
                else
                    if (uploadButton.getProgress() == 100 || uploadButton.getProgress() == -1)
                    {
                        uploadButton.setProgress(0);
                    }
            }
        });
    }


    private void checkConnectionExecute()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            title = titleEdit.getText().toString();
            String  lowTitle = title.toLowerCase();
            author = authorEdit.getText().toString();
            String lowAuthor = author.toLowerCase();
            description = descriptionEdit.getText().toString();
            locality = localityEdit.getText().toString();
            price = priceEdit.getText().toString();
            originalPrice = originalPriceEdit.getText().toString();
            phone = phoneEdit.getText().toString();

            if (title.isEmpty() || author.isEmpty() || description.isEmpty() || locality.isEmpty() || price.isEmpty() || originalPrice.isEmpty() || (isPhone && phone.isEmpty()))
            {
                Toast.makeText(getActivity(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
                uploadButton.setProgress(-1);
            }
            else
            {
                uploadButton.setProgress(50);
                ParseObject bookObject = new ParseObject("Posted");
                bookObject.put("username",Biblio.userName);
                bookObject.put("useremail",Biblio.userEmail);
                bookObject.put("Title", title);
                bookObject.put("Author", author);
                bookObject.put("Description", description);
                bookObject.put("Place", locality);
                bookObject.put("Price", price);
                bookObject.put("oprice", originalPrice);
                bookObject.put("dept", deptSpin.getSelectedItem().toString());
                bookObject.put("phone",phone);
                bookObject.put("title",lowTitle);
                bookObject.put("author",lowAuthor);
                bookObject.saveInBackground();


                Toast.makeText(getActivity(), "Book Posted!.", Toast.LENGTH_LONG).show();
                uploadButton.setProgress(100);
                titleEdit.setText("");
                authorEdit.setText("");
                localityEdit.setText("");
                descriptionEdit.setText("");
                priceEdit.setText("");
                originalPriceEdit.setText("");
                if(phoneEdit.getVisibility() == View.VISIBLE)
                {
                    phoneEdit.setText("");
                    phoneEdit.setVisibility(View.INVISIBLE);
                }
            }
        }

        else
        {
            Toast.makeText(getActivity(), "Connect to the internet!", Toast.LENGTH_LONG).show();
            uploadButton.setProgress(-1);
        }
    }
}