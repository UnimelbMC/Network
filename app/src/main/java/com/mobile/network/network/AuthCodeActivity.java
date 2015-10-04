package com.mobile.network.network;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.common.Caption;
import org.jinstagram.entity.common.Comments;
import org.jinstagram.entity.common.ImageData;
import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.common.Likes;
import org.jinstagram.entity.common.Location;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.List;

public class AuthCodeActivity extends AppCompatActivity {
    Token accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    //network://localhost/redirect/?code=baf92352dbb144eeb2e3bfb6403ba360
        final Intent intent = getIntent();
        String data = getIntent().getData().toString();
        Log.v("TEST_NET",data);
        String[] parts = data.split("=");
        Params.AUHTORIZE_CODE = parts[1];
        Log.v("TEST_NET", Params.AUHTORIZE_CODE );
        getAccessCode(Params.AUHTORIZE_CODE);
        getUserFeed();
        Intent homepage = new Intent(this, MainActivity.class);
        startActivity(homepage);
    }
    private void getAccessCode(String code){
        InstagramService service = new InstagramAuthService()
                .apiKey(Params.CLIENT_ID)
                .apiSecret(Params.CLIENT_SECRET)
                .callback(Params.REDIRECT_URI)
                .build();
       // Note : An empty token can be define as follows -

         final Token EMPTY_TOKEN = null;
       // Validate your user against Instagram
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
       // Getting the Access Token
        Verifier verifier = new Verifier(code);
        accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        Log.v("TEST_ACCESS", accessToken.toString());
    }

    public void getUserFeed(){
        Instagram instagram = new Instagram(accessToken);
        MediaFeed mediaFeed = null;
        try {
            mediaFeed = instagram.getUserFeeds();
        } catch (InstagramException e) {
            e.printStackTrace();
        }
        List<MediaFeedData> mediaFeeds = mediaFeed.getData();

        for (MediaFeedData mediaData : mediaFeeds) {
            System.out.println("id : " + mediaData.getId());
            System.out.println("created time : " + mediaData.getCreatedTime());
            System.out.println("link : " + mediaData.getLink());
            System.out.println("tags : " + mediaData.getTags().toString());
            System.out.println("filter : " + mediaData.getImageFilter());
            System.out.println("type : " + mediaData.getType());

            System.out.println("-- Comments --");
            Comments comments = mediaData.getComments();

            System.out.println("-- Caption --");
            Caption caption = mediaData.getCaption();

            System.out.println("-- Likes --");
            Likes likes = mediaData.getLikes();

            System.out.println("-- Images --");
            Images images = mediaData.getImages();
/*
            ImageData lowResolutionImg = images.getLowResolution();
            ImageData highResolutionImg = images.getHighResolution();
            ImageData thumbnailImg = images.getThumbnail();

            Location location = mediaData.getLocation();
            System.out.println();*/
        }
    }
}
