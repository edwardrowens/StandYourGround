package com.ede.standyourground.networking.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */

public class GooglePlacePhoto {

    @Expose
    @SerializedName("height")
    private int height;

    @Expose
    @SerializedName("photo_reference")
    private String photoReferences;

    @Expose
    @SerializedName("width")
    private int width;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoReference() {
        return photoReferences;
    }

    public void setPhotoReferences(String photoReferences) {
        this.photoReferences = photoReferences;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
