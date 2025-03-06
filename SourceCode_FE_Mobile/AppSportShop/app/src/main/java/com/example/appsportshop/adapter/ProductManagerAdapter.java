package com.example.appsportshop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.Login;
import com.example.appsportshop.activity.MainAdmin;
import com.example.appsportshop.activity.MainEmployee;
import com.example.appsportshop.activity.ManagerProductDetail;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.ProductAPI;
import com.example.appsportshop.fragment.Admin.FragManagerProduct;
import com.example.appsportshop.model.Product;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.Utils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductManagerAdapter extends ArrayAdapter<Product> implements Filterable {
    Context myContext;
    int myLayout;
    ArrayList<Product> data;
    ArrayList<Product> data_tmp;

    public ProductManagerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> listProduct) {
        super(context, resource, listProduct);
        this.myContext = context;
        this.myLayout = resource;
        this.data = listProduct;
        data_tmp = data;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHorder viewHorder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(myLayout, null);
            viewHorder = new ViewHorder(convertView);
            convertView.setTag(viewHorder);
        } else {
            viewHorder = (ViewHorder) convertView.getTag();
        }
        Product pd = data.get(position);
        viewHorder.txtProductName.setText((pd.getNameProduct()));

        viewHorder.txtquantity.setText(String.valueOf(pd.getStockQuantity()));
//        viewHorder.txtPrice.setText(String.valueOf(pd.getPrice()));
        viewHorder.txtPrice.setText(String.format("%.0f", pd.getPrice()));

        viewHorder.txtDescription.setText(pd.getDescription());
        Glide.with(getContext()).load(pd.getUrlImage()).into(viewHorder.imageView);

//click vào icon sửa
        viewHorder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContext, ManagerProductDetail.class);
                intent.putExtra("msg", pd);

                myContext.startActivity(intent);
            }
        });

        viewHorder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteItem(pd.getId());
            }
        });


        return convertView;

    }


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchStr = charSequence.toString();
                if (searchStr.isEmpty()) {
                    data_tmp = data;
                } else {
                    ArrayList<Product> list = new ArrayList<>();
                    for (Product product : data
                    ) {

                        if (product.getNameProduct().toLowerCase().contains(searchStr.toLowerCase())) {
                            list.add(product);
                        }
                    }
                    data_tmp = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = data_tmp;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };


    }

    private void clickDeleteItem(long idProduct) {
        Dialog dialog = new Dialog(myContext);

        dialog.setContentView(R.layout.alert_yes_no);

        Button btnYes = dialog.findViewById(R.id.YES);
        Button btnNo = dialog.findViewById(R.id.NO);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call api xóa

                try {
//                    com.example.ecomerceshoppe.ultils.dialog loadding = new dialog();
                    ProductAPI.DeleteProduct(myContext.getApplicationContext(), Utils.BASE_URL + "product/delete/"+ idProduct, new APICallBack() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException {
                            Object data =  response.get("data");
                            System.out.println(data);
                            if (data.toString().equalsIgnoreCase("null")) {
                                CustomToast.makeText(myContext.getApplicationContext(), "Sản phẩm đã tồn tại trong giỏ hàng hoặc đơn hàng \\n Xóa không thành công !", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();

                            }else {
                                CustomToast.makeText(myContext.getApplicationContext(), "Xóa sản phẩm thành công !", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();
                               FragManagerProduct.isDisplayManagerProd = true;
                                myContext.startActivity(new Intent(myContext, MainAdmin.class));
                            }

                        }

                        @Override
                        public void onError(VolleyError error) {

                        }

                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private class ViewHorder {


        TextView txtProductName, txtTag, txtquantity, txtPrice, txtDescription;
        ImageView imageView, ivEdit, ivDel;


        public ViewHorder(View view) {
            txtProductName = view.findViewById(R.id.name_ManagerProduct);
            //txtTag = view.findViewById(R.id.tag_ManagerProduct);
            txtquantity = view.findViewById(R.id.quantity_ManagerProduct);
            txtPrice = view.findViewById(R.id.price_ManagerProduct);
            txtDescription = view.findViewById(R.id.description_ManagerProduct);
            imageView = view.findViewById(R.id.image_ManagerProduct);
            ivEdit = view.findViewById(R.id.updateProduct);
            ivDel = view.findViewById(R.id.Delete_Product);
        }
    }



}
