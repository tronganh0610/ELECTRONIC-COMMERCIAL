package com.example.appsportshop.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appsportshop.R;

import org.json.JSONException;


public class CategoryRCVAdapter extends RecyclerView.Adapter<CategoryRCVAdapter.ViewHolder> {
    //Dữ liệu hiện thị là danh sách sinh viên
    private String[] logoNameList;
    private int[] imgPhotoList;
    // Lưu Context để dễ dàng truy cập


    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position) throws JSONException;
    }

    public CategoryRCVAdapter(Context mContext, String[] logoNameList, int[] imgPhotoList, OnItemClickListener onItemClickListener) {
        this.imgPhotoList = imgPhotoList;
        this.logoNameList = logoNameList;
        this.mOnItemClickListener = onItemClickListener;
        this.mContext = mContext;
    }

    public void setListData(String[] logoNameList, int[] imgPhotoList) {
        this.imgPhotoList = imgPhotoList;
        this.logoNameList = logoNameList;
        notifyDataSetChanged();
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        // Nạp layout cho View biểu diễn phần tử sinh viên
        View itemView =
                inflater.inflate(R.layout.row_category_product, parent, false);


        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mOnItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.imgCategory.setImageResource(imgPhotoList[position]);
        holder.txtNameCategory.setText(logoNameList[position]);

    }



    @Override
    public int getItemCount() {
        return logoNameList.length;}

    /**
     * Lớp nắm giữ cấu trúc view
     */
    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView txtNameCategory;
        public ImageView imgCategory;


        public ViewHolder(View itemView) {
            super(itemView);

            txtNameCategory = itemView.findViewById(R.id.txtCategoryProduct);
            imgCategory = itemView.findViewById(R.id.imgCategoryProduct);
//            txtView = itemView.findViewById(R.id.itemrecycle);
//            birthyear = itemView.findViewById(R.id.birthyear);
//            detail_button = itemView.findViewById(R.id.detail_button);

            //Xử lý khi nút Chi tiết được bấm
//            detail_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),
//                                    studentname.getText() +" | "
//                                            + " Demo function", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            });
        }


    }


}
