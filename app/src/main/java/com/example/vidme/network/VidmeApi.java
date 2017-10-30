package com.example.vidme.network;

import com.example.vidme.model.AuthResponse;
import com.example.vidme.model.Response;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VidmeApi {
    @GET("videos/featured")
    Observable<Response> listFeaturedVideos(@Query("limit") int limit, @Query("offset") int offset);

    @GET("videos/new")
    Observable<Response> listNewVideos(@Query("limit") int limit, @Query("offset") int offset);

    @GET("videos/following")
    Observable<Response> listFollowingVideos(@Query("limit") int limit, @Query("offset") int offset, @Header("AccessToken") String token);

    @FormUrlEncoded
    @POST("auth/create")
    Observable<AuthResponse> createAuthSession(@Field("username") String username, @Field("password") String password);
}
