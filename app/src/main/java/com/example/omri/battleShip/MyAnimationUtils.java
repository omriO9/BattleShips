package com.example.omri.battleShip;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;

import java.util.Random;

/**
 * Created by Mark on 12/01/2018.
 */

public class MyAnimationUtils {

    Context context;
    GridLayout gridLayout;

    public MyAnimationUtils(Context context, GridLayout gridLayout) {
        this.context = context;
        this.gridLayout = gridLayout;
    }

    private static final String TAG = "AnimationUtils";

    private DisplayMetrics _metrics;

    public void explodeGrid() {
        for (int column = 0; column < gridLayout.getColumnCount(); column++) {
            for (int row = 0; row < gridLayout.getColumnCount(); row++) {
                animateRandomlyFlyingOut(gridLayout.getChildAt(row + column * gridLayout.getColumnCount()), 17000);
            }
        }
    }

    public void animateRandomlyFlyingOut(View view, long duration) {
        Random random = new Random();
        int otherSide;

        Log.d(TAG, "animateRandomlyFlyingOut: Im here");

        otherSide = random.nextBoolean() ? 1 : -1;
        ObjectAnimator flyOutX = ObjectAnimator.ofFloat(view, "x", view.getX(), screenWidthPixels() * otherSide);
        flyOutX.setDuration(duration);
        flyOutX.setInterpolator(new DecelerateInterpolator());

        otherSide = random.nextBoolean() ? 1 : -1;
        ObjectAnimator flyOutY = ObjectAnimator.ofFloat(view, "y", view.getY(), screenWidthPixels() * otherSide);
        flyOutY.setDuration(duration);
        flyOutY.setInterpolator(new DecelerateInterpolator());

        otherSide = random.nextBoolean() ? 1 : -1;
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", otherSide == 1 ? 0f : 360f, otherSide == 1 ? 360f : 0f);
        rotate.setDuration(duration);
        rotate.setInterpolator(new DecelerateInterpolator());

        rotate.start();
        flyOutY.start();
        flyOutX.start();
    }

    public  int screenWidthPixels() {
        return getMetrics().widthPixels;
    }

    private DisplayMetrics getMetrics() {
        if (_metrics == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            _metrics = new DisplayMetrics();
            display.getMetrics(_metrics);
        }

        return _metrics;
    }
}
