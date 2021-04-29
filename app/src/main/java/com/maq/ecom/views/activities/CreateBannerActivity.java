package com.maq.ecom.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

public class CreateBannerActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = CreateBannerActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String bannerName, bannerLink, banStatus = "Enable";
    MultipartBody.Part imgFileBanner;
    String nameBanner;
    boolean isEditing = false;

    @BindView(R.id.createBannerAct_et_name)
    EditText et_banName;
    @BindView(R.id.createBannerAct_et_link)
    EditText et_banLink;

    @BindView(R.id.createBannerAct_iv_banner)
    AppCompatImageView iv_banner;

    @BindView(R.id.createBannerAct_sp_status)
    SearchableSpinner spinner;


    @OnClick(R.id.createBannerAct_layout_banner)
    void uploadBanner() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @OnClick(R.id.createBannerAct_btn_submit)
    void btnSubmit() {
        bannerName = et_banName.getText().toString().trim();
        bannerLink = et_banLink.getText().toString().trim();

        if (!isEditing)
            validateForm();
        else {
            uploadImage(imgFileBanner);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_banner);
        ButterKnife.bind(this);
        init();
        setupSpinner();
        getCatIntent();
    }

    private void getCatIntent() {
        Intent dataIntent = getIntent();
        if (dataIntent.hasExtra("ban_edit")) {
            isEditing = true;
            super.setupToolbar("Edit Banner");
            et_banName.setText(dataIntent.getStringExtra("ban_name"));
            et_banLink.setText(dataIntent.getStringExtra("ban_link"));
            nameBanner= dataIntent.getStringExtra("ban_img");
            Utils.loadImage(context, dataIntent.getStringExtra("ban_img"), iv_banner);
        } else super.setupToolbar("Create Banner");

    }

    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
    }

    private void setupSpinner() {
        List<String> statusList = new ArrayList<String>();
        statusList.add("Enable");
        statusList.add("Disable");

        spinner.setTitle("Select One");
        spinner.setPositiveButton("Close");
        if (isEditing) {
            if (getIntent().getStringExtra("ban_status").equals("Disable"))
                spinner.setSelection(1, false);
        }

        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                banStatus = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void validateForm() {
        if (TextUtils.isEmpty(bannerName))
            Utils.showToast(context, "Banner name required");
        else if (imgFileBanner == null)
            Utils.showToast(context, "Upload banner image");
        else uploadImage(imgFileBanner);
    }

    private void uploadImage(MultipartBody.Part imgFile) {
        if (imgFile != null) {
            if (!loadingDialog.isShowing()) loadingDialog.show();
            Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                    .API_uploadImg(imgFile);
            RetrofitClient.callRetrofit(apiCall, "UPLOAD_IMAGES", this);
        } else {
            requestAddNewBanner();
        }
    }

    private void requestAddNewBanner() {
        Call<JsonObject> apiCall;
        if (!isEditing)
            apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                    .API_addNewBanner(sessionManager.getFirmId(), bannerName,  bannerLink, nameBanner, banStatus);
        else apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_editBanner(getIntent().getStringExtra("ban_id"), sessionManager.getFirmId(), bannerName,  bannerLink,  nameBanner, banStatus);

        RetrofitClient.callRetrofit(apiCall, "NEW_BANNER", this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ImagePicker result
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri resultUri = result.getUri();
                File file = new File(resultUri.getPath());

                iv_banner.setImageURI(resultUri);
                imgFileBanner = Utils.ImageToMultipartBody("file", Utils.compressImage(file)); //get file to submit
                nameBanner = file.getName();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Utils.showSnackBar(this, String.valueOf(result.getError()));
            }
        }
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "UPLOAD_IMAGES":
                try {
                    callbackUploadImages(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "NEW_BANNER":
                try {
                    callbackNewBanner(response);
                    loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }


    }

    @Override
    public void onRetrofitFailure(String responseError, String requestName) {
        if (loadingDialog.isShowing()) loadingDialog.dismiss();
        if (responseError.contains(context.getString(R.string.str_unable_to_resolve_host)))
            responseError = context.getString(R.string.str_no_internet);

        Utils.showSnackBar(context, responseError);
    }

    private void callbackUploadImages(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("success").equals("true")) {
                requestAddNewBanner();
            } else Utils.showToast(context, jsonObject.getString("success"));
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void callbackNewBanner(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            Utils.showToast(context, jsonObject.getString("error_msg"));
            if (jsonObject.getString("error").equals("false")) {
                finish();
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

}