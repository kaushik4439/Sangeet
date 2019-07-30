package com.example.lokeshkaushik.sangeet;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlbumSongs extends AppCompatActivity {
    private ArrayList<SongData> songList;
    Context applicationContext = MainActivity.getContextOfApplication();
    private ListView listView ;
    private  SongListAdapter songListAdapter;
    private MusicService musicSrv;
    private Intent playIntent;
    private int songIndex;
    private boolean musicBound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);
        Intent intent = getIntent();
        long albumId = intent.getLongExtra("albumId", 1);
        String albumName = intent.getStringExtra("albumName");
        listView = (ListView) findViewById(R.id.listView);
        songList = new ArrayList<>();
        songList = getAlbumSongs(albumId);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setTitle(albumName);
        bar.setDisplayHomeAsUpEnabled(true);

        songListAdapter = new SongListAdapter(songList,this);
        listView.setAdapter(songListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                musicSrv.setList(songList);
                songIndex = position;
                musicSrv.playSong(songIndex);

            }
        });

    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list

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
        if(playIntent == null) {
            playIntent = new Intent(applicationContext, MusicService.class);
            applicationContext.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            applicationContext.startService(playIntent);
        }
    }


    private final Cursor makeAlbumSongCursor(final Long albumId) {
        ContentResolver contentResolver = applicationContext.getContentResolver();
        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.ALBUM_ID + "=" + albumId);
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM,
                        MediaStore.Audio.AlbumColumns.ALBUM_ID
                }, selection.toString(), null, MediaStore.Audio.Media.TRACK + ", " + MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public ArrayList<SongData> getAlbumSongs(final Long albumId) {
        ArrayList<SongData> mSongList = new ArrayList<SongData>();
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Cursor mCursor = makeAlbumSongCursor(albumId);
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                final long id = mCursor.getLong(0);
                final String songName = mCursor.getString(1);
                final String artist = mCursor.getString(2);
                final String album = mCursor.getString(3);
                long albumid = mCursor.getLong(4);
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumid);
                final SongData song = new SongData( songName, artist,id,albumArtUri.toString(), album);
                mSongList.add(song);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mSongList;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }




}
