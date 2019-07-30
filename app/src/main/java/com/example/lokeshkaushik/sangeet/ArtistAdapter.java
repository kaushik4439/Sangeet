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
 * Created by Lokesh Kaushik on 01-Sep-16.
 */
public class ArtistAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<ArtistModel> songList;
    private Context context;
    public ArtistAdapter(ArrayList<ArtistModel> songs, Context context){
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
        LinearLayout artistLayout = (LinearLayout) layoutInflater.inflate(R.layout.artistlistitem,parent,false);
        TextView artistName = (TextView) artistLayout.findViewById(R.id.artist_name);
        TextView noOfSongs = (TextView) artistLayout.findViewById(R.id.no_of_songs);
        ImageView artistImage = (ImageView) artistLayout.findViewById(R.id.artist_image);
        ArtistModel currentSong =  songList.get(position);
        artistName.setText( currentSong.getArtistName());
        noOfSongs.setText(currentSong.getSongCount()+ " Song");
        artistImage.setImageResource(R.mipmap.ic_launcher);
        artistLayout.setTag(position);
        return artistLayout;
    }

}
