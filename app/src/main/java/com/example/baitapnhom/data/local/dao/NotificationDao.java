package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.Notification;
import java.util.List;

@Dao
public interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Notification notification);

    @Update
    void update(Notification notification);

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    LiveData<List<Notification>> getNotificationsByUser(int userId);

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    LiveData<Integer> getUnreadCount(int userId);

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    void markAsRead(int id);

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    void markAllAsRead(int userId);

    @Query("DELETE FROM notifications WHERE userId = :userId")
    void clearAll(int userId);

    @Delete
    void delete(Notification notification);
}