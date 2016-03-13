package com.example.johnw.instagramclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class comment_activity extends AppCompatActivity {

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<Comment> Comment;
    String id;
    InstagramPhoto thismedia;
    private commentAdapter AComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_activity);
        thismedia = new InstagramPhoto();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = null;
            } else {
                id = extras.getString("id");
            }
        }
        Comment = new ArrayList<Comment>();
        AComment = new commentAdapter(this, Comment);
        ListView lvcomment = (ListView) findViewById(R.id.lvComment);
        lvcomment.setAdapter(AComment);
        fletchComment();
        mediaInfomation();

    }

    public void mediaInfomation(){
        String url = "https://api.instagram.com/v1/media/"+id+"?client_id="+CLIENT_ID;
        AsyncHttpClient client2 = new AsyncHttpClient();
        Log.d("debug", "url 1 " + url);
        client2.get(url,null,new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray PhotosJson = null;
                try {

                   // PhotosJson = response.getJSONArray("data");

                   // Log.d("debug", "JSON " + PhotosJson.toString());
                    JSONObject photoJ = response.getJSONObject("data");
                    Log.d("debug", "JSON " + photoJ.toString());
                    thismedia.username = photoJ.getJSONObject("user").getString("username");
                    thismedia.avaurl = photoJ.getJSONObject("user").getString("profile_picture");
                    thismedia.datetime = photoJ.getLong("created_time");
                    thismedia.imageurl = photoJ.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                    thismedia.likeCount = photoJ.getJSONObject("likes").getInt("count");
                    thismedia.caption = photoJ.getString("caption");
                    Log.d("debug",thismedia.username+ " "+thismedia.avaurl);
                    ImageView ivavamedia = (ImageView) findViewById(R.id.ivpostava);
                    TextView tvpostname = (TextView) findViewById(R.id.tvpostname);
                    TextView tvposttime = (TextView) findViewById(R.id.tvposttime);
                    ImageView ivMedia = (ImageView) findViewById(R.id.ivmedia);
                    TextView tvLike = (TextView) findViewById(R.id.tvpostlike);
                    TextView tvcaption = (TextView) findViewById(R.id.tvCaption);
                    try{
                        tvpostname.setText(thismedia.username);
                       // if(thismedia.caption!= null){
                        //tvcaption.setText(thismedia.caption);
                       // }
                        tvLike.setText("\uD83D\uDC99 " + String.valueOf(thismedia.likeCount) + " likes");
                        tvposttime.setText("\uD83D\uDD52 " + DateUtils.getRelativeTimeSpanString(thismedia.datetime * 1000).toString());
                        Transformation transformation = new RoundedTransformationBuilder()
                                .borderColor(Color.BLACK)
                                .borderWidthDp(0)
                                .cornerRadiusDp(30)
                                .oval(false)
                                .build();
                        Picasso.with(comment_activity.this).load(thismedia.imageurl).into(ivMedia);
                        Picasso.with(comment_activity.this).load(thismedia.avaurl).fit().transform(transformation).into(ivavamedia);
                    }catch (Exception e){e.printStackTrace();}
                }catch (JSONException e){e.printStackTrace();};



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }
    public void fletchComment(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/"+id+"/comments?client_id="+CLIENT_ID;
        Log.d("debug", "url " + url);
        client.get(url,null,new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray commentJSON = null;
                try {
                   commentJSON = response.getJSONArray("data");
                    for (int i = 0; i < commentJSON.length(); i++) {
                        JSONObject commentJ = commentJSON.getJSONObject(i);
                        Comment sleeppy = new Comment();
                        sleeppy.username = commentJ.getJSONObject("from").getString("username");
                        sleeppy.avaurl = commentJ.getJSONObject("from").getString("profile_picture");
                        sleeppy.comment = commentJ.getString("text");
                        sleeppy.created_time = commentJ.getLong("created_time");
                        AComment.add(sleeppy);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }
}
