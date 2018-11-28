package com.lincs.mobcare.utils;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lincs.mobcare.models.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Firebase{
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseRef = database.getReference();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference storageRef = storage.getReference();
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static boolean existe_pront;

    public static void recover_anjo(final String numero_prontuario, final Runnable onLoaded) {
        TaskCompletionSource<Boolean> getAnjoSource = new TaskCompletionSource<>();
        Task getAnjo = getAnjoSource.getTask();
        final TaskCompletionSource<Boolean> getTemSource = new TaskCompletionSource<>();
        final Task getTem = getTemSource.getTask();
        final TaskCompletionSource<Boolean> getAcompanhanteSource = new TaskCompletionSource<>();
        final Task getAcompanhante = getAcompanhanteSource.getTask();
        final TaskCompletionSource<Boolean> getAvisoSource = new TaskCompletionSource<>();
        final Task getAviso = getAvisoSource.getTask();
        final TaskCompletionSource<Boolean> getProfissionalSource = new TaskCompletionSource<>();
        final Task getProfissional = getProfissionalSource.getTask();
        final TaskCompletionSource<Boolean> getRegistraSource = new TaskCompletionSource<>();
        final ArrayList<Task> getRegistraList = new ArrayList<>();
        final ArrayList<Task> getApresenta = new ArrayList<>();
        final ArrayList<Task> getRealiza = new ArrayList<>();
        final ArrayList<Task> getConsulta = new ArrayList<>();
        final ArrayList<Task> getExercicios=new ArrayList<>();

        getAnjo(numero_prontuario,getAnjoSource);

        final Task<Void> task;
        task = Tasks.whenAll(getAnjo);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if(Snapshot.getAnjo() != null) {

                    getTem(Snapshot.getAnjo().id_tem, getTemSource);
                    ArrayList<Task> tasks = new ArrayList<>();

                    tasks.add(getTem);

                    Task<Void> allTask;
                    allTask = Tasks.whenAll(tasks.toArray(new Task[tasks.size()]));
                    allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (Snapshot.getTem() != null) {

                                getAcompanhante(Snapshot.getTem().id_acompanhante, getAcompanhanteSource);
                                getAviso(getAvisoSource);
                                getProfissional(getProfissionalSource);

                                if (Snapshot.getAnjo().id_apresenta != null) {
                                    for (String id_apresenta : Snapshot.getAnjo().id_apresenta.keySet()) {
                                        TaskCompletionSource<Boolean> getApresentaSource = new TaskCompletionSource<>();
                                        getApresenta.add(getApresentaSource.getTask());
                                        getApresenta(id_apresenta, getApresentaSource);
                                    }
                                }

                                if (Snapshot.getAnjo().id_realiza != null) {
                                    for (String id_realiza : Snapshot.getAnjo().id_realiza.keySet()) {
                                        TaskCompletionSource<Boolean> getRealizaSource = new TaskCompletionSource<>();
                                        getRealiza.add(getRealizaSource.getTask());
                                        getRealiza(id_realiza, getRealizaSource);
                                    }
                                }

                                if (Snapshot.getAnjo().id_consulta != null) {
                                    for (String id_consulta : Snapshot.getAnjo().id_consulta.keySet()) {
                                        TaskCompletionSource<Boolean> getConsultaSource = new TaskCompletionSource<>();
                                        getConsulta.add(getConsultaSource.getTask());
                                        getConsulta(id_consulta, getConsultaSource);
                                    }
                                }

                                if (Snapshot.getAnjo().id_registra != null) {
                                    for (String id_registra : Snapshot.getAnjo().id_registra.keySet()) {
                                        getRegistraList.add(getRegistraSource.getTask());
                                        getRegistra(id_registra, getRegistraSource);
                                    }
                                }


                                ArrayList<Task> tasks = new ArrayList<>();

                                tasks.add(getAcompanhante);
                                tasks.add(getAviso);
                                tasks.addAll(getRegistraList);
                                tasks.addAll(getApresenta);
                                tasks.addAll(getRealiza);
                                tasks.addAll(getConsulta);
                                tasks.add(getProfissional);

                                Task<Void> allTask;
                                allTask = Tasks.whenAll(tasks.toArray(new Task[tasks.size()]));
                                allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (Snapshot.getRegistra().size()>0){
                                            for (String id_registra : Snapshot.getRegistra().keySet()){
                                                TaskCompletionSource<Boolean> taskCompletionSource=new TaskCompletionSource<>();
                                                getExercicios.add(taskCompletionSource.getTask());
                                                Registra registra=Snapshot.getRegistra().get(id_registra);
                                                Firebase.getExercicio(registra.id_exercicio,taskCompletionSource);
                                            }
                                            Task<Void> allTask;
                                            allTask = Tasks.whenAll(getExercicios.toArray(new Task[getExercicios.size()]));
                                            allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                        getChanges(Snapshot.getAnjo().id);
                                        onLoaded.run();
                                    }
                                });
                            }
                            else{
                                onLoaded.run();
                            }
                        }
                    });
                }
                else{
                    onLoaded.run();
                }
            }
        });
    }

    public static void recoverFromUserUid(final String uid, final Runnable onLoaded){
        Query query = databaseRef.child("Anjo");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if(Objects.equals(childDataSnapshot.child("user_uid").getValue(), uid)){
                        String anjo_id = (String) childDataSnapshot.child("id").getValue();
                        Snapshot.setAnjo_id(anjo_id);
                        onLoaded.run();
                    }
                }
                onLoaded.run();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void getAnjo(final String numero_prontuario, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Anjo");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Anjo anjo = data.getValue(Anjo.class);
                    assert anjo != null;
                    if(anjo.numero_prontuario.equals(numero_prontuario) || anjo.id.equals(numero_prontuario)) {
                        Snapshot.getInstance().setAnjo(anjo);
                        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                        myFirebaseInstanceIDService.onTokenRefresh();
                    }

                }
                try {
                    dbSource.setResult(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getChanges(String id){
        Query query = databaseRef.child("Anjo").child(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Anjo anjo = dataSnapshot.getValue(Anjo.class);
                Snapshot.getInstance().setAnjo(anjo);
                if(Snapshot.getConsulta()!=null && Snapshot.getAnjo().id_consulta!=null && Snapshot.getConsulta().size() < Snapshot.getAnjo().id_consulta.size()) {
                    for (String id_consulta : Snapshot.getAnjo().id_consulta.keySet()) {
                        if(Snapshot.getConsulta().get(id_consulta) == null) {
                            TaskCompletionSource<Boolean> getConsultaSource = new TaskCompletionSource<>();
                            getConsulta(id_consulta, getConsultaSource);
                        }
                    }
                }
                if(Snapshot.getRegistra()!=null && Snapshot.getAnjo().id_registra!=null && Snapshot.getRegistra().size() < Snapshot.getAnjo().id_registra.size()) {
                    for (String id_registra : Snapshot.getAnjo().id_registra.keySet()) {
                        if(Snapshot.getRegistra().get(id_registra) == null) {
                            TaskCompletionSource<Boolean> getRegistraSource = new TaskCompletionSource<>();
                            getRegistra(id_registra, getRegistraSource);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static void getTem(String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Tem").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tem tem = dataSnapshot.getValue(Tem.class);
                Snapshot.getInstance().setTem(tem);
                try {
                    if(tem != null) {
                        dbSource.setResult(true);
                    }
                    else {
                        dbSource.setResult(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void getAcompanhante(String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Acompanhante").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Acompanhante acompanhante = dataSnapshot.getValue(Acompanhante.class);
                Snapshot.getInstance().setAcompanhante(acompanhante);
                try {
                    dbSource.setResult(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void getAviso(final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Aviso");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Aviso aviso = data.getValue(Aviso.class);
                    assert aviso != null;
                    Snapshot.getAviso().put(aviso.id, aviso);
                }
                try {
                    dbSource.setResult(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void getApresenta(String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Apresenta").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Apresenta apresenta = dataSnapshot.getValue(Apresenta.class);
                if (apresenta != null) {

                    Snapshot.getApresenta().put(apresenta.id, apresenta);
                    String folder;
                    if (apresenta.comentario_audio != null) {
                        folder="audio";
                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
                        File file=new File(path,"MobCare");
                        if (!file.exists()){
                            boolean criou=file.mkdirs();
                            if (criou){
                                Log.d("Create folder","Valor "+true);
                            }else{
                                Log.d("Create folder","Valor: "+false);
                            }
                        }
                            path += "/MobCare/";
                            String [] nome_arquivo=apresenta.local_audio.split("/");
                            final String file_name=nome_arquivo[6];
                            file = new File(path,file_name);
                        if (!file.exists()){
                            StorageReference fileRef = storageRef.child("comentario/" + folder + "/" + apresenta.id + "/"+file_name);
                            fileRef.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Log.d("Download file","Downloaded successfully "+file_name);
                                    }
                                    else {
                                        Log.d("Download file","Downloaded failed "+file_name);
                                    }
                                }
                            });
                        }
                    }
                    try {
                        dbSource.setResult(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void getRealiza(String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Realiza").child(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Realiza realiza = dataSnapshot.getValue(Realiza.class);
                if (realiza != null) {
                    Snapshot.getRealiza().put(realiza.id, realiza);
                    String folder;
                    if (realiza.comentario_audio != null && realiza.local_audio!=null) {
                        realiza.comentario_video = null;
                        realiza.local_video=null;
                        folder="audio";
                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
                        File file=new File(path,"MobCare");
                        if (!file.exists()){
                            boolean criou=file.mkdirs();
                            if (criou){
                                Log.d("Create folder","Valor "+true);
                            }else{
                                Log.d("Create folder","Valor: "+false);
                            }
                        }
                        path += "/MobCare/";
                        String [] nome_arquivo=realiza.local_audio.split("/");
                        final String file_name=nome_arquivo[6];
                        file = new File(path,file_name);
                        if (!file.exists()){
                            StorageReference fileRef = storageRef.child("comentario/" + folder + "/" + realiza.id + "/"+file_name);
                            fileRef.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Log.d("Download file","Downloaded successfully "+file_name);
                                    }
                                    else {
                                        Log.d("Download file","Downloaded failed "+file_name);
                                    }
                                }
                            });
                        }
                    }
                    try {
                        dbSource.setResult(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Exercicios
    public static void getExercicio(String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Exercicio").child(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Exercicio exercicio = dataSnapshot.getValue(Exercicio.class);
                assert exercicio != null;
                Snapshot.getExercicio().put(exercicio.id, exercicio);
                getPossui(exercicio.id_possui, dbSource);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private static void getPossui(String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Possui").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Possui possui = dataSnapshot.getValue(Possui.class);
                assert possui != null;
                Snapshot.getPossui().put(possui.id,possui);
                for (String id_video : possui.id_video.keySet()) {
                    if (Snapshot.getId_video().indexOf(id_video) == -1) {
                        Snapshot.getId_video().add(id_video);
                        getVideo(id_video,dbSource);
                    }
                    else {
                        try {
                            dbSource.setResult(true);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private static void getVideo(String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Video").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Video video = dataSnapshot.getValue(Video.class);
                assert video != null;
                if (Snapshot.getId_video().indexOf(video.id) != -1) {
                    Snapshot.getVideo().put(video.id,video);
                    //downloadVideo(video.id,video.arquivo,dbSource);
                }
                else {
                    try {
                        dbSource.setResult(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void getConsulta(String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Consulta").child(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Consulta consulta = dataSnapshot.getValue(Consulta.class);
                if(consulta!= null) {
                    Snapshot.getConsulta().put(consulta.id, consulta);
                    Snapshot.updateConsultaAdapter();
                }
                //item is removed
                else {
                    String key = dataSnapshot.getKey();
                    //remove from Snapshot
                    Snapshot.getConsulta().remove(key);
                    Snapshot.updateConsultaAdapter();
                    //remove from Anjo.realiza
                    Snapshot.getAnjo().id_consulta.remove(key);
                    databaseRef.child("Anjo").child(Snapshot.getAnjo().id).child("id_consulta").child(key).setValue(null);
                }
                try {
                    dbSource.setResult(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void getProfissional(final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Profissional");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    Profissional profissional = dataSnapshotChild.getValue(Profissional.class);
                    assert profissional != null;
                    Snapshot.getProfissionais().put(profissional.id,profissional);
                    Snapshot.getId_profissional().add(profissional.id);
                }
                try {
                    dbSource.setResult(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void getRegistra(final String id, final TaskCompletionSource<Boolean> dbSource) {
        Query query = databaseRef.child("Registra").child(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Registra registra = dataSnapshot.getValue(Registra.class);
                if(registra != null) {
                    Snapshot.getRegistra().put(registra.id, registra);
                    Snapshot.updateRegistraAdapter();
                    try {
                        dbSource.setResult(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //item is removed
                else {
                    String key = dataSnapshot.getKey();
                    //remove from Snapshot
                    Snapshot.getRegistra().remove(key);
                    Snapshot.updateRegistraAdapter();
                    //remove from Anjo.realiza
                    Snapshot.getAnjo().id_registra.remove(key);
                    databaseRef.child("Anjo").child(Snapshot.getAnjo().id).child("id_registra").child(key).setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static ArrayList<Realiza> getRealizaHoje(){
        ArrayList<Realiza> array = new ArrayList<>();
        Calendar c2 = Calendar.getInstance();
        for (Map.Entry<String, Realiza> entry : Snapshot.getRealiza().entrySet()) {
            Realiza realiza = entry.getValue();
            Calendar c1 = Calendar.getInstance();
            c2.setTime(new Date(realiza.data));
            if (c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR)&&
                    c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH)&&
                    c1.get(Calendar.DATE)==c2.get(Calendar.DATE)) {
                array.add(realiza);
            }
        }
        return array;
    }

    public static ArrayList<Registra> getRegistraHoje() {
        ArrayList<Registra> array = new ArrayList<>();
        for (Map.Entry<String, Registra> entry : Snapshot.getRegistra().entrySet()) {
            array.add(entry.getValue());
        }
        return array;
    }

    static ArrayList<Apresenta> getApresentaHoje(){
        ArrayList<Apresenta> array = new ArrayList<>();
        Calendar c2 = Calendar.getInstance();
        for (Map.Entry<String, Apresenta> entry : Snapshot.getApresenta().entrySet()) {
            Apresenta apresenta = entry.getValue();
            Calendar c1 = Calendar.getInstance();
            c2.setTime(new Date(apresenta.data));
            if (c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR)&&
                    c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH)&&
                    c1.get(Calendar.DATE)==c2.get(Calendar.DATE)) {
                array.add(apresenta);
            }
        }

        return array;
    }

    public static void removeRealiza(String id) {
        Realiza realiza = Snapshot.getRealiza().get(id);
        final String folder;
        if (realiza != null) {
            if (realiza.comentario_audio != null) {
                String path=realiza.local_audio;
                String [] nome_arquivo=path.split("/");
                folder = "audio";
                StorageReference fileRef = storageRef.child("comentario/" + folder + "/" + realiza.id + "/"+nome_arquivo[6]);
                // Delete the file
                fileRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Deleted", "File deleted!");
                        }
                        else{
                            Log.d("Deleted", "Failed!");
                        }
                    }
                });
            } else if (realiza.comentario_video != null) {
                String path=realiza.local_video;
                String [] nome_arquivo=path.split("/");
                folder = "video";
                StorageReference fileRef = storageRef.child("comentario/" + folder + "/" + realiza.id + "/"+nome_arquivo[6]);
                // Delete the file
                fileRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Deleted", "File deleted!");
                        }
                        else{
                            Log.d("Deleted", "Failed!");
                        }
                    }
                });
            }
        }
        Snapshot.getRealiza().remove(id);
        databaseRef.child("Realiza").child(id).setValue(null);
        Snapshot.getAnjo().id_realiza.remove(id);
        databaseRef.child("Anjo").child(Snapshot.getAnjo().id).child("id_realiza").child(id).setValue(null);
    }

    public static void removeApresenta(String id) {
        final Apresenta apresenta1 = Snapshot.getApresenta(id);
        final String folder;
        if (apresenta1 != null) {
            if (apresenta1.comentario_audio != null) {
                folder = "audio";
                String path=apresenta1.local_audio;
                String [] nome_arquivo=path.split("/");
                StorageReference fileRef = storageRef.child("comentario/" + folder + "/" + apresenta1.id + "/"+nome_arquivo[nome_arquivo.length-1]);
                // Delete the file
                fileRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Deleted", "File deleted!");
                        }
                        else{
                            Log.d("Deleted", "Failed!");
                        }
                    }
                });
            } else if (apresenta1.comentario_video != null) {
                folder = "video";
                String path=apresenta1.local_video;
                String [] nome_arquivo=path.split("/");
                StorageReference fileRef = storageRef.child("comentario/" + folder + "/" + apresenta1.id + "/"+nome_arquivo[nome_arquivo.length-1]);
                // Delete the file
                fileRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Deleted", "File deleted!");
                        }
                        else{
                            Log.d("Deleted", "Failed!");
                        }
                    }
                });
            }
        }
       Snapshot.getApresenta().remove(id);
       databaseRef.child("Apresenta").child(id).setValue(null);

       Snapshot.getAnjo().id_apresenta.remove(id);
       databaseRef.child("Anjo").child(Snapshot.getAnjo().id).child("id_apresenta").child(id).setValue(null);
    }


    public static void updateAnjo(String foto) {
        final String id = Snapshot.getAnjo().id;
        if(foto.contains("firebasestorage")){
            databaseRef.child("Anjo").child(id).setValue(Snapshot.getAnjo());
        }
        else {
            uploadPhotoAnjo(foto, new Runnable() {
                @Override
                public void run() {
              databaseRef.child("Anjo").child(id).setValue(Snapshot.getAnjo());
                }
            });
        }
    }

    public static void updateAcompanhante(String foto) {
        final String id = Snapshot.getAcompanhante().id;
        if(foto.contains("firebasestorage")){
            databaseRef.child("Acompanhante").child(id).setValue(Snapshot.getAcompanhante());
        }
        else {
            uploadPhotoAcomp(foto, new Runnable() {
                @Override
                public void run() {
               databaseRef.child("Acompanhante").child(id).setValue(Snapshot.getAcompanhante());
                }
            });
        }
    }

    public static void updateRealiza(final Realiza realiza, final Runnable onLoaded) {
        if(realiza.id==null){
            writeNewRealiza(Snapshot.getAnjo().id, realiza.id_exercicio, realiza.data, realiza.comentario_texto,
                    realiza.comentario_audio, realiza.comentario_video, realiza.status);
            onLoaded.run();
        }
        else {
            if (realiza.local_audio != null) {

                String[] nome_arquivo=realiza.local_audio.split("/");
                Uri file = Uri.fromFile(new File(realiza.local_audio));
                final StorageReference fileRef = storageRef.child("comentario/audio/" + realiza.id + "/"+nome_arquivo[nome_arquivo.length-1]);
                UploadTask uploadTask = fileRef.putFile(file);

               uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continue with the task to get the download URL
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            realiza.comentario_audio = downloadUri.toString();
                            //realiza is updated when audio is uploaded
                            databaseRef.child("Realiza").child(realiza.id).setValue(realiza);
                            onLoaded.run();
                        } else {
                            Log.d("Task","Failed");
                        }
                    }
                });

            } else if (realiza.local_video != null) {

                String[] nome_arquivo=realiza.local_video.split("/");
                Uri file = Uri.fromFile(new File(realiza.local_video));
                final StorageReference fileRef = storageRef.child("comentario/video/" + realiza.id + "/"+nome_arquivo[nome_arquivo.length-1]);
                UploadTask uploadTask = fileRef.putFile(file);

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continue with the task to get the download URL
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            realiza.comentario_video = downloadUri.toString();
                            //realiza is updated when audio is uploaded
                            databaseRef.child("Realiza").child(realiza.id).setValue(realiza);
                            onLoaded.run();
                        } else {
                            Log.d("Task","Failed");
                        }
                    }
                });

            } else {
                databaseRef.child("Realiza").child(realiza.id).setValue(realiza);
                onLoaded.run();
            }
        }
    }

    public static void updateApresenta(final Apresenta apresenta, final Runnable onLoaded) {
        databaseRef.child("Apresenta").child(apresenta.id).setValue(apresenta);
        if (apresenta.local_audio != null) {

            String[] nome_arquivo=apresenta.local_audio.split("/");
            Uri file = Uri.fromFile(new File(apresenta.local_audio));
            final StorageReference fileRef = storageRef.child("comentario/audio/" + apresenta.id + "/"+nome_arquivo[nome_arquivo.length-1]);
            UploadTask uploadTask = fileRef.putFile(file);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        apresenta.comentario_audio = downloadUri.toString();
                        //realiza is updated when audio is uploaded
                        databaseRef.child("Realiza").child(apresenta.id).setValue(apresenta);
                        onLoaded.run();
                    } else {
                        Log.d("Task","Failed");
                    }
                }
            });

        }  else if (apresenta.local_video != null) {

            String[] nome_arquivo=apresenta.local_video.split("/");
            Uri file = Uri.fromFile(new File(apresenta.local_video));
            final StorageReference fileRef = storageRef.child("comentario/video/" + apresenta.id + "/"+nome_arquivo[nome_arquivo.length-1]);
            UploadTask uploadTask = fileRef.putFile(file);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        apresenta.comentario_video = downloadUri.toString();
                        //realiza is updated when audio is uploaded
                        databaseRef.child("Apresenta").child(apresenta.id).setValue(apresenta);
                        onLoaded.run();
                    } else {
                        Log.d("Task","Failed");
                    }
                }
            });

        }
        else {
            databaseRef.child("Apresenta").child(apresenta.id).setValue(apresenta);
            onLoaded.run();
        }
    }

    //Upload de imagens
    private static void uploadPhotoAcomp(String path, final Runnable onLoaded) {
        if (!path.contains("googleuser")) {
            Uri file = Uri.fromFile(new File(path));
            final StorageReference fileRef = storageRef.child("foto_acompanhante/" + Snapshot.getAcompanhante().id + "/foto_acompanhante");
            UploadTask uploadTask = fileRef.putFile(file);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Snapshot.getAcompanhante().foto = downloadUri.toString();
                        //realiza is updated when audio is uploaded
                        onLoaded.run();
                    } else {
                        Log.d("Task", "Failed");
                        onLoaded.run();
                    }
                }
            });
        }
        else{
            Snapshot.getAcompanhante().foto =path;
            onLoaded.run();
        }
    }
    private static void uploadPhotoAnjo(String path, final Runnable onLoaded) {
        Uri file = Uri.fromFile(new File(path));
        final StorageReference fileRef = storageRef.child("foto_anjo/"+Snapshot.getAnjo().id+"/foto_anjo");
        UploadTask uploadTask = fileRef.putFile(file);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Snapshot.getAnjo().foto= downloadUri.toString();
                    //realiza is updated when audio is uploaded
                    onLoaded.run();
                } else {
                    Log.d("Task","Failed");
                    onLoaded.run();
                }
            }
        });
    }

    //Verifica se prontuário existe
    public static void existe_prontuario(final String num_prontuario, final Runnable onLoaded){

        Query query = databaseRef.child("Anjo");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if(Objects.equals(childDataSnapshot.child("numero_prontuario").getValue(), num_prontuario)){
                        String num = (String)childDataSnapshot.child("numero_prontuario").getValue();
                        Log.d("Anjo id","Existe numero de prontuario "+num + " = " +num_prontuario);
                        setExiste_pront(true);
                    }
                }
                onLoaded.run();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void setExiste_pront(boolean existe_pront){
        Firebase.existe_pront=existe_pront;
    }
    public static boolean getExiste_pront(){
        return Firebase.existe_pront;
    }

    public static void writeNewAnjo(String uid, String nome, String sexo, String data_de_nascimento,String cidade,String numero_prontuario,String foto) {
        String id_anjo = databaseRef.child("Anjo").push().getKey();
        String id_tem = databaseRef.child("Tem").push().getKey();
        ArrayList<String>terapias=new ArrayList<>();
        terapias.add("Definir");
        ArrayList<String>telefones=new ArrayList<>();
        telefones.add("(XX)XXXXX-XXXX");
        DadosGeraisAnjo dadosGeraisAnjo=new DadosGeraisAnjo("Definir",telefones,cidade,"01/01/1970",
            terapias,"NÃO");
        DadosOftalmologicos dadosOftalmologicos=new DadosOftalmologicos("Definir","Definir","Definir",false,
                false,false,"Não",false,"");

        final Anjo anjo = new Anjo(uid, id_anjo, id_tem, null, null, null, nome,data_de_nascimento,sexo,
                numero_prontuario,dadosGeraisAnjo,dadosOftalmologicos,foto);
        Snapshot.getInstance().setAnjo(anjo);
        uploadPhotoAnjo(foto, new Runnable() {
            @Override
            public void run() {
                Anjo anjo = Snapshot.getAnjo();
                databaseRef.child("Anjo").child(anjo.id).setValue(anjo);
                MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                myFirebaseInstanceIDService.onTokenRefresh();
            }
        });
    }

    private static void writeNewTem(String id, String id_anjo, String id_acompanhante) {
        Tem tem = new Tem(id, id_anjo, id_acompanhante);
        Snapshot.getInstance().setTem(tem);
        databaseRef.child("Tem").child(id).setValue(tem);
    }

    public static void writeNewAcompanhante(String parentesco, String nome, String telefone, String foto) {
        String id = databaseRef.child("Acompanhante").push().getKey();
        final String id_tem = Snapshot.getAnjo().id_tem;

        Acompanhante acompanhante = new Acompanhante(id, parentesco, nome, telefone, foto);
        Snapshot.getInstance().setAcompanhante(acompanhante);
        uploadPhotoAcomp(foto, new Runnable() {
            @Override
            public void run() {
                Acompanhante acompanhante = Snapshot.getAcompanhante();
                databaseRef.child("Acompanhante").child(acompanhante.id).setValue(acompanhante);
                writeNewTem(id_tem, Snapshot.getAnjo().id, Snapshot.getAcompanhante().id);
            }
        });

    }

    public static Apresenta writeNewApresenta(String id_anjo,String id_sintoma,Long data,String valor,String texto,String audio,String video) {
        String id = databaseRef.child("Apresenta").push().getKey();
        Apresenta apresenta = new Apresenta(id,id_anjo,id_sintoma,data,valor,texto,null,null,audio,video);
        databaseRef.child("Apresenta").child(id).setValue(apresenta);
        databaseRef.child("Anjo").child(id_anjo).child("id_apresenta").child(id).setValue(true);
        Snapshot.getApresenta().put(apresenta.id,apresenta);
        if(Snapshot.getAnjo().id_apresenta == null){
            Snapshot.getAnjo().id_apresenta = new HashMap<>();
        }
        Snapshot.getAnjo().id_apresenta.put(apresenta.id, true);
        return apresenta;
    }

    private static void writeNewRealiza(String id_anjo, String id_exercicio, long data, String comentario_texto,
                                           String comentario_audio, String comentario_video, boolean status) {
        String id = databaseRef.child("Realiza").push().getKey();
        Realiza realiza = new Realiza(id, id_anjo, id_exercicio, data, comentario_texto, null, null, status,
                comentario_audio, comentario_video);
        databaseRef.child("Realiza").child(id).setValue(realiza);
        databaseRef.child("Anjo").child(id_anjo).child("id_realiza").child(id).setValue(true);
        Snapshot.getRealiza().put(realiza.id,realiza);
        if(Snapshot.getAnjo().id_realiza == null){
            Snapshot.getAnjo().id_realiza = new HashMap<>();
        }
        Snapshot.getAnjo().id_realiza.put(realiza.id, true);
    }

    public static void writeToken(String id_anjo, String token){//Da crash toda vez que o app inicia pela 1ª vez
        databaseRef.child("Anjo").child(id_anjo).child("token").setValue(token);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getUserUid() {
        return Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }
    public static void logout(){
        auth.signOut();
    }
    public static FirebaseAuth getAuth(){
        return auth;
    }
}