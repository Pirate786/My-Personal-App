package com.example.defaultuser0.myapplication.constants;

import android.content.Context;

import com.example.defaultuser0.myapplication.utils.ObscuredSharedPreferences;

public final class Constants {

    public static String ENDPOINT = "";
    public static String PIC_ENDPOINT = "";

    public static String getEndPoint(Context mContext) {
        return new ObscuredSharedPreferences(mContext, mContext.getSharedPreferences("My_Application", Context.MODE_PRIVATE)).getString("ip", ENDPOINT);
    }

    public static String getImageEndPoint(Context mContext) {
        return new ObscuredSharedPreferences(mContext, mContext.getSharedPreferences("My_Application", Context.MODE_PRIVATE)).getString("ip_image", PIC_ENDPOINT);
    }

    public static class Methods {
        public static final int SEARCH = 1;

    }

    public static class PreferenceKeys {
        public static final String USERNAME = "username";


    }


}