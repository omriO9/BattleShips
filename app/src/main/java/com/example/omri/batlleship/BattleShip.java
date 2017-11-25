package com.example.omri.batlleship;

/**
 * Created by Mark on 24/11/2017.
 */

public class BattleShip {
    int length;
    boolean isSunk;
    boolean orientation; // true = vertical , false = horizontal
    Coordinate position; // the back of the ship - starting point.
    int numberOfHits;

    public BattleShip(int size,boolean orientation,Coordinate pos){
        length=size;
        isSunk=false;
        this.orientation=orientation;
        position=pos;
        numberOfHits=0;
    }

    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public boolean isSunk() {
        return isSunk;
    }
    public void setSunk(boolean sunk) {
        isSunk = sunk;
    }
    public boolean isOrientation() {
        return orientation;
    }
    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }
    public Coordinate getPosition() {
        return position;
    }
    public void setPosition(Coordinate position) {
        this.position = position;
    }
    public int getNumberOfHits() {
        return numberOfHits;
    }
    public void setNumberOfHits(int numberOfHits) {
        this.numberOfHits = numberOfHits;
    }
}
