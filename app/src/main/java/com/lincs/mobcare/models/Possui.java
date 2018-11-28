package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class Possui {
    public String id;
    public String id_exercicio;
    public Map<String,Boolean> id_video;

    public Possui() {}

    public Possui(String id, String id_exercicio, Map<String,Boolean> id_video) {
        this.id = id;
        this.id_exercicio = id_exercicio;
        this.id_video = id_video;
    }
}