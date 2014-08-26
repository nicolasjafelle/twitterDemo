package com.twitter.olx.test.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nicolas on 8/25/14.
 */
public class User implements Serializable {

    @SerializedName("profile_image_url")
    private String profileUrl;


    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
