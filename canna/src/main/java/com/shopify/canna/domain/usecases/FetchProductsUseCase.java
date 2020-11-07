package com.shopify.canna.domain.usecases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shopify.canna.core.UseCase.Callback1;
import com.shopify.canna.core.UseCase.Cancelable;
import com.shopify.canna.domain.model.Product;

import java.util.List;

public interface FetchProductsUseCase {

  Cancelable execute(@NonNull String collectionId, @Nullable String cursor, int perPage, @NonNull Callback1<List<Product>> callback);
}
