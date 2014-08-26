package com.twitter.olx.test.client.session;

import android.content.SharedPreferences;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitter.olx.test.dto.TokenType;

@Singleton
public class SessionManager {

    @Inject
    private SharedPreferences sharedPref;

    private static final String TOKEN = "access_token";
    private static final String ACCESS_TYPE = "token_type";
    private static final String IS_FIRST_TIME = "is_first_time";

    @Inject
    public SessionManager() {}


    public void setSession(String token, TokenType tokenType){
        this.saveToken(token);
        this.saveTokenType(tokenType);
    }

    public void saveToken(String token){
        String bearerToken = "Bearer " + token;
        this.saveValue(TOKEN, bearerToken);
    }


    public void saveTokenType(TokenType tokenType){
        this.saveValue(ACCESS_TYPE, tokenType.ordinal());
    }

    public void setFirstTime() {
        this.saveValue(IS_FIRST_TIME, false);
    }

    public String getToken(){
        String buffer = this.sharedPref.getString(TOKEN, "");
        if (buffer.trim().isEmpty()){
            return null;
        }else{
            return buffer;
        }
    }


    public boolean isConnected() {
        if (this.getToken() != null) {
            return true;
        } else {
            return false;
        }
    }

    public TokenType getTokenType(){
        int ordinal = this.sharedPref.getInt(ACCESS_TYPE,-1);
        if (ordinal < 0 || ordinal > TokenType.values().length-1){
            return null;
        }else{
            return TokenType.values()[ordinal];
        }
    }

    public boolean isFirstTime(){
        return this.sharedPref.getBoolean(IS_FIRST_TIME, true);
    }

    public void clear() {
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.clear();
        editor.commit();
    }


    // Helpers
    protected void saveValue(String key, String value){
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putString(key,value);
        editor.commit();
    }

    protected void saveValue(String key, long value){
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putLong(key,value);
        editor.commit();
    }

    protected void saveValue(String key, int value){
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    protected void saveValue(String key, boolean value){
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
