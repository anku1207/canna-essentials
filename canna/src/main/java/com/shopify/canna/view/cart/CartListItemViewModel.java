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

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shopify.canna.R;
import com.shopify.canna.R2;
import com.shopify.canna.domain.model.CartItem;
import com.shopify.canna.view.base.ListItemViewHolder;
import com.shopify.canna.view.base.ListItemViewModel;
import com.shopify.canna.view.widget.image.ShopifyDraweeView;

import java.text.NumberFormat;
import java.util.Currency;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shopify.canna.util.Util.checkNotNull;

final class CartListItemViewModel extends ListItemViewModel<CartItem> {
  private final OnChangeQuantityClickListener onChangeQuantityClickListener;

  CartListItemViewModel(final CartItem cartItem,
    @NonNull final OnChangeQuantityClickListener onChangeQuantityClickListener) {
    super(cartItem, R.layout.cart_list_item);
    this.onChangeQuantityClickListener = checkNotNull(onChangeQuantityClickListener, "onChangeQuantityClickListener == null");
  }

  @Override public ListItemViewHolder<CartItem, ListItemViewModel<CartItem>> createViewHolder(
    @NonNull final ListItemViewHolder.OnClickListener onClickListener) {
    return new CartListItemViewModel.ItemViewHolder(onChangeQuantityClickListener, onClickListener);
  }

  @Override public boolean equalsById(@NonNull final ListItemViewModel other) {
    if (other instanceof CartListItemViewModel) {
      CartItem otherPayload = ((CartListItemViewModel) other).payload();
      return payload().equalsById(otherPayload);
    }
    return false;
  }

  @Override public boolean equalsByContent(@NonNull final ListItemViewModel other) {
    if (other instanceof CartListItemViewModel) {
      CartItem otherPayload = ((CartListItemViewModel) other).payload();
      return payload().equalsByContent(otherPayload);
    }
    return false;
  }

  static final class ItemViewHolder extends ListItemViewHolder<CartItem, ListItemViewModel<CartItem>> {
    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    @BindView(R2.id.image) ShopifyDraweeView imageView;
    @BindView(R2.id.title) TextView titleView;
    @BindView(R2.id.variant) TextView variantView;
    @BindView(R2.id.price) TextView priceView;
    @BindView(R2.id.quantity) TextView quantityView;
    @BindView(R2.id.divider) View dividerView;
    final OnChangeQuantityClickListener onChangeQuantityClickListener;

    ItemViewHolder(@NonNull final OnChangeQuantityClickListener onChangeQuantityClickListener,
      @NonNull final OnClickListener onClickListener) {
      super(onClickListener);
      this.onChangeQuantityClickListener = checkNotNull(onChangeQuantityClickListener, "onChangeQuantityClickListener == null");
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<CartItem> listViewItemModel, final int position) {
      super.bindModel(listViewItemModel, position);
      imageView.loadShopifyImage(listViewItemModel.payload().image);
      titleView.setText(listViewItemModel.payload().productTitle);
      if (listViewItemModel.payload().variantTitle != null && !listViewItemModel.payload().variantTitle.isEmpty() &&
              !listViewItemModel.payload().variantTitle.equalsIgnoreCase("Default Title")){
        variantView.setText(listViewItemModel.payload().variantTitle);
      }else {
        variantView.setVisibility(View.GONE);
      }
      quantityView.setText(String.valueOf(listViewItemModel.payload().quantity));
      priceView.setText(Currency.getInstance("INR").getSymbol()+listViewItemModel.payload().quantity * listViewItemModel.payload().price.doubleValue());
      dividerView.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @OnClick(R2.id.decrement_quantity)
    void onDecrementQuantityClick() {
      onChangeQuantityClickListener.onRemoveCartItemClick(itemModel().payload());
    }

    @OnClick(R2.id.increment_quantity)
    void onIncrementQuantityClick() {
      onChangeQuantityClickListener.onAddCartItemClick(itemModel().payload());
    }
  }

  interface OnChangeQuantityClickListener {
    void onAddCartItemClick(CartItem cartItem);

    void onRemoveCartItemClick(CartItem cartItem);
  }
}
