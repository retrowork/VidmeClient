package com.example.vidme;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by volod on 10/28/2017.
 */

public interface VidmeApi {
    @GET("videos/featured")
    Observable<Response> listFeaturedVideos();

    @GET("videos/new")
    Observable<Response> listNewVideos();

    @GET("videos/following")
    Observable<Response> listFollowingVideos();
}
