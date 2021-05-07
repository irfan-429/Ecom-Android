package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.views.activities.CategoryItemDetailsActivity;
import com.maq.ecom.views.activities.MainActivity;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.List;

import butterknife.BindView;

@SuppressLint("UseSparseArrays")
public class CategoryItemGridAdapter extends BaseAdapter {

    Context context;
    List<CategoryItem> arrayList;
    boolean isFound = false;
    int foundIndex;

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

    @SuppressLint("SetTextI18n")
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
            viewHolder.desc = gridView.findViewById(R.id.catItem_desc);
            viewHolder.qty = gridView.findViewById(R.id.catItem_qty);
            viewHolder.sellingPrice = gridView.findViewById(R.id.catItem_selling_price);
            viewHolder.price = gridView.findViewById(R.id.catItem_cost);
            viewHolder.numberPicker = gridView.findViewById(R.id.catItem_number_picker);

            // store the holder with the view.
//            convertView.setTag(viewHolder);

            if (arrayList != null && arrayList.size() > 0) {
                CategoryItem model = arrayList.get(position);

                Utils.loadImage(context, model.getProductCover(), viewHolder.imageView);
                viewHolder.catItem_name.setText(model.getProductName());
                viewHolder.code.setText(model.getProductCode());
                viewHolder.desc.setText(model.getShortDesc());
                viewHolder.qty.setText("QTY\n" +(int) Double.parseDouble(model.getStock()));
                viewHolder.sellingPrice.setText(context.getResources().getString(R.string.INR_symbol) +model.getSellingPrice());

                if (arrayList.get(position).getPrice() != null && !arrayList.get(position).getPrice().isEmpty() && !arrayList.get(position).getPrice().equals("0.00") && !arrayList.get(position).getPrice().equals(arrayList.get(position).getSellingPrice())) {
                    viewHolder.price.setText(context.getResources().getString(R.string.INR_symbol) + arrayList.get(position).getPrice());
                    viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }


                //setting number picker
                viewHolder.numberPicker.setMin(0);
                viewHolder.numberPicker.setMax((int) Double.parseDouble(model.getStock()));
                viewHolder.numberPicker.setFocusable(true);

                viewHolder.numberPicker.setValue(0);
                if (MainActivity.mCartList.size() > 0)
                    for (CategoryItem item : MainActivity.mCartList)
                        if (item.getProductId().equals(model.getProductId()))
                            viewHolder.numberPicker.setValue(item.getQty());

                viewHolder.numberPicker.setValueChangedListener(new ValueChangedListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void valueChanged(int value, ActionEnum action) {

                        model.setQty(value);

                        if (MainActivity.mCartList.size() > 0)
                            for (int i = 0; i < MainActivity.mCartList.size(); i++) {
                                if (MainActivity.mCartList.get(i).getProductId().equals(model.getProductId())) {
                                    isFound = true;
                                    foundIndex = i;
                                    break;
                                } else isFound = false;
                            }

                        if (isFound) MainActivity.mCartList.set(foundIndex, model);
                        else MainActivity.mCartList.add(model);

                        notifyDataSetChanged();
                    }
                });

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
        TextView catItem_name, code, desc, qty, sellingPrice, price;
        RelativeLayout layout;
        NumberPicker numberPicker;
    }


}