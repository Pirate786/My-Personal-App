package com.example.defaultuser0.myapplication.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.defaultuser0.myapplication.utils.ObscuredSharedPreferences;
import com.example.defaultuser0.myapplication.utils.Utilities;


public class BaseActivity extends AppCompatActivity {

    public ObscuredSharedPreferences getSharedPreferencesHelper() {
        return mSharedPreferencesHelper;
    }

    public void setSharedPreferencesHelper(ObscuredSharedPreferences mSharedPreferencesHelper) {
        this.mSharedPreferencesHelper = mSharedPreferencesHelper;
    }

    public Utilities getUtilities() {

        if (mUtilities == null)
            mUtilities = new Utilities(this);

        return mUtilities;
    }

    public void setUtilities(Utilities mUtilities) {
        this.mUtilities = mUtilities;
    }

    Utilities mUtilities;

    ObscuredSharedPreferences mSharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedPreferencesHelper( new ObscuredSharedPreferences(this, this.getSharedPreferences("My_Application", Context.MODE_PRIVATE)));
        mUtilities = new Utilities(this);
        setUtilities(mUtilities);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void viewKeyboard() {
        // Check if no view has focus:
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }
}
