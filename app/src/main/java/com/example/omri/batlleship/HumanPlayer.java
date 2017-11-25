package com.example.omri.batlleship;

/**
 * Created by Mark on 24/11/2017.
 */

public class HumanPlayer extends Player {


    public HumanPlayer(String name){
        this.playerName=name;
        initShipList();
    }

    private void initShipList() {
        battleShips.add(new BattleShip("ship5",5,true,new Coordinate(-1,-1)));
        battleShips.add(new BattleShip("ship4",4,true,new Coordinate(-1,-1)));
        battleShips.add(new BattleShip("ship3",3,true,new Coordinate(-1,-1)));
        battleShips.add(new BattleShip("ship3_2",3,true,new Coordinate(-1,-1)));
        battleShips.add(new BattleShip("ship2",2,true,new Coordinate(-1,-1)));
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
