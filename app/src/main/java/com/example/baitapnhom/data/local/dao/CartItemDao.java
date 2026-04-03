package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.CartItem;
import java.util.List;

@Dao
public interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart_items WHERE userId = :userId ORDER BY addedAt DESC")
    LiveData<List<CartItem>> getCartItems(int userId);

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId LIMIT 1")
    CartItem getCartItem(int userId, int productId);

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    LiveData<Integer> getCartCount(int userId);

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :cartItemId")
    void updateQuantity(int cartItemId, int quantity);

    @Query("UPDATE cart_items SET isSelected = :isSelected WHERE userId = :userId")
    void updateAllSelected(int userId, boolean isSelected);

    @Query("UPDATE cart_items SET isSelected = :isSelected WHERE id = :cartItemId")
    void updateSelected(int cartItemId, boolean isSelected);

    @Query("DELETE FROM cart_items WHERE userId = :userId AND isSelected = 1")
    void deleteSelectedItems(int userId);

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    void clearCart(int userId);

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND isSelected = 1")
    List<CartItem> getSelectedItemsSync(int userId);

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    List<CartItem> getCartItemsSync(int userId);
}