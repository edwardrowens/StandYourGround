package com.ede.standyourground.app.activity.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.ede.standyourground.app.event.MarkerOptionsCallback;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.GraphicService;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
     * @param unit The unit that the marker options are created for
     * @param callback Called when the bitmap for the unit's texture is loaded. This may never occur
     *                 if the bitmap could not be loaded successfully
     * @return A default google maps marker with the unit's hostility-appropriate color
     */
    public MarkerOptions createMarkerOptions(final Activity activity, final Unit unit, final MarkerOptionsCallback callback) {
        final LatLng position = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                callback.onCreated(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).zIndex(1.0f));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                logger.e("Failed to load drawable to a bitmap");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Drawable drawable = activity.getResources().getDrawable(unit.getType().getDrawableId(), null);
        if (drawable != null) {
            logger.e("WTF PICASSO");
        }
        Picasso.with(activity).load(unit.getType().getDrawableId()).into(target);
        int color = unit.getHostility() == Hostility.FRIENDLY ? unit.getType().getFriendlyColor() : unit.getType().getEnemyColor();
        String hexColor = activity.getString(color);
        return new MarkerOptions().position(unit.getStartingPosition()).icon(BitmapDescriptorFactory.defaultMarker(graphicService.get().hexToHue(hexColor))).zIndex(1.0f);
    }
}
