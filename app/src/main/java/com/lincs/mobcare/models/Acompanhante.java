package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Acompanhante {
    public String id;
    public String parentesco;
    public String nome;
    public String telefone;
    public String foto;

    public Acompanhante() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Acompanhante(String id, String parentesco, String nome, String telefone, String foto) {
        this.id = id;
        this.parentesco = parentesco;
        this.nome = nome;
        this.telefone = telefone;
        this.foto = foto;
    }
    public void setNome(String nome) {this.nome = nome;}
    public void setParentesco(String parentesco){this.parentesco=parentesco;}
    public void setTelefone(String telefone){this.telefone=telefone;}
}
