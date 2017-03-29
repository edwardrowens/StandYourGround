package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.component.UnitGroupBlock;
import com.ede.standyourground.app.ui.api.event.FinalDecrementListener;
import com.ede.standyourground.app.ui.api.event.FinalDecrementObserver;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */

public class UnitGroupBlockCount extends UnitGroupBlock implements FinalDecrementObserver {

    private static Logger logger = new Logger(UnitGroupBlockCount.class);

    private final TextView countContainer;
    private final Activity activity;
    private final AtomicInteger count;
    private FinalDecrementListener finalDecrementListener;

    public UnitGroupBlockCount(UUID componentElementId, final List<UUID> unitIds, Activity activity, Units units, final int count) {
        super(componentElementId, unitIds, activity, units);
        this.count = new AtomicInteger(count);
        this.activity = activity;
        this.countContainer = (TextView) LayoutInflater.from(activity).inflate(R.layout.text_view_component, null);

        countContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        countContainer.setText(activity.getResources().getString(R.string.unitGroupCountText, count));
        container.addView(countContainer);
        unitService.get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal, final Unit killer) {
                if (getUnitIds().remove(mortal.getId())) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (getUnitIds().size() == 1) {
                                finalDecrementListener.onFinalDecrement(unitService.get().getUnit(getUnitIds().iterator().next()));
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void setVisible(int visibility) {
        countContainer.setVisibility(visibility);
    }

    @Override
    protected void clearViews() {
        countContainer.setVisibility(View.GONE);
    }

    @Override
    public View getContainer() {
        return container;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void drawComponentElements() {
        iconContainer.postInvalidate();
        countContainer.postInvalidate();
    }

    @Override
    public void registerFinalDecrementListener(FinalDecrementListener finalDecrementListener) {
        this.finalDecrementListener = finalDecrementListener;
    }

    private int decrementCount() {
        count.decrementAndGet();
        countContainer.setText(activity.getResources().getString(R.string.unitGroupCountText, count.get()));
        return count.get();
    }
}
