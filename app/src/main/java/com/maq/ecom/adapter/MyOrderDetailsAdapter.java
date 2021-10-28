package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.MyOrder;
import com.maq.ecom.model.OrderDetailsItem;
import com.maq.ecom.views.activities.MyOrderDetailsActivity;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * developed by irfan A.
 */

public class MyOrderDetailsAdapter extends RecyclerView.Adapter<MyOrderDetailsAdapter.MyViewHolder> {

    SessionManager sessionManager;
    Context context;
    List<OrderDetailsItem> arrayList;

    public MyOrderDetailsAdapter(Context context, List<OrderDetailsItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_order_details, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderDetailsItem model = arrayList.get(position);

        Log.i("TAG", "onBindViewHolder: " + model.getProductName());
        //bind data
        Utils.loadImage(context, model.getImage(), holder.iv_itemImg);
        holder.tv_itemName.setText(model.getProductName());
        holder.tv_product.setText(model.getProductCode());
        holder.tv_desc.setText(model.getDisc());
        holder.tv_sellingPrice.setText(context.getResources().getString(R.string.INR_symbol) + model.getSellingPrice());
        holder.tv_qty.setText("Qty " + (int) Double.parseDouble(model.getQty()));

        if (model.getPrice() != null && !model.getPrice().isEmpty() && !model.getPrice().equals("0.00") && !model.getPrice().equals(model.getSellingPrice())) {
            holder.tv_price.setText(context.getResources().getString(R.string.INR_symbol) + model.getPrice());
            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        if (sessionManager.isAdmin()) {
            holder.number_picker.setVisibility(View.VISIBLE);
        }

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
        @BindView(R.id.myOrderDetailsItem_tv_itemName)
        TextView tv_itemName;
        @BindView(R.id.myOrderDetailsItem_tv_product)
        TextView tv_product;
        @BindView(R.id.myOrderDetailsItem_tv_desc)
        TextView tv_desc;
        @BindView(R.id.myOrderDetailsItem_tv_selling_price)
        TextView tv_sellingPrice;
        @BindView(R.id.myOrderDetailsItem_tv_price)
        TextView tv_price;
        @BindView(R.id.myOrderDetailsItem_tv_qty)
        TextView tv_qty;

        @BindView(R.id.myOrderDetailsItem_iv_itemImg)
        AppCompatImageView iv_itemImg;

        @BindView(R.id.number_picker)
        NumberPicker number_picker;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
