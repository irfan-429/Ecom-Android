package com.maq.ecom.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity implements RetrofitRespondListener {

    String TAG = LoginActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager = new SessionManager(this);
    LoadingDialog loadingDialog;
    String isAdmin = "0"; //default customer

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;

//    @BindView(R.id.radio_grp)
//    RadioGroup radioGroup;

    @OnClick(R.id.btn_login)
    void login() {
        validateForm();
    }

    @OnClick(R.id.tv_resetHere)
    void forgotPwd() {
        Utils.navigateTo(context, ForgotPasswordActivity.class);
    }

    @OnClick(R.id.tv_registerHere)
    void registerHere() {
        Utils.navigateTo(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        loadingDialog = new LoadingDialog(context);

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_admin)
//                    isAdmin = "1";
//                else isAdmin = "0";
//            }
//        });
    }


    private void validateForm() {
        final String str_username = et_username.getText().toString().trim();
        final String str_password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password)) {
            Utils.showToast(context, "All fields must be filled");
        } else requestSignIn(str_username, str_password);
    }

    private void requestSignIn(String str_username, String str_password) {
        loadingDialog.show(); //show loader
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_requestLogin(str_username, str_password);
        RetrofitClient.callRetrofit(apiCall, "LOGIN", this);
    }

    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "LOGIN":
                try {
                    callbackSignIn(response);
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

    private void callbackSignIn(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("true")) {
                Utils.showToast(context, jsonObject.getString("error_msg"));
            } else {
                JSONObject objectUser = jsonObject.getJSONObject("user");
                sessionManager.setUserId(objectUser.getString("UserId"));
                sessionManager.setUserName(objectUser.getString("UserName"));
                sessionManager.setEmailId(objectUser.getString("EmailId"));
                sessionManager.setUserMobile(objectUser.getString("MobileNo"));
                sessionManager.setUserPwd(objectUser.getString("Password"));
                sessionManager.setProfileImg(objectUser.getString("UserImage"));
                sessionManager.setFirmId(objectUser.getString("FirmId"));

                if (objectUser.getString("isAdmin").equals("1")) sessionManager.setAdmin(true);
                sessionManager.setIsLoggedIn(true);

                Utils.navigateClearTo(context, MainActivity.class);

//                setResult(Activity.RESULT_OK, new Intent());
//                finish();
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }
}