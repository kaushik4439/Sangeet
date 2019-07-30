package com.example.lokeshkaushik.sangeet;


import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSongs extends android.support.v4.app.Fragment  {


    private ListView listView ;
    public ArrayList<SongData> songList ;
    private  SongListAdapter songListAdapter;
    private int songIndex;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private RelativeLayout relativeLayout;
    private TextView currentSongName;

    Context applicationContext = MainActivity.getContextOfApplication();
    public FragmentSongs() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_songs, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_view);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relativeLayout);

        songList = new ArrayList<SongData>();
        songList = getSongList();
        songListAdapter = new SongListAdapter(songList,getActivity());
        listView.setAdapter(songListAdapter);
        listView.setItemsCanFocus(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                musicSrv.setList(songList);
                songIndex = position;
                musicSrv.playSong(songIndex);


            }
        });

        return rootView;

    }
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    public ArrayList<SongData> getSongList(){
        ArrayList<SongData> list = new ArrayList<>();
        ContentResolver contentResolver = applicationContext.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Cursor cursor = contentResolver.query(musicUri, null, null, null, null);

        if(cursor!=null && cursor.moveToFirst()) {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID);
            int albumNameColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            do {
                long id = cursor.getLong(idColumn);
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                long albumid = cursor.getLong(albumColumn);
                String albumName = cursor.getString(albumNameColumn);
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumid);
                list.add(new SongData(title,artist,id,albumArtUri.toString(),albumName));
            } while (cursor.moveToNext());
        }

        Collections.sort(list, new Comparator<SongData>() {
            @Override
            public int compare(SongData lhs, SongData rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
        return list;

    }







}
