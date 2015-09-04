package com.bilanchuk.alexandr.dictaphone;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Dictaphone extends AppCompatActivity {

    private static String mFileName = null;
    private static final String RECORD_FOLDER = "/ABrecords";

    private File recordFolder;

    private Button mRecordButton;
    private Button mPlayButton;
    private Button mStopButton;
    private SeekBar seekBar;
    private ListView playListView;
    private TextView mediaTime;

    private Recorder recorder;
    private Player player;
    private PlayList playList;
    private OnButtonClickListner onButtonClickListner;

    public boolean isRecordStart = true;



    public Dictaphone() {
        mFileName = generateFileName();
    }

   //при кожному записі файла генерується назва залежно від поточного часу
    public String generateFileName() {

        String audioFileName;
        recordFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + RECORD_FOLDER);
        if (!recordFolder.exists()) {
            recordFolder.mkdir();
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        audioFileName = recordFolder.getAbsolutePath() + "/audio" + date + ".mp3";

        return audioFileName;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.dictaphone);
        onButtonClickListner=new OnButtonClickListner();

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        mediaTime=(TextView)findViewById(R.id.time_play);

        playListView = (ListView) findViewById(R.id.play_list);
        playList = new PlayList(playListView, this);
        playList.loadList(recordFolder);

        mStopButton=(Button)findViewById(R.id.stop_btn);
        mPlayButton = (Button) findViewById(R.id.play_btn);
        mRecordButton = (Button) findViewById(R.id.rec_btn);

        mStopButton.setOnClickListener(onButtonClickListner);
        mPlayButton.setOnClickListener(onButtonClickListner);
        mRecordButton.setOnClickListener(onButtonClickListner);


        player = new Player(mPlayButton, seekBar,mediaTime);
        recorder = new Recorder(this,mRecordButton,mediaTime);


    }

    @Override
    public void onPause() {
        super.onPause();
        if (recorder != null) {
            recorder.stop();
        }
        if (player != null) {
            player.stop();
        }
    }




    class OnButtonClickListner implements OnClickListener {

        @Override
        public void onClick(View view) {
            int buttonId=view.getId();
            switch (buttonId){
                case R.id.play_btn:{
                    player.start(playList.getCurrentRecord());
                    break;
                }
                case R.id.rec_btn:{
                    onRecord();
                    break;
                }
                case R.id.stop_btn:{
                    player.stop();
                    break;
                }
            }
        }



        private void onRecord(){
            if (isRecordStart) {
                mFileName = generateFileName();
                recorder.start(mFileName);

            } else {
                recorder.stop();
                playList.updateList(new File(mFileName));
            }
            isRecordStart = !isRecordStart;
        }
    }
}