package com.shopify.canna.domain.interactor;

import androidx.annotation.NonNull;

import com.shopify.canna.domain.model.ShopSettings;

import io.reactivex.Single;

public interface ShopSettingInteractor {

  @NonNull Single<ShopSettings> execute();
}
