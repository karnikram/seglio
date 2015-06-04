
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
import com.wrapp.floatlabelededittext.FloatLabeledEditText;


public class PostCardFragment extends Fragment
{

    TextView welcomeMessage;
    EditText title, author, locality, description;
    Spinner dept, contact;
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
        title = (EditText) rootView.findViewById(R.id.title);
        author = (EditText) rootView.findViewById(R.id.author);
        dept = (Spinner) rootView.findViewById(R.id.dept_spinner);
        contact = (Spinner) rootView.findViewById(R.id.cont_spinner);
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
        welcomeMessage.setText("Welcome," + " " + "!");

        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.departments, R.layout.spinner_item);
        dept.setAdapter(deptAdapter);

        ArrayAdapter<CharSequence> contAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.contact, R.layout.spinner_item);
        contact.setAdapter(contAdapter);

        contact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
                if (uploadButton.getProgress() == 0)
                {
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