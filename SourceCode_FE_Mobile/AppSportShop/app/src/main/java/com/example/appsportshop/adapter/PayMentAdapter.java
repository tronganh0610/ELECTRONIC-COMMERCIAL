package com.example.appsportshop.adapter;



import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.model.Cart;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PayMentAdapter extends ArrayAdapter<Cart> {
    Context myContext;
    int myLayout;

    ArrayList<Cart> listCart ;
    public PayMentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Cart> listCart) {
        super(context, resource, listCart);
        this.myContext = context;
        this.myLayout = resource;
        this.listCart = listCart;
    }

    @Override
    public View getView(int i, @Nullable View view,@NonNull ViewGroup viewGroup) {


        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(myContext).inflate(myLayout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Cart cart = listCart.get(i);

        viewHolder.txtDescription.setText(cart.getNameProduct());
        viewHolder.txtQuantity.setText("x"+String.valueOf(cart.getQuantity()));
//        viewHolder.txtShopName.setText(cart.getShopName());

        viewHolder.txtPrice.setText(  String.format("%.0f",cart.getPrice_total())+" VND" );
        Glide.with(myContext).load(cart.getUrlImage()).into(viewHolder.ImgCart);
        return view;
    }
    private class ViewHolder {
        TextView txtShopName, txtPrice,txtDescription,txtQuantity;
        ImageView ImgCart;

        public ViewHolder(View view) {
            txtShopName = view.findViewById(R.id.shopPM);
            txtDescription = view.findViewById(R.id.descriptionPM);
            txtPrice = view.findViewById(R.id.pricePM);
            txtQuantity = view.findViewById(R.id.x1);
            ImgCart = view.findViewById(R.id.imgPM);
        }
    }
}

