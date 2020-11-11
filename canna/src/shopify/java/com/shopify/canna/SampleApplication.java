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

package com.shopify.canna;

import android.text.TextUtils;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.canna.domain.usecases.UseCases;
import com.shopify.canna.domain.usecases.UseCasesImpl;
import com.shopify.canna.util.CallbackExecutors;

import net.danlew.android.joda.JodaTimeAndroid;

import kotlin.Unit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

public class SampleApplication extends BaseApplication {

  private static final String SHOP_PROPERTIES_INSTRUCTION =
      "\n\tAdd your shop credentials to a shop.properties file in the main app folder (e.g. 'app/shop.properties')."
          + "Include these keys:\n" + "\t\tSHOP_DOMAIN=<myshop>.myshopify.com\n"
          + "\t\tAPI_KEY=0123456789abcdefghijklmnopqrstuvw\n";

  private static GraphClient graphClient;

  public static GraphClient graphClient() {
    return graphClient;
  }

  @Override
  protected void initialize() {
    JodaTimeAndroid.init(this);
    initializeGraphClient();
  }

  @Override
  protected UseCases onCreateUseCases() {
    return new UseCasesImpl(CallbackExecutors.createDefault(), graphClient);
  }

  private void initializeGraphClient() {
    String shopUrl = BuildConfig.SHOP_DOMAIN;
    if (TextUtils.isEmpty(shopUrl)) {
      throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "You must add 'SHOP_DOMAIN' entry in "
          + "app/shop.properties, in the form '<myshop>.myshopify.com'");
    }

    String shopifyApiKey = BuildConfig.API_KEY;
    if (TextUtils.isEmpty(shopifyApiKey)) {
      throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "You must populate the 'API_KEY' entry in "
          + "app/shop.properties");
    }

    OkHttpClient httpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.OKHTTP_LOG_LEVEL))
        .build();

    graphClient = GraphClient.Companion.build(this, BuildConfig.SHOP_DOMAIN, BuildConfig.API_KEY,
        builder -> {
          builder.setHttpClient(httpClient);
          builder.httpCache(getCacheDir(), config -> {
            config.setCacheMaxSizeBytes(1024 * 1024 * 10);
            config.setDefaultCachePolicy(HttpCachePolicy.Default.CACHE_FIRST.expireAfter(20, TimeUnit.MINUTES));
            return Unit.INSTANCE;
          });
          return Unit.INSTANCE;
        }, BuildConfig.DEFAULT_LOCALE);
  }
}
