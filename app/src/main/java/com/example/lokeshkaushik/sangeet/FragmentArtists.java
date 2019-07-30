package com.example.lokeshkaushik.sangeet;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentArtists extends android.support.v4.app.Fragment {
    Context applicationContext = MainActivity.getContextOfApplication();
    private ListView artistListView;
    private ArrayList<ArtistModel> list;
    private ArtistAdapter artistAdapter;
    public FragmentArtists() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_artists, container, false);
        list = new ArrayList<ArtistModel>();
        artistListView = (ListView) rootView.findViewById(R.id.listView2);
        list = getArtists();
        artistAdapter = new ArtistAdapter(list,getActivity());
        artistListView.setAdapter(artistAdapter);
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                long artistId =  list.get(position).getId();
                String artistName = list.get(position).getArtistName();
                Intent intent = new Intent(FragmentArtists.this.getActivity(), ArtistSongs.class);
                intent.putExtra("EXTRA",artistId);
                intent.putExtra("artistName",artistName);
                startActivity(intent);
            }
        });
        return rootView;
    }
    private final Cursor makeArtistCursor() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        return contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[] {BaseColumns._ID, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS
                }, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }

    public ArrayList<ArtistModel> getArtists() {
        ArrayList<ArtistModel> mArtistsList = new ArrayList<ArtistModel>();
        Cursor mCursor = makeArtistCursor();
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                final long id = mCursor.getLong(0);
                final String artistName = mCursor.getString(1);
                final int songCount = mCursor.getInt(2);
                final ArtistModel artist = new ArtistModel(id, artistName, songCount);
                mArtistsList.add(artist);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mArtistsList;
    }



}
