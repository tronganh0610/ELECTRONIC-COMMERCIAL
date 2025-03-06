package com.example.appsportshop.api;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.example.appsportshop.utils.SingletonUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface ApiServiceRetrofit {

    @Multipart
    @PATCH("user/update/{id}")
    Call<JSONObject> updateUser(
            @Path("id") long id,
            @Part MultipartBody.Part image  ,
            @Part("fullname")RequestBody fullname,
            @Part("email")RequestBody email,
            @Part("phone")RequestBody phone,
            @Part("adress")RequestBody adress,
            @Part("birthday")RequestBody birthday

//                                @Part("email")RequestBody email,
//                                @Part("phone")RequestBody phone,
//                                @Part("adress")RequestBody adress,
//                                @Part("birthday")RequestBody birthday

                                );

    @Multipart
    @PATCH("user/update/{id}")
    Call<JSONObject> updateUsernotFile(
            @Path("id") long id,
            @Part("fullname")RequestBody fullname,
            @Part("email")RequestBody email,
            @Part("phone")RequestBody phone,
            @Part("adress")RequestBody adress,
            @Part("birthday")RequestBody birthday
//                                @Part("email")RequestBody email,
//                                @Part("phone")RequestBody phone,
//                                @Part("adress")RequestBody adress,
//                                @Part("birthday")RequestBody birthday

    );

    @Multipart
    @POST("product/add")
    Call<JSONObject> createProduct(
            @Part MultipartBody.Part image  ,
            @Part("productName")RequestBody productName,
            @Part("stockQuantity")RequestBody stockQuantity,
            @Part("price")RequestBody price,
            @Part("description")RequestBody description,
            @Part("category_id")RequestBody category_id

    );

    @Multipart
    @POST("product/update/{id}")
    Call<JSONObject> updateProduct(
            @Path("id") long id,
            @Part MultipartBody.Part image  ,
            @Part("productName")RequestBody productName,
            @Part("stockQuantity")RequestBody stockQuantity,
            @Part("price")RequestBody price,
            @Part("description")RequestBody description
//            @Part("category_id")RequestBody category_id

    );

    @Multipart
    @POST("product/update/{id}")
    Call<JSONObject> updateProductnotImage(
            @Path("id") long id,
            @Part("productName")RequestBody productName,
            @Part("stockQuantity")RequestBody stockQuantity,
            @Part("price")RequestBody price,
            @Part("description")RequestBody description
//            @Part("category_id")RequestBody category_id

    );


}
