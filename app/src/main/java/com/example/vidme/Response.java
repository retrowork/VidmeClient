
package com.example.vidme;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;


    public List<Video> getVideos() {
        return videos;
    }

}
