package com.example.shock_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ImageView imageView ;
    Uri photoUri;
    Uri soundUri;

    TextToSpeech tts;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        //imageView.setImageResource();
        photoUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator + File.separator + File.separator + "/drawable/bust_1");
        soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator + File.separator + File.separator + "/raw/scream2");

        showImage();
        playSoundClip();

    }
    private void showImage(){
        Glide.with(this).load(photoUri).into(imageView);
        imageView.setVisibility(View.VISIBLE);
    }
    private void playSoundClip(){
        mediaPlayer = MediaPlayer.create(this,soundUri);
        mediaPlayer.start();

    }

}