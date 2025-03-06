package com.example.appsportshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.ProductAPI;
import com.example.appsportshop.fragment.Customer.FragCart;
import com.example.appsportshop.fragment.Customer.FragHome;
import com.example.appsportshop.model.Product;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.ObjectWrapperForBinder;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ProductDetail extends AppCompatActivity {
    ImageView Img_ProductDetail, btnBackHome, btnCart;
    TextView nameProductDetail, priceProductDeltail, tagProductDeltail, descriptionProductDetail;
    TextView addCart;


    private Product product;
    SingletonUser singletonUser = SingletonUser.getInstance();
    BottomSheetDialog dialogSelectQuanti;
    int quantiCart = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.product_detail);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        // nhận về giá trị gửi từ frag Home
        mapping();


        String id = getIntent().getStringExtra("idProduct");
//        System.out.println(id+"dã lấy đc ở product detail");
        try {
            ProductAPI.getProductByid(ProductDetail.this, Utils.BASE_URL + "product/" + id, new APICallBack() {
                @Override
                public void onSuccess(JSONObject response) throws JSONException {

                    JSONObject productObj = response.getJSONObject("data");

                    product = new Product();
                    product.setId(productObj.getLong("id"));
                    product.setNameProduct(productObj.getString("productName"));
                    product.setPrice((float) productObj.getDouble("price"));
                    product.setDescription(productObj.getString("description"));
                    product.setStockQuantity(productObj.getInt("stockQuantity"));
                    product.setUrlImage(productObj.getString("imageUrl"));

                    dialogSelectQuanti = new BottomSheetDialog(ProductDetail.this);
//                    dialogSelectQuanti.dismiss();
                    createDialog();

                    setEvent();

                }



                @Override
                public void onError(VolleyError error) {
                    System.err.println("lỗi call api get product by id in Product detail");
                }
            });
        } catch (JSONException e) {
            System.err.println("lỗi call api");

        }


    }


    private void loadData() {

        Glide.with(this).load(product.getUrlImage()).into(Img_ProductDetail);
        nameProductDetail.setText(product.getNameProduct());

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedValue = formatter.format( Double.valueOf(String.format("%.0f", product.getPrice())));

        priceProductDeltail.setText(formattedValue +" VND");

//        priceProductDeltail.setText(String.format("%.0f", product.getPrice()) +" VND");

//        priceProductDeltail.setText(String.valueOf(product.getPrice()) + " VND");
        descriptionProductDetail.setText(product.getDescription());
    }

    private void setEvent() {
        loadData();


        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogSelectQuanti.show();

            }
        });



        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragHome.isDispHomeCustommer = true;
                startActivity(new Intent(ProductDetail.this, Main_Customer.class));
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragCart.isDisplay = true;
                startActivity(new Intent(ProductDetail.this, Main_Customer.class));
            }
        });


    }


    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null, false);
        Button btnSubmit = view.findViewById(R.id.submit_quanti);
        TextView btnreduce = view.findViewById(R.id.reduce);
        TextView btnIncrease = view.findViewById(R.id.increase);
        TextView quanti = view.findViewById(R.id.quantityCart);
        quantiCart = Integer.parseInt(quanti.getText().toString());
        btnreduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(quanti.getText().toString()) - 1;
                if (quantity <= 0) {
                    CustomToast.makeText(ProductDetail.this, "Vui Lòng Chọn Số Lượng Lớn Hơn 0 !!!", CustomToast.LENGTH_LONG, CustomToast.ERROR, true).show();
                    quanti.setText("1");
                } else {
                    quanti.setText(String.valueOf(quantity));
                }
                quantiCart = Integer.parseInt(quanti.getText().toString());


            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(quanti.getText().toString()) + 1;
                quanti.setText(String.valueOf(quantity));

                quantiCart = Integer.parseInt(quanti.getText().toString());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (singletonUser.getToken() == null) {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    }else
                        addToCart();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                dialogSelectQuanti.dismiss();
            }
        });
        dialogSelectQuanti.setContentView(view);

    }

    private void addToCart() throws JSONException {
        System.out.println(singletonUser.getIdUser()+"|"+product.getId()+"|"+ quantiCart);
        ProductAPI.AddToCart(ProductDetail.this, Utils.BASE_URL + "cart-management/carts", singletonUser.getIdUser(), product.getId(), quantiCart, new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                CustomToast.makeText(ProductDetail.this, "Thêm sản phẩm vào giỏ hàng thành công", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();
            }

            @Override
            public void onError(VolleyError error) {
                CustomToast.makeText(ProductDetail.this, "Thêm sản phẩm vào giỏ hàng thất bại", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();

            }
        });
    }

    private void mapping() {
        Img_ProductDetail = findViewById(R.id.img_ProductDetail);
        nameProductDetail = findViewById(R.id.nameProductDetail);
        priceProductDeltail = findViewById(R.id.priceProductDeltail);
        tagProductDeltail = findViewById(R.id.tagProductDeltail);
        descriptionProductDetail = findViewById(R.id.descriptionProductDetail);
        addCart = findViewById(R.id.addCart);
        btnBackHome = findViewById(R.id.back_Productdetail);
        btnCart = findViewById(R.id.cart_Productdetail);

    }
}
