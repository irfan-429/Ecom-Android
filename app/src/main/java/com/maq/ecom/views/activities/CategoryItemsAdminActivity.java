package com.maq.ecom.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.CategoryItemsAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Category;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.networking.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class CategoryItemsAdminActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = CategoryItemsAdminActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    List<CategoryItem> arrayList = new ArrayList<>();
    CategoryItemsAdapter adapter;
    MenuItem sortItem;
    boolean sortByAsc = false;
    Category category;


    @BindView(R.id.catItemAct_rv)
    RecyclerView recyclerView;

    @BindView(R.id.catItemAct_tv_notFound)
    TextView tv_notFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);
        super.setupToolbar("Items");
        ButterKnife.bind(this);
        init();
        fetchCatItems();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
        category = (Category) getIntent().getSerializableExtra("category");

    }

    private void fetchCatItems() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_getCategoryItems(sessionManager.getFirmId(), category.getCategoryId());
        RetrofitClient.callRetrofit(apiCall, "ITEMS", this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        sortItem = menu.findItem(R.id.action_sort_by);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.str_search_by_anything));
        searchView.setBackground(null);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (adapter != null) adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by:
                if (!sortByAsc) {
                    Collections.sort(arrayList, (o1, o2) -> o1.getCategoryName().compareToIgnoreCase(o2.getCategoryName()));
                    sortByAsc = true;
                    sortItem.setTitle(getResources().getString(R.string.action_sort_ZA));
                } else {
                    Collections.sort(arrayList, (o1, o2) -> o2.getCategoryName().compareToIgnoreCase(o2.getCategoryName()));
                    sortByAsc = false;
                    sortItem.setTitle(getResources().getString(R.string.action_sort_AZ));
                }
                adapter = new CategoryItemsAdapter(context, arrayList);
                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "ITEMS":
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
        if (responseError.contains(getString(R.string.str_unable_to_resolve_host)))
            responseError = getString(R.string.str_no_internet);

        Utils.showSnackBar(context, responseError);
    }

    @SuppressLint("SetTextI18n")
    private void callback(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allitems")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allitems").getJSONArray("allitems");
                    if (jsonArray.length() > 0) {
                        arrayList.clear();
                        tv_notFound.setVisibility(View.INVISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String productId = object.getString("ProductId");
                            String firmId = object.getString("FirmId");
                            String productCode = object.getString("ProductCode");
                            String productName = object.getString("ProductName");
                            String categoryId = object.getString("CategoryId");
                            String categoryName = object.getString("CategoryName");
                            String price = object.getString("Price");
                            String discount = object.getString("Discount");
                            String sellingPrice = object.getString("SellingPrice");
                            String shortDesc = object.getString("ShortDesc");
                            String description = object.getString("Description");
                            String status = object.getString("Status");
                            String isFeatured = object.getString("isFeatured");
                            String isNew = object.getString("isNew");
                            String isPopular = object.getString("isPopular");
                            String productCover = object.getString("ProductCover");
                            String image1 = object.getString("Image1");
                            String image2 = object.getString("Image2");
                            String image3 = object.getString("Image3");
                            String image4 = object.getString("Image4");
                            String image5 = object.getString("Image5");
                            String image6 = object.getString("Image6");
                            String keyFeatures = object.getString("KeyFeatures");
                            String isSize = object.getString("isSize");
                            String stock = object.getString("Stock");

                            arrayList.add(new CategoryItem(productId, firmId, productCode, productName, categoryId, categoryName,
                                    price, discount, sellingPrice, shortDesc, description, status, isFeatured, isNew, isPopular,
                                    productCover, image1, image2, image3, image4, image5, image6, keyFeatures, isSize, stock));
                        }
                        adapter = new CategoryItemsAdapter(context, arrayList);
                        recyclerView.setAdapter(adapter);
                    } else tv_notFound.setVisibility(View.VISIBLE);
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }


}