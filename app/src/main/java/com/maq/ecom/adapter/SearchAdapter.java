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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.networking.RetrofitClient;
import com.maq.ecom.views.activities.CategoryItemDetailsActivity;

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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<CategoryItem> arrayList;
    List<CategoryItem> arrayListFull;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    AlertDialog dialog;
    int position = -1;
    String str_stock;

    public SearchAdapter(Context context, List<CategoryItem> arrayList) {
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
        CategoryItem model = arrayList.get(position);

        //bind data
//        Utils.loadImage(context, model.getCategoryItemCover(), holder.iv_img);
//        Utils.underlineTextView(holder.tv_updateStock);
        holder.tv_name.setText(model.getProductName());
        holder.tv_code.setText("(" + model.getProductCode() + ")");
        holder.tv_cat.setText(model.getCategoryName());
        holder.tv_cost.setText(context.getResources().getString(R.string.INR_symbol)+model.getSellingPrice());

        holder.layout.setOnClickListener(v ->
                context.startActivity(new Intent(context, CategoryItemDetailsActivity.class)
                        .putExtra("category_item", arrayList.get(position))
                )
        );
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



    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CategoryItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CategoryItem item : arrayListFull) {
                    if (item.getProductName().toLowerCase().contains(filterPattern) ||
                            item.getProductCode().toLowerCase().contains(filterPattern) ||
                            item.getSellingPrice().toLowerCase().contains(filterPattern) ||
                            item.getCategoryName().toLowerCase().contains(filterPattern) ||
                            item.getDescription().toLowerCase().contains(filterPattern) ||
                            item.getShortDesc().toLowerCase().contains(filterPattern)
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
        @BindView(R.id.product_name)
        TextView tv_name;
        @BindView(R.id.product_code)
        TextView tv_code;
        @BindView(R.id.product_cat)
        TextView tv_cat;
        @BindView(R.id.product_cost)
        TextView tv_cost;
//
//        @BindView(R.id.productItem_img)
//        AppCompatImageView iv_img;

        @BindView(R.id.productItem_layout)
        CardView layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
