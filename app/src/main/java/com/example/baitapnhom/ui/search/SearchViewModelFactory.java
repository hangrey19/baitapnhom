package com.example.baitapnhom.ui.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    private final FruitApplication application;

    public SearchViewModelFactory(FruitApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(application);
    }
}