package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.Category;
import com.maq.ecom.views.activities.CategoryItemsActivity;

import java.util.List;

@SuppressLint("UseSparseArrays")
public class CategoryGridAdapter extends BaseAdapter {

    Context context;
    List<Category> arrayList;


    public CategoryGridAdapter(Context c,  List<Category> arrayList) {
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

        ViewHolderItem viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderItem();

            convertView = inflater.inflate(R.layout.item_grid_category, parent, false);
            viewHolder.imageView = convertView.findViewById(R.id.catItem_img);
            viewHolder.catItem_name = convertView.findViewById(R.id.catItem_name);

            convertView.setTag(viewHolder);

        } else viewHolder = (ViewHolderItem) convertView.getTag();


        if (arrayList != null && arrayList.size() > 0) {
            Utils.loadImage(context, arrayList.get(position).getCategoryImage(), viewHolder.imageView);
            viewHolder.catItem_name.setText(arrayList.get(position).getCategoryName());
        }
        convertView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, CategoryItemsActivity.class)
                    .putExtra("category", arrayList.get(position))
            );

        });

        return convertView;
    }

    public static class ViewHolderItem {
        AppCompatImageView imageView;
        TextView catItem_name;
        RelativeLayout layout;
    }


}