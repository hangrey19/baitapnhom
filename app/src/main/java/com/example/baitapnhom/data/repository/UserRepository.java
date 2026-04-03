package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;
import com.example.baitapnhom.data.local.dao.UserDao;
import com.example.baitapnhom.data.local.entity.User;
import com.example.baitapnhom.utils.AppExecutors;

public class UserRepository {
    private final UserDao dao;

    public UserRepository(UserDao dao) { this.dao = dao; }

    public interface Callback<T> { void onResult(T result); }

    public void login(String username, String password, Callback<User> cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            User user = dao.login(username.trim(), password);
            AppExecutors.getInstance().mainThread().execute(() -> cb.onResult(user));
        });
    }

    public void register(User user, Callback<String> cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (dao.isUsernameTaken(user.username) > 0) {
                AppExecutors.getInstance().mainThread().execute(() -> cb.onResult("Tên đăng nhập đã tồn tại"));
                return;
            }
            if (dao.isEmailTaken(user.email) > 0) {
                AppExecutors.getInstance().mainThread().execute(() -> cb.onResult("Email đã được sử dụng"));
                return;
            }
            long id = dao.insert(user);
            user.id = (int) id;
            AppExecutors.getInstance().mainThread().execute(() -> cb.onResult(null)); // null = success
        });
    }

    public LiveData<User> getUserById(int id) { return dao.getUserById(id); }

    public void getUserByIdSync(int id, Callback<User> cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            User u = dao.getUserByIdSync(id);
            AppExecutors.getInstance().mainThread().execute(() -> cb.onResult(u));
        });
    }

    public void updateUser(User user, Runnable onDone) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            dao.update(user);
            AppExecutors.getInstance().mainThread().execute(onDone);
        });
    }
}