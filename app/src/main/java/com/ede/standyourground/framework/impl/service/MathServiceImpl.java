package com.ede.standyourground.framework.impl.service;

import android.graphics.Point;

import com.ede.standyourground.framework.api.service.MathService;

import javax.inject.Inject;


public class MathServiceImpl implements MathService {

    @Inject
    MathServiceImpl() {

    }

    @Override
    public double calculateLinearDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }
}
