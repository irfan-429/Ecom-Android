package com.maq.ecom.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.CartAdapter;
import com.maq.ecom.adapter.MyOrderDetailsAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.CustomDatePicker;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.model.MyOrder;
import com.maq.ecom.model.OrderDetailsItem;
import com.maq.ecom.networking.RetrofitClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Response;

public class MyOrderDetailsActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = MyOrderDetailsActivity.class.getSimpleName();
    SessionManager sessionManager = new SessionManager(this);
    Context context = this;
    LoadingDialog loadingDialog;

    List<OrderDetailsItem> orderDetailsItemList = new ArrayList<>();

    String str_status;
    String orderId = "", orderDate = "", nag = "", promoCode = "", noOfItems = "", delCharge = "", disc = "", subTotal = "", advance = "", address = "", orderAmount = "", city = "", state = "", pin = "", altMobile = "", status = "";

    String str_orderAmount,  str_subTotal, str_noOfItems;

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

    @BindView(R.id.createProductAct_et_invNo)
    EditText createProductAct_et_invNo;
    @BindView(R.id.createProductAct_et_invDate)
    EditText createProductAct_et_invDate;
    @BindView(R.id.createProductAct_et_courierName)
    EditText createProductAct_et_courierName;
    @BindView(R.id.createProductAct_et_trackingNo)
    EditText createProductAct_et_trackingNo;
    @BindView(R.id.createProductAct_et_invRemarks)
    EditText createProductAct_et_invRemarks;

    @BindView(R.id.createProductAct_sp_status)
    SearchableSpinner sp_status;

    @BindView(R.id.myOrderDetailsAct_tv_orderStCancel)
    LinearLayoutCompat layout_orderStCancel;
    @BindView(R.id.myOrderDetailsAct_tv_orderSt)
    LinearLayoutCompat layout_orderSt;

    @BindView(R.id.layout_date)
    LinearLayout layoutDate;

    @OnClick(R.id.createProductAct_btn_update)
    void update() {
        updateOrder(makePayload());
    }


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
        layoutDate.setOnClickListener(v -> new CustomDatePicker(context, createProductAct_et_invDate));

    }

    private void fetchOrderDetails() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_orderDetail(getIntent().getStringExtra("order_id"));
        RetrofitClient.callRetrofit(apiCall, "ORDER_DETAILS", this);
    }


    private void setupSpinner() {
        List<String> statusList = new ArrayList<String>();
        statusList.add("Pending");
        statusList.add("Processing");
        statusList.add("Shipped");
        statusList.add("Delivered");
        statusList.add("Cancelled");

        sp_status.setTitle("Select One");
        sp_status.setPositiveButton("Close");

        if (status != null)
            if (status.equals("Pending"))
                sp_status.setSelection(0);
            else if (status.equals("Processing"))
                sp_status.setSelection(1);
            else if (status.equals("Shipped"))
                sp_status.setSelection(2);
            else if (status.equals("Delivered"))
                sp_status.setSelection(3);
            else sp_status.setSelection(4);


        sp_status.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList));
        sp_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                str_status = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateOrder(JsonObject jsonObject) {
        loadingDialog.show();
        String str_nag = String.valueOf(orderDetailsItemList.size());

        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_updateOrder(sessionManager.getFirmId(), orderId, str_orderAmount, str_nag, delCharge, promoCode, disc, str_subTotal, str_noOfItems, createProductAct_et_invNo.getText().toString(), createProductAct_et_invDate.getText().toString(),
                        createProductAct_et_courierName.getText().toString(), createProductAct_et_trackingNo.getText().toString(), createProductAct_et_invRemarks.getText().toString(), status, jsonObject);

        RetrofitClient.callRetrofit(apiCall, "UPDATE_ORDER", this);
    }

    private JsonObject makePayload() {
        double totalQty = 0.0;
        double subTotal=0.0;
        JsonArray items = new JsonArray();
        for (int i = 0; i < orderDetailsItemList.size(); i++) {
            OrderDetailsItem item = orderDetailsItemList.get(i);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("SNo", item.getsNo());
            jsonObject.addProperty("ProductId", item.getProductId());
            jsonObject.addProperty("Qty", item.getQty());
            jsonObject.addProperty("Unit", item.getUnit());
            jsonObject.addProperty("Price", item.getPrice());
            jsonObject.addProperty("SellingPrice", item.getSellingPrice());
            jsonObject.addProperty("Amount", String.valueOf(Double.parseDouble(String.valueOf(item.getQty())) * Double.parseDouble(item.getSellingPrice())));
            jsonObject.addProperty("ProductName", item.getProductName());

            totalQty = totalQty + Double.parseDouble(item.getQty());
            subTotal = subTotal + Double.parseDouble(item.getQty()) *  Double.parseDouble(item.getSellingPrice());
            items.add(jsonObject);
        }

        str_noOfItems = String.valueOf(totalQty);
        str_subTotal = String.valueOf(subTotal);
        str_orderAmount= String.valueOf(subTotal+ Double.parseDouble(delCharge)- Double.parseDouble(disc));

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("items", items);
        return jsonObject;
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

            case "UPDATE_ORDER":
                try {
                    callbackUpdate(response);
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

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            orderId = object.getString("OrderId");
                            nag = object.getString("Nag");
                            orderDate = object.getString("OrderDate");
                            delCharge = object.getString("DelCharge");
                            promoCode = object.getString("PromoCode");
                            disc = object.getString("Disc");
                            String remarks = object.getString("Remarks");
                            subTotal = object.getString("SubTotal");
                            advance = object.getString("Advance");
                            noOfItems = object.getString("NoOfItems");
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

                            this.status = status;

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

                            case "Processing": {
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

                        setupSpinner();
                        if (status.equalsIgnoreCase("Cancelled")) {
                            layout_orderStCancel.setVisibility(View.VISIBLE);
                            layout_orderSt.setVisibility(View.GONE);
                        }

                    }
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void callbackUpdate(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                Utils.showToast(context, "Order updated successfully!");
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

}