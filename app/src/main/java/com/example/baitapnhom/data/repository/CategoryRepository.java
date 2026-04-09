package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;

import com.example.baitapnhom.data.local.dao.CategoryDao;
import com.example.baitapnhom.data.local.entity.Category;

import java.util.List;

public class CategoryRepository {
    private final CategoryDao categoryDao;

    public CategoryRepository(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public LiveData<List<Category>> getAll() {
        return categoryDao.getAll();
    }

    public List<Category> getAllSync() {
        return categoryDao.getAllSync();
    }
}