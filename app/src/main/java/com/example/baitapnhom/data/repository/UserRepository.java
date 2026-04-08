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
            String safeUsername = username == null ? "" : username.trim();
            String safePassword = password == null ? "" : password.trim();
            if (safeUsername.isBlank() || safePassword.isBlank()) {
                AppExecutors.mainThread().execute(() -> callback.onResult(null, "Vui long nhap day du thong tin"));
                return;
            }

            User user = userDao.login(safeUsername, safePassword);
            AppExecutors.mainThread().execute(() -> {
                if (user != null) {
                    callback.onResult(user, null);
                } else {
                    callback.onResult(null, "Sai tai khoan hoac mat khau");
                }
            });
        });
    }

    public void register(String fullName, String username, String password, String phone, SimpleCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            String safeFullName = fullName == null ? "" : fullName.trim();
            String safeUsername = username == null ? "" : username.trim();
            String safePassword = password == null ? "" : password.trim();
            String safePhone = phone == null ? "" : phone.trim();

            if (safeFullName.length() < 3) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Ho ten khong hop le"));
                return;
            }
            if (safeUsername.length() < 4 || safeUsername.contains(" ")) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Username khong hop le"));
                return;
            }
            if (!safePhone.matches("\\d{10,11}")) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "So dien thoai khong hop le"));
                return;
            }
            if (safePassword.length() < 6) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Mat khau qua ngan"));
                return;
            }

            User existing = userDao.findByUsername(safeUsername);
            if (existing != null) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Ten dang nhap da ton tai"));
                return;
            }

            userDao.insert(new User(safeFullName, safeUsername, safePassword, safePhone));
            AppExecutors.mainThread().execute(() -> callback.onComplete(true, "Dang ky thanh cong"));
        });
    }

    public void getById(int id, UserCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            User user = userDao.findById(id);
            AppExecutors.mainThread().execute(() -> callback.onResult(user, user == null ? "Khong tim thay nguoi dung" : null));
        });
    }
}
