package com.example.lokeshkaushik.sangeet;

import android.app.Activity;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lokesh Kaushik on 31-Aug-16.
 */
public class MusicService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private ArrayList<SongData> songs;
    private int songPosn;
    long currentTime;
    long totalTime;
    String albumId;
    String artistName;
    String songName;
    private final IBinder musicBind = new MusicBinder();
    Handler handler = new Handler();
    private boolean isRepeat = false;
    private boolean isRandom = false;
    MainActivity mainActivity;


    @Override
    public void onCreate() {

        super.onCreate();
        player = new MediaPlayer();
        initMusicPlayer();

        /*SharedPreferences prefs = getSharedPreferences("songList", Context.MODE_PRIVATE);
       String httpParamJSONList = prefs.getString("songList", "");
        songPosn = new Gson().fromJson(String.valueOf(songPosn), int.class);
        ArrayList<SongData> httpParamList =
                new Gson().fromJson(httpParamJSONList, new TypeToken<List<SongData>>() {
                }.getType());
        setList(httpParamList);
        playSong(songPosn);*/
    }

    public void initMusicPlayer() {

        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<SongData> theSongs) {
        songs = theSongs;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void playSong(int songIndex) {

        player.reset();


        SongData playSong = songs.get(songIndex);
        songPosn = songIndex;

        long currSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
            player.prepare();

        } catch (IOException | IllegalArgumentException | IllegalStateException | SecurityException e) {
            e.printStackTrace();
        }
        player.start();

        updateProgress();
        sendSongDetails(playSong);


    }

    public void updateProgress() {
        handler.postDelayed(serviceRunnable, 1000);

    }

    Runnable serviceRunnable = new Runnable() {
        @Override
        public void run() {
            currentTime = player.getCurrentPosition();
            totalTime = player.getDuration();
            sendMessageToActivity(currentTime, totalTime);
            handler.postDelayed(this, 1000);
        }
    };

    private void sendMessageToActivity(long currentTime, long totalTime) {
        Intent intent = new Intent("SongPlayer");

        intent.putExtra("currentTime", currentTime);
        intent.putExtra("totalTime", totalTime);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



    private void sendSongDetails(SongData currentSong) {
        Intent intent = new Intent("SongDetails");
        albumId = currentSong.getAlbumid();
        artistName = currentSong.getArtist();
        songName = currentSong.getTitle();
        intent.putExtra("songImage", albumId);
        intent.putExtra("songName", songName);
        intent.putExtra("artistName", artistName);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void pauseSong() {
        player.pause();
    }

    public void resumeSong() {
        if (!player.isPlaying()) {
            player.start();
        }
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void playNext() {
        if (songPosn < songs.size() - 1) {
            playSong(songPosn + 1);

        } else {
            playSong(0);
            songPosn = 0;
        }
    }

    public void setRandom(){
        if(isRandom)
        {
            isRandom = false;
        }

        else
        {
            isRandom = true;
            isRepeat = false;
         }

    }

    public void setRepeat(){
        if(isRepeat)
        {
            isRepeat = false;
        }

        else
        {
            isRepeat = true;
            isRandom = false;
        }
    }
    public void playPrevious(){

        if (songPosn > 0) {
            playSong(songPosn - 1);

        } else {
            playSong(songs.size() - 1);

        }
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        player.reset();
        if (isRandom){
            Random random =new Random();
            songPosn = random.nextInt(songs.size());
            playSong(songPosn);

        }
        else if(isRepeat){
            playSong(songPosn);

        }
        else {
            if (songPosn < songs.size() - 1) {
                playSong(songPosn + 1);

            } else {
                playSong(0);

            }
        }

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

   /* @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();

        updateProgress();

    }*/




}
