package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.Product;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    LiveData<Product> getProductById(int id);

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    Product getProductByIdSync(int id);

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY createdAt DESC")
    LiveData<List<Product>> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE isFeatured = 1 LIMIT 10")
    LiveData<List<Product>> getFeaturedProducts();

    @Query("SELECT * FROM products WHERE isFlashSale = 1 LIMIT 10")
    LiveData<List<Product>> getFlashSaleProducts();

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' " +
            "OR description LIKE '%' || :query || '%' " +
            "OR brand LIKE '%' || :query || '%' ORDER BY soldCount DESC")
    LiveData<List<Product>> searchProducts(String query);

    @Query("SELECT * FROM products " +
            "WHERE (:categoryId = 0 OR categoryId = :categoryId) " +
            "AND price BETWEEN :minPrice AND :maxPrice " +
            "AND (:minRating = 0 OR rating >= :minRating) " +
            "ORDER BY createdAt DESC")
    LiveData<List<Product>> getFilteredProducts(int categoryId, double minPrice,
                                                double maxPrice, float minRating);

    @Query("UPDATE products SET rating = :rating, reviewCount = :reviewCount WHERE id = :productId")
    void updateRating(int productId, float rating, int reviewCount);

    @Query("UPDATE products SET stock = stock - :quantity WHERE id = :productId AND stock >= :quantity")
    void decreaseStock(int productId, int quantity);

    @Query("UPDATE products SET soldCount = soldCount + :quantity WHERE id = :productId")
    void increaseSoldCount(int productId, int quantity);
}