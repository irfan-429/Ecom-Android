package com.maq.ecom.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class ForgotPasswordActivity extends AppCompatActivity implements RetrofitRespondListener {

    String TAG = ForgotPasswordActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager = new SessionManager(this);
    LoadingDialog loadingDialog;

    @BindView(R.id.forgotPwdAct_et_username)
    EditText et_username;

    @OnClick(R.id.forgotPwdAct_btn_submit)
    void forgotPwd() {
        validateForm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        loadingDialog = new LoadingDialog(context);
    }

    private void validateForm() {
        final String str_username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(str_username)) {
            Utils.showToast(context, "Field must be filled");
        } else requestForgotPwd(str_username);
    }


    private void requestForgotPwd(String str_username) {
        loadingDialog.show(); //show loader
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_forgotPassword(str_username);
        RetrofitClient.callRetrofit(apiCall, "FORGOT_PWD", this);
    }

    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "FORGOT_PWD":
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
        if (responseError.contains(context.getString(R.string.str_unable_to_resolve_host)))
            responseError = context.getString(R.string.str_no_internet);

        Utils.showToast(context, responseError);
    }

    private void callback(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("true")) {
                Utils.showToast(context, jsonObject.getString("error_msg"));
            } else {
                String pwd = jsonObject.getJSONArray("allemployee").getJSONObject(0).getString("Password");
                new AlertDialog.Builder(this)
                        .setTitle("Your Password is \"" + pwd + "\"")
                        .setPositiveButton("Thanks", null)
                        .show();
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }
}