package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baitapnhom.data.local.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {


    @Query("SELECT * FROM products WHERE expiryDate > date('now', 'localtime') ORDER BY id DESC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM products WHERE todaySale = 1 AND expiryDate > date('now', 'localtime') ORDER BY id DESC")
    LiveData<List<Product>> getTodayProducts();

    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND expiryDate > date('now', 'localtime') ORDER BY id DESC")
    LiveData<List<Product>> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE id = :productId AND expiryDate > date('now', 'localtime') LIMIT 1")
    LiveData<Product> getProductById(int productId);

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    Product getProductByIdSync(int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Update
    void update(Product product);

    @Query("SELECT * FROM products " +
            "WHERE expiryDate > date('now', 'localtime') " +
            "AND name LIKE '%' || :keyword || '%' " +
            "ORDER BY name ASC")
    LiveData<List<Product>> searchByName(String keyword);


    @Query("SELECT * FROM products " +
            "WHERE expiryDate > date('now', 'localtime') " +
            "AND name LIKE '%' || :keyword || '%' " +
            "AND (:categoryId = 0 OR categoryId = :categoryId) " +
            "AND (:minPrice < 0 OR price >= :minPrice) " +
            "AND (:maxPrice < 0 OR price <= :maxPrice) " +
            "ORDER BY price ASC")
    LiveData<List<Product>> searchFiltered_PriceAsc(
            String keyword, int categoryId, double minPrice, double maxPrice);

    @Query("SELECT * FROM products " +
            "WHERE expiryDate > date('now', 'localtime') " +
            "AND name LIKE '%' || :keyword || '%' " +
            "AND (:categoryId = 0 OR categoryId = :categoryId) " +
            "AND (:minPrice < 0 OR price >= :minPrice) " +
            "AND (:maxPrice < 0 OR price <= :maxPrice) " +
            "ORDER BY price DESC")
    LiveData<List<Product>> searchFiltered_PriceDesc(
            String keyword, int categoryId, double minPrice, double maxPrice);

    @Query("SELECT * FROM products " +
            "WHERE expiryDate > date('now', 'localtime') " +
            "AND name LIKE '%' || :keyword || '%' " +
            "AND (:categoryId = 0 OR categoryId = :categoryId) " +
            "AND (:minPrice < 0 OR price >= :minPrice) " +
            "AND (:maxPrice < 0 OR price <= :maxPrice) " +
            "ORDER BY name ASC")
    LiveData<List<Product>> searchFiltered_NameAsc(
            String keyword, int categoryId, double minPrice, double maxPrice);

    @Query("SELECT * FROM products " +
            "WHERE expiryDate > date('now', 'localtime') " +
            "AND name LIKE '%' || :keyword || '%' " +
            "AND (:categoryId = 0 OR categoryId = :categoryId) " +
            "AND (:minPrice < 0 OR price >= :minPrice) " +
            "AND (:maxPrice < 0 OR price <= :maxPrice) " +
            "ORDER BY id DESC")
    LiveData<List<Product>> searchFiltered_Newest(
            String keyword, int categoryId, double minPrice, double maxPrice);


    @Query("SELECT MIN(price) FROM products WHERE expiryDate > date('now', 'localtime')")
    double getMinPrice();

    @Query("SELECT MAX(price) FROM products WHERE expiryDate > date('now', 'localtime')")
    double getMaxPrice();
}