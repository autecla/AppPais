package com.lincs.mobcare.evolution;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Video;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.Snapshot;


class EvolutionAlertDialogVideo {

    private MediaController mediaController;
    @SuppressLint("ClickableViewAccessibility")
    EvolutionAlertDialogVideo(final Context context, Video video, EvolutionAdapter.ViewHolder holder) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setNeutralButton("FECHAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        if (video!=null) {
                alertDialogBuilder.setTitle(video.arquivo);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.cyan_500));
                    }
                });
                View v = holder.inflater.inflate(R.layout.activity_video_landscape, null);

                final VideoView videoview = v.findViewById(R.id.video_view_landscape);
                final ImageView btnPlay = v.findViewById(R.id.btn_play);

                    String uri = Snapshot.getVideoFromId(video.id);
                    Uri uri_video=null;
                        try {
                            uri_video=Uri.parse(uri);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    if (uri_video!=null) {
                        videoview.setVideoURI(uri_video);
                        mediaController=new MediaController(context);
                        mediaController.setAnchorView(videoview);
                        videoview.setMediaController(mediaController);
                        videoview.requestFocus();
                        //videoview.setBackgroundColor(context.getResources().getColor(R.color.cyan_300));

                        videoview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (videoview.isPlaying()) {
                                    btnPlay.setVisibility(View.VISIBLE);
                                    videoview.pause();
                                    videoview.seekTo(0);
                                } else {
                                    btnPlay.setVisibility(View.INVISIBLE);
                                    videoview.start();
                                }
                            }
                        });
                        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setVolume(30, 30);
                            }
                        });

                        alertDialog.setView(v);//add your own xml with defied with and height of videoview
                        alertDialog.show();

                        btnPlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!videoview.isPlaying()) {
                                    btnPlay.setVisibility(View.INVISIBLE);
                                    videoview.start();
                                }
                                else{
                                    btnPlay.setVisibility(View.VISIBLE);
                                    videoview.pause();
                                }
                            }
                        });
                    }
                    else {
                        Log.d("Carregando video","Carregando");
                        Toast.makeText(context,"Carregando vídeo! Atualize a página",Toast.LENGTH_LONG).show();
                    }
        }
    }
}
