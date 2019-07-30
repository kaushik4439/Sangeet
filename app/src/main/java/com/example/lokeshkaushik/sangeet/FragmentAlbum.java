package com.example.lokeshkaushik.sangeet;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAlbum extends android.support.v4.app.Fragment {

    private Context applicationContext = MainActivity.getContextOfApplication();
    private GridView gridView;
    private ArrayList<AlbumModel> list ;
    private GridLayoutAdapter gridLayoutAdapter;
    public FragmentAlbum() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_album, container, false);
        list = new ArrayList<AlbumModel>();
        gridView = (GridView) rootview.findViewById(R.id.gridView);
        list =  getAlbums();
        gridLayoutAdapter = new GridLayoutAdapter(list,getActivity());
        gridView.setAdapter(gridLayoutAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                long albumId =  list.get(position).getAlbumId();
                String albumName = list.get(position).getAlbumName();
                Intent intent = new Intent(FragmentAlbum.this.getActivity(), AlbumSongs.class);
                intent.putExtra("albumId",albumId);
                intent.putExtra("albumName",albumName);
                startActivity(intent);

            }
        });

        return rootview;
    }

    private final Cursor makeAlbumCursor() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        return contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {
          BaseColumns._ID, MediaStore.Audio.AlbumColumns.ALBUM, MediaStore.Audio.AlbumColumns.ARTIST, MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS,
                }, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    public ArrayList<AlbumModel> getAlbums() {
        ArrayList<AlbumModel> albumList = new ArrayList<AlbumModel>();
        Cursor mCursor;

        mCursor = makeAlbumCursor();
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                final long id = mCursor.getLong(0);
                final String albumName = mCursor.getString(1);
                final String artist = mCursor.getString(2);
                final int songCount = mCursor.getInt(3);
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri,id);
                final AlbumModel album = new AlbumModel(id, albumName, artist, albumArtUri.toString());
                albumList.add(album);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return albumList;
    }



}
