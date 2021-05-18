package com.maq.ecom.views.activities;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.CartAdapter;
import com.maq.ecom.adapter.CategoryItemsAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.CartListener;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Category;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.networking.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CartActivity extends BaseActivity implements RetrofitRespondListener, CartListener {

    String TAG = CartActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    CartAdapter adapter;
    MenuItem sortItem;
    boolean sortByAsc = false;
    Category category;
    TextView menu_tv_cartCount;
    int index = 0;
    int position;

    @BindView(R.id.cartAct_rv)
    RecyclerView cartAct_rv;

    @BindView(R.id.cartAct_tv_noCart)
    TextView tv_notFound;
    @BindView(R.id.cartAct_tv_cartItems)
    TextView cartAct_tv_cartItems;
    @BindView(R.id.cartAct_tv_cartAmt)
    TextView cartAct_tv_cartAmt;

    @OnClick(R.id.cartAct_layout_confirmOrder)
    void confirmOrder() {
        if (MainActivity.mCartList.size() == 0)
            Utils.showToast(context, "Cart is empty");
        else {
            if (sessionManager.getIsLoggedIn()) {
                //insert cart into DB
                loadingDialog.show();
                requestAddToCart(index);
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (menu_tv_cartCount != null)
//            setCartCounter();
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        super.setupToolbar("Your Cart");
        ButterKnife.bind(this);
        init();
        getCatItems();

        if (sessionManager.getIsLoggedIn()) {
            fetchCatItems();
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
    }

    private void getCatItems() {
        setAdapter();
    }

    private void setAdapter() {
        if (MainActivity.mCartList.size() > 0) {
            tv_notFound.setVisibility(View.GONE);
            adapter = new CartAdapter(context, MainActivity.mCartList, this::onCartDelete);
            cartAct_rv.setAdapter(adapter);
        } else tv_notFound.setVisibility(View.VISIBLE);
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
//
//    void setCartCounter() {
//        if (MainActivity.mCartList.size() > 0) {
//            menu_tv_cartCount.setText(String.valueOf(MainActivity.mCartList.size()));
//            menu_tv_cartCount.setVisibility(View.VISIBLE);
//        } else menu_tv_cartCount.setVisibility(View.GONE);
//    }

    private void requestAddToCart(int index) {
        CategoryItem item = MainActivity.mCartList.get(index);
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_insertCart(sessionManager.getUserId(), sessionManager.getFirmId(), item.getProductId(), item.getPrice(), item.getSellingPrice(), String.valueOf(item.getQty()));
        RetrofitClient.callRetrofit(apiCall, "INSERT_CART", this);
    }

    private void fetchCatItems() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_cartList(sessionManager.getUserId());
        RetrofitClient.callRetrofit(apiCall, "CART_LIST", this);
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "INSERT_CART":
                try {
                    callback(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "CART_LIST":
                try {
                    callbackList(response);
                    loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "REMOVE_CART":
                try {
                    callbackDel(response);
                    loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }

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
                index = index + 1;
                if (index < MainActivity.mCartList.size())
                    requestAddToCart(index);
                else {
                    loadingDialog.dismiss();
                    Utils.showToast(context, "Cart saved successfully!");
                    context.startActivity(new Intent(context, AddressListActivity.class)
                            .putExtra("cart", (Serializable) MainActivity.mCartList)
                    );
                }

            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void callbackList(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allcartlist")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allcartlist").getJSONArray("allcartlist");
                    if (jsonArray.length() > 0) {
                        MainActivity.mCartList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
//                            String type = object.getString("Type");
                            String productId = object.getString("ProductId");
                            String productCode = object.getString("ProductCode");
                            String productName = object.getString("ProductName");
                            String price = object.getString("Price");
//                            String discount = object.getString("Discount");
                            String sellingPrice = object.getString("SellingPrice");
                            String shortDesc = object.getString("ShortDesc");
                            String stock = object.getString("Stock");
                            String ProductCover = object.getString("ProductCover");
                            String description = object.getString("Description");
                            String image1 = object.getString("Image1");
                            String image2 = object.getString("Image2");
                            String image3 = object.getString("Image3");
                            String image4 = object.getString("Image4");
                            String image5 = object.getString("Image5");
                            String image6 = object.getString("Image6");

                            MainActivity.mCartList.add(new CategoryItem(null, productId, productCode, productName, price, "0", sellingPrice, shortDesc, stock, ProductCover, description, image1, image2, image3, image4, image5, image6));
                        }

                        setAdapter();
                    }
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    @Override
    public void onCartDelete(CategoryItem model, int position) {
        this.position = position;
        if (sessionManager.getIsLoggedIn())
            removeCartItem(model);
        else {
            MainActivity.mCartList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    private void removeCartItem(CategoryItem item) {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_removeCart(sessionManager.getUserId(), item.getProductId());
        RetrofitClient.callRetrofit(apiCall, "REMOVE_CART", this);
    }

    private void callbackDel(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                MainActivity.mCartList.remove(position);
                adapter.notifyDataSetChanged();
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

}