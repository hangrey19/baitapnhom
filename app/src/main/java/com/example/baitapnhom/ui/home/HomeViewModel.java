package com.example.baitapnhom.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.Product;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final LiveData<List<Product>> products;

    public HomeViewModel(FruitApplication app) {
        products = app.getProductRepository().getTodayProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
