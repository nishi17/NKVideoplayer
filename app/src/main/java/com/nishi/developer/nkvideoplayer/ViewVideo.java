package com.nishi.developer.nkvideoplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.swipper.library.Swipper;

public class ViewVideo extends Activity implements View.OnClickListener {

    private String filename;

    private VideoView vv;

    private ImageView iv_lock;

    private boolean result, isLOck = false;

    private MediaController mMediaController;

    private AudioManager audioManager;

    private GestureDetectorCompat gestureDetectorCompat = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //  this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        setContentView(R.layout.activity_view);

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        System.gc();

        Intent i = getIntent();

        Bundle extras = i.getExtras();

        filename = extras.getString("videofilename");
        // vv = new VideoView(getApplicationContext());

        iv_lock = (ImageView) findViewById(R.id.iv_lock);
        iv_lock.setOnClickListener(this);

        vv = (VideoView) findViewById(R.id.videoView);

        vv.setVideoPath(filename);

        mMediaController = new MediaController(this);

        vv.setMediaController(mMediaController);

        vv.requestFocus();

        vv.start();

        LockIsonInvisible();

        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();

        // Set activity in the listener.
        gestureListener.setActivity(this);

        // Create the gesture detector with the gesture listener.
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);



    }


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        int action = event.getAction();
//        int keyCode = event.getKeyCode();
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                if (action == KeyEvent.ACTION_DOWN) {
//                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
//                }
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                if (action == KeyEvent.ACTION_DOWN) {
//                    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
//                }
//                return true;
//            default:
//                return super.dispatchKeyEvent(event);
//        }
//    }

    private void LockIsonInvisible() {
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                // This method will be executed once the timer is over

                if (!isLOck) {

                    if (!mMediaController.isShowing())
                        iv_lock.setVisibility(View.GONE);
                }
            }
        }, 500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                show(0); // show until hide is called
                break;
            case MotionEvent.ACTION_UP:
                show(3000); // start timeout
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    public void hide() {
        if (mMediaController.isShowing()) {
            iv_lock.setVisibility(View.VISIBLE);
        } else {
            if (!isLOck)
                iv_lock.setVisibility(View.INVISIBLE);
        }
    }

    public void show(int timeout) {

//       if (mMediaController.isShowing())
        iv_lock.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over

                if (!isLOck) {
                    if (!mMediaController.isShowing())
                        iv_lock.setVisibility(View.GONE);
                }
            }
        }, timeout);

    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (isLOck)
//            return false;
//        else
//            return true;
//    }


    @Override
    public void onBackPressed() {

        if (!isLOck)
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        //this is for lock key

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);

        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);

        switch (v.getId()) {

            case R.id.iv_lock:

                if (isLOck) {

                    lock.reenableKeyguard();

                    isLOck = false;

                    iv_lock.setImageDrawable(getResources().getDrawable(R.drawable.unlock));

                    LockIsonInvisible();

                    VideoTouchView(false);


                } else {

                    lock.disableKeyguard();

                    isLOck = true;

                    iv_lock.setImageDrawable(getResources().getDrawable(R.drawable.lock));

                    VideoTouchView(true);

                }

                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void VideoTouchView(final boolean touch) {

        vv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // do nothing here......
                return touch;
            }
        });


    }


/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

       *//* if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getWindow().getDecorView().invalidate();

            float height = getWidthInPx(this);

            float width = getHeightInPx(this);

            vv.getLayoutParams().height = (int) width;

            vv.getLayoutParams().width = (int) height;

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();

            final WindowManager.LayoutParams attrs = getWindow().getAttributes();

            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getWindow().setAttributes(attrs);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            float width = getWidthInPx(this);

            float height = dip2px(this, 200.f);

            vv.getLayoutParams().height = (int) height;

            vv.getLayoutParams().width = (int) width;

        }
*//*
    }*/

 /*   public static float getHeightInPx(Context context) {

        final float height = context.getResources().getDisplayMetrics().heightPixels;

        return height;
    }

    public static float getWidthInPx(Context context) {

        final float width = context.getResources().getDisplayMetrics().widthPixels;

        return width;
    }

    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {


        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }*/


}