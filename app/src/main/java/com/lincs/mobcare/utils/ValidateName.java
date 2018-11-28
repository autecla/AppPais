package com.lincs.mobcare.utils;

public class ValidateName {
    public static boolean validate(String nome){
        boolean retorno = false;
        for (int i=0;i<nome.length();i++){
            if (nome.charAt(i)>=48 &&nome.charAt(i)<=57){
                retorno=true;
            }
        }
        return retorno;
    }
}
