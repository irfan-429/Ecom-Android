package com.maq.ecom.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.maq.ecom.R;
import com.maq.ecom.adapter.ProductCategoryAdapter;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.views.fragments.HomeFragment;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * developed by irfan A.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context context = this;
    protected Toolbar toolbar;
    //    protected TextView toolbarTitle;
    AppCompatImageView logo;

    NavigationView navigationView;
    SessionManager sessionManager = new SessionManager(this);
    DrawerLayout drawer;
    Menu nav_Menu;
    TextView menu_tv_cartCount;
    CircleImageView iv_photo;
    TextView tv_userName, tv_userMobile, tv_login;

    @Override
    protected void onResume() {
        super.onResume();
        if (menu_tv_cartCount != null) {
            setCartCounter();
            setupDrawerItems();
        }
    }

    @Override
    public void setContentView(int view) {
        super.setContentView(view);
        init(view);
        setupToolbar();
        setupDrawer();
        fetchProductItems();
    }

    @SuppressLint("InflateParams")
    private void init(int view) {
        DrawerLayout fullLayout;
        if (sessionManager.isAdmin())
            fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_admin, null);
        else
            fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

        FrameLayout frameLayout = fullLayout.findViewById(R.id.baseActToolbar_frame);
        getLayoutInflater().inflate(view, frameLayout, true);
        super.setContentView(fullLayout);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.base_toolbar);
//        toolbarTitle = toolbar.findViewById(R.id.base_toolbar_title);
        logo = toolbar.findViewById(R.id.base_toolbar_logo);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //set back button on toolbar
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back)); //default
//        toolbar.setNavigationOnClickListener(v -> finish());

        logo.setOnClickListener(v -> Utils.navigateClearTo(this, MainActivity.class));
    }

    /**
     * set toolbar title
     *
     * @param title
     */
    protected void setupToolbar(int title) {
//        toolbarTitle.setText(title);
    }

    /**
     * set toolbar title
     *
     * @param title
     */
    protected void setupToolbar(String title) {
//        toolbarTitle.setText(title);
    }

    /**
     * set toolbar title & navigation icon
     *
     * @param title
     * @param drawable
     */
    protected void setupToolbar(int title, int drawable) {
//        toolbarTitle.setText(title);
//        if (drawable != -1) toolbar.setNavigationIcon(getResources().getDrawable(drawable));
    }

    /**
     * set toolbar title & navigation icon
     *
     * @param title
     * @param drawable
     */
    protected void setupToolbar(String title, int drawable) {
//        toolbarTitle.setText(title);
//        if (drawable != -1) toolbar.setNavigationIcon(getResources().getDrawable(drawable));
    }

    private void setupDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        if (sessionManager.isAdmin())
            navigationView = findViewById(R.id.nav_view_admin);
        else
            navigationView = findViewById(R.id.nav_view);

        //inflating nav header layout
        View headerView = navigationView.getHeaderView(0);
        iv_photo = headerView.findViewById(R.id.nav_header_civ_photo);
        tv_userName = headerView.findViewById(R.id.nav_header_name);
        tv_userMobile = headerView.findViewById(R.id.nav_header_mob);
        tv_login = headerView.findViewById(R.id.nav_header_login);


        nav_Menu = navigationView.getMenu();
        setupDrawerItems();

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setupDrawerItems() {
        nav_Menu.findItem(R.id.nav_logout).setVisible(sessionManager.getIsLoggedIn());

        if (sessionManager.getIsLoggedIn()) {
            Utils.loadProfileImage(context, sessionManager.getProfileImg(), iv_photo);
            tv_login.setVisibility(View.GONE);
            tv_userName.setVisibility(View.VISIBLE);
            tv_userMobile.setVisibility(View.VISIBLE);
        } else {
            iv_photo.setImageResource(R.drawable.img_profile_user);
            tv_login.setVisibility(View.VISIBLE);
            tv_userName.setVisibility(View.GONE);
            tv_userMobile.setVisibility(View.GONE);
        }

        tv_userName.setText(sessionManager.getUserName());
        tv_userMobile.setText(sessionManager.getUserMobile());
        tv_login.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, LoginActivity.class), 200);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (sessionManager.isAdmin())
            getMenuInflater().inflate(R.menu.menu_base_admin, menu);
        else {
            getMenuInflater().inflate(R.menu.menu_base, menu);

            final MenuItem menuItem = menu.findItem(R.id.action_cart);
            View actionView = menuItem.getActionView();
            menu_tv_cartCount = (TextView) actionView.findViewById(R.id.menu_tv_notiBadge);
            actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

            setCartCounter();
        }


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_drawer:
                if (drawer.isDrawerOpen(Gravity.RIGHT))
                    drawer.closeDrawer(Gravity.RIGHT);
                else
                    drawer.openDrawer(Gravity.RIGHT);
                break;

            case R.id.action_cart:
                Utils.navigateTo(context, CartActivity.class);
                finish();
                break;

            case R.id.action_search:
                Utils.navigateTo(context, SearchActivity.class);
                finish();
                break;

            case R.id.action_list:
                showProductCatDialog(R.layout.dialog_product_category);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
//            case R.id.nav_home:
//                Utils.navigateTo(context, ProductsActivity.class);
//                break;
            case R.id.nav_cart:
                Utils.navigateTo(context, CartActivity.class);
                finish();
                break;

            case R.id.nav_notifications:
                Utils.navigateTo(context, NotificationActivity.class);
                finish();
                break;

            case R.id.nav_my_orders:
                Utils.navigateTo(context, MyOrdersActivity.class);
                finish();
                break;

            case R.id.nav_change_pwd:
                Utils.navigateTo(context, ChangePwdActivity.class);
                finish();
                break;

            case R.id.nav_manage_address:
                Utils.navigateTo(context, AddressListActivity.class);
                finish();
                break;

            case R.id.nav_share:
                Utils.shareApp(context, "Check this app!!");
                break;

            case R.id.nav_contact_us:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blue.maqsolution.com/user.php?action=content&FirmId=1&Id=1")));
                break;

            case R.id.nav_about:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blue.maqsolution.com/user.php?action=content&FirmId=1&Id=1")));
                break;

            case R.id.nav_faq:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blue.maqsolution.com/user.php?action=content&FirmId=1&Id=1")));
                break;

            case R.id.nav_terms:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blue.maqsolution.com/user.php?action=content&FirmId=1&Id=1")));
                break;

            case R.id.nav_privacy:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blue.maqsolution.com/user.php?action=content&FirmId=1&Id=1")));
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(context)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Logout", ((dialogInterface, i) -> {
                            sessionManager.clearSharedPref(); //del pref data
//                            setupDrawerItems();
                            Utils.navigateClearTo(context, MainActivity.class);

                        }))
                        .setNegativeButton("No", null)
                        .show();
                break;
        }

        return true;
    }


    void fetchProductItems() {

    }

    private void showProductCatDialog(int layoutId) {
        ViewGroup viewGroup = ((Activity) context).findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        //custom dialog
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();

        //handle sys nav setting
//        dialog.setOnKeyListener((arg0, keyCode, event) -> {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                //disable back press on lock screen
//            }
//            return true;
//        });

        Log.i("TAG", "showProductCatDialog: " + HomeFragment.arrayList.toString());//TODO:
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new ProductCategoryAdapter(context, HomeFragment.arrayList));

    }

}