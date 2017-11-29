package com.example.omri.batlleship;

import android.content.Context;

/**
 * Created by Mark on 22/11/2017.
 */

public class GridButton extends android.support.v7.widget.AppCompatButton {
    private int positionX;
    private int positionY;



    private boolean isAvailable; // false - can click on it to finish placing a ship. true - can click on it to begin placing a ship.

    public GridButton(Context context) {
        super(context);
        isAvailable=true;
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
    public boolean isAvailable() {
        return isAvailable;
    }

    public void toggleAvailable() {
        if (isAvailable)
            isAvailable=false;
        else
            isAvailable=true;
    }

    public void removePossiblePositionMarks(){
        this.setBackgroundResource(R.drawable.cell_border);
    }
}
