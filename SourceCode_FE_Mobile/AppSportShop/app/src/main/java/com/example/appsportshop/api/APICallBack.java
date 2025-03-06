package com.example.appsportshop.api;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface APICallBack {

    public void onSuccess(JSONObject response) throws JSONException;


    public void onError(VolleyError error);

}
