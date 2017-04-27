package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.framework.api.service.DirectionsService;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.ArtificialOpponentService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.impl.model.ArtificialOpponent;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.maps.android.PolyUtil;

import javax.inject.Inject;

import dagger.Lazy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */

public class ArtificialOpponentServiceImpl implements ArtificialOpponentService {

    private final Lazy<DirectionsService> directionsService;
    private final Lazy<UnitService> unitService;

    @Inject
    public ArtificialOpponentServiceImpl(Lazy<UnitService> unitService,
                                         Lazy<DirectionsService> directionsService) {
        this.unitService = unitService;
        this.directionsService = directionsService;
    }

    @Override
    public void runArtificialOpponent(ArtificialOpponent artificialOpponent) {
        switch(artificialOpponent.getArtificialOpponentDifficulty()) {
            case EASY:
                runEasyArtificialOpponent(artificialOpponent);
                break;
            case MEDIUM:
                runMediumArtificialOpponent(artificialOpponent);
                break;
            case HARD:
                runHardArtificialOpponent(artificialOpponent);
                break;
        }
    }

    private void runEasyArtificialOpponent(final ArtificialOpponent artificialOpponent) {
        artificialOpponent.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                directionsService.get().getRoutes(artificialOpponent.getBasePosition(), artificialOpponent.getOpponentPosition(), null, new Callback<Routes>() {
                    @Override
                    public void onResponse(Call<Routes> call, Response<Routes> response) {
                        unitService.get().createEnemyUnit(PolyUtil.decode(response.body().getRoutes().get(0).getOverviewPolyline().getPoints()), artificialOpponent.getBasePosition(), UnitType.FOOT_SOLDIER);
                    }

                    @Override
                    public void onFailure(Call<Routes> call, Throwable t) {

                    }
                });
                artificialOpponent.getHandler().postDelayed(this, 30000);
            }
        }, 30000);
    }

    private void runMediumArtificialOpponent(final ArtificialOpponent artificialOpponent) {
        artificialOpponent.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                directionsService.get().getRoutes(artificialOpponent.getBasePosition(), artificialOpponent.getOpponentPosition(), null, new Callback<Routes>() {
                    @Override
                    public void onResponse(Call<Routes> call, Response<Routes> response) {
                        unitService.get().createEnemyUnit(PolyUtil.decode(response.body().getRoutes().get(0).getOverviewPolyline().getPoints()), artificialOpponent.getBasePosition(), UnitType.FOOT_SOLDIER);
                    }

                    @Override
                    public void onFailure(Call<Routes> call, Throwable t) {

                    }
                });
                artificialOpponent.getHandler().postDelayed(this, 30000);
            }
        }, 30000);
    }

    private void runHardArtificialOpponent(final ArtificialOpponent artificialOpponent) {
        artificialOpponent.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                directionsService.get().getRoutes(artificialOpponent.getBasePosition(), artificialOpponent.getOpponentPosition(), null, new Callback<Routes>() {
                    @Override
                    public void onResponse(Call<Routes> call, Response<Routes> response) {
                        unitService.get().createEnemyUnit(PolyUtil.decode(response.body().getRoutes().get(0).getOverviewPolyline().getPoints()), artificialOpponent.getBasePosition(), UnitType.FOOT_SOLDIER);
                    }

                    @Override
                    public void onFailure(Call<Routes> call, Throwable t) {

                    }
                });
                artificialOpponent.getHandler().postDelayed(this, 30000);
            }
        }, 30000);
    }
}
