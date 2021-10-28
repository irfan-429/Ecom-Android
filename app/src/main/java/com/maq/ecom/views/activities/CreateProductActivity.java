package com.maq.ecom.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.maq.ecom.model.Category;
import com.maq.ecom.model.Product;
import com.maq.ecom.networking.RetrofitClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
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

public class CreateProductActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = CreateProductActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;

    String str_code, str_name, str_price, str_discount, str_sellPrice, str_shortDisc, str_desc, str_keyFeature, str_stock, str_catId, str_status = "Enable";
    String pickedImg;
    String isFeatured = "0", isNew = "0", isPopular = "0", isSize = "0";

    MultipartBody.Part imgFileCover, imgFile1, imgFile2, imgFile3, imgFile4, imgFile5, imgFile6;
    String nameCover, nameImg1, nameImg2, nameImg3, nameImg4, nameImg5, nameImg6;
    boolean isEditing = false;
    Product product;

    @BindView(R.id.createProductAct_et_code)
    EditText et_code;
    @BindView(R.id.createProductAct_et_name)
    EditText et_name;
    @BindView(R.id.createProductAct_et_price)
    EditText et_price;
    @BindView(R.id.createProductAct_et_discount)
    EditText et_discount;
    @BindView(R.id.createProductAct_et_sellPrice)
    EditText et_sellPrice;
    @BindView(R.id.createProductAct_et_shortDesc)
    EditText et_shortDesc;
    @BindView(R.id.createProductAct_et_desc)
    EditText et_desc;
    @BindView(R.id.createProductAct_et_keyFeatures)
    EditText et_keyFeatures;
    @BindView(R.id.createProductAct_et_stock)
    EditText et_stock;

    @BindView(R.id.createProductAct_iv_cover)
    AppCompatImageView iv_cover;
    @BindView(R.id.createProductAct_iv_img1)
    AppCompatImageView iv_img1;
    @BindView(R.id.createProductAct_iv_img2)
    AppCompatImageView iv_img2;
    @BindView(R.id.createProductAct_iv_img3)
    AppCompatImageView iv_img3;
    @BindView(R.id.createProductAct_iv_img4)
    AppCompatImageView iv_img4;
    @BindView(R.id.createProductAct_iv_img5)
    AppCompatImageView iv_img5;
    @BindView(R.id.createProductAct_iv_img6)
    AppCompatImageView iv_img6;

    @BindView(R.id.createProductAct_sp_cat)
    SearchableSpinner sp_cat;
    @BindView(R.id.createProductAct_sp_status)
    SearchableSpinner sp_status;

    @BindView(R.id.createProductAct_sw_isFeatured)
    SwitchCompat sw_isFeatured;
    @BindView(R.id.createProductAct_sw_isNew)
    SwitchCompat sw_isNew;
    @BindView(R.id.createProductAct_sw_isPopular)
    SwitchCompat sw_isPopular;
    @BindView(R.id.createProductAct_sw_size)
    SwitchCompat sw_isSize;


    @OnClick(R.id.createProductAct_layout_cover)
    void cover() {
        pickedImg = "cover";
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @OnClick(R.id.createProductAct_layout_img1)
    void img1() {
        pickedImg = "img1";
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @OnClick(R.id.createProductAct_layout_img2)
    void img2() {
        pickedImg = "img2";
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @OnClick(R.id.createProductAct_layout_img3)
    void img3() {
        pickedImg = "img3";
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @OnClick(R.id.createProductAct_layout_img4)
    void img4() {
        pickedImg = "img4";
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @OnClick(R.id.createProductAct_layout_img5)
    void img5() {
        pickedImg = "img5";
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @OnClick(R.id.createProductAct_layout_img6)
    void img6() {
        pickedImg = "img6";
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @OnClick(R.id.createProductAct_btn_submit)
    void btnSubmit() {
//        if (!isEditing)
//
//        else {
//            uploadImage(imgFileCover);
//        }
        validateForm();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        ButterKnife.bind(this);
        init();
        setupSpinner();
        fetchCategories();
        getProductIntent();
        fetchLastCode();

    }


    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
    }

    private void fetchLastCode() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_getLastCode(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "LAST_CODE", this);
    }

    private void getProductIntent() {
        Intent dataIntent = getIntent();
        if (dataIntent.hasExtra("product_edit")) {
            isEditing = true;
            super.setupToolbar("Edit Product");
            product = (Product) dataIntent.getSerializableExtra("product");
            setupData();
        } else super.setupToolbar("Create Product");

    }

    private void setupData() {
        et_code.setText(product.getProductCode());
        et_name.setText(product.getProductName());
        et_price.setText(product.getPrice());
        et_discount.setText(product.getDiscount());
        et_sellPrice.setText(product.getSellingPrice());
        et_shortDesc.setText(product.getShortDesc());
        et_desc.setText(product.getDescription());
        et_keyFeatures.setText(product.getKeyFeatures());
        et_stock.setText(product.getStock());

        Utils.loadImage(context, product.getProductCover(), iv_cover);
        Utils.loadImage(context, product.getImage1(), iv_img1);
        Utils.loadImage(context, product.getImage2(), iv_img2);
        Utils.loadImage(context, product.getImage3(), iv_img3);
        Utils.loadImage(context, product.getImage4(), iv_img4);
        Utils.loadImage(context, product.getImage5(), iv_img5);
        Utils.loadImage(context, product.getImage6(), iv_img6);

        if (product.getIsFeatured().equals("1"))
            sw_isFeatured.setChecked(true);
        if (product.getIsNew().equals("1"))
            sw_isNew.setChecked(true);
        if (product.getIsPopular().equals("1"))
            sw_isPopular.setChecked(true);
        if (product.getIsSize().equals("1"))
            sw_isSize.setChecked(true);


    }


    private void setupSpinner() {
        List<String> statusList = new ArrayList<String>();
        statusList.add("Enable");
        statusList.add("Disable");

        sp_status.setTitle("Select One");
        sp_status.setPositiveButton("Close");

        if (isEditing)
            if (product.getStatus().equals("Disable"))
                sp_status.setSelection(1);

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

    private void setupCatSpinner(List<Category> arrayList) {
        sp_cat.setTitle("Select One");
        sp_cat.setPositiveButton("Close");

        if (isEditing)
            for (int i = 0; i < arrayList.size(); i++) {
                Category category = arrayList.get(i);
                if (product.getCategoryId().equals(category.getCategoryId()))
                    sp_status.setSelection(i);
            }

        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, arrayList);
        sp_cat.setAdapter(adapter);
        sp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Category category = adapter.getItem(position);
                str_catId = category.getCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fetchCategories() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_getCategoryList(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "CATEGORIES", this);
    }

    private void validateForm() {
        str_code = et_code.getText().toString().trim();
        str_name = et_name.getText().toString().trim();
        str_price = et_price.getText().toString().trim();
        str_discount = et_discount.getText().toString().trim();
        str_sellPrice = et_sellPrice.getText().toString().trim();
        str_shortDisc = et_shortDesc.getText().toString().trim();
        str_desc = et_desc.getText().toString().trim();
        str_keyFeature = et_keyFeatures.getText().toString().trim();
        str_stock = et_stock.getText().toString().trim();

        if (sw_isFeatured.isEnabled())
            isFeatured = "1";

        if (sw_isNew.isEnabled())
            isNew = "1";

        if (sw_isPopular.isEnabled())
            isPopular = "1";

        if (sw_isSize.isEnabled())
            isSize = "1";


        if (TextUtils.isEmpty(str_code) || TextUtils.isEmpty(str_name) || TextUtils.isEmpty(str_price) || TextUtils.isEmpty(str_discount) || TextUtils.isEmpty(str_sellPrice) || TextUtils.isEmpty(str_shortDisc))
            Utils.showToast(context, "All fields are required");
        else if (imgFileCover == null)
            Utils.showToast(context, "Upload product cover photo");
        else uploadImage(imgFileCover);
    }

    private void uploadImage(MultipartBody.Part imgFile) {
        if (imgFile != null) {
            if (!loadingDialog.isShowing()) loadingDialog.show();
            Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                    .API_uploadImg(imgFile);
            RetrofitClient.callRetrofit(apiCall, "UPLOAD_IMAGES", this);
        } else {
            if (imgFile1 != null)
                uploadImage(imgFile1);
            else requestAddNewProduct();
        }
    }


    private void requestAddNewProduct() {
        Call<JsonObject> apiCall;
        if (!isEditing)
            apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                    .API_addNewProduct(sessionManager.getFirmId(), str_code, str_name, str_price, str_discount, str_sellPrice, str_catId, str_shortDisc, str_desc, str_status,
                            isFeatured, isNew, isPopular, nameCover, nameImg1, nameImg2, nameImg3, nameImg4, nameImg5, nameImg6,
                            str_keyFeature, isSize, str_stock);
        else apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_addEditProduct(sessionManager.getFirmId(), product.getProductId(), str_code, str_name, str_price, str_discount, str_sellPrice, str_catId, str_shortDisc, str_desc, str_status,
                        isFeatured, isNew, isPopular, nameCover, nameImg1, nameImg2, nameImg3, nameImg4, nameImg5, nameImg6,
                        str_keyFeature, isSize, str_stock);

        RetrofitClient.callRetrofit(apiCall, "NEW_PRODUCT", this);
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

                if (pickedImg.equals("cover")) {
                    iv_cover.setImageURI(resultUri);
                    imgFileCover = Utils.ImageToMultipartBody("file", Utils.compressImage(file)); //get file to submit
                    nameCover = file.getName();
                } else if (pickedImg.equals("img1")) {
                    iv_img1.setImageURI(resultUri);
                    imgFile1 = Utils.ImageToMultipartBody("file", Utils.compressImage(file));
                    nameImg1 = file.getName();
                } else if (pickedImg.equals("img2")) {
                    iv_img2.setImageURI(resultUri);
                    imgFile2 = Utils.ImageToMultipartBody("file", Utils.compressImage(file));
                    nameImg2 = file.getName();
                } else if (pickedImg.equals("img3")) {
                    iv_img3.setImageURI(resultUri);
                    imgFile3 = Utils.ImageToMultipartBody("file", Utils.compressImage(file));
                    nameImg3 = file.getName();
                } else if (pickedImg.equals("img4")) {
                    iv_img4.setImageURI(resultUri);
                    imgFile4 = Utils.ImageToMultipartBody("file", Utils.compressImage(file));
                    nameImg4 = file.getName();
                } else if (pickedImg.equals("img5")) {
                    iv_img5.setImageURI(resultUri);
                    imgFile5 = Utils.ImageToMultipartBody("file", Utils.compressImage(file));
                    nameImg5 = file.getName();
                } else if (pickedImg.equals("img6")) {
                    iv_img6.setImageURI(resultUri);
                    imgFile6 = Utils.ImageToMultipartBody("file", Utils.compressImage(file));
                    nameImg6 = file.getName();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Utils.showSnackBar(this, String.valueOf(result.getError()));
            }
        }
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "CATEGORIES":
                try {
                    callback(response);
                    loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "LAST_CODE":
                try {
                    callbackLastCode(response);
                    loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "UPLOAD_IMAGES":
                try {
                    callbackUploadImages(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "NEW_PRODUCT":
                try {
                    callbackNewProduct(response);
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

    private void callback(Response<?> response) throws JSONException {
        List<Category> arrayList = new ArrayList<>();

        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allbanks")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allbanks").getJSONArray("allcatlist");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String categoryId = object.getString("CategoryId");
                            String categoryName = object.getString("CategoryName");

                            arrayList.add(new Category(categoryId, categoryName));
                        }
                        setupCatSpinner(arrayList);
                    }
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    @SuppressLint("SetTextI18n")
    private void callbackLastCode(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {

                String lastCode = jsonObject.getJSONArray("allproducts").getJSONObject(0).getString("LastCode");
                et_code.setText("BSC"+ (Integer.parseInt(lastCode) + 1));
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void callbackUploadImages(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
//            if (jsonObject.getString("success").equals("true")) {
//                imgUploadedCounter = imgUploadedCounter + 1;
//                if (imgUploadedCounter == 2) {
            requestAddNewProduct();
//                } else {
//                    uploadImage(imgFileThumbnail);
//                }
//            } else Utils.showToast(context, jsonObject.getString("success"));
        } else Utils.showToast(context, String.valueOf(responseCode));
    }

    private void callbackNewProduct(Response<?> response) throws JSONException {
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