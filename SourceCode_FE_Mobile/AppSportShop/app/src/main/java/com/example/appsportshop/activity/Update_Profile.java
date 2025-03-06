package com.example.appsportshop.activity;

import static android.hardware.SensorPrivacyManager.Sensors.CAMERA;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.ApiServiceRetrofit;
import com.example.appsportshop.api.AuthAPI;
import com.example.appsportshop.api.UpdateUser;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.fragment.Customer.FragProfile;
import com.example.appsportshop.model.User;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.RealPathUtil;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;
import com.example.appsportshop.utils.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Update_Profile extends AppCompatActivity {

    private int GALLERY_REQ_CODE = 1000;
    AppCompatButton btnSave;
    EditText tvFullName, tvEmail, tvAdress, tvBirthdat, tvPhone;
     TextView tvChangAvt;

     ImageView btnBack;
    Bitmap bitmap = null;
    ImageView imgProfile;
    User userCur = null;
    String ngaySinh="";

    String path="";
    SingletonUser singletonUser = SingletonUser.getInstance();


    private void setMapping() {
        tvFullName = findViewById(R.id.fullnameUpdate);
        tvEmail = findViewById(R.id.EmailUpdate);
        tvAdress = findViewById(R.id.adressUpdatePf);
        tvBirthdat = findViewById(R.id.birthdayUpdate);
        tvBirthdat.setInputType(InputType.TYPE_CLASS_DATETIME);
        tvPhone = findViewById(R.id.phoneUpdate);
        btnBack = findViewById(R.id.btnBack);
        tvPhone.setInputType(InputType.TYPE_CLASS_NUMBER);

        imgProfile = findViewById(R.id.img_profile_avatar);
        tvChangAvt = findViewById(R.id.txt_profile_btn_change_avatar);

        btnSave = findViewById(R.id.btnSaveProfile);

    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.update_profile);

        userCur = getInfoUser();

        setMapping();
        loadData();
        setEvent();

    }

    private void loadData() {

        Glide.with(this).load(userCur.getAvatarUrl()).into(imgProfile);
        if (userCur.getFullName() == null ){
            userCur.setFullName("");
        }
        tvFullName.setText(userCur.getFullName());
        if (userCur.getEmail() == null ){
            userCur.setEmail("");
        }
        tvEmail.setText(userCur.getEmail());
        if (userCur.getAdress() == null ){
            userCur.setAdress("");
        }
        tvAdress.setText(userCur.getAdress());
//        System.out.println("nânnanananna"+userCur.getBirthday());

        if (userCur.getBirthday()== null){
            tvBirthdat.setText("");

        }else{
            tvBirthdat.setText(String.valueOf( userCur.getBirthday()));

        }
        if (userCur.getPhone() == null ){
            userCur.setPhone("");
        }
        tvPhone.setText(userCur.getPhone());

    }


    private void setEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInfoUser();

//                String districtName =(district.getText().toString());
//                if(TextUtils.isEmpty((nameShip))){
//                    name.setError("Vui lòng không để trống");
//                }

                if(tvFullName.getText().toString().isEmpty()||
                tvEmail.getText().toString().isEmpty()||
                tvAdress.getText().toString().isEmpty()||
                tvBirthdat.getText().toString().isEmpty()||
                tvPhone.getText().toString().isEmpty()){
                    CustomToast.makeText(Update_Profile.this, "Vui lòng nhập đầy đủ thông tin cá nhân !", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();

                }else {
                    try {
                        APIUpdateInfo();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if (userCur.getRole().equalsIgnoreCase("EMPLOYEE")){
                        startActivity(new Intent(Update_Profile.this, MainEmployee.class));

                    }else
                    if (userCur.getRole().equalsIgnoreCase("ADMIN")) {
                        FragProfile.isDisplay = true;
                        startActivity(new Intent(Update_Profile.this, MainAdmin.class));

                    }
                    else
                    if (userCur.getRole().equalsIgnoreCase("CUSTOMER")) {
                        FragProfile.isDisplay = true;
                        startActivity(new Intent(Update_Profile.this, Main_Customer.class));

                    }
                }



            }
        });

        tvBirthdat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        tvChangAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragProfile.isDisplay = true;
                startActivity(new Intent(Update_Profile.this, Main_Customer.class));
            }
        });
    }

    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        c.set(year, monthOfYear, dayOfMonth);
                        EditText editTextDate = findViewById(R.id.birthdayUpdate);
                        ngaySinh = simpleDateFormat.format(c.getTime());
                        editTextDate.setText(simpleDateFormat.format(c.getTime()));

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private User getInfoUser() {
        User user = new User();

        user.setIdUser(singletonUser.getIdUser());
        user.setFullName(singletonUser.getFullName());
        user.setEmail(singletonUser.getEmail());
        user.setAdress(singletonUser.getAdress());
        System.out.println(singletonUser.getBirthday()+"test");

        if(singletonUser.getBirthday().isEmpty()){

            user.setBirthday(null);
        }else{

            user.setBirthday(singletonUser.getBirthday());

        }
        user.setPhone(singletonUser.getPhone());
        user.setAvatarUrl(singletonUser.getAvatarUrl());
        user.setPublicId(singletonUser.getPublicId());
        user.setRole(singletonUser.getRole());


        return user;
    }

    private void APIUpdateInfo() throws JSONException {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MultipartBody.Part body = null;
        if (!path.equalsIgnoreCase("")) {
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }


        RequestBody hoten = RequestBody.create(MediaType.parse("multipart/form-data"), tvFullName.getText().toString() );
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), tvEmail.getText().toString());
        RequestBody sdt = RequestBody.create(MediaType.parse("multipart/form-data"), tvPhone.getText().toString() );
        RequestBody diachi = RequestBody.create(MediaType.parse("multipart/form-data"), tvAdress.getText().toString());
        RequestBody ngaysinh = RequestBody.create(MediaType.parse("multipart/form-data"), tvBirthdat.getText().toString() );

        ApiServiceRetrofit apiServiceRetrofit = retrofit.create(ApiServiceRetrofit.class);
        if (body != null) {
            Call<JSONObject> call = apiServiceRetrofit.updateUser(singletonUser.getIdUser(), body, hoten,email,sdt,diachi,ngaysinh);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    try {
                        Log.d("33334",response.toString());
                        callAPIgetInfo();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    System.err.println("3333"+t.toString());
                }
            });
        }else {

            Call<JSONObject> call = apiServiceRetrofit.updateUsernotFile( singletonUser.getIdUser(),hoten,email,sdt,diachi,ngaysinh);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    try {
                        callAPIgetInfo();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    System.err.println("failed"+t.toString());
                }
            });

        }

    }

    private void callAPIgetInfo() throws JSONException {
        UserAPI.getUserbyId(getApplicationContext(), Utils.BASE_URL + "user/findById/" + singletonUser.getIdUser(), new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONObject res = response.getJSONObject("data");
                String role = res.getString("role");

                singletonUser.setIdUser(res.getLong("id"));
                singletonUser.setUserName(res.getString("username"));
                singletonUser.setFullName(res.getString("fullname"));
                singletonUser.setAdress(res.getString("adress"));
//                System.out.println(res.getString("email")==null+"hhuhu");

                singletonUser.setEmail(res.getString("email"));
                singletonUser.setBirthday(res.getString("birthday"));
                singletonUser.setPhone(res.getString("phone"));
                singletonUser.setRole(role);
                singletonUser.setAvatarUrl(res.getString("avatarUrl"));
                singletonUser.setPublicId(res.getString("publicId"));
//                singletonUser.setToken(response.getString("token"));
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY_REQ_CODE) {
            if (data != null) {
//                data.get
                Uri contentURI = data.getData();
                Context context = Update_Profile.this;
                path = RealPathUtil.getRealPath(context, contentURI);
//                data.getByteArrayExtra("a");

//                imgProfile.setImageURI(contentURI);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                    imgProfile.setImageBitmap(scaled);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");

            Uri contentURI = data.getData();
            Context context = Update_Profile.this;
            path = RealPathUtil.getRealPath(context, contentURI);
            imgProfile.setImageBitmap(bitmap);
        }
    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Vui lòng chọn !!!");
        String[] pictureDialogItems = {
                "Chọn ảnh từ Thư Viện",
                "Chụp ảnh bằng Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
}
