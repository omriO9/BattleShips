package com.example.omri.battleShip;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Omri on 12/1/2017.
 */

public class PCPlayer extends Player {
    private static final String TAG = PCPlayer.class.getSimpleName();

   private List<Integer> listOfPossibleShots; // possible locations to randomize a shot that are left.
    private Coordinate lastShot;

    public PCPlayer(String name,int size,int numOfShips){
        super(name,size,numOfShips);
        initBattleShipsRandomly();
        listOfPossibleShots=new ArrayList<>();
        initListOfPossibleShots();
        lastShot=null;
    }

    private void initListOfPossibleShots() {
        for (int i=0;i<getMyField().myShipsLocation.length*getMyField().myShipsLocation.length;i++){
            listOfPossibleShots.add(i);
        }
    }

    public void initBattleShipsRandomly() {
        Map<String,BattleShip> map = myField.shipMap;
        List<Coordinate> listOfPossibilities;
        Coordinate randomShipCord;

       for(Map.Entry<String,BattleShip> entry : map.entrySet()){
            Log.d(TAG, "initBattleShipsRandomly: shipName =  "+ entry.getKey());
           do {
                randomShipCord = generateRandomCoordinate(getMyField().myShipsLocation.length);
                listOfPossibilities  = myField.showPossiblePositions(randomShipCord, entry.getKey());//check what if listOfPossibilities is empty
            }while(listOfPossibilities.size() ==0);
            int randomIndex = generateRandomIndex(listOfPossibilities.size());
            myField.placeShip(randomShipCord,listOfPossibilities.get(randomIndex),entry.getKey());
       }
    }

    private int generateRandomIndex(int length ) {
        Random r = new Random();
        return r.nextInt(length);
    }

    private Coordinate generateRandomCoordinate(int battleFieldSize ) {
        Log.d(TAG, "generateRandomCoordinate: "+battleFieldSize);
        int x ,y;
        Random r = new Random();
        do {
            x = r.nextInt(battleFieldSize-0);
            y = r.nextInt(battleFieldSize-0);
        }while(!myField.isShipInXY(x,y));
        return new Coordinate(x,y);
    }


    public void generateShot() {
        // has an ArrayList of 0-99 possible positions to atk , remove after each attack.
        // randomize index 0-list.size();
        Random r = new Random();
        Log.d(TAG, "attack: listOfPossibleShots.size="+listOfPossibleShots.size());
        int randomIndex = r.nextInt(listOfPossibleShots.size());
        Log.d(TAG, "attack: listOfPossibleShots.size randomIndex="+randomIndex);

        int chosenCell = listOfPossibleShots.get(randomIndex);
        int fieldSize = getMyField().myShipsLocation.length;
        Coordinate shotTarget = new Coordinate(chosenCell/fieldSize,chosenCell%fieldSize);
        listOfPossibleShots.remove(randomIndex);

        Log.d(TAG, "attack: PCAttacks="+shotTarget.toString());
        // shotTarget - PC requested atk target.
        setLastShot(shotTarget);
    }

    @Override
    public String getName() {
        return this.playerName;
    }

    public Coordinate getLastShot() {
        return lastShot;
    }

    public void setLastShot(Coordinate lastShot) {
        this.lastShot = lastShot;
    }

}
