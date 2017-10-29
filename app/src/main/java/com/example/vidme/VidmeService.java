package com.example.vidme;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class VidmeService {
    private VidmeApi vidmeApi;

    VidmeService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vid.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        vidmeApi = retrofit.create(VidmeApi.class);
    }

    Observable<Response> getFeaturedVideos(int limit, int offset) throws IOException {
        return vidmeApi.listFeaturedVideos(limit, offset);
    }

    Observable<Response> getNewVideos(int limit, int offset) throws IOException {
        return vidmeApi.listNewVideos(limit, offset);
    }
}
