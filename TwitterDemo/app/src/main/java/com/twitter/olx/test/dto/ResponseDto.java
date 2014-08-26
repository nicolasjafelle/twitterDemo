package com.twitter.olx.test.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nicolas on 8/24/14.
 */
public class ResponseDto {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private TokenType tokenType;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
}