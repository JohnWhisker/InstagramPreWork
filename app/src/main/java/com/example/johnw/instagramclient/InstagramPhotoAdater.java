package com.example.johnw.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by johnw on 3/12/2016.
 */
public class InstagramPhotoAdater extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotoAdater(Context context, List<InstagramPhoto> objects) {

        super(context,android.R.layout.simple_list_item_1,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
        }
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvlike = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        tvTime.setText("\uD83D\uDD52 " + DateUtils.getRelativeTimeSpanString(photo.datetime * 1000).toString());
        ImageView ivAvartar = (ImageView) convertView.findViewById(R.id.ivAvatar);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        tvCaption.setText(photo.caption);
        setTags(tvCaption,photo.caption);
        tvlike.setText("\uD83D\uDC99 " + String.valueOf(photo.likeCount) + " likes");
        tvUserName.setText(photo.username);
        ivAvartar.setImageResource(0);
        ivPhoto.setImageResource(0);
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.Loading);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(getContext()).load(photo.imageurl).into(ivPhoto, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
        public void onError(){

            }
        });
        Picasso.with(getContext()).load(photo.avaurl).fit().transform(transformation).into(ivAvartar);

        return convertView;
    }
    public void setTags(TextView pTextView, String pTagString) {
        SpannableString string = new SpannableString(pTagString);

        int start = -1;
        for (int i = 0; i < pTagString.length(); i++) {
            if (pTagString.charAt(i) == '#'|| pTagString.charAt(i) == '@') {
                start = i;
            } else if (pTagString.charAt(i) == ' ' || (i == pTagString.length() - 1 && start != -1)) {
                if (start != -1) {
                    if (i == pTagString.length() - 1) {
                        i++; // case for if hash is last word and there is no
                        // space after word
                    }

                    final String tag = pTagString.substring(start, i);
                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // link color
                            ds.setColor(Color.parseColor("#1a75ff"));
                            ds.setUnderlineText(false);
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = -1;
                }
            }
        }

        pTextView.setMovementMethod(LinkMovementMethod.getInstance());
        pTextView.setText(string);
    }
}
