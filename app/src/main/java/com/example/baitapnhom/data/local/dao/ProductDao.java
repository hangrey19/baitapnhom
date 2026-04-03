package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.baitapnhom.data.local.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products ORDER BY id DESC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM products WHERE todaySale = 1 ORDER BY id DESC")
    LiveData<List<Product>> getTodayProducts();

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY id DESC")
    LiveData<List<Product>> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    LiveData<Product> getProductById(int productId);

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    Product getProductByIdSync(int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);
}