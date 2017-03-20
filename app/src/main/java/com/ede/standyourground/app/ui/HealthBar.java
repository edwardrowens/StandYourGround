package com.ede.standyourground.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.dagger.application.MyApp;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.api.HealthChangeListener;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class HealthBar extends ComponentElement {

    private static final Logger logger = new Logger(HealthBar.class);

    private final Paint paint = new Paint();
    private final Paint newPaint = new Paint();
    private final CornerPathEffect cornerPathEffect = new CornerPathEffect(15);

    private final UUID componentElementId;
    private final AtomicReference<RectF> rect;
    private final AtomicReference<RectF> border;
    private float width;
    private float height;
    private float healthPercentage;

    public HealthBar(final UUID componentElementId, Context context) {
        super(context);
        this.componentElementId = componentElementId;
        this.rect = new AtomicReference<>(new RectF());
        this.border = new AtomicReference<>(new RectF());
        this.healthPercentage = 1f;
        MyApp.getAppComponent().getUnitService().get().registerHealthChangeListener(new HealthChangeListener() {
            @Override
            public void onHealthChange(final Unit unit) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (unit.getId().equals(componentElementId)) {
                            setHealthPercentage(((float)unit.getHealth()) / unit.getMaxHealth());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDraw(Canvas canvas) {
        // background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setPathEffect(cornerPathEffect);
        canvas.drawRect(border.get(), paint);

        paint.set(newPaint);
        // fill
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect.get(), paint);
    }

    public void setPoint(PointF point) {
        rect.get().set(point.x, point.y, point.x + (width * healthPercentage), point.y + height);
        border.get().set(point.x - 5, point.y - 5, point.x + width + 5, point.y + height + 5);
        postInvalidate();
    }

    @Override
    public UUID getComponentElementId() {
        return componentElementId;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setHealthPercentage(float healthPercentage) {
        this.healthPercentage = healthPercentage;
        rect.get().set(rect.get().left, rect.get().top, rect.get().left + (width * healthPercentage), rect.get().bottom);
        postInvalidate();
    }

    public float getHealthPercentage() {
        return healthPercentage;
    }

    public RectF getHealthBar() {
        return rect.get();
    }

    public RectF getHealthBarBorder() {
        return border.get();
    }
}
