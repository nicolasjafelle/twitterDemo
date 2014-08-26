package com.twitter.olx.test.dto;

import com.twitter.olx.test.domain.LittleTweet;

import java.util.List;

/**
 * Created by nicolas on 8/25/14.
 */
public class TweetDto {

    private List<LittleTweet> statuses;


    public List<LittleTweet> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<LittleTweet> statuses) {
        this.statuses = statuses;
    }
}
