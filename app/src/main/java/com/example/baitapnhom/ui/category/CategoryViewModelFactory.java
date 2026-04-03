package com.example.baitapnhom.ui.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;

public class CategoryViewModelFactory implements ViewModelProvider.Factory {
    private final FruitApplication application;

    public CategoryViewModelFactory(FruitApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CategoryViewModel(application);
    }
}
