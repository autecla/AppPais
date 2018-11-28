package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Tem {
    public String id;
    public String id_anjo;
    public String id_acompanhante;

    public Tem() {}

    public Tem(String id, String id_anjo, String id_acompanhante) {
        this.id = id;
        this.id_anjo = id_anjo;
        this.id_acompanhante = id_acompanhante;
    }
}
