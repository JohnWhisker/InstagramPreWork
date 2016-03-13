package com.example.johnw.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class comment_activity extends AppCompatActivity {
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<Comment> Comment;
    String id;
    private commentAdapter AComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_activity);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id= null;
            } else {
                id= extras.getString("id");
            }
        }
        Comment = new ArrayList<Comment>();
        AComment = new commentAdapter(this,Comment);
        ListView lvcomment = (ListView) findViewById(R.id.lvComment);
        lvcomment.setAdapter(AComment);
        fletchComment();
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
