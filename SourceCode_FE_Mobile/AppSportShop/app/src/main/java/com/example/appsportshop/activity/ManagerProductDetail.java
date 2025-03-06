package com.example.appsportshop.activity;

import static android.hardware.SensorPrivacyManager.Sensors.CAMERA;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.api.ApiServiceRetrofit;
import com.example.appsportshop.fragment.Admin.FragManagerProduct;
import com.example.appsportshop.model.Product;
import com.example.appsportshop.service.CategoryService;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.RealPathUtil;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;
import com.example.appsportshop.utils.dialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerProductDetail extends AppCompatActivity {

    private int GALLERY_REQ_CODE = 1000;
    private EditText edtName, edtQuanti, edtPrice, edtDescription;
    private Spinner spCategory;
    private ImageView imgProduct;
//    private Button btnImage, btnSave, btnExit;
String path="";
    TextView btnSave;

    LinearLayout btnImage, btnExit;

    long idUser ;

    Bitmap bitmap = null;
    private Product product;
    ArrayAdapter adapterCategory;
    int categoryCurrent = 0;



    String[]  ListCategory ;

    dialog loadding = new dialog(ManagerProductDetail.this);

    SingletonUser singletonUser = SingletonUser.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.layout_product_detail);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        idUser = singletonUser.getIdUser();

        product = (Product) getIntent().getSerializableExtra("msg");
//        if(product.getId()!=null)
//        System.out.println("id   "+product.getId());
        mapping();
        setEvent();
    }

    private void mapping() {

        edtName = findViewById(R.id.nameProduct_ManagerProductDetail);
        edtQuanti = findViewById(R.id.quantity_ManagerProductDetail);
        edtQuanti.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtPrice = findViewById(R.id.price_ManagerProductDetail);
        edtPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtDescription = findViewById(R.id.description_ManagerProductDetail);
        spCategory = findViewById(R.id.category_ManagerProductDetail);
        imgProduct = findViewById(R.id.img_ManagerProductDetail);
        btnImage = findViewById(R.id.editImg_ManagerProductDetail);
        btnSave = findViewById(R.id.save_ManagerProductDetail);
        btnExit = findViewById(R.id.exit_ManagerProductDetail);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY_REQ_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                Context context = ManagerProductDetail.this;
                path = RealPathUtil.getRealPath(context, contentURI);
//                data.getByteArrayExtra("a");
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//

                    int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

//                    Picasso.load(String.valueOf(Uri.parse(String.valueOf(contentURI)))).fit().centerCrop().into(imageView);
                    imgProduct.setImageBitmap(scaled);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            imgProduct.setImageBitmap(scaled);
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



    private void SaveCreate() throws JSONException {

        //call api create product
        loadding.startLoadingdialog();
        System.err.println("nút lưu Save Create đc nhấn");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MultipartBody.Part body = null;
//        if (!path.equalsIgnoreCase("")) {

            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//        }

//        edtName, edtQuanti, edtPrice, edtDescription;
        RequestBody productName = RequestBody.create(MediaType.parse("multipart/form-data"), edtName.getText().toString() );
        RequestBody stockQuantity = RequestBody.create(MediaType.parse("multipart/form-data"), edtQuanti.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), edtPrice.getText().toString() );
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), edtDescription.getText().toString());
        final int[] idCategory = {0};
        idCategory[0] = 1;


//        System.out.println(categorySelectedItem.toString()+"nam nam nam");
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf( categoryCurrent));
        Log.d("id-category", String.valueOf(categoryCurrent));
        ApiServiceRetrofit apiServiceRetrofit = retrofit.create(ApiServiceRetrofit.class);

            Call<JSONObject> call = apiServiceRetrofit.createProduct( body, productName,stockQuantity,price,description,category_id);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    Log.d("manager-product", response.toString());

                    if (response.code() == 200) {

                        CustomToast.makeText(ManagerProductDetail.this, "Thêm sản phẩm mới thành công !", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();

                    }else {
                        CustomToast.makeText(ManagerProductDetail.this, "Sản phẩm đã tồn tại hoặc thiếu dữ liệu !", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
                    }
                    loadding.dismissdialog();

                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    System.err.println("failed"+t.toString());
                    loadding.dismissdialog();
                }
            });


    }




    private void SaveUpdate() {

        //call api create product
        loadding.startLoadingdialog();
        System.out.println("nút lưu Save Update đc nhấn");

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

//        edtName, edtQuanti, edtPrice, edtDescription;
        RequestBody productName = RequestBody.create(MediaType.parse("multipart/form-data"), edtName.getText().toString() );
        RequestBody stockQuantity = RequestBody.create(MediaType.parse("multipart/form-data"), edtQuanti.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), edtPrice.getText().toString() );
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), edtDescription.getText().toString());

        ApiServiceRetrofit apiServiceRetrofit = retrofit.create(ApiServiceRetrofit.class);

        //update có ảnh
        if(body!=null){
            Call<JSONObject> call = apiServiceRetrofit.updateProduct(product.getId(), body, productName,stockQuantity,price,description);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                    System.out.println(response);
                    if (response.code() == 200) {

                        CustomToast.makeText(ManagerProductDetail.this, "Cập nhật sản phẩm mới thành công !", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();

                    }else {
                        CustomToast.makeText(ManagerProductDetail.this, "Sản phẩm đã tồn tại hoặc thiếu dữ liệu !", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
                    }
                    loadding.dismissdialog();

                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    System.err.println("failed"+t.toString());
                    loadding.dismissdialog();
                }
            });
        }
        else  {
       //update không ảnh

        Call<JSONObject> call = apiServiceRetrofit.updateProductnotImage(product.getId(), productName,stockQuantity,price,description);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                System.out.println(response);
                if (response.code() == 200) {

                    CustomToast.makeText(ManagerProductDetail.this, "Cập nhật sản phẩm mới thành công !", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();

                }else {
                    CustomToast.makeText(ManagerProductDetail.this, "Sản phẩm đã tồn tại hoặc thiếu dữ liệu !", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
                }
                loadding.dismissdialog();

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                System.err.println("failed"+t.toString());
                loadding.dismissdialog();
            }
        });

        }

    }

    private void setEvent() {

        ListCategory = CategoryService.loadLogoName().toArray(new String[0]);
        adapterCategory = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListCategory);
        spCategory.setAdapter(adapterCategory);
//        int selectionPosition= adapterCategory.getPosition(product.getCategory());
        spCategory.setSelection(categoryCurrent);
        //nếu tồn tại product , khi bấm bấm update
        if (product != null) {
            Glide.with(this).load(product.getUrlImage()).into(imgProduct);

            edtName.setText(product.getNameProduct());

            edtQuanti.setText(String.valueOf(product.getStockQuantity()));
//            edtPrice.setText(String.valueOf(product.getPrice()));
            edtPrice.setText(String.format("%.0f", product.getPrice()));
            edtDescription.setText(product.getDescription());

            categoryCurrent = adapterCategory.getPosition(product.getCategory());
            spCategory.setSelection(categoryCurrent);
            spCategory.setEnabled(false);
        }

        //get index spiner get id category
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryCurrent = i+1;
                System.out.println("idcategory -->" +categoryCurrent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerProductDetail.this, MainAdmin.class);
                FragManagerProduct.isDisplayManagerProd= true;
                startActivity(intent);

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // click lưu khi update
                if (product != null) {

                    SaveUpdate();

                } else {
                    try {
                        SaveCreate();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });


    }


}
