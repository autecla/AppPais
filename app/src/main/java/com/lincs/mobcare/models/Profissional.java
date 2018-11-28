package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Profissional {
    public String user_uid;
    public String id;
    public String nome;
    public String especialidade;
    public String email;

    public Profissional() {
    }

    /*public Profissional(String user_uid, String id, String nome, String especialidade, String email) {
        this.user_uid = user_uid;
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.email = email;
    }*/
}