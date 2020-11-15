package com.shopify.canna.view.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.data.graphql.Converter;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.view.home.HomeActivity;
import com.shopify.canna.view.login.User_Login;

import kotlin.Unit;

import static androidx.core.os.HandlerCompat.postDelayed;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        new Handler().postDelayed(() -> {
            Intent mainIntent;
            if (Prefs.INSTANCE.getAccessToken() != null && Prefs.INSTANCE.getAccessToken().length() > 4 &&
                Prefs.INSTANCE.getTokenExpiryTimestamp() > System.currentTimeMillis() &&
                Prefs.INSTANCE.fetchCustomerDetails() != null){
                mainIntent = new Intent(Splash_Screen.this, HomeActivity.class);
            }else {
                mainIntent = new Intent(Splash_Screen.this, User_Login.class);
            }
            startActivity(mainIntent);
            finish();
        }, 1500);
    }
}