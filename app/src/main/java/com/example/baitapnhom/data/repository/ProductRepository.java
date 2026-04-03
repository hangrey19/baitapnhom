package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;
import com.example.baitapnhom.data.local.dao.ProductDao;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.utils.AppExecutors;
import java.util.List;

public class ProductRepository {
    private final ProductDao dao;

    public ProductRepository(ProductDao dao) { this.dao = dao; }

    public LiveData<List<Product>> getAllProducts()                  { return dao.getAllProducts(); }
    public LiveData<Product>       getProductById(int id)           { return dao.getProductById(id); }
    public LiveData<List<Product>> getProductsByCategory(int catId) { return dao.getProductsByCategory(catId); }
    public LiveData<List<Product>> getFeaturedProducts()            { return dao.getFeaturedProducts(); }
    public LiveData<List<Product>> getFlashSaleProducts()           { return dao.getFlashSaleProducts(); }
    public LiveData<List<Product>> searchProducts(String query)     { return dao.searchProducts(query); }

    public LiveData<List<Product>> getFilteredProducts(int categoryId, double minPrice,
                                                       double maxPrice, float minRating) {
        return dao.getFilteredProducts(categoryId, minPrice, maxPrice, minRating);
    }

    /**
     * Gọi TRỰC TIẾP từ background thread (diskIO).
     * Không wrap thêm Executor — synchronous DAO call.
     */
    public Product getProductByIdDirect(int id) {
        return dao.getProductByIdSync(id);
    }

    /**
     * Gọi từ main thread — kết quả trả về qua Callback trên main thread.
     */
    public void getProductByIdAsync(int id, UserRepository.Callback<Product> cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            Product p = dao.getProductByIdSync(id);
            AppExecutors.getInstance().mainThread().execute(() -> cb.onResult(p));
        });
    }

    public void updateRating(int productId, float rating, int count) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.updateRating(productId, rating, count));
    }

    public void decreaseStock(int productId, int qty) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.decreaseStock(productId, qty));
    }

    public void increaseSoldCount(int productId, int qty) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.increaseSoldCount(productId, qty));
    }
}