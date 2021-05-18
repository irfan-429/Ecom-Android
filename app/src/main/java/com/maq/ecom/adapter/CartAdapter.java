package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.CartListener;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.views.activities.MainActivity;
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
    boolean isFound = false;
    int foundIndex;
    CartListener listener;

    public CartAdapter(Context context, List<CategoryItem> arrayList, CartListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener=listener;
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
        holder.tv_name.setText(model.getProductName());
        holder.tv_products.setText(model.getProductCode());
        holder.tv_qty.setText("QTY " + (int) Double.parseDouble(model.getStock()));
        holder.tv_price.setText(context.getResources().getString(R.string.INR_symbol) + model.getSellingPrice());

        holder.iv_del.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Are you want to delete?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onCartDelete(arrayList.get(position), position);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        holder.numberPicker.setMin(1);
        holder.numberPicker.setMax((int) Double.parseDouble(model.getStock()));
        holder.numberPicker.setFocusable(true);
        holder.numberPicker.setValue(model.getQty());

        holder.numberPicker.setValue(0);
        if (MainActivity.mCartList.size() > 0)
            for (CategoryItem item : MainActivity.mCartList)
                if (item.getProductId().equals(model.getProductId()))
                    holder.numberPicker.setValue(item.getQty());

        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
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
