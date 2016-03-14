package com.example.johnw.instagramclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
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

public class Showprofile extends AppCompatActivity {
    String userid;
    GridView gvpost;
    User user ;
    private ArrayList<String> urllist;
    private customGrid urladapter;
    String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showprofile);
        user = new User();
        urllist = new ArrayList<String>();
        urladapter = new customGrid(this,urllist);
        gvpost = (GridView) findViewById(R.id.gvpost);
        gvpost.setAdapter(urladapter);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userid = null;
            } else {
                userid = extras.getString("id");
            }
        }
        //get url list
        fletchinfo();

    }
    private void fletchinfo(){
        String urlmedia = "https://api.instagram.com/v1/users/"+userid+"/media/recent/?client_id="+CLIENT_ID;
        String urluser = "https://api.instagram.com/v1/users/"+userid+"/?client_id="+CLIENT_ID;
        Log.d("debug2",urlmedia+"\n"+urluser);
        AsyncHttpClient getmedia = new AsyncHttpClient();
        AsyncHttpClient getuserinfo = new AsyncHttpClient();
        getmedia.get(urlmedia, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    Log.d("debug2","I'm in");
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJ = photosJSON.getJSONObject(i);
                        urllist.add(photoJ.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));

                    }
                    urladapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("debug2", "url" + urllist.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
        getuserinfo.get(urluser, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON =null;
                try {
                    Log.d("debug2","I'm in");
                        JSONObject photoJ = response.getJSONObject("data");
                        user.id = photoJ.getString("id");
                        user.username = photoJ.getString("username");
                        user.profile_picture_url = photoJ.getString("profile_picture");
                        user.bio = photoJ.getString("bio");
                        user.post = photoJ.getJSONObject("counts").getInt("media");
                        user.follows = photoJ.getJSONObject("counts").getInt("follows");
                        user.followers = photoJ.getJSONObject("counts").getInt("followed_by");
                    TextView tvusername = (TextView) findViewById(R.id.tvUN);
                    TextView tvpost = (TextView) findViewById(R.id.tvposts);
                    TextView tvbio = (TextView)findViewById(R.id.tvbio);
                    TextView tvfollow = (TextView)findViewById(R.id.tvfollower);
                    TextView tvfollowing = (TextView)findViewById(R.id.tvfollowing);
                    ImageView ivava = (ImageView)findViewById(R.id.ivavashow);
                    tvusername.setText(user.username);
                    tvbio.setText(user.bio);
                    tvpost.setText(String.valueOf(user.post));
                    tvfollow.setText(String.valueOf(user.followers));
                    tvfollowing.setText(String.valueOf(user.follows));
                    Transformation transformation = new RoundedTransformationBuilder()
                            .borderColor(Color.BLACK)
                            .borderWidthDp(0)
                            .cornerRadiusDp(30)
                            .oval(false)
                            .build();
                    Picasso.with(Showprofile.this).load(user.profile_picture_url).fit().transform(transformation).into(ivava);
                        Log.d("debug2", "user" + user.username.toString());

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
