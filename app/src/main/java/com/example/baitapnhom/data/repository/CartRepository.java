package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;
import com.example.baitapnhom.data.local.dao.CartItemDao;
import com.example.baitapnhom.data.local.entity.CartItem;
import com.example.baitapnhom.utils.AppExecutors;
import java.util.List;

public class CartRepository {
    private final CartItemDao dao;

    public CartRepository(CartItemDao dao) { this.dao = dao; }

    public LiveData<List<CartItem>> getCartItems(int userId) { return dao.getCartItems(userId); }
    public LiveData<Integer>        getCartCount(int userId) { return dao.getCartCount(userId); }

    public void addToCart(int userId, int productId, int quantity) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            CartItem existing = dao.getCartItem(userId, productId);
            if (existing != null) {
                dao.updateQuantity(existing.id, existing.quantity + quantity);
            } else {
                dao.insert(new CartItem(userId, productId, quantity));
            }
        });
    }

    public void updateQuantity(int cartItemId, int quantity) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.updateQuantity(cartItemId, quantity));
    }

    public void removeItem(CartItem item) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.delete(item));
    }

    public void updateSelected(int cartItemId, boolean isSelected) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.updateSelected(cartItemId, isSelected));
    }

    public void updateAllSelected(int userId, boolean isSelected) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.updateAllSelected(userId, isSelected));
    }

    public void getSelectedItems(int userId, UserRepository.Callback<List<CartItem>> cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<CartItem> items = dao.getSelectedItemsSync(userId);
            AppExecutors.getInstance().mainThread().execute(() -> cb.onResult(items));
        });
    }

    public void deleteSelectedItems(int userId) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.deleteSelectedItems(userId));
    }

    public void clearCart(int userId) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.clearCart(userId));
    }
}