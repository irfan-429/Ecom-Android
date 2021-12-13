package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.OrderStatusListener;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.MyOrder;
import com.maq.ecom.networking.RetrofitClient;
import com.maq.ecom.views.activities.MyOrderDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * developed by irfan A.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> implements RetrofitRespondListener {

    SessionManager sessionManager;
    Context context;
    List<MyOrder> arrayList;
    LoadingDialog loadingDialog;
    MyViewHolder mHolder;
    OrderStatusListener listener;
    String status;

    public MyOrderAdapter(Context context, List<MyOrder> arrayList, OrderStatusListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        sessionManager = new SessionManager(context);
        loadingDialog = new LoadingDialog(context);
        this.listener = listener;
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

        holder.tv_viewDetails.setOnClickListener(v -> {
            String orderId = arrayList.get(position).getOrderNo();
            context.startActivity(new Intent(context, MyOrderDetailsActivity.class)
                    .putExtra("order_id", orderId));
        });


        if (sessionManager.isAdmin()) {
            if (model.getStatus().equalsIgnoreCase("Pending")) {
                holder.btn_actions.setVisibility(View.VISIBLE);
            }

        }


        holder.btn_reject.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Reject Order")
                    .setMessage("Are you sure you want to reject the order?")
                    .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mHolder = holder;
                            status = "Cancelled";
                            updateOrderStatus(arrayList.get(position).getOrderNo(), "Cancelled");
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.btn_accept.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Accept Order")
                    .setMessage("Are you sure you want to accept the order?")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mHolder = holder;
                            status = "Processing";
                            updateOrderStatus(arrayList.get(position).getOrderNo(), "Processing");
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });


    }

    private void updateOrderStatus(String orderId, String status) {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_updateOrderStatus(sessionManager.getFirmId(), orderId, status);
        RetrofitClient.callRetrofit(apiCall, "STATUS_UPDATE", this);
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
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "STATUS_UPDATE":
                try {
                    callback(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        loadingDialog.dismiss();

    }

    @Override
    public void onRetrofitFailure(String responseError, String requestName) {
        loadingDialog.dismiss();
        if (responseError.contains(context.getString(R.string.str_unable_to_resolve_host)))
            responseError = context.getString(R.string.str_no_internet);

        Utils.showSnackBar(context, responseError);
    }

    private void callback(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.getString("status").equals("true")) {

                    Utils.showToast(context, status.equals("Processing") ? "Order Accepted!!" : "Order Rejected!");
                    listener.onStatusUpdate(status, mHolder.getAdapterPosition());
                }
            }

        } else Utils.showToast(context, String.valueOf(responseCode));
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
//        @BindView(R.id.checkoutAct_btn_received)
//        Button btn_received;
        @BindView(R.id.checkoutAct_btn_reject)
        Button btn_reject;
        @BindView(R.id.checkoutAct_btn_accept)
        Button btn_accept;

        @BindView(R.id.checkoutAct_btn_actions)
        LinearLayoutCompat btn_actions;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
