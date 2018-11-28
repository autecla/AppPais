package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Realiza {
    public String id;
    public String id_anjo;
    public String id_exercicio;
    public long data;
    public String comentario_texto;
    public String comentario_audio;
    public String comentario_video;
    public boolean status;
    public String local_audio;
    public String local_video;

    public Realiza() {
    }

    public Realiza(String id, String id_anjo, String id_exercicio, long data, String comentario_texto,
                   String comentario_audio, String comentario_video, boolean status, String local_audio, String local_video) {
        this.id = id;
        this.id_anjo = id_anjo;
        this.id_exercicio = id_exercicio;
        this.data = data;
        this.comentario_texto = comentario_texto;
        this.comentario_audio = comentario_audio;
        this.comentario_video = comentario_video;
        this.status = status;
        this.local_audio = local_audio;
        this.local_video = local_video;
    }
}