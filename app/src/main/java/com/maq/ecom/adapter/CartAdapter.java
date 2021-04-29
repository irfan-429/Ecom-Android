package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.CategoryItem;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * developed by irfan A.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context context;
    List<CategoryItem> arrayList;

    public CartAdapter(Context context, List<CategoryItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryItem model = arrayList.get(position);

        //bind data
        Utils.loadImage(context, model.getProductCover(), holder.iv_catImg);
        holder.tv_name.setText(model.getCategoryName());
        holder.tv_products.setText(model.getProductCode());
        holder.tv_qty.setText("QTY " + (int)Double.parseDouble(model.getStock()));
        holder.tv_price.setText(model.getPrice());

        holder.iv_del.setOnClickListener(v -> {
            arrayList.remove(position);
            notifyDataSetChanged();
        });


        holder.numberPicker.setMin(1);
        holder.numberPicker.setMax((int)Double.parseDouble(model.getStock()));
        holder.numberPicker.setFocusable(true);
        holder.numberPicker.setValue(model.getQty());
//
//        holder.numberPicker.setValue(1); //default val
//        if (arrayList.size() > 0)
//            for (CategoryItem item : arrayList)
//                if (item.getProductId().equals(model.getProductId()))
//                    holder.numberPicker.setValue(item.getQty());

        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void valueChanged(int value, ActionEnum action) {
                CategoryItem model = arrayList.get(position);
                model.setQty(value);

                notifyDataSetChanged();
//                if (arrayList.size() > 0)
//                    for (int i = 0; i < arrayList.size(); i++) {
//                        if (arrayList.get(i).getProductId().equals(model.getProductId())) {
//                            isFound = true;
//                            foundIndex = i;
//                            break;
//                        } else isFound = false;
//                    }
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


    static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.catItem_tv_itemName)
        TextView tv_name;
        @BindView(R.id.catItem_tv_product)
        TextView tv_products;
        @BindView(R.id.catItem_tv_qty)
        TextView tv_qty;
        @BindView(R.id.catItem_tv_selling_price)
        TextView tv_price;

        @BindView(R.id.catItem_iv_itemImg)
        AppCompatImageView iv_catImg;

        @BindView(R.id.catItem_iv_del)
        AppCompatImageView iv_del;

        @BindView(R.id.catItem_number_picker)
        NumberPicker numberPicker;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
