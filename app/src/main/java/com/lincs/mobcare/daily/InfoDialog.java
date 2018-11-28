package com.lincs.mobcare.daily;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Apresenta;
import com.lincs.mobcare.utils.AudioRecord;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.Snapshot;
import com.lincs.mobcare.utils.TimeUtils;
import com.lincs.mobcare.utils.VideoFromFirebaseActivity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.view.Window.FEATURE_NO_TITLE;

public class InfoDialog extends DialogFragment {

    private String id;
    private int tipo;
    private ImageView btnGravarAudio;
    private ImageView btnReproduzirAudio;
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
    private Uri videoUri = null;
    private String videoPath = null;

    public static InfoDialog newInstance(String id) {
        InfoDialog fragment = new InfoDialog();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.info_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Apresenta apresenta = Snapshot.getApresenta(id);

        switch (apresenta.id_sintoma) {
            case "0":
                tipo = R.id.menu_item_alimentacao;
                break;
            case "1":
                tipo = R.id.menu_item_sono;
                break;
            case "2":
                tipo = R.id.menu_item_mudanca;
                break;
            case "3":
                tipo = R.id.menu_item_doenca;
                break;
            case "4":
                tipo = R.id.menu_item_choro;
                break;
            case "5":
                tipo = R.id.menu_item_convulsao;
                break;
            case "6":
                tipo = R.id.menu_item_agressividade;
                break;
            case "7":
                tipo = R.id.menu_item_outro;
                break;
        }

        //Recuperar

        Button cancelar = view.findViewById(R.id.btn_cancelar_dialog_info);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button editar = view.findViewById(R.id.btn_editar_dialog_info);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //A partir do tipo chamar o layout para edição correspondente
                assert getFragmentManager() != null;
                switch (tipo) {

                    case R.id.menu_item_alimentacao:
                        SintomaDialogFragment hdf = SintomaDialogFragment.newInstance(R.layout.febre_dialog, R.id.menu_item_alimentacao, id, true);
                        hdf.setTargetFragment(getTargetFragment(), 0);

                        hdf.show(getFragmentManager(), "febre_dialog");
                        break;
                    case R.id.menu_item_sono:
                        SintomaDialogFragment hdv = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_sono, id, true);
                        hdv.setTargetFragment(getTargetFragment(), 1);
                        hdv.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                        break;
                    case R.id.menu_item_mudanca:
                        SintomaDialogFragment hdg = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_mudanca, id, true);
                        hdg.setTargetFragment(getTargetFragment(), 2);
                        hdg.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                        break;
                    case R.id.menu_item_doenca:
                        SintomaDialogFragment hden = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_doenca, id, true);
                        hden.setTargetFragment(getTargetFragment(), 3);
                        hden.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                        break;
                    case R.id.menu_item_choro:
                        SintomaDialogFragment hdc = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_choro, id, true);
                        hdc.setTargetFragment(getTargetFragment(), 4);
                        hdc.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                        break;
                    case R.id.menu_item_convulsao:
                        SintomaDialogFragment hdc2 = SintomaDialogFragment.newInstance(R.layout.convulsao_dialog, R.id.menu_item_convulsao, id, true);
                        hdc2.setTargetFragment(getTargetFragment(), 5);
                        hdc2.show(getFragmentManager(), "convulsao_dialog");
                        break;

                    case R.id.menu_item_agressividade:
                        SintomaDialogFragment hde = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_agressividade, id, true);
                        hde.setTargetFragment(getTargetFragment(), 6);
                        hde.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                        break;

                    case R.id.menu_item_outro:

                        SintomaDialogFragment hdout = SintomaDialogFragment.newInstance(R.layout.outros_sintomas_dialog, R.id.menu_item_outro, id, true);
                        hdout.setTargetFragment(getTargetFragment(), 7);
                        hdout.show(getFragmentManager(), "outros_sintomas_dialog");
                        break;
                }
                dismiss();
            }
        });

        Button remover = view.findViewById(R.id.btn_remover_dialog_info);
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Remover");
                builder.setMessage("Tem certeza que deseja remover?");
                builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Firebase.removeApresenta(id);

                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment != null) {
                            targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                        }

                        dismiss();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(apresenta.data));
        View linearValor = view.findViewById(R.id.valor_dialog_info);

        TextView date = view.findViewById(R.id.data_dialog_info);
        date.setText(TimeUtils.convertDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)).substring(0,10));
        TextView time = view.findViewById(R.id.time_dialog_info);
        time.setText(TimeUtils.convertHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        audioRecord = new AudioRecord(getContext());
        LinearLayout btnicoTxt = view.findViewById(R.id.comentar_dialog_sintoma);
        LinearLayout btnicoMic = view.findViewById(R.id.icone_gravar_audio_dialog_sintoma);
        LinearLayout btnicoCam = view.findViewById(R.id.icone_gravar_video_dialog_sintoma);
        comentario = view.findViewById(R.id.comentario_dialog_sintoma);
        infoTexto =view.findViewById(R.id.informação_comentario_dialog_sintoma);
        btnGravarAudio = view.findViewById(R.id.gravar_audio_dialog_sintoma);
        btnReproduzirAudio =view.findViewById(R.id.reproduzir_audio_dialog_sintoma);
        ImageView btnGravarVideo = view.findViewById(R.id.gravar_video_dialog_sintoma);
        ImageView btnReproduzirVideo = view.findViewById(R.id.reproduzir_video_dialog_sintoma);
        info =view.findViewById(R.id.informacao_dialog_sintoma);
        infoAudio = view.findViewById(R.id.informacao_audio_dialog_sintoma);
        infoVideo = view.findViewById(R.id.informacao_video_dialog_sintoma);
        ImageView btnCan = view.findViewById(R.id.icone_cancelar);
        ImageView btnCanMic = view.findViewById(R.id.icone_cancelar_audio);
        ImageView btnCanCam = view.findViewById(R.id.icone_cancelar_video);
        textAudio =view.findViewById(R.id.nome_audio_dialog_sintoma);
        textVideo =  view.findViewById(R.id.nome_video_dialog_sintoma);

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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });

        btnReproduzirVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (apresenta.comentario_video!=null){
                    startActivity(new Intent(getContext(), VideoFromFirebaseActivity.class).
                            putExtra("url_video",apresenta.comentario_video));
                }
                else {
                    Toast.makeText(getContext(), "Video não encontrado", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (apresenta.comentario_texto != null) {
            btnicoTxt.callOnClick();
            comentario.setText(apresenta.comentario_texto);
            comentario.setEnabled(false);
            btnCan.setVisibility(View.GONE);
        }
        else if (apresenta.local_audio != null) {
            btnicoMic.callOnClick();
            btnGravarAudio.setEnabled(false);
            String[] path = apresenta.local_audio.split("/");
            textAudio.setText(path[path.length-1]);
            AudioRecord.setmFileName(apresenta.local_audio);
            btnCanMic.setVisibility(View.GONE);
        }
        else if (apresenta.local_video != null) {
            btnicoCam.callOnClick();
            btnGravarVideo.setEnabled(false);
            String[] path = apresenta.local_video.split("/");
            textVideo.setText(path[path.length-1]);
            videoUri = Uri.parse(apresenta.local_video);
            btnCanCam.setVisibility(View.GONE);
        }
        else {
            info.setVisibility(View.GONE);
        }

        switch (tipo) {

            case R.id.menu_item_alimentacao:
                String alimentacao = apresenta.valor;
                if (alimentacao.equals("0")) {
                    alimentacao = "Não informada";
                }
                ((TextView) view.findViewById(R.id.label_valor_dialog_info)).setText(alimentacao);
                break;

            case R.id.menu_item_sono:
                ((ImageView) view.findViewById(R.id.imagem_dialog_info)).setImageResource(R.mipmap.ic_vomito_new);
                ((TextView) view.findViewById(R.id.titulo_dialog_info)).setText(R.string.sintoma_vomito);
                (view.findViewById(R.id.barra_superior_dialog_info)).setBackgroundColor(getResources().getColor(R.color.colorSintomaVomito));
                ((ViewGroup) linearValor.getParent()).removeView(linearValor);
                break;

            case R.id.menu_item_mudanca:
                ((ImageView) view.findViewById(R.id.imagem_dialog_info)).setImageResource(R.mipmap.ic_gripe_new);
                ((TextView) view.findViewById(R.id.titulo_dialog_info)).setText(R.string.sintoma_gripe);
                (view.findViewById(R.id.barra_superior_dialog_info)).setBackgroundColor(getResources().getColor(R.color.colorSintomaTrocadeMedicacao));
                ((ViewGroup) linearValor.getParent()).removeView(linearValor);
                break;

            case R.id.menu_item_doenca:
                ((ImageView) view.findViewById(R.id.imagem_dialog_info)).setImageResource(R.mipmap.ic_engasgo_new);
                ((TextView) view.findViewById(R.id.titulo_dialog_info)).setText(R.string.sintoma_doenca);
                (view.findViewById(R.id.barra_superior_dialog_info)).setBackgroundColor(getResources().getColor(R.color.colorSintomaDoenca));
                ((ViewGroup) linearValor.getParent()).removeView(linearValor);
                break;

            case R.id.menu_item_choro:
                ((ImageView) view.findViewById(R.id.imagem_dialog_info)).setImageResource(R.mipmap.ic_choro_continuo_new);
                ((TextView) view.findViewById(R.id.titulo_dialog_info)).setText(R.string.sintoma_choro);
                (view.findViewById(R.id.barra_superior_dialog_info)).setBackgroundColor(getResources().getColor(R.color.colorSintomaChoro));
                ((ViewGroup) linearValor.getParent()).removeView(linearValor);
                break;

            case R.id.menu_item_convulsao:
                ((ImageView) view.findViewById(R.id.imagem_dialog_info)).setImageResource(R.mipmap.ic_convulsao_new);
                ((TextView) view.findViewById(R.id.titulo_dialog_info)).setText(R.string.sintoma_convulsao);
                (view.findViewById(R.id.barra_superior_dialog_info)).setBackgroundColor(getResources().getColor(R.color.colorSintomaConvulsao));
                String tempo_duracao="Tempo de Duração";
                ((TextView) view.findViewById(R.id.titulo_valor_dialog_info)).setText(tempo_duracao);
                String duracao = apresenta.valor;
                if (duracao.equals("0")) {
                    duracao = "Não informada";
                }
                ((TextView) view.findViewById(R.id.label_valor_dialog_info)).setText(duracao);
                ((TextView) view.findViewById(R.id.label_valor_dialog_info)).setText(apresenta.valor);
                break;

            case R.id.menu_item_agressividade:
                ((ImageView) view.findViewById(R.id.imagem_dialog_info)).setImageResource(R.mipmap.ic_espasmos_new);
                ((TextView) view.findViewById(R.id.titulo_dialog_info)).setText(R.string.sintoma_agressividade);
                (view.findViewById(R.id.barra_superior_dialog_info)).setBackgroundColor(getResources().getColor(R.color.colorSintomaAgressividade));
                ((ViewGroup) linearValor.getParent()).removeView(linearValor);
                break;

            case R.id.menu_item_outro:
                ((ImageView) view.findViewById(R.id.imagem_dialog_info)).setImageResource(R.mipmap.ic_convulsao_new);
                ((TextView) view.findViewById(R.id.titulo_dialog_info)).setText(R.string.outros);
                (view.findViewById(R.id.barra_superior_dialog_info)).setBackgroundColor(getResources().getColor(R.color.colorSintomaOutro));
                ((ViewGroup) linearValor.getParent()).removeView(linearValor);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                Objects.requireNonNull(getContext()).getContentResolver().delete(videoUri,null,null);
                videoPath = null;
                videoUri = null;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("Video","onActivityResult: requestCode="+requestCode+" resultCode="+resultCode);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            String[] proj = { MediaStore.Video.Media.DATA };
            try (Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(videoUri, proj, null, null,
                    null)) {
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                videoPath = cursor.getString(column_index);

                Log.d("Video", "videoUri is saved");
                Log.d("Video", videoUri.toString());

                Log.d("Video", "name is " + "video");
                String[] path = videoPath.split("/");
                textVideo.setText(path[path.length - 1]);
                infoVideo.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
