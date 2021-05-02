package com.maq.ecom.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.model.Slider;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryItemDetailsActivity extends BaseActivity {

    String TAG = CategoryItemDetailsActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    CategoryItem categoryItem;
    TextView menu_tv_cartCount;
    boolean isFound = false;
    int foundIndex;
    boolean enable = false;

//    @BindView(R.id.catItem_img)
//    AppCompatImageView catItem_img;

    @BindView(R.id.image_slider)
    SliderLayout sliderLayout;

    @BindView(R.id.catItem_name)
    TextView catItem_name;
    @BindView(R.id.catItem_code)
    TextView catItem_code;
    @BindView(R.id.catItem_desc)
    TextView catItem_dessc;
    @BindView(R.id.catItem_selling_price)
    TextView catItem_selling_price;
    @BindView(R.id.catItem_cost)
    TextView catItem_cost;
    @BindView(R.id.catItem_stock)
    TextView catItem_stock;

    @BindView(R.id.catItem_number_picker)
    NumberPicker numberPicker;

    @BindView(R.id.btn_addToCart)
    Button btn_addToCart;

    @OnClick(R.id.btn_addToCart)
    void addToCart() {
        if (!enable)
            return;

        if (isFound) MainActivity.mCartList.set(foundIndex, categoryItem);
        else MainActivity.mCartList.add(categoryItem);
        Utils.showToast(context, "Added to Cart");
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menu_tv_cartCount != null)
            setCartCounter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item_details);
        ButterKnife.bind(this);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
        categoryItem = (CategoryItem) getIntent().getSerializableExtra("category_item");
        super.setupToolbar(categoryItem.getCategoryName());

        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.NONE); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(5); //set scroll delay in seconds :

//        Utils.loadImage(context, categoryItem.getProductCover(), catItem_img);
        catItem_name.setText(categoryItem.getCategoryName());
        catItem_code.setText(categoryItem.getProductCode());
        catItem_dessc.setText(categoryItem.getDescription());
        catItem_stock.setText("Available Stock " + (int) Double.parseDouble(categoryItem.getStock()));
        catItem_selling_price.setText(getResources().getString(R.string.INR_symbol) + categoryItem.getSellingPrice());

        if (categoryItem.getPrice() != null && !categoryItem.getPrice().isEmpty() && !categoryItem.getPrice().equals("0.00") && !categoryItem.getPrice().equals(categoryItem.getSellingPrice())) {
            catItem_cost.setText(getResources().getString(R.string.INR_symbol) + categoryItem.getPrice());
            catItem_cost.setPaintFlags(catItem_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        numberPicker.setFocusable(true);
        numberPicker.setMin(1);
        numberPicker.setMax((int) Double.parseDouble(categoryItem.getStock()));
        numberPicker.setValue(1); //default val
        if (MainActivity.mCartList.size() > 0)
            for (CategoryItem item : MainActivity.mCartList)
                if (item.getProductId().equals(categoryItem.getProductId()))
                    numberPicker.setValue(item.getQty());

        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void valueChanged(int value, ActionEnum action) {
                if (!enable) {
                    enable = true;
                    btn_addToCart.setEnabled(true);
                    btn_addToCart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }

                categoryItem.setQty(value);

                if (MainActivity.mCartList.size() > 0)
                    for (int i = 0; i < MainActivity.mCartList.size(); i++) {
                        if (MainActivity.mCartList.get(i).getProductId().equals(categoryItem.getProductId())) {
                            isFound = true;
                            foundIndex = i;
                            break;
                        } else isFound = false;
                    }
            }
        });

        ArrayList<String> imges = new ArrayList();
        imges.add(categoryItem.getProductCover());
        imges.add(categoryItem.getImage1());
        imges.add(categoryItem.getImage2());
        imges.add(categoryItem.getImage3());
        imges.add(categoryItem.getImage4());
        imges.add(categoryItem.getImage5());
        imges.add(categoryItem.getImage6());

        ArrayList<String> finalList = new ArrayList();

        for (String img : imges)
            if (img != null && !img.isEmpty())
                finalList.add(Utils.IMAGE_COLLECTION + img);



        setSliderViews(finalList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();
        menu_tv_cartCount = (TextView) actionView.findViewById(R.id.menu_tv_notiBadge);
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        setCartCounter();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Utils.navigateTo(context, CartActivity.class);
                break;

            case R.id.action_search:
                Utils.navigateTo(context, SearchActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    void setCartCounter() {
        if (MainActivity.mCartList.size() > 0) {
            menu_tv_cartCount.setText(String.valueOf(MainActivity.mCartList.size()));
            menu_tv_cartCount.setVisibility(View.VISIBLE);
        } else menu_tv_cartCount.setVisibility(View.GONE);
    }

    private void setSliderViews(ArrayList<String> bannerList) {
        for (int i = 0; i < bannerList.size(); i++) {
            SliderView sliderView = new SliderView(context);
            sliderView.setImageUrl(bannerList.get(i));

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }


}