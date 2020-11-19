package com.shopify.canna.view.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopify.canna.R;
import com.shopify.canna.util.Utility;

public class WebView extends AppCompatActivity {
    android.webkit.WebView webView;
    ImageView back_activity_button;
    ProgressDialog progressBar;
    TextView titleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        if(getSupportActionBar()!=null) getSupportActionBar().hide();
        titleview=findViewById(R.id.title);
        webView = findViewById(R.id.webView);
        progressBar = ProgressDialog.show(WebView.this, null, " Please wait...", false, false);

        String url= getIntent().getStringExtra("url");
        String title=getIntent().getStringExtra("title");

        titleview.setText(title);
        openWebView(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    void openWebView(final String receiptUrl) {




        // webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        webView.getSettings().setBuiltInZoomControls(false);

        //  wv.setWebViewClient(new MyBrowser());
        //webSettings.setDomStorageEnabled(true);

        //wv.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setInitialScale(1);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setMinimumFontSize(16);
        webView.setDrawingCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.WebView.enableSlowWholeDocumentDraw();
        }



        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (!WebView.this.isFinishing() && progressBar != null && !progressBar.isShowing()) {
                    try {
                        progressBar.show();
                    } catch (Exception e) {
                    }
                }
                if (newProgress == 100) {
                    Utility.dismissDialog(WebView.this, progressBar);
                }
            }
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d("WebView", consoleMessage.message());
                return true;
            }
        });


        back_activity_button=findViewById(R.id.back_activity_button);

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        webView.loadUrl(receiptUrl);
     }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return false;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
