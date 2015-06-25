package com.astuetz.cyber.teen.biblio;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Biblio extends Application
{
    static String userEmail, userName;
    @Override
    public void onCreate()
    {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
        printHashkey();
    }

    public void printHashkey()
    {
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.karnix.cyberteen.biblio",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
}
