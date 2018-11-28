package com.lincs.mobcare.daily;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lincs.mobcare.R;
import com.lincs.mobcare.utils.AudioRecord;
import com.lincs.mobcare.utils.TimeUtils;
import com.lincs.mobcare.utils.VideoFromFirebaseActivity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.lincs.mobcare.utils.Firebase.removeRealiza;

public class ShowDailyDialogFragment extends DialogFragment {

    private String id;
    private String comentario_texto, local_audio, local_video,comentario_video;
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
    private TextView textAudio;
    private TextView textVideo;
    private AudioRecord audioRecord;
    private boolean mStartPlaying = true;
    private Uri videoUri = null;
    private String videoPath = null;

    public static ShowDailyDialogFragment newInstance(String id,long data,String titulo,String comentario_texto, String local_audio,
                                                      String local_video,String comentario_video) {
        ShowDailyDialogFragment fragment = new ShowDailyDialogFragment();

        Bundle args = new Bundle();
        args.putString("id", id);
        args.putLong("data", data);
        args.putString("titulo", titulo);
        args.putString("comentario_texto", comentario_texto);
        args.putString("local_audio", local_audio);
        args.putString("local_video", local_video);
        args.putString("comentario_video",comentario_video);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        id = getArguments().getString("id");
        comentario_texto = getArguments().getString("comentario_texto");
        local_audio = getArguments().getString("local_audio");
        local_video = getArguments().getString("local_video");
        comentario_video=getArguments().getString("comentario_video");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_dialog_daily_show, container);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        audioRecord = new AudioRecord(this.getContext());
        //Recuperar

        Button cancelar = view.findViewById(R.id.btn_cancelar_dialog_activity);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        Button remover = view.findViewById(R.id.btn_remover_dialog_activity);
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRealiza(id);

                Fragment targetFragment = getTargetFragment();
                if (targetFragment != null) {
                    targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                }

                dismiss();
            }
        });

        Calendar calendar = Calendar.getInstance();
        assert getArguments() != null;
        calendar.setTime(new Date(getArguments().getLong("data")));

        ((TextView) view.findViewById(R.id.tipo_dialog_activity)).setText(getArguments().getString("titulo"));
        ((TextView) view.findViewById(R.id.data_dialog_activity)).setText(TimeUtils.convertDate(
                calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)).substring(0,10));
        ((TextView) view.findViewById(R.id.time_dialog_activity))
                .setText(TimeUtils.convertHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        audioRecord = new AudioRecord(this.getContext());
        btnicoTxt =view.findViewById(R.id.comentar_dialog_activity);
        btnicoMic =view.findViewById(R.id.icone_gravar_audio_dialog_activity);
        btnicoCam =view.findViewById(R.id.icone_gravar_video_dialog_activity);
        EditText comentario = view.findViewById(R.id.comentario_dialog_activity);
        infoTexto = view.findViewById(R.id.informação_comentario_dialog_activity);
        btnGravarAudio = view.findViewById(R.id.gravar_audio_dialog_activity);
        btnReproduzirAudio = view.findViewById(R.id.reproduzir_audio_dialog_activity);
        btnGravarVideo = view.findViewById(R.id.gravar_video_dialog_activity);
        btnReproduzirVideo = view.findViewById(R.id.reproduzir_video_dialog_activity);
        info = view.findViewById(R.id.informacao_dialog_activity);
        infoAudio = view.findViewById(R.id.informacao_audio_dialog_activity);
        infoVideo = view.findViewById(R.id.informacao_video_dialog_activity);
        ImageView btnCan = view.findViewById(R.id.icone_cancelar);
        ImageView btnCanMic = view.findViewById(R.id.icone_cancelar_audio);
        ImageView btnCanCam = view.findViewById(R.id.icone_cancelar_video);
        textAudio = view.findViewById(R.id.nome_audio_dialog_activity);
        textVideo = view.findViewById(R.id.nome_video_dialog_activity);

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


        btnReproduzirAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioRecord.onPlay(mStartPlaying);
                if (mStartPlaying) {
                    btnReproduzirAudio.setImageResource(R.drawable.ic_stop_black_24dp);
                    audioRecord.getMPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            btnReproduzirAudio.setImageResource(R.drawable.ic_play_arrow_blue_24dp);
                        }
                    });
                }
                else {
                    btnReproduzirAudio.setImageResource(R.drawable.ic_play_arrow_blue_24dp);
                }
                mStartPlaying = !mStartPlaying;
                Log.d("AudioRecord","reproduzir audio");
            }
        });


        btnReproduzirVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (comentario_video!=null){
                        Intent intent = new Intent(getContext(), VideoFromFirebaseActivity.class).putExtra("url_video",comentario_video);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getContext(), "Video não encontrado", Toast.LENGTH_LONG).show();
                    }
            }
        });

        if (comentario_texto != null) {
            btnicoTxt.callOnClick();
            comentario.setText(comentario_texto);
            comentario.setEnabled(false);
            btnCan.setVisibility(View.GONE);
        }
        else if (local_audio != null) {
            btnicoMic.callOnClick();
            btnGravarAudio.setEnabled(false);
            String[] path = local_audio.split("/");
            textAudio.setText(path[path.length-1]);
            AudioRecord.setmFileName(local_audio);
            btnCanMic.setVisibility(View.GONE);
        }
        else if (local_video != null) {
            btnicoCam.callOnClick();
            btnGravarVideo.setEnabled(false);
            String[] path = local_video.split("/");
            textVideo.setText(path[path.length-1]);
            videoUri = Uri.fromFile(new File(local_video));
            btnCanCam.setVisibility(View.GONE);
        }
        else {
            info.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}