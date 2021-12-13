package com.maq.ecom.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.SearchAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Category;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.model.Product;
import com.maq.ecom.networking.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = CategoryItemsActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager = new SessionManager(context);
    LoadingDialog loadingDialog;
    List<CategoryItem> arrayList = new ArrayList<>();
    SearchAdapter adapter;
    MenuItem sortItem;
    boolean sortByAsc = false;
    Category category;
    int position;
    TextView menu_tv_cartCount;
    SearchView searchView;

    @BindView(R.id.productAct_rv)
    RecyclerView recyclerView;

    @BindView(R.id.productAct_tv_notFound)
    TextView tv_notFound;

    @Override
    protected void onResume() {
        super.onResume();
//        if (menu_tv_cartCount != null)
//            setCartCounter();

//        if (adapter!=null) {
//            adapter =new CategoryItemGridAdapter(context, arrayList);
//            gridView.setAdapter(adapter);
//        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        super.hideLogo();
        ButterKnife.bind(this);
        init();
        fetchProducts();
    }

    private void init() {
        loadingDialog = new LoadingDialog(context);
    }

    private void fetchProducts() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_getProductList(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "PRODUCTS", this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_item);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.str_search_by_anything));
        searchView.setFocusable(true);
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
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "PRODUCTS":
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
                if (jsonObject.has("allproducts")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allproducts").getJSONArray("allproductlist");
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
//                            String unit = object.getString("Unit");
                            String categoryBanner = object.getString("CategoryBanner");

                            arrayList.add(new CategoryItem(productId, firmId, productCode, productName, categoryId, categoryName,
                                    price, discount, sellingPrice, shortDesc, description, status, isFeatured, isNew, isPopular,
                                    productCover, image1, image2, image3, image4, image5, image6, keyFeatures, isSize, stock, "", categoryBanner));
                        }
                        adapter = new SearchAdapter(context, arrayList);
                        recyclerView.setAdapter(adapter);
                        searchView.setIconified(false);


                    } else tv_notFound.setVisibility(View.VISIBLE);
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

}