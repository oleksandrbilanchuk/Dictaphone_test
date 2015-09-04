package com.bilanchuk.alexandr.dictaphone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Alexandr on 03.09.2015.
 */
public class Recorder implements AudioInterface {

    private MediaRecorder mediaRecorder;
    protected Button recordButton;
    private TextView timeTextView;
    private Context context;

    private int recTime = 0;
    private boolean count = true;
    private boolean isRecord = true;

    private Drawable recIconWhite;
    private Drawable recIconRed;

    private static final String LOG_TAG = "AudioRecordTest";

    public Recorder(Context context, Button recordButton, TextView timeTextView) {
        this.recordButton = recordButton;
        this.context = context;
        this.timeTextView = timeTextView;
        recIconWhite = context.getResources().getDrawable(R.drawable.ic_mic_white_36dp);
        recIconRed = context.getResources().getDrawable(R.drawable.ic_mic_red_36dp);
    }

    @Override
    public void start(String audioFileName) {
        isRecord = true;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setOutputFile(audioFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mediaRecorder.start();
        recordButton.setText("Stop");
        recordButton.postDelayed(recordAnim, 1000);
    }


    @Override
    public void stop() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            recordButton.setText("Rec");
            isRecord = false;
            mediaRecorder = null;
            recTime = 0;
            recordButton.setCompoundDrawablesWithIntrinsicBounds(recIconWhite, null, null, null);
        }


    }
    //обновляється час запису і анімацію в новому потоці
    private Runnable recordAnim = new Runnable() {

        @Override
        public void run() {
            if (isRecord) {
                if (count) {
                    recordButton.setCompoundDrawablesWithIntrinsicBounds(recIconRed, null, null, null);
                } else {
                    recordButton.setCompoundDrawablesWithIntrinsicBounds(recIconWhite, null, null, null);
                }
                count = !count;
                recTime += 1000;
                updateTime(recTime);
                recordButton.postDelayed(recordAnim, 1000);
            }
        }
    };


    private void updateTime(int recTime) {

        int cSeconds = (int) (recTime / 1000) % 60;
        int cMinutes = (int) ((recTime / (1000 * 60)) % 60);
        int cHours = (int) ((recTime / (1000 * 60 * 60)) % 24);

        timeTextView.setText(String.format("%02d:%02d:%02d ", cHours, cMinutes,
                cSeconds));


    }

}
