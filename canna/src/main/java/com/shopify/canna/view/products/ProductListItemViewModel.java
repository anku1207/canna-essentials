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

package com.shopify.canna.view.products;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shopify.canna.R;
import com.shopify.canna.R2;
import com.shopify.canna.domain.model.Product;
import com.shopify.canna.view.base.ListItemViewHolder;
import com.shopify.canna.view.base.ListItemViewModel;
import com.shopify.canna.view.widget.image.ShopifyDraweeView;

import java.text.NumberFormat;
import java.util.Currency;

import butterknife.BindView;
import butterknife.OnClick;

final class ProductListItemViewModel extends ListItemViewModel<Product> {

  ProductListItemViewModel(final Product payload) {
    super(payload, R.layout.product_list_item);
  }

  @Override
  public ListItemViewHolder<Product, ListItemViewModel<Product>> createViewHolder(
    final ListItemViewHolder.OnClickListener onClickListener) {
    return new ItemViewHolder(onClickListener);
  }

  @Override
  public boolean equalsById(@NonNull final ListItemViewModel other) {
    if (other instanceof ProductListItemViewModel) {
      Product otherPayload = ((ProductListItemViewModel) other).payload();
      return payload().equalsById(otherPayload);
    }
    return false;
  }

  @Override
  public boolean equalsByContent(@NonNull final ListItemViewModel other) {
    if (other instanceof ProductListItemViewModel) {
      Product otherPayload = ((ProductListItemViewModel) other).payload();
      return payload().equals(otherPayload);
    }
    return false;
  }

  static final class ItemViewHolder extends ListItemViewHolder<Product, ListItemViewModel<Product>> {

    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    @BindView(R2.id.image) ShopifyDraweeView imageView;
    @BindView(R2.id.title) TextView titleView;
    @BindView(R2.id.price) TextView priceView;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override
    public void bindModel(@NonNull final ListItemViewModel<Product> listViewItemModel, final int position) {
      super.bindModel(listViewItemModel, position);
      imageView.loadShopifyImage(listViewItemModel.payload().image);
      titleView.setText(listViewItemModel.payload().title);
      priceView.setText(Currency.getInstance("INR").getSymbol()+listViewItemModel.payload().price);
    }

    @SuppressWarnings("unchecked")
    @OnClick({R2.id.image, R2.id.title, R2.id.price})
    void onClick() {
      onClickListener().onClick(itemModel());
    }
  }
}
