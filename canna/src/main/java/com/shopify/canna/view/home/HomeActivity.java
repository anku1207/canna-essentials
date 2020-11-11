package com.shopify.canna.view.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shopify.canna.R;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView navigation;
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
                            Toast.makeText(HomeActivity.this, "search", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.brands:
                            fragment=new BrandsFragment();
                            break;
                        case R.id.bag:
                            Toast.makeText(HomeActivity.this, "bag", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.account:
                            fragment=new AccountFragment();
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
}