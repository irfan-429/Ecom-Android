package com.maq.ecom.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
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

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.networking.RetrofitClient;
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

public class CreateCategoryActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = CreateCategoryActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String catName, catStatus = "Enable";
    MultipartBody.Part imgFileBanner, imgFileThumbnail;
    String nameBanner, nameThumbnail;
    int imgUploadedCounter = 0;
    String choosedImg;
    boolean isEditing = false;

    @BindView(R.id.createCatAct_et_catName)
    EditText et_catName;

    @BindView(R.id.createCatAct_iv_banner)
    AppCompatImageView iv_banner;
    @BindView(R.id.createCatAct_iv_thumbnail)
    AppCompatImageView iv_thumbnail;

    @BindView(R.id.createCatAct_sp_status)
    SearchableSpinner spinner;


    @OnClick(R.id.createCatAct_layout_banner)
    void uploadBanner() {
        choosedImg = "banner";
        ImagePicker.with(this).start(100);

    }


    @OnClick(R.id.createCatAct_layout_thumbnail)
    void uploadThumbnail() {
        choosedImg = "thumbnail";
        ImagePicker.with(this).start(100);

    }

    @OnClick(R.id.createCatAct_btn_submit)
    void btnSubmit() {
        catName = et_catName.getText().toString().trim();

        if (!isEditing)
            validateForm();
        else {
            loadingDialog.show();
            uploadImage(imgFileBanner);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        ButterKnife.bind(this);
        init();
        setupSpinner();
        getCatIntent();
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
            if (getIntent().getStringExtra("cat_status").equals("Disable"))
                spinner.setSelection(1);
        }

        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                catStatus = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCatIntent() {
        Intent dataIntent = getIntent();
        if (dataIntent.hasExtra("cat_edit")) {
            isEditing = true;
            super.setupToolbar("Edit Category");
            et_catName.setText(dataIntent.getStringExtra("cat_name"));
            nameBanner = dataIntent.getStringExtra("cat_banner");
            nameThumbnail = dataIntent.getStringExtra("cat_img");
            Utils.loadImage(context, nameBanner, iv_banner);
            Utils.loadImage(context, nameThumbnail, iv_thumbnail);
        } else super.setupToolbar("Create Category");

    }

    private void validateForm() {
        if (TextUtils.isEmpty(catName))
            Utils.showToast(context, "Category name required");
        else if (imgFileBanner == null)
            Utils.showToast(context, "Upload category banner");
        else if (imgFileThumbnail == null)
            Utils.showToast(context, "Upload category thumbnail");
        else uploadImage(imgFileBanner);
    }

    private void uploadImage(MultipartBody.Part imgFile) {
        if (imgFile != null) {
            if (!loadingDialog.isShowing()) loadingDialog.show();
            Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                    .API_uploadImg(imgFile);
            RetrofitClient.callRetrofit(apiCall, "UPLOAD_IMAGES", this);
        } else {
            if (imgFileThumbnail != null)
                uploadImage(imgFileThumbnail);
            else requestAddNewCat();
        }
    }

    private void requestAddNewCat() {
        Call<JsonObject> apiCall;
        if (!isEditing)
            apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                    .API_addNewCategory(sessionManager.getFirmId(), catName, nameBanner, nameThumbnail, catStatus);
        else apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_editCategory(getIntent().getStringExtra("cat_id"), sessionManager.getFirmId(), catName, nameBanner, nameThumbnail, catStatus);

        RetrofitClient.callRetrofit(apiCall, "NEW_CATEGORY", this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ImagePicker result
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            startActivityForResult(new Intent(context, CropView.class).putExtra("uri", data.getData().toString()), 69);
        } else if (resultCode == RESULT_OK && requestCode == 69) {
            Uri uri = Uri.parse(data.getExtras().getString("image"));
            Log.i(TAG, "onActivityResult: "+uri.toString());
            File file = new File(uri.getPath());

            if (choosedImg.equals("banner")) {
                iv_banner.setImageURI(uri);

                imgFileBanner = Utils.ImageToMultipartBody("file",Utils.compressImage(this,file)); //get file to submit
//                nameBanner = file.getName();
            } else {
                iv_thumbnail.setImageURI(uri);
                imgFileThumbnail = Utils.ImageToMultipartBody("file", Utils.compressImage(this,file));
                nameThumbnail = file.getName();
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

            case "NEW_CATEGORY":
                try {
                    callbackNewCategory(response);
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
                imgUploadedCounter = imgUploadedCounter + 1;
                if (imgUploadedCounter == 2) {
                    requestAddNewCat();
                } else {
                    uploadImage(imgFileThumbnail);
                }
            } else Utils.showToast(context, jsonObject.getString("success"));
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void callbackNewCategory(Response<?> response) throws JSONException {
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