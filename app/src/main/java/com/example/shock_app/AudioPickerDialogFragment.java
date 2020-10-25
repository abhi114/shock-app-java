package com.example.shock_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AudioPickerDialogFragment extends DialogFragment implements AudioPickerAdapter.Callback { //we are implementing the callback created in AudioAdapter and is passed with the item view
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    AudioPickerAdapter adapter ;
    RecyclerView recyclerView; //A flexible view for providing a limited window into a large data set.
    LinearLayoutManager linearLayoutManager; //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getContext().getSharedPreferences(ShockUtils.SHOCK_SCARED_PREFS, Context.MODE_PRIVATE); //getting the shared prefernce
        editor = preferences.edit();
    }

    //Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null (which is the default implementation). This will be called between onCreate(android.os.Bundle) and onActivityCreated(android.os.Bundle).
    //If you return a View from here, you will later be called in onDestroyView() when the view is being released.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_media_picker,container,false); //inflating the recycler view

        List<AudioModel> items = new AudioStorer(getContext()).getAllAudios();//getting all the audio to create the adapter

        adapter = new AudioPickerAdapter(items,this); // making the instance of the adapter
        recyclerView = view.findViewById(R.id.recyclerView); // recyclerview
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());  //Creates a vertical LinearLayoutManager
        recyclerView.setLayoutManager(linearLayoutManager); // RecyclerView will not function without it


        return view;
    }


    //Called when the Fragment is visible to the user. This is generally tied to Activity#onStart() of the containing Activity's lifecycle.
    //If you override this method you must call through to the superclass implementation.
    //will be used to make the dialog fill the screen
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog  = getDialog(); // getDialog() simply returns the private variable mDialog from the DialogFragment.
        if(dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT; //getting the match parent of the view group
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            //Retrieve the current Window for the activity
            //making the dialog full screnn
            dialog.getWindow().setLayout(width,height);
        }

    }

    @Override
    public void itemSelected(AudioModel item) {
        editor.putInt(getString(R.string.key_audio_id),item.getId());// update which item was selected
        editor.commit();
        dismiss();
        //Dismiss the fragment and its dialog. If the fragment was added to the back stack, all back stack state up to and including this entry will be popped. Otherwise, a new transaction will be committed to remove the fragment.
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(ShockUtils.MEDIA_UPDATED_ACTION)); // sending a local broadcast to the main activity
        //Broadcast the given intent to all interested BroadcastReceivers.
    }
}
