package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.Wishlist;
import java.util.List;

@Dao
public interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Wishlist wishlist);

    @Delete
    void delete(Wishlist wishlist);

    @Query("SELECT * FROM wishlist WHERE userId = :userId ORDER BY addedAt DESC")
    LiveData<List<Wishlist>> getWishlistByUser(int userId);

    @Query("SELECT * FROM wishlist WHERE userId = :userId ORDER BY addedAt DESC")
    List<Wishlist> getWishlistByUserSync(int userId);

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE userId = :userId AND productId = :productId)")
    LiveData<Boolean> isWishlisted(int userId, int productId);

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE userId = :userId AND productId = :productId)")
    boolean isWishlistedSync(int userId, int productId);

    @Query("DELETE FROM wishlist WHERE userId = :userId AND productId = :productId")
    void deleteByProductId(int userId, int productId);

    @Query("SELECT COUNT(*) FROM wishlist WHERE userId = :userId")
    LiveData<Integer> getWishlistCount(int userId);
}