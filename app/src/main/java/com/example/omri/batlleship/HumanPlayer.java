package com.example.omri.batlleship;

import java.io.Serializable;

/**
 * Created by Mark on 24/11/2017.
 */

public class HumanPlayer extends Player implements Serializable{


    private int amountOfLogins;

    public HumanPlayer(String name,int size){
        this.playerName=name;
        amountOfLogins=0;
        this.myField=new BattleField(size);
    }



    @Override
    public void placeBattleShips(Coordinate position) {

    }

    @Override
    public void attack() {

    }

    @Override
    public boolean receiveFire() {
        return false;
    }

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
