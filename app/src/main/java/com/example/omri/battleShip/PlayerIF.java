package com.example.omri.battleShip;

/**
 * Created by Mark on 24/11/2017.
 */

interface PlayerIF {
   // public Coordinate attack();
    //public boolean hasBeenDefeated();
    public BattleField.shotState receiveFire(Coordinate target);
    public String getName();
}
