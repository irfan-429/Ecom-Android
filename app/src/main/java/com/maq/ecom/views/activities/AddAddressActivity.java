package com.maq.ecom.views.activities;

import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.GPSTracker;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Address;
import com.maq.ecom.networking.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class AddAddressActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = CreateProductActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    GPSTracker gpsTracker;

    String str_address, str_city, str_state, str_pin, str_altMob, str_GSTNo, str_aadharNo, str_type = "Home";
    String isDefault = "0", lat="0.0", lng="0.0";

    boolean isEditing = false;
    Address address;

    @BindView(R.id.addAddressAct_et_address)
    EditText et_address;
    @BindView(R.id.addAddressAct_et_city)
    EditText et_city;
    @BindView(R.id.addAddressAct_et_State)
    EditText et_State;
    @BindView(R.id.addAddressAct_et_pin)
    EditText et_pin;
    @BindView(R.id.addAddressAct_et_altMobile)
    EditText et_altMobile;
    @BindView(R.id.addAddressAct_et_GSTNo)
    EditText et_GSTNo;
    @BindView(R.id.addAddressAct_et_aadharNo)
    EditText et_aadharNo;

    @BindView(R.id.addAddressAct_sp_type)
    AppCompatSpinner sp_type;

    @BindView(R.id.addAddressAct_cb_isDefault)
    CheckBox cb_isDefault;

    @BindView(R.id.addAddressAct_btn)
    Button addAddressAct_btn;

    @OnClick(R.id.addAddressAct_btn)
    void btnAdd() {
        validateForm();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        init();
        setupSpinner();
        getProductIntent();
        getUserLocation();
    }

    private void getUserLocation() {
        gpsTracker = new GPSTracker(context);

        if (gpsTracker.canGetLocation()) {
            lat = String.valueOf(gpsTracker.getLatitude());
            lng = String.valueOf(gpsTracker.getLongitude());
        } else gpsTracker.showSettingsAlert();
    }


    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
    }

    private void getProductIntent() {
        Intent dataIntent = getIntent();
        if (dataIntent.hasExtra("address_edit")) {
            isEditing = true;
            super.setupToolbar("Edit Address");
            addAddressAct_btn.setText("Save Address");
            address = (Address) dataIntent.getSerializableExtra("address");

            setupData();
        } else super.setupToolbar("Add Address");

    }

    private void setupData() {
        et_address.setText(address.getAddress());
        et_city.setText(address.getCity());
        et_State.setText(address.getState());
        et_pin.setText(address.getPin());
        et_altMobile.setText(address.getAltMobile());
        et_GSTNo.setText(address.getGstNo());
        et_aadharNo.setText(address.getAadharNo());

        if (address.getIsDefault().equals("1")) {
            cb_isDefault.setChecked(true);
            isDefault = "1";
        }
    }


    private void setupSpinner() {
        List<String> statusList = new ArrayList<String>();
        statusList.add("Address Type");
        statusList.add("Home");
        statusList.add("Office");

        if (isEditing)
            if (address.getAddressType().equals("Office"))
                sp_type.setSelection(1);

        sp_type.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList));
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != -1)
                    str_type = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void validateForm() {
        str_address = et_address.getText().toString().trim();
        str_city = et_city.getText().toString().trim();
        str_state = et_State.getText().toString().trim();
        str_pin = et_pin.getText().toString().trim();
        str_altMob = et_altMobile.getText().toString().trim();
        str_GSTNo = et_GSTNo.getText().toString().trim();
        str_aadharNo = et_aadharNo.getText().toString().trim();

        if (cb_isDefault.isChecked())
            isDefault = "1";


        if (TextUtils.isEmpty(str_address) || TextUtils.isEmpty(str_city) || TextUtils.isEmpty(str_state) || TextUtils.isEmpty(str_pin) || TextUtils.isEmpty(str_altMob) || TextUtils.isEmpty(str_GSTNo))
            Utils.showToast(context, "All fields are required");
        else requestAddNewAddress();
    }


    private void requestAddNewAddress() {
        loadingDialog.show();
        Call<JsonObject> apiCall;
        if (!isEditing)
            apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                    .API_addNewAddress(sessionManager.getUserId(), str_address, str_city, str_state, str_pin, str_type, str_altMob, lat, lng, isDefault, str_GSTNo, str_aadharNo);
        else apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_editAddress(sessionManager.getUserId(), str_address, str_city, str_state, str_pin, str_type, str_altMob, lat, lng, isDefault, str_GSTNo, str_aadharNo, address.getId());

        RetrofitClient.callRetrofit(apiCall, "ADD_ADDRESS", this);
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {

            case "ADD_ADDRESS":
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
        if (loadingDialog.isShowing()) loadingDialog.dismiss();
        if (responseError.contains(context.getString(R.string.str_unable_to_resolve_host)))
            responseError = context.getString(R.string.str_no_internet);

        Utils.showSnackBar(context, responseError);
    }


    private void callback(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            Utils.showToast(context, jsonObject.getString("error_msg"));
            if (jsonObject.getString("error").equals("false")) {
                finish();
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gpsTracker != null)
            gpsTracker.stopUsingGPS();
    }
}