package com.example.baitapnhom.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private final FruitApplication application;

    public HomeViewModelFactory(FruitApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(application);
    }
}
