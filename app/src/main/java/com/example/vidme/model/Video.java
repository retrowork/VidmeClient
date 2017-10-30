package com.example.vidme.model;


import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("video_id")
    public String videoId;

    @SerializedName("complete_url")
    public String completeUrl;

    @SerializedName("title")
    public String title;

    @SerializedName("date_created")
    public String dateCreated;

    @SerializedName("thumbnail")
    public String thumbnail;

    @SerializedName("thumbnail_url")
    public String thumbnailUrl;

    @SerializedName("score")
    public int score;

}
