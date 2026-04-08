package com.example.baitapnhom.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.local.model.SearchFilter;
import com.example.baitapnhom.data.repository.CategoryRepository;
import com.example.baitapnhom.data.repository.ProductRepository;
import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.utils.AppExecutors;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private final ProductRepository  productRepo;
    private final CategoryRepository categoryRepo;

    private final MutableLiveData<SearchFilter> filterLiveData =
            new MutableLiveData<>(SearchFilter.defaultFilter());

    public final LiveData<List<Product>> results;

    public final LiveData<List<Category>> categories;

    public final MutableLiveData<double[]> priceRange = new MutableLiveData<>(new double[]{0, 1_000_000});

    public SearchViewModel(FruitApplication app) {
        this.productRepo  = app.getProductRepository();
        this.categoryRepo = app.getCategoryRepository();
        this.results      = Transformations.switchMap(filterLiveData, productRepo::search);
        this.categories   = categoryRepo.getAll();


        AppExecutors.diskIO().execute(() -> {
            double min = productRepo.getMinPrice();
            double max = productRepo.getMaxPrice();
            AppExecutors.mainThread().execute(() ->
                    priceRange.setValue(new double[]{min, max}));
        });
    }


    public void setKeyword(String keyword) {
        SearchFilter current = filterLiveData.getValue();
        if (current == null) return;
        filterLiveData.setValue(current.withKeyword(keyword));
    }

    public void setCategory(int categoryId) {
        SearchFilter current = filterLiveData.getValue();
        if (current == null) return;
        filterLiveData.setValue(current.withCategory(categoryId));
    }

    public void setPriceRange(double min, double max) {
        SearchFilter current = filterLiveData.getValue();
        if (current == null) return;
        filterLiveData.setValue(current.withPriceRange(min, max));
    }

    public void setSortOrder(SearchFilter.SortOrder order) {
        SearchFilter current = filterLiveData.getValue();
        if (current == null) return;
        filterLiveData.setValue(current.withSortOrder(order));
    }

    public void resetFilter() {
        filterLiveData.setValue(SearchFilter.defaultFilter());
    }

    public SearchFilter getCurrentFilter() {
        return filterLiveData.getValue();
    }
}