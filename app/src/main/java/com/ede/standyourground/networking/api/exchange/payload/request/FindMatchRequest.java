package com.ede.standyourground.networking.api.exchange.payload.request;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class FindMatchRequest implements Parcelable {
    public FindMatchRequest(){}
    private double lat;
    private double lng;
    private int radius;
    private UUID id;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeInt(radius);
        dest.writeSerializable(id);
    }

    public static final Parcelable.Creator<FindMatchRequest> CREATOR
            = new Parcelable.Creator<FindMatchRequest>() {
        public FindMatchRequest createFromParcel(Parcel in) {
            return new FindMatchRequest(in);
        }

        public FindMatchRequest[] newArray(int size) {
            return new FindMatchRequest[size];
        }
    };

    private FindMatchRequest(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
        radius = in.readInt();
        id = (UUID) in.readSerializable();
    }
}
