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

package com.shopify.canna.domain.interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.shopify.buy3.Storefront;
import com.shopify.canna.domain.model.Address;
import com.shopify.canna.util.Prefs;
import com.shopify.graphql.support.ID;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.domain.model.Checkout;
import com.shopify.canna.domain.model.UserMessageError;
import com.shopify.canna.domain.repository.CheckoutRepository;
import com.shopify.canna.domain.repository.UserError;
import com.shopify.graphql.support.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import kotlin.Unit;

import static com.shopify.canna.util.Util.checkNotEmpty;
import static com.shopify.canna.util.Util.mapItems;

public final class RealCheckoutCreateInteractor implements CheckoutCreateInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutCreateInteractor() {
    repository = new CheckoutRepository(SampleApplication.graphClient());
  }

  @Override public Single<Checkout> execute(@NonNull final List<Checkout.LineItem> lineItems) {
    checkNotEmpty(lineItems, "lineItems can't be empty");
    List<Storefront.CheckoutLineItemInput> storefrontLineItems = mapItems(lineItems, lineItem ->
      new Storefront.CheckoutLineItemInput(lineItem.quantity, new ID(lineItem.variantId)));
    Log.d("CHECKOUT_VALUES",""+Objects.requireNonNull(Prefs.INSTANCE.fetchCustomerDetails()).getEmail());

    Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
            .setEmail(Objects.requireNonNull(Prefs.INSTANCE.fetchCustomerDetails()).getEmail())
            .setLineItems(storefrontLineItems);
    return repository.create(input, q -> q.checkout(new CheckoutCreateFragment())).map(Converters::convertToCheckout)
            .onErrorResumeNext(t -> {
              return Single.error((t instanceof UserError) ? new UserMessageError(t.getMessage()) : t);
            });
  }
}
