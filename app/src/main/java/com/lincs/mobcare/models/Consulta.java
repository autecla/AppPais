package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Consulta {
    public String id;
    public String id_anjo;
    public String id_profissional;
    public String especialidade;
    public String local;
    public Long data;
    public boolean status;

    public Consulta() {}

    public Consulta(String id, String id_anjo, String id_profissional, String especialidade, String local, Long data, boolean status) {
        this.id = id;
        this.id_anjo = id_anjo;
        this.id_profissional = id_profissional;
        this.especialidade = especialidade;
        this.local = local;
        this.data = data;
        this.status = status;
    }
}
