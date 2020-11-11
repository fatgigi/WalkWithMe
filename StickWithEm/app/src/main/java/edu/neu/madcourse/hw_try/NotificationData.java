package edu.neu.madcourse.hw_try;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class NotificationData {
    public String message;
    public String uml;
    public String sender;


    public NotificationData() {

    }

    public NotificationData(String message, String uml, String sender) {
        this.message = message;
        this.uml = uml;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUml() {
        return uml;
    }

    public void setUml(String url) {
        this.uml = url;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}