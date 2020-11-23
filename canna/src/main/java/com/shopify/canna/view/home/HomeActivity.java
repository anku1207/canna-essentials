package com.shopify.canna.view.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shopify.canna.R;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Util;
import com.shopify.canna.util.Utils;
import com.shopify.canna.view.ScreenActionEvent;
import com.shopify.canna.view.ScreenRouter;
import com.shopify.canna.view.cart.CartActivity;
import com.shopify.canna.view.cart.CartClickActionEvent;
import com.shopify.canna.view.login.User_Login;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    Boolean doubleBackpress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(getSupportActionBar()!=null) getSupportActionBar().hide();

        navigation=findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.shop);
    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.shop:
                            fragment=new ShopFragment();
                            break;
                        case R.id.search:
                            // fragment=new Profile();
                            fragment=new SearchFragment();
                            break;
                        case R.id.brands:
                            fragment=new BrandsFragment();
                            break;
                        case R.id.bag:
                            if (Utils.INSTANCE.isUserAuthenticated()){
                                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                                intent.putExtra("title","Bag");
                                startActivity(intent);
                            }else {
                                User_Login.launchActivity(HomeActivity.this);
                                finish();
                            }
                            break;
                        case R.id.account:
                            if (Utils.INSTANCE.isUserAuthenticated()){
                                fragment=new AccountFragment();
                            }else {
                                User_Login.launchActivity(HomeActivity.this);
                                finish();
                            }
                            break;
                    }
                    return loadFragment(fragment);
                }
            };

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Log.d("BACK_HOME",""+getSupportFragmentManager().getBackStackEntryCount()+"\n"+getFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        }else{
            if (doubleBackpress){
                super.onBackPressed();
            }else {
                Utils.INSTANCE.showToast(HomeActivity.this, getString(R.string.backpress_to_exit));
                doubleBackpress = true;
                new Handler().postDelayed(() -> doubleBackpress = false, 2000);
            }
        }
    }

    public void onContinueShoppingClick(){
        this.navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
        this.mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().getItem(0));
    }
}