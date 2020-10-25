package com.example.shock_app;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// refer to the audio adapter for comments
public class ImagePickerAdapter {

    class ViewHolder extends RecyclerView.ViewHolder{ //
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    //interface for callback
    //used to notify the dialog fragment that something had happend
    interface Callback{
        //it cannot have a body
        void itemSelected(ImageModel item);
    }



}
