package com.example.lokeshkaushik.sangeet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lokesh Kaushik on 20-Aug-16.
 */
public class SongListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<SongData> songList;
    private Context context;
    public SongListAdapter(ArrayList<SongData> songs, Context context){
        layoutInflater = LayoutInflater.from(context);
        songList = songs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLayout = (LinearLayout) layoutInflater.inflate(R.layout.songlistitem,parent,false);
        TextView artistView = (TextView) songLayout.findViewById(R.id.song_artist);
        TextView titleView = (TextView) songLayout.findViewById(R.id.song_title);
        ImageView songImage = (ImageView) songLayout.findViewById(R.id.song_image);
        SongData currentSong =  songList.get(position);
        artistView.setText( currentSong.getArtist());
        titleView.setText(currentSong.getTitle());
        Picasso.with(context).load(currentSong.getAlbumid()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(songImage);
        songLayout.setTag(position);
        return songLayout;
    }


}