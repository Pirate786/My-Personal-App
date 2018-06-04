package com.example.defaultuser0.myapplication.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.defaultuser0.myapplication.utils.ObscuredSharedPreferences;
import com.example.defaultuser0.myapplication.utils.Utilities;


public class BaseFragment extends Fragment {

    public ObscuredSharedPreferences getSharedPreferencesHelper() {

        if (mSharedPreferencesHelper != null) {
            return mSharedPreferencesHelper;
        } else {
            setSharedPreferencesHelper( new ObscuredSharedPreferences(getContext(), getContext().getSharedPreferences("My_Application", Context.MODE_PRIVATE)));
            return mSharedPreferencesHelper;
        }
    }

    public void setSharedPreferencesHelper(ObscuredSharedPreferences mSharedPreferencesHelper) {
        this.mSharedPreferencesHelper = mSharedPreferencesHelper;
    }

    ObscuredSharedPreferences mSharedPreferencesHelper;

    public Utilities getUtilities() {

        if (mUtilities != null) {
            return mUtilities;
        } else {
            setUtilities(new Utilities(getActivity()));
            return mUtilities;
        }
    }
    protected void hideKeyboard() {
        // Check if no view has focus:
        try {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUtilities(Utilities mUtilities) {
        this.mUtilities = mUtilities;
    }

    Utilities mUtilities;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            if (activity != null && activity instanceof BaseActivity) {
                ObscuredSharedPreferences mSharedPreferencesHelper = (((BaseActivity) activity)).getSharedPreferencesHelper();
                if (mSharedPreferencesHelper != null) {
                    setSharedPreferencesHelper(mSharedPreferencesHelper);
                }

                Utilities mUtilities = (((BaseActivity) activity)).getUtilities();

                if (mUtilities != null) {
                    setUtilities(mUtilities);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    protected void viewKeyboard() {
        // Check if no view has focus:
        try {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
