package com.maq.ecom.views.activities;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maq.ecom.R;
import com.maq.ecom.adapter.CartAdapter;
import com.maq.ecom.adapter.CategoryItemsAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.Category;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends BaseActivity {

    String TAG = CartActivity.class.getSimpleName();
    Context context = this;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    CategoryItemsAdapter adapter;
    MenuItem sortItem;
    boolean sortByAsc = false;
    Category category;
    TextView menu_tv_cartCount;


    @BindView(R.id.cartAct_rv)
    RecyclerView cartAct_rv;

    @BindView(R.id.cartAct_tv_noCart)
    TextView tv_notFound;
    @BindView(R.id.cartAct_tv_cartItems)
    TextView cartAct_tv_cartItems;
    @BindView(R.id.cartAct_tv_cartAmt)
    TextView cartAct_tv_cartAmt;

    @OnClick(R.id.cartAct_layout_confirmOrder)
    void confirmOrder() {
        if (MainActivity.mCartList.size() == 0)
            Utils.showToast(context, "Cart is empty");
        else
            context.startActivity(new Intent(context, AddressListActivity.class)
                    .putExtra("cart", (Serializable) MainActivity.mCartList)
            );

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (menu_tv_cartCount != null)
//            setCartCounter();
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        super.setupToolbar("Your Cart");
        ButterKnife.bind(this);
        init();
        fetchCatItems();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        loadingDialog = new LoadingDialog(context);
        sessionManager = new SessionManager(context);
//        category = (Category) getIntent().getSerializableExtra("cart");

    }

    private void fetchCatItems() {
//        arrayList= (List<CartItem> )getIntent().getSerializableExtra("cart_items");
        Log.d(TAG, "cartSize: " + MainActivity.mCartList.size());
        if (MainActivity.mCartList.size() > 0) {
            tv_notFound.setVisibility(View.GONE);
            cartAct_rv.setAdapter(new CartAdapter(context, MainActivity.mCartList));
        } else tv_notFound.setVisibility(View.VISIBLE);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_cart, menu);
//        final MenuItem menuItem = menu.findItem(R.id.action_cart);
//
//        View actionView = menuItem.getActionView();
//        menu_tv_cartCount = (TextView) actionView.findViewById(R.id.menu_tv_notiBadge);
//        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
//        setCartCounter();
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_cart:
//                Utils.navigateTo(context, CartActivity.class);
//                break;
//
//            case R.id.action_search:
//                Utils.navigateTo(context, SearchActivity.class);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    void setCartCounter() {
//        if (MainActivity.mCartList.size() > 0) {
//            menu_tv_cartCount.setText(String.valueOf(MainActivity.mCartList.size()));
//            menu_tv_cartCount.setVisibility(View.VISIBLE);
//        } else menu_tv_cartCount.setVisibility(View.GONE);
//    }

}