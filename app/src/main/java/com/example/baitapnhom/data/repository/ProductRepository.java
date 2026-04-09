package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;

import com.example.baitapnhom.data.local.dao.ProductDao;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.local.model.SearchFilter;

import java.util.List;

public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }


    public LiveData<List<Product>> getAllProducts() {
        return productDao.getAllProducts();
    }

    public LiveData<List<Product>> getTodayProducts() {
        return productDao.getTodayProducts();
    }

    public LiveData<List<Product>> getProductsByCategory(int categoryId) {
        return productDao.getProductsByCategory(categoryId);
    }

    public LiveData<Product> getProductById(int productId) {
        return productDao.getProductById(productId);
    }

    public Product getProductByIdSync(int productId) {
        return productDao.getProductByIdSync(productId);
    }

    public void update(Product product) {
        productDao.update(product);
    }


    public LiveData<List<Product>> search(SearchFilter filter) {
        String kw         = filter.keyword;
        int    catId      = filter.categoryId;
        double minPrice   = filter.minPrice;
        double maxPrice   = filter.maxPrice;

        switch (filter.sortOrder) {
            case PRICE_ASC:
                return productDao.searchFiltered_PriceAsc(kw, catId, minPrice, maxPrice);
            case PRICE_DESC:
                return productDao.searchFiltered_PriceDesc(kw, catId, minPrice, maxPrice);
            case NAME_ASC:
                return productDao.searchFiltered_NameAsc(kw, catId, minPrice, maxPrice);
            case NEWEST:
            default:
                return productDao.searchFiltered_Newest(kw, catId, minPrice, maxPrice);
        }
    }

    public double getMinPrice() { return productDao.getMinPrice(); }
    public double getMaxPrice() { return productDao.getMaxPrice(); }
}