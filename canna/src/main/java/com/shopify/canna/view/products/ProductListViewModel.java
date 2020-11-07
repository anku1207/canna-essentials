package com.shopify.canna.view.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.annotation.NonNull;

import com.shopify.canna.core.UseCase;
import com.shopify.canna.domain.model.Product;
import com.shopify.canna.util.Util;
import com.shopify.canna.view.Constant;
import com.shopify.canna.view.base.BasePaginatedListViewModel;
import com.shopify.canna.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListViewModel extends BasePaginatedListViewModel<Product> {

  private final String collectionId;

  private final LiveData<List<ListItemViewModel>> items = Transformations
    .map(data(), products -> Util.reduce(products, (viewModels, product) -> {
      viewModels.add(new ProductListItemViewModel(product));
      return viewModels;
    }, new ArrayList<ListItemViewModel>()));

  public ProductListViewModel(String collectionId) {
    super();
    this.collectionId = collectionId;
  }

  public LiveData<List<ListItemViewModel>> items() {
    return items;
  }

  @Override
  protected UseCase.Cancelable onFetchData(@NonNull final List<Product> data) {
    String cursor = Util.reduce(data, (acc, val) -> val.cursor, null);
    return useCases()
      .fetchProducts()
      .execute(collectionId, cursor, Constant.PAGE_SIZE, this);
  }
}
