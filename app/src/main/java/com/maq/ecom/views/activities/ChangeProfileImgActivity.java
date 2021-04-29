package com.maq.ecom.views.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

public class ChangeProfileImgActivity extends AppCompatActivity implements RetrofitRespondListener {

    String TAG = ChangeProfileImgActivity.class.getSimpleName();
    Context context = this;
    String imgFileName;
    LoadingDialog loadingDialog;
    SessionManager sessionManager = new SessionManager(this);
    MultipartBody.Part imgFileSlip = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_change_profile_img);
        loadingDialog = new LoadingDialog(this);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }


    private void uploadNewImg() {
        loadingDialog.show(); //show loader
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_uploadProfileImage(imgFileSlip);
        RetrofitClient.callRetrofit(apiCall, "UPLOAD_IMG", this);
    }

    private void requestImgUploaded() {
        loadingDialog.show(); //show loader
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_changeProfileImg(sessionManager.getEmailId(), imgFileName);
        RetrofitClient.callRetrofit(apiCall, "CHANGE_IMG", this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ImagePicker result
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri resultUri = result.getUri();

                File file = new File(resultUri.getPath());
                imgFileSlip = Utils.ImageToMultipartBody("file", file); //get file to submit
                imgFileName = file.getName();

                uploadNewImg();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Utils.showSnackBar(context, String.valueOf(result.getError()));
            }
        }
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "UPLOAD_IMG":
                try {
                    callbackImg(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "CHANGE_IMG":
                try {
                    callbackImgUploaded(response);
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

        Utils.showSnackBar(context, responseError);
    }

    private void callbackImg(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("success").equals("true")) {
                requestImgUploaded();
            } else Utils.showToast(context, jsonObject.getString("success"));
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void callbackImgUploaded(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            Utils.showToast(context, jsonObject.getString("user"));
            sessionManager.setProfileImg(imgFileName);
            finish();
        } else Utils.showToast(context, String.valueOf(responseCode));
    }
}