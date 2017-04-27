package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.networking.api.service.GooglePlacesService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 *
 */
public class NeutralCampListingComponent implements Component {

    private static final Logger logger = new Logger(NeutralCampListingComponent.class);

    private final Activity activity;
    private final LinearLayout container;
    private final TextView textView;
    private final int width;
    private final ProgressBar progressBar;
    private final ImageView imageView;

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    @Inject
    GooglePlacesService googlePlacesService;

    public NeutralCampListingComponent(Activity activity, Point point, String neutralCampName) {
        MyApp.getAppComponent().inject(this);
        this.activity = activity;
        this.container = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.neutral_camp_listing, null);
        this.container.setLayoutParams(createLayoutParams(point));
        float dpWidth = activity.getResources().getDimension(R.dimen.neutral_camp_listing_component_width);
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth, activity.getResources().getDisplayMetrics());

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        this.textView = (TextView) LayoutInflater.from(activity).inflate(R.layout.text_view_component, container).findViewById(R.id.textViewComponent);
        textView.setText(neutralCampName);

        this.progressBar = (ProgressBar) LayoutInflater.from(activity).inflate(R.layout.small_progress_bar, container).findViewById(R.id.smallProgressBar);

        imageView = new ImageView(activity);
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.addView(imageView);
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void drawComponentElements() {

    }

    public void clear() {
        container.setVisibility(View.INVISIBLE);
        if (onGlobalLayoutListener != null) {
            container.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    public void setTextAndPhoto(String text, String photoReference, final Point center, final double lineDistance) {
        if (onGlobalLayoutListener != null) {
            container.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        realign(center, lineDistance);
        textView.setText(text.trim());
        setPhoto(photoReference);
    }

    public void setPoint(Point point) {
        this.container.setLayoutParams(createLayoutParams(point));
    }

    private void setPhoto(String photoReference) {
        imageView.setImageDrawable(null);
        if (photoReference != null) {
            progressBar.setVisibility(View.VISIBLE);
            String googlePlacePhotoUrl = googlePlacesService.generatePhotoUrl(photoReference, width);
            Picasso.with(activity.getApplicationContext()).load(googlePlacePhotoUrl).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    logger.e("Could not load photo for %s", textView.getText());
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            logger.i("No photos found for %s", textView.getText());
            progressBar.setVisibility(View.GONE);
        }
    }

    public void initialize(int containerId) {
        ((RelativeLayout) activity.findViewById(containerId)).addView(container);
    }

    public int getWidth() {
        return width;
    }

    private ViewGroup.LayoutParams createLayoutParams(Point point) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = point.x;
        layoutParams.topMargin = point.y;

        return layoutParams;
    }

    private void realign(final Point center, final double lineDistance) {
        final Point point = new Point();
        point.x = center.x - (int) Math.round(lineDistance) - 5 - (getWidth() / 2) + (int) Math.round(lineDistance);

        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                point.y = center.y - (int) Math.round(lineDistance) - 5 - container.getMeasuredHeight();
                setPoint(point);
                container.setVisibility(View.VISIBLE);
            }
        };

        container.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        container.bringToFront();
    }
}
