package com.ede.standyourground.networking.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */

public class GooglePlaceAltId {

    @Expose
    @SerializedName("place_id")
    private String placeId;

    @Expose
    @SerializedName("scope")
    private String scope;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
