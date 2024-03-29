package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.maq.ecom.interfaces.ItemListener;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.views.activities.CategoryItemDetailsActivity;
import com.maq.ecom.views.activities.MainActivity;
import com.maq.ecom.views.fragments.HomeFragment;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * developed by irfan A.
 */

public class CategoryItemsAdapter extends RecyclerView.Adapter<CategoryItemsAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<CategoryItem> arrayList;
    List<CategoryItem> arrayListFull;
    ItemListener listener;
    boolean isFound = false;
    int foundIndex;

    public CategoryItemsAdapter(Context context, List<CategoryItem> arrayList, ItemListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        this.arrayListFull = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryItem model = arrayList.get(position);

        //bind data
        Utils.loadImage(context, model.getProductCover(), holder.iv_itemImg);
        holder.tv_itemName.setText(model.getProductName());
        holder.tv_product.setText(model.getProductCode());
        holder.tv_desc.setText(model.getShortDesc());
        holder.tv_sellingPrice.setText(context.getResources().getString(R.string.INR_symbol) + model.getSellingPrice());
        holder.tv_qty.setText("Qty " + (int) Double.parseDouble(model.getStock()));

        if (model.getPrice() != null && !model.getPrice().isEmpty() && !model.getPrice().equals("0.00") && !model.getPrice().equals(model.getSellingPrice())) {
            holder.tv_price.setText(context.getResources().getString(R.string.INR_symbol) + model.getPrice());
            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.catItem_layout.setOnClickListener(v -> {
            context.startActivity(new Intent(context, CategoryItemDetailsActivity.class)
                    .putExtra("category_item", arrayList.get(position))
            );
        });


        holder.numberPicker.setMin(0);
        holder.numberPicker.setMax((int) Double.parseDouble(model.getStock()));
        holder.numberPicker.setFocusable(true);

        holder.numberPicker.setValue(0);
        if (MainActivity.mCartList.size() > 0)
            for (CategoryItem item : MainActivity.mCartList)
                if (item.getProductId().equals(model.getProductId()))
                    holder.numberPicker.setValue(item.getQty());

        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void valueChanged(int value, ActionEnum action) {
                model.setQty(value); //change qty

                if (MainActivity.mCartList.size() > 0)
                    for (int i = 0; i < MainActivity.mCartList.size(); i++) {
                        if (MainActivity.mCartList.get(i).getProductId().equals(model.getProductId())) {
                            isFound = true;
                            foundIndex = i;
                            break;
                        } else isFound = false;
                    }

                if (isFound) {
                    if (value == 0) MainActivity.mCartList.remove(foundIndex);
                    else {
                        if (MainActivity.mCartList.size() > 0)
                            MainActivity.mCartList.set(foundIndex, model);
                        else MainActivity.mCartList.add(model);
                    }
                } else MainActivity.mCartList.add(model);

                notifyDataSetChanged();

                listener.onItemClick();
            }
        });


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
            List<CategoryItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CategoryItem item : arrayListFull) {
                    if (item.getCategoryName().toLowerCase().contains(filterPattern) ||
                            item.getCategoryName().toLowerCase().contains(filterPattern) ||
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

        @BindView(R.id.catItem_tv_itemName)
        TextView tv_itemName;
        @BindView(R.id.catItem_tv_product)
        TextView tv_product;
        @BindView(R.id.catItem_tv_desc)
        TextView tv_desc;
        @BindView(R.id.catItem_tv_selling_price)
        TextView tv_sellingPrice;
        @BindView(R.id.catItem_tv_price)
        TextView tv_price;
        @BindView(R.id.catItem_tv_qty)
        TextView tv_qty;

        @BindView(R.id.catItem_iv_itemImg)
        AppCompatImageView iv_itemImg;

        @BindView(R.id.catItem_layout)
        CardView catItem_layout;

        @BindView(R.id.catItem_number_picker)
        NumberPicker numberPicker;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
