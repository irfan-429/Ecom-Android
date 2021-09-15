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
import com.maq.ecom.interfaces.ItemListener;
import com.maq.ecom.model.CategoryItem;
import com.maq.ecom.views.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;

public class RecentSellingFragment extends Fragment implements ItemListener {

    SessionManager sessionManager;
    MultipartBody.Part imgFileSlip = null;
    String imgFileName;
    LoadingDialog loadingDialog;
    List<CategoryItem> arrayList;
    CategoryItemsAdapter adapter;

    @BindView(R.id.frag_rv)
    RecyclerView recyclerView;

    @BindView(R.id.frag_tv_notFound)
    TextView tv_notFound;

    public RecentSellingFragment(List<CategoryItem> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
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

        if (arrayListFiltered.size() > 0) {
            adapter = new CategoryItemsAdapter(getContext(), arrayListFiltered, this::onItemClick);
            recyclerView.setAdapter(adapter);
        }        else tv_notFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick() {
        ((MainActivity)getActivity()).setCartCounter();
    }

}