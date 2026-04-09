package com.example.baitapnhom.ui.product;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;

public class ProductDetailViewModelFactory implements ViewModelProvider.Factory {
    private final FruitApplication application;

    public ProductDetailViewModelFactory(FruitApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProductDetailViewModel(application);
    }
}