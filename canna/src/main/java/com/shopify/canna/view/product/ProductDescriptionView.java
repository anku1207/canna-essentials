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

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.shopify.canna.R;
import com.shopify.canna.R2;
import com.shopify.canna.domain.model.ProductDetails;
import com.shopify.canna.view.base.HtmlImageGetter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shopify.canna.util.Util.mapItems;
import static com.shopify.canna.util.Util.minItem;

public final class ProductDescriptionView extends NestedScrollView {
  static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R2.id.title) TextView titleView;
  @BindView(R2.id.price) TextView priceView;
  @BindView(R2.id.description) TextView descriptionView;
  @BindView(R2.id.button_add_to_cart) AppCompatButton buttonAddToCart;
  private OnAddToCartClickListener onAddToCartClickListener;

  public ProductDescriptionView(final Context context) {
    super(context);
  }

  public ProductDescriptionView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public ProductDescriptionView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void renderProduct(final String title, final double price) {
    titleView.setText(title);
  }

  @SuppressLint("ResourceType")
  public void renderProduct(final ProductDetails product) {
    titleView.setText(product.title);
    Log.d("AVAILABLE_PRODUCT",""+product.isAvailableForSale);
    descriptionView.setText(Html.fromHtml(product.description, new HtmlImageGetter(descriptionView, getContext()), null));
    if (product.isAvailableForSale){
      priceView.setTextColor(getResources().getColor(R.color.green));
      priceView.setText(getResources().getString(R.string.price_from, String.valueOf(formatMinPrice(product))));
      buttonAddToCart.setSelected(true);
    }else {
      priceView.setTextColor(getResources().getColor(R.color.snackbar_error_background));
      priceView.setText(getResources().getString(R.string.sold_out));
      buttonAddToCart.setSelected(false);
    }
  }

  public void setOnAddToCartClickListener(final OnAddToCartClickListener onAddToCartClickListener) {
    this.onAddToCartClickListener = onAddToCartClickListener;
  }

  @OnClick({R2.id.button_add_to_cart})
  void onAddToCartClick() {
    if (onAddToCartClickListener != null && buttonAddToCart != null && buttonAddToCart.isSelected()) {
      onAddToCartClickListener.onAddToCartClick();
    }else {
      Toast.makeText(getContext(), getResources().getString(R.string.item_not_available),Toast.LENGTH_LONG).show();
    }
  }

  private Double formatMinPrice(final ProductDetails product) {
    List<BigDecimal> prices = mapItems(product.variants, variant -> variant.price);
    BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
    return minPrice.doubleValue();
  }

  public interface OnAddToCartClickListener {
    void onAddToCartClick();
  }
}
