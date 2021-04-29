package com.maq.ecom.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.BannerAdapter;
import com.maq.ecom.adapter.CategoryAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Banner;
import com.maq.ecom.networking.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class BannersActivity extends BaseActivity implements RetrofitRespondListener {

    String TAG = BannersActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    List<Banner> arrayList = new ArrayList<>();
    BannerAdapter adapter;
    MenuItem sortItem;
    boolean sortByAsc = false;

    @BindView(R.id.bannerAct_rv)
    RecyclerView recyclerView;

    @BindView(R.id.bannerAct_tv_notFound)
    TextView tv_notFound;

    @OnClick(R.id.bannerAct_fab)
    void addNewBanner() {
        Utils.navigateTo(context, CreateBannerActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchBanners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banners);
        super.setupToolbar("Banners");
        ButterKnife.bind(this);
        init();

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
    }

    private void fetchBanners() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_getBannerList(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "BANNERS", this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        sortItem = menu.findItem(R.id.action_sort_by);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.str_search_by_anything));
        searchView.setBackground(null);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (adapter != null) adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by:
                if (!sortByAsc) {
                    Collections.sort(arrayList, (o1, o2) -> o1.getBanName().compareToIgnoreCase(o2.getBanName()));
                    sortByAsc = true;
                    sortItem.setTitle(getResources().getString(R.string.action_sort_ZA));
                } else {
                    Collections.sort(arrayList, (o1, o2) -> o2.getBanName().compareToIgnoreCase(o2.getBanName()));
                    sortByAsc = false;
                    sortItem.setTitle(getResources().getString(R.string.action_sort_AZ));
                }
                adapter = new BannerAdapter(context, arrayList);
                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "BANNERS":
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
        if (responseError.contains(getString(R.string.str_unable_to_resolve_host)))
            responseError = getString(R.string.str_no_internet);

        Utils.showSnackBar(context, responseError);
    }

    @SuppressLint("SetTextI18n")
    private void callback(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allbanks")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allbanks").getJSONArray("allbannnerlist");
                    if (jsonArray.length() > 0) {
                        arrayList.clear();
                        tv_notFound.setVisibility(View.INVISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String bannerId = object.getString("BannerId");
                            String bannerName = object.getString("BannerName");
                            String bannerLink = object.getString("BannerLink");
                            String bannerImage = object.getString("BannerImage");
                            String status = object.getString("Status");

                            arrayList.add(new Banner(bannerId, bannerName, bannerLink, bannerImage, status));
                        }
                        adapter = new BannerAdapter(context, arrayList);
                        recyclerView.setAdapter(adapter);
                    } else tv_notFound.setVisibility(View.VISIBLE);
                }
            }
        } else Utils.showToast(context, String.valueOf(responseCode));
    }
}