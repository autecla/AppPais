package com.lincs.mobcare.daily;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Exercicio;
import com.lincs.mobcare.models.Realiza;
import com.lincs.mobcare.utils.AudioRecord;
import com.lincs.mobcare.utils.SetTime;
import com.lincs.mobcare.utils.Snapshot;
import com.lincs.mobcare.utils.TimeUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.lincs.mobcare.utils.Firebase.updateRealiza;

public class DailyEditDialog extends DialogFragment {

    private String id;
    public String comentario_texto;
    private EditText time;

    private ImageView btnGravarAudio;
    private ImageView btnReproduzirAudio;
    private ImageView btnGravarVideo;
    private ImageView btnReproduzirVideo;
    private LinearLayout btnicoMic;
    private LinearLayout btnicoCam;
    private LinearLayout btnicoTxt;
    private LinearLayout info;
    private LinearLayout infoTexto;
    private RelativeLayout infoAudio;
    private RelativeLayout infoVideo;
    private EditText comentario;
    private TextView textAudio;
    private TextView textVideo;
    private AudioRecord audioRecord;
    private boolean mStartRecording = true;
    private boolean mStartPlaying = true;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private Uri videoUri = null;
    private String videoPath = null;
    FrameLayout progressBarHolder;
    ProgressBar progressBar;

    public static DailyEditDialog newInstance(String id) {
        DailyEditDialog fragment = new DailyEditDialog();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        id = getArguments().getString("id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.edit_daily_fragment, container);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        Log.d("Video","saving preferences");
        outState.putParcelable("videoUri", videoUri);
        outState.putString("videoPath", videoPath);
        outState.putInt("btnicoTxt",btnicoTxt.getVisibility());
        outState.putInt("btnicoMic",btnicoMic.getVisibility());
        outState.putInt("btnicoCam",btnicoCam.getVisibility());
        outState.putInt("btnGravarAudio",btnGravarAudio.getVisibility());
        outState.putInt("btnReproduzirAudio",btnReproduzirAudio.getVisibility());
        outState.putInt("btnGravarVideo",btnGravarVideo.getVisibility());
        outState.putInt("btnReproduzirVideo",btnReproduzirVideo.getVisibility());
        outState.putInt("info",info.getVisibility());
        outState.putInt("infoAudio",infoAudio.getVisibility());
        outState.putInt("infoVideo",infoVideo.getVisibility());
        outState.putInt("infoTexto",infoTexto.getVisibility());
        outState.putInt("textAudio",textAudio.getVisibility());
        outState.putString("textAudioText",textAudio.getText().toString());
        outState.putInt("textVideo",textVideo.getVisibility());
        outState.putString("textVideoText",textVideo.getText().toString());
    }

    @Override
    @SuppressWarnings("ResourceType")
    public void onViewStateRestored(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            Log.d("Video","recovering preferences");
            videoUri = savedInstanceState.getParcelable("videoUri");
            videoPath = savedInstanceState.getString("videoPath");
            btnicoTxt.setVisibility(savedInstanceState.getInt("btnicoTxt",View.VISIBLE));
            btnicoMic.setVisibility(savedInstanceState.getInt("btnicoMic",View.VISIBLE));
            btnicoCam.setVisibility(savedInstanceState.getInt("btnicoCam",View.VISIBLE));
            btnGravarAudio.setVisibility(savedInstanceState.getInt("btnGravarAudio",View.VISIBLE));
            btnReproduzirAudio.setVisibility(savedInstanceState.getInt("btnReproduzirAudio",View.VISIBLE));
            btnGravarVideo.setVisibility(savedInstanceState.getInt("btnGravarVideo",View.VISIBLE));
            btnReproduzirVideo.setVisibility(savedInstanceState.getInt("btnReproduzirVideo",View.VISIBLE));
            info.setVisibility(savedInstanceState.getInt("info",View.VISIBLE));
            infoAudio.setVisibility(savedInstanceState.getInt("infoAudio",View.VISIBLE));
            infoVideo.setVisibility(savedInstanceState.getInt("infoVideo",View.VISIBLE));
            infoTexto.setVisibility(savedInstanceState.getInt("infoTexto",View.VISIBLE));
            textAudio.setVisibility(savedInstanceState.getInt("textAudio",View.VISIBLE));
            textAudio.setText(savedInstanceState.getString("textAudioText","Título do áudio"));
            textVideo.setVisibility(savedInstanceState.getInt("textVideo",View.VISIBLE));
            textVideo.setText(savedInstanceState.getString("textVideoText","Título do vídeo"));
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Realiza evolutionAct = Snapshot.getRealiza().get(id);
          Exercicio evolution = Snapshot.getExercicio().get(evolutionAct.id_exercicio);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(evolutionAct.data));

        ((TextView) view.findViewById(R.id.tipo_dialog_edit)).setText(evolution.especialidade);
        final EditText data = view.findViewById(R.id.date_dialog_edit);
        data.setText(String.valueOf(TimeUtils.convertDate(
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)).substring(0,10)));
        data.setClickable(false);
        data.setAlpha(0.4f);

        time =view.findViewById(R.id.time_dialog_edit);
        time.setText(String.valueOf(TimeUtils.convertHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))));

        //Recuperar

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new SetTime(data, time, view.getContext());
            }
        });

        Button cancelar = view.findViewById(R.id.btn_cancelar_dialog_edit);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        progressBarHolder =view.findViewById(R.id.progressBarHolder);
        progressBar = view.findViewById(R.id.progressBar);

        Button salvar = view.findViewById(R.id.btn_salvar_dialog_edit);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                String horaS = time.getText().toString().trim();
                String[] datas = data.getText().toString().trim().split("/");
                String[] hora = horaS.trim().split(":");

                calendar.set(Integer.valueOf(datas[2]),Integer.valueOf(datas[1])-1,Integer.valueOf(datas[0]),
                        Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));

                progressBarHolder.setAlpha(0.4f);
                progressBar.setVisibility(View.VISIBLE);


                  Realiza realiza = new   Realiza(evolutionAct.id,
                        evolutionAct.id_anjo,
                        evolutionAct.id_exercicio,
                        calendar.getTime().getTime(),
                        comentario.getText().toString(),
                        evolutionAct.comentario_audio,
                        evolutionAct.comentario_video,
                        true,
                        evolutionAct.local_audio,
                        evolutionAct.local_video);
                updateRealiza(realiza, new Runnable() {
                    @Override
                    public void run() {
                        progressBarHolder.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);

                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment != null) {
                            targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                        }

                        dismiss();
                    }
                });
            }
        });

        audioRecord = new AudioRecord(getContext());
        btnicoTxt =  view.findViewById(R.id.comentar_dialog_edit);
        btnicoMic = view.findViewById(R.id.icone_gravar_audio_dialog_edit);
        btnicoCam = view.findViewById(R.id.icone_gravar_video_dialog_edit);
        comentario = view.findViewById(R.id.comentario_dialog_edit);
        infoTexto =  view.findViewById(R.id.informação_comentario_dialog_edit);
        btnGravarAudio = view.findViewById(R.id.gravar_audio_dialog_edit);
        btnReproduzirAudio = view.findViewById(R.id.reproduzir_audio_dialog_edit);
        btnGravarVideo = view.findViewById(R.id.gravar_video_dialog_edit);
        btnReproduzirVideo =  view.findViewById(R.id.reproduzir_video_dialog_edit);
        info = view.findViewById(R.id.informacao_dialog_edit);
        infoAudio = view.findViewById(R.id.informacao_audio_dialog_edit);
        infoVideo = view.findViewById(R.id.informacao_video_dialog_edit);
        ImageView btnCan = view.findViewById(R.id.icone_cancelar);
        ImageView btnCanMic = view.findViewById(R.id.icone_cancelar_audio);
        ImageView btnCanCam = view.findViewById(R.id.icone_cancelar_video);
        textAudio =  view.findViewById(R.id.nome_audio_dialog_edit);
        textVideo = view.findViewById(R.id.nome_video_dialog_edit);

        btnicoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setVisibility(View.GONE);
                infoTexto.setVisibility(View.VISIBLE);
            }
        });
        btnicoMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setVisibility(View.GONE);
                infoAudio.setVisibility(View.VISIBLE);
            }
        });
        btnicoCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("VIdeoRecord", "filmar");
                info.setVisibility(View.GONE);
                infoVideo.setVisibility(View.VISIBLE);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setVisibility(View.VISIBLE);
                infoTexto.setVisibility(View.GONE);
                infoAudio.setVisibility(View.GONE);
                infoVideo.setVisibility(View.GONE);
                deleteMedia();
            }
        };
        btnCan.setOnClickListener(onClickListener);
        btnCanMic.setOnClickListener(onClickListener);
        btnCanCam.setOnClickListener(onClickListener);

        btnGravarAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecord.startTime();
                audioRecord.onRecord(mStartRecording);
                if (mStartRecording) {
                    btnGravarAudio.setImageResource(R.drawable.ic_microphone_red_24dp);
                    String grav="Gravando audio...";
                    textAudio.setText(grav);
                }
                else if (audioRecord.countTime() > 150 && !mStartRecording) {
                    btnGravarAudio.setImageResource(R.drawable.ic_mic_black_24dp);
                    mStartRecording = !mStartRecording;
                    String[] path = audioRecord.getmFileName().split("/");
                    textAudio.setText(path[path.length-1]);
                    textAudio.setVisibility(View.VISIBLE);
                    btnReproduzirAudio.setVisibility(View.VISIBLE);

                }
                else {
                    btnGravarAudio.setImageResource(R.drawable.ic_mic_black_24dp);
                    String[] path = audioRecord.getmFileName().split("/");
                    textAudio.setText(path[path.length-1]);
                    textAudio.setVisibility(View.VISIBLE);
                    btnReproduzirAudio.setVisibility(View.VISIBLE);
                }
                mStartRecording = !mStartRecording;

            }

        });


        btnReproduzirAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioRecord.onPlay(mStartPlaying);
                if (mStartPlaying) {
                    btnReproduzirAudio.setImageResource(R.drawable.ic_stop_black_24dp);
                    audioRecord.getMPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            btnReproduzirAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        }
                    });
                }
                else {
                    btnReproduzirAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                mStartPlaying = !mStartPlaying;
                Log.d("AudioRecord","reproduzir audio");
            }
        });

        btnGravarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });

        btnReproduzirVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoUri != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
                    startActivity(intent);
                }
            }
        });

        if (evolutionAct.comentario_texto != null) {
            btnicoTxt.callOnClick();
            comentario.setText(comentario_texto);
            comentario.setEnabled(false);
            btnCan.setVisibility(View.VISIBLE);
        }
        else if (evolutionAct.local_audio != null) {
            btnicoMic.callOnClick();
            btnGravarAudio.setEnabled(false);
            String[] path = evolutionAct.local_audio.split("/");
            textAudio.setText(path[path.length-1]);
            AudioRecord.setmFileName(evolutionAct.local_audio);
            btnCanMic.setVisibility(View.VISIBLE);
        }
        else if (evolutionAct.local_video != null) {
            btnicoCam.callOnClick();
            btnGravarVideo.setEnabled(false);
            String[] path = evolutionAct.local_video.split("/");
            textVideo.setText(path[path.length-1]);
            videoUri = Uri.fromFile(new File(evolutionAct.local_video));
            btnCanCam.setVisibility(View.VISIBLE);
        }
    }

    public void dispatchTakeVideoIntent() {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d("Video","has Camera");
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                Log.d("Video","has video cam app");
            }
        }
    }

    public void deleteMedia () {
        String aperte="Aperte para filmar";
        String press="Pressione para gravar";
        textVideo.setText(aperte);
        textAudio.setText(press);
        comentario.setText(null);
        File audio = new File(audioRecord.getmFileName());
        if (audio.exists()) {
            boolean audio_del=audio.delete();
            if (audio_del){
                Log.d("Audio deleted","Delete: "+ true);
            }
        }
        if (videoPath != null) {
            File video = new File(videoPath);
            if (video.delete()) {
                getContext().getContentResolver().delete(videoUri,null,null);
                videoPath = null;
                videoUri = null;
            }
        }
    }
}

