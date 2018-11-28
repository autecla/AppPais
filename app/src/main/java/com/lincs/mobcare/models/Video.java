package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Video {
    public String id;
    public String arquivo;
    public String url_video;

    public Video() {}

    public Video(String id, String arquivo,String url_video) {
        this.id = id;
        this.arquivo = arquivo;
        this.url_video=url_video;
    }
}
