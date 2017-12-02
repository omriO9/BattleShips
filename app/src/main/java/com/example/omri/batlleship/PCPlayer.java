package com.example.omri.batlleship;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Omri on 12/1/2017.
 */

public class PCPlayer extends Player  {
    private static final String TAG = PCPlayer.class.getSimpleName();

   private List<Integer> listOfPossibleShots; // possible locations to randomize a shot that are left.
    private Coordinate lastShot;

    public PCPlayer(String name,int size,int numOfShips){
        super(name,size,numOfShips);
        //this.playerName="BlueGene";
        //this.battleFieldSize = size;
        //this.myField=new BattleField(battleFieldSize);
        initBattleShipsRandomly();
        listOfPossibleShots=new ArrayList<>();
        initListOfPossibleShots();
        lastShot=null;
        //myField.printMat();

    }

    private void initListOfPossibleShots() {
        int id = 0;
        for (int i=0;i<getMyField().myShipsLocation.length*getMyField().myShipsLocation.length;i++){
            listOfPossibleShots.add(i);
        }
    }

    public void initBattleShipsRandomly() {
        List <Coordinate> listOfPossibilities = new ArrayList<>();

        Map<String,BattleShip> map = myField.shipMap;
       for(Map.Entry<String,BattleShip> entry : map.entrySet()){
            Log.d(TAG, "initBattleShipsRandomly: inside "+ entry.getKey());
            Coordinate randomShipCord = generateRandomCoordinate(getMyField().myShipsLocation.length);
            //Coordinate randomShipCord = generateRandomCoordinate(battleFieldSize);
            listOfPossibilities = myField.showPossiblePositions(randomShipCord, entry.getKey());//check what if listOfPossibilities is empty
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


    public Coordinate generateShot() {
        // has an ArrayList of 0-99 possible positions to atk , remove after each attack.
        // randomize index 0-list.size();
        Random r = new Random();
        Log.d(TAG, "attack: listOfPossibleShots.size="+listOfPossibleShots.size());
        int randomIndex = r.nextInt(listOfPossibleShots.size());
        Log.d(TAG, "attack: listOfPossibleShots.size randomIndex="+randomIndex);
        Coordinate shotTarget = new Coordinate(listOfPossibleShots.get(randomIndex)/getMyField().myShipsLocation.length,listOfPossibleShots.get(randomIndex)%getMyField().myShipsLocation.length);
        listOfPossibleShots.remove(randomIndex);
        Log.d(TAG, "attack: PCAttacks="+shotTarget.toString());
        // shotTarget - PC requested atk target.
        setLastShot(shotTarget);
        return shotTarget;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void placeBattleShips(Coordinate position) {

    }
    public Coordinate getLastShot() {
        return lastShot;
    }

    public void setLastShot(Coordinate lastShot) {
        this.lastShot = lastShot;
    }

}
