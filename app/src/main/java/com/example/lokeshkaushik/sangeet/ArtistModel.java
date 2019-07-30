package com.example.lokeshkaushik.sangeet;

/**
 * Created by Lokesh Kaushik on 01-Sep-16.
 */
public class ArtistModel {
    private long id;
    private String artistName;
    private int songCount;

    public ArtistModel(long id, String artistName,int songCount){
        this.id = id;
        this.artistName = artistName;
        this.songCount = songCount;

    }

    public long getId() {
        return id;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getSongCount() {
        return songCount;
    }
}
