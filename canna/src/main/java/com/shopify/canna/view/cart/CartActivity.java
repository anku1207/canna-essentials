/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.shopify.canna.view.cart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.Storefront;
import com.shopify.canna.BaseApplication;
import com.shopify.canna.R;
import com.shopify.canna.R2;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.domain.model.Checkout;
import com.shopify.canna.domain.model.ShopSettings;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utils;
import com.shopify.canna.view.ProgressDialogHelper;
import com.shopify.canna.view.ScreenRouter;
import com.shopify.canna.view.checkout.CheckoutViewModel;
import com.shopify.canna.view.webview.WebViewActivity;
import com.shopify.graphql.support.ID;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public final class CartActivity extends AppCompatActivity {
  @BindView(R2.id.root) View rootView;
  @BindView(R2.id.cart_header) CartHeaderView cartHeaderView;
  @BindView(R2.id.cart_list) CartListView cartListView;
  @BindView(R2.id.toolbar) Toolbar toolbarView;
  @BindView(R2.id.linear_cart_empty) LinearLayout linearLayoutEmptyCart;
  @BindView(R2.id.button_continue_shopping)
  AppCompatButton buttonContinueShopping;

  private CartDetailsViewModel cartDetailsViewModel;
  private CartHeaderViewModel cartHeaderViewModel;

//  private GoogleApiClient googleApiClient;
  private ProgressDialogHelper progressDialogHelper;

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }



  @SuppressWarnings("ConstantConditions") @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    ButterKnife.bind(this);

    setSupportActionBar(toolbarView);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.rof_backbutton);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    progressDialogHelper = new ProgressDialogHelper(this);
    buttonContinueShopping.setOnClickListener(v -> {
        onSupportNavigateUp();
    });
    initViewModels();
//    connectGoogleApiClient();

  }


  @Override protected void onDestroy() {

    super.onDestroy();

    if (progressDialogHelper != null) {
      progressDialogHelper.dismiss();
      progressDialogHelper = null;
    }

  }

  @Override protected void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBundle(RealCartViewModel.class.getName(), cartDetailsViewModel.saveState());
  }

  @Override protected void onRestoreInstanceState(final Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    cartDetailsViewModel.restoreState(savedInstanceState.getBundle(RealCartViewModel.class.getName()));
  }

  private void initViewModels() {
    ShopSettings shopSettings = ((BaseApplication) getApplication()).shopSettings().getValue();
    RealCartViewModel cartViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
      @Override public <T extends ViewModel> T create(final Class<T> modelClass) {
        if (modelClass.equals(RealCartViewModel.class)) {
          //noinspection unchecked
          return (T) new RealCartViewModel(shopSettings);
        } else {
          return null;
        }
      }
    }).get(RealCartViewModel.class);
    cartHeaderViewModel = cartViewModel;
    cartDetailsViewModel = cartViewModel;

    cartDetailsViewModel.webCheckoutCallback().observe(this.getLifecycle(), checkout -> {
      if (checkout != null) {

        Storefront.QueryRootQuery queryAddress = Storefront.query(root -> root
                .customer(Prefs.INSTANCE.getAccessToken(), customer -> customer
                        .addresses(arg -> arg.first(30), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .address1()
                                                .address2()
                                                .city()
                                                .province()
                                                .country()
                                                .firstName()
                                                .lastName()
                                                .phone()
                                                .zip()
                                        )
                                )
                        )
                )
        );
        SampleApplication.graphClient().queryGraph(queryAddress).enqueue(new Handler(Looper.getMainLooper()), resultAddress -> {
          if (resultAddress instanceof GraphCallResult.Success){
            if (((GraphCallResult.Success<Storefront.QueryRoot>) resultAddress).getResponse() != null &&
                    ((GraphCallResult.Success<Storefront.QueryRoot>) resultAddress).getResponse().getData().getCustomer() != null &&
                    ((GraphCallResult.Success<Storefront.QueryRoot>) resultAddress).getResponse().getData().getCustomer().getAddresses() != null &&
                    !((GraphCallResult.Success<Storefront.QueryRoot>) resultAddress).getResponse().getData().getCustomer().getAddresses().getEdges().isEmpty()){

              Storefront.MailingAddressEdge mailingAddressEdge = ((GraphCallResult.Success<Storefront.QueryRoot>) resultAddress).getResponse().getData().getCustomer().getAddresses().getEdges().get(
                      ((GraphCallResult.Success<Storefront.QueryRoot>) resultAddress).getResponse().getData().getCustomer().getAddresses().getEdges().size() - 1);

              Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
                      .setAddress1(mailingAddressEdge.getNode().getAddress1())
                      .setAddress2(mailingAddressEdge.getNode().getAddress2())
                      .setCity(mailingAddressEdge.getNode().getCity())
                      .setCountry("IN")
                      .setFirstName(mailingAddressEdge.getNode().getFirstName())
                      .setLastName(mailingAddressEdge.getNode().getLastName())
                      .setPhone(mailingAddressEdge.getNode().getPhone())
                      .setProvince(mailingAddressEdge.getNode().getProvince())
                      .setZip(mailingAddressEdge.getNode().getZip());
              List<Storefront.MailingAddressEdge> customer= ((GraphCallResult.Success<Storefront.QueryRoot>) resultAddress).getResponse().getData().getCustomer().getAddresses().getEdges();
              Storefront.MutationQuery query = Storefront.mutation((mutationQuery -> mutationQuery
                              .checkoutShippingAddressUpdate(input, new ID(checkout.id), shippingAddressUpdatePayloadQuery -> shippingAddressUpdatePayloadQuery
                                      .checkout(Storefront.CheckoutQuery::webUrl
                                      )
                                      .userErrors(userErrorQuery -> userErrorQuery
                                              .field()
                                              .message()
                                      )
                              )
                      )
              );

              SampleApplication.graphClient().mutateGraph(query).enqueue(result -> {
                if (result instanceof GraphCallResult.Success){
                  if (((GraphCallResult.Success<Storefront.Mutation>) result).getResponse() != null &&
                          ((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData() != null &&
                          ((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().getCheckoutShippingAddressUpdate() != null &&
                          ((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().getCheckoutShippingAddressUpdate().getCheckout().getWebUrl() != null){
                    WebViewActivity.Companion.launchActivity(CartActivity.this,"",
                            ((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().getCheckoutShippingAddressUpdate().getCheckout().getWebUrl());
                  }else {
                    onWebCheckoutConfirmation(checkout);
                  }
                }else {
                  onWebCheckoutConfirmation(checkout);
                }
                Log.d("address_update",""+result);
                return Unit.INSTANCE;
              });
            }else {
              onWebCheckoutConfirmation(checkout);
            }
          }else {
            onWebCheckoutConfirmation(checkout);
          }
          return Unit.INSTANCE;
        });

      }
    });
    cartDetailsViewModel.androidPayStartCheckoutCallback().observe(this.getLifecycle(), payCart -> {
      if (cartHeaderViewModel.googleApiClientConnectionData().getValue() == Boolean.TRUE && payCart != null) {
//        PayHelper.requestMaskedWallet(googleApiClient, payCart, BuildConfig.ANDROID_PAY_PUBLIC_KEY);
      }
    });
    cartDetailsViewModel.androidPayCheckoutCallback().observe(this.getLifecycle(), confirmation -> {
      if (confirmation != null) {
        ScreenRouter.route(this, new AndroidPayConfirmationClickActionEvent(confirmation.checkoutId, confirmation.payCart));
//            , confirmation.maskedWallet));
      }
    });

    cartDetailsViewModel.cartItemsLiveData().observe(this, cartItems -> {
      Log.d("TRIGGER_CART",""+cartItems);
      if (cartItems != null && cartItems.size() > 0){
        linearLayoutEmptyCart.setVisibility(View.GONE);
        cartListView.setVisibility(View.VISIBLE);
        cartHeaderView.setVisibility(View.VISIBLE);
      }else {
        linearLayoutEmptyCart.setVisibility(View.VISIBLE);
        cartHeaderView.setVisibility(View.GONE);
        cartListView.setVisibility(View.GONE);
      }
    });

    cartDetailsViewModel.progressLiveData().observe(this, progress -> {
      if (progress != null) {
        if (progress.show) {
          showProgress(progress.requestId);
        } else {
          hideProgress(progress.requestId);
        }
      }
    });
    cartDetailsViewModel.errorErrorCallback().observe(this.getLifecycle(), error -> {
      if (error != null) {
        showError(error.requestId, error.t, error.message);
      }
    });

    cartHeaderView.bindViewModel(cartHeaderViewModel);

    CartListViewModel cartListViewModel = ViewModelProviders.of(this).get(CartListViewModel.class);
    cartListView.bindViewModel(cartListViewModel);
  }

  private void showProgress(final int requestId) {
    progressDialogHelper.show(requestId, null, getResources().getString(R.string.progress_loading), () -> {
      cartDetailsViewModel.cancelRequest(requestId);
      cartDetailsViewModel.progressLiveData().hide(requestId);
    });
  }

  private void hideProgress(final int requestId) {
    progressDialogHelper.dismiss(requestId);
  }

  private void onWebCheckoutConfirmation(final Checkout checkout) {
    WebViewActivity.Companion.launchActivity(CartActivity.this, "Checkout", checkout.webUrl);
//    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkout.webUrl));
//    startActivity(intent);
  }

  private void showError(final int requestId, final Throwable t, final String message) {
    if (message != null) {
      showAlertErrorMessage(message);
      return;
    }

    if (t instanceof CheckoutViewModel.ShippingRateMissingException) {
      showAlertErrorMessage(getString(R.string.checkout_shipping_select_shipping_rate));
      return;
    }

    showDefaultErrorMessage();
  }

  private void showAlertErrorMessage(final String message) {
    new AlertDialog.Builder(this)
      .setMessage(message)
      .setPositiveButton(android.R.string.ok, (dialog, which) -> {
      })
      .show();
  }

  private void showDefaultErrorMessage() {
    Snackbar snackbar = Snackbar.make(rootView, R.string.default_error, Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
    snackbar.show();
  }
}
