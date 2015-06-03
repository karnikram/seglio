
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
import android.widget.Spinner;
import android.widget.TextView;

import com.wrapp.floatlabelededittext.FloatLabeledEditText;


public class PostCardFragment extends Fragment
{

    TextView welcomeMessage;
    EditText title,author,locality,description;
    Spinner dept,contact;
    FloatLabeledEditText phone;

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
        author = (EditText)rootView.findViewById(R.id.author);
        dept = (Spinner) rootView.findViewById(R.id.dept_spinner);
        contact = (Spinner) rootView.findViewById(R.id.cont_spinner);
        phone = (FloatLabeledEditText) rootView.findViewById(R.id.phone);
        phone.setVisibility(View.INVISIBLE);
        ViewCompat.setElevation(rootView, 50);
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
                R.array.contact,R.layout.spinner_item);
        contact.setAdapter(contAdapter);

        contact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 0)
                {
                    phone.setVisibility(View.INVISIBLE);
                }
                if(position == 1)
                {
                    phone.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
}