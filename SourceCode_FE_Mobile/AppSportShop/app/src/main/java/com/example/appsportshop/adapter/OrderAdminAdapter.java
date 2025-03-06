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
import com.example.appsportshop.model.Order;

import java.util.ArrayList;

public class OrderAdminAdapter extends ArrayAdapter<Order> {
    Context myContext;
    int myLayout;
    ArrayList<Order> data;
    ArrayList<Order> data_tmp;


    public OrderAdminAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> listOrder) {
        super(context, resource, listOrder);
        this.myContext = context;
        this.myLayout = resource;
        this.data = listOrder;
        data_tmp = data;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        OrderAdminAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(myLayout, null);
            viewHolder = new OrderAdminAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderAdminAdapter.ViewHolder) convertView.getTag();
        }
        Order order = data.get(position);
        viewHolder.tvIdOder.setText(order.getId());
        viewHolder.tvNameReciver.setText(order.getName_ceciver());
        viewHolder.tvdayOrder.setText(String.valueOf(order.getOrderDate()));
        viewHolder.tvStatusOrder.setText(order.getIdOderStatus());
        viewHolder.tvphoneRiciver.setText(String.valueOf(order.getPhoneNumber()));
        viewHolder.tvshippingAdress.setText(String.valueOf(order.getShippingAdress()));

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
                    ArrayList<Order> list = new ArrayList<>();
                    for (Order order : data
                    ) {

                        if (order.getId().toLowerCase().contains(searchStr.toLowerCase()) ||order.getName_ceciver().toLowerCase().contains(searchStr.toLowerCase()) || order.getPhoneNumber().toLowerCase().contains(searchStr.toLowerCase())) {
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
                data = (ArrayList<Order>) filterResults.values;
                notifyDataSetChanged();
            }
        };


    }

    private class ViewHolder {

        TextView tvIdOder, tvNameReciver, tvshippingAdress, tvphoneRiciver, tvdayOrder, tvStatusOrder;
        public ViewHolder(View view) {
            tvIdOder = view.findViewById(R.id.idOrder);
            tvNameReciver = view.findViewById(R.id.name_reciver);
            tvshippingAdress = view.findViewById(R.id.shipAdrees);
            tvphoneRiciver = view.findViewById(R.id.phoneRiciver);
            tvdayOrder = view.findViewById(R.id.orderDate);
            tvStatusOrder = view.findViewById(R.id.status);




        }
    }

}

