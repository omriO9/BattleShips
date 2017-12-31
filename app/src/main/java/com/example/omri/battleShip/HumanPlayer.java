package com.example.omri.battleShip;

import java.io.Serializable;

public class HumanPlayer extends Player implements Serializable{

    //private int amountOfLogins; - for version 2.0
    private int numOfAttempts;

    public HumanPlayer(String name,int size,int numOfShips){
        super(name,size,numOfShips);
        numOfAttempts=0;
    }

    public int getNumOfAttempts() {
        return numOfAttempts;
    }

    public void increaseAttempts() {
        this.numOfAttempts++;
    }
}
