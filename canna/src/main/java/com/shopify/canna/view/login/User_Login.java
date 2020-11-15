package com.shopify.canna.view.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.EditText;
import android.widget.ProgressBar;

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
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Util;
import com.shopify.canna.util.Utils;
import com.shopify.canna.view.bottom_sheet.CreateAccountBottomSheet;
import com.shopify.canna.view.bottom_sheet.RecoverPasswordBottomSheet;
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
import timber.log.Timber;

public class User_Login extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__login);

        WebView webview = (WebView)findViewById(R.id.webview);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);

        editTextEmail = findViewById(R.id.edt_email);
        editTextPassword = findViewById(R.id.edt_password);
        progressBar = findViewById(R.id.progress);

        AppCompatButton button= findViewById(R.id.newuser);
        button.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (validInput()){
                Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput(editTextEmail.getText().toString(), editTextPassword.getText().toString());
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

                SampleApplication.graphClient().mutateGraph(mutationQuery).enqueue(new Handler(Looper.getMainLooper()), result -> {
                    if (result instanceof GraphCallResult.Success){
                        if (((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().getCustomerAccessTokenCreate() != null){
                            Storefront.CustomerAccessTokenCreatePayload token = Objects.requireNonNull(((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData()).getCustomerAccessTokenCreate();
                            if (token.getUserErrors() != null && !token.getUserErrors().isEmpty()){
                                Utils.INSTANCE.showToast(User_Login.this, token.getUserErrors().get(0).getMessage());
                            }else {
                                Prefs.INSTANCE.setAccessToken(token.getCustomerAccessToken().getAccessToken());
                                Prefs.INSTANCE.setTokenExpiryTimestamp(token.getCustomerAccessToken().getExpiresAt().getMillis());
                                getUserProfile();
                                Timber.d("Expiry time : %s", token.getCustomerAccessToken().getExpiresAt().getMillis());
                            }
                        }else{
                            Utils.INSTANCE.showToast(User_Login.this, getString(R.string.something_wrong));
                        }
                    }else {
                        Utils.INSTANCE.showToast(User_Login.this, getString(R.string.something_wrong));
                    }
                    progressBar.setVisibility(View.GONE);
                    return Unit.INSTANCE;
                });
            }else {
                Utils.INSTANCE.showToast(User_Login.this, getString(R.string.invalid_input));
                progressBar.setVisibility(View.GONE);
            }

        });


        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/test.gif");
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        // disable scroll on touch
        webview.setOnTouchListener((v, event) -> (event.getAction() == MotionEvent.ACTION_MOVE));

    }

    private void getUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(Prefs.INSTANCE.getAccessToken(), customer -> customer
                        .firstName()
                        .lastName()
                        .email()
                )
        );

        SampleApplication.graphClient().queryGraph(query).enqueue(new Handler(Looper.getMainLooper()), result -> {
            if (result instanceof GraphCallResult.Success){
                progressBar.setVisibility(View.GONE);
                if (((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getCustomer() != null){
                    Storefront.Customer customer = ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getCustomer();
                    Prefs.INSTANCE.storeCustomerDetails(customer);
                    Intent mainIntent = new Intent(User_Login.this, HomeActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else {
                    Utils.INSTANCE.showToast(User_Login.this, getString(R.string.something_wrong));
                }
            }else {
                Utils.INSTANCE.showToast(User_Login.this, getString(R.string.something_wrong));
                progressBar.setVisibility(View.GONE);
            }
            return Unit.INSTANCE;
        });
    }

    private boolean validInput() {
        return Utils.INSTANCE.isValidEmail(editTextEmail.getText().toString()) &&
                Utils.INSTANCE.isValidPassword(editTextPassword.getText().toString());
    }

    public void signUp(View view) {
        CreateAccountBottomSheet.Companion.showDialog(getSupportFragmentManager());
    }

    public void recoverPassword(View view) {
        RecoverPasswordBottomSheet.Companion.showDialog(getSupportFragmentManager());
    }

}