package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;
import com.example.baitapnhom.data.local.dao.NotificationDao;
import com.example.baitapnhom.data.local.entity.Notification;
import com.example.baitapnhom.utils.AppExecutors;
import java.util.List;

public class NotificationRepository {
    private final NotificationDao dao;

    public NotificationRepository(NotificationDao dao) { this.dao = dao; }

    public LiveData<List<Notification>> getNotifications(int userId) { return dao.getNotificationsByUser(userId); }
    public LiveData<Integer>            getUnreadCount(int userId)    { return dao.getUnreadCount(userId); }

    public void addNotification(Notification n) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.insert(n));
    }

    public void markAsRead(int id) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.markAsRead(id));
    }

    public void markAllAsRead(int userId) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.markAllAsRead(userId));
    }

    public void clearAll(int userId) {
        AppExecutors.getInstance().diskIO().execute(() -> dao.clearAll(userId));
    }
}