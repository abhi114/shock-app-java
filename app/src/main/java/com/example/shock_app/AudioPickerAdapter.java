package com.example.shock_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//The adapter creates view holders as needed. The adapter also binds the view holders to their data. It does this by assigning the view holder to a position, and calling the adapter's onBindViewHolder() method. That method uses the view holder's position to determine what the contents should be, based on its list position.

// This adapter must extend a class called RecyclerView.Adapter passing our class that implements the ViewHolder pattern:
public class AudioPickerAdapter extends RecyclerView.Adapter<AudioPickerAdapter.ViewHolder> {
    List<AudioModel> audios;
    Callback callback;

    public AudioPickerAdapter(List<AudioModel> items,Callback callback){
        this.audios = items;
        this.callback = callback;
    }


    @NonNull
    //Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //parent is the ViewGroup into which the new View will be added after it is bound to an adapter position
        //Instantiates a layout XML file into its corresponding View objects.
        //it is never used directly. Instead, use Activity.getLayoutInflater() or Context#getSystemService to retrieve a standard LayoutInflater instance that is already hooked up to the current context and correctly configured for the device you are running on.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item,parent,false);

        return new ViewHolder(view); //creating a new instance of the ViewHolder class defined down
    }
    //Called by RecyclerView to display the data at the specified position. This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect the item at the given position.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AudioModel item = audios.get(position); // getting the audio at the specific position
        holder.itemView.setOnClickListener(new View.OnClickListener() { // calling the itemview constructor of the Viewholder class and setting listener on each view
            @Override
            public void onClick(View v) {
                callback.itemSelected(item); // we are passing the item to the interface method so that it can be communicated with the fragment

            }
        });
        holder.textView.setText(item.getDescriptionMessage()); // as the holder is created with the view and the view is created with the audio_item.xml which contains the text view
    }

    @Override
    public int getItemCount() {
        return audios.size(); // return the size of the model
    }

    //A ViewHolder describes an item view and metadata about its place within the RecyclerView.
    //RecyclerView.Adapter implementations should subclass ViewHolder and add fields for caching potentially expensive View.findViewById(int) results.


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView; // description of the audio clip

        public ViewHolder(@NonNull View itemView) { // the view of each item of the recycler view
            super(itemView);
            textView = itemView.findViewById(R.id.textView);  //Finds the first descendant view with the given ID

        }
    }
    // using interface to communicate the adapter with the dialog fragment
    //This will implement the interface that we have in our Adapter.
    // we will add extends AudioPikcerAdapter implements Callback and then override itemSelected
    interface Callback{
        void itemSelected(AudioModel item);
    }

}
