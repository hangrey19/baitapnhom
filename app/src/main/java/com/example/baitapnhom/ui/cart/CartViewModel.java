package com.example.baitapnhom.ui.cart;

import androidx.lifecycle.*;
import com.example.baitapnhom.data.local.entity.CartItem;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.CartRepository;
import com.example.baitapnhom.data.repository.ProductRepository;
import com.example.baitapnhom.utils.AppExecutors;
import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {

    public static class CartItemWithProduct {
        public final CartItem cartItem;
        public final String   productName;
        public final String   productImage;
        public final double   productPrice;
        public final int      productStock;

        public CartItemWithProduct(CartItem c, String name, String img, double price, int stock) {
            cartItem     = c;
            productName  = name;
            productImage = img;
            productPrice = price;
            productStock = stock;
        }
    }

    private final CartRepository     cartRepo;
    private final ProductRepository  productRepo;
    private final PreferencesManager prefs;

    private final MutableLiveData<List<CartItemWithProduct>> _items =
            new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<CartItemWithProduct>> cartItemsWithProduct = _items;

    public final LiveData<Double> totalPrice = Transformations.map(_items, items -> {
        double sum = 0;
        if (items != null)
            for (CartItemWithProduct i : items)
                if (i.cartItem.isSelected) sum += i.cartItem.quantity * i.productPrice;
        return sum;
    });

    public final LiveData<Integer> selectedCount = Transformations.map(_items, items -> {
        int count = 0;
        if (items != null)
            for (CartItemWithProduct i : items)
                if (i.cartItem.isSelected) count++;
        return count;
    });

    public final LiveData<Boolean> isAllSelected = Transformations.map(_items, items -> {
        if (items == null || items.isEmpty()) return false;
        for (CartItemWithProduct i : items)
            if (!i.cartItem.isSelected) return false;
        return true;
    });

    public CartViewModel(CartRepository cartRepo, ProductRepository productRepo,
                         PreferencesManager prefs) {
        this.cartRepo    = cartRepo;
        this.productRepo = productRepo;
        this.prefs       = prefs;
        if (prefs.isLoggedIn()) loadCart();
    }

    private void loadCart() {
        cartRepo.getCartItems(prefs.getLoggedInUserId()).observeForever(cartItems -> {
            if (cartItems == null) return;
            AppExecutors.getInstance().diskIO().execute(() -> {
                List<CartItemWithProduct> result = new ArrayList<>();
                for (CartItem ci : cartItems) {
                    // getProductByIdDirect() là synchronous — an toàn khi gọi trong diskIO
                    Product p = productRepo.getProductByIdDirect(ci.productId);
                    if (p == null) continue;
                    result.add(new CartItemWithProduct(ci, p.name, p.imageUrl, p.price, p.stock));
                }
                AppExecutors.getInstance().mainThread().execute(() -> _items.setValue(result));
            });
        });
    }

    public void updateQuantity(CartItem cartItem, int newQty) {
        if (newQty <= 0) cartRepo.removeItem(cartItem);
        else             cartRepo.updateQuantity(cartItem.id, newQty);
    }

    public void removeItem(CartItem cartItem)                { cartRepo.removeItem(cartItem); }
    public void toggleSelected(int cartItemId, boolean sel) { cartRepo.updateSelected(cartItemId, sel); }
    public void toggleSelectAll(boolean all)                { cartRepo.updateAllSelected(prefs.getLoggedInUserId(), all); }

    public List<CartItemWithProduct> getSelectedItems() {
        List<CartItemWithProduct> selected = new ArrayList<>();
        List<CartItemWithProduct> all = _items.getValue();
        if (all != null)
            for (CartItemWithProduct i : all)
                if (i.cartItem.isSelected) selected.add(i);
        return selected;
    }
}