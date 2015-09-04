package com.bilanchuk.alexandr.dictaphone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alexandr on 03.09.2015.
 */
public class AudioListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<File> fileArrayList;

    private TextView recordNameView;

    public AudioListAdapter(Context context,ArrayList<File> fileArrayList){
        this.context=context;
        this.fileArrayList=fileArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= layoutInflater.inflate(R.layout.play_list_item, null);
        recordNameView=(TextView)convertView.findViewById(R.id.record_name);
        String recName=fileArrayList.get(position).getName();
        recordNameView.setText((position+1)+". "+recName);
        return convertView;
    }

    public void updateAdapterList(File file){
        fileArrayList.add(file);
        notifyDataSetChanged();
    }
}
