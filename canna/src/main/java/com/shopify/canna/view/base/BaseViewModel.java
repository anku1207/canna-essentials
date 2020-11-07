package com.shopify.canna.view.base;

import androidx.lifecycle.ViewModel;

import com.shopify.canna.BaseApplication;
import com.shopify.canna.domain.usecases.UseCases;
import com.shopify.canna.core.UseCase.Cancelable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewModel extends ViewModel {

  private final List<Cancelable> tasks = new ArrayList<>();

  protected UseCases useCases() {
    return BaseApplication.instance().useCases();
  }

  protected void addTask(Cancelable task) {
    tasks.add(task);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    for (Cancelable task : tasks) {
      task.cancel();
    }
  }
}
