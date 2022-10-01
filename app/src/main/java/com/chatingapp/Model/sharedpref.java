package com.chatingapp.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedpref {
     String images;
    Context context;

    SharedPreferences sharedPreferences;

    public sharedpref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("mypref",Context.MODE_PRIVATE);
    }

    public String getImgaes() {
        images = sharedPreferences.getString("profile", null);
        return images;
    }

    public void setImgaes(String images) {
        this.images = images;
        sharedPreferences.edit().putString("profile",images.toString()).commit();
    }

    public void cleashared(){
        sharedPreferences.edit().clear().commit();
    }

}
