package com.lincs.mobcare.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AudioRecord extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecord";
    private static String mFileName = null;
    private long previousTime = 0;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private Context context;

    public AudioRecord(){}
    public AudioRecord(Context context) {
        this.context = context;
        mFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
        File file = new File(mFileName, "MobCare");
        if (!file.exists()) {
            if (file.mkdir()){
                Log.d(LOG_TAG, "Folder created!");
            }
            else{
                Log.d(LOG_TAG, "Problem creating folder!");
            }
        }
        mFileName += "/MobCare/";

        Log.d(LOG_TAG, "AudioRecord Constructor");
    }

    public void startTime() {
        previousTime = System.currentTimeMillis();
    }

    public long countTime() {
        long time = System.currentTimeMillis();
        long aux = time - previousTime;

        Log.d("AudioRecord", "time: " + Long.toString(time - previousTime));
        previousTime = time;

        return aux;
    }

    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        mFileName += formatter.format(now) + ".3gp";
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, "stop() failed");
            mRecorder.reset();
        }
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
    }

    public String getmFileName() {
        return mFileName;
    }

    public static void setmFileName(String mFileName) {
        AudioRecord.mFileName = mFileName;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public MediaPlayer getMPlayer() {
        return mPlayer;
    }
}