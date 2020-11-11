package com.shopify.canna.view.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.RetryHandler;
import com.shopify.buy3.Storefront;
import com.shopify.canna.BuildConfig;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.view.home.HomeActivity;
import com.shopify.canna.view.splash.Splash_Screen;
import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.Input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

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

        /*Storefront.CustomerCreateInput input = new Storefront.CustomerCreateInput("akashchauhan.er@gmail.com","123456")
                .setFirstName("Akash")
                .setLastName("Chauhan")
                .setAcceptsMarketing(true)
                .setPhone("917065908608");

        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerCreate(input, query -> query
                        .customer(customer -> customer
                                .id()
                                .email()
                                .firstName()
                                .lastName()
                        )
                        .userErrors(userError -> userError
                                .field()
                                .message()
                        )
                )
        );*/
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.OKHTTP_LOG_LEVEL))
                .build();
        GraphClient g = GraphClient.Companion.build(this, BuildConfig.SHOP_DOMAIN, BuildConfig.API_KEY,
                builder -> {
                    builder.setHttpClient(httpClient);
                    builder.httpCache(getCacheDir(), config -> {
                        config.setCacheMaxSizeBytes(1024 * 1024 * 10);
                        config.setDefaultCachePolicy(HttpCachePolicy.Default.CACHE_FIRST.expireAfter(20, TimeUnit.MINUTES));
                        return Unit.INSTANCE;
                    });
                    return Unit.INSTANCE;
                }, BuildConfig.DEFAULT_LOCALE);

        /*MutationGraphCall graphCall = g.mutateGraph(mutationQuery);
        graphCall.enqueue(new Handler(Looper.getMainLooper()), result -> {
            if (result instanceof GraphCallResult.Success){
                Log.d("RES_CALL","Success : "+((GraphCallResult.Success<Storefront.Mutation>) result).getResponse()+"\n"+
                        ((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().responseData);
            }else {
                Log.d("RES_CALL","Fails : "+result);
            }
            return Unit.INSTANCE;
        });*/

        Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput("amit.pandey@mreservicesindia.com", "123456");
        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAccessTokenCreate(input, query -> query
                        .customerAccessToken(customerAccessToken -> customerAccessToken
                                .accessToken()
                                .expiresAt()
                        )
                        .userErrors(userError -> userError
                                .field()
                                .message()
                        )
                )
        );

        MutationGraphCall graphCall = g.mutateGraph(mutationQuery);
        graphCall.enqueue(new Handler(Looper.getMainLooper()), result -> {
            if (result instanceof GraphCallResult.Success){
                if (((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().getCustomerAccessTokenCreate() != null){
                    final Storefront.CustomerAccessTokenCreatePayload token = Objects.requireNonNull(((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData()).getCustomerAccessTokenCreate();
                    if (token.getUserErrors() != null && !token.getUserErrors().isEmpty()){
                        Log.d("RES_CALL","Success : "+token.getUserErrors().get(0).getMessage());
                    }else {
                        Log.d("RES_CALL","Success : "+token.getCustomerAccessToken().getExpiresAt()+"\n"+
                                token.getCustomerAccessToken().getAccessToken());
                    }
                }else{

                }
            }else {
//                Log.d("RES_CALL","Fails : "+result);
            }
            return Unit.INSTANCE;
        });

    }
}