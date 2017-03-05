package com.ede.standyourground.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.UUID;

public class HealthBar extends ComponentElement {

    public static final int HEIGHT = 50;
    public static final int WIDTH = 100;

    private final Paint paint = new Paint();
    private final UUID componentElementId;
    private Point point;
    private Rect rect;

    public HealthBar(UUID componentElementId, Context context) {
        super(context);
        this.componentElementId = componentElementId;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.GREEN);
        canvas.drawRect(rect, paint);
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
        rect = new Rect(point.x, point.y, point.x + WIDTH, point.y + HEIGHT);
    }

    @Override
    public UUID getComponentElementId() {
        return componentElementId;
    }
}
