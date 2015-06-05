package com.astuetz.viewpager.extensions.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;


public class PostCardFragment extends Fragment
{

    TextView welcomeMessage;
    EditText titleEdit, authorEdit, localityEdit, descriptionEdit, priceEdit;
    Spinner deptSpin, contactSpin;
    FloatLabeledEditText phone;
    ImageView phoneIcon;

    CircularProgressButton uploadButton;

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
        welcomeMessage = (TextView) rootView.findViewById(R.id.welcome);
        titleEdit = (EditText) rootView.findViewById(R.id.title);
        authorEdit = (EditText) rootView.findViewById(R.id.author);
        descriptionEdit = (EditText) rootView.findViewById(R.id.description);
        localityEdit = (EditText) rootView.findViewById(R.id.locality);
        priceEdit = (EditText) rootView.findViewById(R.id.price);
        deptSpin = (Spinner) rootView.findViewById(R.id.dept_spinner);
        contactSpin = (Spinner) rootView.findViewById(R.id.cont_spinner);
        phoneIcon = (ImageView) rootView.findViewById(R.id.phone_icon);
        phone = (FloatLabeledEditText) rootView.findViewById(R.id.phone);
        phone.setVisibility(View.INVISIBLE);
        ViewCompat.setElevation(rootView, 50);

        uploadButton = (CircularProgressButton) rootView.findViewById(R.id.circularButton1);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        String loggedUser = ParseUser.getCurrentUser().getString("Name");

           welcomeMessage.setText("Welcome " + loggedUser + "!" );

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
                if (position == 0)
                {
                    phone.setVisibility(View.INVISIBLE);
                }
                if (position == 1)
                {
                    phone.setVisibility(View.VISIBLE);
                    phoneIcon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        uploadButton.setIndeterminateProgressMode(true);
        uploadButton.setBackgroundColor(getActivity().getResources().getColor(R.color.accentColor));
        uploadButton.setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v)
                                            {
                                                String title = titleEdit.getText().toString();
                                                String author = authorEdit.getText().toString();
                                                String location = localityEdit.getText().toString();
                                                String descp = descriptionEdit.getText().toString();
                                                int price = Integer.parseInt(priceEdit.getText().toString());
                                                String dept = deptSpin.getSelectedItem().toString();


                                                if (uploadButton.getProgress() == 0)
                                                {
                                                    ParseObject book = new ParseObject("Posted");
                                                    book.put("Title",title);
                                                    book.put("Author",author);
                                                    book.put("Place",location);
                                                    book.put("Description",descp);
                                                    book.put("Price",price);
                                                    book.put("Department",dept);
                                                    book.saveInBackground();

                                                    uploadButton.setProgress(100);  //Upload complete state
                                                }


                                                else

                                                {
                                                    uploadButton.setProgress(-1); //Error state
                                                }
                                            }
                                        }

        );
    }


}