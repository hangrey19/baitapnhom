package com.example.baitapnhom.ui.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.Product;

public class ProductDetailViewModel extends ViewModel {
    private final MutableLiveData<Integer> productIdLiveData = new MutableLiveData<>();
    private final LiveData<Product> product;

    public ProductDetailViewModel(FruitApplication app) {
        product = Transformations.switchMap(productIdLiveData,
                app.getProductRepository()::getProductById);
    }

    public void load(int productId) {
        productIdLiveData.setValue(productId);
    }

    public LiveData<Product> getProduct() {
        return product;
    }
}