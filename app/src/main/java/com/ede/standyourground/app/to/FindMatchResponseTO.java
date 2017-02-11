package com.ede.standyourground.app.to;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eddie on 2/9/2017.
 */

public class FindMatchResponseTO implements Parcelable {
    private double lat;
    private double lng;
    private String ip;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(ip);
    }

    public static final Parcelable.Creator<FindMatchResponseTO> CREATOR
            = new Parcelable.Creator<FindMatchResponseTO>() {
        public FindMatchResponseTO createFromParcel(Parcel in) {
            return new FindMatchResponseTO(in);
        }

        public FindMatchResponseTO[] newArray(int size) {
            return new FindMatchResponseTO[size];
        }
    };

    private FindMatchResponseTO(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
        ip = in.readString();
    }
}
