package com.example.johnw.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by JohnWhisker on 3/13/16.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {
   public CommentAdapter(Context context, List<Comment> objects) {

        super(context,android.R.layout.simple_list_item_1,objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment1 = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
        }
        TextView timestamnp = (TextView)convertView.findViewById(R.id.tvTime);
        ImageView userpicture = (ImageView) convertView.findViewById(R.id.ivcommentAva);
        TextView usercomment = (TextView) convertView.findViewById(R.id.tvCommentUser);
        TextView comment = (TextView) convertView.findViewById(R.id.tvComment);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        usercomment.setText(comment1.username);
        timestamnp.setText("\uD83D\uDD52 " + android.text.format.DateUtils.getRelativeTimeSpanString(comment1.created_time * 1000).toString());
        comment.setText(comment1.comment);
        setTags(comment,comment1.comment);
        Picasso.with(getContext()).load(comment1.avaurl).fit().transform(transformation).into(userpicture);
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
