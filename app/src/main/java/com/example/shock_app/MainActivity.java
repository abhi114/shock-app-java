package com.example.shock_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //create reference to the audio store and the image store
    AudioStorer audioStorer;
    imageStorer imgStorer;

    ImageView imageView;
    TextView audioTextView;
    //when someone will touch the images in the activity a broadcast will be sent to fetch latest info
    //Broadcast Receivers simply respond to broadcast messages from other applications or from the system itself. These messages are sometime called events or intents. For example, applications can also initiate broadcasts to let other applications know that some data has been downloaded to the device and is available for them to use, so this is broadcast receiver who will intercept this communication and will initiate appropriate action.
    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        //This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

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

        preferences = getSharedPreferences(ShockUtils.SHOCK_SCARED_PREFS,Context.MODE_PRIVATE); // getting the shared preferences
        editor = preferences.edit();
        audioStorer = new AudioStorer(this);
        imgStorer = new imageStorer(this); // initializing the storer class

        imageView = findViewById(R.id.scaryImageView);
        audioTextView = findViewById(R.id.audioTextView);

        updateUI();

        //add the click listener for the opening of the select audiopicker fragment
        findViewById(R.id.audioSurface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //API for performing a set of Fragment operations. fragment transaction
                //Return the FragmentManager for interacting with fragments associated with this activity. getSupportFragmentManager()
                //Start a series of edit operations on the Fragments associated with this FragmentManager. it returns an fragment Transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if(prev != null){
                    ft.remove(prev); //Remove an existing fragment.

                }
                ft.addToBackStack(null); //Add this transaction to the back stack. This means that the transaction will be remembered after it is committed, and will reverse its operation when later popped off the stack.

                AudioPickerDialogFragment dialogFragment = new AudioPickerDialogFragment();
                dialogFragment.setCancelable(true);
                dialogFragment.show(ft,"dialog"); //Display the dialog, adding the fragment using an existing transaction and then committing the transaction.

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
    private void addTTSAudio(String message){ // adding the string which is spoken by the user using text to speech
        int mediaId = preferences.getInt(getString(R.string.key_media_id),ShockUtils.STARTING_ID); // default will be the starting id
        editor.putInt(getString(R.string.key_media_id),mediaId+1); // it is basically next media id
        // we are incrementing the media id by 1
        editor.commit();
        // for this we will use the 2nd constructor
        AudioModel audioModel = new AudioModel(mediaId,message);
        audioStorer.addAudio(audioModel);   //adding it to the audiostorer or the shared preferences






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
        //download the image with glide
        Glide.with(this)
                //by default it is set to as drawable but as bitmap build the request in the form of bitmap
                .asBitmap()
                .load(url)
                //The into(Target) method is used not only to start each request, but also to specify the Target that will receive the results of the request:
                // Note that both into(Target) and into(ImageView) return a Target instance

                .into(new SimpleTarget<Bitmap>() { // it is basically used when you dont want to load the image to the image view and wants to save it
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImage(resource);
                    }
                });


    }
    public void saveImage(Bitmap bitmap){
        FileOutputStream outputStream = null;
        //FileOutputStream is meant for writing streams of raw bytes such as image data. For writing streams of characters, consider using FileWriter.
        File file = createInternalFile(UUID.randomUUID().toString()); //A class that represents an immutable universally unique identifier (UUID). A UUID represents a 128-bit value.
        //he UUID is generated using a cryptographically strong pseudo random number generator.
        int mediaId = preferences.getInt(getString(R.string.key_media_id),ShockUtils.STARTING_ID);
        editor.putInt(getString(R.string.key_media_id),mediaId +1);
        editor.commit();

        //Returns the absolute path of this file. An absolute path is a path that starts at a root of the file system. On Android, there is only one root: /.
        ImageModel imageModel = new ImageModel(mediaId,file.getAbsolutePath(),false); // the file.absolute path will be same as passed in the uuid as file is of the that instance

        try {
            //first we create a new file instance by writing the giving the file name as new File(imageModel.getImageFilename())
            // the file instance created is passed to the fileoutputStream to written with the image

            outputStream = new FileOutputStream(new File(imageModel.getImageFilename()));
            //Write a compressed version of the bitmap to the specified outputstream.
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            //Compress to the JPEG format. quality of 0 means compress for the smallest size. 100 means compress for max visual quality.
            outputStream.close();

            imgStorer.addImage(imageModel);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private File createInternalFile(String filename){
        //using this to create file
        File outputDir = getExternalCacheDir();
        //Returns absolute paths to application-specific directories on all shared/external storage devices where the application can place cache files it owns. These files are internal to the application, and not typically visible to the user as media.
        // This is like getCacheDir() in that these files will be deleted when the application is uninstalled
        File outputFile = new File(outputDir,filename); // FIRST IS THE ABSOLUTE PATH OF THE APP DIRECTIORY AND THE SECOND IS THE FILE NAME

        return outputFile;
    }

    //register the broadcast receiver

    // you can register a BroadcastReceiver in onStart() to monitor for changes that impact your UI, and unregister it in onStop() when the user no longer needs them
    @Override
    protected void onStart() {
        super.onStart();
        //Helper to register for and send broadcasts of Intents to local objects within your process. This has a number of advantages over sending global broadcasts
        //Helper to register for and send broadcasts of Intents to local objects within your process. This has a number of advantages over sending global broadcasts
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver,new IntentFilter(ShockUtils.MEDIA_UPDATED_ACTION));
        //New IntentFilter that matches a single action with no data.
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
    }

    private void updateUI(){
        ImageModel imageModel = imgStorer.getSelectedImage(); //get the image selected by the user

        Uri imgUri;
        if(imageModel.isAsset()){ // if it is a part of the app and present in drawable
            imgUri = ShockUtils.getDrawableUri(this,imageModel.getImageFilename()); // images are in drawable
        }else{
            imgUri = Uri.fromFile(new File(imageModel.getImageFilename())); // create a uri from a file

        }
        //update to current selected image
        Glide.with(this).load(imgUri)
                .into(imageView);

        //handling the audio text
        AudioModel audioModel = audioStorer.getSelectedAudio(); // taking the selected audio from the list
        audioTextView.setText(audioModel.getDescriptionMessage()); // show the description message on the screen


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