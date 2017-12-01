package com.example.omri.batlleship;

import java.io.Serializable;

/**
 * Created by Mark on 24/11/2017.
 */

public class HumanPlayer extends Player implements Serializable{

    private int amountOfLogins;

    public HumanPlayer(String name,int size,int numOfShips){
        super(name,size,numOfShips);
        amountOfLogins=0;
    }

    @Override
    public void placeBattleShips(Coordinate position) {

    }

//    @Override
//    public Coordinate attack() {
//        return null;
//    }

//    @Override
//    public boolean receiveFire(Coordinate target) {
//        if (myField.myShipsLocation[target.getX()][target.getY()]!=null) { // i got hit!
//            String shipHitName = myField.myShipsLocation[target.getX()][target.getY()];
//            myField.shipWasHit(shipHitName);
//            return true;
//        }
//        return false;
//    }

    @Override
    public String getName() {
        return null;
    }

    public int getAmountOfLogins() {
        return amountOfLogins;
    }

    public void setAmountOfLogins(int amountOfLogins) {
        this.amountOfLogins = amountOfLogins;
    }
}
