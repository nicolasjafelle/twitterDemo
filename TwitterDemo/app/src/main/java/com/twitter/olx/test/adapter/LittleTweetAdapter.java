package com.twitter.olx.test.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twitter.olx.test.domain.LittleTweet;
import com.twitter.olx.test.view.ItemRowView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 8/25/14.
 */
public class LittleTweetAdapter extends BaseAdapter {

    private List<LittleTweet> littleTweets;

    public LittleTweetAdapter(List<LittleTweet> littleTweets) {
        this.littleTweets = littleTweets;
    }

    public ArrayList<LittleTweet> getTweets() {
        return (ArrayList<LittleTweet>)this.littleTweets;
    }

    @Override
    public int getCount() {
        return littleTweets.size();
    }

    @Override
    public LittleTweet getItem(int i) {
        return littleTweets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return littleTweets.get(i).hashCode();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LittleTweet littleTweet = getItem(i);
        ItemRowView itemRowView;

        if(convertView != null) {
            itemRowView = (ItemRowView) convertView;
        }else {
            itemRowView = new ItemRowView(viewGroup.getContext());
        }

        String url = littleTweet.getUser().getProfileUrl();
        itemRowView.loadData(url, littleTweet.getText());
        return itemRowView;
    }
}
