package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class Anjo {
    public String user_uid;
    public String id;
    public String id_tem;
    public Map<String,Boolean> id_apresenta;
    public Map<String,Boolean> id_realiza;
    public Map<String,Boolean> id_consulta;
    public Map<String,Boolean> id_registra;
    public String nome;
    public String data_de_nascimento;
    public String sexo;
    public String numero_prontuario;
    public DadosGeraisAnjo dadosGeraisAnjo;
    public DadosOftalmologicos dadosOftalmologicos;
    public String token;
    public String foto;

    public Anjo() {}

    public Anjo(String user_uid, String id, String id_tem, Map<String,Boolean> id_apresenta, Map<String,Boolean> id_realiza, Map<String,Boolean> id_consulta,
                String nome, String data_de_nascimento, String sexo, String numero_prontuario, DadosGeraisAnjo dadosGeraisAnjo, DadosOftalmologicos dadosOftalmologicos,
                String foto) {
        this.user_uid = user_uid;
        this.id = id;
        this.id_tem = id_tem;
        this.id_apresenta = id_apresenta;
        this.id_realiza = id_realiza;
        this.id_consulta = id_consulta;
        this.nome = nome;
        this.numero_prontuario=numero_prontuario;
        this.data_de_nascimento=data_de_nascimento;
        this.sexo=sexo;
        this.dadosGeraisAnjo=dadosGeraisAnjo;
        this.dadosOftalmologicos=dadosOftalmologicos;
        this.token = null;
        this.foto=foto;
    }
}
