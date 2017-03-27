package com.ede.standyourground.networking.api.exchange.payload.response;

import com.ede.standyourground.networking.api.model.GooglePlaceResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */

public class GooglePlacesResponsePayload {

    @Expose
    @SerializedName("results")
    private List<GooglePlaceResult> results;

    @Expose
    @SerializedName("status")
    private String status;

    public List<GooglePlaceResult> getResults() {
        return results;
    }

    public void setResults(List<GooglePlaceResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
