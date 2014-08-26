package com.twitter.olx.test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.olx.test.R;
import com.twitter.olx.test.utils.ViewUtil;

import roboguice.inject.InjectView;

/**
 * Created by nicolas on 8/25/14.
 */
public class ItemRowView extends LinearLayout {

    @InjectView(R.id.item_row_image)
    private CircularImageView image;

    @InjectView(R.id.item_row_text)
    private TextView text;

    public ItemRowView(Context context) {
        super(context);
        init();
    }

    public ItemRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.item_row, this);
        ViewUtil.reallyInjectViews(this);
    }

    public void loadData(String url, String tweet) {
        Picasso.with(getContext())
                .load(url)
                .error(R.drawable.image_placeholder)
                .placeholder(R.drawable.image_placeholder)
                .into(image);

        text.setText(tweet);
    }

    public void loadData(String tweet) {
        Picasso.with(getContext())
                .load(R.drawable.image_placeholder)
                .placeholder(R.drawable.image_placeholder)
                .into(image);

        text.setText(tweet);
    }
}
