package com.shopify.canna.view.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.shopify.canna.R;
import com.shopify.canna.view.home.HomeActivity;
import com.shopify.canna.view.splash.Splash_Screen;

public class User_Login extends AppCompatActivity {
    BottomSheetBehavior behavior;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__login);

        WebView webview = (WebView)findViewById(R.id.webview);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);


        View bottomSheet = findViewById(R.id.createAccount);
        behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


        Button button= findViewById(R.id.newuser);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Intent mainIntent = new Intent(User_Login.this, HomeActivity.class);
                startActivity(mainIntent);
                finish();

            }
        });

        //dfgcfbcbcbcfg


        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/test.gif");
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        // disable scroll on touch
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
    }
}