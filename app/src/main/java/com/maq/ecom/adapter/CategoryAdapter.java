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
import com.maq.ecom.model.Category;
import com.maq.ecom.views.activities.CreateCategoryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * developed by irfan A.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>  implements Filterable {

    Context context;
    List<Category> arrayList;
    List<Category> arrayListFull;

    public CategoryAdapter(Context context, List<Category> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListFull = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category model = arrayList.get(position);

        //bind data
        Utils.loadImage(context, model.getCategoryImage(), holder.iv_catImg);
        holder.tv_name.setText(model.getCategoryName());
        holder.tv_products.setText(model.getProducts());
        holder.tv_status.setText(model.getStatus());

        holder.layout.setOnClickListener(v ->
                context.startActivity(new Intent(context, CreateCategoryActivity.class)
                        .putExtra("cat_id", arrayList.get(position).getCategoryId())
                        .putExtra("cat_name", arrayList.get(position).getCategoryName())
                        .putExtra("cat_banner", arrayList.get(position).getCategoryBanner())
                        .putExtra("cat_img", arrayList.get(position).getCategoryImage())
                        .putExtra("cat_status", arrayList.get(position).getStatus())
                        .putExtra("cat_edit", true)
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
            List<Category> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Category item : arrayListFull) {
                    if (item.getCategoryName().toLowerCase().contains(filterPattern) ||
                            item.getProducts().toLowerCase().contains(filterPattern) ||
                            item.getStatus().toLowerCase().contains(filterPattern)) {
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

        @BindView(R.id.catItem_name)
        TextView tv_name;
        @BindView(R.id.catItem_products)
        TextView tv_products;
        @BindView(R.id.catItem_status)
        TextView tv_status;

        @BindView(R.id.catItem_img)
        AppCompatImageView iv_catImg;

        @BindView(R.id.catItem_layout)
        CardView layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
