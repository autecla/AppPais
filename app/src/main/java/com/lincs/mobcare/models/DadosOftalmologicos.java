package com.lincs.mobcare.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DadosOftalmologicos {
    public String OD;
    public String OE;
    public String AO;
    public boolean estrabismo;
    public boolean nistagmo;
    public boolean oclusao;
    public String tipo_oclusao;
    public boolean prescricao_de_oculos;
    public String funcionalidade_visual;

    public DadosOftalmologicos(){}
    public DadosOftalmologicos(String OD,String OE,String AO,boolean estrabismo,boolean nistagmo,boolean oclusao,String tipo_oclusao,
                               boolean prescricao_de_oculos,String funcionalidade_visual){
        this.OD=OD;
        this.OE=OE;
        this.AO=AO;
        this.estrabismo=estrabismo;
        this.nistagmo=nistagmo;
        this.oclusao=oclusao;
        this.tipo_oclusao=tipo_oclusao;
        this.prescricao_de_oculos=prescricao_de_oculos;
        this.funcionalidade_visual=funcionalidade_visual;
    }
}
