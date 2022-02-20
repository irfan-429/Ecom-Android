package com.maq.ecom.views.fragments.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.CategoryRoundedAdapter;
import com.maq.ecom.adapter.SectionsPagerAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Category;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.model.Slider;
import com.maq.ecom.networking.RetrofitClient;
import com.maq.ecom.views.fragments.RecentSellingFragment;
import com.maq.ecom.views.fragments.TopSellingFragment;
import com.maq.ecom.views.fragments.WhatsNewFragment;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class HomeFragmentAdmin extends Fragment implements RetrofitRespondListener {

    String TAG = HomeFragmentAdmin.class.getSimpleName();
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
   public static List<Category> arrayList;
    List<CategoryItem> arrayListTabItem;
    CategoryRoundedAdapter adapter;
    private ArrayList<Slider> bannerList;
    String marqueText="";
    SectionsPagerAdapter sectionsPagerAdapter;

//    @BindView(R.id.image_slider)
//    SliderLayout sliderLayout;
//
//    @BindView(R.id.homeFrag_rv_cat)
//    RecyclerView recyclerView;
//
//
//    @BindView(R.id.marque)
//    TextView marque;
//
//    @BindView(R.id.myRides_view_pager)
//    ViewPager viewPager;
//    @BindView(R.id.myRides_tabs)
//    TabLayout tabs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_admin, container, false);
        setHasOptionsMenu(true); //toolbar item click
        ButterKnife.bind(this, root);
        init();
//        fetchBanners();
//        fetchCategories();
//        fetchTabItems();
//        fetchMarque();
        return root;
    }


    private void init() {
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        arrayList = new ArrayList<>();
        arrayListTabItem = new ArrayList<>();
        bannerList = new ArrayList<>();
//        sectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getChildFragmentManager());

//        marque.setSelected(true);
//
//        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.NONE); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderLayout.setScrollTimeInSec(5); //set scroll delay in seconds :

    }


    private void fetchBanners() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getContext()).create(ApiConfig.class)
                .API_getUserBannerList(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "BANNERS", this);
    }

    private void fetchCategories() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getContext()).create(ApiConfig.class)
                .API_getUserCategoryList(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "CATEGORIES", this);
    }

    private void fetchTabItems() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getContext()).create(ApiConfig.class)
                .API_getTabItems(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "TAB_ITEMS", this);
    }

    private void fetchMarque() {
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getContext()).create(ApiConfig.class)
                .API_fetchMarque(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "MARQUE", this);
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "CATEGORIES":
                try {
                    callback(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "TAB_ITEMS":
                try {
                    callbackTabItems(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "BANNERS":
                try {
                    callbackBanner(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "MARQUE":
                try {
                    callbackMarque(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }

        if (loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    public void onRetrofitFailure(String responseError, String requestName) {
        loadingDialog.dismiss();
        if (responseError.contains(getString(R.string.str_unable_to_resolve_host)))
            responseError = getString(R.string.str_no_internet);

        Utils.showSnackBar(getActivity(), responseError);
    }


    private void callback(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allcategory")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allcategory").getJSONArray("allcatlist");
                    if (jsonArray.length() > 0) {
                        arrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String categoryId = object.getString("CategoryId");
                            String categoryName = object.getString("CategoryName");
                            String categoryImage = object.getString("CategoryImage");

                            arrayList.add(new Category(categoryId, categoryName, categoryImage));
                        }

                        adapter = new CategoryRoundedAdapter(getContext(), arrayList);
//                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        } else Utils.showToast(getContext(), String.valueOf(responseCode));
    }

    private void callbackTabItems(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allitems")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allitems").getJSONArray("allitems");
                    if (jsonArray.length() > 0) {
                        arrayListTabItem.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String type = object.getString("Type");
                            String productId = object.getString("ProductId");
                            String productCode = object.getString("ProductCode");
                            String productName = object.getString("ProductName");
                            String price = object.getString("Price");
                            String discount = object.getString("Discount");
                            String sellingPrice = object.getString("SellingPrice");
                            String shortDesc = object.getString("ShortDesc");
                            String stock = object.getString("Stock");
                            String unit = object.getString("Unit");
                            String ProductCover = object.getString("ProductCover");
                            String description = object.getString("Description");
                            String image1 = object.getString("Image1");
                            String image2 = object.getString("Image2");
                            String image3 = object.getString("Image3");
                            String image4 = object.getString("Image4");
                            String image5 = object.getString("Image5");
                            String image6 = object.getString("Image6");

                            arrayListTabItem.add(new CategoryItem(type, productId, productCode, productName, price, discount, sellingPrice, shortDesc, stock, unit, ProductCover, description,  image1, image2, image3, image4, image5, image6, 0));
                        }

                        initTabs(arrayListTabItem);
                    }
                }
            }
        } else Utils.showToast(getContext(), String.valueOf(responseCode));
    }


    private void initTabs(List<CategoryItem> arrayList) {
        sectionsPagerAdapter.addFragment(new TopSellingFragment(arrayList), "TOP SELLING");
        sectionsPagerAdapter.addFragment(new RecentSellingFragment(arrayList), "RECENT SELLING");
        sectionsPagerAdapter.addFragment(new WhatsNewFragment(arrayList), "WHAT'S NEW");

//        viewPager.setAdapter(sectionsPagerAdapter);
//        tabs.setupWithViewPager(viewPager);
    }

    private void callbackMarque(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
//            List<String> marqueList= new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allmsglist")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allmsglist").getJSONArray("allmsglist");
                    if (jsonArray.length() > 0) {
                        bannerList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String message = object.getString("Message");

                            marqueText=marqueText + message +" ";
                        }

                        if (!marqueText.isEmpty()){
//                            marque.setText(marqueText);
//                            marque.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } else Utils.showToast(getContext(), String.valueOf(responseCode));
    }

    private void callbackBanner(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allbanners")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allbanners").getJSONArray("allbannnerlist");
                    if (jsonArray.length() > 0) {
                        bannerList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String bannerImage = object.getString("BannerImage");
                            String title = object.getString("BannerName");

                            Log.d(TAG, "callbackBanner: " + Utils.IMAGE_COLLECTION + bannerImage);
                            bannerList.add(new Slider(title, Utils.IMAGE_COLLECTION + bannerImage));
                        }

                        setSliderViews(bannerList);
                    }
                }
            }
        } else Utils.showToast(getContext(), String.valueOf(responseCode));
    }

    private void setSliderViews(ArrayList<Slider> bannerList) {
        for (int i = 0; i <bannerList.size(); i++) {

            SliderView sliderView = new SliderView(getContext());

            sliderView.setImageUrl(bannerList.get(0).getImg());
            sliderView.setDescription(bannerList.get(0).getTitle());

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                }
            });

            //at last add this view in your layout :
//            sliderLayout.addSliderView(sliderView);
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_home, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_cart:
//                Utils.navigateTo(getContext(), AddressListActivity.class);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}