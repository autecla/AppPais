package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Aviso {
    public String id;
    public String tipo;
    public String mensagem;

    public Aviso() {
    }

    public Aviso(String id, String tipo, String mensagem) {
        this.id = id;
        this.tipo = tipo;
        this.mensagem = mensagem;
    }
}