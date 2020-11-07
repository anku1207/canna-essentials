package com.shopify.canna.domain.interactor;

import androidx.annotation.NonNull;

import com.shopify.buy3.Storefront;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.domain.model.ShopSettings;
import com.shopify.canna.domain.repository.ShopRepository;

import io.reactivex.Single;

public final class RealShopSettingInteractor implements ShopSettingInteractor {
  private final ShopRepository repository;

  public RealShopSettingInteractor() {
    repository = new ShopRepository(SampleApplication.graphClient());
  }

  @NonNull @Override public Single<ShopSettings> execute() {
    Storefront.ShopQueryDefinition query = q -> q
      .name()
      .paymentSettings(settings -> settings
        .countryCode()
        .acceptedCardBrands()
      );
    return repository
      .shopSettings(query)
      .map(Converters::convertToShopSettings);
  }
}
