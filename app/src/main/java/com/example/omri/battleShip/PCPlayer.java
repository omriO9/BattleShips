package com.example.omri.battleShip;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        int amountOfShots = getMyField().getMyShipsLocation().length*getMyField().getMyShipsLocation().length;
        for (int i=0;i<amountOfShots;i++){
            listOfPossibleShots.add(i);
        }
    }

    public void initBattleShipsRandomly() {
        Map<String,BattleShip> map = myField.getShipMap();
        List<Coordinate> listOfPossibilities;
        Log.d(TAG, "initBattleShipsRandomly: starting initBattleShipsRandomly ");
       for(Map.Entry<String,BattleShip> entry : map.entrySet()){
           Coordinate randomShipCord;
           do {
               randomShipCord = generateRandomCoordinate(getMyField().getMyShipsLocation().length);
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

    public Coordinate generateRandomCoordinate(int battleFieldSize ) {
        Log.d(TAG, "generateRandomCoordinate: "+battleFieldSize);
        int x ,y;
        Random r = new Random();
        do {
            x = r.nextInt(battleFieldSize);
            y = r.nextInt(battleFieldSize);
        }while(!myField.isShipInXY(x,y));
        return new Coordinate(x,y);
    }

    public void generateShot() {
        // has an ArrayList of 0-99 possible positions to atk , remove after each attack.
        // randomize index 0-list.size();
        Random r = new Random();
        int randomIndex = r.nextInt(listOfPossibleShots.size());
        Log.d(TAG, "generateShot: listOfPossibleShots.size randomIndex="+randomIndex);
        int chosenCell = listOfPossibleShots.get(randomIndex);
        int fieldSize = getMyField().getMyShipsLocation().length;
        Coordinate shotTarget = new Coordinate(chosenCell/fieldSize,chosenCell%fieldSize);
        listOfPossibleShots.remove(randomIndex);
        Log.d(TAG, "attack: PCAttacks="+shotTarget.toString());
        // shotTarget - PC requested atk target.
        setLastShot(shotTarget);
    }



    public Coordinate getLastShot() {
        return lastShot;
    }

    public void setLastShot(Coordinate lastShot) {
        this.lastShot = lastShot;
    }

}
