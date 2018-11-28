package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Sintoma {
    public String id;
    public String nome;

    public Sintoma() {}

    public Sintoma(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
