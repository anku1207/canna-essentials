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

package com.shopify.canna.view.checkout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.shopify.canna.R;
import com.shopify.canna.R2;

import java.math.BigDecimal;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.canna.util.Util.checkNotNull;

public final class TotalSummaryView extends ConstraintLayout {
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R2.id.subtotal) TextView subtotalView;
  @BindView(R2.id.shipping) TextView shippingView;
  @BindView(R2.id.tax) TextView taxView;
  @BindView(R2.id.total) TextView totalView;

  public TotalSummaryView(final Context context) {
    super(context);
  }

  public TotalSummaryView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public TotalSummaryView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void render(@NonNull final BigDecimal subtotal, @NonNull final BigDecimal shipping, @NonNull final BigDecimal tax,
    @NonNull final BigDecimal total) {
    subtotalView.setText(CURRENCY_FORMAT.format(checkNotNull(subtotal, "subtotal == null")));
    shippingView.setText(CURRENCY_FORMAT.format(checkNotNull(shipping, "shipping == null")));
    taxView.setText(CURRENCY_FORMAT.format(checkNotNull(tax, "tax == null")));
    totalView.setText(getResources().getString(R.string.checkout_summary_total,
      CURRENCY_FORMAT.format(checkNotNull(total, "total == null"))));
  }
}
