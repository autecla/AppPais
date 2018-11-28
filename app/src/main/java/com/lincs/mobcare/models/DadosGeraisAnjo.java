package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class DadosGeraisAnjo {
    public String numero_cartao_sus;
    public ArrayList<String> numeros_telefone;
    public String cidade;
    public String data_primeira_consulta_fav;
    public String convulsoes;
    public ArrayList<String> terapias;

    public DadosGeraisAnjo(){}
    public DadosGeraisAnjo(String numero_cartao_sus, ArrayList<String> numeros_telefone, String cidade,
                           String data_primeira_consulta_fav, ArrayList<String> terapias, String convulsoes){

        this.numero_cartao_sus=numero_cartao_sus;
        this.numeros_telefone=numeros_telefone;
        this.cidade=cidade;
        this.data_primeira_consulta_fav=data_primeira_consulta_fav;
        this.convulsoes=convulsoes;
        this.terapias=terapias;
    }
}
