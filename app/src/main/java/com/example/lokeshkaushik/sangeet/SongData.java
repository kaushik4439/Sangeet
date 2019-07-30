package com.example.lokeshkaushik.sangeet;

/**
 * Created by Lokesh Kaushik on 20-Aug-16.
 */
public class SongData {
    private String title;
    private String artist;
    private long id;
    private String albumid;
    private String albumName;

    public SongData(String title, String artist, long id, String albumid, String albumName) {
        this.title = title;
        this.artist = artist;
        this.id = id;
        this.albumid = albumid;
        this.albumName = albumName;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbumName() {
        return albumName;
    }

    public long getId() {
        return id;
    }

    public String getAlbumid() {
        return albumid;
    }
}

