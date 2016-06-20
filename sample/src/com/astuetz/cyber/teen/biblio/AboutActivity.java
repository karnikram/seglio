package com.astuetz.cyber.teen.biblio;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andexert.library.RippleView;
import com.karnix.cyberteen.biblio.R;

public class AboutActivity extends Fragment
{
    TextView title,tc,sp, appname, fb4;
    ImageView fb1,fb2,fb3;

//    RippleView rippleLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about, container, false);
        title = (TextView) getActivity().findViewById(R.id.tool_title);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/TitleFont.otf"));

        fb1 = (ImageView) v.findViewById(R.id.fb1);
        fb2 = (ImageView)v.findViewById(R.id.fb2);
        fb3 = (ImageView) v.findViewById(R.id.fb3);

        fb4 = (TextView) v.findViewById(R.id.fb4);

        tc = (TextView) v.findViewById(R.id.tc);
        sp = (TextView) v.findViewById(R.id.sp);
        appname = (TextView) v.findViewById(R.id.about_2);
        appname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/TitleFont.otf"));

        fb1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/678829498")));
                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.facebook.com/karnik28")));
                }
            }
        });

        fb2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/1260756861")));
                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.facebook.com/Shankar.Sridhar.95")));
                }
            }
        });

        fb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/1643122802")));
                }
                catch(Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://m.facebook.com/tvphere")));
                }
            }
        });

        fb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/390781484458442")));
                }
                catch(Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://m.facebook.com/seglio15")));
                }
            }
        });

//        rippleLogin = (RippleView) findViewById(R.id.rippleUser);
//
//        rippleLogin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener()
//        {
//            @Override
//            public void onComplete(RippleView rippleView)
//            {
//                startActivity(new Intent(AboutActivity.this, AccountFragment.class));
//            }
//        });


        tc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showTC();
            }
        });

        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showSP();
            }
        });

return  v;

    }
    void showTC()
    {
        new MaterialDialog.Builder(getActivity())
                .title("Terms & Conditions")
                .content(R.string.tc)
                .positiveText("Dismiss")
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    void showSP()
    {
        new MaterialDialog.Builder(getActivity())
                .title("Safety Guidelines")
                .content(R.string.sp)
                .contentGravity(GravityEnum.CENTER)
                .positiveText("Dismiss")
                .show();
    }
}
