package com.shopify.canna.domain.usecases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.canna.core.UseCase.Callback1;
import com.shopify.canna.core.UseCase.Cancelable;
import com.shopify.canna.data.graphql.Converter;
import com.shopify.canna.data.graphql.Query;
import com.shopify.canna.domain.model.Product;
import com.shopify.canna.util.CallbackExecutors;
import com.shopify.canna.view.Constant;
import kotlin.Unit;

import java.util.List;

public final class FetchProductsUseCaseImpl implements FetchProductsUseCase {

  private final CallbackExecutors callbackExectors;
  private final GraphClient graphClient;

  public FetchProductsUseCaseImpl(final CallbackExecutors callbackExecutors, final GraphClient graphClient) {
    this.callbackExectors = callbackExecutors;
    this.graphClient = graphClient;
  }

  @Override
  public Cancelable execute(@NonNull final String collectionId, @Nullable final String cursor, final int perPage, @NonNull final Callback1<List<Product>> callback) {
    Storefront.QueryRootQuery query = Storefront.query(root -> root.node(new ID(collectionId), node -> node
        .onCollection(collection -> collection
            .products(args -> args
                .first(Constant.PAGE_SIZE)
                .after(cursor), Query::products
            )
        )
    ));
    final QueryGraphCall call = graphClient.queryGraph(query)
        .enqueue(callbackExectors.handler(), result -> {
          if (result instanceof GraphCallResult.Success) {
            final Storefront.Collection collection = (Storefront.Collection) ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getNode();
            callback.onResponse(Converter.convertProducts(collection.getProducts()));
          } else {
            callback.onError(((GraphCallResult.Failure) result).getError());
          }
          return Unit.INSTANCE;
        });
    return call::cancel;
  }
}
