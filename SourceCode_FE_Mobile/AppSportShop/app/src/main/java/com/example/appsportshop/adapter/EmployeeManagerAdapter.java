package com.example.appsportshop.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.MainAdmin;
import com.example.appsportshop.activity.ManagerProductDetail;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.fragment.Admin.FragManageEmloyee;
import com.example.appsportshop.model.User;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmployeeManagerAdapter extends ArrayAdapter<User> implements Filterable {
    Context myContext;
    int myLayout;
    ArrayList<User> data;
    ArrayList<User> data_tmp;

    public EmployeeManagerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> listProduct) {
        super(context, resource, listProduct);
        this.myContext = context;
        this.myLayout = resource;
        this.data = listProduct;
        data_tmp = data;

    }

    @SuppressLint("SuspiciousIndentation")
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
        User pd = data.get(position);
        if (pd.getFullName().equalsIgnoreCase("null"))
            viewHorder.tvFullName.setText("");
        else
        viewHorder.tvFullName.setText((pd.getFullName()));

        viewHorder.tvMaNV.setText(String.valueOf(pd.getIdUser()));
//        viewHorder.txtPrice.setText(String.valueOf(pd.getPrice()));
        viewHorder.tvEmail.setText(pd.getEmail());

        if (pd.getPhone().equalsIgnoreCase("null"))
            viewHorder.tvSDT.setText("");
        else
            viewHorder.tvSDT.setText(pd.getPhone());



        Glide.with(getContext()).load(pd.getAvatarUrl()).into(viewHorder.imageView);

//click vào icon sửa

        if (pd.getRole().equalsIgnoreCase("CUSTOMER"))
            viewHorder.ivEdit.setVisibility(View.GONE);
        viewHorder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              PopUpUpdateEmployee(pd);
            }
        });

        viewHorder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteItem(pd.getIdUser());
            }
        });


        return convertView;

    }

    private void PopUpUpdateEmployee(User user) {
        Dialog dialog = new Dialog(myContext);

        dialog.setContentView(R.layout.update_employee);
        EditText maNV, hoten, email,sdt;
        maNV = dialog.findViewById(R.id.updateMaNV);
        hoten = dialog.findViewById(R.id.updatehoten);
        email = dialog.findViewById(R.id.updateEmail);
        sdt = dialog.findViewById(R.id.updateSdt);

        maNV.setText ( String.valueOf(user.getIdUser()));
        email.setText(user.getEmail());

        if (user.getFullName().equalsIgnoreCase("null")){
            hoten.setText("");
            user.setFullName("");
        }
        else
            hoten.setText(String.valueOf( user.getFullName()));
        if (user.getPhone().equalsIgnoreCase("null")){
            sdt.setText("");
            user.setPhone("");
        }
        else
            sdt.setText(user.getPhone());



        Button btnYes = dialog.findViewById(R.id.save);
        Button btnNo = dialog.findViewById(R.id.huy);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call api update
                try {

                    UpdateUserByAdmin(user.getIdUser(),  hoten.getText().toString(),
                    email.getText().toString(),
                    sdt.getText().toString() );
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                FragManageEmloyee.isDisplay = true;
                myContext.startActivity(new Intent(getContext(), MainAdmin.class));
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

    private void UpdateUserByAdmin(long idUser, String hoten, String email, String sdt) throws JSONException {

        JSONObject postData = new JSONObject();
        System.out.println(idUser+hoten+email+sdt);
        postData.put("email", email );
        postData.put("fullname", hoten );
        postData.put("sdt", sdt);
        UserAPI.ApiPostandBody(getContext(), Utils.BASE_URL + "user/update/"+ idUser, postData , new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                System.out.println(response);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
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
                    ArrayList<User> list = new ArrayList<>();
                    for (User user : data
                    ) {

                        if (user.getFullName().toLowerCase().contains(searchStr.toLowerCase())) {
                            list.add(user);
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
                data = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };


    }

    private void clickDeleteItem(long idUser) {
        Dialog dialog = new Dialog(myContext);

        dialog.setContentView(R.layout.alert_yes_no);
        TextView titel = dialog.findViewById(R.id.txt_popup_title);
        TextView content = dialog.findViewById(R.id.txt_popup_content);
        titel.setText("KHÓA TÀI KHOẢN !!!");
        content.setText("Bạn có chắc muốn khóa tài khoản này ?");

        Button btnYes = dialog.findViewById(R.id.YES);
        Button btnNo = dialog.findViewById(R.id.NO);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call api xóa

                try {
                    DeleteUser(idUser);
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

    private void DeleteUser(long idUser) throws JSONException {
        UserAPI.getUserbyId(myContext.getApplicationContext(), Utils.BASE_URL + "user/delete/" + idUser, new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                System.out.println(response.getString("data").equalsIgnoreCase("null"));
                if (response.getString("data").equalsIgnoreCase("null"))
                    CustomToast.makeText(getContext(), "Tài Khoản đã có thông tin và không thể xóa!", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();

                else {
                    CustomToast.makeText(getContext(), "Khóa tài khoản thanh công!", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();
                    FragManageEmloyee.isDisplay = true;
                    myContext.startActivity(new Intent(getContext(), MainAdmin.class));
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


    private class ViewHorder {


        TextView tvFullName, tvMaNV, tvEmail, tvSDT;
        ImageView imageView, ivEdit, ivDel;


        public ViewHorder(View view) {
            tvFullName = view.findViewById(R.id.fullname_NV);
            tvMaNV = view.findViewById(R.id.maNV);
            tvEmail = view.findViewById(R.id.emailNV);
            tvSDT = view.findViewById(R.id.stdNV);

            imageView = view.findViewById(R.id.image_ManagerEmployee);
            ivEdit = view.findViewById(R.id.updateNV);
            ivDel = view.findViewById(R.id.Delete_NV);
        }
    }



}
