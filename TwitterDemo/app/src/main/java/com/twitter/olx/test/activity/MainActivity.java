package com.twitter.olx.test.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.twitter.olx.test.R;
import com.twitter.olx.test.fragment.MainFragment;


public class MainActivity extends AbstractFragmentActivity implements MainFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setInitialFragment() {
        setInitialFragment(R.layout.activity_main, R.id.activity_main_root, MainFragment.newInstance());
    }


    @Override
    public void onOpenTwitter() {
        String url = "https://www.twitter.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
