package com.ss_technology.mushinprojectandroidapp.Config;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class KeepMeLogin {

    Context context;
    private SharedPreferences mPreferences;
    private String sharedPrefFileName = "urData";

    public KeepMeLogin(Context context) {
        this.context = context;
        mPreferences=context.getSharedPreferences(sharedPrefFileName,MODE_PRIVATE);
    }

    public void setData(String obj)
    {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("obj",obj);
        preferencesEditor.apply();
    }

    public String getData()
    {
     return mPreferences.getString("obj","null");
    }

    public Boolean checkData()
    {
        if(mPreferences.contains("obj"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void Clear()
    {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear().commit();
    }
}
