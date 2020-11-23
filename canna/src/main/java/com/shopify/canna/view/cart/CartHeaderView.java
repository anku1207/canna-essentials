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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.shopify.canna.R;
import com.shopify.canna.R2;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class CartHeaderView extends FrameLayout implements LifecycleOwner {
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R2.id.android_pay_checkout) View androidPayCheckoutView;
  @BindView(R2.id.subtotal) TextView subtotalView;
//  @BindView(R2.id.linear_cart_empty) LinearLayout linearLayoutEmptyCart;
//  @BindView(R2.id.root) LinearLayout linearLayoutRoot;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

  private CartHeaderViewModel viewModel;

  public CartHeaderView(@NonNull final Context context) {
    super(context);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public CartHeaderView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public CartHeaderView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public void bindViewModel(final CartHeaderViewModel viewModel) {
    if (this.viewModel != null) {
      throw new IllegalStateException("Already bound");
    }
    this.viewModel = viewModel;
    viewModel.cartTotalLiveData().observe(this, bigDecimal -> subtotalView.setText(getResources().getString(R.string.price_from, bigDecimal != null ? bigDecimal.toString()  : "0.0") ));

    /*viewModel.cartItemsLiveData().observe(this, cartItems -> {
        if (cartItems != null && !cartItems.isEmpty()){
            linearLayoutEmptyCart.setVisibility(GONE);
            linearLayoutRoot.setVisibility(VISIBLE);
        }else {
            linearLayoutEmptyCart.setVisibility(VISIBLE);
            linearLayoutRoot.setVisibility(GONE);
        }
    });*/

    viewModel.googleApiClientConnectionData().observe(this, connected ->
      androidPayCheckoutView.setVisibility(connected == Boolean.TRUE ? VISIBLE : GONE));
  }

  @Override public @NotNull LifecycleRegistry getLifecycle() {
    return lifecycleRegistry;
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    subtotalView.setText(Currency.getInstance("INR").getSymbol()+"0");
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
  }

  @Override protected void onDetachedFromWindow() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    super.onDetachedFromWindow();
  }

  @OnClick(R2.id.web_checkout) void onWebCheckoutClick() {
    try {
      viewModel.webCheckout();
    }catch (Throwable r){

    }
  }

  @OnClick(R2.id.android_pay_checkout)
  void onAndroidPayCheckoutClick() {
    viewModel.androidPayCheckout();
  }
}
