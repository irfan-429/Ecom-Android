package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.model.MyOrder;
import com.maq.ecom.views.activities.MainActivity;
import com.maq.ecom.views.activities.MyOrderDetailsActivity;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * developed by irfan A.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    Context context;
    List<MyOrder> arrayList;

    public MyOrderAdapter(Context context, List<MyOrder> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_order, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MyOrder model = arrayList.get(position);

        //bind data
        holder.tv_num.setText("Order No " + model.getOrderNo());
        holder.tv_stats.setText(model.getNag() + " / " + model.getItems());
        holder.tv_adv.setText("Advance " + context.getResources().getString(R.string.INR_symbol) + model.getAdvance());
        holder.tv_date.setText("Order placed on " + model.getOrderDate());
        holder.tv_amt.setText("For amount " + context.getResources().getString(R.string.INR_symbol) + model.getOrderAmount());

        holder.btn_received.setOnClickListener(v -> {
            context.startActivity(new Intent(context, MyOrderDetailsActivity.class));
        });


        holder.btn_received.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you have received the order?")
                    .setPositiveButton("Received", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
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

        @BindView(R.id.myOrderItem_num)
        TextView tv_num;
        @BindView(R.id.myOrderItem_stats)
        TextView tv_stats;
        @BindView(R.id.myOrderItem_adv)
        TextView tv_adv;
        @BindView(R.id.myOrderItem_date)
        TextView tv_date;
        @BindView(R.id.myOrderItem_amt)
        TextView tv_amt;
        @BindView(R.id.checkoutAct_tv_viewDetails)
        TextView tv_viewDetails;

        @BindView(R.id.checkoutAct_btn_received)
        Button btn_received;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
