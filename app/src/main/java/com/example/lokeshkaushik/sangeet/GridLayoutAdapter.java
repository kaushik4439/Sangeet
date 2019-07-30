package com.example.lokeshkaushik.sangeet;

/**
 * Created by Lokesh Kaushik on 26-Aug-16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridLayoutAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    private ArrayList<AlbumModel> albumList;
    private Context context;
    public GridLayoutAdapter(ArrayList<AlbumModel> albums, Context context){
        layoutInflater = LayoutInflater.from(context);
        albumList = albums;
        this.context = context;
    }

    @Override
    public int getCount() {
        return albumList.size();
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
        LinearLayout albumLayout = (LinearLayout) layoutInflater.inflate(R.layout.gridlistitem,parent,false);
        TextView albumName = (TextView) albumLayout.findViewById(R.id.album_name);
        ImageView albumImage = (ImageView) albumLayout.findViewById(R.id.grid_album);
        AlbumModel currentAlbum = albumList.get(position);
        albumName.setText(currentAlbum.getAlbumName());
        Picasso.with(context).load(currentAlbum.getAlbumArt()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(albumImage);
        return albumLayout;
    }


}
