package com.example.johnw.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
    private InstagramPhotoAdater aPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photos = new ArrayList<InstagramPhoto>();
        aPhotos = new InstagramPhotoAdater(this, photos);
        ListView lvPhoto = (ListView) findViewById(R.id.lvPhotos);
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
    }
    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data

        // `client` here is an instance of Android Async HTTP
            aPhotos.clear();
           fletchPhotos();
        swipeContainer.setRefreshing(false);
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
