package com.example.johnw.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by johnw on 3/14/2016.
 */
public class customGrid extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> url;
    public customGrid(Context context, List<String> objects) {

        super(context,android.R.layout.simple_list_item_1,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        for(;;){
        try{
        String picurl = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post,parent,false);
        }
        ImageView photo = (ImageView) convertView.findViewById(R.id.ivpostitem);
        Picasso.with(getContext()).load(picurl).resize(400,400).into(photo);
        return convertView;
        }catch (Exception e){}
        }
    }
}


