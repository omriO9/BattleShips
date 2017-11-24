package com.example.omri.batlleship;

import android.content.Context;

/**
 * Created by Mark on 22/11/2017.
 */

public class GridButton extends android.support.v7.widget.AppCompatButton {
    private int positionX;
    private int positionY;

    public GridButton(Context context) {
        super(context);
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
}