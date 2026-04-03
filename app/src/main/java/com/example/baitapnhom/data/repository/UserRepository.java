package com.example.baitapnhom.data.repository;

import com.example.baitapnhom.data.local.dao.UserDao;
import com.example.baitapnhom.data.local.entity.User;
import com.example.baitapnhom.utils.AppExecutors;

public class UserRepository {
    private final UserDao userDao;

    public interface UserCallback {
        void onResult(User user, String error);
    }

    public interface SimpleCallback {
        void onComplete(boolean success, String message);
    }

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public void login(String username, String password, UserCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            User user = userDao.login(username.trim(), password.trim());
            AppExecutors.mainThread().execute(() -> {
                if (user != null) {
                    callback.onResult(user, null);
                } else {
                    callback.onResult(null, "Sai tài khoản hoặc mật khẩu");
                }
            });
        });
    }

    public void register(String fullName, String username, String password, String phone, SimpleCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            User existing = userDao.findByUsername(username.trim());
            if (existing != null) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Tên đăng nhập đã tồn tại"));
                return;
            }
            userDao.insert(new User(fullName.trim(), username.trim(), password.trim(), phone.trim()));
            AppExecutors.mainThread().execute(() -> callback.onComplete(true, "Đăng ký thành công"));
        });
    }

    public void getById(int id, UserCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            User user = userDao.findById(id);
            AppExecutors.mainThread().execute(() -> callback.onResult(user, user == null ? "Không tìm thấy người dùng" : null));
        });
    }
}
