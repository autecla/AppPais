package com.lincs.mobcare.evolution;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Realiza;
import com.lincs.mobcare.utils.AudioRecord;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.Snapshot;
import java.io.File;

class EvolutionAlertDialogComment{
    private Context context;
    private EvolutionFragment activity;
    private EvolutionAdapter.ViewHolder holder;

    private ImageView btnGravarAudio;
    private ImageView btnReproduzirAudio;
    private LinearLayout btnicoMic;
    private LinearLayout btnicoCam;
    private LinearLayout btnicoTxt;
    private LinearLayout info;
    private LinearLayout infoTexto;
    private RelativeLayout infoAudio;
    private RelativeLayout infoVideo;
    private TextView textAudio;
    private TextView textVideo;
    private EditText comentario;
    private AudioRecord audioRecord;
    private boolean mStartRecording = true;
    private boolean mStartPlaying = true;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private Uri videoUri;
    private String videoPath;

    private Realiza realiza;

    EvolutionAlertDialogComment(final Context context, EvolutionFragment activity, final EvolutionAdapter.ViewHolder holder,
                                final Realiza realiza) {
        this.context = context;
        this.activity = activity;
        this.holder = holder;
        this.realiza = realiza;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        if (realiza.comentario_texto != null || (realiza.local_audio != null && realiza.comentario_audio != null) ||
                (realiza.local_video != null && realiza.comentario_video != null)) {
            alertDialogBuilder.setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialogBuilder.setNegativeButton("REMOVER", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    realiza.comentario_texto = null;
                    realiza.comentario_audio=null;
                    realiza.comentario_video=null;
                    realiza.local_video = null;
                    realiza.local_audio = null;

                    Firebase.updateRealiza(realiza, new Runnable() {
                        @Override
                        public void run() {
                            String comentar="Comentar";
                            holder.comment.setText(comentar);
                            holder.comment_2.setText(comentar);
                            holder.comment_image.setVisibility(View.GONE);
                            holder.comment_image_2.setVisibility(View.GONE);
                            Toast.makeText(context,"Removido com sucesso!",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        alertDialogBuilder.setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {

                realiza.status = holder.evolutionCheckBox.isChecked();

                if (infoTexto.getVisibility() == View.VISIBLE) {
                    realiza.comentario_texto = comentario.getText().toString();
                }
                else if (infoAudio.getVisibility() == View.VISIBLE) {
                    realiza.local_audio = audioRecord.getmFileName();
                }
                else if (infoVideo.getVisibility() == View.VISIBLE) {
                    realiza.local_video = videoPath;
                }
                Firebase.updateRealiza(realiza, new Runnable() {
                    @Override
                    public void run() {
                        String edit_remove="Editar/Remover";
                        holder.comment.setText(edit_remove);
                        holder.comment_2.setText(edit_remove);
                        holder.comment_image.setVisibility(View.VISIBLE);
                        holder.comment_image_2.setVisibility(View.VISIBLE);
                        Toast.makeText(context,"Enviado com sucesso!",Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });

            }
        });
        alertDialogBuilder.setTitle("Envie um comentário");
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.cyan_500));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.cyan_500));
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.cyan_500));
            }
        });
        View v = holder.inflater.inflate(R.layout.activity_video_comment, null);
        alertDialog.setView(v);//add your own xml with defied with and height of videoview
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        audioRecord = new AudioRecord(context);

        btnicoTxt = alertDialog.findViewById(R.id.comentar_dialog_sintoma);
        btnicoMic =  alertDialog.findViewById(R.id.icone_gravar_audio_dialog_sintoma);
        btnicoCam =  alertDialog.findViewById(R.id.icone_gravar_video_dialog_sintoma);
        comentario =  alertDialog.findViewById(R.id.comentario_dialog_sintoma);
        infoTexto =  alertDialog.findViewById(R.id.informação_comentario_dialog_sintoma);
        btnGravarAudio =  alertDialog.findViewById(R.id.gravar_audio_dialog_sintoma);
        btnReproduzirAudio = alertDialog.findViewById(R.id.reproduzir_audio_dialog_sintoma);
        ImageView btnGravarVideo =  alertDialog.findViewById(R.id.gravar_video_dialog_sintoma);
        ImageView btnReproduzirVideo =  alertDialog.findViewById(R.id.reproduzir_video_dialog_sintoma);
        info =  alertDialog.findViewById(R.id.informacao_dialog_sintoma);
        infoAudio =  alertDialog.findViewById(R.id.informacao_audio_dialog_sintoma);
        infoVideo = alertDialog.findViewById(R.id.informacao_video_dialog_sintoma);
        ImageView btnCan =  alertDialog.findViewById(R.id.icone_cancelar);
        ImageView btnCanMic = alertDialog.findViewById(R.id.icone_cancelar_audio);
        ImageView btnCanCam = alertDialog.findViewById(R.id.icone_cancelar_video);
        textAudio =  alertDialog.findViewById(R.id.nome_audio_dialog_sintoma);
        textVideo = alertDialog.findViewById(R.id.nome_video_dialog_sintoma);

        videoUri = realiza.local_video != null ? Uri.fromFile(new File(realiza.local_video)) : null;
        videoPath = realiza.local_video;

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
        if (!holder.comment.getText().toString().equals("Editar/Remover")&&!holder.comment_2.getText().toString().equals("Editar/Remover")) {
            btnCan.setOnClickListener(onClickListener);
            btnCanMic.setOnClickListener(onClickListener);
            btnCanCam.setOnClickListener(onClickListener);
        }
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
                    Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
                    intent.setDataAndType(videoUri, "video/mp4");
                    context.startActivity(intent);
                }
                else {
                    Log.d("URI_Invalid","Uri Inválida!");
                }
            }
        });

        commented();
    }

    private void commented() {
        if (realiza.comentario_texto != null) {
            btnicoTxt.callOnClick();
            comentario.setText(realiza.comentario_texto);
        }
        else if (realiza.local_audio != null) {
            btnicoMic.callOnClick();
            String[] path = realiza.local_audio.split("/");
            textAudio.setText(path[path.length-1]);
            AudioRecord.setmFileName(realiza.local_audio);
        }
        else if (realiza.local_video != null) {
            btnicoCam.callOnClick();
            String[] path = realiza.local_video.split("/");
            textVideo.setText(path[path.length-1]);
            videoUri = Uri.parse(realiza.local_video);
        }
    }

    private void deleteMedia() {
        String aperte="Aperte para filmar";
        String press="Pressione para gravar";
        textVideo.setText(aperte);
        realiza.local_audio=null;
        realiza.local_video=null;
        realiza.comentario_texto=null;
        textAudio.setText(press);
        comentario.setText(null);
        File audio = new File(audioRecord.getmFileName());
        if (audio.exists()) {
            boolean deleted=audio.delete();
            Log.d("Audio delete", String.valueOf(deleted));
        }
        if (videoPath != null) {
            /*File video = new File(videoPath);
            if (video.exists()) {
                if (video.delete()) {
                    context.getContentResolver().delete(videoUri, null, null);
                    videoPath = null;
                    videoUri = null;
                }
            }else {*/
                videoPath = null;
                videoUri = null;
            //}
        }
    }

    private void dispatchTakeVideoIntent() {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d("Video","has Camera");
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(context.getPackageManager()) != null) {
                Snapshot.setId_realiza_activity(realiza.id);
                activity.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                Log.d("Video","has video cam app");
            }
        }
    }
}
