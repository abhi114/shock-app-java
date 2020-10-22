package com.example.shock_app;

import java.io.Serializable;
import java.util.Objects;

public class AudioModel implements Serializable {
    int id;
    String audioFileName;
    String descriptionMessage;
    boolean isAsset;
    boolean isTTS;  // text to speech

    //for asset audio only
    public AudioModel(int id, String audioFileName, String descriptionMessage, boolean isAsset) {
        this.id = id;
        this.audioFileName = audioFileName;
        this.descriptionMessage = descriptionMessage;
        this.isAsset = isAsset;
    }

    // for text to speech only
    public AudioModel(int id, String descriptionMessage) {
        this.id = id;
        this.descriptionMessage = descriptionMessage;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public String getDescriptionMessage() {
        return descriptionMessage;
    }

    public boolean isAsset() {
        return isAsset;
    }

    public boolean isTTS() {
        return isTTS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioModel that = (AudioModel) o;
        return id == that.id &&
                isAsset == that.isAsset &&
                isTTS == that.isTTS &&
                Objects.equals(audioFileName, that.audioFileName) &&
                Objects.equals(descriptionMessage, that.descriptionMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, audioFileName, descriptionMessage, isAsset, isTTS);
    }
}
