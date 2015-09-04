package com.bilanchuk.alexandr.dictaphone;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alexandr on 03.09.2015.
 */
public class PlayList implements PlayListInterface {

    private Context context;
    private ArrayList<File> audioFileList;
    private ListView playListView;
    private AudioListAdapter audioListAdapter;
    private int indexSelectedRecord;


    public PlayList(ListView playListView, Context context) {
        this.playListView = playListView;
        this.context = context;
    }

    @Override
    public void loadList(File directory) {
        audioFileList = getFileList(directory);
        audioListAdapter = new AudioListAdapter(context, audioFileList);
        playListView.setAdapter(audioListAdapter);
        playListView.setOnItemClickListener(new OnPlayListClickListner());
        playListView.setSelector(R.drawable.list_click);

    }


    @Override
    public void updateList(File file) {
        audioListAdapter.updateAdapterList(file);
    }

    private ArrayList<File> getFileList(File directory) {
        ArrayList<File> files = new ArrayList<>();
        if (directory != null) {
            File[] filenames = directory.listFiles();
            for (File tmpf : filenames) {
                files.add(tmpf);
            }
        }
        return files;
    }


    @Override
    public String getCurrentRecord() {
        if (audioFileList.size()>0) {
            String recordPath = audioFileList.get(indexSelectedRecord).getAbsolutePath();
            return recordPath;
        } else
            return null;
    }

    class OnPlayListClickListner implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view.setSelected(true);
            indexSelectedRecord = position;
        }
    }
}
