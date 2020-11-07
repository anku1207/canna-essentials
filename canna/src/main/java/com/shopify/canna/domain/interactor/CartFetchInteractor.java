package com.shopify.canna.domain.interactor;

import androidx.annotation.NonNull;

import com.shopify.canna.domain.model.Cart;

public interface CartFetchInteractor {
  @NonNull Cart execute();
}
