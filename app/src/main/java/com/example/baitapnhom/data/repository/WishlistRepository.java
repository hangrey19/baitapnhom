package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;
import com.example.baitapnhom.data.local.dao.WishlistDao;
import com.example.baitapnhom.data.local.entity.Wishlist;
import com.example.baitapnhom.utils.AppExecutors;
import java.util.List;

public class WishlistRepository {
    private final WishlistDao dao;

    public WishlistRepository(WishlistDao dao) { this.dao = dao; }

    public LiveData<List<Wishlist>> getWishlistByUser(int userId)          { return dao.getWishlistByUser(userId); }
    public LiveData<Boolean>        isWishlisted(int userId, int productId) { return dao.isWishlisted(userId, productId); }
    public LiveData<Integer>        getWishlistCount(int userId)            { return dao.getWishlistCount(userId); }

    public void toggleWishlist(int userId, int productId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (dao.isWishlistedSync(userId, productId)) {
                dao.deleteByProductId(userId, productId);
            } else {
                dao.insert(new Wishlist(userId, productId));
            }
        });
    }
}