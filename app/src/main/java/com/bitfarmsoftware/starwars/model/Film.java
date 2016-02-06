package com.bitfarmsoftware.starwars.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class Film {

    @SerializedName("url")
    private Uri id;
    private String title;
    @SerializedName("episode_id")
    private String episodeId;
    private String director;
    private String producer;
    @SerializedName("release_date")
    private String releaseDateString;

    public Uri getId() {
        return id;
    }
    public String getTitle() { return title; }
    public String getEpisode() { return episodeId; }
    public String getDirector() { return director; }
    public String getProducer() { return producer; }
    public String getReleaseDateString() { return releaseDateString; }

}
