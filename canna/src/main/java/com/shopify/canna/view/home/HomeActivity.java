package com.shopify.canna.view.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shopify.canna.R;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Util;
import com.shopify.canna.util.Utility;
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
    LinearLayout home_title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(getSupportActionBar()!=null) getSupportActionBar().hide();

        navigation=findViewById(R.id.navigation);
        home_title_bar=findViewById(R.id.home_title_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeTitleByActivity(HomeActivity.this,true,null);
        navigation.setSelectedItemId(R.id.shop);
    }
    public  void changeTitleByActivity(Context context , boolean imageTitle,String title){
        home_title_bar.removeAllViews();
        if(imageTitle){
            home_title_bar.addView(getImageViewTitle());
        }else {
            home_title_bar.addView(getTextView(context,title));
        }
    }


    public ImageView getImageViewTitle(){
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(layoutparams);
        imageView.setImageDrawable(this.getDrawable(R.drawable.headerlogo));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
    }

    public static TextView getTextView(Context context, String txt){
        Typeface typeface = ResourcesCompat.getFont(context, R.font.sweetsanspromedium);
        TextView tv = new TextView(context);
        tv.setText(txt);
        tv.setTypeface(typeface);
        tv.setLayoutParams(getLayoutparams(context,0,0,0,0));
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(context.getResources().getColor(R.color.black) );
        tv.setTextSize(18);
        return tv;
    }
    public static LinearLayout.LayoutParams getLayoutparams(Context context, int left, int top, int right, int bottom){
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        return layoutparams;
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