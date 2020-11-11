package edu.neu.madcourse.hw_try;

public class ItemCard {
    private String userName;
    private String userToken;

    public ItemCard(String userName, String userToken) {
        this.userName = userName;
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserToken() {
        return userToken;
    }
}
