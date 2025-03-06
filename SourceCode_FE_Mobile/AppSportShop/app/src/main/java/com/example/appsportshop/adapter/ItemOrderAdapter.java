package com.example.appsportshop.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appsportshop.R;
import com.example.appsportshop.model.HoaDon;
import com.example.appsportshop.model.Order;

import java.util.ArrayList;

public class ItemOrderAdapter extends ArrayAdapter<HoaDon> {
    Context myContext;
    int myLayout;
    ArrayList<HoaDon> data;
    ArrayList<HoaDon> data_tmp;


    public ItemOrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HoaDon> listOrder) {
        super(context, resource, listOrder);
        this.myContext = context;
        this.myLayout = resource;
        this.data = listOrder;
        data_tmp = data;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ItemOrderAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(myLayout, null);
            viewHolder = new ItemOrderAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemOrderAdapter.ViewHolder) convertView.getTag();
        }
        HoaDon order = data.get(position);

//        viewHolder.tvGiaSP.setText(order.getPrice());
        viewHolder.tvGiaSP.setText(String.format("%.0f", order.getPrice()));
        viewHolder.tvSoluong.setText(String.valueOf(order.getQuantity()));
        viewHolder.tvTenSp.setText(order.getProductName());

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
                    ArrayList<HoaDon> list = new ArrayList<>();
                    for (HoaDon order : data
                    ) {

                        if (order.getId_Order().toLowerCase().contains(searchStr.toLowerCase()) ||order.getNameReciver().toLowerCase().contains(searchStr.toLowerCase()) || order.getSdt().toLowerCase().contains(searchStr.toLowerCase())) {
                            list.add(order);
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
                data = (ArrayList<HoaDon>) filterResults.values;
                notifyDataSetChanged();
            }
        };


    }

    private class ViewHolder {

        TextView tvTenSp, tvGiaSP, tvSoluong;
        public ViewHolder(View view) {
            tvTenSp = view.findViewById(R.id.tensp);
            tvGiaSP = view.findViewById(R.id.giasp);
            tvSoluong = view.findViewById(R.id.soluongsp);

        }
    }

}

