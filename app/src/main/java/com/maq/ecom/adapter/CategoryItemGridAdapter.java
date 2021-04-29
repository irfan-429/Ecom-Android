package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.Category;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.views.activities.CategoryItemDetailsActivity;
import com.maq.ecom.views.activities.CategoryItemsActivity;

import java.util.List;

@SuppressLint("UseSparseArrays")
public class CategoryItemGridAdapter extends BaseAdapter {

    Context context;
    List<CategoryItem> arrayList;


    public CategoryItemGridAdapter(Context c, List<CategoryItem> arrayList) {
        context = c;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        ViewHolderItem viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderItem();

//            gridView = new View(context);
            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.item_grid_category_item, null);
            viewHolder.imageView = gridView.findViewById(R.id.catItem_img);
            viewHolder.catItem_name = gridView.findViewById(R.id.catItem_name);
            viewHolder.code = gridView.findViewById(R.id.catItem_code);
            viewHolder.size = gridView.findViewById(R.id.catItem_size);
            viewHolder.qty = gridView.findViewById(R.id.catItem_qty);
            viewHolder.cost = gridView.findViewById(R.id.catItem_cost);

            // store the holder with the view.
//            convertView.setTag(viewHolder);

            if (arrayList != null && arrayList.size() > 0) {
                Utils.loadImage(context, arrayList.get(position).getProductCover(), viewHolder.imageView);
                viewHolder.catItem_name.setText(arrayList.get(position).getCategoryName());
                viewHolder.code.setText(arrayList.get(position).getProductCode());
                viewHolder.size.setText(arrayList.get(position).getIsSize());
                viewHolder.qty.setText("QTY "+arrayList.get(position).getStock());
                viewHolder.cost.setText(context.getResources().getString(R.string.INR_symbol) +arrayList.get(position).getSellingPrice());
            }


            gridView.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryItemDetailsActivity.class)
                        .putExtra("category_item", arrayList.get(position))
                );

            });

        } else gridView = convertView;

        return gridView;
    }

    public static class ViewHolderItem {
        AppCompatImageView imageView;
        TextView catItem_name, code, size, qty, cost;
        RelativeLayout layout;
    }


}