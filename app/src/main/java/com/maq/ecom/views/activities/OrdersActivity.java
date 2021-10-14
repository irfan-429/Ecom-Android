package com.maq.ecom.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.SectionsPagerAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.CustomDatePicker;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.MyOrder;
import com.maq.ecom.networking.RetrofitClient;
import com.maq.ecom.views.fragments.orders.MyOrdersFragment;

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

//admin side
public class OrdersActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = CartActivity.class.getSimpleName();
    SessionManager sessionManager = new SessionManager(this);
    Context context = this;
    LoadingDialog loadingDialog;

    List<MyOrder> allOrders = new ArrayList<>();
    List<MyOrder> pendingOrders = new ArrayList<>();
    List<MyOrder> processingOrders = new ArrayList<>();
    List<MyOrder> shippedOrders = new ArrayList<>();
    List<MyOrder> deliveredOrders = new ArrayList<>();
    List<MyOrder> cancelledOrders = new ArrayList<>();

    TabLayout tabLayout;

    @BindView(R.id.layout_fromDate)
    CardView layoutFromDate;
    @BindView(R.id.layout_toDate)
    CardView layoutToDate;

    @BindView(R.id.tv_fromDate)
    TextView tv_fromDate;
    @BindView(R.id.tv_toDate)
    TextView tv_toDate;

    String fromDate, toDate, currentDate;


    @OnClick(R.id.iv_reload)
    void reloadRecord() {
        fromDate = tv_fromDate.getText().toString();
        toDate = tv_toDate.getText().toString();
        fetchMyOrders();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        super.setupToolbar("Orders");
        ButterKnife.bind(this);

        if (!sessionManager.getIsLoggedIn())
            startActivityForResult(new Intent(context, LoginActivity.class), 200);


        init();
        fetchMyOrders();
        listener();
    }

    private void initTabs() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new MyOrdersFragment(allOrders), "All");
        adapter.addFragment(new MyOrdersFragment(pendingOrders), "Pending");
        adapter.addFragment(new MyOrdersFragment(processingOrders), "Processing");
        adapter.addFragment(new MyOrdersFragment(shippedOrders), "Shipped");
        adapter.addFragment(new MyOrdersFragment(deliveredOrders), "Delivered");
        adapter.addFragment(new MyOrdersFragment(cancelledOrders), "Cancelled");

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //change color selected/un-selected
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density)); //change size of indicator
        tabLayout.setTabTextColors(Color.parseColor("#727272"), getResources().getColor(R.color.colorPrimary));


        toDate = Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy-MM-dd");
        //from date= current date - 5 days
        fromDate = Utils.addDays(-5);

        tv_fromDate.setText(fromDate);
        tv_toDate.setText(toDate);

    }


    private void listener() {
        layoutFromDate.setOnClickListener(v -> new CustomDatePicker(context, tv_fromDate));
        layoutToDate.setOnClickListener(v -> new CustomDatePicker(context, tv_toDate));

    }

    private void init() {
        loadingDialog = new LoadingDialog(context);
    }

    private void fetchMyOrders() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_orders(sessionManager.getFirmId(), fromDate, toDate);
        RetrofitClient.callRetrofit(apiCall, "ORDERS", this);
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


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "ORDERS":
                try {
                    callbackList(response);
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

    private void callbackList(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allorderlist")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allorderlist").getJSONArray("allorderlist");
                    if (jsonArray.length() > 0) {
                        allOrders.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String orderNo = object.getString("OrderNo");
                            String nag = object.getString("Nag");
                            String items = object.getString("Items");
                            String orderDate = object.getString("OrderDate");
                            String itemAmount = object.getString("ItemAmount");
                            String delCharge = object.getString("DelCharge");
                            String disc = object.getString("Disc");
                            String total = object.getString("Total");
                            String advance = object.getString("Advance");
                            String address = object.getString("Address");
                            String orderAmount = object.getString("OrderAmount");
                            String city = object.getString("City");
                            String state = object.getString("State");
                            String pin = object.getString("Pin");
                            String gstNo = object.getString("GSTNo");
                            String aadharNo = object.getString("AadharNo");
                            String name = object.getString("Name");
                            String emailId = object.getString("EmailId");
                            String mobileNo = object.getString("MobileNo");
                            String status = object.getString("Status");

                            MyOrder myOrder = new MyOrder(orderNo, nag, items, orderDate, itemAmount, delCharge, disc, total, advance, orderAmount, address, city, state, pin, gstNo, aadharNo, name, emailId, mobileNo, status);
                            allOrders.add(myOrder);
                            switch (status) {
                                case "Pending":
                                    pendingOrders.add(myOrder);
                                    break;

                                case "Processing":
                                    processingOrders.add(myOrder);
                                    break;

                                case "Shipped":
                                    shippedOrders.add(myOrder);
                                    break;

                                case "Delivered":
                                    deliveredOrders.add(myOrder);
                                    break;

                                case "Cancelled":
                                    cancelledOrders.add(myOrder);
                                    break;
                            }

                        }
                    }
                }
            }
            Log.i(TAG, "callbackList: " + allOrders.size());
            initTabs();
        } else Utils.showToast(context, String.valueOf(responseCode));
    }
}