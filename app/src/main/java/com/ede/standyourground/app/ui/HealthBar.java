package com.ede.standyourground.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.ede.standyourground.framework.Logger;

import java.util.UUID;

public class HealthBar extends ComponentElement {

    private static final Logger logger = new Logger(HealthBar.class);

    private final Paint paint = new Paint();
    private final Paint newPaint = new Paint();
    private final CornerPathEffect cornerPathEffect = new CornerPathEffect(15);

    private final UUID componentElementId;
    private final RectF rect;
    private final RectF border;
    private float width;
    private float height;
    private float healthPercentage;

    public HealthBar(UUID componentElementId, Context context) {
        super(context);
        this.componentElementId = componentElementId;
        this.rect = new RectF();
        this.border = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setPathEffect(cornerPathEffect);
        paint.setStrokeWidth(5);
        canvas.drawRect(border, paint);

        paint.set(newPaint);
        // fill
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
    }

    public void setPoint(PointF point) {
        rect.set(point.x, point.y, point.x + (width * healthPercentage), point.y + height);
        border.set(point.x, point.y, point.x + width, point.y + height);
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
    }

    public float getHealthPercentage() {
        return healthPercentage;
    }

    public RectF getHealthBar() {
        return rect;
    }

    public RectF getHealthBarBorder() {
        return border;
    }
}
