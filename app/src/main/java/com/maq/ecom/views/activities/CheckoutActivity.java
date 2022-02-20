package com.maq.ecom.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.CartAdapter;
import com.maq.ecom.adapter.CheckoutAdapter;
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

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CheckoutActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = CartActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    CheckoutAdapter adapter;


    String addressId;
    boolean enableOnline = false;
    String description;
    double percentage;

    double subTotal = 0;
    double advPayment = 1;
    double fullPayment = 0;
    int qty = 0;

    @BindView(R.id.checkoutAct_rv)
    RecyclerView checkoutAct_rv;

    @BindView(R.id.checkoutAct_tv_noCart)
    TextView tv_notFound;
    @BindView(R.id.checkoutAct_tv_desc)
    TextView checkoutAct_tv_desc;
    @BindView(R.id.checkoutAct_tv_subTotal)
    TextView checkoutAct_tv_subTotal;
    @BindView(R.id.checkoutAct_tv_adv)
    TextView checkoutAct_tv_adv;
    @BindView(R.id.checkoutAct_tv_finalAmt)
    TextView checkoutAct_tv_finalAmt;
    @BindView(R.id.checkoutAct_tv_pay)
    TextView checkoutAct_tv_pay;
    @BindView(R.id.checkoutAct_tv_stats)
    TextView checkoutAct_tv_stats;

    @BindView(R.id.checkoutAct_et_remarks)
    EditText checkoutAct_et_remarks;

    @OnClick(R.id.checkoutAct_btn_pay)
    void actionPay() {
        new AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Are you want to place order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        placeNewOrder(makePayload());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        super.setupToolbar("Checkout");
        ButterKnife.bind(this);
        init();
        fetchCheckoutSettings();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
        addressId = getIntent().getStringExtra("addressId");
    }


    private void fetchCheckoutSettings() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_checkoutSetting(sessionManager.getUserId());
        RetrofitClient.callRetrofit(apiCall, "CHECKOUT_SETTINGS", this);
    }

    private JsonObject makePayload() {
        JsonArray items = new JsonArray();
        for (int i = 0; i < MainActivity.mCartList.size(); i++) {
            CategoryItem item = MainActivity.mCartList.get(i);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("SNo", String.valueOf(i + 1));
            jsonObject.addProperty("ProductId", item.getProductId());
            jsonObject.addProperty("Qty", String.valueOf(item.getQty()));
            jsonObject.addProperty("Unit", item.getUnit());
            jsonObject.addProperty("Price", item.getPrice());
            jsonObject.addProperty("SellingPrice", item.getSellingPrice());
            jsonObject.addProperty("Amount", String.valueOf(Double.parseDouble(String.valueOf(item.getQty())) * Double.parseDouble(item.getSellingPrice())));
            jsonObject.addProperty("ProductName", item.getProductName());

            items.add(jsonObject);
        }


        JsonObject jsonObject = new JsonObject();
        jsonObject.add("items", items);
        return jsonObject;
    }


    private void placeNewOrder(JsonObject jsonObject) {
        loadingDialog.show();
        String orderDate = Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy-MM-dd");
        String nag = String.valueOf(MainActivity.mCartList.size());
        String remarks = checkoutAct_et_remarks.getText().toString();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_placeNewOrder(sessionManager.getFirmId(), orderDate, sessionManager.getUserId(), String.valueOf(fullPayment), nag, "0", "BLUE", "0", remarks, String.valueOf(subTotal), String.valueOf(advPayment), String.valueOf(qty), addressId,
                        jsonObject);
        RetrofitClient.callRetrofit(apiCall, "PLACE_ORDER", this);
    }

    private void getCatItems() {
        setAdapter();
    }

    @SuppressLint("SetTextI18n")
    private void setAdapter() {
        if (MainActivity.mCartList.size() > 0) {
            tv_notFound.setVisibility(View.GONE);
            adapter = new CheckoutAdapter(context, MainActivity.mCartList);
            checkoutAct_rv.setAdapter(adapter);

            for (CategoryItem item : MainActivity.mCartList) {
                subTotal = subTotal + Double.parseDouble(item.getSellingPrice()) * item.getQty();
                qty = qty + item.getQty();
            }

            advPayment = subTotal * (percentage / 100);
            fullPayment = subTotal - advPayment;

            checkoutAct_tv_desc.setText(description);
            checkoutAct_tv_subTotal.setText(getResources().getString(R.string.INR_symbol) + subTotal);
            checkoutAct_tv_adv.setText(getResources().getString(R.string.INR_symbol) + advPayment);
            checkoutAct_tv_finalAmt.setText(getResources().getString(R.string.INR_symbol) + fullPayment);
            checkoutAct_tv_pay.setText(getResources().getString(R.string.INR_symbol) + fullPayment);
            checkoutAct_tv_stats.setText(MainActivity.mCartList.size() + " Nag / " + qty + " Item(s)");

        } else tv_notFound.setVisibility(View.VISIBLE);
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "CHECKOUT_SETTINGS":
                try {
                    callback(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "PLACE_ORDER":
                try {
                    callbackPlaceOrder(response);
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
                JSONObject object = jsonObject.getJSONArray("setting").getJSONObject(0);
                enableOnline = object.getString("EnableOnline").equals("1");
                description = object.getString("Description");
                percentage = Double.parseDouble(object.getString("OnlinePerc"));
                getCatItems();

            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    @SuppressLint("SetTextI18n")
    private void callbackPlaceOrder(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                context.startActivity(new Intent(context, SuccessActivity.class));
            } else
                Toast.makeText(context, jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
        } else Utils.showToast(context, String.valueOf(responseCode));
    }
}