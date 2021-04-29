package com.maq.ecom.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.Nullable;
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

public class ChangePwdActivity extends AppCompatActivity implements RetrofitRespondListener {

    String TAG = ChangePwdActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager = new SessionManager(this);
    LoadingDialog loadingDialog;

    @BindView(R.id.changePwdAct_et_oldPwd)
    EditText et_oldPwd;
    @BindView(R.id.changePwdAct_et_newPwd)
    EditText et_newPwd;
    @BindView(R.id.changePwdAct_et_cNewPwd)
    EditText et_cNewPwd;

    @OnClick(R.id.changePwdAct_btn_changePwd)
    void changePwd() {
        validateForm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (!sessionManager.getIsLoggedIn())
            startActivityForResult(new Intent(context, LoginActivity.class), 200);

        loadingDialog = new LoadingDialog(context);
    }

    private void validateForm() {
        final String str_oldPwd = et_oldPwd.getText().toString().trim();
        final String str_newPwd = et_newPwd.getText().toString().trim();
        final String str_cNewPwd = et_cNewPwd.getText().toString().trim();

        if (TextUtils.isEmpty(str_oldPwd) || TextUtils.isEmpty(str_newPwd) || TextUtils.isEmpty(str_cNewPwd)) {
            Utils.showToast(context, "All fields are required");
        } else if (!str_oldPwd.equals(sessionManager.getUserPwd())) {
            Utils.showToast(context, "Incorrect old password entered.");
        } else if (!str_newPwd.equals(str_cNewPwd)) {
            Utils.showToast(context, "Password doesn't match");
        } else requestChangePwd(str_newPwd);
    }

    private void requestChangePwd(String str_newPwd) {
        loadingDialog.show(); //show loader
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_changePassword(sessionManager.getEmailId(), str_newPwd);
        RetrofitClient.callRetrofit(apiCall, "CHANGE_PWD", this);
    }

    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "CHANGE_PWD":
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
                Utils.showToast(context, jsonObject.getString("user"));
                sessionManager.setUserPwd(et_newPwd.getText().toString().trim()); //update local storage
                finish();
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
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

}