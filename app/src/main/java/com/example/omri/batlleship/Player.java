package com.example.omri.batlleship;

import java.util.List;

/**
 * Created by Mark on 24/11/2017.
 */

public abstract class Player implements PlayerIF {
    public final int numOfShips = 5;
    String playerName; // me/computer
    boolean[][] myShipsLocation; // 0 - there is no ship here , 1 - there is.
    int[][] hitShots; // possibly change to enum with - {not shot,hit,miss} . - this mat of where i shot.
    List<BattleShip> battleShips;

//    public void addBattleShip(BattleShip s){
//        battleShips.add(s);
//    }
    public abstract void placeBattleShips(Coordinate position);
    public boolean hasBeenDefeated(){
        for (BattleShip b : battleShips){
            if (b.isSunk())
                return true;
        }
        return false;
    }
    public boolean receiveFire(Coordinate pos){


        return true;
    }
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean[][] getMyShipsLocation() {
        return myShipsLocation;
    }

    public void setMyShipsLocation(boolean[][] attemptedShots) {
        this.myShipsLocation = attemptedShots;
    }

    public int[][] getHitShots() {
        return hitShots;
    }

    public void setHitShots(int[][] hitShots) {
        this.hitShots = hitShots;
    }
}
