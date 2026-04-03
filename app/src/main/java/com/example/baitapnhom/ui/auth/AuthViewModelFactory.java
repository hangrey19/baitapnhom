package com.example.baitapnhom.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.UserRepository;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    private final UserRepository     userRepo;
    private final PreferencesManager prefs;

    public AuthViewModelFactory(UserRepository userRepo, PreferencesManager prefs) {
        this.userRepo = userRepo;
        this.prefs    = prefs;
    }

    @NonNull @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AuthViewModel(userRepo, prefs);
    }
}