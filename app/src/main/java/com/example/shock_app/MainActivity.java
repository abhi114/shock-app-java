package com.example.shock_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.prankSurface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification();
                finish(); // to finish the activity
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu); //to inflate the drop-down + icon menu at the top right
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_button){
            final PopupMenu popupMenu = new PopupMenu(this,findViewById(R.id.add_button)); // creating the pop-up-menu and adding it to the anchor of the add-button
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu()); //it is used to take the popup-menu xml file and inflate it to the menu
                //setting click listener on the on item click listener
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem popItem) {
                    switch (popItem.getItemId()){ //finding the id of the item selected
                        case R.id.addImage:
                            addImageDialog();
                            break;
                        case R.id.addAudio:
                            addAudioDialog();
                            break;
                    }

                    return false;
                }
            });

            popupMenu.show(); // this is compulsory for showing the popup screen


            return true;
        }

        return super.onOptionsItemSelected(item); // already there in the boilerplate
    }
    private void addAudioDialog(){ // adding audio dialog
        final EditText soundEditText = new EditText(this);
        soundEditText.setHint("Message to speak");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Audio")
                .setMessage("Enter Message for text to speech")
                .setView(soundEditText)
                .setCancelable(true) // whether the dialog is cancelable
                //creating the two button of the dialog box
                //the positive and the negative ones
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { //Interface used to allow the creator of a dialog to run some code when an item on the dialog is clicked.
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // when the okay button is clicked
                        String message = soundEditText.getText().toString();//getting the message from the edittext
                        if(message == null || message.trim().isEmpty()){ // if the editext is empty
                            Toast.makeText(getBaseContext(),"message cannot be empty",Toast.LENGTH_SHORT).show();
                            return;
                        } else{
                            addTTSAudio(message); // for the text to speech part to add audio
                        }

                    }
                })
                .setNegativeButton(android.R.string.cancel,null).create(); // when the cancel button is clicked

        dialog.show();
    }
    private void addTTSAudio(String message){

    }

    private void addImageDialog(){
        final EditText urlBox = new EditText(this);
        urlBox.setHint("image Url"); // setting the hint for the dialog box
        // see the audio dialog box method for reference with comments
        AlertDialog dialog = new AlertDialog.Builder(this) //creating the alert dialog box
                .setTitle("image Url")
                .setMessage("Import as image from web.")
                .setView(urlBox)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = urlBox.getText().toString();
                        if(url == null || url.trim().isEmpty()){
                            Toast.makeText(getBaseContext(),"url cannot be empty",Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            downloadImageToFile(url);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel,null).create();
        dialog.show();
    }

    private void downloadImageToFile(String url){

    }


    private void createNotification(){
        String notificationMessage = "Tap to Shock Friends"; //the message to me displayed on notification
        int reqId = (int) System.currentTimeMillis(); //it should be unique so best way is to use current time
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); //getting the notification manager from the system services
        Intent notificationIntent = new Intent(this,SupriseActivity.class); //creating the intent for the shock activity
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //creating the new task
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);// closing if previous activity is already opened

        PendingIntent contentIntent = PendingIntent.getActivity(this,reqId,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT); // pending intent passes the intent with permission supplied to the app and the intetn remains even after the app got terminated

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this) // creating the ui for the notification
                .setSmallIcon(R.mipmap.ic_launcher).setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessage)) // setting the icons
                .setContentText(notificationMessage).setAutoCancel(true)
                .setContentIntent(contentIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "Anything Channel";
            NotificationChannel channel = new NotificationChannel(channelId,"Channel Human Readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);

        }
        notificationManager.notify(12312,builder.build());


    }
}