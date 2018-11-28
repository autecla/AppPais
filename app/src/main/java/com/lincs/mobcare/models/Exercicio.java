package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Exercicio {
    public String id;
    public String id_possui;
    public String titulo;
    public String objetivo;
    public String especialidade;

    public Exercicio() {}

    public Exercicio(String id, String id_possui, String titulo, String objetivo, String especialidade) {
        this.id = id;
        this.id_possui = id_possui;
        this.titulo = titulo;
        this.objetivo = objetivo;
        this.especialidade = especialidade;
    }
}
