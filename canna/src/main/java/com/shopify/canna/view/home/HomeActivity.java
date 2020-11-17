package com.shopify.canna.view.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shopify.canna.R;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Util;
import com.shopify.canna.util.Utils;
import com.shopify.canna.view.login.User_Login;

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
                            Toast.makeText(HomeActivity.this, "bag", Toast.LENGTH_SHORT).show();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackpress){
            super.onBackPressed();
        }else {
            Utils.INSTANCE.showToast(HomeActivity.this, getString(R.string.backpress_to_exit));
            doubleBackpress = true;
            new Handler().postDelayed(() -> doubleBackpress = false, 2000);
        }
    }
}