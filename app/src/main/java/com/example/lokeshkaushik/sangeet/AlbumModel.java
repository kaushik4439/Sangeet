package com.example.lokeshkaushik.sangeet;

import android.provider.MediaStore;

/**
 * Created by Lokesh Kaushik on 26-Aug-16.
 */
public class AlbumModel {

    private long _id ;
    private String album_name;
    private String artist ;
    private String albumart;
    private String tracks;


    public AlbumModel(long id,String album_name,String artist,String albumart){
        this._id = id;
        this.album_name = album_name;
        this.artist = artist;
        this.albumart = albumart;

    }
    public long getAlbumId() {
        return _id;
    }

    public String getAlbumName() {
        return album_name;
    }

    public String getAlbumArtist() {
        return artist;
    }

    public String getAlbumArt() {
        return albumart;
    }

    public String getTracks() {
        return tracks;
    }


}
