package com.example.baitapnhom.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.repository.CategoryRepository;
import com.example.baitapnhom.data.repository.ProductRepository;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final ProductRepository  productRepo;
    private final CategoryRepository categoryRepo;

    public final LiveData<List<Product>>  featuredProducts;
    public final LiveData<List<Product>>  flashSaleProducts;
    public final LiveData<List<Category>> allCategories;

    public HomeViewModel(ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.productRepo  = productRepo;
        this.categoryRepo = categoryRepo;

        featuredProducts  = productRepo.getFeaturedProducts();
        flashSaleProducts = productRepo.getFlashSaleProducts();
        allCategories     = categoryRepo.getAllCategories();
    }
}