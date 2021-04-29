package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Product;
import com.maq.ecom.networking.RetrofitClient;
import com.maq.ecom.views.activities.CreateProductActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * developed by irfan A.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> implements Filterable, RetrofitRespondListener {

    Context context;
    List<Product> arrayList;
    List<Product> arrayListFull;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    AlertDialog dialog;
    int position = -1;
    String str_stock;

    public ProductAdapter(Context context, List<Product> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListFull = new ArrayList<>(arrayList);
        sessionManager = new SessionManager(context);
        loadingDialog = new LoadingDialog(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product model = arrayList.get(position);

        //bind data
        Utils.loadImage(context, model.getProductCover(), holder.iv_img);
        Utils.underlineTextView(holder.tv_updateStock);
        holder.tv_name.setText(model.getProductName());
        holder.tv_cost.setText(model.getPrice());
        holder.tv_status.setText(model.getStatus());
        holder.tv_stock.setText(model.getStock());

        holder.ll_updateStock.setOnClickListener(v -> {
            //open dialog to edti stock
            this.position = position;
            openDialog(arrayList.get(position));
        });

        holder.layout.setOnClickListener(v ->
                context.startActivity(new Intent(context, CreateProductActivity.class)
                        .putExtra("product", arrayList.get(position))
                        .putExtra("product_edit", true)
                )
        );
    }

    private void openDialog(Product product) {
        final EditText taskEditText = new EditText(context);
        taskEditText.setText(product.getStock());
        dialog = new AlertDialog.Builder(context)
                .setTitle("Update Stock")
                .setView(taskEditText)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        str_stock = String.valueOf(taskEditText.getText());
                        requestStockUpdate(product, str_stock);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
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

    private void requestStockUpdate(Product product, String str_stock) {
        loadingDialog.show(); //show loader
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_updateStock(sessionManager.getFirmId(), str_stock, product.getProductId());
        RetrofitClient.callRetrofit(apiCall, "UPDATE", this);
    }

    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "UPDATE":
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
                dialog.dismiss();
                arrayList.get(position).setStock(str_stock);
                notifyDataSetChanged();
            }
            Utils.showToast(context, jsonObject.getString("error_msg"));
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product item : arrayListFull) {
                    if (item.getProductName().toLowerCase().contains(filterPattern) ||
                            item.getStatus().toLowerCase().contains(filterPattern)
                    ) {
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
        @BindView(R.id.productItem_name)
        TextView tv_name;
        @BindView(R.id.productItem_cost)
        TextView tv_cost;
        @BindView(R.id.productItem_status)
        TextView tv_status;
        @BindView(R.id.productItem_stock)
        TextView tv_stock;
        @BindView(R.id.productItem_updateStock)
        TextView tv_updateStock;

        @BindView(R.id.productItem_ll_updateStock)
        LinearLayout ll_updateStock;

        @BindView(R.id.productItem_img)
        AppCompatImageView iv_img;

        @BindView(R.id.productItem_layout)
        CardView layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
