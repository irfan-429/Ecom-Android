package com.maq.ecom.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.CartAdapter;
import com.maq.ecom.adapter.MyOrderDetailsAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.MyOrder;
import com.maq.ecom.model.OrderDetailsItem;
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

public class MyOrderDetailsActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = MyOrderDetailsActivity.class.getSimpleName();
    SessionManager sessionManager = new SessionManager(this);
    Context context = this;
    LoadingDialog loadingDialog;

    List<OrderDetailsItem> orderDetailsItemList = new ArrayList<>();


    @BindView(R.id.myOrderDetailsAct_rv)
    RecyclerView orderDetailsAct_rv;

    @BindView(R.id.indicator)
    StepperIndicator indicator;

    @BindView(R.id.myOrderDetailsAct_tv_orderId)
    TextView myOrderDetailsAct_tv_orderId;
    @BindView(R.id.myOrderDetailsAct_tv_orderDate)
    TextView myOrderDetailsAct_tv_orderDate;
    @BindView(R.id.myOrderDetailsAct_tv_itemsAmt)
    TextView myOrderDetailsAct_tv_itemsAmt;
    @BindView(R.id.myOrderDetailsAct_tv_delCharges)
    TextView myOrderDetailsAct_tv_delCharges;
    @BindView(R.id.myOrderDetailsAct_tv_discount)
    TextView myOrderDetailsAct_tv_discount;
    @BindView(R.id.myOrderDetailsAct_tv_total)
    TextView myOrderDetailsAct_tv_total;
    @BindView(R.id.myOrderDetailsAct_tv_adv)
    TextView myOrderDetailsAct_tv_adv;
    @BindView(R.id.myOrderDetailsAct_tv_finalAmt)
    TextView myOrderDetailsAct_tv_finalAmt;
    @BindView(R.id.myOrderDetailsAct_tv_name)
    TextView myOrderDetailsAct_tv_name;
    @BindView(R.id.myOrderDetailsAct_tv_mob)
    TextView myOrderDetailsAct_tv_mob;
    @BindView(R.id.myOrderDetailsAct_tv_address)
    TextView myOrderDetailsAct_tv_address;

    @BindView(R.id.myOrderDetailsAct_tv_stPlaced_time)
    TextView myOrderDetailsAct_tv_stPlaced_time;
    @BindView(R.id.myOrderDetailsAct_tv_stProcessed_time)
    TextView myOrderDetailsAct_tv_stProcessed_time;
    @BindView(R.id.myOrderDetailsAct_tv_stShipped_time)
    TextView myOrderDetailsAct_tv_stShipped_time;
    @BindView(R.id.myOrderDetailsAct_tv_stDelivered_time)
    TextView myOrderDetailsAct_tv_stDelivered_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);
        ButterKnife.bind(this);

        init();
        fetchOrderDetails();
    }


    private void init() {
        loadingDialog = new LoadingDialog(context);
    }

    private void fetchOrderDetails() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_orderDetail(getIntent().getStringExtra("order_id"));
        RetrofitClient.callRetrofit(apiCall, "ORDER_DETAILS", this);
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "ORDER_DETAILS":
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
                if (jsonObject.has("orderdetail")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("orderdetail");

                    if (jsonArray.length() > 0) {
                        orderDetailsItemList.clear();

                        String orderId = "", orderDate = "", delCharge = "", disc = "", subTotal = "", advance = "", address = "", orderAmount = "", city = "", state = "", pin = "", altMobile = "", status = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            orderId = object.getString("OrderId");
                            String nag = object.getString("Nag");
                            orderDate = object.getString("OrderDate");
                            delCharge = object.getString("DelCharge");
                            String promoCode = object.getString("PromoCode");
                            disc = object.getString("Disc");
                            String remarks = object.getString("Remarks");
                            subTotal = object.getString("SubTotal");
                            advance = object.getString("Advance");
                            String noOfItems = object.getString("NoOfItems");
                            String addressId = object.getString("AddressId");
                            address = object.getString("Address");
                            orderAmount = object.getString("OrderAmount");
                            city = object.getString("City");
                            state = object.getString("State");
                            pin = object.getString("PIN");
                            String addressType = object.getString("AddressType");
                            altMobile = object.getString("AltMobile");
                            String gstNo = object.getString("GSTNo");
                            String aadharNo = object.getString("AadharNo");
                            String image = object.getString("Image");
                            String sNo = object.getString("SNo");
                            String productId = object.getString("ProductId");
                            String productName = object.getString("ProductName");
                            String qty = object.getString("Qty");
                            String unit = object.getString("Unit");
                            String price = object.getString("Price");
                            String sellingPrice = object.getString("SellingPrice");
                            String amount = object.getString("Amount");
                            status = object.getString("Status");
                            String productCode = object.getString("ProductCode");
                            String description = object.getString("Description");

                            orderDetailsItemList.add(new OrderDetailsItem(orderId, nag, orderDate, delCharge, promoCode, disc, remarks, subTotal, advance, noOfItems,
                                    addressId, orderAmount, address, city, state, pin, addressType, altMobile, gstNo, aadharNo, image, sNo, productId, productName, qty, unit, price,
                                    sellingPrice, amount, status, productCode, description));

                        }

                        orderDetailsAct_rv.setAdapter(new MyOrderDetailsAdapter(context, orderDetailsItemList));

                        //order stats
                        myOrderDetailsAct_tv_orderId.setText("Order Id: " + orderId);
                        myOrderDetailsAct_tv_orderDate.setText("Order Date: " + orderDate);
                        myOrderDetailsAct_tv_itemsAmt.setText(getResources().getString(R.string.INR_symbol) + orderAmount);
                        myOrderDetailsAct_tv_delCharges.setText("+ " + getResources().getString(R.string.INR_symbol) + delCharge);
                        myOrderDetailsAct_tv_discount.setText("- " + getResources().getString(R.string.INR_symbol) + disc);
                        myOrderDetailsAct_tv_total.setText(getResources().getString(R.string.INR_symbol) + subTotal);
                        myOrderDetailsAct_tv_adv.setText("- " + getResources().getString(R.string.INR_symbol) + advance);
                        myOrderDetailsAct_tv_finalAmt.setText(getResources().getString(R.string.INR_symbol) + (Double.parseDouble(subTotal) - Double.parseDouble(advance)));
                        myOrderDetailsAct_tv_name.setText(sessionManager.getUserName());
                        myOrderDetailsAct_tv_mob.setText(altMobile);
                        myOrderDetailsAct_tv_address.setText(address + ", " + city + ", " + state + "\nPIN code: " + pin);

                        //order status tracker
                        String date = Utils.formatDateTimeFromString(orderDate, "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy");
                        String time = Utils.formatDateTimeFromString(orderDate, "yyyy-MM-dd HH:mm:ss", "hh:mm a");
                        switch (status) {
                            case "Pending": {
                                indicator.setCurrentStep(0);
                                myOrderDetailsAct_tv_stPlaced_time.setText(date + "\n" + time);
                                myOrderDetailsAct_tv_stProcessed_time.setText("");
                                myOrderDetailsAct_tv_stShipped_time.setText("");
                                myOrderDetailsAct_tv_stDelivered_time.setText("");
                            }
                            break;

                            case "Processed": {
                                indicator.setCurrentStep(1);
                                myOrderDetailsAct_tv_stPlaced_time.setText("");
                                myOrderDetailsAct_tv_stProcessed_time.setText(date + "\n" + time);
                                myOrderDetailsAct_tv_stShipped_time.setText("");
                                myOrderDetailsAct_tv_stDelivered_time.setText("");
                            }
                            break;

                            case "Shipped": {
                                indicator.setCurrentStep(2);
                                myOrderDetailsAct_tv_stPlaced_time.setText("");
                                myOrderDetailsAct_tv_stProcessed_time.setText("");
                                myOrderDetailsAct_tv_stShipped_time.setText(date + "\n" + time);
                                myOrderDetailsAct_tv_stDelivered_time.setText("");
                            }
                            break;

                            case "Delivered": {
                                indicator.setCurrentStep(3);
                                myOrderDetailsAct_tv_stPlaced_time.setText("");
                                myOrderDetailsAct_tv_stProcessed_time.setText("");
                                myOrderDetailsAct_tv_stShipped_time.setText("");
                                myOrderDetailsAct_tv_stDelivered_time.setText(date + "\n" + time);
                            }
                            break;
                        }


                    }
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

}