package com.example.omri.batlleship;

import java.io.Serializable;

/**
 * Created by Mark on 24/11/2017.
 */

public abstract class Player implements PlayerIF , Serializable {
    public final int numOfShips = 5;
    protected String playerName; // me/computer



    protected BattleField myField; // 0 - there is no ship here , 1 - there is.
    protected int[][] hitShots; // possibly change to enum with - {not shot,hit,miss} . - this mat of where i shot.
    //protected List<BattleShip> battleShips;

//    public void addBattleShip(BattleShip s){
//        battleShips.add(s);
//    }
    public abstract void placeBattleShips(Coordinate position);
//    public boolean hasBeenDefeated(){
//        for (BattleShip b : myField.shipMap){
//            if (!b.isSunk())
//                return false;
//        }
//        return true;
//    }
    public boolean receiveFire(Coordinate pos){


        return true;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public BattleField getBattleField() {
        return myField;
    }
    public void setBattleField(BattleField bf) {
        this.myField = bf;
    }
    public int[][] getHitShots() {
        return hitShots;
    }
    public void setHitShots(int[][] hitShots) {
        this.hitShots = hitShots;
    }

//    public List<BattleShip> getBattleShips() {
//        return battleShips;
//    }
//    public void setBattleShips(List<BattleShip> list) {
//        this.battleShips=list;
//    }

    public BattleField getMyField() {
        return myField;
    }
}
