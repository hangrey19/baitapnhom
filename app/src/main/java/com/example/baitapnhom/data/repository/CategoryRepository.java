package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;
import com.example.baitapnhom.data.local.dao.CategoryDao;
import com.example.baitapnhom.data.local.entity.Category;
import java.util.List;

public class CategoryRepository {
    private final CategoryDao dao;

    public CategoryRepository(CategoryDao dao) { this.dao = dao; }

    public LiveData<List<Category>> getAllCategories()      { return dao.getAllCategories(); }
    public List<Category>           getAllCategoriesSync()  { return dao.getAllCategoriesSync(); }
    public Category                 getCategoryById(int id) { return dao.getCategoryById(id); }
}