package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Apresenta {
    public String id;
    public String id_anjo;
    public String id_sintoma;
    public Long data;
    public String valor;
    public String comentario_texto;
    public String comentario_audio;
    public String comentario_video;
    public String local_audio;
    public String local_video;

    public Apresenta() {}

    public Apresenta(String id, String id_anjo, String id_sintoma, Long data, String valor, String comentario_texto, String comentario_audio, String comentario_video, String local_audio, String local_video) {
        this.id = id;
        this.id_anjo = id_anjo;
        this.id_sintoma = id_sintoma;
        this.data = data;
        this.valor = valor;
        this.comentario_texto = comentario_texto;
        this.comentario_audio = comentario_audio;
        this.comentario_video = comentario_video;
        this.local_audio = local_audio;
        this.local_video = local_video;
    }
}
