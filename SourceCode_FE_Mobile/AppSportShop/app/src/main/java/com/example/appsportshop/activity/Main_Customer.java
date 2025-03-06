package com.example.appsportshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.AuthAPI;
import com.example.appsportshop.fragment.Admin.FragManagerProduct;
import com.example.appsportshop.fragment.Customer.FragCart;
import com.example.appsportshop.fragment.Customer.FragHome;
import com.example.appsportshop.fragment.Customer.FragProfile;
import com.example.appsportshop.fragment.Customer.FragSearch;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;
import com.example.appsportshop.utils.dialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class Main_Customer extends AppCompatActivity {

    FragHome fragHome = null;
    FragCart fragCart = null;
    FragProfile fragProfile = null;
    FragSearch fragSearch = null;
    BottomNavigationView navi;
    SharedPreferences sharedPreferences;
    private String username = "";
    private String password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.main_layout);
//        LoadDataInLocal();

        // mở lên sẽ vào fragHome
        FragHome.isDispHomeCustommer = false;
        fragHome = new FragHome();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fade_out,  // enter
                        R.anim.slide_out_left  // exit
                )
                .replace(R.id.content_main, fragHome)
                .commit();
        loadingFragment();

        if (ReadPassWord()){
            try {
                APILoginDefault();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }



        mapping();
        //nếu bấm lưu trong UpdateProfile thì nhảy qua fragProfile


        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        item.setChecked(true);
                        if (fragHome == null)
                            fragHome = new FragHome();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.fade_out,  // enter
                                        R.anim.slide_out_left  // exit
                                )
                                .replace(R.id.content_main, fragHome)
                                .commit();
                        break;
                    case R.id.ic_cart:
                        item.setChecked(true);
                        if (fragCart == null)
                            fragCart = new FragCart();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_in,
                                        R.anim.fade_out
                                )
                                .replace(R.id.content_main, fragCart)
                                .commit();

                        break;

                    case R.id.ic_search:
                        item.setChecked(true);
                        if (fragSearch == null)
                            fragSearch = new FragSearch();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_in,
                                        R.anim.fade_out
                                )
                                .replace(R.id.content_main, fragSearch)
                                .commit();

                        break;
                    case R.id.ic_profile:
                        item.setChecked(true);
                        if (fragProfile==null)
                            fragProfile= new FragProfile();

                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.fade_out,  // enter
                                        R.anim.slide_in  // exit
                                )
                                .replace(R.id.content_main, fragProfile)
                                .commit();
                        break;
                }
                return false;
            }
        });

    }
    private void loadingFragment() {
        if (FragHome.isDispHomeCustommer) {
            FragHome.isDispHomeCustommer = false;
            fragHome = new FragHome();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fade_out,  // enter
                            R.anim.slide_out_left  // exit
                    )
                    .replace(R.id.content_main, fragHome)
                    .commit();
        }else
        if (FragProfile.isDisplay){
            FragProfile.isDisplay=false;
            fragProfile= new FragProfile();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fade_out,  // enter
                            R.anim.slide_in  // exit
                    )
                    .replace(R.id.content_main, fragProfile)
                    .commit();
        }
        else
        if (FragCart.isDisplay){
            FragCart.isDisplay=false;
            fragCart= new FragCart();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fade_out,  // enter
                            R.anim.slide_in  // exit
                    )
                    .replace(R.id.content_main, fragCart)
                    .commit();
        }


    }
    private void mapping() {
        navi = findViewById(R.id.bottom_navigation);


    }
    private Boolean ReadPassWord() {

        try {
            username = sharedPreferences.getString("username", "");
            password = sharedPreferences.getString("password", "");
            if (password.equalsIgnoreCase("") || password == null) {
//            btnBack.setVisibility(View.GONE);
                return false;
            }
            return true;
        }catch (Exception R) {
            return false;
        }
//        System.out.println("doc mat khau "+sharedPreferences.getString("username","")+sharedPreferences.getString("password",""));

    }

    private void APILoginDefault() throws JSONException {

        AuthAPI.LoginAPI(getApplicationContext(), Utils.BASE_URL + "auth/login", username, password, new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONObject res = response.getJSONObject("data");
                String role = res.getString("role");
//                SaveInfoToLocal(username, password);

                SingletonUser singletonUser = SingletonUser.getInstance();
                singletonUser.setIdUser(res.getLong("id"));

                singletonUser.setUserName(res.getString("username"));
                if (res.getString("fullname").equalsIgnoreCase("null")) {
                    singletonUser.setFullName("");
                } else {
                    singletonUser.setFullName(res.getString("fullname"));

                }


                if (res.getString("adress").equalsIgnoreCase("null")) {
                    singletonUser.setAdress("");
                } else {
                    singletonUser.setAdress(res.getString("adress"));

                }
                if (res.getString("email").equalsIgnoreCase("null")) {
                    singletonUser.setEmail("");
                } else {
                    singletonUser.setEmail(res.getString("email"));

                }

                if (res.getString("birthday").equalsIgnoreCase("null")) {
                    singletonUser.setBirthday("");
                } else {
                    singletonUser.setBirthday(res.getString("birthday"));

                }

                if (res.getString("phone").equalsIgnoreCase("null")) {
                    singletonUser.setPhone("");
                } else {
                    singletonUser.setPhone(res.getString("phone"));
                }


//                System.out.println(singletonUser.getBirthday()+"woa-------------");
                singletonUser.setPassword(password);

                singletonUser.setRole(role);
                singletonUser.setAvatarUrl(res.getString("avatarUrl"));
                singletonUser.setPublicId(res.getString("publicId"));
                singletonUser.setToken(response.getString("token"));


            }

            @Override
            public void onError(VolleyError error){
            }
        });
    }
}
