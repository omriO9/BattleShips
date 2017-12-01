package com.example.omri.batlleship;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Omri on 12/1/2017.
 */

public class PCPlayer extends Player  {
    private static final String TAG = PCPlayer.class.getSimpleName();

    private int battleFieldSize;

    public PCPlayer(int size){
        this.playerName="BlueGene";
        this.battleFieldSize = size;
        this.myField=new BattleField(battleFieldSize);
        initBattleShipsRandomly();
        myField.printMat();

    }

    public void initBattleShipsRandomly() {
        List <Coordinate> listOfPossibilities = new ArrayList<>();

        Map<String,BattleShip> map = myField.shipMap;
       for(Map.Entry<String,BattleShip> entry : map.entrySet()){
            Log.d(TAG, "initBattleShipsRandomly: inside "+ entry.getKey());
            Coordinate randomShipCord = generateRandomCoordinate(battleFieldSize);
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
        int x ,y;
        Random r = new Random();

        do {
            x = r.nextInt(battleFieldSize-0);
            y = r.nextInt(battleFieldSize-0);
        }while(!myField.isShipInXY(x,y));

        return new Coordinate(x,y);
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

    @Override
    public void placeBattleShips(Coordinate position) {

    }
}
