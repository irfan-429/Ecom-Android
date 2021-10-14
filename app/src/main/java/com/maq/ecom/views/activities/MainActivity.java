package com.maq.ecom.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.model.CategoryItem;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context context = this;
    AppBarConfiguration mAppBarConfiguration;
    public MenuItem menuItemSync;
    SessionManager sessionManager = new SessionManager(this);
    NavigationView navigationView;
    DrawerLayout drawer;
    Menu nav_Menu;
    TextView menu_tv_cartCount;
    CircleImageView iv_photo;
    TextView tv_userName, tv_userMobile, tv_login;

    public static List<CategoryItem> mCartList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        if (!sessionManager.isAdmin())
            if (menu_tv_cartCount != null) {
                setCartCounter();
                setupDrawerItems();
            }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sessionManager.isAdmin())
            setContentView(R.layout.activity_main_admin);
        else setContentView(R.layout.activity_main);


        setupToolbar();
        setupDrawer();
        setupBottomNavView();
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }


    private void setupDrawer() {
        drawer = findViewById(R.id.drawer_layout);

        if (sessionManager.isAdmin())
            navigationView = findViewById(R.id.nav_view_admin);
        else navigationView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if (sessionManager.isAdmin())
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home_admin, R.id.nav_categories_admin, R.id.nav_noti, R.id.nav_profile)
//                .setDrawerLayout(drawer)
                    .build();
        else mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_categories, R.id.nav_noti, R.id.nav_profile)
//                .setDrawerLayout(drawer)
                .build();


        //inflating nav header layout
        View headerView = navigationView.getHeaderView(0);
        iv_photo = headerView.findViewById(R.id.nav_header_civ_photo);
        tv_userName = headerView.findViewById(R.id.nav_header_name);
        tv_userMobile = headerView.findViewById(R.id.nav_header_mob);
        tv_login = headerView.findViewById(R.id.nav_header_login);


        nav_Menu = navigationView.getMenu();
        setupDrawerItems();
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

    private void setupBottomNavView() {
        BottomNavigationView bottomNavigationView;
//        BottomNavigationView bottomNavigationViewAdmin = findViewById(R.id.btm_nav_view_admin);

        if (sessionManager.isAdmin()) {
            bottomNavigationView = findViewById(R.id.btm_nav_view_admin);
//            bottomNavigationView.setVisibility(View.INVISIBLE);
//            bottomNavigationViewAdmin.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView = findViewById(R.id.btm_nav_view);
//            bottomNavigationView.setVisibility(View.VISIBLE);
//            bottomNavigationViewAdmin.setVisibility(View.INVISIBLE);
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        if (sessionManager.isAdmin()) {
//            NavigationUI.setupWithNavController(bottomNavigationViewAdmin, navController);
//        } else NavigationUI.setupWithNavController(bottomNavigationView, navController);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setItemIconTintList(null); //to get original colors of nav icons

        //drawer menu items
//        Menu menu =navigationView.getMenu();
//        MenuItem navCrateStatement = menu.findItem(R.id.nav_crate_statement);
//        if (!sessionManager.isAdmin()){
//            navCrateStatement.setVisible(false);
//        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
//            case R.id.nav_home:
//                Utils.navigateTo(context, ProductsActivity.class);
//                break;
            case R.id.nav_cart:
                Utils.navigateTo(context, CartActivity.class);
                break;

            case R.id.nav_banners:
                Utils.navigateTo(context, BannersActivity.class);
                break;

            case R.id.nav_product:
                Utils.navigateTo(context, ProductsActivity.class);
                break;

            case R.id.nav_notifications:
                Utils.navigateTo(context, NotificationActivity.class);
                break;

            case R.id.nav_my_orders:
                Utils.navigateTo(context, MyOrdersActivity.class);
                break;

            case R.id.nav_orders:
                Utils.navigateTo(context, OrdersActivity.class);
                break;

            case R.id.nav_change_pwd:
                Utils.navigateTo(context, ChangePwdActivity.class);
                break;

            case R.id.nav_manage_address:
                Utils.navigateTo(context, AddressListActivity.class);
                break;

            case R.id.nav_contact_us:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")));
                break;

            case R.id.nav_about:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")));
                break;

            case R.id.nav_share:
                Utils.shareApp(context, "Check this app!!");
                break;

            case R.id.nav_faq:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")));
                break;

            case R.id.nav_terms:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")));
                break;

            case R.id.nav_privacy:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")));
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(context)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Logout", ((dialogInterface, i) -> {
                            sessionManager.clearSharedPref(); //del pref data
                            setupDrawerItems();
                        }))
                        .setNegativeButton("No", null)
                        .show();
                break;
        }


        return true;
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
                Intent intent = new Intent();
                intent.setAction("getCroppedImgURI");
                intent.putExtra("URI", String.valueOf(resultUri));
                sendBroadcast(intent);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Utils.showSnackBar(this, String.valueOf(result.getError()));
            }
        } else if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                setupDrawerItems();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (sessionManager.isAdmin())
            getMenuInflater().inflate(R.menu.menu_main_admin, menu);
        else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
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
                break;

            case R.id.action_search:
                Utils.navigateTo(context, SearchActivity.class);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setCartCounter() {
        if (MainActivity.mCartList.size() > 0) {
            menu_tv_cartCount.setText(String.valueOf(MainActivity.mCartList.size()));
            menu_tv_cartCount.setVisibility(View.VISIBLE);
        } else menu_tv_cartCount.setVisibility(View.GONE);
    }

}