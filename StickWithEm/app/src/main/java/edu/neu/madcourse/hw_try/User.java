package edu.neu.madcourse.hw_try;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {
    public String username;
    public int notificationCount;
    public String deviceToken;

    public List<NotificationData> notificationDataList;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String deviceToken) {
        this.username = username;
        this.deviceToken = deviceToken;
        this.notificationDataList = new ArrayList<>();
    }

    public User(String username, int count, String deviceToken) {
        this.username = username;
        this.notificationCount = count;
        this.deviceToken = deviceToken;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", notificationCount=" + notificationCount +
                ", deviceToken='" + deviceToken + '\'' +
                ", notificationDataList=" + notificationDataList +
                '}';
    }
}