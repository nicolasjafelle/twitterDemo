package com.twitter.olx.test.domain;

import java.io.Serializable;

/**
 * Created by nicolas on 8/25/14.
 */
public class LittleTweet implements Serializable {

    private String text;
    private User user;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
