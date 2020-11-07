package com.shopify.canna.domain.usecases;

import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.internal.Optional;
import com.apollographql.apollo.exception.ApolloException;
import com.shopify.canna.core.UseCase.Callback1;
import com.shopify.canna.core.UseCase.Cancelable;
import com.shopify.canna.data.apollo.Converter;
import com.shopify.canna.domain.CollectionProductPageQuery;
import com.shopify.canna.domain.model.Product;
import com.shopify.canna.util.CallbackExecutors;

import java.util.List;

import javax.annotation.Nonnull;

public final class FetchProductsUseCaseImpl implements FetchProductsUseCase {

  private final CallbackExecutors callbackExecutors;
  private final ApolloClient apoloClient;

  public FetchProductsUseCaseImpl(final CallbackExecutors callbackExecutors, final ApolloClient apolloClient) {
    this.callbackExecutors = callbackExecutors;
    this.apoloClient = apolloClient;
  }

  @Override
  public Cancelable execute(@NonNull final String collectionId, @Nullable final String cursor, final int perPage, @NonNull final Callback1<List<Product>> callback) {
    CollectionProductPageQuery query = CollectionProductPageQuery.builder()
      .perPage(perPage)
      .nextPageCursor(cursor)
      .collectionId(collectionId)
      .build();
    final ApolloQueryCall<Optional<CollectionProductPageQuery.Data>> call = apoloClient.query(query);
    call.enqueue(new Callback(callback, callbackExecutors.handler()));
    return call::cancel;
  }

  private static class Callback extends ApolloCall.Callback<Optional<CollectionProductPageQuery.Data>> {

    private final Callback1<List<Product>> callback;
    private final Handler handler;

    public Callback(final Callback1<List<Product>> callback, final Handler handler) {
      this.callback = callback;
      this.handler = handler;
    }

    @Override
    public void onResponse(@Nonnull final Response<Optional<CollectionProductPageQuery.Data>> response) {
      final Optional<CollectionProductPageQuery.Collection> collection = response.data().get().collection();
      List<Product> products = Converter.convertProducts(((CollectionProductPageQuery.AsCollection) collection.get()).productConnection());
      handler.post(() -> callback.onResponse(products));
    }

    @Override
    public void onFailure(@Nonnull final ApolloException error) {
      handler.post(() -> callback.onError(error));
    }
  }
}
