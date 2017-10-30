package com.example.vidme.network;

import com.example.vidme.model.AuthResponse;
import com.example.vidme.model.Response;
import com.example.vidme.navigation.MainActivity;

import java.io.IOException;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class VidmeService {
    private VidmeApi vidmeApi;

    public VidmeService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vid.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        vidmeApi = retrofit.create(VidmeApi.class);
    }

    public Observable<Response> getVideos(int listType, int limit, int offset, String token) throws IOException {
        switch (listType) {
            case MainActivity.FEATURED: {
                return vidmeApi.listFeaturedVideos(limit, offset);
            }
            case MainActivity.NEW: {
                return vidmeApi.listNewVideos(limit, offset);
            }
            case MainActivity.FEED: {
                return vidmeApi.listFollowingVideos(limit, offset, token);
            }
        }
        return null;
    }


    public Observable<AuthResponse> createAuthSession(String username, String password) throws IOException {
        return vidmeApi.createAuthSession(username, password);
    }
}
