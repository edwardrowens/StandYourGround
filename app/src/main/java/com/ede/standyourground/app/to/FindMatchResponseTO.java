package com.ede.standyourground.app.to;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eddie on 2/9/2017.
 */

public class FindMatchResponseTO implements Parcelable {

    private Double lat;
    private Double lng;
    private String gameSessionId;

    public FindMatchResponseTO() {}

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(gameSessionId);
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
        gameSessionId = in.readString();
    }
}
