package com.shopify.canna.domain.interactor;

import com.shopify.canna.SampleApplication;
import com.shopify.canna.domain.ShopSettingsQuery;
import com.shopify.canna.domain.model.ShopSettings;
import com.shopify.canna.domain.repository.ShopRepository;

import io.reactivex.Single;

public class RealShopSettingInteractor implements ShopSettingInteractor {
  private final ShopRepository repository;

  public RealShopSettingInteractor() {
    repository = new ShopRepository(SampleApplication.apolloClient());
  }

  @Override public Single<ShopSettings> execute() {
    ShopSettingsQuery query = new ShopSettingsQuery();
    return repository.shopSettings(query).map(Converters::convertToShopSettings);
  }
}
