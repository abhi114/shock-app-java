package com.example.shock_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AudioPickerDialogFragment extends DialogFragment {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    AudioPickerAdapter adapter ;
    RecyclerView recyclerView; //A flexible view for providing a limited window into a large data set.
    LinearLayoutManager linearLayoutManager; //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getContext().getSharedPreferences(ShockUtils.SHOCK_SCARED_PREFS, Context.MODE_PRIVATE);
    }

    //Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null (which is the default implementation). This will be called between onCreate(android.os.Bundle) and onActivityCreated(android.os.Bundle).
    //If you return a View from here, you will later be called in onDestroyView() when the view is being released.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    //Called when the Fragment is visible to the user. This is generally tied to Activity#onStart() of the containing Activity's lifecycle.
    //If you override this method you must call through to the superclass implementation.
    //will be used to make the dialog fill the screen
    @Override
    public void onStart() {
        super.onStart();
    }
}
