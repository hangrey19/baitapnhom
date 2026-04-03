package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.Category;
import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Category category);

    @Query("SELECT * FROM categories ORDER BY name ASC")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM categories ORDER BY name ASC")
    List<Category> getAllCategoriesSync();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Category getCategoryById(int id);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);
}