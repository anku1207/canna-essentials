package com.shopify.canna.domain.usecases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shopify.canna.domain.model.Collection;
import com.shopify.canna.core.UseCase;

import java.util.List;

public interface FetchCollectionsUseCase extends UseCase {

  Cancelable execute(@Nullable String cursor, int perPage, @NonNull Callback1<List<Collection>> callback);
}
