package com.example.shock_app;

import java.io.Serializable;
import java.util.Objects;

public class ImageModel implements Serializable {

    int id;
    String imageFilename;
    boolean isAsset;

    public ImageModel(int id, String imageFilename, boolean isAsset) { // default constructor
        this.id = id;
        this.imageFilename = imageFilename;
        this.isAsset = isAsset;
    }

    public int getId() { // getter for thee fields
        return id;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public boolean isAsset() {
        return isAsset;
    }

    @Override
    public boolean equals(Object o) { // equals is used to compare this image object with the object which will be passed as o
        // it is compared deeply as each and every fields of the objects are compared
        if (this == o) return true; // comparing that the object are equal
        if (o == null || getClass() != o.getClass()) return false; // if the class paseed int the object is not same as this class then return false
        ImageModel that = (ImageModel) o; //type casting it in the form of imagemodel so that we can compare the parameters
        return id == that.id && // comparing the parameters
                isAsset == that.isAsset &&
                Objects.equals(imageFilename, that.imageFilename); // equals is used to compare these two fields
    }

    @Override
    public int hashCode() { // it should be called when equals is called
        return Objects.hash(id, imageFilename, isAsset); // it used to convert this class(object) in the form of hash with all its parameters
    }
}
