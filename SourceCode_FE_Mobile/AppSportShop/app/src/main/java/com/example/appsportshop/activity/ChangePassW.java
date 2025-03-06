package com.example.appsportshop.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.AuthAPI;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.fragment.Customer.FragProfile;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;
import com.example.appsportshop.utils.dialog;


import org.json.JSONException;
import org.json.JSONObject;


public class ChangePassW extends AppCompatActivity {
    EditText oldPassword, newPassW, renewPassW;
    AppCompatButton btnSave;

    ImageView btnReturn;
    String oldpass = "", password = "", repassword = "";
    //SharedPreferences sharedPreferences;
    SingletonUser singletonUser = SingletonUser.getInstance();
    private void mapping() {
        oldPassword = findViewById(R.id.oldPass);
        btnReturn = findViewById(R.id.quaylai);
        newPassW = findViewById(R.id.newPass);
        renewPassW = findViewById(R.id.reNewPass);
        btnSave = findViewById(R.id.btnSavePass);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.change_passw);
        mapping();
        setEvent();
    }

    private void setEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpass = String.valueOf(oldPassword.getText());
                password = String.valueOf(newPassW.getText());
                repassword = String.valueOf(renewPassW.getText());

                if (oldpass.isEmpty() || password.isEmpty() || repassword.isEmpty()){
                    CustomToast.makeText(ChangePassW.this, "Vui lòng nhập đầy đủ thông tin!", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();
                    oldPassword.setText("");
                    newPassW.setText("");
                    renewPassW.setText("");
                    oldPassword.requestFocus();
                }


                if (CheckRepassWord()) {
                    try {
                        APIChangePassW();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    newPassW.setText("");
                    renewPassW.setText("");
                    newPassW.requestFocus();
                    CustomToast.makeText(ChangePassW.this, "Mật khẩu và mật khâu nhập lại phải trùng nhau!", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();

                }


            }
        });


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragProfile.isDisplay=true;
                startActivity(new Intent(ChangePassW.this, Main_Customer.class));
            }
        });

    }

    private void APIChangePassW() throws JSONException {
        dialog dialog = new dialog(ChangePassW.this);
        dialog.startLoadingdialog();
        SharedPreferences sharedPreferences  = getApplicationContext().getSharedPreferences("matkhau", Context.MODE_PRIVATE);
        String passold = sharedPreferences.getString("password", "");
        if (!oldpass.equalsIgnoreCase(passold)) {
            CustomToast.makeText(ChangePassW.this, "Mật khẩu bạn nhập không đúng", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();
            oldPassword.setText("");
            newPassW.setText("");
            renewPassW.setText("");
            oldPassword.requestFocus();
            dialog.dismissdialog();
            return;
        }
        JSONObject postData = new JSONObject();

        postData.put("newpassword",repassword);

//        if (sharedPreferences.get)
        UserAPI.ApiPostandBody(getApplicationContext(), Utils.BASE_URL+"user/change-password/"+singletonUser.getIdUser(), postData, new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
//                System.out.println(response.get("message") +"---------");

                sharedPreferences.edit().clear().commit();
                CustomToast.makeText(ChangePassW.this, "Đổi Mật khẩu Tài Khoản Thành Công", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();
                startActivity(new Intent(ChangePassW.this, Login.class));
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private Boolean CheckRepassWord() {
        if (password.equals(repassword))
            return true;
        return false;
    }



}

