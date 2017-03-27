package com.ede.standyourground.networking.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */

public class GooglePlaceResult {
    @Expose
    @SerializedName("geometry")
    private GooglePlaceGeometry geometry;

    @Expose
    @SerializedName("icon")
    private String icon;

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("opening_hours")
    private GooglePlaceOpeningHours openingHours;

    @Expose
    @SerializedName("photos")
    private List<GooglePlacePhoto> photos;

    @Expose
    @SerializedName("place_id")
    private String placeId;

    @Expose
    @SerializedName("scope")
    private String scope;

    @Expose
    @SerializedName("alt_ids")
    private List<GooglePlaceAltId> altIds;

    @Expose
    @SerializedName("reference")
    private String reference;

    @Expose
    @SerializedName("types")
    private List<String> types;

    @Expose
    @SerializedName("vicinity")
    private String vicinity;

    public GooglePlaceGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GooglePlaceGeometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GooglePlaceOpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(GooglePlaceOpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<GooglePlacePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<GooglePlacePhoto> photos) {
        this.photos = photos;
    }

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

    public List<GooglePlaceAltId> getAltIds() {
        return altIds;
    }

    public void setAltIds(List<GooglePlaceAltId> altIds) {
        this.altIds = altIds;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
