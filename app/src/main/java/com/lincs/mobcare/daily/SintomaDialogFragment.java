package com.lincs.mobcare.daily;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.*;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.SetDate;
import com.lincs.mobcare.utils.SetTime;
import com.lincs.mobcare.utils.Snapshot;
import com.lincs.mobcare.utils.TimeUtils;
import com.lincs.mobcare.utils.AudioRecord;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.view.Window.FEATURE_NO_TITLE;
import static com.lincs.mobcare.utils.Firebase.updateApresenta;
import static com.lincs.mobcare.utils.Firebase.writeNewApresenta;

public class SintomaDialogFragment extends DialogFragment{

    private EditText comentario;
    private static int layout;
    private EditText time, date;
    private HashMap<Integer, Double> alimentacao;
    private HashMap<Integer, String> tempo;
    private TextView textTempo;
    private int titulo;
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
    private boolean editar;
    private String id;
    private AudioRecord audioRecord;
    private boolean mStartRecording = true;
    private boolean mStartPlaying = true;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private Uri videoUri = null;
    private String videoPath = null;
    private static final int MY_PERMISSIONS_REQUEST = 283;

    FrameLayout progressBarHolder;
    ProgressBar progressBar;

    public static SintomaDialogFragment newInstance(int layout, int titulo) {
        SintomaDialogFragment fragment = new SintomaDialogFragment();
        Bundle args = new Bundle();
        args.putInt("layout", layout);
        args.putInt("titulo", titulo);
        fragment.setArguments(args);
        return fragment;
    }

    public static SintomaDialogFragment newInstance(int layout, int titulo, String id, boolean editar) {
        SintomaDialogFragment fragment = new SintomaDialogFragment();
        Bundle args = new Bundle();
        args.putInt("layout", layout);
        args.putInt("titulo", titulo);
        args.putBoolean("editar", editar);
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("deleteMedia","onCancel");
        deleteMedia();
        dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        layout = bundle.getInt("layout");
        this.titulo = bundle.getInt("titulo");
        this.editar = bundle.getBoolean("editar");
        this.id = bundle.getString("id");

        Objects.requireNonNull(getDialog().getWindow()).requestFeature(FEATURE_NO_TITLE);

        return inflater.inflate(layout, container);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int permissionCheck = ContextCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()),Manifest.permission.RECORD_AUDIO);
        if(permissionCheck== PackageManager.PERMISSION_GRANTED) {
            audioRecord = new AudioRecord(getContext());

        }else{
            ActivityCompat.requestPermissions((Activity) this.getContext(),new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST);
        }


        //Alteração do Layout de choro_vomito_gripe_dialog
        switch (titulo) {
            case R.id.menu_item_sono:
                ((ImageView) view.findViewById(R.id.imagem_cvg_dialog_sintoma)).setImageResource(R.mipmap.ic_vomito_new);
                ((TextView) view.findViewById(R.id.titulo_choro_dialog_sintoma)).setText(R.string.comportamento_sono);
                (view.findViewById(R.id.barra_superior_dialog_sintoma)).setBackgroundColor(getResources().getColor(R.color.colorSintomaVomito));
                break;

            case R.id.menu_item_mudanca:
                ((ImageView) view.findViewById(R.id.imagem_cvg_dialog_sintoma)).setImageResource(R.mipmap.ic_gripe_new);
                ((TextView) view.findViewById(R.id.titulo_choro_dialog_sintoma)).setText(R.string.comportamento_mudanca);
                (view.findViewById(R.id.barra_superior_dialog_sintoma)).setBackgroundColor(getResources().getColor(R.color.colorSintomaTrocadeMedicacao));
                break;

            case R.id.menu_item_doenca:
                ((ImageView) view.findViewById(R.id.imagem_cvg_dialog_sintoma)).setImageResource(R.mipmap.ic_engasgo_new);
                ((TextView) view.findViewById(R.id.titulo_choro_dialog_sintoma)).setText(R.string.comportamento_doenca);
                (view.findViewById(R.id.barra_superior_dialog_sintoma)).setBackgroundColor(getResources().getColor(R.color.colorSintomaDoenca));
                break;

            case R.id.menu_item_agressividade:
                ((ImageView) view.findViewById(R.id.imagem_cvg_dialog_sintoma)).setImageResource(R.mipmap.ic_espasmos_new);
                ((TextView) view.findViewById(R.id.titulo_choro_dialog_sintoma)).setText(R.string.comportamento_agressividade);
                (view.findViewById(R.id.barra_superior_dialog_sintoma)).setBackgroundColor(getResources().getColor(R.color.colorSintomaAgressividade));
                break;
            case R.id.menu_item_outro:
                ((ImageView) view.findViewById(R.id.imagem_cvg_dialog_sintoma)).setImageResource(R.mipmap.ic_convulsao_new);
                ((TextView) view.findViewById(R.id.titulo_outro_sintoma)).setText(R.string.outros);
                (view.findViewById(R.id.barra_superior_dialog_sintoma)).setBackgroundColor(getResources().getColor(R.color.colorSintomaOutro));
                break;
        }

        Calendar cal = Calendar.getInstance();
        int ano = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int hrs = cal.get(Calendar.HOUR_OF_DAY);
        int mnts = cal.get(Calendar.MINUTE);

        btnicoTxt =  view.findViewById(R.id.comentar_dialog_sintoma);
        btnicoMic =  view.findViewById(R.id.icone_gravar_audio_dialog_sintoma);
        btnicoCam =  view.findViewById(R.id.icone_gravar_video_dialog_sintoma);
        comentario = view.findViewById(R.id.comentario_dialog_sintoma);
        infoTexto =  view.findViewById(R.id.informação_comentario_dialog_sintoma);
        btnGravarAudio =  view.findViewById(R.id.gravar_audio_dialog_sintoma);
        btnReproduzirAudio =  view.findViewById(R.id.reproduzir_audio_dialog_sintoma);
        btnGravarVideo = view.findViewById(R.id.gravar_video_dialog_sintoma);
        btnReproduzirVideo = view.findViewById(R.id.reproduzir_video_dialog_sintoma);
        info =  view.findViewById(R.id.informacao_dialog_sintoma);
        infoAudio =  view.findViewById(R.id.informacao_audio_dialog_sintoma);
        infoVideo =  view.findViewById(R.id.informacao_video_dialog_sintoma);
        ImageView btnCan = view.findViewById(R.id.icone_cancelar);
        ImageView btnCanMic = view.findViewById(R.id.icone_cancelar_audio);
        ImageView btnCanCam = view.findViewById(R.id.icone_cancelar_video);
        textAudio =  view.findViewById(R.id.nome_audio_dialog_sintoma);
        textVideo = view.findViewById(R.id.nome_video_dialog_sintoma);

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
                    audioRecord.onRecord(false);
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
                    File file = new File(videoUri.getPath());
                    if (file.exists()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
                        intent.setDataAndType(videoUri, "video/mp4");
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getContext(), "Video não encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        progressBarHolder =  view.findViewById(R.id.progressBarHolder);
        progressBar =  view.findViewById(R.id.progressBar);

        Button cancelar;
        Button enviar;
        SeekBar seekBar;
        if (!editar) {

            String hora = TimeUtils.convertHour(hrs, mnts);
            String data = TimeUtils.convertDate(dia, mes, ano, hrs, mnts).substring(0,11);
            //Mapeamento dos componentes conforme o layout
            switch (layout) {

                case R.layout.febre_dialog:

                    createHashMapAlimentacao();
                    time =  view.findViewById(R.id.time_febre_dialog_sintoma);
                    time.setText(hora);
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { new SetTime(date, time, view.getContext());    }
                    });

                    date = view.findViewById(R.id.data_febre_dialog_sintoma);
                    date.setText(data);
                    date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { new SetDate(date, time, view.getContext());}
                    });



                    cancelar = view.findViewById(R.id.btn_cancelar_febre_dialog_sintoma);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    });

                    enviar = view.findViewById(R.id.btn_enviar_febre_dialog_sintoma);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            String[] hora = time.getText().toString().trim().split(":");
                            String[] data = date.getText().toString().trim().split("/");

                            calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]),
                                    Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));

                            Log.d("hora", hora[0] + ":" + hora[1]);
                            Log.d("data", data[2] + "/" + hora[1]+ "/" + hora[0]);

                            String texto = null;
                            String audio = null;
                            String video = null;
                            if (infoTexto.getVisibility() == View.VISIBLE) {
                                texto = comentario.getText().toString();
                            }
                            else if (infoAudio.getVisibility() == View.VISIBLE) {
                                audio = audioRecord.getmFileName();
                            }
                            else if (infoVideo.getVisibility() == View.VISIBLE) {
                                video = videoPath;
                            }
                              Apresenta apresenta =   writeNewApresenta(Snapshot.getAnjo().id,getIdSintoma(),calendar.getTime().getTime()
                                    ,"Teste",texto,audio,video);

                            progressBarHolder.setAlpha(0.4f);
                            progressBar.setVisibility(View.VISIBLE);
                              updateApresenta(apresenta, new Runnable() {
                                @Override
                                public void run() {
                                    progressBarHolder.setAlpha(1f);
                                    progressBar.setVisibility(View.GONE);

                                    Fragment targetFragment = getTargetFragment();
                                    if (targetFragment != null) {
                                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                    }

                                    Toast.makeText(getContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    });

                    break;

                case R.layout.choro_vomito_gripe_dialog:
                    time =  view.findViewById(R.id.time_choro_dialog_sintoma);
                    time.setText(hora);
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SetTime(date, time, view.getContext()/*, Calendar.getInstance()*/);
                        }
                    });

                    date = view.findViewById(R.id.data_choro_dialog_sintoma);
                    date.setText(data);
                    date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SetDate(date, time, view.getContext());
                        }
                    });

                    cancelar =  view.findViewById(R.id.btn_cancelar_choro_dialog_sintoma);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {dismiss();}
                    });

                    enviar =  view.findViewById(R.id.btn_enviar_choro_dialog_sintoma);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            String[] hora = time.getText().toString().trim().split(":");
                            String[] data = date.getText().toString().trim().split("/");

                            calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]),
                                    Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));

                            String texto = null;
                            String audio = null;
                            String video = null;
                            if (infoTexto.getVisibility() == View.VISIBLE) {
                                texto = comentario.getText().toString();
                            }
                            else if (infoAudio.getVisibility() == View.VISIBLE) {
                                audio = audioRecord.getmFileName();
                            }
                            else if (infoVideo.getVisibility() == View.VISIBLE) {
                                video = videoPath;
                            }

                              Apresenta apresenta =   writeNewApresenta(Snapshot.getAnjo().id,getIdSintoma(),calendar.getTime().getTime(),
                                    null,texto,audio,video);

                            progressBarHolder.setAlpha(0.4f);
                            progressBar.setVisibility(View.VISIBLE);
                              updateApresenta(apresenta, new Runnable() {
                                @Override
                                public void run() {
                                    progressBarHolder.setAlpha(1f);
                                    progressBar.setVisibility(View.GONE);

                                    Fragment targetFragment = getTargetFragment();
                                    if (targetFragment != null) {
                                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                    }

                                    Toast.makeText(getContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    });

                    break;

                case R.layout.convulsao_dialog:

                    createHashMapTempo();
                    time =  view.findViewById(R.id.time_convulsao_dialog_sintoma);
                    time.setText(hora);
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SetTime(date, time, view.getContext()/*, Calendar.getInstance()*/);
                        }
                    });

                    date = view.findViewById(R.id.data_convulsao_dialog_sintoma);
                    date.setText(data);
                    date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SetDate(date, time, view.getContext());
                        }
                    });

                    cancelar =  view.findViewById(R.id.btn_cancelar_convulsao_dialog_sintoma);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onCancel(getDialog());
                        }
                    });

                    textTempo =  view.findViewById(R.id.label_convulsao_dialog_sintoma);

                    seekBar =  view.findViewById(R.id.seekbar_convulsao_dialog_sintoma);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        int progress = 0;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progresValue, boolean b) {
                            progress = progresValue;
                            String tmp= tempo.get(progress) +" min";
                            textTempo.setText(tmp);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {}
                    });

                    enviar = view.findViewById(R.id.btn_enviar_convulsao_dialog_sintoma);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            String[] hora = time.getText().toString().trim().split(":");
                            String[] data = date.getText().toString().trim().split("/");

                            calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]),
                                    Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));
                            String texto = null;
                            String audio = null;
                            String video = null;
                            if (infoTexto.getVisibility() == View.VISIBLE) {
                                texto = comentario.getText().toString();
                            }
                            else if (infoAudio.getVisibility() == View.VISIBLE) {
                                audio = audioRecord.getmFileName();
                            }
                            else if (infoVideo.getVisibility() == View.VISIBLE) {
                                video = videoPath;
                            }

                              Apresenta apresenta =   writeNewApresenta(Snapshot.getAnjo().id,getIdSintoma(),calendar.getTime().getTime(),textTempo.getText().toString(),texto,audio,video);

                            progressBarHolder.setAlpha(0.4f);
                            progressBar.setVisibility(View.VISIBLE);
                              updateApresenta(apresenta, new Runnable() {
                                @Override
                                public void run() {
                                    progressBarHolder.setAlpha(0.4f);
                                    progressBar.setVisibility(View.VISIBLE);

                                    Fragment targetFragment = getTargetFragment();
                                    if (targetFragment != null) {
                                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                    }

                                    Toast.makeText(getContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    });

                    break;
                case R.layout.outros_sintomas_dialog:

                    createHashMapTempo();
                    time =  view.findViewById(R.id.time_outro_dialog_sintoma);
                    time.setText(hora);
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SetTime(date, time, view.getContext());
                        }
                    });

                    date = view.findViewById(R.id.data_outro_sintoma);
                    date.setText(data);
                    date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           new SetDate(date, time, view.getContext());
                        }
                    });

                    cancelar =  view.findViewById(R.id.btn_cancelar_outro_dialog_sintoma);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onCancel(getDialog());
                        }
                    });

                    enviar = view.findViewById(R.id.btn_enviar_outro_dialog_sintoma);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            String[] hora = time.getText().toString().trim().split(":");
                            String[] data = date.getText().toString().trim().split("/");

                            calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]),
                                    Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));
                            String texto = null;
                            String audio = null;
                            String video = null;
                            if (infoTexto.getVisibility() == View.VISIBLE) {
                                texto = comentario.getText().toString();
                            }
                            else if (infoAudio.getVisibility() == View.VISIBLE) {
                                audio = audioRecord.getmFileName();
                            }
                            else if (infoVideo.getVisibility() == View.VISIBLE) {
                                video = videoPath;
                            }
                              Apresenta apresenta =   writeNewApresenta(Snapshot.getAnjo().id,getIdSintoma(),calendar.getTime().getTime(),
                                    null,texto,audio,video);

                            progressBarHolder.setAlpha(0.4f);
                            progressBar.setVisibility(View.VISIBLE);
                              updateApresenta(apresenta, new Runnable() {
                                @Override
                                public void run() {
                                    progressBarHolder.setAlpha(0.4f);
                                    progressBar.setVisibility(View.VISIBLE);

                                    Fragment targetFragment = getTargetFragment();
                                    if (targetFragment != null) {
                                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                    }

                                    Toast.makeText(getContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    });

                    break;
            }
        } else { // EDITAR SINTOMA

            final   Apresenta apresenta = Snapshot.getApresenta(id);

            if (apresenta.comentario_texto != null) {
                btnicoTxt.callOnClick();
                comentario.setText(apresenta.comentario_texto);
                comentario.setEnabled(true);
                btnCan.setVisibility(View.VISIBLE);
            }
            else if (apresenta.local_audio != null) {
                btnicoMic.callOnClick();
                btnGravarAudio.setEnabled(false);
                String[] path = apresenta.local_audio.split("/");
                textAudio.setText(path[path.length-1]);
                AudioRecord.setmFileName(apresenta.local_audio);
                btnCanMic.setVisibility(View.VISIBLE);
            }
            else if (apresenta.local_video != null) {
                btnicoCam.callOnClick();
                btnGravarVideo.setEnabled(false);
                String[] path = apresenta.local_video.split("/");
                textVideo.setText(path[path.length-1]);
                videoUri = Uri.parse(apresenta.local_video);
                btnCanCam.setVisibility(View.VISIBLE);
            }

            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(apresenta.data));

            //Mapeamento dos componentes conforme o layout
            switch (layout) {

                case R.layout.febre_dialog:
                    createHashMapAlimentacao();

                    date =  view.findViewById(R.id.data_febre_dialog_sintoma);
                    date.setText(TimeUtils.convertDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)).substring(0,10));
                    date.setClickable(false);
                    date.setAlpha(0.4f);

                    time =  view.findViewById(R.id.time_febre_dialog_sintoma);
                    time.setText(TimeUtils.convertHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           new SetTime(date, time, view.getContext());
                        }
                    });


                    int position = 0;

                    Log.d("alimentacao", "" + Double.valueOf(apresenta.valor));
                    for (int key : alimentacao.keySet()) {
                        if (alimentacao.get(key).equals(Double.valueOf(apresenta.valor))) {
                            position = key;
                        }
                    }



                    cancelar =  view.findViewById(R.id.btn_cancelar_febre_dialog_sintoma);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    });

                    enviar =  view.findViewById(R.id.btn_enviar_febre_dialog_sintoma);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            String horaS = time.getText().toString().trim();
                            String dataS = date.getText().toString().trim();
                            String[] data = dataS.trim().split("/");
                            String[] hora = horaS.trim().split(":");

                            calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]),
                                    Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));
                              Apresenta apresenta1 = new   Apresenta(apresenta.id,apresenta.id_anjo,apresenta.id_sintoma,calendar.getTime().getTime(),
                                    "Teste",null,null,null,null,null);
                            if (infoTexto.getVisibility() == View.VISIBLE) {
                                apresenta1.comentario_texto = comentario.getText().toString();
                            }
                            else if (infoAudio.getVisibility() == View.VISIBLE) {
                                apresenta1.local_audio = audioRecord.getmFileName();
                            }
                            else if (infoVideo.getVisibility() == View.VISIBLE) {
                                apresenta1.local_video = videoPath;
                            }

                            progressBarHolder.setAlpha(0.4f);
                            progressBar.setVisibility(View.VISIBLE);
                              updateApresenta(apresenta1, new Runnable() {
                                @Override
                                public void run() {
                                    progressBarHolder.setAlpha(1f);
                                    progressBar.setVisibility(View.GONE);

                                    Fragment targetFragment = getTargetFragment();
                                    if (targetFragment != null) {
                                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                    }

                                    Toast.makeText(getContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    });

                    break;

                case R.layout.choro_vomito_gripe_dialog:
                    date = view.findViewById(R.id.data_choro_dialog_sintoma);
                    date.setText(TimeUtils.convertDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)).substring(0,10));
                    date.setClickable(false);
                    date.setAlpha(0.4f);

                    time =  view.findViewById(R.id.time_choro_dialog_sintoma);
                    time.setText(TimeUtils.convertHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SetTime(date, time, view.getContext());
                        }
                    });

                    cancelar =  view.findViewById(R.id.btn_cancelar_choro_dialog_sintoma);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    });

                    enviar = view.findViewById(R.id.btn_enviar_choro_dialog_sintoma);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            String horaS = time.getText().toString().trim();
                            String dataS = date.getText().toString().trim();
                            String[] data = dataS.trim().split("/");
                            String[] hora = horaS.trim().split(":");

                            calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]),
                                    Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));
                              Apresenta apresenta1 = new   Apresenta(apresenta.id,apresenta.id_anjo,apresenta.id_sintoma,calendar.getTime().getTime(),
                                    null,null,null,null,null,null);
                            if (infoTexto.getVisibility() == View.VISIBLE) {
                                apresenta1.comentario_texto = comentario.getText().toString();
                            }
                            else if (infoAudio.getVisibility() == View.VISIBLE) {
                                apresenta1.local_audio = audioRecord.getmFileName();
                            }
                            else if (infoVideo.getVisibility() == View.VISIBLE) {
                                apresenta1.local_video = videoPath;
                            }

                            progressBarHolder.setAlpha(0.4f);
                            progressBar.setVisibility(View.VISIBLE);
                              updateApresenta(apresenta1, new Runnable() {
                                @Override
                                public void run() {
                                    progressBarHolder.setAlpha(1f);
                                    progressBar.setVisibility(View.GONE);

                                    Fragment targetFragment = getTargetFragment();
                                    if (targetFragment != null) {
                                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                    }

                                    Toast.makeText(getContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    });

                    break;
                case R.layout.outros_sintomas_dialog:
                    date = view.findViewById(R.id.data_outro_sintoma);
                    date.setText(TimeUtils.convertDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)).substring(0,10));
                    date.setClickable(false);
                    date.setAlpha(0.4f);

                    time =  view.findViewById(R.id.time_outro_dialog_sintoma);
                    time.setText(TimeUtils.convertHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SetTime(date, time, view.getContext());
                        }
                    });

                    cancelar =  view.findViewById(R.id.btn_cancelar_outro_dialog_sintoma);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    });

                    enviar = view.findViewById(R.id.btn_enviar_outro_dialog_sintoma);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            String horaS = time.getText().toString().trim();
                            String dataS = date.getText().toString().trim();
                            String[] data = dataS.trim().split("/");
                            String[] hora = horaS.trim().split(":");

                            calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]),
                                    Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));
                              Apresenta apresenta1 = new   Apresenta(apresenta.id,apresenta.id_anjo,apresenta.id_sintoma,calendar.getTime().getTime(),
                                    null,null,null,null,null,null);
                            if (infoTexto.getVisibility() == View.VISIBLE) {
                                apresenta1.comentario_texto = comentario.getText().toString();
                            }
                            else if (infoAudio.getVisibility() == View.VISIBLE) {
                                apresenta1.local_audio = audioRecord.getmFileName();
                            }
                            else if (infoVideo.getVisibility() == View.VISIBLE) {
                                apresenta1.local_video = videoPath;
                            }

                            progressBarHolder.setAlpha(0.4f);
                            progressBar.setVisibility(View.VISIBLE);
                              updateApresenta(apresenta1, new Runnable() {
                                @Override
                                public void run() {
                                    progressBarHolder.setAlpha(1f);
                                    progressBar.setVisibility(View.GONE);

                                    Fragment targetFragment = getTargetFragment();
                                    if (targetFragment != null) {
                                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                    }
                                    Toast.makeText(getContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    });

                    break;

                case R.layout.convulsao_dialog:

                    createHashMapTempo();
                    date =  view.findViewById(R.id.data_convulsao_dialog_sintoma);
                    date.setText(TimeUtils.convertDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)).substring(0,10));
                    date.setClickable(false);
                    date.setAlpha(0.4f);

                    time = view.findViewById(R.id.time_convulsao_dialog_sintoma);
                    time.setText(TimeUtils.convertHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SetTime(date, time, view.getContext());
                        }
                    });

                    cancelar =  view.findViewById(R.id.btn_cancelar_convulsao_dialog_sintoma);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onCancel(getDialog());
                        }
                    });

                    textTempo = view.findViewById(R.id.label_convulsao_dialog_sintoma);
                    position = 0;

                    for (int key : tempo.keySet()) {
                        if(tempo.get(key).equals(apresenta.valor.split(" ")[0])){
                            position = key;
                        }
                    }

                    seekBar =  view.findViewById(R.id.seekbar_convulsao_dialog_sintoma);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        int progress = 0;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progresValue, boolean b) {
                            progress = progresValue;
                            String tmp= tempo.get(progress) +" min";
                            textTempo.setText(tmp);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    seekBar.setProgress(position);

                    enviar =  view.findViewById(R.id.btn_enviar_convulsao_dialog_sintoma);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            String horaS = time.getText().toString().trim();
                            String dataS = date.getText().toString().trim();
                            String[] data = dataS.trim().split("/");
                            String[] hora = horaS.trim().split(":");

                            calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]),
                                    Integer.valueOf(hora[0]), Integer.valueOf(hora[1]));
                              Apresenta apresenta1 = new   Apresenta(apresenta.id,apresenta.id_anjo,apresenta.id_sintoma,calendar.getTime().getTime(),textTempo.getText().toString(),null,null,null,null,null);
                            if (infoTexto.getVisibility() == View.VISIBLE) {
                                apresenta1.comentario_texto = comentario.getText().toString();
                            }
                            else if (infoAudio.getVisibility() == View.VISIBLE) {
                                apresenta1.local_audio = audioRecord.getmFileName();
                            }
                            else if (infoVideo.getVisibility() == View.VISIBLE) {
                                apresenta1.local_video = videoPath;
                            }

                            progressBarHolder.setAlpha(0.4f);
                            progressBar.setVisibility(View.VISIBLE);
                              updateApresenta(apresenta1, new Runnable() {
                                @Override
                                public void run() {
                                    progressBarHolder.setAlpha(1f);
                                    progressBar.setVisibility(View.GONE);

                                    Fragment targetFragment = getTargetFragment();
                                    if (targetFragment != null) {
                                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                    }

                                    Toast.makeText(getContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    });

                    break;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    audioRecord = new AudioRecord(getContext());
                    Toast.makeText(this.getContext(), "Agora é possível enviar sintoma!", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this.getContext(), "Acesso negado!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public String getIdSintoma() {
        String r;
        switch (this.titulo) {
            case R.id.menu_item_alimentacao:
                r = "0";
                break;
            case R.id.menu_item_sono:
                r = "1";
                break;
            case R.id.menu_item_mudanca:
                r = "2";
                break;
            case R.id.menu_item_doenca:
                r = "3";
                break;
            case R.id.menu_item_choro:
                r = "4";
                break;
            case R.id.menu_item_convulsao:
                r = "5";
                break;
            case R.id.menu_item_agressividade:
                r = "6";
                break;
            default:
                r = "7";
                break;
        }
        return r;
    }

    public void deleteMedia () {
        String aperte="Aperte para filmar";
        String pressione="Pressione para gravar";
        textVideo.setText(aperte);
        textAudio.setText(pressione);
        comentario.setText(null);
        File audio = new File(audioRecord.getmFileName());
        if (audio.exists()) {

            boolean excluiu=audio.delete();
            if (excluiu){
                Log.d("Excluiu", String.valueOf(true));
            }
        }
        if (videoPath != null) {
            File video = new File(videoPath);
            if (video.delete()) {
                Objects.requireNonNull(getContext()).getContentResolver().delete(videoUri,null,null);
                videoPath = null;
                videoUri = null;
            }
        }
    }

    public void dispatchTakeVideoIntent() {
        if (Objects.requireNonNull(getContext()).getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d("Video","has Camera");
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                Log.d("Video","has video cam app");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("Video","onActivityResult: requestCode="+requestCode+" resultCode="+resultCode);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            String[] proj = { MediaStore.Video.Media.DATA };
            Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(videoUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            videoPath = cursor.getString(column_index);

            Log.d("Video","videoUri is saved");
            Log.d("Video",videoUri.toString());

            Log.d("Video","name is "+"video");
            String[] path = videoPath.split("/");
            textVideo.setText(path[path.length-1]);
            infoVideo.setVisibility(View.VISIBLE);
            cursor.close();
        }
    }

    @SuppressLint("UseSparseArrays")
    private void createHashMapAlimentacao() {
        alimentacao = new HashMap<>();
        alimentacao.put(0, 0.0);
        alimentacao.put(1, 37.5);
        alimentacao.put(2, 38.0);
        alimentacao.put(3, 38.5);
        alimentacao.put(4, 39.0);
        alimentacao.put(5, 39.5);
        alimentacao.put(6, 40.0);
        alimentacao.put(7, 40.5);
    }

    @SuppressLint("UseSparseArrays")
    private void createHashMapTempo() {
        tempo = new HashMap<>();
        tempo.put(0, "0");
        tempo.put(1, "<1");
        tempo.put(2, "1-2");
        tempo.put(3, "3-5");
        tempo.put(4, ">5");
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}
