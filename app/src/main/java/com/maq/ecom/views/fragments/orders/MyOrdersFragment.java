package com.maq.ecom.views.fragments.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.maq.ecom.R;
import com.maq.ecom.adapter.MyOrderAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.OrderStatusListener;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.MyOrder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class MyOrdersFragment extends Fragment implements RetrofitRespondListener,  OrderStatusListener {

    SessionManager sessionManager;

    LoadingDialog loadingDialog;
    MyOrderAdapter adapter;
    List<MyOrder> myOrders;
//    OrderStatusListener listener;


    @BindView(R.id.myOrdersAct_rv)
    RecyclerView myOrdersAct_rv;

    @BindView(R.id.tv_noFound)
    TextView tv_notFound;

    public MyOrdersFragment(List<MyOrder> arrayList) {
        myOrders = arrayList;
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        if (adapter != null)
//            adapter.notifyDataSetChanged();
//    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ButterKnife.bind(this, root);
        init();
        setAdapter();
        return root;
    }

    private void init() {
        loadingDialog = new LoadingDialog(getContext());
        sessionManager = new SessionManager(getContext());
    }


    private void setAdapter() {
        if (myOrders.size() > 0) {
            tv_notFound.setVisibility(View.GONE);
            adapter = new MyOrderAdapter(getContext(), myOrders, this::onStatusUpdate);
            myOrdersAct_rv.setAdapter(adapter);
        } else tv_notFound.setVisibility(View.VISIBLE);
    }


    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
//        switch (requestName) {
//            case "MY_ORDERS":
//                try {
//                    callbackList(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//        loadingDialog.dismiss();

    }

    @Override
    public void onRetrofitFailure(String responseError, String requestName) {
        loadingDialog.dismiss();
        if (responseError.contains(getString(R.string.str_unable_to_resolve_host)))
            responseError = getString(R.string.str_no_internet);

        Utils.showSnackBar(getContext(), responseError);
    }


    @Override
    public void onStatusUpdate(String status, int position) {
        myOrders.get(position).setStatus(status); //update status locally
        adapter.notifyDataSetChanged();
    }
}