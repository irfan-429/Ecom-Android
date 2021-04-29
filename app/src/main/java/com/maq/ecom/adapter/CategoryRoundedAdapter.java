package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.Category;
import com.maq.ecom.views.activities.CategoryItemsActivity;
import com.maq.ecom.views.activities.CreateCategoryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * developed by irfan A.
 */

public class CategoryRoundedAdapter extends RecyclerView.Adapter<CategoryRoundedAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<Category> arrayList;
    List<Category> arrayListFull;

    public CategoryRoundedAdapter(Context context, List<Category> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListFull = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_rounded, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category model = arrayList.get(position);

        //bind data
        Utils.loadProfileImage(context, model.getCategoryImage(), holder.iv_catImg);
        holder.tv_name.setText(model.getCategoryName());

        holder.layout.setOnClickListener(v ->
                context.startActivity(new Intent(context, CategoryItemsActivity.class)
                        .putExtra("category", arrayList.get(position))
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

        @BindView(R.id.catItemRounded_name)
        TextView tv_name;

        @BindView(R.id.catItemRounded_img)
        CircleImageView iv_catImg;

        @BindView(R.id.catItemRounded_parent)
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
