package com.example.omri.battleShip;

import java.io.Serializable;

/**
 * Created by Mark on 24/11/2017.
 */

class Coordinate implements Serializable {
    int x;
    int y;


    public Coordinate(int x,int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public String toString(){
        return "["+x+","+y+"]";
    }
}
