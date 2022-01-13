package com.example.mongodocument;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    VideoView vv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        vv = findViewById(R.id.videoView);
        String Url = getIntent().getStringExtra("url");
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mediaController);
        vv.setMediaController(mediaController);
        vv.setVideoURI(Uri.parse(Url));
        vv.requestFocus();
        vv.start();
    }
}