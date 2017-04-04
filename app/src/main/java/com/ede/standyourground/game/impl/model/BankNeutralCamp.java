package com.ede.standyourground.game.impl.model;

import com.ede.standyourground.game.api.event.listener.BankNeutralCampIncomeListener;
import com.ede.standyourground.game.api.event.listener.IncomeAccruedListener;
import com.ede.standyourground.game.api.event.observer.BankNeutralCampIncomeObserver;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.NeutralCamp;
import com.ede.standyourground.game.api.model.Units;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public class BankNeutralCamp extends NeutralCamp implements BankNeutralCampIncomeObserver, IncomeAccruedListener {
    private static final int HEALTH = 100;
    private static final int INCOME_PROVIDED = 5;

    private BankNeutralCampIncomeListener bankNeutralCampIncomeListener;

    public BankNeutralCamp(LatLng startingPosition, String name, String photoReference, Hostility hostility) {
        super(startingPosition, Units.BANK_NEUTRAL_CAMP, name, photoReference, hostility);
    }

    @Override
    public int getMaxHealth() {
        return HEALTH;
    }

    @Override
    public double getVisionRadius() {
        return 0;
    }

    public int getProvidedIncome() {
        return INCOME_PROVIDED;
    }

    @Override
    public void registerBankNeutralCampIncomeListener(BankNeutralCampIncomeListener bankNeutralCampIncomeListener) {
        this.bankNeutralCampIncomeListener = bankNeutralCampIncomeListener;
    }

    @Override
    public void onIncomeAccrued() {
        bankNeutralCampIncomeListener.onBankNeutralCampIncome(this);
    }
}
