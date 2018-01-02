package com.example.omri.battleShip;

import android.util.Log;

import java.io.Serializable;

public abstract class Player implements Serializable {

    private static final String TAG = Player.class.getSimpleName();
    public int numOfShips;
    protected String playerName; // me/computer
    protected BattleField myField;
    private int numOfSunkShips;
    private boolean hasBeenDefeated;

    public Player(String name,int sizeOfMap,int numOfShips){
        this.playerName=name;
        this.numOfShips=numOfShips;
        numOfSunkShips=0;
        this.myField=new BattleField(sizeOfMap,numOfShips);
        hasBeenDefeated=false;
    }

    public boolean hasBeenDefeated(){
        return hasBeenDefeated;
    }

    public String getPlayerName() {
        return playerName;
    }

    public BattleField getMyField() {
        return myField;
    }

    public BattleField.shotState receiveFire(Coordinate target) {
        if (myField.getMyShipsLocation()[target.getX()][target.getY()]!=null) { // i got hit!
            Log.d(TAG, "receiveFire: fireHit in "+target);
            String shipHitName = myField.getMyShipsLocation()[target.getX()][target.getY()].getShipName();
            if(myField.shipWasHit(shipHitName)==BattleField.shotState.SUNK) { // true = hit and sunk.
                numOfSunkShips++;
                Log.d(TAG, "receiveFire: ship sunk ("+numOfSunkShips+")");
                if (numOfSunkShips==numOfShips) {
                    hasBeenDefeated = true;
                    Log.d(TAG, "receiveFire: player "+this.getPlayerName()+" has been defeated!!!");
                }
                return BattleField.shotState.SUNK;
            }
            return BattleField.shotState.HIT;
        }
        return BattleField.shotState.MISS;
    }
}
