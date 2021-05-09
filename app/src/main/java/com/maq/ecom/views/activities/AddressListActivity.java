package com.maq.ecom.views.activities;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.AddressListAdapter;
import com.maq.ecom.adapter.BannerAdapter;
import com.maq.ecom.adapter.CategoryItemsAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.DeleteAddress;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Address;
import com.maq.ecom.model.Banner;
import com.maq.ecom.model.Category;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.networking.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class AddressListActivity extends BaseActivity implements RetrofitRespondListener, DeleteAddress {

    String TAG = CategoryItemsActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager = new SessionManager(context);
    LoadingDialog loadingDialog;
    List<Address> arrayList = new ArrayList<>();
    AddressListAdapter adapter;
    MenuItem sortItem;
    boolean sortByAsc = false;
    Category category;
    int position;
    TextView menu_tv_cartCount;

    @BindView(R.id.addressAct_rv)
    RecyclerView recyclerView;

    @BindView(R.id.addressAct_tv_notFound)
    TextView tv_notFound;

    @OnClick(R.id.addressAct_fab)
    void addNewAddress() {
        Utils.navigateTo(context, AddAddressActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAddress();
//        if (menu_tv_cartCount != null)
//            setCartCounter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        super.setupToolbar("Address List");
        ButterKnife.bind(this);
        init();

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        if (!sessionManager.getIsLoggedIn())
            startActivityForResult(new Intent(context, LoginActivity.class), 200);

        loadingDialog = new LoadingDialog(context);
    }

    private void fetchAddress() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_getAddressList(sessionManager.getUserId());
        RetrofitClient.callRetrofit(apiCall, "ADDRESS", this);
    }

    private void delAddress(String id) {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_delAddress(id);
        RetrofitClient.callRetrofit(apiCall, "DEL_ADDRESS", this);
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
            case "ADDRESS":
                try {
                    callback(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "DEL_ADDRESS":
                try {
                    callbackDel(response);
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
                if (jsonObject.has("allbanks")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allbanks").getJSONArray("alladdlist");
                    if (jsonArray.length() > 0) {
                        arrayList.clear();
                        tv_notFound.setVisibility(View.INVISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("AddressId");
                            String userId = object.getString("UserId");
                            String address = object.getString("Address");
                            String city = object.getString("City");
                            String state = object.getString("State");
                            String pin = object.getString("PIN");
                            String addressType = object.getString("AddressType");
                            String altMobile = object.getString("AltMobile");
                            String latitude = object.getString("Latitude");
                            String longtitude = object.getString("Longtitude");
                            String isDefault = object.getString("isDefault");
                            String gstNo = object.getString("GSTNo");
                            String aadharNo = object.getString("AadharNo");

                            arrayList.add(new Address(id, userId, address, city, state, pin, addressType, altMobile, latitude, longtitude, isDefault, gstNo, aadharNo));
                        }
                        adapter = new AddressListAdapter(context, arrayList, this::onDeleteAddress);
                        recyclerView.setAdapter(adapter);
                    } else tv_notFound.setVisibility(View.VISIBLE);
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void callbackDel(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                arrayList.remove(position);
                adapter.notifyDataSetChanged();
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    @Override
    public void onDeleteAddress(Address address, int position) {
        this.position = position;
        delAddress(address.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode != Activity.RESULT_OK) {
                finish();
            }
        }
    }

//    void setCartCounter() {
//        if (MainActivity.mCartList.size() > 0) {
//            menu_tv_cartCount.setText(String.valueOf(MainActivity.mCartList.size()));
//            menu_tv_cartCount.setVisibility(View.VISIBLE);
//        } else menu_tv_cartCount.setVisibility(View.GONE);
//    }

}