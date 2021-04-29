package com.maq.ecom.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.networking.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements RetrofitRespondListener {

    String TAG = SignUpActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager = new SessionManager(this);
    LoadingDialog loadingDialog;

    @BindView(R.id.et_fullname)
    EditText et_fullname;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_cPassword)
    EditText et_cPassword;


    @OnClick(R.id.btn_register)
    void register() {
        validateForm();
    }

    @OnClick(R.id.tv_loginHere)
    void loginHere() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        loadingDialog = new LoadingDialog(context);

    }


    private void validateForm() {
        final String str_fullname = et_fullname.getText().toString().trim();
        final String str_username = et_username.getText().toString().trim();
        final String str_phone = et_phone.getText().toString().trim();
        final String str_password = et_password.getText().toString().trim();
        final String str_cPassword = et_cPassword.getText().toString().trim();

        if (TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_phone) || TextUtils.isEmpty(str_password) || TextUtils.isEmpty(str_cPassword)) {
            Utils.showToast(context, "All fields must be filled");
        } else if (!str_password.equals(str_cPassword))
            Utils.showToast(context, "Password doesn't match");
        else requestSignUp(str_fullname, str_username, str_phone, str_password, str_cPassword);
    }

    private void requestSignUp(String str_fullname, String str_username, String str_phone, String str_password, String str_cPassword) {
        loadingDialog.show(); //show loader
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_registerNewUser("1", str_fullname, str_username, str_phone, str_password);
        RetrofitClient.callRetrofit(apiCall, "SIGN_UP", this);
    }

    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "SIGN_UP":
                try {
                    callbackSignUp(response);
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
        if (responseError.contains(context.getString(R.string.str_unable_to_resolve_host)))
            responseError = context.getString(R.string.str_no_internet);

        Utils.showToast(context, responseError);
    }

    private void callbackSignUp(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("true")) {
                Utils.showToast(context, jsonObject.getString("error_msg"));
            } else {
                Utils.navigateClearTo(context, LoginActivity.class);
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }
}