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
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.CartAPI;
import com.example.appsportshop.fragment.Customer.FragCart;
import com.example.appsportshop.model.Cart;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<Cart> {
    Context myContext;
    int myLayout;

    ArrayList<Cart> listCart ;
    public static Double  sumCart=0.0;

    ArrayList<Cart> listCartTmp = new ArrayList<Cart>();


    public CartAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Cart> listCart) {
        super(context, resource, listCart);
        this.myContext = context;
        sumCart=0.0;
        this.myLayout = resource;
        this.listCart = listCart;
        this.listCartTmp.addAll(listCart);
    }

    private void clickDeleteItem(long idCartItem, int i) {
        Dialog dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.alert_yes_no);

        Button btnYes = dialog.findViewById(R.id.YES);
        Button btnNo = dialog.findViewById(R.id.NO);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call api xóa
                try {
                    CartAPI.DeleteCart(getContext().getApplicationContext(), Utils.BASE_URL + "cart-management/"+ idCartItem, new APICallBack() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException {
                            CustomToast.makeText(getContext(), "Xóa Sản Phẩm Thành Công", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();
                            notifyDataSetChanged();
                            dialog.dismiss();
                            listCart.remove(i);

                        }

                        @Override
                        public void onError(VolleyError error) {
                            CustomToast.makeText(getContext(), "Xóa Sản Phẩm Không Thành Công", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
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



    private Boolean checkAll(){
        int j =0;
        int len = listCart.size();
        for (int i = 0; i < len; i++) {
            if(listCart.get(i).getSelected()){
                j++;
            }
        }

        if (j==len)
            return true;
        return false;
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
        viewHolder.txtProductName.setText(cart.getNameProduct());
        viewHolder.txtQuanti.setText("x"+ String.valueOf(cart.getQuantity()));
//        viewHolder.txtShopName.setText(cart.getShopName());

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedValue = formatter.format( Double.valueOf(String.format("%.0f", cart.getPrice_total())));

        viewHolder.txtPrice.setText(formattedValue +" VND");


//        viewHolder.txtPrice.setText(String.format("%.0f", cart.getPrice_total()) +" VND");
        Glide.with(myContext).load(cart.getUrlImage()).into(viewHolder.ImgCart);



        if(cart.getSelected()) {
            viewHolder.checkBox.setChecked(true);
        }

        else{
            viewHolder.checkBox.setChecked(false);

        }

//        viewHolder.


        viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.getSelected())
                    sumCart-=cart.getPrice_total();
                clickDeleteItem(cart.getId(), i);

                notifyDataSetChanged();

            }
        });


        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cart.setSelected(b);

                if(checkAll()){
                    FragCart.cbCheckAll.setChecked(true);

                    FragCart.tongTien.setText( String.format("%.0f",sumCart)+" vnđ");
                }else {
                    FragCart.cbCheckAll.setChecked(false);
                    FragCart.tongTien.setText( String.format("%.0f",sumCart)+" vnđ");
                }
                if(cart.getSelected()){
                    sumCart += cart.getPrice_total();
                } else if (!cart.getSelected()) {
                    sumCart -= cart.getPrice_total();
                }
                FragCart.tongTien.setText( String.format("%.0f",sumCart)+" vnđ");
//                System.out.println(sumCart);

            }
        });
        return view;
    }

    private class ViewHolder {

        TextView txtShopName, txtProductName, txtPrice, Delete, txtQuanti;
//        TextView edtQuantity;
        public   CheckBox checkBox;
        ImageView ImgCart;




        public ViewHolder(View view) {
            txtShopName = view.findViewById(R.id.nameShopCart);
            checkBox = view.findViewById(R.id.checkCart);
            txtProductName = view.findViewById(R.id.nameProductCart);
            txtPrice = view.findViewById(R.id.priceCart);
            txtQuanti = view.findViewById(R.id.quanti);
            ImgCart = view.findViewById(R.id.imgCart);
            Delete = view.findViewById(R.id.Delete);

        }

    }

    //    public  void searchSV(String query){
//        listCart.clear();
//        if(query==""){
//            listCart.addAll(listCartTmp);
//        }
//        for (Cart sv:listCartTmp) {
//            if(sv.getHoTen().contains(query))
//                data.add(sv);
//        }
//        notifyDataSetChanged();
//    }
    public  void XoaDuLieu(){
        listCartTmp.clear();
        listCartTmp.addAll(listCart);
        listCart.clear();
        for (Cart cart:listCartTmp ) {
            if(!cart.getSelected())
                listCart.add(cart);
        }
        notifyDataSetChanged();
        listCartTmp.clear();
        listCartTmp.addAll(listCart);
    }
    public void CheckAll(){

        for ( Cart sv:listCart      ) {
            sv.setSelected(true);

//            sumCart+=sv.getPrice();
        }
        notifyDataSetChanged();
    }

    public void UnCheckAll(){

        for ( Cart sv:listCart      ) {
            sv.setSelected(false);
        }
        notifyDataSetChanged();
    }
}
