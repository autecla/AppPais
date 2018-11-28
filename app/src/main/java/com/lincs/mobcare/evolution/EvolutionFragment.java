package com.lincs.mobcare.evolution;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.lincs.mobcare.R;
import com.lincs.mobcare.daily.AdapterCallback;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.Snapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.lincs.mobcare.utils.Firebase.getRegistraHoje;
import static com.lincs.mobcare.utils.Firebase.updateRealiza;
import com.lincs.mobcare.models.*;

public class EvolutionFragment extends Fragment implements AdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private HashMap<CompoundButton, Boolean> checks;
    private boolean checked;
    private EvolutionAdapter adapter;

    FrameLayout progressBarHolder;
    ProgressBar progressBar;
    Context context;
    SwipeRefreshLayout swipeLayout;
    AdapterCallback adapterCallback;
    private boolean flagDialog;
    RecyclerView rvEvolution;
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    public EvolutionFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        checks = new HashMap<>();
        checked = false;
        context = this.getContext();
        return inflater.inflate(R.layout.fragment_evolution, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lookup the recyclerview in activity layout
        rvEvolution =view.findViewById(R.id.rvEvolution);

        final Button btn_evolution_confirmar = view.findViewById(R.id.btn_evolution_confirmar);

        ArrayList<Registra> evolutionsAct = getRegistraHoje();

        progressBarHolder =view.findViewById(R.id.progressBarHolder);
        progressBar = view.findViewById(R.id.progressBar);

        swipeLayout = view.findViewById(R.id.swipe_container_ev);
        swipeLayout.setOnRefreshListener(this);

        adapterCallback = this;

        // Create adapter passing in the sample user data
        adapter = new EvolutionAdapter(evolutionsAct, Objects.requireNonNull(getView()).getContext(), this);

        adapter.setOnCheckedChangeListener(new EvolutionAdapter.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CompoundButton compoundButton, boolean isChecked) {
                saveOrUpdateHash(compoundButton, isChecked);
                for (Object o : checks.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    if ((Boolean) pair.getValue() && ((CompoundButton) pair.getKey()).isEnabled()) {
                        checked = true;
                    }
                }
                if (checked) {
                    turnOnButtonConfirmar(btn_evolution_confirmar);
                } else {
                    turnOffButtonConfirmar(btn_evolution_confirmar);
                }
                checked = false;
            }
        });


        Snapshot.setRegistraAdapter(adapter);

        btn_evolution_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < adapter.positions2.size(); i++) {
                    EvolutionAdapter.ViewHolder vh = adapter.positions2.get(i);
                    Registra reg = adapter.checked.get(i);

                    if(vh.evolutionCheckBox.isEnabled()) {
                        vh.evolutionCheckBox.setEnabled(false);
                        vh.evolutionCheckBox2.setEnabled(false);

                        Calendar calendar = Calendar.getInstance();
                        Realiza ev = new Realiza(null,null,reg.id_exercicio,calendar.getTime().getTime(),
                                null,null,null,true,null,null);

                        // criar nova Realiza
                        /*ev.data = calendar.getTime().getTime();
                        ev.comentario_texto = null;
                        ev.comentario_audio = null;
                        ev.comentario_video = null;
                        ev.status = true;
                        ev.id_exercicio = reg.id_exercicio;*/


                        updateRealiza(ev, new Runnable() {
                            @Override
                            public void run() {
                                //Recuperar os evolutions
                                //Setar true no checked

                                turnOffButtonConfirmar(btn_evolution_confirmar);
                                if(!flagDialog) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            flagDialog=true;
                                        }
                                    });

                                    Calendar calendar1 = Calendar.getInstance();
                                    Date date = calendar1.getTime();
                                    String formatDate="dd/MM/yyyy";
                                    SimpleDateFormat df = new SimpleDateFormat(formatDate, Locale.ROOT);
                                    alertDialogBuilder.setMessage("As atividades foram marcadas como executadas no dia " + df.format(date));
                                    alertDialogBuilder.setTitle("Execução de atividade");
                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialog) {
                                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.cyan_500));
                                        }
                                    });
                                    alertDialog.show();
                                }
                            }
                        });
                    }
                }
            }
        });

        // Attach the adapter to the recyclerview to populate items
        rvEvolution.setAdapter(adapter);
        // Set layout manager to position the items
        rvEvolution.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void saveOrUpdateHash(CompoundButton cb, Boolean bool) {
        if (checks.size() > 0 && checks.containsKey(cb)) {
            checks.remove(cb);
            checks.put(cb, bool);
        } else {
            checks.put(cb, bool);
        }
    }

    public void turnOffButtonConfirmar(Button btn) {
        btn.setBackgroundResource(R.drawable.shape_retangle);
        btn.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.grey500));
        btn.setEnabled(false);
    }

    private void turnOnButtonConfirmar(Button btn) {
        btn.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.mdtp_white));
        btn.setBackgroundResource(R.drawable.shape_retangle_blue);
        btn.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("Video","onActivityResult: requestCode="+requestCode+" resultCode="+resultCode);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

            Uri videoUri = intent.getData();
            String[] proj = { MediaStore.Video.Media.DATA };
            assert videoUri != null;
            Cursor cursor = null;
            try{
                cursor = context.getContentResolver().query(videoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                String videoPath = cursor.getString(column_index);
                Realiza realiza = Snapshot.getRealiza().get(Snapshot.getId_realiza_activity());
                realiza.local_video = videoPath;
            }
            catch (Exception e){e.getMessage();}
            finally {
                assert cursor != null;
                cursor.close();
            }

        }
    }


    @Override
    public void onRefresh() {
        onMethodCallback();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);

            }
        }, 5000);
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public void onMethodCallback() {
        Firebase.getChanges(Snapshot.getAnjo_id());
        ArrayList<Task>getExercicios=new ArrayList<>();
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
                    Log.d("Tam","Tam getVideo: "+Snapshot.getVideo().size());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Falhou","Exercicios, possui e getvideos!");
                }
            });
        }
    }
}