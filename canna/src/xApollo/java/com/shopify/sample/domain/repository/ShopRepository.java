package com.shopify.canna.domain.repository;

import androidx.annotation.NonNull;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.internal.Optional;
import com.shopify.canna.domain.ShopSettingsQuery;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.canna.RxUtil.rxApolloCall;
import static com.shopify.canna.util.Util.checkNotNull;

public final class ShopRepository {
  private final ApolloClient apolloClient;

  public ShopRepository(@NonNull final ApolloClient apolloClient) {
    this.apolloClient = checkNotNull(apolloClient, "apolloClient == null");
  }

  @NonNull public Single<ShopSettingsQuery.Shop> shopSettings(@NonNull ShopSettingsQuery query) {
    checkNotNull(query, "query == null");
    return rxApolloCall(apolloClient.query(query))
      .map(Optional::get)
      .map(ShopSettingsQuery.Data::shop)
      .subscribeOn(Schedulers.io());
  }
}
