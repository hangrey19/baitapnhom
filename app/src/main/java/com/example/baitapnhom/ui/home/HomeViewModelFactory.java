package com.example.baitapnhom.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.baitapnhom.data.repository.CategoryRepository;
import com.example.baitapnhom.data.repository.ProductRepository;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private final ProductRepository  productRepo;
    private final CategoryRepository categoryRepo;

    public HomeViewModelFactory(ProductRepository p, CategoryRepository c) {
        this.productRepo  = p;
        this.categoryRepo = c;
    }

    @NonNull @Override
    public <T extends ViewModel> T create(@NonNull Class<T> cls) {
        return (T) new HomeViewModel(productRepo, categoryRepo);
    }
}