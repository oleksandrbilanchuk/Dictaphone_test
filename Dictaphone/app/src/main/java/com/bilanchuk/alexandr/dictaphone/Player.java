package com.bilanchuk.alexandr.dictaphone;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Alexandr on 03.09.2015.
 */
public class Player implements AudioInterface {

    private MediaPlayer mediaPlayer = null;
    private Button playButton;
    private SeekBar playingBar;
    private TextView mediaTime;

    private boolean isAudioPlay;
    private int pauseLenght;
    private int timePlay = 0;
    private int totalTimePlay = 0;

    public Player(Button playButton, SeekBar playingBar, TextView mediaTime) {
        this.playingBar = playingBar;
        this.playButton = playButton;
        this.mediaTime = mediaTime;
    }

    @Override
    public void start(String audioFileName) {
// якщо стартує новий аудіозапис, попередній зупиняється і очищується память
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isAudioPlay = false;
        }
        isAudioPlay = true;

        //створення і підготовка обєкту для відтворення аудиозапису
        mediaPlayer = new MediaPlayer();
        //задаються початкові параметри seekBar
        mediaPlayer.setOnPreparedListener(new OnMediaPrepareListner());
        try {
            mediaPlayer.setDataSource(audioFileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playingBar.postDelayed(updateSeekBar, 1000);

        } catch (IOException e) {
            Log.e("Exception", "prepare() failed");
        } catch (NullPointerException e) {
            mediaPlayer.release();
            mediaPlayer=null;
            Log.e("Exception", "No file");
        }
    }


    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            pauseLenght = mediaPlayer.getDuration();
        }
    }

    public void continuePlay() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.seekTo(pauseLenght);
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }

    }


    @Override
    public void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.release();
                mediaPlayer = null;
                isAudioPlay = false;
                playingBar.setProgress(0);
                totalTimePlay = 0;
                timePlay = 0;
                updateTime();

            }

        }
    }

    // в новому потоці обновляються дані seekBar та mediaTimer
    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (isAudioPlay == true) {
                if (playingBar != null) {
                    playingBar.setProgress(mediaPlayer.getCurrentPosition());
                }
                if (mediaPlayer.isPlaying()) {
                    playingBar.postDelayed(updateSeekBar, 1000);
                    updateTime();
                } else {
                    playingBar.setProgress(0);
                    totalTimePlay = 0;
                    updateTime();
                }
            }
        }
    };

    private void updateTime() {
        if (totalTimePlay == 0) {
            timePlay = 0;
        } else {
            timePlay = mediaPlayer.getCurrentPosition();
        }

        int tSeconds = (int) (totalTimePlay / 1000) % 60;
        int tMinutes = (int) ((totalTimePlay / (1000 * 60)) % 60);
        int tHours = (int) ((totalTimePlay / (1000 * 60 * 60)) % 24);

        int cSeconds = (int) (timePlay / 1000) % 60;
        int cMinutes = (int) ((timePlay / (1000 * 60)) % 60);
        int cHours = (int) ((timePlay / (1000 * 60 * 60)) % 24);

        if (tHours == 0) {
            mediaTime.setText(String.format("%02d:%02d / %02d:%02d", cMinutes, cSeconds,
                    tMinutes, tSeconds));
        } else {
            mediaTime.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes,
                    cSeconds, tHours, tMinutes, tSeconds));
        }

    }

    // перед початком відтворення , визначається тривалість запису і задаються параметри seekbar
    class OnMediaPrepareListner implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            totalTimePlay = mediaPlayer.getDuration();
            playingBar.setMax(totalTimePlay);
            playingBar.postDelayed(updateSeekBar, 1000);
        }
    }
}
