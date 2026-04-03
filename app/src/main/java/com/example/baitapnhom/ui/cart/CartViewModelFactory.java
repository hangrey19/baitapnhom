package com.example.baitapnhom.ui.cart;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.CartRepository;
import com.example.baitapnhom.data.repository.ProductRepository;

public class CartViewModelFactory implements ViewModelProvider.Factory {
    private final CartRepository    cartRepo;
    private final ProductRepository productRepo;
    private final PreferencesManager prefs;

    public CartViewModelFactory(CartRepository c, ProductRepository p, PreferencesManager prefs) {
        this.cartRepo    = c;
        this.productRepo = p;
        this.prefs       = prefs;
    }

    @NonNull @Override
    public <T extends ViewModel> T create(@NonNull Class<T> cls) {
        return (T) new CartViewModel(cartRepo, productRepo, prefs);
    }
}