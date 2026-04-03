package com.example.baitapnhom.ui.product;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.*;

public class ProductViewModelFactory implements ViewModelProvider.Factory {
    private final ProductRepository  productRepo;
    private final CartRepository     cartRepo;
    private final WishlistRepository wishlistRepo;
    private final ReviewRepository   reviewRepo;
    private final PreferencesManager prefs;

    public ProductViewModelFactory(ProductRepository p, CartRepository c,
                                   WishlistRepository w, ReviewRepository r,
                                   PreferencesManager prefs) {
        this.productRepo  = p;
        this.cartRepo     = c;
        this.wishlistRepo = w;
        this.reviewRepo   = r;
        this.prefs        = prefs;
    }

    @NonNull @Override
    public <T extends ViewModel> T create(@NonNull Class<T> cls) {
        return (T) new ProductViewModel(productRepo, cartRepo, wishlistRepo, reviewRepo, prefs);
    }
}