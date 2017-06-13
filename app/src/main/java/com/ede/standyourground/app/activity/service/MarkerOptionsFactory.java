package com.ede.standyourground.app.activity.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.ede.standyourground.app.event.MarkerOptionsCallback;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.GraphicService;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class MarkerOptionsFactory {

    private static final Logger logger = new Logger(MarkerOptionsFactory.class);

    private final Lazy<GraphicService> graphicService;

    @Inject
    public MarkerOptionsFactory(Lazy<GraphicService> graphicService) {
        this.graphicService = graphicService;
    }

    /**
     * Creates the {@link MarkerOptions} for a specified {@link Unit}.
     *
     * @param activity The activity which will use the marker options
     * @param unit     The unit that the marker options are created for
     * @param callback Called when the bitmap for the unit's texture is loaded. This may never occur
     *                 if the bitmap could not be loaded successfully
     * @return A default google maps marker with the unit's hostility-appropriate color
     */
    public void createMarkerOptions(final Activity activity, final Unit unit, final MarkerOptionsCallback callback) {
        final int drawableId = unit.getHostility() == Hostility.FRIENDLY ? unit.getType().getFriendlyDrawableId() : unit.getType().getUnfriendlyDrawableId();
        final LatLng position = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
        int pixelSize = graphicService.get().dpToPixel((float)unit.getType().getSize(), activity);
        Drawable drawable = activity.getDrawable(drawableId);
        callback.onCreated(new MarkerOptions().position(position).icon(createMarkerIconFromDrawable(drawable, pixelSize)).zIndex(1.0f));
    }

    private BitmapDescriptor createMarkerIconFromDrawable(Drawable drawable, int pixelSize) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(pixelSize, pixelSize, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, pixelSize, pixelSize);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
