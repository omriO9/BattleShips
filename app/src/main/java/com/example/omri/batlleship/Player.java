package com.example.omri.batlleship;

import java.util.List;

/**
 * Created by Mark on 24/11/2017.
 */

public abstract class Player implements PlayerIF {
    public final int numOfShips = 5;
    String playerName; // me/computer
    boolean[][] attemptedShots;
    boolean[][] hitShots;
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

    public boolean[][] getAttemptedShots() {
        return attemptedShots;
    }

    public void setAttemptedShots(boolean[][] attemptedShots) {
        this.attemptedShots = attemptedShots;
    }

    public boolean[][] getHitShots() {
        return hitShots;
    }

    public void setHitShots(boolean[][] hitShots) {
        this.hitShots = hitShots;
    }
}
