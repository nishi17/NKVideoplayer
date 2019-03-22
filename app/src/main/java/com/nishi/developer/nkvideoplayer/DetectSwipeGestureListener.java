package com.nishi.developer.nkvideoplayer;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class DetectSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    // Minimal x and y axis swipe distance.
    private static int MIN_SWIPE_DISTANCE_X = 100;
    private static int MIN_SWIPE_DISTANCE_Y = 100;

    // Maximal x and y axis swipe distance.
    private static int MAX_SWIPE_DISTANCE_X = 1000;
    private static int MAX_SWIPE_DISTANCE_Y = 1000;

    // Source activity that display message in text view.
    private ViewVideo activity = null;

    public ViewVideo getActivity() {
        return activity;
    }

    public void setActivity(ViewVideo activity) {
        this.activity = activity;
    }

    private int sWidth, sHeight;

    /* This method is invoked when a swipe gesture happened. */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Get swipe delta value in x axis.
        float deltaX = e1.getX() - e2.getX();

        // Get swipe delta value in y axis.
        float deltaY = e1.getY() - e2.getY();

        // Get absolute value.
        float deltaXAbs = Math.abs(deltaX);
        float deltaYAbs = Math.abs(deltaY);

        // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
        if ((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X)) {

            if (deltaX > 0) {

                Log.e("left ", "left");

            } else {

                Log.e("right ", "right");
            }
        }
        getScreenSize();

        if ((deltaYAbs >= MIN_SWIPE_DISTANCE_Y) && (deltaYAbs <= MAX_SWIPE_DISTANCE_Y)) {

            if (deltaY > 0) {

                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);


                /* with out Ui of volume control */
                // audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

                Log.e("up ", "   up  - " + e1.getX());


            } else {

                Log.e("down ", "down");


                //touch is start
                float downX = e1.getX();
                float downY = e2.getY();
                if (e2.getX() < (sWidth / 2)) {

                    //here check touch is screen left or right side
                    intLeft = true;
                    intRight = false;

                } else if (e1.getX() > (sWidth / 2)) {

                    //here check touch is screen left or right side
                    intLeft = false;
                    intRight = true;

                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

                }



                /* with out Ui of volume control */

                //  audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            }
        }
        return true;
    }

    private boolean intLeft, intRight;
    private long diffX, diffY;
    private Display display;
    private Point size;
    private float downX, downY;
    private AudioManager mAudioManager;

    private void getScreenSize() {
        display = getActivity().getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        sWidth = size.x;
        sHeight = size.y;
    }

    // Invoked when single tap screen.
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //  this.activity.displayMessage("Single tap occurred.");
        return true;
    }

    // Invoked when double tap screen.
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // this.activity.displayMessage("Double tap occurred.");
        return true;
    }
}
