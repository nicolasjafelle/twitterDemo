package com.twitter.olx.test.client;

import android.util.Base64;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.twitter.olx.test.client.session.SessionManager;
import com.twitter.olx.test.domain.LittleTweet;
import com.twitter.olx.test.dto.ResponseDto;
import com.twitter.olx.test.dto.TweetDto;

import java.util.Arrays;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedString;

/**
 * Created by nicolas on 8/24/14.
 */
@Singleton
public class TwitterClient {

    private static final String API_URL = "https://api.twitter.com";

    private RestAdapter restAdapter;

    private ITwitterClient iTwitterClient;

    @Inject
    private SessionManager sessionManager;

    @Inject
    public TwitterClient() {
        if(restAdapter == null || iTwitterClient == null) {

            OkHttpClient client = new OkHttpClient();
            client.setProtocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.SPDY_3));

            restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(API_URL)
                    .setClient(new OkClient(client))
                    .build();

            iTwitterClient = restAdapter.create(ITwitterClient.class);
        }
    }


    public ResponseDto authenticate() {
        String authorization = "Basic " + Base64.encodeToString(ITwitterClient.API_STRING.getBytes(), Base64.NO_WRAP);
        String contentType = "application/x-www-form-urlencoded;charset=UTF-8";
        String grantType = "client_credentials";

        return iTwitterClient.authenticate(authorization, contentType, grantType);
    }


    public TweetDto getTweets(String keyword) {
        String accessToken = sessionManager.getToken();
        String contentType = "application/json";
        keyword = "#" + keyword;
        return iTwitterClient.getTweets(accessToken, contentType, keyword);
    }



}

