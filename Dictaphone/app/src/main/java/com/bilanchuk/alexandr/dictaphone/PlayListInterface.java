package com.bilanchuk.alexandr.dictaphone;

import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alexandr on 03.09.2015.
 */
public interface PlayListInterface {

    void loadList(File directory);

    void updateList(File file);


    String getCurrentRecord();
}
