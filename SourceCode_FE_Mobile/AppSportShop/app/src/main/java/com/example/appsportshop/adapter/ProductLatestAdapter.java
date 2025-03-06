package com.example.appsportshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appsportshop.R;


import java.text.DecimalFormat;
import java.util.List;

public class ProductLatestAdapter extends BaseAdapter {

    Context context;
    private List<String> nameProductList;

    private List<Double> priceProductList;

    private List<String> urlProductList;

    @Override
    public int getCount() {
        return nameProductList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }


    public ProductLatestAdapter(Context context, List<String> nameProductList, List<Double> priceProductList, List<String> urlProductList) {
        this.context = context;
        this.nameProductList = nameProductList;
        this.priceProductList = priceProductList;
        this.urlProductList = urlProductList;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.row_leastproduct, null);
        TextView txtName = view.findViewById(R.id.txtProductName);

        TextView txtPrice = view.findViewById(R.id.txtPrice);
        ImageView imageView = view.findViewById(R.id.imgProductLatest);

        txtName.setText(nameProductList.get(i));

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedValue = formatter.format( Double.valueOf(String.format("%.0f", priceProductList.get(i))));

        txtPrice.setText(formattedValue +" VND");
        Glide.with(context).load(urlProductList.get(i)).into(imageView);
//        Picasso.get().load(urlProductList.get(i)).into(imageView);

        return view;
    }

}
