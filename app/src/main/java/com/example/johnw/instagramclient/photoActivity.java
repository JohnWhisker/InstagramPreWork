package com.example.johnw.instagramclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class photoActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    ListView lvPhoto;
    private InstagramPhotoAdater aPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,  android.support.v7.appcompat.R.anim.abc_fade_out);

        setContentView(R.layout.activity_photo);
        photos = new ArrayList<InstagramPhoto>();
        aPhotos = new InstagramPhotoAdater(this, photos);
        lvPhoto = (ListView) findViewById(R.id.lvPhotos);
        lvPhoto.setAdapter(aPhotos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)

                // once the network request has completed successfully.

                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        fletchPhotos();
        setupListViewListener();
    }
    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data

        // `client` here is an instance of Android Async HTTP
        AsyncHttpClient client2 = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
        client2.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {

                    photosJSON = response.getJSONArray("data");
                    photos.clear();
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJ = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.id = photoJ.getString("id");
                        Log.d("debug", (photo.id == null ? "null" : photo.id));
                        photo.username = photoJ.getJSONObject("user").getString("username");
                        photo.caption = photoJ.getJSONObject("caption").getString("text");
                        photo.datetime = photoJ.getJSONObject("caption").getLong("created_time");
                        photo.imageurl = photoJ.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imagaeheight = photoJ.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likeCount = photoJ.getJSONObject("likes").getInt("count");
                        photo.avaurl = photoJ.getJSONObject("user").getString("profile_picture");
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

        swipeContainer.setRefreshing(false);
    }


    private void setupListViewListener() {
        final Context context = this;

        lvPhoto.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(context, comment_activity.class);
                       Log.v("ID photo",photos.get(position).id);
                        intent.putExtra("id", photos.get(position).id);
                        startActivity(intent);

                    }
                }
        );
    }
    public void fletchPhotos(){
        //https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        //e05c462ebd86446ea48a5af73769b602

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJ = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.id = photoJ.getString("id");
                        photo.username = photoJ.getJSONObject("user").getString("username");
                        photo.caption = photoJ.getJSONObject("caption").getString("text");
                        photo.datetime = photoJ.getJSONObject("caption").getLong("created_time");
                        photo.imageurl = photoJ.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imagaeheight = photoJ.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likeCount = photoJ.getJSONObject("likes").getInt("count");
                        photo.avaurl = photoJ.getJSONObject("user").getString("profile_picture");
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

    }
}
