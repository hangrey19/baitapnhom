package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.Review;
import java.util.List;

@Dao
public interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Review review);

    @Update
    void update(Review review);

    @Delete
    void delete(Review review);

    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY createdAt DESC")
    LiveData<List<Review>> getReviewsByProduct(int productId);

    @Query("SELECT AVG(rating) FROM reviews WHERE productId = :productId")
    float getAverageRating(int productId);

    @Query("SELECT COUNT(*) FROM reviews WHERE productId = :productId")
    int getReviewCount(int productId);

    @Query("SELECT * FROM reviews WHERE userId = :userId AND productId = :productId LIMIT 1")
    Review getUserReview(int userId, int productId);

    @Query("SELECT * FROM reviews WHERE userId = :userId ORDER BY createdAt DESC")
    LiveData<List<Review>> getReviewsByUser(int userId);
}