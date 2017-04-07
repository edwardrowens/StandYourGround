package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.game.api.model.Units;

/**
 *
 */

public class UnitChoicesComponent implements Component {

    private final Activity activity;
    private final HorizontalScrollView unitChoices;

    public UnitChoicesComponent(Activity activity, ViewGroup parent) {
        this.activity = activity;
        ViewGroup unitChoicesParent = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_choices, parent);
        unitChoices = (HorizontalScrollView) unitChoicesParent.findViewById(R.id.unitChoicesScrollView);
        unitChoices.setZ(1f);
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void drawComponentElements() {

    }

    public void setVisibility(Units units, int visibility) {
        int id;
        switch (units) {
            case FOOT_SOLDIER:
                id = R.id.foot_soldier_choice_container;
                break;
            case MARAUDER:
                id = R.id.marauder_choice_container;
                break;
            case MEDIC:
                id = R.id.medic_choice_container;
                break;
            default:
                throw new IllegalArgumentException(String.format("%s does not have a supported unit choice type", units.toString()));
        }
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(id);
        viewGroup.setVisibility(visibility);
    }

    public void realign(Point center, double lineDistance) {
        int pixelWidth = (int)activity.getResources().getDimension(R.dimen.unit_choice_scroll_width);
        int x = center.x - pixelWidth / 2;
        int y = (int) (center.y - lineDistance) - unitChoices.getMeasuredHeight() - 5;
        unitChoices.setX(x);
        unitChoices.setY(y);
        unitChoices.setVisibility(View.VISIBLE);
        unitChoices.invalidate();
    }

    public void clear() {
        unitChoices.setVisibility(View.INVISIBLE);
    }
}
