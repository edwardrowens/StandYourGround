package com.ede.standyourground.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.ede.standyourground.framework.Logger;

import java.util.UUID;

public class HealthBar extends ComponentElement {

    private static final Logger logger = new Logger(HealthBar.class);

    private final Paint paint = new Paint();
    private final CornerPathEffect cornerPathEffect = new CornerPathEffect(15);

    private final UUID componentElementId;
    private final Rect rect;
    private final Rect border;
    private int width;
    private int height;
    private float healthPercentage;

    public HealthBar(UUID componentElementId, Context context) {
        super(context);
        this.componentElementId = componentElementId;
        this.rect = new Rect();
        this.border = new Rect();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(border, paint);

        // fill
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);

        // border
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setPathEffect(cornerPathEffect);
        paint.setStrokeWidth(5);
        canvas.drawRect(border, paint);
    }

    public void setPoint(Point point) {
        rect.set(point.x, point.y, point.x + (int)(width * healthPercentage), point.y + height);
        border.set(point.x, point.y, point.x + width, point.y + height);
    }

    @Override
    public UUID getComponentElementId() {
        return componentElementId;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHealthPercentage(float healthPercentage) {
        this.healthPercentage = healthPercentage;
    }
}
