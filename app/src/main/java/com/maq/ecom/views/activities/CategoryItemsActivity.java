package com.maq.ecom.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.CategoryItemGridAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.ItemListener;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Category;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.networking.RetrofitClient;
import com.rizlee.rangeseekbar.RangeSeekBar;

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

public class CategoryItemsActivity extends BaseActivity implements RetrofitRespondListener , ItemListener {

    String TAG = CategoryItemsActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    List<CategoryItem> arrayList = new ArrayList<>();
    CategoryItemGridAdapter adapter;
    MenuItem sortItem;
    boolean sortByAsc = false;
    Category category;
    TextView menu_tv_cartCount;


    @BindView(R.id.catItemAct_gv)
    GridView gridView;

    @BindView(R.id.catItemAct_tv_notFound)
    TextView tv_notFound;
    @BindView(R.id.catItemAct_top_name)
    TextView catItemAct_top_name;

    @BindView(R.id.sort_tv)
    TextView tv_sort;
    @BindView(R.id.tv_filter)
    TextView tv_filter;

    @BindView(R.id.catItemAct_top_img)
    AppCompatImageView catItemAct_top_img;


    @Override
    protected void onResume() {
        super.onResume();
//        if (menu_tv_cartCount != null)
//            setCartCounter();

        if (adapter != null) {
            setAdapter(arrayList);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);
        ButterKnife.bind(this);
        init();
        fetchCatItems();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
        category = (Category) getIntent().getSerializableExtra("category");
        super.setupToolbar(category.getCategoryName());

    }

    private void fetchCatItems() {
        loadingDialog.show();
        Log.i(TAG, "fetchCatItems: "+category.getCategoryId());
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_getCategoryItems(sessionManager.getFirmId(), category.getCategoryId());
        RetrofitClient.callRetrofit(apiCall, "ITEMS", this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_cart, menu);
//        final MenuItem menuItem = menu.findItem(R.id.action_cart);
//
//        View actionView = menuItem.getActionView();
//        menu_tv_cartCount = (TextView) actionView.findViewById(R.id.menu_tv_notiBadge);
//        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
//        setCartCounter();
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_cart:
//                Utils.navigateTo(context, CartActivity.class);
//                break;
//
//            case R.id.action_search:
//                Utils.navigateTo(context, SearchActivity.class);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


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
            String categoryBanner = null, categoryName = "";
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
                            categoryName = object.getString("CategoryName");
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
                            String unit = object.getString("Unit");
                            categoryBanner = object.getString("CategoryBanner");

                            arrayList.add(new CategoryItem(productId, firmId, productCode, productName, categoryId, categoryName,
                                    price, discount, sellingPrice, shortDesc, description, status, isFeatured, isNew, isPopular,
                                    productCover, image1, image2, image3, image4, image5, image6, keyFeatures, isSize, stock, unit, categoryBanner));
                        }

                        setAdapter(arrayList);

                        if (categoryBanner != null && !categoryBanner.isEmpty()) {
                            catItemAct_top_img.setVisibility(View.VISIBLE);
                            Utils.loadImage(context, categoryBanner, catItemAct_top_img);
                        }

                        catItemAct_top_name.setText(categoryName);
                        Utils.underlineTextView(catItemAct_top_name);


                    } else tv_notFound.setVisibility(View.VISIBLE);
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void setAdapter(List<CategoryItem> arrayList) {
        adapter = new CategoryItemGridAdapter(context, arrayList, this::onItemClick);
        gridView.setAdapter(adapter);
    }

//    void setCartCounter() {
//        if (MainActivity.mCartList.size() > 0) {
//            menu_tv_cartCount.setText(String.valueOf(MainActivity.mCartList.size()));
//            menu_tv_cartCount.setVisibility(View.VISIBLE);
//        } else menu_tv_cartCount.setVisibility(View.GONE);
//    }

    public void actionSort(View view) {
        showSortDialog(R.layout.dialog_sort);
    }

    public void actionFilter(View view) {
        showFilterDialog(R.layout.dialog_filter);
    }

    private void showSortDialog(int layoutId) {
        ViewGroup viewGroup = ((Activity) context).findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        //custom dialog
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();

        //handle sys nav setting
//        dialog.setOnKeyListener((arg0, keyCode, event) -> {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                //disable back press on lock screen
//            }
//            return true;
//        });

        TextView product_name_AZ = dialogView.findViewById(R.id.product_name_AZ);
        product_name_AZ.setOnClickListener(v -> {
            Collections.sort(arrayList, (o1, o2) -> o1.getProductName().compareToIgnoreCase(o2.getProductName()));
            setAdapter(arrayList);
            dialog.dismiss();
            tv_sort.setText(product_name_AZ.getText());

        });

        TextView product_name_ZA = dialogView.findViewById(R.id.product_name_ZA);
        product_name_ZA.setOnClickListener(v -> {
            Collections.sort(arrayList, (o1, o2) -> o2.getProductName().compareToIgnoreCase(o1.getProductName()));
            setAdapter(arrayList);
            dialog.dismiss();
            tv_sort.setText(product_name_ZA.getText());
        });

        TextView price_LH = dialogView.findViewById(R.id.price_LH);
        price_LH.setOnClickListener(v -> {
            Collections.sort(arrayList, (o1, o2) -> o1.getSellingPrice().compareToIgnoreCase(o2.getSellingPrice()));
            setAdapter(arrayList);
            dialog.dismiss();
            tv_sort.setText(price_LH.getText());
        });

        TextView price_HL = dialogView.findViewById(R.id.price_HL);
        price_HL.setOnClickListener(v -> {
            Collections.sort(arrayList, (o1, o2) -> o2.getSellingPrice().compareToIgnoreCase(o1.getSellingPrice()));
            setAdapter(arrayList);
            dialog.dismiss();
            tv_sort.setText(price_HL.getText());
        });

        TextView code_asc = dialogView.findViewById(R.id.code_asc);
        code_asc.setOnClickListener(v -> {
            Collections.sort(arrayList, (o1, o2) -> o1.getProductCode().compareToIgnoreCase(o2.getProductCode()));
            setAdapter(arrayList);
            dialog.dismiss();
            tv_sort.setText(code_asc.getText());
        });

        TextView code_desc = dialogView.findViewById(R.id.code_desc);
        code_desc.setOnClickListener(v -> {
            Collections.sort(arrayList, (o1, o2) -> o2.getProductCode().compareToIgnoreCase(o1.getProductCode()));
            setAdapter(arrayList);
            dialog.dismiss();
            tv_sort.setText(code_desc.getText());
        });


    }

    float minPrice = 0f;
    float maxPrice = 0f;

    @SuppressLint("SetTextI18n")
    private void showFilterDialog(int layoutId) {
        ViewGroup viewGroup = ((Activity) context).findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        //custom dialog
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();

        //handle sys nav setting
//        dialog.setOnKeyListener((arg0, keyCode, event) -> {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                //disable back press on lock screen
//            }
//            return true;
//        });

        RangeSeekBar sk_priceRange = dialogView.findViewById(R.id.sk_priceRange);


        if (maxPrice != 0f)
            sk_priceRange.setCurrentValues(minPrice, maxPrice);

        dialogView.findViewById(R.id.btn_apply).setOnClickListener(v -> {
            minPrice = sk_priceRange.getCurrentValues().component1();
            maxPrice = sk_priceRange.getCurrentValues().component2();

            String INR = context.getResources().getString(R.string.INR_symbol);
            tv_filter.setText(INR + minPrice + " - " + INR + maxPrice);

            setAdapter(multipleFilter(minPrice, maxPrice, arrayList));
            dialog.dismiss();
        });


    }

    public List<CategoryItem> multipleFilter(float filterMinPrice, float filterMaxPrice, List<CategoryItem> list) {
        List<CategoryItem> listAfterFiltering = new ArrayList<>();
        for (CategoryItem item : list) {
            float cost = Utils.extractFloatPart(item.getSellingPrice());

            if (cost > filterMinPrice && cost < filterMaxPrice) {
                listAfterFiltering.add(item);
            }
        }

        return listAfterFiltering;
    }

    @Override
    public void onItemClick() {
        setCartCounter();
    }
}