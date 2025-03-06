package com.example.appsportshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.AuthAPI;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.dialog;


import org.json.JSONException;
import org.json.JSONObject;


public class Register extends AppCompatActivity {
    EditText edtUserName, edtPassword, edtRePassword;
    AppCompatButton btnRegister;
    TextView btnLoginNow;
    String userName = "", password = "", repassword = "";
    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.register_act);
        mapping();
        setEvent();
    }

    private void setEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = String.valueOf(edtUserName.getText());
                password = String.valueOf(edtPassword.getText());
                repassword = String.valueOf(edtRePassword.getText());

                if (userName.isEmpty() || password.isEmpty() || repassword.isEmpty()){
                    CustomToast.makeText(Register.this, "Vui lòng nhập đầy đủ thông tin!", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();
                    edtUserName.setText("");
                    edtRePassword.setText("");
                    edtPassword.setText("");
                    edtUserName.requestFocus();
                }


                if (CheckRepassWord()) {
                    try {
                        APIRegister();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    edtPassword.setText("");
                    edtRePassword.setText("");
                    edtPassword.requestFocus();
                    CustomToast.makeText(Register.this, "Mật khẩu và mật khâu nhập lại phải trùng nhau!", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();

                }


            }
        });
    btnLoginNow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(Register.this,Login.class));
        }
    });
    }

    private void APIRegister() throws JSONException {
        dialog dialog = new dialog(Register.this);
        dialog.startLoadingdialog();
        AuthAPI.RegisterAPI(getApplicationContext(), userName, password, new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
//                System.out.println(response.get("message") +"---------");
                CustomToast.makeText(Register.this, "Đăng Ký Tài Khoản Thành Công", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();
                startActivity(new Intent(Register.this, Login.class));
            }

            @Override
            public void onError(VolleyError error) {
                if (error.networkResponse.statusCode == 400) {
                    edtUserName.setText("");
                    edtRePassword.setText("");
                    edtPassword.setText("");
                    edtUserName.requestFocus();
                    dialog.dismissdialog();
                    CustomToast.makeText(Register.this, "Tài khoản đã tồn tại!", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();
                } else {
                    // Handle other error codes here
                    edtUserName.setText("");
                    edtRePassword.setText("");
                    edtPassword.setText("");
                    edtUserName.requestFocus();
                    dialog.dismissdialog();
                    CustomToast.makeText(Register.this, "Tài khoản đã tồn tại!", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();

                }
            }
        });
    }

    private Boolean CheckRepassWord() {
        if (password.equals(repassword))
            return true;
        return false;
    }

    private void mapping() {
        edtUserName = findViewById(R.id.register_Username);
        edtPassword = findViewById(R.id.register_password);
        edtRePassword = findViewById(R.id.register_rePassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginNow = findViewById(R.id.loginNow);
    }

}
