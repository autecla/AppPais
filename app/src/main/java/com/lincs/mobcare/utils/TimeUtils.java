package com.lincs.mobcare.utils;

public class TimeUtils {

    public static String convertHour(int hrs, int mnts) {

        String hora = String.valueOf(hrs);

        if (hrs < 10) {
            hora = "0" + hrs;
        }

        if (mnts < 10) {
            hora = hora + ":0" + mnts;
        } else {
            hora = hora + ":" + mnts;
        }

        return hora;
    }
    public static String convertDate(int dia, int mes, int ano, int hrs, int mnts) {

        String hora = String.valueOf(dia);

        if (dia < 10) {
            hora = "0" + dia;
        }

        if ((mes+1) < 10) {
            hora = hora + "/0" + (mes+1);
        } else {
            hora = hora + "/" + (mes+1);
        }

        String anos = String.valueOf(ano);
        hora = hora + String.valueOf("/" + anos + " ");

        if (hrs < 10) {
            hora = hora + "0" + hrs;
        } else {
            hora = hora + hrs;
        }

        if (mnts < 10) {
            hora = hora + ":0" + mnts;
        } else {
            hora = hora + ":" + mnts;
        }

        return hora;
    }
}
