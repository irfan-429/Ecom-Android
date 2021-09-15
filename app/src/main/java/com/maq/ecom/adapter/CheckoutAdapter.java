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

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder> {

    Context context;
    List<CategoryItem> arrayList;

    public CheckoutAdapter(Context context, List<CategoryItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists", "RecyclerView"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryItem model = arrayList.get(position);

        //bind data
        Utils.loadImage(context, model.getProductCover(), holder.iv_checkoutImg);
        holder.tv_name.setText(model.getProductName());
        holder.tv_products.setText(model.getProductCode());
        holder.tv_qty.setText("QTY " + model.getQty());
        holder.tv_price.setText(context.getResources().getString(R.string.INR_symbol) + model.getSellingPrice());
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

        @BindView(R.id.checkoutItem_tv_itemName)
        TextView tv_name;
        @BindView(R.id.checkoutItem_tv_product)
        TextView tv_products;
        @BindView(R.id.checkoutItem_tv_qty)
        TextView tv_qty;
        @BindView(R.id.checkoutItem_tv_selling_price)
        TextView tv_price;

        @BindView(R.id.checkoutItem_iv_itemImg)
        AppCompatImageView iv_checkoutImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
