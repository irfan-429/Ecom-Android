package com.maq.ecom.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.maq.ecom.R;
import com.maq.ecom.adapter.CategoryItemsAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;

public class RecentSellingFragment extends Fragment {

    SessionManager sessionManager;
    MultipartBody.Part imgFileSlip = null;
    String imgFileName;
    LoadingDialog loadingDialog;
    List<CategoryItem> arrayList;

    @BindView(R.id.frag_rv)
    RecyclerView recyclerView;

    @BindView(R.id.frag_tv_notFound)
    TextView tv_notFound;

    public RecentSellingFragment(List<CategoryItem> arrayList) {
        this.arrayList = arrayList;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recent_selling, container, false);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    private void init() {
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());

        List<CategoryItem> arrayListFiltered = new ArrayList<>();
        for (CategoryItem item : arrayList) {
            if (item.getType().equals("Recentelleing")) {
                arrayListFiltered.add(item);
            }
        }

        if (arrayListFiltered.size() > 0)
            recyclerView.setAdapter(new CategoryItemsAdapter(getContext(), arrayListFiltered));
        else tv_notFound.setVisibility(View.VISIBLE);
    }
}