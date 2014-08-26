package com.twitter.olx.test.client;

import com.twitter.olx.test.dto.ResponseDto;
import com.twitter.olx.test.dto.TweetDto;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by nicolas on 5/29/14.
 */
public interface ITwitterClient {

    String API_KEY = "pMkjX93mPLsGtbrxDoutYQ";

    String API_SECRET = "GphYuLZWC6XwGrgzkoYIdVZw82y4We46UZ4ns7Q2G4";

    String API_STRING = API_KEY + ":" + API_SECRET;

    @FormUrlEncoded
    @POST("/oauth2/token")
    ResponseDto authenticate(@Header("Authorization") String auth, @Header("Content-Type") String contentType,
                          @Field("grant_type") String type);

    @GET("/1.1/search/tweets.json")
    TweetDto getTweets(@Header("Authorization") String bearerToken, @Header("Content-type") String contentType,
                       @Query("q") String keyword);


//	@GET("/msg")
//	Detail redirectUrl(@Query("eid") String eid);
//	@GET("/flv/nb/nb028/nbnb028100554/ios_config.xml")
//	Detail getVideoData();
//
//    @GET("/{first_path}/{second_path}/{third_path}/{fourth_path}/{fifth_path}")
//    Detail getVideoData(@Path("first_path") String firstPath, @Path("second_path") String secondPath, @Path("third_path") String thirdPath,
//                        @Path("fourth_path") String fourthPath, @Path("fifth_path") String fifthPath);
//
//    @GET("/flv/{first_path}/{second_path}/{third_path}/ios_config.xml")
//    Detail getVideoData(@Path("first_path") String firstPath, @Path("second_path") String secondPath, @Path("third_path") String thirdPath);
//
//
//
//	@POST("/ipad_rest/pushMessagesSince.php")
//	@FormUrlEncoded
//	Response getPushMessageSince(@Field("token") String token, @Field("submit") String submit, @Field("fromdate") String fromDate);


}