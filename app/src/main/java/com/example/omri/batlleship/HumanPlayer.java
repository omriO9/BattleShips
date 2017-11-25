package com.example.omri.batlleship;

/**
 * Created by Mark on 24/11/2017.
 */

public class HumanPlayer extends Player {


    public HumanPlayer(String name){
        this.playerName=name;
        for (int i=0;i<numOfShips;i++){
            battleShips.add(new BattleShip(5,true,new Coordinate(-1,-1)));
        }
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
}
