package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;
import com.example.baitapnhom.data.local.dao.ReviewDao;
import com.example.baitapnhom.data.local.entity.Review;
import com.example.baitapnhom.utils.AppExecutors;
import java.util.List;

public class ReviewRepository {
    private final ReviewDao dao;

    public ReviewRepository(ReviewDao dao) { this.dao = dao; }

    public LiveData<List<Review>> getReviewsByProduct(int productId) { return dao.getReviewsByProduct(productId); }
    public LiveData<List<Review>> getReviewsByUser(int userId)       { return dao.getReviewsByUser(userId); }

    public void addReview(Review review, Runnable onDone) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            dao.insert(review);
            if (onDone != null) AppExecutors.getInstance().mainThread().execute(onDone);
        });
    }

    public void getRatingStats(int productId, UserRepository.Callback<float[]> cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            float avg   = dao.getAverageRating(productId);
            int   count = dao.getReviewCount(productId);
            AppExecutors.getInstance().mainThread().execute(() -> cb.onResult(new float[]{avg, count}));
        });
    }

    public void getUserReview(int userId, int productId, UserRepository.Callback<Review> cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            Review r = dao.getUserReview(userId, productId);
            AppExecutors.getInstance().mainThread().execute(() -> cb.onResult(r));
        });
    }

    public void deleteReview(Review review, Runnable onDone) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            dao.delete(review);
            if (onDone != null) AppExecutors.getInstance().mainThread().execute(onDone);
        });
    }
}