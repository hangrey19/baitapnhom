package com.example.baitapnhom.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.Category;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final LiveData<List<Category>> categories;

    public CategoryViewModel(FruitApplication app) {
        categories = app.getCategoryRepository().getAll();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }
}
