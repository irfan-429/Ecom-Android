package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.Banner;
import com.maq.ecom.views.activities.CreateBannerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * developed by irfan A.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyViewHolder>  implements Filterable {

    Context context;
    List<Banner> arrayList;
    List<Banner> arrayListFull;

    public BannerAdapter(Context context, List<Banner> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListFull = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Banner model = arrayList.get(position);

        //bind data
        Utils.loadImage(context, model.getBanImg(), holder.iv_banImg);
        holder.tv_name.setText(model.getBanName());
        holder.tv_link.setText(model.getBanLink());
        holder.tv_status.setText(model.getBanStatus());

        holder.layout.setOnClickListener(v ->
                context.startActivity(new Intent(context, CreateBannerActivity.class)
                        .putExtra("ban_id", arrayList.get(position).getBanId())
                        .putExtra("ban_name", arrayList.get(position).getBanName())
                        .putExtra("ban_link", arrayList.get(position).getBanLink())
                        .putExtra("ban_img", arrayList.get(position).getBanImg())
                        .putExtra("ban_status", arrayList.get(position).getBanStatus())
                        .putExtra("ban_edit", true)
                )
        );
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Banner> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Banner item : arrayListFull) {
                    if (item.getBanName().toLowerCase().contains(filterPattern) ||
                            item.getBanStatus().toLowerCase().contains(filterPattern)
                            ) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.banItem_name)
        TextView tv_name;
        @BindView(R.id.banItem_link)
        TextView tv_link;
        @BindView(R.id.banItem_status)
        TextView tv_status;

        @BindView(R.id.banItem_img)
        AppCompatImageView iv_banImg;

        @BindView(R.id.banItem_layout)
        CardView layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
