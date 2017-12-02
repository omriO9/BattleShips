package com.example.omri.batlleship;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Mark on 24/11/2017.
 */

public abstract class Player implements PlayerIF , Serializable {

    private static final String TAG = Player.class.getSimpleName();
    public int numOfShips = 5;
    protected String playerName; // me/computer
    //protected int battleFieldSize;
    protected BattleField myField; // 0 - there is no ship here , 1 - there is.
    protected int[][] hitShots; // possibly change to enum with - {not shot,hit,miss} . - this mat of where i shot.
    //protected List<BattleShip> battleShips;
    private int numOfSunkShips;
    private boolean hasBeenDefeated;

    public Player(String name,int sizeOfMap,int numOfShips){
        this.playerName=name;
        this.numOfShips=numOfShips;
        numOfSunkShips=0;
        //battleFieldSize=sizeOfMap;
        this.myField=new BattleField(sizeOfMap,numOfShips);
        hasBeenDefeated=false;
    }
//    public void addBattleShip(BattleShip s){
//        battleShips.add(s);
//    }
    public abstract void placeBattleShips(Coordinate position);

    public boolean hasBeenDefeated(){
        return hasBeenDefeated;
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

    @Override
    public boolean receiveFire(Coordinate target) {
        if (myField.myShipsLocation[target.getX()][target.getY()]!=null) { // i got hit!
            String shipHitName = myField.myShipsLocation[target.getX()][target.getY()];
            if(myField.shipWasHit(shipHitName)) { // true = hit and sunk.
                Log.d(TAG, "receiveFire: inside if,numOfSunkShips="+numOfSunkShips+", numOfShips="+numOfShips);
                numOfSunkShips++;
                Log.d(TAG, "receiveFire: ship sunk ("+numOfShips+")");
                if (numOfSunkShips==numOfShips) {
                    hasBeenDefeated = true;
                    Log.d(TAG, "receiveFire: player "+this.getPlayerName()+" has been defeated!!!");
                }
            }
            return true;
        }
        return false;
    }
}
