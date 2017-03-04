package com.ede.standyourground.game.framework.render.impl;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.HealthBar;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.render.api.RenderService;
import com.ede.standyourground.game.model.Unit;
import com.google.android.gms.maps.Projection;

import javax.inject.Inject;

import dagger.Lazy;

public class RenderServiceImpl implements RenderService {

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<Context> context;
    private final Projection projection;

    @Inject
    RenderServiceImpl(Lazy<GoogleMapProvider> googleMapProvider,
                      Lazy<Context> context) {
        this.googleMapProvider = googleMapProvider;
        this.projection = googleMapProvider.get().getGoogleMap().getProjection();
        this.context = context;
    }

    @Override
    public void renderHealthBar(Unit unit) {
        Point point = projection.toScreenLocation(unit.getCurrentPosition());
        HealthBar healthBar = new HealthBar(context.get());
        healthBar.setPoint(point);
        Fragment fragment = (Fragment) context.get().getResources().getLayout(R.id.map);
    }
}
