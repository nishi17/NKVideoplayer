package com.nishi.developer.nkvideoplayer;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.Random;

public class SplashScreen extends AppCompatActivity implements ConfettoGenerator {

    protected ViewGroup container;

    private int size;

    private int velocitySlow, velocityNormal;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        init();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                generateInfinite();
            }
        }, 500);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);

    }

    private void init() {

        container = (ViewGroup) findViewById(R.id.container);

        final Resources res = getResources();

        size = res.getDimensionPixelSize(R.dimen.big_confetti_size);

        velocitySlow = res.getDimensionPixelOffset(R.dimen.default_velocity_slow);

        velocityNormal = res.getDimensionPixelOffset(R.dimen.default_velocity_normal);


        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.snowflake), size, size, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        terminate();


    }

    private void terminate() {
        //getConfettiManager().terminate();
    }

    @Override
    protected void onStop() {
        super.onStop();

        terminate();
    }

    protected ConfettiManager generateInfinite() {
        return getConfettiManager().setNumInitialCount(0)
                .setEmissionDuration(ConfettiManager.INFINITE_DURATION)
                .setEmissionRate(15)
                .animate();
    }


    private ConfettiManager getConfettiManager() {

        final ConfettiSource source = new ConfettiSource(0, -size, container.getWidth(), -size);

        return new ConfettiManager(this, this, source, container)
                .setVelocityX(0, velocitySlow)
                .setVelocityY(velocityNormal, velocitySlow)
                .setRotationalVelocity(180, 90)
                .setTouchEnabled(true);
    }

    @Override
    public Confetto generateConfetto(Random random) {
        return new BitmapConfetto(bitmap);
    }


}

