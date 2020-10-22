package com.example.shock_app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class imageStorer {
    Context context;
    SharedPreferences preferences;
    //If you have a relatively small collection of key-values that you'd like to save, you should use the SharedPreferences APIs. A SharedPreferences object points to a file containing key-value pairs and provides simple methods to read and write them. Each SharedPreferences file is managed by the framework and can be private or shared.
    SharedPreferences.Editor editor;// Interface used for modifying values in a SharedPreferences object. All changes you make in an editor are batched, and not copied back to the original SharedPreferences until you call commit() or apply()

    public imageStorer(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(ShockUtils.SHOCK_SCARED_PREFS,Context.MODE_PRIVATE); // getting the sharedprefernce if it is not there with the specified name it will be created and the mode tells that only our app can access it
        editor = preferences.edit(); // editor is used to make change or edit the preferences
    }

    public void storerImages(List<ImageModel> imageModelList){ //list for the images so that if changes are made we will resave the entire list
        String key = context.getString(R.string.key_stored_offline_images);
        Gson gson = new Gson(); // Gson is a Java library that can be used to convert Java Objects into their JSON representation. Gson considers both of these as very important design goals.
        //we are converting the imagemodel object into json string and then storing it in the shared preferences
        editor.putString(key,gson.toJson(imageModelList)); //Set a String value in the preferences editor, to be written back once commit() or apply() are called.
        editor.commit(); // to save the changes to the shared preferences
    }

    // now we will take all the images that will pe present in the shared preference and then add them to our collection in the app including the ones that are downloaded
    public void addImage(ImageModel image){
        List<ImageModel> images = getStoredImages(); // we take all the stored images
        images.add(image);
        storerImages(images);

    }



    private List<ImageModel> getStoredImages(){
        String imagesAsJson = preferences.getString(context.getString(R.string.key_stored_offline_images),null); // taking the images that are stored in the shared preference or that are currently downloaded and the passed it to the getallimages so that it can be merged with the default images and can be inserted in the app
        if(imagesAsJson == null || imagesAsJson.length() == 0){
            return  new ArrayList<>(); // we have to return this because null cant be returned to getAllImages to be added to the list

        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<ImageModel>>(){}.getType(); // we are creating a type to convert the gson into the specified List<ImageModel>
        //see this example

        //List marks = new ArrayList();
    //Serialization
        //System.out.println("marks:" + gson.toJson(marks));

        //De-serialization
        //get the type of the collection.
        //Type listType = new TypeToken<list>(){}.getType();

        //pass the type of collection
        //marks = gson.fromJson("[100,90,85]", listType);
        //System.out.println("marks:" +marks);</list>

        // it will basically return the class type

        List<ImageModel> storedImages = gson.fromJson(imagesAsJson,type);
        return storedImages;

    }

    private  List<ImageModel> getAllImages(){
        ArrayList<ImageModel> assetImages = new ArrayList<>(); // creating the image arraylist and passing the ImageModel object ot it
        assetImages.add(new ImageModel(0,"bust_1",true));
        assetImages.add(new ImageModel(1,"bust_2",true));
        assetImages.add(new ImageModel(2,"clown",true));

        assetImages.addAll(getStoredImages()); // it will take the list passed by the get stored function and then add it to the the assertImages arraylist and the return it
        return assetImages;


    }

    //get the selected image
    public ImageModel getSelectedImage(){
        List<ImageModel> images = getAllImages();
        ImageModel defaultImages = images.get(0);

        int imageId = preferences.getInt(context.getString(R.string.key_photo_id),0);

        for(ImageModel image: images){
            if(image.getId() == imageId){
                return image;
            }
        }

        // fallback on default
        editor.putInt(context.getString(R.string.key_photo_id),0);
        editor.commit();
        return defaultImages;
    }


}
