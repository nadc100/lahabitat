package org.fiap.lhabitat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Animation animationUn = AnimationUtils.loadAnimation(this, R.anim.deplacement_audessus);
        Animation animationDeux = AnimationUtils.loadAnimation(this, R.anim.deplacement_audessous);

        ImageView lhabitatView = findViewById(R.id.tipoImageView);
        ImageView logoView = findViewById(R.id.logoImageView);

        lhabitatView.setAnimation(animationDeux);
        logoView.setAnimation(animationUn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                Intent intent = new Intent(SplashScreenActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);

    }
}