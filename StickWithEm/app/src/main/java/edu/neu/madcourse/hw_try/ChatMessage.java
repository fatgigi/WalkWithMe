package edu.neu.madcourse.hw_try;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChatMessage {
    private String userName;
    private String message;
    private String imageSrc;

    public ChatMessage(String userName, String message, String src) {
        this.userName = userName;
        this.message = message;
        this.imageSrc = src;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}