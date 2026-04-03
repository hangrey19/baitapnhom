package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.baitapnhom.data.local.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);

    @Query("SELECT * FROM categories ORDER BY id ASC")
    LiveData<List<Category>> getAll();

    @Query("SELECT * FROM categories ORDER BY id ASC")
    List<Category> getAllSync();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Category findById(int id);
}
