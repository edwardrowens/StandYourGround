package com.ede.standyourground.app.event;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 *
 */

public class MarkerOptionsLoadedTarget implements Target {

    public void onLoaded(Bitmap bitmap, Picasso.LoadedFrom from, MarkerOptionsCallback markerOptionsCallback, Unit unit) {
        final LatLng position = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
        markerOptionsCallback.onCreated(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).zIndex(1.0f));
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
