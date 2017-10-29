package com.example.vidme;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by volod on 10/28/2017.
 */

public interface VidmeApi {
    @GET("videos/featured")
    Observable<Response> listFeaturedVideos(@Query("limit") int limit, @Query("offset") int offset);

    @GET("videos/new")
    Observable<Response> listNewVideos(@Query("limit") int limit, @Query("offset") int offset);

    @GET("videos/following")
    Observable<Response> listFollowingVideos(@Query("limit") int limit, @Query("offset") int offset);
}
