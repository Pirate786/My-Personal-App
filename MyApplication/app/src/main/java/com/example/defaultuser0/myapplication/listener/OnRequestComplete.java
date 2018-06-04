package com.example.defaultuser0.myapplication.listener;

import org.json.JSONArray;
import org.json.JSONObject;

public interface OnRequestComplete {

    public void requestComplete(int mMethod, JSONObject mObject);

    public void requestComplete(int mMethod, JSONArray mJsonArray);

    public void requestFailed(int mMethod);
}
