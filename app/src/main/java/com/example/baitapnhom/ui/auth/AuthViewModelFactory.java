package com.example.baitapnhom.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    private final FruitApplication application;

    public AuthViewModelFactory(FruitApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AuthViewModel(application);
    }
}
