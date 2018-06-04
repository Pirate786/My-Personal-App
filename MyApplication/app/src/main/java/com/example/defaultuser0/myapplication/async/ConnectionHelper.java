package com.example.defaultuser0.myapplication.async;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.defaultuser0.myapplication.constants.Constants;
import com.example.defaultuser0.myapplication.listener.OnRequestComplete;
import com.example.defaultuser0.myapplication.utils.API;
import com.example.defaultuser0.myapplication.utils.ObscuredSharedPreferences;
import com.example.defaultuser0.myapplication.utils.Utilities;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConnectionHelper {


    private final OnRequestComplete mOnRequestComplete;
    private final Context mContext;
    private final int mMethod;
    private RequestBody mRequestBody;
    private MultipartBody.Part mMultipartBody;
    Call<JsonObject> mAPICall;
    JsonObject jsonObject;
    Response<JsonObject> mResponse;
    boolean isConnectException = false;
    int mResponseCode;
    JsonObject mResponseJsonObject;
    Call call;
    Utilities mUtilities;
    String[] mArgs;
    JsonObject mRequestJSONObject;


    public ConnectionHelper(Fragment mFragment, int mMethod, String[] mArgs, JsonObject mRequestJSONObject) {
        this.mContext = mFragment.getActivity();
        mUtilities = new Utilities(mContext);
        this.mOnRequestComplete = (OnRequestComplete) mFragment;
        this.mMethod = mMethod;
        this.mArgs = mArgs;
        this.mRequestJSONObject = mRequestJSONObject;


        if (this.mArgs == null) {
            this.mArgs = new String[]{};
            //Make it Empty if null.
        }

        if (this.mRequestJSONObject == null) {
            mRequestJSONObject = new JsonObject();
        }

//        mFingerPrint = mUtilities.getCertificateSHA1Fingerprint();
    }

    public ConnectionHelper(Context mContext, int mMethod, String[] mArgs, JsonObject mRequestJSONObject) {
        this.mContext = mContext;
        mUtilities = new Utilities(mContext);
        this.mOnRequestComplete = (OnRequestComplete) mContext;
        this.mMethod = mMethod;
        this.mArgs = mArgs;
        this.mRequestJSONObject = mRequestJSONObject;


        if (this.mArgs == null) {
            this.mArgs = new String[]{};
            //Make it Empty if null.
        }

        if (this.mRequestJSONObject == null) {
            mRequestJSONObject = new JsonObject();
        }

        ObscuredSharedPreferences hp = new ObscuredSharedPreferences(mContext, mContext.getSharedPreferences("My_Application", Context.MODE_PRIVATE));
        String aa = hp.getString(Constants.PreferenceKeys.ACCESS_TOKEN, "nulll");

    }

    public ConnectionHelper(Context mContext, int mMethod, MultipartBody.Part mMultipartBody, RequestBody mRequestBody, String[] mArgs) {
        this.mContext = mContext;
        mUtilities = new Utilities(mContext);
        this.mOnRequestComplete = (OnRequestComplete) mContext;
        this.mMethod = mMethod;
        this.mMultipartBody = mMultipartBody;
        this.mRequestBody = mRequestBody;
        this.mArgs = mArgs;
    }

    public ConnectionHelper(Activity mActivity, int mMethod, String[] mArgs, JsonObject mRequestJSONObject) {
        this.mContext = mActivity;
        mUtilities = new Utilities(mContext);
        this.mOnRequestComplete = (OnRequestComplete) mContext;
        this.mMethod = mMethod;
        this.mArgs = mArgs;
        this.mRequestJSONObject = mRequestJSONObject;

    }


    public void execute() {

        call = getCall(mMethod);

        if (call != null) {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    //convert the response to json and store in cache, if its necessary
                    JSONObject mdata = new JSONObject();

                    try {
                        ResponseBody mObj = response.body();
                        mdata = new JSONObject(mObj.string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    switch (response.code()) {
                        case 500:
//                            showMessage(mContext, "Sorry! Something went wrong. Please try after sometime");
                            mUtilities.writeLog("500:Server error");
                            mOnRequestComplete.requestFailed(mMethod);
                            break;
                        case 400:
//                            showMessage(mContext, "Sorry! Unexpected error occurred. Please try after sometime");
                            mUtilities.writeLog("400:Bad request");
                            mOnRequestComplete.requestFailed(mMethod);
                            break;
                        case 404:
//                            showMessage(mContext, "Sorry! Unexpected error occurred. Please try after sometime");
                            mUtilities.writeLog("404:Page Not Found");
                            mOnRequestComplete.requestFailed(mMethod);
                            break;
                        case 200:
                            mOnRequestComplete.requestComplete(mMethod, mdata);
                            break;
                        case 201:
                            mOnRequestComplete.requestComplete(mMethod, mdata);
                            break;
                        default:
//                            showMessage(mContext, "Sorry! Unexpected error occurred. Please try after sometime");
                            mOnRequestComplete.requestFailed(mMethod);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    if (t.getMessage().contains("Failed to connect")) {

                        mUtilities.writeLog("Connection timed out");
                        mOnRequestComplete.requestFailed(mMethod);
                    } else {
                        mUtilities.writeLog("Connection Failed");
                        mOnRequestComplete.requestFailed(mMethod);
                    }
                }
            });
        } else {
            mUtilities.writeLog("Something went wrong");
            mOnRequestComplete.requestFailed(mMethod);
        }


    }


    private Call getCall(int mRequestCode) {
        


        try {
            switch (mRequestCode) {
//                case Constants.Methods.CREATE_RESTAURANT:
//                    call = API.getInstance().getServices().addRestaurant(jsonObject);
//                    break;


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return call;

    }

}


