package com.ede.standyourground.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public class HealthBar extends View {
    private final Paint paint = new Paint();
    private Point point;

    public HealthBar(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.GREEN);
        Rect rect = new Rect(point.x, point.y, point.x + 10, point.y + 10);
        canvas.drawRect(rect, paint);
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
