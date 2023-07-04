package com.mastercodingapp.rest;

import com.mastercodingapp.model.HomepageModel;
import com.mastercodingapp.model.OurYtModel;
import com.mastercodingapp.model.YtModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    // Retrofit Interface
    // JSON --> GSON Library --> Java Object

    @GET("homepage_api")
    Call<HomepageModel> getHomepageApi(@QueryMap Map<String, String> params);


   // Getting news by id
    // Displaying news details using news id
    @GET("news_by_pid")
    Call<HomepageModel> getNewsDetailsById(@QueryMap Map<String, String> params);


    // Getting Youtube details
    @GET("youtube")
    Call<OurYtModel> getYoutubeDetailsFromServer();


    // Connecting to cloud console
    // Check the documentations on  youtube api 3  for the link or download our app from playstore: "Master Coding"
    @GET("https://www.googleapis.com/youtube/v3/activities")
    Call<YtModel> getYoutubeServerData(@QueryMap Map<String,String> params);





}
