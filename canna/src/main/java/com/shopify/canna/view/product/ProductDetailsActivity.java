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

package com.shopify.canna.view.product;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.shopify.canna.R;
import com.shopify.canna.R2;
import com.shopify.canna.domain.model.ProductDetails;
import com.shopify.canna.util.Utils;
import com.shopify.canna.view.ScreenRouter;
import com.shopify.canna.view.cart.CartClickActionEvent;
import com.shopify.canna.view.login.User_Login;
import com.shopify.canna.view.widget.image.ImageGalleryView;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.shopify.canna.util.Util.checkNotNull;

public final class ProductDetailsActivity extends AppCompatActivity {
  public static final String EXTRAS_PRODUCT_ID = "product_id";
  public static final String EXTRAS_PRODUCT_IMAGE_URL = "product_image_url";
  public static final String EXTRAS_PRODUCT_TITLE = "product_title";
  public static final String EXTRAS_PRODUCT_PRICE = "product_price";

  @BindView(R2.id.root) View rootView;
  @BindView(R2.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayoutView;
  @BindView(R2.id.toolbar) Toolbar toolbarView;
  @BindView(R2.id.image_gallery) ImageGalleryView imageGalleryView;
  @BindView(R2.id.product_description) ProductDescriptionView productDescriptionView;

  private ProductViewModel productViewModel;


  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override public boolean onCreateOptionsMenu(final Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    toolbarView.setTitleTextAppearance(ProductDetailsActivity.this, R.style.SweetSansMediumTextAppearance);
    menu.findItem(R.id.cart).getActionView().setOnClickListener(v -> {
      if (Utils.INSTANCE.isUserAuthenticated()){
        ScreenRouter.route(this, new CartClickActionEvent());
      }else {
        User_Login.launchActivity(ProductDetailsActivity.this);
        finish();
      }
    });
    return true;
  }

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_details);
    ButterKnife.bind(this);

    String productId = getIntent().getStringExtra(EXTRAS_PRODUCT_ID);
    String productTitle = getIntent().getStringExtra(EXTRAS_PRODUCT_TITLE);
    String productImageUrl = getIntent().getStringExtra(EXTRAS_PRODUCT_IMAGE_URL);
    double productPrice = getIntent().getDoubleExtra(EXTRAS_PRODUCT_PRICE, 0);

    checkNotNull(productId, "productId == null");
    checkNotNull(productTitle, "productTitle == null");

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(productTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    initViewModels(productId);

    imageGalleryView.renderImages(TextUtils.isEmpty(productImageUrl) ? Collections.emptyList()
            : Collections.singletonList(productImageUrl));
    swipeRefreshLayoutView.setOnRefreshListener(() -> productViewModel.refetch());
    productDescriptionView.renderProduct(productTitle, productPrice);
    productDescriptionView.setOnAddToCartClickListener(() -> productViewModel.addToCart());

  }


  private void initViewModels(final String productId) {
    productViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
      @SuppressWarnings("unchecked") @Override public <T extends ViewModel> T create(final Class<T> modelClass) {
        if (modelClass.equals(RealProductViewModel.class)) {
          return (T) new RealProductViewModel(productId);
        } else {
          return null;
        }
      }
    }).get(RealProductViewModel.class);
    productViewModel.productLiveData().observe(this, this::renderProduct);
    productViewModel.progressLiveData().observe(this, progress -> {
      if (progress != null) {
        swipeRefreshLayoutView.setRefreshing(progress.show);
      }
    });
    productViewModel.errorErrorCallback().observe(this.getLifecycle(), error -> {
      if (error != null) {
        showDefaultErrorMessage();
      }
    });
  }
  private void renderProduct(final ProductDetails product) {
    imageGalleryView.renderImages(product.images);
    productDescriptionView.renderProduct(product);
  }
  private void showDefaultErrorMessage() {
    Snackbar snackbar = Snackbar.make(rootView, R.string.default_error, Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
    snackbar.show();
  }
}
