package com.example.shock_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class SupriseActivity extends AppCompatActivity {
    ImageView imageView ;
    Uri photoUri;
    Uri soundUri;

    TextToSpeech tts;
    MediaPlayer mediaPlayer;
    boolean acceptingTouches = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suprise);

        imageView = findViewById(R.id.imageView);

        //imageView.setImageResource();
        photoUri = ShockUtils.getDrawableUri(this,"bust_1");
        soundUri = ShockUtils.getRawUri(this,"behind_you");

        Toast.makeText(this,"ready",Toast.LENGTH_SHORT).show();

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    private void showImage(){
        Glide.with(this).load(photoUri).into(imageView);
        imageView.setVisibility(View.VISIBLE);
    }
    private void playSoundClip(){
        mediaPlayer = MediaPlayer.create(this,soundUri);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        mediaPlayer.start();

    }

    private void userTriggeredActions(){
        if(!acceptingTouches){
            return;
        }
        acceptingTouches = false;

        showImage();
        playSoundClip();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        userTriggeredActions();
        return super.onTouchEvent(event);
    }
}