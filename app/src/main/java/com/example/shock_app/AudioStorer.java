package com.example.shock_app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AudioStorer {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public AudioStorer(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(ShockUtils.SHOCK_SCARED_PREFS,Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    //add audio method
    public void addAudio(AudioModel audioModel){
        List<AudioModel> audios = getStoredAudios(); // we first call the audios that are present
        audios.add(audioModel); // then we added the new one
        storeAudios(audios); // now we stored it back with the new one
    }

    public void storeAudios(List<AudioModel> audioModelList){
        String key = context.getString(R.string.key_stored_audios);
        Gson gson = new Gson();
        editor.putString(key,gson.toJson(audioModelList));
        editor.commit();
    }

    private List<AudioModel> getStoredAudios(){
        String audiosAsString = preferences.getString(context.getString(R.string.key_stored_audios),null);
        if(audiosAsString == null || audiosAsString.length() == 0){
            return new ArrayList<>();

        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AudioModel>>(){}.getType();
        return gson.fromJson(audiosAsString,type);

    }

    public List<AudioModel> getAllAudios(){
        List<AudioModel> audios = new ArrayList<>();
        audios.add(new AudioModel(0,"Scream2","Scream 2",true));
        audios.add(new AudioModel(1,"behind_you","behind you",true));
        audios.add(new AudioModel(2,"Scream1","Scream 1",true));

        audios.addAll(getStoredAudios());
        return audios;
    }

    public AudioModel getSelectedAudio(){
        List<AudioModel> audios = getAllAudios();

        AudioModel defaultAudio = audios.get(0);

        int audioId = preferences.getInt(context.getString(R.string.key_audio_id),0);
        for(AudioModel audio : audios){
            if(audio.getId() == audioId){
                return audio;
            }
        }
        //fallback on default
        editor.putInt(context.getString(R.string.key_audio_id),0);
        editor.commit();
        return  defaultAudio;
    }


}
