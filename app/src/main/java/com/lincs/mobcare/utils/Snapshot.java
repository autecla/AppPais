package com.lincs.mobcare.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lincs.mobcare.evolution.EvolutionAdapter;
import com.lincs.mobcare.scheduler.SchedulerAdapter;
import com.lincs.mobcare.models.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Snapshot {
    private static String anjo_id;
    private static Anjo anjo;
    private static Profissional profissional;
    private static Tem tem;
    private static Acompanhante acompanhante;
    private static Map<String, Apresenta> apresenta = new HashMap<>();
    private static Map<String, Sintoma> sintoma = new HashMap<>();
    private static Map<String, Realiza> realiza = new HashMap<>();
    private static Map<String, Exercicio> exercicio = new HashMap<>();
    private static Map<String, Consulta> consulta = new HashMap<>();
    private static Map<String, Aviso> aviso = new HashMap<>();
    private static Map<String, Profissional> profissionais = new HashMap<>();
    private static Map<String, Possui> possui = new HashMap<>();
    private static Map<String,   Video> video = new HashMap<>();
    private static Map<String, String> download_video = new HashMap<>();
    private static ArrayList<String> id_consulta = new ArrayList<>();
    private static ArrayList<String> id_profissional = new ArrayList<>();
    private static ArrayList<String> id_video = new ArrayList<>();
    private static String id_realiza_activity;
    private static SchedulerAdapter consultaAdapter;
    private static EvolutionAdapter registraAdapter;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static String fileName = "snapshot";
    private static Map<String, Registra> registra = new HashMap<>();
    private static boolean adapterChanged;
    private static final Snapshot snapshot = new Snapshot();

    private Snapshot() {}

    public static Snapshot getInstance() {
        return snapshot;
    }

    public void setAnjo(Anjo anjo) {
        Snapshot.anjo = anjo;
    }

    public void setAcompanhante(Acompanhante acompanhante) {
        Snapshot.acompanhante = acompanhante;
    }

    public void setTem(Tem tem) {
        Snapshot.tem = tem;
    }

    public static Anjo getAnjo() {
        return anjo;
    }

    public static Acompanhante getAcompanhante() {
        return acompanhante;
    }

    public static Tem getTem() {
        return tem;
    }


    public static String getIdade2(String data_nasc) throws ParseException {
        String formatDate="dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ROOT);
        Date date=sdf.parse(data_nasc);

        String age="";
        int years;
        int months;

        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(date.getTime());
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;
        months = currMonth - birthMonth;

        if (months < 0)        {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
            if (years==0){
                age=months+" meses";
            }
            else{
                age=years+" anos e " + months+" meses";
            }

        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
            //years--;
            months = 11;
            age= months+" meses";
        }
        return age;
    }

    public static Map<String, Exercicio> getExercicio() {
        return exercicio;
    }

    public static Map<String, Realiza> getRealiza() {
        return realiza;
    }

    public static ArrayList<Apresenta> getApresentaMes(int month) {
        ArrayList<Apresenta> array = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Map.Entry<String, Apresenta> entry : apresenta.entrySet()) {
            Apresenta apresenta = entry.getValue();

            calendar.setTime(new Date(apresenta.data));
            if (calendar.get(Calendar.MONTH) == month) {
                array.add(apresenta);
            }
        }
        return array;
    }

    public static ArrayList<Realiza> getRealizaMes(int month) {
        ArrayList<Realiza> array = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int tmpYear = month <= calendar.get(Calendar.MONTH) ? calendar.get(Calendar.YEAR) : calendar.get(Calendar.YEAR) - 1;

        calendar.set(tmpYear, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(tmpYear, month, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        for (Map.Entry<String, Realiza> entry : realiza.entrySet()) {
            Realiza realiza = entry.getValue();
            calendar.setTime(new Date(realiza.data));

            if(calendar.get(Calendar.MONTH) == month && realiza.status) {
                array.add(realiza);
            }
        }

        Collections.sort(array, new Comparator<Realiza>() {
            @Override
            public int compare(Realiza realiza, Realiza t1) {
                return Long.compare(realiza.data, t1.data);
            }
        });

        return array;
    }

    public static Apresenta getApresenta(String id) {
        return apresenta.get(id);
    }

    public static Map<String,Apresenta> getApresenta() {
        return apresenta;
    }

    public static Map<String, Aviso> getAviso() {
        return aviso;
    }

    public static Map<String, Consulta> getConsulta() {
        consulta = sortByValue(consulta);
        return consulta;
    }

    public static Map<String, Possui> getPossui() {
        return possui;
    }

    public static Map<String, Video> getVideo() {
        return video;
    }
    public static void setVideo( Map<String, Video> videos) {
        video=videos;
    }
    public static String getVideoFromId(String id_video) {
        return getDownload_video().get(id_video);
    }

    public static List<Video> getVideoFromExercicio(String id_exercicio) {
        List<Video> videos = new ArrayList<>();
        Exercicio exercicio = getExercicio().get(id_exercicio);
        Possui possui = getPossui().get(exercicio.id_possui);
        for (String id_video : possui.id_video.keySet()) {
            Video video = getVideo().get(id_video);
            videos.add(video);
        }
        return videos;
    }

    public static ArrayList<String> getId_profissional() {
        return id_profissional;
    }

    public static ArrayList<String> getId_video() {
        return id_video;
    }

    private static Map<String, String> getDownload_video() {
        return download_video;
    }

    public static String getId_realiza_activity() {
        return id_realiza_activity;
    }

    public static void setId_realiza_activity(String id_realiza_activity) {
        Snapshot.id_realiza_activity = id_realiza_activity;
    }

    public static void setConsultaAdapter(SchedulerAdapter consultaAdapter) {
        Snapshot.consultaAdapter = consultaAdapter;
        updateConsultaAdapter();
    }

    public static void setRegistraAdapter(EvolutionAdapter registraAdapter) {
        Snapshot.registraAdapter = registraAdapter;
        updateRegistraAdapter();
    }

    public static void updateRegistraAdapter(){
        if(registraAdapter != null) {
            ArrayList<Registra> aux = new ArrayList<>(registra.values());
            registraAdapter.setAppointmentList(aux);
            registraAdapter.notifyDataSetChanged();

        }
    }

    public static void updateConsultaAdapter(){
        if(consultaAdapter != null) {
            consulta = sortByValue(consulta);
            ArrayList<Consulta> aux = new ArrayList<>(consulta.values());
            consultaAdapter.setAppointmentList(aux);
            consultaAdapter.notifyDataSetChanged();
        }
    }

    public static void setAdapterChanged(boolean changed){
        adapterChanged=changed;
    }

    public static void logout(Context context){
        if(sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        editor.clear();
        editor.apply();
    }

    public static String getAnjo_id() {
        return anjo_id;
    }

    public static void setAnjo_id(String anjo_id) {
        Snapshot.anjo_id = anjo_id;
    }

    public static void reset(){
        anjo_id=null;
        anjo=null;
        tem=null;
        acompanhante=null;
        apresenta=new HashMap<>();
        sintoma=new HashMap<>();
        realiza=new HashMap<>();
        exercicio=new HashMap<>();
        consulta=new HashMap<>();
        aviso=new HashMap<>();
        profissionais=new HashMap<>();
        possui=new HashMap<>();
        video=new HashMap<>();
        download_video=new HashMap<>();
        id_consulta=new ArrayList<>();
        id_profissional=new ArrayList<>();
        id_video=new ArrayList<>();
        id_realiza_activity=null;
        consultaAdapter=null;
        registraAdapter=null;
        sharedPreferences=null;
        editor=null;
        fileName=null;
        registra = new HashMap<>();
    }

    public static void setProfissional(Profissional profissional) {
        Snapshot.profissional = profissional;
    }

    public static Map<String, Profissional> getProfissionais() {
        return profissionais;
    }

    public static Map<String, Registra> getRegistra() {
        return registra;
    }

    private static Map<String, Consulta> sortByValue(Map<String, Consulta> unsortMap) {
        // 1. Convert Map to List of Map
        List<Map.Entry<String, Consulta>> list =
                new LinkedList<>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Consulta>>() {
            public int compare(Map.Entry<String, Consulta> o1,
                               Map.Entry<String, Consulta> o2) {
                return (o2.getValue().data).compareTo(o1.getValue().data);
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Consulta> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Consulta> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
