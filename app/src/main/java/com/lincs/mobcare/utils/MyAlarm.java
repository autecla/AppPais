package com.lincs.mobcare.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Apresenta;
import com.lincs.mobcare.models.Realiza;

import java.util.ArrayList;
import java.util.Calendar;

import static com.lincs.mobcare.utils.Firebase.getApresentaHoje;
import static com.lincs.mobcare.utils.Firebase.getRealizaHoje;

public class MyAlarm extends BroadcastReceiver {

    private static String fileName = "alarm";
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    // this constructor is called by the alarm manager.
    public MyAlarm(){ }

    @Override
    public void onReceive(final Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        wl.acquire(10*60*1000L /*10 minutes*/);

        prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String name;
        name = prefs.getString("name", null);
        String date = prefs.getString("date", null);
        boolean atividades = prefs.getBoolean("atividades", true);
        boolean sintomas = prefs.getBoolean("sintomas", true);

        assert name != null;
        switch (name){
            case "alarm":
                if(!atividades){
                    createNotification(context, "Realização de estimulação", "Estimulação diária deve ser executada");
                }

                if(!sintomas){
                    createNotification(context, "Preenchimento do diário", "Novos sintomas não registrados");
                }
                break;

            case "reminder":
                createNotification(context, "Consulta "+date, "Leve documentos e materiais a serem utilizados durante a consulta ou evento no dia "+date);
                break;

            default:
                Toast.makeText(context, "notification without a name", Toast.LENGTH_SHORT).show();
                break;
        }

        wl.release();
    }

    public static void createNotification(Context context, String title, String text){
        prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = prefs.edit();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"channel_id");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(prefs.getString("title", null) == null){
            editor.putString("title", title);
            editor.apply();
            int num = (int) System.currentTimeMillis();
            assert mNotificationManager != null;
            mNotificationManager.notify(num, mBuilder.build());
        }
    }

    public static void createNotificationMessage(Context context, String title, String text){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"channel_id");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int num = (int) System.currentTimeMillis();
        assert mNotificationManager != null;
        mNotificationManager.notify(num, mBuilder.build());
    }

    public static void createAlarm(Context context){
        prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = prefs.edit();

        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, MyAlarm.class);
        editor.putString("name", "alarm");
        editor.apply();

        ArrayList<Realiza> array0 =   getRealizaHoje();
        for (  Realiza realiza: array0) {
            if (!realiza.status){
                editor.putBoolean("atividades", false);
                editor.commit();
                break;
            }
        }

        ArrayList<  Apresenta> array1 =  getApresentaHoje();
        if (array1.isEmpty()){
            editor.putBoolean("sintomas", false);
            editor.commit();
        }

        int num = (int) System.currentTimeMillis();
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, num, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        assert alarmMgr != null;
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

}