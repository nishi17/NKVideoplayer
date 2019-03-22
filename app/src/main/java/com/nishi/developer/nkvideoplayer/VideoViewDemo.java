package com.nishi.developer.nkvideoplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.swipper.library.Swipper;

public class VideoViewDemo extends Swipper {

    VideoView videoView;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_view);

        videoView = (VideoView) findViewById(R.id.videoView);

        Intent i = getIntent();

        Bundle extras = i.getExtras();

        filename = extras.getString("videofilename");
        // vv = new VideoView(getApplicationContext());

        videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setVideoPath(filename);

        MediaController mMediaController = new MediaController(this);

        videoView.setMediaController(mMediaController);

        videoView.start();

        Brightness(Orientation.CIRCULAR);

        Volume(Orientation.VERTICAL);

        Seek(Orientation.HORIZONTAL, videoView);

        set(this);

    }

}
