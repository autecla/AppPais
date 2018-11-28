package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Registra {
    public String id;
    public String id_anjo;
    public String id_exercicio;
    public int periodicidade;

    public Registra() {
    }

    public Registra(String id, String id_anjo, String id_exercicio, int periodicidade) {
        this.id = id;
        this.id_anjo = id_anjo;
        this.id_exercicio = id_exercicio;
        this.periodicidade = periodicidade;
    }
}
