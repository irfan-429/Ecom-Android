package com.maq.ecom.views.fragments.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.adapter.CategoryAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Category;
import com.maq.ecom.networking.RetrofitClient;
import com.maq.ecom.views.activities.CreateCategoryActivity;

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

public class CategoriesFragmentAdmin extends Fragment implements RetrofitRespondListener {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    List<Category> arrayList = new ArrayList<>();
    CategoryAdapter adapter;
    MenuItem sortItem;
    boolean sortByAsc = false;

    @BindView(R.id.catFrag_rv)
    RecyclerView recyclerView;

    @BindView(R.id.catFrag_tv_notFound)
    TextView tv_notFound;

    @OnClick(R.id.catFrag_fab)
    void addNewCat() {
        Utils.navigateTo(getContext(), CreateCategoryActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCategories();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_categories_admin, container, false);
        setHasOptionsMenu(true); //toolbar item click
        ButterKnife.bind(this, root);
        init();

        return root;
    }


    @SuppressLint("SetTextI18n")
    private void init() {
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
    }

    private void fetchCategories() {
        loadingDialog.show();
        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiConfig.class)
                .API_getCategoryList(sessionManager.getFirmId());
        RetrofitClient.callRetrofit(apiCall, "CATEGORIES", this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by:
                if (!sortByAsc) {
                    Collections.sort(arrayList, (o1, o2) -> o1.getCategoryName().compareToIgnoreCase(o2.getCategoryName()));
                    sortByAsc = true;
                    sortItem.setTitle(getResources().getString(R.string.action_sort_ZA));
                } else {
                    Collections.sort(arrayList, (o1, o2) -> o2.getCategoryName().compareToIgnoreCase(o2.getCategoryName()));
                    sortByAsc = false;
                    sortItem.setTitle(getResources().getString(R.string.action_sort_AZ));
                }
                adapter = new CategoryAdapter(getContext(), arrayList);
                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                break;


        }
        return super.onOptionsItemSelected(item);
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

        }

        loadingDialog.dismiss();

    }

    @Override
    public void onRetrofitFailure(String responseError, String requestName) {
        loadingDialog.dismiss();
        if (responseError.contains(getString(R.string.str_unable_to_resolve_host)))
            responseError = getString(R.string.str_no_internet);

        Utils.showSnackBar(getActivity(), responseError);
    }

    @SuppressLint("SetTextI18n")
    private void callback(Response<?> response) throws JSONException {
        int responseCode = response.code();
        if (responseCode == Utils.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            if (jsonObject.getString("error").equals("false")) {
                if (jsonObject.has("allbanks")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("allbanks").getJSONArray("allcatlist");
                    if (jsonArray.length() > 0) {
                        arrayList.clear();
                        tv_notFound.setVisibility(View.INVISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String categoryId = object.getString("CategoryId");
                            String categoryName = object.getString("CategoryName");
                            String categoryBanner = object.getString("CategoryBanner");
                            String categoryImage = object.getString("CategoryImage");
                            String status = object.getString("Status");
                            String products = object.getString("Products");

                            arrayList.add(new Category(categoryId, categoryName, categoryBanner, categoryImage, status, products));
                        }
                        adapter = new CategoryAdapter(getContext(), arrayList);
                        recyclerView.setAdapter(adapter);
                    } else tv_notFound.setVisibility(View.VISIBLE);
                }
            }
        } else Utils.showToast(getActivity(), String.valueOf(responseCode));
    }
}