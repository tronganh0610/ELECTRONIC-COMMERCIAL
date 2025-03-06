package com.example.appsportshop.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appsportshop.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthAPI {

    public static void LoginAPI(Context context, String url, String username, String password, APICallBack callBack) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        postData.put("username", username);
        postData.put("password", password);

        JSONObject requestBody = new JSONObject();
        requestBody.put("data", postData);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody.getJSONObject("data"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response
                        try {
                            callBack.onSuccess(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            System.err.println(jsonError);
                        }
                        System.err.println("Lỗi call api LoginAPI (AuthAPI.java) ->" + error.getMessage());
                        callBack.onError(error);


                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };


        requestQueue.add(request);

    }


    public static void RegisterAPI(Context context, String username, String password,APICallBack callBack) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        postData.put("username", username);
        postData.put("password", password);
        postData.put("role", "EMPLOYEE");
        JSONObject requestBody = new JSONObject();
        requestBody.put("data", postData);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Utils.BASE_URL + "auth/register", requestBody.getJSONObject("data"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response
                        try {
                            callBack.onSuccess(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.err.println("Lỗi call api RegisterAPI (AuthAPI.java) ->" + error.getMessage());
                        callBack.onError(error);


                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };


        requestQueue.add(request);

    }
}
