package com.example.omri.battleShip;

import java.io.Serializable;

/**
 * Created by Mark on 24/11/2017.
 */


public class BattleShip implements Serializable {


    private String name; // for identification towards XML id.
    private int length;
    public boolean isSunk;
    private int numberOfHits;

    public BattleShip(String name,int size){
        this.name=name;
        length=size;
        isSunk=false;
        numberOfHits=0;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getLength() {
        return length;
    }
    public boolean isSunk() {
        return numberOfHits==length?true:false;
    }
    public void setSunk(){
        this.isSunk=true;
    }

    public int getNumberOfHits() {
        return numberOfHits;
    }
    public void setNumberOfHits(int numberOfHits) {
        this.numberOfHits = numberOfHits;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
