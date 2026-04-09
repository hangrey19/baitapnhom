package com.example.baitapnhom.ui.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.Product;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private final FruitApplication app;
    private final MutableLiveData<Integer> categoryIdLiveData = new MutableLiveData<>();
    private final LiveData<List<Product>> products;

    public ProductViewModel(FruitApplication app) {
        this.app = app;
        products = Transformations.switchMap(categoryIdLiveData,
                categoryId -> app.getProductRepository().getProductsByCategory(categoryId));
    }

    public void loadByCategory(int categoryId) {
        categoryIdLiveData.setValue(categoryId);
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}