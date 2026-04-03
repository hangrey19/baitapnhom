package com.example.baitapnhom.ui.product;

import androidx.lifecycle.*;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.local.entity.Review;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.*;
import java.util.List;

public class ProductViewModel extends ViewModel {

    private final ProductRepository  productRepo;
    private final CartRepository     cartRepo;
    private final WishlistRepository wishlistRepo;
    private final ReviewRepository   reviewRepo;
    private final PreferencesManager prefs;

    // Filter state (simple MutableLiveData for Java)
    public final MutableLiveData<String> searchQuery    = new MutableLiveData<>("");
    public final MutableLiveData<Integer> filterCategory= new MutableLiveData<>(0);
    public final MutableLiveData<Double> filterMinPrice = new MutableLiveData<>(0.0);
    public final MutableLiveData<Double> filterMaxPrice = new MutableLiveData<>(Double.MAX_VALUE);
    public final MutableLiveData<Float>  filterMinRating= new MutableLiveData<>(0f);
    public final MutableLiveData<String> sortBy         = new MutableLiveData<>("newest");

    // Products list reacting to search/filter trigger
    private final MutableLiveData<LiveData<List<Product>>> _productSource = new MutableLiveData<>();
    public final LiveData<List<Product>> filteredProducts =
            Transformations.switchMap(_productSource, source -> source);

    // Single selected product
    private final MutableLiveData<Integer> _selectedProductId = new MutableLiveData<>();
    public final LiveData<Product> selectedProduct;
    public final LiveData<Boolean> isWishlisted;
    public final LiveData<List<Review>> productReviews;

    public ProductViewModel(ProductRepository productRepo, CartRepository cartRepo,
                            WishlistRepository wishlistRepo, ReviewRepository reviewRepo,
                            PreferencesManager prefs) {
        this.productRepo  = productRepo;
        this.cartRepo     = cartRepo;
        this.wishlistRepo = wishlistRepo;
        this.reviewRepo   = reviewRepo;
        this.prefs        = prefs;

        // Initialize LiveData that depend on repositories
        this.selectedProduct = Transformations.switchMap(_selectedProductId, id -> 
                this.productRepo.getProductById(id));

        this.isWishlisted = Transformations.switchMap(_selectedProductId, id -> {
            if (this.prefs.isLoggedIn()) {
                return this.wishlistRepo.isWishlisted(this.prefs.getLoggedInUserId(), id);
            }
            return new MutableLiveData<>(false);
        });

        this.productReviews = Transformations.switchMap(_selectedProductId, id -> 
                this.reviewRepo.getReviewsByProduct(id));

        // Default: show all
        applyFilter();
    }

    public void applyFilter() {
        String  q    = searchQuery.getValue();
        int     cat  = filterCategory.getValue() != null ? filterCategory.getValue() : 0;
        double  minP = filterMinPrice.getValue() != null ? filterMinPrice.getValue() : 0.0;
        double  maxP = filterMaxPrice.getValue() != null ? filterMaxPrice.getValue() : Double.MAX_VALUE;
        float   minR = filterMinRating.getValue() != null ? filterMinRating.getValue() : 0f;

        LiveData<List<Product>> src;
        if (q != null && !q.trim().isEmpty()) {
            src = productRepo.searchProducts(q.trim());
        } else {
            src = productRepo.getFilteredProducts(cat, minP, maxP, minR);
        }
        _productSource.setValue(src);
    }

    public void selectProduct(int productId) { _selectedProductId.setValue(productId); }

    public void toggleWishlist(int productId) {
        if (!prefs.isLoggedIn()) return;
        wishlistRepo.toggleWishlist(prefs.getLoggedInUserId(), productId);
    }

    public void addToCart(int productId, int quantity) {
        if (!prefs.isLoggedIn()) return;
        cartRepo.addToCart(prefs.getLoggedInUserId(), productId, quantity);
    }

    public void resetFilters() {
        searchQuery.setValue("");
        filterCategory.setValue(0);
        filterMinPrice.setValue(0.0);
        filterMaxPrice.setValue(Double.MAX_VALUE);
        filterMinRating.setValue(0f);
        sortBy.setValue("newest");
        applyFilter();
    }
}
